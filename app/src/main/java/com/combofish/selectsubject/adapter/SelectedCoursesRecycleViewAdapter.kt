package com.combofish.selectsubject.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.combofish.selectsubject.CourseActivity
import com.combofish.selectsubject.CoursesActivity
import com.combofish.selectsubject.R
import com.combofish.selectsubject.bean.Course
import com.combofish.selectsubject.data.DataGlobal
import com.google.gson.Gson


@Suppress("DEPRECATION")
class SelectedCoursesRecycleViewAdapter(
    private var courses: List<Course>,
    private val context: Context
) :
    RecyclerView.Adapter<SelectedCoursesRecycleViewAdapter.RRViewHolder>() {

    private val TAG = "CoursesRecycleViewAdapt"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RRViewHolder {
        val view = View.inflate(context, R.layout.course_item_rv, null)
        return RRViewHolder(view)
    }

    fun changeCourses(newCourse: MutableList<Course>) {
        //notifyItemMoved(0,oldCourse.size)
        courses = newCourse
        // notifyItemMoved(0,5)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RRViewHolder, position: Int) {
        Log.i("TAG", "getPosition: $position")
        //Log.i("TAG", "position: ${rr[position].rememberTime}")
        Log.i(TAG, "$courses")

        holder.tvCourseName.setText("${courses[position].name}")
        holder.tvCredit.setText("${courses[position].credit}")
        holder.tvClassTime.setText("${courses[position].class_time}")

        holder.tvAvailableNumber.setText("${courses[position].available_amount}")
        // 取消显示余量
        if (DataGlobal.account.status.equals(0)) {
            holder.tvAvailableNumber.visibility = View.GONE
            holder.tvAvailableNumberStr.visibility = View.GONE
        }

        holder.tvTeacher.setText("${courses[position].account_name}")
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    inner class RRViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var tvCourseName: TextView
        var tvCredit: TextView
        var tvClassTime: TextView
        var tvTeacher: TextView
        var tvAvailableNumber: TextView
        var tvAvailableNumberStr: TextView
        var watchDetail: Button

        init {
            tvCourseName = itemView.findViewById(R.id.tv_course_name)
            tvCredit = itemView.findViewById(R.id.tv_credit)
            tvClassTime = itemView.findViewById(R.id.tv_class_time)
            tvAvailableNumber = itemView.findViewById(R.id.tv_available_number)
            tvAvailableNumberStr = itemView.findViewById(R.id.tv_available_number_str)
            tvTeacher = itemView.findViewById(R.id.tv_teacher)

            watchDetail = itemView.findViewById(R.id.watchDetail)
            watchDetail.setOnClickListener(this)

            itemView.setOnClickListener {
                mOnItemCLickListener?.onRecycleItemClick(adapterPosition)
            }
        }

        override fun onClick(v: View?) {
            Log.i(TAG, "watchDetail button press,position: ")

            var intent = Intent(context, CourseActivity::class.java)
            var course = courses[position]
            intent.putExtra("course", Gson().toJson(course))
            intent.putExtra("opType", "1")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

    }

    var mOnItemCLickListener: OnRecycleItemClickListener? =
        null

    fun setRecycleItemClickListener(listener: OnRecycleItemClickListener?) {
        mOnItemCLickListener = listener
    }

    interface OnRecycleItemClickListener {
        fun onRecycleItemClick(position: Int)
    }

}