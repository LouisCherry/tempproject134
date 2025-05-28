package com.epoint.auditproject.auditproject.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditocrid.domain.AuditOcrId;
import com.epoint.basic.auditorga.auditocrid.inter.IAuditOcrId;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.fileupload.MultipartRequest;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController("jnauditgpyuploadaction")
@Scope("request")
public class JNAuditGpyuploadAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -7964796173554880741L;

    /**
     * 照片名
     */
    private String attachName;

    /**
     * 材料名
     */
    private String material;

    /**
     * 办件标识
     */
    private String projectGuid;

    /**
     * 办件过程标识
     */
    private String processVersionInstanceGuid;

    /**
     * 附件标识
     */
    private String hidAttachGuid;

    private String biguid;

    /**
     * 材料列表
     */
    private List<SelectItem> MaterialModal;

    /**
     * 附件上传model
     */
    private FileUploadModel9 attachUploadModel;

    @Autowired
    IAuditProject auditProjectService;

    @Autowired
    IHandleMaterial handleMaterialService;

    @Autowired
    IAttachService attachService;

    @Autowired
    private IAuditProjectMaterial auditMaterialService;

    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;

    @Autowired
    IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    ICertConfigExternal iCertConfigExternal;

    @Autowired
    IAuditOcrId iAuditOcrId;

    @Autowired
    IConfigService iConfigService;

    private int attachCount;

    private String materialInstanceGuid;
    private String cliengGuid;
    private String auditstatus;

    @Override
    public void pageLoad() {
        projectGuid = getRequestParameter("projectGuid");
        if (!isPostback()) {
            materialInstanceGuid = getRequestParameter("materialInstanceGuid");
            cliengGuid = getRequestParameter("cliengGuid");
            auditstatus = getRequestParameter("auditstatus");
            this.addCallbackParam("flag", "1");
        }
        else {
            materialInstanceGuid = this.getViewData("materialInstanceGuid");
            cliengGuid = this.getViewData("cliengGuid");
            auditstatus = this.getViewData("auditstatus");
        }

        addCallbackParam("materialInstanceGuid", materialInstanceGuid);
        addCallbackParam("cliengGuid", cliengGuid);
        addCallbackParam("auditstatus", auditstatus);
        try {
            // 这里需要判断一下当前的附件数，如果大于0个，需要控制显示删除按钮
            String cliengGuid = getRequestParameter("cliengGuid");
            List<FrameAttachInfo> list = getAttachList(cliengGuid);
            int count = list.size();
            attachCount = count;
            String rtnType = "add";
            if (count > 0) {
                rtnType = "addanddel";
            }
            addCallbackParam("type", rtnType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 判断当前材料是否是共享材料，共享材料对应的照面信息是否是身份证
    public void doJudgeScan() {
        // 判断是否配置系统参数
        String AS_ID_CERTCATCODE = iConfigService.getFrameConfigValue("AS_ID_CERTCATCODE");
        if (StringUtil.isNotBlank(AS_ID_CERTCATCODE)) {
            // 材料实例Guid
            String materialInstanceGuid = getRequestParameter("materialInstanceGuid");
            String projectGuid = getRequestParameter("projectGuid");
            // 获取办件材料实例
            AuditProjectMaterial auditProjectMaterial = auditMaterialService
                    .getProjectMaterialDetail(materialInstanceGuid, projectGuid).getResult();
            // 获取事项材料
            AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                    .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
            // 获取事项对应的共享材料guid
            if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                // 获取共享材料对应的照面信息
                String areacode="";
                // 如果是镇村接件
                if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
                    areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                }else{
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
                CertCatalog certCatalog = iCertConfigExternal
                        .getCatalogByCatalogid(auditTaskMaterial.getSharematerialguid(),areacode);
                // 系统参数的身份证证照目录编号匹配
                if (AS_ID_CERTCATCODE.equals(certCatalog.getTycertcatcode())) {
                    // 此处返回识别的类型，默认0为不识别，1为身份证、2为营业执照
                    this.addCallbackParam("ocrtype", "1");
                    AuditOcrId AuditOcrId = iAuditOcrId.getAuditOcrIdByMaterialInstanceGuid(materialInstanceGuid)
                            .getResult();
                    if (AuditOcrId != null) {
                        JSONObject returnJson = new JSONObject();
                        returnJson.put("name", AuditOcrId.getName());
                        returnJson.put("sex", AuditOcrId.getSex());
                        returnJson.put("nation", AuditOcrId.getNation());
                        returnJson.put("birthday", AuditOcrId.getBirthday());
                        returnJson.put("address", AuditOcrId.getAddress());
                        returnJson.put("idnumber", AuditOcrId.getIdnumber());
                        returnJson.put("qfjg", AuditOcrId.getQfjg());
                        returnJson.put("valuedate", AuditOcrId.getValuedate());
                        this.addCallbackParam("ocrdata", returnJson);
                    }
                }
            }
        }
    }

    public void IDScan() {
        String resultText = "";
        String cliengGuid = getRequestParameter("cliengGuid");
        String attachName = getRequestParameter("attachName");
        List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(cliengGuid);
        for (FrameAttachInfo frameAttachInfo : attachInfoList) {
            if (frameAttachInfo.getAttachFileName().equals(attachName)) {
                FrameAttachStorage storage = attachService.getAttach(frameAttachInfo.getAttachGuid());
                byte[] contents = FileManagerUtil.getContentFromInputStream(storage.getContent());
                resultText = Base64Util.encode(contents);
                String url = iConfigService.getFrameConfigValue("AS_ID_URL");
//                String url = "http://localhost:9221/tyocr/area64";
                this.addCallbackParam("ocrurl", url);
                this.addCallbackParam("ocrtype", "1");
                JSONObject jsonobj = new JSONObject();
                jsonobj.put("picture", resultText);
                jsonobj.put("type", "1");
                break;
            }
        }
        // 设置返回参数，
        this.addCallbackParam("resulttext", resultText);
    }

    // 插入/修改证照元数据信息
    public void dataHanle() throws UnsupportedEncodingException {
        String name = getDecodeStr(getRequestParameter("name"));
        String sex = getDecodeStr(getRequestParameter("sex"));
        String nation = getDecodeStr(getRequestParameter("nation"));
        String birthday = getDecodeStr(getRequestParameter("birthday"));
        String address = getDecodeStr(getRequestParameter("address"));
        String idnumber = getDecodeStr(getRequestParameter("idnumber"));
        String qfjg = getDecodeStr(getRequestParameter("qfjg"));
        String valuedate = getDecodeStr(getRequestParameter("valuedate"));
        String materialInstanceGuid = getDecodeStr(getRequestParameter("materialInstanceGuid"));
        // 查询该材料实例对应是否生成元数据信息
        AuditOcrId auditOcrId = iAuditOcrId.getAuditOcrIdByMaterialInstanceGuid(materialInstanceGuid).getResult();
        AuditOcrId auditOcrIdIns = new AuditOcrId();
        String RowGuid = UUID.randomUUID().toString();
        auditOcrIdIns.setOperatedate(new Date());
        auditOcrIdIns.setRowguid(RowGuid);
        if (StringUtil.isNotBlank(name)) {
            auditOcrIdIns.setName(name);
        }
        if (StringUtil.isNotBlank(sex)) {
            auditOcrIdIns.setSex(sex);
        }
        if (StringUtil.isNotBlank(nation)) {
            auditOcrIdIns.setNation(nation);
        }
        if (StringUtil.isNotBlank(birthday)) {
            auditOcrIdIns.setBirthday(birthday);
        }
        if (StringUtil.isNotBlank(address)) {
            auditOcrIdIns.setAddress(address);
        }
        if (StringUtil.isNotBlank(idnumber)) {
            auditOcrIdIns.setIdnumber(idnumber);
        }
        if (StringUtil.isNotBlank(qfjg)) {
            auditOcrIdIns.setQfjg(qfjg);
        }
        if (StringUtil.isNotBlank(valuedate)) {
            auditOcrIdIns.setValuedate(valuedate);
        }
        auditOcrIdIns.setMaterialinstanceguid(materialInstanceGuid);
        // 更新数据
        if (auditOcrId != null) {
            auditOcrIdIns.setRowguid(auditOcrId.getRowguid());
            iAuditOcrId.updateAuditOcrId(auditOcrIdIns);
        }
        // 插入数据
        else {
            iAuditOcrId.addAuditOcrId(auditOcrIdIns);
        }
    }

    public String getDecodeStr(String encodeStr) throws UnsupportedEncodingException {
        if (StringUtil.isNotBlank(encodeStr)) {
            return URLDecoder.decode(encodeStr, "UTF-8");
        }
        else {
            return "";
        }
    }

    public List<SelectItem> getMaterialModal() {
        if (MaterialModal == null) {
            List<Record> projectMaterial = handleMaterialService.getProjectMaterialALL(projectGuid, biguid).getResult();
            String area = "";
            // 如果是镇村接件
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                            .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                area = ZwfwUserSession.getInstance().getBaseAreaCode();
            }
            else {
                area = ZwfwUserSession.getInstance().getAreaCode();
            }

            AuditProject auditproject = auditProjectService.getAuditProjectByRowGuid(projectGuid, area).getResult();
            //情形
            Map<String, Integer> caseMap = null;
            if (auditproject != null && StringUtil.isNotBlank(auditproject.getTaskcaseguid())
                    && !"none".equals(auditproject.getTaskcaseguid())) {
                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(auditproject.getTaskcaseguid()).getResult();
                caseMap = new HashMap<>(16);
                //转成map方便查找
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                    }
                }
            }
            MaterialModal = new ArrayList<SelectItem>();
            projectMaterial.sort((a,
                    b) -> (StringUtil.isNotBlank(b.get("Ordernum")) ? Integer.valueOf(b.get("Ordernum").toString())
                            : Integer.valueOf(0))
                                    .compareTo(StringUtil.isNotBlank(a.get("Ordernum"))
                                            ? Integer.valueOf(a.get("Ordernum").toString()) : 0));
            for (int i = 0; i < projectMaterial.size(); i++) {
                if (!"20".equals(projectMaterial.get(i).get("SUBMITTYPE"))
                        && (caseMap == null || caseMap.containsKey(projectMaterial.get(i).get("TASKMATERIALGUID")))) {
                    MaterialModal.add(new SelectItem(
                            projectMaterial.get(i).get("MATERIALINSTANCEGUID") + "&"
                                    + projectMaterial.get(i).get("cliengguid") + "&"
                                    + projectMaterial.get(i).get("auditstatus"),
                            projectMaterial.get(i).getStr("MATERIALNAME")));
                }
            }
        }
        return this.MaterialModal;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getHidAttachGuid() {
        return hidAttachGuid;
    }

    public void setHidAttachGuid(String hidAttachGuid) {
        this.hidAttachGuid = hidAttachGuid;
    }

    public String getProcessVersionInstanceGuid() {
        return processVersionInstanceGuid;
    }

    public void setProcessVersionInstanceGuid(String processVersionInstanceGuid) {
        this.processVersionInstanceGuid = processVersionInstanceGuid;
    }

    AttachHandler9 handler = new AttachHandler9()
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void afterSaveAttachToDB(Object attach) {
          
        }

        @Override
        public boolean beforeSaveAttachToDB(AttachStorage attach) {

            AttachStorage storage = (AttachStorage) attach;

            attachUploadModel.getExtraDatas().put("signPicture",
                    "data:" + storage.getContentType() + ";base64," + Base64Util.encode(storage.getContent()));

            return true;
        }

    };

    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {
            String cliengGuid = getRequestParameter("cliengGuid");
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }

    public void uploadImage() {
        try {
            MultipartRequest request = (MultipartRequest) getRequestContext().getReq();
            Map<String, List<FileItem>> map = request.getFileParams();

            for (Map.Entry<String, List<FileItem>> en : map.entrySet()) {
                List<FileItem> fileItems = en.getValue();
                if (fileItems != null && !fileItems.isEmpty()) {
                    for (FileItem fileItem : fileItems) {
                        if (!fileItem.isFormField()) {// 是文件流而不是表单数据
                            // 从文件流中获取文件类型
                            //String contentType = fileItem.getContentType();
                            // 获取流大小
                            long size = fileItem.getSize();
                            // 获取流
                            InputStream inputStream = fileItem.getInputStream();
                            String cliengGuid = getRequestParameter("cliengGuid");
                            // 附件信息
                            // TODO attachguid前台产生传递过来
                            String attachguid = UUID.randomUUID().toString();
                            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                            frameAttachInfo.setAttachGuid(attachguid);
                            frameAttachInfo.setCliengGuid(cliengGuid);
                            frameAttachInfo.setAttachFileName(getRequestParameter("attachName"));
                            frameAttachInfo.setCliengTag(cliengGuid.substring(0, 1));
                            frameAttachInfo.setUploadUserGuid(userSession.getUserGuid());
                            frameAttachInfo.setUploadUserDisplayName(userSession.getDisplayName());
                            frameAttachInfo.setUploadDateTime(new Date());
                            frameAttachInfo.setContentType("application/octet-stream");
                            frameAttachInfo.setAttachLength(size);
                            attachService.addAttach(frameAttachInfo, inputStream);


                        }

                    }
                }
            }
        }
        catch (

        Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 处理返回数据
        Record rtnInfo = new Record();
        rtnInfo.put("customer", "success");

        sendRespose(JsonUtil.objectToJson(rtnInfo));

    }

    public void deleteFile() {
        String cliengGuid = getRequestParameter("cliengGuid");
        int count = getAttachList(cliengGuid).size();
        String materialInstanceGuid = getRequestParameter("materialInstanceGuid");
        // 如果删除的附件和当前所有的附件一致，那就说明已经没有电子件了，需要更新材料提交状态
        if (count == 0) {
            auditMaterialService.updateProjectMaterialStatus(materialInstanceGuid, projectGuid, -10);
        }
        String auditstatus = getRequestParameter("auditStatus");
        // 待补正更新为待审核
        if (auditstatus.equals(ZwfwConstant.Material_AuditStatus_DBZ)) {
            auditMaterialService.updateProjectMaterialAuditStatus(materialInstanceGuid,
                    Integer.parseInt(ZwfwConstant.Material_AuditStatus_YBZ), projectGuid);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        List<FrameAttachInfo> list = attachService.getAttachInfoListByGuid(cliengGuid);
        return list;
    }

    public void changeMaterial(String materialInstanceGuid, String cliengGuid, String auditstatus) {
        this.addViewData("materialInstanceGuid", materialInstanceGuid);
        this.addViewData("cliengGuid", cliengGuid);
        this.addViewData("auditstatus", auditstatus);
    }

}
