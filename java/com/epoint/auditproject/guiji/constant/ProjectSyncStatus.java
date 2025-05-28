package com.epoint.auditproject.guiji.constant;

/**
 * 办件同步状态枚举类
 * @author 刘雨雨
 * @time 2018年8月20日下午3:17:14
 */
public enum ProjectSyncStatus {

	/**
	 * 	同步成功
	 */
	SUCCESS("1", "上报成功"),
	
	/**
	 * 	同步失败
	 */
	FAILED("2", "上报失败"), 
	
	/**
	 * 	同步彻底失败，尝试推送后仍然失败，无可救药，扶不起的阿斗
	 */
	FAILED_COMPLETELY("3", "上报彻底失败"), 
	
//	/**
//	 * 	没有数据，无需上报
//	 */
//	NONE("4", "没有数据无需上报"), 
	
	/**
	 * 	未同步，未同步的状态位就是0，不允许为null
	 */
	NEVER("0", "未上报");

	private String value;
	
	private String text;

	private ProjectSyncStatus(String value, String text) {
		this.value = value;
		this.text = text;

	}

	public String getValue() {
		return value;
	}
	
	public String getText() {
		return text;
	}

}
