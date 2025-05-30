package com.epoint.xmz.auditspyyyzmaterial.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.xm.similarity.util.StringUtil;

import com.epoint.core.grammar.Record;
import com.epoint.xmz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;
import com.epoint.core.BaseEntity;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;

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
    /**
     * 根据所选情形，获取情形所对应的材料
     *  [一句话功能简述] 
     *  @param selectIds
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpYyyzMaterial> getYyyzMaterialListBySelectIds(String ids) {
        String sql = "select * from audit_sp_yyyz_material asm join audit_sp_option_material_relation aso on asm.material_id = aso.material_id where aso.choice_id in('"+ids+"') and asm.claim_guide_id <> '0'";
        List<AuditSpYyyzMaterial> auditSpYyyzMaterialList = baseDao.findList(sql, AuditSpYyyzMaterial.class);
        return auditSpYyyzMaterialList;
    }
    /**
     * 根据情形决定的材料获取该材料下获取的事项信息
     *  [一句话功能简述] 
     *  @param claimGuidIds
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getAuditTaskListByClaimGuidIds(String claimGuidIds) {
        String sql = "select distinct taskname,task_id,New_ITEM_CODE from audit_task at where New_ITEM_CODE in('"+claimGuidIds+"') and ifnull(is_history,0) = 0  and IS_ENABLE ='1'";
        List<AuditTask> auditTaskList = baseDao.findList(sql, AuditTask.class);
        return auditTaskList;
    }
    
    /**
     * 根据事项唯一标识和辖区编码获取实体信息
     *  [一句话功能简述] 
     *  @param task_id
     *  @param areaCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditSpBasetaskR getAuditSpBasetaskRBytaskidAndareaCode(String task_id, String areaCode) {
        String sql = "select * from audit_sp_basetask_r where taskid = ?1 and areaCode = ?2";
        AuditSpBasetaskR auditSpBasetaskR = baseDao.find(sql, AuditSpBasetaskR.class, task_id, areaCode);
        return auditSpBasetaskR;
    }
    /**
     * 
     *  [通过获取的一链办理主题材料id查找事项库的事项] 
     *  @param materialId
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditSpYyyzMaterial getYyyzMaterialByMaterialId(String materialId) {
        String sql = "select * from audit_sp_yyyz_material where material_id = ?1";
        AuditSpYyyzMaterial auditSpYyyzMaterial = baseDao.find(sql,AuditSpYyyzMaterial.class, materialId);
        return auditSpYyyzMaterial;
    }
    /**
     * 
     *  [根据事项实例标识获取该泰安事项表事项的信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getTaianInfoByTaskId(String taskid) {
        String sql = "select * from audit_task_taian where task_id = ?";
        return baseDao.find(sql, Record.class,taskid);
    }
    /**
     * 
     *  [根据事项名称查询所有同事项名的再用事项] 
     *  @param taskName
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getTaskListByTaskName(String taskName) {
        String sql = "select task_id,areacode,taskname from AUDIT_TASK where taskName in ("+taskName+")and ifnull(is_history,0) = 0  and IS_ENABLE ='1' and rowguid not in(select taskguid from audit_task_delegate group by taskguid) and new_item_code is not null";
        return baseDao.findList(sql, AuditTask.class, taskName);
    }
    /**
     * 
     *  [通过获取到的国脉绑定的事项guid获取其表单源码] 
     *  @param materialGuids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpYyyzMaterial> getYyyzMaterialByRowguids(String materialGuids) {
        String sql = "select rowguid,ylbl_html_detail,material_id,materialname from audit_sp_yyyz_material where rowguid in("+materialGuids+")";
        return baseDao.findList(sql, AuditSpYyyzMaterial.class, materialGuids);
    }
    /**
     * 
     *  [通过事项标识获取事项表中再用事项] 
     *  @param taskids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getAuditTaskListByTaskIds(String taskids) {
        String sql = "select task_id,areacode,taskname from AUDIT_TASK where task_id in ('"+taskids+"')and ifnull(is_history,0) = 0  and IS_ENABLE ='1' and new_item_code is not null";
        return baseDao.findList(sql,AuditTask.class, taskids);
    }
    /**
     * 
     *  [通过yyyz材料表的rowguid查询材料信息数据] 
     *  @param materialid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditSpYyyzMaterial getSpYyyzMaterialByMaterialId(String materialid) {
        String sql = "select file_source,submittype,ordernumber from audit_sp_yyyz_material where rowguid = ?1";
        return baseDao.find(sql,AuditSpYyyzMaterial.class,materialid);
    }
    
    public List<Record> getItemValuesByIds(String materialids,String itemIds) {
        String sql = "";
        if(StringUtil.isBlank(itemIds)) {
            sql = "SELECT " + 
                    "    mmi.material_id, " + 
                    "    mmi.item_id, " + 
                    "    mmi.parent_item_id, " + 
                    "    msi.origin_id, " + 
                    "    msi.item_name, " + 
                    "    msi.is_required, " + 
                    "  msi.item_category, " + 
                    "  msi.data_value " + 
                    "FROM " + 
                    "    mat_theme_material_item mmi " + 
                    "JOIN  " + 
                    "  mat_theme_standard_item msi " + 
                    "ON mmi.item_id = msi.id " + 
                    "WHERE " + 
                    "    mmi.material_id IN ('"+materialids+"') " + 
                    "AND mmi.parent_item_id = '0' " + 
                    "AND mmi.del_flag = '0' " + 
                    "ORDER BY " + 
                    "    mmi.material_id,mmi.parent_item_id,mmi.sort asc";
        }
        else {
            sql = "SELECT " + 
                    "    mmi.material_id, " + 
                    "    mmi.item_id, " + 
                    "    mmi.parent_item_id, " + 
                    "    msi.origin_id, " + 
                    "    msi.item_name, " + 
                    "    msi.is_required, " + 
                    "  msi.item_category, " + 
                    "  msi.data_value " + 
                    "FROM " + 
                    "    mat_theme_material_item mmi " + 
                    "JOIN  " + 
                    "  mat_theme_standard_item msi " + 
                    "ON mmi.item_id = msi.id " + 
                    "WHERE " + 
                    "    mmi.material_id IN ('"+materialids+"') " + 
                    "AND mmi.parent_item_id in ('"+itemIds+"') " + 
                    "AND mmi.del_flag = '0' " + 
                    "ORDER BY " + 
                    "    mmi.material_id,mmi.parent_item_id,mmi.sort asc";
        }
        return baseDao.findList(sql,Record.class);
    }
}
