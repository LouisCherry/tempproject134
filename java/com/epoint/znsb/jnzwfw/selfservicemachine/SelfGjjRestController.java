package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.core.utils.code.Base64Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController
@RequestMapping(value = "/selfgjj")
public class SelfGjjRestController extends AuditCommonService
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

    public static final String headparams = "centerId=00053700&userId=jAA1lMb49zdDw97EU4EiNhwadXsMSf8NQftgB/g5jok=&userIdType=xc/ezttM2HaU/v1HSTwLLg==&deviceType=3&deviceToken=&currenVersion=1.5.1&buzType=";
    public static final String endparams = "&channel=13&appid=jAA1lMb49zdDw97EU4EiNhwadXsMSf8NQftgB/g5jok=&appkey=UcXAn9EFJfDMhukIlEoksRTeFxapxI2Neqvv4KnoKxIpWRWgb06DFjoDjtQZLP12&appToken=&clientIp=192.168.137.72&money=&original=&encryption=0&transVersion=V1.0&";

    /*
     * public static final String headparams =
     * "centerId=00053700&userId=emRPTukR//R4vxvmneCz5poxAmyuv2RDurze54KvyuU=&userIdType=ZZupNJc4RivThX4zculMlg==&deviceType=3&deviceToken=&currenVersion=1.5.1&buzType=";
     * public static final String endparams =
     * "&channel=13&appid=emRPTukR//R4vxvmneCz5poxAmyuv2RDurze54KvyuU=&appkey=J6lbvexj2nCfR/26qMbn9ZtExfpkqOi2S/eHO147gZjaZtRXlkm0Jupf0JN7Pzpb&appToken=&clientIp=192.168.137.72&money=&original=&encryption=0&transVersion=V1.0&";
     * public static final String headparams2 =
     * "centerId=stdAJWSEPiUvvnmV0Sk39g==&userId=0Gi7dEPHdCqYdJ2xViCD0X1gyLXgIkw28YAGJq/3hz8=&userIdType=+y66y8CLQCj6+IJwWBFuow==&deviceType=3&deviceToken=&currenVersion=1.5.1&buzType=";
     */
    /**
     * 1、公积金登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String certnum = obj.getString("certnum");
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/appapi50006.json";
            String buzType = "5432";
            System.out.println(httpurl);
            String parm = headparams + buzType + endparams
                    + "ybmapMessage={\"transchannel\":\"13\",\"flag\":\"4\",\"pwd\":\"\",\"accnum\":\"\",\"certinum\":\""
                    + certnum + "\"}";
            // System.out.println("1、公积金登录：" + parm);

            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);
            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
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

            String accnum = obj.getString("accnum");
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi40215.json";
            String buzType = "5007";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"accnum\":\""
                    + accnum + "\"}";
            System.out.println(parm);

            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);
            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
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
            // System.out.println("3、个人公积金明细查询:" + parm);

            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);

            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson);

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
            // System.out.println("4、贷款基本信息查询:" + parm);
            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);

            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson);

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

            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);

            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }


    /**
     * 5、贷款还款明细查询
     */
    @RequestMapping(value = "/getnewdaikuanhuankuanmingxilist", method = RequestMethod.POST)
    public String getNewDaiKuanHuanKuanMingXiList(@RequestBody String params, @Context HttpServletRequest request) {
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
            JSONObject backJson = RSASignature.httpURLConnectionPOST(httpurl, parm);
            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson.getJSONArray("result"));

            return JsonUtils.zwdtRestReturn("1", backJson.getString("msg"), dataJson);
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
            System.out.println("6、贷款还款计划查询:" + parm);
            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);

            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson);

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

            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);

            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }


            dataJson.put("content", backJson);

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

            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);

            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson);

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

            JSONObject backJson =  RSASignature.httpURLConnectionPOST(httpurl, parm);

            if(!"000000".equals(backJson.getString("recode"))){
                return JsonUtils.zwdtRestReturn("0", backJson.getString("msg"), "");
            }

            dataJson.put("content", backJson);

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
                doctem = downconfig + "/jnzwdt/individuation/overall/jieqingzm.docx";
            }
            else {
                doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/jnzwdt/individuation/overall/jieqingzm.docx";
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
            String base64 = Base64Util.encode(outputStream.toByteArray());

            base64 = WordWatermark.changeWordWatermark(base64,
                    "提取部门：济宁市行政审批服务局 提取时间：" + EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日HH时mm分"));
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Util.decodeBuffer(base64));
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
                doctem = downconfig + "/jnzwdt/individuation/overall/yididaikuanzm.docx";
            }
            else {
                doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/jnzwdt/individuation/overall/yididaikuanzm.docx";
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
            // ByteArrayInputStream inputStream = new
            // ByteArrayInputStream(outputStream.toByteArray());

            // 将inputStream转为Base64编码的字符串

            String base64 = Base64Util.encode(outputStream.toByteArray());

            base64 = WordWatermark.changeWordWatermark(base64,
                    "提取部门：济宁市行政审批服务局 提取时间：" + EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日HH时mm分"));
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Util.decodeBuffer(base64));

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

    /**
     * 12.职工封存托管满半年公积金提取
     */
    @RequestMapping(value = "/getFengCunTiQu", method = RequestMethod.POST)
    public String getFengCunTiQu(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String grzh = obj.getString("grzh");
            String grckzhkhyhdm = obj.getString("grckzhkhyhdm");
            String grckzhhm = obj.getString("grckzhhm");
            String money = obj.getString("intamt");

            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");
            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/businessquotai/appapi11014.json";
            String buzType = "5395";
            // 需传递money的加密和流水号
            String headpara = "centerId,userId,userIdType,deviceType,deviceToken,currenVersion,buzType,channel,appid,appkey,appToken,clientIp,money,original,encryption,transVersion,channelSeq,ybmapMessage";
            // endparams分割
            String[] backstring = endparams.split("&money=");
            StringBuffer buffer = new StringBuffer();
            buffer.append(headparams);
            buffer.append(buzType);
            buffer.append(backstring[0]);
            buffer.append("&money=");
            AesTest aes = new AesTest();
            buffer.append(aes.encrypt(money.getBytes("UTF-8")));
            buffer.append("&original=&encryption=0&transVersion=V1.0&");
            SimpleDateFormat format = new SimpleDateFormat("ddHHmmss");
            buffer.append("channelSeq=" + format.format(new Date()));
            buffer.append("&");
            String parm = buffer.toString() + "ybmapMessage={\"transchannel\":\"13\",\"grzh\":\"" + grzh
                    + "\",\"grckzhkhyhdm\":\"" + grckzhkhyhdm + "\",\"grckzhhm\":\"" + grckzhhm + "\",\"intamt\":\""
                    + money + "\"}";

            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm, headpara));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 13.销户利息
     */
    @RequestMapping(value = "/getCancellation", method = RequestMethod.POST)
    public String getCancellation(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String grzh = obj.getString("grzh");

            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi40217.json";
            String buzType = "5338";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"grzh\":\"" + grzh
                    + "\"}";

            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 13.离退休
     */
    @RequestMapping(value = "/getCancellationTiQu", method = RequestMethod.POST)
    public String getCancellationTiQu(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String grzh = obj.getString("grzh");
            String grckzhkhyhdm = obj.getString("grckzhkhyhdm");
            String grckzhhm = obj.getString("grckzhhm");
            String intamt = obj.getString("intamt");
            String term = obj.getString("term");
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi11001.json";
            String buzType = "5365";

            // 需传递money的加密和流水号
            String headpara = "centerId,userId,userIdType,deviceType,deviceToken,currenVersion,buzType,channel,appid,appkey,appToken,clientIp,money,original,encryption,transVersion,channelSeq,ybmapMessage";
            // endparams分割
            String[] backstring = endparams.split("&money=");
            StringBuffer buffer = new StringBuffer();
            buffer.append(headparams);
            buffer.append(buzType);
            buffer.append(backstring[0]);
            buffer.append("&money=");
            AesTest aes = new AesTest();
            buffer.append(aes.encrypt(intamt.getBytes("UTF-8")));
            buffer.append("&original=&encryption=0&transVersion=V1.0&");
            SimpleDateFormat format = new SimpleDateFormat("HHmmss");
            buffer.append("channelSeq=" + format.format(new Date()));
            buffer.append("&");
            String parm = buffer.toString() + "ybmapMessage={\"transchannel\":\"13\",\"grzh\":\"" + grzh
                    + "\",\"grckzhkhyhdm\":\"" + grckzhkhyhdm + "\",\"grckzhhm\":\"" + grckzhhm + "\",\"intamt\":\""
                    + intamt + "\",\"term\":\"" + term + "\"}";
            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm, headpara));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 冲还贷款查询
     */
    @RequestMapping(value = "/LoanRepaymentInquiry", method = RequestMethod.POST)
    public String LoanRepaymentInquiry(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String accnum = obj.getString("accnum");
            String jkhtbh = obj.getString("jkhtbh");
            String jkrzjh = obj.getString("jkrzjh");
            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi60101.json";
            String buzType = "6001";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"accnum\":\""
                    + accnum + "\",\"jkhtbh\":\"" + jkhtbh + "\",\"jkrzjh\":\"" + jkrzjh + "\"}";
            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 冲还贷款办理
     */
    @RequestMapping(value = "/RepaymentOfLoan", method = RequestMethod.POST)
    public String RepaymentOfLoan(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String jkhtbh = obj.getString("jkhtbh");
            String jkrgjjzh = obj.getString("jkrgjjzh");
            String transfamt = obj.getString("transfamt");
            String logintype = obj.getString("logintype");
            String repaytype = obj.getString("repaytype");
            String prototype = obj.getString("prototype");

            JSONObject dataJson = new JSONObject();
            String GJJ_URL = configservice.getFrameConfigValue("GJJ_URL");

            String httpurl = "http://" + GJJ_URL + "/YBMAPZH-WEB/business/appapi60102.json";
            String buzType = "6002";
            String parm = headparams + buzType + endparams + "ybmapMessage={\"transchannel\":\"13\",\"jkhtbh\":\""
                    + jkhtbh + "\",\"jkrgjjzh\":\"" + jkrgjjzh + "\",\"transfamt\":\"" + transfamt
                    + "\",\"logintype\":\"" + logintype + "\",\"repaytype\":\"" + repaytype + "\",\"prototype\":\""
                    + prototype + "\"}";
            dataJson.put("content", RSASignature.httpURLConnectionPOST(httpurl, parm));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }


    /**
     * 冲还贷款查询
     */
    @RequestMapping(value = "/changeBase64", method = RequestMethod.POST)
    public String changeBase64(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");

            String filename = obj.getString("filename");
            JSONObject dataJson = new JSONObject();
            dataJson.put("printdpfhtml",convertToBase64("https://wx.gjj.cn/miapp/loadfile.file?fileName=" + filename));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }


    public   String convertToBase64(String pdfUrl) {
        byte[] data = null;
        try {

            URL url = new URL(pdfUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(5*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            data = readInputStream(inputStream);
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return Base64.encode(data);
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }



}
