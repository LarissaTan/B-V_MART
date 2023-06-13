package com.example.bv_mart.activityAndNav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bv_mart.R;
import com.example.bv_mart.adapter.PayRVAdapter;
import com.example.bv_mart.bean.GoodsArrayBean;
import com.example.bv_mart.bean.OrderBean;
import com.example.bv_mart.util.AppContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;


public class OrderDetailsActivity extends AppCompatActivity {

    private OrderBean orderBean ;
    private Gson gson;
    private List<GoodsArrayBean.ItemR> goodsData ;
    private TextView tv_bar_title;
    private Toolbar toolbar;
    private RecyclerView rv_orderDetails;
    private PayRVAdapter payRVAdapter ;
    private TextView tv_total;
    private double total = 0;
    private  BigDecimal b1 ;
    private  BigDecimal b2 ;
    private  BigDecimal b3 ;
    private  BigDecimal result ;
    private  BigDecimal one ;
    private  double a ;
    private TextView tv_orderDetails_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initData();
        initView();
        setActionBar();
    }

    /*设置ActionBar*/
    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    //为toolbar设置返回按钮
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initData() {
        orderBean = (OrderBean) getIntent().getSerializableExtra("orderDetails");
        gson = new Gson();
        Type type = new TypeToken<List<GoodsArrayBean.ItemR>>() {}.getType();
        goodsData = gson.fromJson(orderBean.getGoodsJson(), type);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Order Details");
        tv_total = findViewById(R.id.tv_order_total);
        tv_orderDetails_time = findViewById(R.id.tv_orderDetails_time);
        tv_orderDetails_time.setText(orderBean.getTime());
        rv_orderDetails = findViewById(R.id.rv_orderDetails);
        payRVAdapter = new PayRVAdapter(goodsData);
        rv_orderDetails.setLayoutManager(new LinearLayoutManager(AppContext.getInstance()));
        rv_orderDetails.setAdapter(payRVAdapter);

        for (int i= 0;i<goodsData.size();i++){
            b1 = new BigDecimal(goodsData.get(i).getPrice().trim());
            b2 = new BigDecimal(goodsData.get(i).getNumber());
            b3 = new BigDecimal(total);
            result = b1.multiply(b2);
            result = result.add(b3);
            one = new BigDecimal("1");
            a = result.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位数
            total = a ;
            //Log.e("total",total+"");
        }

        tv_total.setText("$"+ total);
    }
}