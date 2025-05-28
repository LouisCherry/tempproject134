package com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.inter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.domain.ZCAuditZnsbSelfmachinemodule;

/**
 * 自助服务一体机模块配置对应的后台service接口
 * 
 * @author 54201
 * @version [版本号, 2018-06-14 15:50:54]
 */
public interface IZCAuditZnsbSelfmachinemoduleService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            
     * @return int
     */
    public AuditCommonResult<String> insert(ZCAuditZnsbSelfmachinemodule record);

    /**
     * 删除数据
     * 
     * @param guid
     *            
     * @return int
     */
    public AuditCommonResult<String> deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            
     * @return int
     */
    public AuditCommonResult<String> update(ZCAuditZnsbSelfmachinemodule record);

    /**
     * 根据ID查找单个实体
     * 
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditCommonResult<ZCAuditZnsbSelfmachinemodule> find(Object primaryKey);

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditCommonResult<ZCAuditZnsbSelfmachinemodule> find(String sql,Object... args);

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> findList(String sql, int pageNumber, int pageSize,Object... args);
    
    
    /**
     * 分页查询列表
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param conditionMap
     *  @param first
     *  @param pageSize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<PageData<ZCAuditZnsbSelfmachinemodule>> getListByPage(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder);
    
    
    /**
     * 获取列表
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<PageData<ZCAuditZnsbSelfmachinemodule>> getSelfmachinemoduleByPage(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder);
    
    /**
     * 
     *  根据moduleType和centerguid查找对应type的模块数量
     *  @param moduleType 模块类型
     *  @param centerguid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Integer> getCountByModuleType(String moduleType, String centerguid);
    
    /**
     * 
     *  根据moduleType和centerguid和moduleconfigtype查找对应type的模块数量
     *  @param moduleType 模块类型
     *  @param centerguid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Integer> getCountByModuleTypeAndConfig(String moduleType, String centerguid, String moduleconfigtype);
    
    /**
     * 
     *  根据moduleType和centerguid查找对应type的列表
     *  @param moduleType 模块类型
     *  @param centerguid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModuleType(String moduleType, String centerguid);
    
    /**
     * 
     *  根据moduleType和centerguid和moduleconfigtype查找对应type的列表
     *  @param moduleType 模块类型
     *  @param centerguid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModuleTypeAndConfig(String moduleType, String centerguid,String moduleconfigtype);
    
    /**
     * 
     *  根据moduleType和centerguid查找对应type的列表(没有父模块guid，为第二层模块)
     *  @param moduleType 模块类型
     *  @param centerguid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getNoParentModuleGuidModuleListByModuleType(String moduleType, String centerguid);
    
    /**
     * 根据CenterGuid删除
     * @param CenterGuid
     * @return
     */
    public AuditCommonResult<String> deletebyCenterGuid(String CenterGuid);
    
    /**
     * 根据CenterGuid和moduleconfigtype删除
     * @param CenterGuid
     * @return
     */
    public AuditCommonResult<String> deletebyCenterAndConfig(String CenterGuid,String moduleconfigtype);
    
    /**
     * 根据CenterGuid初始化模块数据
     * @param operateusername 操作者名字
     * @param CenterGuid
     * @return
     */
    public AuditCommonResult<String> initModule(String operateusername,String CenterGuid,String moduleconfigtype);

    /**
     * 根据CenterGuid获取模块
     * @param centerguid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByCenterguid(String centerguid);
    
    /**
     * 根据CenterGuid和moduleconfigtype获取模块
     * @param centerguid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByCenterguid(String centerguid,String moduleconfigtype);

    /**
     * 
     *  根据Macaddress,CenterGuid获取模块
     *  @param macaddress
     *  @param centerguid
     *  @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByMacaddress(String macaddress, String centerguid);
    
    /**
     * 
     *  根据Macaddress,CenterGuid和moduleconfigtype获取模块
     *  @param macaddress
     *  @param centerguid
     *  @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByMacAndType(String macaddress, String centerguid, String moduleconfigtype);
    
    /**
     * 
     *  根据Macaddress,CenterGuid,Modulename获取模块
     *  @param macaddress mac地址
     *  @param centerguid 中心guid
     *  @param modulename 模块名称
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByMacaddressAndName(String macaddress, String centerguid,String modulename);
    
    /**
     * 
     * 根据CenterGuid,Modulename获取模块
     *  @param centerguid
     *  @param modulename
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByName( String centerguid,String modulename);


    /**
     * 
     *  根据moduleType和centerguid,macaddress查找对应type的模块数量
     *  @param moduleType
     *  @param centerguid
     *  @param macaddress
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Integer> getCountByModuleTypeAndMac(String moduleType, String centerguid, String macaddress);
    
    /**
     * 
     *  根据moduleType和centerguid,macaddress和moduleconfigtype查找对应type的模块数量
     *  @param moduleType
     *  @param centerguid
     *  @param macaddress
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Integer> getCountByModuleTypeAndMac(String moduleType, String centerguid, String macaddress, String moduleconfigtype);

   /**
    * 
    *  [查找对应查询条件的模块数量] 
    *  @param centerguid 中心guid
    *  @param macaddress 设备地址
    *  @param moduleconfigtype  
    *  @param modulename 查询名称
    *  @return    
    * @exception/throws [违例类型] [违例说明]
    * @see [类、类#方法、类#成员]
    */
    public AuditCommonResult<Integer> getCountByModuleName( String centerguid, String macaddress, String moduleconfigtype, String modulename);
    
    /**
     * 
     *  根据moduleType和centerguid,macaddress查找对应type的列表
     *  @param moduleType
     *  @param centerguid
     *  @param macaddress
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModuleTypeAndMac(String moduleType,
            String centerguid, String macaddress);
    
    /**
     * 
     *  根据moduleType和centerguid,macaddress和moduleconfigtype查找对应type的列表
     *  @param moduleType
     *  @param centerguid
     *  @param macaddress
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModuleTypeAndMac(String moduleType,
            String centerguid, String macaddress, String moduleconfigtype);

    /**
     * 
     *  根据Macaddress删除
     *  @param macaddress
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<String> deletebyMacaddress(String macaddress);
    
    /**
     * 
     *  根据Macaddress和moduleconfigtype删除
     *  @param macaddress
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<String> deletebyMacAndConfigtype(String macaddress,String moduleconfigtype);

    /**
     * 
     *  根据CenterGuid,Macaddress拷贝初始化模块数据
     *  @param operateusername 操作者名字
     *  @param centerGuid
     *  @param macaddress
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<String> copyModule(String operateusername, String centerGuid, String macaddress);
    
    /**
     * 
     *  根据CenterGuid,Macaddress拷贝初始化模块数据
     *  @param operateusername 操作者名字
     *  @param centerGuid
     *  @param macaddress
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<String> copyModule(String operateusername, String centerGuid, String macaddress, String moduleconfigtype);
    
    /**
     * 
     *  根据centerguid,macaddress查找热度排序的列表(自行判断是否需要个性化macaddress)
     *  @param centerguid
     *  @param macaddress
     *  @param modulecount 查询的数量(前几名)
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getHotModuleList(String centerguid, String macaddress,int modulecount);
    
    /**
     * 
     *  根据macaddress,modulename查找模块对象(不包含未个性化的数据)
     *  @param macaddress
     *  @param modulename
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<ZCAuditZnsbSelfmachinemodule> getModuleByModulenameAndMac( String macaddress,String modulename);
    
    /**
     * 
     *  根据centerguid,modulename查找模块对象(仅返回个性化的数据)
     *  @param centerguid
     *  @param modulename
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<ZCAuditZnsbSelfmachinemodule> getModuleByModulenameAndCenterguid( String centerguid,String modulename);

    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     * @param Macaddress,CenterGuid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModulecodelevel(String modulecodelevel,String CenterGuid);
    
    /**
     * 根据parentmoduleguid,CenterGuid获取子模块
     * @param Macaddress,CenterGuid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByParentmoduleguid(String parentmoduleguid,String CenterGuid);
    
    /**
     * 根据parentmoduleguid,CenterGuid获取子模块
     * @param Macaddress,CenterGuid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getHotModuleListByParentmoduleguid(String parentmoduleguid,String CenterGuid);
    
    /**
     * 根据parentmoduleguid,CenterGuid,mac获取子模块
     * @param Macaddress,CenterGuid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByParentmoduleguidAndMac(String parentmoduleguid,String CenterGuid,String macaddress);
    
    /**
     * 根据parentmoduleguid,CenterGuid,mac获取子模块
     * @param Macaddress,CenterGuid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getHotModuleListByParentmoduleguidAndMac(String parentmoduleguid,String CenterGuid,String macaddress);
    
    /**
     * 根据moduleconfigtype获取最上层模块列表
     * @param moduleconfigtype,CenterGuid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getParentListByConfigType(String moduleconfigtype,String centerguid);
    
    /**
     * 根据moduleconfigtype和mac地址获取最上层模块列表
     * @param moduleconfigtype,CenterGuid
     * @return
     */
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getParentListByConfigTypeAndMac(String moduleconfigtype,String centerguid,String macaddress);

}
