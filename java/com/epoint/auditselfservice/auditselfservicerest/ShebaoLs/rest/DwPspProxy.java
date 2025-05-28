package com.epoint.auditselfservice.auditselfservicerest.ShebaoLs.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class DwPspProxy
{

    private Logger logger = Logger.getLogger(this.getClass());
    private static DwPspProxy dwpspproxy = null;
    private String dwpspserverurl = null;

    private DwPspProxy() {
    }

    public static DwPspProxy getInstance() {
        if (dwpspproxy == null) {
            dwpspproxy = new DwPspProxy();
        }
        return dwpspproxy;
    }

    public void setDwPspServerUrl(String dwpspserverurl) {
        this.dwpspserverurl = dwpspserverurl;
    }

    public String getDwPspServerUrl() {
        return this.dwpspserverurl;
    }

    public String invokeService(String licenseKey, String serviceName, String operationName, String xmlPara) {

        String result = ""; // 返回值
        CloseableHttpClient httpclient = null; // 客户端
        HttpPost httppost = new HttpPost(dwpspserverurl + "/dwpspservlet/uddi/serviceProxy"); // 发送请求
        CloseableHttpResponse response = null; // 返回请求
        HttpEntity entity = null; // 返回实体

        try {
            httpclient = HttpClients.createDefault(); // 创建客户端

            // 组织服务入参：包括serviceName、operationName、xmlPara
            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            if (licenseKey != null && !"".equals(licenseKey)) {
                nvps.add(new BasicNameValuePair("licenseKey", licenseKey));
            }
            if (serviceName != null && !"".equals(serviceName)) {
                nvps.add(new BasicNameValuePair("serviceName", serviceName));
                if (operationName != null && !"".equals(operationName)) {
                    nvps.add(new BasicNameValuePair("operationName", operationName));
                }
                if (xmlPara != null && !"".equals(xmlPara)) {
                    nvps.add(new BasicNameValuePair("xmlPara", xmlPara));
                }
            }

            // 设置参数到请求对象中
            httppost.setEntity(new UrlEncodedFormEntity(nvps, "GBK"));

            // 设置请求的超时时间
            RequestConfig rconfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
            httppost.setConfig(rconfig);

            // 执行请求操作，并拿到结果（同步阻塞）
            response = httpclient.execute(httppost);
            // 获取返回结果
            entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                result = EntityUtils.toString(entity, "GBK");
            }
        }
        catch (Exception e) {
            logger.error("代理调用服务, 地址为：" + dwpspserverurl + ", 提示信息：" + e);
        }
        finally {
            try {
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
            }
            catch (Exception e2) {
                logger.error("代理调用服务，释放entity连接时出现异常。", e2);
            }
            try {
                if (response != null) {
                    response.close();
                }
            }
            catch (Exception e2) {
                logger.error("代理调用服务，释放response连接时出现异常。", e2);
            }
            try {
                if (httppost != null) {
                    httppost.releaseConnection();
                }
            }
            catch (Exception e2) {
                logger.error("代理调用服务，释放httppost连接时出现异常。", e2);
            }
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            }
            catch (Exception e2) {
                logger.error("代理调用服务，释放httpclient连接时出现异常。", e2);
            }
        }

        return result;
    }

    public static void main(String[] arg) {
        DwPspProxy.getInstance().setDwPspServerUrl("http://10.1.91.245:8080/dwpspserver");
        String licenseKey = "3f8a85b7-551d-4922-9482-eaf7120f0885";
        String serviceName = "ScmsCardService";
        String operationName = "scCardLsgs";
        String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p><s sfzhm=\"320882199703311619\"/><s xtlb=\"11\"/><s jbr=\"政务平台\"/></p>";
        String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

    }
}
