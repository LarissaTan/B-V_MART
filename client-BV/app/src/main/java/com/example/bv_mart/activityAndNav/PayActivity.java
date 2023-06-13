package com.example.bv_mart.activityAndNav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.bv_mart.adapter.PayRVAdapter;
import com.example.bv_mart.bean.GoodsArrayBean;
import com.example.bv_mart.bean.OrderBean;
import com.example.bv_mart.util.AppContext;
import com.example.bv_mart.util.DateUtill;
import com.example.bv_mart.util.MyDialog;
import com.example.bv_mart.util.MySQLiteHelper;
import com.example.bv_mart.util.ToastUtil;
import com.example.bv_mart.MainActivity;
import com.example.bv_mart.R;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class PayActivity extends AppCompatActivity {

    private TextView tv_bar_title;
    private Toolbar toolbar;

    private List<GoodsArrayBean.ItemR> data =new ArrayList<>();
    private RecyclerView rv_pay;
    private TextView tv_pay_total;
    private TextView tv_submitOrder;
    private PayRVAdapter payRVAdapter;
    private double total = 0;
    private  BigDecimal b1 ;
    private  BigDecimal b2 ;
    private  BigDecimal b3 ;
    private  BigDecimal result ;
    private  BigDecimal one ;
    private  double a ;
    private Dialog dialog;
    private OrderBean orderBean;
    private Gson gson;
    private String goodsJson;

    private  BigDecimal a1 ;
    private  BigDecimal a2 ;
    private  BigDecimal result1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        data = (ArrayList<GoodsArrayBean.ItemR>) getIntent().getSerializableExtra("PayList");
        initView();
        setActionBar();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        rv_pay = findViewById(R.id.rv_pay);
        tv_pay_total = findViewById(R.id.tv_pay_total);
        tv_submitOrder = findViewById(R.id.tv_submitOrder);
        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Submit");
        rv_pay.setItemAnimator(null);
        payRVAdapter = new PayRVAdapter(data);
        rv_pay.setLayoutManager(new LinearLayoutManager(AppContext.getInstance()));
        rv_pay.setAdapter(payRVAdapter);

        for (int i= 0;i<data.size();i++){
            b1 = new BigDecimal(data.get(i).getPrice().trim());
            b2 = new BigDecimal(data.get(i).getNumber());
            b3 = new BigDecimal(total);
            result = b1.multiply(b2);
            result = result.add(b3);
            one = new BigDecimal("1");
            a = result.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位数
            total = a ;
            //Log.e("total",total+"");
        }

        tv_pay_total.setText(total + "RM");

        tv_submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToastUtil.showShort("总金额"+total);
                dialog = new AlertDialog.Builder(PayActivity.this).setTitle("Are you sure to pay？")
                        .setMessage("Total：" + total)
                        .setIcon(R.drawable.icon)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                doInsertOrder();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();


            }
        });

    }


    private void doInsertOrder(){
        //解决double精度丢失问题
            a2 = new BigDecimal(total);

            gson = new Gson();
            goodsJson = gson.toJson(data);
            //Log.e("inputString=" , inputString);
            orderBean = new OrderBean(MainActivity.username, DateUtill.getCurrentTime(),goodsJson);
            //Log.e("order",orderBean.toString());
            //Log.e("时间",DateUtill.getCurrentTime());
            MySQLiteHelper.getInstance(PayActivity.this).insertOrderInfo(orderBean);
            ToastUtil.showShort("We have receive that order!");
            MyDialog.handler.sendEmptyMessage(2);
            PayActivity.this.finish();

    }
}