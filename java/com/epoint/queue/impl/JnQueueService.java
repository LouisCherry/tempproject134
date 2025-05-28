package com.epoint.queue.impl;

import java.util.List;

import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.common.service.AuditCommonService;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 设备维护表对应的后台service
 * 
 * @author zhaoy
 * 
 */
public class JnQueueService extends AuditCommonService
{
	 /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;
    
	 private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "qfspurl");
	 private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "qfspusername");
	 private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "qfsppassword");
	    
	 /**
     * 前置库数据源
     */
     private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);


     public JnQueueService() {
         commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
     }
     
     /**
      * 
      */
     private static final long serialVersionUID = -689826604750277274L;
    
     
     public List<AuditOrgaHall> getHallList(String centerguid){
         String sql = "select  * from AUDIT_ORGA_HALL where 1=1  and centerguid= ? order by ORDERNUM desc ";
         return commonDaoFrom.findList(sql, AuditOrgaHall.class, centerguid);
     }
     
     
     public int getWindowCount(String lobbytype){
    	 String sql = "select  count(1) as total from AUDIT_ORGA_WINDOW where lobbytype= '"+lobbytype+"' and IS_USEQUEUE='1'";
    	 return commonDaoFrom.queryInt(sql, AuditOrgaWindow.class, lobbytype);
     }
     
     public int getWaitCount(String hallguid){
    	 String sql = " select count(1) as total from audit_queue where hallguid= '"+hallguid+"' and status='0' and  date(GETNOTIME) = curdate() ";
    	 return commonDaoFrom.queryInt(sql, AuditQueue.class, hallguid);
     }
     
     public int getTotalWaitCount(String centerguid){
    	 String sql = " select count(1) as total from AUDIT_QUEUE where centerguid= '"+centerguid+"' and status='0'  and  date(GETNOTIME) = curdate() ";
    	 return commonDaoFrom.queryInt(sql, AuditQueue.class, centerguid);
     }
     
     public int getTotalQueueCount(String centerguid){
    	 String sql = " select count(1) as total from AUDIT_QUEUE where centerguid= '"+centerguid+"'  and  date(GETNOTIME) = curdate() ";
    	 return commonDaoFrom.queryInt(sql, AuditQueue.class, centerguid);
     }
     
     
}
