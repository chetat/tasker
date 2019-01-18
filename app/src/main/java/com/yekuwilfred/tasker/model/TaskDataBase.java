package com.yekuwilfred.tasker.model;

import android.content.Context;
import android.util.Log;

import com.yekuwilfred.tasker.utils.DateConverter;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TaskDataBase extends RoomDatabase {
    private static final String LOG_TAG = TaskDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "tasker";
    private static TaskDataBase sInstance;

    static TaskDataBase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new Database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TaskDataBase.class, TaskDataBase.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database Instance");
        return sInstance;
    }

    //Migrate database to new version after adding time column
    private static Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE task ADD COLUMN time INTEGER");
        }
    };
    public abstract TaskDAO taskDao();
}
