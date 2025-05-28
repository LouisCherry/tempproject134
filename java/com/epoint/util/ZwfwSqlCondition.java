package com.epoint.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.epoint.core.dto.Component;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

/**
 * 政务服务搜索条件工具类
 * 
 * @author whfeng
 * @version 2021-6-1
 */
public class ZwfwSqlCondition
{

    /**
     * SQL语句
     */
    private String sql = "";
    /**
     * 对应参数
     */
    private Object[] params = {};
    /**
     * 存储的查询条件
     */
    private Map<String, Object> conditionMap = new HashMap<>();

    private String placeHolder = "_underline_placeHolder_";

    private String autoSearchPrefix = "search_";

    private String custom_expression_key = "custom_expression_key";

    private String custom_expression_value = "custom_expression_value";

    /**
     * SQL类型 LESS->小于(<) MORE->大于(>) EXACT->等于(=),FULL->全字符, RIGHT->右百分号(xx%)
     * CUSTOM->自定义SQL
     */
    public enum SqlType
    {
        LESS, MORE, LESSQ, MOREQ, EXACT, RIGHT, LEFT, CUSTOM, LIKE, FULL
    }

    public ZwfwSqlCondition() {

    }

    /**
     * 生成自动查询的sql
     * 
     * @param searchAreaList
     *            搜索区控件值
     */
    public static ZwfwSqlCondition getSearchCondition(List<Component> searchAreaList) {
        ZwfwSqlCondition condition = new ZwfwSqlCondition();

        if (searchAreaList != null && !searchAreaList.isEmpty()) {
            for (Component component : searchAreaList) {
                if (StringUtil.isBlank(component.getId()) || !component.getId().startsWith(condition.autoSearchPrefix)
                        || StringUtil.isBlank(component.getValue())) {
                    continue;
                }

                Object value = component.getValue();

                String fieldName = component.getId().replace(condition.autoSearchPrefix, "");
                SqlType thisSqlType = null;

                for (SqlType sqlType : SqlType.values()) {
                    if (fieldName.endsWith("_" + StringUtil.toLowerCase(sqlType.toString()))) {
                        thisSqlType = sqlType;
                        break;
                    }
                }

                if (thisSqlType != null) {
                    fieldName = fieldName.replace("_" + StringUtil.toLowerCase(thisSqlType.toString()), "");
                }

                if ("datepicker".equals(component.getType())) {
                    if (SqlType.LESS.equals(thisSqlType) || SqlType.LESSQ.equals(thisSqlType)) {
                        value = EpointDateUtil.getEndOfDate((Date) value);
                    }
                    else {
                        value = EpointDateUtil.getBeginOfDate((Date) value);
                    }
                }

                condition.put(fieldName, value, thisSqlType);
            }
        }

        condition.init();

        return condition;
    }

    /**
     * 加sql条件,默认like
     * 
     * @param key
     *            字段名
     * @param value
     *            字段值
     */
    public void put(String key, Object value) {
        put(key, value, null);
    }

    /**
     * 加sql条件
     * 
     * @param key
     *            字段
     * @param value
     *            字段值
     * @param sqlType
     *            查询类型
     * @param params
     *            其他参数
     */
    public void put(String key, Object value, SqlType sqlType, Object... params) {
        if (StringUtil.isBlank(key) || StringUtil.isBlank(value)) {
            return;
        }

        if (sqlType != null) {
            key += placeHolder + sqlType;

            if (SqlType.CUSTOM.equals(sqlType)) {
                // 自定义模式
                Map<String, Object> map = new HashMap<>();
                map.put(custom_expression_key, value);
                if (params != null) {
                    map.put(custom_expression_value, params);
                }

                conditionMap.put(key, map);
            }
            else {
                conditionMap.put(key, value);
            }

        }
        else {
            conditionMap.put(key, value);
        }

        init();
    }

    /**
     * 条件继承
     * 
     * @param sqlCondition
     */
    public void add(ZwfwSqlCondition sqlCondition) {
        if (sqlCondition != null && sqlCondition.getConditionMap() != null) {
            this.conditionMap.putAll(sqlCondition.getConditionMap());
            init();
        }
    }

    @SuppressWarnings("unchecked")
    public void init() {
        params = new Object[0];
        sql = "";

        if (conditionMap == null || conditionMap.isEmpty()) {
            return;
        }

        Set<String> keySet = conditionMap.keySet();
        if (keySet != null) {
            List<Object> list = new ArrayList<>();

            for (String key : keySet) {
                // 自定义的查询
                if (key.endsWith(placeHolder + SqlType.CUSTOM)) {
                    Map<String, Object> map = (Map<String, Object>) conditionMap.get(key);

                    sql += map.get(custom_expression_key);

                    if (map.containsKey(custom_expression_value)) {
                        Object[] paramArray = (Object[]) map.get(custom_expression_value);
                        for (int i = 0, length = paramArray.length; i < length; i++) {
                            list.add(paramArray[i]);
                        }
                    }
                }
                else {
                    Object value = conditionMap.get(key);
                    if (StringUtil.isNotBlank(value)) {
                        // 解决搜索条件含占位符的问题
                        if (value instanceof String) {
                            if (((String) value).contains("_")) {
                                value = value.toString().replace("_", "[_]");
                            }
                            else if (((String) value).contains("%")) {
                                value = value.toString().replace("%", "[%]");
                            }
                        }

                        if (key.endsWith(placeHolder + SqlType.EXACT)) {
                            key = key.replace(placeHolder + SqlType.EXACT, "");

                            sql += " and " + key + " = ?";
                        }
                        else if (key.endsWith(placeHolder + SqlType.LESS)) {
                            key = key.replace(placeHolder + SqlType.LESS, "");

                            sql += " and " + key + " < ?";
                        }
                        else if (key.endsWith(placeHolder + SqlType.MORE)) {
                            key = key.replace(placeHolder + SqlType.MORE, "");

                            sql += " and " + key + " > ?";
                        }
                        else if (key.endsWith(placeHolder + SqlType.LESSQ)) {
                            key = key.replace(placeHolder + SqlType.LESSQ, "");

                            sql += " and " + key + " <= ?";
                        }
                        else if (key.endsWith(placeHolder + SqlType.MOREQ)) {
                            key = key.replace(placeHolder + SqlType.MOREQ, "");

                            sql += " and " + key + " >= ?";
                        }
                        else if (key.endsWith(placeHolder + SqlType.LEFT)) {
                            key = key.replace(placeHolder + SqlType.LEFT, "");

                            sql += " and " + key + " like ?";

                            value = "%" + value;
                        }
                        else if (key.endsWith(placeHolder + SqlType.RIGHT)) {
                            key = key.replace(placeHolder + SqlType.RIGHT, "");

                            sql += " and " + key + " like ?";

                            value = value + "%";
                        }
                        else if (key.endsWith(placeHolder + SqlType.LIKE)) {
                            key = key.replace(placeHolder + SqlType.LIKE, "");

                            sql += " and " + key + " like ?";
                        }
                        else if (key.endsWith(placeHolder + SqlType.FULL)) {
                            key = key.replace(placeHolder + SqlType.FULL, "");

                            sql += " and " + key + " like ?";

                            char[] chars = value.toString().toCharArray();

                            value = "%";
                            for (int i = 0, length = chars.length; i < length; i++) {
                                value += chars[i] + "%";
                            }
                        }
                        else {
                            sql += " and " + key + " like ?";

                            value = "%" + value + "%";
                        }

                        list.add(value);
                    }
                }
            }

            params = list.toArray();
        }
    }

    /**
     * 生成列表/实体查询sql
     * 
     * @param tableName
     * @param selectFields
     * @param conditionSql
     * @param orderSql
     * @return
     */
    public static String generateSql(String tableName, String selectFields, String conditionSql, String orderSql) {
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotBlank(selectFields)) {
            sb.append("select " + selectFields + " from ");
        }
        else {
            sb.append("select * from ");
        }
        sb.append(tableName);
        sb.append(" where 1=1 ");
        sb.append(conditionSql);
        if (StringUtil.isNotBlank(orderSql)) {
            sb.append(" order by " + orderSql);
        }
        return sb.toString();
    }

    /**
     * 生成数量查询sql
     * 
     * @param tableName
     * @param conditionSql
     * @return
     */
    public static String generateCountSql(String tableName, String conditionSql) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        sb.append(tableName);
        sb.append(" where 1=1 ");
        sb.append(conditionSql);
        return sb.toString();
    }

    public void clear() {
        this.params = new Object[0];
        this.conditionMap.clear();
        this.sql = "";
    }

    public static String getEndDateString(String input) {
        if (StringUtil.isBlank(input)) {
            return null;
        }

        if (input.length() == 4) {
            return input + "-12-31 23:59:59.999";
        }
        else if (input.length() == 7) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(input.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(input.substring(5, 7)) - 1);
            int day = calendar.getActualMaximum(Calendar.DATE);

            calendar.set(Calendar.DAY_OF_MONTH, day);

            String lastDayOfMonth = EpointDateUtil.convertDate2String(calendar.getTime(), "dd");

            return input + "-" + lastDayOfMonth + " 23:59:59.999";
        }
        else if (input.length() == 10) {
            return input + " 23:59:59.999";
        }
        else if (input.length() == 13) {
            return input + ":59:59.999";
        }
        else if (input.length() == 16) {
            return input + ":59.999";
        }
        else if (input.length() == 19) {
            return input + ".999";
        }

        return null;
    }

    public String getSql() {
        return sql;
    }

    public Map<String, Object> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, Object> conditionMap) {
        this.conditionMap = conditionMap;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

}
