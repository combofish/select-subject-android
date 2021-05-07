package com.example.select;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.combofish.select.adapter.MyListViewAdapter;
import com.example.select.bean.Course;
import com.example.uselogin.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Course> data = new ArrayList<>();
    //private List<Bean> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_welcome);
        initData();
        ListView viewById = findViewById(R.id.lv1);
        viewById.setAdapter(new MyListViewAdapter(data, this));
    }

    private void addCourse(String name,int credit,int classTime, String introduction, int availableAmount){
        Course course1 = new Course();
        course1.setName(name);
        course1.setCredit(credit);
        course1.setClass_time(classTime);
        course1.setIntroduction(introduction);
        course1.setAvailable_amount(availableAmount);
        data.add(course1);
    }

    private void initData() {
        

        addCourse("嵌入式开发技术与应用",2,48,"王伟",30);
        addCourse("高频电子技术",4,64,"田学民",45);
        addCourse("电磁场与电磁波",3,36,"田汉民",25);
        addCourse("MATLAB开发技术与应用",1,24,"李薇薇",50);



        /**
        for (int i = 0; i < 100; i++) {
            Bean bean = new Bean();
            // bean.setBtnStr("btn" + i);
            bean.setTextStr("text" + i);
            data.add(bean);

        }
        */

    }
}
