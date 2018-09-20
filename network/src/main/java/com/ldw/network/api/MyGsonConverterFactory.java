package com.ldw.network.api;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by LDW on 2018/7/16.
 */

public class MyGsonConverterFactory extends Converter.Factory {


    public static MyGsonConverterFactory create(DataSourse mDataSourse) {
        return create(new Gson(), mDataSourse);
    }

    public static MyGsonConverterFactory create(Gson gson, DataSourse mDataSourse) {
        if (gson == null) throw new NullPointerException("gson == null");
        if (mDataSourse == null) throw new NullPointerException("mDataSourse == null");

        return new MyGsonConverterFactory(gson, mDataSourse);
    }

    private final Gson gson;
    private final DataSourse mDataSourse;

    private MyGsonConverterFactory(Gson gson, DataSourse mDataSourse) {
        this.gson = gson;
        this.mDataSourse = mDataSourse;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new MyGsonResponseBodyConverter<>(gson, type,mDataSourse);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MyGsonRequestBodyConverter<>(gson, adapter);
    }
}
