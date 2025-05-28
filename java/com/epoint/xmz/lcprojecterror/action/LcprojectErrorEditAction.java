package com.epoint.xmz.lcprojecterror.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.util.TARequestUtil;
import com.epoint.xmz.lcprojecterror.api.ILcprojectErrorService;
import com.epoint.xmz.lcprojecterror.api.entity.LcprojectError;

/**
 * 浪潮推送失败记录表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-06-29 10:03:18]
 */
@RightRelation(LcprojectErrorListAction.class)
@RestController("lcprojecterroreditaction")
@Scope("request")
public class LcprojectErrorEditAction extends BaseController
{

    public static final String lclcurl = ConfigUtil.getConfigValue("epointframe", "lclcurl");

    public static final String lcjbxxurl = ConfigUtil.getConfigValue("epointframe", "lcjbxxurl");

    @Autowired
    private ILcprojectErrorService service;

    @Autowired
    private IAuditProject auditProjectServcie;

    /**
     * 浪潮推送失败记录表实体对象
     */
    private LcprojectError dataBean = null;

    /**
     * 辖区编码下拉列表model
     */
    private List<SelectItem> areacodeModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new LcprojectError();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        AuditProject auditProject = auditProjectServcie.getAuditProjectByRowGuid(dataBean.getProjectguid(), null)
                .getResult();
        if (auditProject != null && StringUtil.isNotBlank(dataBean.getStr("record"))) {
            String url = "";
            if ("otherSystemAddCourse".equals(dataBean.getStr("type"))) {
                url = lclcurl;
            }
            else if ("otherSystemAccpet".equals(dataBean.getStr("type"))) {
                url = lcjbxxurl;
            }

            String resultsign = TARequestUtil.sendPostInner(url, dataBean.getStr("record"), "", "");
            JSONObject jsonObject = JSONObject.parseObject(resultsign);
            JSONObject custom = jsonObject.getJSONObject("custom");
            if ("1".equals(custom.getString("code"))) {
                addCallbackParam("msg", "数据重推成功！");
            }
            else {
                log.info("resultsign:"+resultsign);
                addCallbackParam("msg", "数据重推失败！");
            }
        }
        else {
            addCallbackParam("msg", "办件信息未查询到！");
        }

    }

    public LcprojectError getDataBean() {
        return dataBean;
    }

    public void setDataBean(LcprojectError dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getAreacodeModel() {
        if (areacodeModel == null) {
            areacodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区对应关系", null, false));
        }
        return this.areacodeModel;
    }

}
