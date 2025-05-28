package com.epoint.jnycsl.utils;

/**
 * 新点restful接口token工具类
 * @author 刘雨雨
 * @time 2018年9月11日下午5:57:19
 */
public class EpointTokenUtils {
	
	/** token码采用标准版的，前端需要传递这样的token，后端会校验*/
	public static final String TOKEN = "Epoint_WebSerivce_**##0601";
	
	/**
	 * 校验前台传过来的token值，相等返回true，不相等返回false
	 * @param token
	 * @return
	 */
	public static boolean validateToken(String token) {
		return TOKEN.equals(token);
	} 
}
