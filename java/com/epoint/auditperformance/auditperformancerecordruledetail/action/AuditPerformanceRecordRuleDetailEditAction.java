package com.epoint.auditperformance.auditperformancerecordruledetail.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerecordruledetail.domain.AuditPerformanceRecordRuleDetail;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评记录考评细则修改页面对应的后台
 * 
 * @author 泪流云
 * @version [版本号, 2018-01-09 16:07:13]
 */
@RightRelation(AuditPerformanceRecordRuleDetailListAction.class)
@RestController("auditperformancerecordruledetaileditaction")
@Scope("request")
public class AuditPerformanceRecordRuleDetailEditAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 6640118228587199740L;

    @Autowired
    private IAuditPerformanceRecordRuleDetailService service;

    /**
     * 考评记录考评细则实体对象
     */
    private AuditPerformanceRecordRuleDetail dataBean = null;

    /**
    * 考评对象类别下拉列表model
    */
    private List<SelectItem> objecttypeModel = null;
    /**
     * 考评方式下拉列表model
     */
    private List<SelectItem> typeModel = null;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditPerformanceRecordRuleDetail();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public AuditPerformanceRecordRuleDetail getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditPerformanceRecordRuleDetail dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getObjecttypeModel() {
        if (objecttypeModel == null) {
            objecttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评对象类别", null, false));
        }
        return this.objecttypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评方式", null, false));
        }
        return this.typeModel;
    }

}
