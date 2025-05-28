package com.epoint.xmz.spgl.impl;

import java.util.List;

import com.epoint.auditproject.monitorsupervise.api.auditprojectold;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;


public class PushSpglXmspsxpfwService
{
	  /**
     * 数据增删改查组件
     */
    protected ICommonDao commonDao;
    
	
	
    
    
    /**
     * 前置库数据源
     */
    
    public PushSpglXmspsxpfwService() {
    	commonDao = CommonDao.getInstance();
    }
   
   
	
	
	/**
	 * 修改某条记录ue
	 * 
	 * @param baseClass
	 * @param record
	 */
	public List<AuditProject> findProjectList() {
		String sql = "select rowguid,OfficalDocGuid,certrowguid,Subappguid,Flowsn,banjiedate from audit_project where status = '90' and biguid is not null and (OfficalDocGuid is not null or certrowguid is not null) and ifnull(pfwj_sync,0) = 0 limit 50";
		return commonDao.findList(sql, AuditProject.class);
	}
	
	public List<AuditProject> findYthProjectList() {
		String sql = " select rowguid,Projectname,Subappguid,Ouname,Certrowguid,Areacode from audit_project where status = '90' and  subappguid is not null  and projectname like '建筑工程施工许可证核发%' or projectname like '房屋建筑工程和市政基础设施工程竣工验收备案%' limit 50";
		return commonDao.findList(sql, AuditProject.class);
	}
	
	public List<auditprojectold> findOldYthProjectList() {
		String sql = " select rowguid,Projectname,Subappguid,Ouname,Certrowguid,Areacode from audit_project_old where status = '90' and  subappguid is not null  and projectname like '建筑工程施工许可证核发%' or projectname like '房屋建筑工程和市政基础设施工程竣工验收备案%' limit 50";
		return commonDao.findList(sql, auditprojectold.class);
	}
	
	
	    

}
