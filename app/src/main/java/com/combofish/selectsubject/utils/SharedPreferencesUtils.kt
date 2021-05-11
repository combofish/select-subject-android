package com.combofish.selectsubject.utils;

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils(private val context: Context?, private val fileName: String) {
//class SharedPreferencesUtils(private val fileName: String) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    fun setParam(key: String?, `object`: Any) {
        val type = `object`.javaClass.simpleName
        if ("String" == type) {
            editor.putString(key, `object` as String)
        } else if ("Integer" == type) {
            editor.putInt(key, (`object` as Int))
        } else if ("Boolean" == type) {
            editor.putBoolean(key, (`object` as Boolean))
        } else if ("Float" == type) {
            editor.putFloat(key, (`object` as Float))
        } else if ("Long" == type) {
            editor.putLong(key, (`object` as Long))
        }
        editor.commit()
    }

    fun getParam(key: String?, defaultObject: Any): Any? {
        val type = defaultObject.javaClass.simpleName
        if ("String" == type) {
            return sharedPreferences.getString(key, defaultObject as String)
        } else if ("Integer" == type) {
            return sharedPreferences.getInt(key, (defaultObject as Int))
        } else if ("Boolean" == type) {
            return sharedPreferences.getBoolean(key, (defaultObject as Boolean))
        } else if ("Float" == type) {
            return sharedPreferences.getFloat(key, (defaultObject as Float))
        } else if ("Long" == type) {
            return sharedPreferences.getLong(key, (defaultObject as Long))
        }
        return null
    }

    fun clear() {
        editor.clear().commit()
    }

    init {
        //sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sharedPreferences = context!!.getSharedPreferences(fileName, 0)
        editor = sharedPreferences.edit()
    }
}