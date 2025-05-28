package com.epoint.auditproject.guiji.dto;

/**
 * 办件上报结果
 * @author 刘雨雨
 * @time 2018年9月5日上午11:27:16
 */
public class ReportResult {

	private int successCount;

	private int failedCount;

	private int total;

	public ReportResult() {
	}

	public ReportResult(int successCount, int failedCount, int total) {
		super();
		this.successCount = successCount;
		this.failedCount = failedCount;
		this.total = total;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
