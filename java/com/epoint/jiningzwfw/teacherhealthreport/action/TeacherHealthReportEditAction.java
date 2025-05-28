package com.epoint.jiningzwfw.teacherhealthreport.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jiningzwfw.teacherhealthreport.api.ITeacherHealthReportService;
import com.epoint.jiningzwfw.teacherhealthreport.api.entity.TeacherHealthReport;

/**
 * 教师资格体检报告修改页面对应的后台
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
@RightRelation(TeacherHealthReportListAction.class)
@RestController("teacherhealthreporteditaction")
@Scope("request")
public class TeacherHealthReportEditAction extends BaseController
{
    private static final long serialVersionUID = -6850211125692954434L;

    @Autowired
    private ITeacherHealthReportService service;

    /**
     * 教师资格体检报告实体对象
     */
    private TeacherHealthReport dataBean = null;

    /**
     * 县区下拉列表model
     */
    private List<SelectItem> countyModel = null;
    /**
     * 是否合格单选按钮组model
     */
    private List<SelectItem> isPassModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new TeacherHealthReport();
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

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCountyModel() {
        if (countyModel == null) {
            countyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "县区名称", null, false));
        }
        return this.countyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsPassModel() {
        if (isPassModel == null) {
            isPassModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isPassModel;
    }

    public TeacherHealthReport getDataBean() {
        return dataBean;
    }

    public void setDataBean(TeacherHealthReport dataBean) {
        this.dataBean = dataBean;
    }

}
