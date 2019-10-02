package de.dks.utils.options;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * @class OptionsCont
 * @brief An options storing container.
 * @author Daniel Krajzewicz 
 * @copyright (c) Daniel Krajzewicz 2004-2019
 * @license Eclipse Public License v2.0 (EPL v2.0) 
 */
public class OptionsCont {
	/// @brief A map from option names to options
	private HashMap<String, Option> myOptionsMap = new HashMap<>();
	
	/// @brief The option's assignment to sections
	private HashMap<Option, String> myOption2Section = new HashMap<>();
	
	/// @brief The list of known options
	private Vector<Option> myOptions = new Vector<>();
	
	/// @brief THe last section added
	private String myCurrentSection;
	
	/// @brief The head and the tail of the help pages
	private String myHelpHead, myHelpTail;
	
	

    /** @brief A string-by-length comparator
     */
	public class SortByLengthComparator implements Comparator<String> {
	    public int compare(String obj1, String obj2) {
	    	if(obj1.length() == obj2.length()) {
	    		return 0;
	    	}
	        return obj1.length() < obj2.length() ? -1 : 1;
	    }
	}
	
	
	/** @brief Constructor 
	 */
	public OptionsCont() {
	}

	

    /// @brief Filling Options
    /// @{

    /** @brief Registers an option under an abbreviation
	 * @param[in] abbr The option's abbreviated name
	 * @param[in] option The option
     */
	public void add(char abbr, Option option) {
	    add(convert(abbr), option);
	}


    /** @brief Registers an option under the given name
	 * @param[in] name The option's name
	 * @param[in] option The option
	 */
	public void add(String name, Option option) {
	    // check whether the name is already used
		if(myOptionsMap.containsKey(name)) {
			throw new RuntimeException("An option with the name '" + name + "' already exists.");
		}
	    // check whether a synonyme already exists, if not, add the option to option's array
		if(!myOptions.contains(option)) {
			myOptions.add(option);
			myOption2Section.put(option, myCurrentSection);
		}
	    // add the option to the name-to-option map
		myOptionsMap.put(name, option);
	}


    /** @brief Registers an option under the given abbreviation and the given name
	 * @param[in] name The option's name
	 * @param[in] abbr The option's abbreviated name
	 * @param[in] option The option
     */
	public void add(String name, char abbr, Option option) {
	    add(name, option);
	    add(convert(abbr), option);
	}


    /** @brief Registers a known option under the other synonyme
	 * @param[in] name1 The name the option was already known under
	 * @param[in] name2 The synonyme to register
	 */
	public void addSynonyme(String name1, String name2) {
	    Option o1 = getOptionSecure(name1);
	    Option o2 = getOptionSecure(name2);
	    if(o1==null&&o2==null) {
	        throw new RuntimeException("Neither an option with the name '" + name1 + "' nor an option with the name '" + name2 + "' is known.");
	    }
	    if(o1!=null&&o2!=null) {
	        throw new RuntimeException("Both options are already set ('" + name1 + "' and '" + name2 + "')!");
	    }
	    if(o1!=null) {
	        add(name2, o1);
	    } else {
	        add(name1, o2);
	    }
	}


    /** @brief Sets the description for an already added option
     *
     * The description is what appears in the help menu
     * @param[in] name The name of the option
     * @param[in] desc The description of the option
     * @param[in] semType The type of the value
	 */
	public void setDescription(String name, String desc) {
	    Option o = getOption(name);
	    o.setDescription(desc);
	}


    /** @brief Starts a new section
     *
     * Options will be stored under this section until a new starts.
	 * @param[in] name The name of the section
	 */
	public void beginSection(String name) {
	    myCurrentSection = name;
	}


    /** @brief Sets the head and the tail of the help output
	 * @param[in] head The head of the help output
	 * @param[in] tail The tail of the help output
	 */
	public void setHelpHeadAndTail(String head, String tail) {
	    myHelpHead = head;
	    myHelpTail = tail;
	}
    /// @}




    /// @brief Retrieving Values
    /// @{

    /** @brief Returns the integer value of the named option
     * @param[in] name The name of the option to retrieve the value from
	 * @return The named option's value
	 */
	public int getInteger(String name) {
		Option o = getOptionSecure(name);
		if(!(o instanceof Option_Integer)) { 
			throw new RuntimeException("This is not an integer option!");
		}
	    if(!o.isSet()) {
	    	throw new RuntimeException("The option is not set!");
	    }
	    return ((Option_Integer) o).getValue();
	}


    /** @brief Returns the float value of the named option
	 * @param[in] name The name of the option to retrieve the value from
	 * @return The named option's value
	 */
	public double getDouble(String name) {
		Option o = getOptionSecure(name);
		if(!(o instanceof Option_Double)) { 
			throw new RuntimeException("This is not a double option!");
		}
	    if(!o.isSet()) {
	    	throw new RuntimeException("The option is not set!");
	    }
	    return ((Option_Double) o).getValue();
	}


    /** @brief Returns the boolean value of the named option
	 * @param[in] name The name of the option to retrieve the value from
	 * @return The named option's value
	 */
	public boolean getBool(String name) {
		Option o = getOptionSecure(name);
		if(!(o instanceof Option_Bool)) { 
			throw new RuntimeException("This is not a bool option!");
		}
	    if(!o.isSet()) {
	    	throw new RuntimeException("The option is not set!");
	    }
	    return ((Option_Bool) o).getValue();
	}


    /** @brief Returns the string value of the named option
	 * @param[in] name The name of the option to retrieve the value from
	 * @return The named option's value
	 */
	public String getString(String name) {
		Option o = getOptionSecure(name);
		if(!(o instanceof Option_String)) { 
			throw new RuntimeException("This is not a string option!");
		}
	    if(!o.isSet()) {
	    	throw new RuntimeException("The option is not set!");
	    }
	    return ((Option_String) o).getValue();
	}


    /** @brief Returns the information whether the option is set
	 * @param[in] name The name of the option to check
	 * @return Whether the option has a value set
	 */
	public boolean isSet(String name) {
	    Option o = getOption(name);
	    return o.isSet();
	}


    /** @brief Returns the information whether the option is a boolean option
	 * @param[in] name The name of the option to check
	 * @return Whether the option stores a bool
	 */
	public boolean isBool(String name) {
		Option o = getOptionSecure(name);
		if(!(o instanceof Option_Bool)) { 
			return false;
		}
		return true;
	}


    /** @brief Returns the information whether the named option is known
	 * @param[in] name The name of the option
	 * @return Whether the option is known
	 */
	public boolean contains(String name) {
		return myOptionsMap.containsKey(name);
	}


    /** @brief Returns the list of synonymes to the given option name
	 * @param[in] name The name of the option
	 * @return List of this option's names
	 */
	public Vector<String> getSynonymes(String name) {
	    Option option = getOption(name);
	    return getSynonymes(option);
	}


    /** @brief Returns the list of names of the given option
	 * @param[in] option The option to retrieve her names
	 * @return List of this option's names
	 */
	public Vector<String> getSynonymes(Option option) {
	    Vector<String> ret = new Vector<>();
	    for(Iterator<String> i=myOptionsMap.keySet().iterator(); i.hasNext(); ) {
	    	String name = i.next();
	    	if(myOptionsMap.get(name)==option) {
	    		ret.add(name);
	    	}
	    }
	    Collections.sort(ret, new SortByLengthComparator()) ;
	    return ret;
	}
    /// @}


	
    /// @brief Input / Output
    /// @{

    /** @brief Output operator
     * @param[in] oc The output container to write
	 */
	public void printSetOptions(PrintStream os) {
	    Vector<String> known = new Vector<>();
	    for(Iterator<String> i=myOptionsMap.keySet().iterator(); i.hasNext(); ) { //std::map<std::string, Option*>::const_iterator i=oc.myOptionsMap.begin(); i!=oc.myOptionsMap.end(); i++) {
	        //Vector<string>::iterator j=find(known.begin(), known.end(), (*i).first);
	        String name = i.next();
	        if(known.contains(name)) {
	        	continue;
	        }
            Option o = myOptionsMap.get(name);
            if(!o.isSet()) {
            	continue;
            }
            Vector<String> synonymes = getSynonymes(name);
            Iterator<String> j=synonymes.iterator();
            String first = j.next();
            os.print(first);
            known.add(first);
            if(j.hasNext()) {
            	os.print(" (");
                for(; j.hasNext(); ) {
                	String name2 = j.next();
                    known.add(name2);
                    os.print(name2);
                    if(j.hasNext()) {
                    	os.print(", ");
                    }
                }
                os.print(")");
            }
            os.print(": " + o.getValueAsString());
            if(o.isDefault()) {
            	os.print(" (default)");
            }
            os.println();
	    }
	}
	

    /** @brief Prints the help screen
    *
    * First, the help header is printed. Then, the method iterates over the
    *  known options. In the end, the help tail is printed.
    * @param[in] os The stream to write to
    * @param[in] maxWidth The maximum width of a line
    * @param[in] optionIndent The indent to use before writing an option
    * @param[in] divider The space between the option name and the description
    * @param[in] sectionIndent The indent to use before writing a section name
	 */
	public void printHelp(PrintStream os, int maxWidth, int optionIndent, int divider, int sectionIndent) {
	    // compute needed width
		int optMaxWidth = 0;
		for(Iterator<Option> i=myOptions.iterator(); i.hasNext(); ) {
			Option option = i.next();
			String optNames = getHelpFormattedSynonymes(option);
			optMaxWidth = Math.max(optMaxWidth, optNames.length());
		}
		// build the indent
	    String optionIndentSting = "", sectionIndentSting = "";
	    for(int i=0; i<optionIndent; ++i) {
	        optionIndentSting += " ";
	    }
	    for(int i=0; i<sectionIndent; ++i) {
	        sectionIndentSting += " ";
	    }
	    // 
	    if(myHelpHead!=null) {
	        os.println(myHelpHead);
	    }
	    String lastSection = "";
		for(Iterator<Option> i=myOptions.iterator(); i.hasNext(); ) {
			Option option = i.next();
	        // check whether a new section starts
	        String optSection = myOption2Section.get(option);
	        if(optSection!=null&&!"".contentEquals(optSection)&&lastSection!=optSection) {
	            lastSection = optSection;
	            os.println(sectionIndentSting+lastSection);
	        }
	        // write the option
	        String optNames = getHelpFormattedSynonymes(option);
	        // write the divider
	        os.print(optionIndentSting+optNames);
	        int owidth = optNames.length();
	        // write the description
	        int beg = 0;
	        String desc = option.getDescription();
	        int offset = divider+optMaxWidth-owidth;
            int startCol = divider+optMaxWidth+optionIndent;
	        while(desc!=null&&beg<desc.length()) {
	            for(int j=0; j<offset; ++j) {
	                os.print(" ");
	            }
                if(maxWidth-startCol>=desc.length()-beg) {
	                os.print(desc.substring(beg));
	                beg = desc.length();
	            } else {
	                int end = desc.indexOf(' ', beg+maxWidth-startCol);
	                os.println(desc.substring(beg, end));
                    beg = end;
	            }
                startCol = divider+optMaxWidth+optionIndent+1; // could "running description indent"
                offset = startCol;
	        }
	        os.println();
	    }
	    if(myHelpTail!=null) {
	    	os.println(myHelpTail);
	    }
	}
    /// @}

	
	
    /// @brief (Re-)Setting values
    /// @{

    /** @brief Sets the given value to the given option
     * @param[in] name The name of the option to set
	 * @param[in] value The value to set
     */
	public void set(String name, String value) {
	    Option o = getOption(name);
	    o.set(value);
	}


    /** @brief Sets the given value to the given option (boolean options only)
	 * @param[in] name The name of the option to set
	 * @param[in] value The value to set
	 */
	public void set(String name, boolean value) {
		Option o = getOptionSecure(name);
		if(!(o instanceof Option_Bool)) { 
			throw new RuntimeException("This is not a boolean option");
		}
		if(value) {
			o.set("true");
		} else {
			o.set("false");
		}
	}


    /// @brief Remarks all options as unset
	public void remarkUnset() {
	    for(Iterator<Option> i=myOptions.iterator(); i.hasNext(); ) {
	    	Option o = i.next();
	    	o.remarkSetable();
	    }
	}
    /// @}

	
	
    /// @brief Private helper options
    /// @{

    /** @brief Returns the option; throws an exception when not existing
	 * @param[in] name The name of the option
	 * @return The option if known
	 * @throw InvalidArgument If the option is not known
	 */
	private Option getOption(String name) {
		if(!myOptionsMap.containsKey(name)) {
			throw new RuntimeException("The option '" + name + "' is not known.");
		}
		return myOptionsMap.get(name);
	}


    /** @brief Returns the option or null when not existing
	 * @param[in] name The name of the option
	 * @return The option if known, null otherwise
	 */
	private Option getOptionSecure(String name) {
		if(!myOptionsMap.containsKey(name)) {
			return null;
		}
		return myOptionsMap.get(name);
	}


    /** @brief Converts the character into a string
 	 * @param[in] abbr The abbreviated name
	 * @return The abbreviated name as a string
	 */
	private String convert(char abbr) {
		String ret = "";
		ret += abbr;
		return ret;
	}


    /** @brief Returns the synomymes of an option as a help-formatted string 
    *
    * The synomymes are sorted by length.
    * @param[in] option The option to get the synonymes help string for
    * @return The options as a help-formatted string
    */
	private String getHelpFormattedSynonymes(Option option) {
	    Vector<String> synonymes = getSynonymes(option);
	    Collections.sort(synonymes, new SortByLengthComparator()) ;
	    StringBuffer sb = new StringBuffer();
	    for(Iterator<String> i=synonymes.iterator(); i.hasNext(); ) {
	    	String name = i.next();
	        // consider the - / --
	        if(name.length()==1) {
	            sb.append('-');
	        } else {
	            sb.append("--");
	        }
	        sb.append(name);
	        if(i.hasNext()) {
	            sb.append(", ");
	        }
	    }
	    return sb.toString();
	}

}
