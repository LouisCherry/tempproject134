package com.epoint.smsinfo.util;

import java.util.Map;

import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlConstant;

/**
 * 
 * 加强的SqlConditionUtil
 * 
 * @author yrchan
 * @version 2021年8月31日
 */
public class EnhancedSqlConditionUtil extends SqlConditionUtil
{

    /**
     * 构造方法
     * 
     * @param map
     *            map
     */
    public EnhancedSqlConditionUtil(Map<String, Object> map) {
        if (map != null && SqlConstant.CONSTANT_STR_ONE.equals(map.get("#isnew"))) {
            super.getMap().putAll(map);
        }
        else {
            super.getMap().put("#isnew", SqlConstant.CONSTANT_STR_ONE);
        }
    }
}
