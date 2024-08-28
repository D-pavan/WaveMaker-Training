package com.wm.todoapp.services.interfaces;

import com.wm.todoapp.exceptions.DuplicateTaskException;
import com.wm.todoapp.models.Task;
import com.wm.todoapp.models.User;

import java.util.List;

public interface TodoServices {
    List<Task> getAllTasks();
    boolean addTask(Task task) throws DuplicateTaskException;
    Task deleteTask(int taskId);
    boolean updateTask(int taskId,Task task);
    Task getTaskById(int taskId);
    boolean validateUser(User user);
    List<Task> getTasksByUserName(String userName);
}