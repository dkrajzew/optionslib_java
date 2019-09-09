package de.dks.utils.options;

public class Option_Integer extends Option {
	private int myValue;
	
	
	public Option_Integer() {
		super(false);
	}
	
	
	public Option_Integer(int value) {
		super(true);
		myValue = value;
	}
	
	
	@Override
	public String getTypeName() {
	    return "int";
	}
	
	
	@Override
	public void set(String valueS) {
		int value = Integer.parseInt(valueS);
		myValue = value;
	    setSet();
	}

	
	public int getValue() {
	    return myValue;
	}

	
	
	
	@Override
	public String getValueAsString() {
	    return Integer.toString(myValue);
	}

}
