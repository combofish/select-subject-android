package com.combofish.selectsubject

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.combofish.selectsubject.fragment.CoursesFragment
import com.combofish.selectsubject.fragment.MyFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
        var intent = Intent(this,CoursesActivity::class.java)
        startActivity(intent)
        finish()
         */
        //val myFragment1 = MyFragment()
        val myFragment1 = CoursesFragment()
        myFragment1.message = "home"

        val myFragment2 = MyFragment()
        myFragment2.message = "Search"
        val myFragment3 = MyFragment()
        myFragment3.message = "Me"

        replaceFragment(myFragment1)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
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