package com.employeeservices;
import com.employeeservices.controllers.CLIController;
public class App 
{
    public static void main( String[] args )
    {
        CLIController cliController= CLIController.getInstance();
        cliController.startOperations();
    }
}
