package com.epoint.sghd.auditmember.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

import java.util.List;

public class JnAuditOrgaMemberService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnAuditOrgaMemberService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * @return int    返回类型
     * @throws
     * @Description: 获取新增的先进个人字段，插入数据库
     * @author marin
     * @date 2019年1月22日 上午9:52:36
     */
    public int updateIndivAuditMember(String isAdvanced, String isBiaoBing, String isRed, String rowguid) {
        String sqlupdate = "UPDATE audit_orga_member SET Is_Advanced = ? ,is_BiaoBing = ?, is_red = ? where RowGuid = ?";
        return baseDao.execute(sqlupdate, isAdvanced, isBiaoBing, isRed, rowguid);
    }

    /**
     * @return List<Record>    返回类型
     * @throws
     * @Description: 获取auditMember个性化字段
     * @author marin
     * @date 2019年1月22日 上午9:59:14
     */
    public List<Record> getAuditMemberIndivColum(String rowguid) {
        String sql = "select Is_Advanced,is_BiaoBing,is_red from audit_orga_member where RowGuid = ? ";
        return baseDao.findList(sql, Record.class, rowguid);
    }
}
