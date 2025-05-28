package com.epoint.auditorga.auditwindow.action;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 窗口事项配置页面对应的后台
 * 
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("auditwindowtasktypeconfigaction")
@Scope("request")
public class AuditWindowTaskTypeConfigAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4632640761998959244L;
    @Autowired
    private IAuditTask auditTaskBasicImpl;
    @Autowired
    private IHandleFrameOU frameOu;
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;
    
    @Autowired
    private IAuditQueueTasktype tasktypeservice;
    
    @Autowired
    private IAuditQueueTasktypeTask tasktypetservice;
    
    @Autowired
    private IAuditOrgaServiceCenter  centerservice;
    
    @Autowired
    private IAuditQueueWindowTasktype  windowtasktypeservice;
    
    /**
     * 窗口与事项的关联实体对象
     */
    private AuditOrgaWindowTask dataBean = null;

    
    private TreeModel treeModel1 = null;
    /**
     * 窗口标识
     */
    private String windowGuid = null;


    
    
    private String centerguid;
    
    private String areacode;
    

    @Override
    public void pageLoad() {
        windowGuid = getRequestParameter("windowguid");
        centerguid = getRequestParameter("guid");
        if(StringUtil.isBlank(centerguid)){
            centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        }
        AuditOrgaServiceCenter center = centerservice.findAuditServiceCenterByGuid(centerguid).getResult();
        if(center!=null){
            areacode = center.getBelongxiaqu();
        }
        log.info("登录人员："+userSession.getDisplayName());


    }

    
    public TreeModel getTreeModel() {
        if (treeModel1 == null) {
            treeModel1 = new TreeModel()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 7262093133578183235L;
                /***
                 * 加载树，懒加载
                 */
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    List<AuditQueueTasktype> listTasktype = new ArrayList<>();
                    List<FrameOu> listOu = new ArrayList<>();
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("root");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "true");//标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }
                    else {
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            //以下是框架封装的方法，具体参数请参见api
                            SqlConditionUtil sqlc = new SqlConditionUtil();
                            sqlc.eq("CenterGuid", centerguid);
                            sqlc.like("tasktypename", treeNode.getSearchCondition());
                            listTasktype = tasktypeservice.getAllTasktype(sqlc.getMap()).getResult();
                            //搜索完成后，进行树的绑定
                            for (int j = 0; j < listTasktype.size(); j++) {
                                TreeNode node = new TreeNode();
                                node.setId(listTasktype.get(j).getRowguid());
                                node.setText(listTasktype.get(j).getTasktypename().replace(",","，"));
                                node.setPid("-1");
                                node.setLeaf(true);
                                list.add(node);
                            }
                        }
                        else {
                            String objectGuid = treeData.getObjectGuid();
                            if("root".equals(objectGuid)){
                                listOu = frameOu.getWindowOUList(areacode,true).getResult();
                            }else{
                                SqlConditionUtil sqlc = new SqlConditionUtil();
                                sqlc.eq("CenterGuid", centerguid);
                                sqlc.eq("OUGuid", objectGuid);
                                listTasktype = tasktypeservice.getAllTasktype(sqlc.getMap()).getResult();
                            }

                            // 部门绑定
                            for (int i = 0; i < listOu.size(); i++) {
                                TreeNode node = new TreeNode();
                                node.setId(listOu.get(i).getOuguid());
                                node.setText(listOu.get(i).getOuname().replace(",","，"));   
                                if ("root".equals(objectGuid)) {
                                    node.setPid("root");
                                }
                                node.setCkr(true);
                                node.getColumns().put("isOU", "true");//标记：是部门节点
                                node.setLeaf(false);
                                Integer num =  tasktypeservice.getCountbyOUGuid(listOu.get(i).getOuguid(),centerguid).getResult();
                                if(num>0){
                                    list.add(node);
                                    list.addAll(fetch(node));
                                }
                            }
                            //事项的绑定
                            for (int j = 0; j < listTasktype.size(); j++) {
                                TreeNode node2 = new TreeNode();
                                node2.setId(listTasktype.get(j).getRowguid());
                                node2.setText(listTasktype.get(j).getTasktypename().replace(",","，"));
                                node2.setPid(treeNode.getId());
                                node2.getColumns().put("isOU", "false");//标记：不是部门节点
                                node2.setLeaf(true);
                                list.add(node2);
                            }
                        }
                    }
                    return list;
                }
                /***
                 * 懒加载进行获取数据，把左边树中选择的内容加载到右边
                 */
                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                    //获取到tree原有的select
                    List<SelectItem> selectedItems = treeModel1.getSelectNode();
                    List<AuditQueueTasktype> listTasktype = new ArrayList<>();
                    //查询选中的节点
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("CenterGuid", centerguid);
                    sqlc.eq("OUGuid", treeNode.getId());
                    listTasktype = tasktypeservice.getAllTasktype(sqlc.getMap()).getResult();
                    for (AuditQueueTasktype auditQueueTasktype : listTasktype) {
                        for (int j = 0; j < selectedItems.size(); j++) {
                            if (auditQueueTasktype.getRowguid().equals(selectedItems.get(j).getValue())) {
                                selectedItems.remove(j);
                            }
                        }    
                    }
                    //复选框选中
                    if (treeNode.isChecked() == true) {
                        for (AuditQueueTasktype auditQueueTasktype : listTasktype) {
                            selectedItems.add(  new SelectItem(auditQueueTasktype.getRowguid(),auditQueueTasktype.getTasktypename().replace(",","，")));
                        }
                    }
                    return selectedItems;
                }
            };
        }
        if(!isPostback()){
            treeModel1.setSelectNode(getSelectNode());
        }
        return treeModel1;
    }

    public List<SelectItem> getSelectNode(){
        SqlConditionUtil sqlc = new SqlConditionUtil();
        List<SelectItem> selectitems = new ArrayList<SelectItem>();
        sqlc.eq("windowguid", windowGuid);
        List<AuditQueueWindowTasktype> list = windowtasktypeservice.getAllWindowTasktype(sqlc.getMap()).getResult();
        for (AuditQueueWindowTasktype auditQueueWindowTasktype : list) {
            AuditQueueTasktype tasktype = tasktypeservice.getAuditQueueTasktypeByRowguid(auditQueueWindowTasktype.getTasktypeguid()).getResult();
            if(tasktype!=null){
                selectitems.add(new SelectItem(auditQueueWindowTasktype.getTasktypeguid(), tasktype.getTasktypename().replaceAll(",","，")));                
            }
        }
        return selectitems;
    }


    /**
     * 
     * 把事项和窗口信息保存到关系表里面
     * 
     * @param guidList
     *            前台获取的事项guid 用“;”分割
     * @param windowGuid
     *            窗口guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveTaskToWindow() {
       //获取选中的事项类别
       List<SelectItem> list =  treeModel1.getSelectNode();
       List<String> taskids;
       //刪除关联关系
       windowtasktypeservice.deletebyWindowguid(windowGuid);
       AuditOrgaWindow auditOrgaWindow = auditWindowImpl.getWindowByWindowGuid(windowGuid)
               .getResult();
       //建立关联关系
       for (SelectItem selectItem : list) {
           AuditQueueWindowTasktype wtasktype = new  AuditQueueWindowTasktype();
           wtasktype.setRowguid(UUID.randomUUID().toString());
           wtasktype.setBelongxiaqucode(areacode);
           wtasktype.setTasktypeguid((String) selectItem.getValue());
           wtasktype.setWindowguid(windowGuid);
           windowtasktypeservice.insertWindowtasktype(wtasktype);
           
           AuditTask task;
           //查询事项类别关联的事项
           taskids = tasktypetservice.getTaskIDbyTaskTypeGuid((String) selectItem.getValue());
           
           for (String string : taskids) {
               task = auditTaskBasicImpl.selectUsableTaskByTaskID(string).getResult();
               //获取数据提添加是事项到窗口
               if(task!=null){
                   log.info("删除窗口："+auditOrgaWindow.getWindowname()+",删除事项："+task.getTaskname()+"，操作人："+userSession.getDisplayName()+",删除id："+string);
                   //删除重复事项
                   auditWindowImpl.deleteWindowTaskByWindowGuidAndTaskId(windowGuid, string);
                   AuditOrgaWindowTask auditWindowTask = new AuditOrgaWindowTask();
                   auditWindowTask.setRowguid(UUID.randomUUID().toString());
                   auditWindowTask.setWindowguid(windowGuid);
                   auditWindowTask.setOperatedate(new Date());
                   auditWindowTask.setTaskguid(task.getRowguid());
                   auditWindowTask.setOrdernum(0);
                   auditWindowTask.setTaskid(string);
                   auditWindowTask.setEnabled("1");// 插入的数据默认为有效
                   auditWindowImpl.insertWindowTask(auditWindowTask); 
               }
           }
       }
       addCallbackParam("msg", "保存成功！");
    }


    public String getWindowGuid() {
        return windowGuid;
    }

    public void setWindowGuid(String windowGuid) {
        this.windowGuid = windowGuid;
    }

    public AuditOrgaWindowTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditOrgaWindowTask dataBean) {
        this.dataBean = dataBean;
    }

   

}
