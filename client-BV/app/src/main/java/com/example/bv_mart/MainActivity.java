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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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

                NetworkTask networkTask = new NetworkTask(username, password);
                networkTask.execute();

                break;
            case R.id.M_register:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
        }

    }

    private class NetworkTask extends AsyncTask<Void, Void, String> {
        private String username;
        private String password;

        public NetworkTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                InetAddress host = InetAddress.getByName("172.20.10.5");
                Socket socket = new Socket(host.getHostName(), 16800);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject("login" + "@" + username + "@" + password);

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();

                String[] split = message.split("@");
                Log.i("flag", split[2]);
                flag = Boolean.parseBoolean(split[2]);
                ois.close();
                oos.close();
                socket.close();

                return message;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            if (flag) {
                // 登录成功，执行相应的操作
                if (rember.isChecked()) {
                    ShareUtils.putUserName(username);
                    ShareUtils.putPassword(password);
                }
                Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                MainActivity.this.finish();
            } else {
                // 登录失败，显示相应的提示信息
                Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
            }

        }
    }
}

