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


@WebServlet(urlPatterns = "/emp")
public class EmployeeById extends HttpServlet {
    private final EmployeeDatabaseServices employeeDatabaseServices = EmployeeDatabaseServices.getInstance();
    private final Logger logger = LoggerFactory.getLogger(EmployeeById.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt fetch employee by id through api call");
        resp.setContentType("application/json");
        try {
            HttpSession httpSession = req.getSession(false);
            if (httpSession != null) {
                String idParam = req.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    int id = Integer.parseInt(idParam);
                    Employee employee = employeeDatabaseServices.getEmployeeById(id);
                    if (employee != null)
                        resp.getWriter().println(getJSON(employee, employeeDatabaseServices.getDepartment(employee.getDeptId())));
                    else resp.getWriter().write("No Employee found with given Id");
                    logger.info("Employee fetched through api call");
                }
            } else {
                resp.getWriter().println("Login First");
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid user Id");
            logger.error("Exception while fetching employee by id through api call", e);
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
