package com.select.bean

data class Account(
    var id: Int = 0,
    var passport: String = "",
    var name: String = "",
    var password: String = "",
    var status: Int = 0,
    var email: String = "",
    var phone: String = "",
    var sex: Int = 0,
    var department_id: Int = 0,
    var major_id: Int = 0,
    var description: String = "",
    var last_login_time: Long = 0,
    var create_time: Long = 0,
    var update_time: Long = 0
)
