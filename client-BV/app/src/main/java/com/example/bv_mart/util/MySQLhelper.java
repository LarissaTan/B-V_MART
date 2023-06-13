package com.example.bv_mart.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 创建SQLite数据库的工具类
 */
public class MySQLhelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "db";
    public static final int VERSION = 1;

    public MySQLhelper(@Nullable Context context, @Nullable String DB_NAME, @Nullable SQLiteDatabase.CursorFactory factory, int VERSION) {
        super(context, DB_NAME, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userInfo(_id INTEGER PRIMARY KEY AUTOINCREMENT ,userName VERVHAR(20),password VERVHAR(20))");
        db.execSQL("CREATE TABLE goodsInfo(_id INTEGER PRIMARY KEY autoincrement ,ID VERVHAR(20),goodsJson text)");
        db.execSQL("CREATE TABLE messagesInfo(_id INTEGER PRIMARY KEY autoincrement ,ID INTEGER,img_id VERVHAR(20),message text,userName VERVHAR(20),time VERVHAR(20))");
        db.execSQL("CREATE TABLE orderInfo(_id INTEGER PRIMARY KEY autoincrement ,userName VERVHAR(20),time VERVHAR(20),goodsJson text)");


        db.execSQL("INSERT INTO userInfo(userName,password) VALUES ('1','1')");
        db.execSQL("INSERT INTO messagesInfo(ID,img_id,message,userName,time) VALUES (1,'1','I like the coffee here~~','aaa','2021.12.15 12:08')");
        db.execSQL("INSERT INTO goodsInfo (ID,goodsJson)values(0,'{\"itemsLeft\":[{\"title\":\"Non-food\"},{\"title\":\"Drinks\"},{\"title\":\"Food\"}],\"itemsRight\":[{\"content\":\"Non-food\",\"name\":\"Calendar\",\"number\":0,\"picNumb\":\"0\",\"price\":\"6.50\",\"title\":\"Non-food\"},{\"content\":\"Non-food\",\"name\":\"Chair\",\"number\":0,\"picNumb\":\"1\",\"price\":\"10.00\",\"title\":\"Non-food\"},{\"content\":\"Non-food\",\"name\":\"Flowers\",\"number\":0,\"picNumb\":\"2\",\"price\":\"10.00\",\"title\":\"Non-food\"},{\"content\":\"Drinks\",\"name\":\"Milk\",\"number\":0,\"picNumb\":\"3\",\"price\":\"11.00\",\"title\":\"Drinks\"},{\"content\":\"Drinks\",\"name\":\"Milo\",\"number\":0,\"picNumb\":\"4\",\"price\":\"4.00\",\"title\":\"Drinks\"},{\"content\":\"Food\",\"name\":\"Small cake\",\"number\":0,\"picNumb\":\"5\",\"price\":\"10.99\",\"title\":\"Food\"},{\"content\":\"Food\",\"name\":\"Sweet cookie\",\"number\":0,\"picNumb\":\"6\",\"price\":\"10.90\",\"title\":\"Food\"}]}\n')");
        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
