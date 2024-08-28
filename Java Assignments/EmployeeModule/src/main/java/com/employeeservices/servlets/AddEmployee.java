package com.employeeservices.servlets;

import com.employeeservices.models.Address;
import com.employeeservices.models.Employee;
import com.employeeservices.models.User;
import com.employeeservices.services.impl.EmployeeDatabaseServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(urlPatterns = "/addEmployee")
public class AddEmployee extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(AddEmployee.class);
    private final EmployeeDatabaseServices employeeDatabaseServices = EmployeeDatabaseServices.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to post an employee");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try {
            HttpSession httpSession = req.getSession(false);
            if (httpSession != null) {
                Cookie[] cookies = req.getCookies();
                String userName = null, userPassword = null;
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) userName = cookie.getValue();
                    else userPassword = cookie.getValue();
                }
                String userRole= employeeDatabaseServices.authenticateUser(new User(userName,userPassword));
                if(userRole!=null) {
                    if (userRole.equals("admin")) {
                        Employee employee = null;
                        BufferedReader reader = req.getReader();
                        while ((line = reader.readLine()) != null) {
                            jsonBuilder.append(line);
                        }
                        try {
                            employee = getEmployee(jsonBuilder);
                            logger.info("Employee posted successfully");
                        } catch (Exception e) {
                            resp.getWriter().println("Invalid input format. Only JSON format accepted");
                            logger.error("Exception while parsing json ", e);
                        }
                        employeeDatabaseServices.addEmployee(employee);
                        resp.getWriter().println("Employee added");
                    }else {
                        resp.getWriter().println("You don't have access to post");
                        logger.warn("Access Denied");
                    }
                }
                else {
                    resp.getWriter().println("You have to register first !!");
                    logger.info("User not found in database");
                }
            }
            else{
                resp.getWriter().println("Login First");
            }

        } catch (Exception e) {
            logger.error("Exception while adding an employee ", e);
            resp.getWriter().println("Error in adding an employee");
        }

    }

    public Employee getEmployee(StringBuilder jsonBuilder) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonBuilder.toString());

        int id = -1;
        String name = jsonObject.getString("empName");

        JSONObject addressObject = jsonObject.getJSONObject("address");
        String location = addressObject.getString("location");
        long pincode = addressObject.getLong("pincode");
        int deptId = jsonObject.getInt("deptId");

        return new Employee(id, name, new Address(location, pincode), deptId);
    }
}
