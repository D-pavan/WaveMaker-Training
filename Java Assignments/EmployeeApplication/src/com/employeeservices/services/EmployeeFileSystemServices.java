package com.employeeservices.services;

import com.employeeservices.controllers.EmployeeFileSystemController;
import com.employeeservices.exceptions.EmployeeNotFoundException;
import com.employeeservices.models.Address;
import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.repository.AddressFileSystemRepository;
import com.employeeservices.repository.EmployeeFileSystemRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFileSystemServices implements  Services {
    private  static EmployeeFileSystemServices employeeFileSystemServices;
    private  BufferedReader empReader;
    private  BufferedWriter empWriter;
    private  BufferedReader addressReader;
    private  BufferedWriter addressWriter;
    private final EmployeeFileSystemRepository employeeFileSystemRepository;
    private final AddressFileSystemRepository addressFileSystemRepository;
    private  EmployeeFileSystemServices() throws  IOException{
        employeeFileSystemRepository=EmployeeFileSystemRepository.getInstance();
        addressFileSystemRepository= AddressFileSystemRepository.getInstance();
        empReader=employeeFileSystemRepository.getBufferedReader();
        empWriter=employeeFileSystemRepository.getBufferedWriter();
        addressReader=addressFileSystemRepository.getBufferedReader();
        addressWriter=addressFileSystemRepository.getBufferedWriter();
    }
    public static  EmployeeFileSystemServices getInstance(){
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
        List<Employee> employees = new ArrayList<>();
        try {
            empReader=employeeFileSystemRepository.resetReader();
            addressReader=addressFileSystemRepository.resetReader();
            String line=null;
            while ((line=empReader.readLine())!= null) {
                String empData[]=line.split(",");
                int id1=Integer.parseInt(empData[0]);
                Address add=getAddress(id1);
                employees.add(new Employee(Integer.parseInt(empData[0]),empData[1],add));
            }

        }
        catch (Exception e){
            System.out.println("Error in reading.. "+e);
        }
        return  employees;
    }

    @Override
    public void addEmployee(Employee emp) {
        try{
            String empData=emp.getEmpId()+","+emp.getEmpName()+"\n";
            String addressData=emp.getEmpId()+","+emp.getAddress().getLocation()+","+emp.getAddress().getPincode()+"\n";
            addressWriter.write(addressData);
            empWriter.write(empData);
            addressWriter.flush();
            empWriter.flush();
        }
        catch (Exception e){
            System.out.println("Error in writing.. "+e);
        }
    }

    @Override
    public void updateEmployee(int empId, Employee emp) {
        StringBuffer content=new StringBuffer();
        boolean found=false;
        try {

            empReader=employeeFileSystemRepository.resetReader();
            String line=null;
            while((line=empReader.readLine())!=null) {
                if(line.length()>1) {
                    String data[]=line.split(",");
                    int id=Integer.parseInt(data[0]);
                    if(id==empId){
                        content.append(emp.getEmpId()+","+emp.getEmpName()+"\n");
                        found=true;
                    }
                    else{
                        content.append(line+"\n");
                    }
                }
            }
            empWriter=employeeFileSystemRepository.resetWriter();
            empWriter.write(content.toString());
            addressReader=addressFileSystemRepository.resetReader();
            line=null;
            content=new StringBuffer();
            while((line=addressReader.readLine())!=null) {
                if(line.length()>1) {
                    String data[]=line.split(",");
                    int id=Integer.parseInt(data[0]);
                    if(id==empId){
                        content.append(emp.getEmpId()+","+emp.getAddress().getLocation()+","+emp.getAddress().getPincode()+"\n");
                        found=true;
                    }
                    else{
                        content.append(line+"\n");
                    }
                }
            }
            addressWriter=addressFileSystemRepository.resetWriter();
            addressWriter.write(content.toString());
            addressWriter.flush();
            empWriter.flush();
            if(!found) throw new EmployeeNotFoundException("No employee found with given Id");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public List<Employee> getEmployeeByName(String name) {
        List<Employee> empList=new ArrayList<>();
        try{
            empReader=employeeFileSystemRepository.resetReader();
            String line=null;
            while((line=empReader.readLine())!=null) {
                if(line.length()>1) {
                    String data[]=line.split(",");
                    if(name.equals(data[1])){
                        empList.add(new Employee(Integer.parseInt(data[0]),data[1],getAddress(Integer.parseInt(data[0]))));
                    }
                }
            }
            if(empList.isEmpty()) throw new EmployeeNotFoundException("No employee found with given Name: "+name);
        }
        catch (EmployeeNotFoundException  | IOException e){
            System.out.println(e);
        }
        return empList;
    }

    @Override
    public Employee getEmployeeById(int empId) {
        Employee emp=null;
        try {
            empReader=employeeFileSystemRepository.resetReader();
            String line=null;
            while((line=empReader.readLine())!=null) {
                if(line.length()>1) {
                   String data[]=line.split(",");
                   int id=Integer.parseInt(data[0]);
                   if(id==empId){
                       emp=new Employee(Integer.parseInt(data[0]),data[1],getAddress(id));
                   }
                }
            }
            if(emp==null) throw new EmployeeNotFoundException("No employee found with given Id");
        } catch (Exception e) {
            System.out.println(e);
        }

        return emp;
    }

    @Override
    public Employee deleteEmployee(int empId) {
        Employee emp=null;
        StringBuffer content=new StringBuffer();
        boolean found=false;
        try {

            empReader=employeeFileSystemRepository.resetReader();
            String line=null;
            while((line=empReader.readLine())!=null) {
                if(line.length()>1) {
                    String data[]=line.split(",");
                    int id=Integer.parseInt(data[0]);
                    if(id==empId){
                        found=true;
                        emp=new Employee(Integer.parseInt(data[0]),data[1],null);
                    }
                    else{
                        content.append(line+"\n");
                    }
                }
            }
            empWriter=employeeFileSystemRepository.resetWriter();
            empWriter.write(content.toString());
            addressReader=addressFileSystemRepository.resetReader();
            line=null;
            content=new StringBuffer();
            while((line=addressReader.readLine())!=null) {
                if(line.length()>1) {
                    String data[]=line.split(",");
                    int id=Integer.parseInt(data[0]);
                    if(id==empId){
                        found=true;
                        emp.setAddress(new Address(data[1],Long.parseLong(data[2])));
                    }
                    else{
                        content.append(line+"\n");
                    }
                }
            }
            addressWriter=addressFileSystemRepository.resetWriter();
            addressWriter.write(content.toString());
            addressWriter.flush();
            empWriter.flush();
            if(!found) throw new EmployeeNotFoundException("No employee found with given Id");
        } catch (Exception e) {
            System.out.println(e);
        }
        return emp;
    }
    public Address getAddress(int empId){
        Address address=null;
        try {
            addressReader=addressFileSystemRepository.resetReader();
            String line=null;
            while ((line = addressReader.readLine()) != null) {
                if(line.length()>0) {
                    String data[] = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    if (id == empId) {
                        address = new Address(data[1], Long.parseLong(data[2]));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("getting address "+e);
        }
        if(address==null){
            System.out.println("no address found");
        }

        return address;
    }
    public void close(){
        try {
            empReader.close();
            empWriter.close();
            addressReader.close();
            addressWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
