package com.combofish.selectsubject.utils

import android.app.Application
import android.content.Context
import com.combofish.selectsubject.data.DataGlobal

class SharedPreferencesTools{

    fun processToFirstLogin(helper:SharedPreferencesUtils) {
        helper.setParam("first", true)
        helper.setParam("rememberPassword", false)
        helper.setParam("autoLogin", false)
        helper.setParam("password", "")
    }

    fun processForAfterChangePassword(helper:SharedPreferencesUtils) {
        helper.setParam("rememberPassword", false)
        helper.setParam("autoLogin", false)
        helper.setParam("password", "")
    }
}