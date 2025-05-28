package com.epoint.auditproject.guiji.constant;

/**
 * 前置库里的办件状态
 * 01草稿 02收件 03预受理 04预受理退回 05受理 06补齐补正 07不予受理 08在办 09挂起 10办结 11转报办结 12作废办结 13退件 99其他
 * @author 刘雨雨
 * @time 2018年8月29日下午6:10:08
 */
public class QzkProjectStatus {

	/**
	 * 01草稿
	 */
	public static final String DRAFT = "01";

	/**
	 * 02收件
	 */
	public static final String RECEIVE = "02";

	/**
	 * 03预受理
	 */
	public static final String PREPARE_ACCEPTED = "03";

	/**
	 *  04预受理退回
	 */
	public static final String PREPARE_ACCEPT_SEND_BACK = "04";

	/**
	 * 05受理
	 */
	public static final String ACCEPTED = "05";

	/**
	 * 06补齐补正
	 */
	public static final String BQBZ = "06";

	/**
	 * 07不予受理
	 */
	public static final String REFUSE_ACCEPT = "07";

	/**
	 * 08在办
	 */
	public static final String WORKED = "08";

	/**
	 * 09挂起
	 */
	public static final String SUSPEND = "09";
	
	/**
	 * 10办结
	 */
	public static final String NORMAL_DONE = "10";
	
	/**
	 * 11转报办结
	 */
	public static final String TRANSFOR_DONE = "11";
	
	/**
	 * 12作废办结
	 */
	public static final String OBSOLETE_DONE = "12";
	
	/**
	 * 13退件
	 */
	public static final String CANCELL_DONE = "13";
	
	/**
	 *  99其他
	 */
	public static final String OTHER = "99";
	
}
