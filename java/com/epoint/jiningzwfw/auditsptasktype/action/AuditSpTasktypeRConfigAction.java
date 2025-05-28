package com.epoint.jiningzwfw.auditsptasktype.action;

import com.alibaba.fastjson.JSON;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.audittask.basic.api.IJnAuditTaskService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.jiningzwfw.auditsptasktype.api.IAuditSpTasktypeRService;
import com.epoint.jiningzwfw.auditsptasktype.api.IAuditSpTasktypeService;
import com.epoint.jiningzwfw.auditsptasktype.api.entity.AuditSpTasktype;
import com.epoint.jiningzwfw.auditsptasktype.api.entity.AuditSpTasktypeR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController("auditsptasktyperconfigaction")
@Scope("request")
public class AuditSpTasktypeRConfigAction extends BaseController {

    private static final long serialVersionUID = -5228373039953261567L;

    public TreeModel model;

    @Autowired
    public IAuditOrgaArea iauditorgaarea;

    @Autowired
    public IOuService iOuService;

    @Autowired
    private IHandleFrameOU frameOu;

    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private IHandleConfig config;

    @Autowired
    private IAuditSpTasktypeRService iAuditSpTasktypeRService;

    @Autowired
    private IAuditSpTasktypeService iAuditSpTasktypeService;

    @Autowired
    public IJnAuditTaskService iJnAuditTaskService;

    @Autowired
    private IConfigService configservice;
    @Autowired
    private IAuditTaskDelegate iaudittaskdelegate;

    private String itemcategory = "";
    private String businesstype = "";

    private String fields = "rowguid,task_id,item_id,type,taskname,ouguid,areacode";

    private AuditSpTasktype tasktype;

    // 记录辖区的数据
    private Map<String, Object> areacodeandname;
    // 记录辖区的数据
    private Map<String, Object> xzareacodeandname = new HashMap<>();

    private List<String> redlist;

    @Override
    public void pageLoad() {
        itemcategory = config.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
        String tasktypeguid = getRequestParameter("guid");
        tasktype = iAuditSpTasktypeService.find(tasktypeguid);
        if (tasktype == null) {
            addCallbackParam("msg", "未获取到事项分类！");
        }
        redlist = new ArrayList<>();
        areacodeandname = new HashMap<String, Object>();
    }

    public TreeModel getSelectTaskTreeModel() {
        String tabKey = getRequestParameter("tabKey");
        model = getTaskTreeModel();
        if (!isPostback()) {
            model.setSelectNode(getSelectTaskr());
        }
        return model;

    }

    public boolean hasChildren(List<AuditOrgaArea> arealist, String areacode) {
        for (AuditOrgaArea auditOrgaArea : arealist) {
            if (auditOrgaArea.getXiaqucode().indexOf(areacode) != -1
                    && !areacode.equals(auditOrgaArea.getXiaqucode())) {
                return true;
            }
        }
        return false;
    }

    public TreeModel getTaskTreeModel() {
        if (model == null) {
            model = new TreeModel() {
                private static final long serialVersionUID = 6835935337210088821L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();
                    List<AuditTask> listTask = new ArrayList<>();
                    List<AuditOrgaArea> listarea = new ArrayList<>();
                    List<FrameOu> listou = new ArrayList<>();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (treeNode != null) {
                        treeNode.setPid((String) treeNode.getColumns().get("pid"));
                    }
                    // 根节点
                    if (treeNode == null) {
                        // 获取所有部门
                        SqlConditionUtil conditionMap = new SqlConditionUtil();
                        String[] arealevel = new String[]{ZwfwConstant.AREA_TYPE_SJ, ZwfwConstant.AREA_TYPE_XQJ};
                        String arealevelstr = StringUtil.join(arealevel, "','");
                        conditionMap.in("citylevel", "'" + arealevelstr + "'");
                        listarea = iauditorgaarea.selectAuditAreaList(conditionMap.getMap()).getResult();
                    } else {
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            // 以下是框架封装的方法，具体参数请参见api
                            List<AuditTask> listsearchTask = new ArrayList<AuditTask>();
                            if (StringUtil.isNotBlank(itemcategory)
                                    && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                sql.clear();
                                sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.like("taskname", treeNode.getSearchCondition());
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                listsearchTask = iaudittask
                                        .getAuditEnableTaskPageData(fields, sql.getMap(), 0, 0, "ordernum", "desc")
                                        .getResult().getList();

                            } else {
                                sql.clear();
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.like("taskname", treeNode.getSearchCondition());
                                listsearchTask = iaudittask.getAllTask(sql.getMap()).getResult();
                            }
                            // 将辖区数据转换成map
                            if (StringUtil.isNotBlank(getViewData("areacodeandname"))) {
                                areacodeandname = JsonUtil.jsonToMap(getViewData("areacodeandname"));
                            }
                            ;
                            // 搜索完成后，进行树的绑定
                            for (int j = 0; j < listsearchTask.size(); j++) {

                                FrameOuExtendInfo extendInfo = iOuService
                                        .getFrameOuExtendInfo(listsearchTask.get(j).getOuguid());

                                if (extendInfo == null) {
                                    continue;
//                                    if (extendInfo.getStr("areacode").length() != 6) {
//                                        continue;
//                                    }
                                }
                                TreeNode node = new TreeNode();
                                node.setId(listsearchTask.get(j).getTask_id());
                                if (redlist.contains(listsearchTask.get(j).getTask_id())) {
                                    node.getColumns().put("isred", "1");
                                }
                                if (StringUtil.isNotBlank(listsearchTask.get(j).getYw_catalog_id())
                                        && "1".equals(configservice.getFrameConfigValue("IS_CHECKNEWTASK"))) {
                                    node.setText("【新】" + "(" + areacodeandname.get(listsearchTask.get(j).getAreacode())
                                            + ")" + listsearchTask.get(j).getTaskname().replace(",", "，"));
                                } else {
                                    AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(extendInfo.getStr("areacode")).getResult();
                                    if (area != null) {
                                        node.setText("(" + area.getXiaquname() + ")"
                                                + listsearchTask.get(j).getTaskname().replace(",", "，"));
                                    } else {
                                        node.setText("(" + areacodeandname.get(listsearchTask.get(j).getAreacode()) + ")"
                                                + listsearchTask.get(j).getTaskname().replace(",", "，"));
                                    }

                                }
                                node.setPid("-1");
                                node.setLeaf(true);
                                list.add(node);
                            }
                        } else {
                            // 设置父节点数据
                            String type = (String) treeNode.getColumns().get("type");
                            if ("area".equals(type)) {
                                listou = frameOu.getWindowOUList(treeNode.getId(), true).getResult();
                            } else if ("ou".equals(type)) {
                                if (StringUtil.isNotBlank(itemcategory)
                                        && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                    sql.clear();
                                    sql.eq("OUGUID", treeNode.getId());
//                                    sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                    sql.eq("areacode", treeNode.getPid());
//                                    sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
//                                    sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
//                                    sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
//                                    sql.rightLike("item_id", "000");

                                    listTask = iJnAuditTaskService
                                            .getUseTaskList(fields, sql.getMap(), 0, 0, "ordernum", "desc").getList();
                                } else {
                                    listTask = iaudittask
                                            .selectAuditTaskOuByObjectGuid(treeNode.getId(), treeNode.getPid())
                                            .getResult();
                                }
                            } else {
                                sql.clear();
                                String itemleft;
                                String item_id = (String) treeNode.getColumns().get("item_id");
                                if (item_id != null && item_id.length() > 3) {
//                                    itemleft = item_id.substring(0, item_id.length() - 3);
//                                    sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                    sql.eq("areacode", (String) treeNode.getColumns().get("areacode"));
//                                    sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
//                                    sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
//                                    sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
//                                    sql.leftLike("item_id", itemleft);
                                    sql.nq("item_id", item_id);
                                    listTask = iJnAuditTaskService
                                            .getUseTaskList(fields, sql.getMap(), 0, 0, "ordernum", "desc").getList();
                                }

                            }

                        }
                    }
                    for (FrameOu frameOu : listou) {
                        TreeNode node = new TreeNode();
                        node.setId(frameOu.getOuguid());
                        node.setText(frameOu.getOuname());
                        node.setPid(treeNode.getId());
                        node.setCkr(false);
                        node.getColumns().put("type", "ou");// 标记：是部门节点
                        node.setLeaf(false);
                        int taskCount = 0;
                        if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                            sql.clear();
                            sql.eq("OUGUID", frameOu.getOuguid());
//                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                            sql.eq("areacode", treeNode.getId());
//                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
//                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
//                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
//                            sql.rightLike("item_id", "000");

                            taskCount = iJnAuditTaskService
                                    .getUseTaskList(fields, sql.getMap(), 0, 0, "ordernum", "desc").getList().size();
                        } else {
                            taskCount = iaudittask.selectAuditTaskOuByObjectGuid(frameOu.getOuguid(), treeNode.getId())
                                    .getResult().size();
                        }
                        if (taskCount > 0) {
                            list.add(node);
                        }

                    }

                    for (AuditOrgaArea auditorgaarea : listarea) {
                        TreeNode node = new TreeNode();
                        node.setId(auditorgaarea.getXiaqucode());
                        node.setText(auditorgaarea.getXiaquname());
                        node.setPid("-1");
                        node.setCkr(false);
                        node.getColumns().put("type", "area");// 标记：是部门节点
                        node.setLeaf(false);
                        list.add(node);
                        areacodeandname.put(auditorgaarea.getXiaqucode(), auditorgaarea.getXiaquname());
                    }
                    // 记录辖区数据
                    if (StringUtil.isBlank(getViewData("areacodeandname")) && areacodeandname.size() > 0) {
                        addViewData("areacodeandname", JSON.toJSON(areacodeandname).toString());
                    }
                    // 将辖区数据转换成map
                    if (StringUtil.isNotBlank(getViewData("areacodeandname"))) {
                        areacodeandname = JsonUtil.jsonToMap(getViewData("areacodeandname"));
                    }
                    ;

                    String itemleft;
                    Integer taskCount = 0;
                    for (AuditTask audittask : listTask) {
                        TreeNode node2 = new TreeNode();
                        node2.setId(audittask.getTask_id());
                        if (redlist.contains(audittask.getTask_id())) {
                            node2.getColumns().put("isred", "1");
                        }
                        node2.setText(audittask.getTaskname().replace(",", "，"));
                        node2.setPid(treeNode.getId());
                        node2.getColumns().put("type", "task");// 标记：不是部门节点
                        node2.getColumns().put("areacode", audittask.getAreacode());
                        node2.getColumns().put("item_id", audittask.getItem_id());
                        node2.setLeaf(true);
                        if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                            sql.clear();
//                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                            sql.eq("areacode", audittask.getAreacode());
//                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
//                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
//                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
//                            itemleft = audittask.getItem_id();
//                            itemleft = itemleft.substring(0, itemleft.length() - 3);
                            sql.nq("item_id", audittask.getItem_id());
//                            sql.leftLike("item_id", itemleft);
                            if (audittask.getItem_id().endsWith("000")) {
                                taskCount = iJnAuditTaskService
                                        .getUseTaskList(fields, sql.getMap(), 0, 0, "ordernum", "desc").getList()
                                        .size();
                            } else {
                                taskCount = 0;
                            }
                            if (taskCount > 0) {
                                node2.setLeaf(false);
                            }
                            node2.setText("(" + audittask.getItem_id().substring(audittask.getItem_id().length() - 3)
                                    + ")" + audittask.getTaskname().replace(",", "，"));
                        }
                        if (StringUtil.isNotBlank(audittask.getYw_catalog_id())
                                && "1".equals(configservice.getFrameConfigValue("IS_CHECKNEWTASK"))) {
                            node2.setText(
                                    "【新】" + "(" + areacodeandname.get(audittask.getAreacode()) + ")" + node2.getText());
                        } else {

                            FrameOuExtendInfo extendInfo = iOuService
                                    .getFrameOuExtendInfo(audittask.getOuguid());

                            if (extendInfo == null) {
                                continue;
                            }


                            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(extendInfo.getStr("areacode")).getResult();
                            if (area != null) {
                                node2.setText("(" + area.getXiaquname() + ")" + node2.getText());
                            } else {
                                node2.setText("(" + areacodeandname.get(audittask.getAreacode()) + ")" + node2.getText());
                            }


                        }
                        list.add(node2);

                    }
                    return list;
                }
            };
        }
        if (!isPostback()) {
            model.setSelectNode(getSelectTaskr());
        }
        return model;
    }

    public List<SelectItem> getSelectTaskr() {
        List<SelectItem> list = new ArrayList<>();
        com.epoint.core.utils.sql.SqlConditionUtil sql = new com.epoint.core.utils.sql.SqlConditionUtil();
        sql.eq("tasktypeguid", tasktype.getRowguid());
        List<AuditSpTasktypeR> taskrlist = iAuditSpTasktypeRService.findListByCondition(sql.getMap());
        for (AuditSpTasktypeR auditSpTasktypeR : taskrlist) {
            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(auditSpTasktypeR.getTaskid()).getResult();
            if (audittask != null) {
                FrameOuExtendInfo extendInfo = iOuService
                        .getFrameOuExtendInfo(audittask.getOuguid());

                if (extendInfo == null) {
                    continue;
                }

                AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(extendInfo.getStr("areacode")).getResult();
                if (area != null) {
                    list.add(new SelectItem(audittask.getTask_id(),
                            "(" + area.getXiaquname() + ")" + audittask.getTaskname().replace(",", "，")));
                } else {
                    list.add(new SelectItem(audittask.getTask_id(),
                            audittask.getTaskname().replace(",", "，")));
                }
            }
        }
        return list;
    }

    public void savebasetaskr() {
        // 删除之前的关系重新关联事项
        iAuditSpTasktypeRService.delByTasktypeguid(tasktype.getRowguid());
        List<SelectItem> list = model.getSelectNode();
        SqlConditionUtil sql = new SqlConditionUtil();
        // 提示字符串
        StringBuilder msg = new StringBuilder();
        for (SelectItem selectItem : list) {
            // 查詢选中的事项不是已经被被关联
            String taskid = (String) selectItem.getValue();
            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(taskid).getResult();
            if (audittask == null) {
                addCallbackParam("msg", "你选择得事项不存在！");
                return;
            }
            String taskname = audittask.getTaskname();
            // 添加事项材料关系
            AuditSpTasktypeR auditSpTasktypeR = new AuditSpTasktypeR();
            auditSpTasktypeR.setRowguid(UUID.randomUUID().toString());
            auditSpTasktypeR.setOperatedate(new Date());
            auditSpTasktypeR.setOperateusername(UserSession.getInstance().getDisplayName());
            auditSpTasktypeR.setTasktypeguid(tasktype.getRowguid());
            auditSpTasktypeR.setTaskid(taskid);
            auditSpTasktypeR.setTaskname(taskname);
            FrameOuExtendInfo extendInfo = iOuService
                    .getFrameOuExtendInfo(audittask.getOuguid());

            if (extendInfo != null) {
                AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(extendInfo.getStr("areacode")).getResult();
                if (area != null) {
                    auditSpTasktypeR.setXiaquname(area.getXiaquname());
                }
            }

            iAuditSpTasktypeRService.insert(auditSpTasktypeR);
        }
        if (msg.length() == 0) {
            addCallbackParam("msg", "保存成功！");
        } else {
            addCallbackParam("msg", msg.toString());
        }

    }

}
