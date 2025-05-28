package com.epoint.identity.custom;

import com.epoint.basic.shiro.login.IdentityBuilderWrapper.LoginByUsernameAndPasswordBuilder;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2ClientUtil;

import java.net.URLDecoder;

/**
 * 个性化 sm2解密
 * @author fzj
 * @version [版本号, 2020年9月21日]
 */
public class CustomLoginByUsernameAndPasswordBuilder extends LoginByUsernameAndPasswordBuilder
{

    @Override
    public String decode(String str) {
        if (str.startsWith("=")) {
            // 添加对于自定义加密的支持，用于替换rsa解密性能低下问题 edit by ko 2017-5-23
            str = EncodeUtil.decodeByJs(str);
        }
        else if (str.length() > 100) {
            try {
                str = SM2ClientUtil.getInstance().decrypt(str);
                if (str.indexOf("%") != -1) {
                    str = URLDecoder.decode(str, "UTF-8");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }
}
