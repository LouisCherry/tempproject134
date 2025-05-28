package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdxxb;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglspjdxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspjdxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglspjdxxbService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 住建部_地方项目审批流程阶段信息表对应的后台service实现类
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:10:04]
 */
@Component
@Service
public class SpglspjdxxbImpl implements ISpglspjdxxb {
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
    public int insert(Spglspjdxxb record) {
        return new SpglspjdxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglspjdxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Spglspjdxxb record) {
        return new SpglspjdxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public Spglspjdxxb find(Object primaryKey) {
        return new SpglspjdxxbService().find(primaryKey);
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
    public Spglspjdxxb find(String sql, Object... args) {
        return new SpglspjdxxbService().find(args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<Spglspjdxxb> findList(String sql, Object... args) {
        return new SpglspjdxxbService().findList(sql, args);
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
    public List<Spglspjdxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglspjdxxbService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public void deleteBySpGuid(String rowguid) {
        new SpglspjdxxbService().deleteBySpGuid(rowguid);
    }

    @Override
    public AuditCommonResult<PageData<Spglspjdxxb>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                 Integer pageSize, String sortField, String sortOrder) {
        SpglspjdxxbService spglDfxmsplcjdxxbService = new SpglspjdxxbService();
        AuditCommonResult<PageData<Spglspjdxxb>> result = new AuditCommonResult<PageData<Spglspjdxxb>>();
        PageData<Spglspjdxxb> pageData = new PageData<>();
        try {
            pageData = spglDfxmsplcjdxxbService.getAllRecordByPage(Spglspjdxxb.class, conditionMap, first,
                    pageSize, sortField, sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Spglspjdxxb>> getListByCondition(Map<String, String> conditionMap) {
        SpglspjdxxbService spglDfxmsplcjdxxbService = new SpglspjdxxbService();
        AuditCommonResult<List<Spglspjdxxb>> result = new AuditCommonResult<List<Spglspjdxxb>>();
        List<Spglspjdxxb> list = new ArrayList<>();
        try {
            list = spglDfxmsplcjdxxbService.getAllRecord(Spglspjdxxb.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SpglspjdxxbService spglDfxmsplcjdxxbService = new SpglspjdxxbService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = spglDfxmsplcjdxxbService.getAllRecordCount(SpglDfxmsplcjdxxb.class, conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getPhaseName(String splcbm, Double splcbbh, String xzqhdm, int spjdxh) {
        SpglspjdxxbService spglDfxmsplcjdxxbService = new SpglspjdxxbService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        String phaseName = spglDfxmsplcjdxxbService.getPhaseName(splcbm, splcbbh, xzqhdm, spjdxh);
        result.setResult(phaseName);
        return result;
    }

}
