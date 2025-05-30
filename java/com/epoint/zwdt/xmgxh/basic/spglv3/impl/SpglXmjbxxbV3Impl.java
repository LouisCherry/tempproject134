package com.epoint.zwdt.xmgxh.basic.spglv3.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglXmjbxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.inter.ISpglXmjbxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.service.SpglXmjbxxbV3Service;

/**
 * 住建部_项目基本信息表对应的后台service实现类
 * 
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:08:41]
 */
@Component
@Service
public class SpglXmjbxxbV3Impl implements ISpglXmjbxxbV3
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
    public int insert(SpglXmjbxxbV3 record) {
        return new SpglXmjbxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglXmjbxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmjbxxbV3 record) {
        return new SpglXmjbxxbV3Service().update(record);
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
    public SpglXmjbxxbV3 find(Object primaryKey) {
       return new SpglXmjbxxbV3Service().find(primaryKey);
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
    public SpglXmjbxxbV3 find(String sql, Object... args) {
        return new SpglXmjbxxbV3Service().find(args);
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
    public List<SpglXmjbxxbV3> findList(String sql, Object... args) {
       return new SpglXmjbxxbV3Service().findList(sql,args);
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
    public List<SpglXmjbxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglXmjbxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public SpglXmjbxxbV3 findByLshAndSplclx(String lsh, String splclx) {
        return new SpglXmjbxxbV3Service().findByLshAndSplclx(lsh, splclx);
    }

    @Override
    public boolean isExistGcdm(String gcdm) {
        return new SpglXmjbxxbV3Service().isExistGcdm(gcdm);
    }

    @Override
    public AuditCommonResult<PageData<SpglXmjbxxbV3>> getAllByPage(Map<String, String> conditionMap, Integer first,
            Integer pageSize, String sortField, String sortOrder) {
        SpglXmjbxxbV3Service spglXmjbxxbService = new SpglXmjbxxbV3Service();
        AuditCommonResult<PageData<SpglXmjbxxbV3>> result = new AuditCommonResult<PageData<SpglXmjbxxbV3>>();
        PageData<SpglXmjbxxbV3> pageData = new PageData<>();
        try {
            pageData = spglXmjbxxbService.getAllRecordByPage(SpglXmjbxxbV3.class, conditionMap, first, pageSize,
                    sortField, sortOrder);
            result.setResult(pageData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<SpglXmjbxxbV3>> getAllXmByPage(Map<String, String> conditionMap, Integer first,
            Integer pageSize, String sortField, String sortOrder) {
        SpglXmjbxxbV3Service spglXmjbxxbService = new SpglXmjbxxbV3Service();
        AuditCommonResult<PageData<SpglXmjbxxbV3>> result = new AuditCommonResult<PageData<SpglXmjbxxbV3>>();
        PageData<SpglXmjbxxbV3> pageData = new PageData<>();
        try {
            pageData = spglXmjbxxbService.getAllXmByPage(SpglXmjbxxbV3.class, conditionMap, first, pageSize,
                    sortField, sortOrder);
            result.setResult(pageData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<SpglXmjbxxbV3>> getListByCondition(Map<String, String> conditionMap) {
        SpglXmjbxxbV3Service spglXmjbxxbService = new SpglXmjbxxbV3Service();
        AuditCommonResult<List<SpglXmjbxxbV3>> result = new AuditCommonResult<List<SpglXmjbxxbV3>>();
        List<SpglXmjbxxbV3> list = new ArrayList<>();
        try {
            list = spglXmjbxxbService.getAllRecord(SpglXmjbxxbV3.class, conditionMap);
            result.setResult(list);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SpglXmjbxxbV3Service spglXmjbxxbService = new SpglXmjbxxbV3Service();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = spglXmjbxxbService.getAllRecordCount(SpglXmjbxxbV3.class, conditionMap);
            result.setResult(count);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getGcdmByFailed() {
        SpglXmjbxxbV3Service spglXmjbxxbService = new SpglXmjbxxbV3Service();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        result.setResult(spglXmjbxxbService.getGcdmByFailed());
        return result;
    }
}
