package com.ecity.android.map.core.local;

import android.util.Log;

import java.util.Stack;

/***
 * 输入select sql from sqlite_master where name ="xxx"
 * 这个语句的查询结果（sql）以及和表名、表类型，获得已经存在的某个表的属性字段结构
 * 
 * @author ZiZhengzhuan
 * 
 */
public class TableStructHelper {
    // 左、右括号常量
    private final char LEFTBRACKET = '(';
    private final char RIGHTBRACKET = ')';

    public TableStructHelper() {

    }

    // 第N对括号
    private int TARGETNUM = 1;

    /***
     * 
     * 根据一个完整的建表语句获得表属性结构字段信息
     * 
     * @param tbName
     *            ,type,sql
     * @return
     */
    public TableModel getTableForSQL(String tbName, String tbType, String sql) {
        // 不为空，则进行处理，截取字符串
        if (null == sql || sql.length() <= 0) {
            Log.e("TableStructHelper", "输入的字符串为空。");
            return null;
        }
        // 去除回车和换行符
        sql = sql.replace("\r\n", "");
        // 去除[
        sql = sql.replace("[", "");
        // 去除]
        sql = sql.replace("]", "");
        // 转换为数组处理
        char[] cArr = sql.toCharArray();
        // 校验括号匹配性以及是否达到目标数
        if (!isAllowed(cArr)) {
            return null;
        }

        // 截取括号中的字符串
        String targetStr = getStrFromBracket(cArr);
        if (null != targetStr) {
            // Log.e("TableStructHelper","截取目标括号中的字符串内容为：" + targetStr);
        }
        //

        String strArr[] = targetStr.split(",");
        if (strArr.length < 2)
            return null;
        TableModel table = new TableModel();
        table.setName(tbName);
        table.setType(tbType);

        for (int i = 0; i < strArr.length; i++) {
            String tmpstr = strArr[i];
            if (null == tmpstr || tmpstr.equalsIgnoreCase(""))
                continue;
            // 去除整个字符串，串首的空格
            int subStrIndex = 0;
            for (int j = 0; j < tmpstr.length(); j++) {
                if (tmpstr.charAt(j) != ' ') {
                    subStrIndex = j;
                    break;
                }
            }
            tmpstr = tmpstr.substring(subStrIndex);

            String tmpstrArr[] = tmpstr.split(" ");
            if (null == tmpstrArr || tmpstrArr.length < 2)
                continue;
            try {
                String fname = tmpstrArr[0];
                String ftype = tmpstrArr[1];
                fname = fname.replaceAll("\"", "");
                fname = fname.replaceAll("\'", "");
                ftype = ftype.replaceAll("\"", "");
                ftype = ftype.replaceAll("\'", "");
                ftype = ftype.replaceAll(" ", "");
                if (tmpstrArr.length > 2 && ftype.equalsIgnoreCase("")
                        && !tmpstrArr[2].equalsIgnoreCase(""))
                    ftype = tmpstrArr[2];
                if (!DataTypesHelper.isValidDataType(ftype))
                    continue;
                FieldModel field = new FieldModel();
                field.setName(fname);
                field.setType(DataTypesHelper.fieldDataType(ftype));
                table.addField(field);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return table;
    }

    /**
     * 入参的字符串括号要匹配，个数要超过TARGETNUM值
     */
    private boolean isAllowed(char[] arr) {
        // 若长度小于TARGETNUM，则括号数不够
        if (null == arr || arr.length < TARGETNUM) {
            Log.e("TableStructHelper", "入参字符串不合法！");
            return false;
        }
        // 左括号计数器
        int counter = 0;
        Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < arr.length; i++) {
            char k = arr[i];
            switch (k) {
            // 左括号，计数器+1
            case LEFTBRACKET: {
                counter++;
                stack.add(k);
                break;
            }
            // 右括号，找出相应的左括号出栈
            case RIGHTBRACKET: {
                if (stack.isEmpty()) {
                    Log.e("TableStructHelper", "入参字符串不合法！");
                    return false;
                }
                stack.pop();
                break;
            }
            default:
            }
        }

        // 若匹配括号数少于目标值或者括号不匹配
        if (counter < TARGETNUM || !stack.isEmpty()) {
            Log.e("TableStructHelper", "入参字符串不合法！");
            return false;
        }
        return true;
    }

    /**
     * 从合法的字符串数组中，找到指定的字符串
     * 
     * @param cArr
     * @return
     * @see
     */
    private String getStrFromBracket(char[] cArr) {
        // 左右下标初始化
        // int left = 0;
        // int right = 0;

        // 成对括号计数器
        int counter = 0;

        int i = 0;
        // 找到第N个左括号
        for (; i < cArr.length && counter < TARGETNUM; i++) {
            if (LEFTBRACKET == cArr[i]) {
                counter++;
            }
        }

        // 从目标左括号开始截取字符串
        int leftNum = 0;
        StringBuilder sb = new StringBuilder();
        outer: for (; i < cArr.length; i++) {
            if (cArr[i] == RIGHTBRACKET) {
                // 左右括号数匹配了
                --leftNum;
                if (leftNum < 0) {
                    break outer;
                }
            } else if (cArr[i] == LEFTBRACKET) {
                // 多余的左括号也要加入到String中，这里不需要break操作
                ++leftNum;
            }
            sb.append(cArr[i]);
        }
        return sb.toString();
    }
}
