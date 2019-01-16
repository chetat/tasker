package com.yekuwilfred.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yekuwilfred.tasker.adapters.TaskRecyclerViewAdapter;
import com.yekuwilfred.tasker.model.Task;
import com.yekuwilfred.tasker.model.TaskViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TasksListActivity extends AppCompatActivity implements TaskRecyclerViewAdapter.ItemClickListener {
    public static final String TASK_ITEM_ID = "com.yekuwilfred.tasker.TASK_ITEM_ID";
    public static final int EDIT_TASK_CODE = 12;
    private RecyclerView mTaskRv;
    private TaskRecyclerViewAdapter mTaskAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        mTaskRv = findViewById(R.id.task_recyclerview);


        //Set Layout Manager for RecyclerView
        mTaskAdapter = new TaskRecyclerViewAdapter(this, this);
        mTaskRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        mTaskRv.setAdapter(mTaskAdapter);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTaskIntent = new Intent(TasksListActivity.this, AddTask.class);
                startActivity(addTaskIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTasks();
    }

    private void getTasks() {
    TaskViewModel viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        viewModel.getTaskList().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTaskAdapter.setTaskList(tasks);
            }
        });
    }


    @Override
    public void onItemClick(int itemId) {
        Intent taskIntent = new Intent(getApplicationContext(), EditTask.class);
        taskIntent.putExtra(TASK_ITEM_ID, itemId);
        startActivity(taskIntent);
    }


}
