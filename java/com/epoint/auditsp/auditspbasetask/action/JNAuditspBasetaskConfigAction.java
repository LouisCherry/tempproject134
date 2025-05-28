package com.epoint.auditsp.auditspbasetask.action;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController("jnauditspbasetaskconfigaction")
@Scope("request")
public class JNAuditspBasetaskConfigAction extends BaseController {

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
    private IAuditSpBasetaskR iauditspbasetaskr;

    @Autowired
    private IAuditSpBasetask iauditspbasetask;

    @Autowired
    public IJnAuditTaskService iJnAuditTaskService;

    @Autowired
    private IConfigService configservice;
    @Autowired
    private IAuditTaskDelegate iaudittaskdelegate;

    private String itemcategory = "";
    private String businesstype = "";

    private String fields = "rowguid,task_id,item_id,type,taskname,ouguid,areacode";

    private AuditSpBasetask basetask;

    // 记录辖区的数据
    private Map<String, Object> areacodeandname;
    // 记录辖区的数据
    private Map<String, Object> xzareacodeandname = new HashMap<>();

    private List<String> redlist;

    @Override
    public void pageLoad() {
        businesstype = getRequestParameter("businesstype");
        itemcategory = config.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
        String basetaskguid = getRequestParameter("guid");
        basetask = iauditspbasetask.getAuditSpBasetaskByrowguid(basetaskguid).getResult();
        if (basetask == null) {
            addCallbackParam("msg", "未获取到审批事项数据！");
        }
        areacodeandname = new HashMap<String, Object>();
        redlist = iauditspbasetaskr.getAlreadyUsedTaskid(businesstype);
        // 过滤当前标准事项数据
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("basetaskguid", basetask.getRowguid());
        List<AuditSpBasetaskR> taskrlist = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
        redlist.removeIf(a -> {
            for (AuditSpBasetaskR auditSpBasetaskR : taskrlist) {
                if (auditSpBasetaskR.getTaskid().equals(a)) {
                    return true;
                }
            }
            return false;
        });
        addCallbackParam("redlist", redlist);
    }

    public TreeModel getSelectTaskTreeModel() {
        String tabKey = getRequestParameter("tabKey");
        if ("xz".equals(tabKey)) {
            model = getXzTreeModel();
        } else {
            model = getTaskTreeModel();
        }
        if (!isPostback()) {
            model.setSelectNode(getSelectTaskr());
        }
        return model;

    }

    public TreeModel getXzTreeModel() {
        if (model == null) {
            model = new TreeModel() {
                private static final long serialVersionUID = 6835935337210088821L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();
                    List<AuditTask> listTask = new ArrayList<>();
                    List<AuditOrgaArea> listarea = new ArrayList<>();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 根节点
                    if (treeNode == null) {
                        // 获取所有部门
                        SqlConditionUtil conditionMap = new SqlConditionUtil();
                        String[] arealevel = new String[]{ZwfwConstant.AREA_TYPE_SJ, ZwfwConstant.AREA_TYPE_XQJ,
                                ZwfwConstant.AREA_TYPE_XZJ};
                        String arealevelstr = StringUtil.join(arealevel, "','");
                        conditionMap.in("citylevel", "'" + arealevelstr + "'");
                        listarea = iauditorgaarea.selectAuditAreaList(conditionMap.getMap()).getResult();
                    } else {
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            // 以下是框架封装的方法，具体参数请参见api
                            List<AuditTask> listsearchTask = new ArrayList<AuditTask>();
                            // 将辖区数据转换成map
                            if (StringUtil.isNotBlank(getViewData("xzareacodeandname"))) {
                                xzareacodeandname = JsonUtil.jsonToMap(getViewData("xzareacodeandname"));
                            } else {
                                SqlConditionUtil conditionMap = new SqlConditionUtil();
                                String[] arealevel = new String[]{ZwfwConstant.AREA_TYPE_SJ,
                                        ZwfwConstant.AREA_TYPE_XQJ, ZwfwConstant.AREA_TYPE_XZJ};
                                String arealevelstr = StringUtil.join(arealevel, "','");
                                conditionMap.in("citylevel", "'" + arealevelstr + "'");
                                List<AuditOrgaArea> listareaAll = iauditorgaarea
                                        .selectAuditAreaList(conditionMap.getMap()).getResult();
                                for (AuditOrgaArea auditorgaarea : listareaAll) {
                                    xzareacodeandname.put(auditorgaarea.getXiaqucode(), auditorgaarea.getXiaquname());
                                }
                                addViewData("xzareacodeandname", JSON.toJSON(xzareacodeandname).toString());
                            }
                            ;
                            String xqareacode = ZwfwUserSession.getInstance().getAreaCode();
                            SqlConditionUtil sqlc = new SqlConditionUtil();
                            sqlc.eq("delegatetype", ZwfwConstant.TASKDELEGATE_TYPE_XZFD);
                            if (xqareacode.length() > 6) {
                                xqareacode = xqareacode.substring(0, 6);
                            }
                            sqlc.rightLike("areacode", xqareacode);
                            List<AuditTaskDelegate> delegatelist = iaudittaskdelegate
                                    .getDelegateListByCondition(sqlc.getMap());
                            List<String> taskids = delegatelist.stream().map(AuditTaskDelegate::getTaskid)
                                    .collect(Collectors.toList());
                            if (ValidateUtil.isNotBlankCollection(taskids)) {
                                sql.clear();
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.like("taskname", treeNode.getSearchCondition());
                                sql.in("task_id", StringUtil.joinSql(taskids));
                                listsearchTask = iaudittask.getAllTask(sql.getMap()).getResult();
                            }

                            // 搜索完成后，进行树的绑定
                            for (int j = 0; j < listsearchTask.size(); j++) {
                                List<AuditTaskDelegate> delegates = iaudittaskdelegate
                                        .selectDelegateByTaskID(listsearchTask.get(j).getTask_id()).getResult();
                                String areacode = "";
                                if (ValidateUtil.isNotBlankCollection(delegates)) {
                                    areacode = delegates.get(0).getAreacode();
                                }
                                TreeNode node = new TreeNode();
                                node.setId(listsearchTask.get(j).getTask_id());
                                if (redlist.contains(listsearchTask.get(j).getTask_id())) {
                                    node.getColumns().put("isred", "1");
                                }
                                node.setText("(" + xzareacodeandname.get(areacode) + ")"
                                        + listsearchTask.get(j).getTaskname().replace(",", "，"));
                                node.setPid("-1");
                                node.setLeaf(true);
                                list.add(node);
                            }
                        } else {
                            // 设置父节点数据
                            String type = (String) treeNode.getColumns().get("type");
                            if ("area".equals(type)) { // 市级不是懒加载，不会走逻辑，乡镇直接展示事项
                                // 查出乡镇法定事项
                                String xzareacode = treeNode.getId();
                                List<AuditTaskDelegate> delegatelist = iaudittaskdelegate
                                        .getDelegateListByAreacodeandType(xzareacode,
                                                ZwfwConstant.TASKDELEGATE_TYPE_XZFD);
                                List<String> taskids = delegatelist.stream().map(AuditTaskDelegate::getTaskid)
                                        .collect(Collectors.toList());
                                if (ValidateUtil.isNotBlankCollection(taskids)) {
                                    sql.clear();
                                    sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                    sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                    sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                    sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                    sql.in("task_id", StringUtil.joinSql(taskids));
                                    listTask = iaudittask.getAllTask(sql.getMap()).getResult();
                                }
                            }

                        }
                    }

                    for (AuditOrgaArea auditorgaarea : listarea) {
                        TreeNode node = new TreeNode();
                        node.setId(auditorgaarea.getXiaqucode());
                        node.setText(auditorgaarea.getXiaquname());
                        // 设置乡镇和市级的父子关系
                        if (ZwfwConstant.AREA_TYPE_SJ.equals(auditorgaarea.getCitylevel())
                                || ZwfwConstant.AREA_TYPE_XQJ.equals(auditorgaarea.getCitylevel())) {
                            node.setPid("-1");
                        } else {
                            String areacode = auditorgaarea.getXiaqucode();
                            node.setPid(areacode.substring(0, areacode.length() - 3));
                        }
                        node.setCkr(false);
                        node.getColumns().put("type", "area");// 标记：是部门节点
                        if (ZwfwConstant.AREA_TYPE_SJ.equals(auditorgaarea.getCitylevel())
                                || ZwfwConstant.AREA_TYPE_XQJ.equals(auditorgaarea.getCitylevel())) {
                            node.setLeaf(!hasChildren(listarea, auditorgaarea.getXiaqucode()));
                        } else {
                            // 判断是否有事项
                            node.setLeaf(false);
                        }
                        list.add(node);
                        xzareacodeandname.put(auditorgaarea.getXiaqucode(), auditorgaarea.getXiaquname());
                    }
                    // 记录辖区数据
                    if (StringUtil.isBlank(getViewData("xzareacodeandname")) && xzareacodeandname.size() > 0) {
                        addViewData("xzareacodeandname", JSON.toJSON(xzareacodeandname).toString());
                    }
                    // 将辖区数据转换成map
                    if (StringUtil.isNotBlank(getViewData("xzareacodeandname"))) {
                        xzareacodeandname = JsonUtil.jsonToMap(getViewData("xzareacodeandname"));
                    }
                    ;
                    if (treeNode != null) {
                        String xzareacode = treeNode.getId();
                        for (AuditTask audittask : listTask) {

                            TreeNode node2 = new TreeNode();
                            node2.setId(audittask.getTask_id());
                            if (redlist.contains(audittask.getTask_id())) {
                                node2.getColumns().put("isred", "1");
                            }
                            node2.setText("(" + xzareacodeandname.get(xzareacode) + ")"
                                    + audittask.getTaskname().replace(",", "，"));
                            node2.setPid(xzareacode);
                            node2.getColumns().put("type", "task");// 标记：不是部门节点
                            node2.getColumns().put("areacode", audittask.getAreacode());
                            node2.setLeaf(true);
                            list.add(node2);

                        }
                    }
                    return list;
                }
            };
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
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("basetaskguid", basetask.getRowguid());
        List<AuditSpBasetaskR> taskrlist = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
        for (AuditSpBasetaskR auditSpBasetaskR : taskrlist) {
            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
            if (audittask != null) {
                if (StringUtil.isNotBlank(audittask.getYw_catalog_id())
                        && "1".equals(configservice.getFrameConfigValue("IS_CHECKNEWTASK"))) {
                    list.add(new SelectItem(audittask.getTask_id(),
                            "【新】" + "(" + auditSpBasetaskR.getXiaquname() + ")" + audittask.getTaskname().replace(",", "，")));
                } else {

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
                                "(" + auditSpBasetaskR.getXiaquname() + ")" + audittask.getTaskname().replace(",", "，")));
                    }


                }
            }
        }
        return list;
    }

    public void savebasetaskr() {
        // 删除之前的关系重新关联事项
        iauditspbasetaskr.delByBasetaskguid(basetask.getRowguid());
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
            AuditSpBasetaskR basetaskr = new AuditSpBasetaskR();
            basetaskr.setRowguid(UUID.randomUUID().toString());
            basetaskr.setOperatedate(new Date());
            basetaskr.setOperateusername(UserSession.getInstance().getDisplayName());
            basetaskr.setBasetaskguid(basetask.getRowguid());
            basetaskr.setTaskid(taskid);
            basetaskr.setTaskname(taskname);
            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(audittask.getAreacode()).getResult();
            if (area != null) {
                basetaskr.setXiaquname(area.getXiaquname());
                basetaskr.setAreacode(area.getXiaqucode());
            }
            iauditspbasetaskr.addBasetaskR(basetaskr);
        }
        if (msg.length() == 0) {
            addCallbackParam("msg", "保存成功！");
        } else {
            addCallbackParam("msg", msg.toString());
        }

    }

}
