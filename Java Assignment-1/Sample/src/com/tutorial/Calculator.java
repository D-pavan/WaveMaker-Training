package com.tutorial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

public class Calculator {
    Scanner scan=new Scanner(System.in);
    public void startCalculator(){

        while(true) {
            System.out.println("Enter a value from menu : \n 1.Addition \n 2.Subtraction\n 3.Multiplication\n 4.Division\n 5.exit");
            int n= scan.nextInt();
            switch (n){
                case 1: {
                    System.out.println("Enter first value: ");
                    BigInteger operand1 = new BigInteger(scan.next());
                    System.out.println("Enter second value: ");
                    BigInteger operand2 = new BigInteger(scan.next());
                    BigInteger answer = operand1.add(operand2);
                    System.out.println("Output: " + answer.toString());
                    break;
                }
                case 2: {
                    System.out.println("Enter first value: ");
                    BigInteger operand1 = new BigInteger(scan.next());
                    System.out.println("Enter second value: ");
                    BigInteger operand2 = new BigInteger(scan.next());
                    BigInteger answer = operand1.subtract(operand2);
                    System.out.println("Output: " + answer.toString());
                    break;
                }
                case 3:{
                    System.out.println("Enter first value: ");
                    BigInteger operand1 = new BigInteger(scan.next());
                    System.out.println("Enter second value: ");
                    BigInteger operand2 = new BigInteger(scan.next());
                    BigInteger answer = operand1.multiply(operand2);
                    System.out.println("Output: " + answer.toString());
                    break;
                }
                case 4: {
                    System.out.println("Enter first value: ");
                    BigDecimal operand1 = new BigDecimal(scan.next());
                    System.out.println("Enter second value: ");
                    BigDecimal operand2 = new BigDecimal(scan.next());
                    BigDecimal answer = operand1.divide(operand2, RoundingMode.FLOOR);
                    System.out.println("Output: " + answer.toString());
                    break;
                }
                case 5: System.exit(0);
                default:System.out.println("enter correct number from menu!!!");
            }
        }
    }
}
