package com.epoint.xmz.lcprojecterror.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.lcprojecterror.api.ILcprojectErrorService;
import com.epoint.xmz.lcprojecterror.api.entity.LcprojectError;
/**
 * 浪潮推送失败记录表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2021-06-29 10:03:18]
 */
@Component
@Service
public class LcprojectErrorServiceImpl implements ILcprojectErrorService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(LcprojectError record) {
        return new LcprojectErrorService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new LcprojectErrorService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(LcprojectError record) {
        return new LcprojectErrorService().update(record);
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
    public LcprojectError find(Object primaryKey) {
       return new LcprojectErrorService().find(primaryKey);
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
    public LcprojectError find(String sql, Object... args) {
        return new LcprojectErrorService().find(sql,args);
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
    public List<LcprojectError> findList(String sql, Object... args) {
       return new LcprojectErrorService().findList(sql,args);
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
    public List<LcprojectError> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new LcprojectErrorService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countLcprojectError(String sql, Object... args){
        return new LcprojectErrorService().countLcprojectError(sql, args);
    }
     
     public Integer getCount(Map<String, String> conditionMap, Class<LcprojectError> clazz){
         return new LcprojectErrorService().getCount(conditionMap, clazz);
     }
     public PageData<LcprojectError> getAllLCProjectByPage(Map<String, String> conditionMap,
             Class<LcprojectError> clazz, int start, int pagesize, String sortField, String sortOrder){
         return new LcprojectErrorService().getAllLCProjectByPage(conditionMap, clazz, start,pagesize,sortField,sortOrder);
     }
     
     public List<LcprojectError> getAllLCProject(Map<String, String> conditionMap,Class<LcprojectError> clazz) {
         return new LcprojectErrorService().getAllLCProject(conditionMap,clazz);
     }

}
