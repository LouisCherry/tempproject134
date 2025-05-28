package com.epoint.xmz.thirdreporteddata.basic.spglv3.inter;

import com.epoint.core.BaseEntity;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmqqyjxxbV3;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 项目前期意见信息表对应的后台service接口
 */
public interface ISpglXmqqyjxxbV3 extends Serializable {

    /**
     * 根据条件查询SpglXmqqyjxxb列表数据
     *
     * @param conditionMap 查询条件
     * @param baseClass    实体
     * @return SpglXmqqyjxxb列表数据
     */
    List<SpglXmqqyjxxbV3> getXmqqyjByCondition(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap);

    /**
     * 向项目前期意见信息表中插入一条数据
     *
     * @param record 项目前期意见信息实体
     * @return 是否成功插入成功
     */
    public int insert(SpglXmqqyjxxbV3 record);

    /**
     * 更新项目前期意见信息
     *
     * @param record 项目前期意见信息对象
     * @return 是否成功更新成功
     */
    public int update(SpglXmqqyjxxbV3 record);

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 项目前期意见信息表主键
     * @return 前期意见信息实体
     */
    public SpglXmqqyjxxbV3 find(String primaryKey);

    /**
     * 分页查询数据
     * [一句话功能简述]
     *
     * @param chcode
     * @param first
     * @param pageSize
     * @return
     */
    PageData<SpglXmqqyjxxbV3> getQqyjListByPage(String chcode, int first, int pageSize);

    /**
     * 获取前期意见表
     */
    public List<SpglXmqqyjxxbV3> selectXmqqyjb(String qqyjcode, Integer pagesize, Integer pageindex);

    public Integer countXmqqyjb(String qqyjcode);

}
