package com.epoint.synctask;

import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 事项同步服务
 * 
 * @author xbn
 * @version 
 */
public class EpointSyncMaterialService
{

    transient  Logger log = LogUtil.getLog(EpointSyncMaterialService.class);

    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected  ICommonDao commonDaoTo;
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "qzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "qzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "qzpassword");
    
    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public EpointSyncMaterialService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public String getQLSXnew(String inner_code) {
        String sql = "select MATERIAL_INFO from GTQZK.QLT_QLSX_NEW where dh_state=1 and inner_code ='"+inner_code+"'";
        String MATERIAL_INFO = commonDaoFrom.queryString(sql);
        commonDaoFrom.close();
        return MATERIAL_INFO;
    }
    
    public List<Record> findMaterialList(String areacode){
    	String sql = "select m.PAGE_NUM,m.COPY_NUM,t.INNER_CODE,m.TASKGUID "
    			+ "from audit_task_material m RIGHT JOIN audit_task t on m.TASKGUID = t.RowGuid "
    			+ "where t.IS_ENABLE=1 and t.IS_EDITAFTERIMPORT=1 and IFNULL(IS_HISTORY,0)=0 " 
    			+ "and m.COPY_NUM is null and MATERIALID is not null and INNER_CODE is not null "
    			+ (StringUtil.isBlank(areacode) ? "" : "and t.AREACODE='"+areacode+"' ")
    			+ "GROUP BY INNER_CODE LIMIT 0,1000 ";
    	List<Record> list = commonDaoTo.findList(sql, Record.class);
    	return list;
    }
    
    public int updateMaterial(String code,int copy,int page) {
    	String sql = "update audit_task_material set COPY_NUM ="+copy+ ",PAGE_NUM ="+page+" where MATERIALID='"+ code +"'";
    	return commonDaoTo.execute(sql);
    }
    
    public int updateMaterialByTaskguid(String code,int copy,int page) {
    	String sql = "update audit_task_material set COPY_NUM ="+copy+ ",PAGE_NUM ="+page+" where TASKGUID='"+ code +"'";
    	return commonDaoTo.execute(sql);
    }

}
