package com.epoint.xmz.job.DJWebService;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient {
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    public static String doHttp(String token,String date) throws Exception {

            //第一步：创建服务地址，不是WSDL地址
            URL url = new URL("http://221.214.94.40:81/ShangDongJSDataService/ShanDongJSDataReleasing.asmx");
            //2：打开到服务地址的一个连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //3：设置连接参数
            //3.1设置发送方式：POST必须大写
            connection.setRequestMethod("POST");
            //3.2设置数据格式：Content-type
            connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
            //3.3设置输入输出，新创建的connection默认是没有读写权限的，
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //4：组织SOAP协议数据，发送给服务端
            String soapXML = getXML(token,date);
            OutputStream os = connection.getOutputStream();
            os.write(soapXML.getBytes());

            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String temp = null;
        try {
            //5：接收服务端的响应
            int responseCode = connection.getResponseCode();
            if (200 == responseCode) {//表示服务端响应成功
                while (null != (temp = br.readLine())) {
                    sb.append(temp);
                }
                log.info(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
            isr.close();
            br.close();
            os.close();
            return sb.toString();
        }

    }

    /**
     * 请求数据接口的报文
     * @param token token
     * @param Date 查询的日期
     * @return 返回拼接好的请求报文
     */
    public static String getXML(String token,String Date){
        String soapXML ="<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<GetJsonCorpRegisterInfoListByTimeStamp xmlns=\"http://tempuri.org/\">" +
                "<Token>" + token + "</Token>" +
                "<TimeStamp>" + Date + "</TimeStamp>" +
                "<AreaCode></AreaCode>" +
                "</GetJsonCorpRegisterInfoListByTimeStamp>" +
                "</soap:Body>" +
                "</soap:Envelope>";
        return soapXML;
    }
}
