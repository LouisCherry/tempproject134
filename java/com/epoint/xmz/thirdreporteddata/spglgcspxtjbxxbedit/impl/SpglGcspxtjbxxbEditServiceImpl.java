package com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.api.ISpglGcspxtjbxxbEditService;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.api.entity.SpglGcspxtjbxxbEdit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 上报工改系统基本信息表对应的后台service实现类
 * 
 * @author lzhming
 * @version [版本号, 2023-08-31 15:33:35]
 */
@Component
@Service
public class SpglGcspxtjbxxbEditServiceImpl implements ISpglGcspxtjbxxbEditService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglGcspxtjbxxbEdit record) {
        return new SpglGcspxtjbxxbEditService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglGcspxtjbxxbEditService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglGcspxtjbxxbEdit record) {
        return new SpglGcspxtjbxxbEditService().update(record);
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
    public SpglGcspxtjbxxbEdit find(Object primaryKey) {
       return new SpglGcspxtjbxxbEditService().find(primaryKey);
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
    public SpglGcspxtjbxxbEdit find(String sql, Object... args) {
        return new SpglGcspxtjbxxbEditService().find(sql,args);
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
    public List<SpglGcspxtjbxxbEdit> findList(String sql, Object... args) {
       return new SpglGcspxtjbxxbEditService().findList(sql,args);
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
    public List<SpglGcspxtjbxxbEdit> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new SpglGcspxtjbxxbEditService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countSpglGcspxtjbxxb(String sql, Object... args){
        return new SpglGcspxtjbxxbEditService().countSpglGcspxtjbxxb(sql, args);
    }

    @Override
    public PageData<SpglGcspxtjbxxbEdit> getDbListByPage(Class<SpglGcspxtjbxxbEdit> spglGcspxtjbxxbEditClass, Map<String, String> map, int first, int pageSize, String sortField, String sortOrder) {
        return new SQLManageUtil().getDbListByPage(SpglGcspxtjbxxbEdit.class,map,first,pageSize,sortField,sortOrder);
    }

    @Override
    public List<SpglGcspxtjbxxbEdit> getListByXq(String xzqhdm) {
        return new SpglGcspxtjbxxbEditService().getListByXq(xzqhdm);
    }

    @Override
    public Integer IsExistXzqhdm(String xzqhdm) {
        return new SpglGcspxtjbxxbEditService().IsExistXzqhdm(xzqhdm);
    }

}
