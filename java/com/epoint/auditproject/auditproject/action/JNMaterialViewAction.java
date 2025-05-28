package com.epoint.auditproject.auditproject.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 材料预览页面对应的后台
 *
 * @author Administrator
 */
@RestController("jnmaterialviewaction")
@Scope("request")
public class JNMaterialViewAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -2848400623616957620L;

    private List<SelectItem> materialModel = null;

    private AuditProjectMaterial dataBean = null;

    private String projectGuid = "";

    private String materialinstanceguid = "";

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IAuditProject projectService;

    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;

    @Autowired
    IAuditTaskMaterial iAuditTaskMaterialService;

    @Autowired
    IHandleMaterial handleMaterialService;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private IAuditTaskMaterial taskmaterial;


    @Override
    public void pageLoad() {
        if (StringUtil.isNotBlank(getRequestParameter("srcurl"))) {
            String baseUrl = "http://" + EncodeUtil.decode(getRequestParameter("srcurl")) + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
            addViewData("baseUrl", baseUrl);
        }
        projectGuid = getRequestParameter("projectguid");
        materialinstanceguid = getRequestParameter("materialinstanceguid");
        String officeWeb365_Server = handleConfigService.getFrameConfig("AS_OfficeWeb365_Server", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        addCallbackParam("officeWeb365", officeWeb365_Server);
        if (!isPostback()) {
            if (StringUtil.isNotBlank(materialinstanceguid)) {
                dataBean = new AuditProjectMaterial();
                dataBean.setRowguid(materialinstanceguid);
                materialChange();
            }
        }
        if (StringUtil.isNotBlank(materialinstanceguid)) {
            dataBean = new AuditProjectMaterial();
            dataBean.setRowguid(materialinstanceguid);
            materialChange();
        }
        if (dataBean == null) {
            dataBean = new AuditProjectMaterial();
        }
        if (StringUtil.isNotBlank(dataBean.getTaskmaterialguid()) && !isPostback()) {
            changeMaterial(dataBean.getTaskmaterialguid());
        }
    }

    public void changeMaterial(String taskmaterialguid) {
        AuditTaskMaterial atm = taskmaterial.getAuditTaskMaterialByRowguid(taskmaterialguid).getResult();
        if (atm != null) {
            addCallbackParam("xs", StringUtil.isNotBlank(atm.getXsreviewkp()) ? atm.getXsreviewkp() : "无");
            addCallbackParam("sz", StringUtil.isNotBlank(atm.getSzreviewkp()) ? atm.getSzreviewkp() : "无");
        }
    }

    public void materialChange() {
        dataBean = projectMaterialService.getProjectMaterialDetail(dataBean.getRowguid(), projectGuid).getResult();
        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterialService.getAuditTaskMaterialByRowguid(dataBean.getTaskmaterialguid()).getResult();
        if (StringUtil.isBlank(auditTaskMaterial.getExampleattachguid())) {
            addCallbackParam("exampleattachguid", "0");
        } else {
            addCallbackParam("exampleattachguid", auditTaskMaterial.getExampleattachguid());
        }
        addViewData("cliengguid", dataBean.getCliengguid());
        List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(dataBean.getCliengguid());
        JSONArray jsonArray = new JSONArray();
        if (frameAttachInfos != null) {
            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("attachguid", frameAttachInfo.getAttachGuid());
                jsonObject.put("attachfilename", frameAttachInfo.getAttachFileName());
                String url = OfficeWebUrlEncryptUtil.getEncryptUrl(getViewData("baseUrl") + frameAttachInfo.getAttachGuid(), frameAttachInfo.getContentType());
                jsonObject.put("encryptUrl", url);
                String[] contenttype = iConfigService.getFrameConfigValue("showPhotoViewFormat").split(";");
                List<String> contemp = Arrays.asList(contenttype);
                int subtemp = frameAttachInfo.getContentType().lastIndexOf('.');
                if (subtemp != -1 && StringUtil.isNotBlank(frameAttachInfo.getContentType()) && contemp.contains(frameAttachInfo.getContentType().substring(subtemp))) {
                    jsonObject.put("contenttype", "pic");
                } else {
                    jsonObject.put("contenttype", frameAttachInfo.getContentType().substring(subtemp));
                }
                jsonArray.add(jsonObject);
            }
        }
        changeMaterial(dataBean.getTaskmaterialguid());
        addCallbackParam("materials", jsonArray);
    }

    public void showAttach(String attachGuid) throws IOException {
        FrameAttachInfo frameAttachInfo = attachService.getAttachInfoDetail(attachGuid);
        String contentType = frameAttachInfo.getContentType();
        String path = "";
        String[] contenttypes = iConfigService.getFrameConfigValue("showPhotoViewFormat").split(";");
        if (Arrays.asList(contenttypes).contains(contentType)) {
            FrameAttachStorage frameAttachStorage = attachService.getAttach(attachGuid);
            String Filepath = ClassPathUtil.getDeployWarPath() + "AttachStorage/" + attachGuid + "/"
                    + frameAttachInfo.getAttachFileName();
            path = "AttachStorage/" + attachGuid + "/" + frameAttachInfo.getAttachFileName();
            File file = new File(Filepath);
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
                caseMap = new HashMap<>(16);
                //转成map方便查找
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                    }
                }
            }
            List<Record> projectMaterials = handleMaterialService.getProjectMaterialALL(projectGuid, "").getResult();
            if (projectMaterials != null) {
                for (Record projectMaterial : projectMaterials) {
                    if (!"20".equals(projectMaterial.get("SUBMITTYPE"))
                            && (caseMap == null || caseMap.containsKey(projectMaterial.get("taskmaterialguid")))) {
                        String tip = "";
                        if (Integer.parseInt(projectMaterial.get("status")
                                .toString()) >= ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC) {
                            tip = "（已上传）";
                        }
                        materialModel.add(new SelectItem(projectMaterial.get("MaterialInstanceGuid"),
                                tip + projectMaterial.get("MATERIALNAME")));
                    }
                }
            }
        }
        return this.materialModel;
    }

    public void getMaterialInfo() {
        String guid = getRequestParameter("guid");
        AuditProjectMaterial material = projectMaterialService.getProjectMaterialDetail(guid, projectGuid).getResult();
        if (StringUtil.isNotBlank(material.getCertinfoinstanceguid())) {
            addCallbackParam("source", "证照库");
        } else {
            addCallbackParam("source", "手动上传");
        }
        List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
        if (attachs != null && attachs.size() > 0) {
            addCallbackParam("date", attachs.get(0).getUploadDateTime());
        }
    }

    //加密365地址
    public void getOffice365Url(String exampleattachguid) {
        List<FrameAttachInfo> list = attachService.getAttachInfoListByGuid(exampleattachguid);
        if (!list.isEmpty()) {
            FrameAttachInfo frameAttachInfo = list.get(0);
            String baseUrl = WebUtil.getRequestCompleteUrl(WebUtil.getRequest())
                    + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + frameAttachInfo.getAttachGuid();
            String url = OfficeWebUrlEncryptUtil.getEncryptUrl(baseUrl, frameAttachInfo.getContentType());
            if (baseUrl.contains("https")) {
                url += "&ssl=1";
            }
            log.info("office365加密地址：" + url);
            addCallbackParam("weburl", url);
        }

    }


    public AuditProjectMaterial getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditProjectMaterial dataBean) {
        this.dataBean = dataBean;
    }
}
