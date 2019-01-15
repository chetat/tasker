package com.yekuwilfred.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yekuwilfred.tasker.model.Task;
import com.yekuwilfred.tasker.model.TaskDataBase;
import com.yekuwilfred.tasker.model.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import static com.yekuwilfred.tasker.TasksListActivity.TASK_ITEM_ID;

public class AddTask extends AppCompatActivity {
    private static final String DATE_FORMAT = "dd/MM/yy";
    private static final int DEFAULT_TASK_ID = -1;

    private SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private EditText mTitleField;
    private EditText mDescriptionField;
    private EditText mDateField;
    Date dateObject;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    private FloatingActionButton mSaveBtn;

    private TaskDataBase mTaskDataBase;
    private String mDateString;
    private String mDescription;
    private String mTitle;
    private Intent mTaskIntent;
    private int mTaskId;
    private Task mTk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        //Create Database Instance
        mTaskDataBase = TaskDataBase.getInstance(getApplicationContext());
        initialiseViews();

        mTaskIntent = getIntent();

        if (mTaskIntent != null && mTaskIntent.hasExtra(TASK_ITEM_ID)) {
            if (mTaskId != DEFAULT_TASK_ID) {
                TextView addTaskTv = findViewById(R.id.add_task_tv);
                addTaskTv.setText(getString(R.string.update_task));
                mTaskId = mTaskIntent.getIntExtra(TASK_ITEM_ID, DEFAULT_TASK_ID);


                mTk = null;
                TaskViewModel viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
                viewModel.getTaskById(mTaskId).observe(this, new Observer<Task>() {
                    @Override
                    public void onChanged(Task task) {
                        mTk = task;
                       fillTextFields(mTk);
                        Log.i(":AddTask", "onChanged: " + mTk);
                    }
                });

                updateTask(mTk);
            }
        } else {
            addTask();
        }


    }
    public void fillTextFields(Task task){
        //Fill TextField with Task Data
        mTitleField.setText(task.getTitle());
        mDescriptionField.setText(task.getDescription());
        mDateField.setText(dateformat.format(task.getDate()));
    }

    public void updateTask(final Task task){

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTitle = mTitleField.getText().toString();
                mDescription = mDescriptionField.getText().toString();
                mDateString = mDateField.getText().toString();

                try {
                    dateObject = simpleDateFormat.parse(mDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Task uTask = new Task(mTitle, mDescription, dateObject);

                updateTask(uTask);
                TaskViewModel vm = ViewModelProviders.of(AddTask.this).get(TaskViewModel.class);
                vm.update(task);
            }
        });
    }




    private void addTask() {
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTitle = mTitleField.getText().toString();
                mDescription = mDescriptionField.getText().toString();
                mDateString = mDateField.getText().toString();

                try {
                    dateObject = simpleDateFormat.parse(mDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Task task = new Task(mTitle, mDescription, dateObject);


                TaskViewModel viewModel = ViewModelProviders.of(AddTask.this).get(TaskViewModel.class);
                viewModel.insert(task);


                Toast.makeText(AddTask.this, mTitle + " " + mDateString + " " + mDescription, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    private void initialiseViews() {
        mTitleField = findViewById(R.id.title_text_field);
        mDescriptionField = findViewById(R.id.description_text_field);
        mDateField = findViewById(R.id.date_field);
        mSaveBtn = findViewById(R.id.save_btn);
    }


}
