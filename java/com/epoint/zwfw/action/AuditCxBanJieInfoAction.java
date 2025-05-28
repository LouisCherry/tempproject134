package com.epoint.zwfw.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.xmz.api.ISendMaterials;
import com.epoint.zhenggai.api.ZhenggaiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/***
 * 业务中台线下申报对接
 */
@RestController("auditcxbanjieinfoaction")
@Scope("request")
public class AuditCxBanJieInfoAction extends BaseController {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditProject iauditproject;
    String projectguid = null;

    @Override
    public void pageLoad() {
        projectguid = this.getRequestParameter("projectguid");
        AuditProject auditProject = iauditproject.getAuditProjectByRowGuid("rowguid,chaoxiandetailurl", projectguid, "").getResult();
        if (auditProject != null) {
            addCallbackParam("chaoxiandetailurl", auditProject.getStr("chaoxiandetailurl"));
        } else {
            addCallbackParam("errormsg", "未找到办件!");
        }
    }

}
