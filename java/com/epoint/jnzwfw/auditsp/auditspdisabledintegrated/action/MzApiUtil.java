package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.IAuditYjsCjrService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjr;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjrInfo;

/**
 * 残联接口调用工具
 * @author ：lionzz
 * @date ：Created in 2021/4/12 15:56
 * @description：
 * @modified By：
 * @version:
 */
public class MzApiUtil {

    private static final transient Logger log = Logger.getLogger(BaseController.class);

    /**
     * 缓存，登录成功时存入
     */
    private static final HashMap<String, MzApiUtil> cache = new HashMap<>();


    /**
     * 用户名
     */
    final String username;
    /**
     * 密码
     */
    final String password;
    /**
     * 验证码
     */
    final String verycode;
    /**
     * 短信验证码
     */
    final String dx_verifcode;

    /**
     * session - UserSession.getInstance().getUserguid()
     */
    final String apiSession;

    /**
     * 登录成功后的userId - 方便后续接口使用
     */
    String userId;

    UserSession userSession = UserSession.getInstance();

    /**
     * 构造方法
     * @param username
     * @param password
     * @param verycode
     * @param dx_verifcode
     */
    public MzApiUtil(String username, String password, String verycode, String dx_verifcode) {
        this.username = username;
        this.password = password;
        this.verycode = verycode;
        this.dx_verifcode = dx_verifcode;
        this.apiSession = userSession.getUserGuid();
    }

    /**
     * 获取缓存
     * @param userGuid
     * @return
     */
    public static MzApiUtil getInstance(String userGuid) {
        return cache.get(userGuid);
    }


    /**
     * 获取验证码
     *
     * @return
     */
    public static String getVeryCode() {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl+"/sdshuijiuzhushuzipingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=verifycode&APISession=" + UserSession.getInstance().getUserGuid();
        log.info("调用民政验证码接口:" + url);
        try (InputStream in = HttpUtil.doHttp(url, null, "get", HttpUtil.RTN_TYPE_INPUTSTREAM)
             ; ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            return Base64Util.encode(out.toByteArray());
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 登录
     *
     * @return
     */
    public JSONObject login() {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl+"/sdshuijiuzhushuzipingtai/login";
        JSONObject submitString = new JSONObject();
        submitString.put("username", username);
        submitString.put("password", password);
        submitString.put("verifycode", verycode);
        submitString.put("APISession", apiSession);
        submitString.put("iw-apikey", "dfb378cba6794bf2ba834e86d8d9f257");
        String result = HttpUtil.doPostJson(url, submitString.toString());
        log.info("民政登录接口入参:" + submitString.toString());
        JSONObject resultObj = JSON.parseObject(result);
        log.info("民政登录返回结果："+resultObj.toString());
        String rtnCode = resultObj.getString("rtnCode");
        // 1.2、如果登录成功
        if ("000000".equals(rtnCode)) {
            String userId = resultObj.getJSONObject("data").getString("userId");
            this.setUserId(userId);
            cache.put(apiSession, this);
            resultObj.put("issuccess", true);
        }
        return resultObj;
    }

    /**
     * 发送短信
     *
     * @return
     */
    public static JSONObject sendSms() {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl+"/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=dx_verifycode&APISession=" + UserSession.getInstance().getUserGuid();
        log.info("发送短信接口:" + url);
        String result = HttpUtil.doGet(url);
        return JSON.parseObject(result);
    }
    
    public boolean upload(String url,String applyId,String filename,String activityId,String remarks,String APISession,String imgBase64) {
    	 IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
    	 String clUrl = configService.getFrameConfigValue("残联地址");
         String attachurl = clUrl + "/sdshuijiuzhushuzipingtai/"+ url;
    	 JSONObject submitString = new JSONObject();
         submitString.put("applyId", applyId);
         submitString.put("fileName", filename);
         submitString.put("activityId", activityId);
         submitString.put("remarks", remarks);
         submitString.put("APISession", APISession);
         submitString.put("imgBase64", imgBase64);
         submitString.put("iw-apikey", "dfb378cba6794bf2ba834e86d8d9f257");
         log.info("文件上传入参："+submitString.toString());
         String cjrhlresult = HttpUtil.doPostJson(attachurl, submitString.toString());
         log.info("文件上传成功结果："+cjrhlresult);
         JSONObject checkResult = JSON.parseObject(cjrhlresult);
         if ("000000".equals(checkResult.getString("rtnCode"))) {
        	 log.info("文件上传成功"+filename);
        	 return true;
         }else {
        	 log.info("文件上传失败:"+filename);
        	 return false;
         }
         
    }


    /**
     * 受理提交
     *
     * @param dataBean
     * @return
     */
    public boolean acceptSubmit(AuditYjsCjr dataBean) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        IAuditYjsCjrService iAuditYjsCjrService = ContainerFactory.getContainInfo().getComponent(IAuditYjsCjrService.class);
        IAuditSpIMaterial iAuditSpIMaterial = ContainerFactory.getContainInfo().getComponent(IAuditSpIMaterial.class);
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        AuditYjsCjrInfo cjrinfo = iAuditYjsCjrService.getCjrInfoDetailByRowguid(dataBean.getRowguid());
       
        if(cjrinfo == null) {
        	return false;
        }
        log.info("展示对应的一件事详情："+cjrinfo);
        
        // 1、获取受理首页信息
        String url = clUrl+"/sdshuijiuzhushuzipingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=getApplyId&APISession=" + apiSession;;
        
        log.info("获取受理首页信息：" + url);
        String result = HttpUtil.doGet(url);
        log.info("获取受理首页信息结果：" + result);
        //姓名
        String name = dataBean.getName();
        //性别
        String sex = dataBean.getSex();
        //身份证号码
        String idCard = dataBean.getIdcard();
        //民族id
        String nationId = "";
        //推送民政需要前面补0,
        if(StringUtil.isNotBlank(dataBean.getNation())&& dataBean.getNation().toString().length()<2){
        	nationId = "0"+dataBean.getNation();
        }else{
        	if("57".equals(dataBean.getNation())||"58".equals(dataBean.getNation())){
        		dataBean.setNation(dataBean.getNation().replace("5", "9"));
        	}
        	nationId = dataBean.getNation();
        };
        		
        		
        //户籍类型
        String domicileType = dataBean.getHjxz();
        //手机号码
        String phone = dataBean.getCon_phone();
        //申请日期
        String applyDate = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
        
        //起领年月
        String applyDate1 = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM");
        //户籍区域
        String manageCounty = dataBean.getBelong_district();
        if(manageCounty.length() == 6 ) {
        	manageCounty = manageCounty + "000000";
        }else if (manageCounty.length() == 9) {
        	manageCounty = manageCounty + "000";
        }
       
        
        Record record = iAuditYjsCjrService.getMzAreacodeByCjrAreacode(manageCounty);
        if (record != null) {
        	manageCounty = record.getStr("mzareacode");
        }
        log.info("民政推送的户籍区域为："+manageCounty);
        //户籍街道
        String manageTown = dataBean.getBelong_town();
        if(manageTown.length() == 6 ) {
        	manageTown = manageTown + "000000";
        }else if (manageTown.length() == 9) {
        	manageTown = manageTown + "000";
        }
        
        Record record1 = iAuditYjsCjrService.getMzAreacodeByCjrAreacode(manageTown);
        if (record1 != null) {
        	manageTown = record1.getStr("mzareacode");
        }
        log.info("民政推送的户籍街道为："+manageTown);
        //户籍社区
        String manageVillage = dataBean.getBelong_village();
        if(manageVillage.length() == 6 ) {
        	manageVillage = manageVillage + "000000";
        }else if (manageVillage.length() == 9) {
        	manageVillage = manageVillage + "000";
        }
        
        Record record2 = iAuditYjsCjrService.getMzAreacodeByCjrAreacode(manageVillage);
        if (record2 != null) {
        	manageVillage = record2.getStr("mzareacode");
        }
        log.info("民政推送的户籍社区为："+manageVillage);
        //现居地址
        String applicantAddress = dataBean.getResidence_area();
        //是否有监护人
        String isAssistHanding = dataBean.getHasGuard();
        //监护人身份证
        //String assistHandingIdCard = dataBean.getResidence_area();
        //与监护人关系
        String assistHandingRelation = dataBean.getRelation();
        
        if("5".equals(assistHandingRelation) || "9".equals(assistHandingRelation)) {
        	assistHandingRelation = "50";
        }
        else if ("2".equals(assistHandingRelation)) {
        	assistHandingRelation = "20";
        }
        else if ("3".equals(assistHandingRelation)) {
        	assistHandingRelation = "30";
        }
        else if ("4".equals(assistHandingRelation)) {
        	assistHandingRelation = "40";
        }
        else if ("6".equals(assistHandingRelation)) {
        	assistHandingRelation = "60";
        }
        else if ("7".equals(assistHandingRelation)) {
        	assistHandingRelation = "70";
        }
        else if ("8".equals(assistHandingRelation)) {
        	assistHandingRelation = "80";
        }
        
        
        //监护人姓名
        String assistHandingName = dataBean.getGuard_name();
        //监护人联系方式
        String assistHandingPhone = dataBean.getGuard_phone();
        //申请人头像地址
        String applicantPhotoPath = "无";
        //残疾人证号
        String disabilityAccount = cjrinfo.getCjrcard();
        //残疾类别 -- 需要做代码转换
        String disabilityType ="";
        if(StringUtil.isNotBlank(cjrinfo.getCjlb())){
        	switch (cjrinfo.getCjlb()+"") {
			case "视力":
				disabilityType="61";
				break;
			case "听力":
				disabilityType="62";
				break;
			case "言语":
				disabilityType="63";
				break;
			case "肢体":
				disabilityType="64";
				break; 
			case "智力":
				disabilityType="65";
				break; 
			case "精神":
				disabilityType="66";
				break; 
			default:
				break;
			}
        }
        		
        //残疾等级 -- 需要做代码转换
        String disabilityLevel = "";
        if(StringUtil.isNotBlank(cjrinfo.getCjdj())){
        	switch (cjrinfo.getCjdj()+"") {
			case "一级":
				disabilityLevel="1";
				break;
			case "二级":
				disabilityLevel="2";
				break;
			case "三级":
				disabilityLevel="3";
				break;
			case "四级":
				disabilityLevel="4";
				break; 
			default:
				break;
			}
        }
        
        //起领年月
        String beginMonth = cjrinfo.getBelongxiaqucode();
        //银行卡号
        String accountNumber = dataBean.getBankaccount();
        //开户行id
        String accountBank = dataBean.getAccountbank();
        //开户人姓名
        String accountName = dataBean.getBankaccountholder();
        //受理时间
        String assistanceTime = applyDate;
        //常用理由
        String applyReasonSelC = "4";
        //申请理由 暂时固定
        String applyReason = dataBean.getapplyReason();
        //String applyReason = "因身体原因，特申请民政相关补贴";
       
        if(StringUtil.isBlank(accountNumber) || StringUtil.isBlank(accountBank) || StringUtil.isBlank(accountName) ) {
        	return false;
        }
        
        String subappGuid = dataBean.getStr("subappguid");
        List<AuditSpIMaterial> listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappGuid).getResult();
        
        JSONObject resultObj = JSON.parseObject(result);
        if ("000000".equals(resultObj.getString("rtnCode"))) {
            JSONObject data = resultObj.getJSONObject("data");
            String cjrapplyId = data.getString("applyId");
            
            //5.重度残疾人护理补贴
            String cjrhlurl = clUrl + "/sdshuijiuzhushuzipingtai/getZdcjr";
            
            JSONObject submitString = new JSONObject();
            submitString.put("applyId", cjrapplyId);
            submitString.put("idCard", idCard);
            submitString.put("name", name);
            submitString.put("nationId", nationId);
            submitString.put("domicileType", domicileType);
            submitString.put("phone", phone);
            submitString.put("applyDate", applyDate);
            submitString.put("manageCounty", manageCounty);
            submitString.put("manageTown", manageTown);
            submitString.put("manageVillage", manageVillage);
            submitString.put("applicantAddress", applicantAddress);
            submitString.put("APISession", apiSession);
            submitString.put("iw-apikey", "dfb378cba6794bf2ba834e86d8d9f257");
            log.info("重度残疾人护理补贴查询参数："+submitString.toString());
            String cjrhlresult = HttpUtil.doPostJson(cjrhlurl, submitString.toString());
            log.info("重度残疾人护理补贴查询结果："+cjrhlresult);
            JSONObject checkResult = JSON.parseObject(cjrhlresult);
            if ("000000".equals(checkResult.getString("rtnCode"))) {
            	JSONObject datas = checkResult.getJSONObject("data");
            	 String zdcjapplyId = datas.getString("applyId");
            	 String zdcjprocessInstanceId = datas.getString("processInstanceId");
            	 String zdcjactivityId = datas.getString("activityId");
            	 String zdcjapplyCode = datas.getString("applyCode");
            	 String zdcjapplyDate = datas.getString("applyDate");
            	 String zdcjapplicantName = datas.getString("applicantName");
            	 String zdcjapplicantIdCard = datas.getString("applicantIdCard");
            	 String zdcjapplyType = datas.getString("applyType");
            	 String zdcjitemId = datas.getString("itemId");
            	 String zdcjitemName = datas.getString("itemName");
            	 String zdcjcurActivityId = datas.getString("curActivityId");
            	 
            	 boolean filestatus = true;
                 if (listMaterial != null && !listMaterial.isEmpty()) {
                 	for (AuditSpIMaterial material : listMaterial) {
                 		List<FrameAttachInfo> attachinfos =  iAttachService.getAttachInfoListByGuid(material.getCliengguid());
                 		if (attachinfos != null && !attachinfos.isEmpty()) {
                 			FrameAttachInfo attachinfo = attachinfos.get(0);
                 			 FrameAttachStorage attachStorage = iAttachService.getAttach(attachinfo.getAttachGuid());
                 			if("户口簿索引页".equals(material.getMaterialname())) {
                 				filestatus = upload("uploadBooklet",zdcjapplyId,material.getMaterialname()+attachinfo.getContentType(),zdcjactivityId,"1",apiSession,getBase64FromInputStream(attachStorage.getContent()));
                 			}else if ("户口簿户主页".equals(material.getMaterialname())) {
                 				filestatus = upload("uploadBooklet",zdcjapplyId,material.getMaterialname()+attachinfo.getContentType(),zdcjactivityId,"0",apiSession,getBase64FromInputStream(attachStorage.getContent()));
                 			}
                 			else if ("户口簿本人".equals(material.getMaterialname())) {
                 				filestatus = upload("uploadBooklet",zdcjapplyId,material.getMaterialname()+attachinfo.getContentType(),zdcjactivityId,"2",apiSession,getBase64FromInputStream(attachStorage.getContent()));
                 			}
         					else if ("身份证正面".equals(material.getMaterialname())) {
         						filestatus = upload("uploadIdCard",zdcjapplyId,material.getMaterialname()+attachinfo.getContentType(),zdcjactivityId,"0",apiSession,getBase64FromInputStream(attachStorage.getContent()));
         					}
         					else if ("身份证反面".equals(material.getMaterialname())) {
         						filestatus = upload("uploadIdCard",zdcjapplyId,material.getMaterialname()+attachinfo.getContentType(),zdcjactivityId,"1",apiSession,getBase64FromInputStream(attachStorage.getContent()));
         					}
                 			
                 		}
                 		/*else {
                 			filestatus = false;
                 		}*/
                 	}
                 }
                 if(!filestatus){
  					return false;
  				}
            	 
            	 //10.重度残疾人护理提交
            	String Zdcjrurl  = clUrl + "/sdshuijiuzhushuzipingtai/zdcjrSaveInfo";
            	 
            	 
            	 JSONObject submit = new JSONObject();
            	 submit.put("itemId", "35ac8887287c4cbdbc5411d83a0a7973");
            	 submit.put("activityId", zdcjactivityId);
            	 submit.put("applyId", zdcjapplyId);
            	 submit.put("manageCounty", manageCounty);
            	 submit.put("manageTown", manageTown);
            	 submit.put("manageVillage", manageVillage);
            	 submit.put("idCard", idCard);
            	 submit.put("name", name);
            	 submit.put("sex", sex);
            	 submit.put("nationId", nationId);
            	 submit.put("domicileAddress", applicantAddress);
            	 submit.put("domicileType", domicileType);
            	 submit.put("applicantAddress", applicantAddress);
            	 submit.put("phone", phone);
            	 submit.put("applyReasonSelC", "4");
            	 submit.put("applyReason", applyReason);
            	 submit.put("isAssistHanding", isAssistHanding);
            	 submit.put("applyDate", zdcjapplyDate);
            	 //submit.put("assistHandingIdCard", password);
            	 submit.put("assistHandingRelation", assistHandingRelation);
            	 submit.put("assistHandingName", assistHandingName);
            	 submit.put("assistHandingPhone", assistHandingPhone);
            	 submit.put("applicantPhotoPath", applicantPhotoPath);
            	 submit.put("disabilityAccount", disabilityAccount);
            	 submit.put("disabilityType", disabilityType);
            	 submit.put("disabilityLevel", disabilityLevel);
            	 submit.put("beginMonth", applyDate1);
            	 submit.put("accountNumber", accountNumber);
            	 submit.put("accountBank", accountBank);
            	 submit.put("accountName", accountName);
            	 submit.put("assistanceTime", assistanceTime);
            	 submit.put("curActivityId", zdcjcurActivityId);
            	 submit.put("processInstanceId", zdcjprocessInstanceId);
            	 submit.put("APISession", apiSession);
            	 submit.put("iw-apikey", "dfb378cba6794bf2ba834e86d8d9f257");
            	 submit.put("assistHandingIdCard", dataBean.getGuard_idcard() );
            	 log.info("重度残疾人护理提交参数："+submit.toString());
            	 String result2 = HttpUtil.doPostJson(Zdcjrurl, submit.toString());
            	 log.info("重度残疾人护理提交结果："+result2);
                 JSONObject checkResult2 = JSON.parseObject(result2);
                 if ("000000".equals(checkResult2.getString("rtnCode"))) {
                	 log.info("重度残疾人护理提交成功");
                	 return true;
            	 }else {
            		 log.info("重度残疾人护理提交失败");
            		 return false;
            	 } 
            }else {
       		 return false;
       	 }
        }else {
   		 return false;
   	  }
		
        
        
       /* // 1、获取受理首页信息
        String url2 = clUrl+"/sdshuijiuzhushuzipingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=getApplyId&APISession=" + apiSession;;
        
        log.info("获取受理首页信息：" + url);
        String result2 = HttpUtil.doGet(url);
        log.info("获取受理首页信息结果：" + result2);
        
        
        
        JSONObject resultObj2 = JSON.parseObject(result2);
        if ("000000".equals(resultObj2.getString("rtnCode"))) {
	     JSONObject data = resultObj.getJSONObject("data");
         String yzcjrapplyId = data.getString("applyId");
               
        	  
         String kncjrurl = clUrl + "/sdshuijiuzhushuzipingtai/getkncjr";
       	 JSONObject submit2 = new JSONObject();
       	 submit2.put("applyId", yzcjrapplyId);
       	 submit2.put("idCard", idCard);
       	 submit2.put("name", name);
       	 submit2.put("nationId", nationId);
       	 submit2.put("domicileType", domicileType);
       	 submit2.put("phone", phone);
       	 submit2.put("applyDate", applyDate);
       	 submit2.put("manageCounty", manageCounty);
       	 submit2.put("manageTown", manageTown);
       	 submit2.put("manageVillage", manageVillage);
       	 submit2.put("applicantAddress", applicantAddress);
       	 submit2.put("APISession", apiSession);
       	 submit2.put("iw-apikey", "dfb378cba6794bf2ba834e86d8d9f257");
       	 log.info("困难残疾人生活查询参数："+submit2.toString());
       	 String result3 = HttpUtil.doPostJson(kncjrurl, submit2.toString());
       	 log.info("困难残疾人生活查询结果："+result3);
       	 JSONObject checkResult3 = JSON.parseObject(result3);
       	 if ("000000".equals(checkResult3.getString("rtnCode"))) {
       		 log.info("困难残疾人生活补贴查询成功");
       		 JSONObject data2 = checkResult3.getJSONObject("data");
           	 String kncjrapplyId = data2.getString("applyId");
           	 String kncjrprocessInstanceId = data2.getString("processInstanceId");
           	 String kncjractivityId = data2.getString("activityId");
           	 String kncjapplyCode = data2.getString("applyCode");
           	 String kncjapplyDate = data2.getString("applyDate");
           	 String kncjapplicantName = data2.getString("applicantName");
           	 String kncjapplicantIdCard = data2.getString("applicantIdCard");
           	 String kncjapplyType = data2.getString("applyType");
           	 String kncjitemId = data2.getString("itemId");
           	 String kncjitemName = data2.getString("itemName");
           	 String kncjcurActivityId = data2.getString("curActivityId");
           	 
           	 boolean filestatus2 = true;
           	 if (listMaterial != null && !listMaterial.isEmpty()) {
                 	for (AuditSpIMaterial material : listMaterial) {
                 		List<FrameAttachInfo> attachinfos =  iAttachService.getAttachInfoListByGuid(material.getCliengguid());
                 		if (attachinfos != null && !attachinfos.isEmpty()) {
                 			FrameAttachInfo attachinfo = attachinfos.get(0);
                 			FrameAttachStorage attachStorage = iAttachService.getAttach(attachinfo.getAttachGuid());
                 			if("户口簿索引页".equals(material.getMaterialname())) {
                 				filestatus2 = upload("uploadBooklet",kncjrapplyId,material.getMaterialname(),kncjractivityId,"1",apiSession,getBase64FromInputStream(attachStorage.getContent()));
                 			}else if ("户口簿户主页".equals(material.getMaterialname())) {
                 				filestatus2 = upload("uploadBooklet",kncjrapplyId,material.getMaterialname(),kncjractivityId,"0",apiSession,getBase64FromInputStream(attachStorage.getContent()));
                 			}
                 			else if ("户口簿本人".equals(material.getMaterialname())) {
                 				filestatus2 = upload("uploadBooklet",kncjrapplyId,material.getMaterialname(),kncjractivityId,"2",apiSession,getBase64FromInputStream(attachStorage.getContent()));
                 			}
         					else if ("身份证正面".equals(material.getMaterialname())) {
         						filestatus2 = upload("uploadIdCard",kncjrapplyId,material.getMaterialname(),kncjractivityId,"0",apiSession,getBase64FromInputStream(attachStorage.getContent()));
         					}
         					else if ("身份证反面".equals(material.getMaterialname())) {
         						filestatus2 = upload("uploadIdCard",kncjrapplyId,material.getMaterialname(),kncjractivityId,"1",apiSession,getBase64FromInputStream(attachStorage.getContent()));
         					}
                			
                 		}else {
                 			filestatus2 = false;
                		}
                 	}
                 }
           	 
           	 if(!filestatus2){
    					return false;
    		 }
       		 String kncjrSaveInfourl = clUrl + "/sdshuijiuzhushuzipingtai/kncjrSaveInfo";
           	 JSONObject submit3 = new JSONObject();
           	 submit3.put("itemId", "8a7705a298b346b2999a3b4e861d7b83");
           	 submit3.put("activityId", kncjractivityId);
           	 submit3.put("applyId", kncjrapplyId);
           	 submit3.put("manageCounty", manageCounty);
           	 submit3.put("manageTown", manageTown);
           	 submit3.put("manageVillage", manageVillage);
           	 submit3.put("idCard", idCard);
           	 submit3.put("name", name);
           	 submit3.put("sex", sex);
           	 submit3.put("nationId", nationId);
           	 submit3.put("domicileAddress", applicantAddress);
           	 submit3.put("domicileType", domicileType);
           	 submit3.put("applicantAddress", applicantAddress);
           	 submit3.put("phone", phone);
           	 submit3.put("applyReasonSelC", applyReasonSelC);
           	 submit3.put("applyReason", applyReason);
           	 submit3.put("isAssistHanding", isAssistHanding);
           	 submit3.put("applyDate", kncjapplyDate);
           	 //submit3.put("assistHandingIdCard", password);
           	 submit3.put("assistHandingRelation", assistHandingRelation);
           	 submit3.put("assistHandingName", assistHandingName);
           	 submit3.put("assistHandingPhone", assistHandingPhone);
           	 submit3.put("applicantPhotoPath", applicantPhotoPath);
           	 submit3.put("disabilityAccount", disabilityAccount);
           	 submit3.put("disabilityType", disabilityType);
           	 submit3.put("disabilityLevel", disabilityLevel);
           	 submit3.put("beginMonth", beginMonth);
           	 submit3.put("accountNumber", accountNumber);
           	 submit3.put("accountBank", accountBank);
           	 submit3.put("accountName", accountName);
           	 submit3.put("assistanceTime", assistanceTime);
           	 submit3.put("curActivityId", kncjcurActivityId);
           	 submit3.put("processInstanceId", kncjrprocessInstanceId);
           	 submit3.put("APISession", apiSession);
           	 submit3.put("iw-apikey", "dfb378cba6794bf2ba834e86d8d9f257");
           	 log.info("困难残疾人生活提交参数："+submit3.toString());
           	 String result4 = HttpUtil.doPostJson(kncjrSaveInfourl, submit3.toString());
           	 log.info("困难残疾人生活提交结果："+result4);
           	 JSONObject checkResult4 = JSON.parseObject(result4);
           	 if ("000000".equals(checkResult4.getString("rtnCode"))) {
           		 return true;
           	 }else {
           		 return false;
           	 }
       	 }else {
       		 return false;
       	 }
        	
        	
        }else {
   		 return false;
   	  }
        
        */
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取残疾人基本信息
     *
     * @param name
     * @param idcard
     * @return
     */
    public JSONObject getCjrglInfo(String name, String idcard) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl+"/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=cjrglList&userId=" + userId + "&page=1&pageNum=1&nodeId=&name=" + name + "&idcard=" + idcard + "&domicile_area=&state=&APISession=" + apiSession;
        log.info("查询残疾人记录:" + url);
        String result = "";
        result = HttpUtil.doGet(url);
        if (StringUtil.isNotBlank(result)) {
            JSONObject resultObj = JSON.parseObject(result);
            if (resultObj != null) {
                if ("000000".equals(resultObj.getString("rtnCode"))) {
                    JSONObject data = resultObj.getJSONObject("data");
                    String total = data.getString("total");
                    // 正常要么没有，要么查询出来一条
                    if ("1".equals(total)) {
                        JSONArray list = data.getJSONArray("list");
                        String userId = list.getJSONObject(0).getString("id");
                        url = clUrl+"/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=getCjrInfo&userId=" + userId + "&cjrId=" + userId + "&APISession=" + apiSession;
                        log.info("查询残疾人基本信息" + url);
                          result = HttpUtil.doGet(url);
                        if (StringUtil.isNotBlank(result)) {
                            return JSON.parseObject(result);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断是否登录成功
     * @return
     */
    public boolean isLoginSuccess(){
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        // 1、获取受理首页信息
        String url = clUrl+"/sdshuijiuzhushuzipingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=getApplyId&APISession=" + apiSession;
        log.info("获取受理首页信息" + url);
        String result = HttpUtil.doGet(url);
        JSONObject resultObj = JSON.parseObject(result);
        return "000000".equals(resultObj.getString("rtnCode"));
    }
    

    public static String getBase64FromInputStream(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(Base64.encodeBase64(data));
    }

	
	
}
