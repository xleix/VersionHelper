package com.bjnsc.versionlibrary.network;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class BaseNet extends BaseRetrofit {

    private static final String TAG = "BaseModel";

    protected RetrofitService retrofitService;

    protected Map<String, String> mParams = new HashMap<>();

    public BaseNet() {
        super();
        retrofitService = mRetrofit.create(RetrofitService.class);
    }

    @Override
    protected Map<String, String> getCommonMap() {
        Map<String, String> commonMap = new HashMap<>();
//        commonMap.put("username","");
//        commonMap.put("password", "");
        return commonMap;
    }

    protected void addParams(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "the key is null");
            return;
        }
        mParams.put(key, value);
    }

    protected void addParams(Map<String, String> params) {
        if (null == params) {
            Log.e(TAG, "the map is null");
            return;
        }
        mParams.putAll(params);
    }
}
