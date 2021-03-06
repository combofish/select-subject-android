package com.combofish.selectsubject.webapi

import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HttpService {
    @POST("LoginServlet")
    @FormUrlEncoded
    fun login(
        @Field("passport") passport: String?,
        @Field("password") password: String?
    ): Call<ResponseBody?>?

    @get:GET("GetAllCoursesServlet")
    val allCoursesServlet: Call<ResponseBody?>?

    @POST("SelectCourseServlet")
    @FormUrlEncoded
    fun selectCourse(
        @Field("account") account: String?,
        @Field("course") course: String?
    ): Call<ResponseBody?>?


    @GET("GetCourseByCourseIdServlet")
    fun getCourseByCourseId(@Query("course") course: Int?): Call<ResponseBody?>?

    @GET("GetAccountByAccountIdServlet")
    fun getAccountByAccountId(@Query("account") account: Int?): Call<ResponseBody?>?

    @POST("ChangePasswordByAccountIdServlet")
    @FormUrlEncoded
    fun changePasswordByAccountId(
        @Field("password") password: String?,
        @Field("account") account: Int?,
        @Field("token") token: String?
    ): Call<ResponseBody?>?

    @GET("GetSelectedCoursesByAccountIdServlet")
    fun getSelectedCoursesByAccountId(@Query("account") account: Int?): Call<ResponseBody?>?

    @GET("GetCoursesByAccountIdServlet")
    fun getCoursesByAccountId(@Query("account") account: Int?): Call<ResponseBody?>?

    @POST("CancelSelectCourseServlet")
    @FormUrlEncoded
    fun cancelSelectCourse(
        @Field("account") account: String?,
        @Field("course") course: String?
    ): Call<ResponseBody?>?


    /**
    // https://www.httpbin.org/post/xxx
    @POST("post")
    @FormUrlEncoded
    fun post(@Field("username") username: String?, @Field("password") password: String?): Call<ResponseBody?>?

    @GET("get")
    operator fun get(@Query("username") username: String?, @Query("password") password: String?, @QueryMap map: Map<String?, String?>?): Call<ResponseBody?>?

    @HTTP(method = "GET")
    fun http(@Query("username") username: String?, @Query("password") password: String?): Call<ResponseBody?>?

    @POST("post")
    fun postBody(@Body body: FormBody?): Call<ResponseBody?>?

    @POST("{id}")
    fun postPath(@Path("id") path: String?): Call<ResponseBody?>?

    @POST("{id}")
    @FormUrlEncoded
    fun postPath(@Path("id") path: String?, @Field("username") username: String?, @Field("password") password: String?): Call<ResponseBody?>?

    @Headers("os:android", "version:1.0")
    @POST("post")
    fun postWithHeader(): Call<ResponseBody?>?

    @POST
    fun postUrl(@Url url: String?): Call<ResponseBody?>?
     */
}