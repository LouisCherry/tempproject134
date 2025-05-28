package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.spgl.domain.SpglDfxmsplcxxb;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglsplcxxbService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 住建部_地方项目审批流程信息表对应的后台service实现类
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:56]
 */
@Component
@Service
public class SpglsplcxxbImpl implements ISpglsplcxxb {
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
    public int insert(Spglsplcxxb record) {
        return new SpglsplcxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglsplcxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Spglsplcxxb record) {
        return new SpglsplcxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public Spglsplcxxb find(Object primaryKey) {
        return new SpglsplcxxbService().find(primaryKey);
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
    public Spglsplcxxb find(String sql, Object... args) {
        return new SpglsplcxxbService().find(args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<Spglsplcxxb> findList(String sql, Object... args) {
        return new SpglsplcxxbService().findList(sql, args);
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
    public List<Spglsplcxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglsplcxxbService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public void deleteBySpGuid(String guid) {
        new SpglsplcxxbService().deleteBySpGuid(guid);
    }

    @Override
    public Double getMaxSplcbbh(String guid) {
        return new SpglsplcxxbService().getMaxSplcbbh(guid);
    }

    @Override
    public AuditCommonResult<PageData<Spglsplcxxb>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                 Integer pageSize, String sortField, String sortOrder) {
        SpglsplcxxbService spglDfxmsplcxxbService = new SpglsplcxxbService();
        AuditCommonResult<PageData<Spglsplcxxb>> result = new AuditCommonResult<PageData<Spglsplcxxb>>();
        PageData<Spglsplcxxb> pageData = new PageData<>();
        try {
            pageData = spglDfxmsplcxxbService.getAllRecordByPage(Spglsplcxxb.class, conditionMap, first, pageSize,
                    sortField, sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Spglsplcxxb>> getListByCondition(Map<String, String> conditionMap) {
        SpglsplcxxbService spglDfxmsplcxxbService = new SpglsplcxxbService();
        AuditCommonResult<List<Spglsplcxxb>> result = new AuditCommonResult<List<Spglsplcxxb>>();
        List<Spglsplcxxb> list = new ArrayList<>();
        try {
            list = spglDfxmsplcxxbService.getAllRecord(Spglsplcxxb.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SpglsplcxxbService spglDfxmsplcxxbService = new SpglsplcxxbService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = spglDfxmsplcxxbService.getAllRecordCount(SpglDfxmsplcxxb.class, conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Spglsplcxxb getYxsjBySplcbm(String splcbm, Double splcbbh) {
        return new SpglsplcxxbService().getYxsjBySplcbm(splcbm, splcbbh);
    }

}
