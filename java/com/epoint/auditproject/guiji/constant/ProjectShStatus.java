package com.epoint.auditproject.guiji.constant;

/**
 * 办件审核状态 
 * @author 刘雨雨
 * @time 2018年8月23日下午2:53:59
 */
public enum ProjectShStatus {
	/**
	 * 外网申报初始化
	 */
	WW_INITIAL(8, "外网申报初始化"),
	
	/**
	 * 外网申报未提交
	 */
	WW_NOT_SUBMIT(10,"外网申报未提交"),
	
	/**
	 * 外网申报已提交
	 */
	WW_SUBMITED(12,"外网申报已提交"),
	
	/**
	 * 外网申报预审退回
	 */
	WW_YSTH(14,"外网申报预审退回"),
	
	/**
	 * 外网申报预审通过
	 */
	WW_YSTG(16,"外网申报预审通过"),
	
	/**
	 * 初始化
	 */
	INITIAL(20,"初始化"),
	
	/**
	 * 预登记
	 */
	YDJ(22,"预登记"),
	
	/**
	 * 待接件
	 */
	DJJ(24,"待接件"),
	
	/**
	 * 已接件
	 */
	YJJ(26,"已接件"),
	
	/**
	 * 待补办
	 */
	DBB(28,"待补办"),
	
	/**
	 * 已受理
	 */
	YSL(30,"已受理"),
	
	/**
	 * 已受理待补办
	 */
	YSLDBB(37,"已受理待补办"),
	
	/**
	 * 审批不通过
	 */
	YSBTG(40,"审批不通过"),
	
	/**
	 * 审批通过
	 */
	YSTG(50,"审批通过"),
	
	/**
	 * 正常办结
	 */
	ZCBJ(90,"正常办结"),
	
	/**
	 * 不予受理
	 */
	BYSL(97,"不予受理"),
	
	/**
	 * 撤销申请
	 */
	CXSQ(98,"撤销申请"),
	
	/**
	 * 异常终止
	 */
	YCZZ(99,"异常终止");

	private Integer value;

	private String text;
	
	private ProjectShStatus(Integer value, String text) {
		this.value = value;
		this.text = text;
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
