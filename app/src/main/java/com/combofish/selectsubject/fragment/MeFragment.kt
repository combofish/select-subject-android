package com.combofish.selectsubject.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.combofish.selectsubject.ChangePasswordActivity
import com.combofish.selectsubject.LoginActivity
import com.combofish.selectsubject.R
import com.combofish.selectsubject.data.DataGlobal
import com.combofish.selectsubject.utils.SharedPreferencesTools
import com.combofish.selectsubject.utils.SharedPreferencesUtils
import com.combofish.selectsubject.webapi.impl.HttpServiceImpl.httpService
import com.google.gson.Gson
import com.select.bean.Account
import com.select.utils.IdNameTurn
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeFragment : Fragment(), View.OnClickListener {

    private val TAG = "MeFragment"

    private lateinit var me_department: TextView
    private lateinit var me_email: TextView
    private lateinit var me_status: TextView
    private lateinit var me_description: TextView
    private lateinit var me_phone: TextView
    private lateinit var me_major: TextView
    private lateinit var me_sex: TextView

    private lateinit var me_name: TextView
    private lateinit var me_passport: TextView

    private lateinit var me_password: Button
    private lateinit var logout: Button

    // 修改密码或退出登录后清除自动登录标志
    // private lateinit var helper:SharedPreferencesUtils

    private var account = Account()
    private val gson = Gson()
    val handler: Handler = Handler {
        when (it.what) {
            10 -> {
                try {
                    var str = it.obj as Account
                    Log.d(TAG, "Net Work update me account: ${str}")
                    account = str

                    // 存储用户数据到全局
                    //DataGlobal.accountStatus = account.status
                    //DataGlobal.accountStatusStr = IdNameTurn.accountStatusNumToStr(account.status)
                    //Log.i(TAG,"Update DataGlobal accountStatusStr: ${DataGlobal.accountStatusStr}")

                    // 重新刷新数据
                    showData()
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            else -> {
                Log.d(TAG, "handler else")
            }
        }
        false
    }

    var message: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_me, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val viewById = view.findViewById<TextView>(R.id.textView)
        //viewById.text = message

        //helper = SharedPreferencesUtils(this.context, "setting")
        initView(view)
        updateAccountDate()
        showData()
    }

    private fun showData() {
        me_department.setText("${IdNameTurn.departmentIdToName(account.department_id)}")
        me_email.setText("${account.email}")
        me_status.setText("${IdNameTurn.accountStatusNumToStr(account.status)}")
        me_description.setText("${account.description}")
        me_phone.setText("${account.phone}")
        me_major.setText("${IdNameTurn.majorIdToName(account.major_id)}")
        me_sex.setText("${IdNameTurn.accountSexNumToStr(account.sex)}")
        //me_password.setText("${account.password}")
        me_name.setText("${account.name}")
        me_passport.setText("${account.passport}")

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

    private fun initView(view: View) {
        me_department = view.findViewById(R.id.me_department)
        me_email = view.findViewById(R.id.me_email)
        me_status = view.findViewById(R.id.me_status)
        me_description = view.findViewById(R.id.me_description)
        me_phone = view.findViewById(R.id.me_phone)
        me_major = view.findViewById(R.id.me_major)
        me_sex = view.findViewById(R.id.me_sex)
        me_name = view.findViewById(R.id.me_name)
        me_passport = view.findViewById(R.id.me_passport)

        me_password = view.findViewById(R.id.me_password)
        logout = view.findViewById(R.id.me_logout)

        me_password.setOnClickListener(this)
        logout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.me_logout -> {
                Log.i(TAG,"Logout button pressed.")
                Toast.makeText(this.context, "${this.context?.getString(R.string.nowLogout)}", Toast.LENGTH_SHORT).show()

                //var helper = SharedPreferencesUtils(DataGlobal.applicationContext,"setting")
                //
                // 清除密码和自动登录记录
                val helper = SharedPreferencesUtils(this.context,"setting")
                SharedPreferencesTools().processToFirstLogin(helper)
                val intent = Intent(this.context,LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.me_password -> {
                Log.i(TAG,"Change password button pressed.")
                val intent = Intent(this.context,ChangePasswordActivity::class.java)
                intent.putExtra("password",account.password)
                startActivity(intent)
            }
        }
    }
}