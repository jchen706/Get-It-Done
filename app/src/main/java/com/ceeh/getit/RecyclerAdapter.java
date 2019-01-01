package com.ceeh.getit;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceeh.getit.Model.Task;
import com.ceeh.getit.Model.User;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    private List<Task> taskList;

    public RecyclerAdapter( List<Task> _task) {
        this.taskList = _task;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView taskNameView;
        public TextView dueDateView;
        public Task mTask;
        public View mView;
        public TextView idView;
        public CheckBox checkBox;
        public MyViewHolder(View v) {
            super(v);
            mView = v;
            checkBox = (CheckBox) v.findViewById(R.id.checkBox2);
            idView = (TextView) v.findViewById(R.id.id_View_card);
            taskNameView = (TextView) v.findViewById(R.id.taskName_card);
            dueDateView = (TextView) v.findViewById(R.id.due_date_card);

        }
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int position) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }


    @Override
    public void  onBindViewHolder(MyViewHolder holder, int position) {
        final Task task =  taskList.get(position);
        holder.mTask = taskList.get(position);


        TextView taskName = holder.taskNameView;
        taskName.setText(task.get_name());

        TextView dueView = holder.dueDateView;
        String [] time = task.get_time().split(":");

        CheckBox checkBox = holder.checkBox;
        if (task.get_status().equals("Active")){
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }


        dueView.setText(task.get_dueDate().toString() + " " + time[0] + ":" + time[1] + " " + time[2]);

        TextView idView = holder.idView;
        idView.setText(task.get_id() + "");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtra("TASK_ID", task.get_id());

                context.startActivity(intent);
            }
        });





    }





    @Override
    public int getItemCount() {
        return taskList == null ? 0  : taskList.size();
    }







}
