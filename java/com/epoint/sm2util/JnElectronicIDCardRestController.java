package com.epoint.sm2util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.core.utils.code.DESEncrypt;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController
@RequestMapping("/jnelectronicidcard")
public class JnElectronicIDCardRestController
{
	
   
    //接口记录日志的注入信息
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    private IAttachService attachservice;
    
   
    @RequestMapping(value = "/getScanLegalCodeInfo", method = RequestMethod.POST)
    public String getScanLegalCodeInfo(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String qrcode = obj.getString("qrcode");
            String QRcodeInfo = "";
            JSONObject result = new JSONObject();
            JSONObject datajson = new JSONObject();
            if (StringUtil.isNotBlank(qrcode)) {
                // 拼装参数
                JSONObject paramsObj = new JSONObject();
                JSONObject message_header = new JSONObject();
                String syscode = ConfigUtil.getConfigValue("xmzArgs", "syscode");
                String authcode = ConfigUtil.getConfigValue("xmzArgs", "authcode");
                String ddyyzzserviceurl = ConfigUtil.getConfigValue("xmzArgs", "ddyyzzserviceurl");
               
                message_header.put("syscode", syscode);
                message_header.put("authcode", authcode);
                message_header.put("businesstype", "005");
                message_header.put("sign", "taianspj");
                message_header.put("version", "2");

                JSONObject message_content = new JSONObject();
                message_content.put("qrid", qrcode);
                message_content.put("rettype", "5");
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssms");
                String opertime = df.format(System.currentTimeMillis());
                message_content.put("opertime", opertime);
                paramsObj.put("message_header", message_header);
                paramsObj.put("message_content", message_content);

                // 调用接口 获取信息
                log.info(">>>>>>>>>>>>>>>>开始获取电子证照全业务数据数据，入参为：" + paramsObj.toJSONString());
                String dzyyzzinfostr = TaHttpRequestUtils.sendPost(ddyyzzserviceurl + "/authenticationService/httpserver.do",
                        paramsObj.toJSONString(), "","");
                log.info(">>>>>>>>>>>>>>>>获取电子证照全业务数据数据返回的结果为:" + dzyyzzinfostr);
                JSONObject returnobj = JSONObject.parseObject(dzyyzzinfostr);
                JSONObject mess_header = returnobj.getJSONObject("message_header");
                if ("0".equals(mess_header.getString("errorCode"))) {
                    JSONObject mess_content = returnobj.getJSONObject("message_content");
                    // 执照下载地址地址
                    String imageurl = mess_content.getString("imageurl");
                    // 统一社会信用代码
                    String uniscid = WebUtil.getRequestParameterByUrl(imageurl, "uniscid");
                    String pdfcode = WebUtil.getRequestParameterByUrl(imageurl, "pdfcode");
                    // 法定代表人
                    String name = mess_content.getString("name");
                    // 企业名称
                    String entname = mess_content.getString("entname");
                    // 下载附件
                    String returnstr = TaHttpRequestUtils
                            .sendGet(ddyyzzserviceurl + "/authenticationService/getLicencePdf.do?uniscid=" + uniscid
                                    + "&pdfcode=" + pdfcode, "","");
                    JSONObject pdfobj = JSONObject.parseObject(returnstr);
                    JSONObject message_contentpdf = pdfobj.getJSONObject("message_content");
                    String base64 = message_contentpdf.getString("licencePDF");
                    byte[] bytespdf = org.apache.commons.codec.binary.Base64.decodeBase64(base64);
                    InputStream input = new ByteArrayInputStream(bytespdf);
                   /* String pdfattachguid = UUID.randomUUID().toString();
                    AttachUtil.saveFileInputStream(pdfattachguid, UUID.randomUUID().toString(), "电子营业执照.pdf", ".pdf",
                            "电子亮证", input.available(), input, "", "");*/
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    long size = (long) input.available();
                    String attachguid = UUID.randomUUID().toString();
                    frameAttachInfo.setAttachGuid(attachguid);
                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName("电子营业执照.pdf");
                    frameAttachInfo.setCliengTag("电子营业执照");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(".pdf");
                    frameAttachInfo.setAttachLength(size);
                    attachservice.addAttach(frameAttachInfo, input);
                    datajson.put("uniscid", uniscid);
                    datajson.put("name", name);
                    datajson.put("entname", entname);
                    datajson.put("pdfattachguid", attachguid);
                    
                    return JsonUtils.zwdtRestReturn("1", "getScanLegalCodeInfo接口调用成功", datajson);
                   
                } 

            } 
            // 调用省电子证照接口获取证照数据
           
            return JsonUtils.zwdtRestReturn("0", "二维码已过期", datajson);
        }
        catch (Exception e) {
            e.printStackTrace();
          
            return JsonUtils.zwdtRestReturn("0", "getScanLegalCodeInfo接口调用失败", e.getMessage());
        }

    }
    
    @RequestMapping(value = "/getsignfurl", method = RequestMethod.POST)
    public String getSignFurl(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String exampleattachguid = obj.getString("exampleattachguid");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();

            //officeweb服务器地址
         //   String officeweb365url = handleConfigservice.getFrameConfig("OFFICEWEB365_URL", centerguid).getResult();
            String rootpath = QueueCommonUtil.getUrlPath(request.getRequestURL().toString()) + "/rest/auditattach/readAttach?attachguid="+exampleattachguid;
            //加密key
            String deskey = ConfigUtil.getConfigValue("uploadpreview.encrypt.key");
            //加密向量
            String desiv = ConfigUtil.getConfigValue("uploadpreview.encrypt.iv");
            if(StringUtil.isNotBlank(exampleattachguid) && StringUtil.isNotBlank(deskey)){
                DESEncrypt des = new DESEncrypt(deskey, desiv);
                dataJson.put("signfurl", des.encode(rootpath).replaceAll("(\r\n|\n)", ""));   
            }else{
                dataJson.put("signfurl", rootpath);
            }
          
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getsignfurl接口参数：params【" + params + "】=======");
            log.info("=======getsignfurl异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
}
