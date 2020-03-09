package com.netcracker.edu.java.tasks;

public class ZeroTaskImpl implements ZeroTask {
	private int integerValue = 0;
	public void setIntegerValue(int value){
            integerValue = value;
        }	
	public double getDoubleValue(){
            return integerValue;
        }
}