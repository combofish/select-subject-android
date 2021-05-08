package com.combofish.selectsubject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.combofish.selectsubject.bean.Course
import com.combofish.selectsubject.bean.ResultMessage
import com.combofish.selectsubject.webapi.impl.HttpServiceImpl
import com.combofish.selectsubject.webapi.impl.HttpServiceImpl.httpService
import com.google.gson.Gson
import com.select.utils.IdNameTurn
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "CourseActivity"
    private lateinit var course: Course
    private val gson = Gson()

    // ui
    private lateinit var courseName: TextView
    private lateinit var courseCredit: TextView
    private lateinit var classTime: TextView
    private lateinit var teachTime: TextView
    private lateinit var classroom: TextView
    private lateinit var teacher: TextView
    private lateinit var department: TextView
    private lateinit var major: TextView
    private lateinit var courseType: TextView
    private lateinit var requireType: TextView
    private lateinit var availableNumber: TextView
    private lateinit var introduce: TextView
    private lateinit var selectCourse: Button


    val handler: Handler = Handler {
        when (it.what) {
            0 -> {
                try {
                    var str = it.obj
                    Log.d(TAG, "Handle msg: ${str}")
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            1 -> {
                try {
                    var str = it.obj
                    Log.d(TAG, "Handle msg: ${str}")
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            2 -> {
                try {
                    var str = it.obj
                    Log.d(TAG, "Handle msg: ${str}")
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            3 -> {
                try {
                    var str = it.obj
                    Log.d(TAG, "Handle msg: ${str}")
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            10 -> {
                try {
                    var str = it.obj as Course
                    Log.d(TAG, "Net Work update course: ${str}")
                    course = str

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        initData()
        initView()
        showData()
    }

    private fun showData() {
        courseName.setText("${course.name}")
        courseCredit.setText("${course.credit}")
        classTime.setText("${course.class_time}")
        teachTime.setText("${course.teach_time}")
        classroom.setText("${course.classroom}")
        teacher.setText("${course.account_name}")
        department.setText("${IdNameTurn.departmentIdToName(course.department_id)}")
        major.setText("${IdNameTurn.majorIdToName(course.major_id)}")
        courseType.setText("${IdNameTurn.courseTypeNumToStr(course.type)}")
        requireType.setText("${IdNameTurn.courseRequireTypeNumToStr(course.require_type)}")
        availableNumber.setText("${course.available_amount}")
        introduce.setText("${course.introduction}")
    }

    private fun initView() {
        courseName = findViewById(R.id.courseName)
        courseCredit = findViewById(R.id.courseCredit)
        classTime = findViewById(R.id.classTime)
        teachTime = findViewById(R.id.teachTime)
        classroom = findViewById(R.id.classroom)
        teacher = findViewById(R.id.teacher)
        department = findViewById(R.id.department)
        major = findViewById(R.id.major)
        courseType = findViewById(R.id.courseType)
        requireType = findViewById(R.id.requireType)
        availableNumber = findViewById(R.id.availableNumber)
        introduce = findViewById(R.id.introduce)

        selectCourse = findViewById(R.id.selectCourse)
        selectCourse.setOnClickListener(this)
    }

    private fun initData() {
        val stringExtra = intent.getStringExtra("course")
        course = Gson().fromJson(stringExtra, Course::class.java)
        Log.i(TAG, "$course")

        // 重新使用网络更新该课程数据
        updateCourseThroughNetwork(course.id)
    }

    private fun updateCourseThroughNetwork(courseId:Int) {
        val call = httpService.getCourseByCourseId(course.id)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {
                    val result = response.body()
                    val string = result!!.string()
                    val course_inner = gson.fromJson(string, course::class.java)
                    Log.i(TAG, "Get Courses: $course_inner")

                    var msg = Message()
                    msg.obj = course_inner
                    msg.what = 10
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                // 在这里对异常情况进行处理
                //Toast.makeText(this@CoursesActivity, "登录链接异常", Toast.LENGTH_SHORT).show()
                Log.i("tag", "login fail")
            }
        })
    }


    override fun onClick(v: View?) {
        Log.i(TAG, "Select Course: ${course}")

        // 测试
        selectCourseOp(1, course_id = course.id)
    }

    private fun selectCourseOp(account_id: Int, course_id: Int) {
        Log.i(TAG, "Wang to select: account_id ${account_id}, and course_id ${course_id}")
        val call =
            httpService.selectCourse(account = account_id.toString(), course = course_id.toString())
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {
                    val result = response.body()
                    val string = result!!.string()
                    val fromJson = gson.fromJson(string, ResultMessage::class.java)
                    Log.i(TAG, "Select course result: ${fromJson}")

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
                //Toast.makeText(this@CoursesActivity, "登录链接异常", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "login fail")
            }
        })
    }

}