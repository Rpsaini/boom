package com.web.bloomex.apimodel;


import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiModel {

    @Multipart
    @POST("app-signin")
    Observable<Response<Object>> loginApi(
            @Part("email") RequestBody token,
            @Part("password") RequestBody password,
            @Part("Version") RequestBody device,
            @Part("platform") RequestBody platform,
            @Part("device_token") RequestBody device_type,
            @Header("x-api-key") String apikey);




    @Multipart
    @POST("app-dashboard")
    Observable<Response<Object>> getDashboardData(
            @Part("token") RequestBody token,
            @Part("version") RequestBody device,
            @Part("platform") RequestBody platform,
            @Part("device_token") RequestBody device_type,
            @Header("x-api-key") String apikey);





    @Multipart
    @POST("app-signup")
    Observable<Response<Object>> signUp(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("conf_password") RequestBody conf_password,
            @Part("phone") RequestBody mobile,
            @Part("phone_code") RequestBody phone,
            @Part("sponsor") RequestBody sponsor,
            @Part("username") RequestBody username,
            @Part("version") RequestBody version,
            @Part("platform") RequestBody platform,
            @Part("device_token") RequestBody device_token,
            @Header("x-api-key") String apikey);

}


