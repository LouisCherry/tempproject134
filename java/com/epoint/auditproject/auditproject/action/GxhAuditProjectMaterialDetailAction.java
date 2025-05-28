package com.epoint.auditproject.auditproject.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.epoint.core.EpointFrameDsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.util.RotateImageUtil;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
// import
// com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectPatchMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditMaterialLibrary;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibrary;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
/*
 * import com.epoint.common.util.FileSystemUtils;
 * import com.epoint.common.util.ProjectMaterialConvertUtil;
 */
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditresource.handlersmaterial.inter.IHandleRSMaterial;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.officeweb365.DESEncrypt;

/**
 * 办件电子材料上传页面对应的后台
 * 
 * @author Dong
 * @version [版本号, 2016-09-26 11:08:08]
 */
@RestController("gxhauditprojectmaterialdetailaction")
@Scope("request")
public class GxhAuditProjectMaterialDetailAction extends BaseController
{
    /**
     * 办件查询字段
     */
    private String fields = " acceptuserguid,rowguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,"
            + "taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,"
            + "contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,"
            + "biguid,banjiedate,is_pause,is_test,if_express,spendtime,subappguid,businessguid,applydate,"
            + "currentareacode,handleareacode,acceptareacode,legalid,task_id,xmname,xmnum,onlineapplyerguid,xiangmubh";

    /**
     * 办件标识
     */
    private String projectGuid;

    /**
     * 辖区编码
     */
    private String areacode;

    /**
     * 附件数量
     */
    private int attachNum;

    private AuditProject auditProject;

    private String certnum;

    /**
     * 是否为告知承诺材料
     */
    private String isgzcnmaterial = "";

    /**
     * 表单是否可编辑
     */
    private String isallowedit = "";

    /**
     * 附件上传model
     */
    private FileUploadModel9 attachUploadModel;

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IHandleRSMaterial rsMateralService;

    @Autowired
    private IHandleMaterial handleMaterialService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IAuditMaterialLibrary auditMaterialLibraryservice;

    @Autowired
    private IAuditTaskRiskpoint auditTaskRiskpointService;

    @Autowired
    private IAuditTask auditTaskService;

    @Override
    public void pageLoad() {
        projectGuid = getRequestParameter("projectguid");
        isgzcnmaterial = getRequestParameter("isgzcnmaterial");
        areacode = getRequestParameter("areaCode");
        String cliengguid = getRequestParameter("cliengguid");
        String projectMaterialGuid = getRequestParameter("materialInstanceGuid");
        if (StringUtil.isBlank(cliengguid)) {
            AuditProjectMaterial projectMaterial = projectMaterialService
                    .getProjectMaterialDetail(projectMaterialGuid, projectGuid).getResult();
            cliengguid = projectMaterial.getCliengguid();
        }
        if (StringUtil.isBlank(areacode) && ZwfwUserSession.getInstance().getCitylevel() != null
                && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else if (StringUtil.isBlank(areacode)) {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectGuid, areacode).getResult();
        if (auditProject == null) {
            auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectGuid, null).getResult();
        }

        if (auditProject != null) {
            // isallowedit =
            // checkActivityIsAllowEdit(auditProject.getTaskguid(),
            // auditProject.getPviguid());
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
            if (auditTaskExtension != null) {
                addCallbackParam("aisptype", auditTaskExtension.get("aisptype"));
            }
            // 申请人信息可能为多个
            JSONArray applyDataArray = new JSONArray();
            JSONObject applyDataObject = new JSONObject();
            applyDataObject.put("certnum", auditProject.getCertnum());
            applyDataObject.put("certtype", auditProject.getCerttype());
            applyDataObject.put("applyertype", auditProject.getApplyertype());

            applyDataArray.add(applyDataObject);
            addCallbackParam("applydata", applyDataArray);
            addCallbackParam("taskguid", auditProject.getTaskguid());
            addCallbackParam("status", auditProject.getStatus());
        }

        if (StringUtil.isBlank(getViewData("materialCliengguid"))) {
            addViewData("materialCliengguid", cliengguid);
        }
        if (StringUtil.isBlank(getViewData("materialInstanceGuid"))) {
            addViewData("materialInstanceGuid", getRequestParameter("materialInstanceGuid"));
        }

        List<FrameAttachInfo> list = getAttachList(getViewData("materialCliengguid"));
        if (EpointCollectionUtils.isNotEmpty(list)) {
            attachNum = list.size();
        }
        else {
            attachNum = 0;
        }

    }

    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        return attachService.getAttachInfoListByGuid(cliengGuid);
    }

    /**
     * 获取材料树数据，用于模板渲染
     * 
     * @return
     */
    /*
     * public String getFileTreeDataUrl() {
     * // 1.定义返回的参数
     * JSONObject custom = new JSONObject();
     * JSONArray data = new JSONArray();
     * custom.put("data", data);
     * 
     * if (auditProject != null) {
     * // 获取办件材料列表
     * List<AuditProjectMaterial> projectMaterialList =
     * projectMaterialService.selectProjectMaterialList(auditProject.getRowguid(
     * )).getResult();
     * 
     * // 获取所有告知承诺办件材料,
     * if ("true".equals(isgzcnmaterial)) {
     * projectMaterialList = projectMaterialList.stream()
     * .filter(p ->
     * StringUtil.isNotBlank(p.get("promisetype"))).collect(Collectors.toList())
     * ;
     * }
     * // 获取所有非告知承诺办件材料
     * else if ("false".equals(isgzcnmaterial)) {
     * projectMaterialList = projectMaterialList.stream().filter(p ->
     * StringUtil.isBlank(p.get("promisetype")))
     * .collect(Collectors.toList());
     * }
     * 
     * List<String> subMaterialIdList = projectMaterialList.stream().map(s ->
     * s.get("pid").toString())
     * .collect(Collectors.toList());
     * 
     * SqlConditionUtil sql = new SqlConditionUtil();
     * sql.isNotBlank("materialid");
     * sql.eq("taskguid", auditProject.getTaskguid());
     * sql.in("materialid", "'" + StringUtil.join(subMaterialIdList, "','") +
     * "'");
     * List<AuditTaskMaterial> parentTaskMaterialList = iAuditTaskMaterial
     * .selectMaterialListByCondition(sql.getMap()).getResult();
     * for (AuditTaskMaterial parentTaskMaterial : parentTaskMaterialList) {
     * // 定义数组存储二级材料对象
     * JSONArray subMaterialArray = new JSONArray();
     * 
     * // 获取一级材料对应的所有二级材料
     * List<AuditProjectMaterial> subProjectMaterialList =
     * projectMaterialList.stream()
     * .filter(s ->
     * parentTaskMaterial.getMaterialid().equals(s.get("pid").toString()))
     * .collect(Collectors.toList());
     * 
     * for (AuditProjectMaterial subProjectMaterial : subProjectMaterialList) {
     * // 定义二级材料对象
     * JSONObject subProjectMaterialObject = new JSONObject();
     * 
     * subProjectMaterialObject.put("nametitle",
     * subProjectMaterial.get("materialname").toString());
     * if (subProjectMaterial.get("materialname").toString().length() > 10) {
     * subProjectMaterialObject.put("name",
     * subProjectMaterial.get("materialname").toString().substring(0, 10) +
     * "...");
     * }
     * else {
     * subProjectMaterialObject.put("name",
     * subProjectMaterial.get("materialname").toString());
     * }
     * subProjectMaterialObject.put("ordernumer",
     * subProjectMaterial.get("ordernum"));
     * subProjectMaterialObject.put("guid", subProjectMaterial.getRowguid());
     * subProjectMaterialObject.put("cliengguid",
     * subProjectMaterial.getCliengguid());
     * subProjectMaterialObject.put("submittype",
     * subProjectMaterial.get("submittype").toString());
     * subProjectMaterialObject.put("auditstatus",
     * subProjectMaterial.getAuditstatus());
     * subProjectMaterialObject.put("flag",
     * ProjectMaterialConvertUtil.getFlagArray(subProjectMaterial));
     * subProjectMaterialObject.put("enclosure",
     * subProjectMaterial.get("attachnum").toString());
     * subProjectMaterialObject.put("subfiles", new JSONArray());
     * subProjectMaterialObject.put("materialid",
     * subProjectMaterial.get("materialid").toString());
     * subProjectMaterialObject.put("haskeypoint",
     * subProjectMaterial.get("haskeypoint").toString());
     * 
     * // 过滤材料提交方式为：仅提交纸质材料的
     * if (!("20".equals(subProjectMaterial.get("submittype").toString()) &&
     * (subProjectMaterial
     * .getStatus() == ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER
     * || subProjectMaterial.getStatus() ==
     * ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT))) {
     * // 判断是否是认同材料
     * if
     * (ZwfwConstant.CONSTANT_STR_TWO.equals(subProjectMaterial.getPromisetype()
     * )) {
     * JSONObject confirmMaterial = new JSONObject();
     * confirmMaterial.putAll(subProjectMaterialObject);
     * confirmMaterial.put("isconfirmmaterial",
     * ZwfwConstant.CONSTANT_STR_ONE);// 被替换的原材料
     * confirmMaterial.put("enclosure", ZwfwConstant.CONSTANT_STR_ZERO);
     * confirmMaterial.put("guid", "");
     * confirmMaterial.put("enclosure", false);
     * confirmMaterial.put("isallowedit", ZwfwConstant.CONSTANT_STR_ZERO);
     * subMaterialArray.add(confirmMaterial);
     * 
     * subProjectMaterialObject.put("isconfirmmaterial",
     * ZwfwConstant.CONSTANT_STR_TWO);
     * subProjectMaterialObject.put("nametitle",
     * subProjectMaterial.getStr("replacematerialname"));
     * if (subProjectMaterial.getStr("replacematerialname").length() > 10) {
     * subProjectMaterialObject.put("name",
     * subProjectMaterial.getStr("replacematerialname").substring(0, 10) +
     * "...");
     * }
     * else {
     * subProjectMaterialObject.put("name",
     * subProjectMaterial.getStr("replacematerialname"));
     * }
     * JSONArray flagArray = new JSONArray();
     * JSONObject flagObj = new JSONObject();
     * flagObj.put("name", "替");
     * flagObj.put("state", 8);
     * flagArray.add(flagObj);
     * subProjectMaterialObject.put("flag", flagArray);
     * }
     * subProjectMaterialObject.put("isallowedit", handleMaterialService
     * .checkMaterialIsAllowEdit(isallowedit, auditProject,
     * subProjectMaterial));
     * subMaterialArray.add(subProjectMaterialObject);
     * }
     * }
     * 
     * // 定义一个一级办件材料对象
     * JSONObject parentMaterialObject = new JSONObject();
     * 
     * parentMaterialObject.put("nametitle",
     * parentTaskMaterial.get("materialname").toString());
     * if (parentTaskMaterial.getMaterialname().length() > 10) {
     * parentMaterialObject.put("name",
     * parentTaskMaterial.getMaterialname().substring(0, 10) + "...");
     * }
     * else {
     * parentMaterialObject.put("name", parentTaskMaterial.getMaterialname());
     * }
     * parentMaterialObject.put("ordernumer",
     * parentTaskMaterial.get("ordernum"));
     * parentMaterialObject.put("guid", "");
     * parentMaterialObject.put("cliengguid", "");
     * parentMaterialObject.put("submittype", "");
     * parentMaterialObject.put("auditstatus", "");
     * // parentMaterialObject.put("flag",
     * // getFlagArray(parentTaskMaterial));
     * parentMaterialObject.put("enclosure", 0);
     * if (subMaterialArray != null && !subMaterialArray.isEmpty()) {
     * parentMaterialObject.put("isexitchild", 0);
     * }
     * else {
     * parentMaterialObject.put("isexitchild", 1);
     * }
     * parentMaterialObject.put("subfiles", subMaterialArray);
     * parentMaterialObject.put("haskeypoint", ZwfwConstant.CONSTANT_STR_ZERO);
     * parentMaterialObject.put("materialid",
     * parentTaskMaterial.getStr("materialid"));
     * 
     * if (subMaterialArray != null && !subMaterialArray.isEmpty()) {
     * data.add(parentMaterialObject);
     * }
     * 
     * }
     * // 遍历获取没有二级材料的一级材料
     * for (AuditProjectMaterial auditProjectMaterial : projectMaterialList) {
     * 
     * if (StringUtil.isBlank(auditProjectMaterial.get("pid"))) {
     * // 定义一个一级办件材料对象
     * JSONObject parentMaterialObject = new JSONObject();
     * 
     * parentMaterialObject.put("nametitle",
     * auditProjectMaterial.get("materialname").toString());
     * if (auditProjectMaterial.get("materialname").toString().length() > 10) {
     * parentMaterialObject.put("name",
     * auditProjectMaterial.get("materialname").toString().substring(0, 10) +
     * "...");
     * }
     * else {
     * parentMaterialObject.put("name",
     * auditProjectMaterial.get("materialname").toString());
     * }
     * parentMaterialObject.put("ordernumer",
     * auditProjectMaterial.get("ordernum"));
     * parentMaterialObject.put("guid", auditProjectMaterial.getRowguid());
     * parentMaterialObject.put("cliengguid",
     * auditProjectMaterial.getCliengguid());
     * parentMaterialObject.put("submittype",
     * auditProjectMaterial.get("submittype").toString());
     * parentMaterialObject.put("auditstatus",
     * auditProjectMaterial.getAuditstatus());
     * parentMaterialObject.put("flag",
     * ProjectMaterialConvertUtil.getFlagArray(auditProjectMaterial));
     * parentMaterialObject.put("enclosure",
     * auditProjectMaterial.get("attachnum").toString());
     * parentMaterialObject.put("subfiles", new JSONArray());
     * parentMaterialObject.put("materialid",
     * auditProjectMaterial.getStr("materialid"));
     * parentMaterialObject.put("haskeypoint",
     * auditProjectMaterial.getStr("haskeypoint"));
     * 
     * parentMaterialObject.put("isexitchild", 1);
     * // 过滤材料提交方式为：仅提交纸质材料的
     * if (!("20".equals(auditProjectMaterial.get("submittype").toString()) &&
     * (auditProjectMaterial
     * .getStatus() == ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER
     * || auditProjectMaterial.getStatus() ==
     * ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT))) {
     * // 判断是否是认同材料
     * if (ZwfwConstant.CONSTANT_STR_TWO.equals(auditProjectMaterial.get(
     * "promisetype"))) {
     * JSONObject confirmMaterial = new JSONObject();
     * confirmMaterial.putAll(parentMaterialObject);
     * // 认同材料不可点击
     * confirmMaterial.put("isconfirmmaterial",
     * ZwfwConstant.CONSTANT_STR_ONE);// 被替换的原材料
     * confirmMaterial.put("enclosure", ZwfwConstant.CONSTANT_STR_ZERO);
     * confirmMaterial.put("guid", "");
     * confirmMaterial.put("isexitchild", false);
     * confirmMaterial.put("isallowedit", ZwfwConstant.CONSTANT_STR_ZERO);
     * data.add(confirmMaterial);
     * 
     * parentMaterialObject.put("isconfirmmaterial",
     * ZwfwConstant.CONSTANT_STR_TWO);
     * parentMaterialObject.put("name",
     * auditProjectMaterial.getStr("replacematerialname"));
     * parentMaterialObject.put("nametitle",
     * auditProjectMaterial.getStr("replacematerialname"));
     * if (auditProjectMaterial.getStr("replacematerialname").length() > 10) {
     * parentMaterialObject.put("name",
     * auditProjectMaterial.getStr("replacematerialname").substring(0, 10) +
     * "...");
     * }
     * else {
     * parentMaterialObject.put("name",
     * auditProjectMaterial.getStr("replacematerialname"));
     * }
     * JSONArray flagArray = new JSONArray();
     * JSONObject flagObj = new JSONObject();
     * flagObj.put("name", "替");
     * flagObj.put("state", 8);
     * flagArray.add(flagObj);
     * parentMaterialObject.put("flag", flagArray);
     * }
     * parentMaterialObject.put("isallowedit", handleMaterialService
     * .checkMaterialIsAllowEdit(isallowedit, auditProject,
     * auditProjectMaterial));
     * data.add(parentMaterialObject);
     * }
     * }
     * }
     * }
     * 
     * if (data != null && !data.isEmpty()) {
     * data.sort(Comparator.comparing(obj -> {
     * return ((JSONObject) obj).getIntValue("ordernumer");
     * }).reversed());
     * }
     * custom.put("data", data);
     * return custom.toJSONString();
     * }
     */

    public void rendImg(JSONObject attachObject, FrameAttachInfo attachInfo) {
        String xmzmurl = WebUtil.getRequestRootUrl(request);
        String tyString = attachInfo.getContentType().split("\\.").length > 1
                ? attachInfo.getContentType().split("\\.")[1]
                : attachInfo.getContentType();
        tyString = tyString.replace(".", "");
        if ("png/jpg".contains((tyString))) {
            tyString = "png/jpg";
        }
        attachObject.put("type", tyString);
        if ("png/jpg".equals(tyString)) {
            FrameAttachStorage fas = attachService.getAttachByInfo(attachInfo);
            if (fas != null) {
                attachObject.put("img",
                        xmzmurl + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                + attachInfo.getAttachGuid());

                attachObject.put("imgname", fas.getAttachFileName());
            }
        }
    }

    public void savaImg(String angle, String attachguid) {
        InputStream inputStream = null;
        try {
            FrameAttachInfo frameAttachInfo = attachService.getAttachInfoDetail(attachguid);
            if (frameAttachInfo != null) {
                String cliengGuid = frameAttachInfo.getCliengGuid();
                String fileName = frameAttachInfo.getAttachFileName();
                Date uploadDateTime = frameAttachInfo.getUploadDateTime();
                inputStream = attachService.getInputStreamByInfo(frameAttachInfo);
                BufferedImage bufferImg = ImageIO.read(inputStream);
                if (bufferImg != null) {
                    // 调用图片旋转工具类，旋转图片
                    BufferedImage rotateImage = RotateImageUtil.rotateImage(bufferImg, Integer.parseInt(angle));
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(rotateImage, "png", os);
                    InputStream is = new ByteArrayInputStream(os.toByteArray());
                    attachService.deleteAttachByAttachGuid(attachguid);
                    FrameAttachInfo frameAttachInfonew = new FrameAttachInfo();
                    frameAttachInfonew.setAttachGuid(attachguid);
                    frameAttachInfonew.setCliengGuid(cliengGuid);
                    frameAttachInfonew.setAttachFileName(fileName);
                    frameAttachInfonew.setCliengTag("综管上传");
                    frameAttachInfonew.setUploadUserGuid(userSession.getUserGuid());
                    frameAttachInfonew.setUploadUserDisplayName(userSession.getDisplayName());
                    frameAttachInfonew.setUploadDateTime(uploadDateTime);
                    attachService.addAttach(frameAttachInfonew, is);
                }
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取材料附件信息
     * 
     * @param cliengguid
     * @return
     */
    public String getRightFileDataUrl() {
        // 1.定义返回的参数
        JSONObject custom = new JSONObject();
        JSONArray data = new JSONArray();
        custom.put("data", data);
        // 获取系统参数中office365地址参数(172.20.138.153:8088)
        String officeurl = configService.getFrameConfigValue("AS_OfficeWeb365_Server");
        String previewUrl = "http://" + officeurl + "/?furl=";
        DESEncrypt des;
        try {
            String uploadpreview_encrypt_key = ConfigUtil.getConfigValue("uploadpreview.encrypt.key");
            String uploadpreview_encrypt_iv = ConfigUtil.getConfigValue("uploadpreview.encrypt.iv");
            des = new DESEncrypt(uploadpreview_encrypt_key, uploadpreview_encrypt_iv);
            // 办件材料标识
            String projectmaterialguid = getRequestParameter("projectmaterialguid");
            String projectguid = getRequestParameter("projectguid");
            AuditProjectMaterial projectMaterial = projectMaterialService
                    .getProjectMaterialDetail(projectmaterialguid, projectguid).getResult();

            // 是否是ai审批
            boolean isaisp = false;
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
            if (auditTaskExtension != null && StringUtil.isNotBlank(auditTaskExtension.get("aisptype"))
                    && !ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTaskExtension.get("aisptype"))) {
                isaisp = true;
            }

            JSONObject subObject = null;
            /*
             * if (isaisp) {
             * subObject = hasSubMaterial();
             * custom.put("hasSub", subObject.get("hasSub"));
             * custom.put("subcarrier", subObject.get("subcarrier"));
             * }
             */

            if (projectMaterial == null) {
                return custom.toJSONString();
            }

            // 查询补正的材料附件，这些附件不可修改只能查看
            /*
             * if (StringUtil.isNotBlank(projectMaterial.getRowguid())
             * && StringUtil.isNotBlank(projectMaterial.getIsbuzheng())
             * &&
             * ZwfwConstant.CONSTANT_STR_ONE.equals(projectMaterial.getIsbuzheng
             * ())
             * && (ZwfwConstant.Material_AuditStatus_DBZ.equals(projectMaterial.
             * getAuditstatus())
             * || ZwfwConstant.Material_AuditStatus_YBZ.equals(projectMaterial.
             * getAuditstatus()))) {
             * AuditProjectPatchMaterial patchMaterialHistroy =
             * iAuditProjectPatchMaterialService
             * .find(projectMaterial.getCopyProjectMaterialGuid());
             * 
             * if (patchMaterialHistroy != null &&
             * StringUtil.isNotBlank(patchMaterialHistroy.getCliengguid())) {
             * List<FrameAttachInfo> attachInfoList = attachService
             * .getAttachInfoListByGuid(patchMaterialHistroy.getCliengguid());
             * for (FrameAttachInfo attachInfo : attachInfoList) {
             * JSONObject attachObject = new JSONObject();
             * // 压缩文件不显示预览
             * attachObject.put("isannotationfile",
             * !(".rar".equalsIgnoreCase(attachInfo.getContentType())
             * || ".zip".equalsIgnoreCase(attachInfo.getContentType())));
             * attachObject.put("attachtitle", attachInfo.getAttachFileName());
             * if (attachInfo.getAttachFileName().length() > 10) {
             * attachObject.put("attachname",
             * attachInfo.getAttachFileName().substring(0, 10) + "...");
             * }
             * else {
             * attachObject.put("attachname", attachInfo.getAttachFileName());
             * }
             * attachObject.put("guid", attachInfo.getAttachGuid());
             * attachObject.put("url", previewUrl +
             * des.encode(configService.getFrameConfigValue("SYSURL")
             * + attachService.getAttachPreviewDownPath(attachInfo)));
             * 
             * attachObject.put("imgurl",
             * FileSystemUtils.getTbImgurl(attachInfo));
             * attachObject.put("iscopy", ZwfwConstant.CONSTANT_STR_ONE); //
             * 补正材料的附件，不可删除
             * // 20220124-补正材料查看批注使用
             * attachObject.put("projectmaterialguid",
             * patchMaterialHistroy.getRowguid());
             * attachObject.put("projectmaterialcliengguid",
             * patchMaterialHistroy.getCliengguid());
             * attachObject.put("isinvalid", true); // 复制材料的附件，不可删除
             * // 图片预览个性化
             * rendImg(attachObject, attachInfo);
             * data.add(attachObject);
             * }
             * }
             * }
             */

            // 判断当前材料是否允许电子签章、签名
            AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                    .getAuditTaskMaterialByRowguid(projectMaterial.getTaskmaterialguid()).getResult();
            custom.put("taskmaterialguid", auditTaskMaterial.getRowguid());
            custom.put("is_signature", false);

            if (StringUtil.isNotBlank(projectMaterial.getCliengguid())) {
                List<FrameAttachInfo> attachInfoList = attachService
                        .getAttachInfoListByGuid(projectMaterial.getCliengguid());
                custom.put("isfile", attachInfoList != null && !attachInfoList.isEmpty());
                for (FrameAttachInfo attachInfo : attachInfoList) {
                    JSONObject attachObject = new JSONObject();
                    // 压缩文件不显示预览
                    attachObject.put("isannotationfile", !(".rar".equalsIgnoreCase(attachInfo.getContentType())
                            || ".zip".equalsIgnoreCase(attachInfo.getContentType())));
                    attachObject.put("attachtitle", attachInfo.getAttachFileName());
                    if (attachInfo.getAttachFileName().length() > 10) {
                        attachObject.put("attachname", attachInfo.getAttachFileName().substring(0, 8) + "...");
                    }
                    else {
                        attachObject.put("attachname", attachInfo.getAttachFileName());
                    }
                    attachObject.put("guid", attachInfo.getAttachGuid());
                    String baseUrl = WebUtil.getRequestCompleteUrl(WebUtil.getRequest())
                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                            + attachInfo.getAttachGuid();
                    String url = OfficeWebUrlEncryptUtil.getEncryptUrl(baseUrl, attachInfo.getContentType());
                    if (baseUrl.contains("https")) {
                        url += "&ssl=1";
                    }
                    attachObject.put("url", "http://" + officeurl + "?fname=" + attachInfo.getAttachGuid()
                            + attachInfo.getContentType() + "&" + url);
                    /*
                     * previewUrl +
                     * des.encode(configService.getFrameConfigValue("SYSURL")
                     * + attachService.getAttachPreviewDownPath(attachInfo))
                     */

                    // attachObject.put("imgurl",
                    // FileSystemUtils.getTbImgurl(attachInfo)); // 复制材料的附件，不可删除
                    // 20220124-补正材料查看批注使用
                    attachObject.put("projectmaterialguid", projectMaterial.getRowguid());
                    attachObject.put("projectmaterialcliengguid", projectMaterial.getCliengguid());
                    attachObject.put("isinvalid", false); // 复制材料的附件，不可删除
                    // 图片预览个性化
                    rendImg(attachObject, attachInfo);
                    data.add(attachObject);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return custom.toJSONString();
    }

    /**
     * 证照检索后台实现
     * [一句话功能简述]
     */
    public void searchCert() {
        String area = "";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectGuid, area).getResult();
        certnum = getRequestParameter("certnum");
        String certtype = getRequestParameter("certtype");
        // 关联共享材料
        String applyerType = getRequestParameter("applyerType");
        String flag = rsMateralService.is_ExistCert(auditProject.getRowguid(), applyerType, certnum, certtype, area)
                .getResult();
        addCallbackParam("flag", flag);
        addCallbackParam("applyerType", applyerType);
        addCallbackParam("certnum", certnum);
    }

    /**
     * office365预览
     * 
     * @param path
     * @param fileName
     */
    public void previewFile() {
        // 获取系统参数中office365地址参数(172.20.138.153:8088)
        String officeurl = configService.getFrameConfigValue("AS_OfficeWeb365_Server");
        this.addCallbackParam("officeurl", officeurl);
    }

    /**
     * 下载整包
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    /*
     * @RequestSupport(type = {RequestType.GET })
     * public void downloadAttach() {
     * String projectguid = this.getRequestParameter("projectguid");
     * String attachguids = this.getRequestParameter("attachguids");
     * // 办件材料标识
     * String materialguid = this.getRequestParameter("materialguid");
     * String[] attachguidArr = attachguids.split(",");
     * List<String> attachguidlist = Arrays.asList(attachguidArr);
     * 
     * AuditProjectMaterial auditProjectMaterial = projectMaterialService
     * .getProjectMaterialDetail(materialguid, projectguid).getResult();
     * AuditProjectPatchMaterial patchMaterial =
     * iAuditProjectPatchMaterialService.find(materialguid);
     * 
     * String taskmaterial = "";
     * if (auditProjectMaterial != null) {
     * taskmaterial = auditProjectMaterial.getTaskmaterial();
     * }
     * else if (patchMaterial != null) {
     * taskmaterial = patchMaterial.getTaskmaterial();
     * }
     * else {
     * return;
     * }
     * 
     * if (attachguidlist != null && !attachguidlist.isEmpty()) {
     * String savePath = FileSystemUtils.createRoot(taskmaterial);
     * // 2、下载附件到临时文件夹
     * for (String attachguid : attachguidlist) {
     * // 1、获取原文件流
     * FrameAttachInfo frameAttachinfo =
     * attachService.getAttachInfoDetail(attachguid);
     * try {
     * InputStream inputStream =
     * attachService.getInputStreamByInfo(frameAttachinfo);
     * if (inputStream != null) {
     * byte[] bytes = FileManagerUtil.getContentFromInputStream(inputStream);
     * FileSystemUtils.writeByteToFile(savePath,
     * frameAttachinfo.getAttachFileName(), bytes);
     * inputStream.close();
     * }
     * }
     * catch (IOException e) {
     * e.printStackTrace();
     * }
     * }
     * 
     * // 3、压缩文件
     * ZipUtil.doZip(savePath, savePath + ".zip");
     * 
     * // 4、压缩文件转换成流
     * byte[] contentFromSystem = FileManagerUtil.getContentFromSystem(savePath
     * + ".zip");
     * if (contentFromSystem != null) {
     * try {
     * InputStream byteArrayInputStream = new
     * ByteArrayInputStream(contentFromSystem);
     * this.sendRespose(byteArrayInputStream, taskmaterial + ".zip", ".zip");
     * byteArrayInputStream.close();
     * }
     * catch (IOException e) {
     * e.printStackTrace();
     * }
     * }
     * 
     * // 5、删除临时文件夹
     * String dir = savePath.substring(0, savePath.lastIndexOf('\\'));
     * if (FileManagerUtil.isExistFileDir(dir, false)) {
     * FileManagerUtil.deleteFile(dir);
     * }
     * }
     * 
     * }
     */

    AttachHandler9 handler = new AttachHandler9()
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void afterSaveAttachToDB(Object attach) {
            // 生成缩略图
            /*
             * FrameAttachStorage frameAttachStorage = (FrameAttachStorage)
             * attach;
             * if (frameAttachStorage != null) {
             * FileSystemUtils.saveThumbnail(frameAttachStorage.getAttachGuid())
             * ;
             * }
             */

            attachUploadModel.getExtraDatas().put("msg", "上传成功");
            // 如果原本为0，那么需要更新状态为电子提交或电子或纸质提交
            if (attachNum == 0) {
                projectMaterialService.updateProjectMaterialStatus(getViewData("materialInstanceGuid"), projectGuid,
                        10);
            }
            String auditstatus = getRequestParameter("auditStatus");
            // 未提交更新为审核通过
            if (auditstatus.equals(ZwfwConstant.Material_AuditStatus_WTJ)) {
                projectMaterialService.updateProjectMaterialAuditStatus(getViewData("materialInstanceGuid"),
                        Integer.parseInt(ZwfwConstant.Material_AuditStatus_SHTG), projectGuid);
            }
            // 待补正更新为待审核,并联审批办件不在这边进行材料补交
            else if (StringUtil.isBlank(auditProject.getBiguid())
                    && auditstatus.equals(ZwfwConstant.Material_AuditStatus_DBZ)) {
                projectMaterialService.updateProjectMaterialAuditStatus(getViewData("materialInstanceGuid"),
                        Integer.parseInt(ZwfwConstant.Material_AuditStatus_YBZ), projectGuid);
            }
            //
        }

        @Override
        public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
            return true;
        }

    };

    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {
            attachUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(getViewData("materialCliengguid"), "综管上传", null, handler,
                            userSession.getUserGuid(), userSession.getDisplayName())
                    {
                        private static final long serialVersionUID = 1L;
                        private IAttachService frameAttachInfoNewService = ContainerFactory.getContainInfo()
                                .getComponent(IAttachService.class);

                        @Override
                        public List<FrameAttachInfo> getAllAttach() {
                            FrameAttachInfo info = new FrameAttachInfo();
                            info.setCliengGuid(getViewData("materialCliengguid"));
                            info.setSortKey("uploadDateTime");
                            info.setSortDir("asc");
                            List<FrameAttachInfo> attachList = frameAttachInfoNewService.getAttachInfoList(info,
                                    (Date) null, (Date) null);
                            if (!frameAttachInfoNewService.isExistAttach(getViewData("materialCliengguid"))) {
                                attachList = new ArrayList<FrameAttachInfo>();
                            }

                            return attachList;
                        }

                        @Override
                        public int getAllAttachCount() {
                            FrameAttachInfo info = new FrameAttachInfo();
                            info.setCliengGuid(getViewData("materialCliengguid"));
                            Integer attachcount = Integer.valueOf(
                                    this.frameAttachInfoNewService.getAttachInfoCount(info, (Date) null, (Date) null));
                            if (!frameAttachInfoNewService.isExistAttach(getViewData("materialCliengguid"))) {
                                attachcount = Integer.valueOf(0);
                            }
                            return attachcount.intValue();
                        }
                    });
        }
        return attachUploadModel;
    }

    public void changeMaterialCliengguid(String materialInstanceGuid, String materialCliengguid) {
        addViewData("materialInstanceGuid", materialInstanceGuid);
        addViewData("materialCliengguid", materialCliengguid);
        attachUploadModel = null;
    }

    /**
     * 附件删除方法
     * [一句话功能简述]
     */
    public void deleteAttach(String attachguids, String materialinstanceguid, String auditstatus) {

        AuditProjectMaterial material = projectMaterialService
                .getProjectMaterialDetail(materialinstanceguid, projectGuid).getResult();
        // 执行删除操作前，附件数量
        if (material != null) {
            attachNum = attachService.getAttachCountByClientGuid(material.getCliengguid());
        }

        if (StringUtil.isNotBlank(attachguids)) {
            String[] attachguidArr = attachguids.split(",");
            List<String> attachguidlist = Arrays.asList(attachguidArr);
            for (String attachguid : attachguidlist) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("attachguid", attachguid);
                List<AuditMaterialLibrary> recordlist = auditMaterialLibraryservice
                        .getAuditMaterialLibraryListByPage(sqlc.getMap(), 0, 0, "operatedate", "desc").getResult()
                        .getList();
                EpointFrameDsManager.commit();
                if (!recordlist.isEmpty()) {
                    String oldguid = recordlist.get(0).getCopyfrom();
                    if (StringUtil.isNotBlank(oldguid)) {
                        AuditMaterialLibrary oldmaterial = auditMaterialLibraryservice.find(oldguid);
                        if (oldmaterial != null) {
                            oldmaterial.setCount(oldmaterial.getCount() - 1);
                            auditMaterialLibraryservice.update(oldmaterial);
                        }
                    }
                }
                auditMaterialLibraryservice.deleteAuditMaterialLibraryByAttach(attachguid);
                EpointFrameDsManager.commit();
                // 删除附件
                attachService.deleteAttachByAttachGuid(attachguid);
                // 删除附件对应缩略图
                // attachService.deleteAttachByGuid(attachguid);
                EpointFrameDsManager.commit();
            }

            if (material != null && StringUtil.isNotBlank(material.getCertinfoinstanceguid())) {
                if (!attachService.isExistAttach(material.getCliengguid())) {
                    material.setCertinfoinstanceguid("");
                    material.setCertcount(ZwfwConstant.CONSTANT_STR_ZERO);
                    projectMaterialService.updateProjectMaterial(material);
                }
            }

            // 如果删除的附件和当前所有的附件一致，那就说明已经没有电子件了，需要更新材料提交状态
            if (attachNum == attachguidlist.size()) {
                projectMaterialService.updateProjectMaterialStatus(materialinstanceguid, projectGuid, -10);
            }

            // 待补正更新为待审核
            // if (auditstatus.equals(ZwfwConstant.Material_AuditStatus_DBZ)) {
            // projectMaterialService.updateProjectMaterialAuditStatus(materialinstanceguid,
            // Integer.parseInt(ZwfwConstant.Material_AuditStatus_YBZ),
            // projectGuid);
            // }
        }

        addCallbackParam("msg", "删除成功！");
    }

    /**
     * 打印pdf方法
     * [一句话功能简述]
     * 
     * @param attachguids
     */
    /*
     * public void printPdf(String attachguids) {
     * // 先查询附件类型，若附件中包含.rar和.zip则弹出提示
     * String[] attachguidArr = attachguids.split(",");
     * List<String> attachguidlist = Arrays.asList(attachguidArr);
     * boolean isnoprint = false;// 是否有不可打印的附件
     * if (EpointCollectionUtils.isNotEmpty(attachguidlist)) {
     * for (String attachguid : attachguidlist) {
     * FrameAttachInfo attachInfo =
     * attachService.getAttachInfoDetail(attachguid);
     * if (".zip".equalsIgnoreCase(attachInfo.getContentType())
     * || ".rar".equalsIgnoreCase(attachInfo.getContentType())) {
     * isnoprint = true;
     * }
     * }
     * if (isnoprint) {
     * addCallbackParam("error", ".rar和.zip类型不支持打印！");
     * }
     * else {
     * // 调用工具类，合并附件并返回合并后的pdf附件标识
     * String attachGuid = FileSystemUtils.printPdf(attachguids);
     * addCallbackParam("attachguid", attachGuid);
     * }
     * }
     * }
     */

    /**
     * 判断当前办件环节是否可编辑
     * [一句话功能简述]
     * 
     * @param taskGuid
     * @param pviGuid
     * @return
     */
    /*
     * public String checkActivityIsAllowEdit(String taskGuid, String pviGuid) {
     * String result = ZwfwConstant.CONSTANT_STR_ZERO;
     * AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid,
     * false).getResult();
     * if (auditTask != null) {
     * if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))
     * && !ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode())) {
     * // 即办件简易模式，列表可编辑
     * result = ZwfwConstant.CONSTANT_STR_ONE;
     * }
     * else {
     * // 获取流程环节信息，根据办件pviguid获取工作流信息，获取不到说明为第一环节，环节可编辑
     * 
     * IWFInstanceAPI9 wfinstance =
     * ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
     * if (StringUtil.isNotBlank(pviGuid)) {
     * ProcessVersionInstance pVersionInstance =
     * wfinstance.getProcessVersionInstance(pviGuid);
     * if (pVersionInstance != null) {
     * List<WorkflowWorkItem> workflowWorkItems = wfinstance
     * .getWorkItemListByPVIGuidAndStatus(pVersionInstance, null);
     * String activityGuid = "";
     * for (WorkflowWorkItem workflowItem : workflowWorkItems) {
     * if ("20".equals(workflowItem.getStatus().toString())) { // 运行中的工作项即当前流程环节
     * activityGuid = workflowItem.getActivityGuid();
     * break;
     * }
     * }
     * // 查找当前环节类型
     * AuditTaskRiskpoint riskpoint = auditTaskRiskpointService
     * .getAuditTaskRiskpointByActivityguid(activityGuid, false).getResult();
     * if (riskpoint != null) {
     * result = String.valueOf(riskpoint.getIsallowedit());
     * }
     * }
     * else {
     * result = ZwfwConstant.CONSTANT_STR_ONE;
     * }
     * }
     * else {
     * result = ZwfwConstant.CONSTANT_STR_ONE;
     * }
     * }
     * }
     * return result;
     * }
     */

    /*
     * public JSONObject hasSubMaterial() {
     * String projectmaterialguid = getRequestParameter("projectmaterialguid");
     * String projectguid = getRequestParameter("projectguid");
     * AuditProjectMaterial projectMaterial = projectMaterialService
     * .getProjectMaterialDetail(projectmaterialguid, projectguid).getResult();
     * String taskmaterialguid = projectMaterial.getTaskmaterialguid();
     * AuditTaskMaterial audittaskmaterial =
     * iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskmaterialguid)
     * .getResult();
     * String taskmaterialid = audittaskmaterial.getMaterialid();
     * // 是否是ai审批
     * boolean isaisp = false;
     * AuditTaskExtension auditTaskExtension = auditTaskExtensionService
     * .getTaskExtensionByTaskGuid(auditProject.getTaskguid(),
     * false).getResult();
     * if (auditTaskExtension != null &&
     * StringUtil.isNotBlank(auditTaskExtension.getAisptype())
     * &&
     * !ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTaskExtension.getAisptype()))
     * {
     * isaisp = true;
     * }
     * return rsMateralService.hasSubMaterial(taskmaterialguid, taskmaterialid,
     * auditProject, isaisp);
     * }
     */

}
