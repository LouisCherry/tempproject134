package com.epoint.zwdt.xmgxh.basic.spglv3.inter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglxmsqcljqtfjxxbV3;

/**
 * 住建部_项目其他附件信息表对应的后台service接口
 * 
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:37]
 */
public interface ISpglsqcljqtfjxxbV3 extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            住建部_项目其他附件信息对象
     * @return int
     */
    public int insert(SpglxmsqcljqtfjxxbV3 record);

    /**
     * 删除数据
     * 
     * @param guid
     *            住建部_项目其他附件信息标识
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            住建部_项目其他附件信息对象
     * @return int
     */
    public int update(SpglxmsqcljqtfjxxbV3 record);

    /**
     * 根据ID查找单个实体
     * 
     * @param primaryKey
     *            住建部_项目其他附件信息主键
     * @return 
     */
    public SpglxmsqcljqtfjxxbV3 find(Object primaryKey);

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglxmsqcljqtfjxxbV3 find(String sql, Object... args);

    /**
     * 查找一个住建部_项目其他附件信息列表
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return 
     */
    public List<SpglxmsqcljqtfjxxbV3> findList(String sql, Object... args);

    /**
     * 分页查找一个住建部_项目其他附件信息列表
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param args
     *            参数值数组
     * @return
     */
    public List<SpglxmsqcljqtfjxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args);

    public AuditCommonResult<PageData<SpglxmsqcljqtfjxxbV3>> getAllByPage(Map<String, String> map, int first, int pageSize,
                                                                          String sortField, String sortOrder);

    /**
     * 
     * 获取设置分页
     * 
     * @param conditionMap
     *            条件map
     * @param first
     *            第一条记录
     * @param pageSize
     *            页大小
     * @param sortField
     *            排序字段
     * @param sortOrder
     *            排序顺序
     * @return
     */
    public AuditCommonResult<PageData<SpglxmsqcljqtfjxxbV3>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                          Integer pageSize, String sortField, String sortOrder);

    /**
     * 
     * 获取列表
     * 
     * @param conditionMap
     *            条件map
     * @return
     */
    public AuditCommonResult<List<SpglxmsqcljqtfjxxbV3>> getListByCondition(Map<String, String> conditionMap);

    /**
     * 
     * 获取数量
     * 
     * @param conditionMap
     *            条件map
     * @return
     */
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap);

}
