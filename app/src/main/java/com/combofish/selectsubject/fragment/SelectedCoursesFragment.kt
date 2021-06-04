package com.combofish.selectsubject.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.combofish.selectsubject.R
import com.combofish.selectsubject.adapter.CoursesRecycleViewAdapter
import com.combofish.selectsubject.adapter.SelectedCoursesRecycleViewAdapter
import com.combofish.selectsubject.bean.Course
import com.combofish.selectsubject.data.DataGlobal
import com.combofish.selectsubject.webapi.HttpService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.select.utils.IdNameTurn
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

// class SelectedCoursesFragment : Fragment() ,AdapterView.OnItemSelectedListener{
class SelectedCoursesFragment : Fragment() {
    var message: String? = null

    private val TAG = "CoursesFragment"
    val coursesType = object : TypeToken<List<Course?>?>() {}.type
    private lateinit var retrofit: Retrofit
    private lateinit var httpService: HttpService
    private lateinit var gson: Gson

    // ui
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedCoursesRecycleViewAdapter: SelectedCoursesRecycleViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    // show role
    private lateinit var userRole: TextView

    // selected courses
    private var courses = mutableListOf<Course>()

    val handler: Handler = Handler {
        when (it.what) {
            1 -> {
                try {
                    var t: List<Course> = it.obj as List<Course>
                    Log.d(TAG, "Handle : " + t[0].name)

                    val selectedCoursesRecycleViewAdapter =
                        SelectedCoursesRecycleViewAdapter(t, activity!!.applicationContext)
                    recyclerView.adapter = selectedCoursesRecycleViewAdapter

                    //coursesRecycleViewAdapter = CoursesRecycleViewAdapter(t, activity!!.applicationContext)
                    //recyclerView.adapter = coursesRecycleViewAdapter

                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            2 -> {
                try {

                    var t = it.obj as MutableList<Course>
                    Log.d(TAG, "Handle : $t")

                    courses = t
                    //coursesRecycleViewAdapter.changeCourses(t)
                    selectedCoursesRecycleViewAdapter.changeCourses(t)

                    if (t.isEmpty()) {
                        Log.i(
                            TAG,
                            "Selected Courses for name: ${DataGlobal.account.name} is empty: ${t}"
                        )
                        Toast.makeText(
                            this.context,
                            "${this.context?.getString(R.string.selectCoursesEmpty)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    //val selectedCoursesRecycleViewAdapter =
                    //    SelectedCoursesRecycleViewAdapter(t, activity!!.applicationContext)
                    //recyclerView.adapter = selectedCoursesRecycleViewAdapter

                    //coursesRecycleViewAdapter = CoursesRecycleViewAdapter(t, activity!!.applicationContext)
                    //recyclerView.adapter = coursesRecycleViewAdapter

                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            20 -> {
                try {

                    var t = it.obj as MutableList<Course>
                    Log.d(TAG, "Handle : $t")

                    courses = t
                    selectedCoursesRecycleViewAdapter.changeCourses(t)

                    if (t.isEmpty()) {
                        Log.i(
                            TAG,
                            "Teacher's Courses for name: ${DataGlobal.account.name} is empty: ${t}"
                        )
                        Toast.makeText(
                            this.context,
                            "${this.context?.getString(R.string.teacherCoursesEmpty)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_base, null)
        val inflate = inflater.inflate(R.layout.fragment_selected_courses, null)
        Log.i(TAG, "in courseFragment!")
        return inflate
    }

    /**
    private lateinit var spinner: Spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    Log.i(TAG, "Select position: ${position}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
    }

    private fun initSpinner(view:View) {
    spinner = view.findViewById(R.id.spinner1)
    val createFromResource = ArrayAdapter.createFromResource(view.context, R.array.city, android.R.layout.simple_spinner_item)
    createFromResource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = createFromResource
    spinner.onItemSelectedListener = this
    }
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val viewById = view.findViewById<TextView>(R.id.courseName)
        //viewById.text = message


        Log.i(TAG, "on courses Activity")


        // 初始化下拉列表
        // initSpinner(view)

        // 显示用户角色
        userRole = view.findViewById(R.id.select_course_userRole)
        userRole.setText("${IdNameTurn.accountStatusNumToStr(DataGlobal.account.status)}")

        /**
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
         */

        // 初始化网络访问
        val url = DataGlobal.url
        retrofit = Retrofit.Builder().baseUrl(url).build()
        httpService = retrofit.create(HttpService::class.java)
        gson = Gson()

        // 布局初始化
        recyclerView = view.findViewById(R.id.selected_course_rv_courses)
        linearLayoutManager = LinearLayoutManager(context!!.applicationContext);
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(context!!.applicationContext,DividerItemDecoration.VERTICAL))

        // 初始化所有课程数据
        // initData()

        // 初始化已选的课程数据
        if(DataGlobal.account.status.equals(0)){
            initDataOfSelectedCourse()
        }else if (DataGlobal.account.status.equals(1)){
            initDataOfCoursesForTeacher()
        }

        //coursesRecycleViewAdapter =
        // CoursesRecycleViewAdapter(courses = listOf<Course>(), activity!!.applicationContext)
        // recyclerView.adapter = coursesRecycleViewAdapter

        selectedCoursesRecycleViewAdapter =
            SelectedCoursesRecycleViewAdapter(listOf(), activity!!.applicationContext)
        recyclerView.adapter = selectedCoursesRecycleViewAdapter
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
                //Toast.makeText(this, "登录链接异常", Toast.LENGTH_SHORT).show()
                Log.i("tag", "login fail")
            }
        })
    }

    private fun initDataOfCoursesForTeacher() {
        var courses: List<Course>
        val call = httpService.getCoursesByAccountId(DataGlobal.accountId)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {
                    val result = response.body()
                    val string = result!!.string()
                    courses = gson.fromJson(string, coursesType)
                    Log.i(TAG, "Get Courses: $courses")

                    var msg = Message()
                    msg.obj = courses
                    msg.what = 20
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                // 在这里对异常情况进行处理
                //Toast.makeText(this, "登录链接异常", Toast.LENGTH_SHORT).show()
                Log.i("tag", "login fail")
            }
        })
    }

    private fun initDataOfSelectedCourse() {
        var courses: List<Course>
        val call = httpService.getSelectedCoursesByAccountId(DataGlobal.accountId)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {
                    val result = response.body()
                    val string = result!!.string()
                    courses = gson.fromJson(string, coursesType)
                    Log.i(TAG, "Get Courses: $courses")

                    var msg = Message()
                    msg.obj = courses
                    msg.what = 2
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                // 在这里对异常情况进行处理
                //Toast.makeText(this, "登录链接异常", Toast.LENGTH_SHORT).show()
                Log.i("tag", "login fail")
            }
        })
    }
}