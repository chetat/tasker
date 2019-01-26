package com.yekuwilfred.tasker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yekuwilfred.tasker.model.Task;
import com.yekuwilfred.tasker.model.TaskViewModel;
import com.yekuwilfred.tasker.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class AddTask extends AppCompatActivity {
    private static final String DATE_FORMAT = Constants.DATE_FORMAT;
    private static final String TIME_FORMAT = Constants.TIME_FORMAT;
    private static final boolean IS24HOUR = true;

    private EditText mTitleField;
    private EditText mDescriptionField;
    private EditText mDateField;
    private EditText mTimeField;

    private Button setDateBtn;
    private Button setTimeBtn;
    private FloatingActionButton mSaveBtn;

    private Date mDateObject;
    SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private String mDateString;
    private String mDescription;
    private String mTitle;
    private Calendar mCalendar;
    private long mTaskTime;
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);


        initialiseViews();
        mCalendar = Calendar.getInstance();
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(AddTask.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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

        //Displays Time Picker Dialog
        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialise with hours and time of the day
                mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                mMinute = mCalendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(
                                AddTask.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(
                                            TimePicker view, int hourOfDay, int minute) {
                                        mTimeField.setText(hourOfDay + ":" + minute);
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        c.set(Calendar.MINUTE, minute);
                                        if (!mTimeField.getText()
                                                .toString()
                                                .matches("\\d{2}:\\d{2}")) {
                                            Toast.makeText(
                                                    AddTask.this,
                                                    "Please enter a valid time",
                                                    Toast.LENGTH_LONG).show();

                                        } else {
                                            mTaskTime = c.getTime().getTime();
                                        }


                                    }
                                }, mHour, mMinute, IS24HOUR);
                timePickerDialog.show();
            }
        });


        Intent intent =
                new Intent(this, TaskAlertBroReceiver.class);
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager managerCompat =
                (AlarmManager) this.getSystemService(AddTask.ALARM_SERVICE);
        managerCompat.setExact(
                AlarmManager.RTC_WAKEUP, mTaskTime, pendingIntent);

        createTask();

    }


    private void createTask() {
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTitle = mTitleField.getText().toString();
                mDescription = mDescriptionField.getText().toString();
                mDateString = mDateField.getText().toString();

                if (mTitle.isEmpty() || mDescription.isEmpty()) {
                    Toast.makeText(
                            AddTask.this,
                            "Please Fill the text fields",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        mDateObject = simpleDateFormat.parse(mDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Task task =
                            new Task(
                                    mTitle,
                                    mDescription,
                                    mDateObject,
                                    mTaskTime);

                    TaskViewModel viewModel =
                            ViewModelProviders.of(AddTask.this)
                                    .get(TaskViewModel.class);

                    viewModel.insert(task);
                    Log.i("ADD TASK TAG: ", "onClick: " + mTaskTime);

                    Toast.makeText(
                            AddTask.this,
                            mTitle + " " + mDateString + " " +
                                    mDescription,
                            Toast.LENGTH_LONG).show();
                    finish();
                }


            }
        });
    }


    private void initialiseViews() {

        setDateBtn = findViewById(R.id.date_btn);
        setTimeBtn = findViewById(R.id.time_btn);
        mSaveBtn = findViewById(R.id.save_btn);


        mTitleField = findViewById(R.id.task_title_field);
        mDescriptionField = findViewById(R.id.task_description_field);
        mDateField = findViewById(R.id.date_field);
        mTimeField = findViewById(R.id.time_field);
    }


}
