package com.abner.c1n.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日历工具类
 * @author LW
 * @time 2019年6月8日 下午4:21:04
 */
public class CalendarUtils {
	
	public static String formatTimeByDay(String startDate,int day){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(startDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, day);
			return dateFormat.format(calendar.getTime());
		}catch(Exception e){
			return null;
		}
	}
	
	public static String formatTimeByDay(int day){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, day);
			return dateFormat.format(calendar.getTime());
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	public static String formatTimeByHour(String startDate,int hour){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
			Date date = dateFormat.parse(startDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, hour);
			return dateFormat.format(calendar.getTime());
		}catch(Exception e){
			return null;
		}
	}
	
	public static String currentTimeByHour(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		return dateFormat.format(new Date());
	}
	
	public static String currentTimeByDay(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(new Date());
	}

}
