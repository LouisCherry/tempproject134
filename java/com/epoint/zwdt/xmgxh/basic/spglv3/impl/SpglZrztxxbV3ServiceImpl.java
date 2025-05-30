package com.epoint.zwdt.xmgxh.basic.spglv3.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglZrztxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.inter.ISpglZrztxxbV3Service;
import com.epoint.zwdt.xmgxh.basic.spglv3.service.SpglZrztxxbV3Service;
/**
 * 责任主体信息表对应的后台service实现类
 * 
 * @author Epoint
 * @version [版本号, 2023-09-25 17:01:40]
 */
@Component
@Service
public class SpglZrztxxbV3ServiceImpl implements ISpglZrztxxbV3Service
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglZrztxxbV3 record) {
        return new SpglZrztxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglZrztxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglZrztxxbV3 record) {
        return new SpglZrztxxbV3Service().update(record);
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
    public SpglZrztxxbV3 find(Object primaryKey) {
       return new SpglZrztxxbV3Service().find(primaryKey);
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
    public SpglZrztxxbV3 find(String sql, Object... args) {
        return new SpglZrztxxbV3Service().find(sql,args);
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
    public List<SpglZrztxxbV3> findList(String sql, Object... args) {
       return new SpglZrztxxbV3Service().findList(sql,args);
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
    public List<SpglZrztxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new SpglZrztxxbV3Service().findList(sql,pageNumber,pageSize,args);
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
    public Integer countSpglZrztxxbV3(String sql, Object... args){
        return new SpglZrztxxbV3Service().countSpglZrztxxbV3(sql, args);
    }

     @Override
     public AuditCommonResult<List<Record>> getAddedDwInfo(String xzqhdm, String gcdm) {
         AuditCommonResult<List<Record>> result = new AuditCommonResult<>();
         result.setResult(new SpglZrztxxbV3Service().getAddedDwInfo(xzqhdm, gcdm));
         return result;
     }
     
     @Override
     public AuditCommonResult<PageData<SpglZrztxxbV3>> getAllByPage(Map<String, String> conditionMap,
             Integer first, Integer pageSize, String sortField, String sortOrder) {
         SpglZrztxxbV3Service service = new SpglZrztxxbV3Service();
         AuditCommonResult<PageData<SpglZrztxxbV3>> result = new AuditCommonResult<PageData<SpglZrztxxbV3>>();
         PageData<SpglZrztxxbV3> pageData = new PageData<>();
         try {
             pageData = service.getAllRecordByPage(SpglZrztxxbV3.class, conditionMap, first, pageSize, sortField,
                     sortOrder);
             result.setResult(pageData);
         }
         catch (Exception e) {
             e.printStackTrace();
         }
         return result;
     }
     
     
     @Override
     public AuditCommonResult<List<SpglZrztxxbV3>> getListByCondition(Map<String, String> conditionMap) {
         SpglZrztxxbV3Service service = new SpglZrztxxbV3Service();
         AuditCommonResult<List<SpglZrztxxbV3>> result = new AuditCommonResult<List<SpglZrztxxbV3>>();
         List<SpglZrztxxbV3> list = new ArrayList<>();
         try {
             list = service.getAllRecord(SpglZrztxxbV3.class, conditionMap);
             result.setResult(list);
         }
         catch (Exception e) {
             e.printStackTrace();
         }
         return result;
     }
}
