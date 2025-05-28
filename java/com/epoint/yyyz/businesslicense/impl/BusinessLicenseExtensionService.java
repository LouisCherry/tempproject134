package com.epoint.yyyz.businesslicense.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 一业一证扩展信息Service
 * @description
 * @author shibin
 * @date  2020年5月19日 下午2:53:29
 */
public class BusinessLicenseExtensionService
{

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public BusinessLicenseExtensionService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(BusinessLicenseExtension record) {
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
        T t = baseDao.find(BusinessLicenseExtension.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(BusinessLicenseExtension record) {
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
    public BusinessLicenseExtension find(Object primaryKey) {
        return baseDao.find(BusinessLicenseExtension.class, primaryKey);
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
    public BusinessLicenseExtension find(String sql, Object... args) {
        return baseDao.find(sql, BusinessLicenseExtension.class, args);
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
    public List<BusinessLicenseExtension> findList(String sql, Object... args) {
        return baseDao.findList(sql, BusinessLicenseExtension.class, args);
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
    public List<BusinessLicenseExtension> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, BusinessLicenseExtension.class, args);
    }
    
    /**
     * @description
     * @author shibin
     * @date  2020年5月23日 下午2:33:11
     */
    public BusinessLicenseExtension getBusinessExtensionByBiguid(String biGuid) {
        String sql = "select e.* from businesslicense_baseinfo b INNER JOIN businesslicense_extension e on b.RowGuid = e.baseinfoGuid WHERE b.biGuid = ? ";
        return baseDao.find(sql, BusinessLicenseExtension.class, biGuid);
    }

    /**
     * 
     *  [根据主题标识获取主题下的事项标识，办件标识] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getTaskInfoByBiGuid(String biGuid){
        String sql = "select taskguid,projectguid from audit_sp_i_task where biguid = ?";
        return baseDao.findList(sql, Record.class, biGuid);
    }
    /**
     * 
     *  [根据事项标识查询获取其分配的窗口guid] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getWindowGuidByTaskGuid(String taskGuid) {
        String sql = "select windowguid from audit_orga_windowtask where taskguid = ?";
        return baseDao.findList(sql, Record.class, taskGuid);
    }

    /**
     * 
     *  [根据窗口标识查询该窗口下的相关人员标识] 
     *  @param WindowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getUserGuidByWindowGuidList(List<String> windowGuidList) {
        String windowGuids = StringUtil.join(windowGuidList,"','");
        windowGuids = "'" + windowGuids +"'";
        String sql = "select userguid from audit_orga_windowuser where windowguid in ("+windowGuids +") group by userguid" ;
        return baseDao.findList(sql, Record.class);
    }

    /**
     * 
     *  [根据主题实例标识获取主题名称] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getItemNameByBiGuid(String biGuid) {
        String sql = "select itemname from audit_sp_instance where rowguid = ?";
        return baseDao.find(sql, Record.class, biGuid);
    }

    /**
     * 
     *  [根据事项标识和办件标识获取主题实例标识] 
     *  @param guids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getBiGuidByTaskGuidAndProjectGuid(Object[] guids) {
        String sql = "select biguid from audit_sp_i_task where taskguid = ? and projectguid = ?";
        return baseDao.find(sql, Record.class, guids);
    }

    /**
     * 
     *  [根据办件标识和消息类别获取代办消息中拥有该办件标识的用户标识] 
     *  @param rowguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getMessageCenterUserGuidListByProjectGuid(String rowguid) {
        String sql ="SELECT targetuser FROM Messages_Center WHERE sendMode = 4 AND isShow = 1 AND isDel = 0 AND isNoHandle = 0 AND doneDate IS NULL AND messageType = '办理' "
                + "AND clientIdentifier = ?";
        return baseDao.findList(sql, Record.class, rowguid);
    }

    /**
     * 
     *  [根据主题实例的业务guid获取业务实例对象信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getIntegratedCompanyInfoByRowGuid(String rowguid) {
        String sql = "select * from audit_sp_integrated_company where RowGuid = ?";
        return baseDao.find(sql, Record.class, rowguid);
    }

    /**
     * 
     *  [根据办件实例获取该办件的上传材料信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getFrameAttachInfoByCliengguid(String cliengguid) {
        String sql = "select * from Frame_AttachInfo where cliengguid = ?";
        return baseDao.find(sql, Record.class, cliengguid);
    }

    /**
     * 
     *  [根据事项实例标识获取该泰安事项表事项的信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getAuditTaskTaianInfoByTaskId(String taskid) {
        String sql = "select * from audit_task_taian where task_id in "+taskid;
        
        return baseDao.findList(sql, Record.class);
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

}
