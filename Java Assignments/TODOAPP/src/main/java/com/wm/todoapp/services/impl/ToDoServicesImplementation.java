package com.wm.todoapp.services.impl;

import com.wm.todoapp.exceptions.DuplicateTaskException;
import com.wm.todoapp.models.Task;
import com.wm.todoapp.models.User;
import com.wm.todoapp.repository.impl.DataBaseRepositoryImplementation;
import com.wm.todoapp.repository.interfaces.DatabaseOperations;
import com.wm.todoapp.services.interfaces.TodoServices;

import java.util.List;

public class ToDoServicesImplementation implements TodoServices {
    private  static  TodoServices toDoServicesImplementation;
    private final DatabaseOperations dataBaseRepositoryImplementation;
    private  ToDoServicesImplementation(){
        dataBaseRepositoryImplementation=DataBaseRepositoryImplementation.getInstance();
    }
    public static  synchronized  TodoServices getInstance(){
        if(toDoServicesImplementation==null){
            toDoServicesImplementation=new ToDoServicesImplementation();
        }
        return  toDoServicesImplementation;
    }
    @Override
    public boolean addTask(Task task) throws DuplicateTaskException {
        return dataBaseRepositoryImplementation.addTask(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return dataBaseRepositoryImplementation.getAllTasks();
    }

    @Override
    public Task getTaskById(int taskId) {
        return dataBaseRepositoryImplementation.getTaskById(taskId);
    }

    @Override
    public boolean updateTask(int taskId, Task task) {
        return dataBaseRepositoryImplementation.updateTask(taskId,task);
    }

    @Override
    public Task deleteTask(int taskId) {
        return dataBaseRepositoryImplementation.deleteTask(taskId);
    }

    @Override
    public  boolean validateUser(User user){
        return  dataBaseRepositoryImplementation.validateUser(user);
    }

    public static int getCurrentTaskId(){
      return   DataBaseRepositoryImplementation.getCurrentTaskId();
    }

    @Override
    public List<Task> getTasksByUserName(String userName) {
        return dataBaseRepositoryImplementation.getTasksByUserName(userName);
    }
}
