package com.yekuwilfred.tasker.model;

import android.content.Context;
import android.util.Log;

import com.yekuwilfred.tasker.utils.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TaskDataBase extends RoomDatabase {
    private static final String LOG_TAG = TaskDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "tasker";
    private static TaskDataBase sInstance;

    public static TaskDataBase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new Database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TaskDataBase.class, TaskDataBase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database Instance");
        return sInstance;
    }

    public abstract TaskDAO taskDao();
}
