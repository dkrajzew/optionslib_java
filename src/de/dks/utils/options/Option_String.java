package de.dks.utils.options;

public class Option_String extends Option {
	private String myValue;
	
	
	public Option_String() {
		super(false);
	}
	
	
	public Option_String(String value) {
		super(true);
		myValue = value;
	}
	
	
	@Override
	public String getTypeName() {
	    return "string";
	}
	
	
	@Override
	public void set(String valueS) {
		myValue = valueS;
	    setSet();
	}

	
	public String getValue() {
	    return myValue;
	}

	
	
	
	@Override
	public String getValueAsString() {
	    return myValue;
	}

}
