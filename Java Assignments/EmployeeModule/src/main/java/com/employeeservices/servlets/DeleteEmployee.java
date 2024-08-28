package com.employeeservices.servlets;

import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.models.User;
import com.employeeservices.services.impl.EmployeeDatabaseServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = "/deleteEmployeeById")
public class DeleteEmployee extends HttpServlet {
    private final EmployeeDatabaseServices employeeDatabaseServices = EmployeeDatabaseServices.getInstance();
    private final Logger logger = LoggerFactory.getLogger(DeleteEmployee.class);

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to delete employee from api call");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            HttpSession httpSession = req.getSession(false);
            if (httpSession != null) {
                Cookie[] cookies = req.getCookies();
                String userName = null, userPassword= null;
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) userName = cookie.getValue();
                    else userPassword = cookie.getValue();
                }
                String userRole= employeeDatabaseServices.authenticateUser(new User(userName,userPassword));
                if(userRole!=null) {
                    if (userRole.equals("admin")) {
                        String idParam = req.getParameter("id");
                        if (idParam != null && !idParam.isEmpty()) {
                            int id = Integer.parseInt(idParam);
                            Employee employee = employeeDatabaseServices.deleteEmployee(id);
                            logger.info("Employee deleted through api call with id : {}", employee.getEmpId());
                            resp.getWriter().println(getJSON(employee, employeeDatabaseServices.getDepartment(employee.getDeptId())));
                        } else {
                            resp.getWriter().write("Empty id parameter received");
                            logger.info("Empty id parameter received");
                        }
                    }else {
                        resp.getWriter().println("You don't have access to delete");
                        logger.warn("Access denied for deletion");
                    }
                }
                else{
                    resp.getWriter().println("You have to register first !!");

                }

            } else {
                resp.getWriter().println("Login First");
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write("Invalid id received");
            logger.error("Exception Invalid id received as parameter id", e);
        } catch (Exception e) {
            resp.getWriter().write("Error in deleting Employee");
            logger.error("Exception while deleting employee through api call ", e);
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
