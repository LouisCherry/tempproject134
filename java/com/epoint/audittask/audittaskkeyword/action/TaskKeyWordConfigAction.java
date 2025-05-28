package com.epoint.audittask.audittaskkeyword.action;

import com.epoint.audittask.audittaskkeyword.api.IAuditTaskKeywordService;
import com.epoint.audittask.audittaskkeyword.api.entity.AuditTaskKeyword;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 窗口事项配置页面对应的后台
 *
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("taskkeywordconfigaction")
@Scope("request")
public class TaskKeyWordConfigAction extends BaseController {


    private TreeModel treeModel = null;

    private String taskguid;

    /**
     * 页面来源
     */
    private String source;

    private String businessGuid;

    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IAuditTaskKeywordService auditTaskKeywordService;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Override
    public void pageLoad() {
        taskguid = getRequestParameter("taskguid");
        source = getRequestParameter("source");
        businessGuid = getRequestParameter("businessGuid");
    }

    public TreeModel getTaskTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel() {
                /***
                 * 加载树
                 */
                @Override
                public List<TreeNode> fetch(TreeNode root) {
                    List<TreeNode> list = new ArrayList<TreeNode>();
                    if (root == null) {
                        root = new TreeNode();
                        root.setId(TreeFunction9.F9ROOT);
                        root.setText("关键字");
                        root.getColumns().put("isTag", "false");
                        root.setExpanded(true);
                        list.add(root);
                        List<CodeItems> codeItemsList = codeItemsService.listCodeItemsByCodeName("事项关键字代码项");
                        for (CodeItems codeItems : codeItemsList) {
                            TreeNode childNode = new TreeNode();
                            childNode.setPid(TreeFunction9.F9ROOT);
                            childNode.setId(codeItems.getItemValue());
                            childNode.setText(codeItems.getItemText());
                            childNode.getColumns().put("isTag", "true");
                            childNode.setLeaf(true);
                            list.add(childNode);
                        }
                    } else {
                        List<CodeItems> codeItemsList = codeItemsService.listCodeItemsByCodeName("事项关键字代码项");
                        String keyWord = root.getSearchCondition();
                        if (StringUtil.isNotBlank(keyWord)) {
                            //将不在关键字里的筛选掉
                            codeItemsList.removeIf(selectItem -> !selectItem.getItemText().contains(keyWord));
                        }
                        for (CodeItems codeItems : codeItemsList) {
                            TreeNode childNode = new TreeNode();
                            childNode.setPid(TreeFunction9.F9ROOT);
                            childNode.setId(codeItems.getItemValue());
                            childNode.setText(codeItems.getItemText());
                            childNode.getColumns().put("isTag", "true");
                            childNode.setLeaf(true);
                            list.add(childNode);
                        }
                    }
                    return list;
                }
            };
            if (!isPostback()) {
                List<SelectItem> selectedItems = new ArrayList<SelectItem>();
                SqlConditionUtil sql = new SqlConditionUtil();
                List<AuditTaskKeyword> keywordList = new ArrayList<>();
                //如果是事项配置页面
                if ("task".equals(source)){
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                    sql.eq("taskid", auditTask.getTask_id());
                }
                if ("yjs".equals(source)){
                    sql.eq("taskid",businessGuid);
                }
                keywordList = auditTaskKeywordService.findListByCondition(sql.getMap());
                if (ValidateUtil.isNotBlankCollection(keywordList)) {
                    for (AuditTaskKeyword auditTaskKeyword : keywordList) {
                        SelectItem selectItem = new SelectItem();
                        selectItem.setText(auditTaskKeyword.getKeywordname());
                        selectItem.setValue(auditTaskKeyword.getKeywordvalue());
                        selectedItems.add(selectItem);
                    }
                    treeModel.setSelectNode(selectedItems);
                }
            }
        }
        return treeModel;
    }

    /**
     * guidList为关键字的代码项值
     * 一件事配置关键字时，audit_task_keyword的taskid为businessGuid;普通事项配置关键字时，audit_task_keyword的taskid为事项的taskid
     *
     * @param guidList
     */
    public void save(String guidList) {
        String taskid = "";
        String areacode = "";
        //1.删除之前的数据
        if ("task".equals(source)){
            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
            taskid = auditTask.getTask_id();
            areacode = auditTask.getAreacode();
        }
        if ("yjs".equals(source)){
        	AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        	if (business != null) {
        		areacode = business.getAreacode();
        	}
            taskid = businessGuid;
        }
        auditTaskKeywordService.deleteByTaskid(taskid);
        String[] guids = guidList.split(";");
        if (guids != null && guids.length > 0) {
            List<CodeItems> codeItemsList = codeItemsService.listCodeItemsByCodeName("事项关键字代码项");
            //将list转成map key为codevalue
            Map<String, String> map = codeItemsList.stream().collect(Collectors.toMap(CodeItems::getItemValue, CodeItems::getItemText));
            for (String guid : guids) {
                AuditTaskKeyword auditTaskKeyword = new AuditTaskKeyword();
                auditTaskKeyword.setRowguid(UUID.randomUUID().toString());
                auditTaskKeyword.setOperateusername(UserSession.getInstance().getDisplayName());
                auditTaskKeyword.setOperateusername(UserSession.getInstance().getUserGuid());
                auditTaskKeyword.setTaskid(taskid);
                auditTaskKeyword.set("areacode", areacode);
                auditTaskKeyword.setKeywordname(map.get(guid));
                auditTaskKeyword.setKeywordvalue(guid);
                auditTaskKeywordService.insert(auditTaskKeyword);
            }
        }
        addCallbackParam("msg", "保存成功！");
    }

}
