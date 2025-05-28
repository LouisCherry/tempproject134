package com.epoint.mq.spgl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.mq.spgl.api.IJnSpglDfxmsplcjdsxxxb;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 住建部_地方项目审批流程阶段事项信息表对应的后台service实现类
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:10:10]
 */
@Component
@Service
public class JnSpglDfxmsplcjdsxxxbImpl implements IJnSpglDfxmsplcjdsxxxb {
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
    @Override
    public int insert(SpglDfxmsplcjdsxxxb record) {
        return new JnSpglDfxmsplcjdsxxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new JnSpglDfxmsplcjdsxxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(SpglDfxmsplcjdsxxxb record) {
        return new JnSpglDfxmsplcjdsxxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    @Override
    public SpglDfxmsplcjdsxxxb find(Object primaryKey) {
        return new JnSpglDfxmsplcjdsxxxbService().find(primaryKey);
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
    @Override
    public SpglDfxmsplcjdsxxxb find(String sql, Object... args) {
        return new JnSpglDfxmsplcjdsxxxbService().find(args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    @Override
    public List<SpglDfxmsplcjdsxxxb> findList(String sql, Object... args) {
        return new JnSpglDfxmsplcjdsxxxbService().findList(sql, args);
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
    @Override
    public List<SpglDfxmsplcjdsxxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new JnSpglDfxmsplcjdsxxxbService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public void deleteBySpGuid(String rowguid) {
        new JnSpglDfxmsplcjdsxxxbService().deleteBySpGuid(rowguid);
    }

    @Override
    public AuditCommonResult<PageData<SpglDfxmsplcjdsxxxb>> getAllByPage(Map<String, String> conditionMap,
                                                                         Integer first, Integer pageSize, String sortField, String sortOrder) {
        JnSpglDfxmsplcjdsxxxbService spglDfxmsplcjdsxxxbService = new JnSpglDfxmsplcjdsxxxbService();
        AuditCommonResult<PageData<SpglDfxmsplcjdsxxxb>> result = new AuditCommonResult<PageData<SpglDfxmsplcjdsxxxb>>();
        PageData<SpglDfxmsplcjdsxxxb> pageData = new PageData<>();
        try {
            pageData = spglDfxmsplcjdsxxxbService.getAllRecordByPage(SpglDfxmsplcjdsxxxb.class, conditionMap, first,
                    pageSize, sortField, sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<SpglDfxmsplcjdsxxxb>> getListByCondition(Map<String, String> conditionMap) {
        JnSpglDfxmsplcjdsxxxbService spglDfxmsplcjdsxxxbService = new JnSpglDfxmsplcjdsxxxbService();
        AuditCommonResult<List<SpglDfxmsplcjdsxxxb>> result = new AuditCommonResult<List<SpglDfxmsplcjdsxxxb>>();
        List<SpglDfxmsplcjdsxxxb> list = new ArrayList<>();
        try {
            list = spglDfxmsplcjdsxxxbService.getAllRecord(SpglDfxmsplcjdsxxxb.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        JnSpglDfxmsplcjdsxxxbService spglDfxmsplcjdsxxxbService = new JnSpglDfxmsplcjdsxxxbService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = spglDfxmsplcjdsxxxbService.getAllRecordCount(SpglDfxmsplcjdsxxxb.class, conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public boolean isExistSplcSx(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm) {
        JnSpglDfxmsplcjdsxxxbService spglDfxmsplcjdsxxxbService = new JnSpglDfxmsplcjdsxxxbService();
        return spglDfxmsplcjdsxxxbService.isExistSplcSx(splcbbh, splcbm, spsxbbh, spsxbm);
    }

    @Override
    public List<SpglDfxmsplcjdsxxxb> getNeedAddNewVersionByItemId(String item_id) {
        return new JnSpglDfxmsplcjdsxxxbService().getNeedAddNewVersionByItemId(item_id);
    }

    @Override
    public Record getAuditSpBaseTaskInfo(String dfsjzj) {
        return new JnSpglDfxmsplcjdsxxxbService().getAuditSpBaseTaskInfo(dfsjzj);
    }

    @Override
    public String getMaxSpsxbbh(Double splcbbh, String splcbm, String spsxbm) {
        return new JnSpglDfxmsplcjdsxxxbService().getMaxSpsxbbh(splcbbh, splcbm, spsxbm);
    }

    @Override
    public String getMaxSpsxbbhV3(Double splcbbh, String splcbm, String spsxbm) {
        return new JnSpglDfxmsplcjdsxxxbService().getMaxSpsxbbhV3(splcbbh, splcbm, spsxbm);
    }

    @Override
    public List<String> getBasetaskBySplclxAndPhaseid(String splclx, String phaseid) {
        return new JnSpglDfxmsplcjdsxxxbService().getBasetaskBySplclxAndPhaseid(splclx, phaseid);
    }

}
