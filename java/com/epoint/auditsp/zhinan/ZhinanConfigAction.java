package com.epoint.auditsp.zhinan;

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
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.yjs.yjszn.api.IYjsZnService;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
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
@RestController("zhinanconfigaction")
@Scope("request")
public class ZhinanConfigAction extends BaseController {


    private TreeModel treeModel = null;

    private String businessGuid;

    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IYjsZnService service;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Override
    public void pageLoad() {
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
                        root.setText("所有指南");
//                        root.getColumns().put("isTag", "false");
                        root.setExpanded(true);
                        list.add(root);
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("select * from YJS_ZN where areacode=? and type=1");
                        List<YjsZn> yjsZns = service.findList(stringBuffer.toString(), ZwfwUserSession.getInstance().getAreaCode());
                        if(yjsZns != null && yjsZns.size() > 0){
                            for (YjsZn yjsZn : yjsZns) {
                                TreeNode childNode = new TreeNode();
                                childNode.setPid(TreeFunction9.F9ROOT);
                                childNode.setId(yjsZn.getRowguid());
                                childNode.setText(yjsZn.getTitle());
                                childNode.setLeaf(true);
                                list.add(childNode);
                            }
                        }
                    } /*else {
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
                    }*/
                    return list;
                }
            };
            if (!isPostback()) {
                StringBuffer stringBuffer = new StringBuffer();
                List<SelectItem> selectedItems = new ArrayList<SelectItem>();
                stringBuffer.append("select * from YJS_ZN where businessguid=?");
                List<YjsZn> yjsZns = service.findList(stringBuffer.toString(),businessGuid);
                if(yjsZns != null && yjsZns.size() > 0){
                    for (YjsZn yjsZn : yjsZns) {
                        SelectItem selectItem = new SelectItem();
                        selectItem.setText(yjsZn.getTitle());
                        selectItem.setValue(yjsZn.getRowguid());
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
        String[] guids = guidList.split(";");
        if (guids != null && guids.length > 0) {
            //先删除原有关联的
            service.deleteByBusinessGuid(businessGuid);
            for (String guid : guids) {
                YjsZn yjsZn = service.find(guid);
                yjsZn.set("businessGuid",businessGuid);
                service.update(yjsZn);
            }
        }
        addCallbackParam("msg", "保存成功！");
    }

}
