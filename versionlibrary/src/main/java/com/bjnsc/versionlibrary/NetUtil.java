package com.bjnsc.versionlibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by lx123 on 2018/1/23/023.
 */

public class NetUtil {
    /**
     * 是否使用网络
     */
    public static boolean isNetworkConnected(Context mContext) {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断当前的网络连接状态是否能用
     * return 0可用   其他值不可用
     */
    public static boolean Ping() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec("ping -c 3 www.baidu.com");
            int ret = p.waitFor();
            if (ret == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 当前网络状态：0，无网络；1，Wifi网络；2，移动网络
     *
     * @return 0
     */
    public static int getNetWorkType(Context mContext) {
        ConnectivityManager mConnectivity = (ConnectivityManager) mContext.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) mContext
                .getSystemService(TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();

        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return 0;
        }

        int netType = info.getType();
        int netSubtype = info.getSubtype();
        int ret = 0;
        if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
            ret = 1;
        } else if (netType == ConnectivityManager.TYPE_MOBILE
//                && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
                && !mTelephony.isNetworkRoaming()) {   //MOBILE
            ret = 2;
        } else {
            ret = 0;
        }
        return ret;
    }

    public static void get(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder();
//        FormBody.Builder builder = new FormBody.Builder();
//        FormBody body = builder.build();
        builder.connectTimeout(20, TimeUnit.SECONDS);
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
