package com.yekuwilfred.tasker;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.yekuwilfred.tasker.TasksListActivity.TASK_ITEM_ID;

public class EditTask extends AppCompatActivity {

    private static final String DATE_FORMAT = "dd/MM/yy";
    private static final int DEFAULT_TASK_ID = -1;

    private EditText mTitleField;
    private EditText mDescriptionField;
    private EditText mDateField;
    private Date mDateObject;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private FloatingActionButton mSaveBtn;

    private String mDateString;
    private String mDescription;
    private String mTitle;
    private int mTaskId;
    private TextView mAddTaskTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        mTitleField = findViewById(R.id.task_title_field);
        mDescriptionField = findViewById(R.id.task_description_field);
        mDateField = findViewById(R.id.date_field);
        mAddTaskTv = findViewById(R.id.add_task_tv);
        mSaveBtn = findViewById(R.id.save_btn);

        mAddTaskTv.setText(getString(R.string.update_task));


        Intent taskIntent = getIntent();

        /*
         * Check if intent isn't null and get task extra Id for retrieving
         * task in db that needs to be updated
         * */
        if (taskIntent != null && taskIntent.hasExtra(TASK_ITEM_ID)) {
            if (mTaskId != DEFAULT_TASK_ID) {

                mTaskId = taskIntent.getIntExtra(TASK_ITEM_ID, DEFAULT_TASK_ID);

                TaskViewModel viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
                viewModel.getTaskById(mTaskId).observe(this, new Observer<Task>() {
                    @Override
                    public void onChanged(Task task) {
                        fillTextFields(task);
                        updateTask(task);
                    }
                });
            }
        }
    }


    public void updateTask(final Task task) {

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


                task.setTitle(mTitle);
                task.setDescription(mDescription);
                task.setDate(mDateObject);

                TaskViewModel vm = ViewModelProviders.of(EditTask.this).get(TaskViewModel.class);
                vm.update(task);
                Toast.makeText(EditTask.this, task.getTitle() + " " + task.getDate(), Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    public void fillTextFields(Task task) {
        //Fill TextField with Task Data
        mTitleField.setText(task.getTitle());
        mDescriptionField.setText(task.getDescription());
        mDateField.setText(simpleDateFormat.format(task.getDate()));
    }

}
