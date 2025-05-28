package com.epoint.statistics.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.grammar.Record;

import java.util.List;
import java.util.Map;

/**
 * @author yuchenglin
 * @version 1.0.0
 * @ClassName OverdueAuditService.java
 * @Description 接口类
 * @createTime 2022年01月05日 16:41:00
 */
public interface IOverdueAuditService {

    /**
     * @param code: areacode
     * @param sql:  条件列表
     * @Author: yuchenglin
     * @Description:根据areacode来统计
     * @Date: 2022/1/6 19:08
     * @return: java.util.List<com.epoint.core.grammar.Record>
     **/
    List<Record> getListByAreacode(String code, String sql);


    /***
     * @Author: yuchenglin
     * @Description:
     * @Date: 2022/3/7 12:01
     * @param list:
     * @param sql:
     * @return: java.util.List<com.epoint.core.grammar.Record>
     **/
    List<Record> getListByOuguids(List<String> list, String sql);


    /**
     * @param code:
     * @param sql:
     * @Author: yuchenglin
     * @Description: 叶子节点精确查询
     * @Date: 2022/1/20 10:42
     * @return: java.util.List<com.epoint.core.grammar.Record>
     **/
    List<Record> getListByAOuguid(String code, String sql);

    /**
     * @param first:          页数
     * @param pageSize:一页条数
     * @param ouguids:        ouguids
     * @param type:类型
     * @param rouguid:rouguid
     * @Author: yuchenglin
     * @Description:二级查询
     * @Date: 2022/1/7 12:43
     * @return: java.util.List<com.epoint.basic.auditproject.auditproject.domain.AuditProject>
     **/
    List<AuditProject> getAuditProjectByTJ(int first, int pageSize, List<String> ouguids, String type, String flowsn);

    /**
     * @param ouguids:ouguids
     * @param type:type
     * @param rouguid:rouguid
     * @Author: yuchenglin
     * @Description:查询条数
     * @Date: 2022/1/7 12:44
     * @return: int
     **/
    int getCountByTJ(List<String> ouguids, String type, String flowsn);

    int getCountByTJ(List<String> ouguids, String type, AuditProject dataBean);


    List<AuditProject> getAuditProjectByTJ(int first, int pageSize, List<String> ouguids, String type, AuditProject dataBean);

}
