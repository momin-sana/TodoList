package com.student.todolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Collections;

public class TodoFragment extends Fragment{

    public FloatingActionButton openAddBtn;
    public RecyclerView recyclerView;
    public DBHelper dbHelper;
    public EditText editText_inputTask;


    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Exit App?")
                        .setMessage("Sure want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                requireActivity().getOnBackPressedDispatcher();
                                TransitionInflater inflater = TransitionInflater.from(requireActivity());
                                setExitTransition(inflater.inflateTransition(R.transition.fade));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        dbHelper = new DBHelper(getContext());
        recyclerView = view.findViewById(R.id.todo_recyclerView);
        openAddBtn = view.findViewById(R.id.floatingActionButton);
        editText_inputTask = view.findViewById(R.id.editText_bottomDialog);


        ArrayList<TodoModel> todoModelArrayList = new ArrayList<>();
        TodoAdapter todoAdapter = new TodoAdapter(todoModelArrayList, dbHelper, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(todoAdapter);

        todoModelArrayList = dbHelper.displayTask();
        Collections.reverse(todoModelArrayList);
        todoAdapter.setTask(todoModelArrayList);
        setOpenAddBtn(openAddBtn);
        return view;

    }


    public void setOpenAddBtn(FloatingActionButton openAddBtn) {
        this.openAddBtn = openAddBtn;
        openAddBtn.setOnClickListener(view -> {
            BottomDialogFragment bottomDialogFragment = new BottomDialogFragment();
            bottomDialogFragment.show(getActivity().getSupportFragmentManager(), BottomDialogFragment.TAG);
        });
    }
}