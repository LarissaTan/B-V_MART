package com.example.bv_mart.activityAndNav;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bv_mart.MainActivity;
import com.example.bv_mart.util.MySQLiteHelper;
import com.example.bv_mart.util.ShareUtils;
import com.example.bv_mart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView navigationView;
    private int UserID ;
    private OrderFragment orderFragment = new OrderFragment();
    private HomeFragment homeFragment = new HomeFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ininView();
        hideScrollBar();
        setListener();
    }


    private void ininView() {
        navigationView = findViewById(R.id.navigation_view);
        UserID = MySQLiteHelper.getInstance(getApplicationContext()).GetUserId(MainActivity.username);
        replacementFragment(homeFragment);
    }

    private void hideScrollBar() {
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
    }

    private void setListener() {
        navigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.single_0:
                    replacementFragment(homeFragment);
                    break;
                case R.id.single_1:
                    replacementFragment(orderFragment);
                    break;
                case R.id.single_4:
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    ShareUtils.putAuto_Login("0");
                    HomeActivity.this.finish();
                    break;

            }
            return true;
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }


    /**
     * 设置返回两次退出程序的方法
     */
    protected long exitTime ; //记录第一次点击的时间
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis() - exitTime) > 2000 ){
                Toast.makeText(HomeActivity.this,"Click one more time to exist",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                HomeActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void replacementFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_frag,fragment);
        fragmentTransaction.commit();
    }

}