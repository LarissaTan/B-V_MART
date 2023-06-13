package com.example.bv_mart.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtill {

    //get current time
    public static String getCurrentTime(){
        String time = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        time = simpleDateFormat.format(date);
        //Log.e("Tag","当前的时间：" + time);
        return time;
    }

    public static Date stringToDate(String dateString){
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(dateString,position);
        return date;
    }

}
