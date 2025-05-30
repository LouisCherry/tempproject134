package com.epoint.zwdt.xmgxh.basic.spglv3.inter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglXmspsxblxxbV3;

/**
 * 住建部_项目审批事项办理信息表对应的后台service接口
 * 
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:08:57]
 */
public interface ISpglXmspsxblxxbV3 extends Serializable
{

    /**
     * 插入住建部_项目审批事项办理其他环节信息数据
     * 
     * @param record
     *            住建部_项目审批事项办理其他环节信息对象 
     * @return int
     */
    public int insert(SpglXmspsxblxxbV3 record);

    /**
     * 删除数据
     * 
     * @param guid
     *            住建部_项目审批事项办理其他环节信息标识
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            住建部_项目审批事项办理其他环节信息对象
     * @return int
     */
    public int update(SpglXmspsxblxxbV3 record);

    /**
     * 根据住建部_项目审批事项办理其他环节信息标识查找单个实体
     * 
     * @param primaryKey
     *            主键
     * @return 
     */
    public SpglXmspsxblxxbV3 find(Object primaryKey);

    /**
     * 根据办件标识查找单个实体
     * 
     * @param projectguid
     *            办件标识
     * @return T extends BaseEntity
     */
    public SpglXmspsxblxxbV3 findByProjectguid(String projectguid);

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return
     */
    public SpglXmspsxblxxbV3 find(String sql, Object... args);

    /**
     * 查找一个住建部_项目审批事项办理其他环节信息列表
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspsxblxxbV3> findList(String sql, Object... args);

    /**
     * 分页查找一个住建部_项目审批事项办理其他环节信息列表
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspsxblxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args);

    public PageData<SpglXmspsxblxxbV3> getAllByPage(String xzqhdm, String gcdm, String shsxmc, Integer sjsczt, int first,
            int pageSize, String sortField, String sortOrder);
 

    /**
     *  判断办件是否推送成功
     *  @param spsxslbm
     *  @return    
     */
    public boolean isExistFlowsn(String spsxslbm);
    
    /**
     * 根据办件标识获取实例
     *  @param spsxslbm
     *  @return    
     */
    public SpglXmspsxblxxbV3 getSpglXmspsxblxxbBySlbm(String spsxslbm);


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
    public AuditCommonResult<PageData<SpglXmspsxblxxbV3>> getAllByPage(Map<String, String> conditionMap, Integer first,
            Integer pageSize, String sortField, String sortOrder);

    /**
     * 
     * 获取列表
     * 
     * @param conditionMap
     *            条件map
     * @return
     */
    public AuditCommonResult<List<SpglXmspsxblxxbV3>> getListByCondition(Map<String, String> conditionMap);

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
