package com.epoint.xmz.gjj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.znsb.jnzwfw.selfservicemachine.RSASignature;

@RestController
@RequestMapping(value = "/gjj")
public class GjjRestController extends AuditCommonService
{

    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IAttachService attachservice;
    @Autowired
    private IConfigService configservice;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String headparams = "centerId=00053700&userId=8T+DqtUWjOc0c7x42F76Kje2jtBHP6zSx1wCSGNwGeo=&userIdType=TjKAKeTTdQ+l88ZDwOkQgA==&deviceType=3&deviceToken=&currenVersion=1.5.1&buzType=";
    public static final String endparams = "&channel=18&appid=8T+DqtUWjOc0c7x42F76Kje2jtBHP6zSx1wCSGNwGeo=&appkey=B/oqcxgs55L9aZSowmvLKISEpnZKnYT5dEoYD747qYyWtQwxnOYJS1qGI6WxnOMQ&appToken=&clientIp=&money=&original=&encryption=0&transVersion=V1.0&";

    /**
     * 1、公积金登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            CustomServiceCenterMessage custom = new CustomServiceCenterMessage();
            String certnum = obj.getString("certnum");
            JSONObject dataJson = new JSONObject();

            String action = "appapi50006.json";
            String buzType = "5432";
            String userIdType = "02";
            String sendMsg = "{\"transchannel\":\"18\",\"flag\":\"4\",\"pwd\":\"\",\"accnum\":\"\",\"certinum\":\""+certnum+"\"}";
            String content = custom.transBody(sendMsg, certnum, userIdType, buzType, action);
            dataJson.put("content",  JSON.parseObject(content));
            
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 2、个人公积金账户信息查询
     */
    @RequestMapping(value = "/getaccountdetail", method = RequestMethod.POST)
    public String getAccountDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
        	JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            CustomServiceCenterMessage custom = new CustomServiceCenterMessage();
            String certnum = obj.getString("certnum");
            String accnum = obj.getString("accnum");
            String zjhm = obj.getString("zjhm");
            JSONObject dataJson = new JSONObject();

            String action = "business/appapi40215.json";
            String buzType = "5007";
            String userIdType = "02";
            String sendMsg = "ybmapMessage={\"transchannel\":\"18\",\"accnum\":\""+ accnum + "\",\"zjhm\":\""+zjhm+"\"}";
            String content = custom.transBody(sendMsg, certnum, userIdType, buzType, action);
            dataJson.put("content",  JSON.parseObject(content));
            
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 3、个人公积金明细查询
     */
    @RequestMapping(value = "/getgjjmingxichaxun", method = RequestMethod.POST)
    public String getGjjMingXiChaXun(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String begdate = obj.getString("begdate");
            String enddate = obj.getString("enddate");
            String accnum = obj.getString("accnum");
            String dwzh = obj.getString("dwzh");
            String ispaging = "0";
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi00102.json";
            String buzType = "5002";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"accnum\":\"" + accnum
                    + "\",\"begdate\":\"" + begdate + "\",\"enddate\":\"" + enddate
                    + "\",\"transchannel\":\"13\",\"dwzh\":\"" + dwzh + "\",\"ispaging\":\"" + ispaging
                    + "\",\"seqnum1\":0}";
            //System.out.println("3、个人公积金明细查询:" + parm);
            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 4、贷款基本信息查询
     */
    @RequestMapping(value = "/getdaikuaninfo", method = RequestMethod.POST)
    public String getDaiKuanInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String loancontrnum = obj.getString("loancontrnum");

            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi00201.json";
            String buzType = "5071";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"loancontrnum\":\""
                    + loancontrnum + "\"}";
            //System.out.println("4、贷款基本信息查询:" + parm);
            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 5、贷款还款明细查询
     */
    @RequestMapping(value = "/getdaikuanhuankuanmingxilist", method = RequestMethod.POST)
    public String getDaiKuanHuanKuanMingXiList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String loancontrnum = obj.getString("loancontrnum");
            String begdate = obj.getString("begdate");
            String enddate = obj.getString("enddate");
            String ispaging = "0";
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi00203.json";
            String buzType = "5077";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"loancontrnum\":\""
                    + loancontrnum + "\",\"begdate\":\"" + begdate + "\",\"enddate\":\"" + enddate
                    + "\",\"ispaging\":\"" + ispaging + "\",\"chgnum\":\"0\",\"seqnum\":0}";

            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 6、贷款还款计划查询
     */
    @RequestMapping(value = "/getdaikuanhuankuanjihualist", method = RequestMethod.POST)
    public String getDaiKuanHuanKuanJiHuaList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String loancontrnum = obj.getString("loancontrnum");

            String ispaging = "0";
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi00207.json";
            String buzType = "5073";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"loancontrnum\":\""
                    + loancontrnum + "\",\"ispaging\":\"" + ispaging + "\",\"seqnum\":\"0\"}";
            //System.out.println("6、贷款还款计划查询:" + parm);
            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 7、职工住房缴存证明
     */
    @RequestMapping(value = "/getzhufangjiaocunlist", method = RequestMethod.POST)
    public String getZhuFangJiaoCunList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String grzh = obj.getString("grzh");
            String begdate = obj.getString("begdate");
            String enddate = obj.getString("enddate");
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/businessCompany/appapi20203.json";
            String buzType = "5837";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"grzh\":\"" + grzh
                    + "\",\"begdate\":\"" + begdate + "\",\"enddate\":\"" + enddate + "\"}";

            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 8、异地贷款证明打印
     */
    @RequestMapping(value = "/getyididaikuanzhengming", method = RequestMethod.POST)
    public String getYiDiDaiKuanZhengMing(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String grzh = obj.getString("grzh");
            String zjhm = obj.getString("zjhm");
            String begdate = obj.getString("begdate");
            String enddate = obj.getString("enddate");

            String ispaging = "0";
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi20503.json";
            String buzType = "5498";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"grzh\":\"" + grzh
                    + "\",\"zjhm\":\"" + zjhm + "\",\"begdate\":\"" + begdate + "\",\"enddate\":\"" + enddate
                    + "\",\"ispaging\":\"" + ispaging + "\"}";

            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 9、贷款结清证明打印   
     */
    @RequestMapping(value = "/getdaikuanjieqing", method = RequestMethod.POST)
    public String getDaiKuaiJieQing(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String loancontrnum = obj.getString("loancontrnum");

            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/businessCompany/appapi20205.json";
            String buzType = "5863";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"jkhtbh\":\""
                    + loancontrnum + "\"}";

            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 10、贷款结清证明打印
     */
    @RequestMapping(value = "/jieqingzhengmingprint", method = RequestMethod.POST)
    public String jieqingzhengmingprint(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();

            String centerguid = obj.getString("centerguid");
            String bh = obj.getString("bh");

            String jkr = obj.getString("jkr");
            String sfz = obj.getString("sfz");
            String jkje = obj.getString("jkje");
            String wtyh = obj.getString("wtyh");

            String fkrq = obj.getString("fkrq");
            String dknx = obj.getString("dknx");
            String nll = obj.getString("nll");
            String dkdqr = obj.getString("dkdqr");

            String dbfs = obj.getString("dbfs");
            String dkye = obj.getString("dkye");
            String jqrq = obj.getString("jqrq");

            String dywdz = obj.getString("dywdz");
            String rq = obj.getString("rq");

            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            new License().setLicense(licenseName);
            String doctem = "";
            String downconfig = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid).getResult();
            if (StringUtil.isNotBlank(downconfig)) {
                doctem = downconfig
                        + "/jiningzwfw/individuation/overall/equipmentdisplay/selfservicemachine/jiningfw/jieqingzm.docx";
            }
            else {
                doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/jiningzwfw/individuation/overall/equipmentdisplay/selfservicemachine/jiningfw/jieqingzm.docx";
            }
            Document doc = new Document(doctem);
            String[] fieldNames = null;
            Object[] values = null;

            // 获取域与对应的值
            Map<String, String> map = new HashMap<String, String>(16);
            map.put("bh", bh);

            map.put("jkr", jkr);
            map.put("sfz", sfz);
            map.put("jkje", jkje);
            map.put("wtyh", wtyh);

            map.put("fkrq", fkrq);
            map.put("dknx", dknx);
            map.put("nll", nll);
            map.put("dkdqr", dkdqr);

            map.put("dbfs", dbfs);
            map.put("dkye", dkye);
            map.put("jqrq", jqrq);

            map.put("dywdz", dywdz);
            map.put("rq", rq);
            fieldNames = new String[map == null ? 0 : map.size()];
            values = new Object[map == null ? 0 : map.size()];
            int num = 0;

            for (Entry<String, String> entry : map.entrySet()) {
                fieldNames[num] = entry.getKey();
                values[num] = entry.getValue();
                num++;
            }
            // 替换域
            doc.getMailMerge().execute(fieldNames, values);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.DOC);// 保存成word
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = inputStream.available();

            // 附件信息
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName("公积金贷款结清证明.doc");
            frameAttachInfo.setCliengTag("公积金贷款结清证明");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("application/msword");
            frameAttachInfo.setAttachLength(size);
            dataJson.put("attachguid", attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

            outputStream.close();
            inputStream.close();
            dataJson.put("content", "");

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 11、异地贷款缴存证明打印
     */
    @RequestMapping(value = "/yididaikuanzhengmingprint", method = RequestMethod.POST)
    public String yididaikuanzhengmingprint(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();

            String centerguid = obj.getString("centerguid");

            String todwmc = obj.getString("todwmc");
            String zg = obj.getString("zg");

            String zgxm = obj.getString("zgxm");
            String sfz = obj.getString("sfz");

            String dwmc = obj.getString("dwmc");
            String grgjjzh = obj.getString("grgjjzh");

            String khsj = obj.getString("khsj");
            String zhzt = obj.getString("zhzt");

            String jcjs = obj.getString("jcjs");
            String dwjcbl = obj.getString("dwjcbl");
            String grjcbl = obj.getString("grjcbl");

            String yjce = obj.getString("yjce");
            String jcye = obj.getString("jcye");

            String zjlxjcsj = obj.getString("zjlxjcsj");

            String dkqk = obj.getString("dkqk");

            String dkcs = obj.getString("dkcs");
            String dkje = obj.getString("dkje");
            String rq = obj.getString("rq");

            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            new License().setLicense(licenseName);
            String doctem = "";
            String downconfig = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid).getResult();
            if (StringUtil.isNotBlank(downconfig)) {
                doctem = downconfig
                        + "/jiningzwfw/individuation/overall/equipmentdisplay/selfservicemachine/jiningfw/yididaikuanzm.docx";
            }
            else {
                doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/jiningzwfw/individuation/overall/equipmentdisplay/selfservicemachine/jiningfw/yididaikuanzm.docx";
            }
            Document doc = new Document(doctem);
            String[] fieldNames = null;
            Object[] values = null;

            // 获取域与对应的值
            Map<String, String> map = new HashMap<String, String>(16);

            map.put("todwmc", todwmc);
            map.put("zg", zg);
            map.put("zgxm", zgxm);
            map.put("sfz", sfz);

            map.put("dwmc", dwmc);
            map.put("grgjjzh", grgjjzh);
            map.put("khsj", khsj);
            map.put("zhzt", zhzt);

            map.put("jcjs", jcjs);
            map.put("dwjcbl", dwjcbl);
            map.put("grjcbl", grjcbl);

            map.put("yjce", yjce);
            map.put("jcye", jcye);

            map.put("zjlxjcsj", zjlxjcsj);
            map.put("dkqk", dkqk);
            map.put("dkcs", dkcs);
            map.put("dkje", dkje);
            map.put("rq", rq);
            fieldNames = new String[map == null ? 0 : map.size()];
            values = new Object[map == null ? 0 : map.size()];
            int num = 0;

            for (Entry<String, String> entry : map.entrySet()) {
                fieldNames[num] = entry.getKey();
                values[num] = entry.getValue();
                num++;
            }
            // 替换域
            doc.getMailMerge().execute(fieldNames, values);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.DOC);// 保存成word
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = inputStream.available();

            // 附件信息
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName("异地贷款缴存证明.doc");
            frameAttachInfo.setCliengTag("异地贷款缴存证明");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("application/msword");
            frameAttachInfo.setAttachLength(size);
            dataJson.put("attachguid", attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

            outputStream.close();
            inputStream.close();

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

}
