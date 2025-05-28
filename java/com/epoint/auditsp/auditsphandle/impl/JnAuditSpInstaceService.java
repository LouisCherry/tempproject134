package com.epoint.auditsp.auditsphandle.impl;

import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 并联审批对应的后台service
 *
 * @author zhaoy
 * @version [版本号, 2019-04-24 15:11:41]
 */
public class JnAuditSpInstaceService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnAuditSpInstaceService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpInstance record) {
        String sql = "update AUDIT_SP_INSTANCE set imgcliengguid=?1,sltcliengguid=?2,videocliengguid=?3,bdcliengguid=?4,"
                + "lxpfliengguid1=?5,lxpfliengguid2=?6,lxpfliengguid3=?7,lxpfliengguid4=?8  "
                + "where rowguid = ?9";
        return baseDao.execute(sql, record.getStr("imgcliengguid"), record.getStr("sltcliengguid"), record.getStr("videocliengguid"), record.getStr("bdcliengguid"), record.getStr("lxpfliengguid1"), record.getStr("lxpfliengguid2"), record.getStr("lxpfliengguid3"), record.getStr("lxpfliengguid4"), record.getRowguid());
    }

    List<AuditSpInstance> getAuditSpInstanceByPage(int first, int pageSize, String applyername, String itemname) {
        List<String> params = new ArrayList<>();
        String sql = "select * from AUDIT_SP_INSTANCE where 1=1  and businesstype='1' and ifnull(status,'')='' ";
        if (StringUtil.isNotBlank(applyername)) {
            sql += " and applyername like ?";
            params.add("%" + applyername + "%");
        }
        if (StringUtil.isNotBlank(itemname)) {
            sql += " and itemname like ? ";
            params.add("%" + itemname + "%");
        }
        sql += "  order by createdate desc ";
        return baseDao.findList(sql, first, pageSize, AuditSpInstance.class, params.toArray());
    }

    public Integer getAuditSpInstanceCount(String applyername, String itemname) {
        List<String> params = new ArrayList<>();
        String sql = "select count(1) as countnum from AUDIT_SP_INSTANCE where 1=1  and businesstype='1' and ifnull(status,'')='' ";
        if (StringUtil.isNotBlank(applyername)) {
            sql += " and applyername like ? ";
            params.add("%" + applyername + "%");
        }
        if (StringUtil.isNotBlank(itemname)) {
            sql += " and itemname like ? ";
            params.add("%" + itemname + "%");
        }
        return baseDao.queryInt(sql, params.toArray());
    }


    public List<AuditSpInstance> getAuditSpInstanceListBySearch(int first, int pageSize, String itemcode, String itemname, String areacode) {
        List<String> params = new ArrayList<>();
        String sql = "select a.* from AUDIT_SP_INSTANCE a left join audit_rs_item_baseinfo b on a.YEWUGUID = b.RowGuid where 1=1 and a.businesstype='1' and ifnull(a.status,'')='' ";
        if (StringUtil.isNotBlank(itemname)) {
            sql += " and a.itemname like '%" + itemname + "%'";
            params.add("%" + itemname + "%");
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and a.areacode = ? ";
            params.add("%" + areacode + "%");
        }
        if (StringUtil.isNotBlank(itemcode)) {
            sql += " and b.itemcode like  ? ";
            params.add("%" + itemcode + "%");
        }
        sql += "  order by createdate desc ";
        return baseDao.findList(sql, first, pageSize, AuditSpInstance.class, params.toArray());
    }

    public int getAuditSpInstanceCountNew(String itemcode, String itemname, String areacode) {
        List<String> params = new ArrayList<>();
        String sql = "select count(1) from AUDIT_SP_INSTANCE a left join audit_rs_item_baseinfo b on a.YEWUGUID = b.RowGuid where 1=1 and a.businesstype='1' and ifnull(a.status,'')='' ";
        if (StringUtil.isNotBlank(itemcode)) {
            sql += " and b.itemcode like ? ";
            params.add("%" + itemcode + "%");
        }
        if (StringUtil.isNotBlank(itemname)) {
            sql += " and a.itemname like ? ";
            params.add("%" + itemname + "%");
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and a.areacode = ? ";
            params.add("%" + areacode + "%");
        }
        return baseDao.queryInt(sql, params.toArray());
    }
}
