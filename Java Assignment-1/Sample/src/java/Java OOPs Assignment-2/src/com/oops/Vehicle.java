package com.oops;

public abstract  class Vehicle {
    private String name,type,number;
    public Vehicle() {
        this("Bike", "Petrol-Vehicle","TS 8080");
    }
    public Vehicle(String name,String type,String number){
        this.name=name;
        this.type=type;
        this.number=number;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public abstract void displayDetails();
    public abstract void horn();
}
