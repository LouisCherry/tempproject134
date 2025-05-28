package com.epoint.projectstatisticsconfig.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.projectstatisticsconfig.api.entity.ProjectStatisticsConfig;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.projectstatisticsconfig.api.IProjectStatisticsConfigService;

/**
 * 办件统计配置表新增页面对应的后台
 *
 * @author 15056
 * @version [版本号, 2022-05-23 17:43:09]
 */
@RightRelation(ProjectStatisticsConfigListAction.class)
@RestController("projectstatisticsconfigaddaction")
@Scope("request")
public class ProjectStatisticsConfigAddAction extends BaseController {
    @Autowired
    private IProjectStatisticsConfigService iProjectStatisticsConfig;
    @Autowired
    private ICodeItemsService iCodeItems;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    @Autowired
    private IOuService iOuService;

    /**
     * 办件统计配置表实体对象
     */
    private ProjectStatisticsConfig dataBean = null;

    //所属辖区下拉树
    private TreeModel areaCodeModel;

    //所属部门下拉树
    private TreeModel ouguidModel;


    public void pageLoad() {
        dataBean = new ProjectStatisticsConfig();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        //先判断表里是否有数据
        ProjectStatisticsConfig isExist=iProjectStatisticsConfig.getInfoByAreacodeAndOuguid(dataBean.getArea_code(),dataBean.getOu_guid());
        if (isExist!=null){
            addCallbackParam("msg", "该辖区部门已配置，保存成功！");
            addCallbackParam("ouguid", dataBean.getOu_guid());
            dataBean = null;
            return;
        }
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        iProjectStatisticsConfig.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        addCallbackParam("ouguid", dataBean.getOu_guid());
        dataBean = null;
    }

    public ProjectStatisticsConfig getDataBean() {
        if (dataBean == null) {
            dataBean = new ProjectStatisticsConfig();
        }
        return dataBean;
    }

    public void setDataBean(ProjectStatisticsConfig dataBean) {
        this.dataBean = dataBean;
    }

    /**
     * getAreaCodeModel 所属辖区下拉树
     *
     * @return TreeModel
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 11:16
     */
    public TreeModel getAreaCodeModel() {
        if (areaCodeModel == null) {
            areaCodeModel = new TreeModel() {
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();
                    //首次加载树结构
                    if (treeNode == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有辖区");
                        root.setPid("-1");
                        root.setId("f9root");
                        root.setExpanded(false);
                        root.setCkr(false);
                        list.add(root);
                    } else {
                        //获取所有辖区列表
                        List<CodeItems> codeItemsList = iCodeItems.listCodeItemsByCodeName("区县市");
                        if (codeItemsList != null && !codeItemsList.isEmpty()) {
                            for (CodeItems codeItems : codeItemsList) {
                                //先获取辖区code
                                String areacode = iProjectStatisticsConfig.getAreaCodeByAreaName(codeItems.getItemText());
                                TreeNode node = new TreeNode();
                                node.setPid(treeNode.getId());
                                node.setId(areacode);
                                node.setText(codeItems.getItemText());
                                node.setExpanded(false);
                                node.setLeaf(true);
                                node.setCkr(true);
                                list.add(node);
                            }
                        }
                    }
                    return list;
                }
            };
        }
        return areaCodeModel;
    }

    /**
     * getOuguidModel 所属部门下拉树
     *
     * @return TreeModel
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 13:41
     */
    public TreeModel getOuguidModel() {
        if (ouguidModel == null) {
            ouguidModel = new TreeModel() {
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();
                    String areacode = getViewData("areacode");
                    if (StringUtil.isNotBlank(areacode)) {
                        AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAreaByAreacode(areacode).getResult();
                        //首次加载树结构
                        if (treeNode == null) {
                            TreeNode root = new TreeNode();
                            root.setText(auditOrgaArea.getXiaquname());
                            root.setPid("-1");
                            root.setId("f9root");
                            root.setExpanded(false);
                            root.setCkr(false);
                            list.add(root);
                        } else {
                            //获取当前辖区下的所有部门列表
                            List<String> ouguidList = iProjectStatisticsConfig.getAllOuguidByAreacode(areacode);
                            if (ouguidList != null && !ouguidList.isEmpty()) {
                                for (String ouguid : ouguidList) {
                                    //获取部门信息
                                    FrameOu frameOu = iOuService.getOuByOuGuid(ouguid);
                                    TreeNode node = new TreeNode();
                                    node.setPid(treeNode.getId());
                                    node.setId(frameOu.getOuguid());
                                    node.setText(frameOu.getOuname());
                                    node.setExpanded(false);
                                    node.setLeaf(true);
                                    node.setCkr(true);
                                    list.add(node);
                                }
                            }
                        }
                    }
                    return list;
                }
            };
        }
        return ouguidModel;
    }

    /**
     * onAreaCodeChanged 辖区值变更操作
     *
     * @param areacode
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 13:38
     */
    public void onAreaCodeChanged(String areacode) {
        addViewData("areacode", areacode);
    }
}
