package com.example.lenovo.preco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbHelper extends SQLiteOpenHelper {
    public static final String TAG = DbHelper.class.getSimpleName();
    public static final String DB_NAME = "myapp.db";
    public static final int DB_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASS = "password";
    public static final String COLUMN_ConfirmPass = "cpass";
    public static final String COLUMN_FirstName = "fname";
    public static final String COLUMN_LastName = "lname";
    public static final String COLUMN_DOB = "dob";


    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASS + " TEXT,"
            + COLUMN_ConfirmPass + " TEXT,"
            + COLUMN_FirstName + " TEXT,"
            + COLUMN_DOB + " TEXT,"
            + COLUMN_LastName + " TEXT);";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public void addUser(String email, String password, String cpass, String fname, String lname, String dob) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASS, password);
        values.put(COLUMN_ConfirmPass, cpass);
        values.put(COLUMN_FirstName, fname);
        values.put(COLUMN_LastName, lname);
        values.put(COLUMN_DOB, dob);

        long id = db.insert(USER_TABLE, null, values);
        db.close();
        Log.d(TAG, "user inserted" + id);
    }

    public boolean getUser(String email, String pass) {

        String selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'" + email + "'" + " and " + COLUMN_PASS + " = " + "'" + pass + "'";
               /* + " and " + COLUMN_ConfirmPass + " = " + "'"+cpass+"'"
                + " and " + COLUMN_FirstName + " = " + "'"+fname+"'"
                + " and " + COLUMN_LastName + " = " + "'"+lname+"'"
                + " and " + COLUMN_DOB + " = " + "'"+dob+"'"*/

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }
}
