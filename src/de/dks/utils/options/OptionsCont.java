package de.dks.utils.options;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class OptionsCont {
	private HashMap<String, Option> myOptionsMap = new HashMap<>();
	private HashMap<Option, String> myOption2Section = new HashMap<>();
	private Vector<Option> myOptions = new Vector<>();
	private String myCurrentSection;
	private String myHelpHead, myHelpTail;
	
	public class SortByLengthComparator implements Comparator<String> {
	    public int compare(String obj1, String obj2) {
	    	if(obj1.length() == obj2.length()) {
	    		return 0;
	    	}
	        return obj1.length() < obj2.length() ? -1 : 1;
	    }
	}
	
	
	public OptionsCont() {
	}

	

	/* -------------------------------------------------------------------------
	 * Filling Options
	 * ----------------------------------------------------------------------- */
	public void add(char abbr, Option option) {
	    add(convert(abbr), option);
	}


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


	public void add(String name, char abbr, Option option) {
	    add(name, option);
	    add(convert(abbr), option);
	}


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


	public void setDescription(String name, String desc) {
	    Option o = getOption(name);
	    o.setDescription(desc);
	}


	public void beginSection(String name) {
	    myCurrentSection = name;
	}


	public void setHelpHeadAndTail(String head, String tail) {
	    myHelpHead = head;
	    myHelpTail = tail;
	}


	/* -------------------------------------------------------------------------
	 * Filling Options
	 * ----------------------------------------------------------------------- */
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


	public boolean isSet(String name) {
	    Option o = getOption(name);
	    return o.isSet();
	}


	public boolean isBool(String name) {
		Option o = getOptionSecure(name);
		if(!(o instanceof Option_Bool)) { 
			return false;
		}
		return true;
	}


	public void set(String name, String value) {
	    Option o = getOption(name);
	    o.set(value);
	}


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


	public Option getOption(String name) {
		if(!myOptionsMap.containsKey(name)) {
			throw new RuntimeException("The option '" + name + "' is not known.");
		}
		return myOptionsMap.get(name);
	}


	public Option getOptionSecure(String name) {
		if(!myOptionsMap.containsKey(name)) {
			return null;
		}
		return myOptionsMap.get(name);
	}


	public boolean contains(String name) {
		return myOptionsMap.containsKey(name);
	}


	public Vector<String> getSynonymes(String name) {
	    Option option = getOption(name);
	    return getSynonymes(option);
	}


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


	public void remarkUnset() {
	    for(Iterator<Option> i=myOptions.iterator(); i.hasNext(); ) {
	    	Option o = i.next();
	    	o.remarkSetable();
	    }
	}


	public String convert(char abbr) {
		String ret = "";
		ret += abbr;
		return ret;
	}


	public void printHelp(PrintStream os, int maxWidth, int optionIndent, int divider, int sectionIndent) {
	    // compute needed width
		int optMaxWidth = 0;
		for(Iterator<Option> i=myOptions.iterator(); i.hasNext(); ) {
			Option option = i.next();
			String optNames = getHelpFormattedSynonymes(option, optionIndent, divider);
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
	        String optNames = getHelpFormattedSynonymes(option, optionIndent, divider);
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


	public String getHelpFormattedSynonymes(Option option, int optionIndent, int divider) {
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
