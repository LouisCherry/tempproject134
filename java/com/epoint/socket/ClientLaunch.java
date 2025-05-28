package com.epoint.socket;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

public class ClientLaunch {
    // 这个客户端连接到地址为xxx.xxx.xxx.xxx的服务器，端口为10020，并发送16进制到服务器，然后接受服务器的返回信息，最后结束会话。
    // 客户端，使用Socket对网络上某一个服务器的某一个端口发出连接请求，一旦连接成功，打开会话；会话完成后，关闭Socket。客户端不需要指定打开的端口，通常临时的、动态的分配一个1024以上的端口。
    public String Client(Socket socket, socketWriter s, InputStream socketReader, String strInput,byte[] by) {
        String strOutput = "";
        try {
            //客户端输出流作为服务器的输入
            OutputStream socketWriter = socket.getOutputStream();
          /*  socketWriter.write(s.hexStringToBytes(strInput));*/
           socketWriter.write(by);
            socketWriter.flush();
            Thread.sleep(1000);

            //服务器的输出即为客户端的输入，这里主要是为了把服务器输出的字节流报文转化成字符串，方便进行解析，最终测试报文的正确性
            socketReader = socket.getInputStream();
            //因为我测试的报文包含报文头和报文体，这里的字节数组长度37为报文头长度
            byte[] temp = new byte[128];
            int bytes = 0;
            /* read从输入流socketReader中读取temp（37）数量的字节数，并将它们存储到缓冲区数组temp。实际读取的字节数作为一个整数37返回。
             * 此方法块，直到输入数据可用，检测到文件结束，或抛出异常。如果B的长度为零，则没有读取字节数和返回0；
             * 否则，将有一个至少一个字节的尝试。如果没有可用的字节，因为流是在文件的结尾，值- 1返回；否则，至少一个字节被读取和存储到temp。
             */
            bytes = socketReader.read(temp);
       /*     if (bytes != 112) {
                return null;
            }*/
            strOutput += ByteUtil.BinaryToHexString(temp);
            //读取报文体的内容
/*            byte[] array = new byte[2];
            for (int i = 1; i < 3; i++) {
                array[i - 1] = temp[i];
            }
            int let = ByteUtil.bytes2Short2(array);
            array = new byte[let];
            bytes = socketReader.read(array);
           if (bytes != let) {
                return null;
            }
            strOutput += ByteUtil.BinaryToHexString(array);*/
            //把字符串中“ ”去掉
            strOutput = strOutput.replaceAll(" ", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strOutput;
    }


}