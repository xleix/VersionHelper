package com.bjnsc.versionlibrary.network;

import com.bjnsc.versionlibrary.API;
import com.bjnsc.versionlibrary.model.VersionCls;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;


public interface RetrofitService {

    @GET(API.getVersion)
    Observable<VersionCls> getVersion(@QueryMap Map<String, Object> paramters);
}
