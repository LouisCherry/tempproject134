
package com.epoint.socket;

import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketClient {
    public static String _pattern = "yyyy-MM-dd HH:mm:ss SSS";
    public static SimpleDateFormat format = new SimpleDateFormat(_pattern);
    // 设置超时间
    public static int _sec = 20;

    public static String  send(byte[] by) {
        //system.out.println("----------Client----------");
        ClientLaunch cl = new ClientLaunch();
        Socket socket;
        socketWriter socketWriter = new socketWriter();
        InputStream socketReader = null;
        String data = "";
        try {
            // 与服务端建立连接
            socket = new Socket("58.48.161.254", 4008);
            //登陆，通过验证输出报文中的响应报文是否为 "000000"，判断是否登陆成功，其中loginInput为输出报文字符串
            String loginInput="003c600084000060220000000008000020000000c00012082605303131373132323931303332303131373132323933393500110000000100300003303120";
            String outlogin = cl.Client(socket, socketWriter, socketReader, loginInput,by);
            data = outlogin;
            //system.out.println(outlogin);
            socket.close();

        } catch (SocketTimeoutException e) {
            //system.out.println(format.format(new Date()) + "\n" + _sec + "秒没收到回复 我下啦\n\n\n\n\n");
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
