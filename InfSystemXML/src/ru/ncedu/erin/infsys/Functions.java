package ru.ncedu.erin.infsys;

import org.w3c.dom.NodeList;

/**
 * @author Fedor
 */
public interface Functions {
    /** It searches the given string within the name of one of the elements.
     * @param parts The string array for search.
     * @param list The list of elements.
    */
    void find(String[] parts, NodeList list);
    /**
     * It edits the given id's attribute of one of the elements.
     * @param id Element's id.
     * @param attribute Element's attribute.
     */
    void edit(String id, String attribute);
    /**
     * It adds a new element to root node.
     * @param group Element's attribute.
     * @param name Element's attribute.
     * @param scholarship Element's attribute.
     */
    void add(String group, String name, String scholarship);
    /**
     * It deletes a node with given id.
     * @param id Element's id.
     */
    void delete(String id);
}