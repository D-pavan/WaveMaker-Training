
import com.oops.*;
public class Main{
    public static void main(String args[]){
        Vehicle bike=new Bike();
        Vehicle car=new Car();
        ElectricVehicle electricCar=new ElectricCar();
        ElectricVehicle electricBike=new ElectricBike();
        bike.displayDetails();
        bike.horn();
        car.displayDetails();
        car.horn();
        electricCar.displayDetails();
        electricCar.horn();
        electricBike.displayDetails();
        electricBike.horn();
        electricBike.charge();
    }
}