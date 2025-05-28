package com.epoint.xmz.filter.login.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;

public class CommonUserService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public CommonUserService() {
        baseDao = CommonDao.getInstance();
    }

    public FrameUserExtendInfo getExtendinfoByLoginid(String loginid) {
        String sql = "select b.* from frame_user a,Frame_User_ExtendInfo b where a.userguid = b.userguid and a.loginid = ? ";
        return baseDao.find(sql,FrameUserExtendInfo.class,loginid);
    }

}
