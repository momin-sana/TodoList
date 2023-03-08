package com.student.todolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomDialogFragment extends BottomSheetDialogFragment implements OnDialogCloseListner {
    public static final String TAG = "BottomDialog";
    public Button enterBtn;
    private DBHelper dbHelper;
    private EditText editText_inputTask;
    public TodoModel todoModel;
    public OnDialogCloseListner onDialogCloseListner;
    String task;
    int taskId;


    public BottomDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_dialog, container, false);

        dbHelper = new DBHelper(this.getActivity());

        TextView textView = view.findViewById(R.id.textView_bottomDialog);
        enterBtn = view.findViewById(R.id.enterButton_bottomDialog);
        editText_inputTask = view.findViewById(R.id.editText_bottomDialog);


        boolean isUpdate = false;

        if (getArguments() != null) {
            isUpdate = true;
            textView.setText(R.string.heading_edit);
            task = getArguments().getString("task");
//            get id :::
            taskId = getArguments().getInt("id");
            editText_inputTask.setText(task);
        } else textView.setText(R.string.heading_new);

        final boolean finalIsUpdate = isUpdate;

        editText_inputTask.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                String text = editText_inputTask.getText().toString().trim();
                if (finalIsUpdate) {
//                update work
                    dbHelper = new DBHelper(getContext());
                    todoModel = new TodoModel();
                    todoModel.setId(taskId);
                    todoModel.setTask(text);
                    dbHelper.updateTask(todoModel);

                } else if (TextUtils.isEmpty(text)) {
                    editText_inputTask.setError("Enter someValue");
                    Toast.makeText(getActivity(), "Task Not Inserted", Toast.LENGTH_SHORT).show();
                } else if (text.length() > 1) {
                    dbHelper.insertText(text);
                    editText_inputTask.setText("");
                    dismiss();
                }
                TodoFragment todoFragment = new TodoFragment();
                FragmentTransaction transaction = requireActivity()
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, todoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                dismiss();

                enterBtn.performClick();
            }
            return false;
        });


        enterBtn.setOnClickListener(view1 -> {
            String text = editText_inputTask.getText().toString().trim();
            if (finalIsUpdate) {
//                update work
                dbHelper = new DBHelper(this.getActivity());
                todoModel = new TodoModel();
                todoModel.setId(taskId);
                todoModel.setTask(text);
                dbHelper.updateTask(todoModel);

            } else if (TextUtils.isEmpty(text)) {
                editText_inputTask.setError(getString(R.string.enter_value));
            } else if (text.length() > 1) {
                dbHelper.insertText(text);
                editText_inputTask.setText("");
                dismiss();
            }
            TodoFragment todoFragment = new TodoFragment();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, todoFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            dismiss();
        });

        return view;
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        FragmentActivity fragment = this.getActivity();
        if (fragment instanceof OnDialogCloseListner) {
            (onDialogCloseListner).onDialogClose(dialogInterface);
        }
    }
}