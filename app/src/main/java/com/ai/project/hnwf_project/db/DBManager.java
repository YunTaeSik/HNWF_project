package com.ai.project.hnwf_project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ai.project.hnwf_project.util.Contact;


/**
 * Created by sky87 on 2016-06-23.
 */
public class DBManager extends SQLiteOpenHelper {
    private SQLiteDatabase date;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists '" + Contact.MAN_WEIGHT_ONE + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.MAN_WEIGHT_TWO + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.GIRL_WEIGHT_ONE + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.GIRL_WEIGHT_TWO + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");


        db.execSQL("CREATE TABLE if not exists '" + Contact.MID_MAN_WEIGHT_CHO + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.MID_MAN_WEIGHT_JUNG + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.MID_MAN_WEIGHT_JONG + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");

        db.execSQL("CREATE TABLE if not exists '" + Contact.LAST_MAN_WEIGHT_CHO + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.LAST_MAN_WEIGHT_JUNG + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.LAST_MAN_WEIGHT_JONG + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");

        db.execSQL("CREATE TABLE if not exists '" + Contact.MID_GIRL_WEIGHT_CHO + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.MID_GIRL_WEIGHT_JUNG + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.MID_GIRL_WEIGHT_JONG + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");

        db.execSQL("CREATE TABLE if not exists '" + Contact.LAST_GIRL_WEIGHT_CHO + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.LAST_GIRL_WEIGHT_JUNG + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        db.execSQL("CREATE TABLE if not exists '" + Contact.LAST_GIRL_WEIGHT_JONG + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }


}
