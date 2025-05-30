package com.epoint.common.zwdt.login;

import java.util.Date;

import com.epoint.authenticator.asutils.SSOConfigEnvironment;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.zwdt.login.dhrsautil.AESUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;

import com.epoint.authenticator.identity.AuthenticationMapInfo;
import com.epoint.authenticator.identity.Identity;
import com.epoint.authenticator.loginflow.authenticationlistener.NamedAuthenticationListener;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.shiro.login.FrameCredentialsMatcher;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.memory.MemoryUtil;
import com.epoint.core.utils.string.StringUtil;

/**
 * 框架登录后的业务处理
 * 
 * @作者 sjw
 * @version [版本号, 2017年1月3日]
 */
public class JnZwdtAuthenticationListener extends NamedAuthenticationListener
{

    @SuppressWarnings("unused")
    @Override
    public void onSuccessLogin(AuthenticationToken token, AuthenticationInfo info) {
        Identity identity = (Identity) token;
        AuthenticationMapInfo mapInfo = (AuthenticationMapInfo) info;
        AuditOnlineRegister auditOnlineRegister = null;
        // 添加zwdt业务信息
        // 1 把用户信息存放到redis中
        //        RedisCacheUtil redisUtil = new RedisCacheUtil(false);
        auditOnlineRegister = (AuditOnlineRegister) mapInfo.get("zwdtthirdregister");
        //        redisUtil.putByHash(auditOnlineRegister);
        //        redisUtil.commitPipe();
        //        redisUtil.close();  
        IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineRegister.class);
        auditOnlineRegister.setLastlogindate(new Date());
        auditOnlineRegister.set("dhtoken", identity.get("dhtoken"));
        iAuditOnlineRegister.updateRegister(auditOnlineRegister);
        //2 初始化session
        ZwdtUserSession zwdtusersession = ZwdtUserSession.getInstance(auditOnlineRegister.getAccountguid());
        Object dhtoken = identity.get("dhtoken");
        if(dhtoken!=null) {
        	zwdtusersession.setDhtoken(dhtoken.toString());
        }
    }

    @Override
    public void onFailureLogin(AuthenticationToken token, AuthenticationException ae) {
        // 添加登录失败锁定
        addLoginFailedLock(token.getPrincipal().toString());
    }

    @Override
    public void logout(PrincipalCollection principals) {
        //        IUserServiceInternal frameUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        //        FrameUser user = frameUserService.getUserByUserField("loginId", principals.getPrimaryPrincipal().toString());
        //        frameUserService.deleteFrameOnlineUser(user.getUserGuid());
        //        IConfigService frameConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        //
        //        String componentName = frameConfigService.getFrameConfigValue("userCustomSession");
        //        if (StringUtil.isNotBlank(componentName)) {
        //            String[] components = componentName.split(";");
        //            for (String component : components) {
        //                UserSessionInterface9 diyUserSession = ContainerFactory.getContainInfo().getComponent(component);
        //                if (diyUserSession != null) {
        //                    diyUserSession.clearSession();
        //                }
        //            }
        //        }

        //获取大厅session
        ZwdtUserSession zwdtusersession = ZwdtUserSession.getInstance("");
        //        UserSessionInterface9 diyUserSession = ContainerFactory.getContainInfo().getComponent("zwdtusersession");
        //        diyUserSession.clearSession();
        //清除redis缓存
        if (StringUtil.isNotBlank(zwdtusersession.getAccountGuid())) {
            IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineRegister.class);

            //RedisCacheUtil redisUtil = new RedisCacheUtil(false);
            AuditOnlineRegister auditOnlineRegister = null;
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(zwdtusersession.getAccountGuid())
                    .getResult();

            ZwfwRedisCacheUtil redisUtil = null;

            try {
                redisUtil = new ZwfwRedisCacheUtil(false);
                redisUtil.delByHash(AuditOnlineRegister.class, auditOnlineRegister.getRowguid());
            } catch (Exception var10) {
                var10.printStackTrace();
            } finally {
                if (redisUtil != null) {
                    redisUtil.close();
                }

            }
        }
    }

    /**
     * 登录失败后往eh中加入登录失败的时间
     */
    private void addLoginFailedLock(String loginId) {
        if (FrameCredentialsMatcher.failedLockMill > 0 && FrameCredentialsMatcher.failedLockTimes > 0) {
            // 当前时间
            long current = System.currentTimeMillis();
            // 获取登录记录
            Object cacheFailedLock = MemoryUtil.getFromMemory("failedLock" + loginId);
            if (cacheFailedLock != null) {
                MemoryUtil.insertToMemory("failedLock" + loginId,
                        cacheFailedLock.toString() + EpointKeyNames9.SPECIAL_SPLITCHAR_STAR + current);
            }
            else {
                MemoryUtil.insertToMemory("failedLock" + loginId, current);
            }
        }
    }

}
