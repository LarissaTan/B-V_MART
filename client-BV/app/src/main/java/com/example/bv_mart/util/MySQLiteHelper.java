package com.example.bv_mart.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bv_mart.bean.ChatMessageBean;
import com.example.bv_mart.bean.OrderBean;
import com.example.bv_mart.bean.Userinfo;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteHelper {

    private static MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;

    private MySQLiteHelper(Context context) {
        MySQLhelper mySQLhelper = new MySQLhelper(context, MySQLhelper.DB_NAME, null, MySQLhelper.VERSION);
        db = mySQLhelper.getWritableDatabase();
    }

    public synchronized static MySQLiteHelper getInstance(Context context) {
        if (mySQLiteHelper == null) {
            mySQLiteHelper = new MySQLiteHelper(context);
        }
        return mySQLiteHelper;
    }


    //查询用户名是否存在 存在返回true 不存在false
    public boolean queryNameisExist(String username) {
        Cursor cursor = db.rawQuery("select * from userInfo where username = ? ", new String[]{username});
        if (cursor.moveToFirst() == true) {
            cursor.close();
            return true;
        }
        return false;
    }


    public String queryGoods(){
        String resultJson = null;
        Cursor cursor = db.rawQuery("SELECT * FROM goodsInfo WHERE ID LIKE ? ",
                new String[] {"0"});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                resultJson = cursor.getString(2);
            }
        }
        return resultJson;
    }

    //查询所有信息的方法
    public List<ChatMessageBean> queryAllMessages(){
        List<ChatMessageBean> chatMessageBeans = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from messagesInfo ", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ChatMessageBean chatMessageBean = new ChatMessageBean();
                chatMessageBean.setMessage(cursor.getString(3));
                chatMessageBean.setUserName(cursor.getString(4));
                chatMessageBean.setTime(cursor.getString(5));
                chatMessageBeans.add(chatMessageBean);
            }
        }
        return chatMessageBeans;
    }


    //插入聊天信息的方法
    public void insertMessages(ChatMessageBean chatMessageBean){
        ContentValues cv =new ContentValues();
        cv.put("message",chatMessageBean.getMessage());
        cv.put("userName",chatMessageBean.getUserName());
        cv.put("time",chatMessageBean.getTime());
        //插入数据，参数为表名，当列为空时的填充值，封装数据的ContentValue
        db.insert("messagesInfo",null,cv);
    }


    //将订单信息插入到订单表中
    public void insertOrderInfo(OrderBean orderBean) {
        db.execSQL("insert into orderInfo(userName,time,goodsJson) values(?,?,?)", new Object[]{orderBean.getUserName(),orderBean.getTime(),orderBean.getGoodsJson()});
        //Log.e("插入语句:", "插入已执行，插入成功");
    }


    //根据输入用户名返回一个该用户的订单集合
    public List<OrderBean> queryOrderBeanFromUserName(String userName){
        OrderBean orderBean ;
        List<OrderBean> result = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM orderInfo WHERE userName LIKE ? ",
                new String[] {userName});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                orderBean = new OrderBean();
                orderBean.setUserName(cursor.getString(1));
                orderBean.setTime(cursor.getString(2));
                orderBean.setGoodsJson(cursor.getString(3));
                result.add(orderBean);
            }
        }
        return result;
    }
}