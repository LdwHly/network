package com.ldw.network.base.mvp.view;


/**
 * @author LDW
 * @date 2018/6/13
 */

public interface BaseView {
    /**
     * 显示dialog
     *
     * @param msg
     */

    void showLoadingDialog(String msg);


    /**
     * 取消dialog
     */
    void dismissLoadingDialog();

    void showErrorMsg(int code, String msg);

}
