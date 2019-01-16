package com.yekuwilfred.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import androidx.lifecycle.ViewModelProviders;

public class AddTask extends AppCompatActivity {
    private static final String DATE_FORMAT = "dd/MM/yy";

    private EditText mTitleField;
    private EditText mDescriptionField;
    private EditText mDateField;
    private Date mDateObject;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private FloatingActionButton mSaveBtn;

    private String mDateString;
    private String mDescription;
    private String mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        initialiseViews();
        createTask();

    }


    private void createTask() {
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTitle = mTitleField.getText().toString();
                mDescription = mDescriptionField.getText().toString();
                mDateString = mDateField.getText().toString();

                try {
                    mDateObject = simpleDateFormat.parse(mDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Task task = new Task(mTitle, mDescription, mDateObject);

                TaskViewModel viewModel = ViewModelProviders.of(AddTask.this).get(TaskViewModel.class);
                viewModel.insert(task);

                Toast.makeText(AddTask.this, mTitle + " " + mDateString + " " + mDescription, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    private void initialiseViews() {
        mTitleField = findViewById(R.id.task_title_field);
        mDescriptionField = findViewById(R.id.task_description_field);
        mDateField = findViewById(R.id.date_field);
        mSaveBtn = findViewById(R.id.save_btn);
    }


}
