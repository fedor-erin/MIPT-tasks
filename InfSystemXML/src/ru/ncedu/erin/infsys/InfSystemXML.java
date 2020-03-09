package ru.ncedu.erin.infsys;

import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Scanner;

public class InfSystemXML {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream("C:\\Users\\Fedor\\Desktop\\students.xml"));
        Element root = doc.getDocumentElement();
        System.out.print(root.getAttribute("name") + ":\n");
        System.out.print("ID, Group, Name, Scholarship\n");
        NodeList list = root.getChildNodes();  //list of students
        for (int i = 0; i < list.getLength(); i++) { 
            Node childNode = list.item(i);
            NamedNodeMap attr = childNode.getAttributes();  //list of attributes
            if (attr != null) {
                for (int j = 0; j < attr.getLength(); j++){
                    System.out.print(attr.item(j).getNodeValue() + " ");
                }
                System.out.print("\n");
            }
        }
        System.out.println("Avaiable commands: find. Which one?");
        FunctionsImpl func = new FunctionsImpl();
        Scanner sc = new Scanner(System.in);
        String[] parts = sc.nextLine().split("[ ]");
        switch (parts[0]){
            case "find": func.find(parts, list);
                break;
            default: System.out.print("Wrong command, dude.");
        }
    }
}