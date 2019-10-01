package de.dks.utils.options;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OptionsSAXHandler extends DefaultHandler {
	private OptionsCont myOptions;
	private String myCurrentName; 
	
	public OptionsSAXHandler(OptionsCont options) {
		myOptions = options;
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		myCurrentName = localName;
	}
    
	public void characters(char[] ch, int start, int length) throws SAXException {
		// https://howtodoinjava.com/xml/sax-parser-read-xml-example/, 15.09.2019
		String value = new String(ch, start, length).trim();
		if(value.length()>0) {
			myOptions.set(myCurrentName, value);
		}
	}

}
