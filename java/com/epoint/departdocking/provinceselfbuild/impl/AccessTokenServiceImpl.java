package com.epoint.departdocking.provinceselfbuild.impl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.departdocking.provinceselfbuild.api.IAccessTokenService;
import com.epoint.departdocking.provinceselfbuild.api.entity.AccessToken;
import com.epoint.departdocking.provinceselfbuild.constant.OwnerType;

/**
 * 身份认证服务
 * @author 刘雨雨
 * @time 2018年9月14日下午2:55:07
 */
@Component
@Service
public class AccessTokenServiceImpl implements IAccessTokenService
{

    @Override
    public boolean validateToken(String token) {
        return new AccessTokenService().validateToken(token);
    }

    @Override
    public String getToken(String appKey, String appSecret) {
        return new AccessTokenService().getToken(appKey, appSecret);
    }

    @Override
    public AccessToken findAccessTokenByToken(String token) {
        return new AccessTokenService().findAccessTokenByToken(token);
    }

    @Override
    public String generateToken(String appKey, String appSecret, OwnerType ownerType) {
        return new AccessTokenService().generateToken(appKey, appSecret, ownerType);
    }
}
