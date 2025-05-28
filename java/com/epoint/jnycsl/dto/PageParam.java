package com.epoint.jnycsl.dto;

/**
 * 分页查询参数，封装startIndex,pageSize,sortField,sortOrder四个参数
 * @author 刘雨雨
 * @time 2018年9月12日下午1:51:38
 */
public class PageParam {

	private int startIndex;

	private int pageSize;

	private String sortField;

	private String sortOrder;

	public PageParam() {
	}

	public PageParam(int startIndex, int pageSize, String sortField, String sortOrder) {
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public PageParam(int startIndex, int pageSize) {
		this.startIndex = startIndex;
		this.pageSize = pageSize;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
}
