package com.oops;

public  class ElectricCar extends  ElectricVehicle{
    private  String carName;
    private  double carBudget;
    private  int hoursToCharge;
    private  int mileage;
    public ElectricCar(){
        this("Car","Electric-vehicle","Honda",9890909.00,"TS 4023",140,3);
    }
    public ElectricCar(String name, String type, String carName, double carBudget, String carNumber,int mileage,int hoursToCharge) {
        super(name, type,carNumber);
        this.carName = carName;
        this.carBudget = carBudget;
        this.mileage=mileage;
        this.hoursToCharge=hoursToCharge;
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

    public int getHoursToCharge() {
        return hoursToCharge;
    }

    public void setHoursToCharge(int hoursToCharge) {
        this.hoursToCharge = hoursToCharge;
    }

    @Override
    public void displayDetails(){
        System.out.println("Electric Car Details \n");
        System.out.println("Vehicle Name : "+this.getName());
        System.out.println("Vehicle Type : "+this.getType());
        System.out.println("Vehicle Number: "+this.getNumber());
        System.out.println("Car Name     : "+this.getCarName());
        System.out.println("Car Budget   : "+this.getCarBudget());
        System.out.println("Car mileage  : "+this.getMileage());
        System.out.println("Hours To charge: "+this.getHoursToCharge()+"\n");
    }
    @Override
    public  void horn(){
        System.out.println("Electric car horn...");
    }

    @Override
    public String toString() {
        return "Car{" +
                "carBudget=" + carBudget +
                ", carName='" + carName + '\'' +
                ", carNumber='" + super.getNumber() + '\'' +
                ", hoursToCharge='" + this.hoursToCharge + '\'' +
                ", mileage='" + this.mileage + '\'' +
                ", type='" + super.getType() + '\'' +
                ", name='" + super.getName() + '\'' +
                '}';
    }

}
