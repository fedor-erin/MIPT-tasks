package ru.ncedu.erin.infsys;

import javax.xml.parsers.*; 
import org.w3c.dom.*;
import java.io.FileInputStream;
import java.util.Scanner;

public class FunctionsImpl implements Functions {
    @Override
    public void find(String[] parts, NodeList list) {
        String str = "";
        for (int k = 1; k < parts.length; k++) {
            if (k > 1) {
                str += " ";   //spaces between words
            }
            str += parts[k];    //concat all symbols from line
        }
        for (int i = 0; i < list.getLength(); i++) { 
            Node childNode = list.item(i);
            NamedNodeMap attr = childNode.getAttributes();  //list of attributes
            if (attr != null && attr.item(2).getNodeValue().contains(str)){
                    for (int j = 0; j < attr.getLength(); j++){
                        System.out.print(attr.item(j).getNodeValue() + " ");
                    }
                    System.out.print("\n");
                }
        }
    }

    @Override
    public void edit(String id, String attribute) {
        //to be continued
    }

    @Override
    public void add(String group, String name, String scholarship) {      
        //to be continued
    }

    @Override
    public void delete(String id) {       
        //to be continued
    }
}
