package com.combofish.selectsubject.utils

import android.os.Build
import android.view.View
import android.view.Window

class StatusBarUtils {
    fun statusBarSet(window: Window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}