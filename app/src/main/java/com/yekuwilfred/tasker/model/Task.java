package com.yekuwilfred.tasker.model;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "task")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int taskId;
    private String title;
    private String description;
    private Date date;
    private long time;


    public Task(int taskId, String title, String description, Date date, long time) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    @Ignore
    public Task(String title, String description, Date date, long time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
