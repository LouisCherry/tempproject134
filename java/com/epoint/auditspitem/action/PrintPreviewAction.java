package com.epoint.auditspitem.action;

import com.epoint.auditspitem.api.IAuditSpItemService;
import com.epoint.auditspitem.api.entity.AuditSpItem;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目信息表list页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:30:48]
 */
@RestController("printpreviewaction")
@Scope("request")
public class PrintPreviewAction extends BaseController
{
    @Autowired
    private IAuditSpItemService service;

    /**
     * 项目信息表实体对象
     */
    private AuditSpItem dataBean = null;

    private List<SelectItem> areCodeModel;

    private String jurisdictions;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpItem();
        }
        addCallbackParam("imgdata", dataBean.get("imgdata"));
    }

    public AuditSpItem getDataBean() {
        return dataBean;
    }

    public List<SelectItem> getAreCodeModel() {
        if (areCodeModel == null) {
            areCodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "阶段配置辖区", null, false));
        }
        return this.areCodeModel;
    }

    public String getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(String jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

}
