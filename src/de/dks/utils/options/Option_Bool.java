package de.dks.utils.options;

public class Option_Bool extends Option {
	private boolean myValue;
	
	
	public Option_Bool() {
		super(false);
	}
	
	
	public Option_Bool(boolean value) {
		super(true);
		myValue = value;
	}
	
	
	@Override
	public String getTypeName() {
	    return "bool";
	}
	
	
	@Override
	public void set(String value) {
		value = value.toLowerCase();
	    if(value=="t"||value=="true"||value=="1") {
	        myValue = true;
	    } else if(value=="f"||value=="false"||value=="0") {
	        myValue = false;
	    } else {
	        throw new NumberFormatException("inconvertible");
	    }
	    setSet();
	}

	
	public boolean getValue() {
	    return myValue;
	}

	
	
	
	@Override
	public String getValueAsString() {
	    if(myValue) {
	        return "true";
	    } else {
	        return "false";
	    }
	}

}
