package com.yekuwilfred.tasker.model;

import android.app.Application;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TaskViewModel extends AndroidViewModel {

    private static final String TAG = TaskViewModel.class.getSimpleName();

    private TaskRepository mTaskRepository;

    private LiveData<List<Task>> taskList;

//Initiatilise ViewModel Constructor
    public TaskViewModel(Application application){
        super(application);
        mTaskRepository = new TaskRepository(application);
        Log.i(TAG, "TaskViewModel: Actively Retrieving Tasks From Database");
        taskList = mTaskRepository.getAllTasks();
    }

    public LiveData<Task> getTaskById(int taskId){
        return mTaskRepository.getTaskById(taskId);
    }

    public LiveData<List<Task>> getTaskList(){
        if (taskList == null){
            taskList = mTaskRepository.getAllTasks();
        }
        return taskList;
    }

    public void insert(Task task){
        mTaskRepository.insertTask(task);
    }

    public void update(Task task){
        mTaskRepository.updateTask(task);
    }

    public void delete(Task task){
        mTaskRepository.deleteTask(task);
    }

}
