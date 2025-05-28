package com.epoint.composite.auditsp.handleproject.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class TAHandleProjectService
{

    protected ICommonDao baseDao;

    public TAHandleProjectService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     *  获取operateusername，判断是否是同步服务
     *  @param rowguid
     *  @return    
     */
    public String getOperateUsername(String rowguid) {
        String sql = "select operateusername from audit_task where rowguid = ? ";
        return baseDao.queryString(sql, rowguid);
    }

    /**
     *  执行存储过程
     *  @param var1
     *  @param var2
     *  @param var3
     *  @param var4
     *  @return    
     */
    public Object executeProcudureWithResult(int var1, int var2, String var3, Object... var4) {
        return baseDao.executeProcudureWithResult(var1, var2, var3, var4);
    }
}
