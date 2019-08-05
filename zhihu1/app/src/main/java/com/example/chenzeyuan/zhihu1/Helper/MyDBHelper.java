package com.example.chenzeyuan.zhihu1.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;

import com.example.chenzeyuan.zhihu1.Activity.ArticleContentActivity;

/**
 * Created by chenzeyuan on 2018/5/19.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "My.db";
    public String TABLE_NAME = "";
    private static MyDBHelper dbHelper;
    public static MyDBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new MyDBHelper(context);
        }
        return dbHelper;
    }
    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
}

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createTable(SQLiteDatabase db){
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY, title VARCHAR, image VARCHAR)";
        db.execSQL(sql);
    }
    public void setTABLE_NAME(String TABLE_NAME){
        this.TABLE_NAME="news"+TABLE_NAME;
    }
}
