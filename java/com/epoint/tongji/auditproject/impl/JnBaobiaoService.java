package com.epoint.tongji.auditproject.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.tongji.auditproject.domain.JnAuditProjectTJ;

public class JnBaobiaoService
{  
	
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;
    
    public JnBaobiaoService() {
        baseDao = CommonDao.getInstance();
    }
    
	public List<JnAuditProjectTJ> getTjs(String areacode) {
        String sql = "select * from statistics_project_byou where areacode=? ";
        List<JnAuditProjectTJ> list = baseDao.findList(sql, JnAuditProjectTJ.class, areacode);
        return list;
	}
}
