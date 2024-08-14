package com.tutorial;



public class DataTypes {
    private byte byteVar=1;
    private short shorVar=10;
    private  int intVar=1000;
    private  long longVar=100000020;
    private boolean bool=true;
    private char charVar='a';
    private float decimalVar=5.003f;
    private double dDoubleVar=1000.200;
    private String string="wavemaker";
    public  void printDataType(){
        System.out.println("Data Types\n");
        System.out.println("byte: "+byteVar);
        System.out.println("shor: "+shorVar);
        System.out.println("int: "+intVar);
        System.out.println("long: "+longVar);
        System.out.println("boolean: "+bool);
        System.out.println("char: "+charVar);
        System.out.println("float: "+decimalVar);
        System.out.println("Double: "+dDoubleVar);
        System.out.println("string: "+string);
    }
}
