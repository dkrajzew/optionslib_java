package de.dks.examples.options;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import de.dks.utils.options.Option;
import de.dks.utils.options.Option_Bool;
import de.dks.utils.options.Option_Double;
import de.dks.utils.options.Option_FileName;
import de.dks.utils.options.Option_Integer;
import de.dks.utils.options.Option_String;
import de.dks.utils.options.OptionsCont;
import de.dks.utils.options.OptionsIO;

public class Tester {
	private static String configOptionName = "";
	
	private static OptionsCont loadDefinition() throws IOException {
		OptionsCont options = new OptionsCont();
		BufferedReader in = new BufferedReader(new FileReader("options.txt"));
		if(!in.ready()) {
			in.close();
			throw new RuntimeException("Could not open the definitions file ('options.txt')");
		}
	    while(in.ready()) {
	        String line = in.readLine();
	        if(line.indexOf(';')<-1) {
	            // ok, it's a subsection
	        } else {
	            // ok, it's an option
	            // ... parse the line
	        	Vector<String> synonymes = new Vector<>();
	            char abbr = '!';
	            String type=null, description=null, defaultValue=null;
	            StringTokenizer st = new StringTokenizer(line, ";");
	            while (st.hasMoreTokens()) {
	            	String s = st.nextToken();
	                if(type.length()==0) {
	                    type = s;
	                    continue;
	                }
	                if (s.length()==1) {
	                    abbr = s.charAt(0);
	                } else if(s.charAt(0)=='!') {
	                    description = s.substring(1);
	                }  else if(s.charAt(0)=='+') {
	                    defaultValue = s.substring(1);
	                } else {
	                    synonymes.add(s);
	                }
	            }
	            // ... is it the tail/head help stuff?
	            if(type=="HELPHEADTAIL") {
	            	options.setHelpHeadAndTail(synonymes.elementAt(0), synonymes.elementAt(1));
	                continue;
	            }
	            if(type=="CONFIG") {
	                configOptionName = synonymes.elementAt(0);
	                continue;
	            }
	            // ... is it a named section begin?
	            if(type=="SECTION") {
	            	options.beginSection(synonymes.elementAt(0));
	                continue;
	            }
	            // ... build the option, first
	            Option option = null;
	            if(type=="INT") {
	                if(defaultValue.length()==0) {
	                    option = new Option_Integer();
	                } else {
	                    int v = Integer.parseInt(defaultValue);
	                    option = new Option_Integer(v);
	                }
	            } else if(type=="DOUBLE") {
	                if(defaultValue.length()==0) {
	                    option = new Option_Double();
	                } else {
	                    double v = Double.parseDouble(defaultValue);
	                    option = new Option_Double(v);
	                }
	            } else if(type=="BOOL") {
	                if(defaultValue.length()==0) {
	                    option = new Option_Bool();
	                } else {
	                    option = new Option_Bool();
	                }
	            } else if(type=="STRING") {
	                if(defaultValue.length()==0) {
	                    option = new Option_String();
	                } else {
	                    option = new Option_String(defaultValue);
	                }
	            } else if(type=="FILE") {
	                if(defaultValue.length()==0) {
	                    option = new Option_FileName();
	                } else {
	                    option = new Option_FileName(defaultValue);
	                }
	            }
	            // ... add it to the container
	            String firstName = synonymes.elementAt(0);
	            synonymes.remove(0);
	            if(abbr!='!') {
	                options.add(firstName, abbr, option);
	            } else {
	            	options.add(firstName, option);
	            }
	            while(synonymes.size()!=0) {
	            	options.addSynonyme(firstName, synonymes.elementAt(0));
	                synonymes.remove(0);
	            }
	            // ... add description if given
	            if(description.length()!=0) {
	            	options.setDescription(firstName, description);
	            }
	        }
	    }
	    in.close();
	    return options;
	}	

	
	public static void main(String[] args) {
	    try {
	        // load the definition
	        OptionsCont options = loadDefinition();
	        // parse options
            if(OptionsIO.parseAndLoad(options, args, configOptionName, false, false)) {
            	options.printHelp(System.out, 80, 2, 2, 1);
            	System.out.println("-------------------------------------------------------------------------------");
            	//std::cout << myOptions;
            	System.out.println("-------------------------------------------------------------------------------");
            }
	    } catch(Exception e) {
	    	System.err.println(e.toString());
        }
	}

}
