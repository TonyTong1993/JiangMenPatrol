package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 * 改变字符串中特定字符的颜色
 */
public class ColorTextView extends TextView {

    public ColorTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setSpecifiedTextsColor(String text, String specifiedTexts, int color)
    {
        if(!text.contains(specifiedTexts)) {
            setText(text);
            return;
        }
        List sTextsStartList = new ArrayList<>();

        int sTextLength = specifiedTexts.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do
        {
            start = temp.indexOf(specifiedTexts);

            if(start != -1)
            {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }

        }while(start != -1);

        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        int size = sTextsStartList.size();
        for(int i = 0; i < size; i++)
        {
            int position = (int)sTextsStartList.get(i);
            styledText.setSpan(new ForegroundColorSpan(color), position, position + sTextLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(styledText);
    }
}

