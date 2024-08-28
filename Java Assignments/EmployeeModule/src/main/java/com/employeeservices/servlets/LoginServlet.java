package com.employeeservices.servlets;

import com.employeeservices.models.User;
import com.employeeservices.services.impl.EmployeeDatabaseServices;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebServlet(urlPatterns = "/Login")
public class LoginServlet extends HttpServlet {
    private final Logger logger= LoggerFactory.getLogger(LoginServlet.class);
    protected  static String userRole=null;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt Login");
        EmployeeDatabaseServices employeeDatabaseServices=EmployeeDatabaseServices.getInstance();
        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            if (values.length == 2) {
                String username = values[0];
                String password = values[1];
                HttpSession httpSession = req.getSession();
                httpSession.setAttribute("user", httpSession.getId());
                resp.addCookie(new Cookie("user", username));
                resp.addCookie(new Cookie("password", password));
                userRole = employeeDatabaseServices.authenticateUser(new User(username,password));
                resp.getWriter().println("role "+userRole);
                if(userRole!=null){
                    resp.getWriter().println("You logged successfully !!!");
                    logger.info("Login success");
                }
                else {
                    resp.getWriter().println("Invalid Credentials");
                    logger.info("invalid Credentials");
                }
            } else {
                resp.getWriter().println("Please provide credentials");
                logger.info("No credentials provided");
            }
        } else {
            resp.getWriter().println("No Basic authorization provided");
            logger.info("No basic authorization provided");
        }
    }

}
