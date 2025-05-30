package com.epoint.znsb.jnzwfw.water;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.SocketException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class  WaterFtpUtil{


    transient Logger log = LogUtil.getLog(WaterFtpUtil.class);
    /**
     * FTP上传单个文件测试
     */
    public static void uploadFtpFile(String hostname,String username,
                                     String password,String uploadFilePath,String fileName,String ftpWorkPath)
            throws RuntimeException{
        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;

        try {
            ftpClient.connect(hostname);
            ftpClient.login(username, password);

            File srcFile = new File(uploadFilePath+fileName);
            fis = new FileInputStream(srcFile);
            //设置上传目录
            ftpClient.changeWorkingDirectory("/"+ftpWorkPath);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");

            //设置为被动模式
            ftpClient.enterLocalPassiveMode();

            //设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            System.out.println(ftpClient.storeFile(new String(fileName.getBytes("UTF-8"),"iso-8859-1"), fis));

        } catch ( IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
           System.out.println("已上传至FTP服务器路径！");
        }
    }


    /**
     * 获取FTPClient对象
     *
     * @param ftpHost
     *            FTP主机服务器
     * @param ftpPassword
     *            FTP 登录密码
     * @param ftpUserName
     *            FTP登录用户名
     * @param ftpPort
     *            FTP端口 默认为21
     * @return
     */
    public static FTPClient getFTPClient(String ftpHost, String ftpUserName,
                                         String ftpPassword, int ftpPort) throws RuntimeException{
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                System.out.println("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                System.out.println("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("FTP的IP地址可能错误，请正确配置。");
            throw new RuntimeException("FTP的IP地址可能错误，请正确配置！", e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FTP的端口错误,请正确配置。");
            throw new RuntimeException("FTP的端口错误,请正确配置！", e);
        }
        return ftpClient;
    }

    /**
     * 从FTP服务器下载文件
     * @param ftpHost FTP IP地址
     * @param ftpUserName FTP 用户名
     * @param ftpPassword FTP用户名密码
     * @param ftpPort FTP端口
     * @param ftpPath FTP服务器中文件所在路径 格式： ftptest/aa
     * @param localPath 下载到本地的位置 格式：H:/download
     * @param fileName 文件名称
     */
    public static boolean downloadFtpFile(String ftpHost, String ftpUserName,
                                          String ftpPassword, int ftpPort, String ftpPath, String localPath,
                                          String fileName) throws RuntimeException{
        boolean flag = false;
        FTPClient ftpClient = null;

        try {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);

            File localFile = new File(localPath + File.separatorChar + fileName);
            OutputStream os = new FileOutputStream(localFile);
            ftpClient.retrieveFile(fileName, os);
            os.close();
            ftpClient.logout();
            flag = true;
        } catch (FileNotFoundException e) {
            System.out.println("没有找到" + ftpPath + "文件");
            e.printStackTrace();
            throw new RuntimeException("没有找到" + ftpPath + "文件:", e);
        } catch (SocketException e) {
            System.out.println("连接FTP失败.");
            e.printStackTrace();
            throw new RuntimeException("连接FTP失败:", e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误。");
            e.printStackTrace();
            throw new RuntimeException("文件读取错误:", e);
        }
        return flag;
    }


    public static void main(String[] args) {
        //下载
//		FavFTPUtil.downloadFtpFile("192.168.2.133","adks","adconcepts2017",
//				"fileConllect/test","E:\\xcc", "53840afe-0682-4960-84ef-3f3b972a0f12.zip");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filename = format.format(new Date());

        List<JSONObject> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            JSONObject A = new JSONObject();
            A.put("姓名",i);
            A.put("籍贯",i);
            A.put("信息",i);
            A.put("备注",i);
            list.add(A);
        }
       JSONArray objects = JSONArray.parseArray(JSON.toJSONString(list));


        WaterFtpUtil.createTxtFile(objects,"C:\\Users\\HYF\\Desktop\\",filename);


        WaterFtpUtil.uploadFtpFile("192.168.178.28","HYF","asd1397456",
                "C:\\Users\\HYF\\Desktop\\", filename + ".txt",
                "");

    }





    public static boolean createTxtFile(JSONArray jsonArray, String path, String filename) {
        // 标记文件生成是否成功
        boolean flag = true;
        try {
            File file = new File(path + filename + ".txt");
            File folder = new File(path);
            if (!folder.exists() && !folder.isDirectory()) {
                // 如果不存在,创建文件夹
                folder.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }else{
                if(file.delete()){
                    file.createNewFile();
                }

            }
            // 格式化浮点数据
            NumberFormat formatter = NumberFormat.getNumberInstance();
            // 设置最大小数位为10
            formatter.setMaximumFractionDigits(10);
            // 格式化日期数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // 遍历输出每行
            PrintWriter pfp = new PrintWriter(new FileOutputStream(file, true));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.isEmpty()) {
                    break;
                }
                StringBuilder thisLine = new StringBuilder("");
                for (Iterator<String> iterator = jsonObject.keySet().iterator(); iterator.hasNext(); ) {
                    // 当前字段
                    String key = iterator.next();
                    System.out.println(key);
                    Object obj = jsonObject.get(key);
                    // 格式化数据
                    String field = "";
                    if (null != obj) {
                        if (obj.getClass() == String.class) {
                            // 如果是字符串
                            field = (String) obj;
                        } else if (obj.getClass() == Double.class || obj.getClass() == Float.class) {
                            // 格式化浮点数,使浮点数不以科学计数法输出
                            field = formatter.format(obj);
                        } else if (obj.getClass() == Integer.class || obj.getClass() == Long.class
                                || obj.getClass() == Short.class || obj.getClass() == Byte.class) { // 如果是整形
                            field += obj;
                        } else if (obj.getClass() == Date.class) {
                            // 如果是日期类型
                            field = sdf.format(obj);
                        }
                    } else {
                        // null时给一个空格占位
                        field = " ";
                    }
                    // 拼接所有字段为一行数据，用tab键分隔
                    // 不是最后一个元素
                    if (iterator.hasNext()) {
                        thisLine.append(field).append("|");
                    } else {
                        // 是最后一个元素
                        thisLine.append(field);
                    }
                }
                pfp.print(thisLine.toString() + "\n");
            }
            pfp.close();
        } catch (Exception e) {
            flag = false;
            System.out.println("生成txt文件失败");
        }
        return flag;

    }


    //将生成的TXT转化附件上传


    public static String getAttachguid(String fileguid,String filename) {

        try{

            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            String dir = ClassPathUtil.getDeployWarPath() + "jiningzwfw/water/";
            String file = dir + filename + ".txt";
            //找打对应的文件  变成输入流
            InputStream ipt = new FileInputStream(new File(file));

            long size = (long) ipt.available();
            //转化为附件
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(fileguid);
            frameAttachInfo.setAttachFileName(filename + ".txt");
            frameAttachInfo.setCliengTag("水务对账信息");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("txt");
            frameAttachInfo.setAttachLength(size);
            String attachguid =  attachService.addAttach(frameAttachInfo, ipt).getAttachGuid();
            ipt.close();
            return attachguid;
        }catch (IOException e){


        }
        return null;
    }
}
