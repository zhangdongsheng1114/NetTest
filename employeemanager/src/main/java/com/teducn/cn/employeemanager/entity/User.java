package com.teducn.cn.employeemanager.entity;

/**
 * Created by tarena on 2017/7/13.
 */

public class User {

    private int id;
    private String loginName;
    private String password;
    private String email;
    private String realName;

    public User() {
        super();
    }

    public User(int id, String loginName, String password, String email, String realName) {
        this.id = id;
        this.loginName = loginName;
        this.password = password;
        this.email = email;
        this.realName = realName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", realName='" + realName + '\'' +
                '}';
    }
}
