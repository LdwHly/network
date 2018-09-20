package com.ldw.network.base.android;


import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldw.network.R;
import com.ldw.network.base.mvp.presenter.Presenter;
import com.ldw.network.base.mvp.view.BaseView;


/**
 * @author LDW
 * @date 2018/6/12
 */

public abstract class NetWorkBaseActivity<P extends Presenter> extends FragmentActivity implements BaseView {

    protected ImageView ivBack, ivRightTitle;
    protected TextView tvTitle;
    protected TextView tvRightTitle;
    protected LinearLayout llRootLayout;
    FrameLayout flContent;
    protected LinearLayout rlTitle;
    protected P presenter;

    protected View vwTitleLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvRightTitle = findViewById(R.id.tv_right_title);
        llRootLayout = findViewById(R.id.ll_root_layout);
        flContent = findViewById(R.id.fl_content);
        rlTitle = findViewById(R.id.rl_title);
        vwTitleLine = findViewById(R.id.vw_title_line);
        ivRightTitle = findViewById(R.id.iv_right_title);
        presenter = initPresenter();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract P initPresenter();


    @Override
    public void setContentView(int layoutResID) {
        flContent.removeAllViews();
        LayoutInflater.from(this).inflate(layoutResID, flContent);
        initView();
        initData();
    }

    private void initView() {
        rlTitle.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detach();//在presenter中解绑释放view
            presenter = null;
        }
        super.onDestroy();
    }


    @Override
    public void setContentView(View view) {
        flContent.removeAllViews();
        flContent.addView(view);
        initView();
        initData();
    }

    public void setTvTitleString(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
            rlTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setTvTitleString(int title) {
        if (tvTitle != null) {
            tvTitle.setText(getResources().getText(title));
            rlTitle.setVisibility(View.VISIBLE);
        }
    }

    public abstract void initData();


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
