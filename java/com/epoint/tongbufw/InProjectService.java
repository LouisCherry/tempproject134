package com.epoint.tongbufw;

import org.apache.log4j.Logger;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.log.LogUtil;

public class InProjectService
{
    transient Logger log = LogUtil.getLog(InProjectService.class);

    /**
     * 数据库操作DAO
     */

    protected ICommonDao commonDaoTo;

    /**
     * 前置库数据源
     */

    public InProjectService() {
        commonDaoTo = CommonDao.getInstance();
    }


    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    
    public int insert(Record record) {
        int result = commonDaoTo.insert(record);
        return result;
    }
    
}
