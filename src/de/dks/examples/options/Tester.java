package de.dks.examples.options;

import java.io.BufferedReader;
import java.io.File;
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
import de.dks.utils.options.OptionsFileIO_CSV;
import de.dks.utils.options.OptionsFileIO_XML;
import de.dks.utils.options.OptionsIO;
import de.dks.utils.options.OptionsTypedFileIO;

/**
 * @class Tester
 * @brief Used for testing the library in conjunction with TextTest.
 *  
 * The tester application is just for internal testing purposes. 
 * It reads a definitions file called "options.txt" which includes 
 * definitions of options and other things to set up the options container. 
 * Then the application performs the things defined by the 
 * http://texttest.sourceforge.net/ test system.
 * 
 * @author Daniel Krajzewicz (daniel@krajzewicz.de)
 * @copyright Eclipse Public License v2.0 (EPL v2.0), (c) Daniel Krajzewicz 2019-2021
 */
public class Tester {
    /// @brief The (optional) configuration file name
    private static String configOptionName = "";
    
    /// @brief The (optional) configuration reader
    private static OptionsTypedFileIO fileIO = null;
    
    
    /** @brief Loads the definition for setting up options
     * 
     * @return The filled options container
     * @throws IOException If the options definitions file could not be loaded
     */
    private static OptionsCont loadDefinition() throws IOException {
        OptionsCont options = new OptionsCont();
        File f = new File("options.txt");
        if(!f.exists()) {
            return options;
        }
        BufferedReader in = new BufferedReader(new FileReader("options.txt"));
        if(!in.ready()) {
            in.close();
            return options;
        }
        while(in.ready()) {
            String line = in.readLine().trim();
            if(line.length()==0) {
                continue;
            }
            if(line.indexOf(';')<-1) {
                // ok, it's a subsection
            } else {
                // ok, it's an option
                // ... parse the line
                Vector<String> synonyms = new Vector<>();
                char abbr = '!';
                String type=null, description=null, defaultValue=null;
                StringTokenizer st = new StringTokenizer(line, ";");
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    if(type==null) {
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
                        synonyms.add(s);
                    }
                }
                // ... is it the tail/head help stuff?
                if("HELPHEADTAIL".equals(type)) {
                    options.setHelpHeadAndTail(synonyms.elementAt(0), synonyms.elementAt(1));
                    continue;
                }
                if("CONFIG".equals(type)) {
                    configOptionName = synonyms.elementAt(0);
                    if(configOptionName.startsWith("xml")) {
                    	fileIO = new OptionsFileIO_XML();
                    } else if(configOptionName.startsWith("csv")) {
                    	fileIO = new OptionsFileIO_CSV();
                    } else {
                    	in.close();
                    	throw new IOException("Unknown configuration format");
                    }
                    continue;
                }
                // ... is it a named section begin?
                if("SECTION".equals(type)) {
                    options.beginSection(synonyms.elementAt(0));
                    continue;
                }
                // ... build the option, first
                Option option = null;
                if("INT".equals(type)) {
                    if(defaultValue==null) {
                        option = new Option_Integer();
                    } else {
                        int v = Integer.parseInt(defaultValue);
                        option = new Option_Integer(v);
                    }
                } else if("DOUBLE".equals(type)) {
                    if(defaultValue==null) {
                        option = new Option_Double();
                    } else {
                        double v = Double.parseDouble(defaultValue);
                        option = new Option_Double(v);
                    }
                } else if("BOOL".equals(type)) {
                    if(defaultValue==null) {
                        option = new Option_Bool();
                    } else {
                        option = new Option_Bool();
                    }
                } else if("STRING".equals(type)) {
                    if(defaultValue==null) {
                        option = new Option_String();
                    } else {
                        option = new Option_String(defaultValue);
                    }
                } else if("FILE".equals(type)) {
                    if(defaultValue==null) {
                        option = new Option_FileName();
                    } else {
                        option = new Option_FileName(defaultValue);
                    }
                }
                // ... add it to the container
                String firstName = synonyms.elementAt(0);
                synonyms.remove(0);
                if(abbr!='!') {
                    options.add(firstName, abbr, option);
                } else {
                    options.add(firstName, option);
                }
                while(synonyms.size()!=0) {
                    options.addSynonym(firstName, synonyms.elementAt(0));
                    synonyms.remove(0);
                }
                // ... add description if given
                if(description!=null) {
                    options.setDescription(firstName, description);
                }
            }
        }
        in.close();
        return options;
    }    

    
    /** @brief The main method
     * 
     * Loads the definition of options, first. Parses them and prints the help string and the set options.
     * @param args The command line options
     */
    public static void main(String[] args) {
        try {
            // load the definition
            OptionsCont options = loadDefinition();
            // parse options
            if(OptionsIO.parseAndLoad(options, args, fileIO, configOptionName, false, false)) {
            	OptionsIO.printHelp(System.out, options, 80, 2, 2, 1, 1);
                System.out.println("-------------------------------------------------------------------------------");
                OptionsIO.printSetOptions(System.out, options, true, false, false);
                System.out.println("-------------------------------------------------------------------------------");
            }
        } catch(Exception e) {
            System.err.println(e.toString());
            System.err.println("Quitting (on error).");
        }
    }

}
