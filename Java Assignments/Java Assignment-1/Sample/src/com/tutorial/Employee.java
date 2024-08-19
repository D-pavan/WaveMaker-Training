package com.tutorial;

import java.util.Objects;

public class Employee {
    public static String companyName="wavemaker";
    private long empId;
    protected String empName;
    public String role;

    public Employee(long empId,String empName,String role){
        this.empName=empName;
        this.empId=empId;
        this.role=role;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return empId == employee.empId && Objects.equals(empName, employee.empName) && Objects.equals(role, employee.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, empName, role);
    }
}
