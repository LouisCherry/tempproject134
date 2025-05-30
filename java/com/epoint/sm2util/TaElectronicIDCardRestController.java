package com.epoint.sm2util;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;

@RestController
@RequestMapping("/taelectronicidcard")
public class TaElectronicIDCardRestController
{
	
    private String privatekey = "00be74f417ec9e92fa1532d91c5a011d57b9d2ed88343b0dd716e5d3648f9b92d9";
    private String accessId = "DASHUJU_YICHUANGSHOULI";
    private String accessToken = "B5BA2AAB58EE49D5A860BA129468719E";
    //接口记录日志的注入信息
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    private IAttachService attachservice;
    
    @Autowired
    private IJnProjectService iJnProjectService;
    
    
    @RequestMapping(value = "/getcardinfo", method = RequestMethod.POST)
    public String getcardinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSONObject.parseObject(params);
            this.log.info("=======开始调用getcardinfo接口=======");
            JSONObject obj = (JSONObject) e.get("params");
            String idnumber = obj.getString("idnumber");
            String postreason ="{\"head\":{\"accessId\":\""+accessId+"\",\"accessToken\":\""+accessToken+"\"},\"data\":{\"holderCode\":\""+idnumber+"\", \"useCause\":\"济宁政务服务\", \"holderTypeCode\":\"111\",\"certNo\":\""+idnumber+"\", \"certTypeCode\":\"11100000000013127D001\"}}";
            String httpUrl = "http://59.206.96.173:8080/main/CertShare/queryCertInfoByHolderCode";
            String result = TaHttpRequestUtils.sendPost(httpUrl, postreason, "", "");
            
            System.out.println("电子证照result:"+result);
            //String result ="{\"head\": {\"status\": 0,\"message\": \"数据获取成功\"},\"data\": {\"certData\": \"048C356ECB4EBA6E446B00F600D04E4F7BA1D476F8E8DCE86F8C31BD61451CBDBFBCD437B57B9CA9E8A694EEB8D7CEAD298854C670F994E101A7FC72C62E7EB96A4417C2DAD87C2340D38941068A70EFB0AAB0C6388AD84632EFE63820F1850C8EB81E91F1B3B5C7FA5B82886C75E82B24D787D8CDD49C6EFFBDD90B07D25C97975D6ED63C27D97CEB4626347ABDD59417EFC340FB8638CB7310832391A0B932724A50A08FF2B5CC403FEA66CBEDA5CADBD96FF41075CC9786D35DD2D61A4943F9136AB1396F807A8ADD7073057BFDC2A67726EFFAB8FA2B8F91391A38C402C22D9CD5D82F873726F9713E08445ADE127061745545A09530D257AC868511FD183067DB8AD1A52E62B3CD7802E65EF8CF9EEE6CD40F39A334479368951E84387E3320AE64\"}}";
            JSONObject resultjson = JSONObject.parseObject(result);
            JSONObject newdataJson = new JSONObject();
            JSONObject headjson = resultjson.getJSONObject("head");
            if("0".equals(headjson.getString("status"))){
                JSONObject datajson = resultjson.getJSONObject("data");
                String backresult =     SM2Utils.getDecrypt(privatekey,  datajson.getString("certData"));
                JSONObject bacjson =  JSONObject.parseObject(backresult);
                String cert_identifier =  bacjson.getString("cert_identifier");
                String name = bacjson.getString("name");
                String certno = bacjson.getString("certno");
                String getpdfreason ="{\"head\": {\"accessId\": \""+accessId+"\",\"accessToken\": \""+accessToken+"\"},\"data\": {\"certIdentifier\": \""+cert_identifier+"\", \"useCause\":\"济宁政务服务\",\"expiryTime\": \"\"}}";
                String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrl";
                String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, getpdfreason, "", "");
                System.out.println("getpdfresult:"+getpdfresult);
                JSONObject getpdfresultjson = JSONObject.parseObject(getpdfresult);
                JSONObject getpdfheadjson = getpdfresultjson.getJSONObject("head");
                if("0".equals(getpdfheadjson.getString("status"))){
        
                    String getpdfbackresult =     SM2Utils.getDecrypt(privatekey,  getpdfresultjson.getString("data"));
                    JSONObject getpdfbacjson =  JSONObject.parseObject(getpdfbackresult);
                    String getpdfcontent =  getpdfbacjson.getString("content");
                    getpdfcontent = "http://59.206.96.173:8080"+getpdfcontent;
                    
                    URL url = new URL(getpdfcontent);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 设置超时间
                    conn.setConnectTimeout(5 * 1000); // 防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    InputStream inputStream = conn.getInputStream();
                 
                    List<FrameAttachInfo> info = iJnProjectService.getFrameAttachByIdenumberTag(certno, "电子身份证打印");
                    if (info != null && info.size() > 0) {
                    	for (FrameAttachInfo attach : info) {
                    		attachservice.deleteAttachByAttachGuid(attach.getAttachGuid());
                    	}
                    }
                    
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    long size = (long) inputStream.available();
                    String cliengguid = UUID.randomUUID().toString();
                    frameAttachInfo.setCliengGuid(cliengguid);
                    frameAttachInfo.setAttachFileName(name+".pdf");
                    frameAttachInfo.setCliengTag("电子身份证打印");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(".pdf");
                    frameAttachInfo.setAttachLength(size);
                    frameAttachInfo.set("idnumber", certno);
                    frameAttachInfo.set("bigshowtype", "爱山东身份证");
                    String attachguid= attachservice.addAttach(frameAttachInfo,inputStream ).getAttachGuid();
                    log.info(getpdfcontent);
                   
                    newdataJson.put("pdfcontent", attachguid);
                }else{
                    return JsonUtils.zwdtRestReturn("0", " 调用获取身份证接口出现异常：" +headjson.getString("message"), "");
                }
            }else{
                return JsonUtils.zwdtRestReturn("0", " 调用扫码获取信息接口出现异常：" +headjson.getString("message"), "");
            }
            
            this.log.info("=======结束调用getcardinfo接口=======");
            return JsonUtils.zwdtRestReturn("1", "getcardinfo信息成功", newdataJson.toString());

        }
        catch (Exception arg15) {
            arg15.printStackTrace();
            this.log.info("=======getcardinfo接口参数：params【" + params + "】=======");
            this.log.info("=======getcardinfo异常信息：" + arg15.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", " getcardinfo信息出现异常：" + arg15.getMessage(), "");
        }
    }
}
