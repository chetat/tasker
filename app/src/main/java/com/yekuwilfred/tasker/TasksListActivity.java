package com.yekuwilfred.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yekuwilfred.tasker.adapters.RvEmptyObserver;
import com.yekuwilfred.tasker.adapters.TaskRecyclerViewAdapter;
import com.yekuwilfred.tasker.model.Task;
import com.yekuwilfred.tasker.model.TaskViewModel;
import com.yekuwilfred.tasker.settings.SettingsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TasksListActivity extends AppCompatActivity implements TaskRecyclerViewAdapter.ItemClickListener {
    public static final String TASK_ITEM_ID = "com.yekuwilfred.tasker.TASK_ITEM_ID";
    public static final int EDIT_TASK_CODE = 12;
    private RecyclerView mTaskRv;
    private TaskRecyclerViewAdapter mTaskAdapter;
    private TaskViewModel mViewModel;
    private TextView mEmptyRv;
    private FloatingActionButton mAddTaskFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        mTaskRv = findViewById(R.id.task_recyclerview);


        RvEmptyObserver rvEmptyObserver = new RvEmptyObserver(mTaskRv, mEmptyRv);


        //Set Layout Manager for RecyclerView
        mTaskAdapter = new TaskRecyclerViewAdapter(this, this);
        mTaskAdapter.registerAdapterDataObserver(rvEmptyObserver);
        mTaskRv.setAdapter(mTaskAdapter);
        mTaskRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));



        mViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

         /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                List<Task> taskList = mTaskAdapter.getTaskList();
                mViewModel.delete(taskList.get(position));

            }
        }).attachToRecyclerView(mTaskRv);


        mAddTaskFab = findViewById(R.id.add_task_fab);

        mAddTaskFab.setOnClickListener(new View.OnClickListener() {
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

        mViewModel.getTaskList().observe(this, new Observer<List<Task>>() {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.settings:
                //Intent menuIntent = new Intent(this, SettingsActivity.class);
                //startActivity(menuIntent);
                Toast.makeText(this, "Settings to be implemented", Toast.LENGTH_LONG).show();
                break;
            case R.id.about:
                Toast.makeText(this, "About to be Implemented", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
