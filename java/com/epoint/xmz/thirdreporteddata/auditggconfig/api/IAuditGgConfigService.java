package com.epoint.xmz.thirdreporteddata.auditggconfig.api;

import com.epoint.core.grammar.Record;
import com.epoint.xmz.thirdreporteddata.auditggconfig.api.entity.AuditGgConfig;

import java.io.Serializable;
import java.util.List;

/**
 * 工改配置信息表对应的后台service接口
 *
 * @author shaoyuhui
 * @version [版本号, 2023-11-06 17:08:23]
 */
public interface IAuditGgConfigService extends Serializable {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditGgConfig record);

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditGgConfig record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditGgConfig find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditGgConfig find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditGgConfig> findList(String sql, Object... args);

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
    public List<AuditGgConfig> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditGgConfig(String sql, Object... args);

    /**
     * 获取所有参数
     *
     * @return
     */
    public Record getAllConfig();

    /**
     * 根据参数名查找参数值
     *
     * @param configName
     * @return
     */
    public String getConfigValueByName(String configName);

    /**
     * 保存参数名+参数值
     *
     * @param configName
     * @param configValue
     */
    public void saveNameAndValue(String configName, String configValue);
}
