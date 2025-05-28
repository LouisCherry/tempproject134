package com.epoint.xmz.job.api;

import org.quartz.DisallowConcurrentExecution;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;

@DisallowConcurrentExecution
public class SpglSyncService {
	protected ICommonDao commonDao;
	protected ICommonDao qzkcommonDao;
	
	
	private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "spglurl");
	private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "spglusername");
	private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "spglpassword");

	
	DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
	
	// 初始化数据源
	public SpglSyncService() {
		/**
		 * 数据库操作DAO
		 */
		commonDao = CommonDao.getInstance();

		qzkcommonDao = CommonDao.getInstance(dataSourceConfig);
	}

	/**
	 * 
	 * 新增某条记录
	 * 
	 * @param baseClass
	 * @param record
	 */
	public void insert(Record record) {
		qzkcommonDao.insert(record);
	}

	/**
	 * 修改某条记录
	 * 
	 * @param baseClass
	 * @param record
	 */
	public void update(Record record) {
		qzkcommonDao.update(record);
	}
	
	
	/**
	 * 修改某条记录ue
	 * 
	 * @param baseClass
	 * @param record
	 */
	public AuditProjectFormSgxkz getSgxkzInfoByRowguid(String rowguid) {
		String sql = "select  * from audit_project_form_sgxkz where 1=1  and rowguid=? ";
		return commonDao.find(sql, AuditProjectFormSgxkz.class, rowguid);
	}
	
	public AuditProjectFormSgxkzDanti getSgxkzDantiInfoByRowguid(String rowguid) {
		String sql = "select  * from audit_project_form_sgxkz_danti where 1=1  and rowguid=? ";
		return commonDao.find(sql, AuditProjectFormSgxkzDanti.class, rowguid);
	}
	
	public int updateBaseInfo( String sync, String rowguid) {
    	String sql = "update audit_project_form_sgxkz set sync = '"+sync+"' where rowguid = '"+rowguid+"'";
        int result = commonDao.execute(sql);
        return result;
    }
	
	public int updateBaseInfoXk( String sync, String rowguid) {
    	String sql = "update audit_project_form_sgxkz set xmxksync = '"+sync+"' where rowguid = '"+rowguid+"'";
        int result = commonDao.execute(sql);
        return result;
    }
	
	public int updateBaseInfoDanti( String sync, String rowguid) {
    	String sql = "update audit_project_form_sgxkz_danti set sync = '"+sync+"' where rowguid = '"+rowguid+"'";
        int result = commonDao.execute(sql);
        return result;
    }
	
	public Record getProjectBasicInfoByRowguid( String id) {
    	String sql = "select * from Project_Basic_Info where id = ?";
        return qzkcommonDao.find(sql, Record.class, id);
    }
	
	public Record getBaseInfoDantiByRowguid( String id) {
    	String sql = "select * from Project_Single_Info where id = ?";
        return qzkcommonDao.find(sql, Record.class, id);
    }
	
	public Record getBaseInfoXkByRowguid( String id) {
    	String sql = "select * from Project_Builder_Licence_Info where id = ?";
        return qzkcommonDao.find(sql, Record.class, id);
    }
	

}
