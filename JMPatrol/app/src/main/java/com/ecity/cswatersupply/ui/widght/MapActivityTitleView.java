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
public class MapActivityTitleView extends RelativeLayout {
    /**
     * the style of Title
     */
    public enum BtnStyle {
        NOBTN(0), RIGHT_ACTION(1), ONLY_BACK(2), ONLY_RIGHT_ACTION(3), ONLY_CONFIRM(4), WITH_SEARCH(5);

        BtnStyle(int style) {
            this.style = style;
        }

        final int style;
    }

    private ImageButton ibtn_ac_back;
    private TextView tv_ac_title;
    public ImageButton ibtn_ac_legend;
    private TextView btn_action_ok;
    private ImageButton btn_search;

    public MapActivityTitleView(Context context) {
        this(context, null);
    }

    public MapActivityTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_map_activity_titlebar, this, true);
        this.setBackgroundResource(R.color.blue_normal);

        initView();
    }

    private void initView() {
        ibtn_ac_back = (ImageButton) findViewById(R.id.ibtn_ac_back);
        btn_action_ok = (TextView) findViewById(R.id.btn_action_ok);
        ibtn_ac_legend = (ImageButton) findViewById(R.id.ibtn_ac_legend);
        tv_ac_title = (TextView) findViewById(R.id.tv_ac_title);
        btn_search = (ImageButton) findViewById(R.id.ibtn_query_address);
    }

    public void setTitleText(String str_title) {
        this.tv_ac_title.setText(str_title);
    }

    public void setTitleText(int resId) {
        this.tv_ac_title.setText(HostApplication.getApplication().getApplicationContext().getResources().getString(resId));
    }

    public void setLegendBackground(Drawable background) {
        this.ibtn_ac_legend.setBackground(background);
    }

    public void setOnBackListener(OnClickListener listener) {
        ibtn_ac_back.setOnClickListener(listener);
    }

    public void setOnLegendListener(OnClickListener listener) {
        ibtn_ac_legend.setOnClickListener(listener);
    }

    /**
     * 设置右侧按钮类型
     */
    public void setBtnStyle(BtnStyle style) {
        switch (style) {
            case RIGHT_ACTION:
                this.ibtn_ac_back.setVisibility(View.VISIBLE);
                this.ibtn_ac_legend.setVisibility(View.VISIBLE);
                this.btn_action_ok.setVisibility(View.GONE);
                break;
            case ONLY_RIGHT_ACTION:
                this.ibtn_ac_back.setVisibility(View.GONE);
                this.ibtn_ac_legend.setVisibility(View.VISIBLE);
                this.btn_action_ok.setVisibility(View.GONE);
                break;
            case NOBTN:
                this.ibtn_ac_back.setVisibility(View.GONE);
                this.ibtn_ac_legend.setVisibility(View.GONE);
                this.btn_action_ok.setVisibility(View.GONE);
                break;
            case ONLY_BACK:
                this.ibtn_ac_back.setVisibility(View.VISIBLE);
                this.ibtn_ac_legend.setVisibility(View.GONE);
                this.btn_action_ok.setVisibility(View.GONE);
                break;
            case ONLY_CONFIRM:
                this.ibtn_ac_back.setVisibility(View.VISIBLE);
                this.ibtn_ac_legend.setVisibility(View.GONE);
                this.btn_action_ok.setVisibility(View.VISIBLE);
                break;
            case WITH_SEARCH:
                this.ibtn_ac_back.setVisibility(View.VISIBLE);
                this.ibtn_ac_legend.setVisibility(View.VISIBLE);
                this.btn_action_ok.setVisibility(View.GONE);
                this.btn_search.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setRightActionBtnText(int ok) {
        // TODO Auto-generated method stub

    }

    public void setOnActionButtonClickedListener(OnClickListener listener) {
        if (btn_action_ok != null) {
            btn_action_ok.setOnClickListener(listener);
        }
    }

    public void setSearchBtnOnClickedListener(OnClickListener listener) {
        if (btn_search != null) {
            btn_search.setOnClickListener(listener);
        }
    }
}
