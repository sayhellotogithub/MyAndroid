package com.iblogstreet.basecontructor.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.iblogstreet.basecontructor.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MySQLiteOperatorOne {
    MySQLiteOpenHelper myOpenH= null;
    /**
     * 新增
     * */
    public void add(View v){
        //boolean result=false;
        myOpenH=new MySQLiteOpenHelper(v.getContext(),1);
        SQLiteDatabase db=myOpenH.getWritableDatabase();
        String sql = "insert into user (username,age,phone,status) values (?,?,?,1)";
        db.execSQL(sql, new Object[] { "zhangsan" + new Random().nextInt(100),new Random().nextInt(30), ""
                + (5550 + new Random().nextInt(100)) });
        db.close();
    }
    public void delete(View v){
        myOpenH=new MySQLiteOpenHelper(v.getContext(),1);
        SQLiteDatabase db=myOpenH.getWritableDatabase();
        String sql = "delete from user where username=?";
        db.execSQL(sql, new Object[] { "zhangsan1" });
        db.close();
    }
    public void update(View v){
        myOpenH=new MySQLiteOpenHelper(v.getContext(),1);
        SQLiteDatabase db=myOpenH.getWritableDatabase();
        String sql = "update user set username=? where userid=?";
        db.execSQL(sql, new Object[] { "zhangsan1" ,"1"});
        db.close();
    }
    public List<User> getUserList(View v){
       List<User> users=new ArrayList<>() ;
        myOpenH=new MySQLiteOpenHelper(v.getContext(),1);
        SQLiteDatabase db=myOpenH.getReadableDatabase();
        Cursor cursor=db.rawQuery("select userid,username,age,phone,status from user where status=?",new String[]{"1"});
        while(cursor.moveToNext()){
            int uid = cursor.getInt(0);
            String username=cursor.getString(1);
            int age=cursor.getInt(2);
            String phone=cursor.getString(3);
            int status=cursor.getInt(4);
            User user=new User(uid,username,age,phone,status);
            users.add(user);
        }
        cursor.close();
        db.close();
        return users;
    }
}
