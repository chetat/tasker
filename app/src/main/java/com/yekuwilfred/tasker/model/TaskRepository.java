package com.yekuwilfred.tasker.model;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TaskRepository {

    private TaskDAO mTaskDao;
    private LiveData<List<Task>> mTaskList;


    public TaskRepository(Application application) {
        TaskDataBase taskDataBase = TaskDataBase.getInstance(application);
        mTaskDao = taskDataBase.taskDao();
        mTaskList = mTaskDao.getAllTasks();
    }

    //Retrieve tasks from Database
    public LiveData<List<Task>> getAllTasks(){
        return mTaskList;
    }


    public LiveData<Task> getTaskById(int taskId){
       return mTaskDao.getTaskById(taskId);
    }


    //Insert task in Db using AsyncTask
    public void insertTask(Task task){
        new insertAsyncTask(mTaskDao).execute(task);
    }


    public void deleteTask(Task task){
        new deleteAsyncTask(mTaskDao).execute(task);
    }

    public void updateTask(Task task){
        new updateAsyncTask(mTaskDao).execute(task);
    }

    /*
     * Insert Task AsyncTask
     * */
    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDAO mAsyncTaskDAO;

        insertAsyncTask(TaskDAO taskDao) {
            mAsyncTaskDAO = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDAO.addTask(tasks[0]);
            return null;
        }
    }

    /*
     * Delete Task AsyncTask
     * */

    private static class deleteAsyncTask  extends AsyncTask<Task, Void, Void>{

        private TaskDAO mAsyncTaskDao;

        deleteAsyncTask(TaskDAO taskDao) {
            mAsyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.deleteTask(tasks[0]);
            return null;
        }
    }


    /*
    * Update Task AsyncTask
    * */
    private static class updateAsyncTask extends AsyncTask<Task, Void, Void>{

       private TaskDAO mAsyncTaskDao;

        public updateAsyncTask(TaskDAO asyncTaskDao) {
            mAsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.updateTask(tasks[0]);
            return null;
        }
    }
}
