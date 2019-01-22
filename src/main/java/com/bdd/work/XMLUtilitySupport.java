package com.bdd.work;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XMLUtilitySupport {

	static final Logger logger = Logger.getLogger(ResultsAssertions.class);

	public XMLUtilitySupport() {
		
	}
	public Object getValueFromResponse(String xmlStr, String xpathExpression) {
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new java.io.StringReader(xmlStr));
			//Document doc = builder.parse(new InputSource(new StringRader(xmlStr)));
			Document doc = builder.parse(inputSource);
			
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			return xPath.evaluate(xpathExpression, doc, XPathConstants.NODESET);
			
			
		} catch (Exception val5) {	
			logger.info("Exception - " + val5);
		}
		return "";
		
		
	}
}
