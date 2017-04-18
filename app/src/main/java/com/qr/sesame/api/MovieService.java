package com.qr.sesame.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangqi on 2017/4/18.
 */

public interface MovieService {
    @GET("top250")
    Observable getTopMovie(@Query("start") int start, @Query("count") int count);
}
