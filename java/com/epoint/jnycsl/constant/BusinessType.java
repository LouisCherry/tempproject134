package com.epoint.jnycsl.constant;

/**
 * 自建系统 收件接口 业务类型枚举类
 * @author 刘雨雨
 * @time 2018年9月12日上午9:20:43
 */
public enum BusinessType {
	
	/**
	 * 收件业务
	 */
	RECEIVE_BUSINESS(0, "收件业务"),
	
	/**
	 * 受理业务
	 */
	ACCEPT_BUSINESS(1, "受理业务");
	
	private Integer value;
	
	private String text;

	private BusinessType(int value, String text) {
		this.value = value;
		this.text = text;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
}
