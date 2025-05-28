package com.epoint.xmgj.auditrsitembaseinfo.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.xmgj.auditrsitembaseinfo.api.IAuditRsItemBaseinfoService;
import com.epoint.xmgj.auditrsitembaseinfo.api.entity.AuditRsItemBaseinfo;
import com.epoint.xmgj.projectauthorization.api.IProjectAuthorizationService;
import com.epoint.xmgj.projectauthorization.api.entity.ProjectAuthorization;
import com.epoint.zwzt.export.ZwztExportDataHandler;
import com.epoint.zwzt.export.ZwztExportModel;
import com.tzwy.util.StringUtils;

/**
 * 项目表list页面对应的后台
 * 
 * @author pansh
 * @version [版本号, 2025-02-12 17:31:53]
 */
@RestController("auditrsitembaseinfogjlistaction")
@Scope("request")
public class AuditRsItemBaseinfoGjListAction extends BaseController
{
    @Autowired
    private IAuditRsItemBaseinfoService service;
    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 项目表实体对象
     */
    private AuditRsItemBaseinfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditRsItemBaseinfo> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String userguid;

    private String rowguid;
    @Autowired
    private IProjectAuthorizationService iProjectAuthorizationService;

    @Autowired
    private IRoleService iRoleService;

    private List<SelectItem> totalinvestModel = null;

    public boolean flag;
    public String userAreacode;
    public boolean isCity;

    public void pageLoad() {
        flag = iRoleService.isExistUserRoleName(userSession.getUserGuid(), "项目管家管理员");
        userAreacode = ZwfwUserSession.getInstance().getAreaCode();
        if ("370800".equals(userAreacode)) {
            isCity = true;
        }
        else {
            isCity = false;
        }
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<AuditRsItemBaseinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditRsItemBaseinfo>()
            {

                @Override
                public List<AuditRsItemBaseinfo> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();

                    PageData<AuditRsItemBaseinfo> pageData = new PageData<>();
                    if (flag) {
                        // conditionSql +=" and parentid is null";
                        if (StringUtils.isNotBlank(dataBean.getItemcode())) {
                            sqlConditionUtil.like("itemcode", dataBean.getItemcode());
                        }
                        if (StringUtils.isNotBlank(dataBean.getItemname())) {
                            sqlConditionUtil.like("itemname", dataBean.getItemname());
                        }
                        if (StringUtils.isNotBlank(dataBean.getProjectdistrict())) {
                            sqlConditionUtil.in("ProjectDistrict", dataBean.getProjectdistrict());
                        }
                        if (StringUtils.isNotBlank(dataBean.getKeyprojecttype())) {
                            sqlConditionUtil.eq("KeyProjectType", dataBean.getKeyprojecttype());
                        }
                        if (StringUtils.isNotBlank(dataBean.getProjectcategory())) {
                            sqlConditionUtil.eq("projectcategory", dataBean.getProjectcategory());
                        }
                        if (StringUtils.isNotBlank(dataBean.getSupporttype())) {
                            sqlConditionUtil.eq("supporttype", dataBean.getSupporttype());
                        }
                        if (StringUtils.isNotBlank(dataBean.getImageprogress())) {
                            sqlConditionUtil.eq("imageprogress", dataBean.getImageprogress());
                        }
                        if (StringUtils.isNotBlank(dataBean.getStr("totalinveststr"))) {
                            String startData = dataBean.getStr("totalinveststr").split("~")[0];
                            String endData = dataBean.getStr("totalinveststr").split("~")[1];
                            sqlConditionUtil.ge("totalinvest", startData);
                            sqlConditionUtil.le("totalinvest", endData);
                        }
                        if (StringUtils.isNotBlank(dataBean.getInvestmentmethod())) {
                            sqlConditionUtil.eq("investmentmethod", dataBean.getInvestmentmethod());
                        }
                        /*
                         * if (!isCity) {
                         * sqlConditionUtil.eq("ProjectDistrict", userAreacode);
                         * }
                         */
                        sqlConditionUtil.isBlank("parentid");
                        pageData = service.getListPage(sqlConditionUtil.getMap(), first, pageSize, sortField,
                                sortOrder);
                    }
                    else {
                        sqlConditionUtil.setInnerJoinTable("PROJECT_AUTHORIZATION as b", "a.rowguid", "b.ProjectID");
                        sqlConditionUtil.eq("b.UserID", userSession.getUserGuid());
                        if (StringUtils.isNotBlank(dataBean.getItemcode())) {
                            sqlConditionUtil.like("a.itemcode", dataBean.getItemcode());
                        }
                        if (StringUtils.isNotBlank(dataBean.getItemname())) {
                            sqlConditionUtil.like("a.itemname", dataBean.getItemname());
                        }
                        if (StringUtils.isNotBlank(dataBean.getProjectdistrict())) {
                            sqlConditionUtil.in("a.ProjectDistrict", dataBean.getProjectdistrict());
                        }
                        if (StringUtils.isNotBlank(dataBean.getKeyprojecttype())) {
                            sqlConditionUtil.eq("a.KeyProjectType", dataBean.getKeyprojecttype());
                        }
                        if (StringUtils.isNotBlank(dataBean.getProjectcategory())) {
                            sqlConditionUtil.eq("a.projectcategory", dataBean.getProjectcategory());
                        }
                        if (StringUtils.isNotBlank(dataBean.getSupporttype())) {
                            sqlConditionUtil.eq("a.supporttype", dataBean.getSupporttype());
                        }
                        if (StringUtils.isNotBlank(dataBean.getImageprogress())) {
                            sqlConditionUtil.eq("a.imageprogress", dataBean.getImageprogress());
                        }
                        if (StringUtils.isNotBlank(dataBean.getStr("totalinveststr"))) {
                            String startData = dataBean.getStr("totalinveststr").split("~")[0];
                            String endData = dataBean.getStr("totalinveststr").split("~")[1];
                            sqlConditionUtil.ge("a.totalinvest", startData);
                            sqlConditionUtil.le("a.totalinvest", endData);
                        }
                        /*
                         * if (!isCity) {
                         * sqlConditionUtil.eq("a.ProjectDistrict",
                         * userAreacode);
                         * }
                         */
                        sqlConditionUtil.isBlank("a.parentid");
                        sqlConditionUtil.setSelectFields("a.*");
                        pageData = service.getListPage(sqlConditionUtil.getMap(), first, pageSize, sortField,
                                sortOrder);
                    }
                    List<AuditRsItemBaseinfo> list = pageData.getList();
                    for (AuditRsItemBaseinfo data : list) {
                        List<String> userNameList = iProjectAuthorizationService
                                .getUserNamesByProjectId(data.getRowguid());
                        if (userNameList != null) {
                            data.put("authorizations", StringUtil.join(userNameList));
                        }
                        List<String> projectnameList = service.getProjectnamesByProjectid(data.getRowguid());
                        if (projectnameList != null) {
                            data.set("ybyw", StringUtil.join(projectnameList));
                        }
                    }
                    int count = pageData.getRowCount();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditRsItemBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            ZwztExportDataHandler<AuditRsItemBaseinfo> exportDataHandler = new ZwztExportDataHandler<AuditRsItemBaseinfo>()
            {
                @Override
                public List<AuditRsItemBaseinfo> getDataList(int first, int pageSize, String sortField,
                        String sortOrder) {

                    com.epoint.core.utils.sql.SqlConditionUtil sqlConditionUtil = new com.epoint.core.utils.sql.SqlConditionUtil();

                    if (flag) {
                        if (StringUtils.isNotBlank(dataBean.getItemcode())) {
                            sqlConditionUtil.like("itemcode", dataBean.getItemcode());
                        }
                        if (StringUtils.isNotBlank(dataBean.getItemname())) {
                            sqlConditionUtil.like("itemname", dataBean.getItemname());
                        }
                        if (StringUtils.isNotBlank(dataBean.getProjectdistrict())) {
                            sqlConditionUtil.in("ProjectDistrict", dataBean.getProjectdistrict());
                        }
                        if (StringUtils.isNotBlank(dataBean.getKeyprojecttype())) {
                            sqlConditionUtil.eq("KeyProjectType", dataBean.getKeyprojecttype());
                        }
                        if (StringUtils.isNotBlank(dataBean.getProjectcategory())) {
                            sqlConditionUtil.eq("projectcategory", dataBean.getProjectcategory());
                        }
                        if (StringUtils.isNotBlank(dataBean.getSupporttype())) {
                            sqlConditionUtil.eq("supporttype", dataBean.getSupporttype());
                        }
                        if (StringUtils.isNotBlank(dataBean.getImageprogress())) {
                            sqlConditionUtil.eq("imageprogress", dataBean.getImageprogress());
                        }
                        if (StringUtils.isNotBlank(dataBean.getStr("totalinveststr"))) {
                            String startData = dataBean.getStr("totalinveststr").split("~")[0];
                            String endData = dataBean.getStr("totalinveststr").split("~")[1];
                            sqlConditionUtil.ge("totalinvest", startData);
                            sqlConditionUtil.le("totalinvest", endData);
                        }
                        /*
                         * if (!isCity) {
                         * sqlConditionUtil.eq("ProjectDistrict", userAreacode);
                         * }
                         */
                        sqlConditionUtil.isBlank("parentid");
                    }
                    else {
                        sqlConditionUtil.setInnerJoinTable("PROJECT_AUTHORIZATION as b", "a.rowguid", "b.ProjectID");
                        sqlConditionUtil.eq("b.UserID", userSession.getUserGuid());
                        if (StringUtils.isNotBlank(dataBean.getItemcode())) {
                            sqlConditionUtil.like("a.itemcode", dataBean.getItemcode());
                        }
                        if (StringUtils.isNotBlank(dataBean.getItemname())) {
                            sqlConditionUtil.like("a.itemname", dataBean.getItemname());
                        }
                        if (StringUtils.isNotBlank(dataBean.getProjectdistrict())) {
                            sqlConditionUtil.in("a.ProjectDistrict", dataBean.getProjectdistrict());
                        }
                        if (StringUtils.isNotBlank(dataBean.getKeyprojecttype())) {
                            sqlConditionUtil.eq("a.KeyProjectType", dataBean.getKeyprojecttype());
                        }
                        if (StringUtils.isNotBlank(dataBean.getProjectcategory())) {
                            sqlConditionUtil.eq("a.projectcategory", dataBean.getProjectcategory());
                        }
                        if (StringUtils.isNotBlank(dataBean.getSupporttype())) {
                            sqlConditionUtil.eq("a.supporttype", dataBean.getSupporttype());
                        }
                        if (StringUtils.isNotBlank(dataBean.getImageprogress())) {
                            sqlConditionUtil.eq("a.imageprogress", dataBean.getImageprogress());
                        }
                        if (StringUtils.isNotBlank(dataBean.getStr("totalinveststr"))) {
                            String startData = dataBean.getStr("totalinveststr").split("~")[0];
                            String endData = dataBean.getStr("totalinveststr").split("~")[1];
                            sqlConditionUtil.ge("a.totalinvest", startData);
                            sqlConditionUtil.le("a.totalinvest", endData);
                        }
                        /*
                         * if (!isCity) {
                         * sqlConditionUtil.eq("a.ProjectDistrict",
                         * userAreacode);
                         * }
                         */
                        sqlConditionUtil.isBlank("a.parentid");
                        sqlConditionUtil.setSelectFields("a.*");
                    }

                    List<AuditRsItemBaseinfo> list = service.findList(sqlConditionUtil.getMap(), first, pageSize,
                            sortField, sortOrder);

                    for (AuditRsItemBaseinfo data : list) {
                        List<String> userNameList = iProjectAuthorizationService
                                .getUserNamesByProjectId(data.getRowguid());
                        if (userNameList != null) {
                            data.put("authorizations", StringUtil.join(userNameList));
                        }
                        List<String> projectnameList = service.getProjectnamesByProjectid(data.getBiguid());
                        if (projectnameList != null) {
                            data.set("ybyw", StringUtil.join(projectnameList));
                        }

                        if (StringUtil.isNotBlank(data.getProjectdistrict())) {
                            data.setProjectdistrict(
                                    codeItemsService.getItemTextByCodeName("辖区对应关系", data.getProjectdistrict()));
                        }
                        if (StringUtil.isNotBlank(data.getKeyprojecttype())) {
                            data.setKeyprojecttype(
                                    codeItemsService.getItemTextByCodeName("重点项目类型", data.getKeyprojecttype()));
                        }
                        if (StringUtil.isNotBlank(data.getInvestmentmethod())) {
                            data.setInvestmentmethod(
                                    codeItemsService.getItemTextByCodeName("投资方式", data.getInvestmentmethod()));
                        }
                        if (StringUtil.isNotBlank(data.getProjectnature())) {
                            data.setProjectnature(
                                    codeItemsService.getItemTextByCodeName("项目性质", data.getProjectnature()));
                        }
                        if (StringUtil.isNotBlank(data.getProjectcategory())) {
                            data.setProjectcategory(
                                    codeItemsService.getItemTextByCodeName("项目管家项目类型", data.getProjectcategory()));
                        }
                        if (StringUtil.isNotBlank(data.getImageprogress())) {
                            data.setImageprogress(
                                    codeItemsService.getItemTextByCodeName("形象进度", data.getImageprogress()));
                        }
                        if (StringUtil.isNotBlank(data.getProgressdetails())) {
                            data.setProgressdetails(
                                    codeItemsService.getItemTextByCodeName("进度详情", data.getProgressdetails()));
                        }
                        if (StringUtil.isNotBlank(data.getChangestatus())) {
                            data.setChangestatus(
                                    codeItemsService.getItemTextByCodeName("变更状态", data.getChangestatus()));
                        }
                        if (StringUtil.isNotBlank(data.getSupporttype())) {
                            data.setSupporttype(codeItemsService.getItemTextByCodeName("支持类型", data.getSupporttype()));
                        }
                        if (StringUtil.isNotBlank(data.getIndustrialchain())) {
                            data.setIndustrialchain(
                                    codeItemsService.getItemTextByCodeName("所属产业链", data.getIndustrialchain()));
                        }
                        if (StringUtil.isNotBlank(data.getInvestment_percentage())) {
                            data.setInvestment_percentage(codeItemsService.getItemTextByCodeName("占总投资金额比例",
                                    data.getInvestment_percentage()));
                        }
                    }
                    return list;
                }
            };
            exportModel = new ZwztExportModel(
                    "itemcode,itemname,lurutime,itemlegaldept,itemstartdate,itemfinishdate,totalinvest,"
                            + "projectdistrict,keyprojecttype,otherkeyprojecttype,investmentmethod,"
                            + "projectnature,projectcategory,imageprogress,changestatus,progressdetails,"
                            + "supporttype,currentyearinvest,investment_percentage,fundsreceived,industrialchain,"
                            + "industrialpark,industrialparkremark,remark,authorizations,ybyw",
                    "项目代码,项目名称,录入时间,建设单位,建设开始时间,建设结束时间,总投资（万元）," + "项目所属辖区,重点项目类型,其他项目类型补充,投资方式,项目性质,项目类型,形象进度,业务变更情况,"
                            + "进度详情,支持类型,本年度投资金额(万元),占总投资金额比例,资金到位情况说明,所属产业链,所属园区,园区备注,备注,授权维护人员,已办业务",
                    false, exportDataHandler, "项目管家信息列表", getRequestContext());
        }
        return exportModel;
    }

    public void add() {
        if (StringUtils.isNotBlank(rowguid) && StringUtils.isNotBlank(userguid)) {
            // 先删除，再保存
            iProjectAuthorizationService.deleteByProjectId(rowguid);
            String[] users = userguid.split(";");
            if (users.length > 0) {
                for (String user : users) {
                    ProjectAuthorization projectAuthorization = new ProjectAuthorization();
                    projectAuthorization.setAuthorizationdate(new Date());
                    projectAuthorization.setProjectid(rowguid);
                    projectAuthorization.setUserid(user);
                    projectAuthorization.setRowguid(UUID.randomUUID().toString());
                    projectAuthorization.setAuthorizedby(userSession.getDisplayName());
                    iProjectAuthorizationService.insert(projectAuthorization);
                }
            }

        }

    }

    public List<SelectItem> getTotalinvestModel() {
        if (totalinvestModel == null) {
            // 总投资选择了千万以下，则筛选出0~999万的项目，选择亿万以下则筛选出0~9999万之内的项目
            totalinvestModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目管家总投资", null, false));
        }
        return this.totalinvestModel;
    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid;
    }

    public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }
}
