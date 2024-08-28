package com.employeeservices.repository;

import com.employeeservices.models.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeInMemoryRepository {
    private static EmployeeInMemoryRepository employeeInMemoryRepository;
    private final Map<Integer, Employee> employees;
    private final Map<String, List<Integer>> employeeNames;

    private EmployeeInMemoryRepository() {
        employees = new HashMap<>();
        employeeNames = new HashMap<>();
    }

    public static synchronized EmployeeInMemoryRepository getInstance() {
        if (employeeInMemoryRepository == null) {
            employeeInMemoryRepository = new EmployeeInMemoryRepository();
        }
        return employeeInMemoryRepository;
    }

    public Map<Integer, Employee> getEmployees() {
        return this.employees;
    }

    public Map<String, List<Integer>> getEmployeeNames() {
        return employeeNames;
    }
}


