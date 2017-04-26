package com.qr.sesame.api;

import com.qr.sesame.entiy.SuccessData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangqi on 2017/4/17.
 */

public interface QRService {
    @GET("QRCodeServlet")
    Observable<SuccessData> scan(@Query("method") String method,@Query("isPass") String isPass);

    @GET("RegistServlet")
    Observable<SuccessData> register(@Query("idcard") String idcard,@Query("name") String name,@Query("password") String password);

}
