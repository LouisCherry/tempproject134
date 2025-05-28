package com.epoint.auditproject.auditproject.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.apimanage.event.basic.api.IEventDispatchService;
import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.jnzwfw.evaluationpad.api.IEvaluationPadService;
import com.epoint.sm2util.SM2Utils;
import com.epoint.sm2util.Util;
import com.epoint.ta.httprequest.util.TaHttpRequestUtils;
import com.epoint.util.TARequestUtil;
import com.epoint.xmz.jncertrecord.api.IJnCertRecordService;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * 扫码办理后台
 * @description
 * @author shibin
 * @date  2020年6月9日 上午9:44:40
 */
@RestController("jnscancodepageaction")
@Scope("request")
public class JNScanCodePageAction extends BaseController
{

    private static final long serialVersionUID = 1177302451667715305L;

    @Autowired
    private IJNAuditProject jnAuditProjectService;
    
    @Autowired
	private IEventDispatchService iEventDispatchService;
    
    
    @Autowired
    private IAttachService attachservice;
    
    @Autowired
    private IAuditTask iAuditTask;
    

    @Autowired
    private IAuditZnsbEquipment znsbEquipmentService;
    
    @Autowired
    private IEvaluationPadService iEvaluationPadService;
    
    @Autowired
    private IAuditProject iAuditProject;
    
    @Autowired
	private IJnCertRecordService iJnCertRecordService;
    

    private String projectGuid;

    /**
     * 电子身份证参数
     */
    private String privatekey = "00be74f417ec9e92fa1532d91c5a011d57b9d2ed88343b0dd716e5d3648f9b92d9";
    private String accessId = "JNS_SPJYC";
    private String accessToken = "711816CC3FAC4592A216F606A74CA89C";
    
    private static String GSYYURL = ConfigUtil.getConfigValue("datasyncjdbc", "gsyyzzurl");
    private static String JSTFILE = ConfigUtil.getConfigValue("datasyncjdbc", "JstFileUrl");
    private static String ASDQYFILE = ConfigUtil.getConfigValue("datasyncjdbc", "AsdQyFileUrl");
    private static String YTASDQYFILE = ConfigUtil.getConfigValue("datasyncjdbc", "YtAsdQyFileUrl");
    private static String YMTYYZZFILE = ConfigUtil.getConfigValue("datasyncjdbc", "YtQymYyzzFileUrl");
    private static String ApimanagerUrl = ConfigUtil.getConfigValue("datasyncjdbc", "ApimanagerUrl");

    @Override
    public void pageLoad() {

        projectGuid = getRequestParameter("projectguid");

        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectGuid,"").getResult();
        if(auditProject != null){
            /*AuditTask task = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
            if(task != null){
                String areacode = task.getAreacode();
                if(StringUtil.isBlank(areacode)){
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
                addCallbackParam("areacode",areacode);
            }*/
        	 String realareacode = ZwfwUserSession.getInstance().getAreaCode();
             if(StringUtil.isNotBlank(realareacode)) {
             	if(realareacode.length()==12) {
             		realareacode = realareacode.substring(0, 9);
             	}
             }
             addCallbackParam("areacode",realareacode);
        }

    }

    /**
     * 发送mq消息
     * @description
     * @author shibin
     * @date  2020年6月10日 上午12:26:36
     */
    public String sendMQ() {

        //system.out.println("sendMQ");
        JSONObject result = new JSONObject();
        //handlerabbitmqservice.sendMQEvaluatebyEvaluate(ZwfwUserSession.getInstance().getWindowGuid(), projectGuid,
        //        "ScanQRcode");

        String windowguid = ZwfwUserSession.getInstance().getWindowGuid();
        String Macaddress = znsbEquipmentService.getMacaddressbyWindowGuidAndType(windowguid, "4").getResult();

        //system.out.println("Macaddress:"+Macaddress);
        JSONObject dataJson = new JSONObject();
        dataJson.put("status", "999");// status设置为999
        dataJson.put("projectGuid", projectGuid);// 办件标识
        try {
            ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());

            result.put("msg", "发送成功！");
            return result.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String getAppMQQueue(String Macaddress) {
        return "mqtt-subscription-" + Macaddress + "qos1";
    }

    /**
     * 获取扫码信息
     * @description
     * @author shibin
     * @date  2020年6月5日 上午11:47:07
     */
    public String getScanCodeInfo() {

        String QRcodeInfo = "";
        JSONObject result = new JSONObject();
        String qrcode = jnAuditProjectService.getScancodeByguid(projectGuid);

        if (StringUtil.isNotBlank(qrcode)) {
        	// apimanager地址
            //apimanager流水号标识
            String apino = "JN20220826001";
            
//            String postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
//                    + "\"},\"data\":{\"qrcode\":\"" + qrcode + "\", \"useCause\":\"济宁政务服务\"}}";

            JSONObject requestjson = new JSONObject();
            JSONObject headjson1 = new JSONObject();
            JSONObject datajson1 = new JSONObject();
            headjson1.put("accessId",accessId);
            headjson1.put("accessToken",accessToken);
            requestjson.put("head",headjson1);
            //二维码
            datajson1.put("qrcode",qrcode);
            //申请事由
            JSONObject usecausejson = new JSONObject();
            //系统名称
            usecausejson.put("certificateSystemName","济宁行政审批系统");
            //事项调用
            usecausejson.put("certificateCallCategory","1");
            //事项材料名称
            usecausejson.put("certificateMatterName","扫码无需材料");
            //事项编码
            usecausejson.put("certificateEventCode","扫码无需事项id");
            //申请事由
            usecausejson.put("certificateCopyCause","扫码调用");

            datajson1.put("useCause",usecausejson.toJSONString());
            requestjson.put("data",datajson1);

            log.info("getpdfreason:"+requestjson.toJSONString());
            //TODO 去掉代理
//           Record resultParmas = iEventDispatchService.execute(ApimanagerUrl, apino, requestjson.toJSONString());
            String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/queryCertInfoByQRcode";
//            Record resultParmas = iEventDispatchService.execute(getpdfhttpUrl, apino, requestjson.toJSONString());
//
//            JSONObject resultjson = new JSONObject();
//           if (resultParmas != null) {
//               Set<String> keyset = resultParmas.keySet();
//               for (String key : keyset) {
//                   resultjson.put(key, resultParmas.get(key));
//               }
//           }
            String resultParmas = TaHttpRequestUtils.sendPost(getpdfhttpUrl, requestjson.toJSONString(), "", "");
            JSONObject resultjson = JSONObject.parseObject(resultParmas);
          /*  String postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
                    + "\"},\"data\":{\"qrcode\":\"" + qrcode + "\", \"useCause\":\"济宁政务服务\"}}";
            String httpUrl = "http://59.206.96.173:8080/main/CertShare/queryCertInfoByQRcode";
            String resultParmas = TaHttpRequestUtils.sendPost(httpUrl, postreason, "", "");*/
            JSONObject headjson = resultjson.getJSONObject("head");
            log.info("====resultParmas=====" + resultParmas);
            
            
            try {
                if ("0".equals(headjson.getString("status"))) {
                    JSONObject datajson = resultjson.getJSONObject("data");
                    byte[] decrypt;
                    decrypt = SM2Utils.decrypt(Util.hexToByte(privatekey),
                            Util.hexToByte(datajson.getString("certData")));
                    // 将字节数组转为字符串
                    String backresult = new String(decrypt, "utf-8");
                    JSONObject bacjson = JSONObject.parseObject(backresult);
                    String cert_identifier = bacjson.getString("cert_identifier");
                    String name = bacjson.getString("name");
                    String certno = bacjson.getString("certno");
                    QRcodeInfo = cert_identifier + "_SPLIT_" + name + "_SPLIT_" + certno;
                }else {
                	QRcodeInfo = "error";
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 调用省电子证照接口获取证照数据
        result.put("codeguid", QRcodeInfo);
        return result.toString();

    }
    
    
    public String getYtScanCodeInfo() {

        String QRcodeInfo = "";
        JSONObject result = new JSONObject();
        
        if(StringUtil.isBlank(projectGuid)) {
            //TODO 写死的处理
        	projectGuid = "d45d111c-d9a8-4b74-8300-fa3ae2302c3b";
        }
        
        String qrcode = jnAuditProjectService.getJstScancodeByguid(projectGuid);

        if (StringUtil.isNotBlank(qrcode)) {

            String postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
                    + "\"},\"data\":{\"qrcode\":\"" + qrcode + "\", \"useCause\":\"济宁政务服务\"}}";
            String httpUrl = "http://59.206.96.173:8080/main/CertShare/queryCertInfoByQRcode";
            String resultParmas = TaHttpRequestUtils.sendPost(httpUrl, postreason, "", "");
            JSONObject resultjson = JSONObject.parseObject(resultParmas);
            JSONObject headjson = resultjson.getJSONObject("head");
            log.info("====resultParmas=====" + resultParmas);
            try {
                if ("0".equals(headjson.getString("status"))) {
                    JSONObject datajson = resultjson.getJSONObject("data");
                    byte[] decrypt;
                    decrypt = SM2Utils.decrypt(Util.hexToByte(privatekey),
                            Util.hexToByte(datajson.getString("certData")));
                    // 将字节数组转为字符串
                    String backresult = new String(decrypt, "utf-8");
                    JSONObject bacjson = JSONObject.parseObject(backresult);
                    String cert_identifier = bacjson.getString("cert_identifier");
                    String name = bacjson.getString("name");
                    String certno = bacjson.getString("certno");
                    

                    String targetPath = null;
                    String attachguid = UUID.randomUUID().toString();
                    try {
                        String getpdfreason = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
                                + "\"},\"data\": {\"certIdentifier\": \"" + cert_identifier
                                + "\", \"useCause\":\"济宁政务服务\",\"expiryTime\": \"\"}}";
                        String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrl";
                        String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, getpdfreason, "", "");
                        JSONObject getpdfresultjson = JSONObject.parseObject(getpdfresult);
                        JSONObject getpdfheadjson = getpdfresultjson.getJSONObject("head");
                        if ("0".equals(getpdfheadjson.getString("status"))) {

                            byte[] decrypt2 = SM2Utils.decrypt(Util.hexToByte(privatekey),
                                    Util.hexToByte(getpdfresultjson.getString("data")));

                            // 将字节数组转为字符串
                            String getpdfbackresult = new String(decrypt2, "utf-8");

                            JSONObject getpdfbacjson = JSONObject.parseObject(getpdfbackresult);
                            String getpdfcontent = getpdfbacjson.getString("content");
                            //getpdfcontent = "http://59.206.96.173:8080" + getpdfcontent;

                            URL url = new URL(getpdfcontent);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 设置超时间
                            conn.setConnectTimeout(10 * 1000); // 防止屏蔽程序抓取而返回403错误
                            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                            InputStream inputStream = conn.getInputStream();

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = inputStream.read(buffer)) > -1) {
                                baos.write(buffer, 0, len);
                            }
                            baos.flush();

                            InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());

                            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                            long size = (long) inputStream.available();
                            frameAttachInfo.setAttachGuid(attachguid);
                            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                            frameAttachInfo.setAttachFileName(name + ".ofd");
                            frameAttachInfo.setCliengTag("ytdzzz");
                            frameAttachInfo.setCliengInfo(name+";"+certno);
                            frameAttachInfo.setUploadUserGuid(userSession.getUserGuid());
                            frameAttachInfo.setUploadUserDisplayName(userSession.getDisplayName());
                            frameAttachInfo.setUploadDateTime(new Date());
                            frameAttachInfo.setContentType(".ofd");
                            frameAttachInfo.setAttachLength(size);
                            attachservice.addAttach(frameAttachInfo, stream1);
                            QRcodeInfo = attachguid;
                        }
                    }
                    catch (IllegalArgumentException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 调用省电子证照接口获取证照数据
        result.put("codeguid", QRcodeInfo);
        return result.toString();

    }
    
    
    
    /**
     * 获取扫码信息
     * @description
     * @author shibin
     * @date  2020年6月5日 上午11:47:07
     */
    public String getJSTCodeInfo() {
        JSONObject datajson = new JSONObject();
        String qrcode = jnAuditProjectService.getJstScancodeByguid(projectGuid);
        String attachguid = "";
        if (StringUtil.isNotBlank(qrcode)) {
        	JSONObject submitString = new JSONObject();
            submitString.put("code", qrcode);
            String  resultsign = TARequestUtil.sendPostInner(JSTFILE, submitString.toJSONString(), "", "");
            if (StringUtil.isNotBlank(resultsign)) {
                JSONObject result = JSONObject.parseObject(resultsign);
                JSONObject custom = result.getJSONObject("custom");
                if ("1".equals(custom.getString("code"))) {
                	attachguid = custom.getString("attachguid");
                }
            }
        }
        // 调用省电子证照接口获取证照数据
        datajson.put("codeguid", attachguid);
        return datajson.toString();

    }
    
    /**
     * 获取扫码信息
     * @description
     * @author shibin
     * @date  2020年6月5日 上午11:47:07
     */
    public String getYtqymCodeInfo() {
    	JSONObject datajson = new JSONObject();
    	String qrcode = jnAuditProjectService.getJstScancodeByguid(projectGuid);
    	String attachguid = "";
    	String identity = "";
    	String name = "";
    	String ecode = "";
    	if (StringUtil.isNotBlank(qrcode)) {
    		
    		JSONObject submitString = new JSONObject();
    		submitString.put("ymttoken", qrcode);
    		String  resultsign = TARequestUtil.sendPostInner(YMTYYZZFILE, submitString.toJSONString(), "", "");
    		if (StringUtil.isNotBlank(resultsign)) {
    			JSONObject result = JSONObject.parseObject(resultsign);
    			JSONObject custom = result.getJSONObject("custom");
    			if ("1".equals(custom.getString("code"))) {
    				attachguid = custom.getString("attachGuid");
    				identity = custom.getString("identity");
    				name = custom.getString("name");
    				ecode = custom.getString("ecode");
    			}
    		}
    	}
    	// 调用省电子证照接口获取证照数据
    	datajson.put("attachguid", attachguid);
    	datajson.put("identity", identity);
    	datajson.put("name", name);
    	datajson.put("ecode", ecode);
    	return datajson.toString();
    	
    }
    /**
     * 获取扫码信息
     * @description
     * @author shibin
     * @date  2020年6月5日 上午11:47:07
     */
    public String getAsdLicenseCodeInfo() {
        JSONObject datajson = new JSONObject();
        String qrcode = jnAuditProjectService.getJstScancodeByguid(projectGuid);
      //记录调用证照的调用次数
        AuditProject project = iAuditProject.getAuditProjectByRowGuid("taskguid", projectGuid, null).getResult();
        if (project != null) {
        	AuditTask task = iAuditTask.getAuditTaskByGuid(project.getTaskguid(), false).getResult();
			if (task != null) {
				JnCertRecord record = new JnCertRecord();
				record.setOperatedate(new Date());
				record.setRowguid(UUID.randomUUID().toString());
				record.setAreacode(task.getAreacode());
				record.setRecordtotal("1");
				record.set("ouname", task.getOuname());
				record.set("ouguid", task.getOuguid());
				record.set("type", "1");//线下调用
				record.set("taskname", task.getTaskname());
				record.set("itemid", task.getItem_id());
				record.set("certtype", "11100000MB0143028R001");
	    		iJnCertRecordService.insert(record);
			}
        }
        
        String QRcodeInfo = "";
        String attachguid = "";
        String entname = "";
        String uniscid = "";
        if (StringUtil.isNotBlank(qrcode)) {
        	JSONObject submitString = new JSONObject();
            submitString.put("qrcode", qrcode);
            String  resultsign = TARequestUtil.sendPostInner(ASDQYFILE, submitString.toJSONString(), "", "");
            if (StringUtil.isNotBlank(resultsign)) {
                JSONObject result = JSONObject.parseObject(resultsign);
                JSONObject custom = result.getJSONObject("custom");
                if ("1".equals(custom.getString("code"))) {
                	log.info("营业执照材料获取："+custom.toString());
                	attachguid = custom.getString("pdfattachguid");
                	entname = custom.getString("entname");
                	uniscid = custom.getString("uniscid");
                    QRcodeInfo = attachguid + "_SPLIT_" + entname + "_SPLIT_" + uniscid;
                }
            }
        }
        // 调用省电子证照接口获取证照数据
        datajson.put("codeguid", QRcodeInfo);
        return datajson.toString();

    
    }
    
    /**
     * 获取扫码信息
     * @description
     * @author shibin
     * @date  2020年6月5日 上午11:47:07
     */
    public String getYtAsdLicenseCodeInfo() {
        JSONObject datajson = new JSONObject();
        if(StringUtil.isBlank(projectGuid)) {
        	projectGuid = "d45d111c-d9a8-4b74-8300-fa3ae2302c3b";
        }
        String qrcode = jnAuditProjectService.getJstScancodeByguid(projectGuid);
        String QRcodeInfo = "";
        String attachguid = "";
        if (StringUtil.isNotBlank(qrcode)) {
        	JSONObject submitString = new JSONObject();
            submitString.put("qrcode", qrcode);
            String  resultsign = TARequestUtil.sendPostInner(YTASDQYFILE, submitString.toJSONString(), "", "");
            if (StringUtil.isNotBlank(resultsign)) {
                JSONObject result = JSONObject.parseObject(resultsign);
                JSONObject custom = result.getJSONObject("custom");
                if ("1".equals(custom.getString("code"))) {
                	log.info("营业执照材料获取："+custom.toString());
                	attachguid = custom.getString("pdfattachguid");
                    QRcodeInfo = attachguid;
                }
            }
        }
        // 调用省电子证照接口获取证照数据
        datajson.put("codeguid", QRcodeInfo);
        return datajson.toString();

    
    }
    
    

    /**
     * 发送电子营业执照扫码取消操作消息
     * @description
     * @author shibin
     * @date  2020年6月10日 上午12:26:36
     */
    public String sendCancelLegalMQ() {

        log.info("====sendCancelMQ=====");
        JSONObject result = new JSONObject();
        String windowguid = ZwfwUserSession.getInstance().getWindowGuid();
        String Macaddress = znsbEquipmentService.getMacaddressbyWindowGuidAndType(windowguid, "4").getResult();

        JSONObject dataJson = new JSONObject();
        dataJson.put("status", "998");// 
        dataJson.put("projectGuid", projectGuid);// 办件标识
        try {
            ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());

            result.put("msg", "发送成功！");
            return result.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    
    /**
     * 根据二维码获取营业执照信息
     * @description
     * @author shibin
     * @date  2020年6月5日 上午11:47:07
     */
    public String getScanLegalCodeInfo() {
        try {
            String QRcodeInfo = "";
            JSONObject result = new JSONObject();
            String qrcode = jnAuditProjectService.getScanLegalcodeByguid(projectGuid);

            if (StringUtil.isNotBlank(qrcode)) {
            	 JSONObject submit = new JSONObject();
            	 JSONObject qrcodes = new JSONObject();
            	 qrcodes.put("qrcode", qrcode);
                 submit.put("params", qrcodes);
                 String resultsign = TARequestUtil.sendPostInner(GSYYURL, submit.toJSONString(), "", "");
                 
                 if (StringUtil.isNotBlank(resultsign)) {
                     JSONObject result1 = JSONObject.parseObject(resultsign);
                     JSONObject data =  result1.getJSONObject("status");
                     if ("200".equals(data.getString("code"))) {
                    	 JSONObject custom = result1.getJSONObject("custom");
                    	 String uniscid = custom.getString("uniscid");
                    	 String name = custom.getString("name");
                    	 String entname = custom.getString("entname");
                    	 String pdfattachguid = custom.getString("pdfattachguid");
                    	 
                    	 QRcodeInfo = uniscid + "_SPLIT_" + name + "_SPLIT_" + entname + "_SPLIT_" + pdfattachguid;
                         result.put("codeguid", QRcodeInfo);
                         // 清空二维码
                         iEvaluationPadService.updateLegalQRcodeinfoByprojectGuid(projectGuid, "");
                         // 更新办件信息
                         AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid("rowguid, applyername, certnum, legal", projectGuid, "").getResult();
                         if (auditProject != null) {
                             auditProject.setApplyername(entname);
                             auditProject.setLegal(name);
                             auditProject.setCertnum(uniscid);
                             auditProject.setApplyertype(10);
                             iAuditProject.updateProject(auditProject);
                         }
                         return result.toString();
                     }else {
                    	 // 清空二维码
                         iEvaluationPadService.updateLegalQRcodeinfoByprojectGuid(projectGuid, "");
                     }
                     
                 }
            } 
            // 调用省电子证照接口获取证照数据
            result.put("codeguid", QRcodeInfo);
            return result.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public String getProjectGuid() {
        return projectGuid;
    }

    public void setProjectGuid(String projectGuid) {
        this.projectGuid = projectGuid;
    }

}
