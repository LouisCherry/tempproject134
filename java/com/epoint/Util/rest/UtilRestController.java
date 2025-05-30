package com.epoint.Util.rest;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang3.StringUtils;
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
import com.epoint.common.util.JsonUtils;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.zwdt.util.TARequestUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@RestController
@RequestMapping("/tazwdtcommon")
public class UtilRestController
{

    /**
     * 网上用户注册API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    private static String accessAdress = ConfigUtil.getConfigValue("taconfig", "access");

    private static String intermediaryAdress = ConfigUtil.getConfigValue("taconfig", "intermediary");

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ICertInfo iCertInfo;

    @RequestMapping(value = "/getdata", method = RequestMethod.POST)
    public String getData(@RequestBody String params) {
        try {
            log.info("=======开始通用接口=======" + new Date());
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String resturl = obj.getString("resturl");

            String urlconf = resturl;
            StringBuffer url = new StringBuffer(urlconf);

            JSONObject backjsondata = getJSONDataBack(url, params);

            log.info("=======结束调用通用接口=======");
            return backjsondata.toString();
        }
        catch (Exception e) {
            log.info("=======调用获取详情接口参数：params【" + params + "】=======");
            log.info("=======调用获取详情异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "调用获取详情接口调用异常：" + e.getMessage(), "");
        }
    }

    public JSONObject getJSONDataBack(StringBuffer url, String json) throws HttpException, IOException {
        JSONObject backjsondata = JSONObject
                .parseObject(com.epoint.core.utils.httpclient.HttpClientUtil.postBody(url.toString(), json).toString());
        return backjsondata;
    }

    /**
     * 
     * @Description: 工具方法获取内网数据返回
     * @author male   
     * @date 2019年3月13日 下午2:42:11
     * @return String    返回类型    
     * @throws
     */
    @RequestMapping(value = "/getinnerdata", method = RequestMethod.POST)
    public String getDataToInner(@RequestBody String params, @Context HttpServletRequest request) {

        log.info("=======调用getinnerdata接口=======");
        JSONObject jsonObject = JSONObject.parseObject(params);
        JSONObject obj = (JSONObject) jsonObject.get("params");

        //原封数据
        JSONObject requestParam = obj.getJSONObject("requestparam");
        String rowguid = requestParam.getString("rowguid");
        if(requestParam!=null && StringUtils.isNotBlank(rowguid)){
            String keyvalue = requestParam.getString("keyvalue");
            if(StringUtils.isBlank(keyvalue)){
                CertInfo certInfo = iCertInfo.getCertInfoByRowguid(rowguid);
                if(certInfo!=null){
                    keyvalue = certInfo.getCertno();
                    requestParam.put("keyvalue",keyvalue);
                }
            }
        }
        String requestUrl = obj.getString("requesturl");
        String token = jsonObject.getString("token");
        String result = null;
        //拼接入参
        JSONObject submit = new JSONObject();
        submit.put("token", token);
        submit.put("params", requestParam);
        //存入用户信息
        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
        log.info("=======auditOnlineRegister=======" + auditOnlineRegister);
        if (auditOnlineRegister != null) {
            submit.put("auditonlineregister", JSONObject.toJSONString(auditOnlineRegister));
        }
        //存入请求url

        String authorization = "";
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(request);
        if (oAuthCheckTokenInfo != null) {
            authorization = request.getHeader("authorization");//获取Authorization请求头对应的值
            log.info("=======authorization=======" + authorization);
        }

        submit.put("qurl", WebUtil.getRequestCompleteUrl(request));
        log.info("=======submit=======" + submit.toJSONString());

        if (StringUtil.isNotBlank(requestUrl)) {
            result = TARequestUtil.sendPostInner(requestUrl, submit.toJSONString(), "", authorization);
        }
        log.info("=======返回结果：" + result + "=======");

        return result;
    }

    /**
     * 
     * @Description: 宁阳微信访问内网数据接口
     * @author male   
     * @date 2019年10月29日 下午4:33:06
     * @return String    返回类型    
     * @throws
     */
    @RequestMapping(value = "/getnywechatdata", method = RequestMethod.POST)
    public String getNyWeChatDataToInner(@RequestBody String params, @Context HttpServletRequest request) {

        log.info("=======调用getinnerdata接口=======");
        JSONObject jsonObject = JSONObject.parseObject(params);
        JSONObject obj = (JSONObject) jsonObject.get("params");

        //原封数据
        JSONObject requestParam = obj.getJSONObject("requestparam");
        String requestUrl = obj.getString("requesturl");
        String token = jsonObject.getString("token");
        String result = null;
        //拼接入参
        JSONObject submit = new JSONObject();
        submit.put("token", token);
        submit.put("params", requestParam);
        //存入用户信息
        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
        log.info("=======auditOnlineRegister=======" + auditOnlineRegister);
        if (auditOnlineRegister != null) {
            submit.put("auditonlineregister", JSONObject.toJSONString(auditOnlineRegister));
        }
        //存入请求url

        String authorization = "";
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(request);
        if (oAuthCheckTokenInfo != null) {
            authorization = request.getHeader("authorization");//获取Authorization请求头对应的值
            log.info("=======authorization=======" + authorization);
        }

        submit.put("qurl", WebUtil.getRequestCompleteUrl(request));
        //log.info("=======submit=======" + submit.toJSONString());

        if (StringUtil.isNotBlank(requestUrl)) {
            result = TARequestUtil.sendPostInner(requestUrl, submit.toJSONString(), "", authorization);
        }
        //log.info("=======返回结果：" + result + "=======");

        return result;
    }

    /**
     * 
     * @Description: 获取页面头部，脚部
     * @author male   
     * @date 2019年3月15日 下午2:56:54
     * @return String    返回类型    
     * @throws
     */
    @RequestMapping(value = "/getwebtopandfoot", method = RequestMethod.POST)
    public String getWebTopAndFoot(@RequestBody String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String areacode = jsonObject.getString("areacode");
        //String taskguid = jsonObject.getString("taskguid");

        JSONObject result = new JSONObject();

        if (StringUtil.isNotBlank(areacode)) {
            areacode = areacode.substring(0, 8);

            if (areacode.indexOf("370900") != -1) {
                //泰安
                result.put("top", "http://tazwfw.sd.gov.cn/col/col1601/index.html");
                result.put("foot", "http://tazwfw.sd.gov.cn/col/col1602/index.html");
            }
            else if (areacode.indexOf("370982") != -1) {
                //新泰
                result.put("top", "http://taxtzwfw.sd.gov.cn/col/col7949/index.html");
                result.put("foot", "http://taxtzwfw.sd.gov.cn/col/col7950/index.html");
            }
            else if (areacode.indexOf("370990") != -1) {
                //高新
                result.put("top", "http://tagxzwfw.sd.gov.cn/col/col48903/index.html");
                result.put("foot", "http://tagxzwfw.sd.gov.cn/col/col48936/index.html");
            }
            else if (areacode.indexOf("370902") != -1) {
                //泰山
                result.put("top", "http://tatszwfw.sd.gov.cn/col/col3029/index.html");
                result.put("foot", "http://tatszwfw.sd.gov.cn/col/col3030/index.html");
            }
            else if (areacode.indexOf("370911") != -1) {
                //岱岳
                result.put("top", "http://tadyzwfw.sd.gov.cn/col/col4259/index.html");
                result.put("foot", "http://tadyzwfw.sd.gov.cn/col/col4260/index.html");
            }
            else if (areacode.indexOf("370983") != -1) {
                //肥城
                result.put("top", "http://tafczwfw.sd.gov.cn/col/col9179/index.html");
                result.put("foot", "http://tafczwfw.sd.gov.cn/col/col9180/index.html");
            }
            else if (areacode.indexOf("370921") != -1) {
                //宁阳
                result.put("top", "http://tanyzwfw.sd.gov.cn/col/col5489/index.html");
                result.put("foot", "http://tanyzwfw.sd.gov.cn/col/col5490/index.html");
            }
            else if (areacode.indexOf("370923") != -1) {
                //东平
                result.put("top", "http://tadpzwfw.sd.gov.cn/col/col6719/index.html");
                result.put("foot", "http://tadpzwfw.sd.gov.cn/col/col6720/index.html");
            }
        }

        return result.toString();
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
        log.info("=======oAuthCheckTokenInfo=======" + oAuthCheckTokenInfo);
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

    /**
     * 
     * @Description: 绘制办件二维码  
     * @author male   
     * @date 2019年3月20日 下午3:58:57
     * @return void    返回类型    
     * @throws
     */
    @RequestMapping(value = "/getqrcode", method = RequestMethod.GET)
    public void getQRcode(HttpServletResponse response, HttpServletRequest request) {

        //获取projectguid
        String projectGuid = request.getParameter("projectguid");
        //内容
        String content = "http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/eventdetail/publicitydetail?projectguid=" + projectGuid;
        //System.out.println(content);
        //大小
        int qrCodeSize = 300;
        //格式
        String imageFormat = "JPEG";

        try {
            //设置二维码纠错级别ＭＡＰ
            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<EncodeHintType, Object>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q); // 矫错级别  
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");//编码
            hintMap.put(EncodeHintType.MARGIN, 0);//白边

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            //创建比特矩阵(位矩阵)的QR码编码的字符串  
            BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);

            MatrixToImageWriter.writeToStream(byteMatrix, imageFormat, response.getOutputStream());

        }
        catch (WriterException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Description: 工具方法获取内网数据返回
     * @author male   
     * @date 2019年3月13日 下午2:42:11
     * @return String    返回类型    
     * @throws
     */
    @RequestMapping(value = "/getinnerdataget", method = RequestMethod.POST)
    public String getDataToInnerGet(@RequestBody String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        JSONObject obj = (JSONObject) jsonObject.get("params");

        //原封数据
        String projectguid = obj.getString("projectguid");
        String requestUrl = intermediaryAdress + "rest/innerattach/getprojectinfo";
        String result = "";
        //拼接入参
        JSONObject submit = new JSONObject();
        submit.put("projectguid", projectguid);
        if (StringUtil.isNotBlank(projectguid)) {
            //存入请求url
            log.info("=======getinnerdataget请求接口地址：" + requestUrl + "=======");

            if (StringUtil.isNotBlank(requestUrl)) {
                result = TARequestUtil.sendPost(requestUrl, submit.toJSONString(), "", "");
            }
            log.info("=======getinnerdataget返回结果：" + result + "=======");
        }

        return result;
    }

}
