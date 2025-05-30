package com.epoint.zwdt.zwdtrest.project.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dwgcinfo.api.IDwgcInfoService;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.common.util.AttachUtil;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.epointsform.MapMailMergeDataSource;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.spire.doc.DocumentObject;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;

public class GenerateAttachUtil {

    public static String generateWordFile(JSONObject paramsJson, JSONObject basicJson) throws Exception {

        Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

        String retAttachGuid = null;
        try {

            log.info("=======paramsJson=======" + paramsJson);
            log.info("=======basicJson========" + basicJson);

            String template = ClassPathUtil.getDeployWarPath() + "template/申请表模板.doc";

            String basicAttachGuid = UUID.randomUUID().toString();
            /*
             * File file = new File(template);
             * InputStream templateInputStream = new FileInputStream(file);
             *
             * Map<String, Object> fieldMap = getFieldMap(basicJson);
             * log.info("=======fieldMap========" + fieldMap);
             *
             * byte[] pdfDocContent = genPdfDoc(templateInputStream, fieldMap);
             * log.info("=======pdfDocContent========" + pdfDocContent);
             * String basicAttachGuid = UUID.randomUUID().toString();
             * log.info("=======basicAttachGuid========" + basicAttachGuid);
             * InputStream inputStream = new ByteArrayInputStream(pdfDocContent);
             * log.info("=======inputStream========" + inputStream);
             * FrameAttachInfo attach = new FrameAttachInfo();
             * attach.setAttachFileName("道路挖掘修复一件事基本信息.doc");
             * attach.setAttachGuid(basicAttachGuid);
             * attach.setCliengGuid(UUID.randomUUID().toString());
             * attach.setContentType(".doc");
             * attach.setUploadDateTime(new Date());
             * attach.setAttachLength((long) inputStream.available());
             * EpointFrameDsManager.begin(null);
             * iAttachService.addAttach(attach, inputStream);
             * EpointFrameDsManager.commit();
             */

            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            new License().setLicense(licenseName);

            Document doc = new Document(template);
            String[] fieldNames = null;
            Object[] values = null;

            // 获取域与对应的值
            Map<String, String> fieldMap = new HashMap<String, String>(16);

            fieldMap.put("itemcode", basicJson.getString("itemcode"));
            fieldMap.put("subappname", basicJson.getString("subappname"));
            fieldMap.put("subitemcode", basicJson.getString("subitemcode"));
            fieldMap.put("constructionsite", basicJson.getString("constructionsite"));
            fieldMap.put("constructionproperty", basicJson.getString("constructionproperty"));
            fieldMap.put("itemstartdate", basicJson.getString("itemstartdate"));
            fieldMap.put("itemfinishdate", basicJson.getString("itemfinishdate"));
            // 申请单位
            fieldMap.put("applyname", basicJson.getString("applyname"));
            fieldMap.put("applyaddress", basicJson.getString("applyaddress"));
            fieldMap.put("applycode", basicJson.getString("applycode"));
            fieldMap.put("applylegal", basicJson.getString("applylegal"));
            fieldMap.put("applycertnum", basicJson.getString("applycertnum"));

            // 施工单位
            fieldMap.put("buildname", basicJson.getString("sgdepartment"));
            fieldMap.put("buildaddress", basicJson.getString("sgdepartmentaddress"));
            fieldMap.put("buildcode", basicJson.getString("sgdepartmentcreditcode"));
            fieldMap.put("buildlegal", basicJson.getString("sgdepartmentlegal"));
            fieldMap.put("buildcertnum", basicJson.getString("sgdepartmentlegalid"));

            // 主管人
            fieldMap.put("chargename", basicJson.getString("zgrname"));
            fieldMap.put("chargecertnum", basicJson.getString("zgrid"));
            fieldMap.put("chargephone", basicJson.getString("zgrmobile"));
            // 经办人
            fieldMap.put("handlename", basicJson.getString("jbrname"));
            fieldMap.put("handlecertnum", basicJson.getString("jbrid"));
            fieldMap.put("handlephone", basicJson.getString("jbrmobile"));

            // 工程建设项目名称及规划批准文件
            fieldMap.put("document", basicJson.getString("document"));
            // 施工方案
            fieldMap.put("constructionscheme", basicJson.getString("constructionscheme"));
            // 示意图
            fieldMap.put("schematicdiagram", basicJson.getString("schematicdiagram"));
            fieldMap.put("botanypromise", basicJson.getString("botanypromise"));
            fieldMap.put("facilitiespromise", basicJson.getString("facilitiespromise"));

            fieldNames = new String[fieldMap == null ? 0 : fieldMap.size()];
            values = new Object[fieldMap == null ? 0 : fieldMap.size()];
            int num = 0;

            for (Entry<String, String> entry : fieldMap.entrySet()) {
                fieldNames[num] = entry.getKey();
                values[num] = entry.getValue();
                num++;
            }
            // 替换域
            doc.getMailMerge().execute(fieldNames, values);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.DOC);// 保存成word
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = inputStream.available();
            // 附件信息
            AttachUtil.saveFileInputStream(basicAttachGuid, UUID.randomUUID().toString(), "道路挖掘修复一件事基本信息.doc", "doc", "", size, inputStream, "", "");
            // 生成文件到指定的位置
            String basicTargetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + basicAttachGuid + ".doc";
            File basicTargetFile = new File(basicTargetPath);
            FileOutputStream basicFops = null;
            try {
                basicFops = new FileOutputStream(basicTargetFile);
                basicFops.write(readInputStream(inputStream));
                basicFops.flush();
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            finally {
                if (basicFops != null) {
                    try {
                        basicFops.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 添加到待合并集合
            List<String> targetFileList = new ArrayList<>();
            // targetFileList.add(basicTargetPath);

            // 合并文件信息
            String megreGuid = UUID.randomUUID().toString();
            String mergeTargetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + megreGuid + "_merge.doc";
            File file1 = new File(mergeTargetPath);

            // 获取表单附件地址
            String savetype = ".doc";
            String rowguid = paramsJson.getString("rowguid");
            String formids = paramsJson.getString("formids");
            List<String> urlList = new ArrayList<String>();

            if (StringUtil.isNotBlank(formids)) {
                String[] arr = formids.split(",");
                for (String formid : arr) {
                    String eformFileUrl = getEformFileUrl(savetype, rowguid, formid);
                    if (StringUtil.isNotBlank(eformFileUrl)) {
                        urlList.add(eformFileUrl);
                    }
                }
            }

            log.info("=======urlList========" + urlList);
            if (!urlList.isEmpty() && urlList.size() > 0) {
                for (String url : urlList) {
                    String[] attachGuid = url.split("attachGuid=");
                    InputStream targetInputStream = handelUrl(url);
                    // 生成文件到指定的位置
                    String targetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + attachGuid[1] + ".doc";
                    File targetFile = new File(targetPath);
                    FileOutputStream fops = null;
                    try {
                        fops = new FileOutputStream(targetFile);
                        fops.write(readInputStream(targetInputStream));
                        fops.flush();
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    finally {
                        if (fops != null) {
                            try {
                                fops.close();
                            }
                            catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                    targetFileList.add(targetPath);
                }
            }

            // 重新写入基本信息文件
            String url = "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + basicAttachGuid;
            InputStream targetInputStream = handelUrl(url);
            // 生成文件到指定的位置
            String targetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + basicAttachGuid + ".doc";
            File targetFile = new File(targetPath);
            FileOutputStream fops = null;
            try {
                fops = new FileOutputStream(targetFile);
                fops.write(readInputStream(targetInputStream));
                fops.flush();
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            finally {
                if (fops != null) {
                    try {
                        fops.close();
                    }
                    catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }

            log.info("=======targetFileList========" + targetFileList);
            log.info("=======file1========" + file1);

            if (!targetFileList.isEmpty() && targetFileList.size() > 0) {
                // 合并附件
                MergeAppendDoc(mergeTargetPath, targetPath, targetFileList);
            }
            else {
                return basicAttachGuid;
            }
            // 指定位置插入超链接
            // 加载文档
            // com.spire.doc.Document document = new com.spire.doc.Document();
            // mergeTargetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + megreGuid + "_merge.docx";
            //
            // document.loadFromFile(mergeTargetPath, FileFormat.Docx);
            // // 获取书签
            // BookmarksNavigator bookmarksNavigator = new BookmarksNavigator(document);
            // bookmarksNavigator.moveToBookmark("FILE");
            // bookmarksNavigator.insertText("我是标题");
            //
            // bookmarksNavigator.moveToBookmark("FILE");
            // Paragraph paragraph = new Paragraph(document);
            // // 添加超链接
            // String url = "http://localhost:8070/epoint-web-zwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=245f950b-d21b-40b4-9511-32c83369b041";
            // paragraph.appendHyperlink(url, "文件1", HyperlinkType.Web_Link);
            // // 换行
            // paragraph.appendBreak(BreakType.Line_Break);
            // // 添加超链接（多个链接一般用for循环从list中读取，添加到paragraph）
            // url = "http://localhost:8070/epoint-web-zwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=7d630270-57b3-4604-9697-0469962410b8";
            // paragraph.appendHyperlink(url, "文件2", HyperlinkType.Web_Link);
            //
            // TextBodyPart textBodyPart = new TextBodyPart(document);
            // System.out.println(textBodyPart);
            // System.out.println(bookmarksNavigator);
            // bookmarksNavigator.replaceBookmarkContent(textBodyPart);
            // textBodyPart.getBodyItems().add(paragraph);
            // bookmarksNavigator.replaceBookmarkContent(textBodyPart);
            //
            // // 保存到硬盘
            // document.saveToFile(mergeTargetPath, FileFormat.Docx);

            file1 = new File(mergeTargetPath);
            InputStream stream = null;
            try {
                // 存储到数据库
                retAttachGuid = UUID.randomUUID().toString();
                stream = new FileInputStream(file1);
                size = stream.available();
                AttachUtil.saveFileInputStream(retAttachGuid, UUID.randomUUID().toString(), "道路挖掘修复一件事基本信息(总表).doc", "doc", "", size, stream, "", "");

                // 附件信息
                /*
                 * FrameAttachInfo attach = new FrameAttachInfo();
                 * attach.setCliengGuid(UUID.randomUUID().toString());
                 * attach.setAttachFileName("道路挖掘修复一件事基本信息(总表).doc");
                 * attach.setCliengTag("道路挖掘修复一件事");
                 * attach.setUploadUserGuid("");
                 * attach.setUploadUserDisplayName("");
                 * attach.setUploadDateTime(new Date());
                 * attach.setContentType(".doc");
                 * attach.setAttachLength(size);
                 * iAttachService.addAttach(attach, stream);
                 */

            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            finally {
                if (stream != null) {
                    try {
                        stream.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        log.info("========retAttachGuid=======" + retAttachGuid);
        return retAttachGuid;
    }

    private static void MergeAppendDoc(String mergeTargetPath, String targetPath, List<String> targetFileList) {

        for (int i = 0; i < targetFileList.size(); i++) {
            String filePath = targetFileList.get(i);
            // 加载第一个文档
            com.spire.doc.Document document1 = new com.spire.doc.Document(targetPath);
            // 获取第一个文档的最后一个section
            Section lastSection = document1.getLastSection();
            // 加载第二个文档
            com.spire.doc.Document document2 = new com.spire.doc.Document(filePath);
            // 将第二个文档的段落作为新的段落添加到第一个文档的最后一个section
            for (Section section : (Iterable<Section>) document2.getSections()) {
                for (DocumentObject obj : (Iterable<DocumentObject>) section.getBody().getChildObjects()) {
                    lastSection.getBody().getChildObjects().add(obj.deepClone());
                }
            }
            // 保存文档
            if (i == targetFileList.size() - 1) {
                document1.saveToFile(mergeTargetPath, FileFormat.Docx_2013);
            }
            else {
                document1.saveToFile(targetPath, FileFormat.Docx_2013);
            }
        }
    }

    private static String getEformFileUrl(String savetype, String rowguid, String formid) {
        String attachUrl = null;
        try {

            Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

            IConfigService configServicce = ContainerFactory.getContainInfo().getComponent(IConfigService.class);

            String epointsformurl = configServicce.getFrameConfigValue("epointsformurlwt");
            String token = configServicce.getFrameConfigValue("eform_token");
            String url = epointsformurl + "rest/jnepointsform/eformToWordOrPdf";

            JSONObject paramsJson = new JSONObject();
            JSONObject infoJson = new JSONObject();
            infoJson.put("savetype", savetype);
            infoJson.put("rowguid", rowguid);
            infoJson.put("formid", formid);
            paramsJson.put("token", token);
            paramsJson.put("params", infoJson);

            String retInfo = HttpUtil.doPostJson(url, paramsJson.toString());
            log.info("======formid:" + formid + "====retInfo:" + retInfo);
            JSONObject retJson = JSONObject.parseObject(retInfo);
            String code = retJson.getJSONObject("custom").getString("code");
            if ("1".equals(code)) {
                attachUrl = retJson.getJSONObject("custom").getString("attachUrl");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return attachUrl;
    }

    public static Map<String, Object> getFieldMap(JSONObject basicJson) {

        Map<String, Object> fieldMap = new HashMap<String, Object>();

        String ghxk_cliengguid = basicJson.getString("ghxk_cliengguid");
        String sgfa_cliengguid = basicJson.getString("sgfa_cliengguid");
        String syt_cliengguid = basicJson.getString("syt_cliengguid");

        // 申请单位
        fieldMap.put("applyname", basicJson.getString("applyname"));
        fieldMap.put("applyaddress", basicJson.getString("applyaddress"));
        fieldMap.put("applycode", basicJson.getString("applycode"));
        fieldMap.put("applylegal", basicJson.getString("applylegal"));
        fieldMap.put("applycertnum", basicJson.getString("applycertnum"));

        // 施工单位
        fieldMap.put("buildname", basicJson.getString("sgdepartment"));
        fieldMap.put("buildaddress", basicJson.getString("sgdepartmentaddress"));
        fieldMap.put("buildcode", basicJson.getString("sgdepartmentcreditcode"));
        fieldMap.put("buildlegal", basicJson.getString("sgdepartmentlegal"));
        fieldMap.put("buildcertnum", basicJson.getString("sgdepartmentlegalid"));

        // 主管人
        fieldMap.put("chargename", basicJson.getString("zgrname"));
        fieldMap.put("chargecertnum", basicJson.getString("zgrid"));
        fieldMap.put("chargephone", basicJson.getString("zgrmobile"));
        // 经办人
        fieldMap.put("handlename", basicJson.getString("jbrname"));
        fieldMap.put("handlecertnum", basicJson.getString("jbrid"));
        fieldMap.put("handlephone", basicJson.getString("jbrmobile"));

        // 工程建设项目名称及规划批准文件
        fieldMap.put("document", basicJson.getString("document"));
        // 施工方案
        fieldMap.put("constructionscheme", basicJson.getString("constructionscheme"));
        // 示意图
        fieldMap.put("schematicdiagram", basicJson.getString("schematicdiagram"));

        return fieldMap;
    }

    public static byte[] genPdfDoc(InputStream templateInputStream, Map<String, Object> fieldMap) {
        String[] fieldNames = new String[fieldMap.keySet().size()];
        Object[] fieldValues = new Object[fieldMap.keySet().size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            fieldNames[i] = entry.getKey();
            fieldValues[i] = entry.getValue();
            i++;
        }
        Document doc = null;
        try {
            // 替换域
            doc = executeMailMerge(fieldNames, fieldValues, templateInputStream);
            ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
            doc.save(byteArrayOutput, SaveFormat.DOC);
            // 返回新文档的字节数组
            return byteArrayOutput.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Document executeMailMerge(String[] fieldNames, Object[] fieldValues, InputStream inputStream) {
        // 定义存储路径
        try {

            String licenseName = ClassPathUtil.getClassesPath() + "EpointAsposeWords.lic";

            // 获取证书文件
            License license = new License();
            license.setLicense(licenseName);
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
            Document doc = new Document(inputStream);
            doc.getMailMerge().execute(fieldNames, fieldValues);
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("合并邮件域失败");
        }
    }

    /**
     * 根据下载路径获取文件流
     *
     * @description
     * @author shibin
     * @date 2020年4月20日 上午10:07:10
     */
    private static BufferedInputStream handelUrl(String imgUrl) {

        // 统一资源
        URL url;
        BufferedInputStream bin = null;
        try {
            url = new URL(imgUrl);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(5000);
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设置超时
            httpURLConnection.setConnectTimeout(1000 * 5);
            // 设置请求方式，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            // 建立链接从请求中获取数据
            bin = new BufferedInputStream(httpURLConnection.getInputStream());

            /*
             * ByteArrayOutputStream baos = new ByteArrayOutputStream();
             * byte[] buffer = new byte[1024];
             * int read;
             * while ((read = bin.read(buffer, 0, buffer.length)) != -1) {
             * baos.write(buffer, 0, read);
             * }
             * return baos.toByteArray();
             */
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        return bin;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 把多个docx文件合并成一个
     *
     * @param outfile    输出文件
     * @param targetFile 目标文件
     */
    public static void DocxMergeAppendDocx(File outfile, List<File> targetFile) {
        try {

            OutputStream dest = new FileOutputStream(outfile);
            ArrayList<XWPFDocument> documentList = new ArrayList<>();
            XWPFDocument doc = null;
            for (int i = 0; i < targetFile.size(); i++) {
                ZipSecureFile.setMinInflateRatio(-1.0d);
                FileInputStream in = new FileInputStream(targetFile.get(i).getPath());
                OPCPackage open = OPCPackage.open(in);
                XWPFDocument document = new XWPFDocument(open);
                documentList.add(document);
            }
            for (int i = 0; i < documentList.size(); i++) {
                doc = documentList.get(0);
                if (i != 0) {
                    // 解决word合并完后，所有表格都紧紧挨在一起，没有分页。加上了分页符可解决
                    documentList.get(i).createParagraph().setPageBreak(true);
                    appendBody(doc, documentList.get(i));
                }
            }
            doc.write(dest);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendBody(XWPFDocument src, XWPFDocument append) throws Exception {
        CTBody src1Body = src.getDocument().getBody();
        CTBody src2Body = append.getDocument().getBody();

        List<XWPFPictureData> allPictures = append.getAllPictures();
        // 记录图片合并前及合并后的ID
        Map<String, String> map = new HashMap<>();
        for (XWPFPictureData picture : allPictures) {
            String before = append.getRelationId(picture);
            // 将原文档中的图片加入到目标文档中
            String after = src.addPictureData(picture.getData(), 6);// Document.PICTURE_TYPE_PNG
            map.put(before, after);
        }
        // 这个代码主要解决合并word报错，解析抛出压缩炸弹
        ZipSecureFile.setMinInflateRatio(-1.0d);
        appendBody(src1Body, src2Body, map);

    }

    private static void appendBody(CTBody src, CTBody append, Map<String, String> map) throws Exception {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);

        String srcString = src.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String sufix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        // 下面这部分可以去掉，我加上的原因是合并的时候，有时候出现打不开的情况，对照document.xml将某些标签去掉就可以正常打开了
        addPart = addPart.replaceAll("w14:paraId=\"[A-Za-z0-9]{1,10}\"", "");
        addPart = addPart.replaceAll("w14:textId=\"[A-Za-z0-9]{1,10}\"", "");
        addPart = addPart.replaceAll("w:rsidP=\"[A-Za-z0-9]{1,10}\"", "");
        addPart = addPart.replaceAll("w:rsidRPr=\"[A-Za-z0-9]{1,10}\"", "");
        addPart = addPart.replace("<w:headerReference r:id=\"rId8\" w:type=\"default\"/>", "");
        addPart = addPart.replace("<w:footerReference r:id=\"rId9\" w:type=\"default\"/>", "");
        addPart = addPart.replace("xsi:nil=\"true\"", "");

        if (map != null && !map.isEmpty()) {
            // 对xml字符串中图片ID进行替换
            for (Map.Entry<String, String> set : map.entrySet()) {
                addPart = addPart.replace(set.getKey(), set.getValue());
            }
        }
        // 将两个文档的xml内容进行拼接
        CTBody makeBody = CTBody.Factory.parse(prefix + mainPart + addPart + sufix);

        src.set(makeBody);
    }

    public static String generateJskgyjsWordFile(JSONObject paramsJson, JSONObject basicJson) {
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IDantiSubRelationService iDantiSubRelationService = ContainerFactory.getContainInfo()
                .getComponent(IDantiSubRelationService.class);
        IDantiInfoService iDantiInfoService = ContainerFactory.getContainInfo().getComponent(IDantiInfoService.class);
        IDwgcInfoService iDwgcInfoService = ContainerFactory.getContainInfo().getComponent(IDwgcInfoService.class);

        Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

        String retAttachGuid = null;
        try {

            log.info("=======paramsJson=======" + paramsJson);
            log.info("=======basicJson========" + basicJson);

            String template = ClassPathUtil.getDeployWarPath() + "template/建设项目开工一件事模板.doc";

            String basicAttachGuid = UUID.randomUUID().toString();


            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            new License().setLicense(licenseName);

            Document doc = new Document(template);

            String[] fieldNames = null;
            Object[] values = null;

            // 获取域与对应的值
            Map<String, String> fieldMap = new HashMap<String, String>(16);
            // 获取 JSON 对象的所有键
            for (String key : basicJson.keySet()) {
                // 获取每个键对应的值
                String value = basicJson.getString(key);
                //处理代码项：
                if ("isallowsubitem".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("是否", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("zdxmlx".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("国标_重点项目", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("itemtype".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("项目类型", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("constructionproperty".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("建设性质", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("belongtindustry".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("所属行业", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("isimprovement".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("是否", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("tdhqfs".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("土地获取方式", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("tdsfdsjfa".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("是否", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("sfwcqypg".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("是否", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("buildType".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("项目类型", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("xmzjsx".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("项目资金属性", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("lxlx".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("国标_立项类型", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("xmtzly".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("项目投资来源", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("gchyfl".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("国标_工程行业分类", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("jsddxzqh".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("行政区划", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("xmszqx".equals(key)){
                    String codeName = codeItemsService.getItemTextByCodeName("行政区划", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("sfxxgc".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("是否", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("xmfl".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("项目分类", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("gctzxz".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("工程投资性质", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("xmszsf".equals(key)) {
                    value="山东省";
                }
                if ("xmszcs".equals(key)) {
                    value="济宁市";
                }
                if ("xmszcs".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("行政区划", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("gcyt".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("工程用途", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                if ("gcyt".equals(key)) {
                    String codeName = codeItemsService.getItemTextByCodeName("工程用途", value);
                    if (StringUtil.isNotBlank(codeName)) {
                        value = codeName;
                    }
                }
                fieldMap.put(key, value);
            }

            // 单体信息
            List<Map<String, Object>> catdataList = new ArrayList<Map<String, Object>>();

            String subappguid = basicJson.getString("subappguid");// 申报唯一标识
            String itemguid = basicJson.getString("itemguid");// 申报唯一标识

            List<DantiInfo> dantiInfoList = iDantiInfoService.findListByProjectguid(itemguid);
            // 建筑单体栋数
            // 总建筑面积
            Double zjzmjz = 0.0;
            // 最高高度
            Double zgjzgd = 0.0;
            for (DantiInfo dantiInfo : dantiInfoList) {
                if (dantiInfo.getJzgd() != null && dantiInfo.getJzgd() > zgjzgd) {
                    zgjzgd = dantiInfo.getJzgd();
                }
                if (dantiInfo.getZjzmj() != null) {
                    zjzmjz += dantiInfo.getZjzmj();
                }
            }
            List<DantiInfo> alldantiInfo = iDantiInfoService.findListBySubAppguid(subappguid);
            for (DantiInfo dantiInfo : alldantiInfo) {
                JSONObject jsonMaterial = new JSONObject();
                jsonMaterial.put("dantiname", dantiInfo.getDantiname());
                if (StringUtil.isNotBlank(dantiInfo.getGclb())) {
                    String name = codeItemsService.getItemTextByCodeName("国标_工程类别", dantiInfo.getGclb());
                    jsonMaterial.put("gclbmc", name);
                }
                else {
                    jsonMaterial.put("gclbmc", "");
                }
                jsonMaterial.put("zjzmj", dantiInfo.getZjzmj());
                jsonMaterial.put("dscs", dantiInfo.getDscs());
                jsonMaterial.put("dxcs", dantiInfo.getDxcs());

                jsonMaterial.put("rowguid", dantiInfo.getRowguid());
                jsonMaterial.put("phaseguid", dantiInfo.getPhaseguid());
                jsonMaterial.put("dantiguid", dantiInfo.getRowguid());
                jsonMaterial.put("dtbm", dantiInfo.getStr("dtbm"));
                jsonMaterial.put("fllb", dantiInfo.getFllb());
                jsonMaterial.put("gclb", dantiInfo.getGclb());

                if (StringUtil.isNotBlank(dantiInfo.getGcxz())) {
                    if (dantiInfo.getGcxz().contains(",")) {
                        String[] list = dantiInfo.getGcxz().split(",");
                        StringBuilder sb = new StringBuilder();
                        for (String code : list) {
                            String name = codeItemsService.getItemTextByCodeName("工程性质", code);
                            sb.append(name);
                            sb.append(";");
                        }
                        jsonMaterial.put("gcxz", sb.toString().substring(0, sb.toString().length() - 1));
                    }
                    else {
                        jsonMaterial.put("gcxz", codeItemsService.getItemTextByCodeName("工程性质", dantiInfo.getGcxz()));
                    }
                }
                jsonMaterial.put("jzdts", dantiInfoList.size());
                jsonMaterial.put("zjzmjz", zjzmjz);
                jsonMaterial.put("zgjzgd", zgjzgd);
                jsonMaterial.put("zzmj", dantiInfo.getZzmj());
                jsonMaterial.put("gjmj", dantiInfo.getGjmj());
                jsonMaterial.put("dxgjmj", dantiInfo.getDxgjmj());
                jsonMaterial.put("dxckmj", dantiInfo.getDxckmj());
                jsonMaterial.put("dishangmianji", dantiInfo.getDishangmianji());
                jsonMaterial.put("dixiamianji", dantiInfo.getDixiamianji());
                jsonMaterial.put("jzgd", dantiInfo.getJzgd());
                jsonMaterial.put("price", dantiInfo.getPrice());
                jsonMaterial.put("gcguid", dantiInfo.getGongchengguid());
                jsonMaterial.put("rguid", dantiInfo.getStr("rguid"));
                jsonMaterial.put("zjurl", dantiInfo.getStr("zjurl"));
                DwgcInfo dwgcInfo = new DwgcInfo();
                if (StringUtil.isNotBlank(dantiInfo.getGongchengguid())) {
                    dwgcInfo = iDwgcInfoService.find(dantiInfo.getGongchengguid());
                    jsonMaterial.put("gongchengname", dwgcInfo.getGongchengname());
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.putAll(jsonMaterial);
                catdataList.add(map);
            }

            doc.getMailMerge().executeWithRegions(new MapMailMergeDataSource(catdataList, "catlist"));

            fieldNames = new String[fieldMap == null ? 0 : fieldMap.size()];
            values = new Object[fieldMap == null ? 0 : fieldMap.size()];
            int num = 0;

            for (Entry<String, String> entry : fieldMap.entrySet()) {
                fieldNames[num] = entry.getKey();
                values[num] = entry.getValue();
                num++;
            }
            // 替换域

            doc.getMailMerge().execute(fieldNames, values);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.DOC);// 保存成word
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = inputStream.available();
            // 附件信息
            AttachUtil.saveFileInputStream(basicAttachGuid, UUID.randomUUID().toString(), "建设项目开工一件事基本信息.doc", "doc", "", size, inputStream, "", "");
            // 生成文件到指定的位置
            String basicTargetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + basicAttachGuid + ".doc";
            File basicTargetFile = new File(basicTargetPath);
            FileOutputStream basicFops = null;
            try {
                basicFops = new FileOutputStream(basicTargetFile);
                basicFops.write(readInputStream(inputStream));
                basicFops.flush();
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            finally {
                if (basicFops != null) {
                    try {
                        basicFops.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 添加到待合并集合
            List<String> targetFileList = new ArrayList<>();
            // targetFileList.add(basicTargetPath);

            // 合并文件信息
            String megreGuid = UUID.randomUUID().toString();
            String mergeTargetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + megreGuid + "_merge.doc";
            File file1 = new File(mergeTargetPath);

            // 获取表单附件地址
            String savetype = ".doc";
            String rowguid = paramsJson.getString("rowguid");
            String formids = paramsJson.getString("formids");
            List<String> urlList = new ArrayList<String>();

            if (StringUtil.isNotBlank(formids)) {
                String[] arr = formids.split(",");
                for (String formid : arr) {
                    String eformFileUrl = getEformFileUrl(savetype, rowguid, formid);
                    if (StringUtil.isNotBlank(eformFileUrl)) {
                        urlList.add(eformFileUrl);
                    }
                }
            }

            log.info("=======urlList========" + urlList);
            if (!urlList.isEmpty() && urlList.size() > 0) {
                for (String url : urlList) {
                    String[] attachGuid = url.split("attachGuid=");
                    InputStream targetInputStream = handelUrl(url);
                    // 生成文件到指定的位置
                    String targetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + attachGuid[1] + ".doc";
                    File targetFile = new File(targetPath);
                    FileOutputStream fops = null;
                    try {
                        fops = new FileOutputStream(targetFile);
                        fops.write(readInputStream(targetInputStream));
                        fops.flush();
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    finally {
                        if (fops != null) {
                            try {
                                fops.close();
                            }
                            catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                    targetFileList.add(targetPath);
                }
            }

            // 重新写入基本信息文件
            String url = "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + basicAttachGuid;
            InputStream targetInputStream = handelUrl(url);
            // 生成文件到指定的位置
            String targetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/" + basicAttachGuid + ".doc";
            File targetFile = new File(targetPath);
            FileOutputStream fops = null;
            try {
                fops = new FileOutputStream(targetFile);
                fops.write(readInputStream(targetInputStream));
                fops.flush();
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            finally {
                if (fops != null) {
                    try {
                        fops.close();
                    }
                    catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }

            log.info("=======targetFileList========" + targetFileList);
            log.info("=======file1========" + file1);

            if (!targetFileList.isEmpty() && targetFileList.size() > 0) {
                // 合并附件
                MergeAppendDoc(mergeTargetPath, targetPath, targetFileList);
            }
            else {
                return basicAttachGuid;
            }

            file1 = new File(mergeTargetPath);
            InputStream stream = null;
            try {
                // 存储到数据库
                retAttachGuid = UUID.randomUUID().toString();
                stream = new FileInputStream(file1);
                size = stream.available();
                AttachUtil.saveFileInputStream(retAttachGuid, UUID.randomUUID().toString(), "建设项目开工一件事基本信息(总表).doc", "doc", "", size, stream, "", "");


            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            finally {
                if (stream != null) {
                    try {
                        stream.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info("========retAttachGuid=======" + retAttachGuid);
        return retAttachGuid;
    }
}
