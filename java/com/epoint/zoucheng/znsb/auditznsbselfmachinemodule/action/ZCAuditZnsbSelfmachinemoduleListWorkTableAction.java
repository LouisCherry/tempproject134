package com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.inter.IAuditZnsbSelfmachinemoduleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.domain.ZCAuditZnsbSelfmachinemodule;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.inter.IZCAuditZnsbSelfmachinemoduleService;
import com.epoint.zoucheng.znsb.worktablecomment.util.QueueConstant;

/**
 * 自助服务一体机模块配置list页面对应的后台
 * 
 * @author 54201
 * @version [版本号, 2018-06-14 15:50:55]
 */
@RestController("zcauditznsbselfmachinemodulelistworktableaction")
@Scope("request")
public class ZCAuditZnsbSelfmachinemoduleListWorkTableAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 3414105549841931477L;

    @Autowired
    private IZCAuditZnsbSelfmachinemoduleService service;

    private TreeModel treeModel;

    /**
     * 自助服务一体机模块配置实体对象
     */
    private ZCAuditZnsbSelfmachinemodule dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ZCAuditZnsbSelfmachinemodule> model;
    @Autowired
    private ICodeItemsService codeItemService;
    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String modulename;

    private String macaddress;
    
    /**
     * 模块配置类型下拉列表model
     */
     private List<SelectItem> moduleConfigtypeModel = null;

    public void pageLoad() {
        macaddress = getRequestParameter("macaddress");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        List<String> allId = new ArrayList<String>();
        String centerguid = ZwfwUserSession.getInstance().getCenterGuid();  
        for (String sel : select) {
            allId.add(sel);
            getIds(allId, sel, centerguid);
        }
        
        for (String id : allId) {
            service.deleteByGuid(id);
        }
    
        List<ZCAuditZnsbSelfmachinemodule> commonmodulelist = service.getModuleListByCenterguid(ZwfwUserSession.getInstance().getCenterGuid(),QueueConstant.CONSTANT_STR_TWO).getResult();
        for (ZCAuditZnsbSelfmachinemodule auditZnsbSelfmachinemodule : commonmodulelist) {
            List<ZCAuditZnsbSelfmachinemodule> result = service.getModuleListByParentmoduleguid(auditZnsbSelfmachinemodule.getRowguid(), centerguid).getResult();
            if(result!=null&&!result.isEmpty()){
                if(!QueueConstant.CONSTANT_STR_ONE.equals(auditZnsbSelfmachinemodule.getIsParentmodule())){
                    auditZnsbSelfmachinemodule.setIsParentmodule(QueueConstant.CONSTANT_STR_ONE);
                    service.update(auditZnsbSelfmachinemodule);
                }
            }
            else{
                if(QueueConstant.CONSTANT_STR_ONE.equals(auditZnsbSelfmachinemodule.getIsParentmodule())){
                    auditZnsbSelfmachinemodule.setIsParentmodule(QueueConstant.CONSTANT_STR_ZERO);
                    service.update(auditZnsbSelfmachinemodule);
                }
            }
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     *  [获取当前模块下的所有子集模块，用于递归删除]
     *  @param ids 存放id的集合
     *  @param parentId 当前父模块的id
     *  @param centerguid 中心guid
     */
    public void getIds(List<String> ids, String parentId, String centerguid) {

        List<ZCAuditZnsbSelfmachinemodule> result = service.getModuleListByParentmoduleguid(parentId, centerguid)
                .getResult();
        for (ZCAuditZnsbSelfmachinemodule auditZnsbSelfmachinemodule : result) {
            ids.add(auditZnsbSelfmachinemodule.getRowguid());
            getIds(ids, auditZnsbSelfmachinemodule.getRowguid(), centerguid);
        }
    }

    public DataGridModel<ZCAuditZnsbSelfmachinemodule> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ZCAuditZnsbSelfmachinemodule>()
            {
                private static final long serialVersionUID = -6867074069727741581L;

                @Override
                public List<ZCAuditZnsbSelfmachinemodule> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(getRequestParameter("guid"))
                            && "NoPZ".equals(getRequestParameter("guid"))) {

                        sql.isBlank("centerguid");
                    }
                    else {
                        sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    }
                    if (StringUtil.isNotBlank(dataBean.getModulename())) {
                        sql.like("modulename", dataBean.getModulename());
                    }
                    if (StringUtil.isNotBlank(dataBean.getParentmoduleguid())) {
                        if (dataBean.getParentmoduleguid().length() == QueueConstant.CONSTANT_INT_ONE) {
                            sql.eq("moduletype", dataBean.getParentmoduleguid());
                            sql.isBlank("parentmoduleguid");
                        }
                        else {
                            sql.eq("parentmoduleguid", dataBean.getParentmoduleguid());
                        }

                    }
                    
//                    if (StringUtil.isNotBlank(dataBean.getModuleconfigtype())) {
//                        sql.eq("moduleconfigtype", dataBean.getModuleconfigtype());
//                    }
                    sql.eq("moduleconfigtype", "2");
                    
//                    sql.setOrder("moduletype", "asc");
                    sql.setOrder("Ordernum", "desc");
                    if (StringUtil.isNotBlank(macaddress)) {
                        sql.eq("macaddress", macaddress);
                    }
                    else {
                        sql.isBlank("macaddress");
                    }

                    PageData<ZCAuditZnsbSelfmachinemodule> pageData = service
                            .getSelfmachinemoduleByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
//                    service.get
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();

                }

            };
        }
        return model;
    }

    public ZCAuditZnsbSelfmachinemodule getDataBean() {
        if (dataBean == null) {
            dataBean = new ZCAuditZnsbSelfmachinemodule();
        }
        return dataBean;
    }

    public void setDataBean(ZCAuditZnsbSelfmachinemodule dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    /**
     * 初始化模块数据
     */
    public void initModule(String moduleconfigtype) {
        if (StringUtil.isBlank(moduleconfigtype)) {
            moduleconfigtype="0";
        }
        
        //删除历史数据
        service.deletebyCenterAndConfig(ZwfwUserSession.getInstance().getCenterGuid(),moduleconfigtype);
        //初始化数据
        service.initModule(userSession.getDisplayName(), ZwfwUserSession.getInstance().getCenterGuid(),moduleconfigtype);
        // 初始化parentmodule
        List<ZCAuditZnsbSelfmachinemodule> commonmodulelist = service.getModuleListByCenterguid(ZwfwUserSession.getInstance().getCenterGuid(),moduleconfigtype).getResult();
        for (ZCAuditZnsbSelfmachinemodule auditZnsbSelfmachinemodule : commonmodulelist) {
            List<ZCAuditZnsbSelfmachinemodule> result = service.getModuleListByParentmoduleguid(auditZnsbSelfmachinemodule.getRowguid(), ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            if(result!=null&&!result.isEmpty()){
//                if(!QueueConstant.CONSTANT_STR_ONE.equals(auditZnsbSelfmachinemodule.getIsParentmodule())){
                    auditZnsbSelfmachinemodule.setIsParentmodule(QueueConstant.CONSTANT_STR_ONE);
                    service.update(auditZnsbSelfmachinemodule);
//                }
            }
            else{
//                if(QueueConstant.CONSTANT_STR_ONE.equals(auditZnsbSelfmachinemodule.getIsParentmodule())){
                    auditZnsbSelfmachinemodule.setIsParentmodule(QueueConstant.CONSTANT_STR_ZERO);
                    service.update(auditZnsbSelfmachinemodule);
//                }
            }
        }
        addCallbackParam("msg", "初始化数据成功！");
    }

    /**
     * 复制模块数据
     */
    public void copyModule() {
        //复制之前删除历史数据，防止重复复制
        if (StringUtil.isNotBlank(macaddress)) {
            service.deletebyMacaddress(macaddress);
            //复制模块初始化的数据，加入了Macaddress
            service.copyModule(userSession.getDisplayName(), ZwfwUserSession.getInstance().getCenterGuid(), macaddress);
            addCallbackParam("msg", "复制模块成功！");
        }
        else {
            addCallbackParam("msg", "复制模块失败！未获取到Macaddress");
        }
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getModuleConfigTypeModel() {
        if (moduleConfigtypeModel == null) {
            moduleConfigtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "模块配置类型", null, false));
        }
        return this.moduleConfigtypeModel;
    }

    public TreeModel getTreeData() {

        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                /**
                 * 树加载事件
                 *
                 * @param treeNode
                 *            当前展开的节点
                 * @return List<TreeNode>
                 */
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有模块");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        if (StringUtil.isBlank(objectGuid)) {
                            // 一级目录
                            List<ZCAuditZnsbSelfmachinemodule> modulelist=service.getParentListByConfigType("2", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                            if(StringUtil.isNotBlank(macaddress)){
                                modulelist.addAll(service.getParentListByConfigTypeAndMac("2", ZwfwUserSession.getInstance().getCenterGuid(),macaddress).getResult());
                            }
                            for (ZCAuditZnsbSelfmachinemodule auditZnsbSelfmachinemodule : modulelist) {
                                TreeNode node = new TreeNode();
                                node.setId(auditZnsbSelfmachinemodule.getRowguid());
                                node.setText(auditZnsbSelfmachinemodule.getModulename());
                                node.setPid(objectGuid);
                                node.setLeaf(true);
                                int count = modulelist.size();
                                if (count > 0) {
                                    node.setLeaf(false);
                                }
                                list.add(node);
                            }
                          
//                            List<CodeItems> itemlist = codeItemService.listCodeItemsByCodeName("首页左侧分类");
//                            for (CodeItems codeItems : itemlist) {
//                                TreeNode node = new TreeNode();
//                                node.setId(codeItems.getItemValue());
//                                node.setText(codeItems.getItemText());
//                                node.setPid(objectGuid);
//                                node.setLeaf(true);
//                                int count = service.getCountByModuleType(codeItems.getItemValue(),
//                                        ZwfwUserSession.getInstance().getCenterGuid()).getResult();
//                                if (count > 0) {
//                                    node.setLeaf(false);
//                                }
//                                list.add(node);
//                            }
                        }
                        else {
                            // 二级目录
                            List<ZCAuditZnsbSelfmachinemodule> modulelist = null;
                            if (objectGuid.length() == QueueConstant.CONSTANT_INT_ONE) {
                                modulelist = service.getNoParentModuleGuidModuleListByModuleType(objectGuid,
                                        ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                            }
                            else {
                                // 多级目录
                                modulelist = service.getModuleListByParentmoduleguid(objectGuid,
                                        ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                                if(StringUtil.isNotBlank(macaddress)){
                                    modulelist.addAll(service.getModuleListByParentmoduleguidAndMac(objectGuid, ZwfwUserSession.getInstance().getCenterGuid(), macaddress).getResult());
                                }
                                
                            }
                            for (int i = 0; i < modulelist.size(); i++) {
                                TreeNode node = new TreeNode();
                                node.setId(modulelist.get(i).getRowguid());
                                node.setText(modulelist.get(i).getModulename());
                                node.setPid(objectGuid);
                                node.setLeaf(true);
                                List<ZCAuditZnsbSelfmachinemodule> sonmodulelist = service
                                        .getModuleListByParentmoduleguid(modulelist.get(i).getRowguid(),
                                                ZwfwUserSession.getInstance().getCenterGuid())
                                        .getResult();
                                if(StringUtil.isNotBlank(macaddress)){
                                    sonmodulelist.addAll(service.getModuleListByParentmoduleguidAndMac(modulelist.get(i).getRowguid(), ZwfwUserSession.getInstance().getCenterGuid(), macaddress).getResult());
                                }
                                if (!sonmodulelist.isEmpty()) {
                                    node.setLeaf(false);
                                }
                                list.add(node);
                            }
                        }
                    }
                    return list;
                }

                /**
                 * 树节点点击事件
                 *
                 * @param item
                 *            当前点击的树节点
                 * @return List<com.epoint.core.dto.model.SelectItem>
                 */
                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode item) {
                    // TODO Auto-generated method stub
                    return super.onLazyNodeSelect(item);
                }

            };
        }
        return treeModel;
    }

}
