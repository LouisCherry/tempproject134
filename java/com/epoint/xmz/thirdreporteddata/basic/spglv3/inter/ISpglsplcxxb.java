package com.epoint.xmz.thirdreporteddata.basic.spglv3.inter;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcxxb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 住建部_地方项目审批流程信息表对应的后台service接口
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:56]
 */
public interface ISpglsplcxxb extends Serializable {

    /**
     * 插入住建部_地方项目审批流程信息数据
     *
     * @param record 住建部_地方项目审批流程信息对象
     * @return int
     */
    public int insert(Spglsplcxxb record);

    /**
     * 删除住建部_地方项目审批流程信息数据
     *
     * @param guid 住建部_地方项目审批流程信息唯一标识
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新住建部_地方项目审批流程信息数据
     *
     * @param record 住建部_地方项目审批流程信息对象
     * @return int
     */
    public int update(Spglsplcxxb record);

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 住建部_地方项目审批流程信息主键
     * @return T extends BaseEntity
     */
    public Spglsplcxxb find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public Spglsplcxxb find(String sql, Object... args);

    /**
     * 查找一个住建部_地方项目审批流程信息集合
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return
     */
    public List<Spglsplcxxb> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param args       参数值数组
     * @return
     */
    public List<Spglsplcxxb> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 删除记录
     *
     * @param guid 唯一标识
     */
    public void deleteBySpGuid(String guid);

    /**
     * 获取最高版本号
     *
     * @param guid 唯一标识
     * @return
     */
    public Double getMaxSplcbbh(String guid);

    /**
     * 获取流程设置分页
     *
     * @param conditionMap 条件map
     * @param first        第一条记录
     * @param pageSize     页大小
     * @param sortField    排序字段
     * @param sortOrder    排序顺序
     * @return
     */
    public AuditCommonResult<PageData<Spglsplcxxb>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                 Integer pageSize, String sortField, String sortOrder);

    /**
     * 获取流程列表
     *
     * @param conditionMap 条件map
     * @return
     */
    public AuditCommonResult<List<Spglsplcxxb>> getListByCondition(Map<String, String> conditionMap);

    /**
     * 获取流程数量
     *
     * @param conditionMap 条件map
     * @return
     */
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap);

    /**
     * 获取流程有效数据
     *
     * @param splcbm  审批流程编码
     * @param splcbbh 审批流程版本号
     * @return
     */
    public Spglsplcxxb getYxsjBySplcbm(String splcbm, Double splcbbh);

}
