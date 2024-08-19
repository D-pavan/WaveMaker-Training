package com.employeeservices.controllers;

import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.services.EmployeeFileSystemServices;
import com.employeeservices.utils.InputUtil;
import com.employeeservices.utils.OutputUtil;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFileSystemController implements  Controllers{
    private  static  EmployeeFileSystemController employeeFileSystemController;
    private  final EmployeeFileSystemServices employeeFileSystemServices;
    private  final InputUtil inputUtil;
    private  final OutputUtil outputUtil;
    private EmployeeFileSystemController(){
        employeeFileSystemServices=EmployeeFileSystemServices.getInstance();
        inputUtil=InputUtil.getInstance();
        outputUtil=OutputUtil.getInstance();
    }
    public static EmployeeFileSystemController getInstance(){
        if(employeeFileSystemController==null){
            employeeFileSystemController=new EmployeeFileSystemController();
        }
        return employeeFileSystemController;
    }

    @Override
    public void startOperations() {
        try {
            while (true) {
                System.out.println("Enter a number from menu : \n 1.Add Employee \n 2.Update Employee\n 3.Delete Employee\n 4.Display Employees \n 5.Get Employee By Id\n 6.Get Employee by name\n 7.Exit");
                int menuNumber = Integer.parseInt(inputUtil.readString());
                if (menuNumber == 7) {
                    employeeFileSystemServices.close();
                    break;
                }
                switch (menuNumber) {
                    case 1: {
                        System.out.println("Enter the required Details : \n");
                        Employee emp = null;
                        Department dept = null;
                        List<Object> objs = inputUtil.readInput(emp, dept);
                         if(objs!=null) {
                             emp = (Employee) objs.get(0);
                             dept = (Department) objs.get(1);
                             employeeFileSystemServices.addEmployee(emp);
                             System.out.println("Employee Added Successfully..\n");
                         }
                        break;
                    }
                    case 2: {
                        System.out.println("Enter the required Details : \n");
                        System.out.println("Enter the Employee Id to be updated : ");
                        int empId = Integer.parseInt(inputUtil.readString());
                        Employee emp = null;
                        Department dept = null;
                        List<Object> objs = inputUtil.readInput(emp, dept);
                        if(objs!=null) {
                            emp = (Employee) objs.get(0);
                            dept = (Department) objs.get(1);
                            employeeFileSystemServices.updateEmployee(empId, emp);
                        }
                        break;
                    }
                    case 3: {
                        System.out.println("Enter the employee Id to be deleted : ");
                        int empId = Integer.parseInt(inputUtil.readString());
                        Employee emp = employeeFileSystemServices.deleteEmployee(empId);
                        List<Employee> emps = new ArrayList<>();
                        emps.add(emp);
                        System.out.print("Details of deleted Employee : \n");
                        outputUtil.printEmployees(emps);
                        break;
                    }
                    case 4: {
                        System.out.println("Employees Details: ");
                        List<Employee> employees = employeeFileSystemServices.getAllEmployees();
                        if (employees.isEmpty()) {
                            System.out.println("Empty no employees!!");
                            break;
                        }
                        outputUtil.printEmployees(employees);
                        break;
                    }
                    case 5: {
                        System.out.println("Enter the Employee Id :");
                        int empId = Integer.parseInt(inputUtil.readString());


                        Employee e=employeeFileSystemServices.getEmployeeById(empId);
                        if(e!=null){
                            System.out.println("Employee Details : \n");
                            List<Employee> emp = new ArrayList<>();
                            emp.add(e);
                            outputUtil.printEmployees(emp);
                        }
                        break;
                    }
                    case 6: {
                        System.out.println("Enter the employee name :");
                        String name = inputUtil.readString();
                        List<Employee> emps = employeeFileSystemServices.getEmployeeByName(name);
                        if(!emps.isEmpty()) outputUtil.printEmployees(emps);
                        break;
                    }

                    default:
                        System.out.println("Invalid menu option!!!");
                }
            }
        } catch (NumberFormatException  e) {
            System.out.println(e);;
        }
        catch (NullPointerException e){
            System.out.println(e);
        }
    }
}
