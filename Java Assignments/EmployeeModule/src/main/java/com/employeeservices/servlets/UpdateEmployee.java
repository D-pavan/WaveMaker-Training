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

@WebServlet(urlPatterns = "/updateEmployeeById")
public class UpdateEmployee extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(UpdateEmployee.class);

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to update employee through api call");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        EmployeeDatabaseServices employeeDatabaseServices = EmployeeDatabaseServices.getInstance();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try {
            HttpSession httpSession = req.getSession(false);
            if (httpSession != null) {
                Cookie[] cookies = req.getCookies();
                String userName = null, userPassword = null;
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) userName= cookie.getValue();
                    else userPassword = cookie.getValue();
                }
                String userRole = employeeDatabaseServices.authenticateUser(new User(userName,userPassword));
                if(userRole !=null) {
                    if (userRole.equals("admin")) {

                        Employee employee = null;
                        BufferedReader reader = req.getReader();
                        while ((line = reader.readLine()) != null) {
                            jsonBuilder.append(line);
                        }
                        int empId = Integer.parseInt(req.getParameter("id"));
                        try {
                            employee = getEmployee(jsonBuilder);
                        } catch (Exception e) {
                            resp.getWriter().write("Exception while parsing JSON received through api call");
                            logger.error("Exception while parsing JSON received through api call", e);
                        }
                        employeeDatabaseServices.updateEmployee(empId, employee);
                        logger.info("Employee update through api call successfully");
                        resp.getWriter().write("Employee updated successfully!!");
                    } else {
                        resp.getWriter().println("You don't have access");
                        logger.warn("Access Denied");
                    }
                }
                else{
                    resp.getWriter().println("Register First");
                }
            } else {
                resp.getWriter().println("Login First");
            }
        } catch (NumberFormatException e) {
            resp.getWriter().println("Invalid Id");
            logger.error("Exception while converting received  id parameter", e);
        } catch (Exception e) {
            resp.getWriter().println("Error in updating an employee");
            logger.error("Exception while updating employee through api call");
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
