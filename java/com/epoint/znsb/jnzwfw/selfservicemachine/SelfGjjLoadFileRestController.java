package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController
@RequestMapping(value = "/selfgjjloadfile")
public class SelfGjjLoadFileRestController
{
    @Autowired
    private IAttachService attachservice;
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @RequestMapping(value = "/getgjjbyfilename", method = RequestMethod.POST)
    public String getgjjbyfilename(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();

            String filename = obj.getString("filename");
            // 文件URL
            String fileUrl = "https://wx.gjj.cn/miapp/loadfile.file?fileName=" + filename;
            log.info("getgjjbyfilename fileUrl " + fileUrl);
            // 创建URL对象
            URL url = new URL(fileUrl);

            // 创建信任所有证书的 TrustManager
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };

            // 创建 SSLContext 并设置信任所有证书的 TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // 设置默认 SSLContext
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // 打开文件流
            InputStream inputStream = url.openStream();
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

            long size = byteArrayInputStream.available();

            // 附件信息
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName(filename);
            frameAttachInfo.setCliengTag("公积金贷款结清证明");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("application/msword");
            frameAttachInfo.setAttachLength(size);
            dataJson.put("attachguid", attachservice.addAttach(frameAttachInfo, byteArrayInputStream).getAttachGuid());

            inputStream.close();
            byteArrayInputStream.close();
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }
}
