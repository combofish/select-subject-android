package com.combofish.selectsubject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.combofish.selectsubject.bean.ResultMessage
import com.combofish.selectsubject.data.DataGlobal
import com.combofish.selectsubject.utils.SharedPreferencesUtils
import com.combofish.selectsubject.utils.StatusBarUtils
import com.combofish.selectsubject.webapi.impl.HttpServiceImpl.httpService
import com.google.gson.Gson
import com.select.bean.Account
import com.select.utils.IdNameTurn
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private val TAG = "LoginActivity"
    private lateinit var et_inputname: EditText
    private lateinit var et_inputpassword: EditText
    private lateinit var bt_login: Button
    private lateinit var cb_login_rememberpassword: CheckBox
    private lateinit var cb_login_autologin: CheckBox

    // create switch for local debug
    private lateinit var switcher: Switch

    private var helper: SharedPreferencesUtils? = null
    private val gson = Gson()

    val handler: Handler = Handler {
        when (it.what) {
            0 -> {
                try {
                    var str = it.obj
                    Log.d(TAG, "Handle login msg: ${str}")
                    Log.i(TAG, "login fail")
                    Toast.makeText(this@LoginActivity, "账号或者密码错误，请重新输入", Toast.LENGTH_SHORT)
                        .show()
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            1 -> {
                try {
                    var str = it.obj as String
                    Log.d(TAG, "Handle login msg: ${str}")
                    if (str != null) {
                        DataGlobal.accountId = str.toInt()
                        // DataGlobal.accountStatus =
                        Log.i(TAG, "login success. Set global account id = ${DataGlobal.accountId}")
                        updateAccountDate()
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        "${applicationContext.getString(R.string.login_success)}",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    Log.i("tag", "login success")
                    val setIntent = Intent()
                    setIntent.setClass(this@LoginActivity, MainActivity::class.java)
                    startActivity(setIntent)
                    finish()
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            10 -> {
                try {
                    var str = it.obj as Account
                    Log.d(TAG, "Net Work update me account: ${str}")
                    DataGlobal.account = str

                    // 存储用户数据到全局
                    //DataGlobal.accountStatus = account.status
                    //DataGlobal.accountStatusStr = IdNameTurn.accountStatusNumToStr(account.status)
                    Log.i(TAG, "Update DataGlobal account: ${DataGlobal.account}")

                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            else -> {
                Log.d(TAG, "login handler else")
            }
        }
        false
    }

    private fun updateAccountDate() {
        val call = httpService.getAccountByAccountId(DataGlobal.accountId)
        Log.i(
            TAG,
            "Try to get account information through network with id: ${DataGlobal.accountId}"
        )

        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {
                    val result = response.body()
                    val string = result!!.string()
                    val account_inner = gson.fromJson(string, Account::class.java)
                    Log.i(TAG, "Get me account: ${account_inner}")

                    var msg = Message()
                    msg.obj = account_inner
                    msg.what = 10
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                // 在这里对异常情况进行处理
                //Toast.makeText(this@CoursesActivity, "登录链接异常", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Refresh me account fail")
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // bar setting
        StatusBarUtils().statusBarSet(window)

        helper = SharedPreferencesUtils(this.applicationContext, "setting")

        initView()
        setListener()
        initData()
    }

    //初始化布局
    private fun initView() {
        et_inputname = findViewById(R.id.et_name)
        et_inputpassword = findViewById(R.id.et_password)
        bt_login = findViewById(R.id.bt_login)
        cb_login_rememberpassword = findViewById(R.id.cb_login_rememberpassword)
        cb_login_autologin = findViewById(R.id.cb_login_autologin)

        // init switch
        switcher = findViewById(R.id.switchForLocalDebug)
        switcher.setOnCheckedChangeListener(this)
    }

    //点击事件响应
    private fun setListener() {
        bt_login!!.setOnClickListener(View.OnClickListener {
            if (account.isEmpty()) {
                Toast.makeText(this@LoginActivity, "你输入的账号为空！", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "你输入的密码为空！", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                // Toast.makeText(LoginActivity.this, "登录中......", Toast.LENGTH_SHORT).show();
                // Intent intent_loginWelcome = new Intent();
                // intent_loginWelcome.setClass(LoginActivity.this,WelcomeActivity.class);
                // LoginActivity.this.startActivity(intent_loginWelcome);
                //finish();
                login()
            }
        })
    }

    //判断初始化数据
    private fun initData() {
        //判断是否第一次登录
        if (firstLogin()) {
            cb_login_rememberpassword!!.isChecked = false
        }
        //判断是否记住密码
        if (rememberPassword()) {
            et_inputname!!.setText(localName)
            et_inputpassword!!.setText(localPassword)
            cb_login_rememberpassword!!.isChecked = true
        } else {
            et_inputname!!.setText(localName)
            et_inputpassword!!.setText("")
        }
        //判断是否自动登录
        if (autoLogin()) {
            cb_login_autologin!!.isChecked = true
            Toast.makeText(this@LoginActivity, "正在自动登录", Toast.LENGTH_SHORT).show()
            // login();//去登录就可以
        }
    }

    //获得保存在本地的用户名
    val localName: String?
        get() = helper!!.getParam(
            "name",
            "用户名"
        ) as String?//  return Base64Utils.decryptBASE64(password);   //解码一下

    //获得保存在本地的密码
    val localPassword: String?
        get() =//  return Base64Utils.decryptBASE64(password);   //解码一下
            helper!!.getParam("password", "password") as String?

    //判断是否第一次登陆
    private fun firstLogin(): Boolean {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        val first = helper!!.getParam("first", true) as Boolean
        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空
            helper!!.setParam("first", false)
            helper!!.setParam("rememberPassword", false)
            helper!!.setParam("autoLogin", false)
            helper!!.setParam("name", "")
            helper!!.setParam("password", "")
            return true
        }
        return false
    }

    //判断是否记住密码
    private fun rememberPassword(): Boolean {
        return helper!!.getParam("rememberPassword", false) as Boolean
    }

    //判断是否自动登录
    private fun autoLogin(): Boolean {
        return helper!!.getParam("autoLogin", false) as Boolean
    }

    //记住密码和自动登录的状态
    private fun loadCheckBoxState() {
        if (cb_login_autologin!!.isChecked) {
            //创建记住密码和自动登录是都选择,保存密码数据
            helper!!.setParam("rememberPassword", true)
            helper!!.setParam("autoLogin", true)
            helper!!.setParam("name", et_inputname!!.text.toString().trim { it <= ' ' })
            helper!!.setParam("password", et_inputpassword!!.text.toString().trim { it <= ' ' })
        } else if (!cb_login_rememberpassword!!.isChecked) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            helper!!.setParam("rememberPassword", false)
            helper!!.setParam("autoLogin", false)
            helper!!.setParam("name", et_inputname!!.text.toString().trim { it <= ' ' })
            helper!!.setParam("password", "")
        } else if (cb_login_rememberpassword!!.isChecked) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            helper!!.setParam("rememberPassword", true)
            helper!!.setParam("autoLogin", false)
            helper!!.setParam("name", et_inputname!!.text.toString().trim { it <= ' ' })
            helper!!.setParam("password", et_inputpassword!!.text.toString().trim { it <= ' ' })
        }
    }//去掉空格

    //获取账号
    private val account: String
        private get() = et_inputname!!.text.toString().trim { it <= ' ' } //去掉空格

    //去掉空格
    //获取密码
    private val password: String
        private get() = et_inputpassword!!.text.toString().trim { it <= ' ' } //去掉空格

    //登录
    private fun login() {
        Log.i("TAG", "login")
        val call = httpService.login(account, password)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                loadCheckBoxState()
                // 在这里根据返回内容执行具体的逻辑
                try {
                    val result = response.body()
                    val string = result!!.string()
                    val fromJson = gson.fromJson(string, ResultMessage::class.java)
                    Log.i(TAG, "Login Result: ${fromJson}")

                    var msg = Message()
                    msg.obj = fromJson.msg
                    msg.what = fromJson.flag
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                // 在这里对异常情况进行处理
                Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "login fail")
            }
        })
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            DataGlobal.url = DataGlobal.url2

        } else {
            DataGlobal.url = DataGlobal.url1
        }
        Log.i(TAG,"Now url is :${DataGlobal.url}")
    }
}