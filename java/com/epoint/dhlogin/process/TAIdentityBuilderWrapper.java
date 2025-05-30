package com.epoint.dhlogin.process;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Identity;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.zwdt.login.FrametIdentityBuilder;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.util.UserEncodeUtil;

/**
 * 框架不同登录方式前端信息的解析类集合，主要是解析前端数据格式，封装用户身份对象，解密用户名和密码
 * 
 * @author sjw
 * @version [版本号, 2017年1月3日]
 */
public class TAIdentityBuilderWrapper
{ // 用户名和密码---拆分成手机号码+动态密码；手机号码/身份证号码+密码

    static Logger log = Logger.getLogger(TAIdentityBuilderWrapper.class);

    public static class LoginByUsernameAndPasswordBuilder extends FrametIdentityBuilder
    {

        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            String userNameId = request.getParameter("username");
            IHandleConfig handleConfigService = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineRegister.class);
            UserEncodeUtil iEncryptAndDecrypt = ContainerFactory.getContainInfo()
                    .getComponent(UserEncodeUtil.class);
            String customPwdEncrypt = handleConfigService.getFrameConfig("sm2_EncryptPwd", "").getResult();
            // 用来存储手机号
            String username = "";
            // 安全性转码
            userNameId = EncodeUtil.decodeByJs(userNameId);
            // 如果为身份证，则统一改为大写登录
            if (userNameId.length() > 11) {
                // 用来存储身份证
                userNameId = userNameId.toUpperCase();
                AuditOnlineRegister zwdtthirdregister = iAuditOnlineRegister.getRegisterByIdorMobile(userNameId)
                        .getResult();
                if (zwdtthirdregister != null) {
                    username = zwdtthirdregister.getMobile();
                }
            }
            else {
                AuditOnlineRegister zwdtthirdregister = iAuditOnlineRegister.getRegisterByIdorMobile(userNameId)
                        .getResult();
                username = userNameId;
                if (zwdtthirdregister != null) {
                    userNameId = zwdtthirdregister.getIdnumber().toUpperCase();
                }
            }
            // 暂时先统一用手机号的方式，如果是身份证就查相关数据取出手机号
            /*if (username.length() > 12) {
                AuditOnlineRegister zwdtthirdregister = iAuditOnlineRegister.getRegisterByIdorMobile(username)
                        .getResult();
                username = zwdtthirdregister.getMobile();
            }*/
            String password = request.getParameter("code");
            String loadType = request.getParameter("logintype");
            String url = request.getParameter("url");// 登录成功需要跳转的地址            
            if ("".equals(loadType)) {
                // 默认用户名密码登录
                loadType = ZwdtConstant.LoginIdAndPassWord;
            }
            if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
                if (ZwdtConstant.LoginIdAndPassWord.equals(loadType)) {
                    password = iEncryptAndDecrypt.encryption(password, userNameId);
                }
            }
            String cmdparams = WebUtils.getCleanParam(request, "cmdParams");
            if (StringUtil.isNotBlank(cmdparams)) {
                if (cmdparams.startsWith("[")) {
                    cmdparams = cmdparams.substring(1);
                }
                if (cmdparams.endsWith("]")) {
                    cmdparams = cmdparams.substring(0, cmdparams.length() - 1);
                }
                String[] arr = cmdparams.split(",");
                username = format(arr[0]);
                password = format(arr[1]);
                if (arr.length > 2) {
                    loadType = format(arr[2]);
                }
            }
            // 封装请求参数
            Identity identity = createCommonToken(username, password, loadType, request, response);
            identity.setName(ZwdtConstant.LoginidAndPasswordlogin);
            Map<String, String> map = new HashMap<String, String>(16);
            map.put("url", url);
            identity.setExtvalue(map);
            return identity;
        }
    }

    /**
     * 
     * @Description :山东大汉单点登录
     *  
     * @author male
     * @version [版本号, 2019年3月26日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static class DHLoginBuilder extends FrametIdentityBuilder
    {

        @Override
        public Identity build(ServletRequest request, ServletResponse response) {

            Logger log = Logger.getLogger(TAIdentityBuilderWrapper.class);

            //票据
            String ticket = request.getParameter("ticket");

            String loginname = "";// 登录账号
            String name = "";// 用户名
            String mobile = "";// 手机号
            String cardid = "";// 证件号码

            String creditcode = "";// 统一社会信用代码
            String qyname = "";// 企业、机构名称

            String password = "";
            String loadType = ZwdtConstant.Dhlogin;
            String token = "";

            /**
             * uuid
             */
            String uuid = "";
            //提交参数
            JSONObject sumbit = new JSONObject();
            //返回参数
            JSONObject dataJson = new JSONObject();
            //解析参数
            JSONObject result = new JSONObject();

            //大汉秘钥
            //String privateKey = "88A504105DD9EEE936300F6802C5D3ACDBCB827B3E19C147A0EDE38A47C27CBE";
            String privateKey = ConfigUtil.getConfigValue("dhlogin", "privatekey");

            //山东大汉单点登录请求token  
            sumbit.put("ticket", ticket);
            String tokenback = DHSingleLoginRestUtil.getSingleLoginInfo(sumbit.toJSONString(), "ticketValidate");
            //System.out.println(sumbit + " 获取token" + tokenback);
            try {
                if (StringUtil.isNotBlank(tokenback)) {
                    //解析获取token
                    dataJson = DHSingleLoginRestUtil.analysisReturn(tokenback, false);
                    if (dataJson.containsKey("token")) {
                        token = dataJson.getString("token");

                        token = URLEncoder.encode(token, "UTF-8");

                        //山东大汉根据token获取用户uuid
                        if (StringUtil.isNotBlank(token)) {
                            sumbit = new JSONObject();
                            sumbit.put("token", token);

                            String uuidBack = DHSingleLoginRestUtil.getSingleLoginInfo(sumbit.toJSONString(),
                                    "findUserByToken");
                            //解析获取uuid
                            dataJson = new JSONObject();
                            dataJson = DHSingleLoginRestUtil.analysisReturn(uuidBack, true);
                            //解析SM2获取uuid 
                            if (dataJson.containsKey("sm2")) {
                                result = DHSingleLoginRestUtil.dhSM2Decrypt(privateKey, dataJson.getString("sm2"));
                                uuid = result.getString("uuid");
                            }

                            if (StringUtil.isNotBlank(uuid)) {
                                //根据uuid和token获取用户信息
                                sumbit = new JSONObject();
                                sumbit.put("token", token);
                                sumbit.put("uuid", uuid);
                                String userBack = DHSingleLoginRestUtil.getSingleLoginInfo(sumbit.toJSONString(),
                                        "findUserByTokenAndUuid");
                                dataJson = new JSONObject();
                                dataJson = DHSingleLoginRestUtil.analysisReturn(userBack, true);
                                //解密后转化成json  SM2解密  
                                JSONObject userobj = DHSingleLoginRestUtil.dhSM2Decrypt(privateKey,
                                        dataJson.getString("sm2"));

                                //设置用户信息
                                if (userobj.containsKey("loginname")) {// 登录名
                                    //System.out.println(userobj);
                                    loginname = userobj.getString("loginname");
                                    mobile = userobj.getString("mobile");
                                    cardid = userobj.getString("cardid");
                                    name = userobj.getString("name");
                                }
                                else if (userobj.containsKey("errormsg")) {
                                    log.info(userobj.getString("errormsg"));
                                }

                                // 根据令牌，uuid获取企业信息
                                String companyBack = DHSingleLoginRestUtil.getSingleLoginInfo(sumbit.toJSONString(),
                                        "findCorporationByTokenAndUuid");
                                dataJson = new JSONObject();
                                dataJson = DHSingleLoginRestUtil.analysisReturn(companyBack, true);

                                //私钥解密
                                JSONObject corporationobj = DHSingleLoginRestUtil.dhSM2Decrypt(privateKey,
                                        dataJson.getString("sm2"));
                                if (corporationobj.containsKey("creditcode")) {// 统一社会信用代码
                                    creditcode = corporationobj.getString("creditcode");
                                    qyname = corporationobj.getString("corname");
                                }
                                else if (corporationobj.containsKey("errormsg")) {
                                    log.info(corporationobj.getString("errormsg"));
                                }
                            }

                        }
                    }

                }

            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String cmdparams = WebUtils.getCleanParam(request, "cmdParams");
            if (StringUtil.isNotBlank(cmdparams)) {
                if (cmdparams.startsWith("[")) {
                    cmdparams = cmdparams.substring(1);
                }
                if (cmdparams.endsWith("]")) {
                    cmdparams = cmdparams.substring(0, cmdparams.length() - 1);
                }
                String[] arr = cmdparams.split(",");
                loginname = format(arr[0]);
                password = format(arr[1]);
                if (arr.length > 2) {
                    loadType = format(arr[2]);
                }
            }

            // 封装请求参数
            Identity identity = createCommonToken(loginname, password, loadType, request, response);
            identity.setName(ZwdtConstant.Dhlogin);
            identity.put("name", name);
            identity.put("loginname", loginname);
            identity.put("mobile", mobile);
            identity.put("cardid", cardid);
            identity.put("token", token);
            identity.put("creditcode", creditcode);
            identity.put("qyname", qyname);

            //System.out.println("identity: " + identity);
            //测试
            /*Identity identity2 = createCommonToken("18051830527", password, loadType, request, response);
            identity2.setName(ZwdtConstant.Dhlogin);
            identity2.put("name", "测试");
            identity2.put("loginname", "18051830528");
            identity2.put("mobile", "18051830528");
            identity2.put("cardid", "320882199705272213");
            identity2.put("token", "086c5a3325fa453af9c022a4d4c4eb37");
            identity2.put("creditcode", creditcode);
            identity2.put("qyname", qyname);*/

            return identity;
        }

    }

}
