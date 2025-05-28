package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.BaseEntity;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmqqyjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmqqyjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglXmqqyjxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 项目前期意见信息表对应的后台service接口
 *
 * @author lmzhu
 * @date [2020-08-05 11:08:49]
 */
@Component
@Service
public class SpglXmqqyjxxbV3Impl implements ISpglXmqqyjxxbV3 {

    /**
     *
     */
    private static final long serialVersionUID = 6391027852129436843L;

    /**
     * 根据条件查询SpglXmqqyjxxb列表数据
     *
     * @param conditionMap 查询条件
     * @param baseClass    实体
     * @return SpglXmqqyjxxb列表数据
     */
    @Override
    public List<SpglXmqqyjxxbV3> getXmqqyjByCondition(Class<? extends BaseEntity> baseClass,
                                                      Map<String, String> conditionMap) {
        return new SpglXmqqyjxxbV3Service().getXmqqyjByCondition(baseClass, conditionMap);
    }

    /**
     * 向项目前期意见信息表中插入一条数据
     *
     * @param record 项目前期意见信息实体
     * @return 是否成功插入成功
     */
    @Override
    public int insert(SpglXmqqyjxxbV3 record) {
        return new SpglXmqqyjxxbV3Service().insert(record);
    }

    /**
     * 更新项目前期意见信息
     *
     * @param record 项目前期意见信息对象
     * @return 是否成功更新成功
     */
    @Override
    public int update(SpglXmqqyjxxbV3 record) {
        return new SpglXmqqyjxxbV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 项目前期意见信息表主键
     * @return 前期意见信息实体
     */
    @Override
    public SpglXmqqyjxxbV3 find(String primaryKey) {
        return new SpglXmqqyjxxbV3Service().find(primaryKey);
    }

    /**
     * 分页查询数据 [一句话功能简述]
     *
     * @param class1
     * @param map
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public PageData<SpglXmqqyjxxbV3> getQqyjListByPage(String chcode, int first, int pageSize) {
        return new SpglXmqqyjxxbV3Service().getQqyjListByPage(chcode, first, pageSize);
    }

    @Override
    public List<SpglXmqqyjxxbV3> selectXmqqyjb(String qqyjcode, Integer pagesize, Integer pageindex) {
        return new SpglXmqqyjxxbV3Service().selectXmqqyjb(qqyjcode, pagesize, pageindex);
    }

    @Override
    public Integer countXmqqyjb(String qqyjcode) {
        return new SpglXmqqyjxxbV3Service().countXmqqyjb(qqyjcode);
    }

}
