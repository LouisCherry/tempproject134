package com.epoint.auditresource.auditrsitembaseinfo.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.individual.domain.AuditRsIndividualBaseinfo;
import com.epoint.basic.auditresource.individual.inter.IAuditIndividual;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 项目办件信息
 */
@RestController("jnauditxiangmuprojectdetailaction")
@Scope("request")
public class JNAuditXiangmuProjectDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 办件实体对象
     */
    private AuditProject dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    /**
     * 办件接口
     */
    @Autowired
    private IAuditProject auditProjectImpl;
    /**
     * 项目guid
     */
    private String xiangmuguid = "";

    private String individualguid = "";

    private String companyid = "";
    /**
     * 企业库接口
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    /**
     * 人员库接口
     */
    @Autowired
    private IAuditIndividual iAuditIndividual;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditProjectSparetime projectSparetimeService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Override
    public void pageLoad() {
        xiangmuguid = getRequestParameter("guid");
        individualguid = getRequestParameter("individualguid");
        companyid = getRequestParameter("companyid");
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    SqlConditionUtil sql2 = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(xiangmuguid)) {
                        sql.eq("xiangmubh", xiangmuguid);
                    }
                    if (StringUtil.isNotBlank(individualguid)) {
                        sql2.eq("personid", individualguid);
                        List<AuditRsIndividualBaseinfo> list = iAuditIndividual
                                .selectIndividualByCondition(sql2.getMap()).getResult();
                        List<String> rowGuids = new ArrayList<String>();
                        for (AuditRsIndividualBaseinfo individualBaseinfo : list) {
                            rowGuids.add(individualBaseinfo.getRowguid());
                        }
                        if (rowGuids != null) {
                            sql.in("applyeruserguid", "'" + StringUtil.join(rowGuids, "','") + "'");
                        }
                    }
                    if (StringUtil.isNotBlank(companyid)) {
                        sql2.eq("companyid", companyid);
                        List<AuditRsCompanyBaseinfo> list = iAuditRsCompanyBaseinfo
                                .selectAuditRsCompanyBaseinfoByCondition(sql2.getMap()).getResult();
                        List<String> rowGuids = new ArrayList<String>();
                        for (AuditRsCompanyBaseinfo company : list) {
                            rowGuids.add(company.getRowguid());
                        }
                        if (rowGuids != null) {
                            sql.in("applyeruserguid", "'" + StringUtil.join(rowGuids, "','") + "'");
                        }
                    }
                    sql.setSelectFields(
                            "rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,windowname,promise_day,acceptuserdate,banjiedate,ACCEPTUSERDATE,tasktype");
                    PageData<AuditProject> pageData = auditProjectImpl
                            .getAuditProjectPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    if (pageData.getList() != null && pageData.getList().size() > 0) {
                        for (AuditProject auditProject : pageData.getList()) {
                            String sparetime = getSpareTime(auditProject.getRowguid(), auditProject.getStatus());
                            if(sparetime.startsWith("超时")){
                                auditProject.set("ischaoshi", "1");
                            }
                            auditProject.set("sparetime", sparetime);
                            if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditProject.getTasktype()))) {
                                auditProject.set("promise_day", "即办");
                            }
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();

                }

            };
        }
        return model;
    }

    public AuditProject getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditProject();
        }
        return dataBean;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }

    /**
     * 获取剩余时间
     * 
     * @return String
     */
    public String getSpareTime(String rowguid, int status) {
        String result = "";
        // 默认剩余时间
        String defaultSparaeTime = "";
        if (StringUtil.isNotBlank(rowguid) && StringUtil.isNotBlank(status)) {
            String fields = " rowguid,taskguid,projectname,tasktype ";
            AuditProject auditProject = auditProjectService
                    .getAuditProjectByRowGuid(fields, rowguid, ZwfwUserSession.getInstance().getAreaCode()).getResult();
            if (auditProject != null) {

                AuditProjectSparetime auditProjectSparetime = projectSparetimeService.getSparetimeByProjectGuid(rowguid)
                        .getResult();
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                if (auditProjectSparetime == null) {
                    return defaultSparaeTime;
                }
                else if (auditProject.getTasktype() == Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ)) {
                    if ((status >= ZwfwConstant.BANJIAN_STATUS_YSL && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ)
                            || (status == ZwfwConstant.BANJIAN_STATUS_YJJ
                                    && ZwfwConstant.CONSTANT_INT_ONE == auditTaskExtension.getIszijianxitong()
                                    && ZwfwConstant.ZIJIANMODE_JJMODE
                                            .equals(String.valueOf(auditTaskExtension.getZijian_mode())))) {
                        if (StringUtil.isNotBlank(auditProjectSparetime.getSpareminutes())) {
                            int i = auditProjectSparetime.getSpareminutes();
                            if (i > 0) {
                                if (i < 1440) {
                                    result = "剩余" + CommonUtil.getSpareTimes(i);
                                }
                                else {
                                    result = "剩余" + CommonUtil.getSpareTimes(i);
                                }
                            }
                            else {
                                i = -i;
                                result = "超时" + CommonUtil.getSpareTimes(i);
                            }
                        }
                        else {
                            return defaultSparaeTime;
                        }
                    }
                    else if (status >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                        if (StringUtil.isNotBlank(auditProjectSparetime.getSpareminutes())) {
                            int i = auditProjectSparetime.getSpareminutes();
                            if (i < 0) {// 如果剩余时间为负数:办结超时
                                result = "已办结(超过" + CommonUtil.getSpareTimes(i);
                            }
                            else {
                                result = "已办结" ;
                            }
                        }
                        else {// 办结用时为空：默认--
                            return defaultSparaeTime;
                        }
                    }
                    else {
                        return defaultSparaeTime;
                    }
                }
                else {//非承诺件
                    result = ZwfwConstant.getItemtypeKey(auditProject.getTasktype().toString());
                }
            }
            else {
                result = defaultSparaeTime;
            }
        }
        return result;
    }
}
