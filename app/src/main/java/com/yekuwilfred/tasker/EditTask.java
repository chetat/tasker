package com.yekuwilfred.tasker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yekuwilfred.tasker.model.Task;
import com.yekuwilfred.tasker.model.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.yekuwilfred.tasker.TasksListActivity.TASK_ITEM_ID;
import static com.yekuwilfred.tasker.utils.Constants.DATE_FORMAT;
import static com.yekuwilfred.tasker.utils.Constants.TIME_FORMAT;

public class EditTask extends AppCompatActivity {

    private static final int DEFAULT_TASK_ID = -1;
    private static final boolean IS24HOUR = true;


    SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    SimpleDateFormat simpleTimeFormat =
            new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());

    private String mDateString;
    private String mDescription;
    private String mTitle;
    private int mTaskId;
    private Date mDateObject;
    private Calendar mCalendar;
    private long mTaskTime;
    private int mYear, mMonth, mDay, mHour, mMinute;


    private EditText mTimeField;
    private EditText mTitleField;
    private EditText mDescriptionField;
    private EditText mDateField;
    private FloatingActionButton mSaveBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        mTitleField = findViewById(R.id.task_title_field);
        mDescriptionField = findViewById(R.id.task_description_field);
        mDateField = findViewById(R.id.date_field);
        TextView addTaskTv = findViewById(R.id.add_task_tv);
        mSaveBtn = findViewById(R.id.save_btn);
        Button setDateBtn = findViewById(R.id.date_btn);
        Button setTimeBtn = findViewById(R.id.time_btn);
        mTimeField = findViewById(R.id.time_field);

        addTaskTv.setText(getString(R.string.update_task));

        mCalendar = Calendar.getInstance();
        Intent taskIntent = getIntent();

        /*
         * Check if intent isn't null and get task extra Id for retrieving
         * task in db that needs to be updated
         * */
        if (taskIntent != null && taskIntent.hasExtra(TASK_ITEM_ID)) {
            if (mTaskId != DEFAULT_TASK_ID) {

                mTaskId = taskIntent.getIntExtra(
                        TASK_ITEM_ID,
                        DEFAULT_TASK_ID);

                TaskViewModel viewModel =
                        ViewModelProviders.of(this)
                                .get(TaskViewModel.class);
                viewModel.getTaskById(mTaskId)
                        .observe(this, new Observer<Task>() {
                            @Override
                            public void onChanged(Task task) {
                                fillTextFields(task);
                                updateTask(task);
                            }
                        });
            }
        }

        //Display Date Picker Dialog when button is clicked
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(
                                    DatePicker view, int year,
                                    int month,
                                    int dayOfMonth) {
                                mDateField.setText(
                                        dayOfMonth + getString(R.string.slash) +
                                                (month + 1) +
                                                getString(R.string.slash) +
                                                year);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //Displays Time Picker Dia
        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialise with hours and time of the day
                mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                mMinute = mCalendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(
                                EditTask.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(
                                            TimePicker view,
                                            int hourOfDay,
                                            int minute) {
                                        mTimeField.setText(
                                                hourOfDay + ":" + minute);
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        c.set(Calendar.MINUTE, minute);
                                        mTaskTime = c.getTime().getTime();

                                    }
                                }, mHour, mMinute, IS24HOUR);
                timePickerDialog.show();
            }
        });


    }


    public void updateTask(final Task task) {

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTitle = mTitleField.getText().toString();
                mDescription = mDescriptionField.getText().toString();
                mDateString = mDateField.getText().toString();

                //Format String to date object
                try {
                    mDateObject = simpleDateFormat.parse(mDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                task.setTitle(mTitle);
                task.setDescription(mDescription);
                task.setDate(mDateObject);
                task.setTime(mTaskTime);

                TaskViewModel vm = ViewModelProviders.of(
                        EditTask.this)
                        .get(TaskViewModel.class);
                vm.update(task);
                Toast.makeText(
                        EditTask.this,
                        task.getTitle() +
                                " " + task.getDate(),
                        Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    public void fillTextFields(Task task) {
        //Fill TextField with Task Data
        mTitleField.setText(task.getTitle());
        mDescriptionField.setText(task.getDescription());
        mDateField.setText(simpleDateFormat.format(task.getDate()));
        mTimeField.setText(simpleTimeFormat.format(task.getTime()));
    }

}
