package com.select.utils

object IdNameTurn {

    fun accountStatusNumToStr(n: Int): String {
        return when (n) {
            0 -> "学生"
            1 -> "老师"
            else -> "学生"
        }
    }

    fun accountStatusStrToNum(str: String): Int {
        return when (str) {
            "学生" -> 0
            "老师" -> 1
            else -> 0
        }
    }

    fun accountSexNumToStr(n: Int): String {
        return when (n) {
            0 -> "女"
            1 -> "男"
            else -> "女"
        }
    }

    fun accountSexStrToNum(str: String): Int {
        return when (str) {
            "女" -> 0
            "男" -> 1
            else -> 0
        }
    }

    fun courseTypeNumToStr(n: Int): String {
        return when (n) {
            1 -> "选修课"
            2 -> "必修课"
            else -> ""
        }
    }

    fun courseTypeStrToNum(str: String): Int {
        return when (str) {
            "选修课" -> 1
            "必修课" -> 2
            else -> 0
        }
    }

    fun courseRequireTypeNumToStr(n: Int): String {
        return when (n) {
            1 -> "同院系"
            2 -> "同专业"
            3 -> "无限制"
            else -> ""
        }
    }

    fun courseRequireTypeStrToNum(str: String): Int {
        return when (str) {
            "同院系" -> 1
            "同专业" -> 2
            "无限制" -> 3
            else -> 0
        }
    }

    fun majorIdToName(id: Int): String {
        return when (id) {
            1 -> "电子科学与技术"
            2 -> "电子信息工程"
            3 -> "通信工程"
            else -> ""
        }
    }

    fun majorNameToId(name: String): Int {
        return when (name) {
            "电子科学与技术" -> 1
            "电子信息工程" -> 2
            "通信工程" -> 3
            else -> 0
        }
    }

    fun departmentIdToName(id: Int): String {
        if (id == 1) return "电子信息工程学院" else return ""
    }

    fun departmentNameToId(name: String): Int {
        if ("电子信息工程学院" == name)
            return 1
        else
            return 0
    }

}