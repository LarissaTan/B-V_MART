package com.example.bv_mart.activityAndNav;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bv_mart.MainActivity;
import com.example.bv_mart.R;
import com.example.bv_mart.util.MySQLiteHelper;
import com.example.bv_mart.util.ToastUtil;


public class RechargeActivity extends AppCompatActivity {

    private TextView tv_bar_title;
    private Toolbar toolbar;
    private EditText et_rechargeMoney;
    private Button btn_doRecharge;
    private String money1;
    private double currentAccountMoney;
    private AlertDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
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
            setResult(1);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode ==event.KEYCODE_BACK){
            setResult(1);
            finish();

            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void initView() {
        //获取当前账户的金额
        currentAccountMoney = MySQLiteHelper.getInstance(getApplicationContext()).getUserMoneyFromUserName(MainActivity.username);

        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Charge");
        et_rechargeMoney= findViewById(R.id.et_rechargeMoney);
        btn_doRecharge = findViewById(R.id.btn_doRecharge);
        //充值到账户的点击事件
        btn_doRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money1 =et_rechargeMoney.getText().toString();
                if (money1.equals("0")){
                    ToastUtil.showShort("The number can not be 0");
                }else if (TextUtils.isEmpty(money1)) {
                    ToastUtil.showShort("The number can not be null");
                }else {
                    double m1 = Double.parseDouble(money1);
                    currentAccountMoney = currentAccountMoney + m1;
                    dialog1 = new AlertDialog.Builder(RechargeActivity.this).setTitle("Charge Confirm")
                            .setMessage("Account：" + MainActivity.username + '\n' + "Money：" + money1 +"$")
                            .setIcon(R.drawable.icon)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MySQLiteHelper.getInstance(getBaseContext()).RechargeMoney(MainActivity.username, currentAccountMoney);
                                    ToastUtil.showShort("Charge successfully");
                                    et_rechargeMoney.setText("");
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog1.show();
                }
            }
        });

    }
}