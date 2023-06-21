package com.example.bv_mart.bean;

import java.io.Serializable;


/**
 *  评论信息bean
 */
public class ChatMessageBean implements Serializable {
    private String message;
    private String userName;
    private String time;


    public ChatMessageBean(String message, String userName, String time) {
        this.message = message;
        this.userName = userName;
        this.time = time;
    }

    public ChatMessageBean() {

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatMessageBean{" +
                "message='" + message + '\'' +
                ", userName='" + userName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
