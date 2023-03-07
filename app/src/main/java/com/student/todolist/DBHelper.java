package com.student.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;
    ContentValues contentValues;
    private static final String DB_NAME = "todo_db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "myTodo";

    private static final String ID_COL = "id";
    private static final String TASK_COL = "task";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + TASK_COL + " TEXT NOT NULL )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertText(String task){
        db = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(TASK_COL, task);
        long result = db.insert(TABLE_NAME,null,contentValues);
        return result != -1;
    }

    public void updateTask(TodoModel todoModel){
        db= this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(TASK_COL, todoModel.getTask());

        db.update(TABLE_NAME, contentValues, ID_COL + " =? ",new String[]{String.valueOf(todoModel.getId())});
        db.close();
    }

    public void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + " =? ", new String[]{String.valueOf(id)});
        db.close();
    }


    public ArrayList<TodoModel> displayTask(){
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME, null);
        ArrayList<TodoModel> todoModelArrayList = new ArrayList<>();

        while (cursor.moveToNext()){
            TodoModel todoModel = new TodoModel();
            todoModel.setId(cursor.getInt(0));
            todoModel.setTask(cursor.getString(1));
            todoModelArrayList.add(todoModel);
        }

        cursor.close();

        return todoModelArrayList;
    }
}
