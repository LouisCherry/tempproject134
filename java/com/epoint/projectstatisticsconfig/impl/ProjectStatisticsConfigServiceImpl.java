package com.epoint.projectstatisticsconfig.impl;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.stereotype.Component;
import com.epoint.projectstatisticsconfig.api.entity.ProjectStatisticsConfig;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.projectstatisticsconfig.api.IProjectStatisticsConfigService;
/**
 * 办件统计配置表对应的后台service实现类
 * 
 * @author 15056
 * @version [版本号, 2022-05-23 17:43:09]
 */
@Component
@Service
public class ProjectStatisticsConfigServiceImpl implements IProjectStatisticsConfigService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ProjectStatisticsConfig record) {
        return new ProjectStatisticsConfigService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ProjectStatisticsConfigService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ProjectStatisticsConfig record) {
        return new ProjectStatisticsConfigService().update(record);
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
    public ProjectStatisticsConfig find(Object primaryKey) {
       return new ProjectStatisticsConfigService().find(primaryKey);
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
    public ProjectStatisticsConfig find(String sql, Object... args) {
        return new ProjectStatisticsConfigService().find(sql,args);
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
    public List<ProjectStatisticsConfig> findList(String sql, Object... args) {
       return new ProjectStatisticsConfigService().findList(sql,args);
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
    public List<ProjectStatisticsConfig> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ProjectStatisticsConfigService().findList(sql,pageNumber,pageSize,args);
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
     @Override
    public Integer countProjectStatisticsConfig(String sql, Object... args){
        return new ProjectStatisticsConfigService().countProjectStatisticsConfig(sql, args);
    }

    @Override
    public String getAreaCodeByAreaName(String areaName) {
        return new ProjectStatisticsConfigService().getAreaCodeByAreaName(areaName);
    }

    @Override
    public List<String> getAllOuguidByAreacode(String areacode) {
        return new ProjectStatisticsConfigService().getAllOuguidByAreacode(areacode);
    }

    @Override
    public ProjectStatisticsConfig getInfoByAreacodeAndOuguid(String areacode, String ouguid) {
        return new ProjectStatisticsConfigService().getInfoByAreacodeAndOuguid(areacode,ouguid);
    }

    @Override
    public PageData<Record> pageData(int first, int pageSize, Record dataBean) {
        return new ProjectStatisticsConfigService().pageData(first,pageSize,dataBean);
    }

}
