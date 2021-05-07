package com.example.select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.select.utils.SharedPreferencesUtils;
import com.example.select.webapi.HttpService;
import com.example.uselogin.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private EditText et_inputname;
    private EditText et_inputpassword;
    private Button bt_login;
    private CheckBox cb_login_rememberpassword;
    private CheckBox cb_login_autologin;
    public static final int SHOW_RESPONSE = 0;
    public static String Message_RESPONSE = null;

    private SharedPreferencesUtils helper;
    private Retrofit retrofit;

    private Handler handler = new Handler();
    private HttpService httpService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "http://192.168.122.1:8080/SelectSubject_war_exploded/";
        helper = new SharedPreferencesUtils(this, "setting");
        retrofit = new Retrofit.Builder().baseUrl(url).build();
        httpService = retrofit.create(HttpService.class);

        initView();
        setListener();
        initData();

    }

    public static String getIP(Context context) {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    //初始化布局
    private void initView() {
        et_inputname = (EditText) findViewById(R.id.et_name);
        et_inputpassword = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        cb_login_rememberpassword = (CheckBox) findViewById(R.id.cb_login_rememberpassword);
        cb_login_autologin = (CheckBox) findViewById(R.id.cb_login_autologin);
    }


    //点击事件响应
    private void setListener() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getAccount().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "你输入的账号为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (getPassword().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "你输入的密码为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Toast.makeText(LoginActivity.this, "登录中......", Toast.LENGTH_SHORT).show();
                    // Intent intent_loginWelcome = new Intent();
                    // intent_loginWelcome.setClass(LoginActivity.this,WelcomeActivity.class);
                    // LoginActivity.this.startActivity(intent_loginWelcome);
                    //finish();
                    login();
                }
            }
        });
    }

    //判断初始化数据
    private void initData() {
        //判断是否第一次登录
        if (firstLogin()) {
            cb_login_rememberpassword.setChecked(false);
        }
        //判断是否记住密码
        if (rememberPassword()) {
            et_inputname.setText(getLocalName());
            et_inputpassword.setText(getLocalPassword());
            cb_login_rememberpassword.setChecked(true);
        } else {
            et_inputname.setText(getLocalName());
            et_inputpassword.setText("");
        }
        //判断是否自动登录
        if (autoLogin()) {
            cb_login_autologin.setChecked(true);
            Toast.makeText(LoginActivity.this, "正在自动登录", Toast.LENGTH_SHORT).show();
            // login();//去登录就可以

        }
    }

    //获得保存在本地的用户名
    public String getLocalName() {
        String name = (String) helper.getParam("name", "用户名");
        return name;
    }

    //获得保存在本地的密码
    public String getLocalPassword() {
        String password = (String) helper.getParam("password", "password");
        //  return Base64Utils.decryptBASE64(password);   //解码一下
        return password;
    }

    //判断是否第一次登陆
    private boolean firstLogin() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        boolean first = (boolean) helper.getParam("first", true);
        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空
            helper.setParam("first", false);
            helper.setParam("rememberPassword", false);
            helper.setParam("autoLogin", false);
            helper.setParam("name", "");
            helper.setParam("password", "");
            return true;
        }
        return false;
    }

    //判断是否记住密码
    private boolean rememberPassword() {
        boolean rememberPassword = (boolean) helper.getParam("rememberPassword", false);
        return rememberPassword;
    }

    //判断是否自动登录
    private boolean autoLogin() {
        boolean autoLogin = (boolean) helper.getParam("autoLogin", false);
        return autoLogin;
    }

    //记住密码和自动登录的状态
    private void loadCheckBoxState() {

        if (cb_login_autologin.isChecked()) {
            //创建记住密码和自动登录是都选择,保存密码数据

            helper.setParam("rememberPassword", true);
            helper.setParam("autoLogin", true);
            helper.setParam("name", et_inputname.getText().toString().trim());
            helper.setParam("password", et_inputpassword.getText().toString().trim());

        } else if (!cb_login_rememberpassword.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            helper.setParam("rememberPassword", false);
            helper.setParam("autoLogin", false);
            helper.setParam("name", et_inputname.getText().toString().trim());
            helper.setParam("password", "");

        } else if (cb_login_rememberpassword.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            helper.setParam("rememberPassword", true);
            helper.setParam("autoLogin", false);
            helper.setParam("name", et_inputname.getText().toString().trim());
            helper.setParam("password", et_inputpassword.getText().toString().trim());
        }
    }


    //获取账号
    private String getAccount() {
        return et_inputname.getText().toString().trim();//去掉空格
    }

    //获取密码
    private String getPassword() {
        return et_inputpassword.getText().toString().trim();//去掉空格
    }


    //登录
    private void login() {
        Log.i("TAG", "login");

        Call<ResponseBody> call = httpService.login(getAccount(), getPassword());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadCheckBoxState();
                // 在这里根据返回内容执行具体的逻辑
                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = response.toString();
                handler.sendMessage(message);

                try {
                    String result = response.body().string();
                    if ("-1".equals(result)) {
                        Log.i("tag", "login fail");
                        Toast.makeText(LoginActivity.this, "账号或者密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("tag", "login success");
                        Log.i("tag", result);
                        Message_RESPONSE = response.toString();
                        Intent setIntent = new Intent();

                        //setIntent.setClass(LoginActivity.this, ShowCompanyActivity.class);
                        setIntent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(setIntent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 在这里对异常情况进行处理
                Toast.makeText(LoginActivity.this, "登录链接异常", Toast.LENGTH_SHORT).show();
                Log.i("tag", "login fail");
            }
        });


        /**
         Call<ResponseBody> allCouresServlet = httpService.getAllCouresServlet();
         try{
         Response<ResponseBody> response = allCouresServlet.execute();
         System.out.println(response.body().string());
         } catch (Exception e){
         e.printStackTrace();
         }
         */

        /**
         Call<ResponseBody> allCouresServlet = httpService.getAllCouresServlet();
         allCouresServlet.enqueue(new Callback<ResponseBody>() {
        @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        Log.i("tag","onres" + response.toString());
        Log.i("tag",response.message());
        try {
        Log.i("tag",response.body().string());
        } catch (IOException e) {
        e.printStackTrace();
        }
        }

        @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.i("tag","onresfail");
        }
        });

         */


    }
}

