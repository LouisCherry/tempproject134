package com.epoint.zwdt.zwdtrest.project.impl;

import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.List;

public class YjsProjectService {

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public YjsProjectService() {
        baseDao = CommonDao.getInstance();
    }

    public PageData<AuditSpBusiness> getAuditSpBusinessByPage(Record record, int pageIndex, int pageSize) {
        //新增关键词搜索
        String sql = "select rowguid,businessname from audit_sp_business where businesstype = '2' and del = '0' " +
                " and (businessname like ? or keywordcontent like ? ) group by businessname";
        String sqlcount = "select count(1) from audit_sp_business where businesstype = '2' and del = '0' " +
                " and (businessname like ? or keywordcontent like ? ) group by businessname";
        PageData<AuditSpBusiness> pageData = new PageData<>();
        List<AuditSpBusiness> list = baseDao.findList(sql, pageIndex, pageSize, AuditSpBusiness.class, "%" + record.getStr("businessname") + "%", "%" + record.getStr("keyword") + "%");
        String count = baseDao.queryString(sqlcount, "%" + record.getStr("businessname") + "%", "%" + record.getStr("keyword") + "%");
        int count_new = 0;
        if (StringUtil.isNotBlank(count)) {
            count_new = Integer.parseInt(count);
        }
        pageData.setList(list);
        pageData.setRowCount(count_new);
        return pageData;
    }

    public boolean Ishighlight(String itemValue, String businessname) {
        //辖区
        boolean ishighlight = false;
        String sql = "select count(1) from audit_sp_business where 1=1 and businesstype = '2' and del = '0' and businessname = ? and areacode like ? ";
        int count = baseDao.queryInt(sql, businessname, itemValue + "%");
        if (count > 0) {
            ishighlight = true;
        }
        return ishighlight;
    }

    public boolean IshighlightTown(String areacode, String businessname) {
        //辖区
        boolean ishighlight = false;
        String sql = "select count(1) from audit_sp_business where 1=1 and businesstype = '2' and del = '0' and businessname = ? and areacode = ? ";
        int count = baseDao.queryInt(sql, businessname, areacode);
        if (count > 0) {
            ishighlight = true;
        }
        return ishighlight;
    }

    public AuditSpBusiness getBusinessByNameAndAreacode(String taskname, String areacode) {
        String sql = "select rowguid,businessname,areacode from audit_sp_business where 1=1 and businesstype = '2' and del = '0' and businessname = ? and areacode = ? ";
        return baseDao.find(sql, AuditSpBusiness.class, taskname, areacode);
    }

}
