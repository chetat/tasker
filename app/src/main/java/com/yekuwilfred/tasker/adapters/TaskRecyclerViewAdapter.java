package com.yekuwilfred.tasker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yekuwilfred.tasker.R;
import com.yekuwilfred.tasker.model.Task;
import com.yekuwilfred.tasker.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskVH> {


    //Date Format Constant
    private static final String DATE_FORMAT = Constants.DATE_FORMAT;

    private final Context mContext;
    //Handle Item Clicks
    private  ItemClickListener mItemClickListener;

    private List<Task> mTaskList;
    private LayoutInflater mInflater;

    //Date Formater
    private SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
   // private SimpleTimeZone timeZone = new SimpleTimeZone(0, )

    public TaskRecyclerViewAdapter(Context context, ItemClickListener itemClickListener){
        mContext = context;
        mItemClickListener = itemClickListener;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TaskVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.task_item, parent, false);
        return new TaskVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskVH holder, int position) {
        String taskTitle = mTaskList.get(position).getTitle();
        String taskDescription = mTaskList.get(position).getDescription();
        String taskDate = dateformat.format(mTaskList.get(position).getDate());


        holder.title.setText(taskTitle);
        holder.description.setText(taskDescription);
        holder.date.setText(taskDate);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mTaskList == null){
            return 0;
        }
        return mTaskList.size();
    }

    //Get retutn all tasks
    public List<Task> getTaskList(){
        return mTaskList;
    }

    /**
     * When data changes, this method updates the list of tasks
     * and notifies the adapter to use the new values on it
     */
    public void setTaskList(List<Task> tasks){
        mTaskList = tasks;
        notifyDataSetChanged();
    }


    public interface ItemClickListener{
        void onItemClick(int itemId);
    }

    class TaskVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView description;
        private TextView date;

        TaskVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title);
            description = itemView.findViewById(R.id.task_description);
            date = itemView.findViewById(R.id.task_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemId = mTaskList.get(getAdapterPosition()).getTaskId();
            mItemClickListener.onItemClick(itemId);
        }
    }

}
