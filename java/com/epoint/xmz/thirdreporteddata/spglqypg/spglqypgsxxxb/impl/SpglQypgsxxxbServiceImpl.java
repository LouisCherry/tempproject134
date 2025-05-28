package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 区域评估事项信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 14:21:45]
 */
@Component
@Service
public class SpglQypgsxxxbServiceImpl implements ISpglQypgsxxxbService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglQypgsxxxb record) {
        return new SpglQypgsxxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglQypgsxxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglQypgsxxxb record) {
        return new SpglQypgsxxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglQypgsxxxb find(Object primaryKey) {
        return new SpglQypgsxxxbService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglQypgsxxxb find(String sql, Object... args) {
        return new SpglQypgsxxxbService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgsxxxb> findList(String sql, Object... args) {
        return new SpglQypgsxxxbService().findList(sql, args);
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
    public List<SpglQypgsxxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglQypgsxxxbService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglQypgsxxxb(String sql, Object... args) {
        return new SpglQypgsxxxbService().countSpglQypgsxxxb(sql, args);
    }

    @Override
    public AuditCommonResult<PageData<SpglQypgsxxxb>> getAuditSpDanitemByPage(Map<String, String> conditionMap,
                                                                              int firstResult, int maxResults, String sortField, String sortOrder) {
        SpglQypgsxxxbService sQypgxxbService = new SpglQypgsxxxbService();
        AuditCommonResult<PageData<SpglQypgsxxxb>> result = new AuditCommonResult<PageData<SpglQypgsxxxb>>();
        result.setResult(
                sQypgxxbService.getAuditSpDanitemByPage(conditionMap, firstResult, maxResults, sortField, sortOrder));
        return result;
    }

    @Override
    public int deleteByQypgGuid(String qypgguid) {
        return new SpglQypgsxxxbService().deleteByQypgGuid(qypgguid);
    }

    /**
     * 获取list
     *
     * @param map
     * @return
     */
    @Override
    public List<SpglQypgsxxxb> getListByMap(Map<String, String> map) {
        return new SpglQypgsxxxbService().getListByMap(map);
    }
}
