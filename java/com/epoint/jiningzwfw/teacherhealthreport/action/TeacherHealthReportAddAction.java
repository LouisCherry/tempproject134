package com.epoint.jiningzwfw.teacherhealthreport.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jiningzwfw.teacherhealthreport.api.ITeacherHealthReportService;
import com.epoint.jiningzwfw.teacherhealthreport.api.entity.TeacherHealthReport;

/**
 * 教师资格体检报告新增页面对应的后台
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
@RightRelation(TeacherHealthReportListAction.class)
@RestController("teacherhealthreportaddaction")
@Scope("request")
public class TeacherHealthReportAddAction extends BaseController
{
    private static final long serialVersionUID = 3002882209578402141L;
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
    /**
     * 唯一表示
     */
    private String rowGuid;

    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new TeacherHealthReport();
        }
        if (!isPostback()) {
            rowGuid = UUID.randomUUID().toString();
            addViewData("rowguid", rowGuid);
        }
        else {
            rowGuid = getViewData("rowguid");
        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        // 添加之前先判断是否已经存在啦 -根据身份证号
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("sfz", dataBean.getSfz());
        TeacherHealthReport teacherHealthReport = service.getTeacherHealthReportByCondition(sql.getMap());
        if (teacherHealthReport != null) {
            addCallbackParam("info", "已添加过该体检报告记录，请勿重复添加！");
            return;
        }
        // 判断当前的主键是否已经存在
        TeacherHealthReport healthReport = service.find(rowGuid);
        if (healthReport != null) {
            rowGuid = UUID.randomUUID().toString();
        }
        dataBean.setRowguid(rowGuid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new TeacherHealthReport();
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
