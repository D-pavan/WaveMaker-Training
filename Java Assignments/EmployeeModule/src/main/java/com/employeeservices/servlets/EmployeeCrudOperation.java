package com.employeeservices.servlets;

import com.employeeservices.models.Address;
import com.employeeservices.models.Department;
import com.employeeservices.models.Employee;
import com.employeeservices.services.impl.EmployeeDatabaseServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;


@WebServlet(urlPatterns = "/employee_crud")
public class EmployeeCrudOperation extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(EmployeeCrudOperation.class);
    private final EmployeeDatabaseServices employeeDatabaseServices = EmployeeDatabaseServices.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to fetch  employees through api call ");
        resp.setContentType("application/json");
        try {
            HttpSession httpSession = req.getSession(false);
            if (httpSession != null) {
                String idParam = req.getParameter("id");
                String name = req.getParameter("name");
                if (idParam != null && !idParam.isEmpty()) {
                    int id = Integer.parseInt(idParam);
                    Employee employee = employeeDatabaseServices.getEmployeeById(id);
                    if (employee != null)
                        resp.getWriter().println(getJSON(employee, employeeDatabaseServices.getDepartment(employee.getDeptId())));
                    else resp.getWriter().write("No Employee found with given Id");
                    logger.info("Employee fetched through api call");
                } else if (name != null && !name.isEmpty()) {
                    List<Employee> employees = employeeDatabaseServices.getEmployeeByName(name);
                    for (Employee employee : employees) {
                        resp.getWriter().println(getJSON(employee, employeeDatabaseServices.getDepartment(employee.getDeptId())));
                    }
                    if (employees.isEmpty()) resp.getWriter().write("No employee found the given name");
                    logger.info("Employees fetched by name");
                } else {
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
                }
            } else {
                resp.getWriter().println("Login first");
            }
        } catch (Exception e) {
            System.out.println(e);
            logger.error("Exception while fetching employee through api call ");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to post an employee");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try {
            HttpSession httpSession = req.getSession(false);
            String userRole = LoginServlet.userRole;
            if (httpSession != null) {
                if (userRole != null) {
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
                    } else {
                        resp.getWriter().println("You don't have access to post");
                    }
                } else {
                    resp.getWriter().println("You have to register first !!");
                }
            } else {
                resp.getWriter().println("Login First");
            }

        } catch (Exception e) {
            logger.error("Exception while adding an employee ", e);
            resp.getWriter().println("Error in adding an employee");
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to delete employee from api call");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            HttpSession httpSession = req.getSession(false);
            String userRole = LoginServlet.userRole;
            if (httpSession != null) {
                if (userRole != null) {
                    System.out.println("user role " + userRole);
                    if (userRole.equals("admin")) {
                        String idParam = req.getParameter("id");
                        if (idParam != null && !idParam.isEmpty()) {
                            int id = Integer.parseInt(idParam);
                            Employee employee = employeeDatabaseServices.deleteEmployee(id);
                            logger.info("Employee deleted through api call with id : {}", employee.getEmpId());
                            resp.getWriter().println(getJSON(employee, employeeDatabaseServices.getDepartment(employee.getDeptId())));
                        } else {
                            resp.getWriter().write("Empty id parameter received");
                        }
                    } else {
                        resp.getWriter().println("You don't have access to delete");
                    }
                } else {
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
            String userRole = LoginServlet.userRole;
            if (httpSession != null) {
                if (userRole != null) {
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
                    }
                } else {
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

