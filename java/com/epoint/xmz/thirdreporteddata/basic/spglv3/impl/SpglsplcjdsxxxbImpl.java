package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglsplcjdsxxxbService;
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
public class SpglsplcjdsxxxbImpl implements ISpglsplcjdsxxxb {
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
    public int insert(Spglsplcjdsxxxb record) {
        return new SpglsplcjdsxxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglsplcjdsxxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Spglsplcjdsxxxb record) {
        return new SpglsplcjdsxxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public Spglsplcjdsxxxb find(Object primaryKey) {
        return new SpglsplcjdsxxxbService().find(primaryKey);
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
    public Spglsplcjdsxxxb find(String sql, Object... args) {
        return new SpglsplcjdsxxxbService().find(args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<Spglsplcjdsxxxb> findList(String sql, Object... args) {
        return new SpglsplcjdsxxxbService().findList(sql, args);
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
    public List<Spglsplcjdsxxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglsplcjdsxxxbService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public void deleteBySpGuid(String rowguid) {
        new SpglsplcjdsxxxbService().deleteBySpGuid(rowguid);
    }

    @Override
    public AuditCommonResult<PageData<Spglsplcjdsxxxb>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                     Integer pageSize, String sortField, String sortOrder) {
        SpglsplcjdsxxxbService SpglsplcjdsxxxbService = new SpglsplcjdsxxxbService();
        AuditCommonResult<PageData<Spglsplcjdsxxxb>> result = new AuditCommonResult<PageData<Spglsplcjdsxxxb>>();
        PageData<Spglsplcjdsxxxb> pageData = new PageData<>();
        try {
            pageData = SpglsplcjdsxxxbService.getAllRecordByPage(Spglsplcjdsxxxb.class, conditionMap, first, pageSize,
                    sortField, sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Spglsplcjdsxxxb>> getListByCondition(Map<String, String> conditionMap) {
        SpglsplcjdsxxxbService SpglsplcjdsxxxbService = new SpglsplcjdsxxxbService();
        AuditCommonResult<List<Spglsplcjdsxxxb>> result = new AuditCommonResult<List<Spglsplcjdsxxxb>>();
        List<Spglsplcjdsxxxb> list = new ArrayList<>();
        try {
            list = SpglsplcjdsxxxbService.getAllRecord(Spglsplcjdsxxxb.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SpglsplcjdsxxxbService SpglsplcjdsxxxbService = new SpglsplcjdsxxxbService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer count = 0;
        try {
            count = SpglsplcjdsxxxbService.getAllRecordCount(Spglsplcjdsxxxb.class, conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isExistSplcSx(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm) {
        SpglsplcjdsxxxbService SpglsplcjdsxxxbService = new SpglsplcjdsxxxbService();
        return SpglsplcjdsxxxbService.isExistSplcSx(splcbbh, splcbm, spsxbbh, spsxbm);
    }

    @Override
    public Spglsplcjdsxxxb getSpglsplcjdsxxxb(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm) {
        SpglsplcjdsxxxbService SpglsplcjdsxxxbService = new SpglsplcjdsxxxbService();
        return SpglsplcjdsxxxbService.getSpglsplcjdsxxxb(splcbbh, splcbm, spsxbbh, spsxbm);
    }

    @Override
    public AuditCommonResult<List<Spglsplcjdsxxxb>> getNeedAddNewVersionByItemId(String item_id) {
        SpglsplcjdsxxxbService SpglsplcjdsxxxbService = new SpglsplcjdsxxxbService();
        AuditCommonResult<List<Spglsplcjdsxxxb>> result = new AuditCommonResult<List<Spglsplcjdsxxxb>>();
        result.setResult(SpglsplcjdsxxxbService.getNeedAddNewVersionByItemId(item_id));
        return result;
    }

    @Override
    public void close() {
        SpglsplcjdsxxxbService SpglsplcjdsxxxbService = new SpglsplcjdsxxxbService();
        SpglsplcjdsxxxbService.close();
    }

}
