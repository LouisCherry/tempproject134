package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.action;

import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.IAuditYjsCjrService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjr;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjrInfo;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjrInfoDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目基本信息表新增页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("auditspdisabledintegrateddetailaction")
@Scope("request")
public class AuditSpDisabledIntegratedDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 2423143074436388949L;
    /**
     * 项目基本信息表实体对象
     */
    private AuditYjsCjr dataBean = null;
    /**
     * 项目申报日期
     */
    private Date createdate;
    /**
     * 事项与材料情况
     */
    private String materialdesc = "";
    /**
     * 材料提交情况
     */
    private String materialsubmitdesc = "";
    /**
     * biguid
     */
    private String biGuid = "";
    /**
     * 主题实例
     */
    private AuditSpInstance spInstance;

    private List<AuditSpITask> spITasks = null;

    private DataGridModel<AuditSpITask> modelTask = null;

    @Autowired
    private IAuditYjsCjrService iAuditYjsCjrService;

    @Autowired
    private IAuditSpInstance auditSpInstanceService;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditProject iauditproject;

    @Autowired
    private IAuditProjectSparetime iauditprojectsparetime;
    /**
     * 残疾人结果信息
     */
    private AuditYjsCjrInfo cjrInfo;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditYjsCjrInfoDetail> model;


    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("guid");
        spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        if (spInstance != null) {
            dataBean = iAuditYjsCjrService.find(spInstance.getYewuguid());
            createdate = spInstance.getCreatedate();
        }
        spITasks = auditSpITaskService.getSpITaskByBIGuid(biGuid).getResult();
        String taskCount = "0";
        String materialCount = "0";
        // 电子材料
        int dzMaterial = 0;
        // 纸质材料
        int zzMaterial = 0;
        // 容缺材料
        int rqMaterial = 0;
        // 补正材料
        int bzMaterial = 0;
        if (spITasks != null) {
            taskCount = String.valueOf(spITasks.size());
        }
        List<AuditSpIMaterial> auditSpIMaterials = auditSpIMaterialService.getSpIMaterialByBIGuid(biGuid).getResult();
        auditSpIMaterials.removeIf(auditSpIMaterial -> {
            return ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getResult());
        });
        if (auditSpIMaterials != null) {
            materialCount = String.valueOf(auditSpIMaterials.size());
            for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                if ("15".equals(auditSpIMaterial.getStatus())) {
                    zzMaterial++;
                }
                else if ("20".equals(auditSpIMaterial.getStatus())) {
                    dzMaterial++;
                }
                else if ("25".equals(auditSpIMaterial.getStatus())) {
                    zzMaterial++;
                    dzMaterial++;
                }
                if ("1".equals(auditSpIMaterial.getAllowrongque())) {
                    rqMaterial++;
                }
                if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
                    bzMaterial++;
                }
            }
        }
        materialdesc = "共" + taskCount + "个事项，" + materialCount + "份材料";
        materialsubmitdesc = "提交电子材料" + dzMaterial + "份 提交纸质材料" + zzMaterial + "份 容缺（暂无）" + rqMaterial
                + "份 需整改" + bzMaterial + "份";

        // 展示结果信息
        cjrInfo = new AuditYjsCjrInfo();
        SqlConditionUtil sqlCondition = new SqlConditionUtil();
        sqlCondition.eq("cjrguid", dataBean.getRowguid());
        List<AuditYjsCjrInfo> list = iAuditYjsCjrService.findList(sqlCondition.getMap(), AuditYjsCjrInfo.class);
        if (list != null && !list.isEmpty()) {
            cjrInfo = list.get(0);
            addCallbackParam("hasresult", true);
        }
    }

    public DataGridModel<AuditYjsCjrInfoDetail> getCjrDetailGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditYjsCjrInfoDetail>() {

                @Override
                public List<AuditYjsCjrInfoDetail> fetchData(int first, int pageSize, String sortField,
                                                             String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (cjrInfo != null && StringUtil.isNotBlank(cjrInfo.getRowguid())) {
                        conditionSql += " and infoguid = ?";
                        conditionList.add(cjrInfo.getRowguid());
                    }
                    else {
                        return new ArrayList<>();
                    }
                    List<AuditYjsCjrInfoDetail> list = iAuditYjsCjrService.findList(AuditYjsCjrInfoDetail.class,
                            ListGenerator.generateSql("audit_yjs_cjrinfo_detail", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = iAuditYjsCjrService.count(
                            ListGenerator.generateSql("audit_yjs_cjrinfo_detail", conditionSql),
                            conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 648294995318652346L;

                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = new ArrayList<>();
                    if (spITasks != null && !spITasks.isEmpty()) {
                        for (AuditSpITask auditSpITask : spITasks) {
                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                                String fields = " rowguid,taskguid,projectname,pviguid,status,tasktype,is_pause,acceptuserdate ";
                                Record auditProject = iauditproject
                                        .getAuditProjectByRowGuid(fields, auditSpITask.getProjectguid(), auditSpITask.getAreacode())
                                        .getResult();
                                if (auditProject != null) {
                                    int status = auditProject.get("status");
                                    auditSpITask.put("status", codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(status)));
                                    auditSpITask.put("accptdate", auditProject.get("acceptuserdate"));
                                    String sparetime = "--";
                                    if (status >= ZwfwConstant.BANJIAN_STATUS_YSL && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ
                                            && !ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.get("tasktype").toString())) {
                                        if (ZwfwConstant.CONSTANT_INT_ONE == ZwfwUtil.isNull(auditProject.getInt("is_pause"))) {
                                            sparetime = "暂停计时";// 办件处于暂停计时状态
                                        }
                                        else {
                                            Record auditprojectsparetime = iauditprojectsparetime.getSparetimeByProjectGuid(auditSpITask.getProjectguid())
                                                    .getResult();
                                            if (auditprojectsparetime != null) {
                                                sparetime = CommonUtil.getSpareTimes(auditprojectsparetime.get("spareminutes"));
                                                if (ZwfwUtil.isNull(auditprojectsparetime.getInt("spareminutes")) < 0) {
                                                    sparetime = "超时" + sparetime;
                                                }
                                            }
                                        }
                                    }
                                    auditSpITask.put("sparetime", sparetime);
                                    auditSpITask.put("businessname", spInstance.getItemname());
                                }
                                auditSpITasks.add(auditSpITask);
                            }
                        }
                    }
                    auditSpITasks.sort((a, b) -> {

                        if (b.getDate("accptdate") == null) {
                            return -1;
                        }
                        else if (a.getDate("accptdate") == null) {
                            return 1;
                        }
                        if (a.getDate("accptdate").getTime() - b.getDate("accptdate").getTime() > 0) {
                            return 1;
                        }
                        else {
                            return -1;
                        }
                    });
                    this.setRowCount(auditSpITasks.size());
                    return auditSpITasks;
                }
            };
        }
        return modelTask;
    }

    public AuditYjsCjr getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditYjsCjr();
        }
        return dataBean;
    }

    public void setDataBean(AuditYjsCjr dataBean) {
        this.dataBean = dataBean;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getMaterialdesc() {
        return materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        this.materialdesc = materialdesc;
    }

    public String getMaterialsubmitdesc() {
        return materialsubmitdesc;
    }

    public void setMaterialsubmitdesc(String materialsubmitdesc) {
        this.materialsubmitdesc = materialsubmitdesc;
    }

    public AuditYjsCjrInfo getCjrInfo() {
        return cjrInfo;
    }

    public void setCjrInfo(AuditYjsCjrInfo cjrInfo) {
        this.cjrInfo = cjrInfo;
    }
    
    public void saveForm() {
        String msg = "保存成功";
        iAuditYjsCjrService.update(dataBean);
        addCallbackParam("msg", msg);
    }
}
