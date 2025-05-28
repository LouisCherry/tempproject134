package com.epoint.statistics.action;

import java.util.List;
import java.util.stream.Collectors;

import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.statistics.api.IOverdueAuditService;

/**
 * @author yuchenglin
 * @version 1.0.0
 * @ClassName AuditProjectZsAction.java
 * @Description 获取办件展示信息列表
 * @createTime 2022年01月07日 09:59:00
 */
@RestController("auditprojectdisplayaction")
@Scope("request")
public class AuditProjectDisplayAction extends BaseController {

    /**
     * 前台组装的参数
     */
    private String ouguid;

    /**
     * 实体类
     */
    private AuditProject dataBean;

    /**
     * 自定义的操作办件的api
     */
    @Autowired
    private IOverdueAuditService iOverdueAuditService;

    /**
     * 操作部门的api
     */
    @Autowired
    private IOuServiceInternal ouService;

    /**
     * 操作代码项的api
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 部门名称
     */
    private String ouName;
    /**
     * 部门简称
     */
    private String ouShortName;

    /**
     * 左侧选择节点
     */
    private String leftTreeNodeGuid;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    // -----------------------------以下是getset方法-------------------------------------------
    public String getOuName() {
        return ouName;
    }

    public void setOuName(String ouName) {
        this.ouName = ouName;
    }

    public String getOuShortName() {
        return ouShortName;
    }

    public void setOuShortName(String ouShortName) {
        this.ouShortName = ouShortName;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public DataGridModel<AuditProject> getModel() {
        return model;
    }

    public void setModel(DataGridModel<AuditProject> model) {
        this.model = model;
    }

    public String getOuguid() {
        return ouguid;
    }

    public void setOuguid(String ouguid) {
        this.ouguid = ouguid;
    }

    public AuditProject getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }

    // ----------------------------以上是getset方法---------------------------------

    /**
     * @Author: yuchenglin
     * @Description: pageload.
     * @Date: 2022/1/7 12:42
     * @return: void
     **/
    @Override
    public void pageLoad() {
        ouguid = getRequestParameter("ouguid");
        if (dataBean == null) {
            dataBean = new AuditProject();
        }
        String[] bh = ouguid.split("@@");
        if (StringUtil.isNotBlank(bh[5])) {
            dataBean.setStatus(Integer.valueOf(bh[5]));
        }
        if (StringUtil.isNotBlank(bh[4])) {
            dataBean.setApplyway(Integer.valueOf(bh[4]));
        }
        if (StringUtil.isNotBlank(bh[2])) {
            String startData = EpointDateUtil.convertDate2String(
                    EpointDateUtil.convertString2DateAuto(bh[2]));
            dataBean.set("startData", startData);
        }
        if (StringUtil.isNotBlank(bh[3])) {
            String endData = EpointDateUtil.convertDate2String(
                    EpointDateUtil.convertString2DateAuto(bh[3]));
            dataBean.set("endData", endData);
        }
    }

    /**
     * @Author: yuchenglin
     * @Description:获得表格对象
     * @Date: 2022/1/7 12:42
     * @return: com.epoint.core.dto.model.DataGridModel<com.epoint.basic.auditproject.auditproject.domain.AuditProject>
     **/
    public DataGridModel<AuditProject> getDataGridData() {
        if (model == null) {
            model = new DataGridModel<AuditProject>() {
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String[] bh = ouguid.split("@@");
                    String ouguidParams = bh[0];
                    String type = bh[1];
                    List<FrameOu> frameOus = ouService.listOUByGuid(ouguidParams, 4);
                    List<String> ouguidList = frameOus.stream().map(FrameOu::getOuguid).collect(Collectors.toList());
//                    int count = iOverdueAuditService.getCountByTJ(ouguidList, type, dataBean.getFlowsn());
                    int count = iOverdueAuditService.getCountByTJ(ouguidList, type, dataBean);
                    model.setRowCount(count);
//                    List<AuditProject> auditProjectByTJ = iOverdueAuditService.getAuditProjectByTJ(first, pageSize,
//                            ouguidList, type, dataBean.getFlowsn());
                    List<AuditProject> auditProjectByTJ = iOverdueAuditService.getAuditProjectByTJ(first, pageSize,
                            ouguidList, type, dataBean);
                    for (AuditProject auditProject : auditProjectByTJ) {
                        auditProject.set("zt", iCodeItemsService.getItemTextByCodeName("办件状态",
                                String.valueOf(auditProject.getStatus())));
                        auditProject.set("ly", iCodeItemsService.getItemTextByCodeName("申请方式",
                                String.valueOf(auditProject.getApplyway())));
                    }
                    return auditProjectByTJ;
                }
            };
        }
        return model;
    }

    /**
     * @Author: yuchenglin
     * @Description:导出
     * @Date: 2022/1/7 12:42
     * @return: com.epoint.basic.faces.export.ExportModel
     **/
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("ouname,windowname,projectname,flowsn,applydate,banjiedate,zt,ly",
                    "部门名称,窗口名称,事项名称,办件编号,申请时间,办结时间,办结状态,办件来源");
        }
        return exportModel;
    }

}
