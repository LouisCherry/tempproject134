package com.epoint.zbxfdj.auditdocking.auditspcommon.api;


import com.epoint.core.grammar.Record;

import java.util.List;
import java.util.Map;

/**
 * 消防事项电子表单对应的后台service接口
 * 
 * @author WZW
 * @version [版本号, 2022-12-07 14:07:28]
 */
public interface IAuditSpCommonService
{


    /**
     * 查找一个list
     * 
     * @param conditionMap
     *            查询条件集合
     * @return T extends BaseEntity
     */
    public List<Record> findList(Map<String, String> conditionMap,String tableName);


    Record find(Map<String, String> map, String tableName);
}
