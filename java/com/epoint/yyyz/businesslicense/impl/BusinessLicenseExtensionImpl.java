package com.epoint.yyyz.businesslicense.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 一业一证扩展信息实现类 
 * @description
 * @author shibin
 * @date  2020年5月19日 下午2:53:29
 */
@Component
@Service
public class BusinessLicenseExtensionImpl implements IBusinessLicenseExtension
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(BusinessLicenseExtension record) {
        return new BusinessLicenseExtensionService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new BusinessLicenseExtensionService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(BusinessLicenseExtension record) {
        return new BusinessLicenseExtensionService().update(record);
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
        return new BusinessLicenseExtensionService().find(primaryKey);
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
        return new BusinessLicenseExtensionService().find(sql, args);
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
        return new BusinessLicenseExtensionService().findList(sql, args);
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
        return new BusinessLicenseExtensionService().findList(sql, pageNumber, pageSize, args);
    }
    
    @Override
    public BusinessLicenseExtension getBusinessExtensionByBiguid(String biGuid) {
        return new BusinessLicenseExtensionService().getBusinessExtensionByBiguid(biGuid);
    }

    /**
     * 
     *  [根据主题标识获取主题下的事项标识，办件标识] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getTaskInfoByBiGuid(String biGuid){
        return new BusinessLicenseExtensionService().getTaskInfoByBiGuid(biGuid);
    }
    /**
     * 
     *  [根据主题实例标识查询获取其下事项分配的窗口guid] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getWindowGuidByTaskGuid(String taskGuid) {
        return new BusinessLicenseExtensionService().getWindowGuidByTaskGuid(taskGuid);
    }

    /**
     * 
     *  [根据窗口标识查询该窗口下的相关人员] 
     *  @param WindowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getUserGuidByWindowGuidList(List<String> windowGuidList) {
        return new BusinessLicenseExtensionService().getUserGuidByWindowGuidList(windowGuidList);
    }

    /**
     * 
     *  [根据主题实例标识获取主题名称] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getItemNameByBiGuid(String biGuid) {
        return new BusinessLicenseExtensionService().getItemNameByBiGuid(biGuid);
    }

    /**
     * 
     *  [根据事项标识和办件标识获取主题实例标识] 
     *  @param guids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getBiGuidByTaskGuidAndProjectGuid(Object[] guids) {
        return new BusinessLicenseExtensionService().getBiGuidByTaskGuidAndProjectGuid(guids);
    }

    /**
     * 
     *  [根据办件标识和消息类别获取代办消息中拥有该办件标识的用户标识] 
     *  @param rowguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getMessageCenterUserGuidListByProjectGuid(String rowguid) {
        
        return new BusinessLicenseExtensionService().getMessageCenterUserGuidListByProjectGuid(rowguid);
    }
    
    /**
     * 
     *  [根据主题实例的业务guid获取业务实例对象信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getIntegratedCompanyInfoByRowGuid(String rowguid) {
        return new BusinessLicenseExtensionService().getIntegratedCompanyInfoByRowGuid(rowguid);
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
        return new BusinessLicenseExtensionService().getFrameAttachInfoByCliengguid(cliengguid);
    }

    /**
     * 
     *  [根据事项实例标识获取该泰安事项表事项的信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getAuditTaskTaianInfoByTaskId(String taskid) {
        return new BusinessLicenseExtensionService().getAuditTaskTaianInfoByTaskId(taskid);
    }

    
    /**
     * 
     *  [根据事项实例标识获取该泰安事项表事项的信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getTaianInfoByTaskId(String taskid) {
        return new BusinessLicenseExtensionService().getTaianInfoByTaskId(taskid);
    }

}
