package ru.ncedu.java.tasks;

import javax.xml.xpath.*;
import org.w3c.dom.*;

public class XPathCallerImpl implements XPathCaller{

private Element[] toElementArray(NodeList list){
    Element[] elements = new Element[list.getLength()];
    for (int i = 0; i < elements.length; ++i){
        if (list.item(i).getNodeType() == Node.ELEMENT_NODE){
            Element temp = (Element)list.item(i);
            NodeList tempList = temp.getChildNodes();
            for (int j = 0; j < tempList.getLength(); ++j){
                if(tempList.item(j).getNodeType() == Node.TEXT_NODE){
                    temp.appendChild(tempList.item(j));
                }
            }
            elements[i] = temp;
        }
    }
    return elements;
}

@Override
public Element[] getEmployees(Document src, String deptno, String docType) {
    String expression = ".//employee[@deptno='" + deptno + "']";
    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();
    XPathExpression expr = null;
    try {
        expr = xpath.compile(expression);
    } catch (XPathExpressionException ex) { }
    NodeList nodes = null;
    try {
        nodes = (NodeList) expr.evaluate(src, XPathConstants.NODESET);
    } catch (XPathExpressionException ex) { }
    return toElementArray(nodes);
}

@Override
public String getHighestPayed(Document src, String docType) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression = ".//employee[sal = (//sal[not(. < //sal)])[1]]";
    Node node = null;
    try {
        node = (Node) xpath.compile(expression).evaluate(src, XPathConstants.NODE);
    } catch (XPathExpressionException e) { }
    return ((Element) node).getElementsByTagName("ename").item(0).getTextContent();
}

@Override
public String getHighestPayed(Document src, String deptno, String docType) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression = ".//employee[@deptno = '"+deptno+"' and sal = (//employee[@deptno = '"+deptno+"']/sal[not(. < //employee[@deptno = '"+deptno+"']/sal)])[1]]";
    Node node = null;
    try {
        node = (Node) xpath.compile(expression).evaluate(src, XPathConstants.NODE);
    } catch (XPathExpressionException e) { }
    return ((Element) node).getElementsByTagName("ename").item(0).getTextContent();
}


@Override
public Element[] getTopManagement(Document src, String docType) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression = null;
    if (docType.equals("emp-hier")){
        expression = "//employee[count(ancestor::*) = 0]";
    } else {
        expression = "//employee[not(@mgr)]";
    }
    NodeList nodeList = null;
    try {
        nodeList = (NodeList) xpath.compile(expression).evaluate(src, XPathConstants.NODESET);
    } catch (XPathExpressionException e) { }
    return toElementArray(nodeList);
}

@Override
public Element[] getOrdinaryEmployees(Document src, String docType) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression = null;
    if(docType.equals("emp-hier")){
        expression = "//employee[not(./employee)]";
    } else{
        expression = "//employee[not(@empno = (//@mgr))]";
    }
    NodeList nodeList = null;
    try {
        nodeList = (NodeList) xpath.compile(expression).evaluate(src, XPathConstants.NODESET);
    } catch (XPathExpressionException e) { }
    return toElementArray(nodeList);
}

@Override
public Element[] getCoworkers(Document src, String empno, String docType) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression;
    if(docType.equals("emp-hier")){
        expression = "//employee[@empno = '" + empno + "']/ancestor::*[1]/child::employee[@empno != '" + empno + "']";
    } else {
        expression = "//employee[@mgr = //employee[@empno = //employee[@empno = '" + empno + "']/@mgr]/@empno and @empno != '" + empno + "']";
    }
    NodeList nodeList = null;
    try {
        nodeList = (NodeList) xpath.compile(expression).evaluate(src, XPathConstants.NODESET);
    }catch (XPathExpressionException e) { }
    return toElementArray(nodeList);
}
}