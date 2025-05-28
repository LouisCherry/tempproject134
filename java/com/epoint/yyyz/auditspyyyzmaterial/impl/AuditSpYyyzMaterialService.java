package com.epoint.yyyz.auditspyyyzmaterial.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.core.BaseEntity;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.yyyz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;

/**
 * 一业一证专项材料表对应的后台service
 * 
 * @author LYA
 * @version [版本号, 2020-06-18 21:34:52]
 */
public class AuditSpYyyzMaterialService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpYyyzMaterialService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpYyyzMaterial record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditSpYyyzMaterial.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpYyyzMaterial record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditSpYyyzMaterial find(Object primaryKey) {
        return baseDao.find(AuditSpYyyzMaterial.class, primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpYyyzMaterial find(String sql,  Object... args) {
        return baseDao.find(sql, AuditSpYyyzMaterial.class, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpYyyzMaterial> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditSpYyyzMaterial.class, args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpYyyzMaterial> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditSpYyyzMaterial.class, args);
    }

	/**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countAuditSpYyyzMaterial(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    /**
     * 
     *  [根据条件查询主题下的专项材料] 
     *  @param array
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int findCountByCondition(String condition,Object[] array) {
        String sql = "select count(*) from audit_sp_yyyz_material where 1 = 1 " +condition;
        return baseDao.queryInt(sql, array);
    }
    /**
     * 
     *  [根据主题标识获取主题配置下的所有事项] 
     *  @param businessGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpYyyzMaterial> findListBybusinessGuid(String businessGuid) {
        String sql = "select * from audit_sp_yyyz_material where businessguid = ?";
        return this.findList(sql, businessGuid);
    }
    /**
     * 
     *  [根据勾选专项事项的taskid获取事项材料信息集合，没有只显示通用事项] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpYyyzMaterial> getYyyzMaterialByTaskId(String taskid) {
        String taskids = "'"+taskid+"'";
        String sql ="select * from audit_sp_yyyz_material where (task_id is null or task_id = '') or (task_id in ("+taskids+"))";
        return this.findList(sql);
    }
    /**
     * 
     *  [通过biguid获取其主题事项实例表中绑定办件编号的实例] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpITask> getSpITaskByBIGuid(String biGuid) {
        String sql = "select * from audit_sp_i_task where biguid = ? and projectguid is not null";
        return baseDao.findList(sql, AuditSpITask.class, biGuid);
    }
    /**
     * 根据主题实例唯一标识和taskguid获取办件信息
     *  [一句话功能简述] 
     *  @param biGuid
     *  @param taskguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditProject> getProjectListByBiGuidAndTaskguids(String biGuid, String taskguid) {
        String taskguids = "'" + taskguid +"'"; 
        String sql = "select * from audit_project where BIGUID =? and taskguid in ("+taskguids+") ";
        return baseDao.findList(sql, AuditProject.class, biGuid);
    }
    public void updateFlowsnByProjectguid(String newFlowsn, String projectGuid) {
        String sql = "update audit_project set flowsn = ?1 where rowguid = ?2";
        baseDao.execute(sql, newFlowsn,projectGuid);
        
    }
    public List<Record> getAuditTaskByCommonTask(String condition) {
        String sql = "select atk.OUGUID from audit_task atk join audit_task_taian att on atk.TASK_ID = att.task_id  where att.isBusNCommonTask = ?1 and ifnull(atk.is_history,0) = 0  and atk.IS_ENABLE ='1' and atk.IS_EDITAFTERIMPORT ='1' group by atk.ouguid";
        return baseDao.findList(sql, Record.class, condition);
    }
    public List<FrameOu> getCommonFrameOuByOuguids(String ouguids) {
        String sql = "select ouguid,ouname,parentouguid,baseouguid,oucode,oushortname from frame_ou where ouguid in ('"+ouguids+"')";
        return baseDao.findList(sql, FrameOu.class,ouguids);
    }
    public List<AuditTask> selectAuditTaskOuByObjectGuid(String ouguid, String areacodes) {
        String sql = "select ROWGUID,TASKNAME,Task_id,Item_id,areacode,yw_catalog_id from AUDIT_TASK where OUGUID=?1 and( IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1  and IS_ENABLE=1 and ISTEMPLATE=0 and areacode in ('"+areacodes+"') order by item_id asc,ordernum desc";
        return baseDao.findList(sql, AuditTask.class,ouguid, areacodes);
    }
    public List<AuditTask> selectAuditTaskOuByObjectGuids(String ouguid, String areacodes) {
        String sql = "select atk.ROWGUID,atk.TASKNAME,atk.Task_id,atk.Item_id,atk.areacode,atk.yw_catalog_id from AUDIT_TASK atk join audit_task_taian att on atk.TASK_ID = att.task_id where atk.OUGUID=?1 and (atk.IS_HISTORY=0 or atk.IS_HISTORY is null) and atk.IS_EDITAFTERIMPORT=1 and att.isBusNCommonTask = '1'  and atk.IS_ENABLE=1 and atk.ISTEMPLATE=0 and atk.areacode=?2 order by atk.item_id asc,atk.ordernum desc";
        return baseDao.findList(sql, AuditTask.class,ouguid, areacodes);
    }
    public List<AuditSpYyyzMaterial> getYyyzMaterialByRowguids(String materialGuids) {
        String sql = "select rowguid,ylbl_html_detail from audit_sp_yyyz_material where rowguid in("+materialGuids+")";
        return baseDao.findList(sql, AuditSpYyyzMaterial.class, materialGuids);
    }
    
}
