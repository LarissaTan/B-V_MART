package com.example.bv_mart.bean;

import java.io.Serializable;

public class chatObject implements Serializable{
    public String username;
    public String msg;
    public String time;

    public chatObject(String username, String msg, String times){
        this.username = username;
        this.msg = msg;
        this.time = times;
    }
}
