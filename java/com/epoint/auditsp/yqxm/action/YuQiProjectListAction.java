package com.epoint.auditsp.yqxm.action;

import com.epoint.auditsp.yqxm.api.ICrawlDataDY;
import com.epoint.auditsp.yqxm.api.entity.StSpglXmjbxxb;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.jiningzwfw.importstatistics.api.IImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 逾期项目后台
 *
 * @author yangyi
 * @version [版本号, 2022-05-30 14:58:35]
 */
@RestController("yuqiprojectlistaction")
@Scope("request")
public class YuQiProjectListAction extends BaseController {

    @Autowired
    private ICrawlDataDY iCrawlDataDY;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;

    @Autowired
    private IImportService service;

    /**
     * 表格控件model
     */
    private DataGridModel<StSpglXmjbxxb> model;

    private String areaCode;
    private TreeModel areaTreeModel;


    private String areaGuid;

    /**
     * 项目名称
     */
    private String xmmc;


    /**
     * 项目代码
     */
    private String xmdm;

    public void pageLoad() {
        areaCode = getRequestParameter("areaCode");
    }


    public DataGridModel<StSpglXmjbxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<StSpglXmjbxxb>() {

                @Override
                public List<StSpglXmjbxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(areaCode) && !"undefined".equals(areaCode)) {
                        sqlConditionUtil.eq("areacode", areaCode);
                    }
                    //项目名称的筛选条件
                    if (StringUtil.isNotBlank(xmdm)){
                        sqlConditionUtil.like("xmdm",xmdm);
                    }
                    if (StringUtil.isNotBlank(xmmc)){
                        sqlConditionUtil.like("xmmc",xmmc);
                    }
                    //项目代码的筛选条件
                    PageData<StSpglXmjbxxb> pageData = iCrawlDataDY.getListByPage(sqlConditionUtil.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    if (ValidateUtil.isNotBlankCollection(pageData.getList())) {
                        List<StSpglXmjbxxb> list = pageData.getList();
                        //查询每个区县的名字
                        for (StSpglXmjbxxb stSpglXmjbxxb : list) {
                            String xiaQuCode = stSpglXmjbxxb.getAreacode();
                            AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAreaByAreacode(xiaQuCode).getResult();
                            stSpglXmjbxxb.put("areaName",auditOrgaArea.getXiaquname());
                        }
                        this.setRowCount(pageData.getRowCount());
                        return list;
                    } else {
                        this.setRowCount(0);
                        return new ArrayList<>();
                    }
                }

            };
        }
        return model;
    }

    public TreeModel getAreaTreeModel() {

        if (areaTreeModel == null) {
            areaTreeModel = new TreeModel() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeNode == null) {
                        TreeNode root = new TreeNode();

                        root.setText("济宁市");
                        root.setId("");
                        root.setPid("-1");
                        root.getColumns().put("level", 1);
                        root.setExpanded(true);// 展开下一层节点
                        list.add(root);

                        String pId = "";
                        String XiaQuCode = "";
                        Integer level = 2;

                        List<AuditOrgaArea> areaList = service.getAreaWithLevel(level, XiaQuCode);

                        for (AuditOrgaArea auditOrgaArea : areaList) {
                            TreeNode node = new TreeNode();
                            node.setId(auditOrgaArea.getOuguid());
                            node.setText(auditOrgaArea.getOuname());
                            node.setPid(pId);
                            node.getColumns().put("xiaqucode", auditOrgaArea.getXiaqucode());
                            node.getColumns().put("level", level);
                            node.setLeaf(false);
                            list.add(node);
                        }
                    }

                    // 每次点击树节点前的加号，进行加载

                    return list;
                }
            };
        }


        return areaTreeModel;
    }

    public String getAreaGuid() {
        return areaGuid;
    }

    public void setAreaGuid(String areaGuid) {
        this.areaGuid = areaGuid;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getXmdm() {
        return xmdm;
    }

    public void setXmdm(String xmdm) {
        this.xmdm = xmdm;
    }
}
