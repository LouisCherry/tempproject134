package com.epoint.xmgj.auditrsitembaseinfo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmgj.auditrsitembaseinfo.api.IAuditRsItemBaseinfoService;
import com.epoint.xmgj.auditrsitembaseinfo.api.entity.AuditRsItemBaseinfo;
import com.tzwy.util.StringUtils;

/**
 * 项目表修改页面对应的后台
 *
 * @author pansh
 * @version [版本号, 2025-02-12 17:31:52]
 */
@RightRelation(AuditRsItemBaseinfoGjListAction.class)
@RestController("auditrsitembaseinfogjeditaction")
@Scope("request")
public class AuditRsItemBaseinfoGjEditAction extends BaseController {

    @Autowired
    private IAuditRsItemBaseinfoService service;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 项目表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;

    /**
     * 重点项目类型下拉列表model
     */
    private List<SelectItem> keyprojecttypeModel = null;
    /**
     * 投资方式下拉列表model
     */
    private List<SelectItem> investmentmethodModel = null;
    /**
     * 项目性质下拉列表model
     */
    private List<SelectItem> projectnatureModel = null;
    /**
     * 项目类型下拉列表model
     */
    private LazyTreeModal9 projectcategoryModel = null;
    /**
     * 形象进度下拉列表model
     */
    private List<SelectItem> imageprogressModel = null;
    /**
     * 进度详情下拉列表model
     */
    private List<SelectItem> progressdetailsModel = null;
    /**
     * 变更状态下拉列表model
     */
    private List<SelectItem> changestatusModel = null;
    /**
     * 支持类型下拉列表model
     */
    private List<SelectItem> supporttypeModel = null;
    /**
     * 资金到位情况说明下拉列表model
     */
    private List<SelectItem> fundsreceivedModel = null;
    /**
     * 所属产业链下拉列表model
     */
    private LazyTreeModal9 industrialchainModel = null;
    /**
     * 所属园区下拉列表model
     */
    private List<SelectItem> industrialparkModel = null;

    /**
     * 所属辖区下拉列表model
     */
    private List<SelectItem> projectdistrictModel = null;


    /**
     * 资金到位说明文件
     */
    private FileUploadModel9 fundsreceivedUploadModel;
    /**
     * 所属辖区下拉列表model
     */
    private List<SelectItem> investmentpercentageModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }else{
            if(!isPostback() && StringUtil.isBlank(dataBean.get("fundsreceivedclientguid"))){
                dataBean.put("fundsreceivedclientguid", UUID.randomUUID().toString());
                addViewData("fundsreceivedclientguid",dataBean.get("fundsreceivedclientguid"));
            }else{
                if(StringUtil.isBlank(dataBean.get("fundsreceivedclientguid"))){
                    dataBean.put("fundsreceivedclientguid", getViewData("fundsreceivedclientguid"));
                }
            }
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        if (!"其他园区".equals(dataBean.getIndustrialpark())) {
            dataBean.setIndustrialparkremark(null);
        }
        dataBean.setOperatedate(new Date());
        dataBean.set("lurutime",new Date());
        if(StringUtils.isNoneBlank(getViewData("fundsreceivedclientguid"))){
            dataBean.set("fundsreceivedclientguid", getViewData("fundsreceivedclientguid"));
        }
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public AuditRsItemBaseinfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getKeyprojecttypeModel() {
        if (keyprojecttypeModel == null) {
            keyprojecttypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "重点项目类型", null, false));
        }
        return this.keyprojecttypeModel;
    }

    public List<SelectItem> getInvestmentmethodModel() {
        if (investmentmethodModel == null) {
            investmentmethodModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "投资方式", null, false));
        }
        return this.investmentmethodModel;
    }

    public List<SelectItem> getProjectnatureModel() {
        if (projectnatureModel == null) {
            projectnatureModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目性质", null, false));
        }
        return this.projectnatureModel;
    }

    public LazyTreeModal9 getProjectcategoryModel() {
        if (projectcategoryModel == null) {
            projectcategoryModel = CodeModalFactory.factory("下拉单选树", "项目管家项目类型", null, false);
        }
        return this.projectcategoryModel;
    }

    public TreeModel treeModel;

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                public List<TreeNode> fetch(TreeNode treeNode) {
                    String investmentmethod = dataBean.getInvestmentmethod();
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();

                        root.setText("所有项目类型");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    // 每次点击树节点前的加号，进行加载
                    else {
                        // 查询出所有人员的绑定信息，以下是框架封装的方法，具体参数请参见api
                        List<CodeItems> listRoot = codeItemsService.listCodeItemsByCodeName("项目管家项目类型");
                        if (StringUtil.isNotBlank(investmentmethod)) {
                            // 当投资方式选择了市级财政直接投资、县级财政直接投资、市级财政资本金注入、县级财政资本金注入时，项目类型只展示：公共服务设施、市政公用、民生工程、其他，而工业、商业、住宅不在展示
                            if ("05".equals(investmentmethod)) {
                                Iterator<CodeItems> iterator = listRoot.iterator();
                                while (iterator.hasNext()) {
                                    String value = iterator.next().getItemValue();
                                    if (!value.startsWith("01") && !value.startsWith("02") && !value.startsWith("03")) {
                                        iterator.remove();
                                    }
                                }
                            }
                            else {
                                Iterator<CodeItems> iterator = listRoot.iterator();
                                while (iterator.hasNext()) {
                                    String value = iterator.next().getItemValue();
                                    if (value.startsWith("01") || value.startsWith("02") || value.startsWith("03")) {
                                        iterator.remove();
                                    }
                                }
                            }
                        }
                        for (CodeItems codeItems : listRoot) {
                            String itemvalue = codeItems.getItemValue();
                            String pid = "";
                            if (StringUtil.isNotBlank(itemvalue) && itemvalue.length() > 2) {
                                pid = itemvalue.substring(0, itemvalue.length() - 2);
                            }
                            TreeNode ouTreeNode = new TreeNode();
                            ouTreeNode.setId(codeItems.getItemValue());
                            ouTreeNode.setPid(pid);
                            ouTreeNode.setText(codeItems.getItemText());
                            ouTreeNode.setExpanded(false);
                            ouTreeNode.setLeaf(true);
                            list.add(ouTreeNode);
                        }
                    }
                    return list;
                }

            };
        }
        return treeModel;
    }

    public List<SelectItem> getImageprogressModel() {
        if (imageprogressModel == null) {
            imageprogressModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "形象进度", null, false));
        }
        return this.imageprogressModel;
    }

    public List<SelectItem> getProgressdetailsModel() {
        if (progressdetailsModel == null) {
            progressdetailsModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "进度详情", null, false));
        }
        return this.progressdetailsModel;
    }

    public List<SelectItem> getChangestatusModel() {
        if (changestatusModel == null) {
            changestatusModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "变更状态", null, false));
        }
        return this.changestatusModel;
    }

    public List<SelectItem> getSupporttypeModel() {
        if (supporttypeModel == null) {
            supporttypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "支持类型", null, false));
        }
        return this.supporttypeModel;
    }

    public List<SelectItem> getFundsreceivedModel() {
        if (fundsreceivedModel == null) {
            fundsreceivedModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "null", null, false));
        }
        return this.fundsreceivedModel;
    }

    public LazyTreeModal9 getIndustrialchainModel() {
        if (industrialchainModel == null) {
            industrialchainModel = CodeModalFactory.factory("下拉单选树", "所属产业链", null, false);
        }
        return this.industrialchainModel;
    }

    public List<SelectItem> getIndustrialparkModel() {
        if (industrialparkModel == null) {
            industrialparkModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属园区", null, false));
        }
        return this.industrialparkModel;
    }

    public List<SelectItem> getProjectdistrictModel() {
        if (projectdistrictModel == null) {
            projectdistrictModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区对应关系", null, false));
        }
        return this.projectdistrictModel;
    }

    public List<SelectItem> getInvestmentpercentageModel() {
        if (investmentpercentageModel == null) {
            investmentpercentageModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "占总投资金额比例", null, false));
        }
        return this.investmentpercentageModel;
    }

    public FileUploadModel9 getFundsreceivedUploadModel() {
        if (fundsreceivedUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    fundsreceivedUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage arg0) {
                    return true;
                }
            };
            fundsreceivedUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.get("fundsreceivedclientguid") , null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fundsreceivedUploadModel;
    }
}
