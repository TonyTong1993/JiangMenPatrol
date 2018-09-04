package com.ecity.cswatersupply.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;

public class PhoneUtil {

    public static void call(Context mContext, String linkNum) {
        if (linkNum.contains("/")) {
            final String[] tels = linkNum.split("/");
            new AlertDialog.Builder(mContext).setTitle(mContext.getResources().getString(R.string.link_num)).setItems(tels, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    PhoneTo(tels[which].trim());
                }
            }).show();
        } else {
            PhoneTo(linkNum);
        }
    }

    private static void PhoneTo(String num) {
        if (TextUtils.isEmpty(num)) {
            ToastUtil.showShort("电话号码为空");
        } else {
            Pattern p = Pattern.compile("\\d+?");//正则表达式匹配电话号码
            Matcher match = p.matcher(num);

            if (match.matches()) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在Activity外调用startActivity，要加这个flag
                HostApplication.getApplication().getApplicationContext().startActivity(intent);
            } else {
                ToastUtil.showShort("号码格式有误");
            }
        }
    }

    public static List<String> getPhoneNumbers(String content) {
        List<String> phones = new ArrayList<>();
        Pattern p = Pattern.compile("[\\d-]*");
        Matcher matcher = p.matcher(content);
        while (matcher.find()) {
            String n = matcher.group(0).toString();
            phones.add(n);
        }

        List<String> result = new ArrayList<>();
        for(String str : phones) {
            if(str.contains("-")) {
                String temp = trimFirstAndLastChar(str, '-');
                if(!"".equals(temp)) {
                    result.add(temp);
                }
            } else {
                result.add(str);
            }

        }
        return result;
    }

    public static List<String> filterBlankStr(List<String> lists) {
        List<String> phones = new ArrayList<>();
        for(String str : lists) {
            if(StringUtil.isBlank(str) || "".equals(str)) {
                continue;
            }
            phones.add(str);
        }

        return phones;
    }

    public static String trimFirstAndLastChar(String source,char element){
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        String temp = "";
        do{
            int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
            int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element) : source.length();
            if(beginIndex == endIndex) {
                return "";
            }
            temp = source.substring(beginIndex, endIndex);
            beginIndexFlag = temp.indexOf(element) == 0;
            endIndexFlag = temp.lastIndexOf(element) + 1 == temp.length();
            source = temp;
        } while (beginIndexFlag || endIndexFlag);
        return source;
    }

}
