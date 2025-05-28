package com.epoint.banjiantongjibaobiao.api;

import com.epoint.core.BaseEntity;

import java.util.List;

/**
 * 事项办件统计表API
 */
public interface ITjfxTaskProjectService {
    /**
     * 获取指定部门GUID的下属部门guid列表
     *
     * @param ouGuid       节点的GUID
     * @param isParentNode 是否是父节点
     * @return
     */
    List<String> getAllOuList(String ouGuid, boolean isParentNode);

    /**
     * @param sql             拼接好的sql语句
     * @param conditionParams sql中的条件
     * @param first           分页
     * @param pageSize        每页数量
     * @return
     */
    List<? extends BaseEntity> findTaskList(String sql, List<Object> conditionParams, int first, int pageSize);

    /**
     * @param sql             条件
     * @param conditionParams 参数集合
     * @return 获取到的数据总量
     */
    int getTaskListCount(String sql, List<Object> conditionParams);

}
