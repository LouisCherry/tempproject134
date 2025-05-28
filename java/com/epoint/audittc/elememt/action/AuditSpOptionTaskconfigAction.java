package com.epoint.audittc.elememt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.epoint.auditspoptiontownship.api.IAuditSpOptiontownshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;

@RestController("auditspoptiontaskconfigaction")
@Scope("request")
public class AuditSpOptionTaskconfigAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 3065709190052887109L;

    @Autowired
    private IAuditSpTask iauditsptask;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;

    @Autowired
    private IAuditSpBasetask iauditspbasetask;

    @Autowired
    private IAuditTaskDelegate iaudittaskdelegate;

    @Autowired
    private IAuditSpOptiontownshipService iauditspoptiontownshipService;

    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    private String guid;

    private TreeModel treeModel = null;

    public List<String> sjcode;

    @Override
    public void pageLoad() {
        guid = getRequestParameter("guid");
        // 查询所有市级的areacode，和当前辖区
        sjcode = iauditorgaarea.getAllAreacodeByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
    }

    public TreeModel getTaskTreeModel() {
        if (treeModel == null) {
            String tabKey = getRequestParameter("tabKey");
            // 所有事项
            if (StringUtil.isBlank(tabKey) || "allTask".equals(tabKey)) {
                treeModel = new TreeModel()
                {
                    private static final long serialVersionUID = -8073287689498990143L;

                    @Override
                    public List<TreeNode> fetch(TreeNode treenode) {
                        // 第一层加载所有事项
                        List<AuditSpBasetask> listsptask = new ArrayList<AuditSpBasetask>();
                        List<AuditSpBasetaskR> listsptaskr = new ArrayList<AuditSpBasetaskR>();
                        List<TreeNode> listtreenode = new ArrayList<TreeNode>();
                        if (treenode == null) {
                            listsptask = iauditsptask.selectAuditSpBaseTasksByBusinessguid(guid).getResult();
                            List<String> list = new ArrayList<>();
                            for (AuditSpBasetask auditSpBasetask : listsptask) {
                                list.add(auditSpBasetask.getRowguid());
                            }
                            SqlConditionUtil sqlc = new SqlConditionUtil();
                            if (StringUtil.isNotBlank(StringUtil.joinSql(list))) {
                                sqlc.in("basetaskguid", StringUtil.joinSql(list));
                            }
                            else {
                                sqlc.in("basetaskguid", "''");
                            }
                            listsptaskr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                        }
//                        listsptaskr = listsptaskr.stream().filter(a->sjcode.contains(a.getAreacode())).collect(Collectors.toList());
                        for (AuditSpBasetaskR auditSpBasetaskR : listsptaskr) {
                            // 查询在用事项
                            Record audittask = iaudittask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid())
                                    .getResult();
                            AuditSpBasetask basetask = iauditspbasetask
                                    .getAuditSpBasetaskByrowguid(auditSpBasetaskR.getBasetaskguid()).getResult();
                            if (audittask != null && !audittask.isEmpty()) {
                                TreeNode node = new TreeNode();
                                AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(auditSpBasetaskR.getAreacode())
                                        .getResult();
                                String id = area.getXiaqucode() + "_" + auditSpBasetaskR.getTaskid();
                                node.setText("(" + area.getXiaquname() + ")" + audittask.getStr("taskname"));
                                node.setId(id);
                                node.setPid(basetask.getTaskcode());
                                node.getColumns().put("isbasetask", "false");// 标记：不是部门节点
                                node.setCkr(true);
                                node.setLeaf(true);
                                listtreenode.add(node);
                                // 添加下放到乡镇的事项节点audittask.get("task_id")
                                if (area.getXiaqucode().length() == 6) {
                                    SqlConditionUtil sqlc = new SqlConditionUtil();
                                    sqlc.eq("taskid", audittask.get("task_id"));
                                    sqlc.eq("isallowaccept", ZwfwConstant.CONSTANT_STR_ONE);
                                    List<AuditTaskDelegate> delegateList = iaudittaskdelegate
                                            .selectDelegateByTaskID(audittask.get("task_id")).getResult();
                                    if (ValidateUtil.isNotBlankCollection(delegateList)) {
                                        // 构建乡镇下放事项节点
                                        for (AuditTaskDelegate delegate : delegateList) {
                                            node = new TreeNode();
                                            AuditOrgaArea area2 = iauditorgaarea
                                                    .getAreaByAreacode(delegate.getAreacode()).getResult();
                                            if (area2 == null) {
                                            	continue;
                                            }
                                            node.setText(
                                                    "(" + area2.getXiaquname() + ")" + audittask.getStr("taskname"));
                                            node.setId(area2.getXiaqucode() + "_" + auditSpBasetaskR.getTaskid());
                                            node.setPid(basetask.getTaskcode());
                                            node.getColumns().put("isbasetask", "false");// 标记：不是部门节点
                                            node.setCkr(true);
                                            node.setLeaf(true);
                                            listtreenode.add(node);
                                        }
                                    }
                                }
                            }
                        }

                        /*   去除已选择事项
                        if (StringUtil.isNotBlank(getViewData("allselectedtask"))) {
                            List<String> selects = JsonUtil.jsonToList(getViewData("allselectedtask"), String.class);
                            listtreenode.removeIf(a -> {
                                return selects.contains(a.getId());
                            });
                        }*/
                        List<String> codelist = listtreenode.stream().map(TreeNode::getPid)
                                .collect(Collectors.toList());

                        // 设置目录清单节点
                        for (AuditSpBasetask auditSpBasetask : listsptask) {
                            TreeNode root = new TreeNode();
                            root.setText(auditSpBasetask.getTaskname());
                            root.setId(auditSpBasetask.getTaskcode());
                            root.setPid("-1");
                            root.getColumns().put("isbasetask", "true");// 标记：不是部门节点
                            root.setCkr(false);
                            root.setExpanded(true);// 展开下一层节点
                            // 过滤掉没有子节点的数据
                            if (codelist.contains(auditSpBasetask.getTaskcode())) {
                                listtreenode.add(root);
                            }
                        }
                        return listtreenode;
                    }
                };
            }
            // 所有辖区
            else {
                treeModel = new TreeModel()
                {
                    private static final long serialVersionUID = 2774392686752751558L;

                    @Override
                    public List<TreeNode> fetch(TreeNode treenode) {
                        TreeData treeData = TreeFunction9.getData(treenode);
                        // 第一层加载所有事项
                        List<AuditSpBasetask> listsptask = new ArrayList<AuditSpBasetask>();
                        List<AuditSpBasetaskR> listsptaskr = new ArrayList<AuditSpBasetaskR>();
                        List<AuditTaskDelegate> listtaskdelegate = new ArrayList<AuditTaskDelegate>();

                        List<TreeNode> listtreenode = new ArrayList<TreeNode>();
                        // 存放树节点id，防止数据重复
                        if (treeData == null) {
                            listsptask = iauditsptask.selectAuditSpBaseTasksByBusinessguid(guid).getResult();
                            List<String> basetaskguidlist = new ArrayList<>();
                            for (AuditSpBasetask auditSpBasetask : listsptask) {
                                basetaskguidlist.add(auditSpBasetask.getRowguid());
                            }
                            SqlConditionUtil sqlc = new SqlConditionUtil();
                            if (StringUtil.isNotBlank(StringUtil.joinSql(basetaskguidlist))) {
                                sqlc.in("basetaskguid", StringUtil.joinSql(basetaskguidlist));
                            }
                            else {
                                sqlc.in("basetaskguid", "''");
                            }
                            listsptaskr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                        }
                        List<String> taskids = listsptaskr.stream().map(AuditSpBasetaskR::getTaskid)
                                .collect(Collectors.toList());
                        if (ValidateUtil.isNotBlankCollection(taskids)) {
                            SqlConditionUtil sqlc = new SqlConditionUtil();
                            sqlc.in("taskid", StringUtil.joinSql(taskids));
                            sqlc.eq("isallowaccept", ZwfwConstant.CONSTANT_STR_ONE);
                            listtaskdelegate = iaudittaskdelegate.getDelegateListByCondition(sqlc.getMap());
                        }

                        /*    去除已选择事项
                                            if (StringUtil.isNotBlank(getViewData("allselectedtask"))) {
                            List<String> selects = JsonUtil.jsonToList(getViewData("allselectedtask"), String.class);
                            listsptaskr.removeIf(a -> {
                                return selects.contains(a.getAreacode() + "_" + a.getTaskid());
                            });
                            listtaskdelegate.removeIf(a -> {
                                return selects.contains(a.getAreacode() + "_" + a.getTaskid());
                            });
                        }*/

                        List<AuditOrgaArea> areaList = iauditorgaarea.selectAuditAreaList(null).getResult();
                        Map<String, String> areamap = areaList.stream()
                                .collect(Collectors.toMap(AuditOrgaArea::getXiaqucode, AuditOrgaArea::getXiaquname));
                        Map<String, AuditSpBasetask> basetaskmap = listsptask.stream().collect(
                                Collectors.toMap(AuditSpBasetask::getRowguid, AuditSpBasetask -> AuditSpBasetask));

                        // 获取所有标准事项的辖区和下放乡镇的辖区
                        List<String> areas = Stream
                                .concat(listsptaskr.stream().map(AuditSpBasetaskR::getAreacode),
                                        listtaskdelegate.stream().map(AuditTaskDelegate::getAreacode))
                                .distinct().collect(Collectors.toList());
                        Map<String, AuditTask> taskmap = new HashMap<>();

                        for (String areacode : areas) {
                            TreeNode root = new TreeNode();
                            String areaname = areamap.get(areacode);
                            root.setText(areaname);
                            root.setId(areacode);
                            root.setPid(areacode.length() == 6 ? "-1" : areacode.substring(0, 6));
                            root.setCkr(false);
                            root.setLeaf(false);
                            root.setExpanded(areacode.length() == 6);// 展开下一层节点
                            listtreenode.add(root);
                            // 如果是是市级辖区从basetask中获取办理事项
                            if (areacode.length() == 6) {
                                List<AuditSpBasetaskR> areabasetaskr = listsptaskr.stream()
                                        .filter(a -> a.getAreacode().equals(areacode)).collect(Collectors.toList());
                                List<String> basetasks = areabasetaskr.stream().map(AuditSpBasetaskR::getBasetaskguid)
                                        .collect(Collectors.toList());
                                for (String basetaskguid : basetasks) {
                                    AuditSpBasetask auditspbasetask = basetaskmap.get(basetaskguid);
                                    TreeNode node = new TreeNode();
                                    node.setText(auditspbasetask.getTaskname());
                                    node.setId(areacode + auditspbasetask.getTaskcode());
                                    node.setPid(areacode);
                                    node.getColumns().put("isbasetask", "true");// 标记：不是部门节点
                                    node.setCkr(false);
                                    node.setLeaf(false);
                                    listtreenode.add(node);
                                }
                                for (AuditSpBasetaskR auditspbasetaskr : areabasetaskr) {
                                    AuditTask audittask = iaudittask
                                            .selectUsableTaskByTaskID(auditspbasetaskr.getTaskid()).getResult();
                                    if (audittask == null) {
                                        continue;
                                    }
                                    taskmap.put(audittask.getTask_id(), audittask);

                                    AuditSpBasetask auditspbasetask = basetaskmap
                                            .get(auditspbasetaskr.getBasetaskguid());
                                    TreeNode node = new TreeNode();
                                    node.setText("(" + areaname + ")" + audittask.getStr("taskname"));
                                    String id = areacode + "_" + audittask.getTask_id();
                                    node.setId(id);
                                    node.setPid(areacode + auditspbasetask.getTaskcode());
                                    node.getColumns().put("isbasetask", "false");// 标记：不是部门节点
                                    node.setCkr(true);
                                    node.setLeaf(true);
                                    listtreenode.add(node);
                                }
                            }
                            else {
                                Map<String, AuditTaskDelegate> delegatemap = listtaskdelegate.stream()
                                        .filter(a -> a.getAreacode().equals(areacode)).collect(Collectors.toMap(
                                                AuditTaskDelegate::getTaskid, AuditTaskDelegate -> AuditTaskDelegate));

                                List<String> basetasklist = listsptaskr.stream()
                                        .filter(a -> delegatemap.keySet().contains(a.getTaskid()))
                                        .map(AuditSpBasetaskR::getBasetaskguid).distinct().collect(Collectors.toList());

                                for (String basetaskguid : basetasklist) {
                                    AuditSpBasetask auditspbasetask = basetaskmap.get(basetaskguid);
                                    TreeNode node = new TreeNode();
                                    node.setText(auditspbasetask.getTaskname());
                                    node.setId(areacode + auditspbasetask.getTaskcode());
                                    node.setPid(areacode);
                                    node.getColumns().put("isbasetask", "true");// 标记：不是部门节点
                                    node.setCkr(false);
                                    node.setLeaf(false);
                                    listtreenode.add(node);
                                }
                                for (Entry<String, AuditTaskDelegate> entry : delegatemap.entrySet()) {
                                    AuditTaskDelegate audittaskdelegate = entry.getValue();
                                    List<AuditSpBasetaskR> spbasetaskrlist = listsptaskr.stream()
                                            .filter(a -> a.getTaskid().equals(audittaskdelegate.getTaskid()))
                                            .collect(Collectors.toList());
                                    if (ValidateUtil.isNotBlankCollection(spbasetaskrlist)) {
                                        AuditSpBasetaskR auditspbasetaskr = spbasetaskrlist.get(0);
                                        AuditSpBasetask auditspbasetask = basetaskmap
                                                .get(auditspbasetaskr.getBasetaskguid());
                                        TreeNode node = new TreeNode();
                                        node.setText("(" + areaname + ")" + auditspbasetaskr.getStr("taskname"));
                                        String id = areacode + "_" + auditspbasetaskr.getTaskid();
                                        node.setId(id);
                                        node.setPid(areacode + auditspbasetask.getTaskcode());
                                        node.getColumns().put("isbasetask", "false");// 标记：不是部门节点
                                        node.setCkr(true);
                                        node.setLeaf(true);
                                        listtreenode.add(node);
                                    }

                                }

                            }
                        }

                        return listtreenode;
                    }
                };
            }
        }
        return treeModel;
    }

    public void sethidetask(String allselectedtask) {
        if (JsonUtil.isJSONValid(allselectedtask)) {
            addViewData("allselectedtask", allselectedtask);
        }
    }
}
