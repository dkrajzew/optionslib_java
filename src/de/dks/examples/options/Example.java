package de.dks.examples.options;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.dks.utils.options.Option_Bool;
import de.dks.utils.options.Option_Integer;
import de.dks.utils.options.Option_String;
import de.dks.utils.options.OptionsCont;
import de.dks.utils.options.OptionsIO;

public class Example {
	private static OptionsCont getOptions(String[] args) throws ParserConfigurationException, SAXException, IOException {
		OptionsCont options = new OptionsCont();
		options.setHelpHeadAndTail("Usage: example [option]+\n\nOptions:","");
	    // function
		options.add("name", 'n', new Option_String("World"));
	    options.setDescription("name", "Defines how to call the user.");
	    options.add("greet", 'g', new Option_String("Hello"));
	    options.setDescription("greet", "Defines how to greet.");
	    options.add("repeat", 'r', new Option_Integer());
	    options.setDescription("repeat", "Sets an optional number of repetitions.");
	    //
	    options.add("help", '?', new Option_Bool());
	    options.setDescription("help", "Prints this help screen.");
	    // parse
	    OptionsIO.parseAndLoad(options, args, "", false, false);
	    return options;
	}

	
	public static void main(String[] args) {
	    try {
	        // parse options
	    	OptionsCont options = getOptions(args);
	        // check for additional (meta) options
	        if(options.getBool("help")) {
	            // print the help screen
	        	options.printHelp(System.out, 80, 2, 2, 1);
	        } else {
	            // do something
	            String name = options.getString("name");
	            String greet = options.getString("greet");
	            int number = 1;
	            if(options.isSet("repeat")) {
	                number = options.getInteger("repeat");
	            }
	            for (int i=0; i<number; ++i) {
	            	System.out.println(greet + " " + name + "!");
	            }
	        }
	    } catch(Exception e) {
	    	System.err.println(e.toString());
	    }
	}

}
