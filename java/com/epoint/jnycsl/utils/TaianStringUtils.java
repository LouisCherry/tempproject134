package com.epoint.jnycsl.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 格式化字符串,一般用来格式化记录的主键字符串，方便用sql的in关键字
 * @author 刘雨雨
 * @time 2018年9月12日下午3:45:43
 */
public class TaianStringUtils {

	/**
	 * 格式化字符串,将a,b,c 格式化为 'a','b','c'
	 * @param rowguids
	 * @return
	 */
	public static String formatStr(String strs) {
		if (StringUtils.isBlank(strs)) {
			return "";
		}
		String[] strArray = strs.split(",");
		return formatStr(strArray);
	}

	/**
	 * 格式化字符串,将集合里的元素格式化为  'a','b','c'
	 * @param rowguids
	 * @return
	 */
	public static String formatStr(List<String> strList) {
		if (strList == null || strList.isEmpty()) {
			return "";
		}
		return formatStr(strList.toArray(new String[strList.size()]));
	}
	
	/**
	 * 格式化字符串,将数组里的元素格式化为  'a','b','c'
	 * @param rowguids
	 * @return
	 */
	public static String formatStr(String[] strArray) {
		if (strArray == null || strArray.length == 0) {
			return "";
		}
		StringBuffer formatStr = new StringBuffer();
		for (String str : strArray) {
			formatStr.append("'").append(str).append("',");
		}
		if (formatStr.toString().endsWith(",")) {
			formatStr.replace(formatStr.length() - 1, formatStr.length(), "");
		}
		return formatStr.toString();
	}
	
}
