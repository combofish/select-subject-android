package com.combofish.selectsubject.data

import com.combofish.selectsubject.bean.Course
import com.select.bean.Account


object DataGlobal{
    var url: String = "http://192.168.245.224:8080/SelectSubject2_war_exploded/"
    val url1 :String = "http://192.168.245.224:8080/SelectSubject2_war_exploded/"
    val url2 :String = "http://192.168.122.1:8080/SelectSubject2_war_exploded/"

    var accountId: Int = 0

    // 不在使用
    // var accountStatus: Int = 0
    // var accountStatusStr:String = ""

    // 改用用户存储用户数据
    var account = Account()

    // main fragment id
    var mainFragmentId:Int = 0

}
//http://192.168.122.1:8080/SelectSubject2_war_exploded/
