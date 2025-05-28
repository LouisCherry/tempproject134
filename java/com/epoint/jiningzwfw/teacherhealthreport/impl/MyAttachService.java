package com.epoint.jiningzwfw.teacherhealthreport.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

public class MyAttachService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public MyAttachService() {
        baseDao = CommonDao.getInstance();
    }

    public FrameAttachInfo getFrameAttachInfoByCondition(Map<String, String> map) {
        List<String> params = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from frame_attachinfo where 1=1 ");
        if (map != null && !map.isEmpty()) {
            sql.append(new SQLManageUtil().buildPatchSql(map, params));
        }
        return baseDao.find(sql.toString(), FrameAttachInfo.class, params.toArray());
    }

}
