package com.iblogstreet.basecontructor.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iblogstreet.basecontructor.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MySQLiteOperatorTwo {
    MySQLiteOpenHelper myOpenH= null;
    int Version=1;
    /**
     * 新增
     * */
    public boolean add(Context context){
        //boolean result=false;
        myOpenH=new MySQLiteOpenHelper(context,Version);
        SQLiteDatabase db=myOpenH.getWritableDatabase();
        /* 该类底层是 Map 数据结构
         * key 对应数据库表中字段
         * value 是想插入的值
        */
        ContentValues values = new ContentValues();
        values.put("username","test");
        values.put("age",new Random().nextInt(30));
        values.put("phone",""+ (5550 + new Random().nextInt(100)));
        values.put("status", 1);
        /*
        * 第一个参数：表名，注意不是数据库名！
        *第二个参数：如果 ContentValues 为空，那么默认情况下是不允许插入空值的
        * 但是如果给该参数设置了一个指定的列名，那么就允许 ContentValues 为空，
        * 同时给该列插入 null 值。
        * 第三个参数：要插入的数值，封装成了 ContentValues 对象
        * 返回 long 类型的值，代表该条记录在数据库中的 id
        * */
        long result = db.insert("user", null, values);
        db.close();
        return result>0?true:false;
    }
    public boolean delete(Context context){
        myOpenH=new MySQLiteOpenHelper(context,Version);
        SQLiteDatabase db=myOpenH.getWritableDatabase();
        int result= db.delete("user", "username=?", new String[]{"test1"});
        db.close();
        return result>0?true:false;
    }
    public boolean update(Context context){
        myOpenH=new MySQLiteOpenHelper(context,Version);
        SQLiteDatabase db=myOpenH.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("username", "test2");
        int result=db.update("user", values, "username=?", new String[]{"test1"});

        db.close();
        return result>0?true:false;
    }

    public List<User> getUserList(Context context){
       List<User> users=new ArrayList<>() ;
        myOpenH=new MySQLiteOpenHelper(context,Version);
        SQLiteDatabase db=myOpenH.getReadableDatabase();
        /*
        *  参数 1：表名
        * 参数 2：要查询的字段
        * 参数 3：查询条件
        * 参数 4：条件中？对应的值
        * 参数 5：分组查询参数
        * 参数 6：分组查询条件
        * 参数 7：排序字段和规则
        * */
        Cursor cursor=db.query("user",new String[]{"userid","username","age","phone","status"},
                "status=?",new String[]{"1"},null,null," age desc "
                );
        while(cursor.moveToNext()){
            int uid = cursor.getInt(0);
            String name=cursor.getString(1);
            int age=cursor.getInt(2);
            String phone=cursor.getString(3);
            int status=cursor.getInt(4);
            User user=new User(uid,name,age,phone,status);
            users.add(user);
        }
        cursor.close();
        db.close();
        return users;
    }
    /**
     * 事务的使用
     * 事务的使用不仅可以保证数据的一致性，也可以提高批处理时的执行效率。
     *SQLiteDatabase 提供的 beginTransaction（）打开事务，endTransaction（）结束事务。注意：结束
     *事务并不代表事务提交，如果想让数据写入的数据库需要在结束事务前执行 setTransactionSuccessful
     *（）方法。这是事务提交的唯一方式
     * */
    public void addTran(Context context){
        myOpenH=new MySQLiteOpenHelper(context,Version);
        SQLiteDatabase db=myOpenH.getWritableDatabase();
        db.beginTransaction();
        db.setTransactionSuccessful();
        ContentValues values = new ContentValues();
        values.put("username","test");
        values.put("age",new Random().nextInt(30));
        values.put("phone",""+ (5550 + new Random().nextInt(100)));
        values.put("status", 1);
        /*
        * 第一个参数：表名，注意不是数据库名！
        *第二个参数：如果 ContentValues 为空，那么默认情况下是不允许插入空值的
        * 但是如果给该参数设置了一个指定的列名，那么就允许 ContentValues 为空，
        * 同时给该列插入 null 值。
        * 第三个参数：要插入的数值，封装成了 ContentValues 对象
        * 返回 long 类型的值，代表该条记录在数据库中的 id
        * */
        long result = db.insert("user", null, values);
        db.endTransaction();
        db.close();
    }
}
