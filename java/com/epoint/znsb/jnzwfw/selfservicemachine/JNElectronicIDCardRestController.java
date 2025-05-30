package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.itextpdf.text.pdf.PdfReader;

@RestController
@RequestMapping("/jnelectronicidcard")
public class JNElectronicIDCardRestController
{

    @Autowired
    private IHandleConfig handleConfigservice;
   /* private String privatekey = "641585FCDB9B5D71F3456AE9FC5A1FAA6E14D1647A46A037ECF6FCC48A2F9F77";
    private String accessId = "36c98bbb1d1e4a609559570a9c8744fa";
    private String accessToken = "4f48cdb7736b411a95455dd2d3fbda92";*/
    private String privatekey = "74C61A688930EEAF7EFF10FB300161C1AE1A08F029D89A4C42E578E87F8B05CB";
    private String accessId = "37423b1e08384a5a9c5fb35972c7e480";
    private String accessToken = "09c6e430a2834725bbbea82a95907218";
    
    //接口记录日志的注入信息
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAttachService attachservice;
    @Autowired
    private ICodeItemsService iCodeItemsService;


    @RequestMapping(value = "/electroniczz", method = RequestMethod.POST)
    public String electroniczz(@RequestBody String params) {

        String result = "";
        try {
            // 接口调用

            log.info("=======开始调用接口electroniczz=======" + new Date());
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
      
            String posturl = "http://59.206.31.167/archive/outcommonarchsearch/getEntArch.do";
            String postparams = obj.getString("postparams");
           
            HttpPost httppost = new HttpPost(posturl);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 设置10秒连接超时
            // httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
            // 请求头参数键值对
            String[] headerParamsString = null;
            // 输入参数键值对
            String[] paramsString = null;
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          

            if (StringUtil.isNotBlank(postparams)) {
                paramsString = postparams.split(";"); // 以";"分割获取的字符串
                for (String headerParam : paramsString) {
                    String key = headerParam.split("=")[0]; // 键 数组的第一位
                    String value = headerParam.split("=")[1]; // 值 数组的第二位
                    formparams.add( new BasicNameValuePair(key, value));
                }
            }

            UrlEncodedFormEntity uefEntity = null; // 键值对传入的参数
          
                uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
                httppost.setEntity(uefEntity);

                CloseableHttpResponse response = httpClient.execute(httppost);

                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    result = EntityUtils.toString(entity, "UTF-8"); // 写入返回值
                }
                response.close();
            
           
            return result;
               
        }
        catch (Exception e) {
            log.info("=======electroniczz接口参数：params【" + params + "】=======");
            log.info("=======electroniczz异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "electroniczz接口调用异常：" + e.getMessage(), "");
        }
    }
    @RequestMapping(value = "/getpdfcontent", method = RequestMethod.POST)
    public String getpdfcontent(@RequestBody String params) {

        String result = "";
        try {
            // 接口调用

            log.info("=======开始调用接口getpdfcontent=======" + new Date());
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String getpdfcontent = obj.getString("posturl");
            JSONObject datajson = new JSONObject();
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
            
            String certno =  System.currentTimeMillis()+ ".pdf";

            InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            long size = (long) inputStream.available();
            String attachguid = UUID.randomUUID().toString();
            frameAttachInfo.setAttachGuid(attachguid);
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName(certno );
            frameAttachInfo.setCliengTag("工商电子档案");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType(".pdf");
            frameAttachInfo.setAttachLength(size);
            attachservice.addAttach(frameAttachInfo, stream1);
            PdfReader pdfReader = new PdfReader(getpdfcontent);
            int pages = pdfReader.getNumberOfPages();
            datajson.put("pdfcontent", attachguid);
            datajson.put("pages", pages);
            return JsonUtils.zwdtRestReturn("1", "getpdfcontent接口调用成功", datajson);
               
        }
        catch (Exception e) {
            log.info("=======getpdfcontent接口参数：params【" + params + "】=======");
            log.info("=======getpdfcontent异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getpdfcontent接口调用异常：" + e.getMessage(), "");
        }
    }
}
