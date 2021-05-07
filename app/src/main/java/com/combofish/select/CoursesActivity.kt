package com.example.select

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.select.bean.Course
import com.example.select.data.DataGlobal
import com.example.select.webapi.HttpService
import com.example.uselogin.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class CoursesActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var httpService: HttpService

    private var courses_g: List<Course> = mutableListOf()

    val handler: Handler = Handler {
        when (it.what) {
            1 -> {
                try {
                    var t:List<Course> = it.obj as List<Course>
                    Log.d(TAG, "t=" + t[0].name)
                    tv_courseName.setText(t[0].name)
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

    private lateinit var tv_courseName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)
        val url = DataGlobal().url
        retrofit = Retrofit.Builder().baseUrl(url).build()
        httpService = retrofit.create(HttpService::class.java)
        allCourses()
        initView()
    }

    private fun initView() {
        tv_courseName = findViewById<TextView>(R.id.courseName)
        // tv_courseName.setText(courses_g[0].name)
    }

    private fun allCourses() {
        Log.i("TAG", "login")
        val gson = Gson()
        var courses: List<Course>
        val call = httpService.allCoursesServlet
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {
                    val result = response.body()
                    val string = result!!.string()
                    val type = object : TypeToken<List<Course?>?>() {}.type
                    courses = gson.fromJson(string, type)
                    Log.i(TAG, courses[0].toString())

                    courses_g = courses
                    Log.i(TAG, courses_g.toString())
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

    companion object {
        private const val TAG = "CoursesActivity"
    }
}