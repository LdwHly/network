package com.ldw.network.base.mvp.presenter;

import io.reactivex.disposables.Disposable;

/**
 * Created by LDW on 2018/6/13.
 */

public interface Presenter {
    /***
     * 自定义状态码，表示直接弹toast
     */
    int code = 0;

    //默认初始化
    void initData();

    //Activity关闭把view对象置为空
    void detach();

    //将网络请求的每一个disposable添加进入CompositeDisposable，再退出时候一并注销
    void addDisposable(Disposable subscription);

    //注销所有请求
    void unDisposable();

    interface Listener<T> {
        void onResponse(T t);

        void onErrorResponse(int code, String msg);
    }

}
