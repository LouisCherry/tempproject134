package com.epoint.jn.inproject.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jn.externalprojectinfo.api.IExternalProjectInfoService;
import com.epoint.jn.externalprojectinfo.api.entity.ExternalProjectInfo;
import com.epoint.jn.externalprojectinfoext.api.IExternalProjectInfoExtService;
import com.epoint.jn.externalprojectinfoext.api.entity.ExternalProjectInfoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController("inprojectbyoulistaction")
@Scope("request")
public class InProjectByOUListAction extends BaseController {
    private static final long serialVersionUID = 1L;
    private DataGridModel<ExternalProjectInfo> modelall;

    private ExportModel exportModel;
    private String startdate;
    private String enddate;

    private String ouguid;
    /**
     * 左边树列表
     */
    private String leftTreeNodeGuid;
    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    @Autowired
    private IExternalProjectInfoExtService externalProjectInfoExtService;

    @Autowired
    private IExternalProjectInfoService externalProjectInfoService;
    private CommonService commservice = new CommonService();

    @Override
    public void pageLoad() {
        this.startdate = this.getRequestParameter("startdate");
        if ("kong".equals(startdate)) {
            startdate = "";
        }
        this.enddate = this.getRequestParameter("enddate");
        if ("kong".equals(enddate)) {
            enddate = "";
        }
        if (StringUtil.isBlank(startdate)) {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            startdate = EpointDateUtil.convertDate2String(cale.getTime(), "YYYY-MM-dd");
        }
        if (StringUtil.isBlank(enddate)) {
            enddate = EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-dd");
        }
    }

    public DataGridModel<ExternalProjectInfo> getDataGridData() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<ExternalProjectInfo>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<ExternalProjectInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    Integer rowCount = 0;
                    // 1.根据申请时间查询扩展表的办件信息表的id列表
                    StringBuilder extCondition = new StringBuilder("select project_guid from external_project_info_ext where 1=1 ");
                    extCondition.append(" and applydate BETWEEN '")
                            .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate)))
                            .append(" 00:00:00' and '")
                            .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate)))
                            .append(" 23:59:59' ");
                    List<ExternalProjectInfoExt> extList = externalProjectInfoExtService.findList(extCondition.toString());
                    List<String> projectGuids = extList.stream().map(item -> item.getProject_guid()).collect(Collectors.toList());
                    // 2.根据第一步得到的ids 和辖区code获取分页数据

                    List<ExternalProjectInfo> listnum = externalProjectInfoService.findList(first,pageSize,leftTreeNodeGuid,projectGuids);

                    rowCount = externalProjectInfoService.findCount(leftTreeNodeGuid,projectGuids);
                    this.setRowCount(rowCount != null ? rowCount:0);
                    return listnum;
                }
            };
        }
        return modelall;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("ouname,allnum",
                    "部门,申请数");
        }
        return exportModel;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getOuguid() {
        return ouguid;
    }

    public void setOuguid(String ouguid) {
        this.ouguid = ouguid;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }


}
