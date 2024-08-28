package com.wm.todoapp.controllers;

import com.google.gson.JsonParseException;
import com.wm.todoapp.exceptions.DuplicateTaskException;
import com.wm.todoapp.models.Task;
import com.wm.todoapp.services.impl.ToDoServicesImplementation;
import com.wm.todoapp.services.interfaces.TodoServices;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/tasks")
public class TaskCrudOperations extends HttpServlet {
    private final TodoServices toDoServicesImplementation = ToDoServicesImplementation.getInstance();
    private final Logger logger = LoggerFactory.getLogger(TaskCrudOperations.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("Attempt fetch tasks through api call");
        resp.setContentType("application/json");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            try {
                String idParam = req.getParameter("id");
                String userName = (String) httpSession.getAttribute("userName");
                if (idParam != null && !idParam.isEmpty()) {
                    Task task = toDoServicesImplementation.getTaskById(Integer.parseInt(idParam));
                    resp.getWriter().write(task.toString());
                } else if (userName != null && !userName.isEmpty()) {
                    List<Task> tasks = toDoServicesImplementation.getTasksByUserName(userName);
                    StringBuilder json = new StringBuilder("{");
                    int i = tasks.size();
                    for (Task task : tasks) {
                        json.append("\"").append(task.getId()).append("\":").append(task);
                        if (i - 1 > 0) json.append(",");
                        i--;
                    }
                    json.append("}");
                    resp.getWriter().write(json.toString());
                    logger.info("Tasks fetched successfully through api call with userName {}", userName);

                } else {
                    resp.getWriter().write("{\"message\":\"not logged in\"}");
                    logger.info("Not logged In");
                }
            } catch (NumberFormatException e) {
                resp.getWriter().write("Invalid id parameter enter");
                logger.error("Exception while parsing id ", e);
            } catch (Exception e) {

                logger.error("Exception while fetching tasks through api call", e);
            }
        } else {
           resp.setHeader("logged","false");
        }
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("Attempt to add task through api call");

        resp.setContentType("application/json");
        resp.setStatus(200);
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            String userName = (String) httpSession.getAttribute("userName");
            BufferedReader reader = req.getReader();
            try {
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                Task task = null;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                try {
                    task = getTask(jsonBuilder);
                    task.setUserName(userName);
                    logger.info("Task posted successfully");
                } catch (Exception e) {
                    resp.getWriter().println("Invalid input format. Only JSON format accepted");
                    logger.error("Exception while parsing json ", e);
                }
                boolean isTaskAdded = toDoServicesImplementation.addTask(task);
                if (isTaskAdded) {
                    logger.info("Task Added through api call");
                    if (task != null) {
                        task.setId(ToDoServicesImplementation.getCurrentTaskId());
                        resp.getWriter().write(task.toString());
                    }
                } else {
                    logger.info("Problem while adding task through api call");
                    resp.getWriter().write("Problem while adding task through api call");

                }
            } catch (DuplicateTaskException e) {
                logger.error("Duplicate Task Exception");

                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("{\"message\":\"Already Task Exist\"}");
            } catch (Exception e) {

                logger.error("Exception while adding task through api calls", e);
            }
        } else {
            resp.getWriter().write("{}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("Attempt to update task through api call");
        resp.setContentType("application/json");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            String userName = (String) httpSession.getAttribute("userName");
            try {
                BufferedReader reader = req.getReader();
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                Task task = null;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                try {
                    task = getTask(jsonBuilder);
                    task.setUserName(userName);
                } catch (Exception e) {
                    resp.getWriter().println("Invalid input format. Only JSON format accepted");
                    logger.error("Exception while parsing json ", e);
                }
                if (task != null) {
                    int id = Integer.parseInt(req.getParameter("id"));
                    boolean isUpdated = toDoServicesImplementation.updateTask(id, task);
                    if (isUpdated) {
                        logger.info("Updated task successfully");
                        resp.getWriter().write("Task update successfully");
                    } else {
                        resp.getWriter().write("Something went wrong at database level");
                    }
                }
            } catch (Exception e) {

                logger.error("Exception while updating task through api call", e);
                resp.getWriter().write("exception occurred while updating");
            }
        } else {
            resp.getWriter().write("{}");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("Attempt to delete task through api call");
        resp.setContentType("application/json");
        String idParam = req.getParameter("id");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            Task task;
            try {
                if (idParam != null && !idParam.isEmpty()) {
                    task = toDoServicesImplementation.deleteTask(Integer.parseInt(idParam));
                    logger.info("Task deleted successfully through api call");
                    resp.getWriter().write(task.toString());
                } else {
                    resp.getWriter().println("Empty id param");
                    logger.info("Empty id parameter received");

                }
            } catch (NumberFormatException e) {
                resp.getWriter().println("Invalid id");
                logger.error("Exception while deleting task through api call with id {}", idParam, e);
            }
        } else {
            resp.getWriter().write("{}");
        }
    }

    public Task getTask(StringBuilder jsonBuilder) throws JsonParseException {
        JSONObject jsonObject = new JSONObject(jsonBuilder.toString());
        int id = -1;
        String title = jsonObject.getString("title");
        String description = jsonObject.getString("description");
        String priority = jsonObject.getString("priority");
        String timestampString = jsonObject.getString("duration");
        Timestamp timestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(timestampString);
            timestamp = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            logger.error("Exception while parsing timestamp", e);
        } catch (JsonParseException e) {
            throw new JsonParseException("Parsing Exception");
        }
        return new Task(id, title, description, priority, timestamp, null);
    }
}
