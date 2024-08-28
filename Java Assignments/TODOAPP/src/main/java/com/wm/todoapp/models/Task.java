package com.wm.todoapp.models;
import java.sql.Timestamp;
import java.util.Objects;

public class Task {
    private  int id;
    private String title,description,priority;
    private Timestamp duration;
    private String  userName;

    public Task(int id, String title, String description, String priority, Timestamp duration,String userName) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.duration = duration;
        this.userName=userName;
        this.id=id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Timestamp getDuration() {
        return duration;
    }

    public void setDuration(Timestamp duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(getTitle(), task.getTitle())  && Objects.equals(getPriority(), task.getPriority()) && Objects.equals(getDuration(), task.getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getPriority(), getDuration());
    }


    @Override
    public String toString() {
        return  "{" +
                "\"id\": " + getId() + ", " +
                "\"title\": \"" + getTitle() + "\", " +
                "\"description\": \"" + getDescription() + "\", " +
                "\"priority\": \"" + getPriority() + "\", " +
                "\"duration\": \"" + getDuration().toString() + "\"" +
                "}";
    }
}
