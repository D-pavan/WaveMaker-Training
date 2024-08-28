package com.employeeservices.repository;

import com.employeeservices.models.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentInMemoryRepository {
    private static DepartmentInMemoryRepository departmentInMemoryRepository;
    private final Map<Integer, List<Integer>> departmentEmployees;
    private final Map<String, Integer> departmentNames;
    private final Map<Integer, String> departments;

    private DepartmentInMemoryRepository() {
        departments = new HashMap<>();
        departmentNames = new HashMap<>();
        departmentEmployees = new HashMap<>();
        departmentNames.put("HR", 1);
        departmentNames.put("ProductionManagement", 2);
        departmentNames.put("IT", 3);
        departmentNames.put("Sales", 4);
        departmentNames.put("Finance", 5);
        departmentNames.put("Marketing", 6);
        departments.put(1, "HR");
        departments.put(2, "ProductionManagement");
        departments.put(3, "IT");
        departments.put(4, "Sales");
        departments.put(5, "Finance");
        departments.put(6, "Marketing");
    }

    public static synchronized DepartmentInMemoryRepository getInstance() {
        if (departmentInMemoryRepository == null) {
            departmentInMemoryRepository = new DepartmentInMemoryRepository();
        }
        return departmentInMemoryRepository;
    }

    public Map<Integer, List<Integer>> getDepartmentEmployees() {
        return this.departmentEmployees;
    }

    public Map<String, Integer> getDepartmentNames() {
        return this.departmentNames;
    }

    public Map<Integer, String> getDepartments() {
        return this.departments;
    }
}
