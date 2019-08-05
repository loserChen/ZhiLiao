package com.example.chenzeyuan.zhihu1.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chenzeyuan.zhihu1.Helper.MyDBHelper;
import com.example.chenzeyuan.zhihu1.R;
import com.loopj.android.http.HttpGet;

import org.json.JSONObject;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class RegisterActivity extends AppCompatActivity{
    private Button register;
    private EditText username;
    private EditText password;
    private Button check;
    private EditText phone;
    private EditText checkNum;
    private static final String URL = "http://10.87.153.13:8080/register";//MAC上项目部署在idea上，故地址结构跟题目所给不一样
    private static final String MESSAGE_TITLE = "result";
    private Handler handler;
    private String phone1;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initHandler();
        initListener();
    }
    public void initView(){
        register=(Button)findViewById(R.id.register_button);
        username=(EditText)findViewById(R.id.register_id);
        password=(EditText)findViewById(R.id.register_password);
        check=(Button)findViewById(R.id.register_check);
        phone=(EditText)findViewById(R.id.register_phone);
        checkNum=(EditText)findViewById(R.id.register_checkNum);
    }
    public void initHandler(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString(MESSAGE_TITLE).trim();
                if(result.equals("FAIL")) {
                    Toast.makeText(RegisterActivity.this, "用户注册不通过", Toast.LENGTH_SHORT).show();
                }else if(result.equals("SUCCESS")){
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler); // 注册回调监听接口
        mHandler = new Handler() {
            public void handleMessage(Message msg) {

                // TODO Auto-generated method stub
                super.handleMessage(msg);
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                String username1 = username.getText().toString();
                                String password1 = password.getText().toString();
                                String url = URL + "?username=" + username1 + "&&password=" + password1;
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpGet get = new HttpGet(url);
                                try {
                                    HttpResponse response = httpClient.execute(get);
//                            Log.e("1",username1);
                                    if (response.getStatusLine().getStatusCode() == 200) {
                                        String result = EntityUtils.toString(response.getEntity());
//                                Log.e("2",result);
                                        Message msg = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString(MESSAGE_TITLE, result);
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        Log.i("EventHandler", "获取验证码成功");
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                        Log.i("EventHandler", "返回支持发送验证码的国家列表");
                    }
                } else {
                    int status = 0;
                    try {
                        ((Throwable) data).printStackTrace();
                        Throwable throwable = (Throwable) data;

                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");
                        status = object.optInt("status");
                        if (!TextUtils.isEmpty(des)) {
                            Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        SMSLog.getInstance().w(e);
                    }
                }
            }
        };

    }
    public void initListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMSSDK.submitVerificationCode("86", phone1, checkNum.getText().toString());
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
                SMSSDK.getVerificationCode("86", phone.getText().toString());
                phone1 = phone.getText().toString();
            }
        });
    }
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
