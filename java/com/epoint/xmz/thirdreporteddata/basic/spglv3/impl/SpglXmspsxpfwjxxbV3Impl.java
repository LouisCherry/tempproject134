package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxpfwjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxpfwjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglXmspsxpfwjxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 住建部_审批事项批复文件信息表对应的后台service实现类
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:30]
 */
@Component
@Service
public class SpglXmspsxpfwjxxbV3Impl implements ISpglXmspsxpfwjxxbV3 {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmspsxpfwjxxbV3 record) {
        return new SpglXmspsxpfwjxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglXmspsxpfwjxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmspsxpfwjxxbV3 record) {
        return new SpglXmspsxpfwjxxbV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmspsxpfwjxxbV3 find(Object primaryKey) {
        return new SpglXmspsxpfwjxxbV3Service().find(primaryKey);
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
    public SpglXmspsxpfwjxxbV3 find(String sql, Object... args) {
        return new SpglXmspsxpfwjxxbV3Service().find(args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspsxpfwjxxbV3> findList(String sql, Object... args) {
        return new SpglXmspsxpfwjxxbV3Service().findList(sql, args);
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
    public List<SpglXmspsxpfwjxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglXmspsxpfwjxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public AuditCommonResult<PageData<SpglXmspsxpfwjxxbV3>> getAllByPage(Map<String, String> map, int first, int pageSize,
                                                                         String sortField, String sortOrder) {
        return new SpglXmspsxpfwjxxbV3Service().getAllByPage(map, first, pageSize, sortField, sortOrder);
    }

    @Override
    public AuditCommonResult<List<SpglXmspsxpfwjxxbV3>> getListByCondition(Map<String, String> conditionMap) {
        SpglXmspsxpfwjxxbV3Service spglXmdwxxbService = new SpglXmspsxpfwjxxbV3Service();
        AuditCommonResult<List<SpglXmspsxpfwjxxbV3>> result = new AuditCommonResult<List<SpglXmspsxpfwjxxbV3>>();
        List<SpglXmspsxpfwjxxbV3> list = new ArrayList<>();
        try {
            list = spglXmdwxxbService.getAllRecord(SpglXmspsxpfwjxxbV3.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<SpglXmspsxpfwjxxbV3>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                         Integer pageSize, String sortField, String sortOrder) {
        SpglXmspsxpfwjxxbV3Service service = new SpglXmspsxpfwjxxbV3Service();
        AuditCommonResult<PageData<SpglXmspsxpfwjxxbV3>> result = new AuditCommonResult<PageData<SpglXmspsxpfwjxxbV3>>();
        PageData<SpglXmspsxpfwjxxbV3> pageData = new PageData<>();
        try {
            pageData = service.getAllRecordByPage(SpglXmspsxpfwjxxbV3.class, conditionMap, first, pageSize, sortField,
                    sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SpglXmspsxpfwjxxbV3Service service = new SpglXmspsxpfwjxxbV3Service();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = service.getAllRecordCount(SpglXmspsxpfwjxxbV3.class, conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
