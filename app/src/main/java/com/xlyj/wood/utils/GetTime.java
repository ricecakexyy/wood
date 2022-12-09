package com.xlyj.wood.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/15
 * fun: 设置最后发布时间
 */
public class GetTime {
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    /**
     * 以友好的方式显示时间
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if(time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if(curDate.equals(paramDate)){
            int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
            if(hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
            else
                ftime = hour+"小时前";
            return ftime;
        }

        long lt = time.getTime()/86400000;
        long ct = cal.getTimeInMillis()/86400000;
        int days = (int)(ct - lt);
        if(days == 0){
            int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
            if(hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
            else
                ftime = hour+"小时前";
        }
        else if(days == 1){
            ftime = "昨天";
        }
        else if(days == 2){
            ftime = "前天";
        }
        else if(days > 2 && days <= 10){
            ftime = days+"天前";
        }
        else if(days > 10){
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 将字符串转位日期类型
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater2.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }
}
