package com.oops;

public class ElectricBike extends ElectricVehicle {

    private String bikeName;
    private int mileage,hoursToCharge;
    private double budget;
    public ElectricBike(){
        this("Bike","Electric-Bike","TS 3983","Shine",750000.00,120,2);
    }
    public ElectricBike(String name, String type, String bikeNumber, String bikeName, double budget,int mileage,int hoursToCharge) {
        super(name,type,bikeNumber);
        this.bikeName = bikeName;
        this.budget = budget;
        this.mileage=mileage;
        this.hoursToCharge=hoursToCharge;
    }

    public int getHoursToCharge() {
        return hoursToCharge;
    }

    public void setHoursToCharge(int hoursToCharge) {
        this.hoursToCharge = hoursToCharge;
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
    public void horn() {
        System.out.println("Electric bike horn....\n");
    }

    @Override
    public  void displayDetails(){
        System.out.println("Electric Bike Details : ");
        System.out.println("Vehicle Name : "+this.getName());
        System.out.println("Vehicle Type : "+this.getType());
        System.out.println("Vehicle Number: "+this.getNumber());
        System.out.println("Bike Name   : "+this.getBikeName());
        System.out.println("Bike Budget : "+this.getBudget());
        System.out.println("Hour required to charge : "+this.getHoursToCharge());
        System.out.println("Total kilometer can travel : "+this.getMileage()+"\n");
    }

    @Override
    public String toString() {
        return "Bike{" +
                "Vehicle Name ='" + bikeName + '\'' +
                "Vehicle Type ='" + bikeName + '\'' +
                "bikeName='" + bikeName + '\'' +
                "hoursToCharge='" + hoursToCharge + '\'' +
                ", mileage=" + mileage +
                ", budget=" + budget +
                '}';
    }
}
