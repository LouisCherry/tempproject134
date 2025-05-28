package com.epoint.zoucheng.device.infopub.infopubled.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.device.infopub.infopubled.api.IInfopubLedService;
import com.epoint.zoucheng.device.infopub.infopubled.api.entity.InfopubLed;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.IInfopubPublishService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * LED大屏list页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-22 15:26:57]
 */
@RestController("infopubledlistaction")
@Scope("request")
public class InfopubLedListAction extends BaseController
{
    private static final long serialVersionUID = 3777359558883940865L;
    @Autowired
    private IInfopubLedService service;
    @Autowired
    private IInfopubPublishService infopubPublishService;
    @Autowired
    private ICodeItemsService codeService;

    /**
     * LED大屏实体对象
     */
    private InfopubLed dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<InfopubLed> model;

    /**
    * 分辨率下拉列表model
    */
    private List<SelectItem> resolutionModel = null;

    /**
     * 保存节点信息，实现左树右表的动态刷新
     */
    private String course;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            infopubPublishService.deleteByLedGuid(sel);
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<InfopubLed> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<InfopubLed>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 8244933686143903813L;

                @Override
                public List<InfopubLed> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isNotBlank(course) && !course.equals("f9root")) {
                        addViewData("course", course);
                        conditionSql = conditionSql + " and ledstyle = '" + course + "'";
                    }
                    List<InfopubLed> list = service.findList(
                            ListGenerator.generateSql("InfoPub_Led", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.findList(
                            ListGenerator.generateSql("InfoPub_Led", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setLedstyle(course);
        service.insert(dataBean);
        addCallbackParam("msg", "新增成功！");
        dataBean = null;
    }

    public void saveAll() {
        List<InfopubLed> list = getDataGridData().getWrappedData();
        for (InfopubLed info : list) {
            service.update(info);
        }
        addCallbackParam("msg", "修改成功！");
    }

    public TreeModel getTreeModel() {
        return new TreeModel()
        {
            private static final long serialVersionUID = 8910715135343435523L;
            List<TreeNode> nodes = new ArrayList<>();

            @Override
            public List<TreeNode> fetch(TreeNode arg0) {
                TreeNode root = new TreeNode();
                root.setText("LED分组");
                root.setId("f9root");
                root.setPid("");
                nodes.add(root);
                root.setExpanded(false);

                //找到所有的LED分组
                List<CodeItems> codeItemsList = codeService.listCodeItemsByCodeName("LED分组");
                for (CodeItems codeItems : codeItemsList) {
                    TreeNode NodeBuilding = new TreeNode();
                    NodeBuilding.setPid("f9root");
                    NodeBuilding.setId(codeItems.getItemValue());
                    NodeBuilding.setText(codeItems.getItemText());
                    NodeBuilding.setLeaf(true);
                    nodes.add(NodeBuilding);
                }

                return nodes;
            }
        };
    }

    public InfopubLed getDataBean() {
        if (dataBean == null) {
            dataBean = new InfopubLed();
        }
        return dataBean;
    }

    public void setDataBean(InfopubLed dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getResolutionModel() {
        if (resolutionModel == null) {
            resolutionModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "分辨率", null, false));
        }
        return this.resolutionModel;
    }

}
