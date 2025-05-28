package com.epoint.xmz.filter.login.api;

import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;

import java.io.Serializable;

public interface ICommonUserService extends Serializable {

    /**
     * 根据用户登录名查询用户扩展表
     * @param loginid
     * @return
     */
    FrameUserExtendInfo getExtendinfoByLoginid(String loginid);

}
