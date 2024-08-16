package com.oops;

public class ThisDemo {
    private String name;
    public ThisDemo(){
        this("Wavemaker");
    }
    public ThisDemo(String name){
        this.name=name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void displayName(){
        System.out.println("Name : "+this.getName());
    }
}
