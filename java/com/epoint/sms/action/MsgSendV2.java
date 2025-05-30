package com.epoint.sms.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

public class MsgSendV2 implements Job {
    transient Logger log = LogUtil.getLog(MsgSendV2.class);

    private MsgSendService smsService = new MsgSendService();

    private String ecName = "济宁市行政审批服务局";
    private String apId = "jnzwfw";
    private String secretKey = "Jn@1234567";
    private String sign = "njD95FoVl";
    private String mobiles = "";
    private String content = "";
    private String addSerial = "";
    private String mac = "";
    //金乡短信签名
    private String signjx = "BTaKKG2BY";
    private String apIdjx = "jinxzwfw";
    private String secretKeyjx = "Jxsp123..";

    //经开区短信签名 370892
    private String jksign = "rQRl4Y27k";
    private String jkapId = "jkzwfw";
    private String jksecretKey = "jk123456";

    //泗水短信签名 370831
    private String sssign = "wgO3UPFvU";
    private String ssapId = "sszwfw";
    private String sssecretKey = "ss123456";

    //邹城为民服务中心 370883
    private String zcsign = "1XzFWAzbF";
    private String zcapId = "zczwpt";
    private String zcsecretKey = "Jn@123459";

    //嘉祥短信签名 370829
    private String jxsign = "8FtFDXXuf";

    //任城为民服务中心 370811
    private String rcsign = "rMkuI6rtb";

    //鱼台为民服务中心 370827
    private String ytsign = "alvU9H2wG";

    //兖州为民服务中心 370882
    private String yzsign = "QgycO6vyk";
    private String yzapId = "yzzwpt";
    private String yzsecretKey = "Jn@123457";

    //微山为民服务中心 370826
    private String wssign = "lyqM1MXwa";
    private String wsapId = "wszwpt";
    private String wssecretKey = "Jn@123458";

    //太白湖为民中心 370891
    private String tbhsign = "yoEaaNkAm";
    private String tbhapId = "bhzwpt";
    private String tbhsecretKey = "Jn@123410";

    //梁山为民服务中心 370832
    private String lssign = "THVJB8iWL";
    private String lsapId = "lsxdhttp";
    private String lssecretKey = "Gepoint@123";

    //泗水短信签名
    private String gxsign = "I3bcsaI1k";
    private String gxapId = "gxqzw";
    private String gxsecretKey = "Gx123456@";

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            log.info("+++++++++++开始发送短信："
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT)
                    + "+++++++++++");
            List<MessagesCenter> list = smsService.selectMessageCenters();
            EpointFrameDsManager.begin(null);
            if (list != null && list.size() > 0) {
                for (MessagesCenter smssend : list) {
                    //对频次的校验
                    if(checkLimit(smssend)){
                        mobiles = smssend.getMessageTarget();
                        content = smssend.getContent();
                        String type = smssend.getMessageType();


                        StringBuffer stringBuffer = new StringBuffer();
                        if ("370890".equals(type)) {
                            ecName = "济宁高新区行政审批服务局";
                        } else if ("370883".equals(type)) {
                            ecName = "邹城市行政审批服务局";
                        } else if ("370826".equals(type)) {
                            ecName = "微山县行政审批服务局";
                        } else if ("370882".equals(type)) {
                            ecName = "济宁市兖州区行政审批服务局";
                        } else if ("370891".equals(type)) {
                            ecName = "济宁北湖省级旅游度假区行政审批服务局";
                        } else if ("370828".equals(type)) {
                            ecName = "金乡县行政审批服务局";
                        } else if ("370832".equals(type)) {
                            ecName = "梁山县行政审批服务局";
                        }
                        else {
                            ecName = "济宁市行政审批服务局";
                        }

                        stringBuffer.append(ecName);

                        if ("370828".equals(type)) {
                            stringBuffer.append(apIdjx);
                            stringBuffer.append(secretKeyjx);
                        } else if ("370892".equals(type)) {
                            stringBuffer.append(jkapId);
                            stringBuffer.append(jksecretKey);
                        } else if ("370831".equals(type)) {
                            stringBuffer.append(ssapId);
                            stringBuffer.append(sssecretKey);
                        } else if ("370890".equals(type)) {
                            stringBuffer.append(gxapId);
                            stringBuffer.append(gxsecretKey);
                        } else if ("370883".equals(type)) {
                            stringBuffer.append(zcapId);
                            stringBuffer.append(zcsecretKey);
                        } else if ("370826".equals(type)) {
                            stringBuffer.append(wsapId);
                            stringBuffer.append(wssecretKey);
                        } else if ("370882".equals(type)) {
                            stringBuffer.append(yzapId);
                            stringBuffer.append(yzsecretKey);
                        } else if ("370891".equals(type)) {
                            stringBuffer.append(tbhapId);
                            stringBuffer.append(tbhsecretKey);
                        } else if ("370832".equals(type)) {
                            stringBuffer.append(lsapId);
                            stringBuffer.append(lssecretKey);
                        }
                        else {
                            stringBuffer.append(apId);
                            stringBuffer.append(secretKey);
                        }
                        stringBuffer.append(mobiles);
                        stringBuffer.append(content);
                        if ("370828".equals(type)) {
                            stringBuffer.append(signjx);
                        } else if ("370892".equals(type)) {
                            stringBuffer.append(jksign);
                        } else if ("370831".equals(type)) {
                            stringBuffer.append(sssign);
                        } else if ("370890".equals(type)) {
                            stringBuffer.append(gxsign);
                        } else if ("370883".equals(type)) {
                            stringBuffer.append(zcsign);
                        } else if ("370826".equals(type)) {
                            stringBuffer.append(wssign);
                        } else if ("370882".equals(type)) {
                            stringBuffer.append(yzsign);
                        } else if ("370891".equals(type)) {
                            stringBuffer.append(tbhsign);
                        } else if ("370829".equals(type)) {
                            stringBuffer.append(jxsign);
                        } else if ("370811".equals(type)) {
                            stringBuffer.append(rcsign);
                        } else if ("370827".equals(type)) {
                            stringBuffer.append(ytsign);
                        } else if ("370891".equals(type)) {
                            stringBuffer.append(tbhsign);
                        } else if ("370832".equals(type)) {
                            stringBuffer.append(lssign);
                        } else {
                            stringBuffer.append(sign);
                        }
                        stringBuffer.append(addSerial);
                        mac = getMD5(stringBuffer.toString());
                        System.out.println(mac);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("ecName", ecName);
                        if ("370828".equals(type)) {
                            jsonObject.put("apId", apIdjx);
                            jsonObject.put("secretKey", secretKeyjx);
                        } else if ("370892".equals(type)) {
                            jsonObject.put("apId", jkapId);
                            jsonObject.put("secretKey", jksecretKey);
                        } else if ("370831".equals(type)) {
                            jsonObject.put("apId", ssapId);
                            jsonObject.put("secretKey", sssecretKey);
                        } else if ("370890".equals(type)) {
                            jsonObject.put("apId", gxapId);
                            jsonObject.put("secretKey", gxsecretKey);
                        } else if ("370883".equals(type)) {
                            jsonObject.put("apId", zcapId);
                            jsonObject.put("secretKey", zcsecretKey);
                        } else if ("370826".equals(type)) {
                            jsonObject.put("apId", wsapId);
                            jsonObject.put("secretKey", wssecretKey);
                        } else if ("370882".equals(type)) {
                            jsonObject.put("apId", yzapId);
                            jsonObject.put("secretKey", yzsecretKey);
                        } else if ("370891".equals(type)) {
                            jsonObject.put("apId", tbhapId);
                            jsonObject.put("secretKey", tbhsecretKey);
                        } else if ("370832".equals(type)) {
                            jsonObject.put("apId", lsapId);
                            jsonObject.put("secretKey", lssecretKey);
                        }
                        else {
                            jsonObject.put("apId", apId);
                            jsonObject.put("secretKey", secretKey);
                        }
                        jsonObject.put("mobiles", mobiles);
                        jsonObject.put("content", content);
                        if ("370828".equals(type)) {
                            jsonObject.put("sign", signjx);
                        } else if ("370892".equals(type)) {
                            jsonObject.put("sign", jksign);
                        } else if ("370831".equals(type)) {
                            jsonObject.put("sign", sssign);
                        } else if ("370890".equals(type)) {
                            jsonObject.put("sign", gxsign);
                        } else if ("370883".equals(type)) {
                            jsonObject.put("sign", zcsign);
                        } else if ("370826".equals(type)) {
                            jsonObject.put("sign", wssign);
                        } else if ("370882".equals(type)) {
                            jsonObject.put("sign", yzsign);
                        } else if ("370891".equals(type)) {
                            jsonObject.put("sign", tbhsign);
                        } else if ("370829".equals(type)) {
                            jsonObject.put("sign", jxsign);
                        } else if ("370811".equals(type)) {
                            jsonObject.put("sign", rcsign);
                        } else if ("370827".equals(type)) {
                            jsonObject.put("sign", ytsign);
                        } else if ("370891".equals(type)) {
                            jsonObject.put("sign", tbhsign);
                        } else if ("370832".equals(type)) {
                            jsonObject.put("sign", lssign);
                        } else {
                            jsonObject.put("sign", sign);
                        }

                        jsonObject.put("addSerial", addSerial);
                        jsonObject.put("mac", mac);
                        String str = jsonObject.toJSONString();
                        byte[] bytes = str.getBytes("utf-8");
                        String encode = Base64Util.encode(bytes);
                        System.out.println("base64加密：" + encode);
                        //调用接口
                        String url = "http://112.35.1.155:1992/sms/norsubmit";
                        if ("370882".equals(type) || "370832".equals(type)) {
                            url = "http://112.35.10.201:5992/sms/norsubmit";
                        }
                        String msg = sendSms(encode, url);
                        JSONObject json = JSONObject.parseObject(msg);
                        if (Boolean.valueOf(json.getString("success"))) {
                            smsService.handleEpointMessage(smssend, 1);
                        } else {
                            //发不出去的短信处理
                            smsService.handleEpointMessage(smssend, 2);
                        }
                    }else{
                        smssend.setIsSend(0);
                        smsService.handleEpointMessage(smssend, 2);
                    }
                    
                }
            } else {
                log.info("===============结束发送短信：==没有需要发送的短信"
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT)
                        + "===============");
            }
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    public String sendSms(String encode, String url) {

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(60 * 1000);
            connection.setReadTimeout(60 * 1000);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            // 发送请求参数
            out.print(encode);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
            System.out.println("====result===" + result + "======");
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
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
        log.info("===============结束发送短信："
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT)
                + "===============");
        return result;

    }


    public static String getMD5(String s) throws UnsupportedEncodingException {
        if (s == null)
            return "";
        else
            return getMD5(s.getBytes("utf-8"));
    }

    public static String getMD5(byte abyte0[]) {
        String s = null;
        char ac[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'
        };
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(abyte0);
            byte abyte1[] = messagedigest.digest();
            char ac1[] = new char[32];
            int i = 0;
            for (int j = 0; j < 16; j++) {
                byte byte0 = abyte1[j];
                ac1[i++] = ac[byte0 >>> 4 & 15];
                ac1[i++] = ac[byte0 & 15];
            }

            s = new String(ac1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return s;
    }


    /**
     * 限频校验
     * @return
     */
    private boolean checkLimit(MessagesCenter messagescenter) {
        IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo().getComponent(IMessagesCenterService.class);
        // 短信限定发送(次)
        int sendlimit = 10;;
        // 短信限定发送(秒)
        int timelimit = 60;;
        Date senddate = messagescenter.getGenerateDate();
        Boolean isxp = true;
        Date lastLimitDate = EpointDateUtil.addSeconds(messagescenter.getGenerateDate(), timelimit * -1);
        Integer count = messagesCenterService.getSmsDataCount(lastLimitDate, senddate, null, null, null, messagescenter.getTargetUser(), 1, true);
        if(count < sendlimit) {
            isxp = false;
        }
        return isxp;
    }


}
