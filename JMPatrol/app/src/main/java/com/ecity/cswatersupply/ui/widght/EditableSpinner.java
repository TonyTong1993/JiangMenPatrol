package com.ecity.cswatersupply.ui.widght;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.MylistDialog.ChoiceCallBack;




/**
 * 此类描述的是： 可下拉 可填组合输入框
 * 
 * @author: wangliu94@163.com
 * @version: 2015年4月21日 下午8:30:56
 */

public class EditableSpinner extends EditText {
    /**
     * 下拉按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;
    /**
     * 
     * Spinner
     * 
     * @param context
     */

    MylistDialog dialog = null;

    private Context mContext = null;

    private ArrayList<String> items = new ArrayList<String>();
    
//    private OnItemSelected onItemSelected = null;
    
    private ChoiceCallBack callback = null;
    
//    public interface OnItemSelected {
//        public void onSelected(int position, String value);
//    }
    
    

    public EditableSpinner(Context context) {
        this(context, null);

    }

    public EditableSpinner(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditableSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            // throw new
            // NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(
                    R.drawable.icon_expand);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        // 默认设置隐藏图标
        setClearIconVisible(true);        
        dialog = new MylistDialog(mContext, R.style.common_dialog);

        dialog.setListItems(items);
        callback = new ChoiceCallBack() {

            @Override
            public void onSelected(int position, String value) {

                setText(value);
                dialog.dismiss();
//                onItemSelected.onSelected(position, value);

            }
        };
        dialog.setInputCallback(callback);

    }

    public void setList(ArrayList<String> values) {
        this.items = values;
        dialog.setListItems(items);
    }   
    
    public ArrayList<String>getList(){
        return items;
    }
    
    
    public void setList(String[] values) {
        items.clear();
        for (int i = 0; i < values.length; i++) {
            items.add(values[i]);
        }
        
        dialog.setListItems(items);
    }

    public void setOnItemSelectedListener() {

    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    dialog.show();
                    
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * 
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public boolean hasFoucs() {
        return hasFoucs;

    }

//  public Spinner getSpinner() {
//      return spinner;
//  }
//
//  public void setSpinner(Spinner spinner) {
//      this.spinner = spinner;
//  }

//    public OnItemSelected getOnItemSelected() {
//        return onItemSelected;
//    }

//    public void setOnItemSelected(OnItemSelected onItemSelected) {
//        this.onItemSelected = onItemSelected;
//    }

}
