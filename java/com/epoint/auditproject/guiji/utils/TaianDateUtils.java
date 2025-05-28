package com.epoint.auditproject.guiji.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author 刘雨雨
 * @time 2018年11月3日下午8:52:12
 */
public class TaianDateUtils {
	
	/**
	 * 在baseDate基础上，日期加减
	 * @param baseDate
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Calendar add(Date baseDate, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(field, amount);
		return calendar;
	}
}
