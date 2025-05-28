package com.epoint.sghd.auditmember.api;

import com.epoint.core.grammar.Record;

import java.io.Serializable;
import java.util.List;

public interface IJnAuditOrgaMember extends Serializable {
    /**
     * @return int    返回类型
     * @throws
     * @Description: 获取新增的先进个人字段，插入数据库
     * @author marin
     * @date 2019年1月22日 上午9:52:36
     */
    public int updateIndivAuditMember(String isAdvanced, String isBiaoBing, String isRed, String rowguid);

    /**
     * @return List<Record>    返回类型
     * @throws
     * @Description: 获取auditMember个性化字段
     * @author marin
     * @date 2019年1月22日 上午9:59:14
     */
    public List<Record> getAuditMemberIndivColum(String rowguid);
}
