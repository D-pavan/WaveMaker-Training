
package com.employeeservices.utils;

import com.employeeservices.models.Address;
import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.repository.DepartmentInMemoryRepository;
import com.employeeservices.services.impl.EmployeeFileSystemServices;
import com.employeeservices.services.impl.EmployeeInMemoryServices;

import java.util.*;

public class InputUtil {
    private static InputUtil inputUtil;
    private final Scanner scan;

    private InputUtil() {
        scan = new Scanner(System.in);
        DepartmentInMemoryRepository departmentInMemoryRepository = DepartmentInMemoryRepository.getInstance();
    }

    public static InputUtil getInstance() {
        if (inputUtil == null) {
            inputUtil = new InputUtil();
        }
        return inputUtil;
    }

    public String readString() {
        return scan.next();
    }

    public List<Object> readInput(Object o,boolean isUpdate) {
        List<Object> obj = new ArrayList<>();
        try {
            int empId=-1;
            if((o instanceof EmployeeInMemoryServices) && !isUpdate) empId=EmployeeInMemoryServices.getInstance().generateEmployeeId();
            else if((o instanceof EmployeeFileSystemServices) && !isUpdate) empId= EmployeeFileSystemServices.getInstance().generateEmployeeId();
            System.out.print("Enter employee Name :");
            String empName = scan.next();
            System.out.println();
            System.out.print("Enter the location : ");
            String location = scan.next();
            System.out.println();
            long pincode;
            do {
                System.out.print("Enter the 6-digit pin code : ");
                pincode = scan.nextLong();
            } while (String.valueOf(pincode).length() != 6);
            System.out.println();
            System.out.print("Enter the Department ID :");
            int deptId = scan.nextInt();
            System.out.println();
            System.out.print("Enter the Department Name : ");
            String deptName = scan.next();
            System.out.println();
            obj.add(new Employee(empId, empName, new Address(location, pincode), deptId));
            obj.add(new Department(deptId, deptName, new ArrayList<>()));
            return obj;
        } catch (InputMismatchException e) {
            obj = null;
            System.out.println("enter id in numeric format" + e);
        }
        return obj;
    }
}
