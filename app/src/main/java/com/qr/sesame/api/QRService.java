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
    Observable<SuccessData> scan(@Query("isPass") String isPass);
}
