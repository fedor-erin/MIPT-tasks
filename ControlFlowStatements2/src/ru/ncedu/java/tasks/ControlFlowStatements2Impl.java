package ru.ncedu.java.tasks;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ControlFlowStatements2Impl implements ControlFlowStatements2 {
    public int getFunctionValue(int x) {
        int f;
        if (x<-2 | x>2){
            f = 2*x;
        }
        else {
            f = -3*x;
        }
        return f;
    }
    public String decodeMark(int mark){
        switch(mark){
            case 1: return "Fail";
            case 2: return "Poor";
            case 3: return "Satisfactory";
            case 4: return "Good";
            case 5: return "Excellent";
            default: return "Error";
        }
    }
    public double[][] initArray(){
        double[][] array = new double [5][8];
        for(int i=0; i<5; i++){
            for(int j=0; j<8; j++){
                array[i][j] = pow(i,4) - sqrt(j);
            }
        }
        return array;
    }
    public double getMaxValue(double[][] array){
        double MAX = array[0][0];
        for(int i=0; i<array.length; i++){
            for(int j=0; j<array[i].length; j++){
                if(array[i][j]>MAX){
                    MAX = array[i][j];
                }
            }
        }
        return MAX;
    }
    public Sportsman calculateSportsman(float P){
        Sportsman classExample = new Sportsman();
        float totalDistance = 0;
        float distance = 10;
        while(totalDistance<=200){
            classExample.addDay(distance);  //главное отличие от CFS1
            totalDistance += distance;
            distance = distance*(1+P/100);
        }
        return classExample;
    }
}