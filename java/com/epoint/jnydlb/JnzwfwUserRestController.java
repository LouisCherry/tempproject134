package com.epoint.jnydlb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineverycode.domain.AuditOnlineVerycode;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbuserinfoextend.domain.AuditZnsbUserinfoextend;
import com.epoint.basic.auditqueue.auditznsbuserinfoextend.inter.IAuditZnsbUserinfoextend;
import com.epoint.basic.auditqueue.selflogin.domain.AuditZnsbSelflogin;
import com.epoint.basic.auditqueue.selflogin.inter.IAuditZnsbSelfloginService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.entity.CodeMain;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;


@RestController
@RequestMapping("/jnzwfwselfserviceuser")
public class JnzwfwUserRestController {

    @Autowired
    private IAuditOnlineRegister registerservice;

    @Autowired
    private IAuditOnlineIndividual individualservice;

    @Autowired
    private IAuditZnsbSelfloginService auditZnsbSelfloginService;
    
    @Autowired
    private IAuditQueueUserinfo userinfoservice;
    
    @Autowired
    private IAuditZnsbUserinfoextend userinfoextendservice;
    
    @Autowired
    private ICodeItemsService codeItemsService;
    
    @Autowired
    private ICodeMainService codeMainService;

    /**
     * 一体机登录记录实体对象
     */
    private AuditZnsbSelflogin dataBean;
    
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    
    
    /**
     * 判断url里的身份证是否已存在,存在则自动登录
     * 不存在则返回对应辖区的地址,获取信息后自动注册
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/sharelogininfo", method = RequestMethod.POST)
    public String shareLoginInfo(@RequestBody String params) {
        try {   
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            
            String sfz = obj.getString("sfz");
            String beforeareacode = obj.getString("beforeareacode");
            String logintype = obj.getString("logintype");// 1代表刷卡登录，无需输入密码；2代表普通登录；3APP扫码登录
            String macaddress = obj.getString("macaddress");
            String centerguid= obj.getString("centerguid");
            
            String accountguid = "";
            String usertype = "";
            String mobile = "";
            String loginId = "";
            String idnumber = "";
            String clientname = "";
            String applyerguid = "";
            
            AuditOnlineRegister register = registerservice.getRegisterByIdNumber(sfz).getResult();
            if (register != null) {
                accountguid = register.getAccountguid();
                usertype = register.getUsertype();
                mobile = register.getMobile();
                loginId = register.getLoginid();
                
                AuditOnlineIndividual individual = individualservice.getIndividualByAccountGuid(accountguid)
                        .getResult();
                if (individual != null) {
                    idnumber = individual.getIdnumber();
                    clientname = individual.getClientname();
                    applyerguid = individual.getApplyerguid();
                    //登录记录保存
                    dataBean = new AuditZnsbSelflogin();
                    dataBean.setRowguid(UUID.randomUUID().toString());
                    dataBean.setLoginname(clientname);
                    dataBean.setIdcard(idnumber);
                    dataBean.setLogintime(new Date());
                    dataBean.setLogintype(logintype);// 1代表刷卡登录，无需输入密码；2代表普通登录；3APP扫码登录
                    dataBean.setMacaddress(macaddress);
                    dataBean.setCenterguid(centerguid);
                    auditZnsbSelfloginService.insert(dataBean);
                }
            }else{
                //本地数据库无该用户信息,返回对应辖区的地址,获取信息后自动注册
                CodeMain codeMain =codeMainService. getCodeMainByName("长三角账户人员信息接口");
                String beforeareacodeurl=codeItemsService.getItemValueByCodeID(codeMain.getCodeID().toString(), beforeareacode);
                if (StringUtil.isNotBlank(beforeareacodeurl)) {
                    dataJson.put("beforeareacodeurl", beforeareacodeurl.subSequence(0, beforeareacodeurl.length()-7));
                    return JsonUtils.zwdtRestReturn("1", "", dataJson);
                }else{
                    return JsonUtils.zwdtRestReturn("0", "该地区编码未配置访问路径", "");
                }              
            }         
            
            //获取用户身份证照片,住址
            AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(idnumber).getResult();
            if (StringUtil.isNotBlank(auditQueueUserinfo)&&StringUtil.isNotBlank(auditQueueUserinfo.getPhoto())) {
                dataJson.put("photobase64", Base64Util.encode(auditQueueUserinfo.getPhoto()) );            
                dataJson.put("address",auditQueueUserinfo.getAddress());
            }else{
                dataJson.put("photobase64","" );
            }
            
            dataJson.put("accountguid", accountguid);
            dataJson.put("usertype", usertype);
            dataJson.put("mobile", mobile);
            dataJson.put("loginid", loginId);
            dataJson.put("idnumber", idnumber);
            dataJson.put("clientname", clientname);
            dataJson.put("applyerguid", applyerguid);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
            
            
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /**
     * 通过accountguid 用户guid 获取用户人员信息
     * 提供给其他长三角地区的接口
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getAccountUserinfo", method = RequestMethod.POST)
    public String getAccountUserinfo(@RequestBody String params) {
        try {   
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            
            String accountguid = obj.getString("accountguid");
            
            AuditOnlineIndividual individual = individualservice.getIndividualByAccountGuid(accountguid)
                    .getResult();
            if (StringUtil.isNotBlank(individual)) {
                dataJson.put("username", individual.getClientname());//用户名               
                dataJson.put("idnum", individual.getIdnumber());//身份证
                
                AuditOnlineRegister register = registerservice.getRegisterByIdorMobile(individual.getIdnumber()).getResult();
                if (StringUtil.isNotBlank(register)) {
                    dataJson.put("password", register.getPassword());//
                    dataJson.put("mobile", register.getMobile());//手机号码
                }
                
                
                //获取用户身份证照片等信息
                AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(individual.getIdnumber()).getResult();                
                if (StringUtil.isNotBlank(auditQueueUserinfo)) {
                    dataJson.put("sex",auditQueueUserinfo.getSex());//性别
                    dataJson.put("nation", auditQueueUserinfo.getMingzu());//民族
                    dataJson.put("born", auditQueueUserinfo.getBirthday());//出生日期
                    dataJson.put("address", auditQueueUserinfo.getAddress());//家庭住址
                    dataJson.put("picture", StringUtil.isNotBlank(auditQueueUserinfo.getPhoto())?auditQueueUserinfo.getPhoto():"");//人员头像 base64
                }
            }else{
                return JsonUtils.zwdtRestReturn("0", "查询不到该账户,无法完成自动登录", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
            
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /**
     * 根据长三角接口返回的数据,自动注册用户信息
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/registerfromcsz", method = RequestMethod.POST)
    public String registerFromCsz(@RequestBody String params) {
        try {   
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            
            String userName = obj.getString("username");
            String passWord = obj.getString("password");
            String mobile = obj.getString("mobile");
            String idnum = obj.getString("idnum");
            
            String sex= obj.getString("sex");
            String nation= obj.getString("nation");
            String born= obj.getString("born");
            String address= obj.getString("address");
            String picture= obj.getString("picture");
            String accountguid = UUID.randomUUID().toString();            
            
            // 验证手机
            AuditCommonResult<AuditOnlineRegister> resultMobile = registerservice.getRegisterByMobile(mobile);
            if (resultMobile.getResult() != null) {
                return JsonUtils.zwdtRestReturn("0", "该手机号已经被注册！无法完成自动登录", "");
            }
            String applyerguid=UUID.randomUUID().toString();
            AuditOnlineIndividual individual = new AuditOnlineIndividual();
            individual.setRowguid(UUID.randomUUID().toString());
            individual.setAccountguid(accountguid);
            individual.setClientname(userName);
            individual.setIdnumber(idnum);
            individual.setContactmobile(mobile);
            individual.setApplyerguid(applyerguid);
            // 真实姓名，身份证号，accountGuid，手机号
            individualservice.addIndividual(individual);

            AuditOnlineRegister register = new AuditOnlineRegister();
            register.setRowguid(UUID.randomUUID().toString());
            register.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);// 默认自然人
            register.setAccountguid(accountguid);
            register.setPassword(passWord);
            register.setMobile(mobile);
            register.setUsername(userName);
            register.setIdnumber(idnum);
            registerservice.addRegister(register);
            
            if (StringUtil.isNotBlank(picture)) {
                //刷身份证登陆 更新身份证信息
                  byte[] pic = Base64Util.decodeBuffer(picture);
                  //更新人员信息表的数据
                  userinfoservice.insertuserinfo(register.getIdnumber(), register.getUsername(), sex, nation, born, address, pic);
              }
              
            dataJson.put("accountguid", accountguid);
            dataJson.put("usertype", ZwdtConstant.ONLINEUSERTYPE_PERSON);
            dataJson.put("mobile", mobile);     
            dataJson.put("idnumber", idnum);
            dataJson.put("clientname", userName);
            dataJson.put("applyerguid", applyerguid);
            dataJson.put("address", address);
            dataJson.put("photobase64", picture );    
            
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
            
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    
    
}

