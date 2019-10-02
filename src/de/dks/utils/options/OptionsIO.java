package de.dks.utils.options;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @class OptionsIO
 * @brief Static helper methods for parsing and loading of options.
 * @author Daniel Krajzewicz 
 * @copyright (c) Daniel Krajzewicz 2004-2019
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
        if(configOptionName!=null && !"".equals(configOptionName)) {
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

}
