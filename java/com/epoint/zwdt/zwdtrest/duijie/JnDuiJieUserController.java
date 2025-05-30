package com.epoint.zwdt.zwdtrest.duijie;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.zwdt.zwdtrest.dhuser.DhUser_HttpUtil;
import com.epoint.zwdt.zwdtrest.task.impl.JnAppRestImpl;

@RestController
@RequestMapping("/jnduijieuser")
public class JnDuiJieUserController
{

    transient static Logger log = LogUtil.getLog(DhUser_HttpUtil.class);
    
    @Autowired
    private JnAppRestImpl jnApprest;
    
    @Autowired
    private IOuServiceInternal ouService;
    
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    
    /**
     * 政务大厅注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    @Autowired
    private ICodeItemsService iCodeItemsService;


    /**
     * 通过票据获取令牌
     * 
     * @param params 接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTicketByRefreshToken", method = RequestMethod.POST)
    public String getTicketByRefreshToken(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTicketByRefreshToken接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject paramsjson = jsonObject.getJSONObject("params");
            String appmark = paramsjson.getString("appmark");
            log.info("appmark:"+appmark);
            log.info("Referer:"+request.getHeader("Referer"));
            if (ZwdtConstant.SysValidateData.equals(token)) {
                //JSONObject obj = (JSONObject) jsonObject.get("params");
                //String proxyapp = obj.getString("proxyapp");
            	String dhtoken = "";
            	// 1.4、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    log.info("dhtoken:"+auditOnlineRegister.getStr("dhtoken"));
                	dhtoken = auditOnlineRegister.getStr("dhtoken");
                }
            	JSONObject resultObject = new JSONObject();
            	String areasign = request.getServerName().replace(".sd.gov.cn", "");
                String areaurl = ConfigUtil.getConfigValue("jnlogin", areasign);
                areasign = StringUtil.isNotBlank(areaurl)?areasign+"_":"wt";
                log.info("刷新接口登录域名："+request.getServerName()+">>区域标识："+areasign);
                IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
                String AS_ISENABLE_DHNEWSSO = configservice.getFrameConfigValue("AS_ISENABLE_DHNEWSSO");
                if(StringUtil.isNotBlank(AS_ISENABLE_DHNEWSSO) && ZwfwConstant.CONSTANT_STR_ONE.equals(AS_ISENABLE_DHNEWSSO)) {
                	ZwdtUserSession zwdtusersession = ZwdtUserSession.getInstance("");
                    log.info("dhtoken:"+zwdtusersession.getDhtoken());
                	if (StringUtil.isBlank(dhtoken)) {
                		dhtoken = zwdtusersession.getDhtoken();
                	}
            		log.info("登录用户大汉token："+dhtoken);
            		log.info("登录用户大汉appmark："+appmark);
            		String jnappmark = ConfigUtil.getConfigValue("jnlogin1", areasign+"appmark");
                	String jnappword = ConfigUtil.getConfigValue("jnlogin1", areasign+"appword");
                	String decodekey = ConfigUtil.getConfigValue("jnlogin1", areasign+"appkey");
                	JSONObject ticketdata = new JSONObject();
                	ticketdata = com.epoint.common.zwdt.login.DhUser_HttpUtil.generateticket(jnappmark, decodekey, "0", dhtoken, appmark, jnappword);
                	resultObject = ticketdata;
                	log.info("=======结束调用getTicketByRefreshToken接口=======resultObject："+resultObject.toJSONString());
                }else {
                	// 1.7 大汉解密私钥
                	String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
                	String jnappmark = ConfigUtil.getConfigValue("jnlogin", areasign+"appmark");
                	String jnappword = ConfigUtil.getConfigValue("jnlogin", areasign+"appword");
                	String wangzhi = ConfigUtil.getConfigValue("jnlogin", "dhrest");
                	// 通过大汉提供的加密方式进行加密
                	String sign = MD5Util.getMD5(jnappmark + jnappword + time);
                	String urlparams = "appmark=" + jnappmark + "&time=" + time + "&sign=" + sign;
                	Object obj2 = null;
                	//验证票据
                	obj2 = DhUser_HttpUtil.getTicketByRefreshToken(wangzhi, urlparams, StringUtil.isNotBlank(appmark)?appmark:jnappmark);
                	resultObject = JSONObject.parseObject(obj2.toString());
                	log.info("=======结束调用getTicketByRefreshToken接口=======");
                }
                return JsonUtils.zwdtRestReturn("1", "ticket返回成功", resultObject.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "ticket返回失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTicketByRefreshToken接口参数：params【" + params + "】=======");
            log.info("=======getTicketByRefreshToken异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "接口获取ticket出现异常：" + e.getMessage(), "");
        }
    }


    /**
     * 获取其他辖区的跳转地址
     *
     * @param params 接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getOtherAreaUrl", method = RequestMethod.POST)
    public String getOtherAreaUrl(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getOtherAreaUrl接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject paramsjson = jsonObject.getJSONObject("params");
            String appmark = paramsjson.getString("appmark");
            log.info("appmark:"+appmark);
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject resultObject = new JSONObject();
                //省网获取各地市主页地址
                String url = HttpUtil.doGet("http://www.shandong.gov.cn/api-gateway/jpaas-juspace-web-sdywtb/front/sso/zwSiteAddTicket?appMark="+appmark);
                resultObject.put("url",url);
                CodeItems codeItems = iCodeItemsService.getCodeItemByCodeName("各地市appmark",appmark);
                String appmarkstring =  codeItems.getItemValue();
                resultObject.put("appmarkstring",appmarkstring);
                return JsonUtils.zwdtRestReturn("1", "ticket返回成功", resultObject);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "ticket返回失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getOtherAreaUrl接口参数：params【" + params + "】=======");
            log.info("=======getOtherAreaUrl异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "接口获取getOtherAreaUrl出现异常：" + e.getMessage(), "");
        }
    }
    
    
    /**
     * 通过票据获取令牌
     * 
     * @param params 接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getAreanameByAreacode", method = RequestMethod.POST)
    public String getAreanameByAreacode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAreanameByAreacode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject param = jsonObject.getJSONObject("params");
            JSONObject resultObject = new JSONObject();
            FrameOu parentou = new FrameOu();
            FrameOu parentou1 = new FrameOu();
            FrameOu parentou2 = new FrameOu();
            String ouname1 = "";
            String ouname2 = "";
            String ouname3 = "";
            String areacode = param.getString("mareacode");
            String businessguid = param.getString("businessguid");
            
            
//            AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
            
//            if (business != null) {
//            	areacode = business.getAreacode();
//            }
            
            if (StringUtil.isNotBlank(areacode)) {
                FrameOu frame = jnApprest.getAreanameByAreacode(areacode);

                if (frame != null && StringUtil.isNotBlank(frame.getOuname())) {
                    if (StringUtil.isNotBlank(frame.getParentOuguid())) {
                        parentou = ouService.getOuByOuGuid(frame.getParentOuguid());
                        ouname1 = parentou.getOuname();
                        if (StringUtil.isNotBlank(parentou.getParentOuguid())) {
                            parentou1 = ouService.getOuByOuGuid(parentou.getParentOuguid());
                            ouname2 = parentou1.getOuname();
                                if (StringUtil.isNotBlank(parentou1.getParentOuguid())) {
                                    parentou2 = ouService.getOuByOuGuid(parentou1.getParentOuguid());
                                    ouname3 = parentou2.getOuname();
                                }
                        }
                    }
                    
                    if (StringUtil.isNotBlank(ouname1) && StringUtil.isBlank(ouname2) && StringUtil.isBlank(ouname3)) {
                        resultObject.put("oucodelevel", ouname1 + "·" + frame.getOuname());
                    }else if (StringUtil.isNotBlank(ouname1) && StringUtil.isNotBlank(ouname2) && StringUtil.isBlank(ouname3)) {
                        resultObject.put("oucodelevel", ouname2 + "·" +ouname1 + "·" + frame.getOuname());
                    }else if (StringUtil.isNotBlank(ouname1) && StringUtil.isNotBlank(ouname2) && StringUtil.isNotBlank(ouname3)) {
                        resultObject.put("oucodelevel", ouname3 + "·"+ouname2 + "·" +ouname1 + "·" + frame.getOuname());
                    }else {
                        resultObject.put("oucodelevel", frame.getOuname());
                    }
                    
                    if ("太白湖新区".equals(frame.getOuname())) {
                        resultObject.put("areaname", "北湖省级旅游度假区");
                    }else {
                        resultObject.put("areaname", frame.getStr("ouname"));
                    }
                   
                }
                log.info("=======结束调用getAreanameByAreacode接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询辖区名字成功", resultObject.toString());
            }else {
                return JsonUtils.zwdtRestReturn("0", "查询辖区名字失败", resultObject.toString());
            }
           
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAreanameByAreacode接口参数：params【" + params + "】=======");
            log.info("=======getAreanameByAreacode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "接口辖区名字出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 获取用户唯一标识
     * 
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

}
