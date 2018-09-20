package com.ldw.network.base.mvp.presenter;


import com.ldw.network.api.RetrofitFactory;
import com.ldw.network.base.mvp.MyException;
import com.ldw.network.base.mvp.SuccessException;
import com.ldw.network.base.mvp.view.BaseView;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;

/**
 * @author LDW
 * @date 2018/6/13
 */

public abstract class BasePresenterServiceImpl<E, V extends BaseView> implements Presenter {

    public String TAG;
    protected E retrofitService;
    private Retrofit mRetrofit;
    protected String textShowIng = "加载中。。。";
    protected String textLoadFail = "请求失败";


    public BasePresenterServiceImpl(V view) {
        this.view = view;
        TAG = this.getClass().getSimpleName();
        mRetrofit = RetrofitFactory.getRetrofitInstance();
        retrofitService = mRetrofit.create(getServiceClass());
    }

    protected abstract Class<E> getServiceClass();

    /**
     * //给子类使用view
     */
    protected V view;


    @Override
    public void detach() {
        this.view = null;
        unDisposable();
    }

    //将所有正在处理的Subscription都添加到CompositeSubscription中。统一退出的时候注销观察
    private CompositeDisposable mCompositeDisposable;

    /**
     * 将Disposable添加
     *
     * @param subscription
     */
    @Override
    public void addDisposable(Disposable subscription) {
        //csb 如果解绑了的话添加 sb 需要新的实例否则绑定时无效的
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    /**
     * 在界面退出等需要解绑观察者的情况下调用此方法统一解绑，防止Rx造成的内存泄漏
     */
    @Override
    public void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    private RequestBody toRequestBody(String json) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
    }


    /**
     * 获取数据
     *
     * @param observablek
     * @param listener
     * @param <T>
     */
    protected <T> void startGetData1(Observable<T> observablek, final Listener<T> listener) {
        observablek.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //将这个请求的Disposable添加进入CompositeDisposable同一管理（在封装的presenter中）
                        addDisposable(disposable);
                        view.showLoadingDialog(textShowIng);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        view.dismissLoadingDialog();
                        listener.onResponse(t);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.dismissLoadingDialog();
                        listener.onErrorResponse(0, textLoadFail);
                    }
                });
    }

    /**
     * 获取外层数据，获取数据
     *
     * @param observablek
     * @param listener
     * @param <T>
     */
    protected <T> void startGetDataOriginal(Observable<T> observablek, final Listener<T> listener) {

        observablek.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //将这个请求的Disposable添加进入CompositeDisposable同一管理（在封装的presenter中）
                        addDisposable(disposable);
                        view.showLoadingDialog(textShowIng);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        view.dismissLoadingDialog();
                        listener.onResponse(t);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.dismissLoadingDialog();
                        if (throwable instanceof MyException) {
                            MyException mMyException = (MyException) throwable;
                            view.showErrorMsg(mMyException.getCode(), mMyException.getMsg());
                            listener.onErrorResponse(mMyException.getCode(), mMyException.getMsg());
                        } else if (throwable instanceof SuccessException) {
                            listener.onResponse(null);
                        }
                        if (throwable instanceof HttpException || throwable instanceof ConnectException || throwable instanceof SocketTimeoutException) {
                        }
                    }
                });
    }


}
