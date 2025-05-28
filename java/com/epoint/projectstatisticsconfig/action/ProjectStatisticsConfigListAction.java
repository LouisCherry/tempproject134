package com.epoint.projectstatisticsconfig.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.projectstatisticsconfig.api.IProjectStatisticsConfigService;


/**
 * 办件统计配置表list页面对应的后台
 *
 * @author 15056
 * @version [版本号, 2022-05-23 17:43:09]
 */
@RestController("projectstatisticsconfiglistaction")
@Scope("request")
public class ProjectStatisticsConfigListAction extends BaseController {
    @Autowired
    private IProjectStatisticsConfigService iProjectStatisticsConfig;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IUserRoleRelationService iUserRoleRelation;

    /**
     * 办件统计配置表实体对象
     */
    private Record dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    //办件状态
    private List<SelectItem> statusModel;

    //办件来源
    private List<SelectItem> projectSourceModel;

    //所属科室
    private List<SelectItem> keshinumModel;


    public void pageLoad() {
        dataBean=new Record();
        //控制【配置】按钮显隐
        String isAdmin = "false";
        //获取管理员角色的roleguid
        FrameRole frameRole = iRoleService.getRoleByRoleField("ROLENAME", "adminGroup");

        //当前用户userguid
        String userGuid = userSession.getUserGuid();

        //遍历所有管理员角色，与当前用户比对
        List<FrameUserRoleRelation> relationList = iUserRoleRelation.getRelationListByField("ROLEGUID", frameRole.getRoleGuid(), "", "");
        if (relationList != null && !relationList.isEmpty()) {
            for (FrameUserRoleRelation relation : relationList) {
                if (userGuid.equals(relation.getUserGuid())) {
                    isAdmin = "true";
                    break;
                }
            }
        }
        addCallbackParam("isAdmin", isAdmin);

        //判断ouguid是否有赋值，设置当前部门为ouguid  并且是第一次进入页面
        /*if (StringUtil.isBlank(dataBean.get("ouguid")) && !isPostback()){
            dataBean.set("ouguid",userSession.getOuGuid());
        }*/

        //初始化申请时间和办件来源
        dataBean.set("applydateFrom",new Date());
        dataBean.set("applydateTo",new Date());
        dataBean.set("projectsource","3");
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            iProjectStatisticsConfig.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    PageData<Record> pageData = iProjectStatisticsConfig.pageData(first, pageSize, dataBean);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }


    public Record getDataBean() {
        if (dataBean == null) {
            dataBean = new Record();
        }
        return dataBean;
    }

    public void setDataBean(Record dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("keshinum,taskname,projectnum,projectsource", "所属科室,所属事项,办件量,办件来源");
        }
        return exportModel;
    }

    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, false));
        }
        return statusModel;
    }

    public List<SelectItem> getProjectSourceModel() {
        if (projectSourceModel == null) {
            projectSourceModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件统计来源", null, false));
        }
        return projectSourceModel;
    }

    public List<SelectItem> getKeshinumModel() {
        if (keshinumModel == null) {
            keshinumModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "科室名称", null, false));
        }
        return keshinumModel;
    }

}
