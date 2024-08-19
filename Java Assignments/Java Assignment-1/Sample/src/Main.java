
import com.tutorial.*;

import java.util.HashMap;

public class Main{
    public static void main(String args[]){
        NumberGuessing numberGuessing=new NumberGuessing();
        DataTypes dataTypes=new DataTypes();
        Calculator calculator=new Calculator();
        dataTypes.printDataType();
        System.out.println();
        numberGuessing.guessNumber();
        System.out.println();
        calculator.startCalculator();
        HashMap<Employee,String> map=new HashMap<>();
        Employee e1=new Employee(1,"Eshwar","developer");
        Employee e2=new Employee(2,"Eshwar","developer");

        map.put(e1,"Eshwar");
    }
}

