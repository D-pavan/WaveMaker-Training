package com.tutorial;
import  java.util.Random;
import java.util.Scanner;
public class NumberGuessing {
    Random random=new Random();
    Scanner scan=new Scanner(System.in);
    private final int RANDOM_NUMBER=random.nextInt(1000);
    public void guessNumber(){
        int number=-1;
        int totalAttempts=0;
        while(number!=RANDOM_NUMBER){
            System.out.println("Enter your number : ");
            number=scan.nextInt();
            if(number==RANDOM_NUMBER){
                System.out.println("your guess correct!!!");
            }
            else if(number<RANDOM_NUMBER){
                if(RANDOM_NUMBER-number>100)
                    System.out.println("your number is too less");
                else
                    System.out.println("increase your number little bit");
            }
            else {
                if(number-RANDOM_NUMBER>100)
                    System.out.println("your number is too greater");
                else
                    System.out.println("reduce your number little bit");
            }
            totalAttempts++;
        }
        System.out.println("Total Attempts Made : "+totalAttempts);
    }
}
