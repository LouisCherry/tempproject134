package com.epoint.zwdt.xmgxh.basic.spglv3.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglXmspsxblxxxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.inter.ISpglXmspsxblxxxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.service.SpglXmspsxblxxxxbV3Service;

/**
 * 住建部_项目审批事项办理详细信息表对应的后台service实现类
 * 
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:04]
 */
@Component
public class SpglXmspsxblxxxxbV3Impl implements ISpglXmspsxblxxxxbV3
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
    public int insert(SpglXmspsxblxxxxbV3 record) {
        return new SpglXmspsxblxxxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglXmspsxblxxxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmspsxblxxxxbV3 record) {
        return new SpglXmspsxblxxxxbV3Service().update(record);
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
    public SpglXmspsxblxxxxbV3 find(Object primaryKey) {
        return new SpglXmspsxblxxxxbV3Service().find(primaryKey);
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
    public SpglXmspsxblxxxxbV3 find(String sql, Object... args) {
        return new SpglXmspsxblxxxxbV3Service().find(sql, args);
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
    public List<SpglXmspsxblxxxxbV3> findList(String sql, Object... args) {
        return new SpglXmspsxblxxxxbV3Service().findList(sql, args);
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
    public List<SpglXmspsxblxxxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglXmspsxblxxxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    public List<SpglXmspsxblxxxxbV3> findList(String flowsn) {
        return new SpglXmspsxblxxxxbV3Service().findListByflowsn(flowsn);
    }

    @Override
    public AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>> getAllByPage(Map<String, String> map, int first, int pageSize,
            String sortField, String sortOrder) {
        return new SpglXmspsxblxxxxbV3Service().getAllByPage(map, first, pageSize, sortField, sortOrder);
    }

    @Override
    public AuditCommonResult<List<SpglXmspsxblxxxxbV3>> getListByCondition(Map<String, String> conditionMap) {
        SpglXmspsxblxxxxbV3Service spglXmdwxxbService = new SpglXmspsxblxxxxbV3Service();
        AuditCommonResult<List<SpglXmspsxblxxxxbV3>> result = new AuditCommonResult<List<SpglXmspsxblxxxxbV3>>();
        List<SpglXmspsxblxxxxbV3> list = new ArrayList<>();
        try {
            list = spglXmdwxxbService.getAllRecord(SpglXmspsxblxxxxbV3.class, conditionMap);
            result.setResult(list);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>> getAllByPage(Map<String, String> conditionMap, Integer first,
            Integer pageSize, String sortField, String sortOrder) {
        SpglXmspsxblxxxxbV3Service service = new SpglXmspsxblxxxxbV3Service();
        AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>> result = new AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>>();
        PageData<SpglXmspsxblxxxxbV3> pageData = new PageData<>();
        try {
            pageData = service.getAllRecordByPage(SpglXmspsxblxxxxbV3.class, conditionMap, first, pageSize, sortField,
                    sortOrder);
            result.setResult(pageData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SpglXmspsxblxxxxbV3Service service = new SpglXmspsxblxxxxbV3Service();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = service.getAllRecordCount(SpglXmspsxblxxxxbV3.class, conditionMap);
            result.setResult(count);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>> getRiskRepeatData(Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        SpglXmspsxblxxxxbV3Service service = new SpglXmspsxblxxxxbV3Service();
        AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>> result = new AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>>();
        result.setResult(service.getRiskRepeatData(conditionMap, first, pageSize));
        return result;
    }

    @Override
    public List<String> filterHjltSxslbm(List<String> spsxslbm) {
        SpglXmspsxblxxxxbV3Service service = new SpglXmspsxblxxxxbV3Service();
        return service.filterHjltSxslbm(spsxslbm);
    }

}
