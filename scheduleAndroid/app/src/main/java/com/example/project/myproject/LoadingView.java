package com.example.project.myproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

/**
 * Created by wangshuo on 2018/12/28.
 */

public class LoadingView extends RelativeLayout {
    private RelativeLayout mRelativeLayoutLoading, mRelativeLayoutFailed;

    private LoadingView.LoadingViewListener mListener;

    public void setListener(LoadingViewListener listener) {
        this.mListener = listener;
    }

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_loading, this);
        mRelativeLayoutLoading = view.findViewById(R.id.rl_progress_bar);
        mRelativeLayoutFailed = view.findViewById(R.id.rl_tv_failed);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        mRelativeLayoutFailed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 加载失败，点击重试
                mRelativeLayoutLoading.setVisibility(View.VISIBLE);
                mRelativeLayoutFailed.setVisibility(View.GONE);
                mListener.onFailedClickListener();
            }
        });
    }

    /**
     * 显示加载过程中的状态
     */
    public void showLoading() {
        mRelativeLayoutLoading.setVisibility(View.VISIBLE);
        mRelativeLayoutFailed.setVisibility(View.GONE);
    }

    /**
     * 显示加载完成的状态
     */
    public void showContentView() {
        mRelativeLayoutLoading.setVisibility(View.GONE);
        mRelativeLayoutFailed.setVisibility(View.GONE);
    }

    /**
     * 显示加载失败的状态
     */
    public void showFailed() {
        mRelativeLayoutLoading.setVisibility(View.GONE);
        mRelativeLayoutFailed.setVisibility(View.VISIBLE);
    }

    public interface LoadingViewListener {
        void onFailedClickListener();
    }
}
