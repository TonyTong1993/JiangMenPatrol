package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;

/**
 * 自定义界面Title 包含左侧返回按钮和右侧自定义按钮
 * 
 * @author Administrator
 *
 */
public class MapFragmentTitleView extends RelativeLayout {

    private ImageButton ibtn_title_back;
    private TextView tv_title;
    private ImageButton ibtn_query_address, ibtn_map_operator;

    public MapFragmentTitleView(Context context) {
        this(context, null);
    }

    public MapFragmentTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_map_fragment_titlebar, this, true);
        this.setBackgroundResource(R.color.blue_normal);

        initView();
    }

    private void initView() {
        ibtn_title_back = (ImageButton) findViewById(R.id.ibtn_back);
        ibtn_query_address = (ImageButton) findViewById(R.id.ibtn_query_address);
        ibtn_map_operator = (ImageButton) findViewById(R.id.ibtn_map_operator);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    public void setTitleText(String str_title) {
        this.tv_title.setText(str_title);
    }

    public void setTitleText(int resId) {
        this.tv_title.setText(HostApplication.getApplication().getApplicationContext().getResources().getString(resId));
    }

    public void setBackBtnVisible() {
        this.ibtn_title_back.setVisibility(View.VISIBLE);
    }

    public void setBackBtnGone() {
        this.ibtn_title_back.setVisibility(View.GONE);
    }

    public void setOperatorBtnVisible() {
        this.ibtn_map_operator.setVisibility(View.VISIBLE);
    }

    public void setOperatorBtnGone() {
        this.ibtn_map_operator.setVisibility(View.GONE);
    }

    public void setQueryBtnVisible() {
        this.ibtn_query_address.setVisibility(View.VISIBLE);
    }

    public void setQueryBtnGone() {
        this.ibtn_query_address.setVisibility(View.GONE);
    }

    public void setOperatorBtnBackground(Drawable background) {
        this.ibtn_map_operator.setBackground(background);
    }

    public void setOnBackListener(OnClickListener listener) {
        ibtn_title_back.setOnClickListener(listener);
    }

    public void setOnOperatorListener(OnClickListener listener) {
        ibtn_map_operator.setOnClickListener(listener);
    }

    public void setOnQueryAddressListener(OnClickListener listener) {
        ibtn_query_address.setOnClickListener(listener);
    }
}
