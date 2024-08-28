package com.employeeservices.controllers;

import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.services.impl.EmployeeDatabaseServices;
import com.employeeservices.services.impl.EmployeeFileSystemServices;
import com.employeeservices.services.impl.EmployeeInMemoryServices;
import com.employeeservices.utils.InputUtil;
import com.employeeservices.utils.OutputUtil;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class CLIController {
    private static CLIController cliController;
    private final InputUtil inputUtil;
    private final OutputUtil outputUtil;
    private final EmployeeInMemoryServices employeeInMemoryServices;
    private final EmployeeFileSystemServices employeeFileSystemServices;
    private final EmployeeDatabaseServices employeeDatabaseServices;

    private CLIController() {
        inputUtil = InputUtil.getInstance();
        employeeInMemoryServices = EmployeeInMemoryServices.getInstance();
        employeeFileSystemServices = EmployeeFileSystemServices.getInstance();
        employeeDatabaseServices = EmployeeDatabaseServices.getInstance();
        outputUtil = OutputUtil.getInstance();

    }

    public static synchronized CLIController getInstance() {
        if (cliController == null) {
            cliController = new CLIController();
        }
        return cliController;
    }

    public void startOperations() {
        try {
            while (true) {
                System.out.println("Enter a number from menu : \n  1.InMemory \n  2.FileSystem\n  3.Database\n  4.Exit");
                int menuNumber = Integer.parseInt(inputUtil.readString());
                switch (menuNumber) {
                    case 1:
                        while (true) {
                            try {
                                System.out.println("Enter a number from menu : \n 1.Add Employee \n 2.Update Employee\n 3.Delete Employee\n 4.Display Employees \n 5.Get Employee By Id\n 6.Get Employee by name\n 7.Exit");
                                menuNumber = Integer.parseInt(inputUtil.readString());
                                if (menuNumber == 7) break;
                                switch (menuNumber) {
                                    case 1: {
                                        System.out.println("Enter the required Details : \n");
                                        Employee emp = null;
                                        Department dept = null;
                                        List<Object> objs = inputUtil.readInput(employeeInMemoryServices,false);
                                        if (objs != null) {
                                            emp = (Employee) objs.get(0);
                                            dept = (Department) objs.get(1);
                                            employeeInMemoryServices.addEmployee(emp);
                                        }
                                        break;
                                    }
                                    case 2: {
                                        System.out.println("Enter the required Details : \n");
                                        System.out.println("Enter the Employee Id to be updated : ");
                                        int empId = Integer.parseInt(inputUtil.readString());
                                        Employee emp = null;
                                        Department dept = null;
                                        List<Object> objs = inputUtil.readInput(employeeInMemoryServices,true);
                                        if (objs != null) {
                                            emp = (Employee) objs.get(0);
                                            dept = (Department) objs.get(1);
                                            employeeInMemoryServices.updateEmployee(empId, emp);
                                        }
                                        break;
                                    }
                                    case 3: {
                                        System.out.println("Enter the employee Id to be deleted : ");
                                        int empId = Integer.parseInt(inputUtil.readString());
                                        Employee emp = employeeInMemoryServices.deleteEmployee(empId);
                                        if (emp != null) {
                                            List<Employee> emps = new ArrayList<>();
                                            emps.add(emp);
                                            System.out.print("Details of deleted Employee : \n");
                                            outputUtil.printEmployees(emps);
                                        }
                                        break;
                                    }
                                    case 4: {
                                        System.out.println("Employees Details: \n");
                                        List<Employee> employees = employeeInMemoryServices.getAllEmployees();
                                        if (employees.isEmpty()) {
                                            System.out.println("Empty no employees!!");
                                            break;
                                        } else outputUtil.printEmployees(employees);
                                        break;
                                    }
                                    case 5: {
                                        System.out.println("Enter the Employee Id :");
                                        int empId = Integer.parseInt(inputUtil.readString());
                                        Employee e = employeeInMemoryServices.getEmployeeById(empId);
                                        if (e != null) {
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
                                        List<Employee> emps = employeeInMemoryServices.getEmployeeByName(name);
                                        if (!emps.isEmpty()) outputUtil.printEmployees(emps);
                                        break;
                                    }
                                    default:
                                        System.out.println("Invalid menu option!!!");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Number format invalid enter numerical value: " + e);
                            } catch (NullPointerException | InputMismatchException e) {
                                System.out.println(e);
                            }
                        }

                        break;
                    case 2:
                        while (true) {
                            try {
                                System.out.println("Enter a number from menu : \n 1.Add Employee \n 2.Update Employee\n 3.Delete Employee\n 4.Display Employees \n 5.Get Employee By Id\n 6.Get Employee by name\n 7.Exit");
                                menuNumber = Integer.parseInt(inputUtil.readString());
                                if (menuNumber == 7) {
                                    employeeFileSystemServices.close();
                                    break;
                                }
                                switch (menuNumber) {
                                    case 1: {
                                        System.out.println("Enter the required Details : \n");
                                        Employee emp = null;
                                        Department dept = null;
                                        List<Object> objs = inputUtil.readInput(employeeFileSystemServices,false);
                                        if (objs != null) {
                                            emp = (Employee) objs.get(0);
                                            dept = (Department) objs.get(1);
                                            employeeFileSystemServices.addEmployee(emp);
                                        }
                                        break;
                                    }
                                    case 2: {
                                        System.out.println("Enter the required Details : \n");
                                        System.out.println("Enter the Employee Id to be updated : ");
                                        int empId = Integer.parseInt(inputUtil.readString());
                                        Employee emp = null;
                                        Department dept = null;
                                        List<Object> objs = inputUtil.readInput(employeeFileSystemServices,true);
                                        if (objs != null) {
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
                                        if (emp != null) {
                                            List<Employee> emps = new ArrayList<>();
                                            emps.add(emp);
                                            System.out.print("Details of deleted Employee : \n");
                                            outputUtil.printEmployees(emps);
                                        }
                                        break;
                                    }
                                    case 4: {
                                        System.out.println("Employees Details: ");
                                        List<Employee> employees = employeeFileSystemServices.getAllEmployees();
                                        if (employees.isEmpty()) {
                                            System.out.println("Empty no employees!!");
                                        } else outputUtil.printEmployees(employees);
                                        break;
                                    }
                                    case 5: {
                                        System.out.println("Enter the Employee Id :");
                                        int empId = Integer.parseInt(inputUtil.readString());


                                        Employee e = employeeFileSystemServices.getEmployeeById(empId);
                                        if (e != null) {
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
                                        if (!emps.isEmpty()) outputUtil.printEmployees(emps);
                                        break;
                                    }

                                    default:
                                        System.out.println("Invalid menu option!!!");
                                }
                            } catch (NumberFormatException | InputMismatchException | NullPointerException e) {
                                System.out.println(e);
                            }
                        }
                        break;
                    case 3: {
                        while (true) {
                            try {
                                System.out.println("Enter a number from menu : \n 1.Add Employee \n 2.Update Employee\n 3.Delete Employee\n 4.Display Employees \n 5.Get Employee By Id\n 6.Get Employee by name\n 7.Exit");
                                menuNumber = Integer.parseInt(inputUtil.readString());
                                if (menuNumber == 7) {
                                    //employeeFileSystemServices.close();
                                    break;
                                }
                                switch (menuNumber) {
                                    case 1: {
                                        System.out.println("Enter the required Details : \n");
                                        Employee emp = null;
                                        Department dept = null;
                                        List<Object> objs = inputUtil.readInput(employeeDatabaseServices,false);
                                        if (objs != null) {
                                            emp = (Employee) objs.get(0);
                                            dept = (Department) objs.get(1);
                                            employeeDatabaseServices.addEmployee(emp);
                                        }
                                        break;
                                    }
                                    case 2: {
                                        System.out.println("Enter the required Details : \n");
                                        System.out.println("Enter the Employee Id to be updated : ");
                                        int empId = Integer.parseInt(inputUtil.readString());
                                        Employee emp = null;
                                        Department dept = null;
                                        List<Object> objs = inputUtil.readInput(employeeDatabaseServices,true);
                                        if (objs != null) {
                                            emp = (Employee) objs.get(0);
                                            dept = (Department) objs.get(1);
                                            employeeDatabaseServices.updateEmployee(empId, emp);
                                        }
                                        break;
                                    }
                                    case 3: {
                                        System.out.println("Enter the employee Id to be deleted : ");
                                        int empId = Integer.parseInt(inputUtil.readString());
                                        Employee emp = employeeDatabaseServices.deleteEmployee(empId);

                                        if (emp != null) {
                                            List<Employee> emps = new ArrayList<>();
                                            emps.add(emp);
                                            System.out.print("Details of deleted Employee : \n");
                                            outputUtil.printEmployees(emps);
                                        }
                                        break;
                                    }
                                    case 4: {
                                        System.out.println("Employees Details: ");
                                        List<Employee> employees = employeeDatabaseServices.getAllEmployees();
                                        if (employees.isEmpty()) {
                                            System.out.println("Empty no employees!!");
                                        } else outputUtil.printEmployees(employees);
                                        break;
                                    }
                                    case 5: {
                                        System.out.println("Enter the Employee Id :");
                                        int empId = Integer.parseInt(inputUtil.readString());
                                        Employee e = employeeDatabaseServices.getEmployeeById(empId);
                                        if (e != null) {
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
                                        List<Employee> emps = employeeDatabaseServices.getEmployeeByName(name);
                                        if (!emps.isEmpty()) outputUtil.printEmployees(emps);
                                        break;
                                    }

                                    default:
                                        System.out.println("Invalid menu option!!!");
                                }
                            } catch (NumberFormatException | InputMismatchException | NullPointerException e) {
                                System.out.println(e);
                            }
                        }
                    }
                    break;
                    case 4:
                        System.exit(0);
                    default:
                        System.out.println("Invalid Menu Option !!!");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
}
