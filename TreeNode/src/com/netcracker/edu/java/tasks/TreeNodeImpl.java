package com.netcracker.edu.java.tasks;

import java.util.Iterator;
import java.util.ArrayList;

public class TreeNodeImpl implements TreeNode {
    private Object data;
    private ArrayList<TreeNode> children;
    private TreeNode parent;
    private boolean expanded;
    public TreeNodeImpl(){
        super();    //is it really need?
    }
    public TreeNode getParent() {
        return parent;
    }
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
    public TreeNode getRoot() {
        TreeNode current = this;
        TreeNode another = current.getParent();
        if(another == null){
            return null;
        }
        while (another != null){
            current = another;
            another = current.getParent();
        }
        return current;
    }
    public boolean isLeaf() {
        return (children == null || children.isEmpty());
    }
    public int getChildCount() {
        return children.size();
    }
    public Iterator<TreeNode> getChildrenIterator() {
        return children.iterator();
    }
    public void addChild(TreeNode child) {
        if (children == null){
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);
    }
    public boolean removeChild(TreeNode child) {
        if(children.remove(child)){
            child.setParent(null);
            return true;
        }
        return false;
    }
    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;  
        for (TreeNode oneOfThese : children){
            oneOfThese.setExpanded(expanded);
        }
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public String getTreePath() {
        TreeNode current = this;
        String s;
        if(current.getData() == null){
            s = "empty";
        }
        else{
            s = getData().toString();
        }
        while(current.getParent() != null){
            if(current.getParent().getData() == null){
                s = "empty->" + s;
            }
            else{
                s = current.getParent().getData().toString() + "->" + s;
            }
            current = current.getParent();
        }
        return s;
    }
    public TreeNode findParent(Object data) {
        if (data == null && getData() == null){
            return this;
        }
        if (getData() != null && data != null && getData().equals(data)){
            return this;
        }
        if (getParent() == null || getParent() == this){
            return null;
        }
        return getParent().findParent(data);
    }
    public TreeNode findChild(Object data) {
        if (children == null){
            return null;
        }
        for (TreeNode oneOfThese : children) {
            if (oneOfThese == null){
                continue;
            }
            if (data == null && oneOfThese.getData() == null){
                return oneOfThese;
            }
            if (data != null && oneOfThese.getData() != null && oneOfThese.getData().equals(data)){
                return oneOfThese;
            }
            TreeNode temp = oneOfThese.findChild(data);
            if (temp != null){
                return temp;
            }
        }
        return null;
    }
}