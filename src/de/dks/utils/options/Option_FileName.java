package de.dks.utils.options;

public class Option_FileName extends Option_String {
	public Option_FileName() {
		super();
	}
	
	
	public Option_FileName(String value) {
		super(value);
	}
	
	
	@Override
	public String getTypeName() {
	    return "filename";
	}
	

   /** @brief Returns whether this option is of the type "filename"
    *
    * Returns false unless overridden (in Option_Filename)
    * @return Whether this options is a file name
	*/
	@Override
	public boolean isFileName()  {
	    return true;
	}
}
