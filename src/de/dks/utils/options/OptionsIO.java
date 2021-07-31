package de.dks.utils.options;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @class OptionsIO
 * @brief Static helper methods for parsing and loading of options.
 * @author Daniel Krajzewicz 
 * @copyright (c) Daniel Krajzewicz 2004-2021
 * @license Eclipse Public License v2.0 (EPL v2.0) 
 */
public class OptionsIO {
    /** @brief Parses options from the command line and optionally loads options from a configuration file
     * @param[in] into The options container to fill
     * @param[in] args The arguments given on the command line
     * @param[in] configOptionName The path to the configuration to load (XML)
     * @param[in] continueOnError Continues even if an error occures while parsing
     * @param[in] acceptUnknown Unknown options do not throw an exception
     * @todo continueOnError is not used
     * @todo acceptUnknown is not used
     * @throws ParserConfigurationException Thrown if the XML-parser could not be built
     * @throws SAXException Thrown on an XML-parsing error
     * @throws IOException Thrown if the configuration file could not be opened
     */
    public static boolean parseAndLoad(OptionsCont into, String[] args, String configOptionName, boolean continueOnError, boolean acceptUnknown) throws ParserConfigurationException, SAXException, IOException {
        boolean ok = OptionsParser.parse(into, args, continueOnError);
        if(ok) into.remarkUnset();
        if(configOptionName!=null && !"".equals(configOptionName) && into.isSet(configOptionName)) {
            if(ok) ok = load(into, configOptionName);
            if(ok) into.remarkUnset();
            if(ok) ok = OptionsParser.parse(into, args, continueOnError);
        }
        return ok;
    }


    /** @brief Loads options from a configuration file
     * @param[in] into The options container to fill
     * @param[in] configOptionName The path to the configuration to load (XML)
     * @throws ParserConfigurationException Thrown if the XML-parser could not be built
     * @throws SAXException Thrown on an XML-parsing error
     * @throws IOException Thrown if the configuration file could not be opened
     */
    private static boolean load(OptionsCont into, String configOptionName) throws ParserConfigurationException, SAXException, IOException {
        if(configOptionName.length()==0) {
            return true;
        }
        String file = into.getString(configOptionName);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(new OptionsSAXHandler(into));
        xmlReader.parse(file);
        return true;
    }
    
    
    /** @brief Writes the set options as an XML configuration file
     * 
     * @param configName The name of the file to write the configuration to
     * @param options The options container that includes the (set/parsed) options to write 
     * @throws IOException If the file cannot be written
     */
    public static void writeXMLConfiguration(String configName, OptionsCont options) throws IOException {
    	Vector<String> optionNames = options.getSortedOptionNames();
    	FileWriter fileWriter = new FileWriter(configName);
    	fileWriter.append("<configuration>\n");
    	for(Iterator<String> i=optionNames.iterator(); i.hasNext(); ) {
    		String oName = i.next();
    		if(options.isSet(oName) && !options.isDefault(oName)) {
    			fileWriter.append("   <"+oName+">"+options.getValueAsString(oName)+"</"+oName+">\n");
    		}
    	}
    	fileWriter.append("</configuration>\n");
    	fileWriter.close();
    }

    
    /** @brief Writes the a template for a configuration file
     * 
     * @param configName The name of the file to write the template to
     * @param options The options container to write a template for 
     * @throws IOException If the file cannot be written
     */
    public static void writeXMLTemplate(String configName, OptionsCont options) throws IOException {
    	Vector<String> optionNames = options.getSortedOptionNames();
    	FileWriter fileWriter = new FileWriter(configName);
    	fileWriter.append("<configuration>\n");
    	for(Iterator<String> i=optionNames.iterator(); i.hasNext(); ) {
    		String oName = i.next();
   			fileWriter.append("   <"+oName+"></"+oName+">\n");
    	}
    	fileWriter.append("</configuration>\n");
    	fileWriter.close();
    }

    
    /** @brief Output operator
     * @param[in] os The output container to write
     * @param[in] options The options to print
     */
    public static void printSetOptions(PrintStream os, OptionsCont options, boolean includeSynonyms, boolean shortestFirst, boolean skipDefault) {
    	Vector<String> optionNames = options.getSortedOptionNames();
        for(Iterator<String> i=optionNames.iterator(); i.hasNext(); ) { 
            String name = i.next();
            if(!options.isSet(name)) {
                continue;
            }
            if(skipDefault && options.isDefault(name)) {
                continue;
            }
            Vector<String> synonyms = options.getSynonyms(name);
            if(shortestFirst) {
            	Collections.reverse(synonyms);
            }
            Iterator<String> j=synonyms.iterator();
            String first = j.next();
            os.print(first);
            if(includeSynonyms) {
                if(j.hasNext()) {
                    os.print(" (");
                    for(; j.hasNext(); ) {
                        String name2 = j.next();
                        os.print(name2);
                        if(j.hasNext()) {
                            os.print(", ");
                        }
                    }
                    os.print(")");
                }
            }
            os.print(": " + options.getValueAsString(name));
            if(options.isDefault(name)) {
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
     * @param[in] options The options to print
     * @param[in] maxWidth The maximum width of a line
     * @param[in] optionIndent The indent to use before writing an option
     * @param[in] divider The space between the option name and the description
     * @param[in] sectionIndent The indent to use before writing a section name
     */
    public static void printHelp(PrintStream os, OptionsCont options, int maxWidth, int optionIndent, int divider, int sectionIndent) {
    	Vector<String> optionNames = options.getSortedOptionNames();
    	String helpHead = options.getHelpHead();
    	String helpTail = options.getHelpTail();
        // compute needed width
        int optMaxWidth = 0;
        for(Iterator<String> i=optionNames.iterator(); i.hasNext(); ) {
            String name = i.next();
            String optNames = getHelpFormattedSynonyms(options, name);
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
        if(helpHead!=null) {
            os.println(helpHead);
        }
        String lastSection = "";
        for(Iterator<String> i=optionNames.iterator(); i.hasNext(); ) {
        	String name = i.next();
            // check whether a new section starts
            String optSection = options.getSection(name);
            if(optSection!=null&&!"".contentEquals(optSection)&&lastSection!=optSection) {
                lastSection = optSection;
                os.println(sectionIndentSting+lastSection);
            }
            // write the option
            String optNames = getHelpFormattedSynonyms(options, name);
            // write the divider
            os.print(optionIndentSting+optNames);
            int owidth = optNames.length();
            // write the description
            int beg = 0;
            String desc = options.getDescription(name);
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
                    int end = desc.lastIndexOf(' ', beg+maxWidth-startCol);
                    os.println(desc.substring(beg, end));
                    beg = end;
                }
                startCol = divider+optMaxWidth+optionIndent+1; // could "running description indent"
                offset = startCol;
            }
            os.println();
        }
        if(helpTail!=null) {
            os.println(helpTail);
        }
    }

    


    /** @brief Returns the synomymes of an option as a help-formatted string 
     *
     * The synomymes are sorted by length.
     * @param[in] options The options container to get information from
     * @param[in] optionName The name of option to get the synonyms help string for
     * @return The options as a help-formatted string
     */
    private static String getHelpFormattedSynonyms(OptionsCont options, String optionName) {
        Vector<String> synonyms = options.getSynonyms(optionName);
        Collections.sort(synonyms, options.new SortByLengthComparator());
        StringBuffer sb = new StringBuffer();
        for(Iterator<String> i=synonyms.iterator(); i.hasNext(); ) {
            String name2 = i.next();
            // consider the - / --
            if(name2.length()==1) {
                sb.append('-');
            } else {
                sb.append("--");
            }
            sb.append(name2);
            if(i.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
}
