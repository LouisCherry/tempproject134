package com.epoint.xmz.thirdreporteddata.auditsp.controls.danti.action;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.auditsp.dwgcinfo.api.IDwgcInfoService;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.basic.auditsp.dwgcjlneed.api.IDwgcJlneedService;
import com.epoint.basic.auditsp.dwgcjlneed.entity.DwgcJlneed;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 单体信息表（单体同子单位工程）list页面对应的后台
 *
 * @author WIN-H366O37KOW0$
 * @version [版本号, 2018-05-17 21:59:10]
 */
@RestController("dantiinfov3listaction")
@Scope("request")
public class DantiInfov3ListAction extends BaseController {
    private static final long serialVersionUID = 7292762752363027253L;
    @Autowired
    ICodeItemsService iCodeItemsService;
    @Autowired
    private IDantiInfoV3Service service;
    @Autowired
    private IDwgcJlneedService dwgcJlneedService;
    @Autowired
    private IDwgcInfoService dwgcInfoService;
    @Autowired
    private IDantiSubRelationService dantiSubRelationService;
    /**
     * 单体信息表（单体同子单位工程）实体对象
     */
    private DantiInfoV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<DantiInfoV3> model;

    @Autowired
    private ICodeItemsService codeservice;

    private TreeModel treeModel;
    private String selectedguid;
    private String gongchengguid;
    @Autowired
    private IAuditRsItemBaseinfo auditRsItemBaseinfoService;
    private DantiSubRelation dantiSubRelation;

    public void pageLoad() {
        String projectguid = getRequestParameter("projectguid");
        List<DantiInfoV3> dtlist = service.findListByProjectguid(projectguid);
        for (DantiInfoV3 dantiInfo : dtlist) {
            if (StringUtil.isNotBlank(dantiInfo.getGongchengguid())) {
                addCallbackParam("gongchengguid", dantiInfo.getGongchengguid());
            }
        }
    }

    /**
     * 永久删除子单位工程
     */
    public void everdeletesonproj() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);

        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 删除子单位工程
     */
    public void deletesonproj() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            DantiInfoV3 dantiInfo = service.find(sel);
            String gongchengguid_danti = dantiInfo.getGongchengguid();
            List<DantiInfoV3> dantiInfos = service.findListByGCguid(gongchengguid_danti);
            if (dantiInfos.size() == select.size()) {
                //                List<DwgcJlneed> dwgcjlneeds = dwgcJlneedService
                //                        .findListByGCguid(gongchengguid);
                //                if (StringUtil.isNotBlank(dwgcjlneeds)) {
                //                    for (DwgcJlneed dwgcJlneed : dwgcjlneeds) {
                //                        dwgcJlneedService.deleteByGuid(dwgcJlneed.getRowguid());
                //                    }
                //                }
                DwgcInfo dwgcInfo = dwgcInfoService.find(gongchengguid_danti);
                if (dwgcInfo != null) {
                    dwgcInfoService.deleteByGuid(gongchengguid_danti);
                }
            }
            dantiInfo.setGongchengguid("");
            service.update(dantiInfo);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 删除此单位工程
     */
    public void deleteproj() {
        if (StringUtil.isNotBlank(gongchengguid)) {
            List<DantiInfoV3> dantiInfos = service.findListByGCguid(gongchengguid);
            for (DantiInfoV3 dantiInfo : dantiInfos) {
                dantiInfo.setGongchengguid(null);
                service.update(dantiInfo);
            }
            List<DwgcJlneed> dwgcjlneeds = dwgcJlneedService.findListByGCguid(gongchengguid);
            if (dwgcjlneeds != null) {
                for (DwgcJlneed dwgcJlneed : dwgcjlneeds) {
                    dwgcJlneedService.deleteByGuid(dwgcJlneed.getRowguid());
                }
            }
            DwgcInfo dwgcInfo = dwgcInfoService.find(gongchengguid);
            if (dwgcInfo != null) {
                dwgcInfoService.deleteByGuid(gongchengguid);
            }
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<DantiInfoV3> getDataGridData1() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<DantiInfoV3>() {
                private static final long serialVersionUID = 4923522817508238698L;

                @Override
                public List<DantiInfoV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    String projectguid = getRequestParameter("projectguid");
                    PageData<DantiInfoV3> pageData = service.pageDantiInfo(conditionSql, projectguid, selectedguid,
                            gongchengguid, conditionList, first, pageSize, sortField, sortOrder);
                    List<DantiInfoV3> list = pageData.getList();
                    int count = pageData.getRowCount();
                    this.setRowCount(count);
                    addCallbackParam("gongchengguid", gongchengguid);
                    return list;
                }

            };
        }
        return model;
    }

    public DataGridModel<DantiInfoV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<DantiInfoV3>() {
                private static final long serialVersionUID = -6455344876944357468L;

                @Override
                public List<DantiInfoV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    // 改造不需要组建就能选择单体
                    String projectguid = getRequestParameter("projectguid");
                    String subappguid = getRequestParameter("subappguid");
                    PageData<DantiInfoV3> pageData = service.pageDantiLisrInfo(conditionSql, projectguid, subappguid,
                            gongchengguid, conditionList, first, pageSize, sortField, sortOrder);
                    List<DantiInfoV3> list = pageData.getList();
                    // 展示层级 与新增 修改 详情页一致
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (DantiInfoV3 dantiInfo : list) {
                            String gclbName = getNameByCode(dantiInfo.getGcyt());
                            dantiInfo.setGcyt(gclbName);
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

    /**
     * 添加选择
     */
    public void addsonproj() {
        List<String> selects = getDataGridData().getSelectKeys();
        String subappguid = getRequestParameter("subappguid");
        if (selects != null) {
            for (String rowguid : selects) {

                dantiSubRelation = new DantiSubRelation();
                dantiSubRelation.setSubappguid(subappguid);
                dantiSubRelation.setDantiguid(rowguid);
                dantiSubRelation.setRowguid(UUID.randomUUID().toString());
                // 是否V3
                dantiSubRelation.set("is_v3", ZwfwConstant.CONSTANT_STR_ONE);
                dantiSubRelationService.insert(dantiSubRelation);

            }
            addCallbackParam("msg", "添加成功！");
        }
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel() {
                private static final long serialVersionUID = -6475576314632443415L;

                @Override
                public List<TreeNode> fetch(TreeNode root) {
                    List<TreeNode> nodes = new ArrayList<TreeNode>();
                    List<TreeNode> treelist;
                    if (root == null) {// arg0为null，则是根节点
                        root = new TreeNode();
                        // 获取项目projectguid
                        String projectguid = getRequestParameter("projectguid");
                        AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(
                                projectguid).getResult();
                        // 缺少通过projectguid查找项目名称的方法暂时用xxx工程代替

                        if (auditRsItemBaseinfo != null && StringUtil.isNotBlank(auditRsItemBaseinfo.getItemname())) {
                            root.setText(auditRsItemBaseinfo.getItemname());
                        } else {
                            root.setText("单位工程");
                        }
                        root.setId(projectguid);
                        root.setExpanded(true);
                        nodes.add(root);
                        // 子节点
                        treelist = new ArrayList<TreeNode>();
                        Map<String, String> map = new HashMap<>();
                        List<DantiInfoV3> dantiInfos = service.findListByProjectguid(projectguid);
                        for (DantiInfoV3 dantiInfo : dantiInfos) {
                            map.put(dantiInfo.getGongchengguid(), "");
                        }
                        for (String key : map.keySet()) {
                            TreeNode node = new TreeNode();
                            if (StringUtil.isNotBlank(key)) {
                                node.setId(key);
                                DwgcInfo dwgcInfo = dwgcInfoService.find(key);
                                if (dwgcInfo != null) {
                                    if (StringUtil.isNotBlank(dwgcInfo.getGongchengname())) {
                                        node.setText(dwgcInfo.getGongchengname());
                                    } else {
                                        node.setText("未命名单体工程");
                                    }
                                }

                                node.setPid(projectguid);
                                node.setLeaf(true);
                                treelist.add(node);
                            }

                        }

                        nodes.addAll(treelist);
                    }
                    return nodes;
                }
            };
        }
        return treeModel;
    }

    public DantiInfoV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new DantiInfoV3();
        }
        return dataBean;
    }

    public void setDataBean(DantiInfoV3 dataBean) {
        this.dataBean = dataBean;
    }

    public String getNameByCode(String code) {
        if (StringUtil.isBlank(code)) {
            return "";
        }
        String result = "";
        CodeItems codeitem = iCodeItemsService.getCodeItemByCodeName("国标_工程类别", String.valueOf(code));
        if (codeitem != null) {
            result += codeitem.getItemText();
            String codefl = code.substring(0, 2).replaceAll("0", "");
            CodeItems codeitemfl = iCodeItemsService.getCodeItemByCodeName("国标_工程行业分类", String.valueOf(codefl));
            if (codeitemfl != null) {
                result = codeitemfl.getItemText() + "-" + result;
            }
        }
        return result;
    }

    ;

    public String getSelectedguid() {
        return selectedguid;
    }

    public void setSelectedguid(String selectedguid) {
        this.selectedguid = selectedguid;
    }

    public String getGongchengguid() {
        return gongchengguid;
    }

    public void setGongchengguid(String gongchengguid) {
        this.gongchengguid = gongchengguid;
    }

    /**
     * 组建单位工程
     */
    public void buildDanweiProject() {
        List<String> select = getDataGridData().getSelectKeys();
        if (select != null) {
            for (String rowguid : select) {
                dataBean = service.find(rowguid);
                if (StringUtil.isBlank(dataBean.getGongchengguid())) {
                    WebUtil.setSessionAttribute(request.getSession(), "rowguids", select);
                } else {
                    addCallbackParam("msg", "该子单位工程已经被组建！");
                }

            }
        } else {
            addCallbackParam("msg", "请选择要组建的子单位工程！");
        }
    }
}
