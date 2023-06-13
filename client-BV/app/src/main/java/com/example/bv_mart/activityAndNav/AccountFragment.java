package com.example.bv_mart.activityAndNav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bv_mart.bean.Userinfo;
import com.example.bv_mart.util.MySQLiteHelper;
import com.example.bv_mart.MainActivity;
import com.example.bv_mart.R;


import java.text.DecimalFormat;


public class AccountFragment extends Fragment {

    private TextView tv_userName;
    private TextView tv_money;
    private Userinfo userinfo;
    private double newMoney;
    private int myUserID;
    private DecimalFormat df;
    private String result;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        initData();
        initView();
        return view;
    }


    private void initData() {
        //从数据库获取userId和user信息
        userinfo = MySQLiteHelper.getInstance(getContext()).getUserInfoFromUserName(MainActivity.username);
        myUserID = MySQLiteHelper.getInstance(getContext()).GetUserId(MainActivity.username);
    }


    private void initView() {
        tv_userName = view.findViewById(R.id.tv_username_acc);
        tv_money = view.findViewById(R.id.tv_money);


        tv_userName.setText(userinfo.getUserName());
        df = new DecimalFormat("0.00");
        result = df.format(userinfo.getMoney());
        tv_money.setText(result+"$");
    }

    private void refreshMoney(){
        newMoney = MySQLiteHelper.getInstance(getContext()).getUserMoneyFromUserName(MainActivity.username);
        df = new DecimalFormat("0.00");
        result = df.format(newMoney);
        tv_money.setText(result +"$");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            refreshMoney();
        }else {
            return;
        }
    }
}