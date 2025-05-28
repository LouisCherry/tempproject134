package com.epoint.tongbufw;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

public class byslSyncProjectService
{
    transient Logger log = LogUtil.getLog(byslSyncProjectService.class);

    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected ICommonDao commonDaoTo;
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzpassword");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public byslSyncProjectService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public List<AuditProject> getProjectbytsflag() {
        String sql = "select * from audit_project where status = '97' and ifnull(ISSYNACWAVE,0) = 0 and Acceptusername is not null and DATE_FORMAT(applydate,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') limit 200";
        List<AuditProject> list = commonDaoTo.findList(sql, AuditProject.class);
        return list;
    }
   
    
    public int updateProjectIssynacwaveByRowguid( String ISSYNACWAVE, String rowguid) {
    	String sql = "update audit_project set ISSYNACWAVE = '"+ISSYNACWAVE+"' where rowguid = '"+rowguid+"'";
        int result = commonDaoTo.execute(sql);
        return result;
    }
    
}
