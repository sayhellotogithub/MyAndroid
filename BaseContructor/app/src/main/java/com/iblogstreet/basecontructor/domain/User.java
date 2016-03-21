package com.iblogstreet.basecontructor.domain;

/**
 * Created by Administrator on 2016/3/11.
 */
public class User {
    private int userid;
    private String username;
    private int age;
    private String phone;
    private int status;

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public User(int userid, String username, int age, String phone, int status) {
        this.userid = userid;
        this.username = username;
        this.age = age;
        this.phone = phone;
        this.status = status;
    }
}
