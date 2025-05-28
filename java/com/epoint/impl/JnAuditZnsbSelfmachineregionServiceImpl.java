package com.epoint.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.domain.JnAuditZnsbSelfmachineregion;
import com.epoint.inter.IJnAuditZnsbSelfmachineregion;
import com.epoint.service.JnAuditZnsbSelfmachineregionService;
/**
 * 智能化一体机区域配置对应的后台service实现类
 * 
 * @author 54201
 * @version [版本号, 2019-09-27 11:16:41]
 */
@Component
@Service
public class JnAuditZnsbSelfmachineregionServiceImpl implements IJnAuditZnsbSelfmachineregion
{
    /**
     * 
     */
    private static final long serialVersionUID = -1460676515189120943L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnAuditZnsbSelfmachineregion record) {
        return new JnAuditZnsbSelfmachineregionService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnAuditZnsbSelfmachineregionService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnAuditZnsbSelfmachineregion record) {
        return new JnAuditZnsbSelfmachineregionService().update(record);
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
    public JnAuditZnsbSelfmachineregion find(Object primaryKey) {
       return new JnAuditZnsbSelfmachineregionService().find(primaryKey);
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
    public JnAuditZnsbSelfmachineregion find(String sql, Object... args) {
        return new JnAuditZnsbSelfmachineregionService().find(args);
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
    public List<JnAuditZnsbSelfmachineregion> findList(String sql, Object... args) {
       return new JnAuditZnsbSelfmachineregionService().findList(sql,args);
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
    public List<JnAuditZnsbSelfmachineregion> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new JnAuditZnsbSelfmachineregionService().findList(sql,pageNumber,pageSize,args);
    }
    
    /**
     * 分页查找一个list
     * 
     * @param conditionMap
     *            查询语句
     * @param first
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param sortField
     *            查询范围
     * @param sortOrder
     *            排序
     * @return T extends BaseEntity
     */
    @Override
    public AuditCommonResult<PageData<JnAuditZnsbSelfmachineregion>> getRegionByPage(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder) {
        
        AuditQueueBasicService<JnAuditZnsbSelfmachineregion> auditqueueService = new AuditQueueBasicService<JnAuditZnsbSelfmachineregion>();
        AuditCommonResult<PageData<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<PageData<JnAuditZnsbSelfmachineregion>>();
        try {
            PageData<JnAuditZnsbSelfmachineregion> equipmentList = auditqueueService.getRecordPageData(JnAuditZnsbSelfmachineregion.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(equipmentList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getRegionList(){
        AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<List<JnAuditZnsbSelfmachineregion>>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getRegionList());
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }
    
    @Override
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getRegionListByLevel(String level){
        AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<List<JnAuditZnsbSelfmachineregion>>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getRegionListByLevel(level));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }
    @Override
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getRegionListByLevelAndParent(String level,String parentguid){
        AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<List<JnAuditZnsbSelfmachineregion>>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getRegionListByLevelAndParent(level,parentguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }
    
    @Override
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getParentRegionList(){
        AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<List<JnAuditZnsbSelfmachineregion>>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getParentRegionList());
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }
    
    @Override
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getParentRegionListInuse(){
        AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<List<JnAuditZnsbSelfmachineregion>>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getParentRegionListInuse());
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }
    
    @Override
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getChildRegionListByParentguid(String parentguid){
        AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<List<JnAuditZnsbSelfmachineregion>>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getChildRegionListByParentguid(parentguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }
    
    @Override
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getChildRegionListInuseByParentguid(String parentguid){
        AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<List<JnAuditZnsbSelfmachineregion>>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getChildRegionListInuseByParentguid(parentguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }

    @Override
    public AuditCommonResult<JnAuditZnsbSelfmachineregion> getRegionByRowguid(String rowguid){
        AuditCommonResult<JnAuditZnsbSelfmachineregion> result = new AuditCommonResult<JnAuditZnsbSelfmachineregion>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getRegionByRowguid(rowguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }
    
    @Override
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getAllCommonRegionList(){
        AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> result = new AuditCommonResult<List<JnAuditZnsbSelfmachineregion>>();
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        try {
            result.setResult(regionService.getAllCommonRegionList());
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }
    
    @Override
    public AuditCommonResult<String> deleteOldRegion() {
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            regionService.deleteOldRegion();
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<String> updateNewRegion() {
        JnAuditZnsbSelfmachineregionService regionService=new JnAuditZnsbSelfmachineregionService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            regionService.updateNewRegion();
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public JnAuditZnsbSelfmachineregion getRegionByCode(String areacode) {
        return new JnAuditZnsbSelfmachineregionService().getRegionByCode(areacode);
    }

    @Override
    public List<JnAuditZnsbSelfmachineregion> getAllUsedPlaceList(int currentpage, int pagesize) {
        return new JnAuditZnsbSelfmachineregionService().getAllUsedPlaceList(currentpage,pagesize);
    }

    @Override
    public int getAllUsedPlaceListNum() {
        return new JnAuditZnsbSelfmachineregionService().getAllUsedPlaceListNum();
    }

    
  


}
