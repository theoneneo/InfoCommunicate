package com.neo.tools;

import java.text.SimpleDateFormat;

public class DateUtil {
	/**
     * 格式化日期
     * @param long unixTime unix时间戳
     * @return 日期字符串"yyyy-MM-dd HH:mm:ss"
     * */
    public static String formatUnixTime(long unixTime)
    {  
        return formatUnixTime(unixTime, "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 格式化日期
     * @param long unixTime unix时间戳
     * @param String format 格式化字符串
     * @return 日期字符串
     * */
    private static String formatUnixTime(long unixTime, String format)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);  
        return dateFormat.format(unixTime);  
    }
}
