package com.employeeservices.servlets;


import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.services.impl.EmployeeDatabaseServices;
import com.employeeservices.services.impl.EmployeeFileSystemServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/smp")
public class Sample extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(Sample.class);
    private final EmployeeDatabaseServices employeeDatabaseServices = EmployeeDatabaseServices.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to fetch all employees through api call ");
        resp.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
        resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {

                List<Employee> employeeList = employeeDatabaseServices.getAllEmployees();
                int size = employeeList.size();
                if (!employeeList.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("{");
                    for (Employee e : employeeList) {
                        Department department = employeeDatabaseServices.getDepartment(e.getDeptId());
                        stringBuilder.append("\"").append(e.getEmpId()).append("\"").append(":").append(getJSON(e, department));
                        if (size - 1 > 0) stringBuilder.append(",");
                        size--;
                    }
                    stringBuilder.append("}");
                    resp.getWriter().println(stringBuilder.toString());
                    logger.info("Employees fetched through api call");
                } else {
                    resp.getWriter().println("No employees in database");
                }

        } catch (Exception e) {
            System.out.println(e);
            logger.error("Exception while fetching employee through api call ");
        }
    }


    public String getJSON(Employee employee, Department department) {
        return "{" +
                "\"id\": " + employee.getEmpId() + ", " +
                "\"name\": \"" + employee.getEmpName() + "\", " +
                "\"address\": {" +
                "\"location\": \"" + employee.getAddress().getLocation() + "\", " +
                "\"pincode\": \"" + employee.getAddress().getPincode() + "\"" +
                "}, " +
                "\"department\": {" +
                "\"deptId\": " + department.getDeptId() + ", " +
                "\"deptName\": \"" + department.getDeptName() + "\"" +
                "}" +
                "}";
    }

}
