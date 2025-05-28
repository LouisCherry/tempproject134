package com.epoint.zoucheng.znsb.worktablecomment.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.esotericsoftware.minlog.Log;

/**
 * 
 * 通用工具类
 * 
 * @version [版本号, 2016年8月21日]
 */
public class QueueCommonUtil
{
    /**
     * 右补位，左对齐
     * 
     * @param oriStr
     *            原字符串
     * @param len
     *            目标字符串长度
     * @param alexin
     *            补位字符
     * @return 目标字符串
     */
    public static String padRight(String oriStr, int len, char alexin) {
        int strlen = oriStr.length();
        String str = "";
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str = str + alexin;
            }
        }
        str = oriStr + str;
        return str;
    }

    /**
     * 左补位
     * 
     * @param oriStr
     *            原字符串
     * @param len
     *            目标字符串长度
     * @param alexin
     *            补位字符
     * @return 目标字符串
     */
    public static String padLeft(String oriStr, int len, char alexin) {
        int strlen = oriStr.length();
        String str = "";
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str = str + alexin;
            }
        }
        str = str + oriStr;
        return str;
    }

    public static String getUrlPath(String requestURL) {
        int index = requestURL.indexOf("/rest");
        return requestURL.substring(0, index);
    }

    public static String getWebserviceUrlPath(String requestURL) {
        int index = requestURL.indexOf("/services/appwebservice");
        return requestURL.substring(0, index);
    }

    /**
     * 
     *  获取初始化模块list
     *  返回模块list数据
     *  @return    
     * 
     * 
     */
    public static List<Record> getModuleList() {
        List<Record> recordlist = new ArrayList<Record>();
        try {
            Document document = DocumentHelper.parseText(getXML("module"));
            Element ROOT = document.getRootElement();
            Element modulelist = ROOT.element("modulelist");
            List<?> module = modulelist.elements();
            for (Iterator<?> its = module.iterator(); its.hasNext();) {
                Element moduleinfo = (Element) its.next();
                Record moduleRecord = new Record();
                moduleRecord.set("modulename", moduleinfo.element("modulename").getText().trim());
                moduleRecord.set("moduletype", moduleinfo.element("moduletype").getText().trim());
                moduleRecord.set("picturepath", moduleinfo.element("picturepath").getText().trim());
                moduleRecord.set("htmlurl", moduleinfo.element("htmlurl").getText().trim());
                moduleRecord.set("isneedlogin", moduleinfo.element("isneedlogin").getText().trim());
                moduleRecord.set("ordernum", moduleinfo.element("ordernum").getText().trim());
                recordlist.add(moduleRecord);
            }
        }
        catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return recordlist;
    }

    /**
     * 
     *  获取初始化模块list
     *  返回模块list数据
     *  @return    
     * 
     * 
     */
    public static List<Record> getModuleList(String moduleconfigtype) {
        List<Record> recordlist = new ArrayList<Record>();
        try {
            Document document;
            if (StringUtil.isBlank(moduleconfigtype) || QueueConstant.CONSTANT_STR_ZERO.equals(moduleconfigtype)) {
                document = DocumentHelper.parseText(getXML("module1440"));
            }
            else if (QueueConstant.CONSTANT_STR_ONE.equals(moduleconfigtype)) {
                document = DocumentHelper.parseText(getXML("module1920"));
            }
            else if (QueueConstant.CONSTANT_STR_TWO.equals(moduleconfigtype)) {
                document = DocumentHelper.parseText(getXML("module24hour"));
            }
            else {
                document = DocumentHelper.parseText(getXML("module"));
            }

            Element ROOT = document.getRootElement();
            Element modulelist = ROOT.element("modulelist");
            List<?> module = modulelist.elements();
            for (Iterator<?> its = module.iterator(); its.hasNext();) {
                Element moduleinfo = (Element) its.next();
                Record moduleRecord = new Record();
                moduleRecord.set("modulename", moduleinfo.element("modulename").getText().trim());
                moduleRecord.set("moduletype", moduleinfo.element("moduletype").getText().trim());
                moduleRecord.set("picturepath", moduleinfo.element("picturepath").getText().trim());
                moduleRecord.set("htmlurl", moduleinfo.element("htmlurl").getText().trim());
                moduleRecord.set("isneedlogin", moduleinfo.element("isneedlogin").getText().trim());
                moduleRecord.set("ordernum", moduleinfo.element("ordernum").getText().trim());
                if (StringUtil.isNotBlank(moduleinfo.element("moduleconfigtype"))) {
                    moduleRecord.set("moduleconfigtype", moduleinfo.element("moduleconfigtype").getText().trim());
                }
                // 工作台模块增加配置
                if (StringUtil.isNotBlank(moduleinfo.element("rowguid"))) {
                    moduleRecord.set("rowguid", moduleinfo.element("rowguid").getText().trim());
                }
                if (StringUtil.isNotBlank(moduleinfo.element("parentmoduleguid"))) {
                    moduleRecord.set("parentmoduleguid", moduleinfo.element("parentmoduleguid").getText().trim());
                }
                if (StringUtil.isNotBlank(moduleinfo.element("speciallabel"))) {
                    moduleRecord.set("speciallabel", moduleinfo.element("speciallabel").getText().trim());
                }
                if (StringUtil.isNotBlank(moduleinfo.element("linenum"))) {
                    moduleRecord.set("linenum", moduleinfo.element("linenum").getText().trim());
                }
                if (StringUtil.isNotBlank(moduleinfo.element("hotnum"))) {
                    moduleRecord.set("hotnum", moduleinfo.element("hotnum").getText().trim());
                }
                if (StringUtil.isNotBlank(moduleinfo.element("modulecode"))) {
                    moduleRecord.set("modulecode", moduleinfo.element("modulecode").getText().trim());
                }
                recordlist.add(moduleRecord);
            }
        }
        catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return recordlist;
    }

    /**
     * 
     *  获取初始化存取盒list
     *  返回模块list数据
     *  @return    
     * 
     * 
     */
    public static List<Record> getBoxList(String machinetype) {
        List<Record> recordlist = new ArrayList<Record>();
        try {
            Document document = null;
            
            if(QueueConstant.EQUIPMENT_TYPE_YTJ24.equals(machinetype)){
                document = DocumentHelper.parseText(getXML("24hours"));
            }else if (QueueConstant.EQUIPMENT_TYPE_CABINET.equals(machinetype)){
                document = DocumentHelper.parseText(getXML("accesscabinet"));
            }else{
                document = DocumentHelper.parseText(getXML("accessbox"));
            }
            
            Element ROOT = document.getRootElement();
            Element boxlist = ROOT.element("boxlist");
            List<?> box = boxlist.elements();
            for (Iterator<?> its = box.iterator(); its.hasNext();) {
                Element boxinfo = (Element) its.next();
                Record boxRecord = new Record();
                boxRecord.set("boxno", boxinfo.element("boxno").getText().trim());
                boxRecord.set("abscissa", boxinfo.element("abscissa").getText().trim());
                boxRecord.set("ordinate", boxinfo.element("ordinate").getText().trim());
                boxRecord.set("boxstatus", boxinfo.element("boxstatus").getText().trim());
                recordlist.add(boxRecord);
            }
        }
        catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return recordlist;
    }

    /**
     * 
     *  获取系统参数list
     *  返回系统参数list数据
     *  @return    
     * 
     * 
     */
    public static List<Record> getFrameConfigList() {
        List<Record> recordlist = new ArrayList<Record>();
        try {
            Document document = DocumentHelper.parseText(getXML("frameconfig"));
            Element ROOT = document.getRootElement();
            Element configlist = ROOT.element("configlist");
            List<?> config = configlist.elements();
            for (Iterator<?> its = config.iterator(); its.hasNext();) {
                Element configinfo = (Element) its.next();
                Record configRecord = new Record();
                configRecord.set("frameconfigname", configinfo.element("frameconfigname").getText().trim());
                configRecord.set("configintroduce", configinfo.element("configintroduce").getText().trim());
                configRecord.set("configtype", configinfo.element("configtype").getText().trim());
                configRecord.set("isoverall", configinfo.element("isoverall").getText().trim());
                recordlist.add(configRecord);
            }
        }
        catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return recordlist;
    }

    /**
     * 
     *  读取xml文件
     *  根据xml文件名返回xml文件内容(String形式)
     *  @param filetype
     *  @return
     *  @throws IOException    
     * 
     * 
     */
    public static String getXML(String filetype) {
        String xmlPath = "znsb/xmlfile/";//配置相对路径
        if (StringUtil.isNotBlank(filetype)) {
            switch (filetype) {
                case "module":
                    xmlPath += "Module.xml";
                    break;
                case "frameconfig":
                    xmlPath += "FrameConfig.xml";
                    break;
                case "accessbox":
                    xmlPath += "AccessBox.xml";
                    break;
                case "24hours":
                    xmlPath += "24Hours.xml";
                    break;
                case "accesscabinet":
                    xmlPath += "AccessCabinet.xml";
                    break;
                case "module1440":
                    xmlPath += "Module1440.xml";
                    break;
                case "module1920":
                    xmlPath += "Module1920.xml";
                    break;
                case "module24hour":
                    xmlPath += "Module24Hour.xml";
                    break;
                default:
                    xmlPath = "";
                    break;
            }
        }

        if (StringUtil.isNotBlank(xmlPath)) {
            try {
                return file2String(ClassPathUtil.getDeployWarPath() + xmlPath);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } //以相对路径，获取绝对路径
        }

        return null;
    }

    /**
     * 
     *  文本文件转换为指定编码的字符串
     *  传入文件地址 返回utf8编码的文件内容(String)
     *  @param path
     *  @return
     *  @throws IOException    
     * 
     * 
     */
    public static String file2String(String path) throws IOException {
        File file = FileManagerUtil.createFile(path);
        if (!file.exists()) {
            throw new IOException("文件不存在！路径：" + path);
        }
        InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;
        InputStream inputStream = null;
        StringBuffer strBuffer = new StringBuffer();
        try {
            inputStream = new FileInputStream(file);
            inputReader = new InputStreamReader(inputStream, "UTF-8");
            bufferReader = new BufferedReader(inputReader);

            // 读取一行
            String line = null;
            while ((line = bufferReader.readLine()) != null) {
                strBuffer.append(line);
            }

            inputStream.close();
            inputReader.close();
            bufferReader.close();
        }
        catch (IOException e) {
            if (inputStream != null) {
                inputStream.close();
            }

            if (inputReader != null) {
                inputReader.close();
            }
            if (bufferReader != null) {
                bufferReader.close();
            }

            throw new IOException("读取文件内容失败！路径：" + path);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e1) {
                    Log.error(e1.getMessage());
                }
            }

            if (inputReader != null) {
                try {
                    inputReader.close();
                }
                catch (IOException e1) {
                    Log.error(e1.getMessage());
                }
            }

            if (bufferReader != null) {
                try {
                    bufferReader.close();
                }
                catch (IOException e1) {
                    Log.error(e1.getMessage());
                }
            }
        }
        return strBuffer.toString().trim();
    }
}
