package com.ceeh.getit.Model;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "task";


    private static final String TABLE_TASK = "tasks";
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DETAIL = "detail";
    private static final String KEY_TIME = "dueTime";
    private static final String KEY_REPEAT_TIME = "repeatTime";


    //User table for login
    private static final String TABLE_USER = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";

    private static final String TAG = "getMessenge";






    public TaskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {



            String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
                    + KEY_TASK_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TASK_NAME + " TEXT,"
                    + KEY_STATUS + " TEXT,"
                    + KEY_DETAIL + " TEXT,"
                    + KEY_DUE_DATE + " TEXT,"
                    + KEY_TIME + " TEXT,"
                    + KEY_REPEAT_TIME + " INTEGER "+ ");";

            db.execSQL(CREATE_TASK_TABLE);



            String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_PASSWORD + " TEXT" + ");";
            db.execSQL(CREATE_USER_TABLE);

        }




        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + (TABLE_TASK));
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);



            // Create tables again
            onCreate(db);
        }

        // code to add the new task
        public long addTask(Task _task) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_REPEAT_TIME, _task.get_repeatingMinutes());





            values.put(KEY_TASK_NAME, _task.get_name()); //  Name
           // values.put(KEY_TASK_ID, _task.get_id()); // Password
            values.put(KEY_STATUS, _task.get_status());
            values.put(KEY_DUE_DATE, _task.get_dueDate());
            values.put(KEY_DETAIL, _task.get_description());
            values.put(KEY_TIME, _task.get_time());

            // Inserting Row
            long id = db.insert((TABLE_TASK), null, values);

            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
            Log.v(TAG, _task.get_id() + " " + "add Task in database");

            return id;
        }

        // code to get the single task
        public Task getTaskById(long id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query((TABLE_TASK), new String[] { KEY_TASK_ID,
                            KEY_TASK_NAME, KEY_STATUS,KEY_DETAIL,KEY_DUE_DATE, KEY_TIME, KEY_REPEAT_TIME }, KEY_TASK_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            Task _task = new Task( Long.parseLong(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    Integer.parseInt(cursor.getString(6)));
            db.close(); // Closing database connection

            // return user
            return _task;
        }

        // code to get all users in a list
        public List<Task> getAllTask() {
            List<Task> taskList = new ArrayList<Task>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + (TABLE_TASK);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Task _task = new Task(Long.parseLong(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            Integer.parseInt(cursor.getString(6)));


                    // Adding user to list
                    taskList.add(_task);
                } while (cursor.moveToNext());
            }
            db.close(); // Closing database connection

            // return user list
            return taskList;
        }


        // code to get all user in a list name
        public List<String> getAllTaskNames() {

            List<String> userList = new ArrayList<>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + (TABLE_TASK);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    // Adding user to list
                    userList.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }

            // return user list
            db.close(); // Closing database connection

            return userList;
        }

        // code to update the single user
        public int updateTask(Task _task) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(KEY_TASK_NAME, _task.get_name()); //  Name
            //values.put(KEY_TASK_ID, _task.get_id()); // Password
            values.put(KEY_STATUS, _task.get_status());
            values.put(KEY_DUE_DATE, _task.get_dueDate());
            values.put(KEY_DETAIL, _task.get_description());
            values.put(KEY_TIME, _task.get_time());
            values.put(KEY_REPEAT_TIME, _task.get_repeatingMinutes());
            // updating row
            return db.update(TABLE_TASK , values, KEY_TASK_ID + " = ?",
                    new String[] { String.valueOf(_task.get_id()) });
        }

        // Deleting single user
        public void deleteTask(Task task) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_TASK , KEY_TASK_ID + " = ?",
                    new String[] { String.valueOf(task.get_id()) });
            db.close();
        }

        // Getting user Count
        public int getTaskCount() {
            String countQuery = "SELECT  * FROM " + (TABLE_TASK );
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            db.close(); // Closing database connection


            // return count
            return count;

        }


    // code to add the new user
    public void addUser(User _user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, _user.getFirstName()); //  Name
        values.put(KEY_PASSWORD, _user.getPassword()); // Password

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single user
    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_ID,
                        KEY_NAME, KEY_PASSWORD }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User _user= new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return user
        db.close(); // Closing database connection

        return _user;
    }

    // code to get all users in a list
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User _user = new User();
                _user.setId(Integer.parseInt(cursor.getString(0)));
                _user.setFirstName(cursor.getString(1));
                _user.setPassword(cursor.getString(2));
                // Adding user to list
                userList.add(_user);
            } while (cursor.moveToNext());
        }

        // return user list
        db.close(); // Closing database connection

        return userList;
    }


    // code to get all user in a list name
    public List<String> getAllNames() {
        List<String> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // Adding user to list
                userList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return user list
        db.close(); // Closing database connection

        return userList;
    }

    // code to update the single user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getFirstName());
        values.put(KEY_PASSWORD, user.getPassword());

        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }

    // Getting user Count
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        db.close(); // Closing database connection

        return count;

    }




}
