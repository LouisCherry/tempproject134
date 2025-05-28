package com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.domain.ZCAuditZnsbSelfmachinemodule;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.inter.IZCAuditZnsbSelfmachinemoduleService;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.service.ZCAuditZnsbSelfmachinemoduleService;
import com.epoint.zoucheng.znsb.worktablecomment.util.QueueCommonUtil;
import com.epoint.zoucheng.znsb.worktablecomment.util.QueueConstant;
import com.esotericsoftware.minlog.Log;

/**
 * 自助服务一体机模块配置对应的后台service实现类
 * 
 * @author 54201
 * @version [版本号, 2018-06-14 15:50:54]
 */
@Component
@Service
public class ZCAuditZnsbSelfmachinemoduleServiceImpl implements IZCAuditZnsbSelfmachinemoduleService
{
    /**
     * 
     */
    private static final long serialVersionUID = 7467520549103692347L;

    /**
     * 插入数据
     * 
     * @param record
     *            
     * @return int
     */
    public AuditCommonResult<String> insert(ZCAuditZnsbSelfmachinemodule record) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            new ZCAuditZnsbSelfmachinemoduleService().insert(record);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 删除数据
     * 
     * @param record
     *            
     * @return int
     */
    public AuditCommonResult<String> deleteByGuid(String guid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            new ZCAuditZnsbSelfmachinemoduleService().deleteByGuid(guid);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 更新数据
     * 
     * @param record
     *            
     * @return int
     */
    public AuditCommonResult<String> update(ZCAuditZnsbSelfmachinemodule record) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            new ZCAuditZnsbSelfmachinemoduleService().update(record);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

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
    public AuditCommonResult<ZCAuditZnsbSelfmachinemodule> find(Object primaryKey) {
        AuditCommonResult<ZCAuditZnsbSelfmachinemodule> result = new AuditCommonResult<ZCAuditZnsbSelfmachinemodule>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().find(primaryKey));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
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
    public AuditCommonResult<ZCAuditZnsbSelfmachinemodule> find(String sql, Object... args) {
        AuditCommonResult<ZCAuditZnsbSelfmachinemodule> result = new AuditCommonResult<ZCAuditZnsbSelfmachinemodule>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().find(args));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
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
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> findList(String sql, Object... args) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findList(sql, args));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
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
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> findList(String sql, int pageNumber, int pageSize,
            Object... args) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findList(sql, pageNumber, pageSize, args));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;

    }

    @Override
    public AuditCommonResult<PageData<ZCAuditZnsbSelfmachinemodule>> getListByPage(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder) {
        AuditQueueBasicService<ZCAuditZnsbSelfmachinemodule> auditqueueService = new AuditQueueBasicService<ZCAuditZnsbSelfmachinemodule>();
        AuditCommonResult<PageData<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<PageData<ZCAuditZnsbSelfmachinemodule>>();
        try {

            PageData<ZCAuditZnsbSelfmachinemodule> equipmentList = auditqueueService.getRecordPageData(
                    ZCAuditZnsbSelfmachinemodule.class, conditionMap, first, pageSize, sortField, sortOrder);

            result.setResult(equipmentList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
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
    public AuditCommonResult<PageData<ZCAuditZnsbSelfmachinemodule>> getSelfmachinemoduleByPage(
            Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {

        AuditQueueBasicService<ZCAuditZnsbSelfmachinemodule> auditqueueService = new AuditQueueBasicService<ZCAuditZnsbSelfmachinemodule>();
        AuditCommonResult<PageData<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<PageData<ZCAuditZnsbSelfmachinemodule>>();
        try {
            PageData<ZCAuditZnsbSelfmachinemodule> equipmentList = auditqueueService.getRecordPageData(
                    ZCAuditZnsbSelfmachinemodule.class, conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(equipmentList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找对应type的模块数量
     */
    @Override
    public AuditCommonResult<Integer> getCountByModuleType(String moduleType, String centerguid) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findTypeCount(moduleType, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 查找对应type的模块数量
     */
    @Override
    public AuditCommonResult<Integer> getCountByModuleTypeAndConfig(String moduleType, String centerguid, String moduleconfigtype) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findTypeCount(moduleType, centerguid,moduleconfigtype));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找对应type的模块list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModuleType(String moduleType,
            String centerguid) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findTypeList(moduleType, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 查找对应type的模块list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModuleTypeAndConfig(String moduleType, String centerguid,String moduleconfigtype) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findTypeList(moduleType, centerguid,moduleconfigtype));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找对应type的模块list(没有父模块guid，为第二层模块)
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getNoParentModuleGuidModuleListByModuleType(
            String moduleType, String centerguid) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findNoParentModuleGuidTypeList(moduleType, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找对应type,mac的模块数量
     */
    @Override
    public AuditCommonResult<Integer> getCountByModuleTypeAndMac(String moduleType, String centerguid,
            String macaddress) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findType_MacCount(moduleType, centerguid, macaddress));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 查找对应type,mac的模块数量
     */
    @Override
    public AuditCommonResult<Integer> getCountByModuleTypeAndMac(String moduleType, String centerguid, String macaddress, String moduleconfigtype) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findType_MacCount(moduleType, centerguid, macaddress,moduleconfigtype));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找对应查询条件的模块数量
     */
    @Override
    public AuditCommonResult<Integer> getCountByModuleName( String centerguid, String macaddress, String moduleconfigtype, String modulename) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().getCountByModuleName( centerguid, macaddress,moduleconfigtype, modulename));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 查找对应type,mac的模块list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModuleTypeAndMac(String moduleType,
            String centerguid, String macaddress) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findType_MacList(moduleType, centerguid, macaddress));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 查找对应type,mac的模块list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModuleTypeAndMac(String moduleType,
            String centerguid, String macaddress, String moduleconfigtype) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findType_MacList(moduleType, centerguid, macaddress,moduleconfigtype));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找中心的list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByCenterguid(String centerguid) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findCenterList(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 查找中心的list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByCenterguid(String centerguid, String moduleconfigtype) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findCenterList(centerguid,moduleconfigtype));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找对应Mac的list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByMacaddress(String macaddress,
            String centerguid) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findMacList(macaddress, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 查找对应Mac的list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByMacAndType(String macaddress, String centerguid, String moduleconfigtype) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findMacListByConfigType(macaddress, centerguid,moduleconfigtype));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找对应Mac和name的list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByMacaddressAndName(String macaddress,
            String centerguid, String modulename) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findMacListByName(macaddress, centerguid, modulename));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 查找对应Mac和name的list
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByName(String centerguid,
            String modulename) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(new ZCAuditZnsbSelfmachinemoduleService().findCommonListByName(centerguid, modulename));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 根据centerguid删除模块
     */
    @Override
    public AuditCommonResult<String> deletebyCenterGuid(String CenterGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            new ZCAuditZnsbSelfmachinemoduleService().deleteRecords(ZCAuditZnsbSelfmachinemodule.class, CenterGuid,
                    "centerguid");

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据centerguid和moduleconfigtype删除模块
     */
    @Override
    public AuditCommonResult<String> deletebyCenterAndConfig(String CenterGuid,String moduleconfigtype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            new ZCAuditZnsbSelfmachinemoduleService().deleteByCenterguidAndConfigType(CenterGuid, moduleconfigtype);

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 根据macaddress删除模块
     */
    @Override
    public AuditCommonResult<String> deletebyMacaddress(String macaddress) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            new ZCAuditZnsbSelfmachinemoduleService().deleteRecords(ZCAuditZnsbSelfmachinemodule.class, macaddress,
                    "Macaddress");
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据macaddress删除模块
     */
    @Override
    public AuditCommonResult<String> deletebyMacAndConfigtype(String macaddress,String moduleconfigtype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            new ZCAuditZnsbSelfmachinemoduleService().deleteByMacAndConfigType(macaddress, moduleconfigtype);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 初始化模块数据
     */
    @Override
    public AuditCommonResult<String> initModule(String operateusername, String centerGuid,String moduleconfigtype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        ZCAuditZnsbSelfmachinemoduleService service = new ZCAuditZnsbSelfmachinemoduleService();
        try {
            List<Record> modulelist = QueueCommonUtil.getModuleList(moduleconfigtype);
            if(StringUtil.isNotBlank(moduleconfigtype)&&QueueConstant.CONSTANT_STR_TWO.equals(moduleconfigtype)){
                modulelist = changeModuleListGuid(modulelist);
            }
            for (Record module : modulelist) {
                service.initModule(operateusername, centerGuid, module);
            }
        }
        catch (Exception e) {
            Log.error(e.toString());
            result.setSystemFail(e.toString());
        }
        return result;
    }

    /**
     * 
     *  [用于对工作台模块中模块的rowguid进行修改,防止不同中心下初始化工作台出现rowguid重复报错问题] 
     *  @param modulelist
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> changeModuleListGuid(List<Record> modulelist){
        List<Record> recordlist1 = new ArrayList<Record>();
        List<Record> recordlist2 = new ArrayList<Record>();
        List<Record> recordlist3 = new ArrayList<Record>();
        List<Record> recodelist = new ArrayList<Record>();
        for(int i=0;i<modulelist.size();i++){
            Record record = modulelist.get(i);
            if(record.getStr("modulecode").length()==1){
                recordlist1.add(record);
            }
            else if(record.getStr("modulecode").length()==2){
                recordlist2.add(record);      
            }
            else if(record.getStr("modulecode").length()==3){
                recordlist3.add(record);  
            }
        }
        for(int i=0;i<recordlist1.size();i++){
            Record record1 = recordlist1.get(i);
            record1.set("rowguid", UUID.randomUUID().toString());
        }
        for(int i=0;i<recordlist2.size();i++){
            Record record2 = recordlist2.get(i);
            record2.set("rowguid", UUID.randomUUID().toString());
            for(int j=0;j<recordlist1.size();j++){
                Record record1 = recordlist1.get(j);
                if(record2.getStr("modulecode").substring(0, 1).equals(record1.getStr("modulecode"))){
                    record2.set("parentmoduleguid", record1.getStr("rowguid"));
                }
            }
        }
        for(int i=0;i<recordlist3.size();i++){
            Record record3 = recordlist3.get(i);
            record3.set("rowguid", UUID.randomUUID().toString());
            for(int j=0;j<recordlist2.size();j++){
                Record record2 = recordlist2.get(j);
                if(record3.getStr("modulecode").substring(0, 2).equals(record2.getStr("modulecode"))){
                    record3.set("parentmoduleguid", record2.getStr("rowguid"));
                }
            }
        }
        recodelist.addAll(recordlist1);
        recodelist.addAll(recordlist2);
        recodelist.addAll(recordlist3);
        return recodelist;
    }
    
    /**
     * 复制模块数据
     */
    @Override
    public AuditCommonResult<String> copyModule(String operateusername, String centerGuid, String macaddress) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        ZCAuditZnsbSelfmachinemoduleService service = new ZCAuditZnsbSelfmachinemoduleService();
        try {
            List<ZCAuditZnsbSelfmachinemodule> modulelist = service.findCenterList(centerGuid);
            if (modulelist != null && !modulelist.isEmpty()) {
                for (ZCAuditZnsbSelfmachinemodule module : modulelist) {
                    service.copyModule(operateusername, macaddress, module.getModulename(), module.getModuletype(),
                            centerGuid, module.getPicturepath(), module.getHtmlurl(), module.getIsneedlogin(), "1",
                            module.getOrdernum());
                }
            }
        }
        catch (Exception e) {
            //result.setSystemFail(e.toString());
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 复制模块数据
     */
    @Override
    public AuditCommonResult<String> copyModule(String operateusername, String centerGuid, String macaddress, String moduleconfigtype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        ZCAuditZnsbSelfmachinemoduleService service = new ZCAuditZnsbSelfmachinemoduleService();
        try {
            List<ZCAuditZnsbSelfmachinemodule> modulelist = service.findCenterList(centerGuid,moduleconfigtype);
            if (modulelist != null && !modulelist.isEmpty()) {
                for (ZCAuditZnsbSelfmachinemodule module : modulelist) {
                    service.copyModule(operateusername, macaddress, centerGuid, module);
                }
            }
        }
        catch (Exception e) {
            //result.setSystemFail(e.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据centerguid,macaddress查找热度排序的列表(自行判断是否需要个性化macaddress)
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getHotModuleList(String centerguid, String macaddress,
            int modulecount) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            List<ZCAuditZnsbSelfmachinemodule> modulelist = new ZCAuditZnsbSelfmachinemoduleService()
                    .findHotTypeListByMac(macaddress, centerguid, modulecount);
            if (modulelist == null || modulelist.isEmpty()) {
                modulelist = new ZCAuditZnsbSelfmachinemoduleService().findHotTypeList(centerguid, modulecount);
            }
            result.setResult(modulelist);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<ZCAuditZnsbSelfmachinemodule> getModuleByModulenameAndMac(String macaddress,
            String modulename) {
        AuditCommonResult<ZCAuditZnsbSelfmachinemodule> result = new AuditCommonResult<ZCAuditZnsbSelfmachinemodule>();
        try {
            ZCAuditZnsbSelfmachinemodule module = new ZCAuditZnsbSelfmachinemoduleService()
                    .getModuleByModulenameAndMac(macaddress, modulename);

            result.setResult(module);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<ZCAuditZnsbSelfmachinemodule> getModuleByModulenameAndCenterguid(String centerguid,
            String modulename) {
        AuditCommonResult<ZCAuditZnsbSelfmachinemodule> result = new AuditCommonResult<ZCAuditZnsbSelfmachinemodule>();
        try {
            ZCAuditZnsbSelfmachinemodule module = new ZCAuditZnsbSelfmachinemoduleService()
                    .getModuleByModulenameAndCenterguid(centerguid, modulename);

            result.setResult(module);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByModulecodelevel(String modulecodelevel,
            String CenterGuid) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findListByModulecodelevel(modulecodelevel, CenterGuid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByParentmoduleguid(String parentmoduleguid,
            String CenterGuid) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findListByParentmoduleguid(parentmoduleguid, CenterGuid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getHotModuleListByParentmoduleguid(String parentmoduleguid,
            String CenterGuid) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().findHotListByParentmoduleguid(parentmoduleguid, CenterGuid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getModuleListByParentmoduleguidAndMac(String parentmoduleguid,
            String CenterGuid,String macaddress) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().getModuleListByParentmoduleguidAndMac(parentmoduleguid, CenterGuid, macaddress));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getHotModuleListByParentmoduleguidAndMac(String parentmoduleguid,
            String CenterGuid,String macaddress) {
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().getHotModuleListByParentmoduleguidAndMac(parentmoduleguid, CenterGuid, macaddress));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据moduleconfigtype获取最上层模块列表
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getParentListByConfigType(String moduleconfigtype,String centerguid){
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().getParentListByConfigType(moduleconfigtype, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据moduleconfigtype和mac地址获取最上层模块列表
     */
    @Override
    public AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> getParentListByConfigTypeAndMac(String moduleconfigtype,String centerguid,String macaddress){
        AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>> result = new AuditCommonResult<List<ZCAuditZnsbSelfmachinemodule>>();
        try {
            result.setResult(
                    new ZCAuditZnsbSelfmachinemoduleService().getParentListByConfigTypeAndMac(moduleconfigtype, centerguid,macaddress));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
}
