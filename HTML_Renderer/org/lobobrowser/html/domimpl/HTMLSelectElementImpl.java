package org.lobobrowser.html.domimpl;

import java.util.ArrayList;

import org.lobobrowser.html.FormInput;
import org.lobobrowser.html.HtmlAttributeProperties;
import org.lobobrowser.html.dombl.InputContext;
import org.lobobrowser.html.w3c.HTMLCollection;
import org.lobobrowser.html.w3c.HTMLElement;
import org.lobobrowser.html.w3c.HTMLOptionsCollection;
import org.lobobrowser.html.w3c.HTMLSelectElement;
import org.lobobrowser.html.w3c.ValidityState;
import org.mozilla.javascript.Function;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;

public class HTMLSelectElementImpl extends HTMLBaseInputElement implements
		HTMLSelectElement {
	public HTMLSelectElementImpl(String name) {
		super(name);
	}

	public void add(HTMLElement element, HTMLElement before)
			throws DOMException {
		this.insertBefore(element, before);
	}

	public int getLength() {
		return this.getOptions().getLength();
	}

	private Boolean multipleState = null;

	public boolean getMultiple() {
		Boolean m = this.multipleState;
		if (m != null) {
			return m.booleanValue();
		}
		return this.getAttributeAsBoolean("multiple");
	}

	private HTMLOptionsCollection options;

	public HTMLOptionsCollection getOptions() {
		synchronized (this) {
			if (this.options == null) {
				this.options = new HTMLOptionsCollectionImpl(this);
			}
			return this.options;
		}
	}

	public int getSelectedIndex() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			return ic.getSelectedIndex();
		} else {
			return this.deferredSelectedIndex;
		}
	}

	public int getSize() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			return ic.getVisibleSize();
		} else {
			return 0;
		}
	}

	public String getType() {
		return this.getMultiple() ? "select-multiple" : "select-one";
	}

	public void remove(int index) {
		try {
			this.removeChild(this.getOptions().item(index));
		} catch (DOMException de) {
			this.warn("remove(): Unable to remove option at index " + index
					+ ".", de);
		}
	}

	public void setLength(int length) throws DOMException {
		this.getOptions().setLength(length);
	}

	public void setMultiple(boolean multiple) {
		boolean prevMultiple = this.getMultiple();
		this.multipleState = Boolean.valueOf(multiple);
		if (prevMultiple != multiple) {
			this.informLayoutInvalid();
		}
	}

	private int deferredSelectedIndex = -1;

	public void setSelectedIndex(int selectedIndex) {
		this.setSelectedIndexImpl(selectedIndex);
		HTMLOptionsCollection options = this.getOptions();
		int length = options.getLength();
		for (int i = 0; i < length; i++) {
			HTMLOptionElementImpl option = (HTMLOptionElementImpl) options
					.item(i);
			option.setSelectedImpl(i == selectedIndex);
		}
	}

	void setSelectedIndexImpl(int selectedIndex) {
		InputContext ic = this.inputContext;
		if (ic != null) {
			ic.setSelectedIndex(selectedIndex);
		} else {
			this.deferredSelectedIndex = selectedIndex;
		}
	}

	public void setSize(int size) {
		InputContext ic = this.inputContext;
		if (ic != null) {
			ic.setVisibleSize(size);
		}
	}

	protected FormInput[] getFormInputs() {
		// Needs to be overriden for forms to submit.
		InputContext ic = this.inputContext;
		String[] values = ic == null ? null : ic.getValues();
		if (values == null) {
			String value = this.getValue();
			values = value == null ? null : new String[] { value };
			if (values == null) {
				return null;
			}
		}
		String name = this.getName();
		if (name == null) {
			return null;
		}
		ArrayList<FormInput> formInputs = new ArrayList<FormInput>();
		for (int i = 0; i < values.length; i++) {
			formInputs.add(new FormInput(name, values[i]));
		}
		return (FormInput[]) formInputs.toArray(FormInput.EMPTY_ARRAY);
	}

	public void resetInput() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			ic.resetInput();
		}
	}

	public void setInputContext(InputContext ic) {
		super.setInputContext(ic);
		if (ic != null) {
			ic.setSelectedIndex(this.deferredSelectedIndex);
		}
	}

	private Function onchange;

	public Function getOnchange() {
		return this.getEventFunction(this.onchange, "onchange");
	}

	public void setOnchange(Function value) {
		this.onchange = value;
	}

	@Override
	public boolean getAutofocus() {
		String auto = this.getAttribute(HtmlAttributeProperties.AUTOFOCUS);
		return HtmlAttributeProperties.AUTOFOCUS.equalsIgnoreCase(auto);
	}

	@Override
	public void setAutofocus(boolean autofocus) {
		this.setAttribute(HtmlAttributeProperties.AUTOFOCUS, autofocus ? HtmlAttributeProperties.AUTOFOCUS : null);
		
	}

	@Override
	public Object item(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object namedItem(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(HTMLElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(HTMLElement element, int before) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HTMLCollection getSelectedOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getWillValidate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ValidityState getValidity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValidationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkValidity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCustomValidity(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NodeList getLabels() {
		// TODO Auto-generated method stub
		return null;
	}
}
