package com.zzz.ecity.android.applibrary.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzz.ecity.android.applibrary.R;

/**
 * 自定义界面Title 包含左侧返回按钮和右侧自定义按钮
 *
 * @author Administrator
 */
public class CustomTitleView extends RelativeLayout {
    /**
     * the style of Title
     */
    public enum BtnStyle {
        /**
         *
         */
        NOBTN(0), RIGHT_ACTION(1), RIGHT_MORE(2), ONLY_BACK(3), ONLY_RIGHT_ACTION(4), RIGHT_HORIZONTAL_MORE(5),RIGHT_SEARCH(6);

        BtnStyle(int style) {
            this.style = style;
        }

        final int style;
    }

    public TextView tvRightSingle;
    public TextView tvTitle;
    public NotificationImageView imgvTag;
    private ImageButton ibtnBack;
    private CircleImageView circleImageView;
    public LinearLayout llTitle;
    public ImageButton ivHorizontalMore;
    public ImageButton ibtnSearchMore;
    public ImageButton ivSearch;

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
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        imgvTag = (NotificationImageView) findViewById(R.id.iv_tag);
        ibtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        tvRightSingle = (TextView) findViewById(R.id.tv_action);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        circleImageView = (CircleImageView) findViewById(R.id.btn_more);
        ivHorizontalMore = findViewById(R.id.ibtn_more);
        ivSearch = findViewById(R.id.ibtn_search);
        ibtnSearchMore = findViewById(R.id.ibtn_search_more);
    }

    public void setTitleText(String strTitle) {
        this.tvTitle.setText(strTitle);
    }

    public void setTitleText(int resId) {
        this.tvTitle.setText(resId);
    }

    /**
     * 设置右侧动作按钮显示文字
     */
    public void setRightActionBtnText(String strRightBtn) {
        this.tvRightSingle.setText(strRightBtn);
    }

    public void setRightActionBtnText(int resId) {
        this.tvRightSingle.setText(resId);
    }

    public void setRightActionBtnBtnBg(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.tvRightSingle.setBackground(drawable);
        }else {
            this.tvRightSingle.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 设置右侧按钮类型
     */
    public void setBtnStyle(BtnStyle style) {
        switch (style) {
            case RIGHT_ACTION:
                this.ibtnBack.setVisibility(View.VISIBLE);
                this.circleImageView.setVisibility(View.GONE);
                this.tvRightSingle.setVisibility(View.VISIBLE);
                this.ivHorizontalMore.setVisibility(View.GONE);
                this.ivSearch.setVisibility(View.GONE);
                this.ibtnSearchMore.setVisibility(View.GONE);
                break;
            case ONLY_RIGHT_ACTION:
                this.ibtnBack.setVisibility(View.GONE);
                this.circleImageView.setVisibility(View.GONE);
                this.tvRightSingle.setVisibility(View.VISIBLE);
                this.ivHorizontalMore.setVisibility(View.GONE);
                this.ivSearch.setVisibility(View.GONE);
                this.ibtnSearchMore.setVisibility(View.GONE);
                break;
            case RIGHT_MORE:
                this.ibtnBack.setVisibility(View.VISIBLE);
                this.tvRightSingle.setVisibility(View.GONE);
                this.circleImageView.setVisibility(View.VISIBLE);
                this.ivHorizontalMore.setVisibility(View.GONE);
                this.ivSearch.setVisibility(View.GONE);
                this.ibtnSearchMore.setVisibility(View.GONE);
                break;
            case NOBTN:
                this.ibtnBack.setVisibility(View.GONE);
                this.tvRightSingle.setVisibility(View.GONE);
                this.circleImageView.setVisibility(View.GONE);
                this.ivHorizontalMore.setVisibility(View.GONE);
                this.ivSearch.setVisibility(View.GONE);
                this.ibtnSearchMore.setVisibility(View.GONE);
                break;
            case ONLY_BACK:
                this.ibtnBack.setVisibility(View.VISIBLE);
                this.tvRightSingle.setVisibility(View.GONE);
                this.circleImageView.setVisibility(View.GONE);
                this.ivHorizontalMore.setVisibility(View.GONE);
                this.ivSearch.setVisibility(View.GONE);
                this.ibtnSearchMore.setVisibility(View.GONE);
                break;
            case RIGHT_HORIZONTAL_MORE:
                this.ibtnBack.setVisibility(View.VISIBLE);
                this.tvRightSingle.setVisibility(View.GONE);
                this.circleImageView.setVisibility(View.GONE);
                this.ivHorizontalMore.setVisibility(View.VISIBLE);
                this.ivSearch.setVisibility(View.GONE);
                this.ibtnSearchMore.setVisibility(View.GONE);
                break;
            case RIGHT_SEARCH:
                this.ibtnBack.setVisibility(View.VISIBLE);
                this.tvRightSingle.setVisibility(View.GONE);
                this.circleImageView.setVisibility(View.GONE);
                this.ivHorizontalMore.setVisibility(View.GONE);
                this.ivSearch.setVisibility(View.VISIBLE);
                this.ibtnSearchMore.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }

    /**
     * 设置右侧按钮监听
     */
    public void setMessageDeleteListener(OnClickListener listener) {
        this.tvRightSingle.setOnClickListener(listener);
    }
}
