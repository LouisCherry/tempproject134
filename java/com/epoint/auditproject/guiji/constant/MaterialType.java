package com.epoint.auditproject.guiji.constant;

/**
 * 材料类型，纸质还是电子
 * @author 刘雨雨
 * @time 2018年8月29日下午5:53:09
 */
public enum MaterialType {

	/**
	 * 电子文件
	 */
	ELECTRONIC("10", "电子文件"),
	
	/**
	 * 纸质文件
	 */
	PAPER("20","纸质文件"),
	
	/**
	 * 电子或纸质
	 */
	ELECTRONIC_OR_PAPER("35","电子或纸质"),
	
	/**
	 * 同时提交电子和纸质
	 */
	ELECTRONIC_AND_PAPER("40","同时提交电子和纸质");
	

	private String value;

	private String text;

	private MaterialType(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public static void main(String[] args) {
		MaterialType s = MaterialType.valueOf("PAPER");
		System.out.println(s);
	}
}
