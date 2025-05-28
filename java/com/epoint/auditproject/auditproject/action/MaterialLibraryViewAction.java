package com.epoint.auditproject.auditproject.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.material.domain.AuditMaterialLibraryR;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibrary;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibraryR;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

/**
 * 材料预览页面对应的后台
 * @author Administrator
 *
 */
@RestController("materiallibraryviewaction")
@Scope("request")
public class MaterialLibraryViewAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = -2848400623616957620L;

    private AuditProjectMaterial dataBean = null;

    
    private String materialinstanceguid = "";

    @Autowired
    private IAttachService attachService;

    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;
    
    @Autowired
    private IAuditTaskMaterial taskMaterialservice;
    
    @Autowired
    IHandleMaterial handleMaterialService;
    
    @Autowired
    private IHandleConfig handleConfigService;
    
    @Autowired
    private IConfigService iConfigService;
    
    @Autowired
    private IAuditMaterialLibraryR auditMaterialLibraryRService;
    @Autowired
    private IAuditProjectMaterial projectmaterialService;
    @Autowired
    private IAuditMaterialLibrary auditMaterialLibraryService;
    
    @Override
    public void pageLoad() {
        if(StringUtil.isNotBlank(getRequestParameter("srcurl"))){
            String baseUrl = "http://" + EncodeUtil.decode(getRequestParameter("srcurl")) + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
            addViewData("baseUrl", baseUrl);
        }
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

    }
    

    public void materialChange() {
        //接收变量
        String dqmaterialinstanceguid  = getRequestParameter("materialInstanceGuid");
        String dqprojectguid  = getRequestParameter("projectGuid");
        String useRelation  = getRequestParameter("useRelation");
        String certnuml  = getRequestParameter("certnuml");
        String ou  = getRequestParameter("ou");
        String materialname  = getRequestParameter("materialname");
        String dqmaterialname  = getRequestParameter("material");       
        AuditProjectMaterial material = projectmaterialService.getProjectMaterialDetail(dqmaterialinstanceguid, dqprojectguid).getResult();
        List<String> guids = new ArrayList<>();
        if(material!=null){
            String materialid = taskMaterialservice.getAuditTaskMaterialByRowguid(material.getTaskmaterialguid()).getResult().getMaterialid();
            AuditMaterialLibraryR auditMaterialLibraryR =  auditMaterialLibraryRService.selectRelationByMaterialid(materialid).getResult();
            if(auditMaterialLibraryR!=null){
                List<AuditMaterialLibraryR> relations = auditMaterialLibraryRService.selectRelationByShareMaterialGuid(auditMaterialLibraryR.getSharematerialguid()).getResult();
                if(relations!=null&&!relations.isEmpty()){
                    for(AuditMaterialLibraryR relation : relations ){
                        guids.add(relation.getMaterialid());
                    }
                }
            }
        }
        
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("ownerno", certnuml);
        sqlc.eq("firstflag", ZwfwConstant.CONSTANT_STR_ONE);
        // 页面上搜索条件
        if (StringUtil.isNotBlank(ou)) {
            sqlc.eq("ouname", ou);
        }
        if (StringUtil.isNotBlank(materialname)) {
            sqlc.like("materialname", materialname);
        }
        sqlc.setOrderDesc("count");
        List<Record> list = new ArrayList<Record>();
        if (StringUtil.isNotBlank(useRelation) && "true".equals(useRelation)) {
            sqlc.in("materialid", "'" + StringUtil.join(guids, "','") + "'");
            list = auditMaterialLibraryService
                    .getRelatedAuditMaterialLibraryListByPage(sqlc.getMap(), 0, 0, "", "",dqmaterialname)
                    .getResult();
        }
        else{
            sqlc.notin("materialid", "'" + StringUtil.join(guids, "','") + "'");
            list = auditMaterialLibraryService
                    .getRelatedAuditMaterialLibraryListByPage(sqlc.getMap(), 0, 0, "", "",dqmaterialname)
                    .getResult();
        }
        JSONArray jsonArray = new JSONArray();
        if(list!=null&&!list.isEmpty()){
            String[] contenttype = iConfigService.getFrameConfigValue("showPhotoViewFormat").split(";");
            for(Record record : list){
                JSONObject jsonObject = new JSONObject();
                String name = record.get("materialname1");
                jsonObject.put("attachguid", record.get("attachguid"));
                jsonObject.put("materialname", name);
                String fType = record.getStr("filetype");
                if (Arrays.asList(contenttype).contains(fType)) {
                    fType = "pic"; 
                }
                jsonObject.put("filetype", fType);
                String url = OfficeWebUrlEncryptUtil.getEncryptUrl(getViewData("baseUrl") + record.get("attachguid"), record.getStr("filetype"));
                jsonObject.put("encryptUrl", url);
                jsonArray.add(jsonObject);  
            }
        }
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
            File file = FileManagerUtil.createFile(Filepath);
            if (!file.exists()) {
                FileManagerUtil.createFile(ClassPathUtil.getDeployWarPath() + "AttachStorage/" + attachGuid).mkdirs();
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


}
