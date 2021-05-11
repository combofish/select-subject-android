package com.combofish.selectsubject

import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.combofish.selectsubject.data.DataGlobal
import com.combofish.selectsubject.fragment.CoursesFragment
import com.combofish.selectsubject.fragment.MeFragment
import com.combofish.selectsubject.fragment.MyFragment
import com.combofish.selectsubject.fragment.SelectedCoursesFragment
import com.combofish.selectsubject.utils.StatusBarUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // bar setting
        StatusBarUtils().statusBarSet(window)

        // 尝试初始化 context 全局变量
        // DataGlobal.context = applicationContext

        /**
        var intent = Intent(this,CoursesActivity::class.java)
        startActivity(intent)
        finish()
         */
        //val myFragment1 = MyFragment()
        val myFragment1 = CoursesFragment()
        myFragment1.message = "home"

        // val myFragment2 = MyFragment()
        val myFragment2 = SelectedCoursesFragment()
        myFragment2.message = "Search"

        //val myFragment3 = MyFragment()
        val myFragment3 = MeFragment()
        myFragment3.message = "Me"

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        if (DataGlobal.mainFragmentId.equals(0)) {
            replaceFragment(myFragment1)
        } else if (DataGlobal.mainFragmentId.equals(1)) {
            replaceFragment(myFragment2)
            bottomNavigationView.selectedItemId = R.id.navigation_item2
            DataGlobal.mainFragmentId = 0
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.navigation_item1 -> replaceFragment(myFragment1)
                R.id.navigation_item2 -> replaceFragment(myFragment2)
                R.id.navigation_item3 -> replaceFragment(myFragment3)
            }
            true
        }
    }

    open fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_use, fragment)
        // transaction.addToBackStack(null);
        transaction.commit()
    }

}