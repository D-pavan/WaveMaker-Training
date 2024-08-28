package com.wm.todoapp.repository.impl;

import com.wm.todoapp.exceptions.DuplicateTaskException;
import com.wm.todoapp.exceptions.TaskNotFoundException;
import com.wm.todoapp.models.Task;
import com.wm.todoapp.models.User;
import com.wm.todoapp.repository.DatabaseRepository;
import com.wm.todoapp.repository.interfaces.DatabaseOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseRepositoryImplementation implements DatabaseOperations {
    private final Connection connection;
    private static DatabaseOperations dataBaseRepositoryImplementation;
    private final Logger logger = LoggerFactory.getLogger(DataBaseRepositoryImplementation.class);
    private static int currId = 0;

    private DataBaseRepositoryImplementation() {
        connection = DatabaseRepository.getInstance().getConnection();
    }

    public static synchronized DatabaseOperations getInstance() {
        if (dataBaseRepositoryImplementation == null) {
            dataBaseRepositoryImplementation = new DataBaseRepositoryImplementation();
        }
        return dataBaseRepositoryImplementation;
    }

    @Override
    public List<Task> getAllTasks() {
        logger.debug("Attempt to fetch records from database");
        List<Task> tasks = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TASKS");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(new Task(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(5), resultSet.getTimestamp(4),resultSet.getString(6)));
            }
            preparedStatement.close();
            resultSet.close();
            logger.info("All Tasks fetched successfully from database");
        } catch (SQLException e) {

            logger.error("Exception while fetching all tasks from database ", e);
        }
        return tasks;
    }

    @Override
    public boolean addTask(Task task) throws DuplicateTaskException {
        logger.debug("Attempt to add a task to database");
        int row = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(" INSERT INTO TASKS (TITLE,DESCRIPTION,DURATION,PRIORITY,USERNAME)  VALUES(?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setTimestamp(3, task.getDuration());
            preparedStatement.setString(4, task.getPriority());
            preparedStatement.setString(5,task.getUserName());
            row = preparedStatement.executeUpdate();
            if (row > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    currId = resultSet.getInt(1);
                }
            }
            preparedStatement.close();
            logger.info("Task added to database successfully");
        } catch (SQLException e) {
            logger.error("Exception while adding task to database", e);
            if (e instanceof SQLIntegrityConstraintViolationException)
                throw new DuplicateTaskException("Duplicate task");

        }
        return row > 0;
    }

    @Override
    public Task getTaskById(int taskId) {
        logger.debug("Attempt to get Task from database with ID : {}", taskId);
        Task task = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TASKS WHERE ID=?");
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                task = new Task(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(5), resultSet.getTimestamp(4),resultSet.getString(6));
            }
            resultSet.close();
            preparedStatement.close();
            if (task == null) throw new TaskNotFoundException("No task found in database with id : " + taskId);
            logger.info("Task fetched from database with id {}", taskId);
        } catch (TaskNotFoundException e) {
            logger.error("No task found with id {}", taskId, e);
        } catch (SQLException e) {

            logger.error("Exception while fetching task from database with id : {}", taskId, e);
        }
        return task;
    }

    @Override
    public boolean updateTask(int taskId, Task task) {
        logger.debug("Attempt to update task from database with id : {}", taskId);
        int row = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TASKS SET TITLE=?,DESCRIPTION=?,DURATION=?,PRIORITY=? WHERE ID = ?");
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setTimestamp(3, task.getDuration());
            preparedStatement.setString(4, task.getPriority());
            preparedStatement.setInt(5, taskId);
            row = preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("Task updated successfully with id : {}", taskId);
        } catch (SQLException e) {

            logger.error("Exception while updating task with id {}", taskId, e);
        }
        return row > 0;
    }

    @Override
    public Task deleteTask(int taskId) {
        logger.debug("Attempt to delete task from database with id : {}", taskId);
        Task task = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM TASKS WHERE ID=?");
            task = getTaskById(taskId);
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("Task deleted from database with id : {}", taskId);
        } catch (Exception e) {

            logger.error("Exception while deleting the task from database with id {}", taskId, e);
        }
        return task;
    }

    public static int getCurrentTaskId() {
        return currId;
    }

    @Override
    public boolean validateUser(User user){
        logger.debug("Attempt to validate user");
       try {
           PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD = ?");
           preparedStatement.setString(1, user.getUserName());
           preparedStatement.setString(2, user.getPassword());
           ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                logger.info("Validated users successfully");
                return true;
            }
            else{
                logger.info("Invalid credentials");
            }
       }
       catch (SQLException e){
           logger.error("Exception while validating user",e);
       }
       return false;
    }

    @Override
    public List<Task> getTasksByUserName(String userName) {
        logger.debug("Attempt to fetch records from database with userName {}",userName);
        List<Task> tasks = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TASKS WHERE USERNAME=?");
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(new Task(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(5), resultSet.getTimestamp(4),resultSet.getString(6)));
            }
            preparedStatement.close();
            resultSet.close();
            logger.info("All Tasks fetched successfully from database with username : {}",userName);
        } catch (SQLException e) {

            logger.error("Exception while fetching all tasks from database with userName {} ",userName, e);
        }
        return tasks;
    }
}
