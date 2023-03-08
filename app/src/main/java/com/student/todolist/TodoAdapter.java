package com.student.todolist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private ArrayList<TodoModel> todoModelArrayList;
    private final DBHelper dbHelper;
    public Context context;
//    private ArrayList<TodoModel> itemsPendingRemoval;
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
//    private Handler handler = new Handler(); // hanlder for running delayed runnables
//    HashMap<TodoModel, Runnable> pendingRunnables = new HashMap<TodoModel, Runnable>(); // map of items to pending runnable, to cancel the removal


    public TodoAdapter(ArrayList<TodoModel> todoModelArrayList, DBHelper dbHelper, Context context) {
        this.todoModelArrayList = todoModelArrayList;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView.setText(todoModelArrayList.get(position).getTask());
        holder.itemView.setTag(todoModelArrayList.get(position).getId());
        holder.itemView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.textView);
            popupMenu.inflate(R.menu.pop_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popmenu_delete:
                        popDelete(todoModelArrayList.get(position).getId(),todoModelArrayList.get(position).getTask(),position);
                        break;

                    case R.id.popmenu_edit:
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", todoModelArrayList.get(position).getId());
                        bundle.putString("task", todoModelArrayList.get(position).getTask());

                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment bottomDialogFragment = new BottomDialogFragment();
                        bottomDialogFragment.setArguments(bundle);
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .add(bottomDialogFragment, BottomDialogFragment.TAG)
                                .addToBackStack(null).commit();
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
//        if (itemsPendingRemoval.contains(todoModelArrayList.get(position))){
//            /** show regular layout and hide swipe layout*/
//            holder.regularLayout.setVisibility(View.GONE);
//            holder.swipeLayout.setVisibility(View.VISIBLE);
//            holder.undo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    undoDelItem(todoModelArrayList.get(position));
//                }
//            });
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    popDelete(todoModelArrayList.get(position).getId(),todoModelArrayList.get(position).getTask(),position);
//                }
//            });
//        } else
//        /** show regular layout and hide swipe layout*/
//        holder.regularLayout.setVisibility(View.VISIBLE);
//        holder.swipeLayout.setVisibility(View.GONE);
    }

//    private void undoDelItem(TodoModel customer){
//        Runnable pendingRemovalRunnable= pendingRunnables.get(customer);
//        pendingRunnables.remove(customer);
//        if (pendingRemovalRunnable !=null){
//            handler.removeCallbacks(pendingRemovalRunnable);
//            itemsPendingRemoval.remove(customer);
//            // this will rebind the row in "normal" state
//            notifyItemChanged(todoModelArrayList.indexOf(customer));
//        }
//    }
//
//    public void pendingRemoval(int pos, int taskId,String taskName){
//        final TodoModel data = todoModelArrayList.get(pos);
//        if (!itemsPendingRemoval.contains(data)){
//            itemsPendingRemoval.add(data);
//            // this will redraw row in "undo" state
//            notifyItemChanged(pos);
//            //create, store and post a runnable to remove the data
//            Runnable pendingRemovalRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    deleteTaskOnclick(pos, taskId, taskName);
//                }
//            };
//            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
//            pendingRunnables.put(data, pendingRemovalRunnable);
//        }
//    }

    public void deleteTaskOnclick(int position,int taskId, String taskName) {
        dbHelper.deleteTask(taskId);
        todoModelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,todoModelArrayList.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTask(ArrayList<TodoModel> todoModelArrayList) {
        this.todoModelArrayList = todoModelArrayList;
        dbHelper.displayTask();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return todoModelArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView, undo, delete;
        public LinearLayout swipeLayout;
        public RelativeLayout regularLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
              textView = itemView.findViewById(R.id.list_item_textView);
              undo = itemView.findViewById(R.id.undo);
              delete = itemView.findViewById(R.id.delete);
            swipeLayout = itemView.findViewById(R.id.swipeLayout);
            regularLayout = itemView.findViewById(R.id.regularLayout);

        }
    }

    public void popDelete(int taskId,String taskName,int pos) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Task!");
        builder.setMessage("Are You sure ?");
        builder.setPositiveButton("Yes", (dialogInterface, position1)
                -> deleteTaskOnclick(pos,taskId,taskName));
        builder.setNegativeButton("Cancel", (dialogInterface, i)
                -> notifyItemChanged(i));
        builder.create().show();
    }
}
