package ru.ncedu.java.tasks;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleXMLImpl implements SimpleXML{
    private String root = "";
    public SimpleXMLImpl() {
    }

@Override
public String createXML(String tagName, String textNode){
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setNamespaceAware(true);
    f.setValidating(true);
    Document doc = null;
    try {
    doc = f.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException ex) { }
    Element element = doc.createElement(tagName);
    element.appendChild(doc.createTextNode(textNode));
    doc.appendChild(element);
    DOMSource source = new DOMSource(doc);
    CharArrayWriter array = new CharArrayWriter();
    StreamResult result = new StreamResult(array);
    TransformerFactory transFactory = TransformerFactory.newInstance();
    Transformer transformer = null;
    try {
        transformer = transFactory.newTransformer();
    } catch (TransformerConfigurationException ex) { }
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
    try {
        transformer.transform(source, result);
    } catch (TransformerException ex) { }
    return result.getWriter().toString();
}

@Override
public String parseRootElement(InputStream xmlStream) throws SAXException {
    SAXParserFactory f = SAXParserFactory.newInstance();
    SAXParser p = null;
    try {
        p = f.newSAXParser();
    } catch (ParserConfigurationException ex) { }
    root = "";
    DefaultHandler handler = new DefaultHandler(){
        
@Override
public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if (root.equals("")){
    root = qName;
    }
}
public void characters(char[] ch, int start, int length) throws SAXException{ }
};
try {
    p.parse(xmlStream, handler);
} catch (IOException ex) {
} catch (SAXParseException e){
throw new SAXException();
}
return root;
}
}