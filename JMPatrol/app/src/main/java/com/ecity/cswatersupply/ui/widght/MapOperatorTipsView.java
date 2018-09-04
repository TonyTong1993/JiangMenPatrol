package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

public class MapOperatorTipsView extends FrameLayout {
    private LayoutInflater mInflater;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private ImageButton mBtnClean;

    public MapOperatorTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        //this.setBackgroundResource(R.color.transparent_fill);
        initView();
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.view_map_operator_tips, this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_map);
        mTextView = (TextView) view.findViewById(R.id.tv_tips);
        mBtnClean = (ImageButton) view.findViewById(R.id.btn_clean);
    }

    public void setTipsText(int resId) {
        if (null != mTextView) {
            mTextView.setText(resId);
        }
    }

    public void setTipsText(String str) {
        if (null != mTextView) {
            mTextView.setText(str);
        }
    }

    public void disPlayProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void disMissProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void registerCleanListener(OnClickListener onClickListener) {
        mBtnClean.setOnClickListener(onClickListener);
    }

    public void displayButtonClean() {
        if (null != mBtnClean) {
            mBtnClean.setVisibility(View.VISIBLE);
        }
    }

    public void dismissButtonClean() {
        if (null != mBtnClean) {
            mBtnClean.setVisibility(View.GONE);
        }
    }
}
