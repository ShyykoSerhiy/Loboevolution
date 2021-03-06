package org.lobobrowser.html.domimpl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.dombl.UINode;
import org.lobobrowser.html.js.Executor;
import org.lobobrowser.js.JavaScript;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Document;

/**
 * Implements common functionality of most elements.
 */
public class HTMLAbstractUIElement extends HTMLElementImpl {
	private Function onfocus, onblur, onclick, ondblclick, onmousedown,
			onmouseup, onmouseover, onmousemove, onmouseout, onkeypress,
			onkeydown, onkeyup, oncontextmenu, onabort, onplay, onplaying,
			onprogress, onreadystatechange, onscroll, onseeked, onseeking,
			onselect, onshow, onstalled, onsubmit, onsuspend, ontimeupdate,
			onwaiting, onvolumechange,onfinish,onstart,onbounce,onload;

	public HTMLAbstractUIElement(String name) {
		super(name);
	}

	public Function getOnfinish() {
		return this.getEventFunction(onfinish, "onfinish");
	}

	public void setOnfinish(Function onfinish) {
		this.onfinish = onfinish;
	}

	public Function getOnstart() {
		return this.getEventFunction(onstart, "onstart");
	}

	public void setOnstart(Function onstart) {
		this.onstart = onstart;
	}

	public Function getOnbounce() {
		return this.getEventFunction(onbounce, "onbounce");
	}

	public void setOnbounce(Function onbounce) {
		this.onbounce = onbounce;
	}

	public Function getOnblur() {
		return this.getEventFunction(onblur, "onblur");
	}

	public void setOnblur(Function onblur) {
		this.onblur = onblur;
	}

	public Function getOnclick() {
		return this.getEventFunction(onclick, "onclick");
	}

	public void setOnclick(Function onclick) {
		this.onclick = onclick;
	}

	public Function getOndblclick() {
		return this.getEventFunction(ondblclick, "ondblclick");
	}

	public void setOndblclick(Function ondblclick) {
		this.ondblclick = ondblclick;
	}

	public Function getOnfocus() {
		return this.getEventFunction(onfocus, "onfocus");
	}

	public void setOnfocus(Function onfocus) {
		this.onfocus = onfocus;
	}

	public Function getOnkeydown() {
		System.out.println("1");
		return this.getEventFunction(onkeydown, "onkeydown");
	}

	public void setOnkeydown(Function onkeydown) {
		System.out.println("2");
		this.onkeydown = onkeydown;
	}

	public Function getOnkeypress() {
		return this.getEventFunction(onkeypress, "onkeypress");
	}

	public void setOnkeypress(Function onkeypress) {
		this.onkeypress = onkeypress;
	}

	public Function getOnkeyup() {
		return this.getEventFunction(onkeyup, "onkeyup");
	}

	public void setOnkeyup(Function onkeyup) {
		this.onkeyup = onkeyup;
	}

	public Function getOnmousedown() {
		return this.getEventFunction(onmousedown, "onmousedown");
	}

	public void setOnmousedown(Function onmousedown) {
		this.onmousedown = onmousedown;
	}

	public Function getOnmousemove() {
		return this.getEventFunction(onmousemove, "onmousemove");
	}

	public void setOnmousemove(Function onmousemove) {
		this.onmousemove = onmousemove;
	}

	public Function getOnmouseout() {
		return this.getEventFunction(onmouseout, "onmouseout");
	}

	public void setOnmouseout(Function onmouseout) {
		this.onmouseout = onmouseout;
	}

	public Function getOnmouseover() {
		return this.getEventFunction(onmouseover, "onmouseover");
	}

	public void setOnmouseover(Function onmouseover) {
		this.onmouseover = onmouseover;
	}

	public Function getOnmouseup() {
		return this.getEventFunction(onmouseup, "onmouseup");
	}

	public void setOnmouseup(Function onmouseup) {
		this.onmouseup = onmouseup;
	}

	public Function getOncontextmenu() {
		return this.getEventFunction(oncontextmenu, "oncontextmenu");
	}

	public void setOncontextmenu(Function oncontextmenu) {
		this.oncontextmenu = oncontextmenu;
	}

	public Function getOnabort() {
		return this.getEventFunction(onabort, "onabort");
	}

	public void setOnabort(Function onabort) {
		this.onabort = onabort;
	}

	public Function getOnplay() {
		return this.getEventFunction(onplay, "onplay");
	}

	public void setOnplay(Function onplay) {
		this.onplay = onplay;
	}

	public Function getOnplaying() {
		return this.getEventFunction(onplaying, "onplaying");
	}

	public void setOnplaying(Function onplaying) {
		this.onplaying = onplaying;
	}

	public Function getOnprogress() {
		return this.getEventFunction(onprogress, "onprogress");
	}

	public void setOnprogress(Function onprogress) {
		this.onprogress = onprogress;
	}

	public Function getOnratechange() {
		return this.getEventFunction(onprogress, "onprogress");
	}

	public void setOnratechange(Function onratechange) {
	}

	public Function getOnreadystatechange() {
		return this.getEventFunction(onreadystatechange, "onreadystatechange");
	}

	public void setOnreadystatechange(Function onreadystatechange) {
		this.onreadystatechange = onreadystatechange;
	}

	public Function getOnscroll() {
		return this.getEventFunction(onscroll, "onscroll");
	}

	public void setOnscroll(Function onscroll) {
		this.onscroll = onscroll;
	}

	public Function getOnseeked() {
		return this.getEventFunction(onseeked, "onseeked");
	}

	public void setOnseeked(Function onseeked) {
		this.onseeked = onseeked;
	}

	public Function getOnseeking() {
		return this.getEventFunction(onseeking, "onseeking");
	}

	public void setOnseeking(Function onseeking) {
		this.onseeking = onseeking;
	}

	public Function getOnselect() {
		return this.getEventFunction(onselect, "onselect");
	}

	public void setOnselect(Function onselect) {
		this.onselect = onselect;
	}

	public Function getOnshow() {
		return this.getEventFunction(onshow, "onshow");
	}

	public void setOnshow(Function onshow) {
		this.onshow = onshow;
	}

	public Function getOnstalled() {
		return this.getEventFunction(onstalled, "onstalled");
	}

	public void setOnstalled(Function onstalled) {
		this.onstalled = onstalled;
	}

	public Function getOnsubmit() {
		return this.getEventFunction(onsubmit, "onsubmit");
	}

	public void setOnsubmit(Function onsubmit) {
		this.onsubmit = onsubmit;
	}

	public Function getOnsuspend() {
		return this.getEventFunction(onsuspend, "onsuspend");
	}

	public void setOnsuspend(Function onsuspend) {
		this.onsuspend = onsuspend;
	}

	public Function getOntimeupdate() {
		return this.getEventFunction(ontimeupdate, "ontimeupdate");
	}

	public void setOntimeupdate(Function ontimeupdate) {
		this.ontimeupdate = ontimeupdate;
	}

	public Function getOnvolumechange() {
		return this.getEventFunction(onvolumechange, "onvolumechange");
	}

	public void setOnvolumechange(Function onvolumechange) {
		this.onvolumechange = onvolumechange;
	}

	public Function getOnwaiting() {
		return this.getEventFunction(onwaiting, "onwaiting");
	}

	public void setOnwaiting(Function onwaiting) {
		this.onwaiting = onwaiting;
	}
	
	public Function getOnload() {
		return this.getEventFunction(onload, "onload");
	}

	public void setOnload(Function onload) {
		this.onload = onload;
	}
	
	public void focus() {
		UINode node = this.getUINode();
		if (node != null) {
			node.focus();
		}
	}

	public void blur() {
		UINode node = this.getUINode();
		if (node != null) {
			node.blur();
		}
	}

	private Map<String, Function> functionByAttribute = null;

	public Function getEventFunction(Function varValue, String attributeName) {
		
		if (varValue != null) {
			return varValue;
		}
		
		String normalAttributeName = this.normalizeAttributeName(attributeName);
		synchronized (this) {
			Map<String, Function> fba = this.functionByAttribute;
			Function f = fba == null ? null : (Function) fba.get(normalAttributeName);
			if (f != null) {
				return f;
			}
			UserAgentContext uac = this.getUserAgentContext();
			if (uac == null) {
				throw new IllegalStateException("No user agent context.");
			}
			if (uac.isScriptingEnabled()) {
				String attributeValue = this.getAttribute(attributeName);
				if (attributeValue == null || attributeValue.length() == 0) {
					f = null;
				} else {
					String functionCode = "function " + normalAttributeName
							+ "_" + System.identityHashCode(this) + "() { "
							+ attributeValue + " }";
					Document doc = this.document;
					if (doc == null) {
						throw new IllegalStateException(
								"Element does not belong to a document.");
					}
					Context ctx = Executor.createContext(this.getDocumentURL(),uac);
					try {
						Scriptable scope = (Scriptable) doc.getUserData(Executor.SCOPE_KEY);
						if (scope == null) {
							throw new IllegalStateException(
									"Scriptable (scope) instance was expected to be keyed as UserData to document using "
											+ Executor.SCOPE_KEY);
						}
						Scriptable thisScope = (Scriptable) JavaScript.getInstance().getJavascriptObject(this, scope);
						try {
							// TODO: Get right line number for script. //TODO:
							// Optimize this in case it's called multiple times?
							// Is that done?
							f = ctx.compileFunction(thisScope, functionCode,
									this.getTagName() + "[" + this.getId()
											+ "]." + attributeName, 1, null);
						} catch (EcmaError ecmaError) {
							logger.log(
									Level.WARNING,
									"Javascript error at "
											+ ecmaError.sourceName() + ":"
											+ ecmaError.lineNumber() + ": "
											+ ecmaError.getMessage(), ecmaError);
							f = null;
						} catch (Throwable err) {
							logger.log(Level.WARNING,
									"Unable to evaluate Javascript code", err);
							f = null;
						}
					} finally {
						Context.exit();
					}
				}
				if (fba == null) {
					fba = new HashMap<String, Function>(1);
					this.functionByAttribute = fba;
				}
				fba.put(normalAttributeName, f);
			}
			return f;
		}
	}

	protected void assignAttributeField(String normalName, String value) {
		super.assignAttributeField(normalName, value);
		if (normalName.startsWith("on")) {
			synchronized (this) {
				Map<String, Function> fba = this.functionByAttribute;
				if (fba != null) {
					fba.remove(normalName);
				}
			}
		}
	}
}
