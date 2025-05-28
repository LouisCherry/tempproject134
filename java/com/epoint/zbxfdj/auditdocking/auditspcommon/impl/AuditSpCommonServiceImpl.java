package com.epoint.zbxfdj.auditdocking.auditspcommon.impl;

import com.epoint.core.grammar.Record;
import com.epoint.zbxfdj.auditdocking.auditspcommon.api.IAuditSpCommonService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 消防事项电子表单service实现类
 *
 * @author WZW
 * @version [版本号, 2022-12-07 14:07:28]
 */
@Component
public class AuditSpCommonServiceImpl implements IAuditSpCommonService {

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    @Override
    public List<Record> findList(Map<String, String> conditionMap, String tableName) {
        return new AuditSpCommonService().findList(conditionMap, tableName);
    }

    @Override
    public Record find(Map<String, String> map, String tableName) {
        return new AuditSpCommonService().find(map, tableName);
    }


}
