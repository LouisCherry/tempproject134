package com.epoint.cert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.cert.api.IJnCertInfo;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certcorrection.domain.CertCorrection;
import com.epoint.cert.basic.certcorrection.inter.ICertCorrection;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.certsignatureca.domain.CertSignatureca;
import com.epoint.cert.common.api.IGenerateCert;
import com.epoint.cert.commonutils.*;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.util.CertCheckUtil;
import com.epoint.cert.util.CertCheckUtilsNew;
import com.epoint.cert.util.PushTokenUtil;
import com.epoint.cert.util.PushUtil;
import com.epoint.common.cert.authentication.KinggridClassLoader;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
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
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
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
import com.suwell.ofd.custom.wrapper.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JNGenerateCert implements IGenerateCert {

    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(JNGenerateCert.class);
    /**
     * 附件操作服务类
     */
    private IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

    /**
     * 元数据api
     */
    private ICertMetaData iCertMetaData = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);

    /**
     * 证照基本信息api
     */
    private ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);

    private ICertAttachExternal iCertAttachExternal = ContainerFactory.getContainInfo()
            .getComponent(ICertAttachExternal.class);

    /**
     * 代码项api
     */
    private ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo()
            .getComponent(ICodeItemsService.class);

    private IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);

    private ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);

    private ICertCatalog iCertCatalog = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);

    private IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

    private IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);

    private IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

    private IJnCertInfo iJnCertInfo = ContainerFactory.getContainInfo().getComponent(IJnCertInfo.class);

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
    private static ExecutorService executorService = new ThreadPoolExecutor(CertConstant.CONSTANT_INT_ONE,
            CertConstant.CONSTANT_INT_ONE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            namedThreadFactory);

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    @Override
    public Map<String, String> generateCert(String arg0, String arg1, String[] arg2, Object[] arg3, String arg4,
                                            String arg5) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String> generateCert(String templetCliengGuid, String certCliengguid, String[] fieldNames,
                                            Object[] values, String userGuid, String userDisPlayName, Map<String, Record> imageMap, DataSet dataSet,
                                            CertInfo certInfo, boolean isGenerateCopy) {
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap = generateLicenseSupportCopy(templetCliengGuid, isGenerateCopy, userGuid, userDisPlayName, fieldNames,
                values, imageMap, dataSet, certInfo, "", "");
        return returnMap;
    }

    @Override
    public String generateCertinfoCode(CertInfo arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String generateNotCertcatcode(CertCatalog arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private void handleField(DocumentBuilder builder, String kcs, String fieldName) {
        try {
            if (kcs.contains(fieldName)) {
                builder.moveToMergeField(fieldName);
                builder.getFont().setName("Wingdings 2");
                builder.write("\uF052");
            } else {
                builder.moveToMergeField(fieldName);
                builder.write("");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成证照（支持正副本生成）
     *
     * @param cliengGuid      模板文件（正本或副本）
     * @param isGenerateCopy  是否生成副本
     * @param userGuid        用户Guid
     * @param userDisPlayName 用户名称
     * @param fieldNames      word域
     * @param values          word域值
     * @param imageMap        图片map
     * @param dataSet         子表数据
     * @param certInfo        基本信息对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> generateLicenseSupportCopy(String cliengGuid, boolean isGenerateCopy, String userGuid,
                                                          String userDisPlayName, String[] fieldNames, Object[] values, Map<String, Record> imageMap, DataSet dataSet,
                                                          CertInfo certInfo, String keyString, String certData) {
        CertCatalog certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certInfo.getCertcatalogid());
        StringBuffer pushText = new StringBuffer();
        log.info("======================certinfo状态：" + certInfo.getIshistory());
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
            IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            DocumentBuilder builder = new DocumentBuilder(doc);

            for (int i = 0; i < fieldNames.length; i++) {
                //value特殊字符处理
                if(values!=null && values[i]!=null && ((String)values[i]).contains("®")){
                    builder.moveToMergeField(fieldNames[0]);
                    builder.getFont().setName("Arial");
                    ((String)values[i]).replace("®","\u00AE");
                    builder.write((String)values[i]);
                }

                if ("勘察".equals(fieldNames[i])) {
                    String kcs = values[i].toString();
                    if (kcs.contains("勘察全部招标")) {
                        builder.moveToMergeField("勘察全部招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("勘察全部招标");
                        builder.write("");
                    }
                    if (kcs.contains("勘察部分招标")) {
                        builder.moveToMergeField("勘察部分招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("勘察部分招标");
                        builder.write("");
                    }
                    if (kcs.contains("勘察自行招标")) {
                        builder.moveToMergeField("勘察自行招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("勘察自行招标");
                        builder.write("");
                    }
                    if (kcs.contains("勘察委托招标")) {
                        builder.moveToMergeField("勘察委托招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("勘察委托招标");
                        builder.write("");
                    }
                    if (kcs.contains("勘察公开招标")) {
                        builder.moveToMergeField("勘察公开招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("勘察公开招标");
                        builder.write("");
                    }
                    if (kcs.contains("勘察邀请招标")) {
                        builder.moveToMergeField("勘察邀请招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("勘察邀请招标");
                        builder.write("");
                    }
                    if (kcs.contains("勘察不采用招标方式")) {
                        builder.moveToMergeField("勘察不采用招标方式");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("勘察不采用招标方式");
                        builder.write("");
                    }

                }
                else if ("设计".equals(fieldNames[i]))
                {
                    String kcs = values[i].toString();
                    if (kcs.contains("设计全部招标")) {
                        builder.moveToMergeField("设计全部招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设计全部招标");
                        builder.write("");
                    }
                    if (kcs.contains("设计部分招标")) {
                        builder.moveToMergeField("设计部分招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设计部分招标");
                        builder.write("");
                    }
                    if (kcs.contains("设计自行招标")) {
                        builder.moveToMergeField("设计自行招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设计自行招标");
                        builder.write("");
                    }
                    if (kcs.contains("设计委托招标")) {
                        builder.moveToMergeField("设计委托招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设计委托招标");
                        builder.write("");
                    }
                    if (kcs.contains("设计公开招标")) {
                        builder.moveToMergeField("设计公开招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设计公开招标");
                        builder.write("");
                    }
                    if (kcs.contains("设计邀请招标")) {
                        builder.moveToMergeField("设计邀请招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设计邀请招标");
                        builder.write("");
                    }
                    if (kcs.contains("设计不采用招标方式")) {
                        builder.moveToMergeField("设计不采用招标方式");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设计不采用招标方式");
                        builder.write("");
                    }
                } else if ("建筑工程".equals(fieldNames[i]))
                {
                    String kcs = values[i].toString();
                    if (kcs.contains("建筑工程全部招标")) {
                        builder.moveToMergeField("建筑工程全部招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("建筑工程全部招标");
                        builder.write("");
                    }
                    if (kcs.contains("建筑工程部分招标")) {
                        builder.moveToMergeField("建筑工程部分招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("建筑工程部分招标");
                        builder.write("");
                    }
                    if (kcs.contains("建筑工程自行招标")) {
                        builder.moveToMergeField("建筑工程自行招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("建筑工程自行招标");
                        builder.write("");
                    }
                    if (kcs.contains("建筑工程委托招标")) {
                        builder.moveToMergeField("建筑工程委托招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("建筑工程委托招标");
                        builder.write("");
                    }
                    if (kcs.contains("建筑工程公开招标")) {
                        builder.moveToMergeField("建筑工程公开招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("建筑工程公开招标");
                        builder.write("");
                    }
                    if (kcs.contains("建筑工程邀请招标")) {
                        builder.moveToMergeField("建筑工程邀请招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("建筑工程邀请招标");
                        builder.write("");
                    }
                    if (kcs.contains("建筑工程不采用招标方式")) {
                        builder.moveToMergeField("建筑工程不采用招标方式");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("建筑工程不采用招标方式");
                        builder.write("");
                    }
                } else if ("安装工程".equals(fieldNames[i]))
                {
                    String kcs = values[i].toString();
                    if (kcs.contains("安装工程全部招标")) {
                        builder.moveToMergeField("安装工程全部招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("安装工程全部招标");
                        builder.write("");
                    }
                    if (kcs.contains("安装工程部分招标")) {
                        builder.moveToMergeField("安装工程部分招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("安装工程部分招标");
                        builder.write("");
                    }
                    if (kcs.contains("安装工程自行招标")) {
                        builder.moveToMergeField("安装工程自行招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("安装工程自行招标");
                        builder.write("");
                    }
                    if (kcs.contains("安装工程委托招标")) {
                        builder.moveToMergeField("安装工程委托招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("安装工程委托招标");
                        builder.write("");
                    }
                    if (kcs.contains("安装工程公开招标")) {
                        builder.moveToMergeField("安装工程公开招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("安装工程公开招标");
                        builder.write("");
                    }
                    if (kcs.contains("安装工程邀请招标")) {
                        builder.moveToMergeField("安装工程邀请招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("安装工程邀请招标");
                        builder.write("");
                    }
                    if (kcs.contains("安装工程不采用招标方式")) {
                        builder.moveToMergeField("安装工程不采用招标方式");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("安装工程不采用招标方式");
                        builder.write("");
                    }
                } else if ("监理".equals(fieldNames[i]))
                {
                    String kcs = values[i].toString();
                    if (kcs.contains("监理全部招标")) {
                        builder.moveToMergeField("监理全部招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("监理全部招标");
                        builder.write("");
                    }
                    if (kcs.contains("监理部分招标")) {
                        builder.moveToMergeField("监理部分招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("监理部分招标");
                        builder.write("");
                    }
                    if (kcs.contains("监理自行招标")) {
                        builder.moveToMergeField("监理自行招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("监理自行招标");
                        builder.write("");
                    }
                    if (kcs.contains("监理委托招标")) {
                        builder.moveToMergeField("监理委托招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("监理委托招标");
                        builder.write("");
                    }
                    if (kcs.contains("监理公开招标")) {
                        builder.moveToMergeField("监理公开招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("监理公开招标");
                        builder.write("");
                    }
                    if (kcs.contains("监理邀请招标")) {
                        builder.moveToMergeField("监理邀请招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("监理邀请招标");
                        builder.write("");
                    }
                    if (kcs.contains("监理不采用招标方式")) {
                        builder.moveToMergeField("监理不采用招标方式");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("监理不采用招标方式");
                        builder.write("");
                    }
                } else if ("设备".equals(fieldNames[i]))
                {
                    String kcs = values[i].toString();
                    if (kcs.contains("设备全部招标")) {
                        builder.moveToMergeField("设备全部招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设备全部招标");
                        builder.write("");
                    }
                    if (kcs.contains("设备部分招标")) {
                        builder.moveToMergeField("设备部分招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设备部分招标");
                        builder.write("");
                    }
                    if (kcs.contains("设备自行招标")) {
                        builder.moveToMergeField("设备自行招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设备自行招标");
                        builder.write("");
                    }
                    if (kcs.contains("设备委托招标")) {
                        builder.moveToMergeField("设备委托招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设备委托招标");
                        builder.write("");
                    }
                    if (kcs.contains("设备公开招标")) {
                        builder.moveToMergeField("设备公开招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设备公开招标");
                        builder.write("");
                    }
                    if (kcs.contains("设备邀请招标")) {
                        builder.moveToMergeField("设备邀请招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设备邀请招标");
                        builder.write("");
                    }
                    if (kcs.contains("设备不采用招标方式")) {
                        builder.moveToMergeField("设备不采用招标方式");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("设备不采用招标方式");
                        builder.write("");
                    }
                } else if ("其它".equals(fieldNames[i]))
                {
                    String kcs = values[i].toString();
                    if (kcs.contains("其它全部招标")) {
                        builder.moveToMergeField("其它全部招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("其它全部招标");
                        builder.write("");
                    }
                    if (kcs.contains("其它部分招标")) {
                        builder.moveToMergeField("其它部分招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("其它部分招标");
                        builder.write("");
                    }
                    if (kcs.contains("其它自行招标")) {
                        builder.moveToMergeField("其它自行招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("其它自行招标");
                        builder.write("");
                    }
                    if (kcs.contains("其它委托招标")) {
                        builder.moveToMergeField("其它委托招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("其它委托招标");
                        builder.write("");
                    }
                    if (kcs.contains("其它公开招标")) {
                        builder.moveToMergeField("其它公开招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("其它公开招标");
                        builder.write("");
                    }
                    if (kcs.contains("其它邀请招标")) {
                        builder.moveToMergeField("其它邀请招标");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("其它邀请招标");
                        builder.write("");
                    }
                    if (kcs.contains("其它不采用招标方式")) {
                        builder.moveToMergeField("其它不采用招标方式");
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField("其它不采用招标方式");
                        builder.write("");
                    }
                } else if ("事项名称2".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("事项名称4".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("事项名称5".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("合格".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("不合格".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("材料一".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("材料二".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("材料三".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("选择一".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("选择二".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                }
            }
            // 填充word域值
            doc.getMailMerge().execute(fieldNames, values);
            // 子表的渲染
            doc.getMailMerge().executeWithRegions(dataSet);

            certInfo.setIshistory(0);

            // 插入图片（包括二维码）
            String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
            String yyyzqrcode = configService.getFrameConfigValue("yyyzqrcode");
            String cxyscltxzshow = configService.getFrameConfigValue("cxyscltxzshow");
            String uploadimage = "0";
            String qrcoderas = "";

            //安许的来源于数据库
            log.info("certCatalog.get(\"is_push\")"+certInfo.get("ERWEIMA")+certCatalog.get("is_push"));
            if (certInfo!=null && certCatalog!=null && StringUtil.isNotBlank(certInfo.get("ERWEIMA")) && StringUtil.isNotBlank(certCatalog.get("is_push")) && "1".equals(certCatalog.get("is_push"))) {
                qrcodeurl = certInfo.get("ERWEIMA");
            }
            else{
                if (StringUtil.isBlank(qrcodeurl)) {
                    qrcodeurl = certInfo.getCertname();
                }
                else if ("疫情通行证".equals(certInfo.getCertname()) || "建筑工程施工许可证（济宁）".equals(certInfo.getCertname())
                        || "建筑工程施工许可证核发（梁山）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/epointzwmhwz/pages/eventdetail/publicitydetail.html?projectguid="
                            + certInfo.getStr("projectguid") + "";
                } else if ("通行证62".equals(certInfo.getCertname())
                        || frameAttachStorage.getAttachFileName().contains("道路通行证")) {
                    qrcodeurl = "http://jizwfw.sd.gov.cn/wechat.dcloud.JNZWFW/html/service/service_roadpass.html?projectguid="
                            + certInfo.getStr("projectguid") + "";
                } else if ("行业综合许可证".equals(certInfo.getCertname())) {
                    qrcodeurl = yyyzqrcode + "?certguid=" + certInfo.getRowguid();
                } else if ("超限运输车辆通行证".equals(certInfo.getCertname())) {
                    qrcodeurl = cxyscltxzshow + certInfo.getStr("cxcode");
                } else if ("省安全生产许可证".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/saqqscxkz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("省建筑工程质量检测机构资质证书".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/sjzgczljcjgzz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("省建筑业企业资质证".equals(certInfo.getCertname()) || "建筑业企业资质三级资质证".equals(certInfo.getCertname())) {
                    //扫描问题已处理
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/sjzqyzzz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("建筑业企业资质证（住建）".equals(certInfo.getCertname())) {
                    //扫描问题已处理
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/sjzqyzzz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("省房地产开发二级资质证（高新）".equals(certInfo.getCertname())) {
                    //扫描问题已处理
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/sjzqyzzz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("房地产二级资质证（经开区）".equals(certInfo.getCertname())) {
                    //扫描问题已处理
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/sjzqyzzz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("省房地产开发企业资质证书".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/fdckfqyzzzs.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("省工程监理资质证书".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/gcjlzzzs.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("省施工图审查机构认定证书".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/sgtsjgrdzs.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("医疗广告审查（中医医疗机构除外）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/ylggsc.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("消毒产品生产卫生许可电子证照".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/xdcpscqywsxk.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("建筑节能技术产品初次认定".equals(certInfo.getCertname())) {
                    //扫描问题已处理
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/jzjnjscpccrd.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("我要开网约车".equals(certInfo.getCertname())) {
                    uploadimage = "2";
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/wykwyc.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("工程设计证照".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/gcsjzz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("工程勘察设计资质证".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/gckcsjzz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("危险化学品经营许可证".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/wxhxpjyxk.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("建设项目用地预审与选址意见书（市）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/jsxmydysxzyj.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("建设用地规划许可证（市）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/jsydghxkz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("商品房预售许可（市）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/fwysxk.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("建设工程规划许可证".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/jsgcghxkz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("防空地下室易地建设审批意见书（市）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/fkdxsydjsyjs.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("人防工程防护设计文件审查意见书".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/rfgcsgtsjyj.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("特殊建设工程消防设计审查意见书（济宁）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/tsjsgcxfsjsc.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("港口经营许可证".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/gkjyxkz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("城镇污水排入排水管网许可证".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/czwsprpcgwxkz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("公共场所卫生许可新申请".equals(certInfo.getCertname()) || "公共场所卫生许可证（任城）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（微山）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（鱼台）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（金乡）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（汶上）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（泗水）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（梁山）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（曲阜）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（兖州）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（高新）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（经开）".equals(certInfo.getCertname())
                        || "公共场所卫生许可证（邹城市）".equals(certInfo.getCertname())) {
                    uploadimage = "1";
                    // qrcodeurl =
                    // "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/ggcswsxk.html?certguid="
                    // + certInfo.getRowguid() + "";
                    qrcodeurl = "";
                } else if ("城市建筑垃圾处置批准书（济宁）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（任城）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（兖州）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（经开）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（金乡）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（微山）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（鱼台）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（嘉祥）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（汶上）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（泗水）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（梁山）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（曲阜）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（邹城）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（高新）".equals(certInfo.getCertname())
                        || "城市建筑垃圾处置批准书（北湖）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/csjzljpzs.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("医疗机构放射性职业病危害建设项目预评价报告审核".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/fszljsxmbapz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("医疗机构放射性职业病危害建设项目竣工验收".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/fszljsxmjgys.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("城市大型户外广告设置批准书".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/csdxhwgg.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("港航运输经营许可证（正本）".equals(certInfo.getCertname())) {
                    uploadimage = "3";
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/ghysjyxkz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("图书期刊印刷委托书".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/tsqkyswts.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("船舶营业运输经营许可（正本）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/cbyyyszsq.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("船舶营业运输经营许可（正本）".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/cbyyyszsq.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("燃气经营许可".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/rqjyxk.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("供热经营许可电子证照".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/grjyxkdzzz.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("山东省涉及饮用水卫生安全产品许可批件".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/sjyyswsaqcpxk.html?certguid="
                            + certInfo.getRowguid() + "";
                } else if ("审查准予水行政许可决定书".equals(certInfo.getCertname())) {
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/hdglfwjsxmgcjs.html?certinfoguid="
                            + certInfo.getRowguid() + "";
                }
                else {
                    //通用二维码页面
                    qrcodeurl = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/erweimacommon.html?certguid="
                            + certInfo.getRowguid() + "";
                }
            }



            insertImagetoBookmark(doc, imageMap, true, qrcodeurl, uploadimage, certInfo.getStr("projectguid"));

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
            } else {
                doc.save(pdfFilepath, SaveFormat.PDF);
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
                    } else {
                        // 将单个办公文件（Office文件如doc等、版式文件如pdf、xps、ceb等）转换为OFD文件
                        fileOutputStream = new FileOutputStream(ofdFilepath);
                        ha.officeToOFD(FileManagerUtil.createFile(pdfFilepath), fileOutputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                } finally {
                    try {
                        ha.close(); // 注意关闭
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            EpointFrameDsManager.begin(null);
            if (!isGenerateCopy)
            {
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
                // TODO 生成正本的输出
                log.info("======================正本isGenerate" + isGenerate);
                log.info("======================正本attachguid" + attachguid);
                log.info("======================certinfo状态：" + certInfo.getIshistory());

                if (isGenerate) { // 更新
                    log.info("======================正本getCertcliengguid" + certInfo.getCertcliengguid());
                    if (StringUtil.isNotBlank(ofdserviceurl) && "ofd".equals(doctempletformat)) {
                        // 生成ofd
                        AttachUtil.updateOfdAttachNotDelPdf(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                        certInfo.getCertcatalogid(), 0),
                                ofdFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成",
                                attachguid);
                    } else {
                        // 生成pdf
                        // 生成pdf
                        String certno = certInfo.getCertno();
                        if (StringUtil.isBlank(certno)) {
                            certno = certInfo.getCertname();
                        }
                        AttachUtil.updateAttach(
                                certno + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                        certInfo.getCertcatalogid(), 0),
                                pdfFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成",
                                attachguid);
                    }

                } else { // 上传
                    if (StringUtil.isBlank(certInfo.getCertcliengguid())) {
                        certInfo.setCertcliengguid(UUID.randomUUID().toString());
                    }
                    log.info("======================正本getCertcliengguid" + certInfo.getCertcliengguid());
                    if (StringUtil.isNotBlank(ofdserviceurl) && "ofd".equals(doctempletformat)) {
                        // 生成ofd
                        AttachUtil.addOfdAttachNotDelPdf(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                        certInfo.getCertcatalogid(), 0),
                                ofdFilepath, certInfo.getCertcliengguid(), userGuid, userDisPlayName, "系统生成");
                    } else {
                        // 生成pdf
                        String certno = certInfo.getCertno();
                        if (StringUtil.isBlank(certno)) {
                            certno = certInfo.getCertname();
                        }
                        AttachUtil.addAttach(
                                certno + "-"
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
            else {
                // 生成副本
                // 是否生成过附件（副本）
                boolean isCopyGenerate = false;
                String attachguid = "";
                // 生成附件（副本）cliengguid初始化
                if (StringUtil.isBlank(certInfo.getCopycertcliengguid())) {
                    certInfo.setCopycertcliengguid(UUID.randomUUID().toString());
                    iCertInfo.updateCertInfo(certInfo);
                } else {
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

                log.info("======================副本isGenerate" + isCopyGenerate);
                log.info("======================副本attachguid" + attachguid);
                log.info("======================正本getCopycertcliengguid" + certInfo.getCopycertcliengguid());

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
                    } else {
                        // 生成pdf
                        AttachUtil.updateAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                        certInfo.getCertcatalogid(), 0)
                                        + "（副本）",
                                pdfFilepath, certInfo.getCopycertcliengguid(), userGuid, userDisPlayName, "系统生成（副本）",
                                attachguid);

                    }
                } else { // 上传
                    if (StringUtil.isNotBlank(ofdserviceurl) && "ofd".equals(doctempletformat)) {
                        // 生成ofd
                        AttachUtil.addOfdAttach(
                                certInfo.getCertno() + "-"
                                        + FormatUtil.getdecryptSM4ToData(certInfo.getCertownername(),
                                        certInfo.getCertcatalogid(), 0)
                                        + "（副本）",
                                ofdFilepath, certInfo.getCopycertcliengguid(), userGuid, userDisPlayName, "系统生成（副本）");
                    } else {
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
                    } else {
                        executorService.execute(new Runnable() {
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
            if (StringUtil.isNotBlank(pushText.toString())) {
                returnMap.put("msg", "生成证照成功！" + pushText.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        } finally {
            File file = FileManagerUtil.createFile(pdfFilepath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
        return returnMap;
    }

    /**
     * 检测目录模板文件
     *
     * @param cliengGuid     模板文件（正/副本）guid
     * @param isGenerateCopy 是否生成副本
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
            returnMap.put("msg", String.format("未正确配置证照目录模版文件（%s）！", tip));
            return returnMap;
        }

        attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        // 模版文件校验（正副本）
        List<FrameAttachStorage> frameAttachStorageList = attachService.getAttachListByGuid(cliengGuid);
        if (ValidateUtil.isBlankCollection(frameAttachStorageList)) {
            returnMap.put("msg", String.format("当前证照对应的证照目录模版文件（%s）为空，请联系管理员维护！", tip));
            return returnMap;
        }
        FrameAttachStorage frameAttachStorage = new FrameAttachStorage();
        String yqclienguid = ConfigUtil.getConfigValue("yqcliengguid");
        for (FrameAttachStorage frameAttachStorageinfo : frameAttachStorageList) {
            if (yqclienguid.equals(frameAttachStorageinfo.getCliengGuid())) {
                frameAttachStorage = frameAttachStorageinfo;
                break;
            } else {
                frameAttachStorage = frameAttachStorageList.get(0);
            }
        }
        if (StringUtil.isBlank(frameAttachStorage.getContent())) {
            returnMap.put("msg", String.format("当前证照对应的证照目录模版文件（%s）内容为空，请联系管理员维护！", tip));
            return returnMap;
        }
        returnMap.put("frameAttachStorage", frameAttachStorage);
        return returnMap;
    }

    @Override
    public String generateCertcatcode(CertCatalog arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 利用Ofd模板直接生成ofd附件
     *
     * @param frameAttachStorages
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
        Packet pack = null;
        try {
            ha = new HTTPAgent(ofdserviceurl);
            // 创建链接
            pack = new Packet(Const.PackType.COMMON, Const.Target.OFD);
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
                } else {
                    templetInputStream = frameAttachStorage.getContent();
                    infoXml = dataToXmlString(certInfoExtension, metadataList);
                    infoXmlInputStream = new ByteArrayInputStream(infoXml.getBytes("UTF-8"));
                    template = new Template("生成电子证照", templetInputStream, infoXmlInputStream);
                    pack.data(template);
                }
            }

            fileOutputStream = new FileOutputStream(ofdFilepath);
            pack.textMark(new TextInfo("该证照仅作为办理个别事件使用", "宋体", 20, "80999999"),
                    new MarkPosition(10f, 100f, 10f, 10f, MarkPosition.INDEX_ALL), true, true);
            ha.convert(pack, fileOutputStream);
            // 将合成的ofd转成输入流
            // resultInputStream = new
            // ByteArrayInputStream(resultOutputStream.toByteArray());

        } catch (PackException e) {
            e.printStackTrace();
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if(ha!=null){
                ha.close();
            }
            if (pack != null) {
                pack.close();
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

                } else {
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
            } else { // 生成副本
                // 是否生成过附件（副本）
                boolean isCopyGenerate = false;
                String attachguid = "";
                // 生成附件（副本）cliengguid初始化
                if (StringUtil.isBlank(certInfo.getCopycertcliengguid())) {
                    certInfo.setCopycertcliengguid(UUID.randomUUID().toString());
                    iCertInfo.updateCertInfo(certInfo);
                } else {
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
                } else {
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
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("issuccess", "0");
            returnMap.put("msg", "生成证照失败！");
        } finally {
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
     * 插入图片到书签
     *
     * @param doc
     * @param imageMap    图片map
     * @param isUseQrcode 是否使用二维码
     * @param qrcodeText  二维码文本
     */
    public void insertImagetoBookmark(Document doc, Map<String, Record> imageMap, boolean isUseQrcode,
                                      String qrcodeText, String uploadimage, String certrowguid) {
        // DocumentBuilder 类似一个文档操作工具，用来操作已经实例化的Document 对象，DocumentBuilder
        // 里有许多方法 例如插入文本、插入图片、插入段落、插入表格等等
        DocumentBuilder build = null;
        InputStream qrCodeInputStream = null;
        try {
            build = new DocumentBuilder(doc);
            // 是否添加二维码
            if (isUseQrcode && StringUtil.isNotBlank(qrcodeText)) {
                // 添加二维码 (读取epointframe.properties的配置)
                int qcodeWidth = 100;
                int qcodeHeight = 100;
                if ("3".equals(uploadimage)) {
                    qcodeWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("sl_cert_qrcode_width"), 100);
                    qcodeHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("sl_cert_qrcode_height"), 100);
                } else {
                    qcodeWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_width"), 100);
                    qcodeHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_height"), 100);
                }

                if (qrcodeText.contains("hdglfwjsxmgcjs")) {
                    qcodeWidth = 150;
                    qcodeHeight = 150;
                }

                qrCodeInputStream = QrcodeUtil.getQrCode(qrcodeText, qcodeWidth, qcodeHeight);
                // 书签存在
                if (qrCodeInputStream != null && getBookmark(doc, "二维码") != null) {
                    // 指定对应的书签
                    build.moveToBookmark("二维码");
                    // 插入附件流信息
                    build.insertImage(qrCodeInputStream);
                }
            }

            if ("1".equals(uploadimage) && StringUtil.isNotBlank(certrowguid)) {
                List<FrameAttachStorage> frameAttachInfoList = attachService.getAttachListByGuid(certrowguid + "yjs");
                if (!frameAttachInfoList.isEmpty()) {
                    FrameAttachStorage storage = frameAttachInfoList.get(0);
                    if (storage != null && storage.getContent() != null) {
                        InputStream content = null;
                        try {
                            // 指定对应的书签
                            build.moveToBookmark("图图");

                            content = storage.getContent();
                            Integer width = 250;
                            Integer height = 250;
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
                            // 插入附件流信息
                            build.insertImage(content, RelativeHorizontalPosition.MARGIN, 0,
                                    RelativeVerticalPosition.MARGIN, 0, GenerateUtil.mmToPixel(width),
                                    GenerateUtil.mmToPixel(height), WrapType.SQUARE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (content != null) {
                                content.close();
                            }
                        }
                    }

                }

            }

            if ("2".equals(uploadimage) && StringUtil.isNotBlank(certrowguid)) {
                List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(certrowguid).getResult();
                if (!auditSpISubapps.isEmpty()) {
                    AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                    Record rec = iCxBusService.getFrameAttachStorage(auditSpISubapp.getRowguid());
                    if (rec != null) {
                        InputStream content = null;
                        try {
                            // 指定对应的书签
                            build.moveToBookmark("图图");
                            content = rec.getInputStream("content");
                            Integer width = 250;
                            Integer height = 250;
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
                            // 插入附件流信息
                            build.insertImage(content, RelativeHorizontalPosition.MARGIN, 0,
                                    RelativeVerticalPosition.MARGIN, 0, GenerateUtil.mmToPixel(width),
                                    GenerateUtil.mmToPixel(height), WrapType.SQUARE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (content != null) {
                                content.close();
                            }
                        }
                    }
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
                                // 插入附件流信息
                                build.insertImage(content, RelativeHorizontalPosition.MARGIN, 0,
                                        RelativeVerticalPosition.MARGIN, 0, GenerateUtil.mmToPixel(width / 1.85),
                                        GenerateUtil.mmToPixel(height), WrapType.SQUARE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (content != null) {
                                    content.close();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (qrCodeInputStream != null) {
                    qrCodeInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
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

    @SuppressWarnings("finally")
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
        KinggridClassLoader classLoader = null;
        try {
            classLoader = new KinggridClassLoader();
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
            @SuppressWarnings("rawtypes")
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
            setPagen.invoke(pdfSignature4KG, new int[]{1});// 局部 默认为全部页面

            Class<?> pdfSignatureClazz = classLoader.loadClass("com.kinggrid.pdf.executes.PdfSignature");
            Method setPdfSignature = userClazz.getMethod("setPdfSignature", pdfSignatureClazz);
            setPdfSignature.invoke(hummer, pdfSignature4KG);

            Method doSignature = userClazz.getMethod("doSignature");
            doSignature.invoke(hummer);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                Throwable targetEx = ((InvocationTargetException) e).getTargetException();
                if (targetEx != null) {
                    msg = targetEx.getMessage();
                }
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (cert != null) {
                    cert.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (hummer != null) {
                try {
                    Method close = userClazz.getMethod("close");
                    close.invoke(hummer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 删除原pdf
            File file = FileManagerUtil.createFile(inFile);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            if(classLoader!=null){
                try {
                    classLoader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return msg;
    }

    public void markPdfImage(String cliengguid, String sltImagecliengguid, String rowguid, String userGuid,
                             String userDisplayName) {
        List<FrameAttachStorage> attachStorages = attachService.getAttachListByGuid(cliengguid);
        if (attachStorages.size() > 0) {
            PdfReader reader = null;
            try {
                reader = new PdfReader(attachService.getAttachListByGuid(cliengguid).get(0).getContent());
                PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(reader);

                List<Integer> statis = new ArrayList<>();
                pdfReaderContentParser.processContent(1, new RenderListener() {
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
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                PDFFile pdffile = new PDFFile(ByteBuffer
                        .wrap(IOUtils.toByteArray(attachService.getAttachListByGuid(cliengguid).get(0).getContent())));
                PDFPage page = pdffile.getPage(1);
                int width = (int) page.getBBox().getWidth();
                int height = (int) page.getBBox().getHeight();
                int imgWidth = statis.size() > 0 ? statis.get(2) : width;
                int imgHeight = statis.size() > 0 ? statis.get(3) : height;
                BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = img.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                PDFRenderer renderer = new PDFRenderer(page, g2,
                        new Rectangle(statis.size() > 0 ? -statis.get(0) : 0,
                                statis.size() > 0 ? -(height - statis.get(1) - imgHeight) : 0, width, height),
                        null, null);
                try {
                    page.waitForFinish();
                } catch (Exception e) {
                    e.printStackTrace();
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
                } else { // 有缩略图
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String dataToXmlString(CertInfoExtension certInfoExtension, List<CertMetadata> metadataList) {
        // 写入xml头部信息
        StringBuilder infoXml = new StringBuilder(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DataRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"sourcedata.xsd\">");
        for (CertMetadata certMetadata : metadataList) {
            // 获取元数据英文名称
            String fieldName = certMetadata.getFieldname();
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
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        infoXml.append("</DataRoot>");
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
                    } else {
                        // 按默认的日期格式进行格式化
                        infoXml.append(EpointDateUtil.convertDate2String(date, "yyyy年MM月dd日"));
                    }
                }
            } catch (Exception e) {
                // 以字符串形式获取日期
                infoXml.append(
                        StringUtil.isBlank(extension.getString(fieldName)) ? "" : extension.getString(fieldName));
                e.printStackTrace();
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
                } else {
                    infoXml.append(value);
                }
            }
        }
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成打印的证照doc（支持副本打印）
     *
     * @param cliengGuid      证照模板（正/副本）guid
     * @param isPrintCopy     是否打印副本
     * @param userGuid        用户Guid
     * @param userDisPlayName 用户名称
     * @param fieldNames      word域
     * @param values          word域值
     * @param imageMap        图片map
     * @param dataSet         子表数据
     * @param certinfo        基本信息
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
            } catch (Exception e) {
                e.printStackTrace();
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
            DocumentBuilder builder = new DocumentBuilder(doc);

            for (int i = 0; i < fieldNames.length; i++) {
                if ("事项名称2".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("事项名称4".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("事项名称5".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("合格".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                } else if ("不合格".equals(fieldNames[i])) {
                    if ("是".equals(values[i])) {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF052");
                    } else {
                        builder.moveToMergeField(fieldNames[i]);
                        builder.getFont().setName("Wingdings 2");
                        builder.write("\uF0A3");
                    }
                }
            }

            // 填充word域值
            doc.getMailMerge().execute(fieldNames, values);
            // 子表的渲染
            doc.getMailMerge().executeWithRegions(dataSet);
            // 插入行业综合证的二维码
            IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            IHandleConfig handleConfigService = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            String centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
            String catalogid = handleConfigService.getFrameConfig("AS_CENTER_CERT", centerGuid).getResult();
            if (StringUtil.isNotBlank(catalogid) && catalogid.equals(certinfo.getCertcatalogid())) {
                String qrcodeurl = "";
                String yyyzqrcode = configService.getFrameConfigValue("yyyzqrcode");
                if (StringUtil.isNotBlank(yyyzqrcode)) {
                    qrcodeurl = yyyzqrcode + "?certguid=" + certinfo.getRowguid();
                }
                insertImagetoBookmark(doc, imageMap, true, qrcodeurl, "0", null);
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
            } else { // 未打印，上传
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
                } else {
                    certinfo.setCopyprintdocguid(attachGuid);
                }
                iCertInfo.updateCertInfo(certinfo);
            }
            // 打印成功
            returnMap.put("issuccess", "1");
            returnMap.put("attachguid", attachGuid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
        } else {
            cliengguid = certInfo.getCertcliengguid();
        }
        if (StringUtil.isNotBlank(cliengguid)) {
            List<FrameAttachInfo> attachInfos = attachService.getAttachInfoListByGuid(cliengguid);
            if (ValidateUtil.isNotBlankCollection(attachInfos)) {
                for (FrameAttachInfo info : attachInfos) {
                    if (".ofd".equals(info.getContentType()) && "系统生成".equals(info.getCliengInfo())) {
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
            } else {
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
                } else {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnMap;
    }

}
