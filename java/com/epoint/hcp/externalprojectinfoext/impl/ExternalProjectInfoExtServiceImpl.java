package com.epoint.hcp.externalprojectinfoext.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.hcp.externalprojectinfoext.api.IExternalProjectInfoExtService;
import com.epoint.hcp.externalprojectinfoext.api.entity.ExternalProjectInfoExt;

/**
 * 外部办件基本扩展信息表对应的后台service实现类
 *
 * @author wannengDB
 * @version [版本号, 2022-01-06 14:54:57]
 */
@Component
@Service
public class ExternalProjectInfoExtServiceImpl implements IExternalProjectInfoExtService {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExternalProjectInfoExt record) {
        return new ExternalProjectInfoExtService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ExternalProjectInfoExtService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExternalProjectInfoExt record) {
        return new ExternalProjectInfoExtService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public ExternalProjectInfoExt find(Object primaryKey) {
        return new ExternalProjectInfoExtService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public ExternalProjectInfoExt find(String sql, Object... args) {
        return new ExternalProjectInfoExtService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<ExternalProjectInfoExt> findList(String sql, Object... args) {
        return new ExternalProjectInfoExtService().findList(sql, args);
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
    public List<ExternalProjectInfoExt> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new ExternalProjectInfoExtService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countExternalProjectInfoExt(String sql, Object... args) {
        return new ExternalProjectInfoExtService().countExternalProjectInfoExt(sql, args);
    }

    /**
     * 分页查询列表数据
     */
    @Override
    public List<Record> finList(int first, int pagesize, String ouguid, String areacode) {
        return new ExternalProjectInfoExtService().finList(first, pagesize, ouguid, areacode);
    }

    /**
     * 分页查询列表总数
     */
    @Override
    public Integer finTotal(String ouguid, String areacode) {
        return new ExternalProjectInfoExtService().finTotal(ouguid, areacode);
    }

    @Override
    public List<Record> finList(int first, int pagesize, String ouguid, String areacode, String projectno) {
        return new ExternalProjectInfoExtService().finList(first, pagesize, ouguid, areacode, projectno);
    }

    @Override
    public Integer finTotal(String ouguid, String areacode, String projectno) {
        return new ExternalProjectInfoExtService().finTotal(ouguid, areacode, projectno);
    }

    public ExternalProjectInfoExt getExternalProjectByProjectguid(String proejctguid,String month) {
        return new ExternalProjectInfoExtService().getExternalProjectByProjectguid(proejctguid,month);
    }
    
}
