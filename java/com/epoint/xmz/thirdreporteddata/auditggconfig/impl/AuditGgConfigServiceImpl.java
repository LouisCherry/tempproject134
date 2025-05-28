package com.epoint.xmz.thirdreporteddata.auditggconfig.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.thirdreporteddata.auditggconfig.api.IAuditGgConfigService;
import com.epoint.xmz.thirdreporteddata.auditggconfig.api.entity.AuditGgConfig;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 工改配置信息表对应的后台service实现类
 *
 * @author shaoyuhui
 * @version [版本号, 2023-11-06 17:08:23]
 */
@Component
@Service
public class AuditGgConfigServiceImpl implements IAuditGgConfigService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditGgConfig record) {
        return new AuditGgConfigService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditGgConfigService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditGgConfig record) {
        return new AuditGgConfigService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditGgConfig find(Object primaryKey) {
        return new AuditGgConfigService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditGgConfig find(String sql, Object... args) {
        return new AuditGgConfigService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditGgConfig> findList(String sql, Object... args) {
        return new AuditGgConfigService().findList(sql, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditGgConfig> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditGgConfigService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditGgConfig(String sql, Object... args) {
        return new AuditGgConfigService().countAuditGgConfig(sql, args);
    }

    /**
     * 获取所有参数
     *
     * @return
     */
    @Override
    public Record getAllConfig() {
        List<AuditGgConfig> configList = new AuditGgConfigService().getConfigList();
        Record record = new Record();
        for (AuditGgConfig auditGgConfig : configList) {
            record.put(auditGgConfig.getConfigname(), auditGgConfig.getConfigvalue());
        }
        return record;
    }

    /**
     * 根据参数名查找参数值
     *
     * @param configName
     * @return
     */
    @Override
    public String getConfigValueByName(String configName) {
        return new AuditGgConfigService().getConfigValueByName(configName);
    }

    /**
     * 保存参数名+参数值
     *
     * @param configName
     * @param configValue
     */
    @Override
    public void saveNameAndValue(String configName, String configValue) {
        AuditGgConfigService service = new AuditGgConfigService();
        AuditGgConfig config = service.getConfigByName(configName);
        if (config != null) {
            config.setConfigvalue(configValue);
            config.setUpdatetime(new Date());
            service.update(config);
        } else {
            config = new AuditGgConfig();
            config.setRowguid(UUID.randomUUID().toString());
            config.setConfigname(configName);
            config.setConfigvalue(configValue);
            config.setUpdatetime(new Date());
            service.insert(config);
        }
    }

}
