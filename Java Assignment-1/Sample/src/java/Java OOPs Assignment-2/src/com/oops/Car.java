package com.oops;

public class Car extends Vehicle{
    private  String carName;
    private  double carBudget;
    private  int mileage;
    public Car(){
        this("Car","Petrol-vehicle","Tesla",9890909.00,"TS 4023",30);
    }
    public Car(String name, String type, String carName, double carBudget, String carNumber,int mileage) {
        super(name, type,carNumber);
        this.carName = carName;
        this.carBudget = carBudget;
        this.mileage=mileage;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public double getCarBudget() {
        return carBudget;
    }

    public void setCarBudget(double carBudget) {
        this.carBudget = carBudget;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    @Override
    public void horn() {
        System.out.println("car horn...");
    }

    @Override
    public void displayDetails(){
        System.out.println("Car Details \n");
        System.out.println("Vehicle Name : "+this.getName());
        System.out.println("Vehicle Type : "+this.getType());
        System.out.println("Vehicle Number: "+this.getNumber());
        System.out.println("Car Name     : "+this.getCarName());
        System.out.println("Car Budget   : "+this.getCarBudget());
        System.out.println("Car mileage  : "+this.mileage+"\n");
    }

    @Override
    public String toString() {
        return "Car{" +
                "carBudget=" + carBudget +
                ", carName='" + carName + '\'' +
                ", carNumber='" + super.getNumber() + '\'' +
                ", mileage='" + this.mileage + '\'' +
                ", type='" + super.getType() + '\'' +
                ", name='" + super.getName() + '\'' +
                '}';
    }


}
