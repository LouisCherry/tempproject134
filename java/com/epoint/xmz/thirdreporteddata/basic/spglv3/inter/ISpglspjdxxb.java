package com.epoint.xmz.thirdreporteddata.basic.spglv3.inter;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglspjdxxb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 住建部_地方项目审批流程阶段信息表对应的后台service接口
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:10:04]
 */
public interface ISpglspjdxxb extends Serializable {

    /**
     * 插入数据
     *
     * @param record 住建部_地方项目审批流程阶段信息对象
     * @return int
     */
    public int insert(Spglspjdxxb record);

    /**
     * 删除数据
     *
     * @param guid 住建部_地方项目审批流程阶段信息对象
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record 住建部_地方项目审批流程阶段信息对象
     * @return int
     */
    public int update(Spglspjdxxb record);

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 住建部_地方项目审批流程阶段信息主键
     * @return
     */
    public Spglspjdxxb find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public Spglspjdxxb find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<Spglspjdxxb> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<Spglspjdxxb> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 通过编号删除记录
     *
     * @param rowguid 标识编号
     */
    public void deleteBySpGuid(String rowguid);

    /**
     * 获取流程阶段设置分页
     *
     * @param conditionMap 条件map
     * @param first        第一条记录
     * @param pageSize     页大小
     * @param sortField    排序字段
     * @param sortOrder    排序顺序
     * @return
     */
    public AuditCommonResult<PageData<Spglspjdxxb>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                 Integer pageSize, String sortField, String sortOrder);

    /**
     * 获取流程阶段列表
     *
     * @param conditionMap 条件map
     * @return
     */
    public AuditCommonResult<List<Spglspjdxxb>> getListByCondition(Map<String, String> conditionMap);

    /**
     * 获取流程阶段数量
     *
     * @param conditionMap 条件map
     * @return
     */
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap);

    /**
     * 获取流程阶段名称
     *
     * @param splcbm  审批流程编码
     * @param splcbbh 审批流程版本号
     * @param xzqhdm  行政区划代码
     * @param spjdxh  审批阶段序号
     * @return
     */
    public AuditCommonResult<String> getPhaseName(String splcbm, Double splcbbh, String xzqhdm, int spjdxh);

}
