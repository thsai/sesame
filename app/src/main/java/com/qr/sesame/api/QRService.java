package com.qr.sesame.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by wangqi on 2017/4/17.
 */

public interface QRService {
    @GET
    Observable scan(@Query("isPass") String isPass);
}
