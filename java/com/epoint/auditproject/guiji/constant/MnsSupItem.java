package com.epoint.auditproject.guiji.constant;

/**
 * 监察具体内容分类，监察的是哪种数据，办件？考勤？还是时效？
 * 1=时效监察;2=费用监察;3=流程监察;4=内容监察。指监察规则的分类，对于不明确类型的，建议归为4(内容监察)
 * @author 刘雨雨
 * @time 2018年8月29日上午11:05:15
 */
public class MnsSupItem {
	
	/**
	 * 时效监察
	 */
	public static final int TIME = 1; 
	/**
	 * 费用监察
	 */
	public static final int FEE = 2; 
	
	/**
	 * 流程监察
	 */
	public static final int PROCESS = 3; 
	/**
	 * 内容监察
	 */
	public static final int CONTENT = 4; 
	
}
