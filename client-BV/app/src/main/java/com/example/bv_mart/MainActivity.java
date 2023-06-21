package com.example.bv_mart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bv_mart.activityAndNav.RegisterActivity;
import com.example.bv_mart.util.CustomerFileClient;
import com.example.bv_mart.util.ShareUtils;
import com.example.bv_mart.activityAndNav.HomeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_password;
    private EditText et_username;
    private Button btn_login;
    private Button btn_register;
    public static String username;
    private String password;
    private CheckBox rember;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        et_password = (EditText) findViewById(R.id.et_password);
        et_username = (EditText) findViewById(R.id.et_username);
        btn_login = (Button) findViewById(R.id.M_login);
        btn_register = (Button) findViewById(R.id.M_register);
        rember = (CheckBox) findViewById(R.id.remenberpw);


        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        this.deleteDatabase("ShoppingMallInfo");
        this.deleteDatabase("db");


        if (ShareUtils.getRember().equals("1")) {
            rember.setChecked(true);
            et_username.setText(ShareUtils.getUserName());
            et_password.setText(ShareUtils.getPassword());
        } else {
            rember.setChecked(false);
        }


        if (ShareUtils.getAuto_Login().equals("1")) {
            if (TextUtils.isEmpty(et_username.getText()) || TextUtils.isEmpty(et_password.getText())) {
                Toast.makeText(this, "your user name or password is null...", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, HomeActivity.class));
                username = ShareUtils.getUserName();
                this.finish();
            }
        }


        rember.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rember.isChecked()) {
                    //System.out.println("记住密码已被选中");
                    ShareUtils.putRember("1");
                } else {
                    ShareUtils.putRember("0");
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.M_login:
                username = et_username.getText().toString();
                password = et_password.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "username or password can not be null", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncTask<Void, Void, String>() {
                    boolean loginSuccess;

                    @Override
                    protected String doInBackground(Void... voids) {
                        CustomerFileClient customerFileClient = new CustomerFileClient();

                        loginSuccess = customerFileClient.login(username, password);
                        Log.i("login", "login: " + username + " " + password);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String response) {
                        flag = loginSuccess;
                        Log.i("login", "login success: " + loginSuccess);
                    }
                };

                if (flag) {
                    if (rember.isChecked()) {
                        ShareUtils.putUserName(username);
                        ShareUtils.putPassword(password);
                    }
                    Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.M_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }

}