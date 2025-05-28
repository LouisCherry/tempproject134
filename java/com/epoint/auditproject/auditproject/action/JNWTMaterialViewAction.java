package com.epoint.auditproject.auditproject.action;

import java.util.Arrays;
import java.util.List;

import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

/**
 * 材料预览页面对应的后台
 * @author Administrator
 *
 */
@RestController("jnwtmaterialviewaction")
@Scope("request")
public class JNWTMaterialViewAction extends BaseController
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
    IAuditTaskMaterial iAuditTaskMaterialService;
    
    @Autowired
    IHandleMaterial handleMaterialService;
    
    @Autowired
    private IHandleConfig handleConfigService;
    
    @Autowired
    private IConfigService iConfigService;
    


    @Override
    public void pageLoad() {
        String baseUrl = "";
        if (StringUtil.isNotBlank(getRequestParameter("srcurl"))) {
            baseUrl = "http://" + EncodeUtil.decode(getRequestParameter("srcurl")) + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
            addViewData("baseUrl", baseUrl);
        }
        materialinstanceguid = getRequestParameter("materialinstanceguid");
        String officeWeb365_Server = handleConfigService.getFrameConfig("AS_OfficeWeb365_Server", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        addCallbackParam("officeWeb365", officeWeb365_Server);
        List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(materialinstanceguid);
        JSONArray jsonArray = new JSONArray();
        if (frameAttachInfos != null) {
            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("attachguid", frameAttachInfo.getAttachGuid());
                jsonObject.put("attachfilename", frameAttachInfo.getAttachFileName());
                String url = OfficeWebUrlEncryptUtil.getEncryptUrl(baseUrl + frameAttachInfo.getAttachGuid(), frameAttachInfo.getContentType());
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
        addCallbackParam("materials", jsonArray);
        
    }
    

    public AuditProjectMaterial getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditProjectMaterial dataBean) {
        this.dataBean = dataBean;
    }
}
