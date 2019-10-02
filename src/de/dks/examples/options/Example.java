package de.dks.examples.options;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.dks.utils.options.Option_Bool;
import de.dks.utils.options.Option_Integer;
import de.dks.utils.options.Option_String;
import de.dks.utils.options.OptionsCont;
import de.dks.utils.options.OptionsIO;

/**
 * @class Example
 * @brief An example application for using the optionslib_java option parsing library.
 *  
 * A very basic example for using the options. It's basically a "Hello World"-application
 * that allows you to give a name and a greet on the command line. 
 * The default for the name is "World" by default, but may be defined using the option 
 * --name <NAME>, or -n <NAME> for short. The default for the greet is "Hello" by default, 
 * but may be defined using the option --greet <GREET>, or -g <GREET> for short. 
 * Additionally, you may optionally change the number of times "<GREET> <NAME>!" is 
 * printed using the option --repeat (or -r for short).
 * @author Daniel Krajzewicz 
 * @copyright (c) Daniel Krajzewicz 2004-2019
 * @license Eclipse Public License v2.0 (EPL v2.0) 
 */
public class Example {
	/** @brief Builds the options container and sets the values from the given options
	 * 
	 * @param args The options given on the command line
	 * @return The built options container
	 * @throws ParserConfigurationException Thrown if the XML-parser could not be built
	 * @throws SAXException Thrown on an XML-parsing error
	 * @throws IOException Thrown if the configuration file could not be opened
	 */
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

	
	/** @brief The main method
	 * 
	 * Builds the options and parses them, prints the given greet string
	 * @param args Command line options
	 */
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
