package com.combofish.selectsubject.bean

data class Course(
    var id: Int = 0,
    var name: String = "",
    var credit: Int = 0,
    var class_time: Int = 0,
    var teach_time: String = "",
    var classroom: String = "",
    var account_id: Int = 0,
    var account_name: String = "",
    var department_id: Int = 0,
    var major_id: Int = 0,
    var type: Int = 0,
    var require_type: Int = 0,
    var available_amount: Int = 0,
    var introduction: String = "",
    var create_time: Long = 0,
    var update_time: Long = 0
)
