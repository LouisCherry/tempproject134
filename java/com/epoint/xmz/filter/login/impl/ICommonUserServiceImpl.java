package com.epoint.xmz.filter.login.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;
import com.epoint.xmz.filter.login.api.ICommonUserService;
import org.springframework.stereotype.Component;

/**
 * 国土用户管理表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-10-06 12:05:00]
 */
@Component
@Service
public class ICommonUserServiceImpl implements ICommonUserService {

    @Override
    public FrameUserExtendInfo getExtendinfoByLoginid(String loginid){
        return new CommonUserService().getExtendinfoByLoginid(loginid);
    }

}
