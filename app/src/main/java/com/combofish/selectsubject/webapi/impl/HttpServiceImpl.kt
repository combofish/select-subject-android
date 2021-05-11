package com.combofish.selectsubject.webapi.impl

import com.combofish.selectsubject.data.DataGlobal
import com.combofish.selectsubject.webapi.HttpService
import retrofit2.Retrofit

object HttpServiceImpl {
    val url = DataGlobal.url
    val retrofit = Retrofit.Builder().baseUrl(url).build()
    val httpService = retrofit.create(HttpService::class.java)
}