package com.example.chenzeyuan.zhihu1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chenzeyuan.zhihu1.Helper.MyDBHelper;
import com.example.chenzeyuan.zhihu1.R;
import com.loopj.android.http.HttpGet;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private CheckBox checkBox;
    private static final String URL = "http://10.87.153.13:8080/login";//MAC上项目部署在idea上，故地址结构跟题目所给不一样
    private static final String MESSAGE_TITLE = "result";
    private Handler handler;
    private String username1;
    private String password1;
    private boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initHandler();
        initListener();
    }
    private void initView(){
        username=(EditText)findViewById(R.id.login_id);
        password=(EditText)findViewById(R.id.login_password);
        checkBox=(CheckBox)findViewById(R.id.login_rememberpassword);
        login=(Button)findViewById(R.id.login_button);
        register=(Button)findViewById(R.id.register);
        SharedPreferences sf = getSharedPreferences("ex_data", MODE_PRIVATE);
        if(!sf.getString("username","").equals("")&&!sf.getString("password","").equals("")){
            flag=true;
            String username2=sf.getString("username","");
            String password2=sf.getString("password","");
            username.setText(username2);
            password.setText(password2);
            checkBox.setChecked(true);
        }
    }
    public void initHandler(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString(MESSAGE_TITLE).trim();
                if(result.equals("FAIL")) {
                    Toast.makeText(LoginActivity.this, "用户验证不通过", Toast.LENGTH_SHORT).show();
                }else if(result.equals("SUCCESS")){
                    MyDBHelper helper=MyDBHelper.getInstance(LoginActivity.this);
                    helper.setTABLE_NAME(username1);
                    SQLiteDatabase mydb=helper.getWritableDatabase();
                    helper.createTable(mydb);
                    if(checkBox.isChecked()){
                        SharedPreferences sf = getSharedPreferences("ex_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor=sf.edit();
                        editor.putString("username",username1);
                        editor.putString("password",password1);
                        editor.commit();
                    }else if(!checkBox.isChecked()&&flag==true){
                        SharedPreferences sf = getSharedPreferences("ex_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor=sf.edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.commit();
                    }
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }
    public void initListener(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread=new Thread(){
                    @Override
                    public void run() {
                        username1=username.getText().toString();
                        password1=password.getText().toString();
                        String url = URL + "?username="+username1+"&&password="+password1;
                        HttpClient httpClient=new DefaultHttpClient();
                        HttpGet get = new HttpGet(url);
                        try {
                            HttpResponse response = httpClient.execute(get);
//                            Log.e("1",username1);
                            if(response.getStatusLine().getStatusCode()==200){
                                String result= EntityUtils.toString(response.getEntity());
//                                Log.e("2",result);
                                Message msg=new Message();
                                Bundle bundle=new Bundle();
                                bundle.putString(MESSAGE_TITLE,result);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
