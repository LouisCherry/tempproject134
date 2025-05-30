package com.epoint.sms.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import org.quartz.Job;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;

import com.epoint.frame.service.message.entity.MessagesCenter;
public class SmsSendAction implements Job{
    transient Logger log = LogUtil.getLog(SmsSendAction.class);
    private SmsSendService smsService = new SmsSendService();
    String Login_Result = "";
    String Sent_Result = "";
    String Url = "http://mas.ecloud.10086.cn/app/http/authorize?";
    String sendUrl = "http://mas.ecloud.10086.cn/app/http/sendSms?";
    String Ec_name = "济宁市政务服务中心管理办公室";//
    String SDK_name = "jnzwfw";//
    String SDK_pws = "Jn@123456";//
    String mas_user_id="";
    String access_token="";
    private String sign = "njD95FoVl";
    //private String sign = "DbzolnC6";
    private String mobiles = "";
    private String content = "";
    private String addSerial = "";
    
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	 try {
             EpointFrameDsManager.begin(null);
             Ec_name = java.net.URLEncoder.encode(Ec_name, "utf-8");
             String Param = "ec_name=" + Ec_name + "&user_name=" + SDK_name
                     + "&user_passwd=" + SDK_pws;
             
             log.info("+++++++++++开始发送短信："
                     + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT)
                     + "+++++++++++");
             List<MessagesCenter> list = smsService.selectMessageCenters();
             System.out.println("待发送：" + list.size());
             if (list != null && list.size() > 0) {
                 log.info(">>>>开始登陆认证："
                         + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT)
                         + ">>>>>>");
                 //接口一：登陆验证 如果有待发送短信的时候进行登录/一次100条
                 Login_Result = sendPost(Url, Param);
                 JSONObject obj = JSONObject.parseObject(Login_Result);
                 mas_user_id =obj.getString("mas_user_id");
                 access_token = obj.getString("access_token");
                 //获取待发送短信
                 for (MessagesCenter message : list) {
                     mobiles = message.getMessageTarget();
                     content = message.getContent();
                     String mac = mas_user_id + mobiles + content + sign + addSerial
                             + access_token;
                     System.out.println("mac：" + mac);
                     String selfMac = encryptToMD5(mac);
                     String param = "mas_user_id=" + mas_user_id + "&mobiles=" + mobiles
                             + "&" + "content=" + content + "&sign=" + sign + "&serial="
                             + addSerial + "&mac=" + selfMac;
                     String sendResult = sendPost(sendUrl, param);
                     System.out.println("====sendResult===="+sendResult);
                     JSONObject retobj = JSONObject.parseObject(sendResult);
                     if("SC:0000".equals(retobj.getString("RET-CODE"))){
                         log.info(">>>Success>>>发送成功："
                                 + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT)
                                 + ">>>Success>>>");
                         smsService.handleEpointMessage(message, 1);
                     }else{
                         log.info(">>>Failed>>>发送失败："
                                 + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT)
                                 + ">>>Failed>>>");
                     }
                 }
             }else{
                 log.info("===============结束发送短信：==没有需要发送的短信"
                         + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT)
                         + "===============");
             }
             EpointFrameDsManager.commit();
         }
         catch (Exception e) {
             e.printStackTrace();
             EpointFrameDsManager.rollback();
         }
         finally {
             EpointFrameDsManager.close();
         }
    }
    /**
     * API:发送post请求
     */
    public static String sendPost(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
            out.write(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * API:MD5转换
     */
    public static String encryptToMD5(String password) {
        byte[] digesta = null;
        String result = null;
        try {
            // 得到一个MD5的消息摘要
            MessageDigest mdi = MessageDigest.getInstance("MD5");
            // 添加要进行计算摘要的信息
            mdi.update(password.getBytes("utf-8"));
            // 得到该摘要
            digesta = mdi.digest();
            result = byteToHex(digesta);
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * API:进制转换(2 >>转> 16)
     */
    public static String byteToHex(byte[] pwd) {
        StringBuilder hs = new StringBuilder("");
        String temp = "";
        for (int i = 0; i < pwd.length; i++) {
            temp = Integer.toHexString(pwd[i] & 0XFF);
            if (temp.length() == 1) {
                hs.append("0").append(temp);
            } else {
                hs.append(temp);
            }
        }
        return hs.toString().toUpperCase();
    }
}
