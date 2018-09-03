package com.zzz.ecity.android.applibrary.view;

import java.util.ArrayList;
import java.util.List;

import com.zzz.ecity.android.applibrary.adapter.ColorSpinnerAdapter;
import com.zzz.ecity.android.applibrary.adapter.StringSpinnerAdapter;
import com.zzz.ecity.android.applibrary.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class CustomSpinner{
	// 定义回调事件，返回所选的列数
	public interface OnSpinnerListener {
		public void back(int position);
	}
	/***
	 * 字符串类型的数据
	 */
	private ArrayList<String> strList = null;
	/***
	 * 数字类型的数据
	 */
	private List<Integer> intList = null;
	/***
	 * 是否为颜色类型
	 */
	private boolean isColor = false;
	/***
	 * 颜色适配器
	 */
	private ColorSpinnerAdapter colorAdapter = null;
	/***
	 * 字符串适配器
	 */
	private StringSpinnerAdapter stringAdapter = null;
	/***
	 * 回调事件
	 */
	private OnSpinnerListener customSpinnerListener;
	
	/***
	 * popup 布局
	 */
	private LinearLayout popuplayout = null;
	/***
	 * 列表框
	 */
	private ListView listView;
	/***
	 * 弹出框
	 */
	private PopupWindow popupWindow;
	/**
	 * 设备上下文
	 */
	private Context context = null;
	//
	TextView textValueView = null;
	
	private LinearLayout spinnerlayout = null;
	
	private int selectedPosition =0;
	
	/***
	 * 初始化字符串类型
	 * @param context
	 * @param title
	 * @param msg
	 * @param customSpinnerListener
	 * @param strList
	 */
	public CustomSpinner(Context context,LinearLayout spinnerlayout,TextView textValueView,
			OnSpinnerListener customSpinnerListener,ArrayList<String> strList,int selectedPosition) {
		if(null!=strList) {
            this.strList = strList;
        } else {
            this.strList = new ArrayList<String>();
        }
		this.textValueView = textValueView;
		
		if(null == textValueView) {
            textValueView = new TextView(context);
        }
		
		this.intList =  null;
		this.spinnerlayout = spinnerlayout;
		this.context = context;
		stringAdapter = new StringSpinnerAdapter(context,strList);
		
		
		// 点击右侧按钮，弹出下拉框
		if(null != this.spinnerlayout) {
            this.spinnerlayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (CustomSpinner.this.strList.size() > 0) {
                        CustomSpinner.this.spinnerlayout.setBackgroundResource(R.drawable.css_default_input_edit/*css_list_first_item*/);
                    }
                    showWindow(CustomSpinner.this.spinnerlayout);
                }
            });
        }
		this.selectedPosition = selectedPosition;
		this.customSpinnerListener = customSpinnerListener;
		if(selectedPosition<strList.size()) {
            textValueView.setText(strList.get(selectedPosition));
        } else {
            textValueView.setText(strList.size() > 0 ? strList.get(selectedPosition) : "");
        }
	}
	/***
	 * 初始化int类型的,同时判断是否为颜色类型
	 * @param context
	 * @param title
	 * @param customSpinnerListener
	 * @param intList
	 * @param isColor 
	 */
	public CustomSpinner(Context context,LinearLayout spinnerlayout,
			OnSpinnerListener customSpinnerListener,ArrayList<Integer> intList,int selectedPosition,boolean isColor) {
		this.intList =  intList;
		this.isColor = isColor;
		
		if(!isColor)
		{
			strList = new ArrayList<String>();
			if(null!=intList) {
                for (Integer i : intList) {
                    strList.add(String.valueOf(i));
                }
            }
		}
		if(null == this.intList) {
            this.intList = new ArrayList<Integer>();
        }
		this.context = context;
		this.spinnerlayout = spinnerlayout;
		
		if(!isColor) {
            stringAdapter = new StringSpinnerAdapter(context, strList);
        } else {
            colorAdapter = new ColorSpinnerAdapter(context, intList);
        }
		
		// 点击右侧按钮，弹出下拉框
		this.spinnerlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!CustomSpinner.this.isColor)
				{
					if(CustomSpinner.this.strList.size()>0){
						CustomSpinner.this.spinnerlayout.setBackgroundResource(R.drawable.css_default_input_edit/*css_list_first_item*/);
					}
				}
				else
				{
					if(CustomSpinner.this.intList.size()>0){
						CustomSpinner.this.spinnerlayout.setBackgroundResource(R.drawable.css_default_input_edit/*css_list_first_item*/);
					}
				}
				showWindow(CustomSpinner.this.spinnerlayout);

			}
		});
		
		this.selectedPosition = selectedPosition;
		this.customSpinnerListener = customSpinnerListener;
		
		if(!isColor)
		{
			if(selectedPosition<strList.size()) {
                textValueView.setText(strList.get(selectedPosition));
            } else {
                textValueView.setText(strList.size() > 0 ? strList.get(selectedPosition) : "");
            }
		}
		else
		{
			if(selectedPosition<intList.size()) {
                textValueView.setBackgroundColor(intList.get(selectedPosition));
            } else {
                textValueView.setBackgroundColor(intList.size() > 0 ? intList.get(selectedPosition) : Color.BLACK);
            }
		}
	}
	public void setSelection(int selectedPosition)
	{
		this.selectedPosition = selectedPosition;
		if(null == textValueView) {
            return;
        }
		if(!isColor)
		{
			if(selectedPosition<strList.size()) {
                textValueView.setText(strList.get(selectedPosition));
            } else {
                textValueView.setText(strList.size() > 0 ? strList.get(selectedPosition) : "");
            }
		}
		else
		{
			if(selectedPosition<intList.size()) {
                textValueView.setBackgroundColor(intList.get(selectedPosition));
            } else {
                textValueView.setBackgroundColor(intList.size() > 0 ? intList.get(selectedPosition) : Color.BLACK);
            }
		}
	}
	public int getSelectedPosition()
	{
		return selectedPosition;
	}
	/***
	 * 获得选中项序号
	 * @param position
	 * @param txt
	 */
	@SuppressWarnings("deprecation")
	private void showWindow(View position) {
		if(null == context) {
            return;
        }
		popuplayout = (LinearLayout) LayoutInflater.from(this.context).inflate(R.layout.item_spinner_dropdown, null);
		listView = (ListView) popuplayout.findViewById(R.id.listView);
		
		if(!isColor) {
            listView.setAdapter(stringAdapter);
        } else {
            listView.setAdapter(colorAdapter);
        }
		
		popupWindow = new PopupWindow(position);
		// 设置弹框的宽度为布局文件的宽
		if(null!=spinnerlayout) {
            popupWindow.setWidth(spinnerlayout.getWidth());
        }
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(popuplayout);
		// 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
		popupWindow.showAsDropDown(position, 0, 0);
		popupWindow.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				if(null != spinnerlayout) {
                    spinnerlayout.setBackgroundResource(R.drawable.css_default_input_edit/*css_list_single_item*/);
                }
				if(null != customSpinnerListener) {
                    customSpinnerListener.back(selectedPosition);
                }
				if(!isColor) {
                    textValueView.setText(strList.get(selectedPosition));
                } else {
                    textValueView.setBackgroundColor(intList.get(selectedPosition));
                }
					
			}
			
		});
		// listView的item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectedPosition = arg2;
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}
}