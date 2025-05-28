package com.epoint.auditsp.auditsptask.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.epoint.auditsp.auditsptask.api.IJNAuditWindowTaskService;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.composite.auditsp.handlespsharematerial.inter.IHandleSPShareMaterial;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;

/**
 * 阶段事项配置页面对应的后台
 * 个性化市县同体显示事项
 * @author wry,xbn
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("jnauditsptaskconfigaction")
@Scope("request")
@SuppressWarnings("serial")
public class JnAuditSpTaskConfigAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4632640761998959244L;
    /**
     * 窗口与事项的关联实体对象
     */
    private AuditTask dataBean = null;
    /**
     * 窗口部门事项model
     */
    private TreeModel treeModelTask;
    /**
     * 阶段标识
     */
    private String phaseguid = null;

    private String fields = "rowguid,task_id,type,taskname,ouguid";
    private String itemcategory= null;
    @Autowired
    private IAuditTask auditTask;
    
    @Autowired
    private IHandleFrameOU frameOu;

    @Autowired
    private IAuditSpTask auditSpTaskImpl;
    
    @Autowired
    private IAuditTask auditTaskBasicImpl;
    
    @Autowired
    private IAuditSpBusiness iauditSpBusinessImpl;
    
    @Autowired
    private IHandleSPShareMaterial spShareMaterialService;
    
    @Autowired
    private IAuditOrgaArea auditAreaImpl;

    @Autowired
    private IOuService ouService;
    @Autowired
    private IHandleConfig config;
    @Autowired
	private IJNAuditWindowTaskService service;
    @Override
    public void pageLoad() {
        phaseguid = getRequestParameter("phaseguid");
        itemcategory=config.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
    }

    /**
     * 根据phaseguid获取事项SelectItem，初始化事项信息
     * 
     * @param phaseguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private List<SelectItem> getSelectTaskPhaseguid(String phaseguid) {
        List<SelectItem> auditTaskList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(phaseguid)) {
        	
        	List<AuditSpTask> auditSpListTemp = new ArrayList<AuditSpTask>();
    		List<AuditSpTask> auditSpList = new ArrayList<AuditSpTask>();
    		if (StringUtil.isNotBlank(phaseguid)) {
    			auditSpListTemp = auditSpTaskImpl.getAllAuditSpTaskByPhaseguid(phaseguid).getResult();
    			//去除已经删除或禁用的事项
    			if(auditSpListTemp!=null && auditSpListTemp.size()>0){
    				String taskids = "";
    				List<String> taskidList = new ArrayList<>();
    				for (AuditSpTask auditSpTask : auditSpListTemp) {
    					taskids += "'" + auditSpTask.getTaskid() + "',";
    					taskidList.add(auditSpTask.getTaskid());
    				}
    				taskids = taskids.substring(0,taskids.length()-1);
    				SqlConditionUtil sql = new SqlConditionUtil();
    				sql.eq("IS_EDITAFTERIMPORT", "1");
    				sql.eq("IS_ENABLE", "1");
    				sql.isBlankOrValue("IS_HISTORY", "0");
    				sql.in("task_id", taskids);
    				sql.setSelectFields("task_id");
    				List<AuditTask> auditTasks = auditTaskBasicImpl.getAllTask(sql.getMap()).getResult();
    				if(auditTasks!=null && auditTasks.size()>0){
    					for (AuditTask auditTask : auditTasks) {
    						auditSpList.add(auditSpListTemp.get(taskidList.indexOf(auditTask.getTask_id())));
    					}
    				}
    			}
    		}
        	
               
            if (auditSpList != null && auditSpList.size() > 0) {
                for (AuditSpTask auditTask : auditSpList) {

                    auditTaskList.add(new SelectItem(auditTask.getTaskid(), auditTask.getTaskname().replace(",", "，")));
                }
            }
        }
        return auditTaskList;
    }

    public TreeModel getTaskTreeModel() {
        if (treeModelTask == null) {
        	treeModelTask = new TreeModel()
            {
                /***
                 * 加载树，懒加载
                 */
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                                     
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("root");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "false");//标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }
                    else {
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            //以下是框架封装的方法，具体参数请参见api
                            List<AuditTask> listsearchTask = new ArrayList<AuditTask>();
                            if(StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory) ){
                                SqlConditionUtil sql  = new SqlConditionUtil();
                                sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("ISTEMPLATE",  ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.like("taskname", treeNode.getSearchCondition());
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                listsearchTask = auditTask.getAuditEnableTaskPageData(fields, sql.getMap(), 0, 0, "ordernum", "desc").getResult().getList();

                            }else{
                                SqlConditionUtil sql  = new SqlConditionUtil();
                                sql.like("taskname", treeNode.getSearchCondition());
                                listsearchTask = auditTask.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc").getResult().getList();

                            }
                            //搜索完成后，进行树的绑定
                            for (int j = 0; j < listsearchTask.size(); j++) {
                                TreeNode node = new TreeNode();
                                node.setId(listsearchTask.get(j).getTask_id()+"&"+listsearchTask.get(j).getAreacode());
                                node.setText(listsearchTask.get(j).getTaskname());
                                node.setPid("-1");
                                node.setLeaf(true);
                                list.add(node);
                            }
                        }
                        else {
                            String objectGuid = treeData.getObjectGuid();
                            List<AuditTask> listTask = new ArrayList<>();
                            
                            
                            List<FrameOu> listOu = new ArrayList<>();
                            SqlConditionUtil sql  = new SqlConditionUtil();
                        	List<AuditOrgaArea> listArea=new ArrayList<>();
                            if("root".equals(objectGuid)){
                            	listArea=service.getAllArea();
                            	for(int n = 0; n < listArea.size(); n++){
                            	    TreeNode node = new TreeNode();
                                    node.setId(listArea.get(n).getXiaqucode());
                                    node.setText(listArea.get(n).getXiaquname());
                                    node.setPid("root");
                                    node.getColumns().put("isOU", "true");//标记：不是部门节点
                                    list.add(node);
                            	  }
                        	}else if(objectGuid.length()==6){                              
                                 listOu = frameOu.getWindowOU(objectGuid).getResult();
                        	}else{      
                            	FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(objectGuid);
                                if(StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory) ){
                                    sql.clear();
                                    sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                    sql.eq("ISTEMPLATE",  ZwfwConstant.CONSTANT_STR_ZERO);
                                    sql.eq("OUGUID", treeData.getObjectGuid());
                                    sql.eq("areacode", ouExt.get("areacode"));
                                    sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                    listTask = auditTask.getAuditEnableTaskPageData(fields, sql.getMap(), 0, 0, "ordernum", "desc").getResult().getList();

                                }else{
                                    listTask = auditTask.selectAuditTaskOuByObjectGuid(treeData.getObjectGuid(),
                                    		ouExt.get("areacode")).getResult();
                                    for (int i=0;i<listTask.size();i++) {
                                        listTask.get(i).setAreacode(ouExt.get("areacode"));
                                    }

                                }
                        	}
                            // 部门绑定
                            for (int i = 0; i < listOu.size(); i++) {
                                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(listOu.get(i).getOuguid());
                                AuditOrgaArea area = auditAreaImpl.getAreaByAreacode(ouExt.get("areacode")).getResult();
                                if (StringUtil.isBlank(ouExt.get("areacode")) || StringUtil.isBlank(area.getCitylevel())
                                        || Integer.parseInt(area.getCitylevel()) < Integer
                                                .parseInt(ZwfwConstant.AREA_TYPE_XZJ)) {

                                  if ("root".equals(objectGuid)
                                            || (ouExt.get("areacode").equals(objectGuid))) {
                                        TreeNode node1 = new TreeNode();
                                        node1.setId(listOu.get(i).getOuguid());
                                        node1.setText(listOu.get(i).getOuname());                                       
                                        if ("root".equals(objectGuid)) {
                                            node1.setPid("root");
                                        }
                                        else {
                                            node1.setPid(listOu.get(i).getParentOuguid());
                                        }
                                        node1.getColumns().put("isOU", "true");//标记：是部门节点
                                        node1.setLeaf(false);
                                        int taskCount = 0;
                                        if (StringUtil.isNotBlank(itemcategory)
                                                && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                            sql.clear();
                                            sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                            sql.eq("OUGUID", listOu.get(i).getOuguid());
                                            sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                            taskCount = auditTask.getAuditEnableTaskPageData(fields, sql.getMap(), 0, 0,
                                                    "ordernum", "desc").getResult().getRowCount();

                                        }
                                        else {
                                            taskCount = auditTask
                                                    .selectAuditTaskOuByObjectGuid(listOu.get(i).getOuguid(),
                                                    		objectGuid)
                                                    .getResult().size();
                                        }
                                        if (taskCount > 0) {
                                            list.add(node1);
                                        }
                                    }
                                }
                            }
                            //事项的绑定
                            for (int j = 0; j < listTask.size(); j++) {
                                TreeNode node2 = new TreeNode();
                                //node2.setId(listTask.get(j).getTask_id());
                                node2.setId(listTask.get(j).getTask_id()+"&"+listTask.get(j).getAreacode());
                                node2.setText(listTask.get(j).getTaskname().replace(",", "，"));
                                node2.setPid(listTask.get(j).getOuguid());
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
                    List<SelectItem> selectedItems = treeModelTask.getSelectNode();
                    //复选框选中
                    if (treeNode.isChecked() == true) {
                        //利用标记的isOU做判断
                        if (treeNode.getColumns().get("isOU").equals("true")) {
                        	if(treeNode.getId().length()!=6){
                        		
                        	
                        	FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(treeNode.getId());

                            List<AuditTask> listTask = auditTask.selectAuditTaskOuByObjectGuid(treeNode.getId(),
                            		ouExt.get("areacode")).getResult();
                            
                            String areacoden = ouExt.get("areacode");
                            
                            if(StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory) ){
                                for (int i = 0; i < listTask.size(); i++) {
                                    if(listTask.get(i).getItem_id().length()<31){
                                        continue;
                                    }
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (listTask.get(i).getTask_id().equals(selectedItems.get(j).getValue())) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                    selectedItems.add(
                                            new SelectItem(listTask.get(i).getTask_id()+"&"+listTask.get(i).getAreacode(), listTask.get(i).getTaskname().replace(",", "，")));
                                }

                            }else{
                                for (int i = 0; i < listTask.size(); i++) {
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (listTask.get(i).getTask_id().equals(selectedItems.get(j).getValue())) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                    selectedItems.add(
                                            new SelectItem(listTask.get(i).getTask_id()+"&"+listTask.get(i).getAreacode(), listTask.get(i).getTaskname().replace(",", "，")));
                                }
                                
                            }
                        	}
                        }
                        else {
                            selectedItems.add(new SelectItem(treeNode.getId(), treeNode.getText()));
                        }
                    }
                    //复选框取消选中
                    else {
                        if (treeNode.getColumns().get("isOU").equals("true")) {

                            if ("".equals(treeNode.getId())) {
                                selectedItems.clear();
                            }
                            else {
                            	if(treeNode.getId().length()!=6){
                               FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(treeNode.getId());
                                List<AuditTask> listTask = auditTask.selectAuditTaskOuByObjectGuid(treeNode.getId(),
                                		ouExt.get("areacode")).getResult();
                                for (int i = 0; i < listTask.size(); i++) {
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (selectedItems.get(j).getValue().equals(listTask.get(i).getTask_id())) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                }
                            }
                        }
                        }
                        else {
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i).getValue().equals(treeNode.getId())) {
                                    selectedItems.remove(i);
                                }
                            }
                        }
                    }
                    
                    return selectedItems;
                }
            };
            if (!isPostback()) {
            	treeModelTask.setSelectNode(getSelectTaskPhaseguid(phaseguid));
            }
        }
        return treeModelTask;
    }

    /**
     * 
     * 把事项和阶段信息保存到关系表里面
     * 
     * @param guidList
     *            前台获取的事项guid 用“;”分割
     * @param windowGuid
     *            窗口guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveTaskToSp(String guidList, String phaseguid, String businedssguid) {

        String msg = "";
        if (StringUtil.isNotBlank(phaseguid) && StringUtil.isNotBlank(businedssguid)) {
        	
            String myTaskId = "";
            String areaCodeOn = "";
    		if (StringUtil.isNotBlank(phaseguid) && StringUtil.isNotBlank(businedssguid)) {
    			auditSpTaskImpl.deleteAuditSpTaskByPhaseguid(phaseguid);
    			// auditWindowImpl.deleteWindowTaskByWindowGuid(windowGuid);
    			// 添加新的事项关联关系
    			//system.out.println("guidList:"+guidList);
    			if (StringUtil.isNotBlank(guidList)) {
    				String[] taskIDs = guidList.split(";");
    				if (StringUtil.isNotBlank(phaseguid)) {
    					for (int i = 0; i < taskIDs.length; i++) {
    					    if(taskIDs[i].contains("&")) {
    					        String[] taskIdArea = taskIDs[i].split("&");
    					        myTaskId = taskIdArea[0];
                                areaCodeOn =  taskIdArea[1];
    					    }else {
    					        myTaskId = taskIDs[i];
    					    }
    						AuditSpTask auditPhaseTask = new AuditSpTask();
    						auditPhaseTask.setRowguid(UUID.randomUUID().toString());
    						auditPhaseTask.setOperatedate(new Date());
    						auditPhaseTask.setBusinessguid(businedssguid);
    						auditPhaseTask.setPhaseguid(phaseguid);
    						auditPhaseTask.setCustomtag(userSession.getUserGuid());
    					
    						AuditTask auditTask = auditTaskBasicImpl.selectUsableTaskByTaskID(myTaskId).getResult();
    						if (auditTask != null) {
    						    auditPhaseTask.setTaskname(auditTask.getTaskname().replace(",", "，"));
    						    auditPhaseTask.setTaskid(auditTask.getTask_id());
    						    auditPhaseTask.setOuguid(auditTask.getOuguid());
    						    auditPhaseTask.setOrdernumber(auditTask.getOrdernum()==null ? 0 : auditTask.getOrdernum());
    						}
    						
    						AuditSpBusiness auditSpBusiness = iauditSpBusinessImpl
    								.getAuditSpBusinessByRowguid(businedssguid).getResult();
    						if (auditSpBusiness != null) {
    						    auditPhaseTask.setAreacode(areaCodeOn);

    						}

    						AuditCommonResult<String> addResult = auditSpTaskImpl.addAuditSpTask(auditPhaseTask);
    						if (!addResult.isSystemCode()) {
    							msg = "99";
    						} else if (!addResult.isBusinessCode()) {
    							msg = "88";
    						} else {
    							msg = "保存成功！";
    						}
    					}
    				}
    			}else{
    				msg = "保存成功！";
    			}
    			String areacode="";
    		        // 如果是镇村接件
		        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
		            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
		        }else{
		            areacode = ZwfwUserSession.getInstance().getAreaCode();
		        }
    			// 处理共享材料和对应关系
    			spShareMaterialService.initSPShareMaterialFromTask(businedssguid , areacode);
    		}
        	
        	
        	
            addCallbackParam("msg", msg);
        }
        else {
            addCallbackParam("msg", "添加失败！");
        }

    }

    public String getPhaseguid() {
        return phaseguid;
    }

    public void setPhaseguid(String phaseguid) {
        this.phaseguid = phaseguid;
    }

    public AuditTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditTask dataBean) {
        this.dataBean = dataBean;
    }

}
