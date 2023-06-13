package com.example.bv_mart.bean;

public class Userinfo {
    private int id ;
    private String userName;
    private String password;
    private String phoneNumb;
    private double money;


    public Userinfo(){

    }

    public Userinfo(int id, String userName, String password, String phoneNumb, double money) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.phoneNumb = phoneNumb;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumb() {
        return phoneNumb;
    }

    public void setPhoneNumb(String phoneNumb) {
        this.phoneNumb = phoneNumb;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumb='" + phoneNumb + '\'' +
                ", money=" + money +
                '}';
    }
}
