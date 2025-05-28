package com.epoint.xmz.thirdreporteddata.basic.spglv3.inter;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjbxxbV3;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 住建部_项目基本信息表对应的后台service接口
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:08:41]
 */
public interface ISpglXmjbxxbV3 extends Serializable {

    /**
     * 插入数据
     *
     * @param record 住建部_项目基本信息对象
     * @return int
     */
    public int insert(SpglXmjbxxbV3 record);

    /**
     * 删除数据
     *
     * @param guid 住建部_项目基本信息唯一标识
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record 住建部_项目基本信息对象
     * @return int
     */
    public int update(SpglXmjbxxbV3 record);

    /**
     * 根据住建部_项目基本信息唯一标识查找单个实体
     *
     * @param primaryKey 主键
     * @return
     */
    public SpglXmjbxxbV3 find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return
     */
    public SpglXmjbxxbV3 find(String sql, Object... args);

    /**
     * 查找一个住建部_项目基本信息列表
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return
     */
    public List<SpglXmjbxxbV3> findList(String sql, Object... args);

    /**
     * 分页查找一个住建部_项目基本信息集合
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param args       参数值数组
     * @return
     */
    public List<SpglXmjbxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args);


    public SpglXmjbxxbV3 findByLshAndSplclx(String lsh, String splclx);

    /**
     * 单位对应的工程代码 工程代码
     *
     * @param gcdm
     * @return
     */
    public boolean isExistGcdm(String gcdm);

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
    public AuditCommonResult<PageData<SpglXmjbxxbV3>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                   Integer pageSize, String sortField, String sortOrder);

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
    public AuditCommonResult<PageData<SpglXmjbxxbV3>> getAllXmByPage(Map<String, String> conditionMap, Integer first,
                                                                     Integer pageSize, String sortField, String sortOrder);

    /**
     * 获取流程列表
     *
     * @param conditionMap 条件map
     * @return
     */
    public AuditCommonResult<List<SpglXmjbxxbV3>> getListByCondition(Map<String, String> conditionMap);

    /**
     * 获取流程数量
     *
     * @param conditionMap 条件map
     * @return
     */
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap);

    /**
     * 获取流程列表
     *
     * @return
     */
    public AuditCommonResult<List<String>> getGcdmByFailed();


    public int getCountByGcdm(String gcdm);

}
