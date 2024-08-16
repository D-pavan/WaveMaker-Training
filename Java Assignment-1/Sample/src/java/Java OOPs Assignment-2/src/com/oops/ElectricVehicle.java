package com.oops;
public abstract  class ElectricVehicle extends  Vehicle implements Chargable{
    public ElectricVehicle(){
        super("Bike","Electric","TS 1030");
    }
    public ElectricVehicle(String name,String type,String number){
        super(name,type,number);
    }
    @Override
    public void charge() {
        System.out.println("charging....");
    }

}
