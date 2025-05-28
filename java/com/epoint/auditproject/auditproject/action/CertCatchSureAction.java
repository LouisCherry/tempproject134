package com.epoint.auditproject.auditproject.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.individual.domain.AuditRsIndividualBaseinfo;
import com.epoint.basic.auditresource.individual.inter.IAuditIndividual;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handlersmaterial.inter.IHandleRSMaterial;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

/**
 * 证照关联页面匹配确认后台
 */
@RestController("certcatchsureaction")
@Scope("request")
public class CertCatchSureAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;

    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    @Autowired
    private IAuditProject projectService;

    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;

    @Autowired
    IAuditTaskMaterial iaudittaskmaterial;

    @Autowired
    IHandleMaterial handleMaterialService;

    @Autowired
    private ICertInfoExternal certInfoExternalImpl;

    @Autowired
    private ICertAttachExternal certAttachExternalService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IAuditProjectMaterial auditProjectMaterialImpl;

    @Autowired
    private IHandleRSMaterial rsMateralService;

    @Autowired
    private IAuditIndividual iAuditIndividual;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;

    @Autowired
    private IAuditSpISubapp iauditspisubapp;

    @Autowired
    private IAuditTaskElementService auditTaskElementService;

    @Autowired
    private IAuditTaskCase auditTaskCaseService;
    
    @Autowired
    private IAuditSpITask auditSpITaskService;

    private FileUploadModel9 fileUploadModel = null;

    private String certguid = null;

    private String correctattachcliengguid = null;

    private String certname = null;

    private String correctreason = null;
    Logger log = LogUtil.getLog(CertCatchSureAction.class);

    @Override
    public void pageLoad() {

    }

    public void getLegalinfoList() {
        String legalcertnumber = this.getRequestParameter("certnumber");
        JSONObject dataJson = new JSONObject();
        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = null;
        auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByOneField("organcode", legalcertnumber).getResult();
        if (auditRsCompanyBaseinfo != null) {
            dataJson.put("creditcode", auditRsCompanyBaseinfo.getOrgancode());
            dataJson.put("organname", auditRsCompanyBaseinfo.getOrganname()); // 机构名称
            dataJson.put("organlegal", auditRsCompanyBaseinfo.getOrganlegal()); // 法定代表人/负责人
            dataJson.put("orgalegalid", auditRsCompanyBaseinfo.getOrgalegal_idnumber());
        }
        else {
            auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByOneField("creditcode", legalcertnumber)
                    .getResult();
            if (auditRsCompanyBaseinfo != null) {
                dataJson.put("creditcode", auditRsCompanyBaseinfo.getCreditcode());
                dataJson.put("organname", auditRsCompanyBaseinfo.getOrganname()); // 机构名称
                dataJson.put("organlegal", auditRsCompanyBaseinfo.getOrganlegal()); // 法定代表人/负责人
                dataJson.put("orgalegalid", auditRsCompanyBaseinfo.getOrgalegal_idnumber());
            }
        }
        addCallbackParam("legal", dataJson);
    }

    public void getIndividualinfoList() {
        String idnumber = this.getRequestParameter("certnumber");
        JSONObject dataJson = new JSONObject();
        AuditRsIndividualBaseinfo auditRsIndividualBaseinfo = null;
        auditRsIndividualBaseinfo = iAuditIndividual.getAuditRsIndividualBaseinfoByIDNumber(idnumber).getResult();
        if (auditRsIndividualBaseinfo != null) {
            dataJson.put("idnumber", auditRsIndividualBaseinfo.getIdnumber());
            dataJson.put("clientname", auditRsIndividualBaseinfo.getClientname());
            dataJson.put("contactperson", auditRsIndividualBaseinfo.getContactperson());
            dataJson.put("contactmobile", auditRsIndividualBaseinfo.getContactmobile());
        }

        addCallbackParam("individual", dataJson);
    }

    public void getMaterialList() {
        String projectGuid = getRequestParameter("projectguid");
        String areaCode = getRequestParameter("areacode");
        if (StringUtil.isBlank(areaCode)) {
            if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
                areaCode = ZwfwUserSession.getInstance().getBaseAreaCode();
            }
            else if (ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
                areaCode = ZwfwUserSession.getInstance().getBaseAreaCode();
            }
            else {
                areaCode = ZwfwUserSession.getInstance().getAreaCode();
            }
        }
        AuditProject auditProject = projectService.getAuditProjectByRowGuid(projectGuid, areaCode).getResult();
        Map<String, Integer> caseMap = null;
        if (auditProject != null && StringUtil.isNotBlank(auditProject.getTaskcaseguid())
                && !"none".equals(auditProject.getTaskcaseguid())) {
            List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                    .selectTaskMaterialCaseByCaseGuid(auditProject.getTaskcaseguid()).getResult();
            caseMap = new HashMap<>(16);
            // 转成map方便查找
            if (auditTaskMaterialCases != null && !auditTaskMaterialCases.isEmpty()) {
                for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                    caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                }
            }
        }
        Integer elementnum = 0;
        //判断事项是否配置了要素与选项数量
        if (StringUtil.isNotBlank(auditProject.getTask_id()) && StringUtil.isNotBlank(auditProject.getTaskguid())) {
            List<AuditTaskElement> auditTaskElementlist = auditTaskElementService
                    .findAllElementByTaskId(auditProject.getTask_id()).getResult();
            List<AuditTaskCase> auditTaskcaselist = auditTaskCaseService
                    .selectTaskCaseByTaskGuid(auditProject.getTaskguid(), ZwfwConstant.CONSTANT_STR_ONE).getResult();
            if (!auditTaskElementlist.isEmpty()) {
                //存在情形选项
                elementnum = 1;
            }
            else if (auditTaskElementlist.isEmpty() && !auditTaskcaselist.isEmpty()) {
                //不存在情形类别但存在常用情形 即旧情形
                elementnum = 2;
            }
            else {
                //两者都不存在
                elementnum = 0;
            }
        }

        List<Record> projectMaterials = handleMaterialService.getProjectMaterialALL(projectGuid, "").getResult();
        if (projectMaterials != null) {
            projectMaterials.sort((a,
                    b) -> (StringUtil.isNotBlank(b.get("Ordernum")) ? Integer.valueOf(b.get("Ordernum").toString())
                            : Integer.valueOf(0))
                                    .compareTo(StringUtil.isNotBlank(a.get("Ordernum"))
                                            ? Integer.valueOf(a.get("Ordernum").toString()) : 0));
            JSONArray dataArray = new JSONArray();
            SqlConditionUtil sqlc = new SqlConditionUtil();
            for (Record projectMaterial : projectMaterials) {
                sqlc.eq("taskguid", projectMaterial.get("taskguid"));
                sqlc.eq("materialguid", projectMaterial.get("taskmaterialguid"));
                List<AuditTaskMaterialCase> listc = auditTaskMaterialCaseService
                        .getAuditTaskMaterialCaseListByCondition(sqlc.getMap()).getResult();
                //匹配情形，情形下非必要材料不展示
                if (caseMap != null) {
                    if (!ZwfwConstant.NECESSITY_SET_YES.equals(projectMaterial.get("NECESSITY").toString())
                            && !caseMap.containsKey(projectMaterial.get("taskmaterialguid"))) {
                        continue;
                    }
                }
                else {
                    if (elementnum == 1 && (!listc.isEmpty()
                            || !ZwfwConstant.NECESSITY_SET_YES.equals(projectMaterial.get("NECESSITY").toString()))) {
                        continue;
                    }
                }
                if ((caseMap == null || caseMap.containsKey(projectMaterial.get("taskmaterialguid")) || listc.isEmpty())
                        && StringUtil.isNotBlank(projectMaterial.get("SHAREMATERIALGUID"))) {
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("materialname", projectMaterial.get("MATERIALNAME"));
                    dataJson.put("certcount", projectMaterial.get("CERTCOUNT"));
                    dataJson.put("certinstanceguid", projectMaterial.get("CERTINFOINSTANCEGUID"));
                    dataJson.put("sharematerialguid", projectMaterial.get("MaterialInstanceGuid") + ";"
                            + projectMaterial.get("SHAREMATERIALGUID"));
                    dataArray.add(dataJson);
                }
            }
            addCallbackParam("materialList", dataArray);
        }
    }

    public void getSpMaterialList() {
        String subappGuid = getRequestParameter("subappGuid");
        String type = getRequestParameter("type");
        String selectids = getRequestParameter("selectsids");
        String[] taskguid = null;
        if (selectids != null) {
            taskguid = selectids.split(";");
        }
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        listMaterial = auditSpIMaterialService.getSpIMaterialBySubappGuid(subappGuid).getResult();
        //更具事项过滤材料
        if ("submit".equals(type)) {
            AuditSpISubapp subapp = iauditspisubapp.getSubappByGuid(subappGuid).getResult();
            if (taskguid != null && taskguid.length > 0) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.in("rowguid", StringUtil.joinSql(taskguid));
                List<AuditSpITask> auditspitasklist = auditSpITaskService.getAuditSpITaskListByCondition(sqlc.getMap()).getResult();
                sqlc.clear();
                String st[]=new String[auditspitasklist.size()];
                for (int i = 0; i < auditspitasklist.size(); i++) {
                    st[i]=auditspitasklist.get(i).getTaskguid();
                }
                sqlc.in("taskguid", StringUtil.joinSql(st));
                List<AuditTaskMaterial> listm = iaudittaskmaterial.selectMaterialListByCondition(sqlc.getMap())
                        .getResult();
                List<String> materilids = new ArrayList<>();
                for (AuditTaskMaterial auditTaskMaterial : listm) {
                    materilids.add(auditTaskMaterial.getMaterialid());
                }
                List<String> spimaterialids = auditSpIMaterialService
                        .getSpIMaterialsByIDS(subapp.getBusinessguid(), subappGuid, materilids).getResult();
                listMaterial = listMaterial.stream()
                        .filter(auditspimaterial -> spimaterialids.contains(auditspimaterial.getRowguid()))
                        .collect(Collectors.toList());
            }
            else {
                listMaterial = new ArrayList<>();
            }
        }

        if (listMaterial != null) {
            JSONArray dataArray = new JSONArray();
            for (AuditSpIMaterial material : listMaterial) {
                if (!"20".equals(material.getSubmittype()) && StringUtil.isNotBlank(material.getMaterialguid())
                        && ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                        && "10".equals(material.getEffictiverange())) {
                    if (!("patch".equals(type) && !ZwfwConstant.CONSTANT_STR_ONE.equals(material.getIsbuzheng()))) {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("materialname", material.getMaterialname());
                        dataJson.put("certinstanceguid", material.getCertinfoinstanceguid());
                        dataJson.put("sharematerialguid", material.getRowguid() + ";" + material.getMaterialguid());
                        dataArray.add(dataJson);
                    }
                }
            }
            addCallbackParam("materialList", dataArray);
        }
    }

    public void searchCert() {
        String legalcertnumber = this.getRequestParameter("legalcertnumber");
        String certownertype = this.getRequestParameter("certownertype");
        String certownercerttype = this.getRequestParameter("certownercerttype");
        String sharematerialguid = this.getRequestParameter("sharematerialguid");
        String materialguid = this.getRequestParameter("materialguid");
        String projectGuid = getRequestParameter("projectguid");
        String officeWeb365_Server = handleConfigService.getFrameConfig("AS_OfficeWeb365_Server", "").getResult();

        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(certownertype))) {
            certownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
        }
        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(certownertype))) {
            certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
        }
        String areacode = "";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        List<CertInfo> certInfoList = certInfoExternalImpl.selectCertByOwner(sharematerialguid, certownertype,
                certownercerttype, legalcertnumber, false, areacode, null);
        String materialname = "";
        String certinstanceguid = "";
        if (StringUtil.isNotBlank(projectGuid)) {
            AuditProjectMaterial auditProjectMaterial = auditProjectMaterialImpl
                    .getProjectMaterialDetail(materialguid, projectGuid).getResult();
            if (auditProjectMaterial != null) {
                materialname = auditProjectMaterial.getTaskmaterial();
                certinstanceguid = auditProjectMaterial.getCertinfoinstanceguid();
                if (certInfoList != null && !certInfoList.isEmpty()) {
                    auditProjectMaterial.setCertcount(String.valueOf(certInfoList.size()));
                }
                else {
                    auditProjectMaterial.setCertcount(ZwfwConstant.CONSTANT_STR_ZERO);
                }
                auditProjectMaterialImpl.updateProjectMaterial(auditProjectMaterial);
            }
        }
        AuditSpIMaterial spmaterial = auditSpIMaterialService.getSpIMaterialByrowguid(materialguid).getResult();
        if (spmaterial != null) {
            materialname = spmaterial.getMaterialname();
            certinstanceguid = spmaterial.getCertinfoinstanceguid();
        }

        if (certInfoList != null && !certInfoList.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            for (CertInfo certInfo : certInfoList) {
                List<JSONObject> attachList = certAttachExternalService.getAttachList(certInfo.getCertcliengguid(),
                        areacode);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("certname", certInfo.getCertname());
                jsonObject.put("certguid", certInfo.getRowguid());
                jsonObject.put("officeWeb365", officeWeb365_Server);
                if (!ZwfwConstant.CONSTANT_STR_ONE.equals(certInfo.getMaterialtype())) {
                    jsonObject.put("class", "hidden");
                }
                else {
                    if ("C".equals(certInfo.getCertlevel())) {
                        jsonObject.put("class", "hidden");
                    }
                }
                if (StringUtil.isNotBlank(certInfo.getCertlevel())) {
                    jsonObject.put("certlevel", certInfo.getCertlevel() + "级");
                }
                if (StringUtil.isNotBlank(certinstanceguid) && certinstanceguid.equals(certInfo.getRowguid())) {
                    jsonObject.put("confirm", "1");
                }
                if (attachList != null && !attachList.isEmpty()) {
                    JSONArray piclist = new JSONArray();
                    for (JSONObject info : attachList) {
                        JSONObject picObject = new JSONObject();
                        picObject.put("attachguid", info.get("attachguid"));
                        String unrealpicurl = "";
                        String downloadUrl = "";
                        String picUrl = WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwfw/auditbusiness/auditproject/certview/images/view.png";
                        if (StringUtil.isNotBlank(info.get("attachguid"))) {
                            picUrl = certAttachExternalService.getAttachScan(info.get("attachguid").toString(),
                                    areacode);
                            log.info("---------------picUrl:" + picUrl);
                            if (StringUtil.isNotBlank(picUrl)) {
                                picUrl = picUrl.split("furl=")[1];
                                downloadUrl = picUrl;
                            }
                        }
                        String pic = "";
                        String[] contenttype = iConfigService.getFrameConfigValue("showPhotoViewFormat").split(";");
                        String fType = info.getString("attachname").substring(info.getString("attachname").lastIndexOf('.'));
                        if (Arrays.asList(contenttype).contains(fType)) {
                            pic = "pic"; 
                            unrealpicurl = picUrl;
                        }
                        else {
                            pic = info.getString("attachname").substring(info.getString("attachname").lastIndexOf('.'));
                            unrealpicurl = WebUtil.getRequestCompleteUrl(request)
                                    + "/epointzwfw/auditbusiness/auditproject/certview/images/view.png";
                            picUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(picUrl, fType);
                        }
                        picObject.put("contenttype", pic);
                        picObject.put("picUrl", picUrl);
                        picObject.put("unrealpicurl", unrealpicurl);
                        picObject.put("downloadUrl", downloadUrl);
                        piclist.add(picObject);
                    }
                    jsonObject.put("piclist", piclist);
                }
                jsonArray.add(jsonObject);
            }
            addCallbackParam("certs", jsonArray);
        }
        addCallbackParam("materialname", materialname);
    }

    public void checkMaterial() {
        String projectGuid = getRequestParameter("projectguid");
        String certGuid = getRequestParameter("certguid");
        String materialguid = getRequestParameter("materialguid");
        AuditProjectMaterial dataBean = new AuditProjectMaterial();
        SqlConditionUtil sql = new SqlConditionUtil();
        if (StringUtil.isNotBlank(certGuid)) {
            sql.eq("certinfoinstanceguid", certGuid);
        }
        if (StringUtil.isNotBlank(projectGuid)) {
            sql.eq("projectguid", projectGuid);
        }
        List<AuditProjectMaterial> list = auditProjectMaterialImpl.selectProjectMaterialByCondition(sql.getMap())
                .getResult();
        if (list != null && !list.isEmpty()) {
            addCallbackParam("exit", !materialguid.equals(list.get(0).getRowguid()));
        }
        else {
            dataBean.setCertinfoinstanceguid(certGuid);
            dataBean.setRowguid(materialguid);
            auditProjectMaterialImpl.updateProjectMaterial(dataBean);
        }
        String areacode = "";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        // 关联共享材料
        rsMateralService.initMaterialAttachFromCertByMaterialguid(projectGuid, materialguid, areacode,
                userSession.getUserGuid(), userSession.getDisplayName());
    }

    public void checkSpMaterial() {
        String subappGuid = this.getRequestParameter("subappGuid");
        String certGuid = this.getRequestParameter("certguid");
        String materialguid = this.getRequestParameter("materialguid");
        CertInfo certInfo = null;
        certInfo = certInfoExternalImpl.getCertInfoByRowguid(certGuid);
        String areacode = "";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        List<JSONObject> attachLists = certAttachExternalService.getAttachList(certInfo.getCertcliengguid(), areacode);
        int count = attachLists == null ? 0 : attachLists.size();
        SqlConditionUtil sql = new SqlConditionUtil();
        if (StringUtil.isNotBlank(certGuid)) {
            sql.eq("certinfoinstanceguid", certGuid);
        }
        if (StringUtil.isNotBlank(subappGuid)) {
            sql.eq("subappguid", subappGuid);
        }
        List<AuditSpIMaterial> list = auditSpIMaterialService.getSpIMaterialByCondition(sql.getMap()).getResult();
        if (list != null && !list.isEmpty()) {
            addCallbackParam("exit", !materialguid.equals(list.get(0).getRowguid()));
        }
        AuditSpIMaterial dataBean = new AuditSpIMaterial();
        if (StringUtil.isNotBlank(certGuid)) {
            dataBean.setCertinfoinstanceguid(certGuid);
        }
        dataBean.setRowguid(materialguid);
        if (count > 0) {
            String newCliengguid = UUID.randomUUID().toString();
            //			attachService.copyAttachByClientGuid(certInfo.getCertcliengguid(), null, null, newCliengguid);
            if (!attachLists.isEmpty()) {
                for (JSONObject json : attachLists) {
                    AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), newCliengguid,
                            json.getString("attachname"), json.getString("contentype"), "电子证照引用", json.getLong("size"),
                            certAttachExternalService.getAttach(json.getString("attachguid"), areacode),
                            userSession.getUserGuid(), userSession.getDisplayName());
                }
            }
            dataBean.setCliengguid(newCliengguid);
            dataBean.setStatus("20");
        }
        auditSpIMaterialService.updateSpIMaterial(dataBean);
    }

    public void cancelMaterial() {
        String projectGuid = getRequestParameter("projectguid");
        String certGuid = getRequestParameter("certguid");
        String materialguid = getRequestParameter("materialguid");
        AuditProjectMaterial dataBean = new AuditProjectMaterial();
        SqlConditionUtil sql = new SqlConditionUtil();
        if (StringUtil.isNotBlank(certGuid)) {
            sql.eq("certinfoinstanceguid", certGuid);
        }
        if (StringUtil.isNotBlank(projectGuid)) {
            sql.eq("projectguid", projectGuid);
        }
        List<AuditProjectMaterial> list = auditProjectMaterialImpl.selectProjectMaterialByCondition(sql.getMap())
                .getResult();
        if (list != null && !list.isEmpty()) {
            if (materialguid.equals(list.get(0).getRowguid())) {
                dataBean.setCertinfoinstanceguid("");
                dataBean.setRowguid(materialguid);
                dataBean.setCertcount(ZwfwConstant.CONSTANT_STR_ZERO);
                auditProjectMaterialImpl.updateProjectMaterial(dataBean);
                addCallbackParam("cancel", true);
            }
        }
        String areacode = "";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        // 关联共享材料
        rsMateralService.initMaterialAttachFromCert(projectGuid, areacode, userSession.getUserGuid(),
                userSession.getDisplayName());
    }

    public void cancelSpMaterial() {
        String subappGuid = this.getRequestParameter("subappGuid");
        String certGuid = getRequestParameter("certguid");
        String materialguid = getRequestParameter("materialguid");
        AuditSpIMaterial dataBean = new AuditSpIMaterial();
        SqlConditionUtil sql = new SqlConditionUtil();
        if (StringUtil.isNotBlank(certGuid)) {
            sql.eq("certinfoinstanceguid", certGuid);
        }
        if (StringUtil.isNotBlank(subappGuid)) {
            sql.eq("subappguid", subappGuid);
        }
        List<AuditSpIMaterial> list = auditSpIMaterialService.getSpIMaterialByCondition(sql.getMap()).getResult();
        if (list != null && !list.isEmpty()) {
            if (materialguid.equals(list.get(0).getRowguid())) {
                dataBean.setCertinfoinstanceguid(UUID.randomUUID().toString());
                dataBean.setRowguid(materialguid);
                dataBean.setStatus("10");
                auditSpIMaterialService.updateSpIMaterial(dataBean);
                //删除附件
                attachService.deleteAttachByGuid(list.get(0).getCliengguid());
                addCallbackParam("cancel", true);
            }
        }

    }

    public void padConfirmMaterial() {
        String projectGuid = getRequestParameter("projectguid");
        SqlConditionUtil sql = new SqlConditionUtil();
        if (StringUtil.isNotBlank(projectGuid)) {
            sql.eq("projectguid", projectGuid);
        }
        sql.eq("certcount", ZwfwConstant.CONSTANT_STR_ONE);
        List<AuditProjectMaterial> list = auditProjectMaterialImpl.selectProjectMaterialByCondition(sql.getMap())
                .getResult();
        if (list != null && !list.isEmpty()) {
            boolean flag = true;
            String materialname = "";
            for (AuditProjectMaterial auditProjectMaterial : list) {
                if (ZwfwConstant.CONSTANT_STR_ZERO.equals(auditProjectMaterial.getConfirm())) {
                    flag = false;
                    materialname = auditProjectMaterial.getTaskmaterial() + ";";
                }
            }
            addCallbackParam("confirm", flag);
            addCallbackParam("materialname", materialname);
        }
    }

    public FileUploadModel9 getUploadmodel() {
        correctattachcliengguid = getViewData("correctattachcliengguid");
        if (StringUtil.isBlank(correctattachcliengguid)) {
            correctattachcliengguid = UUID.randomUUID().toString();
            addViewData("correctattachcliengguid", correctattachcliengguid);
        }
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(correctattachcliengguid,
                    "dzzzglxt", null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;

    }

    public void certName() {
        certguid = getRequestParameter("certguid");
        if (StringUtil.isNotBlank(certguid)) {
            String certName = certInfoExternalImpl.getCertInfoByRowguid(certguid).getCertname();
            addCallbackParam("certname", certName);
        }
    }

    public void saveCorrectCert() {
        certguid = getRequestParameter("certguid");
        certInfoExternalImpl.CorrectCertInfo(ZwfwUserSession.getInstance().getAreaCode(), certguid, "", "", "",
                correctreason, getViewData("correctattachcliengguid"));
    }

    public String getCertname() {
        return certname;
    }

    public void setCertname(String certname) {
        this.certname = certname;
    }

    public String getCorrectreason() {
        return correctreason;
    }

    public void setCorrectreason(String correctreason) {
        this.correctreason = correctreason;
    }

}
