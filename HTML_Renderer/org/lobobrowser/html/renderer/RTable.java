/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project. Copyright (C) 2014 - 2015 Lobo Evolution

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net; ivan.difrancesco@yahoo.it
 */
/*
 * Created on Nov 19, 2005
 */
package org.lobobrowser.html.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.control.RUIControl;
import org.lobobrowser.html.dombl.ModelNode;
import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.lobobrowser.html.renderstate.RenderState;
import org.lobobrowser.html.style.RenderThreadState;
import org.lobobrowser.util.Objects;

class RTable extends BaseElementRenderable {
	private static final int MAX_CACHE_SIZE = 10;
	private final Map<LayoutKey, LayoutValue> cachedLayout = new HashMap<LayoutKey, LayoutValue>(5);
	private final TableMatrix tableMatrix;
	private SortedSet<PositionedRenderable> positionedRenderables;
	private int otherOrdinal;
	private LayoutKey lastLayoutKey = null;
	private LayoutValue lastLayoutValue = null;

	public RTable(HTMLElementImpl modelNode, UserAgentContext pcontext,
			HtmlRendererContext rcontext, FrameContext frameContext,
			RenderableContainer container) {
		super(container, modelNode, pcontext);
		this.tableMatrix = new TableMatrix(modelNode, pcontext, rcontext,
				frameContext, this, this);
	}

	public int getVAlign() {
		// Not used
		return VALIGN_BASELINE;
	}

	protected int getWidthElement() {
		return this.width;
	}

	protected int getHeightElement() {
		return this.tableMatrix.getTableHeightWithoutCaption();
	}

	protected int getStartY() {
		return this.tableMatrix.getStartYWithoutCaption();
	}

	public void paint(Graphics g) {
		RenderState rs = this.modelNode.getRenderState();
		if (rs != null && rs.getVisibility() != RenderState.VISIBILITY_VISIBLE) {
			// Just don't paint it.
			return;
		}
		try {
			this.prePaint(g);
			Dimension size = this.getSize();
			// TODO: No scrollbars
			TableMatrix tm = this.tableMatrix;
			tm.paint(g, size);
			Collection<PositionedRenderable> prs = this.positionedRenderables;
			if (prs != null) {
				Iterator<PositionedRenderable> i = prs.iterator();
				while (i.hasNext()) {
					PositionedRenderable pr = (PositionedRenderable) i.next();
					BoundableRenderable r = pr.renderable;
					r.paintTranslated(g);
				}
			}
		} finally {
			// Must always call super implementation
			super.paint(g);
		}
	}

	public void doLayout(int availWidth, int availHeight, boolean sizeOnly) {
		Map<LayoutKey, LayoutValue> cachedLayout = this.cachedLayout;
		RenderState rs = this.modelNode.getRenderState();
		int whitespace = rs == null ? RenderState.WS_NORMAL : rs
				.getWhiteSpace();
		Font font = rs == null ? null : rs.getFont();
		// Having whiteSpace == NOWRAP and having a NOWRAP override
		// are not exactly the same thing.
		boolean overrideNoWrap = RenderThreadState.getState().overrideNoWrap;
		LayoutKey layoutKey = new LayoutKey(availWidth, availHeight,
				whitespace, font, overrideNoWrap);
		LayoutValue layoutValue;
		if (sizeOnly) {
			layoutValue = (LayoutValue) cachedLayout.get(layoutKey);
		} else {
			if (Objects.equals(layoutKey, this.lastLayoutKey)) {
				layoutValue = this.lastLayoutValue;
			} else {
				layoutValue = null;
			}
		}
		if (layoutValue == null) {
			Collection<PositionedRenderable> prs = this.positionedRenderables;
			if (prs != null) {
				prs.clear();
			}
			this.otherOrdinal = 0;
			this.clearGUIComponents();
			this.clearDelayedPairs();
			this.applyStyle(availWidth, availHeight);
			TableMatrix tm = this.tableMatrix;
			Insets insets = this.getInsets(false, false);
			tm.reset(insets, availWidth, availHeight);
			// TODO: No scrollbars
			tm.build(availWidth, availHeight, sizeOnly);
			tm.doLayout(insets);

			// Import applicable delayed pairs.
			// Only needs to be done if layout was
			// forced. Otherwise, they should've
			// been imported already.
			Collection<?> pairs = this.delayedPairs;
			if (pairs != null) {
				Iterator<?> i = pairs.iterator();
				while (i.hasNext()) {
					DelayedPair pair = (DelayedPair) i.next();
					if (pair.targetParent == this) {
						this.importDelayedPair(pair);
					}
				}
			}
			layoutValue = new LayoutValue(tm.getTableWidth(),
					tm.getTableHeight());
			if (sizeOnly) {
				if (cachedLayout.size() > MAX_CACHE_SIZE) {
					// Unlikely, but we should ensure it's bounded.
					cachedLayout.clear();
				}
				cachedLayout.put(layoutKey, layoutValue);
				this.lastLayoutKey = null;
				this.lastLayoutValue = null;
			} else {
				this.lastLayoutKey = layoutKey;
				this.lastLayoutValue = layoutValue;
			}
		}
		this.width = layoutValue.width;
		this.height = layoutValue.height;
		this.sendGUIComponentsToParent();
		this.sendDelayedPairsToParent();
	}

	public void invalidateLayoutLocal() {
		super.invalidateLayoutLocal();
		this.cachedLayout.clear();
		this.lastLayoutKey = null;
		this.lastLayoutValue = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.render.BoundableRenderable#getRenderablePoint(int,
	 * int)
	 */
	public RenderableSpot getLowestRenderableSpot(int x, int y) {
		Collection<PositionedRenderable> prs = this.positionedRenderables;
		if (prs != null) {
			Iterator<PositionedRenderable> i = prs.iterator();
			while (i.hasNext()) {
				PositionedRenderable pr = (PositionedRenderable) i.next();
				BoundableRenderable r = pr.renderable;
				int childX = x - r.getX();
				int childY = y - r.getY();
				RenderableSpot rs = r.getLowestRenderableSpot(childX, childY);
				if (rs != null) {
					return rs;
				}
			}
		}
		RenderableSpot rs = this.tableMatrix.getLowestRenderableSpot(x, y);
		if (rs != null) {
			return rs;
		}
		return new RenderableSpot(this, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lobobrowser.html.render.BoundableRenderable#onMouseClick(java.awt.event
	 * .MouseEvent, int, int)
	 */
	public boolean onMouseClick(MouseEvent event, int x, int y) {
		Collection<PositionedRenderable> prs = this.positionedRenderables;
		if (prs != null) {
			Iterator<PositionedRenderable> i = prs.iterator();
			while (i.hasNext()) {
				PositionedRenderable pr = (PositionedRenderable) i.next();
				BoundableRenderable r = pr.renderable;
				Rectangle bounds = r.getBounds();
				if (bounds.contains(x, y)) {
					int childX = x - r.getX();
					int childY = y - r.getY();
					if (!r.onMouseClick(event, childX, childY)) {
						return false;
					}
				}
			}
		}
		return this.tableMatrix.onMouseClick(event, x, y);
	}

	public boolean onDoubleClick(MouseEvent event, int x, int y) {
		Collection<PositionedRenderable> prs = this.positionedRenderables;
		if (prs != null) {
			Iterator<PositionedRenderable> i = prs.iterator();
			while (i.hasNext()) {
				PositionedRenderable pr = (PositionedRenderable) i.next();
				BoundableRenderable r = pr.renderable;
				Rectangle bounds = r.getBounds();
				if (bounds.contains(x, y)) {
					int childX = x - r.getX();
					int childY = y - r.getY();
					if (!r.onDoubleClick(event, childX, childY)) {
						return false;
					}
				}
			}
		}
		return this.tableMatrix.onDoubleClick(event, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lobobrowser.html.render.BoundableRenderable#onMouseDisarmed(java.awt
	 * .event.MouseEvent)
	 */
	public boolean onMouseDisarmed(MouseEvent event) {
		return this.tableMatrix.onMouseDisarmed(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lobobrowser.html.render.BoundableRenderable#onMousePressed(java.awt.
	 * event.MouseEvent, int, int)
	 */
	public boolean onMousePressed(MouseEvent event, int x, int y) {
		Collection<PositionedRenderable> prs = this.positionedRenderables;
		if (prs != null) {
			Iterator<PositionedRenderable> i = prs.iterator();
			while (i.hasNext()) {
				PositionedRenderable pr = (PositionedRenderable) i.next();
				BoundableRenderable r = pr.renderable;
				Rectangle bounds = r.getBounds();
				if (bounds.contains(x, y)) {
					int childX = x - r.getX();
					int childY = y - r.getY();
					if (!r.onMousePressed(event, childX, childY)) {
						return false;
					}
				}
			}
		}
		return this.tableMatrix.onMousePressed(event, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lobobrowser.html.render.BoundableRenderable#onMouseReleased(java.awt
	 * .event.MouseEvent, int, int)
	 */
	public boolean onMouseReleased(MouseEvent event, int x, int y) {
		Collection<PositionedRenderable> prs = this.positionedRenderables;
		if (prs != null) {
			Iterator<PositionedRenderable> i = prs.iterator();
			while (i.hasNext()) {
				PositionedRenderable pr = (PositionedRenderable) i.next();
				BoundableRenderable r = pr.renderable;
				Rectangle bounds = r.getBounds();
				if (bounds.contains(x, y)) {
					int childX = x - r.getX();
					int childY = y - r.getY();
					if (!r.onMouseReleased(event, childX, childY)) {
						return false;
					}
				}
			}
		}
		return this.tableMatrix.onMouseReleased(event, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.render.RCollection#getRenderables()
	 */
	public Iterator<BoundableRenderable> getRenderables() {
		Collection<PositionedRenderable> prs = this.positionedRenderables;
		if (prs != null) {
			Collection<BoundableRenderable> c = new LinkedList<BoundableRenderable>();
			Iterator<PositionedRenderable> i = prs.iterator();
			while (i.hasNext()) {
				PositionedRenderable pr = (PositionedRenderable) i.next();
				BoundableRenderable r = pr.renderable;
				c.add(r);
			}
			Iterator<BoundableRenderable> i2 = this.tableMatrix
					.getRenderables();
			while (i2.hasNext()) {
				c.add(i2.next());
			}
			return c.iterator();
		} else {
			return this.tableMatrix.getRenderables();
		}
	}

	public void repaint(ModelNode modelNode) {
		// NOP
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.render.RenderableContainer#getBackground()
	 */
	public Color getPaintedBackgroundColor() {
		return this.container.getPaintedBackgroundColor();
	}

	private final void addPositionedRenderable(BoundableRenderable renderable,
			boolean verticalAlignable, boolean isFloat) {
		// Expected to be called only in GUI thread.
		SortedSet<PositionedRenderable> others = this.positionedRenderables;
		if (others == null) {
			others = new TreeSet<PositionedRenderable>(new ZIndexComparator());
			this.positionedRenderables = others;
		}
		others.add(new PositionedRenderable(renderable, verticalAlignable,
				this.otherOrdinal++, isFloat));
		renderable.setParent(this);
		if (renderable instanceof RUIControl) {
			this.container.addComponent(((RUIControl) renderable).widget
					.getComponent());
		}
	}

	private void importDelayedPair(DelayedPair pair) {
		BoundableRenderable r = pair.child;
		r.setOrigin(pair.x, pair.y);
		this.addPositionedRenderable(r, false, false);
	}

	public String toString() {
		return "RTable[this=" + System.identityHashCode(this) + ",node="
				+ this.modelNode + "]";
	}

	public static class LayoutKey {
		public final int availWidth;
		public final int availHeight;
		public final int whitespace;
		public final Font font;
		public final boolean overrideNoWrap;

		public LayoutKey(int availWidth, int availHeight, int whitespace,
				Font font, boolean overrideNoWrap) {
			super();
			this.availWidth = availWidth;
			this.availHeight = availHeight;
			this.whitespace = whitespace;
			this.font = font;
			this.overrideNoWrap = overrideNoWrap;
		}

		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof LayoutKey)) {
				return false;
			}
			LayoutKey other = (LayoutKey) obj;
			return other.availWidth == this.availWidth
					&& other.availHeight == this.availHeight
					&& other.whitespace == this.whitespace
					&& other.overrideNoWrap == this.overrideNoWrap
					&& Objects.equals(other.font, this.font);
		}

		public int hashCode() {
			Font font = this.font;
			return (this.availWidth * 1000 + this.availHeight)
					^ (font == null ? 0 : font.hashCode()) ^ this.whitespace;
		}
	}

	public static class LayoutValue {
		public final int width;
		public final int height;

		public LayoutValue(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

}
