package com.epoint.jnycsl.constant;
/**
 * 接口类型
 * @author 刘雨雨
 * @time 2018年9月11日下午9:07:26
 */
public enum ApiType {
	/**
	 * web service 接口
	 */
	WEB_SERVICE("webservice"),
	/**
	 * http 接口
	 */
	HTTP("http");

	private String value;

	private ApiType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
