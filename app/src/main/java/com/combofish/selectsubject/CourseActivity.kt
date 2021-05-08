package com.combofish.selectsubject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.combofish.selectsubject.bean.Course
import com.google.gson.Gson
import com.select.utils.IdNameTurn

class CourseActivity : AppCompatActivity() {
    private val TAG = "CourseActivity"
    private lateinit var course: Course

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
    }

    private fun initData() {
        val stringExtra = intent.getStringExtra("course")
        course = Gson().fromJson(stringExtra, Course::class.java)
        Log.i(TAG, "$course")
    }
}