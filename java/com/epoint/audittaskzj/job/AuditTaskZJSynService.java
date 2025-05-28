package com.epoint.audittaskzj.job;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;

import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

@DisallowConcurrentExecution
public class AuditTaskZJSynService 
{
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkname");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkpassword");
    
    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDao;
    
    protected ICommonDao qzkcommonDao;

    public AuditTaskZJSynService(){
        commonDao = CommonDao.getInstance();
        qzkcommonDao = CommonDao.getInstance(dataSourceConfig);
    }

    /**
     * 
     *  查询需要同步的记录 
     *  @param baseClass
     *  @return    
     */
    @SuppressWarnings("unchecked")
    public <T>List<T> selectNeedsync(Class<? extends BaseEntity> baseClass){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "select * from "+table+" where sjsczt <> '-1' and (sync = 0 or sync is null)";
        List<T> records= (List<T>) commonDao.findList(sql,0,50, baseClass);
        commonDao.close();
        return records;
    }
    
    
    @SuppressWarnings("unchecked")
    public <T>List<T> selectNeedsyncSpjd(Class<? extends BaseEntity> baseClass){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "select * from "+table+" where sjsczt <> '-1' and (sync = 0 or sync is null)";
        List<T> records=  (List<T>) commonDao.findList(sql,0,500, baseClass);
        commonDao.close();
        return records;
    }

    /**
     * 
     *  查询需要同步的记录 
     *  @param baseClass
     *  @return    
     */
    @SuppressWarnings("unchecked")
    public <T>List<T> selectNeedAqbjsjsync(Class<? extends BaseEntity> baseClass){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "select * from "+table+"  where sjsczt <> '-1' and (sync = 0 or sync is null)";
        List<T> records=  (List<T>) commonDao.findList(sql,0,20, baseClass);
        commonDao.close();
        return records;
    }
    
    
    /**
     * 
     * 新增某条记录
     * 
     * @param baseClass
     * @param record
     * @param useCache
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            qzkcommonDao.insert(record);
            qzkcommonDao.close();
        }
    }
    
    /**
     *  修改状态
     *  @param baseClass
     *  @param rowguid
     *  @param status    
     */
    public void updateSync(Class<? extends BaseEntity> baseClass,String rowguid,String status){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "update "+table+" set sync=? where rowguid = ?";
        commonDao.execute(sql,status,rowguid);
        commonDao.close();
    }

   public List<Record> selectNeedsyncErr(Class<? extends BaseEntity> baseClass){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "select * from "+table+" where SJSCZT = 2";
        List<Record> records=  qzkcommonDao.findList(sql,0,20, Record.class);
        qzkcommonDao.close();
        return records;
    }
   
   public void updateSyncErrStatus(Class<? extends BaseEntity> baseClass,String lsh,String status){
       if(ZwfwConstant.CONSTANT_STR_THREE.equals(status)){
           status = "99";
       }
       Entity en = baseClass.getAnnotation(Entity.class);
       String table = en.table();
       String sql = "update "+table+" set SJSCZT=?  where lsh = ?";
       qzkcommonDao.execute(sql,status, lsh);
       qzkcommonDao.close();
   }

    
    
    
    /**
     * 
     *  查询前置库同步时失败的记录
     *  @param baseClass
     *  @param rowguid
     *  @param status    
     */
    public void updateSyncForErr(Class<? extends BaseEntity> baseClass,String fieldname,String fieldvalue){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "update "+table+" set sync=2 where "+fieldname+"=?";        
        commonDao.execute(sql,fieldvalue);
        commonDao.close();
    }
    
    /**
     * 
     *  查询前置库同步时失败的记录
     *  @param baseClass
     *  @param rowguid
     *  @param status    
     */
    public void updateSyncForErr(Class<? extends BaseEntity> baseClass,String fieldname,String fieldvalue,String filedname1,String fieldvalue1){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "update "+table+" set sync=2 where "+fieldname+"=?"+" and "+filedname1+"=?";        
        commonDao.execute(sql,fieldvalue,fieldvalue1);
        commonDao.close();
    }
    
    /**
     * 
     *  查询前置库同步时失败的记录
     *  @param baseClass
     *  @param rowguid
     *  @param status    
     */
    public void updateSyncForErr(Class<? extends BaseEntity> baseClass,String fieldname,String fieldvalue,String filedname1,String fieldvalue1,String filedname2,String fieldvalue2){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "update "+table+" set sync=2 where "+fieldname+"=?"+" and "+filedname1+"=?"+" and "+filedname2 +"=?";        
        commonDao.execute(sql,fieldvalue,fieldvalue1,fieldvalue2);
        commonDao.close();
    }
    
    /**
     * 
     *  查询前置库同步时失败的记录
     *  @param baseClass
     *  @param rowguid
     *  @param status    
     */
    public void updateSyncQqyjForErr(Class<? extends BaseEntity> baseClass,String fieldname,String fieldvalue){
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "update "+table+" set sync=2 where "+fieldname+"=?";        
        commonDao.execute(sql,fieldvalue);
        commonDao.close();
    }
    
   
}
