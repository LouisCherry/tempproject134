package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglXmspsxblxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 住建部_项目审批事项办理信息表对应的后台service实现类
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:08:57]
 */
@Component
@Service
public class SpglXmspsxblxxbV3Impl implements ISpglXmspsxblxxbV3 {
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
    public int insert(SpglXmspsxblxxbV3 record) {
        return new SpglXmspsxblxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglXmspsxblxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmspsxblxxbV3 record) {
        return new SpglXmspsxblxxbV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmspsxblxxbV3 find(Object primaryKey) {
        return new SpglXmspsxblxxbV3Service().find(primaryKey);
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
    public SpglXmspsxblxxbV3 find(String sql, Object... args) {
        return new SpglXmspsxblxxbV3Service().find(args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspsxblxxbV3> findList(String sql, Object... args) {
        return new SpglXmspsxblxxbV3Service().findList(sql, args);
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
    public List<SpglXmspsxblxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglXmspsxblxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public SpglXmspsxblxxbV3 findByProjectguid(String projectguid) {
        return new SpglXmspsxblxxbV3Service().findByProjectguid(projectguid);
    }

    @Override
    public PageData<SpglXmspsxblxxbV3> getAllByPage(String xzqhdm, String gcdm, String shsxmc, Integer sjsczt, int first,
                                                    int pageSize, String sortField, String sortOrder) {
        return new SpglXmspsxblxxbV3Service().getAllByPage(xzqhdm, gcdm, shsxmc, sjsczt, first, pageSize, sortField,
                sortOrder);
    }

    @Override
    public boolean isExistFlowsn(String spsxslbm) {
        return new SpglXmspsxblxxbV3Service().isExistFlowsn(spsxslbm);
    }

    @Override
    public SpglXmspsxblxxbV3 getSpglXmspsxblxxbBySlbm(String spsxslbm) {
        return new SpglXmspsxblxxbV3Service().getSpglXmspsxblxxbBySlbm(spsxslbm);
    }

    @Override
    public AuditCommonResult<List<SpglXmspsxblxxbV3>> getListByCondition(Map<String, String> conditionMap) {
        SpglXmspsxblxxbV3Service spglXmdwxxbService = new SpglXmspsxblxxbV3Service();
        AuditCommonResult<List<SpglXmspsxblxxbV3>> result = new AuditCommonResult<List<SpglXmspsxblxxbV3>>();
        List<SpglXmspsxblxxbV3> list = new ArrayList<>();
        try {
            list = spglXmdwxxbService.getAllRecord(SpglXmspsxblxxbV3.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<SpglXmspsxblxxbV3>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                       Integer pageSize, String sortField, String sortOrder) {
        SpglXmspsxblxxbV3Service service = new SpglXmspsxblxxbV3Service();
        AuditCommonResult<PageData<SpglXmspsxblxxbV3>> result = new AuditCommonResult<PageData<SpglXmspsxblxxbV3>>();
        PageData<SpglXmspsxblxxbV3> pageData = new PageData<>();
        try {
            pageData = service.getAllRecordByPage(SpglXmspsxblxxbV3.class, conditionMap, first, pageSize, sortField,
                    sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SpglXmspsxblxxbV3Service service = new SpglXmspsxblxxbV3Service();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = service.getAllRecordCount(SpglXmspsxblxxbV3.class, conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
