package com.ecity.mobile.android.crossanalysis;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Map;

/**
 * Show pipeline information queried.
 * @author GK
 *
 */
public class QueryPopup {
	private Context context;
	private CrossAnalysisResultView csaview = null;
	private TextView    queryresult;
	private PopupWindow popupWindow;
	private View        view ;
	private Lininfo     CrosssectionLininfo;
	/**
	 * 
	 * @param context 
	 * @param csaview the view of section analysis.
	 * @param CrosssectionLininfo the information of pipe which is clicked.
	 */
	public QueryPopup(Context context, CrossAnalysisResultView csaview, Lininfo CrosssectionLininfo){
		this.context = context;
		this.csaview = csaview; 
		this.CrosssectionLininfo = CrosssectionLininfo;
		initFram();
	}
	/**
	 * 	 
	 * @param csaview the view of section analysis.
	 * @param CrosssectionLininfo the information of pipe which is clicked.
	 */
	public QueryPopup(CrossAnalysisResultView csaview, Lininfo CrosssectionLininfo){
		this.csaview = csaview; 
		this.CrosssectionLininfo = CrosssectionLininfo;
		initFram();
	}
	
	public void dismiss() {
		if(popupWindow!=null)
		{
			if(popupWindow.isShowing()){
				popupWindow.dismiss();								
			}
			popupWindow = null;
		}
	}
	/**
	 * Init the popup, including the text.
	 */
	public void initFram(){
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		this.view = inflater.inflate(R.layout.queryresult, null);
		queryresult = (TextView)view.findViewById(R.id.text_queryresult1);	
		if(null == CrosssectionLininfo)
			return;
		StringBuilder sb = new StringBuilder();
		sb.append("管线信息:\n");
		for (Map.Entry<String, String> entry : CrosssectionLininfo.getLinAtt().entrySet()) {
			sb.append("    "+entry.getKey()+":"+entry.getValue()+"\n");
		}
		sb.append("管网信息:\n");
		for (Map.Entry<String, String> entry : CrosssectionLininfo.getNetAtt().entrySet()) {
			sb.append("    "+entry.getKey()+":"+entry.getValue()+"\n");
		}
		queryresult.setText(sb.toString());
		
		popupWindow = new PopupWindow(view,(int) csaview.CSAWidth ,
				context
				.getResources().getDimensionPixelSize(R.dimen.popup_layer_content_hight));	
		//为了让点击popup外，popup能顺利dismiss掉，必须在showPopup之前设置背景不为空	
        //设置背景要跟setOutsideTouchable方法放在同一函数中，前后顺序无所谓
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.color.transparent));	
		popupWindow.setFocusable(true);		
		popupWindow.setOutsideTouchable(true);
	}
	/**
	 * Show the query popup at a location of parent.
	 * @param parent 
	 */
	public void showPopup(final View parent){
		if (null == csaview||null == parent)
			return;
		else{	
			popupWindow.showAtLocation(parent, Gravity.BOTTOM|Gravity.CENTER,0, 0);	
			Animation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
					Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 1);//从自身中心出现
			//设置动画时间  
			scaleAnimation.setDuration(300);  
			view.startAnimation(scaleAnimation);
			popupWindow.update();			
		}
	}
}
