package com.bjnsc.versionlibrary.network;


import com.bjnsc.versionlibrary.model.VersionCls;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;


public class RetrofitHelper extends BaseNet {

    private volatile static RetrofitHelper instance;

    private RetrofitHelper() {
    }

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            synchronized (RetrofitHelper.class) {
                if (instance == null) {
                    instance = new RetrofitHelper();
                }
            }
        }
        return instance;
    }

    public void getVersion(Map<String, Object> paramers, Observer<VersionCls> observer) {
        Observable<VersionCls> observable = retrofitService.getVersion(paramers);
        toSubscribe(observable, observer);
    }
}
