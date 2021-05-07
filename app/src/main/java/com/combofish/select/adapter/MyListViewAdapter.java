package com.combofish.select.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.select.MainActivity;
import com.example.select.bean.Course;
import com.example.uselogin.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MyListViewAdapter extends BaseAdapter {
    // private List<Bean> data;
    private List<Course> data;
    private Context context;

    public MyListViewAdapter(List<Course> data, MainActivity context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_view, parent, false);

            viewHolder.textView_name = convertView.findViewById(R.id.tv_course_name);
            viewHolder.textView_credit = convertView.findViewById(R.id.tv_credit);
            viewHolder.textView_class_time = convertView.findViewById(R.id.tv_class_time);
            viewHolder.textView_teacher = convertView.findViewById(R.id.tv_teacher);
            viewHolder.textView_available_amount = convertView.findViewById(R.id.tv_available_number);
            // viewHolder.btnView = convertView.findViewById(R.id.btn1);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView_name.setText(data.get(position).getName());
        viewHolder.textView_credit.setText("学分：" + data.get(position).getCredit());
        viewHolder.textView_class_time.setText("课时：" + data.get(position).getClass_time());
        viewHolder.textView_teacher.setText(data.get(position).getIntroduction());
        viewHolder.textView_available_amount.setText("剩余容量：" + data.get(position).getAvailable_amount());
        //viewHolder.btnView.setText(data.get(position).getBtnStr());

        Log.i(TAG,"getView: " + position);
        return convertView;


        /**
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv, parent, false);
        }
        Button button = convertView.findViewById(R.id.btn1);
        TextView textView = convertView.findViewById(R.id.tv1);
        textView.setText(data.get(position).getTextStr());
        button.setText(data.get(position).getBtnStr());

        Log.i(TAG,"getView: " + position);
        return convertView;
         */
    }

    private final class ViewHolder{
        TextView textView_name;
        TextView textView_credit;
        TextView textView_class_time;
        TextView textView_teacher;
        TextView textView_available_amount;

        // Button btnView;

    }
}
