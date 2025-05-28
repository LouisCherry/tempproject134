package com.epoint.statistics.service;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.statistics.api.IOverdueAuditService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yuchenglin
 * @version 1.0.0
 * @ClassName OverdueAuditServiceImpl.java
 * @Description 接口类的实现类
 * @createTime 2022年01月05日 16:43:00
 */
@Service
public class OverdueAuditServiceImpl implements IOverdueAuditService {


    /**
     * @param code: areacode
     * @param sql:  条件列表
     * @Author: yuchenglin
     * @Description:根据areacode来统计
     * @Date: 2022/1/6 19:08
     * @return: java.util.List<com.epoint.core.grammar.Record>
     **/
    @Override
    public List<Record> getListByAreacode(String code, String sql) {
        return new OverdueAuditService().getListByAreaCode(code, sql);
    }

    @Override
    public List<Record> getListByOuguids(List<String> list, String sql) {
        return new OverdueAuditService().getListByOuguids(list, sql);
    }

    @Override
    public List<Record> getListByAOuguid(String code, String sql) {
        return new OverdueAuditService().getListByAOuguid(code, sql);
    }

    /**
     * @param first:        页数
     * @param pageSize:一页条数
     * @param ouguids:      ouguids
     * @param type:类型
     * @param sql:rouguid
     * @Author: yuchenglin
     * @Description:二级查询
     * @Date: 2022/1/7 12:43
     * @return: java.util.List<com.epoint.basic.auditproject.auditproject.domain.AuditProject>
     **/
    @Override
    public List<AuditProject> getAuditProjectByTJ(int first, int pageSize, List<String> ouguids, String type, String flowsn) {
        return new OverdueAuditService().getAuditProjectByTJ(first, pageSize, ouguids, type, flowsn);
    }

    /**
     * @param ouguids:ouguids
     * @param type:type
     * @param rouguid:rouguid
     * @Author: yuchenglin
     * @Description:查询条数
     * @Date: 2022/1/7 12:44
     * @return: int
     **/
    @Override
    public int getCountByTJ(List<String> ouguids, String type, String flowsn) {
        return new OverdueAuditService().getCount(ouguids, type, flowsn);
    }

    @Override
    public int getCountByTJ(List<String> ouguids, String type, AuditProject dataBean) {
        return new OverdueAuditService().getCount(ouguids, type, dataBean);
    }

    @Override
    public List<AuditProject> getAuditProjectByTJ(int first, int pageSize, List<String> ouguids, String type, AuditProject dataBean) {
        return new OverdueAuditService().getAuditProjectByTJ(first, pageSize, ouguids, type, dataBean);
    }
}
