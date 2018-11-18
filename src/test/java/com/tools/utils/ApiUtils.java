package com.tools.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiUtils {

	/**
	 * 获取现在日期
	 * 
	 * @return返回字符串格式 yyyyMMdd
	 */
    public static String getCurrentDateStr()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }
    
	/**
	 * 获取昨天日期
	 * 
	 * @return返回字符串格式 yyyyMMdd
	 */
    public static String getYesterdayStr()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String timeString = dataFormat.format(date.getTime()-86400000L);
        return timeString;
    }
    
	/**
	 * 获取明天日期
	 * 
	 * @return返回字符串格式 yyyyMMdd
	 */
    public static String getTomorrowStr()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String timeString = dataFormat.format(date.getTime()+86400000L);
        return timeString;
    }
    
	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyyMMddHHmmss
	 */
    public static String getCurrentDateSecondStr()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }
    
	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyyMMdd HH:mm:ss
	 */
    public static String getCurrentDateSecondStr1()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }
    
	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyyMMddHHmmssSSS
	 */
    public static String getCurrentDateMillisecondStr()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }

    
	/**
	 * 获取手机号，每秒一个新的
	 * 
	 * @return返回字符串格式 140********
	 */
    public static String getMobStr()
    {
    	long currentTime=Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
//    	long currentTime1 = (long) (currentTime + Math.random()*900+100 + 12512840000L);
    	long currentTime1 = currentTime + 12512840000L;
        String timeString = String.valueOf(currentTime1);
        return timeString;
    }
    
	/**
	 * 获取随机手机号
	 * 
	 * @return返回字符串格式 160********
	 */
    public static String getMobRandomStr()
    {
    	long currentTime=Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
    	long currentTime1 = (long) (currentTime + Math.random()*10+10 + 14489275000L);
//    	long currentTime1 = (long) (currentTime + Math.random()*100+100);
        String timeString = String.valueOf(currentTime1);
        return timeString;
    }
    
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
//			Thread.sleep(1000);
//			System.out.println(getMobStr());
			System.out.println(getMobRandomStr());
//			System.out.println((long)(Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0,10))));
			
			
//			System.out.print("\t");
//			System.out.print(g.generate());
//			System.out.print("\t");
//			System.out.print(g.generate());
//			System.out.print("\t");
//			System.out.println(g.generate());
		}
	}
}
