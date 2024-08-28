package com.employeeservices.services.impl;

import com.employeeservices.models.Employee;
import com.employeeservices.repository.impl.EmployeeInMemoryRepositoryImplementation;
import com.employeeservices.services.EmployeeServices;

import java.util.*;

public class EmployeeInMemoryServices implements EmployeeServices {
    private static EmployeeInMemoryServices employeeInMemoryServices;
    private final EmployeeInMemoryRepositoryImplementation employeeInMemoryRespositoryImpl;

    private EmployeeInMemoryServices() {
        employeeInMemoryRespositoryImpl = EmployeeInMemoryRepositoryImplementation.getInstance();
    }

    public static synchronized EmployeeInMemoryServices getInstance() {
        if (employeeInMemoryServices == null) {
            employeeInMemoryServices = new EmployeeInMemoryServices();
        }
        return employeeInMemoryServices;
    }

    @Override
    public Employee getEmployeeById(int empId) {
        return employeeInMemoryRespositoryImpl.getEmployeeById(empId);
    }

    @Override
    public List<Employee> getEmployeeByName(String name) {
        return employeeInMemoryRespositoryImpl.getEmployeeByName(name);
    }

    @Override
    public Employee deleteEmployee(int empId) {
        return employeeInMemoryRespositoryImpl.deleteEmployee(empId);
    }

    @Override
    public void addEmployee(Employee emp) {
        employeeInMemoryRespositoryImpl.addEmployee(emp);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeInMemoryRespositoryImpl.getAllEmployees();
    }

    @Override
    public void updateEmployee(int id, Employee emp) {
        employeeInMemoryRespositoryImpl.updateEmployee(id, emp);
    }

    public int generateEmployeeId(){
        return employeeInMemoryRespositoryImpl.generateEmployeeId();
    }
}
