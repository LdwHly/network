package com.ldw.network.base.mvp;

import java.io.IOException;

/**
 * Created by LDW on 2018/6/13.
 */

public class MyException extends IOException {
    final Object code;
    final Object msg;
    public MyException(Object code, Object msg) {
        this.code =  code;
        this.msg = msg;
    }

    public int getCode() {
        return (int) code;
    }

    public String getMsg() {
        return (String) msg;
    }

}
