package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.zzz.ecity.android.applibrary.view.CircleImageView;

/**
 * 自定义界面Title 包含左侧返回按钮和右侧自定义按钮
 * 
 * @author Administrator
 *
 */
public class CustomTitleView extends RelativeLayout {
    /**
     * the style of Title
     */
    public enum BtnStyle {
        NOBTN(0), RIGHT_ACTION(1), RIGHT_MORE(2), ONLY_BACK(3), ONLY_RIGHT_ACTION(4);
        BtnStyle(int style) {
            this.style = style;
        }

        final int style;
    }

    public TextView tv_rightSingle;
    public TextView tv_title;
    public NotificationImageView imgv_tag;
    private ImageButton ibtn_back;
    private CircleImageView civ_more;
    public LinearLayout ll_title;

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_common_titlebar, this, true);
        this.setBackgroundResource(R.color.blue_normal);

        initView();
    }

    private void initView() {
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        imgv_tag = (NotificationImageView) findViewById(R.id.iv_tag);
        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        tv_rightSingle = (TextView) findViewById(R.id.tv_action);
        tv_title = (TextView) findViewById(R.id.tv_title);
        civ_more = (CircleImageView) findViewById(R.id.btn_more);
    }

    public void setTitleText(String str_title) {
        this.tv_title.setText(str_title);
    }

    public void setTitleText(int resId) {
        this.tv_title.setText(HostApplication.getApplication().getApplicationContext().getResources().getString(resId));
    }

    /**
     * 设置右侧动作按钮显示文字
     */
    public void setRightActionBtnText(String str_rightBtn) {
        this.tv_rightSingle.setText(str_rightBtn);
    }

    public void setRightActionBtnText(int resId) {
        this.tv_rightSingle.setText(HostApplication.getApplication().getApplicationContext().getResources().getString(resId));
    }

    /**
     * 设置右侧按钮类型
     */
    public void setBtnStyle(BtnStyle style) {
        switch (style) {
            case RIGHT_ACTION:
                this.ibtn_back.setVisibility(View.VISIBLE);
                this.civ_more.setVisibility(View.GONE);
                this.tv_rightSingle.setVisibility(View.VISIBLE);
                break;
            case ONLY_RIGHT_ACTION:
                this.ibtn_back.setVisibility(View.GONE);
                this.civ_more.setVisibility(View.GONE);
                this.tv_rightSingle.setVisibility(View.VISIBLE);
                break;
            case RIGHT_MORE:
                this.ibtn_back.setVisibility(View.VISIBLE);
                this.tv_rightSingle.setVisibility(View.GONE);
                this.civ_more.setVisibility(View.VISIBLE);
                break;
            case NOBTN:
                this.ibtn_back.setVisibility(View.GONE);
                this.tv_rightSingle.setVisibility(View.GONE);
                this.civ_more.setVisibility(View.GONE);
                break;
            case ONLY_BACK:
                this.ibtn_back.setVisibility(View.VISIBLE);
                this.tv_rightSingle.setVisibility(View.GONE);
                this.civ_more.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 设置右侧按钮监听
     */
    public void setMessageDeleteListener(OnClickListener listener) {
        this.tv_rightSingle.setOnClickListener(listener);
    }
}
