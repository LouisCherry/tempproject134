package com.epoint.jnycsl.constant;
/**
 * 综合受理平台与自建系统交互 推送状态
 * @author 刘雨雨
 * @time 2018年10月8日下午4:00:55
 */
public enum PushStatus {
	NOT("0", "未推送"), SUCCESS("1", "推送成功"), FAILED("2", "推送失败");

	private String val;

	private String text;

	private PushStatus(String val, String text) {
		this.val = val;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public String getVal() {
		return val;
	}
}
