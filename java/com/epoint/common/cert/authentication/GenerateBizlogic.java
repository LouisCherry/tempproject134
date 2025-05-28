package com.epoint.common.cert.authentication;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Bookmark;
import com.aspose.words.BookmarkCollection;
import com.aspose.words.CellMerge;
import com.aspose.words.CellVerticalAlignment;
import com.aspose.words.ConvertUtil;
import com.aspose.words.DataSet;
import com.aspose.words.DataTable;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontSettings;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.HorizontalAlignment;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.License;
import com.aspose.words.LineStyle;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeImporter;
import com.aspose.words.NodeType;
import com.aspose.words.PaperSize;
import com.aspose.words.Paragraph;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.RelativeHorizontalPosition;
import com.aspose.words.RelativeVerticalPosition;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.SectionCollection;
import com.aspose.words.SectionStart;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.aspose.words.TextPath;
import com.aspose.words.VerticalAlignment;
import com.aspose.words.WrapType;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certcatalog.certcatalogou.inter.ICertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certcorrection.domain.CertCorrection;
import com.epoint.cert.basic.certcorrection.inter.ICertCorrection;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.certsignatureca.domain.CertSignatureca;
import com.epoint.cert.basic.certsignatureca.inter.ICertSignatureca;
import com.epoint.cert.common.api.IGenerateCert;
import com.epoint.cert.commonutils.AttachUtil;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.FormatUtil;
import com.epoint.cert.commonutils.GenerateUtil;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.QrcodeUtil;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.kgofd.encrypt.Base64;
import com.kgofd.ofd.KGOfdHummer;
import com.kgofd.ofd.executes.OfdElectronicSeal4KG;
import com.kgofd.ofd.signinter.SignatureInterByKey;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;
import com.suwell.ofd.custom.agent.ConvertException;
import com.suwell.ofd.custom.agent.HTTPAgent;
import com.suwell.ofd.custom.wrapper.Const;
import com.suwell.ofd.custom.wrapper.PackException;
import com.suwell.ofd.custom.wrapper.Packet;
import com.suwell.ofd.custom.wrapper.model.CA;
import com.suwell.ofd.custom.wrapper.model.Common;
import com.suwell.ofd.custom.wrapper.model.MarkPosition;
import com.suwell.ofd.custom.wrapper.model.SealImage;
import com.suwell.ofd.custom.wrapper.model.SealInfo;
import com.suwell.ofd.custom.wrapper.model.Template;
import com.suwell.ofd.custom.wrapper.model.TextInfo;

/**
 * 生成证照相关的bizlogic
 *
 * @version [版本号, 2017年11月30日]
 * @作者 dingwei
 */
@SuppressWarnings({"rawtypes", "unchecked" })
public class GenerateBizlogic
{
    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(GenerateBizlogic.class);

    /**
     * 附件操作服务类
     */
    private IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

    /**
     * 证照目录api
     */
    private ICertCatalog iCertCatalog = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);

    /**
     * 元数据api
     */
    private ICertMetaData iCertMetaData = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);

    /**
     * 证照基本信息api
     */
    private ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);

    /**
     * 部门API
     */
    private IOuServiceInternal iOuService = ContainerFactory.getContainInfo().getComponent(IOuServiceInternal.class);

    /**
     * 代码项api
     */
    private ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo()
            .getComponent(ICodeItemsService.class);

    private ICertCatalogOu catalogOuService = ContainerFactory.getContainInfo().getComponent(ICertCatalogOu.class);

    private ICertSignatureca caService = ContainerFactory.getContainInfo().getComponent(ICertSignatureca.class);

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
    private static ExecutorService executorService = new ThreadPoolExecutor(CertConstant.CONSTANT_INT_ONE,
            CertConstant.CONSTANT_INT_ONE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            namedThreadFactory);

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    /**
     * 获得word填充数组
     *
     * @param certMetadataList
     * @param dataBean
     * @return
     */
    public Map<String, Object> getWordInfo(List<CertMetadata> certMetadataList, Record dataBean) {
        Map<String, Object> wordInfo = new HashMap<>();
        // word域名数组
        String[] fieldNames = null;
        List<String> nameList = new ArrayList<>();
        // word域值数组
        Object[] values = null;
        List<String> valueList = new ArrayList<>();
        // 图片map
        Map<String, Record> imageMap = new HashMap<>();
        // 子表表格数据
        DataSet dataSet = new DataSet();

        // fieldNames和values值处理
        if (ValidateUtil.isNotBlankCollection(certMetadataList) && dataBean != null) {
            for (int i = 0; i < certMetadataList.size(); i++) {
                CertMetadata certMetadata = certMetadataList.get(i);
                // 是否有子表结构
                if (CertConstant.CONSTANT_STR_ONE.equals(certMetadata.getIsrelatesubtable())) { // 有子表
                    List<Record> recordList = new ArrayList<>();
                    String subJson = dataBean.getStr(certMetadata.getFieldname());
                    // JSON转list
                    try {
                        recordList = JsonUtil.jsonToList(subJson, Record.class);
                    }
                    catch (Exception e) {
                        log.info(e);
                        recordList = new ArrayList<>();
                    }
                    // 查询子元数据
                    List<CertMetadata> subMetaDataList = iCertMetaData
                            .selectSubDispinListByCertguid(certMetadata.getRowguid());
                    try {
                        if (ValidateUtil.isNotBlankCollection(subMetaDataList)
                                && ValidateUtil.isNotBlankCollection(recordList)) {
                            // 构造dataTable
                            DataTable dataTable = new DataTable(certMetadata.getFieldname());
                            // 创建column
                            List<String> columnList = new ArrayList<>();
                            for (CertMetadata subMetadata : subMetaDataList) {
                                dataTable.getColumns().add(subMetadata.getFieldname());
                                columnList.add(subMetadata.getFieldname());
                                // 如果配置了代码项渲染成代码项文本
                                for (Record record : recordList) {
                                    // 复选框组或单选按钮组 并且为勾选形式
                                    if (("checkboxlist"
                                            .equals(StringUtil.toLowerCase(subMetadata.getFielddisplaytype()))
                                            || "radiobuttonlist"
                                                    .equals(StringUtil.toLowerCase(subMetadata.getFielddisplaytype())))
                                            && CertConstant.CONSTANT_STR_TWO.equals(subMetadata.getCodeformat())) {
                                        if (StringUtil.isNotBlank(subMetadata.getDatasource_codename())) {
                                            List<CodeItems> codeItemsList = iCodeItemsService
                                                    .listCodeItemsByCodeName(subMetadata.getDatasource_codename());
                                            Set<String> value = new HashSet<>();
                                            String itemvalues = record.get(subMetadata.getFieldname());
                                            if (StringUtil.isNotBlank(itemvalues)) {
                                                for (String s : itemvalues.split(",")) {
                                                    value.add(s);
                                                }
                                            }
                                            for (CodeItems codeItems : codeItemsList) {
                                                if (value.contains(codeItems.getItemValue())) {
                                                    record.put(codeItems.getItemValue(), "√");
                                                }
                                                else {
                                                    record.put(codeItems.getItemValue(), "");
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        String itemText = "";
                                        if (StringUtil.isNotBlank(subMetadata.getDatasource_codename())) {
                                            itemText = iCodeItemsService.getItemTextByCodeName(
                                                    subMetadata.getDatasource_codename(),
                                                    record.get(
                                                            StringUtil.toLowerCase(subMetadata.getFieldname())) == null
                                                                    ? ""
                                                                    : record.get(StringUtil
                                                                            .toLowerCase(subMetadata.getFieldname())));
                                        }
                                        if (StringUtil.isNotBlank(itemText)) {
                                            record.put(StringUtil.toLowerCase(subMetadata.getFieldname()), itemText);
                                        }
                                    }
                                }
                            }
                            // 每行数据赋值
                            for (Record record : recordList) {
                                List<Object> rowValueList = new ArrayList<>();
                                for (String column : columnList) {
                                    rowValueList.add(record.get(StringUtil.toLowerCase(column)));
                                }
                                Object[] rowValueArr = new Object[rowValueList.size()];
                                rowValueList.toArray(rowValueArr);
                                dataTable.getRows().add(rowValueArr);
                            }
                            dataSet.getTables().add(dataTable);
                        }
                    }
                    catch (SQLException e) {
                        log.info(e);
                    }
                }
                else { // 无子表
                       // 分离图片类型和普通类型
                    if ("image".equals(StringUtil.toLowerCase(certMetadata.getFieldtype()))) { // 文件类型
                        String cliengguid = dataBean.getStr(certMetadata.getFieldname());
                        int attachCount = attachService.getAttachCountByClientGuid(cliengguid);
                        if (StringUtil.isNotBlank(cliengguid) && attachCount > 0) {
                            List<FrameAttachStorage> frameAttachStorageList = attachService
                                    .getAttachListByGuid(cliengguid);
                            Record imageReocrd = new Record();
                            imageReocrd.put("certmetadata", certMetadata);
                            imageReocrd.put("attachStorage", frameAttachStorageList.get(0));
                            imageMap.put(certMetadata.getFieldchinesename(), imageReocrd);
                        }
                    }
                    else {
                        nameList.add(certMetadata.getFieldchinesename());
                        // 判断是否属于日期类型，如果是日期类型，则进行格式转换
                        if ("datetime".equals(StringUtil.toLowerCase(certMetadata.getFieldtype()))) {
                            String dateStr = dataBean.getStr(certMetadata.getFieldname());
                            if (StringUtil.isNotBlank(dateStr)) {
                                // 先获取用户配置的日期格式
                                String dateformat = certMetadata.getDateFormat();
                                if (dateStr.contains(" ")) {
                                    dateStr = dateStr.split(" ")[0];
                                    Date date = EpointDateUtil.convertString2Date(dateStr.replace("/", "-"),
                                            "yyyy-MM-dd");
                                    if (StringUtil.isNotBlank(dateformat)) {
                                        try {
                                            valueList.add(EpointDateUtil.convertDate2String(date, dateformat));
                                        }
                                        catch (Exception e) {
                                            log.info(e);
                                        }
                                    }
                                    else {
                                        valueList.add(EpointDateUtil.convertDate2String(date, "yyyy年MM月dd日"));
                                    }

                                }
                                else {
                                    valueList.add(dateStr);
                                }
                            }
                            else {
                                valueList.add(null);
                            }
                        }
                        // 复选框组或单选按钮组 并且为勾选形式
                        else if (("checkboxlist".equals(StringUtil.toLowerCase(certMetadata.getFielddisplaytype()))
                                || "radiobuttonlist".equals(StringUtil.toLowerCase(certMetadata.getFielddisplaytype())))
                                && CertConstant.CONSTANT_STR_TWO.equals(certMetadata.getCodeformat())) {
                            if (StringUtil.isNotBlank(certMetadata.getDatasource_codename())) {
                                List<CodeItems> codeItemsList = iCodeItemsService
                                        .listCodeItemsByCodeName(certMetadata.getDatasource_codename());
                                Set<String> value = new HashSet<>();
                                String itemvalues = dataBean.get(certMetadata.getFieldname());
                                if (StringUtil.isNotBlank(itemvalues)) {
                                    for (String s : itemvalues.split(",")) {
                                        value.add(s);
                                    }
                                }
                                nameList.remove(nameList.size() - 1);
                                for (CodeItems codeItems : codeItemsList) {
                                    if (value.contains(codeItems.getItemValue())) {
                                        nameList.add(codeItems.getItemValue());
                                        valueList.add("√");
                                    }
                                    else {
                                        nameList.add(codeItems.getItemValue());
                                        valueList.add("");
                                    }
                                }
                            }
                        }
                        else {
                            // 如果配置了代码项渲染成代码项文本
                            String itemText = "";
                            if (StringUtil.isNotBlank(certMetadata.getDatasource_codename())) {
                                itemText = iCodeItemsService.getItemTextByCodeName(
                                        certMetadata.getDatasource_codename(),
                                        dataBean.get(certMetadata.getFieldname()) == null ? ""
                                                : dataBean.get(certMetadata.getFieldname()));

                            }
                            if (StringUtil.isNotBlank(itemText)) {
                                valueList.add(itemText);
                            }
                            else {
                                valueList.add("" + dataBean.get(certMetadata.getFieldname()));
                            }
                        }
                    }
                }
            }
        }
        fieldNames = new String[nameList.size()];
        nameList.toArray(fieldNames);
        values = new Object[valueList.size()];
        valueList.toArray(values);
        wordInfo.put("fieldNames", fieldNames);
        wordInfo.put("values", values);
        wordInfo.put("imageMap", imageMap);
        wordInfo.put("dataSet", dataSet);
        return wordInfo;
    }

    /**
     * 生成证照（支持正本生成，不支持副本生成）
     *
     * @param templetCliengGuid
     *            证照模板guid
     *            用户Guid
     * @param userDisPlayName
     *            用户名称
     * @param fieldNames
     *            word域
     * @param values
     *            word域值
     * @param imageMap
     *            图片map
     * @param dataSet
     *            子表数据
     * @param certInfo
     *            基本信息对象
     */
    @Deprecated
    public Map<String, String> generateLicense(String templetCliengGuid, String userGuid, String userDisPlayName,
            String[] fieldNames, Object[] values, Map<String, Record> imageMap, DataSet dataSet, CertInfo certInfo) {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        // 生成附件
        if (StringUtil.isNotBlank(templetCliengGuid)) {// 正确配置证照附件
            String certClass = ConfigUtil.getConfigValue("cert_reflectgenratecert");
            if (StringUtil.isNotBlank(certClass) && ReflectUtil.exist(certClass)) {
                if (attachService.getAttachCountByClientGuid(templetCliengGuid) > 0) {
                    IGenerateCert iCertGeneratePdf = (IGenerateCert) ReflectUtil.getObjByClassName(certClass);
                    returnMap = iCertGeneratePdf.generateCert(templetCliengGuid, certInfo.getCertcliengguid(),
                            fieldNames, values, userGuid, userDisPlayName);
                    if (CertConstant.CONSTANT_INT_ZERO.equals(certInfo.getIscreatecert())
                            && attachService.getAttachCountByClientGuid(certInfo.getCertcliengguid()) > 0) {
                        // 更新是否生成证照字段
                        certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
                        iCertInfo.updateCertInfo(certInfo);
                    }
                    return returnMap;
                }
                else {
                    returnMap.put("issuccess", "0");
                    returnMap.put("msg", "当前证照对应的证照类型模版文件为空，请联系管理员维护！");
                    return returnMap;
                }
            }

            // 查询证照附件
            List<FrameAttachStorage> frameAttachStorageList = attachService.getAttachListByGuid(templetCliengGuid);
            if (frameAttachStorageList != null && !frameAttachStorageList.isEmpty()) {
                FrameAttachStorage frameAttachStorage = frameAttachStorageList.get(0);
                if (StringUtil.isBlank(frameAttachStorage.getContent())) {
                    returnMap.put("issuccess", "0");
                    returnMap.put("msg", "当前证照对应的证照类型模版文件内容为空，请联系管理员维护！");
                    return returnMap;
                }

                // 定义存储路径
                String certFilepath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                        + frameAttachStorage.getAttachFileName();
                String pdfFilepath = certFilepath.replaceAll(".docx", ".pdf").replaceAll(".doc", ".pdf");
                String ofdFilepath = certFilepath.replaceAll(".docx", ".ofd").replaceAll(".doc", ".ofd");
                try {
                    // 获取证书文件
                    String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                    License license = new License();
                    license.setLicense(licenseName);
                    FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
                    Document doc = new Document(frameAttachStorage.getContent());

                    // 填充word域值
                    doc.getMailMerge().execute(fieldNames, values);
                    // 子表的渲染
                    doc.getMailMerge().executeWithRegions(dataSet);
                    // 插入图片（包括二维码）
                    String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
                    if (StringUtil.isBlank(qrcodeurl)) {
                        qrcodeurl = certInfo.getCertname();
                    }
                    else {
                        qrcodeurl += "?guid=" + certInfo.getRowguid() + "";
                    }
                    log.info("certInfo.getStr(\"defaultwidth\"):"+certInfo.getStr("defaultwidth"));
                    log.info("certInfo.getStr(\"defaultheight\"):"+certInfo.getStr("defaultheight"));
                    if(StringUtils.isNotBlank(certInfo.getStr("defaultwidth")) && StringUtils.isNotBlank(certInfo.getStr("defaultheight"))){
                        insertImagetoBookmark(doc, imageMap, true, qrcodeurl,Integer.parseInt(certInfo.getStr("defaultwidth")),Integer.parseInt(certInfo.getStr("defaultheight")));
                    }else{
                        insertImagetoBookmark(doc, imageMap, true, qrcodeurl,0,0);
                    }

                    // 添加水印(读取epointframe.properties的配置)
                    String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
                    if (StringUtil.isNotBlank(watermarkText)) {
                        // 转码
                        watermarkText = new String(watermarkText.getBytes("ISO-8859-1"), "UTF-8");
                        // 默认宽度500，高度100
                        int watermarkWidth = GenerateUtil
                                .parseInteger(ConfigUtil.getConfigValue("cert_watermark_width"), 500);
                        int watermarkHeight = GenerateUtil
                                .parseInteger(ConfigUtil.getConfigValue("cert_watermark_height"), 100);
                        insertWatermarkText(doc, watermarkText, watermarkWidth, watermarkHeight);
                    }

                    // 保存成PDF文件
                    doc.save(pdfFilepath, SaveFormat.PDF);
                    // 制作缩略图
                    markSltImage(doc, frameAttachStorage.getAttachFileName(), userGuid, userDisPlayName, certInfo);

                    // 生成ofd格式的附件
                    // 获取ofd转换服务地址
                    String ofdserviceurl = ConfigUtil.getConfigValue("ofdserviceurl");
                    if (StringUtil.isNotBlank(ofdserviceurl)) {
                        HTTPAgent ha = new HTTPAgent(ofdserviceurl);
                        try {
                            // 将单个办公文件（Office文件如doc等、版式文件如pdf、xps、ceb等）转换为OFD文件
                            ha.officeToOFD(FileManagerUtil.createFile(pdfFilepath), new FileOutputStream(ofdFilepath));
                        }
                        catch (Exception e) {
                            log.info(e);
                        }
                        finally {
                            try {
                                ha.close(); // 注意关闭
                            }
                            catch (IOException e) {
                                log.info(e);
                            }
                        }
                    }

                    // 是否生成过附件
                    boolean isGenerate = false;
                    String attachguid = "";
                    List<FrameAttachInfo> frameAttachInfos = attachService
                            .getAttachInfoListByGuid(certInfo.getCertcliengguid());
                    if (ValidateUtil.isNotBlankCollection(frameAttachInfos)) {
                        for (FrameAttachInfo attchInfo : frameAttachInfos) {
                            if ("系统生成".equals(attchInfo.getCliengInfo())) {
                                isGenerate = true;
                                attachguid = attchInfo.getAttachGuid();
                                break;
                            }
                        }
                    }

                    if (isGenerate) { // 更新
                        if (StringUtil.isNotBlank(ofdserviceurl)) {
                            // 生成ofd
                            AttachUtil.updateOfdAttach(certInfo.getCertno() + "-" + certInfo.getCertownername(),
                                    ofdFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成",
                                    attachguid);
                        }
                        else {
                            // 生成pdf
                            AttachUtil.updateAttach(certInfo.getCertno() + "-" + certInfo.getCertownername(),
                                    pdfFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成",
                                    attachguid);
                        }

                    }
                    else { // 上传
                        if (StringUtil.isBlank(certInfo.getCertcliengguid())) {
                            certInfo.setCertcliengguid(UUID.randomUUID().toString());
                        }
                        if (StringUtil.isNotBlank(ofdserviceurl)) {
                            // 生成ofd
                            AttachUtil.addOfdAttach(certInfo.getCertno() + "-" + certInfo.getCertownername(),
                                    ofdFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成");
                        }
                        else {
                            // 生成pdf
                            AttachUtil.addAttach(certInfo.getCertno() + "-" + certInfo.getCertownername(), pdfFilepath,
                                    certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成");
                        }
                    }
                    if (CertConstant.CONSTANT_INT_ZERO.equals(certInfo.getIscreatecert())) {
                        // 更新是否生成证照字段
                        certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
                        iCertInfo.updateCertInfo(certInfo);
                    }
                    returnMap.put("issuccess", "1");
                    returnMap.put("msg", "生成证照成功！");
                }
                catch (Exception e) {
                    log.info(e);
                    returnMap.put("issuccess", "0");
                    returnMap.put("msg", "生成证照失败！");
                }
            }
            else {
                returnMap.put("issuccess", "0");
                returnMap.put("msg", "获取证照样本底图失败，请联系管理员！");
            }
        }
        else {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "未正确配置证照样本底图，请先配置正确的证照样本底图！");
        }
        return returnMap;
    }

    /**
     * 生成证照（支持正副本生成）
     *
     * @param cliengGuid
     *            模板文件（正本或副本）
     * @param isGenerateCopy
     *            是否生成副本
     * @param userGuid
     *            用户Guid
     * @param userDisPlayName
     *            用户名称
     * @param fieldNames
     *            word域
     * @param values
     *            word域值
     * @param imageMap
     *            图片map
     * @param dataSet
     *            子表数据
     * @param certInfo
     *            基本信息对象
     * @return
     */
    public Map<String, String> generateLicenseSupportCopy(String cliengGuid, boolean isGenerateCopy, String userGuid,
            String userDisPlayName, String[] fieldNames, Object[] values, Map<String, Record> imageMap, DataSet dataSet,
            CertInfo certInfo) {
        return generateLicenseSupportCopy(cliengGuid, isGenerateCopy, userGuid, userDisPlayName, fieldNames, values,
                imageMap, dataSet, certInfo, "", "");
    }

    /**
     * 生成证照（支持正副本生成）
     *
     * @param cliengGuid
     *            模板文件（正本或副本）
     * @param isGenerateCopy
     *            是否生成副本
     * @param userGuid
     *            用户Guid
     * @param userDisPlayName
     *            用户名称
     * @param fieldNames
     *            word域
     * @param values
     *            word域值
     * @param imageMap
     *            图片map
     * @param dataSet
     *            子表数据
     * @param certInfo
     *            基本信息对象
     * @return
     */
    public Map<String, String> generateLicenseSupportCopy(String cliengGuid, boolean isGenerateCopy, String userGuid,
            String userDisPlayName, String[] fieldNames, Object[] values, Map<String, Record> imageMap, DataSet dataSet,
            CertInfo certInfo, String keyString, String certData) {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        // 检测模本文件
        Map<String, Object> templetMap = checkCatalogTemplet(cliengGuid, isGenerateCopy);
        if (StringUtil.isNotBlank(templetMap.get("msg"))) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", (String) templetMap.get("msg"));
            return returnMap;
        }
        // 获得模本文件attachStorage
        FrameAttachStorage frameAttachStorage = (FrameAttachStorage) templetMap.get("frameAttachStorage");

        // 生成附件
        String certClass = ConfigUtil.getConfigValue("cert_reflectgenratecert");
        //cert_reflectgenratecert =com.epoint.cert.JNGenerateCert
        // 检查是否配置了实现类地址并且反射实现类源文件是否存在
        log.info("gencert:" + (StringUtil.isNotBlank(certClass) && ReflectUtil.exist(certClass)));
        if (StringUtil.isNotBlank(certClass) && ReflectUtil.exist(certClass)) {
            IGenerateCert iCertGeneratePdf = (IGenerateCert) ReflectUtil.getObjByClassName(certClass);
            returnMap = iCertGeneratePdf.generateCert(cliengGuid, certInfo.getCertcliengguid(), fieldNames, values,
                    userGuid, userDisPlayName, imageMap, dataSet, certInfo, isGenerateCopy);
            if (CertConstant.CONSTANT_INT_ZERO.equals(certInfo.getIscreatecert())
                    && attachService.getAttachCountByClientGuid(certInfo.getCertcliengguid()) > 0) {
                // 更新是否生成证照字段
                certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
                iCertInfo.updateCertInfo(certInfo);
            }
            return returnMap;
        }

        // 检测模板文件是ofd格式还是word格式
        if (frameAttachStorage != null && frameAttachStorage.getAttachFileName() != null
                && frameAttachStorage.getAttachFileName().endsWith("ofd")) {
            // 直接用ofd模板来生成ofd附件
            try {
                List<FrameAttachStorage> frameAttachStorageList = (List<FrameAttachStorage>) templetMap
                        .get("frameAttachStorageList");
                return generateOfdLicenseSupportCopy(frameAttachStorageList, userGuid, userDisPlayName, certInfo,
                        isGenerateCopy, keyString, certData);
            }
            catch (Exception e) {
                log.info(e);
                returnMap.put("issuccess", "0");
                returnMap.put("msg", "生成证照失败！");
                return returnMap;
            }
        }

        // 定义存储路径
        String certFilepath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                + UUID.randomUUID().toString() + frameAttachStorage.getAttachFileName();
        String pdfFilepath = certFilepath.replaceAll(".docx", ".pdf").replaceAll(".doc", ".pdf");
        String ofdFilepath = certFilepath.replaceAll(".docx", ".ofd").replaceAll(".doc", ".ofd");
        try {
            // 获取证书文件
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            License license = new License();
            license.setLicense(licenseName);
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
            Document doc = new Document(frameAttachStorage.getContent());
            // 填充word域值
            doc.getMailMerge().execute(fieldNames, values);
            // 子表的渲染
            doc.getMailMerge().executeWithRegions(dataSet);
            // 插入图片（包括二维码）
            String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
            if (StringUtil.isBlank(qrcodeurl)) {
                qrcodeurl = certInfo.getCertname();
            }
            else {
                qrcodeurl += "?guid=" + certInfo.getRowguid() + "";
            }
            log.info("qrcodeurl1=" + qrcodeurl);
            log.info("qrcode" + certInfo.getStr("qrcode"));
            if ("1".equals(certInfo.getStr("qrcode"))) {
                qrcodeurl = ConfigUtil.getConfigValue("certqrcodeurl");
                qrcodeurl += "&certinfoguid=" + certInfo.getRowguid() + "";
            }
            log.info("qrcodeurl2=" + qrcodeurl);
            log.info("certInfo.getStr(\"defaultwidth\"):"+certInfo.getStr("defaultwidth"));
            log.info("certInfo.getStr(\"defaultheight\"):"+certInfo.getStr("defaultheight"));
            if(StringUtils.isNotBlank(certInfo.getStr("defaultwidth")) && StringUtils.isNotBlank(certInfo.getStr("defaultheight"))){
                insertImagetoBookmark(doc, imageMap, true,qrcodeurl, Integer.parseInt(certInfo.getStr("defaultwidth")),Integer.parseInt(certInfo.getStr("defaultheight")));
            }else{
                insertImagetoBookmark(doc, imageMap, true, qrcodeurl,0,0);
            }
            

            // 水印
            String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
            if (StringUtil.isNotBlank(watermarkText)) {
                watermarkText = new String(watermarkText.getBytes("ISO-8859-1"), "UTF-8");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                doc.save(outputStream, SaveFormat.PDF);
                File file = FileManagerUtil
                        .createFile(ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/");
                // 如果文件夹不存在则创建
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(pdfFilepath);
                PdfReader reader = new PdfReader(outputStream.toByteArray());
                PdfStamper stamper = new PdfStamper(reader, fos);
                int total = reader.getNumberOfPages() + 1;
                PdfContentByte content;
                BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(0.4f);
                for (int i = 1; i < total; i++) {
                    content = stamper.getOverContent(i);// 在内容上方加水印
                    PdfDocument document = content.getPdfDocument();
                    content.setGState(gs);
                    content.beginText();
                    content.setColorFill(BaseColor.GRAY);
                    content.setFontAndSize(base, 20);
                    int width = GenerateUtil.mmToPixel(document.getPageSize().getWidth());
                    int height = GenerateUtil.mmToPixel(document.getPageSize().getHeight());
                    for (int y = 0; y < height; y = y + 250) {
                        for (int x = 0; x < width; x = x + 300) {
                            content.showTextAligned(Element.ALIGN_CENTER, watermarkText, x, y, 25);
                        }
                    }
                    content.endText();
                }
                stamper.close();
                reader.close();
            }
            else {
                doc.save(pdfFilepath, SaveFormat.PDF);
            }

            // 金格pdf签章
            String signatureUrl = ConfigUtil.getConfigValue("kinggridsignatureurl");
            if (StringUtil.isNotBlank(signatureUrl)) {
                String ouguid = null;
                FrameOu frameOu = iOuService.getOuByOuGuid(certInfo.getOuguid());
                if (frameOu != null) {
                    ouguid = certInfo.getOuguid();
                }
                else {
                    //存在一些历史数据是存放oucode
                    List<FrameOuExtendInfo> frameOuExtendInfoList = iOuService.getAllFrameOuExtendInfo();
                    for (int i = 0; i < frameOuExtendInfoList.size(); i++) {
                        if (certInfo.getOuguid().equals(frameOuExtendInfoList.get(i).getStr("orgcode"))) {
                            frameOu = iOuService.getOuByOuGuid(frameOuExtendInfoList.get(i).getOuGuid());
                            if (frameOu != null) {
                                ouguid = frameOu.getOuguid();
                            }
                        }
                    }
                }
                CertCatalogOu certCatalogOu = catalogOuService.getCertCataLogOuByCatalogid("*", false, ouguid,
                        certInfo.getCertcatalogid());
                if (certCatalogOu == null) {
                    returnMap.put("issuccess", "0");
                    returnMap.put("msg", "生成证照失败，部门没有证照类型的权限！");
                    return returnMap;
                }
                CertSignatureca ca = null;
                ca = caService.getSignaturecaByRowguid(certCatalogOu.getCaguid());
                if (ca != null) {
                    String outFilepath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                            + UUID.randomUUID().toString() + ".pdf";
                    PdfReader reader = new PdfReader(pdfFilepath);
                    float x = reader.getPageSize(1).getWidth();
                    String msg = kinggridSignature(pdfFilepath, outFilepath,
                            ClassPathUtil.getClassesPath() + "kinggrid/iSignature.key",
                            ClassPathUtil.getClassesPath() + "kinggrid/" + ca.getCaname(), "", x - 130, 130f,
                            signatureUrl, ca);
                    reader.close();
                    if (StringUtil.isNotBlank(msg)) {
                        // 删除pdf
                        File file = FileManagerUtil.createFile(outFilepath);
                        // 路径为文件且不为空则进行删除
                        if (file.isFile() && file.exists()) {
                            file.delete();
                        }
                        file = FileManagerUtil.createFile(pdfFilepath);
                        // 路径为文件且不为空则进行删除
                        if (file.isFile() && file.exists()) {
                            file.delete();
                        }

                        returnMap.put("issuccess", "0");
                        returnMap.put("msg", msg);
                        return returnMap;
                    }
                    pdfFilepath = outFilepath;
                }
                else {
                    returnMap.put("issuccess", "0");
                    returnMap.put("msg", "生成证照失败，证照目录未配置签章！");
                    return returnMap;
                }
            }

            // 生成ofd格式的附件
            // 获取ofd转换服务地址
            String ofdserviceurl = ConfigUtil.getConfigValue("ofdserviceurl");
            String doctempletformat = ConfigUtil.getConfigValue("doctempletformat");
            if (StringUtil.isBlank(doctempletformat) && StringUtil.isNotBlank(ofdserviceurl)) {
                doctempletformat = "ofd";
            }
            if (StringUtil.isNotBlank(ofdserviceurl) && "ofd".equals(doctempletformat)) {
                HTTPAgent ha = new HTTPAgent(ofdserviceurl);
                FileOutputStream fileOutputStream = null;
                try {
                    // 是否自动签章
                    String ofdautoseal = ConfigUtil.getConfigValue("ofdautoseal");
                    if (CertConstant.CONSTANT_STR_ONE.equals(ofdautoseal)) {
                        Packet packet = Packet.common();
                        packet.file(new Common("test", "pdf",
                                new FileInputStream(FileManagerUtil.createFile(pdfFilepath))));
                        File pfx = FileManagerUtil.createFile(ClassPathUtil.getClassesPath() + "seal/my.p12");
                        File cer = FileManagerUtil.createFile(ClassPathUtil.getClassesPath() + "seal/my.cer");
                        CA ca = new CA("MD5", "SHA1withRSA", new FileInputStream(pfx), "123456",
                                new FileInputStream(cer));
                        SealImage image = new SealImage("002",
                                new FileInputStream(
                                        FileManagerUtil.createFile(ClassPathUtil.getClassesPath() + "seal/seal.png")),
                                40, 40);
                        SealInfo info = new SealInfo(0, "002", 20, 20, 30, 30);
                        packet.seal(ca, image, info);
                        fileOutputStream = new FileOutputStream(ofdFilepath);
                        ha.convert(packet, fileOutputStream);
                    }
                    else {
                        // 将单个办公文件（Office文件如doc等、版式文件如pdf、xps、ceb等）转换为OFD文件
                        fileOutputStream = new FileOutputStream(ofdFilepath);
                        ha.officeToOFD(FileManagerUtil.createFile(pdfFilepath), fileOutputStream);
                    }
                }
                catch (Exception e) {
                    log.info(e);
                    returnMap.put("issuccess", "0");
                    returnMap.put("msg", "生成证照失败！");
                    File file = FileManagerUtil.createFile(pdfFilepath);
                    // 路径为文件且不为空则进行删除
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    file = FileManagerUtil.createFile(ofdFilepath);
                    // 路径为文件且不为空则进行删除
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                    return returnMap;
                }
                finally {
                    try {
                        ha.close(); // 注意关闭
                    }
                    catch (IOException e) {
                        log.info(e);
                    }
                }
            }

            EpointFrameDsManager.begin(null);
            if (!isGenerateCopy) { // 生成正本
                // 是否生成过附件（正本）
                boolean isGenerate = false;
                String attachguid = "";
                List<FrameAttachInfo> frameAttachInfos = attachService
                        .getAttachInfoListByGuid(certInfo.getCertcliengguid());
                if (ValidateUtil.isNotBlankCollection(frameAttachInfos)) {
                    for (FrameAttachInfo attchInfo : frameAttachInfos) {
                        if ("系统生成".equals(attchInfo.getCliengInfo())) {
                            isGenerate = true;
                            attachguid = attchInfo.getAttachGuid();
                            break;
                        }
                    }
                }

                if (isGenerate) { // 更新
                    if (StringUtil.isNotBlank(ofdserviceurl) && "ofd".equals(doctempletformat)) {
                        // 生成ofd
                        AttachUtil.updateOfdAttachNotDelPdf(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0),
                                ofdFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成",
                                attachguid);
                    }
                    else {
                        // 生成pdf
                        AttachUtil.updateAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0),
                                pdfFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成",
                                attachguid);
                    }

                }
                else { // 上传
                    if (StringUtil.isBlank(certInfo.getCertcliengguid())) {
                        certInfo.setCertcliengguid(UUID.randomUUID().toString());
                    }
                    if (StringUtil.isNotBlank(ofdserviceurl) && "ofd".equals(doctempletformat)) {
                        // 生成ofd
                        AttachUtil.addOfdAttachNotDelPdf(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0),
                                ofdFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成");
                    }
                    else {
                        // 生成pdf
                        AttachUtil.addAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0),
                                pdfFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成");
                    }
                }
                if (CertConstant.CONSTANT_INT_ZERO.equals(certInfo.getIscreatecert())) {
                    // 更新是否生成证照字段
                    certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
                    iCertInfo.updateCertInfo(certInfo);
                }
            }
            else { // 生成副本
                   // 是否生成过附件（副本）
                boolean isCopyGenerate = false;
                String attachguid = "";
                // 生成附件（副本）cliengguid初始化
                if (StringUtil.isBlank(certInfo.getCopycertcliengguid())) {
                    certInfo.setCopycertcliengguid(UUID.randomUUID().toString());
                    iCertInfo.updateCertInfo(certInfo);
                }
                else {
                    List<FrameAttachInfo> frameAttachInfoList = attachService
                            .getAttachInfoListByGuid(certInfo.getCopycertcliengguid());
                    if (ValidateUtil.isNotBlankCollection(frameAttachInfoList)) {
                        for (FrameAttachInfo attchInfo : frameAttachInfoList) {
                            if ("系统生成（副本）".equals(attchInfo.getCliengInfo())) {
                                isCopyGenerate = true;
                                attachguid = attchInfo.getAttachGuid();
                                break;
                            }
                        }
                    }
                }

                if (isCopyGenerate) { // 更新
                    if (StringUtil.isNotBlank(ofdserviceurl) && "ofd".equals(doctempletformat)) {
                        // 生成ofd
                        AttachUtil.updateOfdAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0)
                                        + "（副本）",
                                ofdFilepath, certInfo.getCopycertcliengguid(), userGuid, userDisPlayName, "系统生成（副本）",
                                attachguid);
                    }
                    else {
                        // 生成pdf
                        AttachUtil.updateAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0)
                                        + "（副本）",
                                pdfFilepath, certInfo.getCopycertcliengguid(), userGuid, userDisPlayName, "系统生成（副本）",
                                attachguid);

                    }
                }
                else { // 上传
                    if (StringUtil.isNotBlank(ofdserviceurl) && "ofd".equals(doctempletformat)) {
                        // 生成ofd
                        AttachUtil.addOfdAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0)
                                        + "（副本）",
                                ofdFilepath, certInfo.getCopycertcliengguid(), userGuid, userDisPlayName, "系统生成（副本）");
                    }
                    else {
                        // 生成pdf
                        AttachUtil.addAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0)
                                        + "（副本）",
                                pdfFilepath, certInfo.getCopycertcliengguid(), userGuid, userDisPlayName, "系统生成（副本）");
                    }
                }
            }
            EpointFrameDsManager.commit();
            EpointFrameDsManager.close();

            if (StringUtil.isBlank(userGuid) || !userGuid.contains("job批量生成")) {
                // 正本生成缩略图
                if (!isGenerateCopy) {
                    String cliengguid = StringUtil.isNotBlank(ConfigUtil.getConfigValue("ofdserviceurl"))
                            ? UUID.randomUUID().toString()
                            : certInfo.getCertcliengguid();
                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("ofdserviceurl"))
                            && "ofd".equals(doctempletformat)) {
                        EpointFrameDsManager.begin(null);
                        // 生成pdf
                        AttachUtil.addAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                                certInfo.getCertcatalogid(), 0),
                                pdfFilepath, cliengguid, userGuid, userDisPlayName, "");
                        EpointFrameDsManager.commit();
                    }
                    // insertCertinfo接口调用需要返回缩略图guid，不能异步
                    if ("insertCertInfo".equals(userGuid)) {
                        String sltimagecliengguid = UUID.randomUUID().toString();
                        certInfo.setSltimagecliengguid(sltimagecliengguid);
                        // 制作缩略图
                        markPdfImage(cliengguid, sltimagecliengguid, certInfo.getRowguid(), userGuid, userDisPlayName);
                    }
                    else {
                        executorService.execute(new Runnable()
                        {
                            @Override
                            public void run() {
                                EpointFrameDsManager.begin(null);
                                // 制作缩略图
                                // markSltImage(doc, "缩略图", userGuid,
                                // userDisPlayName,certInfo);
                                markPdfImage(cliengguid, certInfo.getSltimagecliengguid(), certInfo.getRowguid(),
                                        userGuid, userDisPlayName);
                                EpointFrameDsManager.commit();
                                EpointFrameDsManager.close();
                            }
                        });
                    }
                }
            }

            returnMap.put("issuccess", "1");
            returnMap.put("msg", "生成证照成功！");
        }
        catch (Exception e) {
            log.info(e);
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        }
        finally {
            File file = FileManagerUtil.createFile(pdfFilepath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
        return returnMap;
    }

    public void markPdfImage(String cliengguid, String sltImagecliengguid, String rowguid, String userGuid,
            String userDisplayName) {
        List<FrameAttachStorage> attachStorages = attachService.getAttachListByGuid(cliengguid);
        if (!attachStorages.isEmpty()) {
            PdfReader reader = null;
            try {
                reader = new PdfReader(attachService.getAttachListByGuid(cliengguid).get(0).getContent());
                PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(reader);

                List<Integer> statis = new ArrayList<>();
                pdfReaderContentParser.processContent(1, new RenderListener()
                {
                    int i = 1;

                    @Override
                    public void beginTextBlock() {

                    }

                    @Override
                    public void renderText(TextRenderInfo textRenderInfo) {

                    }

                    @Override
                    public void endTextBlock() {

                    }

                    @Override
                    public void renderImage(ImageRenderInfo imageRenderInfo) {
                        // 获取图片坐标、大小信息
                        // 只处理第一张图片
                        if (i == 1) {
                            PdfImageObject image;
                            try {
                                image = imageRenderInfo.getImage();
                                if (image != null) {
                                    float[] resu = new float[3];
                                    // 0 => x; 1 => y; 2 => z
                                    // z的值始终为1
                                    resu[0] = imageRenderInfo.getStartPoint().get(0);
                                    resu[1] = imageRenderInfo.getStartPoint().get(1);
                                    statis.add((int) resu[0]); // 起始x轴坐标
                                    statis.add((int) resu[1]); // 起始y轴坐标
                                    statis.add((int) imageRenderInfo.getImageCTM().get(0)); // 图片长
                                    statis.add((int) imageRenderInfo.getImageCTM().get(4)); // 图片高
                                    i++;
                                }
                            }
                            catch (IOException e) {
                                log.info(e);
                            }
                        }
                    }
                });

                PDFFile pdffile = new PDFFile(ByteBuffer
                        .wrap(IOUtils.toByteArray(attachService.getAttachListByGuid(cliengguid).get(0).getContent())));
                PDFPage page = pdffile.getPage(1);
                int width = (int) page.getBBox().getWidth();
                int height = (int) page.getBBox().getHeight();
                int imgWidth = !statis.isEmpty() ? statis.get(2) : width;
                int imgHeight = !statis.isEmpty() ? statis.get(3) : height;
                BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = img.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                PDFRenderer renderer = new PDFRenderer(page, g2,
                        new Rectangle(!statis.isEmpty() ? -statis.get(0) : 0,
                                !statis.isEmpty() ? -(height - statis.get(1) - imgHeight) : 0, width, height),
                        null, null);
                try {
                    page.waitForFinish();
                }
                catch (Exception e) {
                    log.info(e);
                }
                renderer.run();
                g2.dispose();

                CertInfo certInfo = iCertInfo.getCertInfoByRowguid(rowguid);
                if (certInfo == null) {
                    // 为空从纠错表查
                    ICertCorrection correctionService = ContainerFactory.getContainInfo()
                            .getComponent(ICertCorrection.class);
                    CertCorrection certCorrection = correctionService.getCertCorrectionByRowGuid(rowguid);
                    if (certCorrection != null) {
                        certInfo = certCorrection.toEntity(CertInfo.class);
                    }
                }
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", bs);
                InputStream inputStream = new ByteArrayInputStream(bs.toByteArray());
                // 没有缩略图
                if (StringUtil.isBlank(sltImagecliengguid)
                        || attachService.getAttachCountByClientGuid(sltImagecliengguid) == 0) {
                    if (StringUtil.isBlank(sltImagecliengguid)) {
                        sltImagecliengguid = UUID.randomUUID().toString();
                    }
                    AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), sltImagecliengguid,
                            certInfo.getCertno() + "-"
                                    + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                            certInfo.getCertcatalogid(), 0)
                                    + "-" + System.currentTimeMillis() + "-缩略图.jpg",
                            "jpg", "缩略图", inputStream.available(), inputStream, userGuid, userDisplayName);
                    certInfo.setSltimagecliengguid(sltImagecliengguid);
                    iCertInfo.updateCertInfo(certInfo);
                }
                else { // 有缩略图
                    FrameAttachInfo frameAttachInfo = attachService.getAttachInfoListByGuid(sltImagecliengguid).get(0);
                    if (frameAttachInfo != null) {
                        // 先删除物理关系
                        attachService.deleteAttachByAttachGuid(frameAttachInfo.getAttachGuid());
                    }
                    // 重新上传
                    AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), sltImagecliengguid,
                            certInfo.getCertno() + "-"
                                    + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                            certInfo.getCertcatalogid(), 0)
                                    + "-" + System.currentTimeMillis() + "-缩略图.jpg",
                            "jpg", "缩略图", inputStream.available(), inputStream, userGuid, userDisplayName);
                }
                bs.close();
                reader.close();
                inputStream.close();
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("ofdserviceurl"))) {
                    attachService.deleteAttachByGuid(cliengguid);
                }
            }
            catch (Exception e) {
                log.info(e);
            }
        }
    }

    public String kinggridSignature(String inFile, String outFile, String keyPath, String certPath, String text,
            float x, float y, String signatureUrl, CertSignatureca ca) {
        String msg = "";
        FileOutputStream fileOutputStream = null;
        FileInputStream cert = null;
        Object hummer = null;
        Class<?> userClazz = null;
        String password = ca.getPassword();
        String capassword = ca.getCapassword();
        String castorepassword = ca.getCastorepassword();
        // 所有密码默认都为123456
        if (StringUtil.isBlank(password)) {
            password = "123456";
        }
        if (StringUtil.isBlank(capassword)) {
            capassword = "123456";
        }
        if (StringUtil.isBlank(castorepassword)) {
            castorepassword = "123456";
        }
        try {
            @SuppressWarnings("resource")
            KinggridClassLoader classLoader = new KinggridClassLoader();
            userClazz = classLoader.loadClass("com.kinggrid.pdf.KGPdfHummer");
            fileOutputStream = new FileOutputStream(outFile);
            Method createSignature = userClazz.getMethod("createSignature", String.class, byte[].class, boolean.class,
                    OutputStream.class, File.class, boolean.class);
            hummer = createSignature.invoke(null, inFile, null, true, fileOutputStream, null, true);
            cert = new FileInputStream(certPath);
            Method setCertificate = userClazz.getMethod("setCertificate", InputStream.class, String.class,
                    String.class);
            setCertificate.invoke(hummer, cert, castorepassword, capassword);
            Class<?> typeClazz = classLoader.loadClass("com.kinggrid.kgcore.enmu.KGServerTypeEnum");
            Class<?> signClazz = classLoader.loadClass("com.kinggrid.pdf.executes.PdfSignature4KG");
            // 服务器签章
            Constructor c = signClazz.getConstructor(String.class, typeClazz, String.class, String.class, String.class);
            Object pdfSignature4KG = c.newInstance(signatureUrl, typeClazz.getEnumConstants()[5], ca.getKeysn(),
                    password, ca.getSignname());
            // 本地签章
            // Constructor c = signClazz.getConstructor(String.class,
            // int.class,String.class);
            // Object pdfSignature4KG =c.newInstance(keyPath,0,"123456");

            // 文本定位
            if (text != null) {
                Method setText = signClazz.getMethod("setText", String.class);
                setText.invoke(pdfSignature4KG, text);
            }

            // 坐标定位
            if (x != 0 && y != 0) {
                Method setXY = signClazz.getMethod("setXY", float.class, float.class);
                setXY.invoke(pdfSignature4KG, x, y);// 坐标定位
            }
            Method setPagen = signClazz.getMethod("setPagen", int[].class);
            setPagen.invoke(pdfSignature4KG, new int[] {1 });// 局部 默认为全部页面

            Class<?> pdfSignatureClazz = classLoader.loadClass("com.kinggrid.pdf.executes.PdfSignature");
            Method setPdfSignature = userClazz.getMethod("setPdfSignature", pdfSignatureClazz);
            setPdfSignature.invoke(hummer, pdfSignature4KG);

            Method doSignature = userClazz.getMethod("doSignature");
            doSignature.invoke(hummer);
        }
        catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                Throwable targetEx = ((InvocationTargetException) e).getTargetException();
                if (targetEx != null) {
                    msg = targetEx.getMessage();
                    System.out.println(msg);
                }
            }
            else {
                log.info(e);
            }
        }
        finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (cert != null) {
                    cert.close();
                }
            }
            catch (IOException e) {
                log.info(e);
            }
            if (hummer != null) {
                try {
                    Method close = userClazz.getMethod("close");
                    close.invoke(hummer);
                }
                catch (Exception e) {
                    log.info(e);
                }
            }
            // 删除原pdf
            File file = FileManagerUtil.createFile(inFile);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
        return msg;
    }

    public void kinggridOfd(String keyString, String certData, String inPath, String outPath) {
        KGOfdHummer hummer = null;
        Base64 base64 = Base64.of();
        try {
            hummer = KGOfdHummer.createInstance(inPath, null, outPath);
            OfdElectronicSeal4KG ofdElectronicSeal4KG = new OfdElectronicSeal4KG();
            ofdElectronicSeal4KG.setSealMsg(base64.decode(keyString));
            SignatureInterByKey signatureInterByKey = new SignatureInterByKey(certData, ofdElectronicSeal4KG);
            ofdElectronicSeal4KG.setInfomation(signatureInterByKey);
            ofdElectronicSeal4KG.setXY(10, 10);
            // 文本定位
            // ofdElectronicSeal4KG.setText("PKI", true);
            ofdElectronicSeal4KG.setPagen(1);
            hummer.addExecute(ofdElectronicSeal4KG);
            hummer.doExecute();
            hummer.doSignature(ofdElectronicSeal4KG.getToSignData(), ofdElectronicSeal4KG.getSignDirPath(), outPath);

        }
        catch (Exception e) {
            log.info(e);
        }
    }

    /**
     * 生成证明
     *
     *            用户名称
     * @param fieldNames
     *            word域
     * @param values
     *            word域值
     * @return
     */
    public Map<String, Object> generateCertification(CertCatalog certCatalog, String[] fieldNames, Object[] values) {
        // 返回值Map
        Map<String, Object> returnMap = new HashMap<String, Object>();
        // 检测模本文件
        Map<String, Object> templetMap = checkCatalogTemplet(certCatalog.getTempletcliengguid(), false);
        if (StringUtil.isNotBlank(templetMap.get("msg"))) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", (String) templetMap.get("msg"));
            return returnMap;
        }
        // 获得模本文件attachStorage
        FrameAttachStorage frameAttachStorage = (FrameAttachStorage) templetMap.get("frameAttachStorage");

        // 生成附件
        /*
         * String certClass =
         * ConfigUtil.getConfigValue("cert_reflectgenratecert");
         * // 检查是否配置了实现类地址并且反射实现类源文件是否存在
         * if (StringUtil.isNotBlank(certClass) && ReflectUtil.exist(certClass))
         * {
         * IGenerateCert iCertGeneratePdf = (IGenerateCert)
         * ReflectUtil.getObjByClassName(certClass);
         * returnMap = iCertGeneratePdf.generateCert(cliengGuid,
         * certInfo.getCertcliengguid(), fieldNames, values,
         * userGuid, userDisPlayName, imageMap, dataSet, certInfo,
         * isGenerateCopy);
         * if (CertConstant.CONSTANT_INT_ZERO.equals(certInfo.getIscreatecert())
         * &&
         * attachService.getAttachCountByClientGuid(certInfo.getCertcliengguid()
         * ) > 0) {
         * // 更新是否生成证照字段
         * certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
         * iCertInfo.updateCertInfo(certInfo);
         * }
         * return returnMap;
         * }
         */

        /*
         * // 定义存储路径
         * String certFilepath = ClassPathUtil.getDeployWarPath() +
         * "epointcert/resultfile/zhengzhao/"
         * + UUID.randomUUID().toString() +
         * frameAttachStorage.getAttachFileName();
         * String pdfFilepath = certFilepath.replaceAll(".docx",
         * ".pdf").replaceAll(".doc", ".pdf");
         * String ofdFilepath = certFilepath.replaceAll(".docx",
         * ".ofd").replaceAll(".doc", ".ofd");
         */
        try {
            // 获取证书文件
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            License license = new License();
            license.setLicense(licenseName);
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
            Document doc = new Document(frameAttachStorage.getContent());

            // 填充word域值
            doc.getMailMerge().execute(fieldNames, values);
            // 子表的渲染
            // doc.getMailMerge().executeWithRegions(dataSet);
            // 插入图片（包括二维码）
            /*
             * String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
             * if (StringUtil.isBlank(qrcodeurl)) {
             * qrcodeurl = certInfo.getCertname();
             * }
             * else {
             * qrcodeurl += "?guid=" + certInfo.getRowguid() + "";
             * }
             * insertImagetoBookmark(doc, imageMap, true, qrcodeurl);
             */

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            // 水印
            String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
            if (StringUtil.isNotBlank(watermarkText)) {
                watermarkText = new String(watermarkText.getBytes("ISO-8859-1"), "UTF-8");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                doc.save(outputStream, SaveFormat.PDF);
                File file = FileManagerUtil
                        .createFile(ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/");
                // 如果文件夹不存在则创建
                if (!file.exists()) {
                    file.mkdirs();
                }
                PdfReader reader = new PdfReader(outputStream.toByteArray());
                PdfStamper stamper = new PdfStamper(reader, bao);
                int total = reader.getNumberOfPages() + 1;
                PdfContentByte content;
                BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(0.4f);
                for (int i = 1; i < total; i++) {
                    content = stamper.getOverContent(i);// 在内容上方加水印
                    PdfDocument document = content.getPdfDocument();
                    content.setGState(gs);
                    content.beginText();
                    content.setColorFill(BaseColor.GRAY);
                    content.setFontAndSize(base, 20);
                    int width = GenerateUtil.mmToPixel(document.getPageSize().getWidth());
                    int height = GenerateUtil.mmToPixel(document.getPageSize().getHeight());
                    for (int y = 0; y < height; y = y + 250) {
                        for (int x = 0; x < width; x = x + 300) {
                            content.showTextAligned(Element.ALIGN_CENTER, watermarkText, x, y, 25);
                        }
                    }
                    content.endText();
                }
                stamper.close();
                reader.close();
            }
            else {
                doc.save(bao, SaveFormat.PDF);
            }

            returnMap.put("issuccess", "1");
            returnMap.put("msg", "生成证照成功！");
            returnMap.put("outputstream", bao);
        }
        catch (Exception e) {
            log.info(e);
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        }
        return returnMap;
    }

    /**
     * 生成证照(元数据预览)
     *
     * @param cliengGuid
     *            证照模板guid（正本或副本）
     * @param isGenerateCopy
     *            是否生成副本
     * @param userDisPlayName
     *            用户名称
     * @param fieldNames
     *            word域
     * @param values
     *            word域值
     * @param imageMap
     *            图片map
     * @param dataSet
     *            子表数据
     * @param demoExtension
     *            演示拓展信息
     */
    public Map<String, String> generateDemoLicense(String cliengGuid, boolean isGenerateCopy, String userGuid,
            String userDisPlayName, String[] fieldNames, Object[] values, Map<String, Record> imageMap, DataSet dataSet,
            Record demoExtension) {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        // 检测模本文件
        Map<String, Object> templetMap = checkCatalogTemplet(cliengGuid, isGenerateCopy);
        if (StringUtil.isNotBlank(templetMap.get("msg"))) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", (String) templetMap.get("msg"));
            return returnMap;
        }
        // 获得模本文件attachStorage
        FrameAttachStorage frameAttachStorage = (FrameAttachStorage) templetMap.get("frameAttachStorage");

        // 检测模板文件是ofd格式还是word格式
        if (frameAttachStorage != null && frameAttachStorage.getAttachFileName() != null
                && frameAttachStorage.getAttachFileName().endsWith("ofd")) {
            // 直接用ofd模板来生成ofd附件
            try {
                List<FrameAttachStorage> frameAttachStorageList = (List<FrameAttachStorage>) templetMap
                        .get("frameAttachStorageList");
                return generateOfdDemoLicense(frameAttachStorageList, userGuid, userDisPlayName, demoExtension,
                        isGenerateCopy);
            }
            catch (Exception e) {
                log.info(e);
                returnMap.put("issuccess", "0");
                returnMap.put("msg", "生成证照失败！");
                return returnMap;
            }
        }

        // 定义存储路径
        String certFilepath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                + frameAttachStorage.getAttachFileName();
        String pdfFilepath = certFilepath.replaceAll(".docx", ".pdf").replaceAll(".doc", ".pdf");
        String ofdFilepath = certFilepath.replaceAll(".docx", ".ofd").replaceAll(".doc", ".ofd");
        try {
            // 获取证书文件
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            License license = new License();
            license.setLicense(licenseName);
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
            Document doc = new Document(frameAttachStorage.getContent());

            // 填充word域值
            doc.getMailMerge().execute(fieldNames, values);
            // 子表的渲染
            doc.getMailMerge().executeWithRegions(dataSet);
            // 插入图片（包括二维码）
            String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
            log.info("certInfo.getStr(\"defaultwidth\"):"+0);
            insertImagetoBookmark(doc, imageMap, true, qrcodeurl,0,0);

            // 添加水印(读取epointframe.properties的配置)
            String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
            if (StringUtil.isNotBlank(watermarkText)) {
                // 转码
                watermarkText = new String(watermarkText.getBytes("ISO-8859-1"), "UTF-8");
                // 默认宽度500，高度100
                int watermarkWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_watermark_width"), 500);
                int watermarkHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_watermark_height"),
                        100);
                insertWatermarkText(doc, watermarkText, watermarkWidth, watermarkHeight);
            }

            // 保存成PDF文件
            doc.save(pdfFilepath, SaveFormat.PDF);

            // 生成ofd格式的附件
            // 获取ofd转换服务地址
            String ofdserviceurl = ConfigUtil.getConfigValue("ofdserviceurl");
            if (StringUtil.isNotBlank(ofdserviceurl)) {
                HTTPAgent ha = new HTTPAgent(ofdserviceurl);
                try {
                    // 将单个办公文件（Office文件如doc等、版式文件如pdf、xps、ceb等）转换为OFD文件
                    ha.officeToOFD(FileManagerUtil.createFile(pdfFilepath), new FileOutputStream(ofdFilepath));
                }
                catch (Exception e) {
                    log.info(e);
                }
                finally {
                    try {
                        ha.close(); // 注意关闭
                    }
                    catch (IOException e) {
                        log.info(e);
                    }
                }
            }

            if (!isGenerateCopy) { // 生成正本
                // 上传或更新证照附件（正本）
                boolean isUpdate = false;
                String certcliengguid = demoExtension.getStr("certcliengguid");
                String attachGuid = "";
                if (StringUtil.isBlank(certcliengguid)) { // 上传
                    certcliengguid = UUID.randomUUID().toString();
                }
                else {
                    List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(certcliengguid);
                    if (ValidateUtil.isNotBlankCollection(attachInfoList)) { // 没有对应的附件,上传
                        for (FrameAttachInfo info : attachInfoList) {
                            if ("系统生成".equals(info.getCliengInfo())) {
                                isUpdate = true;
                                attachGuid = info.getAttachGuid();
                                break;
                            }
                        }
                    }
                }

                if (isUpdate) { // 更新附件
                    if (StringUtil.isNotBlank(ofdserviceurl)) {
                        // 生成ofd
                        AttachUtil.updateOfdAttach("证照附件", ofdFilepath, certcliengguid, userGuid, userDisPlayName,
                                "系统生成", attachGuid);
                    }
                    else {
                        // 生成pdf
                        AttachUtil.updateAttach("证照附件", pdfFilepath, certcliengguid, userGuid, userDisPlayName, "系统生成",
                                attachGuid);
                    }
                }
                else { // 上传附件
                    if (StringUtil.isNotBlank(ofdserviceurl)) {
                        // 生成ofd
                        AttachUtil.addOfdAttach("证照附件", ofdFilepath, certcliengguid, userGuid, userDisPlayName, "系统生成");
                    }
                    else {
                        // 生成pdf
                        AttachUtil.addAttach("证照附件", pdfFilepath, certcliengguid, userGuid, userDisPlayName, "系统生成");
                    }
                    // 更新demoExtension
                    demoExtension.set("certcliengguid", certcliengguid);
                    demoExtension.setSql_TableName(CertConstant.SQL_TABLE_DEMO_EXTENSION);
                    noSQLSevice.update(demoExtension);
                }
            }
            else { // 生成副本
                   // 上传或更新证照附件（副本）
                boolean isUpdate = false;
                String copycertcliengguid = demoExtension.getStr("copycertcliengguid");
                String attachGuid = "";
                if (StringUtil.isBlank(copycertcliengguid)) { // 上传
                    copycertcliengguid = UUID.randomUUID().toString();
                }
                else {
                    List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(copycertcliengguid);
                    if (ValidateUtil.isNotBlankCollection(attachInfoList)) { // 没有对应的附件,上传
                        for (FrameAttachInfo info : attachInfoList) {
                            if ("系统生成（副本）".equals(info.getCliengInfo())) {
                                isUpdate = true;
                                attachGuid = info.getAttachGuid();
                                break;
                            }
                        }
                    }
                }

                if (isUpdate) { // 更新附件
                    if (StringUtil.isNotBlank(ofdserviceurl)) {
                        // 生成ofd
                        AttachUtil.updateOfdAttach("证照附件（副本）", ofdFilepath, copycertcliengguid, userGuid,
                                userDisPlayName, "系统生成（副本）", attachGuid);
                    }
                    else {
                        // 生成pdf
                        AttachUtil.updateAttach("证照附件（副本））", pdfFilepath, copycertcliengguid, userGuid, userDisPlayName,
                                "系统生成（副本）", attachGuid);
                    }
                }
                else { // 上传附件
                    if (StringUtil.isNotBlank(ofdserviceurl)) {
                        // 生成ofd
                        AttachUtil.addOfdAttach("证照附件（副本）", ofdFilepath, copycertcliengguid, userGuid, userDisPlayName,
                                "系统生成（副本）");
                    }
                    else {
                        // 生成pdf
                        AttachUtil.addAttach("证照附件（副本）", pdfFilepath, copycertcliengguid, userGuid, userDisPlayName,
                                "系统生成（副本）");
                    }
                    // 更新demoExtension
                    demoExtension.set("copycertcliengguid", copycertcliengguid);
                    demoExtension.setSql_TableName(CertConstant.SQL_TABLE_DEMO_EXTENSION);
                    noSQLSevice.update(demoExtension);
                }
            }
            returnMap.put("issuccess", "1");
            returnMap.put("msg", "生成证照成功！");
        }
        catch (Exception e) {
            log.info(e);
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        }
        return returnMap;
    }

    /**
     * 生成打印的证照(doc)
     *
     * @param templetCliengGuid
     *            证照模板guid
     * @param userGuid
     *            用户Guid
     * @param userDisPlayName
     *            用户名称
     * @param fieldNames
     *            word域
     * @param values
     *            word域值
     * @param imageMap
     *            图片map
     * @param dataSet
     *            子表数据
     * @param certinfo
     *            基本信息
     * @return
     */
    public String generatePrintDoc(String templetCliengGuid, String userGuid, String userDisPlayName,
            String[] fieldNames, Object[] values, Map<String, Record> imageMap, DataSet dataSet, CertInfo certinfo) {
        // 查询证照底图
        String attachguid = CertConstant.BLANK;
        List<FrameAttachStorage> frameAttachStorageList = attachService.getAttachListByGuid(templetCliengGuid);
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            if (ValidateUtil.isNotBlankCollection(frameAttachStorageList)) {
                FrameAttachStorage frameAttachStorage = frameAttachStorageList.get(0);
                if (StringUtil.isBlank(frameAttachStorage.getContent())) {
                    return "当前证照对应的证照类型模版文件内容为空，请联系管理员维护！";
                }
                // 获取证书文件
                String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                License license = new License();
                license.setLicense(licenseName);
                FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
                outputStream = new ByteArrayOutputStream();
                Document doc = new Document(frameAttachStorage.getContent());

                // 填充word域值
                doc.getMailMerge().execute(fieldNames, values);
                // 子表的渲染
                doc.getMailMerge().executeWithRegions(dataSet);
                // 插入图片（包括二维码）
                String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
                if (StringUtil.isBlank(qrcodeurl)) {
                    qrcodeurl = certinfo.getCertname();
                }
                else {
                    qrcodeurl += "?guid=" + certinfo.getRowguid() + "";
                }

                log.info("certInfo.getStr(\"defaultwidth\"):"+certinfo.getStr("defaultwidth"));
                log.info("certInfo.getStr(\"defaultheight\"):"+certinfo.getStr("defaultheight"));
                if(StringUtils.isNotBlank(certinfo.getStr("defaultwidth")) && StringUtils.isNotBlank(certinfo.getStr("defaultheight"))){
                    insertImagetoBookmark(doc, imageMap, true,qrcodeurl, Integer.parseInt(certinfo.getStr("defaultwidth")),Integer.parseInt(certinfo.getStr("defaultheight")));
                }else{
                    insertImagetoBookmark(doc, imageMap, true, qrcodeurl,0,0);
                }
                // 添加水印(读取epointframe.properties的配置)
                String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
                if (StringUtil.isNotBlank(watermarkText)) {
                    // 转码
                    watermarkText = new String(watermarkText.getBytes("ISO-8859-1"), "UTF-8");
                    // 默认宽度500，高度100
                    int watermarkWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_watermark_width"),
                            500);
                    int watermarkHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_watermark_height"),
                            100);
                    insertWatermarkText(doc, watermarkText, watermarkWidth, watermarkHeight);
                }

                // 保存成word
                doc.save(outputStream, SaveFormat.DOC);

                inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                FrameAttachInfo frameAttachInfo = null;
                attachguid = UUID.randomUUID().toString();
                if (StringUtil.isNotBlank(certinfo.getPrintdocguid())) {
                    frameAttachInfo = attachService.getAttachInfoDetail(certinfo.getPrintdocguid());
                    if (frameAttachInfo != null) {
                        attachService.updateAttach(frameAttachInfo, inputStream);
                        attachguid = frameAttachInfo.getAttachGuid();
                    }
                }
                if (frameAttachInfo == null) {
                    long size = inputStream.available();
                    // 实际插入的attachguid不同
                    attachguid = AttachUtil.saveFileInputStream(attachguid, UUID.randomUUID().toString(),
                            frameAttachStorage.getAttachFileName(), ".doc", "证照打印文件", size, inputStream, userGuid,
                            userDisPlayName).getAttachGuid();
                    certinfo.setPrintdocguid(attachguid);
                    iCertInfo.updateCertInfo(certinfo);
                }
            }
        }
        catch (Exception e) {
            log.info(e);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (IOException e) {
                log.info(e);
            }
        }

        return attachguid;
    }

    /**
     * 生成打印的证照doc（支持副本打印）
     *
     * @param cliengGuid
     *            证照模板（正/副本）guid
     * @param isPrintCopy
     *            是否打印副本
     * @param userGuid
     *            用户Guid
     * @param userDisPlayName
     *            用户名称
     * @param fieldNames
     *            word域
     * @param values
     *            word域值
     * @param imageMap
     *            图片map
     * @param dataSet
     *            子表数据
     * @param certinfo
     *            基本信息
     * @return
     */
    public Map<String, String> generatePrintDocSupportCopy(String cliengGuid, Boolean isPrintCopy, String userGuid,
            String userDisPlayName, String[] fieldNames, Object[] values, Map<String, Record> imageMap, DataSet dataSet,
            CertInfo certinfo) {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        // 检测模本文件
        Map<String, Object> templetMap = checkCatalogTemplet(cliengGuid, isPrintCopy);
        if (StringUtil.isNotBlank(templetMap.get("msg"))) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", (String) templetMap.get("msg"));
            return returnMap;
        }
        // 获得模本文件attachStorage
        FrameAttachStorage frameAttachStorage = (FrameAttachStorage) templetMap.get("frameAttachStorage");

        // 检测模板文件是ofd格式还是word格式
        if (frameAttachStorage != null && frameAttachStorage.getAttachFileName() != null
                && frameAttachStorage.getAttachFileName().endsWith("ofd")) {
            // 直接用ofd模板来生成ofd附件
            try {
                return generatePrintOfdSupportCopy(frameAttachStorage, userGuid, userDisPlayName, certinfo,
                        isPrintCopy);
            }
            catch (Exception e) {
                log.info(e);
                returnMap.put("issuccess", "0");
                returnMap.put("msg", "打印证照失败！");
                return returnMap;
            }
        }

        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            // 获取证书文件
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            License license = new License();
            license.setLicense(licenseName);
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
            outputStream = new ByteArrayOutputStream();
            Document doc = new Document(frameAttachStorage.getContent());

            // 填充word域值
            doc.getMailMerge().execute(fieldNames, values);
            // 子表的渲染
            doc.getMailMerge().executeWithRegions(dataSet);
            // 插入图片（包括二维码）
            String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
            if (StringUtil.isBlank(qrcodeurl)) {
                qrcodeurl = certinfo.getCertname();
            }
            else {
                qrcodeurl += "?guid=" + certinfo.getRowguid() + "";
            }

            log.info("certInfo.getStr(\"defaultwidth\"):"+certinfo.getStr("defaultwidth"));
            log.info("certInfo.getStr(\"defaultheight\"):"+certinfo.getStr("defaultheight"));
            if(StringUtils.isNotBlank(certinfo.getStr("defaultwidth")) && StringUtils.isNotBlank(certinfo.getStr("defaultheight"))){
                insertImagetoBookmark(doc, imageMap, true,qrcodeurl, Integer.parseInt(certinfo.getStr("defaultwidth")),Integer.parseInt(certinfo.getStr("defaultheight")));
            }else{
                insertImagetoBookmark(doc, imageMap, true, qrcodeurl,0,0);
            }
            // 添加水印(读取epointframe.properties的配置)
            String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
            if (StringUtil.isNotBlank(watermarkText)) {
                // 转码
                watermarkText = new String(watermarkText.getBytes("ISO-8859-1"), "UTF-8");
                // 默认宽度500，高度100
                int watermarkWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_watermark_width"), 500);
                int watermarkHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_watermark_height"),
                        100);
                insertWatermarkText(doc, watermarkText, watermarkWidth, watermarkHeight);
            }

            // 保存成word
            doc.save(outputStream, SaveFormat.DOC);

            inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            // 打印正/副本
            String attachGuid = certinfo.getPrintdocguid();
            String clienginfo = "证照打印文件";
            if (isPrintCopy) { // 打印副本
                attachGuid = certinfo.getCopyprintdocguid();
                clienginfo = "证照打印文件（副本）";
            }
            boolean isPrint = false;
            FrameAttachInfo frameAttachInfo = null;
            if (StringUtil.isNotBlank(attachGuid)) {
                frameAttachInfo = attachService.getAttachInfoDetail(attachGuid);
                if (frameAttachInfo != null) {
                    isPrint = true;
                }
            }
            // 是否已打印
            if (isPrint) { // 已打印，更新
                attachService.updateAttach(frameAttachInfo, inputStream);
            }
            else { // 未打印，上传
                   // 生成Attchguid
                if (StringUtil.isBlank(attachGuid)) {
                    attachGuid = UUID.randomUUID().toString();
                }
                // 实际插入的attachguid不同
                attachGuid = AttachUtil.saveFileInputStream(attachGuid, UUID.randomUUID().toString(),
                        frameAttachStorage.getAttachFileName(), ".doc", clienginfo, inputStream.available(),
                        inputStream, userGuid, userDisPlayName).getAttachGuid();
                // 是否打印副本
                if (!isPrintCopy) {
                    certinfo.setPrintdocguid(attachGuid);
                }
                else {
                    certinfo.setCopyprintdocguid(attachGuid);
                }
                iCertInfo.updateCertInfo(certinfo);
            }
            // 打印成功
            returnMap.put("issuccess", "1");
            returnMap.put("attachguid", attachGuid);
        }
        catch (Exception e) {
            log.info(e);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnMap;
    }

    public Map<String, String> generatePrintDocSupportCopy(CertInfo certInfo, Boolean isPrintCopy, String userguid,
            String displayname) {
        Map<String, String> returnMap = new HashMap<String, String>();
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certInfo.getCertareaguid());
        // 获得照面信息
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", certInfo.getRowguid());
        CertInfoExtension dataBean = noSQLSevice.find(CertInfoExtension.class, filter, false);
        if (dataBean == null) {
            dataBean = new CertInfoExtension();
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setCertinfoguid(certInfo.getRowguid());
        }
        // 日期格式转换
        new CertInfoBizlogic().convertDate(metadataList, dataBean);
        GenerateBizlogic generateBizlogic = new GenerateBizlogic();
        try {
            EpointFrameDsManager.begin(null);
            Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, dataBean);
            // word域名数组
            String[] fieldNames = (String[]) wordMap.get("fieldNames");
            // word域值数组
            Object[] values = (Object[]) wordMap.get("values");
            // 图片map
            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            CertCatalog certCatalog = iCertCatalog.getCatalogDetailByrowguid(certInfo.getCertareaguid());
            String cliengguid = "";
            if (certCatalog != null) {
                if (isPrintCopy) {
                    cliengguid = certCatalog.getCopytempletcliengguid();
                }
                else {
                    cliengguid = certCatalog.getTempletcliengguid();
                }
                returnMap = generateBizlogic.generatePrintDocSupportCopy(cliengguid, isPrintCopy, userguid, displayname,
                        fieldNames, values, imageMap, dataSet, certInfo);
            }
            else {
                returnMap.put("issuccess", "0");
                returnMap.put("msg", "对应的证照不存在！");
            }
        }
        catch (Exception e) {
            log.info(e);
        }
        finally {
            EpointFrameDsManager.commit();
            EpointFrameDsManager.close();
        }
        return returnMap;
    }

    /**
     * 插入图片到书签
     *
     * @param doc
     * @param imageMap
     *            图片map
     * @param isUseQrcode
     *            是否使用二维码
     * @param qrcodeText
     *            二维码文本
     * @param defaultwidth 宽度 单位mm
     * 
     * @param defaultheight 高度 单位mm
     *
     */
    public void insertImagetoBookmark(Document doc, Map<String, Record> imageMap, boolean isUseQrcode,
            String qrcodeText,int defaultwidth,int defaultheight) {
        // DocumentBuilder 类似一个文档操作工具，用来操作已经实例化的Document 对象，DocumentBuilder
        // 里有许多方法 例如插入文本、插入图片、插入段落、插入表格等等
        DocumentBuilder build = null;
        InputStream qrCodeInputStream = null;
        try {
            build = new DocumentBuilder(doc);
            // 是否添加二维码
            if (isUseQrcode && StringUtil.isNotBlank(qrcodeText)) {
                // 添加二维码 (读取epointframe.properties的配置)
                int qcodeWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_width"), 100);
                int qcodeHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_height"), 100);
                qrCodeInputStream = QrcodeUtil.getQrCode(qrcodeText, qcodeWidth, qcodeHeight);
                // 书签存在
                if (qrCodeInputStream != null && getBookmark(doc, "二维码") != null) {
                    // 指定对应的书签
                    build.moveToBookmark("二维码");
                    // 插入附件流信息
                    build.insertImage(qrCodeInputStream);
                }
            }

            // 插入照面信息的附件字段
            if (ValidateUtil.isNotBlankMap(imageMap)) {
                for (Map.Entry<String, Record> entry : imageMap.entrySet()) {
                    String fieldChineseName = entry.getKey();
                    // 是否存在对应的书签
                    if (getBookmark(doc, fieldChineseName) != null) {
                        // 指定对应的书签
                        build.moveToBookmark(fieldChineseName);
                        Record record = entry.getValue();
                        FrameAttachStorage storage = (FrameAttachStorage) record.get("attachStorage");
                        CertMetadata certMetadata = (CertMetadata) record.get("certmetadata");
                        if (storage != null && storage.getContent() != null) {
                            InputStream content = null;
                            try {
                                content = storage.getContent();
                                Integer width = certMetadata.getWidth();
                                Integer height = certMetadata.getHeight();
                                // 宽高都是0的时候图片尺寸不能大于2100
                                if (CertConstant.CONSTANT_INT_ZERO.equals(width)
                                        && CertConstant.CONSTANT_INT_ZERO.equals(height)) {
                                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = content.read(buffer)) > -1) {
                                        out.write(buffer, 0, len);
                                    }
                                    out.flush();
                                    out.close();
                                    content.close();

                                    content = new ByteArrayInputStream(out.toByteArray());
                                    BufferedImage image = ImageIO.read(content);
                                    width = image.getWidth();
                                    if (width > 2100) {
                                        width = 2100;
                                    }
                                    height = image.getHeight();
                                    if (height > 2100) {
                                        height = 2100;
                                    }

                                    content.close();
                                    content = new ByteArrayInputStream(out.toByteArray());
                                }
                                if(defaultwidth>0 && defaultheight>0){
                                    width=defaultwidth;
                                    height=defaultheight;
                                }
                                log.info("width:"+width);
                                log.info("height:"+height);
                                // 插入附件流信息
                                build.insertImage(content, RelativeHorizontalPosition.MARGIN, 0,
                                        RelativeVerticalPosition.MARGIN, 0, GenerateUtil.mmToPixel(width),
                                        GenerateUtil.mmToPixel(height), WrapType.SQUARE);
                            }
                            catch (Exception e) {
                                log.info(e);
                            }
                            finally {
                                if (content != null) {
                                    content.close();
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            log.info(e);
        }
        finally {
            try {
                if (qrCodeInputStream != null) {
                    qrCodeInputStream.close();
                }
            }
            catch (IOException e) {
                log.info(e);
            }
        }
    }

    /**
     * 获得书签
     *
     * @param doc
     * @param bookmarkName
     * @return
     */
    private Bookmark getBookmark(Document doc, String bookmarkName) {
        if (doc == null || StringUtil.isBlank(bookmarkName)) {
            return null;
        }
        BookmarkCollection bookmarkList = doc.getRange().getBookmarks();
        if (bookmarkList != null && bookmarkList.getCount() > 0) {
            for (Bookmark bookmark : bookmarkList) {
                if (StringUtil.isNotBlank(bookmark.getName()) && bookmark.getName().trim().equals(bookmarkName)) {
                    return bookmark;
                }
            }
        }
        return null;
    }

    /**
     * 生成缩略图
     *
     * @param srcDoc
     *            源doc
     * @param fileName
     *            文件名称
     * @param userGuid
     * @param userDisplayName
     * @param certInfo
     */
    public void markSltImage(Document srcDoc, String fileName, String userGuid, String userDisplayName,
            CertInfo certInfo) {
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            // 创建一个新的doc，拷贝第一页
            Document dstDoc = new Document();
            // 移除所有section
            dstDoc.removeAllChildren();
            // 获得第一页
            Section firstSection = srcDoc.getFirstSection();
            // 创建node导入，因为node必须指定一个Document，并且不可修改，所以只能导入
            NodeImporter nodeImporter = new NodeImporter(srcDoc, dstDoc, ImportFormatMode.KEEP_SOURCE_FORMATTING);
            // 生成新的node，同时指定Document
            Section section = (Section) nodeImporter.importNode(firstSection, true);
            // 设置页边距(去除多余的白边)
            section.getPageSetup().setLeftMargin(0);
            section.getPageSetup().setTopMargin(0);
            section.getPageSetup().setRightMargin(0);
            section.getPageSetup().setBottomMargin(0);
            section.getPageSetup().setSectionStart(SectionStart.NEW_PAGE);
            section.getPageSetup().setPaperSize(PaperSize.LETTER);
            // 去除水印
            removeWatermark(section);
            // 添加到新的doc
            dstDoc.appendChild(section);

            // 获得缩略图尺寸
            int width = 0;
            int height = 0;
            CertCatalog catalog = iCertCatalog.getCatalogDetailByrowguid(certInfo.getCertareaguid());
            if (catalog == null) {
                log.error("制作缩略图失败，不存在对应的证照类型！");
                return;
            }
            if (catalog.getCertwidth() == 0 || catalog.getCertheight() == 0) {
                // 自动计算图片宽、高
                // 获取二维码宽 高
                int qcodeWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_width"), 100);
                int qcodeHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_height"), 100);
                Shape image = null;
                NodeCollection shapes = dstDoc.getChildNodes(NodeType.SHAPE, true);
                for (Object object : shapes) {
                    Shape shape = (Shape) object;
                    if (shape.hasImage()) {
                        // points 转为像素
                        int imageWidth = (int) Math.ceil(ConvertUtil.pointToPixel(shape.getWidth()));
                        int imageHeight = (int) Math.ceil(ConvertUtil.pointToPixel(shape.getHeight()));
                        if (imageWidth > qcodeWidth && imageHeight > qcodeHeight) {
                            width = imageWidth;
                            height = imageHeight;
                            image = shape;
                        }
                    }
                }
                if (image == null) {
                    return;
                }
            }
            else {
                width = GenerateUtil.mmToPixel(catalog.getCertwidth());
                height = GenerateUtil.mmToPixel(catalog.getCertheight());
                // 解决尺寸上溢
                // A4尺寸 (210 * 297)(mm)
                int a4Width = GenerateUtil.mmToPixel(CertConstant.A4_PAPER_WIDTH);
                int a4Height = GenerateUtil.mmToPixel(CertConstant.A4_PAPER_HEIGHT);
                if (width > a4Width) {
                    width = a4Width;
                }
                if (height > a4Height) {
                    height = a4Height;
                }
            }
            // 设置doc尺寸(points)
            section.getPageSetup().setPageWidth(ConvertUtil.pixelToPoint(width));
            section.getPageSetup().setPageHeight(ConvertUtil.pixelToPoint(height));
            // 调整图片位置 TODO
            dstDoc.save(outputStream, SaveFormat.PNG);
            // 保存缩略图
            String sltimageCliengguid = certInfo.getSltimagecliengguid();
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            // 没有缩略图
            if (StringUtil.isBlank(sltimageCliengguid)
                    || attachService.getAttachCountByClientGuid(sltimageCliengguid) == 0) {
                sltimageCliengguid = UUID.randomUUID().toString();
                AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), sltimageCliengguid,
                        certInfo.getCertno() + "-" + certInfo.getCertownername().replace("^", ",") + "-"
                                + System.currentTimeMillis() + "-缩略图.png",
                        "png", "缩略图", inputStream.available(), inputStream, userGuid, userDisplayName);
                certInfo.setSltimagecliengguid(sltimageCliengguid);
                iCertInfo.updateCertInfo(certInfo);
            }
            else { // 有缩略图
                FrameAttachInfo frameAttachInfo = attachService.getAttachInfoListByGuid(sltimageCliengguid).get(0);
                if (frameAttachInfo != null) {
                    // 先删除物理关系
                    attachService.deleteAttachByAttachGuid(frameAttachInfo.getAttachGuid());
                }
                // 重新上传
                AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), sltimageCliengguid,
                        certInfo.getCertno() + "-" + certInfo.getCertownername().replace("^", ",") + "-"
                                + System.currentTimeMillis() + "-缩略图.png",
                        "png", "缩略图", inputStream.available(), inputStream, userGuid, userDisplayName);
            }

        }
        catch (Exception e) {
            log.info(e);
            log.error("制作缩略图失败!");
        }
        finally {
            try {
                // 输入流必须关闭，否则文件无法删除
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                log.info(e);
                log.error("缩略图临时文件删除失败!");
            }
        }
    }

    /**
     * 插入水印
     *
     * @param doc
     * @param watermarkText
     *            水印内容
     * @param width
     *            宽度
     * @param height
     *            高度
     */
    public void insertWatermarkText(Document doc, String watermarkText, double width, double height) {
        Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);
        // 获得文本路径对象
        TextPath textPath = watermark.getTextPath();
        textPath.setText(watermarkText);
        // 不加粗
        textPath.setBold(false);
        // textPath.setSpacing(5000);
        // textPath.setSize(2);
        textPath.setFontFamily("宋体");
        watermark.setWidth(width);
        watermark.setHeight(height);
        // Text will be directed from the bottom-left to the top-right corner.
        watermark.setRotation(-40);
        // 填充颜色为浅灰
        watermark.getFill().setColor(Color.lightGray);
        watermark.setStrokeColor(Color.lightGray);
        // 设置透明度
        watermark.getFill().setOpacity(0.3);

        // 居中显示
        watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
        watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
        watermark.setWrapType(WrapType.NONE);
        // 显示在下方
        watermark.setVerticalAlignment(VerticalAlignment.BOTTOM);
        // 显示在右方
        watermark.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        // 创建一个新的段落，并把水印添加到段落
        Paragraph watermarkPara = new Paragraph(doc);
        watermarkPara.appendChild(watermark);

        // 水印插入到doc的每一页
        SectionCollection sections = doc.getSections();
        if (sections != null && sections.getCount() > 0) {
            for (Section section : sections) {
                insertParagraphIntoHeader(watermarkPara, section, HeaderFooterType.HEADER_PRIMARY);
                insertParagraphIntoHeader(watermarkPara, section, HeaderFooterType.HEADER_FIRST);
                insertParagraphIntoHeader(watermarkPara, section, HeaderFooterType.HEADER_EVEN);
            }
        }
    }

    /**
     * 水印段落插入header
     *
     * @param watermarkPara
     *            段落
     * @param sect
     *            文档的部分
     * @param headerType
     */
    public void insertParagraphIntoHeader(Paragraph watermarkPara, Section sect, int headerType) {
        try {
            HeaderFooter header = sect.getHeadersFooters().get(headerType);
            // 如果没有header，创建
            if (header == null) {
                header = new HeaderFooter(sect.getDocument(), headerType);
                sect.getHeadersFooters().add(header);
            }
            // 水印插入到header
            header.appendChild(watermarkPara.deepClone(true));
        }
        catch (Exception e) {
            log.error("水印段落插入header失败!");
            log.info(e);
        }
    }

    /**
     * 去除水印
     *
     * @param section
     */
    public void removeWatermark(Section section) {
        // 去除水印
        HeaderFooter headerPrimary = section.getHeadersFooters().get(HeaderFooterType.HEADER_PRIMARY);
        if (headerPrimary != null) {
            section.getHeadersFooters().remove(headerPrimary);
        }
        HeaderFooter headerFirst = section.getHeadersFooters().get(HeaderFooterType.HEADER_FIRST);
        if (headerFirst != null) {
            section.getHeadersFooters().remove(headerFirst);
        }
        HeaderFooter headerEven = section.getHeadersFooters().get(HeaderFooterType.HEADER_EVEN);
        if (headerEven != null) {
            section.getHeadersFooters().remove(headerEven);
        }
    }

    /**
     * 生成document
     *
     * @param baseImage
     *            底图
     * @param waterImage
     *            水印图片
     * @return
     */
    private Document generateDocumentForLevelC(File baseImage, File waterImage) {
        Document document = null;
        // A4尺寸 (210 * 297)(mm)
        // double a4WidthInMm = 210;
        // double a4HeightInMm = 297;
        // double a4WidthInPoints = GenerateUtil.mmToPixel(a4WidthInMm);
        // double a4HeightInPoints = GenerateUtil.mmToPixel(a4HeightInMm);
        try {
            DocumentBuilder builder = new DocumentBuilder();
            // 2. 水印 图片，底图合成
            // 居中插入底图
            Shape baseShape = builder.insertImage(baseImage.getAbsolutePath());
            baseShape.setWrapType(WrapType.NONE);
            baseShape.setBehindText(true);
            baseShape.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
            baseShape.setHorizontalAlignment(HorizontalAlignment.CENTER);
            baseShape.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
            baseShape.setVerticalAlignment(VerticalAlignment.CENTER);

            double baseTop = (builder.getPageSetup().getPageHeight() - baseShape.getHeight()) / 2;
            double baseLeft = (builder.getPageSetup().getPageWidth() - baseShape.getWidth()) / 2;

            // 插入水印图片
            Shape waterShape = builder.insertImage(waterImage.getAbsolutePath());
            waterShape.setWrapType(WrapType.NONE);
            waterShape.setBehindText(true);
            waterShape.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
            waterShape.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
            // 图片设置
            BufferedImage bufferedImage = ImageIO.read(waterImage);
            waterShape.setWidth(bufferedImage.getWidth() / 1.5);
            waterShape.setHeight(bufferedImage.getHeight() / 1.5);
            waterShape.setTop(baseTop + 40);
            waterShape.setLeft(baseLeft - 40);
            waterShape.setRotation(-30);

            // 插入可信等级
            String levelPath = ClassPathUtil.getDeployWarPath() + "epointcert/certutils/certinfo/images/level.png";
            Shape levelImage = builder.insertImage(levelPath);
            levelImage.setWrapType(WrapType.NONE);
            levelImage.setBehindText(true);
            levelImage.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
            levelImage.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
            // 设置图片大小
            levelImage.setWidth(60);
            levelImage.setHeight(10);
            levelImage.setTop(10);
            levelImage.setLeft(builder.getPageSetup().getPageWidth() - levelImage.getWidth() - 10);
            document = builder.getDocument();
        }
        catch (Exception e) {
            log.info(e);
        }

        return document;
    }

    /**
     * C类证照生成（此方法提供南京项目组，暂不使用）
     *
     * @param certInfo
     * @param waterText
     *            水印文字
     * @param fildName
     *            文件名称
     * @param content
     *            文件内容
     * @param isBase64
     * @throws UnsupportedEncodingException
     */
    @Deprecated
    public void generateCertForLevelC(CertInfo certInfo, String waterText, String fildName, String content,
            boolean isBase64) throws UnsupportedEncodingException {
        String baseImagePath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                + UUID.randomUUID().toString() + "-" + fildName;
        byte[] bytes = null;
        if (isBase64) {
            bytes = Base64Util.decodeBuffer(content);
        }
        else {
            bytes = content.getBytes("UTF-8");
        }
        File baseImage = GenerateUtil.byte2Image(bytes, baseImagePath);
        try {
            generateCertForLevelC(certInfo, waterText, baseImage);
        }
        catch (Exception e) {
            log.info(e);
        }
        finally {
            if (baseImage != null && baseImage.isFile() && baseImage.exists()) {
                baseImage.delete();
            }
        }
    }

    /**
     * C类证照生成（此方法提供南京项目组，暂不使用）
     *
     * @param certInfo
     * @param waterText
     *            水印文字
     * @param baseImage
     *            底图
     */
    @Deprecated
    public void generateCertForLevelC(CertInfo certInfo, String waterText, File baseImage) {
        File waterImage = null; // 水印图片
        File sltImage = null; // 缩略图
        File pdf = null; // pdf
        FileInputStream sltImageInputStreamm = null; // 缩略图输入流
        FileInputStream pdfInputSteam = null; // pdf输入流
        try {
            // 获取证书文件
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            License license = new License();
            license.setLicense(licenseName);
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);

            // 1. 创建水印图片
            String format = "本件由%s上传，在南京市行政区域内办理许可审批和公共服务时，请与原件核对无误后使用";
            waterText = String.format(format, waterText);
            String waterImagePath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                    + UUID.randomUUID().toString() + ".png";
            waterImage = FileManagerUtil.createFile(waterImagePath);
            GenerateUtil.createImage(waterText, waterImage);

            // 2. 生成document
            Document document = generateDocumentForLevelC(baseImage, waterImage);
            if (document == null) {
                return;
            }
            // 保存缩略图
            String sltimageCliengguid = UUID.randomUUID().toString();
            certInfo.setSltimagecliengguid(sltimageCliengguid);
            String sltImagePath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                    + sltimageCliengguid + ".png";
            sltImage = FileManagerUtil.createFile(sltImagePath);
            document.save(sltImagePath, SaveFormat.PNG);
            // 上传缩略图
            sltImageInputStreamm = new FileInputStream(sltImage);
            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), sltimageCliengguid, sltImage.getName(), "png",
                    "缩略图", sltImageInputStreamm.available(), sltImageInputStreamm, null, null);

            // 保存pdf
            String certcliengguid = UUID.randomUUID().toString();
            certInfo.setCertcliengguid(certcliengguid);
            String pdfPath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/" + certcliengguid
                    + ".pdf";
            pdf = FileManagerUtil.createFile(pdfPath);
            document.save(pdfPath, SaveFormat.PDF);
            // 上传pdf
            pdfInputSteam = new FileInputStream(pdf);
            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), certcliengguid, pdf.getName(), "pdf", "系统生成",
                    pdfInputSteam.available(), pdfInputSteam, null, null);

        }
        catch (Exception e) {
            log.info(e);
        }
        finally {
            try {
                if (sltImageInputStreamm != null) {
                    sltImageInputStreamm.close();
                }
                if (pdfInputSteam != null) {
                    pdfInputSteam.close();
                }
                if (sltImage != null && sltImage.isFile() && sltImage.exists()) {
                    sltImage.delete();
                }
                if (pdf != null && pdf.isFile() && pdf.exists()) {
                    pdf.delete();
                }
                if (waterImage != null && waterImage.isFile() && waterImage.exists()) {
                    waterImage.delete();
                }
            }
            catch (IOException e) {
                log.info(e);
            }
        }
    }

    /**
     * 检测目录模板文件
     *
     * @param cliengGuid
     *            模板文件（正/副本）guid
     * @param isGenerateCopy
     *            是否生成副本
     * @return
     */
    public Map<String, Object> checkCatalogTemplet(String cliengGuid, boolean isGenerateCopy) {
        Map<String, Object> returnMap = new HashMap<>();
        String tip = "正本";
        if (isGenerateCopy) {
            tip = "副本";
        }

        // 模版文件cliengguid的非空验证
        if (StringUtil.isBlank(cliengGuid)) {
            returnMap.put("msg", String.format("未正确配置证照类型模版文件（%s）！", tip));
            return returnMap;
        }

        // 模版文件校验（正副本）
        List<FrameAttachInfo> attachInfos = attachService.getAttachInfoListByGuid(cliengGuid);
        List<FrameAttachStorage> frameAttachStorageList = attachService.getAttachListByGuid(cliengGuid);
        if (ValidateUtil.isBlankCollection(attachInfos)) {
            returnMap.put("msg", String.format("当前证照对应的证照类型模版文件（%s）为空，请联系管理员维护！", tip));
            return returnMap;
        }
        FrameAttachInfo attachInfo = attachInfos.parallelStream().filter(item -> "dzzzglxt".equals(item.getCliengTag()))
                .findFirst().orElse(null);
        if (attachInfo != null) {
            log.info(attachInfo.getAttachGuid() + attachInfo.getCliengTag());
            FrameAttachStorage frameAttachStorage = attachService.getAttach(attachInfo.getAttachGuid());
            if (StringUtil.isBlank(frameAttachStorage.getContent())) {
                returnMap.put("msg", String.format("当前证照对应的证照类型模版文件（%s）内容为空，请联系管理员维护！", tip));
                return returnMap;
            }
            else if (frameAttachStorage.getAttachFileName().endsWith("ofd")) {
                Collections.sort(frameAttachStorageList, new Comparator<FrameAttachStorage>() // 进行倒序排列
                {
                    @Override
                    public int compare(FrameAttachStorage o1, FrameAttachStorage o2) {
                        int order = (attachService.getAttachInfoDetail(o1.getAttachGuid()).getUploadDateTime())
                                .compareTo(attachService.getAttachInfoDetail(o2.getAttachGuid()).getUploadDateTime());
                        return order;
                    }
                });
                returnMap.put("frameAttachStorageList", frameAttachStorageList);
            }
            returnMap.put("frameAttachStorage", frameAttachStorage);
        }
        return returnMap;
    }

    /**
     * 绘制表格（自动合并单元格）
     *
     * @param metadataList
     *            元数据列表
     * @param recordList
     *            record列表
     * @param builder
     * @param bookmark
     *            书签名
     */
    public void drawTableAutoMerge(List<CertMetadata> metadataList, List<Record> recordList, DocumentBuilder builder,
            String bookmark) {
        try {
            if (StringUtil.isBlank(bookmark)) {
                return;
            }
            builder.moveToBookmark(bookmark);
            // 设置表格水平居中显示
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
            builder.getFont().setSize(12);
            // 用这个效果居然是整个表格在页面居中..
            List<String> fieldlist = new ArrayList<>();
            List<Integer> widthlist = new ArrayList<>();
            // 绘制表头
            if (ValidateUtil.isNotBlankCollection(metadataList)) {
                for (CertMetadata metadata : metadataList) {
                    fieldlist.add(metadata.getFieldname());
                    widthlist.add(metadata.getWidth());
                    builder.insertCell();
                    // 外边框
                    builder.getCellFormat().getBorders().setLineStyle(LineStyle.SINGLE);
                    builder.getCellFormat().getBorders().setColor(Color.black);
                    builder.getCellFormat().setVerticalMerge(CellMerge.NONE);
                    // 设置宽度
                    builder.getCellFormat().setWidth(metadata.getWidth());
                    builder.getCellFormat().setTopPadding(8);
                    // 居中
                    builder.getCellFormat().setVerticalAlignment(CellVerticalAlignment.CENTER);
                    builder.write(metadata.getFieldchinesename());
                }
                builder.endRow();
            }
            // 记录比较的列
            Map<Integer, HashSet<Integer>> map = new HashMap<>();
            if (ValidateUtil.isNotBlankCollection(recordList)) {
                for (int i = 0; i < recordList.size(); i++) {
                    // 当前记录
                    Record currentRecord = recordList.get(i);
                    // 上一行记录
                    Record previousRecord = null;
                    if (i > 0) {
                        previousRecord = recordList.get(i - 1);
                    }
                    // 下一行记录
                    Record nextRecord = null;
                    if (!recordList.isEmpty() && i < recordList.size() - 1) {
                        nextRecord = recordList.get(i + 1);
                    }

                    for (int j = 0; j < fieldlist.size(); j++) {
                        String field = fieldlist.get(j);
                        builder.insertCell();
                        builder.getCellFormat().getBorders().setLineStyle(LineStyle.SINGLE);
                        builder.getCellFormat().getBorders().setColor(Color.black);
                        // 居中
                        builder.getCellFormat().setVerticalAlignment(CellVerticalAlignment.CENTER);
                        // 设置宽度
                        builder.getCellFormat().setWidth(widthlist.get(j));
                        builder.getCellFormat().setTopPadding(8);

                        // 是否合并
                        if (i == 0) { // 第一行
                            // 和第二行比
                            if (compare2Value(currentRecord, nextRecord, field)) {
                                // 记录merge列
                                HashSet<Integer> hashSet = map.get(i + 1);
                                if (hashSet == null) {
                                    hashSet = new HashSet<>();
                                }
                                hashSet.add(j);
                                map.put(i + 1, hashSet);
                                builder.getCellFormat().setVerticalMerge(CellMerge.FIRST);
                            }
                            else {
                                builder.getCellFormat().setVerticalMerge(CellMerge.NONE);
                            }
                            builder.write(StringUtil.isBlank(currentRecord.get(field)) ? "" : currentRecord.get(field));
                        }
                        else if (i == recordList.size() - 1) { // 最后一行
                            // 和前一行比
                            // 如果相同合并单头格
                            if (compare2Value(currentRecord, previousRecord, field)) {
                                // 和上一行merge列值全部相同才合并
                                HashSet<Integer> hashSet = map.get(i - 1);
                                boolean isMerage = true;
                                if (ValidateUtil.isNotBlankCollection(hashSet)) {
                                    for (Integer index : hashSet) {
                                        if (!compare2Value(currentRecord, previousRecord, fieldlist.get(index))) {
                                            isMerage = false;
                                        }
                                    }
                                }
                                if (isMerage) {
                                    builder.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
                                }
                                else {
                                    builder.getCellFormat().setVerticalMerge(CellMerge.NONE);
                                    builder.write(StringUtil.isBlank(currentRecord.get(field)) ? ""
                                            : currentRecord.get(field));
                                }
                            }
                            else {
                                builder.getCellFormat().setVerticalMerge(CellMerge.NONE);
                                builder.write(
                                        StringUtil.isBlank(currentRecord.get(field)) ? "" : currentRecord.get(field));
                            }
                        }
                        else { // 中间行
                            if (compare2Value(currentRecord, previousRecord, field)) {
                                // 和上一行merge列值全部相同才合并
                                HashSet<Integer> hashSet = map.get(i - 1);
                                boolean isMerage = true;
                                if (ValidateUtil.isNotBlankCollection(hashSet)) {
                                    for (Integer index : hashSet) {
                                        if (!compare2Value(currentRecord, previousRecord, fieldlist.get(index))) {
                                            isMerage = false;
                                        }
                                    }
                                }
                                if (isMerage) {
                                    builder.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
                                }
                                else {
                                    // 如果和下一行相同则标记
                                    if (compare2Value(currentRecord, nextRecord, field)) {
                                        builder.getCellFormat().setVerticalMerge(CellMerge.FIRST);
                                    }
                                    else {
                                        builder.getCellFormat().setVerticalMerge(CellMerge.NONE);
                                    }
                                    builder.write(StringUtil.isBlank(currentRecord.get(field)) ? ""
                                            : currentRecord.get(field));
                                }
                            }
                            else {
                                // 和后一行比
                                if (compare2Value(currentRecord, nextRecord, field)) {
                                    // 记录merge列
                                    HashSet<Integer> hashSet = map.get(i + 1);
                                    if (hashSet == null) {
                                        hashSet = new HashSet<>();
                                    }
                                    hashSet.add(j);
                                    map.put(i + 1, hashSet);
                                    builder.getCellFormat().setVerticalMerge(CellMerge.FIRST);
                                }
                                else {
                                    builder.getCellFormat().setVerticalMerge(CellMerge.NONE);
                                }

                                builder.write(
                                        StringUtil.isBlank(currentRecord.get(field)) ? "" : currentRecord.get(field));
                            }
                        }

                    }

                    builder.endRow();
                }

            }
            builder.getDocument().getRange().getBookmarks().get(bookmark).setText("");
        }
        catch (Exception e) {
            log.info(e);
        }
    }

    /**
     * 比较两个实体的字段值
     *
     * @param record
     *            原record
     * @param compareRecord
     *            比较的record
     * @param field
     *            字段名称
     * @return
     */
    private boolean compare2Value(Record record, Record compareRecord, String field) {
        boolean result = false;
        if (record == null || compareRecord == null) {
            return result;
        }
        String text = record.getStr(field);
        String compareText = compareRecord.getStr(field);
        if (StringUtil.isBlank(text) && StringUtil.isBlank(compareText)) {
            result = true;
        }
        else if (StringUtil.isNotBlank(text) && StringUtil.isNotBlank(compareText)
                && text.trim().equals(compareText.trim())) {
            result = true;
        }
        return result;
    }

    /**
     * 利用Ofd模板直接生成ofd附件
     *
     * @param userGuid
     * @param userDisPlayName
     * @param certInfo
     * @param isGenerateCopy
     * @throws ConvertException
     * @throws IOException
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public Map<String, String> generateOfdLicenseSupportCopy(List<FrameAttachStorage> frameAttachStorages,
            String userGuid, String userDisPlayName, CertInfo certInfo, boolean isGenerateCopy, String keyString,
            String certData) throws IOException, ConvertException {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        // 模板输入流
        InputStream templetInputStream = null;
        // xml文件输入流
        InputStream infoXmlInputStream = null;
        // 转换对象
        HTTPAgent ha = null;
        // 查询业务信息配置
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certInfo.getCertareaguid());
        // 判断业务信息是否配置
        if (ValidateUtil.isBlankCollection(metadataList)) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "证照业务信息未配置！");
            return returnMap;
        }
        // 查询业务信息
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("certinfoguid", certInfo.getRowguid());
        CertInfoExtension certInfoExtension = noSQLSevice.find(CertInfoExtension.class, filter, false);

        // 判断业务信息是否存在
        if (certInfoExtension == null) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "证照业务信息数据不存在！");

            return returnMap;
        }

        // 将照面信息生成xml字符串
        String infoXml = null;
        // 将照面信息转成流
        // 获取ofd转换服务地址
        String ofdserviceurl = ConfigUtil.getConfigValue("ofdserviceurl");
        // 定义存储路径
        String ofdFilepath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                + UUID.randomUUID().toString() + frameAttachStorages.get(0).getAttachFileName();
        String outPath = ofdFilepath;
        FileOutputStream fileOutputStream = null;
        try {
            ha = new HTTPAgent(ofdserviceurl);
            // 创建链接
            Packet pack = new Packet(Const.PackType.COMMON, Const.Target.OFD);
            Template template = null;
            for (FrameAttachStorage frameAttachStorage : frameAttachStorages) {
                // 获取模板文件内容
                templetInputStream = frameAttachStorage.getContent();
                if (templetInputStream == null) {
                    returnMap.put("issuccess", "0");
                    returnMap.put("msg", "证照模版内容为空！");
                    return returnMap;
                }

                String fieldname = frameAttachStorage.getAttachFileName().replace(".ofd", "");
                // 如果是子表附件
                if (certInfoExtension.get(fieldname) != null) {
                    CertMetadata parentmetadata = null;
                    for (CertMetadata data : metadataList) {
                        if (fieldname.equals(data.getFieldname())) {
                            parentmetadata = data;
                        }
                    }
                    JSONArray arr = JSONArray.parseArray(certInfoExtension.getStr(fieldname));
                    List<CertMetadata> sublist = iCertMetaData
                            .selectSubDispinListByCertguid(parentmetadata.getRowguid());
                    for (Object obj : arr) {
                        templetInputStream = attachService.getInputStreamByInfo(
                                attachService.getAttachInfoDetail(frameAttachStorage.getAttachGuid()));
                        infoXml = dataToSubXmlString(obj, sublist, parentmetadata);
                        infoXmlInputStream = new ByteArrayInputStream(infoXml.getBytes("UTF-8"));
                        template = new Template(UUID.randomUUID().toString(), templetInputStream, infoXmlInputStream);
                        pack.data(template);
                    }
                }
                else {
                    templetInputStream = frameAttachStorage.getContent();
                    infoXml = dataToXmlString(certInfoExtension, metadataList);
                    infoXmlInputStream = new ByteArrayInputStream(infoXml.getBytes("UTF-8"));
                    template = new Template("生成电子证照", templetInputStream, infoXmlInputStream);
                    pack.data(template);
                }
            }

            fileOutputStream = new FileOutputStream(ofdFilepath);
            String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
            if (StringUtil.isNotBlank(watermarkText)) {
                pack.textMark(new TextInfo(watermarkText, "宋体", 20, "80999999"),
                        new MarkPosition(10f, 100f, 10f, 10f, MarkPosition.INDEX_ALL), true, true);
            }
            ha.convert(pack, fileOutputStream);
            // 将合成的ofd转成输入流
            // resultInputStream = new
            // ByteArrayInputStream(resultOutputStream.toByteArray());

        }
        catch (PackException e) {
            log.info(e);
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        }
        finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }

        try {
            // 是否自动签章
            String ofdautoseal = ConfigUtil.getConfigValue("ofdautoseal");
            if (CertConstant.CONSTANT_STR_ONE.equals(ofdautoseal) && StringUtil.isNotBlank(certData)
                    && StringUtil.isNotBlank(keyString)) {
                outPath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                        + UUID.randomUUID().toString() + frameAttachStorages.get(0).getAttachFileName();
                kinggridOfd(keyString, certData, ofdFilepath, outPath);
            }

            EpointFrameDsManager.begin(null);
            if (!isGenerateCopy) {
                // 生成正本
                // 是否生成过附件（正本）
                boolean isGenerate = false;
                String attachguid = "";
                List<FrameAttachInfo> frameAttachInfos = attachService
                        .getAttachInfoListByGuid(certInfo.getCertcliengguid());
                if (ValidateUtil.isNotBlankCollection(frameAttachInfos)) {
                    for (FrameAttachInfo attchInfo : frameAttachInfos) {
                        if ("系统生成".equals(attchInfo.getCliengInfo())) {
                            isGenerate = true;
                            attachguid = attchInfo.getAttachGuid();
                            break;
                        }
                    }
                }

                if (isGenerate) {
                    // 更新
                    AttachUtil.updateOfdAttach(
                            certInfo.getCertno() + "-" + certInfo.getCertownername().replace("^", ","), outPath,
                            certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成", attachguid);

                }
                else {
                    // 上传
                    if (StringUtil.isBlank(certInfo.getCertcliengguid())) {
                        certInfo.setCertcliengguid(UUID.randomUUID().toString());
                    }
                    AttachUtil.addOfdAttach(certInfo.getCertno() + "-" + certInfo.getCertownername().replace("^", ","),
                            outPath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成");
                }

                if (CertConstant.CONSTANT_INT_ZERO.equals(certInfo.getIscreatecert())) {
                    // 更新是否生成证照字段
                    certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
                    iCertInfo.updateCertInfo(certInfo);
                }
            }
            else { // 生成副本
                   // 是否生成过附件（副本）
                boolean isCopyGenerate = false;
                String attachguid = "";
                // 生成附件（副本）cliengguid初始化
                if (StringUtil.isBlank(certInfo.getCopycertcliengguid())) {
                    certInfo.setCopycertcliengguid(UUID.randomUUID().toString());
                    iCertInfo.updateCertInfo(certInfo);
                }
                else {
                    List<FrameAttachInfo> frameAttachInfoList = attachService
                            .getAttachInfoListByGuid(certInfo.getCopycertcliengguid());
                    if (ValidateUtil.isNotBlankCollection(frameAttachInfoList)) {
                        for (FrameAttachInfo attchInfo : frameAttachInfoList) {
                            if ("系统生成（副本）".equals(attchInfo.getCliengInfo())) {
                                isCopyGenerate = true;
                                attachguid = attchInfo.getAttachGuid();
                                break;
                            }
                        }
                    }
                }

                if (isCopyGenerate) {
                    // 更新
                    AttachUtil.updateOfdAttach(
                            certInfo.getCertno() + "-" + certInfo.getCertownername().replace("^", ",") + "（副本）",
                            outPath, certInfo.getCopycertcliengguid(), userGuid, userDisPlayName, "系统生成（副本）",
                            attachguid);
                }
                else {
                    // 上传
                    AttachUtil.addOfdAttach(
                            certInfo.getCertno() + "-" + certInfo.getCertownername().replace("^", ",") + "（副本）",
                            outPath, certInfo.getCopycertcliengguid(), userGuid, userDisPlayName, "系统生成（副本）");
                }
            }
            EpointFrameDsManager.commit();
            EpointFrameDsManager.close();
            returnMap.put("issuccess", "1");
            returnMap.put("msg", "生成证照成功！");
        }
        catch (Exception e) {
            log.info(e);
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        }
        finally {
            File file = FileManagerUtil.createFile(outPath);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            if (!ofdFilepath.equals(outPath)) {
                file = FileManagerUtil.createFile(ofdFilepath);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            }
        }
        return returnMap;
    }

    /**
     * 利用Ofd模板打印
     *
     * @param frameAttachStorage
     * @param userGuid
     * @param userDisPlayName
     * @param certInfo
     * @param isPrintCopy
     * @throws ConvertException
     * @throws IOException
     * @throws Exception
     */
    public Map<String, String> generatePrintOfdSupportCopy(FrameAttachStorage frameAttachStorage, String userGuid,
            String userDisPlayName, CertInfo certInfo, boolean isPrintCopy) throws IOException, ConvertException {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        InputStream inputStream = null;
        if (certInfo == null) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "证照不存在！");
            return returnMap;
        }
        String cliengguid = "";
        if (isPrintCopy) {
            cliengguid = certInfo.getCopycertcliengguid();
        }
        else {
            cliengguid = certInfo.getCertcliengguid();
        }
        if (StringUtil.isNotBlank(cliengguid)) {
            List<FrameAttachInfo> attachInfos = attachService.getAttachInfoListByGuid(cliengguid);
            if (ValidateUtil.isNotBlankCollection(attachInfos)) {
                for (FrameAttachInfo info : attachInfos) {
                    if (".ofd".equals(info.getContentType()) && info.getCliengInfo().contains("系统生成")) {
                        inputStream = attachService.getInputStreamByInfo(info);
                    }
                }
            }
        }

        if (inputStream == null) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "请先生成证照！");
            return returnMap;
        }

        try {
            EpointFrameDsManager.begin(null);
            // 打印正/副本
            String attachGuid = certInfo.getPrintdocguid();
            String clienginfo = "证照打印文件";
            if (isPrintCopy) {
                // 打印副本
                attachGuid = certInfo.getCopyprintdocguid();
                clienginfo = "证照打印文件（副本）";
            }
            boolean isPrint = false;
            FrameAttachInfo frameAttachInfo = null;
            if (StringUtil.isNotBlank(attachGuid)) {
                frameAttachInfo = attachService.getAttachInfoDetail(attachGuid);
                if (frameAttachInfo != null) {
                    isPrint = true;
                }
            }
            // 是否已打印
            if (isPrint) {
                // 已打印，更新
                attachService.updateAttach(frameAttachInfo, inputStream);
            }
            else {
                // 未打印，上传
                // 生成Attchguid
                if (StringUtil.isBlank(attachGuid)) {
                    attachGuid = UUID.randomUUID().toString();
                }
                // 实际插入的attachguid不同
                attachGuid = AttachUtil.saveFileInputStream(attachGuid, UUID.randomUUID().toString(),
                        frameAttachStorage.getAttachFileName(), ".ofd", clienginfo, inputStream.available(),
                        inputStream, userGuid, userDisPlayName).getAttachGuid();
                // 是否打印副本
                if (!isPrintCopy) {
                    certInfo.setPrintdocguid(attachGuid);
                }
                else {
                    certInfo.setCopyprintdocguid(attachGuid);
                }
                iCertInfo.updateCertInfo(certInfo);
            }
            // 打印成功
            returnMap.put("issuccess", "1");
            returnMap.put("attachguid", attachGuid);
            returnMap.put("contenttype", "ofd");
            EpointFrameDsManager.commit();
            EpointFrameDsManager.close();
        }
        catch (Exception e) {
            log.info(e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                log.info(e);
            }
        }
        return returnMap;
    }

    /**
     * 利用Ofd模板直接生成ofd附件
     *
     * @param userGuid
     * @param userDisPlayName
     * @param isGenerateCopy
     * @throws ConvertException
     * @throws IOException
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public Map<String, String> generateOfdDemoLicense(List<FrameAttachStorage> frameAttachStorages, String userGuid,
            String userDisPlayName, Record demoExtension, boolean isGenerateCopy) throws IOException, ConvertException {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        // 模板输入流
        InputStream templetInputStream = null;
        // xml文件输入流
        InputStream infoXmlInputStream = null;
        // 结果输出流
        ByteArrayOutputStream resultOutputStream = null;
        // 转换对象
        HTTPAgent ha = null;

        // 判断业务信息是否存在
        if (demoExtension == null) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "证照业务信息数据不存在！");
            return returnMap;
        }

        // 查询业务信息配置
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(demoExtension.get("catalogguid"));
        // 判断业务信息是否配置
        if (ValidateUtil.isBlankCollection(metadataList)) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "证照业务信息未配置！");
            return returnMap;
        }
        CertInfoExtension certInfoExtension = demoExtension.toEntity(CertInfoExtension.class);
        certInfoExtension.remove("operatedate", "operateusername", "rowguid", "catalogguid", "certcliengguid",
                "copycertcliengguid");

        // 将照面信息生成xml字符串
        String infoXml = dataToXmlString(certInfoExtension, metadataList);
        // 将照面信息转成流
        infoXmlInputStream = new ByteArrayInputStream(infoXml.getBytes("UTF-8"));
        // 获取ofd转换服务地址
        String ofdserviceurl = ConfigUtil.getConfigValue("ofdserviceurl");
        try {
            ha = new HTTPAgent(ofdserviceurl);
            // 创建链接
            Packet pack = new Packet(Const.PackType.COMMON, Const.Target.OFD);
            Template template = null;
            for (FrameAttachStorage frameAttachStorage : frameAttachStorages) {
                // 获取模板文件内容
                templetInputStream = frameAttachStorage.getContent();
                if (templetInputStream == null) {
                    returnMap.put("issuccess", "0");
                    returnMap.put("msg", "证照模版内容为空！");
                    return returnMap;
                }

                String fieldname = frameAttachStorage.getAttachFileName().replace(".ofd", "");
                // 如果是子表附件
                if (certInfoExtension.get(fieldname) != null) {
                    CertMetadata parentmetadata = null;
                    for (CertMetadata data : metadataList) {
                        if (fieldname.equals(data.getFieldname())) {
                            parentmetadata = data;
                        }
                    }
                    JSONArray arr = JSONArray.parseArray(certInfoExtension.getStr(fieldname));
                    List<CertMetadata> sublist = iCertMetaData
                            .selectSubDispinListByCertguid(parentmetadata.getRowguid());
                    for (Object obj : arr) {
                        templetInputStream = attachService.getInputStreamByInfo(
                                attachService.getAttachInfoDetail(frameAttachStorage.getAttachGuid()));
                        infoXml = dataToSubXmlString(obj, sublist, parentmetadata);
                        infoXmlInputStream = new ByteArrayInputStream(infoXml.getBytes("UTF-8"));
                        template = new Template(UUID.randomUUID().toString(), templetInputStream, infoXmlInputStream);
                        pack.data(template);
                    }
                }
                else {
                    templetInputStream = frameAttachStorage.getContent();
                    infoXml = dataToXmlString(certInfoExtension, metadataList);
                    infoXmlInputStream = new ByteArrayInputStream(infoXml.getBytes("UTF-8"));
                    template = new Template("生成电子证照", templetInputStream, infoXmlInputStream);
                    pack.data(template);
                }
            }

            resultOutputStream = new ByteArrayOutputStream();
            String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
            if (StringUtil.isNotBlank(watermarkText)) {
                pack.textMark(new TextInfo(watermarkText, "宋体", 20, "80999999"),
                        new MarkPosition(10f, 100f, 10f, 10f, MarkPosition.INDEX_ALL), true, true);
            }
            ha.convert(pack, resultOutputStream);
        }
        catch (PackException e) {
            log.info(e);
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        }

        EpointFrameDsManager.begin(null);
        if (!isGenerateCopy) { // 生成正本
            // 上传或更新证照附件（正本）
            boolean isUpdate = false;
            String certcliengguid = demoExtension.getStr("certcliengguid");
            String attachGuid = "";
            if (StringUtil.isBlank(certcliengguid)) { // 上传
                certcliengguid = UUID.randomUUID().toString();
            }
            else {
                List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(certcliengguid);
                if (ValidateUtil.isNotBlankCollection(attachInfoList)) { // 没有对应的附件,上传
                    for (FrameAttachInfo info : attachInfoList) {
                        if ("系统生成".equals(info.getCliengInfo())) {
                            isUpdate = true;
                            attachGuid = info.getAttachGuid();
                            break;
                        }
                    }
                }
            }

            if (isUpdate) { // 更新附件
                // 生成ofd
                AttachUtil.updateOfdAttachByOutputStream("证照附件", resultOutputStream, certcliengguid, userGuid,
                        userDisPlayName, "系统生成", attachGuid);
            }
            else { // 上传附件
                AttachUtil.addOfdAttachByOutputStream("证照附件", resultOutputStream, certcliengguid, userGuid,
                        userDisPlayName, "系统生成");
                // 更新demoExtension
                demoExtension.set("certcliengguid", certcliengguid);
                demoExtension.setSql_TableName(CertConstant.SQL_TABLE_DEMO_EXTENSION);
                noSQLSevice.update(demoExtension);
            }
        }
        else { // 生成副本
               // 上传或更新证照附件（副本）
            boolean isUpdate = false;
            String copycertcliengguid = demoExtension.getStr("copycertcliengguid");
            String attachGuid = "";
            if (StringUtil.isBlank(copycertcliengguid)) { // 上传
                copycertcliengguid = UUID.randomUUID().toString();
            }
            else {
                List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(copycertcliengguid);
                if (ValidateUtil.isNotBlankCollection(attachInfoList)) { // 没有对应的附件,上传
                    for (FrameAttachInfo info : attachInfoList) {
                        if ("系统生成".equals(info.getCliengInfo())) {
                            isUpdate = true;
                            attachGuid = info.getAttachGuid();
                            break;
                        }
                    }
                }
            }

            if (isUpdate) { // 更新附件
                AttachUtil.updateOfdAttachByOutputStream("证照附件（副本）", resultOutputStream, copycertcliengguid, userGuid,
                        userDisPlayName, "系统生成（副本）", attachGuid);
            }
            else { // 上传附件
                AttachUtil.addOfdAttachByOutputStream("证照附件（副本）", resultOutputStream, copycertcliengguid, userGuid,
                        userDisPlayName, "系统生成（副本）");
                // 更新demoExtension
                demoExtension.set("copycertcliengguid", copycertcliengguid);
                demoExtension.setSql_TableName(CertConstant.SQL_TABLE_DEMO_EXTENSION);
                noSQLSevice.update(demoExtension);
            }
        }
        EpointFrameDsManager.commit();
        EpointFrameDsManager.close();
        returnMap.put("issuccess", "1");
        returnMap.put("msg", "生成证照成功！");
        return returnMap;
    }

    private String dataToXmlString(CertInfoExtension certInfoExtension, List<CertMetadata> metadataList) {
        // 写入xml头部信息
        StringBuilder infoXml = new StringBuilder(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DataRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"sourcedata.xsd\">");
        for (CertMetadata certMetadata : metadataList) {
            // 获取元数据中文名称
            String fieldChineseName = certMetadata.getFieldchinesename();
            // 写入DataInfo节点信息
            infoXml.append("<DataInfo Name=\"" + fieldChineseName + "\" Value=\"");
            // 没有子表
            // 没有子表
            if (CertConstant.CONSTANT_STR_ZERO.equals(certMetadata.getIsrelatesubtable())) {
                generateXml(infoXml, JsonUtil.objectToJson(certInfoExtension), certMetadata, null);
            }
            infoXml.append("\"/>");
        }

        // 插入二维码
        String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
        if (StringUtil.isNotBlank(qrcodeurl)) {
            infoXml.append("<DataInfo Name=\"二维码\" Value=\"");
            qrcodeurl += "?guid=" + certInfoExtension.getCertinfoguid() + "";
            // 添加二维码 (读取epointframe.properties的配置),把二维码内容转变成图片
            int qcodeWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_width"), 120);
            int qcodeHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_height"), 120);
            InputStream qrCodeInputStream;
            try {
                qrCodeInputStream = QrcodeUtil.getQrCode(qrcodeurl, qcodeWidth, qcodeHeight);
                // qrCodeInputStream = new
                // ByteArrayInputStream(qrcodeurl.getBytes("UTF-8"));
                if (qrCodeInputStream != null) {
                    // 转换成字节数据
                    byte[] bytes = FileManagerUtil.getContentFromInputStream(qrCodeInputStream);
                    // 写入Base64位编码字符串
                    infoXml.append(Base64Util.encode(bytes));
                }
                infoXml.append("\"/>");
            }
            catch (Exception e) {
                log.info(e);
            }
        }
        infoXml.append("</DataRoot>");
        return infoXml.toString();
    }

    private String dataToSubXmlString(Object obj, List<CertMetadata> subdatalist, CertMetadata parentdata) {
        // 写入xml头部信息
        StringBuilder infoXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        // 获取元数据英文名称
        String fieldName = parentdata.getFieldname();
        infoXml.append("<" + fieldName + ">");
        for (CertMetadata subdata : subdatalist) {
            infoXml.append("<" + subdata.getFieldname() + ">");
            generateXml(infoXml, obj.toString(), subdata, parentdata);
            infoXml.append("</" + subdata.getFieldname() + ">");
        }

        infoXml.append("</" + fieldName + ">");
        return infoXml.toString();
    }

    public void generateXml(StringBuilder infoXml, String certInfoExtension, CertMetadata metadata,
            CertMetadata parentdata) {
        JSONObject extension = JSONObject.parseObject(certInfoExtension);
        String fieldName = metadata.getFieldname();
        // 获取元数据中文名称
        if ("image".equals(StringUtil.toLowerCase(metadata.getFieldtype()))) {
            // 获取图片cliengguid
            String cliengguid = extension.getString(fieldName);
            // 查询附件信息
            List<FrameAttachStorage> frameAttachStorages = attachService.getAttachListByGuid(cliengguid);
            if (ValidateUtil.isNotBlankCollection(frameAttachStorages)) {
                // 获取附件
                FrameAttachStorage attachStorage = frameAttachStorages.get(0);
                // 获取附件内容
                InputStream inputStream = attachStorage.getContent();
                if (inputStream != null) {
                    // 转换成字节数据
                    byte[] bytes = FileManagerUtil.getContentFromInputStream(inputStream);
                    // 写入Base64位编码字符串
                    infoXml.append(Base64Util.encode(bytes));
                }
            }
        } // 日期格式
        else if ("datetime".equals(StringUtil.toLowerCase(metadata.getFieldtype()))) {
            try {
                // 获取日期
                Date date = EpointDateUtil.convertString2DateAuto(extension.getString(fieldName));
                if (date != null) {
                    // 获取日期格式
                    String dateFormat = metadata.getDateFormat();
                    if (StringUtil.isNotBlank(dateFormat)) {
                        // 按配置的日期格式进行格式化
                        infoXml.append(EpointDateUtil.convertDate2String(date, dateFormat));
                    }
                    else {
                        // 按默认的日期格式进行格式化
                        infoXml.append(EpointDateUtil.convertDate2String(date, "yyyy年MM月dd日"));
                    }
                }
            }
            catch (Exception e) {
                // 以字符串形式获取日期
                infoXml.append(
                        StringUtil.isBlank(extension.getString(fieldName)) ? "" : extension.getString(fieldName));
                log.info(e);
            }
        }
        // 数字类型
        else if ("numeric".equals(StringUtil.toLowerCase(metadata.getFieldtype()))
                || "integer".equals(StringUtil.toLowerCase(metadata.getFieldtype()))) {
            // TODO 数字类型暂当作字符串处理
            infoXml.append(StringUtil.isBlank(extension.getString(fieldName)) ? "" : extension.getString(fieldName));
        }
        // 文本格式
        else {
            String value = extension.getString(fieldName);
            if (StringUtil.isNotBlank(value)) {
                String codeName = metadata.getDatasource_codename();
                if (StringUtil.isNotBlank(codeName)) {
                    String itemText = iCodeItemsService.getItemTextByCodeName(codeName, value);
                    if (StringUtil.isNotBlank(itemText)) {
                        infoXml.append(itemText);
                    }
                }
                else {
                    infoXml.append(value);
                }
            }
        }
    }
}
