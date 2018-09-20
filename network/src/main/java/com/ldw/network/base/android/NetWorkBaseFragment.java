package com.ldw.network.base.android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldw.network.base.mvp.presenter.Presenter;
import com.ldw.network.base.mvp.view.BaseView;


/**
 * @author LDW
 * @date 2018/6/13
 */

public abstract class NetWorkBaseFragment<P extends Presenter> extends Fragment implements BaseView {
    protected P presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResourceId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = initPresenter();
        initData();
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detach();//在presenter中解绑释放view
            presenter = null;
        }
        super.onDestroyView();
    }

    protected abstract int getLayoutResourceId();

    protected abstract void initData();

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract P initPresenter();



}
