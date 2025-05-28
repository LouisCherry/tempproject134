package com.epoint.jiningzwfw.teacherhealthreport.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.jiningzwfw.teacherhealthreport.api.ITeacherHealthReportService;
import com.epoint.jiningzwfw.teacherhealthreport.api.entity.TeacherHealthReport;

/**
 * 教师资格体检报告详情页面对应的后台
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
@RightRelation(TeacherHealthReportListAction.class)
@RestController("teacherhealthreportdetailaction")
@Scope("request")
public class TeacherHealthReportDetailAction extends BaseController
{
    private static final long serialVersionUID = 5412786681180377115L;

    @Autowired
    private ITeacherHealthReportService service;

    /**
     * 教师资格体检报告实体对象
     */
    private TeacherHealthReport dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new TeacherHealthReport();
        }
    }

    public TeacherHealthReport getDataBean() {
        return dataBean;
    }
}
