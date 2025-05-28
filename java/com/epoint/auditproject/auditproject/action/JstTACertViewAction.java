package com.epoint.auditproject.auditproject.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.AppletOfficeWebUrlEncryptUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.attach.service.FrameAttachInfoNewService9;
import com.epoint.jnzwfw.projectdocandcert.api.IProjectdocandcertService;
import com.epoint.jnzwfw.projectdocandcert.api.entity.Projectdocandcert;
import com.epoint.sm2util.SM2Utils;
import com.epoint.sm2util.Util;
import com.epoint.ta.httprequest.util.TaHttpRequestUtils;
import com.epoint.xmz.jncertrecord.api.IJnCertRecordService;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.epoint.basic.auditcert.api.IAuditCertChangeServiceTA;

/**
 * 材料预览页面对应的后台
 *
 * @author Administrator
 */
@RestController("jsttacertviewaction")
@Scope("request")
public class JstTACertViewAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -2848400623616957620L;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private List<SelectItem> materialModel = null;

    private AuditProjectMaterial dataBean = null;

    private String projectGuid = "";
    private String certownertype = "";
    private String certNum = "";

    private String certinfo = "";

    String materialStatus = "";
    String submittype = "";
    String auditstatus = "";
    String materialguid = "";
    String info = "";
    String url = "";
    String type = "";
    String cert_identifier = "";
    String QRcodeAttachguid = "";

    String jstcodeAttachguid = "";

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IAuditProject projectService;

    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;

    @Autowired
    IHandleMaterial handleMaterialService;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private ICertInfoExternal certInfoExternalImpl;

    @Autowired
    private ICertCatalog iCertCatalg;

    @Autowired
    private IAuditTaskMaterial taskMaterialService;

    static BASE64Encoder encoder = new BASE64Encoder();
    static BASE64Decoder decoder = new BASE64Decoder();

    @Autowired
    private IJNAuditProject taAuditProjectService;

    @Autowired
    private IProjectdocandcertService projectdocandcertService;

    @Autowired
    private IJnCertRecordService iJnCertRecordService;


    /**
     * 电子身份证参数
     */
    private String privatekey = "00be74f417ec9e92fa1532d91c5a011d57b9d2ed88343b0dd716e5d3648f9b92d9";
    private String accessId = "JNS_SPJYC";
    private String accessToken = "A17B2F35B3E243B59852F182316E4414";

    @Override
    public void pageLoad() {

        projectGuid = getRequestParameter("projectGuid");
        certownertype = getRequestParameter("certownertype");
        certNum = getRequestParameter("certNum");
        cert_identifier = getRequestParameter("cert_identifier");
        jstcodeAttachguid = getRequestParameter("jstattachguid");

        // 获取存储路径
        if (StringUtil.isNotBlank(cert_identifier)) {
            String returnUrl = getQRcodePicinfo();
            url = returnUrl.split("_SPLIT_")[0];
            QRcodeAttachguid = returnUrl.split("_SPLIT_")[1];
            addCallbackParam("cert_identifier", cert_identifier);
            addCallbackParam("QRcodeAttachguid", QRcodeAttachguid);
        } else {
            url = getRequestParameter("certinfo");
        }

        String officeWeb365Server = handleConfigService.getFrameConfig("AS_OfficeWeb365_Server", "").getResult();
        addCallbackParam("officeWeb365", officeWeb365Server);

        if (dataBean == null) {
            dataBean = new AuditProjectMaterial();
        }
        // 材料下拉框
        getMaterialModel();

        String[] arrCertOwenerType = certownertype.split(";");

        String tempcertownertype = "";

        String newcertownertype = "";
        for (String type : arrCertOwenerType) {
            if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(type))) {
                tempcertownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
            } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(type))) {
                tempcertownertype = ZwfwConstant.CERTOWNERTYPE_FR;
            }
            newcertownertype += tempcertownertype + ";";
        }
        if (!StringUtil.isBlank(newcertownertype)) {
            certownertype = newcertownertype.substring(0, newcertownertype.length() - 1);
        }

        addCallbackParam("targetPath", url);

    }

    public void showAttach(String attachGuid) throws IOException {
        FrameAttachInfo frameAttachInfo = attachService.getAttachInfoDetail(attachGuid);
        String contentType = frameAttachInfo.getContentType();
        String path = "";
        if (".jpeg".equals(contentType) || ".jpg".equals(contentType) || ".gif".equals(contentType)
                || ".png".equals(contentType) || ".bmp".equals(contentType) || ".JPG".equals(contentType)) {
            FrameAttachStorage frameAttachStorage = attachService.getAttach(attachGuid);
            String filePath = ClassPathUtil.getDeployWarPath() + "AttachStorage/" + attachGuid + "/"
                    + frameAttachInfo.getAttachFileName();
            path = "AttachStorage/" + attachGuid + "/" + frameAttachInfo.getAttachFileName();
            File file = new File(filePath);
            if (!file.exists()) {
                new File(ClassPathUtil.getDeployWarPath() + "AttachStorage/" + attachGuid).mkdirs();
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                // 读文件
                InputStream in = frameAttachStorage.getContent();
                byte[] b = new byte[1024];
                int len = 0;
                // 写文件
                while ((len = in.read(b)) != -1) {
                    fileOutputStream.write(b, 0, len);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                in.close();

            }
        }
        addCallbackParam("attachguid", attachGuid);
        addCallbackParam("contentType", contentType);
        addCallbackParam("url", path);
    }

    /**
     * 获取材料名
     *
     * @return
     */
    public List<SelectItem> getMaterialModel() {
        if (materialModel == null) {
            materialModel = new ArrayList<>();
            String areaCode = getRequestParameter("areacode");
            if (StringUtil.isBlank(areaCode)) {
                areaCode = ZwfwUserSession.getInstance().getAreaCode();
            }
            AuditProject auditProject = projectService.getAuditProjectByRowGuid(projectGuid, areaCode).getResult();
            Map<String, Integer> caseMap = null;
            if (auditProject != null && StringUtil.isNotBlank(auditProject.getTaskcaseguid())
                    && !"none".equals(auditProject.getTaskcaseguid())) {
                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(auditProject.getTaskcaseguid()).getResult();
                caseMap = new HashMap<>();
                //转成map方便查找
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                    }
                }
            }
            List<Record> projectMaterials = handleMaterialService.getProjectMaterialALL(projectGuid, "").getResult();
            String belongType = "";
            String banlitype = "";
            String certareaguid = "";
            if (projectMaterials != null) {
                for (Record projectMaterial : projectMaterials) {
                    if (!"20".equals(projectMaterial.get("SUBMITTYPE"))
                            && (caseMap == null || caseMap.containsKey(projectMaterial.get("taskmaterialguid")))) {
                        AuditTaskMaterial auditTaskMaterial = taskMaterialService
                                .getAuditTaskMaterialByRowguid(projectMaterial.get("taskmaterialguid")).getResult();

                        if (auditTaskMaterial != null) {
                            banlitype = auditTaskMaterial.get("cert_applyertype");
                        }
                        if (StringUtil.isNotBlank(jstcodeAttachguid)) {
                            materialModel.add(new SelectItem(projectMaterial.get("MaterialInstanceGuid") + ";"
                                    + projectMaterial.get("CERTINFOINSTANCEGUID") + ";"
                                    + projectMaterial.get("SHAREMATERIALGUID") + ";" + banlitype + ";" + belongType
                                    + ";" + certareaguid + ";" + projectMaterial.get("ROWGUID") + ";"
                                    + projectMaterial.get("cliengguid") + ";" + projectMaterial.get("MATERIALNAME"),
                                    projectMaterial.get("MATERIALNAME")));
                        } else {
                            if (projectMaterial.get("MATERIALNAME").toString().contains("身份")
                                    || projectMaterial.get("MATERIALNAME").toString().contains("营业执照")
                                    || projectMaterial.get("MATERIALNAME").toString().contains("结婚")
                            ) {
                                materialModel.add(new SelectItem(projectMaterial.get("MaterialInstanceGuid") + ";"
                                        + projectMaterial.get("CERTINFOINSTANCEGUID") + ";"
                                        + projectMaterial.get("SHAREMATERIALGUID") + ";" + banlitype + ";" + belongType
                                        + ";" + certareaguid + ";" + projectMaterial.get("ROWGUID") + ";"
                                        + projectMaterial.get("cliengguid") + ";" + projectMaterial.get("MATERIALNAME"),
                                        projectMaterial.get("MATERIALNAME")));
                            }
                        }


                    }
                }
            }
        }
        return this.materialModel;
    }

    /**
     * 材料核对
     *
     * @throws IOException
     */
    public void checkMaterial(String rowguid, String cliengguid, String certname) throws IOException {
        log.info("cert_identifier:"+cert_identifier);
        log.info("jstattachguid:"+getRequestParameter("jstattachguid"));
        log.info("jstcodeAttachguid:"+jstcodeAttachguid);
        log.info("rowguid:"+rowguid);
        log.info("cliengguid:"+cliengguid);
        log.info("certname:"+certname);
        if (StringUtil.isBlank(cert_identifier) && StringUtil.isBlank(getRequestParameter("jstattachguid"))) {

            if (StringUtil.isNotBlank(cliengguid)) {

                String statsu = taAuditProjectService.getMaterialStatus(cliengguid);
                log.info("statsu:"+statsu);
                if (20 == Integer.parseInt(statsu)) {
                    addCallbackParam("exit", "exit");
                    return;
                }

                File file = new File(url);

                // 根据路径获取文件名
                String filename = url.substring(url.lastIndexOf("/") + 1);

                String attachguid = "";
                String attachstorageguid = "";
                String attchpath = "/opt/epoint/";
                log.info("attchpath:"+attchpath);
                attachguid = UUID.randomUUID().toString();
                attachstorageguid = attachguid;
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNowStr = sdf.format(date);

                // 判断当前系统   b
                boolean flag = isOSLinux();
                String attPath;
                // 保存文件路径
                if (flag) {
                    attPath = attchpath + "BigFileUpLoadStorage" + File.separator + "temp" + File.separator + dateNowStr
                            + File.separator + attachstorageguid + File.separator;
                } else {
                    attPath = attchpath + "BigFileUpLoadStorage/temp/" + dateNowStr + "/" + attachstorageguid + "/";
                }
                log.info("attPath:"+attPath);
                ByteArrayOutputStream bos = null;
                BufferedInputStream in = null;
                try {
                    bos = new ByteArrayOutputStream((int) file.length());
                    in = new BufferedInputStream(new FileInputStream(file));


                    int bufSize = 1024;
                    byte[] buffer = new byte[bufSize];
                    int len = 0;
                    while (-1 != (len = in.read(buffer, 0, bufSize))) {
                        bos.write(buffer, 0, len);
                    }

                    String newpath = attPath + filename;
                    File filepath = new File(newpath);

                    File newfile = new File(attPath);
                    if (!newfile.exists()) {
                        newfile.mkdirs();

                        File photo = new File(newfile, filename);
                        if (!photo.exists()) {
                            photo.createNewFile();
                        }

                        // 写文件
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
                        FileOutputStream fops;
                        try {
                            fops = new FileOutputStream(filepath);
                            fops.write(readInputStream(inputStream));
                            fops.flush();
                            fops.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        String operateUserName = UserSession.getInstance().getDisplayName();

                        FrameAttachInfoNewService9 frameAttachInfoNewService = new FrameAttachInfoNewService9();
                        FrameAttachInfo frameattachinfo = new FrameAttachInfo();
                        frameattachinfo.setAttachGuid(attachguid);
                        frameattachinfo.setAttachFileName(filename);
                        frameattachinfo.setContentType(".png");
                        frameattachinfo.setAttachLength(file.length());
                        frameattachinfo.setAttachConnectionStringName("附件存储");
                        frameattachinfo.setCliengGuid(cliengguid);
                        frameattachinfo.setStorageType("NasShareDirectory");
                        frameattachinfo.setFilePath(attPath);
                        frameattachinfo.setUploadUserDisplayName(operateUserName);
                        frameattachinfo.setUploadDateTime(new Date());
                        frameattachinfo.setAttachStorageGuid(attachstorageguid);
                        frameAttachInfoNewService.addFrameAttach(frameattachinfo);

                        // 获取材料的属性值
                        List<Record> materials = handleMaterialService.getProjectMaterialALL(projectGuid, "").getResult();
                        if (materials != null) {
                            for (Record projectMaterial : materials) {
                                if (projectMaterial.get("ROWGUID").toString().equals(rowguid)) {
                                    materialStatus = projectMaterial.get("ROWGUID").toString();
                                    submittype = projectMaterial.get("submittype").toString();
                                    auditstatus = projectMaterial.get("auditstatus").toString();
                                    materialguid = projectMaterial.get("materialinstanceguid").toString();
                                }
                                // 材料提交后，更改材料对应的属性值
                                if (projectMaterial.get("cliengguid").equals(cliengguid)) {
                                    taAuditProjectService.updateMaterialStatus(cliengguid);
                                }
                            }
                        }

                        // 保存调用记录
                        Projectdocandcert docandcert = projectdocandcertService.getInfoByProjectGuid(projectGuid);
                        Record record = projectdocandcertService.getProjectAndTaskinfo(projectGuid);
                        if (docandcert != null) {
                            docandcert.setCertcard(docandcert.getCertcard() + 1);
                            docandcert.setAcceptdate(record.getDate("ACCEPTUSERDATE"));
                            docandcert.setBanjiedate(record.getDate("banjiedate"));
                            projectdocandcertService.update(docandcert);
                        } else {
                            Projectdocandcert projectdocandcert = new Projectdocandcert();
                            projectdocandcert.setRowguid(UUID.randomUUID().toString());
                            projectdocandcert.setProjectguid(projectGuid);
                            projectdocandcert.setOuguid(record.getStr("cbdwguid"));
                            projectdocandcert.setOuname(record.getStr("cbdwname"));
                            projectdocandcert.setWindowguid(record.getStr("WINDOWGUID"));
                            projectdocandcert.setWindowname(record.getStr("WINDOWNAME"));
                            projectdocandcert.setTaskguid(record.getStr("TASKGUID"));
                            projectdocandcert.setAcceptdate(record.getDate("ACCEPTUSERDATE"));
                            projectdocandcert.setBanjiedate(record.getDate("banjiedate"));
                            projectdocandcert.setCertcard(1);
                            projectdocandcert.setClbzdoc(0);
                            projectdocandcert.setSldoc(0);
                            projectdocandcert.setBysldoc(0);
                            projectdocandcert.setByxkdoc(0);
                            projectdocandcert.setZyxkdoc(0);
                            projectdocandcert.setBusinesslicense(0);
                            projectdocandcertService.insert(projectdocandcert);
                        }

                        info = "true" + ";" + materialStatus + ";" + submittype + ";" + auditstatus + ";" + materialguid
                                + ";" + projectGuid;
                        addCallbackParam("exit", info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null) {
                        bos.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }

            } else {
                addCallbackParam("exit", "");
            }
        } else if (StringUtil.isNotBlank(jstcodeAttachguid)) {
            List<Record> materials = handleMaterialService.getProjectMaterialALL(projectGuid, "").getResult();
            if (materials != null) {
                for (Record projectMaterial : materials) {
                    if (projectMaterial.get("ROWGUID").toString().equals(rowguid)) {
                        materialStatus = projectMaterial.get("ROWGUID").toString();
                        submittype = projectMaterial.get("submittype").toString();
                        auditstatus = projectMaterial.get("auditstatus").toString();
                        materialguid = projectMaterial.get("materialinstanceguid").toString();
                    }
                    // 材料提交后，更改材料对应的属性值
                    if (projectMaterial.get("cliengguid").equals(cliengguid)) {
                        taAuditProjectService.updateMaterialStatus(cliengguid);
                        taAuditProjectService.updateFrameCliengguid(jstcodeAttachguid, cliengguid);
                    }
                }
            }
            info = "true" + ";" + materialStatus + ";" + submittype + ";" + auditstatus + ";" + materialguid + ";"
                    + projectGuid;
            addCallbackParam("exit", info);

        } else {

            List<Record> materials = handleMaterialService.getProjectMaterialALL(projectGuid, "").getResult();
            if (materials != null) {
                for (Record projectMaterial : materials) {
                    if (projectMaterial.get("ROWGUID").toString().equals(rowguid)) {
                        materialStatus = projectMaterial.get("ROWGUID").toString();
                        submittype = projectMaterial.get("submittype").toString();
                        auditstatus = projectMaterial.get("auditstatus").toString();
                        materialguid = projectMaterial.get("materialinstanceguid").toString();
                    }
                    // 材料提交后，更改材料对应的属性值
                    if (projectMaterial.get("cliengguid").equals(cliengguid)) {
                        taAuditProjectService.updateMaterialStatus(cliengguid);
                        taAuditProjectService.updateFrameCliengguid(QRcodeAttachguid, cliengguid);
                    }
                }
            }
            info = "true" + ";" + materialStatus + ";" + submittype + ";" + auditstatus + ";" + materialguid + ";"
                    + projectGuid;
            addCallbackParam("exit", info);
        }

    }

    /**
     * 判断当前的系统
     *
     * @return
     */
    public static boolean isOSLinux() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取附件存储路径
     */
    public String getAttachConfig(String configname) {
        //String sql = "select ATTACH_CONNECTIONSTRING from frame_attachconfig where ATTACH_CONNECTIONSTRINGNAME=?1";
        //String attachConnectionString = dao.queryString(sql, configname);
        String attachConnectionString = taAuditProjectService.getAttachConnect(configname);
        //dao.close();
        return attachConnectionString;
    }

    //查询证照
    public void searchCert(String sharematerialguid) {

        String[] arrcertNum = certNum.split(";");
        String[] arrcertType = certownertype.split(";");
        List<CertInfo> certInfoList = new ArrayList<CertInfo>();
        List<CertInfo> certInfoListtemp = new ArrayList<CertInfo>();
        int i = 0;
        for (String cert : arrcertNum) {
            certInfoListtemp = certInfoExternalImpl.selectCertByOwner(sharematerialguid, arrcertType[i], "", cert,
                    false, null);
            for (CertInfo certtemp : certInfoListtemp) {
                if (certtemp != null) {
                    certInfoList.add(certtemp);
                }
            }
            i++;
        }
        if (certInfoList != null) {
            JSONArray jsonArray = new JSONArray();
            String certareaGuids = "";
            for (CertInfo certInfo : certInfoList) {
                List<FrameAttachInfo> frameAttachInfos = attachService
                        .getAttachInfoListByGuid(certInfo.getCertcliengguid());
                if (frameAttachInfos != null) {
                    for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("attachguid", frameAttachInfo.getAttachGuid());
                        jsonObject.put("certname", certInfo.getCertname());
                        jsonObject.put("certguid", certInfo.getRowguid());

                        //找附件最新证照目录标识
                        CertCatalog certCatalog = iCertCatalg
                                .getLatestCatalogDetailByCatalogid(certInfo.getCertcatalogid());
                        certareaGuids = certCatalog.getRowguid();
                        //判断有没有父节点目录，找到后补充（目前只写了两级情况）
                        certCatalog = iCertCatalg.getLatestCatalogDetailByCatalogid(certInfo.getParentcertcatalogid());
                        if (certCatalog != null) {
                            certareaGuids += "," + certCatalog.getRowguid();
                        }
                        jsonObject.put("certareaguid", certareaGuids);
                        jsonObject.put("certcliengguid", certInfo.getCertcliengguid());
                        String pic = "";
                        if (".jpeg".equals(frameAttachInfo.getContentType())
                                || ".jpg".equals(frameAttachInfo.getContentType())
                                || ".gif".equals(frameAttachInfo.getContentType())
                                || ".png".equals(frameAttachInfo.getContentType())
                                || ".bmp".equals(frameAttachInfo.getContentType())
                                || ".JPG".equals(frameAttachInfo.getContentType())) {
                            pic = "pic";
                        }
                        jsonObject.put("contenttype", pic);
                        jsonArray.add(jsonObject);
                    }
                }
            }
            addCallbackParam("certs", jsonArray);
        }
    }

    /**
     * 编码转换
     *
     * @param src
     * @return
     */
    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * 调用接口获取图片
     *
     * @description
     * @author shibin
     * @date 2020年6月9日 下午2:19:04
     */
    public String getQRcodePicinfo() {

        String targetPath = null;
        String attachguid = UUID.randomUUID().toString();
        try {
            String getpdfreason = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
                    + "\"},\"data\": {\"certIdentifier\": \"" + cert_identifier
                    + "\", \"useCause\":\"济宁政务服务\",\"expiryTime\": \"\"}}";
            String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrl";
            String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, getpdfreason, "", "");

            //记录调用证照的调用次数
            AuditProject project = projectService.getAuditProjectByRowGuid("taskguid", projectGuid, null).getResult();
            if (project != null) {
                AuditTask task = iAuditTask.getAuditTaskByGuid(project.getTaskguid(), false).getResult();
                if (task != null) {
                    JnCertRecord record = new JnCertRecord();
                    record.setOperatedate(new Date());
                    record.setRowguid(UUID.randomUUID().toString());
                    record.setAreacode(task.getAreacode());
                    record.setRecordtotal("1");
                    record.set("ouname", task.getOuname());
                    record.set("ouguid", task.getOuguid());
                    record.set("type", "1");//线下调用
                    record.set("taskname", task.getTaskname());
                    record.set("itemid", task.getItem_id());
                    record.set("certnum", "11100000000013127D001");
                    iJnCertRecordService.insert(record);
                }
            }


            JSONObject getpdfresultjson = JSONObject.parseObject(getpdfresult);
            JSONObject getpdfheadjson = getpdfresultjson.getJSONObject("head");
            log.info("status:"+getpdfresultjson.getString("status"));
            if ("0".equals(getpdfheadjson.getString("status"))) {

                byte[] decrypt2 = SM2Utils.decrypt(Util.hexToByte(privatekey),
                        Util.hexToByte(getpdfresultjson.getString("data")));

                // 将字节数组转为字符串
                String getpdfbackresult = new String(decrypt2, "utf-8");

                JSONObject getpdfbacjson = JSONObject.parseObject(getpdfbackresult);
                String getpdfcontent = getpdfbacjson.getString("content");

                URL url = new URL(getpdfcontent);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 设置超时间
                conn.setConnectTimeout(10 * 1000); // 防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                InputStream inputStream = conn.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();

                InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());

                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                long size = (long) inputStream.available();
                frameAttachInfo.setAttachGuid(attachguid);
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName(certNum + ".ofd");
                frameAttachInfo.setCliengTag("电子身份证");
                frameAttachInfo.setUploadUserGuid(userSession.getUserGuid());
                frameAttachInfo.setUploadUserDisplayName(userSession.getDisplayName());
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType(".ofd");
                frameAttachInfo.setAttachLength(size);
                attachService.addAttach(frameAttachInfo, stream1);


            }
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
        targetPath = targetPath + "_SPLIT_" + attachguid;
        return targetPath;
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
     * [office365加密]
     *
     * @param url
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void getEncryptUrl(String url) {
        String encryptUrl = AppletOfficeWebUrlEncryptUtil.getEncryptUrl(url);
        if (StringUtil.isNotBlank(encryptUrl)) {
            addCallbackParam("encryptUrl", encryptUrl);
        }
    }


    public AuditProjectMaterial getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditProjectMaterial dataBean) {
        this.dataBean = dataBean;
    }

    public String getCertinfo() {
        return certinfo;
    }

    public void setCertinfo(String certinfo) {
        this.certinfo = certinfo;
    }

}
