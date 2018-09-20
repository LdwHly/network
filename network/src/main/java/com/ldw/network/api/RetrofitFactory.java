package com.ldw.network.api;


import android.text.TextUtils;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author LDW
 * @date 2018/12/12
 */

public class RetrofitFactory {
    //访问超时
    private static final long TIMEOUT = 20;
    private static final String TAG = RetrofitFactory.class.getSimpleName();
    private static Retrofit retrofit;

    //获得RetrofitService对象
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            throw new RuntimeException("retrofit must be init");
        }
        return retrofit;
    }

    public static void init(String HTTP_BASE, OkHttpClient httpClient ,DataSourse mDataSourse) {
        if (TextUtils.isEmpty(HTTP_BASE)) {
            throw new RuntimeException("error base url");
        }
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    //打印接口信息，方便接口调试
                    .addInterceptor(new Interceptor() {
                                        @Override
                                        public Response intercept(Chain chain) throws IOException {
                                            Request request = chain.request();
                                            HttpUrl.Builder builder = request.url().newBuilder();
                                            HttpUrl httpUrl = builder.build();
                                            request = request.newBuilder()
                                                    .method(request.method(), request.body())
                                                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").url(httpUrl).build();
                                            Response response;
                                            try {
                                                response = chain.proceed(request);
                                            } catch (Exception e) {
                                                throw e;
                                            }
                                            return response;
                                        }
                                    }
                    )
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(HTTP_BASE)
                    .addConverterFactory(MyGsonConverterFactory.create(mDataSourse))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build();
        }
    }
}
