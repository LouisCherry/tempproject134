package com.epoint.znsb.jnzwfw.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.log.LogUtil;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

@RestController
@RequestMapping(value = "/iso8583")
public class SocektRestController extends AuditCommonService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    transient Logger log = LogUtil.getLog(SocektRestController.class);

    /**
     * 人脸比对
     */
    @RequestMapping(value = "/sendData", method = RequestMethod.POST)
    public String sendData(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("==================发送报文=================");
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj =  e.getJSONObject("params");
            JSONObject dataJson = new JSONObject();
            String ip = obj.getString("ip");
            String port = obj.getString("port");
            String data = obj.getString("data");
            String backString = sendIso8583(ip,Integer.parseInt(port),data);
          
            dataJson.put("backstring", backString);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }


    private String  sendIso8583(String ip, Integer port,String loginInput) {
        System.out.println("----------Client----------");
        ClientLaunch cl = new ClientLaunch();
        Socket socket;
        SocketWriter socketWriter = new SocketWriter();
        InputStream socketReader = null;
        String data = "";
        try {
            // 与服务端建立连接
            socket = new Socket(ip, port);
            //登陆，通过验证输出报文中的响应报文是否为 "000000"，判断是否登陆成功，其中loginInput为输出报文字符串
        
            String outlogin = cl.Client(socket, socketWriter, socketReader, loginInput);
            data = outlogin;
            log.info(outlogin);
            socket.close();

        } catch (SocketTimeoutException e) {
         
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }



    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

}
