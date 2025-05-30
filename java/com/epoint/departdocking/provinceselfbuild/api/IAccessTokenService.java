package com.epoint.departdocking.provinceselfbuild.api;

import java.io.Serializable;

import com.epoint.departdocking.provinceselfbuild.api.entity.AccessToken;
import com.epoint.departdocking.provinceselfbuild.constant.OwnerType;

/**
 * 身份认证服务
 * @author 刘雨雨
 * @time 2018年9月14日下午2:55:07
 */
public interface IAccessTokenService extends Serializable
{

    /**
     * 校验token是否有效
     * @param accessToken
     * @return
     */
    boolean validateToken(String accessToken);

    /**
     * 生成token，并将其存入数据，有效期2小时
     * @param appKey
     * @param appSecret
     * @return
     */
    public String getToken(String appKey, String appSecret);

    /**
     * 生成token,每次调用都返回不同的结果
     * @param appKey
     * @param appSecret
     * @param ownerType
     * @return
     */
    public String generateToken(String appKey, String appSecret, OwnerType ownerType);

    /**
     * 根据token获取AccessToken对象
     * @param token
     * @return
     */
    public AccessToken findAccessTokenByToken(String token);
}
