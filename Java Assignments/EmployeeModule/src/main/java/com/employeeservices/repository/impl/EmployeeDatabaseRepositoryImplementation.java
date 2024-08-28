package com.employeeservices.repository.impl;

import com.employeeservices.exceptions.EmployeeNotFoundException;
import com.employeeservices.models.Address;
import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.models.User;
import com.employeeservices.repository.DatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabaseRepositoryImplementation {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDatabaseRepositoryImplementation.class);
    private static EmployeeDatabaseRepositoryImplementation employeeDatabaseRepositoryImplementation;
    private final Connection connection;

    private EmployeeDatabaseRepositoryImplementation() {
        connection = DatabaseRepository.getInstance().getConnection();
    }

    public static synchronized EmployeeDatabaseRepositoryImplementation getInstance() {
        if (employeeDatabaseRepositoryImplementation == null) {
            employeeDatabaseRepositoryImplementation = new EmployeeDatabaseRepositoryImplementation();
        }
        return employeeDatabaseRepositoryImplementation;
    }

    public void addEmployee(Employee emp) {
        logger.debug("Attempt to add an employee to database");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO  EMPLOYEE (EMPLOYEE_NAME,DEPARTMENT_ID) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, emp.getEmpName());
            preparedStatement.setInt(2, emp.getDeptId());
            int empId=0;
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                ResultSet resultSet=preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    empId=resultSet.getInt(1);
                }
            }
            preparedStatement = connection.prepareStatement("INSERT INTO ADDRESS VALUES(?,?,?)");
            preparedStatement.setString(1, emp.getAddress().getLocation());
            preparedStatement.setLong(2, emp.getAddress().getPincode());
            preparedStatement.setInt(3, empId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("Employee added to database");
        } catch (SQLException e) {
            System.out.println(e);
            logger.error("Exception while adding employee to database ", e);
        }
    }

    public Employee deleteEmployee(int empId) {
        logger.debug("Attempt to delete an employee from database with id: {}", empId);
        Employee employee = null;
        try {
            employee = getEmployeeById(empId);
            if (employee == null) throw new EmployeeNotFoundException("No employee found with given Id");
            PreparedStatement preparedStatement = connection.prepareStatement("delete from employee where employee_id=?");
            preparedStatement.setInt(1, empId);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("delete from address where employee_id=?");
            preparedStatement.setInt(1, empId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("Employee deleted from database with id :{}", empId);

        } catch (SQLException | EmployeeNotFoundException e) {
            System.out.println(e);
            logger.error("Exception while deleting employee from database with id :{}", empId, e);
        }
        return employee;
    }

    public Employee getEmployeeById(int empId) {
        logger.debug("Attempt to fetch an employee from database with id : {}", empId);
        Employee employee = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select e.employee_id,e.employee_name,e.department_id,a.location,a.pincode from employee e inner join address a on e.employee_id = a.employee_id where e.employee_id= ?");
            preparedStatement.setInt(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employee = new Employee(resultSet.getInt(1), resultSet.getString(2), new Address(resultSet.getString(4), resultSet.getLong(5)), resultSet.getInt(3));
            }
            if (employee == null) throw new EmployeeNotFoundException("No employee found with given ID");
            logger.info("Employee fetched from database with id {}", empId);
        } catch (SQLException | EmployeeNotFoundException e) {
            System.out.println(e);
            logger.error("Exception while fetching employee from database with id : {}", empId, e);
        }
        return employee;
    }

    public List<Employee> getEmployeeByName(String name) {
        logger.debug("Attempt to fetch employees from database with name : {}", name);
        List<Employee> employees = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select employee_id from employee where employee_name=?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employees.add(getEmployeeById(resultSet.getInt(1)));
            }
            logger.info("Employees fetched from database with name : {}", name);

        } catch (SQLException e) {
            System.out.println(e);
            logger.error("Exception while fetching employees with name : {}", name, e);

        }
        return employees;
    }

    public List<Employee> getAllEmployees() {
        logger.debug("Attempt to get all records from database");

        List<Employee> employees = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select e.employee_id,e.employee_name,e.department_id,a.location,a.pincode from employee e inner join address a on e.employee_id = a.employee_id");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employees.add(new Employee(resultSet.getInt(1), resultSet.getString(2), new Address(resultSet.getString(4), resultSet.getLong(5)), resultSet.getInt(3)));
            }
            logger.info("All records fetched  from database successfully");
        } catch (SQLException e) {
            System.out.println(e);
            logger.error("Exception while fetching all records from database ", e);
        }
        return employees;
    }

    public void updateEmployee(int id, Employee emp) {
        logger.debug("Attempt to update employee in database with id : {}", id);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update employee set employee_name=?,department_id=? where employee_id=?");
            preparedStatement.setString(1, emp.getEmpName());
            preparedStatement.setInt(2, emp.getDeptId());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("update address set location=?,pincode=? where employee_id=?");
            preparedStatement.setString(1, emp.getAddress().getLocation());
            preparedStatement.setLong(2, emp.getAddress().getPincode());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            logger.info("Employee updated in database with id : {}", id);
        } catch (SQLException e) {
            System.out.println(e);
            logger.error("Exception while updating database for employee with id : {}", id, e);
        }
    }
    public Department getDepartment(int deptId){
        logger.debug("Attempt to get Department details");
        Department department=null;
        try{
            PreparedStatement preparedStatement=connection.prepareStatement("select department_id,department_name from department where department_id=?");
            preparedStatement.setInt(1,deptId);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                department=new Department(resultSet.getInt(1),resultSet.getString(2),null);
            }
            logger.info("Department fetched successfully");
        }
        catch (SQLException e){
            System.out.println(e);
            logger.error("Exception while fetching department",e);
        }
        return  department;
    }
    public String authenticateUser(User user){
        logger.debug("Authenticating user.....");
        try{
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM USERS WHERE USER_NAME=? AND USER_PASSWORD=?");
            preparedStatement.setString(1,user.getUserName());
            preparedStatement.setString(2,user.getUserPassword());
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                System.out.println("role : "+resultSet.getString(3));
                logger.info("Authentication success!!");
                return resultSet.getString(3);

            }

        } catch (SQLException e) {
            logger.error("Exception while Authenticating user",e);
            System.out.println(e);
        }
        return  null;
    }
}
