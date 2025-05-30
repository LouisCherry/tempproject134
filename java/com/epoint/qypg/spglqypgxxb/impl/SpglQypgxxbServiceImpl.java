package com.epoint.qypg.spglqypgxxb.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.qypg.spglqypgsxxxb.impl.SpglQypgsxxxbService;
import com.epoint.qypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.qypg.spglqypgxxb.api.entity.SpglQypgxxb;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 区域评估信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 13:56:34]
 */
@Component
@Service
public class SpglQypgxxbServiceImpl implements ISpglQypgxxbService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglQypgxxb record) {
        return new SpglQypgxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        // 删除区域评估信息同时删除关联事项
        SpglQypgsxxxbService spglQypgsxxxbService = new SpglQypgsxxxbService();
        spglQypgsxxxbService.deleteByQypgGuid(guid);
        return new SpglQypgxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglQypgxxb record) {
        return new SpglQypgxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglQypgxxb find(Object primaryKey) {
        return new SpglQypgxxbService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglQypgxxb find(String sql, Object... args) {
        return new SpglQypgxxbService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgxxb> findList(String sql, Object... args) {
        return new SpglQypgxxbService().findList(sql, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglQypgxxbService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglQypgxxb(String sql, Object... args) {
        return new SpglQypgxxbService().countSpglQypgxxb(sql, args);
    }

    @Override
    public AuditCommonResult<PageData<SpglQypgxxb>> getAuditSpDanitemByPage(Map<String, String> conditionMap,
                                                                            int firstResult, int maxResults, String sortField, String sortOrder) {
        SpglQypgxxbService sQypgxxbService = new SpglQypgxxbService();
        AuditCommonResult<PageData<SpglQypgxxb>> result = new AuditCommonResult<PageData<SpglQypgxxb>>();
        result.setResult(
                sQypgxxbService.getAuditSpDanitemByPage(conditionMap, firstResult, maxResults, sortField, sortOrder));
        return result;
    }

    /**
     * 获取list
     *
     * @param map
     * @return
     */
    @Override
    public List<SpglQypgxxb> getListByMap(Map<String, String> map) {
        return new SpglQypgxxbService().getListByMap(map);
    }

    /**
     * 获取未关联列表
     *
     * @param firstResult
     * @param maxResults
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public PageData<SpglQypgxxb> getNotAssociationPageData(String itemguid, String subappguid, int firstResult,
                                                           int maxResults, String sortField, String sortOrder) {
        return new SpglQypgxxbService().getNotAssociationPageData(itemguid, subappguid, firstResult, maxResults, sortField,
                sortOrder);
    }

}
