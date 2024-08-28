package com.employeeservices.services.impl;

import com.employeeservices.models.Employee;

import com.employeeservices.repository.impl.EmployeeFileSystemRepositoryImplementation;
import com.employeeservices.services.EmployeeServices;

import java.io.*;
import java.util.List;

public class EmployeeFileSystemServices implements EmployeeServices {
    private static EmployeeFileSystemServices employeeFileSystemServices;
    private final EmployeeFileSystemRepositoryImplementation employeeFileSystemRepositoryImpl;

    private EmployeeFileSystemServices() throws IOException {
        employeeFileSystemRepositoryImpl = EmployeeFileSystemRepositoryImplementation.getInstance();
    }

    public static synchronized EmployeeFileSystemServices getInstance() {
        try {
            if (employeeFileSystemServices == null) {
                employeeFileSystemServices = new EmployeeFileSystemServices();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return employeeFileSystemServices;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeFileSystemRepositoryImpl.getAllEmployees();
    }

    @Override
    public void addEmployee(Employee emp) {
        employeeFileSystemRepositoryImpl.addEmployee(emp);
    }

    @Override
    public void updateEmployee(int id, Employee emp) {
        employeeFileSystemRepositoryImpl.updateEmployee(id, emp);
    }

    @Override
    public Employee getEmployeeById(int empId) {
        return employeeFileSystemRepositoryImpl.getEmployeeById(empId);
    }

    @Override
    public Employee deleteEmployee(int empId) {
        return employeeFileSystemRepositoryImpl.deleteEmployee(empId);
    }

    @Override
    public List<Employee> getEmployeeByName(String name) {
        return employeeFileSystemRepositoryImpl.getEmployeeByName(name);
    }

    public void close() {
        employeeFileSystemRepositoryImpl.close();
    }

    public int generateEmployeeId(){
        return employeeFileSystemRepositoryImpl.generateEmployeeId();
    }
}
