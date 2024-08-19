package com.oops;

public class Bike extends  Vehicle{
    private String bikeName;
    private int mileage;
    private double budget;
    public Bike(){
        this("Bike","Petrol-Bike","TS 3983","Bullet",24500000.00,35);
    }
    public Bike(String name, String type, String bikeNumber, String bikeName, double budget,int mileage) {
        super(name, type,bikeNumber);
        this.bikeName = bikeName;
        this.budget = budget;
        this.mileage=mileage;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
    @Override
    public  void displayDetails(){
        System.out.println("Bike Details : ");
        System.out.println("Vehicle Name : "+this.getName());
        System.out.println("Vehicle Type : "+this.getType());
        System.out.println("Vehicle Number: "+this.getNumber());
        System.out.println("Bike Name   : "+this.bikeName);
        System.out.println("Bike Budget : "+this.budget);
        System.out.println("Bike mileage: "+this.mileage+"/ltr\n");
    }

    @Override
    public void horn() {
        System.out.println("bike horn....");
    }

    @Override
    public String toString() {
        return "Bike{" +
                "Vehicle Name ='" + bikeName + '\'' +
                "Vehicle Type ='" + bikeName + '\'' +
                "bikeName='" + bikeName + '\'' +
                "bikeName='" + bikeName + '\'' +
                ", mileage=" + mileage +
                ", budget=" + budget +
                '}';
    }
}
