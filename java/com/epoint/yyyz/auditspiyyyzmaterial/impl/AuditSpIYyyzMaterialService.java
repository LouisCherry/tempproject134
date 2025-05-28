package com.epoint.yyyz.auditspiyyyzmaterial.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.BaseEntity;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.yyyz.auditspiyyyzmaterial.api.entity.AuditSpIYyyzMaterial;
import com.epoint.yyyz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;

/**
 * 一业一证主题材料实例表对应的后台service
 * 
 * @author LYA
 * @version [版本号, 2020-06-20 19:20:32]
 */
public class AuditSpIYyyzMaterialService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpIYyyzMaterialService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpIYyyzMaterial record) {
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
        T t = baseDao.find(AuditSpIYyyzMaterial.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpIYyyzMaterial record) {
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
    public AuditSpIYyyzMaterial find(Object primaryKey) {
        return baseDao.find(AuditSpIYyyzMaterial.class, primaryKey);
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
    public AuditSpIYyyzMaterial find(String sql,  Object... args) {
        return baseDao.find(sql, AuditSpIYyyzMaterial.class, args);
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
    public List<AuditSpIYyyzMaterial> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditSpIYyyzMaterial.class, args);
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
    public List<AuditSpIYyyzMaterial> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditSpIYyyzMaterial.class, args);
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
    public Integer countAuditSpIYyyzMaterial(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    public List<AuditSpIYyyzMaterial> initTcSubappMaterial(String subappGuid, String businessGuid, String biGuid, String phaseGuid,
            List<AuditSpYyyzMaterial> auditSpYyyzMaterialList) {
        for (AuditSpYyyzMaterial auditSpYyyzMaterial : auditSpYyyzMaterialList) {
            AuditSpIYyyzMaterial dataBean = new AuditSpIYyyzMaterial();
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setBusinessguid(businessGuid);
            dataBean.setBiguid(biGuid);
            dataBean.setSubappguid(subappGuid);
            dataBean.setPhaseguid(phaseGuid);
            dataBean.setNecessity(auditSpYyyzMaterial.getNecessity());
            dataBean.setAllowrongque(auditSpYyyzMaterial.getRongque());
            if(StringUtil.isBlank(auditSpYyyzMaterial.getTask_id()) || auditSpYyyzMaterial.getTask_id() == null) {
                dataBean.setShared("1");
            }else {
                dataBean.setShared("0");
                dataBean.setTaks_id(auditSpYyyzMaterial.getTask_id());
            }
            dataBean.setResult("0");
            dataBean.setStatus("10");
            dataBean.setSubmittype(auditSpYyyzMaterial.getSubmittype());
            dataBean.setCliengguid(UUID.randomUUID().toString());
            dataBean.setMaterialguid(auditSpYyyzMaterial.getRowguid());
            dataBean.setMaterialname(auditSpYyyzMaterial.getMaterialname());
            dataBean.set("yyyztype", auditSpYyyzMaterial.getStr("yyyztype"));
            if(auditSpYyyzMaterial.getOrdernumber() != null) {
                dataBean.setOrdernum(auditSpYyyzMaterial.getOrdernumber());
            }
            this.insert(dataBean);
        }
        String sql = "select * from audit_sp_i_yyyz_material where SUBAPPGUID = ?1 and BUSINESSGUID =?2 and BIGUID = ?3 and PHASEGUID = ?4";
        return this.findList(sql, subappGuid,businessGuid,biGuid,phaseGuid);
    }
    
    /**
     * 
     *  [根据子申报唯一标识查询一业一证主题材料实例集合] 
     *  @param subappGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpIYyyzMaterial> getSpIMaterialBySubappGuid(String subappGuid) {
        String sql = "select * from audit_sp_i_yyyz_material where SUBAPPGUID = ?1";
        return this.findList(sql, subappGuid);
    }
    
    /**
     * 
     *  [根据主题标识和勾选事项的task_id获取通用材料和个性化材料] 
     *  @param businessGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpIYyyzMaterial> getYyyzMaterialByTaskId(String biGuid, String taskid) {
        String sql = "";
        if(StringUtil.isNotBlank(taskid)) {
            taskid = "'"+ taskid + "'";
            sql = "select * from audit_sp_i_yyyz_material where (biguid =?1 and task_id is null) or (biguid = ?1 and task_id in (" +taskid+ "))";
        }else {
            sql = "select * from audit_sp_i_yyyz_material where biguid =?1 and task_id is null ";
        }
        return this.findList(sql, biGuid);
    }
    
    /**
     * 
     *  [根据一业一证主题材料实例标识，修改提交状态] 
     *  @param materialInstanceGuid
     *  @param i    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateMaterialStatus(String materialInstanceGuid, int i) {
        AuditSpIYyyzMaterial auditSpIYyyzMaterial = this.find(materialInstanceGuid);
        Integer status = Integer.parseInt(auditSpIYyyzMaterial.getStatus()) + i;
        auditSpIYyyzMaterial.setStatus(status.toString());
        this.update(auditSpIYyyzMaterial);
    }
    /**
     * 
     *  [根据主题实例唯一标识标识查询一业一证主题材料实例集合] 
     *  @param subappGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpIYyyzMaterial> getSpIMaterialBybiGUid(String biGuid) {
        String sql = "select * from audit_sp_i_yyyz_material where biguid = ?1";
        return this.findList(sql, biGuid);
    }
    
    public List<AuditSpIYyyzMaterial> findSpIListByBiGuidAndtaskId(String biGuid, String taskid) {
        String sql = "select * from audit_sp_i_yyyz_material where biguid = ?1 and task_id=?2";
        return baseDao.findList(sql, AuditSpIYyyzMaterial.class,biGuid,taskid);
    }
    
    public List<Record> findYlblFormListByBiGuidAndMaterialId(String biGuid, String materialGuid) {
        String sql = "select * from ylbl_business_from_relation where biguid = ?1 and materialid in ('"+materialGuid+"')";
        return baseDao.findList(sql, Record.class,biGuid);
    }
    
    
}
