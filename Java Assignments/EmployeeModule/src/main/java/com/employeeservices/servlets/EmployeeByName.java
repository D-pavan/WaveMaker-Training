package com.employeeservices.servlets;

import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.services.impl.EmployeeDatabaseServices;
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

@WebServlet(urlPatterns = "/getEmployeeByName")
public class EmployeeByName extends HttpServlet {
    private final EmployeeDatabaseServices employeeDatabaseServices = EmployeeDatabaseServices.getInstance();
    private Logger logger = LoggerFactory.getLogger(EmployeeByName.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to fetch employee by name through api call");
        resp.setContentType("application/json");
        try {
            HttpSession httpSession = req.getSession(false);
            if (httpSession != null) {
                String name = req.getParameter("name");

                if (name != null && !name.isEmpty()) {
                    List<Employee> employees = employeeDatabaseServices.getEmployeeByName(name);
                    for (Employee employee : employees) {
                        resp.getWriter().println(getJSON(employee, employeeDatabaseServices.getDepartment(employee.getDeptId())));
                    }
                    if (employees.isEmpty()) resp.getWriter().write("No employee found the given name");
                    logger.info("Employees fetched by name");

                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Bad Request");
                }
            } else {
                resp.getWriter().println("Login First");
            }

        } catch (Exception e) {
            resp.getWriter().println("Exception while fetching employee by name");
            logger.error("Exception while fetching employee with name through api call", e);
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
