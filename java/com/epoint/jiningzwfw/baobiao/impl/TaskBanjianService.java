package com.epoint.jiningzwfw.baobiao.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.jiningzwfw.auditsptasktype.api.entity.AuditSpTasktypeR;

import java.util.List;
import java.util.Map;

/**
 * 工改事项分类关联表对应的后台service
 *
 * @author qichudong
 * @version [版本号, 2024-09-22 11:34:59]
 */
public class TaskBanjianService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public TaskBanjianService() {
        baseDao = CommonDao.getInstance();
    }


    public Integer countTask(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }


}
