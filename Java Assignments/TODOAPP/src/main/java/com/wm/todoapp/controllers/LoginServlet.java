package com.wm.todoapp.controllers;

import com.google.gson.JsonParseException;
import com.wm.todoapp.models.User;
import com.wm.todoapp.services.impl.ToDoServicesImplementation;
import com.wm.todoapp.services.interfaces.TodoServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    TodoServices todoServices = ToDoServicesImplementation.getInstance();
    private final Logger logger = LoggerFactory.getLogger(LoginServlet.class);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("attempt to login");
        resp.setContentType("application/json");
        BufferedReader reader = req.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        User user = getUser(jsonBuilder);
        if (todoServices.validateUser(user)) {
            resp.setStatus(200);
            //resp.getWriter().write("{\"isValidUser\" : \"true\"}");
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("userName", user.getUserName());

        } else {
            resp.setStatus(409);
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user");
            // resp.getWriter().write("{\"isValidUser\" : \"false\"}");
        }

    }

    public User getUser(StringBuilder jsonBuilder) throws JsonParseException {
        JSONObject jsonObject = new JSONObject(jsonBuilder.toString());
        return new User(jsonObject.getString("userName"), jsonObject.getString("password"));
    }
}
