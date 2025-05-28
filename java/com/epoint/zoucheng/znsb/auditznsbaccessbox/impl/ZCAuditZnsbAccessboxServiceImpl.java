package com.epoint.zoucheng.znsb.auditznsbaccessbox.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbaccessbox.domain.AuditZnsbAccessbox;
import com.epoint.basic.auditqueue.auditznsbaccessbox.inter.IAuditZnsbAccessbox;
import com.epoint.basic.auditqueue.auditznsbaccessbox.service.AuditZnsbAccessboxService;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.domain.AuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.inter.IAuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;

import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.znsb.auditznsbaccessbox.inter.IZCAuditZnsbAccessbox;
import com.epoint.zoucheng.znsb.worktablecomment.util.QueueCommonUtil;

/**
 * 智能化存取盒表对应的后台service实现类
 * 
 * @author 54201
 * @version [版本号, 2019-02-20 14:45:09]
 */
@Component
@Service
public class ZCAuditZnsbAccessboxServiceImpl implements IZCAuditZnsbAccessbox
{
    /**
     * 
     */
    private static final long serialVersionUID = 5653089260996587441L;

    /**
     * 插入数据
     * 
     * @param record
     *            
     * @return int
     */
    public int insert(AuditZnsbAccessbox record) {
        return new AuditZnsbAccessboxService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditZnsbAccessboxService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            
     * @return int
     */
    public int update(AuditZnsbAccessbox record) {
        return new AuditZnsbAccessboxService().update(record);
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
    public AuditZnsbAccessbox find(Object primaryKey) {
        return new AuditZnsbAccessboxService().find(primaryKey);
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
    public AuditZnsbAccessbox find(String sql, Object... args) {
        return new AuditZnsbAccessboxService().find(args);
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
    public List<AuditZnsbAccessbox> findList(String sql, Object... args) {
        return new AuditZnsbAccessboxService().findList(sql, args);
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
    public List<AuditZnsbAccessbox> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditZnsbAccessboxService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 根据存储柜guid删除所有存储盒数据
     */
    @Override
    public AuditCommonResult<String> deletebyCabinetGuid(String CabinetGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            new AuditZnsbAccessboxService().deleteRecords(AuditZnsbAccessbox.class, CabinetGuid, "cabinetguid");

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
    public AuditCommonResult<String> initBox(String cabinetguid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        AuditZnsbAccessboxService service = new AuditZnsbAccessboxService();
        IAuditZnsbAccesscabinet cabinetservice = ContainerFactory.getContainInfo().getComponent(IAuditZnsbAccesscabinet.class);
        IAuditZnsbEquipment equipmentservice = ContainerFactory.getContainInfo().getComponent(IAuditZnsbEquipment.class);
        try {
            List<Record> boxlist = null;
            
            AuditZnsbAccesscabinet cabinet = cabinetservice.getDetailByGuid(cabinetguid).getResult();
            if(StringUtil.isNotBlank(cabinet)){
                if(StringUtil.isNotBlank(cabinet.getMacaddress())){
                    AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(cabinet.getMacaddress(), "machinetype").getResult();
                    if(StringUtil.isNotBlank(equipment)){
                        boxlist = QueueCommonUtil.getBoxList(equipment.getMachinetype());
                    }else{
                        result.setResult("未找到对应设备请检查设备是否被删除。");
                        return result;
                    }
                }else{
                    result.setResult("请先绑定一体机或智能存储柜。");
                    return result;
                }
            }
            
            for (Record box : boxlist) {
                service.initBox(box.getStr("boxno"), box.getStr("abscissa"), box.getStr("ordinate"),
                        box.getStr("boxstatus"), cabinetguid);
            }
            result.setResult("success");
            return result;
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditZnsbAccessbox>> getBoxList(Map<String, String> conditionMap, String fields) {
        AuditQueueBasicService<AuditZnsbAccessbox> boxService = new AuditQueueBasicService<AuditZnsbAccessbox>();
        AuditCommonResult<List<AuditZnsbAccessbox>> result = new AuditCommonResult<List<AuditZnsbAccessbox>>();
        try {
            result.setResult(boxService.selectRecordList(AuditZnsbAccessbox.class, conditionMap, fields));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditZnsbAccessbox> getDetailByGuid(String RowGuid) {
        AuditQueueBasicService<AuditZnsbAccessbox> boxService = new AuditQueueBasicService<AuditZnsbAccessbox>();
        AuditCommonResult<AuditZnsbAccessbox> result = new AuditCommonResult<AuditZnsbAccessbox>();
        try {
            result.setResult(boxService.getDetail(AuditZnsbAccessbox.class, RowGuid, "rowguid"));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

}
