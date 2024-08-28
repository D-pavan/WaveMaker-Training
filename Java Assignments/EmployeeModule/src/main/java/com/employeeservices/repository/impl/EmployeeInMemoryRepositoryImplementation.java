package com.employeeservices.repository.impl;

import com.employeeservices.exceptions.DuplicateValueException;
import com.employeeservices.exceptions.EmployeeNotFoundException;
import com.employeeservices.models.Address;
import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.repository.AddressInMemoryRepository;
import com.employeeservices.repository.DepartmentInMemoryRepository;
import com.employeeservices.repository.EmployeeInMemoryRepository;
import com.employeeservices.servlets.EmployeeById;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

public class EmployeeInMemoryRepositoryImplementation {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeInMemoryRepositoryImplementation.class);
    private static EmployeeInMemoryRepositoryImplementation employeeInMemoryRepositoryImpl;
    private final Map<Integer, Employee> employees;
    private final Map<Integer, Address> addresses;
    private final Map<String, List<Integer>> employeeNames;
    private final Map<Integer, List<Integer>> departmentEmployees;
    private final Map<Integer, String> departments;
    private final Map<String, Integer> departmentNames;
    private  int employeeId=0;
    private EmployeeInMemoryRepositoryImplementation() {
        employees = EmployeeInMemoryRepository.getInstance().getEmployees();
        addresses = AddressInMemoryRepository.getInstance().getAddress();
        employeeNames = EmployeeInMemoryRepository.getInstance().getEmployeeNames();
        departmentEmployees = DepartmentInMemoryRepository.getInstance().getDepartmentEmployees();
        departmentNames = DepartmentInMemoryRepository.getInstance().getDepartmentNames();
        departments = DepartmentInMemoryRepository.getInstance().getDepartments();
    }

    public static EmployeeInMemoryRepositoryImplementation getInstance() {
        if (employeeInMemoryRepositoryImpl == null) {
            employeeInMemoryRepositoryImpl = new EmployeeInMemoryRepositoryImplementation();
        }
        return employeeInMemoryRepositoryImpl;
    }

    public List<Employee> getAllEmployees() {
        logger.debug("Attempt get all Employees from In memory");
        List<Employee> emps = new ArrayList<>();
        for (Map.Entry<Integer, Employee> emp : employees.entrySet()) {
            emp.getValue().setAddress(addresses.get(emp.getKey()));
            emps.add(emp.getValue());
        }
        logger.info("List<Employees>  successfully returned from InMemory");
        return emps;
    }

    public Employee deleteEmployee(int empId) {
        logger.debug("Attempt to delete an Employee from InMemory with id :{}", empId);
        Employee deletedEmployee = null;
        try {
            if (employees.containsKey(empId)) {
                deletedEmployee = employees.remove(empId);
                if (addresses.containsKey(empId)) deletedEmployee.setAddress(addresses.remove(empId));
                if (employeeNames.containsKey(deletedEmployee.getEmpName())) {
                    List<Integer> ids = employeeNames.get(deletedEmployee.getEmpName());
                    for (int i = 0; i < ids.size(); i++) {
                        if (ids.get(i) == empId) {
                            employeeNames.get(deletedEmployee.getEmpName()).remove(i);
                            break;
                        }
                    }
                    if (employeeNames.get(deletedEmployee.getEmpName()).isEmpty()) {
                        employeeNames.remove(deletedEmployee.getEmpName());
                    }
                }
            }
            if (deletedEmployee == null) throw new EmployeeNotFoundException("No Employee Found with given Id!!!");
            logger.info("Employee deleted successfully from InMemory with id : {} {}", empId, deletedEmployee);
        } catch (EmployeeNotFoundException e) {
            logger.error("Exception while deleting an employee from InMemory with id : {}", empId, e);
            System.out.println(e);
        }
        return deletedEmployee;
    }

    public Employee getEmployeeById(int empId) {
        logger.debug("Attempt to fetch employee from InMemory with id: {}", empId);
        Employee emp = null;
        try {
            if (employees.containsKey(empId)) {
                emp = employees.get(empId);
                if (addresses.containsKey(empId)) emp.setAddress(addresses.get(empId));
            }
            if (emp == null) {
                throw new EmployeeNotFoundException("No employee found with given ID!!!!");
            }
            logger.info("Employee Fetched from InMemory with Id{} : {}", empId, emp);
        } catch (EmployeeNotFoundException e) {

            System.out.println(e);
            logger.error("Exception while fetching an employee from InMemory with id : {}", empId, e);
        }

        return emp;
    }

    public List<Employee> getEmployeeByName(String name) {
        logger.debug("Attempt to fetch employee  from InMemory by name : {}", name);
        List<Employee> emps = new ArrayList<>();
        try {
            if (!employeeNames.containsKey(name))
                throw new EmployeeNotFoundException("No employee found with given name..");
            else {
                List<Integer> ids = employeeNames.get(name);
                for (Integer id : ids) {
                    emps.add(employees.get(id));
                    emps.get(emps.size() - 1).setAddress(addresses.get(id));
                }
            }
            logger.info("Employees fetched from InMemory with name {}", name);
        } catch (EmployeeNotFoundException e) {
            System.out.println(e);
            logger.error("Error in fetching an employees from InMemory with name {}", name, e);
        }
        return emps;
    }

    public void addEmployee(Employee emp) {
        logger.debug("Attempt to add an employee to InMemory");
        try {
            if (employees.containsKey(emp.getEmpId())) throw new DuplicateValueException("Duplicate ID..");
            else {
                addresses.put(emp.getEmpId(), emp.getAddress());
                emp.setAddress(null);
                employees.put(emp.getEmpId(), emp);
                if (!employeeNames.containsKey(emp.getEmpName())) {
                    employeeNames.put(emp.getEmpName(), new ArrayList<>());
                }
                employeeNames.get(emp.getEmpName()).add(emp.getEmpId());
                if (!departmentEmployees.containsKey(emp.getDeptId())) {
                    departmentEmployees.put(emp.getDeptId(), new ArrayList<>());
                }
                departmentEmployees.get(emp.getDeptId()).add(emp.getEmpId());
            }
            logger.info("Employee added successfully to InMemory");
        } catch (DuplicateValueException | NullPointerException e) {
            System.out.println(e);
            logger.error("Exception while adding employee {} to InMemory", emp, e);
        }
    }

    public void updateEmployee(int empId, Employee emp) {
        logger.debug("Attempt to update an employee from InMemory with id {}", empId);
        try {
            if (!employees.containsKey(empId))
                throw new EmployeeNotFoundException("Employee not found with given ID" + empId);
            else {
                Employee oldEmp=employees.get(empId);
                List<Integer> deptEmp=departmentEmployees.get(oldEmp.getDeptId());
                for(int i=0;i<deptEmp.size();i++){
                    if(deptEmp.get(i)==oldEmp.getDeptId()){
                        departmentEmployees.get(oldEmp.getDeptId()).remove(i);
                        break;
                    }
                }
                if (!departmentEmployees.containsKey(emp.getDeptId())) {
                    departmentEmployees.put(emp.getDeptId(), new ArrayList<>());
                }
                departmentEmployees.get(emp.getDeptId()).add(emp.getEmpId());
                String name = employees.get(empId).getEmpName();
                List<Integer> ids = employeeNames.getOrDefault(name, new ArrayList<>());
                employeeNames.remove(name);
                employeeNames.put(emp.getEmpName(), ids);
                if (addresses.containsKey(empId)) {
                    addresses.put(empId, emp.getAddress());
                }
                emp.setEmpId(empId);
                emp.setAddress(null);
                employees.put(empId, emp);
                logger.info("Employee updated successfully in InMemory with id: {}", empId);

            }
        } catch (EmployeeNotFoundException e) {
            System.out.println(e);
            logger.error("Exception while updating  employee in  InMemory with id : {}", empId, e);
        }
    }
    public int generateEmployeeId(){
        return ++employeeId;
    }
    public boolean checkDepartmentExist(String name) {
        return departmentNames.containsKey(name);
    }

    public Department getDepartment(int empId) {
        for (Map.Entry<Integer, List<Integer>> entry : departmentEmployees.entrySet()) {
            if (entry.getValue().contains(empId)) {
                return new Department(entry.getKey(), departments.get(entry.getKey()), null);
            }
        }
        return null;
    }
}
