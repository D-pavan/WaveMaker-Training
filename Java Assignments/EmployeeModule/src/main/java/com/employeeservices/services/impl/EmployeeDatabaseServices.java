package com.employeeservices.services.impl;

import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.models.User;
import com.employeeservices.repository.DatabaseRepository;
import com.employeeservices.repository.impl.EmployeeDatabaseRepositoryImplementation;
import com.employeeservices.services.EmployeeServices;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabaseServices implements EmployeeServices {
    private static EmployeeDatabaseServices employeeDatabaseServices;
    private final EmployeeDatabaseRepositoryImplementation employeeDatabaseRepositoryImplementation;

    private EmployeeDatabaseServices() {
        employeeDatabaseRepositoryImplementation = EmployeeDatabaseRepositoryImplementation.getInstance();
    }

    public static synchronized EmployeeDatabaseServices getInstance() {
        if (employeeDatabaseServices == null) {
            employeeDatabaseServices = new EmployeeDatabaseServices();
        }
        return employeeDatabaseServices;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDatabaseRepositoryImplementation.getAllEmployees();
    }

    @Override
    public Employee deleteEmployee(int empId) {
        return employeeDatabaseRepositoryImplementation.deleteEmployee(empId);
    }

    @Override
    public Employee getEmployeeById(int empId) {
        return employeeDatabaseRepositoryImplementation.getEmployeeById(empId);
    }

    @Override
    public void updateEmployee(int id, Employee emp) {
        employeeDatabaseRepositoryImplementation.updateEmployee(id, emp);
    }

    @Override
    public List<Employee> getEmployeeByName(String name) {
        return employeeDatabaseRepositoryImplementation.getEmployeeByName(name);
    }

    @Override
    public void addEmployee(Employee emp) {
        employeeDatabaseRepositoryImplementation.addEmployee(emp);
    }

    public Department getDepartment(int deptId) {
        return employeeDatabaseRepositoryImplementation.getDepartment(deptId);
    }

    public String authenticateUser(User user){
        return employeeDatabaseRepositoryImplementation.authenticateUser(user);
    }
}
