package com.epoint.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

public class SqlUtils {

	private Map<String, String> conditionMap = new HashMap<>();

	public SqlUtils() {
		// 设置标记，用于兼容当前模式
		conditionMap.put("#isnew", "1");
	}

	public SqlUtils(Map<String, String> map) {
		if (map != null && "1".equals(map.get("#isnew"))) {
			conditionMap.putAll(map);
		} else {
			conditionMap.put("#isnew", "1");
		}
	}

	/**
	 * 等于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void eq(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_EQ + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * 不等于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void nq(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_NQ + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * 大于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void gt(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_GT + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * 时间类型大于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void gt(String fieldName, Date fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_GT + SqlConstant.SPLIT + "D",
				EpointDateUtil.convertDate2String(fieldValue, EpointDateUtil.DATE_TIME_FORMAT));
	}

	/**
	 * 大于等于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void ge(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_GE + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * 时间类型大于等于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void ge(String fieldName, Date fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_GE + SqlConstant.SPLIT + "D",
				EpointDateUtil.convertDate2String(fieldValue, EpointDateUtil.DATE_TIME_FORMAT));
	}

	/**
	 * 小于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void lt(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_LT + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * 时间类型小于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void lt(String fieldName, Date fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_LT + SqlConstant.SPLIT + "D",
				EpointDateUtil.convertDate2String(fieldValue, EpointDateUtil.DATE_TIME_FORMAT));
	}

	/**
	 * 小于等于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void le(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_LE + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * 时间类型小于等于
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void le(String fieldName, Date fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_LE + SqlConstant.SPLIT + "D",
				EpointDateUtil.convertDate2String(fieldValue, EpointDateUtil.DATE_TIME_FORMAT));
	}

	/**
	 * like
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void like(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_LIKE + SqlConstant.SPLIT + "S",
				fieldValue);
	}
	/**
     * startWidth
     * @param fieldName 字段名
     * @param fieldValue 字段值
     */
    public void startWidth(String fieldName, String fieldValue) {
        conditionMap.put(
                fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_START_WITH + SqlConstant.SPLIT + "S",
                fieldValue);
    }
    
    /**
     * endWidth
     * @param fieldName 字段名
     * @param fieldValue 字段值
     */
    public void endWidth(String fieldName, String fieldValue) {
        conditionMap.put(
                fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_END_WITH + SqlConstant.SPLIT + "S",
                fieldValue);
    }

	/**
	 * in
	 * @param fieldName 字段名
	 * @param fieldValue 字段值,括号中的内容
	 */
	public void in(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_IN + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * not in 
	 * @param fieldName 字段名
	 * @param fieldValue 字段值，括号中的内容
	 */
	public void notin(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_NOTIN + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * between,只用于时间类型
	 * @param fieldName 字段名
	 * @param fromDate	开始时间
	 * @param endDate	结束时间
	 */
	public void between(String fieldName, Date fromDate, Date endDate) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_BETWEEN + SqlConstant.SPLIT + "D",
				EpointDateUtil.convertDate2String(fromDate, EpointDateUtil.DATE_TIME_FORMAT) + SqlConstant.SPLIT
						+ EpointDateUtil.convertDate2String(endDate, EpointDateUtil.DATE_TIME_FORMAT));
	}

	/**
	 * 为空
	 * @param fieldName 字段名
	 */
	public void isBlank(String fieldName) {
		isBlankOrValue(fieldName,"");
	}

	/**
	 * 为空或者等于某个值
	 * @param fieldName 字段名
	 * @param fieldValue 等于的值
	 */
	public void isBlankOrValue(String fieldName, String fieldValue) {
		conditionMap.put(
				fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_ISNULL + SqlConstant.SPLIT + "S",
				fieldValue);
	}

	/**
	 * 不为空
	 * @param fieldName 字段名
	 */
	public void isNotBlank(String fieldName) {
		conditionMap.put(fieldName + SqlConstant.SPLIT + SqlConstant.SQL_OPERATOR_ISNOTNULL
				+ SqlConstant.SPLIT + "S", "");
	}

	/**
	 * 设置倒序排列的字段
	 * @param fieldName
	 */
	public void setOrderDesc(String fieldName) {
		setOrder(fieldName,"desc");
	}

	/**
	 * 设置正序排列的字段
	 * @param fieldName
	 */
	public void setOrderAsc(String fieldName) {
		setOrder(fieldName,"asc");
	}

	/**
	 * 设置排序字段和方向
	 * @param sortField 字段名
	 * @param sortOrder 排序方向（asc,desc）
	 */
	public void setOrder(String sortField, String sortOrder) {
		String sort = conditionMap.get("#sort");
		if (StringUtil.isBlank(sort)){
			conditionMap.put("#sort", sortField + " " + sortOrder);
		}
		else{
			conditionMap.put("#sort", sort + "," + sortField + " " + sortOrder);
		}
	}

	/**
	 * 设置查询数目,只在查询List时有效
	 * @param count 查询数目
	 */
	public void setSelectCounts(Integer count) {
		conditionMap.put("#count", count.toString());
	}

	/**
	 * 需要查询的字段
	 * @param fields select的字段
	 */
	public void setSelectFields(String fields) {
		conditionMap.put("#fields", fields);
	}

	/**
	 * 左连接表，此时原表默认别名为 a
	 * @param tableName 表名
	 * @param leftTableField 左侧表关联的字段
	 * @param rightTableField 右侧表关联的字段
	 */
	public void setLeftJoinTable(String tableName, String leftTableField, String rightTableField){
		conditionMap.put("#join",
				(StringUtil.isBlank(conditionMap.get("#join")) ? "" : conditionMap.get("#join")) + "#left#" + tableName
						+ "#" + leftTableField + "#" + rightTableField + ";");
	}
	
	/**
	 * 右连接表，此时原表默认别名为 a
	 * @param tableName 表名
	 * @param leftTableField 左侧表关联的字段
	 * @param rightTableField 右侧表关联的字段
	 */
	public void setRightJoinTable(String tableName, String leftTableField, String rightTableField){
		conditionMap.put("#join",
				(StringUtil.isBlank(conditionMap.get("#join")) ? "" : conditionMap.get("#join")) + "#right#" + tableName
						+ "#" + leftTableField + "#" + rightTableField + ";");
	}
	
	/**
	 * 内连接表，此时原表默认别名为 a
	 * @param tableName 表名
	 * @param leftTableField 左侧表关联的字段
	 * @param rightTableField 右侧表关联的字段
	 */
	public void setInnerJoinTable(String tableName, String leftTableField, String rightTableField){
		conditionMap.put("#join",
				(StringUtil.isBlank(conditionMap.get("#join")) ? "" : conditionMap.get("#join")) + "#inner#" + tableName
						+ "#" + leftTableField + "#" + rightTableField + ";");
	}
	
	public Map<String, String> getMap() {
		return conditionMap;
	}
	
	/**
	 * 清除条件
	 */
	public void clear(){
		conditionMap.clear();
		conditionMap.put("#isnew", "1");
	}
}
