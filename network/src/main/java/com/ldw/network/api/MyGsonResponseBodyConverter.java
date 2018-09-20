package com.ldw.network.api;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ldw.network.base.mvp.MyException;
import com.ldw.network.base.mvp.SuccessException;


import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by LDW on 2018/7/16.
 */

public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Gson gson;
    private Type type;
    private DataSourse mDataSourse;

    public MyGsonResponseBodyConverter(Gson gson, Type type, DataSourse mDataSourse) {
        this.gson = gson;
        this.type = type;
        this.mDataSourse = mDataSourse;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = mDataSourse.getSourse(value.string());
        JSONObject obj = JSON.parseObject(response);
        int code = (int) obj.get(mDataSourse.getKeyCode());
        if (code == mDataSourse.isSucc()) {
            if (obj.get(mDataSourse.getKeyData()) == null || TextUtils.isEmpty(obj.get(mDataSourse.getKeyData()).toString())) {
                throw new SuccessException(response);
            }
            T t = null;
            t = gson.fromJson(obj.get(mDataSourse.getKeyData()).toString(), type);
            value.close();
            return t;
        } else {
            throw new MyException(obj.get(mDataSourse.getKeyCode()), obj.get(mDataSourse.getKeyMsg()));
        }
    }

}
