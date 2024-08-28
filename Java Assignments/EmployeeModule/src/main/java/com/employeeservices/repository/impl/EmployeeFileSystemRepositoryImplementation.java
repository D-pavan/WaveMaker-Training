package com.employeeservices.repository.impl;

import com.employeeservices.exceptions.EmployeeNotFoundException;
import com.employeeservices.models.Address;
import com.employeeservices.models.Employee;
import com.employeeservices.repository.AddressFileSystemRepository;
import com.employeeservices.repository.EmployeeFileSystemRepository;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

public class EmployeeFileSystemRepositoryImplementation {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeFileSystemRepositoryImplementation.class);
    private static EmployeeFileSystemRepositoryImplementation employeeFileSystemRepositoryImpl;
    private BufferedReader empReader;
    private BufferedWriter empWriter;
    private BufferedReader addressReader;
    private BufferedWriter addressWriter;
    private final EmployeeFileSystemRepository employeeFileSystemRepository;
    private final AddressFileSystemRepository addressFileSystemRepository;

    private EmployeeFileSystemRepositoryImplementation() throws IOException {
        employeeFileSystemRepository = EmployeeFileSystemRepository.getInstance();
        addressFileSystemRepository = AddressFileSystemRepository.getInstance();
        empReader = employeeFileSystemRepository.getBufferedReader();
        empWriter = employeeFileSystemRepository.getBufferedWriter();
        addressReader = addressFileSystemRepository.getBufferedReader();
        addressWriter = addressFileSystemRepository.getBufferedWriter();
    }

    public static synchronized EmployeeFileSystemRepositoryImplementation getInstance() {
        try {
            if (employeeFileSystemRepositoryImpl == null) {
                employeeFileSystemRepositoryImpl = new EmployeeFileSystemRepositoryImplementation();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return employeeFileSystemRepositoryImpl;
    }

    public List<Employee> getAllEmployees() {
        logger.debug("Attempt to fetch all employees from file");
        List<Employee> employees = new ArrayList<>();
        try {
            empReader = employeeFileSystemRepository.resetReader();
            addressReader = addressFileSystemRepository.resetReader();
            empReader.readLine();
            String line = null;
            while ((line = empReader.readLine()) != null) {
                String empData[] = line.split(",");
                int id1 = Integer.parseInt(empData[0]);
                Address add = getAddress(id1);
                employees.add(new Employee(Integer.parseInt(empData[0]), empData[1], add, 101));
            }
            logger.info("List<Employee> returned successfully");

        } catch (Exception e) {
            System.out.println("Error in reading.. " + e);
            logger.error("Exception while fetching all employees from file", e);
        }

        return employees;
    }

    public void addEmployee(Employee emp) {
        logger.debug("Attempt to add an employee to file");
        try {
            String empData = emp.getEmpId() + "," + emp.getEmpName() + "\n";
            String addressData = emp.getEmpId() + "," + emp.getAddress().getLocation() + "," + emp.getAddress().getPincode() + "\n";
            addressWriter.write(addressData);
            empWriter.write(empData);
            addressWriter.flush();
            empWriter.flush();
            logger.info("Employee added successfully to file");
        } catch (Exception e) {
            System.out.println("Error in writing.. " + e);
            logger.error("Exception while adding an employee to file", e);
        }
    }

    public void updateEmployee(int empId, Employee emp) {
        logger.debug("Attempt to update an employee in file System with id :{}", empId);
        StringBuilder content = new StringBuilder();
        boolean found = false;
        try {

            empReader = employeeFileSystemRepository.resetReader();
            content.append(empReader.readLine()).append("\n");
            String line;
            while ((line = empReader.readLine()) != null) {
                if (line.length() > 1) {
                    String data[] = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    if (id == empId) {
                        content.append(empId).append(",").append(emp.getEmpName()).append("\n");
                        found = true;
                    } else {
                        content.append(line).append("\n");
                    }
                }
            }
            empWriter = employeeFileSystemRepository.resetWriter();
            empWriter.write(content.toString());
            addressReader = addressFileSystemRepository.resetReader();
            content = new StringBuilder();
            content.append(addressReader.readLine()).append("\n");
            while ((line = addressReader.readLine()) != null) {
                if (line.length() > 1) {
                    String data[] = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    if (id == empId) {
                        content.append(empId).append(",").append(emp.getAddress().getLocation()).append(",").append(emp.getAddress().getPincode()).append("\n");
                        found = true;
                    } else {
                        content.append(line).append("\n");
                    }
                }
            }
            addressWriter = addressFileSystemRepository.resetWriter();
            addressWriter.write(content.toString());
            addressWriter.flush();
            empWriter.flush();
            if (!found) throw new EmployeeNotFoundException("No employee found with given Id");
            logger.info("Employee updated successfully in file");
        } catch (Exception e) {
            logger.error("Exception while updating the employee in file system with id :{}", empId, e);
            System.out.println(e);
        }

    }


    public List<Employee> getEmployeeByName(String name) {
        logger.debug("Attempt to fetch employees by name from file with name :{} ", name);
        List<Employee> empList = new ArrayList<>();
        try {
            empReader = employeeFileSystemRepository.resetReader();
            empReader.readLine();
            String line = null;
            while ((line = empReader.readLine()) != null) {
                if (line.length() > 1) {
                    String data[] = line.split(",");
                    if (name.equals(data[1])) {
                        empList.add(new Employee(Integer.parseInt(data[0]), data[1], getAddress(Integer.parseInt(data[0])), 101));
                    }
                }
            }
            if (empList.isEmpty()) throw new EmployeeNotFoundException("No employee found with given Name: " + name);
            logger.info("Employees fetched successfully from file system with name : {}", name);
        } catch (EmployeeNotFoundException | IOException e) {
            System.out.println(e);
            logger.error("Exception while fetching employees from  file system with name :{}", name, e);
        }
        return empList;
    }


    public Employee getEmployeeById(int empId) {
        logger.debug("Attempt to get an employee from file system with id  {}", empId);
        Employee emp = null;
        try {
            empReader = employeeFileSystemRepository.resetReader();
            empReader.readLine();
            String line = null;
            while ((line = empReader.readLine()) != null) {
                if (line.length() > 1) {
                    String data[] = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    if (id == empId) {
                        emp = new Employee(Integer.parseInt(data[0]), data[1], getAddress(id), 101);
                    }
                }
            }
            if (emp == null) throw new EmployeeNotFoundException("No employee found with given Id");
            logger.info("Employee fetched successfully from file system with id : {}", empId);
        } catch (Exception e) {
            System.out.println(e);
            logger.error("Exception while fetching employee from file system with id : {}", empId, e);
        }
        return emp;
    }


    public Employee deleteEmployee(int empId) {
        logger.debug("Attempt to delete an Employee from file system with id: {}", empId);
        Employee emp = null;
        StringBuilder content = new StringBuilder();
        boolean found = false;
        try {

            empReader = employeeFileSystemRepository.resetReader();
            content.append(empReader.readLine()).append("\n");
            String line = null;
            while ((line = empReader.readLine()) != null) {
                if (line.length() > 1) {
                    String data[] = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    if (id == empId) {
                        found = true;
                        emp = new Employee(Integer.parseInt(data[0]), data[1], null, 101);
                    } else {
                        content.append(line).append("\n");
                    }
                }
            }
            empWriter = employeeFileSystemRepository.resetWriter();
            empWriter.write(content.toString());
            addressReader = addressFileSystemRepository.resetReader();
            content = new StringBuilder();
            content.append(addressReader.readLine()).append("\n");
            while ((line = addressReader.readLine()) != null) {
                if (line.length() > 1) {
                    String data[] = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    if (id == empId) {
                        found = true;
                        if (emp != null) emp.setAddress(new Address(data[1], Long.parseLong(data[2])));
                    } else {
                        content.append(line).append("\n");
                    }
                }
            }
            addressWriter = addressFileSystemRepository.resetWriter();
            addressWriter.write(content.toString());
            addressWriter.flush();
            empWriter.flush();
            if (!found) throw new EmployeeNotFoundException("No employee found with given Id");
            logger.info("Employee deleted successfully from file system  with id : {}{}", empId, emp);
        } catch (Exception e) {
            System.out.println(e);
            logger.error("Exception while deleting the employee from file system with id : {}", empId, e);
        }
        return emp;
    }

    public Address getAddress(int empId) {
        logger.debug("Attempt to get address of employee from file system with id :{}", empId);
        Address address = null;
        try {
            addressReader = addressFileSystemRepository.resetReader();
            addressReader.readLine();
            String line = null;
            while ((line = addressReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String data[] = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    if (id == empId) {
                        address = new Address(data[1], Long.parseLong(data[2]));
                        break;
                    }
                }
            }
            logger.info("Employee address fetched from file system with id : {}", empId);
        } catch (IOException e) {
            System.out.println("error in getting address " + e);
            logger.error("Exception while fetching address from file system with empId:{}", empId, e);

        }
        return address;
    }

    public void close() {
        logger.debug("Attempt to close file system  resources");
        try {
            empReader.close();
            empWriter.close();
            addressReader.close();
            addressWriter.close();
            logger.info("File system Resources closed");
        } catch (IOException e) {
            System.out.println(e);
            logger.error("Exception while closing file system resources", e);
        }
    }
    public void setId(int id){
        logger.debug("Attempt set id for file System");
        try{
            Writer fileWriter=new FileWriter("D://pavan_internship/empId.txt");
            fileWriter.write("id,"+id);
            fileWriter.flush();
            fileWriter.close();
            logger.info("Id set successfully");
        }
        catch (IOException e){
            System.out.println(e);
            logger.error("Exception while setting new Id",e);
        }
    }
    public int generateEmployeeId(){
        logger.debug("Attempt to generate new  ID");
        int id=-1;
        try{
            BufferedReader br=new BufferedReader(new FileReader("D://pavan_internship/empId.txt"));
            String line=null;
            while((line=br.readLine())!=null){
                id=Integer.parseInt(line.split(",")[1]);
            }
            setId(++id);
            logger.info("Id generation successful");
        } catch (Exception e) {
            System.out.println(e);
            logger.error("Exception in generating id",e);
        }
        return  id;
    }
}
