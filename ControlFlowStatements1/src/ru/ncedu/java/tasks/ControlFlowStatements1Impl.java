package ru.ncedu.java.tasks;

import static java.lang.Math.sin;

public class ControlFlowStatements1Impl implements ControlFlowStatements1{
	public float getFunctionValue(float x){
            float f;
            if (x>0){
                f = (float) (2*sin(x));
            }
            else {
                f = 6-x;
            }
            return f;
        }
	public String decodeWeekday(int weekday){
            switch(weekday){
                case 1: return "Monday";
                case 2: return "Tuesday";
                case 3: return "Wednesday";
                case 4: return "Thursday";
                case 5: return "Friday";
                case 6: return "Saturday";
                case 7: return "Sunday";
            }
            return null;
        }
	public int[][] initArray(){
            int[][] array = new int [8][5];
            for (int i=0; i<8; i++){
                for (int j=0; j<5; j++)
                array[i][j] = i*j;
            }
            return array;
        }
	public int getMinValue(int[][] array){
            int MIN = array[0][0];
            for (int i=0; i<array.length; i++){
                for (int j=0; j<array[i].length; j++){
                    if (array[i][j] < MIN){
                    MIN = array[i][j];
                    }
                }
            }
            return MIN;
        }
	public BankDeposit calculateBankDeposit(double P){
            BankDeposit classExample = new BankDeposit();
            classExample.years = 0;
            classExample.amount = 1000;
            while (classExample.amount <= 5000){
            classExample.amount = classExample.amount*(1+P/100);
            classExample.years++;
            }
            return classExample;
        }
}