package com.example.bv_mart.activityAndNav;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bv_mart.bean.Userinfo;
import com.example.bv_mart.util.MySQLiteHelper;
import com.example.bv_mart.util.ToastUtil;
import com.example.bv_mart.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog dialog;
    private EditText et_userName;
    private EditText et_password;
    private Button btn_register;

    private String userName;
    private String password;

    private TextView tv_bar_title;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setActionBar();
    }

    /*设置ActionBar*/
    private void setActionBar() {
        setSupportActionBar(toolbar);
        /*显示Home图标*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置不显示项目名称
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        et_userName = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);

        toolbar = findViewById(R.id.toolbar);
        tv_bar_title = findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("Register");

        btn_register.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                userName = et_userName.getText().toString();
                password = et_password.getText().toString();

                if (MySQLiteHelper.getInstance(this).queryNameisExist(userName)) {
                    ToastUtil.showShort("user exist, please login");
                    return;
                }
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    ToastUtil.showShort("user name or password can not be null");
                    return;
                }


                final Userinfo userinfo = new Userinfo();
                userinfo.setUserName(userName);
                userinfo.setPassword(password);
                dialog = new AlertDialog.Builder(this).setTitle("Check the information first please~~")
                        .setMessage("name：" + userName + '\n' + "pwd：" + password + '\n')
                        .setIcon(R.drawable.icon)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DoInsert(userinfo);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                break;
        }
    }


    public void DoInsert(Userinfo userinfo){
        MySQLiteHelper.getInstance(this).insertUserinfo(userinfo);
        ToastUtil.showShort("Register successfully");
        RegisterActivity.this.finish();
    }

}