package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxjbxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspsxjbxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglspsxjbxxbService;
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
public class SpglspsxjbxxbImpl implements ISpglspsxjbxxb {
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
    public int insert(SpglSpsxjbxxb record) {
        return new SpglspsxjbxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglspsxjbxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglSpsxjbxxb record) {
        return new SpglspsxjbxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglSpsxjbxxb find(Object primaryKey) {
        return new SpglspsxjbxxbService().find(primaryKey);
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
    public SpglSpsxjbxxb find(String sql, Object... args) {
        return new SpglspsxjbxxbService().find(args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglSpsxjbxxb> findList(String sql, Object... args) {
        return new SpglspsxjbxxbService().findList(sql, args);
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
    public List<SpglSpsxjbxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglspsxjbxxbService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public void deleteBySpGuid(String rowguid) {
        new SpglspsxjbxxbService().deleteBySpGuid(rowguid);
    }

    @Override
    public AuditCommonResult<PageData<SpglSpsxjbxxb>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                   Integer pageSize, String sortField, String sortOrder) {
        SpglspsxjbxxbService SpglsplcjdsxxxbService = new SpglspsxjbxxbService();
        AuditCommonResult<PageData<SpglSpsxjbxxb>> result = new AuditCommonResult<PageData<SpglSpsxjbxxb>>();
        PageData<SpglSpsxjbxxb> pageData = new PageData<>();
        try {
            pageData = SpglsplcjdsxxxbService.getAllRecordByPage(SpglSpsxjbxxb.class, conditionMap, first, pageSize,
                    sortField, sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<SpglSpsxjbxxb>> getListByCondition(Map<String, String> conditionMap) {
        SpglspsxjbxxbService SpglsplcjdsxxxbService = new SpglspsxjbxxbService();
        AuditCommonResult<List<SpglSpsxjbxxb>> result = new AuditCommonResult<List<SpglSpsxjbxxb>>();
        List<SpglSpsxjbxxb> list = new ArrayList<>();
        try {
            list = SpglsplcjdsxxxbService.getAllRecord(SpglSpsxjbxxb.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SpglspsxjbxxbService SpglsplcjdsxxxbService = new SpglspsxjbxxbService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = SpglsplcjdsxxxbService.getAllRecordCount(SpglSpsxjbxxb.class, conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isExistSplcSx(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm) {
        SpglspsxjbxxbService SpglsplcjdsxxxbService = new SpglspsxjbxxbService();
        return SpglsplcjdsxxxbService.isExistSplcSx(splcbbh, splcbm, spsxbbh, spsxbm);
    }

    @Override
    public SpglSpsxjbxxb getSpglsplcjdsxxxb(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm) {
        SpglspsxjbxxbService SpglsplcjdsxxxbService = new SpglspsxjbxxbService();
        return SpglsplcjdsxxxbService.getSpglsplcjdsxxxb(splcbbh, splcbm, spsxbbh, spsxbm);
    }

    @Override
    public AuditCommonResult<List<SpglSpsxjbxxb>> getNeedAddNewVersionByItemId(String item_id) {
        SpglspsxjbxxbService SpglsplcjdsxxxbService = new SpglspsxjbxxbService();
        AuditCommonResult<List<SpglSpsxjbxxb>> result = new AuditCommonResult<List<SpglSpsxjbxxb>>();
        result.setResult(SpglsplcjdsxxxbService.getNeedAddNewVersionByItemId(item_id));
        return result;
    }

}
