package de.dks.utils.options;

public class Option_Double extends Option {
	private double myValue;
	
	
	public Option_Double() {
		super(false);
	}
	
	
	public Option_Double(double value) {
		super(true);
		myValue = value;
	}
	
	
	@Override
	public String getTypeName() {
	    return "double";
	}
	
	
	@Override
	public void set(String valueS) {
		double value = Double.parseDouble(valueS);
		myValue = value;
	    setSet();
	}

	
	public double getValue() {
	    return myValue;
	}

	
	
	
	@Override
	public String getValueAsString() {
	    return Double.toString(myValue);
	}

}
