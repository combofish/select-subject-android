package com.combofish.selectsubject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.combofish.selectsubject.bean.ResultMessage
import com.combofish.selectsubject.data.DataGlobal
import com.combofish.selectsubject.utils.SharedPreferencesTools
import com.combofish.selectsubject.utils.SharedPreferencesUtils
import com.combofish.selectsubject.utils.StatusBarUtils
import com.combofish.selectsubject.webapi.impl.HttpServiceImpl
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "ChangePasswordActivity"

    private lateinit var originalPassword: String
    private lateinit var password_ori: EditText
    private lateinit var password_new2: EditText
    private lateinit var password_new1: EditText
    private lateinit var changePasswordButton: Button

    // private var helper = SharedPreferencesUtils(DataGlobal.applicationContext,"setting")
    // private var helper = SharedPreferencesUtils(DataGlobal.applicationContext,"setting")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.change_password_changeP -> {
                Log.i(TAG, "change password pressed!")
                if (oriP.equals(originalPassword)) {
                    if (newP1.equals(newP2) && newP1.isNotEmpty() && newP2.isNotEmpty()) {
                        Toast.makeText(this.applicationContext, "正在修改密码", Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "Changing password")
                        changePasswordThroughNetwork(newP1, DataGlobal.accountId)

                    } else {
                        Toast.makeText(this.applicationContext, "新密码不相同或为空", Toast.LENGTH_SHORT)
                            .show()
                        Log.i(TAG, "new password wrong")
                    }
                } else {
                    Toast.makeText(this.applicationContext, "原密码书写错误", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "original password wrong")
                }
            }
        }
    }

    val handler: Handler = Handler {
        when (it.what) {
            0 -> {
                try {
                    var str = it.obj
                    Log.d(TAG, "Handle login msg: ${str}")
                    Log.i(TAG, "login fail")
                    Toast.makeText(applicationContext, "${applicationContext.getString(R.string.change_password_fail)}", Toast.LENGTH_SHORT)
                       .show()
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            1 -> {
                try {
                    var str = it.obj as String
                    Log.d(TAG, "Handle Change password msg: ${str}")
                    Toast.makeText(applicationContext, "${applicationContext.getString(R.string.change_password_success)}", Toast.LENGTH_SHORT)
                        .show()

                    // 清除密码记录
                    // processToFirstLogin()
                    val helper = SharedPreferencesUtils(this.applicationContext,"setting")
                    // SharedPreferencesTools().processToFirstLogin(helper)
                    SharedPreferencesTools().processForAfterChangePassword(helper)
                    val setIntent = Intent(this,LoginActivity::class.java)
                    startActivity(setIntent)
                    finish()
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            else -> {
                Log.d(TAG, "Change password handler else")
            }
        }
        false
    }

    private val gson = Gson()
    private fun changePasswordThroughNetwork(newP: String, accountId: Int) {
        Log.i(TAG, "Change password. new Password: ${newP}. accountId: ${accountId}")
        val call = HttpServiceImpl.httpService.changePasswordByAccountId(newP, accountId)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
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
                Toast.makeText(applicationContext, "修改密码失败", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "login fail")
            }
        })
    }


    private val oriP: String
        private get() = password_ori!!.text.toString().trim() { it <= ' ' }

    private val newP1: String
        private get() = password_new1!!.text.toString().trim() { it <= ' ' }

    private val newP2: String
        private get() = password_new2!!.text.toString().trim() { it <= ' ' }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // bar setting
        StatusBarUtils().statusBarSet(window)

        originalPassword = intent.getStringExtra("password")
        Log.i(TAG, "Get Original password is ${originalPassword}")
        initView()
    }

    private fun initView() {
        password_ori = findViewById(R.id.et_password_changeP)
        password_new1 = findViewById(R.id.et_password_changeP2)
        password_new2 = findViewById(R.id.et_password_changeP3)

        changePasswordButton = findViewById(R.id.change_password_changeP)
        changePasswordButton.setOnClickListener(this)
    }

}