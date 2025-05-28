package com.epoint.jnycsl.constant;

/**
 * 新点事项配置里的自建系统模式
 * @author 刘雨雨
 * @time 2018年9月11日下午8:46:46
 */
public enum SelfBuildSystemMode {
	
	/**
	 * 接件模式
	 */
	RECEIVE_MODE("0", "接件模式"),
	
	/**
	 * 受理模式
	 */
	ACCEPT_MODE("1","受理模式");
	
	private String value;
	
	@SuppressWarnings("unused")
	private String text;
	
	private SelfBuildSystemMode(String value, String text) {
		this.value = value;
		this.text = text;
	}

	@Override
	public String toString() {
		return this.value;
	}
	
}
