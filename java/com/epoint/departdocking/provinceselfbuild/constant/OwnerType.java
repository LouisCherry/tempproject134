package com.epoint.departdocking.provinceselfbuild.constant;

/**
 * appKey的拥有者类型
 * @author 刘雨雨
 * @time 2018年9月17日上午10:03:36
 */
public enum OwnerType {

	/**
	 * 部门自建系统，表示appKey是综合受理系统分配给自建系统的
	 */
	SELF_BUILD("自建"),

	/**
	 * 综合受理平台，表示appKey是自建系统分配给综合受理平台的
	 */
	COMPREHENSIVE("综合");

	private String value;

	private OwnerType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public String getValue() {
		return value;
	}

}
