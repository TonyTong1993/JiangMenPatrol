package com.ecity.cswatersupply.contact.widght;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.R;

import java.util.LinkedList;


/**
 * 显示路径的View，支持返回上一级，支持点击某个位置回到指定层级。
 */
public class PathTextView extends LinearLayout {
    private TextView mTextView;
    private HorizontalScrollView hsView;
    private OnItemClickListener mListener;
    //保存每一个路径的id和名称
    private LinkedList<PathItem> pathItemList;

    //分隔符
    private static final String DIV_STR = " > ";

    public PathTextView(Context context) {
        super(context);
    }

    public PathTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View root = LayoutInflater.from(context).inflate(R.layout.view_hvs, this, true);
        hsView = (HorizontalScrollView) root.findViewById(R.id.path_hs);
        mTextView = (TextView) root.findViewById(R.id.path_tv);
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mTextView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        pathItemList = new LinkedList<>();
    }


    /**
     * 初始化根路径名称。
     * textColor
     */
    public void initRoot(String text, int textColor) {
        mTextView.append(createSpannableString(-1, textColor, text));
        pathItemList.addLast(new PathItem(-1, text));
    }

    /**
     * 继续拼接一个路径。
     */
    public void append(long id, int textColor, String text) {
        mTextView.append(DIV_STR);
        mTextView.append(createSpannableString(id, textColor, text));
        pathItemList.addLast(new PathItem(id, text));
        //HorizontalScrollView滑动到最右边
        hsView.postDelayed(new Runnable() {
            @Override
            public void run() {
                hsView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100);
    }

    /**
     * 返回父级路径，一般用户点击“返回”时调用。
     */
    public void backParent() {
        int lastItemLength = pathItemList.removeLast().text.length();
        CharSequence oldCs = mTextView.getText();
        mTextView.setText(oldCs.subSequence(0, oldCs.length() - lastItemLength - DIV_STR.length()));
    }

    /**
     * 清除路径
     * textColor
     */
    public void clearRoot() {
        mTextView.setText("");
        pathItemList.clear();
    }

    private SpannableString createSpannableString(long id, int textColor, String text) {
        if(StringUtil.isBlank(text)) {
            return new SpannableString("");
        }
        SpannableString spStr = new SpannableString(text);
        ClickableSpan clickSpan = new MyClickableSpan(id);
        spStr.setSpan(clickSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spStr.setSpan(new ForegroundColorSpan(textColor), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spStr;
    }

    private class MyClickableSpan extends ClickableSpan {
        private long id;

        MyClickableSpan(long id) {
            super();
            this.id = id;
        }

        @Override
        public void onClick(View widget) {
            //更新当前路径
            int backCount = 0;
            while (pathItemList.getLast().id != id) {
                backParent();
                backCount++;
            }
            //回调
            if (mListener != null && backCount > 0) {
                mListener.onClick(id, backCount);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    private class PathItem {
        private long id;
        private String text;

        private PathItem(long id, String text) {
            this.id = id;
            this.text = text;
        }
    }

    public interface OnItemClickListener {
        /**
         * @param currentId 返回后目录的id.
         * @param backCount 返回层级的数量.
         */
        void onClick(long currentId, int backCount);
    }

    /**
     * 设置点击某个中间路径时的回调。
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
