package com.yekuwilfred.tasker.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDAO {

    @Query("select * from task order by date")
    LiveData<List<Task>> getAllTasks();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Insert
    void addTask(Task task);

    @Query("select * from task where taskId = :id ")
    LiveData<Task> getTaskById(int id);

}
