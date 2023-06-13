package com.example.bv_mart.activityAndNav;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bv_mart.fragment.StoreGoodsFragment;
import com.example.bv_mart.R;
import com.example.bv_mart.adapter.MyTabAdapter;
import com.example.bv_mart.bean.StoreBean;
import com.example.bv_mart.fragment.StoreCommentFragment;
import com.example.bv_mart.util.MySQLiteHelper;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;


public class StoreGoodsActivity extends AppCompatActivity {

    private TabLayout tabLayout = null;
    private MyTabAdapter myTabAdapter;
    private ViewPager home_news_viewPager;
    private StoreGoodsFragment storeGoodsFragment;
    private StoreCommentFragment storeCommentFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private Toolbar toolbar;
    private String storeID;
    private StoreBean storeBean;
    private TextView tv_storeName;
    private TextView tv_storeSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_goods);
        storeID = "0";
        initData();
        initView();
        setActionBar();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    //toolbar set return button
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initData() {
        storeBean = new StoreBean();
        storeBean = MySQLiteHelper.getInstance(getApplicationContext()).queryStoreBeanFromStoreID(storeID);
        storeGoodsFragment = new StoreGoodsFragment(storeID);
        storeCommentFragment = new StoreCommentFragment();
        fragments.add(storeGoodsFragment);
        fragments.add(storeCommentFragment);
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.home_newsTab);
        home_news_viewPager = findViewById(R.id.home_news_vp);
        tv_storeName = findViewById(R.id.tv_storeName_act);
        tv_storeSell = findViewById(R.id.tv_store_sell_act);

        tv_storeName.setText(storeBean.getStoreName());
        tv_storeSell.setText("Monthly sales："+storeBean.getStoreSell());

        //初始化tab的Adapter
        myTabAdapter = new MyTabAdapter(getSupportFragmentManager(),fragments);
        home_news_viewPager.setAdapter(myTabAdapter);
        tabLayout.setupWithViewPager(home_news_viewPager);

    }



}