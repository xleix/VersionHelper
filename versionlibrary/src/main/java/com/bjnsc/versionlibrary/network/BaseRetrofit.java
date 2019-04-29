package com.bjnsc.versionlibrary.network;


import com.bjnsc.versionlibrary.API;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leixiang on 2017/9/1/001.
 */

public abstract class BaseRetrofit {

    protected Retrofit mRetrofit;
    private static final int DEFAULT_TIME = 30;    //默认超时时间
    private final long RETRY_TIMES = 0;   //重订阅次数

    public BaseRetrofit() {
        if (null == mRetrofit) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

            builder.readTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
            builder.connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS);

            builder.addInterceptor(new BasicParamsInterceptor.Builder().addParamsMap(getCommonMap()).build());
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(API.VersionHost)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    //公共参数
    protected abstract Map<String, String> getCommonMap();

    protected <T> void toSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())    // 指定subscribe()发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())  // 指定Subscriber的回调发生在io线程
                .timeout(DEFAULT_TIME, TimeUnit.SECONDS)    //重连间隔时间
                .retry(RETRY_TIMES)
                .subscribe(observer);   //订阅
    }

    //retrofit+rxjava请求的正常流程
    //    public BaseRetrofit() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Ins.STS_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//        RetrofitService service = retrofit.create(RetrofitService.class);
//        Call<ResidentBean> data = service.getResidentData("1", "no");
//        data.enqueue(new Callback<ResidentBean>() {
//            @Override
//            public void onResponse(Call<ResidentBean> call, Response<ResidentBean> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ResidentBean> call, Throwable t) {
//
//            }
//        });
//
//        Observable<ResidentBean> observable = service.getResident("1", "yes");
//        observable.subscribeOn(Schedulers.io())
//                  .observeOn(AndroidSchedulers.mainThread())
//                  .subscribe(new Observer<ResidentBean>() {
//                      @Override
//                      public void onSubscribe(@NonNull Disposable d) {
//
//                      }
//
//                      @Override
//                      public void onNext(@NonNull ResidentBean bean) {
//
//                      }
//
//                      @Override
//                      public void onError(@NonNull Throwable e) {
//
//                      }
//
//                      @Override
//                      public void onComplete() {
//
//                      }
//                  });

}
