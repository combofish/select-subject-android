package com.combofish.selectsubject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.combofish.selectsubject.adapter.CoursesRecycleViewAdapter
import com.combofish.selectsubject.bean.Course
import com.combofish.selectsubject.data.DataGlobal
import com.combofish.selectsubject.utils.StatusBarUtils
import com.combofish.selectsubject.webapi.HttpService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CoursesActivity : AppCompatActivity() {


    private val TAG = "CoursesActivity"
    val coursesType = object : TypeToken<List<Course?>?>() {}.type
    private lateinit var retrofit: Retrofit
    private lateinit var httpService: HttpService
    private lateinit var gson: Gson

    // ui
    private lateinit var recyclerView: RecyclerView
    private lateinit var coursesRecycleViewAdapter: CoursesRecycleViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager


    val handler: Handler = Handler {
        when (it.what) {
            1 -> {
                try {
                    var t: List<Course> = it.obj as List<Course>
                    Log.d(TAG, "Handle : " + t[0].name)

                    coursesRecycleViewAdapter = CoursesRecycleViewAdapter(t, this)
                    recyclerView.adapter = coursesRecycleViewAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        Log.i(TAG,"on courses Activity")

        // bar setting
        StatusBarUtils().statusBarSet(window)

        // 初始化网络访问
        val url = DataGlobal.url
        retrofit = Retrofit.Builder().baseUrl(url).build()
        httpService = retrofit.create(HttpService::class.java)
        gson = Gson()

        // 布局初始化
        recyclerView = findViewById(R.id.rv_courses)
        linearLayoutManager = LinearLayoutManager(this);
        recyclerView.layoutManager = linearLayoutManager

        initData()

        /**
        val coursesRecycleViewAdapter = CoursesRecycleViewAdapter(courses, this)
        recyclerView.adapter = coursesRecycleViewAdapter
         */

    }

    /**
     * 初始化记录数据
     */
    private fun initData() {
        var courses: List<Course>
        val call = httpService.allCoursesServlet
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {
                    val result = response.body()
                    val string = result!!.string()
                    courses = gson.fromJson(string, coursesType)
                    Log.i(TAG, "Get Courses: $courses")

                    var msg = Message()
                    msg.obj = courses
                    msg.what = 1
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                // 在这里对异常情况进行处理
                Toast.makeText(this@CoursesActivity, "登录链接异常", Toast.LENGTH_SHORT).show()
                Log.i("tag", "login fail")
            }
        })
    }
}