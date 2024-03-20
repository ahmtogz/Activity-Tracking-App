package com.nexis.running.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.nexis.running.model.IUser;

import androidx.annotation.Nullable;


public class ContactDbHelper extends SQLiteOpenHelper {
    public static final String databaseName = "Running.db";
    public ContactDbHelper(@Nullable Context context) {
        super(context, "Running.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table users(name TEXT, email TEXT primary key, password TEXT, gender TEXT, weight INT, age INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists users");
    }
    public Boolean insertData(IUser user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        contentValues.put("gender", user.getGender());
        contentValues.put("weight", user.getWeight());
        contentValues.put("age", user.getAge());

        long result = sqLiteDatabase.insert("users", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        User user = null;

        if (cursor.moveToFirst()) {
            user = new User();
            user.setName(cursor.getString(0));
            user.setEmail(cursor.getString(1));
            user.setGender(cursor.getString(3));
            user.setWeight(cursor.getInt(4));
            user.setAge(cursor.getInt(5));
        }

        cursor.close();
        sqLiteDatabase.close();

        return user;
    }

    public void displayData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from users", null);

        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String email = cursor.getString(1);
                String password = cursor.getString(2);
                String gender = cursor.getString(3);
                int weight = cursor.getInt(4);
                int age = cursor.getInt(5);


            }
        }

        cursor.close();
        sqLiteDatabase.close();
    }
    public Boolean checkEmail(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
}
