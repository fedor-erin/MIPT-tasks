package com.netcracker.edu.java.tasks;

import java.util.Arrays;
import java.lang.Double;
import java.lang.String;
import static java.lang.Math.pow;

public class ComplexNumberImpl implements ComplexNumber {
    private double re, im;
    public ComplexNumberImpl(){
        this.re = 0;
        this.im = 0;
    }
    public ComplexNumberImpl(double re, double im){
        this.re = re;
        this.im = im;
    }
    public ComplexNumberImpl(String value){}

    public double getRe(){
        return this.re;
    }
    public double getIm(){
        return this.im;
    }
    public boolean isReal(){
        return (getIm() == 0);
    }
    public void set(double re, double im){
        this.re = re;
        this.im = im;
    }
    public void set(String value) throws NumberFormatException{
        this.re = 0;
        this.im = 0;
        int flag = 0, pos = -1;
        String[] parts = value.split("[+-]");
        for (String s : parts) {
            if (pos != -1) {
                s = value.charAt(pos) + s;  //parse signs before numbers
            } else {
                pos = 0;
                if ("".equals(s)) {
                    continue;
                }
            }
            pos += s.length();
            if (s.lastIndexOf('i') == -1) {
                if (!"+".equals(s) && !"-".equals(s)) {
                    if (flag == 1){
                        throw new NumberFormatException();  //for "2+2" case
                    }
                    this.re = Double.parseDouble(s);
                    flag++;
                }
            }
            else {
                s = s.replace("i","");
                if ("+".equals(s) || "".equals(s)) {
                    this.im = 1;
                } else if ("-".equals(s)) {
                    this.im = -1;
                } else {
                    this.im = Double.parseDouble(s);
                }
            }
        }
    }
    public ComplexNumber copy(){
        ComplexNumber cnCopy = new ComplexNumberImpl();
        cnCopy.set(getRe(),getIm());
        return cnCopy;
    }
    public ComplexNumber clone() throws CloneNotSupportedException{
        return (ComplexNumber) super.clone();
    }
    public String toString(){
        if (getIm() == 0 & getRe() != 0) {
	    return Double.toString(getRe());
	}
        else if (getRe() == 0 & getIm() != 0) {
	    return Double.toString(getIm())+"i";
	}
        else if (getIm() == 0 & getRe() == 0) {
	    return "0.0";
	}
        else {
            return Double.toString(getRe())+"+"+Double.toString(getIm())+"i";
        }
    }
    public boolean equals(Object other){
        if (other == this) {
            return true;
        }
        if (other instanceof ComplexNumber) {
            ComplexNumber ex = (ComplexNumber)other;
            return (ex.getRe()==this.getRe() & ex.getIm()==this.getIm());
        }
        return false;
    }
    public int compareTo(ComplexNumber other){
        double absValue1 = pow(getRe(),2)+pow(getIm(),2);
        double absValue2 = pow(other.getRe(),2)+pow(other.getIm(),2);
        if (absValue1 > absValue2){
            return 1;
        }
        else if (absValue1 < absValue2){
            return -1;
        }
        else {
            return 0;
        }
    }
    public void sort(ComplexNumber[] array){
        Arrays.sort(array);
    }
    public ComplexNumber negate(){
        this.re = -getRe();
        this.im = -getIm();
        return this;
    }
    public ComplexNumber add(ComplexNumber arg2){
        this.re += arg2.getRe();
        this.im += arg2.getIm();
        return this;
    }
    public ComplexNumber multiply(ComplexNumber arg2){
        double multRe;
        double multIm;
        multRe = getRe()*arg2.getRe()-getIm()*arg2.getIm();
        multIm = getIm()*arg2.getRe()+getRe()*arg2.getIm();
        this.re = multRe;
        this.im = multIm;
        return this;
    }
}