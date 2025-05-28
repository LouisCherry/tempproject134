package com.epoint.cs.yyzzprint.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.epoint.cert.commonutils.QrcodeUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.cs.yyzzprint.api.IYyzzprintService;
import com.epoint.cs.yyzzprint.api.entity.Yyzzprint;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController
@RequestMapping("/yyzzprint")
public class YyzzPrintRestController
{
    @Autowired
    private IYyzzprintService service;
    @Autowired
    private IAttachService attachservice;
    private static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    private static final String[] IUNIT = {"元整", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰",
            "仟" };
    private static final String[] DUNIT = {"角", "分", "厘" };
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    
    @RequestMapping(value = "/getcompanyinfo", method = RequestMethod.POST)
    public String getcompanyinfo(@RequestBody String params, @Context HttpServletRequest request) {
      
            log.info("yyzzprint/getcompanyinfo开始"+new Date());
            JSONObject e = JSONObject.parseObject(params);
            JSONObject obj = e.getJSONObject("params");
            String idnum = obj.getString("certnum");
            String name = obj.getString("name");
            String sfz = obj.getString("sfz");
            String machineno = obj.getString("machineno");
            String faceimg = obj.getString("faceimg");
            String fontimg = obj.getString("fontimg");
            String macaddress = obj.getString("macaddress");
            String address =  obj.getString("address");
            JSONObject result = new JSONObject();
         try {      
             String certnum = obj.getString("certnum");
             Map<String, String> headers = new HashMap<String, String>();
             headers.put("ApiKey", "571374208735510528");
             String url = "http://59.206.96.178:9960/gateway/api/6/gsj_qydjbaxx?q=" + certnum;
             String results = com.epoint.cert.commonutils.HttpUtil.doGet(url, headers);
            /* JSONObject a = new JSONObject();
             a.put("data", "{\"code\":200,\"data\":\"{\\\"header\\\":{\\\"status\\\":\\\"1\\\",\\\"msg\\\":\\\"查询成功\\\"},\\\"data\\\":{\\\"alterRecoder\\\":[],\\\"baseinfo\\\":{\\\"apprdate\\\":\\\"2018-12-10\\\",\\\"uniscid\\\":\\\"91370800MA3NRCRQ84\\\",\\\"dom\\\":\\\"山东省济宁北湖省级旅游度假区荷花路京投总部大厦A座1204室\\\",\\\"empnum\\\":20,\\\"entname\\\":\\\"济宁公用快速路建设工程有限公司\\\",\\\"enttypeCn\\\":\\\"有限责任公司(非自然人投资或控股的法人独资)\\\",\\\"estdate\\\":\\\"2018-12-10\\\",\\\"industryco\\\":\\\"4813\\\",\\\"industryphy\\\":\\\"E\\\",\\\"lerep\\\":\\\"李鲁\\\",\\\"opfrom\\\":\\\"2018-12-10\\\",\\\"opscope\\\":\\\"市政道路工程、城市及道路照明工程、园林绿化工程、土石方工程、管道工程、地下综合管廊工程施工。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\\\",\\\"opto\\\":\\\"2048-12-09\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\",\\\"regcap\\\":10000,\\\"regcapcurCn\\\":null,\\\"regno\\\":\\\"370891000000341\\\",\\\"regorg\\\":\\\"370891\\\",\\\"regorgCn\\\":\\\"济宁北湖省级旅游度假区市场监督管理局\\\",\\\"regstateCn\\\":\\\"在营（开业）企业\\\",\\\"town\\\":\\\"否\\\"},\\\"brchinfo\\\":[],\\\"cancel\\\":null,\\\"contact\\\":{\\\"lmid\\\":\\\"370891000012018120411236\\\",\\\"cerno\\\":\\\"370802196906253619\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"丁磊\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\"},\\\"investment\\\":[{\\\"acconam\\\":null,\\\"blicno\\\":\\\"9137080016593424X9\\\",\\\"blictypeCn\\\":\\\"企业法人营业执照(公司)\\\",\\\"condate\\\":\\\"2023-12-03\\\",\\\"conform\\\":\\\"1\\\",\\\"inv\\\":\\\"山东公用控股有限公司\\\",\\\"invid\\\":\\\"ff808081677dc0cd016787842e692477\\\",\\\"invtypeCn\\\":\\\"企业法人\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\",\\\"subconam\\\":10000,\\\"subconform\\\":\\\"1\\\"}],\\\"invPerson\\\":[],\\\"priPerson\\\":[{\\\"personid\\\":\\\"c0cd01678788e18025e1\\\",\\\"cerno\\\":\\\"37080219651204331X\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"李鲁\\\",\\\"positionCn\\\":\\\"执行董事\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\"},{\\\"personid\\\":\\\"ff808081677dc0cd0167878b2ba226c9\\\",\\\"cerno\\\":\\\"370802196906253619\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"丁磊\\\",\\\"positionCn\\\":\\\"监事\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\"},{\\\"personid\\\":\\\"c0cd0167878e20e927b3\\\",\\\"cerno\\\":\\\"370802196509213912\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"冯中君\\\",\\\"positionCn\\\":\\\"经理\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\"}],\\\"revoke\\\":null,\\\"liquidating\\\":null}}\",\"message\":\"\"}");
             a.put("message" , "");
             a.put("code", "200");
             String results = a.toJSONString();*/
             if (results == null) {
                 return JsonUtils.zwdtRestReturn("0", "企业信息未获取到，请重新申报", "");
             }
             JSONObject res = JSONObject.parseObject(results);
             JSONObject data = res.getJSONObject("data");
             JSONObject data1 = data.getJSONObject("data");
             JSONObject data2 = data1.getJSONObject("data");
             if (data2 == null) {
                 return JsonUtils.zwdtRestReturn("0", "企业信息核验不通过", "");
             } else {
                 JSONObject baseinfo = data2.getJSONObject("baseinfo");
                 JSONArray priPerson = data2.getJSONArray("priPerson");
                 result.put("baseinfo", baseinfo);
                 result.put("priPerson", priPerson);
                String uniscid =    baseinfo.getString("uniscid");
                int printtimes =  service.getPrintCount(uniscid);
                if(printtimes==0){
                    
                    String creditcode = baseinfo.getString("uniscid");
                    String dwmc = baseinfo.getString("entname");
                    String companytype = baseinfo.getString("enttypeCn");
                    String reason = idnum;
                    String legalman = baseinfo.getString("lerep");
                    String jyfw = baseinfo.getString("opscope");
                    String regcap =baseinfo.getString("regcap");
                    String opto = baseinfo.getString("opto");
                    String estdate = baseinfo.getString("estdate");
                    String opfrom = baseinfo.getString("opfrom");
                    String apprdate = baseinfo.getString("apprdate");
                    String dom = baseinfo.getString("dom");
                    String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
               
                    String zbdoctem = "";
                    new License().setLicense(licenseName);
                    String overall = "overall";
                    if (StringUtil.isNotBlank(address)){
                        overall = address;
                    }
                    zbdoctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/jiningzwfw/individuation/"+overall+"/equipmentdisplay/selfservicemachine/yyzz/yyzzzb.docx";
                    String cnestdate = estdate.substring(0, 4) + "年" + estdate.substring(5, 7) + "月" + estdate.substring(8)
                            + "日";
                    String cnopfrom = opfrom.substring(0, 4) + "年" + opfrom.substring(5, 7) + "月" + opfrom.substring(8) + "日";
                    String cnopto = "";
                    if(StringUtil.isNotBlank(opto)){
                        cnopto = opto.substring(0, 4) + "年" + opto.substring(5, 7) + "月" + opto.substring(8) + "日";
                    }else{
                        cnopto = "  年    月    日";
                    }
                   
                    Document doc = new Document(zbdoctem);
                    String cnregcap = toChinese(regcap + "0000");
                    String[] fieldNames = null;
                    Object[] values = null;
                    Map<String, String> map = new HashMap(16);
                    map.put("creditcode", creditcode);
                    map.put("dwmc", dwmc);
                    map.put("companytype", companytype);
                    map.put("legalman", legalman);
                    map.put("jyfw", jyfw);
                    map.put("ZCZB", cnregcap);
                    map.put("CLRQ", cnestdate);
                    map.put("ValidFrom", cnopfrom + "至" + cnopto);
                    map.put("Address", dom);
                    map.put("year", apprdate.substring(0, 4));
                    map.put("mon", apprdate.substring(5, 7));
                    map.put("day", apprdate.substring(8));

                    fieldNames = new String[map.size()];
                    values = new Object[map.size()];
                    int num = 0;

                    for (Iterator var18 = map.entrySet().iterator(); var18.hasNext(); ++num) {
                        Entry<String, String> entry = (Entry) var18.next();
                        fieldNames[num] = (String) entry.getKey();
                        values[num] = entry.getValue();
                    }

                    doc.getMailMerge().execute(fieldNames, values);
                    InputStream qrCode = QrcodeUtil.getQrCode("http://www.gsxt.gov.cn/index.html?uniscid=" + creditcode, 130,
                            130);

                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("EWM");
                    build.insertImage(qrCode);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    doc.save(outputStream, 10);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                    long size = (long) inputStream.available();
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName("营业执照正本.doc");
                    frameAttachInfo.setCliengTag("营业执照打印");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType("application/msword");
                    frameAttachInfo.setAttachLength(size);
                    String attachguid = attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid();
                    log.info("yyzzprint/setprintrecord正本结束"+new Date()+"    attac"+attachguid);
                    String fbdoctem = "";

                    fbdoctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/jiningzwfw/individuation/"+overall+"/equipmentdisplay/selfservicemachine/yyzz/yyzzfb.docx";

                    Document fbdoc = new Document(fbdoctem);
                 
                    InputStream fbqrCode = QrcodeUtil.getQrCode("http://www.gsxt.gov.cn/index.html?uniscid=" + creditcode, 130,
                            130);

                    DocumentBuilder fbbuild = new DocumentBuilder(fbdoc);
                    fbbuild.moveToBookmark("ewm");
                    fbbuild.insertImage(fbqrCode);
                    fbdoc.getMailMerge().execute(fieldNames, values);

                    ByteArrayOutputStream fboutputStream = new ByteArrayOutputStream();
                    fbdoc.save(fboutputStream, 10);
                    ByteArrayInputStream fbinputStream = new ByteArrayInputStream(fboutputStream.toByteArray());
                    long fbsize = (long) fbinputStream.available();
                    FrameAttachInfo fbframeAttachInfo = new FrameAttachInfo();
                    fbframeAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    fbframeAttachInfo.setAttachFileName("营业执照副本.doc");
                    fbframeAttachInfo.setCliengTag("营业执照打印副本");
                    fbframeAttachInfo.setUploadUserGuid("");
                    fbframeAttachInfo.setUploadUserDisplayName("");
                    fbframeAttachInfo.setUploadDateTime(new Date());
                    fbframeAttachInfo.setContentType("application/msword");
                    fbframeAttachInfo.setAttachLength(fbsize);
                    String fbattachguid = attachservice.addAttach(fbframeAttachInfo, fbinputStream).getAttachGuid();

                    
                   
                    result.put("zbattachguid", attachguid);
                    result.put("fbattachguid", fbattachguid);
                    result.put("isprint", 0);
                    outputStream.close();
                  
                    inputStream.close();
                    
                    
                    Yyzzprint record = new Yyzzprint();
                    record.setRowguid(UUID.randomUUID().toString());
                    String fontattachguid="";
                    String faceattachguid= "";
                    if(StringUtil.isNotBlank(fontimg)){
                      fontattachguid= getimgattachguid( fontimg, creditcode+"font");
                    }
                    if(StringUtil.isNotBlank(faceimg)){
                        faceattachguid= getimgattachguid(faceimg, creditcode+"face");
                      }
                    record.set("fontattachguid",fontattachguid);
                    record.set("faceattachguid",faceattachguid);
                    record.setCreditcode(creditcode);
                    record.setOperatedate(new Date());
                    record.setCompanytype(companytype);
                    record.setJyfw(jyfw);
                    record.setDwmc(dwmc);
                    record.setLegalman(legalman);
                    record.setMachineno(machineno);
                    record.setAttacguid(attachguid);
                    record.setFbattacguid(fbattachguid);
                    record.set("isprint", 0);
                    record.set("reason",idnum);
                    record.set("username",name);
                    record.set("sfz",sfz);
                    record.setOperatedate(new Date());
                    
                    service.insert(record);
                }else{
                    Yyzzprint printdetail =   service.getPrintdeatil(uniscid);
                    result.put("isprint", printdetail.getStr("isprint"));
                    if("0".equals(printdetail.getStr("isprint"))){
                        result.put("zbattachguid", printdetail.getAttacguid());
                        result.put("fbattachguid", printdetail.getFbattacguid()); 
                        printdetail.setOperatedate(new Date());
                        printdetail.set("reason",idnum);
                        printdetail.set("username",name);
                        printdetail.set("sfz",sfz);
                        printdetail.setOperatedate(new Date());
                        
                        service.update(printdetail);
                    }
                    
                }
                
               
                log.info("yyzzprint/getcompanyinfo结束"+new Date());
                return JsonUtils.zwdtRestReturn("1", "获取营业执照信息成功", result.toString());
            }
            
           
          
          
        }
        catch (Exception e1) {
           
            return JsonUtils.zwdtRestReturn("0", "接口调用异常：" + e1.getMessage(), "");
        }

    }
    @RequestMapping(value = "/getpersonalinfo", method = RequestMethod.POST)
    public String getpersonalinfo(@RequestBody String params, @Context HttpServletRequest request) {
        
            log.info("yyzzprint/getpersonalinfo开始"+new Date());
            JSONObject e = JSONObject.parseObject(params);
            JSONObject obj = e.getJSONObject("params");
            String idnum = obj.getString("certnum");
            String name = obj.getString("name");
            String sfz = obj.getString("sfz");
            String machineno = obj.getString("machineno");
            String faceimg = obj.getString("faceimg");
            String fontimg = obj.getString("fontimg");
            String macaddress = obj.getString("macaddress");
            String address = obj.getString("address");
            JSONObject result = new JSONObject();
          try { 
              String certnum = obj.getString("certnum");
              Map<String, String> headers = new HashMap<String, String>();
              headers.put("ApiKey", "576417450254401536");
              String url = "http://59.206.96.178:9960/gateway/api/3/gsjgtdjxxcxjk?q=" + certnum;
              String results = com.epoint.cert.commonutils.HttpUtil.doGet(url, headers);
              
            /*  JSONObject a = new JSONObject();
              a.put("data", "{\"code\":200,\"data\":\"{\\\"header\\\":{\\\"status\\\":\\\"1\\\",\\\"msg\\\":\\\"查询成功\\\"},\\\"data\\\":{\\\"baseinfo\\\":{\\\"apprdate\\\":\\\"2019-04-03\\\",\\\"compformCn\\\":\\\"个人经营\\\",\\\"estdate\\\":\\\"2018-11-27\\\",\\\"name\\\":\\\"唐芹\\\",\\\"oploc\\\":\\\"泰安市泰山区东岳大街130号一楼（15）\\\",\\\"opscope\\\":\\\"打字复印、名片设计、电脑制图、喷绘、写真、文化用品、电脑耗材、复印机、打印机、通讯器材销售、其它印刷品印刷（依法须经批准的项目，经相关部门批准后方可开展经营活动）\\\",\\\"pripid\\\":\\\"370902025052018112034638\\\",\\\"regno\\\":\\\"370902601053030\\\",\\\"regorg\\\":\\\"370902\\\",\\\"regorgCn\\\":\\\"泰安市泰山区市场监督管理局\\\",\\\"regstateCn\\\":\\\"注销企业\\\",\\\"traname\\\":\\\"泰安市泰山区宏泰图文快印中心\\\",\\\"uniscid\\\":\\\"92370902MA3NN0543F\\\",\\\"empnum\\\":\\\"2\\\",\\\"industryphy\\\":\\\"居民服务、修理和其他服务业\\\"},\\\"operator\\\":{\\\"cerno\\\":\\\"370983197805076121\\\",\\\"name\\\":\\\"唐芹\\\",\\\"personid\\\":\\\"370902025052018112034638\\\",\\\"pripid\\\":\\\"370902025052018112034638\\\"},\\\"alterRecoders\\\":[{\\\"altaf\\\":\\\"打字复印、名片设计、电脑制图、喷绘、写真、文化用品、电脑耗材、复印机、打印机、通讯器材销售、其它印刷品印刷（依法须经批准的项目，经相关部门批准后方可开展经营活动）\\\",\\\"altbe\\\":\\\"打字复印、名片设计、电脑制图、喷绘、写真、文化用品、电脑耗材、复印机、打印机、通讯器材销售（依法须经批准的项目，经相关部门批准后方可开展经营活动）\\\",\\\"altdate\\\":\\\"2018-12-18\\\",\\\"altid\\\":\\\"bee1883dbee1d9c700ff80808167b0bc7f0167bee1423c5189\\\",\\\"altitemCn\\\":\\\"经营范围及方式\\\",\\\"pripid\\\":\\\"370902025052018112034638\\\"},{\\\"altaf\\\":null,\\\"altbe\\\":null,\\\"altdate\\\":\\\"2018-12-18\\\",\\\"altid\\\":\\\"bee1883fbee1d9cb00ff80808167b0bc7f0167bee1423d518b\\\",\\\"altitemCn\\\":\\\"行政审批\\\",\\\"pripid\\\":\\\"370902025052018112034638\\\"}],\\\"cancel\\\":null,\\\"revoke\\\":null}}\",\"message\":\"\"}");
              a.put("message" , "");
              a.put("code", "200");
              String results = a.toJSONString();*/
              if (results == null) {
                  return JsonUtils.zwdtRestReturn("0", "个人单位信息未获取到，请重新申报", "");
              }
              JSONObject res = JSONObject.parseObject(results);
              JSONObject data = res.getJSONObject("data");
              JSONObject data1 = data.getJSONObject("data");
              JSONObject data2 = data1.getJSONObject("data");
              if (data2 == null) {
                  return JsonUtils.zwdtRestReturn("0", "个人单位信息核验不通过", "");
              } else {
                  JSONObject baseinfo = data2.getJSONObject("baseinfo");
                  JSONObject operator = data2.getJSONObject("operator");
           
                result.put("baseinfo", baseinfo);
                result.put("operator", operator);
                String uniscid =    baseinfo.getString("uniscid");
                int printtimes =  service.getPrintCount(uniscid);
                String fontattachguid="";
                String faceattachguid= "";
                if(StringUtil.isNotBlank(fontimg)){
                  fontattachguid= getimgattachguid( fontimg, uniscid+"font");
                }
                if(StringUtil.isNotBlank(faceimg)){
                    faceattachguid= getimgattachguid(faceimg, uniscid+"face");
                 }
                if(printtimes==0){
                    
                    String creditcode =  baseinfo.getString("uniscid");
                    String dwmc =  baseinfo.getString("traname");
                    String companytype =  "个体工商户";
                    String legalman =  baseinfo.getString("name");
                  
                    String jyfw =  baseinfo.getString("opscope");
                    String compformCn =  baseinfo.getString("compformCn");
                    String estdate =  baseinfo.getString("estdate");
                    estdate =  estdate.substring(0, 4)+"年"+estdate.substring(5, 7)+"月"+estdate.substring(8)+"日";
                    baseinfo.put("estdate", estdate);
                    result.put("baseinfo", baseinfo);
                    String apprdate =  baseinfo.getString("apprdate");
                    String dom =  baseinfo.getString("oploc");
                
                    String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                    JSONObject dataJson = new JSONObject();
                    new License().setLicense(licenseName);
                    String zbdoctem = "";
                    String overall = "overall";
                    if (StringUtil.isNotBlank(address)){
                        overall = address;
                    }
                    zbdoctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/jiningzwfw/individuation/"+overall+"/equipmentdisplay/selfservicemachine/yyzz/gryyzzzb.docx";
                       Document doc = new Document(zbdoctem);
                 
                    String[] fieldNames = null;
                    Object[] values = null;
                    Map<String, String> map = new HashMap(16);
                    map.put("BH", creditcode);
                    map.put("DWMC", dwmc);
                    map.put("Type", companytype);
                    map.put("ZCXS", compformCn);
                    map.put("JYZ", legalman);
                    map.put("ZCRQ", estdate);
                    map.put("BusinessScope",jyfw );
                    map.put("ValidFrom", dom);
                    map.put("Address", dom);
                  
                    map.put("IssueYear", apprdate.substring(0, 4));
                    map.put("IssueMonth", apprdate.substring(5, 7));
                    map.put("IssueDay", apprdate.substring(8));

                    fieldNames = new String[map.size()];
                    values = new Object[map.size()];
                    int num = 0;

                    for (Iterator var18 = map.entrySet().iterator(); var18.hasNext(); ++num) {
                        Entry<String, String> entry = (Entry) var18.next();
                        fieldNames[num] = (String) entry.getKey();
                        values[num] = entry.getValue();
                    }

                    doc.getMailMerge().execute(fieldNames, values);
                    InputStream qrCode = QrcodeUtil.getQrCode("http://www.gsxt.gov.cn/index.html?uniscid=" + creditcode, 130,
                            130);

                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("EWM");
                    build.insertImage(qrCode);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    doc.save(outputStream, 10);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                    long size = (long) inputStream.available();
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName("营业执照正本.doc");
                    frameAttachInfo.setCliengTag("营业执照打印");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType("application/msword");
                    frameAttachInfo.setAttachLength(size);
                    String attachguid = attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid();

                    String fbdoctem = "";

                    fbdoctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/jiningzwfw/individuation/"+overall+"/equipmentdisplay/selfservicemachine/yyzz/gryyzzfb.docx";

                    Document fbdoc = new Document(fbdoctem);
                 
                    InputStream fbqrCode = QrcodeUtil.getQrCode("http://www.gsxt.gov.cn/index.html?uniscid=" + creditcode, 130,
                            130);

                    DocumentBuilder fbbuild = new DocumentBuilder(fbdoc);
                    fbbuild.moveToBookmark("ewm");
                    fbbuild.insertImage(fbqrCode);
                    fbdoc.getMailMerge().execute(fieldNames, values);

                    ByteArrayOutputStream fboutputStream = new ByteArrayOutputStream();
                    fbdoc.save(fboutputStream, 10);
                    ByteArrayInputStream fbinputStream = new ByteArrayInputStream(fboutputStream.toByteArray());
                    long fbsize = (long) fbinputStream.available();
                    FrameAttachInfo fbframeAttachInfo = new FrameAttachInfo();
                    fbframeAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    fbframeAttachInfo.setAttachFileName("营业执照副本.doc");
                    fbframeAttachInfo.setCliengTag("营业执照打印副本");
                    fbframeAttachInfo.setUploadUserGuid("");
                    fbframeAttachInfo.setUploadUserDisplayName("");
                    fbframeAttachInfo.setUploadDateTime(new Date());
                    fbframeAttachInfo.setContentType("application/msword");
                    fbframeAttachInfo.setAttachLength(fbsize);
                    String fbattachguid = attachservice.addAttach(fbframeAttachInfo, fbinputStream).getAttachGuid();

                    result.put("isprint", "0");
                    result.put("zbattachguid", attachguid);
                    result.put("fbattachguid", fbattachguid);
                    outputStream.close();
                    inputStream.close();
                    
                    Yyzzprint record = new Yyzzprint();
                    record.setRowguid(UUID.randomUUID().toString());
                 
                    record.set("fontattachguid",fontattachguid);
                    record.set("faceattachguid",faceattachguid);
                    record.setCreditcode(creditcode);
                    record.setOperatedate(new Date());
                    record.setCompanytype(companytype);
                    record.setJyfw(jyfw);
                    record.setDwmc(dwmc);
                    record.setLegalman(legalman);
                    record.setMachineno(machineno);
                    record.setAttacguid(attachguid);
                    record.setFbattacguid(fbattachguid);
                    record.set("isprint", 0);
                    record.set("reason",idnum);
                    record.set("username",name);
                    record.set("sfz",sfz);
                    record.setOperatedate(new Date());
                    
                    service.insert(record);
                }else{
                    Yyzzprint printdetail =   service.getPrintdeatil(uniscid);
                    result.put("isprint", printdetail.getStr("isprint"));
                    if("0".equals(printdetail.getStr("isprint"))){
                        result.put("zbattachguid", printdetail.getAttacguid());
                        result.put("fbattachguid", printdetail.getFbattacguid());
                        printdetail.set("fontattachguid",fontattachguid);
                        printdetail.set("faceattachguid",faceattachguid);
                        printdetail.setOperatedate(new Date());
                        printdetail.set("reason",idnum);
                        printdetail.set("username",name);
                        printdetail.set("sfz",sfz);
                        printdetail.setOperatedate(new Date());
                        
                        service.update(printdetail);
                    }
             
                }
              
                log.info("yyzzprint/getpersonalinfo结束"+new Date());
                return JsonUtils.zwdtRestReturn("1", "获取营业执照信息成功", result.toString());
            }
            
           
          
          
        }
        catch (Exception e1) {
            
            return JsonUtils.zwdtRestReturn("0", "接口调用异常：" + e1.getMessage(), "");
        }

    }
    @RequestMapping(value = "/updateprintrecord", method = RequestMethod.POST)
    public String updateprintrecord(@RequestBody String params) {
        try {
            log.info("yyzzprint/updateprintrecord开始"+new Date());
            JSONObject e = JSONObject.parseObject(params);
            JSONObject obj = e.getJSONObject("params");
            String uniscid = obj.getString("uniscid");
           
            Yyzzprint printdetail =   service.getPrintdeatil(uniscid);
            printdetail.set("isprint", "1");
            int i =service.update(printdetail);
          
        
         
            log.info("yyzzprint/updateprintrecord结束"+new Date());
            return JsonUtils.zwdtRestReturn("1", "接口调用成功！", "");
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口调用异常：" + e.getMessage(), "");
        }

    }
    @RequestMapping(value = "/getprintcount", method = RequestMethod.POST)
    public String getprintcount(@RequestBody String params) {
        try {
            log.info("yyzzprint/getprintcount开始"+new Date());
            JSONObject e = JSONObject.parseObject(params);
            JSONObject obj = e.getJSONObject("params");
            String idnum = obj.getString("certnum");
            String name = obj.getString("name");
            String sfz = obj.getString("sfz");
          
            JSONObject dataJson = new JSONObject();
            int printtimes =  service.getPrintCount(idnum);
            if(printtimes==0){
                Yyzzprint record = new Yyzzprint();
                record.setRowguid(UUID.randomUUID().toString());
      
                record.set("reason",idnum);
              
                record.set("username",name);
                record.set("sfz",sfz);
                record.setOperatedate(new Date());
                service.insert(record);
            }
            dataJson.put("printtime",printtimes);
            log.info("yyzzprint/getprintcount结束"+new Date());
            return JsonUtils.zwdtRestReturn("1", "接口调用成功！", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口调用异常：" + e.getMessage(), "");
        }

    }

    @RequestMapping(value = "/setprintrecord", method = RequestMethod.POST)
    public String setprintrecord(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("yyzzprint/setprintrecord开始"+new Date());
            JSONObject e = JSONObject.parseObject(params);
            JSONObject obj = e.getJSONObject("params");
            String creditcode = obj.getString("idnum");
            String dwmc = obj.getString("dwmc");
            String companytype = obj.getString("companytype");
            String reason = obj.getString("reason");
            String legalman = obj.getString("legalman");
            String machineno = obj.getString("machineno");
            String jyfw = obj.getString("jyfw");
            String regcap = obj.getString("regcap");
            String opto = obj.getString("opto");
            String estdate = obj.getString("estdate");
            String opfrom = obj.getString("opfrom");
            String apprdate = obj.getString("apprdate");
            String dom = obj.getString("dom");
            String faceimg = obj.getString("faceimg");
            String fontimg = obj.getString("fontimg");
            String address = obj.getString("address");
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            JSONObject dataJson = new JSONObject();
            (new License()).setLicense(licenseName);
            String zbdoctem = "";
            String overall = "overall";
            if (StringUtil.isNotBlank(address)){
                overall = address;
            }
            zbdoctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/jiningzwfw/individuation/"+overall+"/equipmentdisplay/selfservicemachine/yyzz/yyzzzb.docx";
            String cnestdate = estdate.substring(0, 4) + "年" + estdate.substring(5, 7) + "月" + estdate.substring(8)
                    + "日";
            String cnopfrom = opfrom.substring(0, 4) + "年" + opfrom.substring(5, 7) + "月" + opfrom.substring(8) + "日";
            String cnopto = "";
            if(StringUtil.isNotBlank(opto)){
                cnopto = opto.substring(0, 4) + "年" + opto.substring(5, 7) + "月" + opto.substring(8) + "日";
            }
           
            Document doc = new Document(zbdoctem);
            String cnregcap = toChinese(regcap + "0000");
            String[] fieldNames = null;
            Object[] values = null;
            Map<String, String> map = new HashMap(16);
            map.put("creditcode", creditcode);
            map.put("dwmc", dwmc);
            map.put("companytype", companytype);
            map.put("legalman", legalman);
            map.put("jyfw", jyfw);
            map.put("ZCZB", cnregcap);
            map.put("CLRQ", cnestdate);
            map.put("ValidFrom", cnopfrom + "至" + cnopto);
            map.put("Address", dom);
            map.put("year", apprdate.substring(0, 4));
            map.put("mon", apprdate.substring(5, 7));
            map.put("day", apprdate.substring(8));

            fieldNames = new String[map.size()];
            values = new Object[map.size()];
            int num = 0;

            for (Iterator var18 = map.entrySet().iterator(); var18.hasNext(); ++num) {
                Entry<String, String> entry = (Entry) var18.next();
                fieldNames[num] = (String) entry.getKey();
                values[num] = entry.getValue();
            }

            doc.getMailMerge().execute(fieldNames, values);
            InputStream qrCode = QrcodeUtil.getQrCode("http://www.gsxt.gov.cn/index.html?uniscid=" + creditcode, 130,
                    130);

            DocumentBuilder build = new DocumentBuilder(doc);
            build.moveToBookmark("EWM");
            build.insertImage(qrCode);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, 10);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = (long) inputStream.available();
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName("营业执照正本.doc");
            frameAttachInfo.setCliengTag("营业执照打印");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("application/msword");
            frameAttachInfo.setAttachLength(size);
            String attachguid = attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid();
            log.info("yyzzprint/setprintrecord正本结束"+new Date()+"    attac"+attachguid);
            String fbdoctem = "";

            fbdoctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/jiningzwfw/individuation/"+overall+"/equipmentdisplay/selfservicemachine/yyzz/yyzzfb.docx";

            Document fbdoc = new Document(fbdoctem);
         
            InputStream fbqrCode = QrcodeUtil.getQrCode("http://www.gsxt.gov.cn/index.html?uniscid=" + creditcode, 130,
                    130);

            DocumentBuilder fbbuild = new DocumentBuilder(fbdoc);
            fbbuild.moveToBookmark("ewm");
            fbbuild.insertImage(fbqrCode);
            fbdoc.getMailMerge().execute(fieldNames, values);

            ByteArrayOutputStream fboutputStream = new ByteArrayOutputStream();
            fbdoc.save(fboutputStream, 10);
            ByteArrayInputStream fbinputStream = new ByteArrayInputStream(fboutputStream.toByteArray());
            long fbsize = (long) fbinputStream.available();
            FrameAttachInfo fbframeAttachInfo = new FrameAttachInfo();
            fbframeAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            fbframeAttachInfo.setAttachFileName("营业执照副本.doc");
            fbframeAttachInfo.setCliengTag("营业执照打印副本");
            fbframeAttachInfo.setUploadUserGuid("");
            fbframeAttachInfo.setUploadUserDisplayName("");
            fbframeAttachInfo.setUploadDateTime(new Date());
            fbframeAttachInfo.setContentType("application/msword");
            fbframeAttachInfo.setAttachLength(fbsize);
            String fbattachguid = attachservice.addAttach(fbframeAttachInfo, fbinputStream).getAttachGuid();

            Yyzzprint record = service.getPrint(reason);
            if(record!=null){
                String fontattachguid="";
                String faceattachguid= "";
                if(StringUtil.isNotBlank(fontimg)){
                  fontattachguid= getimgattachguid( fontimg, creditcode+"font");
                }
                if(StringUtil.isNotBlank(faceimg)){
                    faceattachguid= getimgattachguid(faceimg, creditcode+"face");
                  }
                record.set("fontattachguid",fontattachguid);
                record.set("faceattachguid",faceattachguid);
                record.setCreditcode(creditcode);
                record.setOperatedate(new Date());
                record.setCompanytype(companytype);
                record.setJyfw(jyfw);
                record.setDwmc(dwmc);
                record.setLegalman(legalman);
                record.setMachineno(machineno);
                record.setAttacguid(attachguid);
                record.setFbattacguid(fbattachguid);
                service.update(record); 
            }
           
            dataJson.put("zbattachguid", attachguid);
            dataJson.put("fbattachguid", fbattachguid);
            outputStream.close();
            inputStream.close();
            log.info("yyzzprint/setprintrecord结束"+new Date());
            return JsonUtils.zwdtRestReturn("1", "接口调用成功！", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口调用异常：" + e.getMessage(), "");
        }

    }
    @RequestMapping(value = "/setgrprintrecord", method = RequestMethod.POST)
    public String setgrprintrecord(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("yyzzprint/setgrprintrecord开始"+new Date());
            JSONObject e = JSONObject.parseObject(params);
            JSONObject obj = e.getJSONObject("params");
            String creditcode = obj.getString("idnum");
            String dwmc = obj.getString("dwmc");
            String companytype = obj.getString("companytype");
            String legalman = obj.getString("name");
            String machineno = obj.getString("machineno");
            String jyfw = obj.getString("jyfw");
            String compformCn = obj.getString("compformCn");
            String reason = obj.getString("reason");
            String apprdate = obj.getString("apprdate");


            String dom = obj.getString("dom");
            String faceimg = obj.getString("faceimg");
            String fontimg = obj.getString("fontimg");
            String address = obj.getString("address");
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            JSONObject dataJson = new JSONObject();
            (new License()).setLicense(licenseName);
            String zbdoctem = "";
            String overall = "overall";
            if (StringUtil.isNotBlank(address)){
                overall = address;
            }
            zbdoctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/jiningzwfw/individuation/"+overall+"/equipmentdisplay/selfservicemachine/yyzz/gryyzzzb.docx";
               Document doc = new Document(zbdoctem);
         
            String[] fieldNames = null;
            Object[] values = null;
            Map<String, String> map = new HashMap(16);
            map.put("BH", creditcode);
            map.put("DWMC", dwmc);
            map.put("Type", companytype);
            map.put("ZCXS", compformCn);
            map.put("JYZ", legalman);
            apprdate =  apprdate.substring(0, 4)+"年"+apprdate.substring(5, 7)+"月"+apprdate.substring(8)+"日";
            map.put("ZCRQ", apprdate);
            map.put("BusinessScope",jyfw );
            map.put("ValidFrom", dom);
            map.put("Address", dom);
            http://localhost:8090/epoint-web-zwfw/rest/auditattach/readAttach?attachguid=2a558dbc-9749-48b2-8d30-3b46ee8178ef
            map.put("IssueYear", apprdate.substring(0, 4));
            map.put("IssueMonth", apprdate.substring(5, 7));
            map.put("IssueDay", apprdate.substring(8));

            fieldNames = new String[map.size()];
            values = new Object[map.size()];
            int num = 0;

            for (Iterator var18 = map.entrySet().iterator(); var18.hasNext(); ++num) {
                Entry<String, String> entry = (Entry) var18.next();
                fieldNames[num] = (String) entry.getKey();
                values[num] = entry.getValue();
            }

            doc.getMailMerge().execute(fieldNames, values);
            InputStream qrCode = QrcodeUtil.getQrCode("http://www.gsxt.gov.cn/index.html?uniscid=" + creditcode, 130,
                    130);

            DocumentBuilder build = new DocumentBuilder(doc);
            build.moveToBookmark("EWM");
            build.insertImage(qrCode);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, 10);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = (long) inputStream.available();
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName("营业执照正本.doc");
            frameAttachInfo.setCliengTag("营业执照打印");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("application/msword");
            frameAttachInfo.setAttachLength(size);
            String attachguid = attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid();

            String fbdoctem = "";

            fbdoctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/jiningzwfw/individuation/"+overall+"/equipmentdisplay/selfservicemachine/yyzz/gryyzzfb.docx";

            Document fbdoc = new Document(fbdoctem);
         
            InputStream fbqrCode = QrcodeUtil.getQrCode("http://www.gsxt.gov.cn/index.html?uniscid=" + creditcode, 130,
                    130);

            DocumentBuilder fbbuild = new DocumentBuilder(fbdoc);
            fbbuild.moveToBookmark("ewm");
            fbbuild.insertImage(fbqrCode);
            fbdoc.getMailMerge().execute(fieldNames, values);

            ByteArrayOutputStream fboutputStream = new ByteArrayOutputStream();
            fbdoc.save(fboutputStream, 10);
            ByteArrayInputStream fbinputStream = new ByteArrayInputStream(fboutputStream.toByteArray());
            long fbsize = (long) fbinputStream.available();
            FrameAttachInfo fbframeAttachInfo = new FrameAttachInfo();
            fbframeAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            fbframeAttachInfo.setAttachFileName("营业执照副本.doc");
            fbframeAttachInfo.setCliengTag("营业执照打印副本");
            fbframeAttachInfo.setUploadUserGuid("");
            fbframeAttachInfo.setUploadUserDisplayName("");
            fbframeAttachInfo.setUploadDateTime(new Date());
            fbframeAttachInfo.setContentType("application/msword");
            fbframeAttachInfo.setAttachLength(fbsize);
            String fbattachguid = attachservice.addAttach(fbframeAttachInfo, fbinputStream).getAttachGuid();

            Yyzzprint record = service.getPrint(reason);
            if(record!=null){
                String fontattachguid="";
                String faceattachguid= "";
                if(StringUtil.isNotBlank(fontimg)){
                  fontattachguid= getimgattachguid( fontimg, creditcode+"font");
                }
                if(StringUtil.isNotBlank(faceimg)){
                    faceattachguid= getimgattachguid(faceimg, creditcode+"face");
                  }
                record.set("fontattachguid",fontattachguid);
                record.set("faceattachguid",faceattachguid);
                record.setCreditcode(creditcode);
                record.setOperatedate(new Date());
                record.setCompanytype(companytype);
                record.setJyfw(jyfw);
                record.setDwmc(dwmc);
                record.setLegalman(legalman);
                record.setMachineno(machineno);
                record.setAttacguid(attachguid);
                record.setFbattacguid(fbattachguid);
                service.update(record); 
            }
            dataJson.put("zbattachguid", attachguid);
            dataJson.put("fbattachguid", fbattachguid);
            outputStream.close();
            inputStream.close();
            log.info("yyzzprint/setgrprintrecord结束"+new Date());
            return JsonUtils.zwdtRestReturn("1", "接口调用成功！", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口调用异常：" + e.getMessage(), "");
        }

    }
    public static String toChinese(String str) {
        if (StringUtil.isBlank(str) || !str.matches("(-)?[\\d]*(.)?[\\d]*")) {
            //system.out.println("抱歉，请输入数字！");
            return str;
        }
        if ("0".equals(str) || "0.00".equals(str) || "0.0".equals(str)) {
            return "零元";
        }

        boolean flag = false;
        if (str.startsWith("-")) {
            flag = true;
            str = str.replaceAll("-", "");
        }
        str = str.replaceAll(",", "");
        String integerStr;
        String decimalStr;
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."));
            decimalStr = str.substring(str.indexOf(".") + 1);
        }
        else if (str.indexOf(".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        }
        else {
            integerStr = str;
            decimalStr = "";
        }
        if (integerStr.length() > IUNIT.length) {
            //system.out.println(str + "：超出计算能力");
            return str;
        }
        int[] integers = toIntArray(integerStr);
        if (integers.length > 1 && integers[0] == 0) {
            //system.out.println("抱歉，请输入数字！");
            if (flag) {
                str = "-" + str;
            }
            return str;
        }
        boolean isWan = isWan5(integerStr);
        int[] decimals = toIntArray(decimalStr);
        String result = getChineseInteger(integers, isWan) + getChineseDecimal(decimals);
        if (flag) {
            return "负" + result;
        }
        else {
            return result;
        }
    }

    private static int[] toIntArray(String number) {
        int[] array = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }

    public static String getChineseInteger(int[] integers, boolean isWan) {
        StringBuffer chineseInteger = new StringBuffer("");
        int length = integers.length;
        if (length == 1 && integers[0] == 0) {
            return "";
        }
        for (int i = 0; i < length; i++) {
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13)
                    key = IUNIT[4];
                else if ((length - i) == 9) {
                    key = IUNIT[8];
                }
                else if ((length - i) == 5 && isWan) {
                    key = IUNIT[4];
                }
                else if ((length - i) == 1) {
                    key = IUNIT[0];
                }
                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += NUMBERS[0];
                }
            }
            chineseInteger.append(integers[i] == 0 ? key : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
        }
        return chineseInteger.toString();
    }

    private static String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");
        for (int i = 0; i < decimals.length; i++) {
            if (i == 3) {
                break;
            }
            chineseDecimal.append(decimals[i] == 0 ? "" : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return chineseDecimal.toString();
    }

    private static boolean isWan5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            }
            else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        }
        else {
            return false;
        }
    }
    public String getimgattachguid(String img,String name){
        
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Util.decodeBuffer(img));
        FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
        long size = (long) inputStream.available();
        frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
        frameAttachInfo.setAttachFileName(name+".png");
        frameAttachInfo.setCliengTag("事项打印");
        frameAttachInfo.setUploadUserGuid("");
        frameAttachInfo.setUploadUserDisplayName("");
        frameAttachInfo.setUploadDateTime(new Date());
        frameAttachInfo.setContentType("png");
        frameAttachInfo.setAttachLength(size);
       return attachservice.addAttach(frameAttachInfo,inputStream ).getAttachGuid();  
    }
    /*  public static void main(String[] args) {
        String number = "12.56";
        //system.out.println(number + ": " + toChinese(number));
        number = "1234567890563886.123";
        //system.out.println(number + ": " + toChinese(number));
        number = "1600";
        //system.out.println(number + ": " + toChinese(number));
        number = "156,0";
        //system.out.println(number + ": " + toChinese(number));
        number = "-156,0";
        //system.out.println(number + ": " + toChinese(number));
        number = "0.12";
        //system.out.println(number + ": " + toChinese(number));
        number = "0.0";
        //system.out.println(number + ": " + toChinese(number));
        number = "01.12";
        //system.out.println(number + ": " + toChinese(number));
        number = "0125";
        //system.out.println(number + ": " + toChinese(number));
        number = "-0125";
        //system.out.println(number + ": " + toChinese(number));
        number = "sdw5655";
        //system.out.println(number + ": " + toChinese(number));
        //system.out.println(null + ": " + toChinese(null));
    }*/
}
