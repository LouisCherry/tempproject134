package com.epoint.departdocking.provinceselfbuild.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import com.epoint.basic.authentication.UserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.departdocking.provinceselfbuild.api.entity.AccessToken;
import com.epoint.departdocking.provinceselfbuild.constant.OwnerType;

/**
 * 身份认证服务
 * @author 刘雨雨
 * @time 2018年9月14日下午2:55:07
 */
public class AccessTokenService
{

    /**
     * token有效期 单位 :小时
     */
    private static final int TOKEN_VALIDITY = 2;

    public boolean validateToken(String token) {
        AccessToken accessToken = findAccessTokenByToken(token);
        if (accessToken == null) {
            return false;
        }
        else {

            //            if (System.currentTimeMillis() < accessToken.getTokenEndTime()) {
            //                return true;
            //            }
            //            else {
            //                return false;
            //            }
            return true;
        }
    }

    public String getToken(String appKey, String appSecret) {
        validateAppKey(appKey, appSecret);
        OwnerType ownerType = OwnerType.SELF_BUILD;
        if (existApp(appKey, appSecret, ownerType)) {
            return updateToken(appKey, appSecret, ownerType);
        }
        else {
            return addToken(appKey, appSecret, ownerType);
        }
    }

    /**
     * 更新token，不管tokne是否过期，都产生1个新的token
     * @param appKey
     * @param appSecret
     * @param ownerType
     * @return
     */
    private String updateToken(String appKey, String appSecret, OwnerType ownerType) {
        CommonDao commonDao = CommonDao.getInstance();
        String sql = "update taian_access_token set token = ?,tokenGenerateTime=?,tokenEndTime=?"
                + " where appKey = ? and appSecret = ? and ownerType = ?";
        String token = generateToken(appKey, appSecret, ownerType);
        long tokenGenerateTime = System.currentTimeMillis();
        long tokenEndTime = tokenGenerateTime + TOKEN_VALIDITY * 3600 * 1000;
        commonDao.execute(sql, token, tokenGenerateTime, tokenEndTime, appKey, appSecret, ownerType.toString());
        return token;
    }

    private boolean existApp(String appKey, String appSecret, OwnerType ownerType) {
        CommonDao commonDao = CommonDao.getInstance();
        String sql = "select count(*) from taian_access_token where appKey = ? and appSecret = ? and ownerType = ?";
        return commonDao.queryInt(sql, appKey, appSecret, ownerType.getValue()) > 0;
    }

    /**
     * 新增一条AccessToken记录
     * @param appKey
     * @param appSecret
     * @param ownerType
     * @return
     */
    private String addToken(String appKey, String appSecret, OwnerType ownerType) {
        CommonDao commonDao = CommonDao.getInstance();
        AccessToken accessToken = new AccessToken();
        accessToken.setAppKey(appKey);
        accessToken.setAppSecret(appSecret);
        accessToken.setCreateDate(new Date());
        accessToken.setModifyDate(null);
        accessToken.setOperateUserName(UserSession.getInstance().getDisplayName());
        accessToken.setOwnerType(ownerType.toString());
        accessToken.setRowguid(UUID.randomUUID().toString());
        String token = generateToken(appKey, appSecret, ownerType);
        long tokenGenerateTime = System.currentTimeMillis();
        long tokenEndTime = tokenGenerateTime + TOKEN_VALIDITY * 3600 * 1000;
        accessToken.setTokenGenerateTime(tokenGenerateTime);
        accessToken.setToken(token);
        accessToken.setTokenEndTime(tokenEndTime);
        commonDao.insert(accessToken);
        return token;
    }

    /**
     * 验证appKey和appSecret是否正确
     * @param appKey
     * @param appSecret
     */
    private void validateAppKey(String appKey, String appSecret) {
        CommonDao commonDao = CommonDao.getInstance();
        String sql = "select count(1) from taian_dept_self_build_system where appKey = ? and appSecret = ?";
        int count = commonDao.queryInt(sql, appKey, appSecret);
        if (count == 0) {
            throw new RuntimeException("appKey或appSecret不正确");
        }
    }

    /**
     * 生成token,每次调用都返回不同的结果
     * @param appKey
     * @param appSecret
     * @param ownerType
     * @return
     */
    public String generateToken(String appKey, String appSecret, OwnerType ownerType) {
        String str = appKey + appSecret + ownerType + System.currentTimeMillis();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md5.digest(str.getBytes("UTF-8"));
            String token = Base64Util.encode(bytes);
            return token.replace("=", "");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public AccessToken findAccessTokenByToken(String token) {
        CommonDao commonDao = CommonDao.getInstance();
        String sql = "select * from taian_access_token where token = ?";
        return commonDao.find(sql, AccessToken.class, token);
    }

}
