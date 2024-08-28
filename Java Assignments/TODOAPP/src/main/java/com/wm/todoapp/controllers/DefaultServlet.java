package com.wm.todoapp.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = "")
public class DefaultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String uri=req.getRequestURI();
        resp.setContentType("text/html");
        if (session != null && session.getAttribute("userName") != null) {
            if(!uri.equals("/TODOAPP/index.html")) resp.sendRedirect(req.getContextPath() + "/index.html");
            System.out.println("logged in" +uri);
        } else if(session==null){
            resp.sendRedirect(req.getContextPath() + "/login.html");
        }
    }
}
