package org.lobobrowser.html.w3c;

import org.w3c.dom.NodeList;

public interface HTMLOutputElement extends HTMLElement {
	// HTMLOutputElement
	public DOMSettableTokenList getHtmlFor();

	public void setHtmlFor(String htmlFor);

	public HTMLFormElement getForm();

	public String getName();

	public void setName(String name);

	public String getType();

	public String getDefaultValue();

	public void setDefaultValue(String defaultValue);

	public String getValue();

	public void setValue(String value);

	public boolean getWillValidate();

	public ValidityState getValidity();

	public String getValidationMessage();

	public boolean checkValidity();

	public void setCustomValidity(String error);

	public NodeList getLabels();
}
