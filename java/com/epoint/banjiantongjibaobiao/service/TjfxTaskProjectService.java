package com.epoint.banjiantongjibaobiao.service;

import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.banjiantongjibaobiao.api.TjfxTaskProject;

import java.util.List;

public class TjfxTaskProjectService {
    private ICommonDao baseDao;

    public TjfxTaskProjectService() {
        if (baseDao == null) {
            this.baseDao = CommonDao.getInstance();
        }

    }

    public List<String> getAllOuList(String ouGuid, boolean isParentNode) {
        String sql = "select ouguid from frame_ou where ouguid=? ";
        if (isParentNode) {
            sql += "or parentouguid=? ";
        }
        return baseDao.findList(sql, String.class, ouGuid, ouGuid);
    }


    public List<? extends BaseEntity> findTaskList(String sql, List<Object> conditionParams, int first, int pageSize) {
        return baseDao.findList(sql, first, pageSize, TjfxTaskProject.class, conditionParams.toArray());
    }

    public int getTaskListCount(String sql, List<Object> conditionParams) {
        return baseDao.queryInt(sql, conditionParams.toArray());

    }
}
