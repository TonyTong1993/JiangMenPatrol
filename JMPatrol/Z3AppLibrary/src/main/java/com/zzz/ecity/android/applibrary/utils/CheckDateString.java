package com.zzz.ecity.android.applibrary.utils;

/**
 * @author yongzhan
 * @date 2018/5/22
 * 进行时间数据格式检查类
 */
public class CheckDateString {

    private static final String TAG = "CheckDateString";

    private static String[] splitSpace(String data){
        if(data == null){
            return null;
        }
        String[] spaces = data.split(" ");
        return spaces;
    }

    private static String[] splitUnderLine(String data){
        if(data == null){
            return null;
        }
        String[] spaces = data.split("-");
        return spaces;
    }

    private static String[] splitColon(String data){
        if(data == null){
            return null;
        }
        String[] spaces = data.split(":");
        return spaces;
    }

    /**
     * 通过横线（-）、空格、冒号进行时间字符串划分
     * @param data
     * @return
     */

    private static String[] splitTime(String data){
        if(data == null){
            return null;
        }
        String[] spaces = data.split("-| |:");
        return spaces;
    }

    /**
     * 检查被分的时间字符串数组，返回符合2018-03-03 22:22:22格式的时间
     * @param datas
     * @return
     */

    private static String checkTime(String[] datas){
        if(datas == null || datas.length < 6){
            return null;
        }
        String[] ds = new String[6];
        ds[0] = datas[0];
        for(int i=1;i<6;i++){
            String s = datas[i];
            if(s.length()>2){
                s = s.substring(s.length()-2,s.length());
            }
            ds[i] = s;
        }
        return  ds[0]+"-"+ds[1]+"-"+ds[2]+" "+ds[3]+":"+ds[4]+":"+ds[5];
    }

    public static String checkTimeStr(String data){
        String[] spaces = splitTime(data);
        return checkTime(spaces);
    }

//    private Date parseArriveTime(String timeStr) {
//        Date  date = DateUtil.stringToDate(timeStr, "yyyy-MM-dd HH:mm:ss");
//        if (date == null) {
//            LogUtil.i(TAG,"解析到位时间失败，将尝试再次解析 timeStr=" + timeStr);
//            String[] parts = timeStr.split(" ");
//            String[] dateComponents = parts[0].split("-");
//            dateComponents[1] = adjustTimeComponent(dateComponents, 1);
//            dateComponents[2] = adjustTimeComponent(dateComponents, 2);
//            String[] timeComponets = parts[1].split(":");
//            timeComponets[0] = adjustTimeComponent(timeComponets, 0);
//            timeComponets[1] = adjustTimeComponent(timeComponets, 1);
//            timeComponets[2] = adjustTimeComponent(timeComponets, 2);
//
//            StringBuilder datePart = new StringBuilder();
//            for (String str:dateComponents) {
//                if (datePart.length() > 0) {
//                    datePart.append("-");
//                }
//                datePart.append(str);
//            }
//
//            StringBuilder timePart = new StringBuilder();
//            for (String str:timeComponets) {
//                if (timePart.length() > 0) {
//                    timePart.append(":");
//                }
//                timePart.append(str);
//            }
//
//            timeStr = datePart.append(" ").append(timePart).toString();
//            LogUtil.i(TAG,"再次解析的结果 timeStr=" + timeStr);
//            date = DateUtil.stringToDate(timeStr, "yyyy-MM-dd HH:mm:ss");
//        }
//
//        return date;
//    }
//
//    private String adjustTimeComponent(String[] array, int index) {
//        String str = array[index];
//        LogUtil.i(TAG,"index=" + index + ", timeComponet=" + str);
//        if (str.length() == 4) {
//            str = str.substring(2, str.length());
//        }
//
//        if (str.length() == 1) {
//            str = "0" + str;
//        }
//        return str;
//    }


}
