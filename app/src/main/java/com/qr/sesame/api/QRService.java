package com.qr.sesame.api;

import com.qr.sesame.entiy.SuccessData;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangqi on 2017/4/17.
 */

public interface QRService {
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    @POST("QRCodeServlet")
    Observable<SuccessData> scan(@Query("method") String method, @Query("isPass") String isPass);

    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    @POST("RegistServlet")
    Observable<SuccessData> register(@Query("method") String method, @Query("idcard") String idcard, @Query("name") String name, @Query("password") String password);

    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    @POST("LoginServlet")
    Observable<SuccessData> login(@Query("idcard") String idcard, @Query("name") String name, @Query("password") String password);

}
