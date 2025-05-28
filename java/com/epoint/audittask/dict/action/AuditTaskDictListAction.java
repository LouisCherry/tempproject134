package com.epoint.audittask.dict.action;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.basic.audittask.dict.inter.IAuditTaskDict;
import com.epoint.basic.audittask.map.domain.AuditTaskMap;
import com.epoint.basic.audittask.map.inter.IAuditTaskMap;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

/**
 * 事项类型表list页面对应的后台
 * 
 * @author yjl
 * @version [版本号, 2017-03-24 11:51:11]
 */
@RestController("audittaskdictlistaction")
@Scope("request")
public class AuditTaskDictListAction extends BaseController
{
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 
     */
    private static final long serialVersionUID = 7739713556514702908L;
    
    @Autowired
    private IAuditTaskDict auditTaskDictImpl ;
    
    @Autowired
    private IAuditTaskMap auditTaskMapImpl ;

    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private ISendMQMessage sendMQMessageService;
    
    @Autowired
    private IHandleConfig handleConfigService;
    
    /**
     * 事项类别model
     */
    private TreeModel treeModel = null;
    /**
     * 事项guid
     */
    private String taskID = null;

    @Override
    public void pageLoad() {
        taskID = getRequestParameter("taskid");
        addCallbackParam("taskID", taskID);
    }

    /***
     * 
     * 用treebean构造事项类别树
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public TreeModel getTreeModel() {

        if (treeModel == null) {
            treeModel = loadAllDicts();
            if (!isPostback()) {
                treeModel.setSelectNode(getSelectTask(taskID));
            }
           
        }
        return treeModel;
    }

	/**
	 * 
	 * 把事项和类型信息保存到表中
	 * 
	 * @param guidList
	 *            前台获取的事项guid 用“;”分割
	 * @param windowGuid
	 *            窗口guid
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void saveTaskToMap(String guidList) {
		try {
			EpointFrameDsManager.begin(null);
			String result = saveTaskToWindow(guidList, taskID);
			EpointFrameDsManager.commit();
			AuditTask task = iAuditTask.selectUsableTaskByTaskID(taskID).getResult();
			if (task != null) {
				// 发送RabbitMQ Enable通知
				syncWindowTask("modify", task.getRowguid());
			}
			this.addCallbackParam("msg", result);
			this.addCallbackParam("message", "保存成功");
		} catch (Exception e) {
			EpointFrameDsManager.rollback();
			log.info("========Exception信息========" + e.getMessage());
		}
	}

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }
    

    public TreeModel loadAllDicts(){
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

                //首次加载树结构
                if (treeData == null) {
                    TreeNode root = new TreeNode();

                    root.setText("所有分类");
                    root.setId("");
                    root.setPid("-1");
                    list.add(root);
                    root.setExpanded(true);//展开下一层节点
                    root.setCkr(false);
                    list.addAll(fetch(root));//自动加载下一层树结构
                }
                else {
                    String objectGuid = treeData.getObjectGuid();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    List<AuditTaskDict> auditTaskDicts = null;
                    if(StringUtil.isNotBlank(treeNode.getSearchCondition())){
                        auditTaskDicts = auditTaskDictImpl.searchAuditTaskDictChild(treeNode.getSearchCondition()).getResult();
                        if(auditTaskDicts!=null && auditTaskDicts.size()>0){
                            for (AuditTaskDict auditTaskDict : auditTaskDicts) {
                                TreeNode node = new TreeNode();
                                node.setId(auditTaskDict.getRowguid());
                                node.setText(auditTaskDict.getClassname());
                                node.setPid(objectGuid);
                                sql = new SqlConditionUtil();
                                node.setLeaf(true);
                                list.add(node);
                            }
                        }
                    }else{
                        if(StringUtil.isBlank(objectGuid)){
                            sql.isBlank("parent_dict_id");
                            auditTaskDicts = auditTaskDictImpl.getAuditTaskDictList(sql.getMap()).getResult();                    
                        }else{
                            sql.eq("parent_dict_id", objectGuid);
                            auditTaskDicts = auditTaskDictImpl.getAuditTaskDictList(sql.getMap()).getResult(); 
                        }
                        if(auditTaskDicts!=null && auditTaskDicts.size()>0){
                            for (AuditTaskDict auditTaskDict : auditTaskDicts) {
                                TreeNode node = new TreeNode();

                                node.setId(auditTaskDict.getRowguid());
                                node.setText(auditTaskDict.getClassname());
                                node.setPid(objectGuid);
                                sql = new SqlConditionUtil();
                                sql.eq("parent_dict_id", auditTaskDict.getRowguid());
                                List<AuditTaskDict> lists = auditTaskDictImpl.getAuditTaskDictList(sql.getMap()).getResult();
                                if(lists!=null && lists.size()>0){
                                    node.setLeaf(false);
                                    node.setCkr(false);
                                }else{
                                    node.setLeaf(true);
                                }
                                list.add(node);
                            }
                        }
                    }
                }
                return list;
            }
        };
        return treeModel;
    }
    
    public List<SelectItem> getSelectTask(String taskID) {
        List<SelectItem> auditTaskDictListResult = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(taskID)) {
            List<Record> recordResult = auditTaskMapImpl.getDictConnectMap(taskID).getResult();
            if (recordResult != null && recordResult.size() > 0) {
                for (Record record : recordResult) {
                    String dictguid = record.get("dict_id");
                    String finalname = "";
                    AuditTaskDict dict = auditTaskDictImpl.getAuditTaskDictByRowguid(dictguid).getResult();
                    finalname = dict.getClassname();
                    auditTaskDictListResult.add(new SelectItem(record.get("dict_id"), finalname));
                }
            }
        }
        return auditTaskDictListResult;
    }
    
    public String saveTaskToWindow(String guidList, String taskID) {
        String msg = "";
        if (StringUtil.isNotBlank(taskID)) {
            auditTaskMapImpl.deleteAuditTaskMapByTaskID(taskID);
            // 添加新的事项关联关系
            if (StringUtil.isNotBlank(guidList)) {
                String[] dictGuids = guidList.split(";");
                if (StringUtil.isNotBlank(taskID)) {
                    Map<String, String> map = new HashMap<String, String>(16);
                    for (String str : dictGuids) {
                        map.put("rowguid=", str);
                        AuditTaskMap result = auditTaskMapImpl.getAuditTaskMap(map).getResult();
                        if (result != null) {
                            auditTaskMapImpl.updateAuditTaskMap(result);
                        }
                        else {
                            AuditTaskMap auditTaskMap = new AuditTaskMap();
                            auditTaskMap.setRowguid(UUID.randomUUID().toString());
                            auditTaskMap.setOperateusername(UserSession.getInstance().getDisplayName());
                            auditTaskMap.setOperatedate(new Date());
                            auditTaskMap.setMapid(UUID.randomUUID().toString());
                            auditTaskMap.setDictid(str);
                            auditTaskMap.setTaskid(taskID);
                            auditTaskMapImpl.addAuditTaskMap(auditTaskMap);
                        }
                        String finalname = "";
                        AuditTaskDict dict = auditTaskDictImpl.getAuditTaskDictByRowguid(str).getResult();
                        if (StringUtil.isBlank(dict.getParentdictid())) {
                            finalname = dict.getClassname();
                        }
                        else {
                            Map<String, String> map2 = new HashMap<String, String>(16);
                            map2.put("rowguid=", dict.getParentdictid());
                            AuditTaskDict pDict = auditTaskDictImpl.getAuditTaskDictList(map2).getResult().get(0);
                            if (StringUtil.isBlank(pDict.getParentdictid())) {
                                finalname = pDict.getClassname() + "-" + dict.getClassname();
                            }
                            else {
                                map2.put("rowguid=", pDict.getParentdictid());
                                AuditTaskDict gDict = auditTaskDictImpl.getAuditTaskDictList(map2).getResult().get(0);
                                finalname = gDict.getClassname() + "-" + pDict.getClassname() + "-" + dict.getClassname();
                            }
                        }
                        msg += str + "_SPLIT_" + finalname + ";";
                    }
                }
            }
        }
        return msg;
    }
    
    /**
     * 
     * 同步事项到窗口
     * 
     * @param SendType
     *            事项消息发送类型 消息类型有enable、insert、modify、delete
     * @param RabbitMQMsg
     *            事项id
     */
    public void syncWindowTask(String SendType, String taskGuid) {
        // TODO 事项变更之后需要使用通知的方式来处理，不能直接进行更新
        // 2017_4_7 CH 事项变更以后发送消息至RabbitMQ队列
        try {
            String RabbitMQMsg = SendType + ";" + taskGuid ;
            sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg, "task."+ZwfwUserSession.getInstance().getAreaCode()+"."+SendType);
            String isStaticTask = handleConfigService.getFrameConfig("AS_isStaticTask", null).getResult();
            if(StringUtil.isNotBlank(isStaticTask)&&ZwfwConstant.CONSTANT_STR_ONE.equals(isStaticTask)){
                sendMQMessageService.sendByExchange("zwdt_exchange_handle", RabbitMQMsg, "task."+ZwfwUserSession.getInstance().getAreaCode()+"."+SendType);
            }
        }
        catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }

    }
}
