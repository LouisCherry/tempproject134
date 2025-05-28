package com.epoint.jnauditprojectdata.action;

import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstance.entity.EvainstanceCk;
import com.epoint.evainstanceck.api.IEvainstanceCkService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController("jnauditprojectdatabyouaction")
@Scope("request")
public class JNAuditProjectDataByOUAction extends BaseController {
    private static final long serialVersionUID = 1L;
    @Autowired
    IWFInstanceAPI9 iwf9;

    @Autowired
    IAuditProjectNumber auditProjectNumber;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IJNAuditProject ijnAuditProject;
    @Autowired
    private IAuditTask iAuditTask;
    //评价api
    @Autowired
    private IEvainstanceCkService iEvainstanceService;

    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;
    /**
     * 是否上传材料
     */
    private int ismaterial;
    /**
     * 申请时间
     */
    private String applyDateStart;
    private String applyDateEnd;
    /**
     * 办结时间
     */
    private String finishDateStart;
    private String finishDateEnd;
    /**
     * 办件状态
     */
    private String status;
    /**
     * 办件来源
     */
    private String applyway;
    /**
     * 传参
     */
    private String ouguid;

    private ExportModel exportModel;

    /**
     * 日志
     */
    transient Logger log = LogUtil.getLog(JNAuditProjectDataByOUAction.class);

    @Override
    public void pageLoad() {
        ouguid = this.getRequestParameter("ouguid");
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>() {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditProject> list = new ArrayList();
                    if (StringUtil.isNotBlank(ouguid) && !"undefined".equals(ouguid)) {
                        // 申请状态
                        status = getRequestParameter("status");
                        if (StringUtil.isNotBlank(status)) {
                            dataBean.setStatus(Integer.valueOf(status));
                        }
                        // 办件来源
                        applyway = getRequestParameter("applyway");
                        if (StringUtil.isNotBlank(applyway)) {
                            dataBean.setApplyway(Integer.valueOf(applyway));
                        }
                        // 申请时间 办结时间
                        applyDateStart = getRequestParameter("applyDateStart");
                        applyDateEnd = getRequestParameter("applydateEnd");
                        finishDateStart = getRequestParameter("finishdateStart");
                        finishDateEnd = getRequestParameter("finishdateEnd");
                        // 左侧区域条件
                        String leftTreeNodeGuid = getRequestParameter("leftTreeNodeGuid");
                        String areacode = ZwfwUserSession.getInstance().getAreaCode();
                        List<FrameOu> searchOulist = new ArrayList<>();
                        if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                            // 查询区域编码
                            FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(leftTreeNodeGuid);
                            areacode = frameOuExtendInfo.getStr("AREACODE");
                            // 获取当前部门信息
                            FrameOu ouByOuGuid = ouService.getOuByOuGuid(leftTreeNodeGuid);
                            if (areacode.length() == 6 && StringUtil.isNotBlank(ouByOuGuid.getParentOuguid())) {
                                searchOulist.add(ouByOuGuid);
                            }
                            else {
                                searchOulist = ouService.listOUByGuid(leftTreeNodeGuid, 1);
                            }
                        }
                        List<String> ouguids = new ArrayList<>();
                        if (!"ALL".equals(ouguid)) {
                            ouguids.add(ouguid);
                        }
                        else {
                            if (CollectionUtils.isNotEmpty(searchOulist)) {
                                ouguids = searchOulist.stream().map(FrameOu::getOuguid).collect(Collectors.toList());
                            }
                        }
                        List<AuditProject> resultList = ijnAuditProject.getAuditProjectDataPageData(dataBean
                                , applyDateStart, applyDateEnd, finishDateStart, finishDateEnd, areacode, ouguids
                                , first, pageSize, sortField, sortOrder);
                        resultList.forEach(x->{
                            AuditTask task = iAuditTask.getAuditTaskByGuid(x.getTaskguid(), false).getResult();
                            x.put("item_id",task.getItem_id());
                            EvainstanceCk record = (EvainstanceCk) iEvainstanceService.findByflowsn(x.getFlowsn());
                            if (ValidateUtil.isNotNull(record)){
                                x.put("satisfactiondate",record.getCreatedate());
                                x.put("satisfaction",record.getSatisfaction());
                            }
                            if (ValidateUtil.isNotNull(x.getReceivedate()) && ValidateUtil.isNotNull(x.getBanjiedate())){
                                x.put("processtime",EpointDateUtil.dateDiffCompare(x.getBanjiedate(),x.getReceivedate(),"min"));
                            }

                        });
                        int rowCount = ijnAuditProject.getAuditProjectDataCount(dataBean, applyDateStart, applyDateEnd
                                , finishDateStart, finishDateEnd, areacode, ouguids);
                        this.setRowCount(rowCount);
                        return resultList;
                    }
                    else {
                        this.setRowCount(0);
                        return list;
                    }
                }
            };
        }
        return model;
    }


    public ExportModel getExportModel2() {
        if (exportModel == null) {
            exportModel = new ExportModel("ouname,windowname,projectname,item_id,flowsn,applydate,receivedate," +
                    "receiveusername,acceptuserdate,acceptusername,banjieusername,banjiedate,processtime,status,applyway,satisfactiondate,satisfaction",
                    "部门名称,窗口名称,事项名称,事项编码,办件编号,申请时间,接件时间," +
                            "接件人员,受理时间,受理人员,审核人员,办结时间,办件时长,办件状态,办件来源,评价时间,评价结果");
        }
        return exportModel;
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


    public int getIsmaterial() {
        return ismaterial;
    }

    public void setIsmaterial(int ismaterial) {
        this.ismaterial = ismaterial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplyDateStart() {
        return applyDateStart;
    }

    public void setApplyDateStart(String applyDateStart) {
        this.applyDateStart = applyDateStart;
    }

    public String getApplyDateEnd() {
        return applyDateEnd;
    }

    public void setApplyDateEnd(String applyDateEnd) {
        this.applyDateEnd = applyDateEnd;
    }

    public String getFinishDateStart() {
        return finishDateStart;
    }

    public void setFinishDateStart(String finishDateStart) {
        this.finishDateStart = finishDateStart;
    }

    public String getFinishDateEnd() {
        return finishDateEnd;
    }

    public void setFinishDateEnd(String finishDateEnd) {
        this.finishDateEnd = finishDateEnd;
    }
}
