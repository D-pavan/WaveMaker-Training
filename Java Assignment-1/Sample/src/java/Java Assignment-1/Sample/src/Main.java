
import com.tutorial.*;
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
    }
}

