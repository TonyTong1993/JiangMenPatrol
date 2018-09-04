/**   
 * 文件名：QueryPopup.java   
 *   
 * 版本信息：   
 * 日期：2016年7月11日   
 * Copyright Ecity Corporation 2016    
 * 版权所有   
 *   
 */

package com.ecity.mobile.android.plugins.pipecross.ui;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ecity.mobile.android.crossanalysis.R;

/**
 * 此类描述的是：点击查询窗口
 * 
 * @author: wly
 * @version: 2016年7月11日 下午1:43:09
 */

public class QueryPopup {

    private Context context;
    private PipeCrossView csaview = null;
    private TextView queryResult, queryValue;
    private PopupWindow popupWindow;
    private View view;
    PipeItem pipeItem;

    public QueryPopup(Context context, PipeCrossView csaview, PipeItem pipeItem) {
        this.context = context;
        this.csaview = csaview;
        this.pipeItem = pipeItem;
        initFram();
    }

    /**
     * 
     * 此方法描述的是： 添加内容
     * 
     * @author: wly
     * @version: 2016年7月11日 下午3:42:08
     */
    public void initFram() {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        this.view = inflater.inflate(R.layout.query_result, null);
        queryResult = (TextView) view.findViewById(R.id.text_queryresult1);
        if (pipeItem == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("管道信息：\n");
        if (null != pipeItem) {
            JSONObject attribute = pipeItem.getAttributes();
            Iterator iterator = attribute.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value;
                try {
                    value = attribute.getString(key);
                    sb.append(key + ":\t" + value + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        queryResult.setText(sb.toString());
        popupWindow = new PopupWindow(view, (int) csaview.csaWidth, context
                .getResources().getDimensionPixelSize(
                        R.dimen.popup_layer_content_hight));
        // 点击popup外消失
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.color.transparent));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
    }

    /**
     * 
     * 此方法描述的是： 弹出popup
     * 
     * @author: wly
     * @version: 2016年7月11日 下午2:59:08
     */
    public void showPopup(View parent) {

        if (csaview == null || null == parent) {
            return;
        } else {
            popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER,
                    0, 0);
            Animation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 1);
            scaleAnimation.setDuration(300);
            view.startAnimation(scaleAnimation);
            popupWindow.update();
        }
    }

    /**
     * 
     * 此方法描述的是： popup消失
     * 
     * @author: wly
     * @version: 2016年7月11日 下午3:09:11
     */
    public void dismiss() {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            popupWindow = null;
        }
    }
}
