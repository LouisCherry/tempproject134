package com.epoint.auditorga.auditoumanager.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.EpointTreeHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.soa.SOAService;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.jnzwfw.jntask.api.IJnTaskService;

/**
 * 部门列表
 * 
 * @author fzj
 * @version [版本号, 2016年3月7日]
 */
@RestController("jncenterzwfwframeoulistaciton")
@Scope("request")
public class JNCZwfwFrameOuListAciton extends BaseController
{

    private static final long serialVersionUID = 1L;

    private LazyTreeModal9 treeModel;

    /**
     * 表格控件model
     */
    private DataGridModel<FrameOu> model;
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
     * 是否直属
     */
    private Boolean isDirect;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 是否以独立部门模式进入
     */
    private String isSub;

    @Autowired
    private IOuServiceInternal ouService;
    
    @Autowired
    private IJnTaskService iJnTaskService;

    @Override
    public void pageLoad() {
        if (SOAService.isEnableSOA()) {
            addCallbackParam("soa", SOAService.message);
        }
        isSub = getRequestParameter("isSub");
    }

    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            int tableName = ConstValue9.FRAMEOU;
            if ("1".equals(isSub)) {
                tableName = ConstValue9.SUB_DEPARTMENT;
            }
            treeModel = new LazyTreeModal9(new EpointTreeHandler9(tableName));
            treeModel.setRootName("所有部门");
        }
        return treeModel;
    }

    /**
     * 获取系统参数表格数据
     * 
     * @return DataGridModel
     */
    public DataGridModel<FrameOu> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameOu>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = -786592857879027080L;

                @Override
                public List<FrameOu> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
                    String sql = "select distinct b.ouname,b.ouguid,b.oushortname,b.BaseOuguid,b.IsSubWebFlow from audit_orga_window a join frame_ou b on a.ouguid = b.ouguid where a.centerguid = ?";
                    List<FrameOu> oulist = iJnTaskService.findList(sql, first, pageSize, centerguid);
                    String sql1 = "select count(1) from (select distinct b.ouname,b.ouguid,b.oushortname,b.BaseOuguid,b.IsSubWebFlow from audit_orga_window a join frame_ou b on a.ouguid = b.ouguid where a.centerguid = ?) a";
                    int count = iJnTaskService.findListCount(sql1,centerguid);
                    this.setRowCount(count);
                    // 对特殊列进行处理
                    if (oulist != null) {
                        for (FrameOu ou : oulist) {
                            boolean isBaseou = StringUtil.isNotBlank(ou.getBaseOuguid())
                                    && ou.getBaseOuguid().equals(ou.getOuguid());
                            boolean isSubWebFlow = ou.getIsSubWebFlow() != null && ou.getIsSubWebFlow() == 1;
                            // 是否是部门窗口
                            FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(ou.getOuguid());
                            boolean isWindowOu = false;
                            if (ouExtendInfo != null && StringUtil.isNotBlank(ouExtendInfo.get("is_windowou"))
                                    && "1".equals(ouExtendInfo.get("is_windowou").toString())) {
                                isWindowOu = true;
                            }
                            ou.put("isbaseou", isBaseou ? "是" : "否");
                            ou.put("issubwebflowtext", isSubWebFlow ? "是" : "否");
                            ou.put("iswindowou", isWindowOu ? "是" : "否");
                        }
                    }
                    return oulist;
                }

            };
        }
        return model;
    }

    /**
     * 删除部门
     * 
     * @param ouGuid
     */
    public void deleteOu(String ouGuid) {
        String message = ouService.deleteFrameOuByOuguid(ouGuid);
        addCallbackParam("msg", message);
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            // ,独立单位,子流转
            exportModel = new ExportModel("ouname,oushortName,orderNumber,isBaseOu,isSubWebFlowText",
                    "部门名称,简称,排序号,独立单位,子流转");
        }
        return exportModel;
    }

    /**
     * 保存修改
     */
    public void saveAll() {
        List<FrameOu> ouList = getDataGridData().getWrappedData();
        String msg = "保存成功！";
        for (FrameOu ou : ouList) {
            if (StringUtil.isBlank(ou.getOuname())) {
                msg = "部门名称不允许为空，为空的记录将不会进行保存！";
                break;
            }
            ou.remove("isBaseOu");// 删除无用字段
            ou.remove("isSubWebFlowText");
            if (StringUtil.isBlank(ou.getOushortName())) {// 如果简称为空 自动赋值为全称
                ou.setOushortName(ou.getOuname());
            }
            if (ou.getOrderNumber() == null) {// null会跑到最前
                ou.setOrderNumber(0);
            }else{
                ou.setOrderNumber(ou.getOrderNumber());
            }
            ouService.updateFrameOu(ou, null);
        }
        addCallbackParam("msg", msg);
    }

    /******************************
     * 以下为get set方法
     ***********************************************************/
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

    public Boolean getIsDirect() {
        if (isDirect == null) {
            isDirect = false;
        }
        return isDirect;
    }

    public void setIsDirect(Boolean isDirect) {
        this.isDirect = isDirect;
    }

    public String getIsSub() {
        return isSub;
    }

    public void setIsSub(String isSub) {
        this.isSub = isSub;
    }

}
