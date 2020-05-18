package cn.liuyin.web.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper// 继承SQLiteOpenHelper类
{
    public static final String DATABASE_NAME = "MWebDataBase.db";
    public static final String TABLE_NAME0 = "ShuQian";
    public static final String TABLE_NAME1 = "History";
    public static final String TABLE_NAME2 = "Chajian";
    public static final String TABLE_NAME3 = "adHosts";
    public static final String TABLE_NAME4 = "adDiv";
    public static final String TABLE_NAME5 = "hosts";
    public static final String TABLE_NAME6 = "malwaredomains";
    // 数据库版本号
    private static final int DATABASE_VERSION = 1;
    //public static final String TABLE_NAME3 = "";
    // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
    String s0 = ("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME0 + "] (") +
            "[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[title] TEXT," +
            "[url] TEXT)";
    String s1 = ("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME1 + "] (") +
            "[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[day] INTEGER," +
            "[time] TEXT," +
            "[title] TEXT," +
            "[url] TEXT)";
    String s2 = ("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME2 + "] (") +
            "[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[name] TEXT," +
            "[host] TEXT," +
            "[url] TEXT," +
            "[javascript] TEXT)";
    String s3 = ("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME3 + "] (") +
            "[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[url] TEXT)";
    String s4 = ("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME4 + "] (") +
            "[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[name] TEXT," +
            "[host] TEXT," +
            "[fiter] TEXT)";
    String s5 = ("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME5 + "] (") +
            "[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[ip] TEXT," +
            "[domain] TEXT)";
    String s6 = ("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME6 + "] (") +
            "[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[domain] TEXT)";


    public DatabaseHelper(Context context) {

        super(context, context.getExternalFilesDir("db").getPath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);

    }

    // 继承SQLiteOpenHelper类,必须要覆写的三个方法：onCreate(),onUpgrade(),onOpen()
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(s0);
        db.execSQL(s1);
        db.execSQL(s2);
        db.execSQL(s3);
        db.execSQL(s4);
        db.execSQL(s5);
        db.execSQL(s6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        onCreate(db);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL(s0);
        db.execSQL(s1);
        db.execSQL(s2);
        db.execSQL(s3);
        db.execSQL(s4);
        db.execSQL(s5);
        db.execSQL(s6);
        //deleteDuplicates();


    }

}
