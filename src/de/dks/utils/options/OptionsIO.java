package de.dks.utils.options;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class OptionsIO {
	

	public static boolean parseAndLoad(OptionsCont into, String[] args, String configurationName, boolean continueOnError, boolean acceptUnknown) throws ParserConfigurationException, SAXException, IOException {
	    boolean ok = OptionsParser.parse(into, args, continueOnError);
	    if(ok) into.remarkUnset();
    	if(ok) ok = load(into, configurationName);
	    if(ok) into.remarkUnset();
	    if(ok) ok = OptionsParser.parse(into, args, continueOnError);
	    return ok;
	}


	private static boolean load(OptionsCont into, String configurationName) throws ParserConfigurationException, SAXException, IOException {
	    if(configurationName.length()==0) {
	        return true;
	    }
	    String file = into.getString(configurationName);
	    SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    SAXParser saxParser = spf.newSAXParser();
	    XMLReader xmlReader = saxParser.getXMLReader();
	    xmlReader.setContentHandler(new OptionsSAXHandler(into));
	    xmlReader.parse(file);
	    return true;
	}


}
