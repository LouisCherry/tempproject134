package com.epoint.auditproject.auditproject.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
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
import com.epoint.sm2util.SM2Utils;
import com.epoint.sm2util.Util;
import com.epoint.ta.httprequest.util.TaHttpRequestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

//import com.epoint.basic.auditcert.api.IAuditCertChangeServiceTA;

/**
 * 材料预览页面对应的后台
 *
 * @author Administrator
 */
@RestController("talegalcertviewaction")
@Scope("request")
public class TALegalCertViewAction extends BaseController {
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
    private ICertInfoExternal certInfoExternalImpl;

    @Autowired
    private ICertCatalog iCertCatalg;

    @Autowired
    private IAuditTaskMaterial taskMaterialService;

    @Autowired
    private IJNAuditProject taAuditProjectService;


    /**
     * 电子身份证参数
     */
    private String privatekey = "641585FCDB9B5D71F3456AE9FC5A1FAA6E14D1647A46A037ECF6FCC48A2F9F77";
    private String accessId = "36c98bbb1d1e4a609559570a9c8744fa";
    private String accessToken = "4f48cdb7736b411a95455dd2d3fbda92";

    @Override
    public void pageLoad() {

        projectGuid = getRequestParameter("projectGuid");
        certownertype = getRequestParameter("certownertype");
        certNum = getRequestParameter("certNum");
        cert_identifier = getRequestParameter("cert_identifier");

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

                        if (projectMaterial.get("MATERIALNAME").toString().contains("营业执照")) {
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
        return this.materialModel;
    }

    /**
     * 材料核对
     *
     * @throws IOException
     */
    public void checkMaterial(String rowguid, String cliengguid, String pdfattachguid, String proguid) throws IOException {

        if (StringUtil.isNotBlank(cliengguid) && StringUtil.isNotBlank(pdfattachguid)) {
            FrameAttachInfo frameAttachInfo = attachService.getAttachInfoDetail(pdfattachguid);
            if (frameAttachInfo != null) {
                frameAttachInfo.setCliengGuid(cliengguid);
                attachService.updateAttach(frameAttachInfo, null);
                List<Record> materials = handleMaterialService.getProjectMaterialALL(projectGuid, "").getResult();

                if (materials != null) {
                    for (Record projectMaterial : materials) {
                        if (projectMaterial.get("ROWGUID").toString().equals(rowguid)) {
                            materialStatus = projectMaterial.getStr("ROWGUID");
                            submittype = projectMaterial.getStr("submittype");
                            auditstatus = projectMaterial.getStr("auditstatus");
                            materialguid = projectMaterial.getStr("materialinstanceguid");
                            // 材料提交后，更改材料对应的属性值
                            taAuditProjectService.updateMaterialStatus(cliengguid, proguid);
                        }
                    }
                }
                info = "true" + ";" + materialStatus + ";" + submittype + ";" + auditstatus + ";" + materialguid + ";"
                        + projectGuid;
                addCallbackParam("exit", info);
            }
        } else {
            addCallbackParam("exit", "noselect");
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
            String getpdfhttpUrl = "http://59.206.216.230:18883/CertShare/downloadUrl";
            String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, getpdfreason, "", "");
            JSONObject getpdfresultjson = JSONObject.parseObject(getpdfresult);
            JSONObject getpdfheadjson = getpdfresultjson.getJSONObject("head");
            if ("0".equals(getpdfheadjson.getString("status"))) {

                byte[] decrypt2 = SM2Utils.decrypt(Util.hexToByte(privatekey),
                        Util.hexToByte(getpdfresultjson.getString("data")));

                // 将字节数组转为字符串
                String getpdfbackresult = new String(decrypt2, "utf-8");

                JSONObject getpdfbacjson = JSONObject.parseObject(getpdfbackresult);
                String getpdfcontent = getpdfbacjson.getString("content");
                getpdfcontent = "http://59.206.216.230:18883" + getpdfcontent;

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

                InputStream stream2 = new ByteArrayInputStream(baos.toByteArray());

                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                long size = (long) inputStream.available();
                frameAttachInfo.setAttachGuid(attachguid);
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName(certNum + ".pdf");
                frameAttachInfo.setCliengTag("电子身份证");
                frameAttachInfo.setCliengInfo("政务平台调用");
                frameAttachInfo.setUploadUserGuid(userSession.getUserGuid());
                frameAttachInfo.setUploadUserDisplayName(userSession.getDisplayName());
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType(".pdf");
                frameAttachInfo.setAttachLength(size);
                attachService.addAttach(frameAttachInfo, stream1);

                // 生成word文件到指定的位置
                targetPath = ClassPathUtil.getDeployWarPath().replace("\\", "/") + "AttachStorage/CertCard/"
                        + attachguid + ".pdf";

                File file = new File(targetPath);
                FileOutputStream fops = null;

                try {
                    fops = new FileOutputStream(file);

                    fops.write(readInputStream(stream2));
                    fops.flush();
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        fops.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

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
