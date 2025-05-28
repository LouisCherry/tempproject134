package com.epoint.xmz.thirdreporteddata.basic.spglv3.service;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmqqyjxxbV3;

import java.util.List;
import java.util.Map;

/**
 * 项目前期意见信息表对应的后台service接口
 *
 * @author lmzhu
 * @date [2020-08-05 11:08:49]
 */
public class SpglXmqqyjxxbV3Service {

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglXmqqyjxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 根据条件查询SpglXmqqyjxxb列表数据
     *
     * @param conditionMap 查询条件
     * @param baseClass    实体
     * @return SpglXmqqyjxxb列表数据
     */
    public List<SpglXmqqyjxxbV3> getXmqqyjByCondition(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }


    /**
     * 向项目前期意见信息表中插入一条数据
     *
     * @param record 项目前期意见信息实体
     * @return 是否成功插入成功
     */
    public int insert(SpglXmqqyjxxbV3 record) {
        return baseDao.insert(record);
    }

    /**
     * 更新项目前期意见信息
     *
     * @param record 项目前期意见信息对象
     * @return 是否成功更新成功
     */
    public int update(SpglXmqqyjxxbV3 record) {
        return baseDao.update(record);
    }


    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 项目前期意见信息表主键
     * @return 前期意见信息实体
     */
    public SpglXmqqyjxxbV3 find(String primaryKey) {
        return baseDao.find(SpglXmqqyjxxbV3.class, primaryKey);
    }

    /**
     * 分页查询数据
     * [一句话功能简述]
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
    public PageData<SpglXmqqyjxxbV3> getQqyjListByPage(String chcode, int first, int pageSize) {
        String sql = "select * from SPGL_XMQQYJXXB_V3 where qqyjcode = ?";
        List<SpglXmqqyjxxbV3> findList = baseDao.findList(sql, first, pageSize, SpglXmqqyjxxbV3.class, chcode);
        String sql2 = "select count(*) from SPGL_XMQQYJXXB_V3 where qqyjcode = ?";
        Integer count = baseDao.queryInt(sql2, chcode);
        return new PageData<SpglXmqqyjxxbV3>(findList, count);
    }

    ;

    public List<SpglXmqqyjxxbV3> selectXmqqyjb(String qqyjcode, Integer pagesize, Integer pageindex) {
        String sql = "select * from spgl_xmqqyjxxb_v3 where qqyjcode = ? ";
        return baseDao.findList(sql, pagesize * pageindex, pagesize, SpglXmqqyjxxbV3.class);
    }

    public Integer countXmqqyjb(String qqyjcode) {
        String sql = "select count(1) from spgl_xmqqyjxxb_v3 where qqyjcode = ? ";
        return baseDao.queryInt(sql, qqyjcode);
    }
}
