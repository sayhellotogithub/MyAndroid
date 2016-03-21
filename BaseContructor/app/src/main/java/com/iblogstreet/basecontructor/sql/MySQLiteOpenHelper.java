package com.iblogstreet.basecontructor.sql;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION =1;
    private static final String TAG = "MySQLiteOpenHelper";
    //定义数据库名称
    public static final String TABLE_NAME = "myuser.db";
    /**
     * context 上下文
     * name 数据库名称
     * factory null
     * version 版本号
     * */
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    //TODO 实现基类的构造函数，三个参数的
    public MySQLiteOpenHelper(Context context, String DBname ,int version ) {
        this(context, DBname, null, version);

    }
    //TODO 实现基类的构造函数，两个参数的
    public MySQLiteOpenHelper(Context context,String DBname) {
        this(context, DBname, null, VERSION);
    }
    //TODO 实现基类的构造函数，两个参数的
    public MySQLiteOpenHelper(Context context,int version) {
        this(context, TABLE_NAME, null, version);
    }

    //TODO 实现基类的构造函数，两个参数的
    public MySQLiteOpenHelper(Context context) {
        this(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "数据库创建onCreate");
        String sql=" create table User(userid integer primary key not null,\n" +
                "username nvarchar(100),\n" +
                "age int,\n" +
                "phone nvarchar(20),\n" +
                "status int)";
        Log.d(TAG, "create User table");
        db.execSQL(sql);
    }
    /**
    *当数据库升级是回调该方法
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     /*在创建 MySQLiteOpenHelper 对象的时候需要传递一个 int 类型的 version 参数，代表数据库的版本号，
        数值从 1 开始。如果在 new MySQLiteOpenHelper 对象的时候传递的 version 大于先去创建的 version，那
        么就会导致系统回调 onUpgrade 方法，从而实现了数据库的升级*/
        Log.d(TAG, "onUpgrade");
    }
    @Override
    public synchronized void close() {
        super.close();
        Log.d(TAG,"close");
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d(TAG, "onOpen");
    }


}
