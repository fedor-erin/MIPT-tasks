package ru.ncedu.java.tasks;

import java.lang.Math;
import java.util.Arrays;
        
public class ArrayVectorImpl implements ArrayVector {
    private double[] vector;
    public ArrayVectorImpl(){
    }
    public void set(double... elements){
        vector = elements;
    }
    public double[] get(){
        return vector;
    }
    public ArrayVector clone(){
        ArrayVector vectorCopy = new ArrayVectorImpl();
        vectorCopy.set(get().clone());
        return vectorCopy;
    }
    public int getSize(){
        return vector.length;
    }
    public void set(int index, double value){
        if ((index >= 0) & (index < getSize())){
            get()[index] = value;
        }
        else if (index >= getSize()){
            this.vector = Arrays.copyOf(get(), index+1);
            get()[index] = value;
        }
    }
    public double get(int index) throws ArrayIndexOutOfBoundsException{
        try {
            return vector[index];
        } catch (ArrayIndexOutOfBoundsException e){
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    public double getMax(){
        double MAX = get()[0];
        for (int i=0; i<getSize(); i++){
            if (get()[i] > MAX){
                MAX = get()[i];
            }
        }
        return MAX;
    }
    public double getMin(){
        double MIN = get()[0];
        for (int i=0; i<getSize(); i++){
            if (get()[i] < MIN){
                MIN = get()[i];
            }
        }
        return MIN;
    }
    public void sortAscending(){
        Arrays.sort(get());
    }
    public void mult(double factor){
        for (int i=0; i<getSize(); i++){
            get()[i] *= factor;
        }
    }
    public ArrayVector sum(ArrayVector anotherVector){
        int lenght1 = getSize();
        int lenght2 = anotherVector.getSize();
        if (lenght1 < lenght2){
            for (int i=0; i<lenght1; i++){
            get()[i] += anotherVector.get()[i];
            }
        }
        else {
            for (int i=0; i<lenght2; i++){
            get()[i] += anotherVector.get()[i];
            }
        }
        return this;
    }
    public double scalarMult(ArrayVector anotherVector){
        int lenght1 = getSize();
        int lenght2 = anotherVector.getSize();
        double mult = 0;
        if (lenght1 < lenght2){
            for (int i=0; i<lenght1; i++){
            mult += get()[i]*anotherVector.get()[i];
            }
        }
        else {
            for (int i=0; i<lenght2; i++){
            mult += get()[i]*anotherVector.get()[i];
            }
        }
        return mult;
    }
    public double getNorm(){
        double norm;
        norm = Math.sqrt(scalarMult(this));
        return norm;
    }
}