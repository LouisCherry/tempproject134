package com.epoint.jntongji.action;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
@RestController("jnreportprojecthcpbyoulistaction")
@Scope("request")
public class JNReportProjectHcpByOUList extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<Record> modelall;

    private ExportModel exportModel;
    private String startdate;
    private String enddate;
    private String projectname;
    
    private String ouguid;
    
    private String taskouguid;
    private TreeModel checkboxtreeModel;
    private TreeModel checkTaskboxtreeModel;
    
    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditProjectSparetime projectSparetimeService;
    
    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;
    
    /**
     * 办件状态
     */
    private String status;
    
    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> statusModel = null;
    
    /**
     * 左边树列表
     */
    private String leftTreeNodeGuid;
    @Autowired
    private IOuService ouService;    
    @Autowired
    private IHandleFrameOU iHandleFrameOU;
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
  
    private CommonService commservice = new CommonService();
    @Override
    public void pageLoad() {
        this.startdate = this.getRequestParameter("startdate");
        if ("kong".equals(startdate)) {
            startdate = "";
        }
        this.enddate = this.getRequestParameter("enddate");
        if ("kong".equals(enddate)) {
            enddate = "";
        }
        if (StringUtil.isBlank(startdate)) {
        	Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
        	startdate = EpointDateUtil.convertDate2String(cale.getTime(), "YYYY-MM-dd");
        }
        if (StringUtil.isBlank(enddate)) {
        	enddate = EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-dd");
        }
    }

    public DataGridModel<Record> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<Record>()
            {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					String oustr0 = "select a.currentareacode,a.rowguid,c.ouname as xqouname,a.ouname,a.projectname,a.flowsn,a.applyername,a.applydate,a.acceptuserdate,a.banjiedate,a.status ";
                    String strsql = "from audit_project a left join frame_user b on a.acceptuserguid = b.userguid left join frame_ou c on b.ouguid = c.ouguid " + 
                    		" where a.Currentareacode <> '370800' " ;
                    String condition = "";
                    condition += " and a.APPLYDATE BETWEEN '" + EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate))
                    		+ " 00:00:00' and '" + EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate)) + " 23:59:59' ";
                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                    	areacode = leftTreeNodeGuid;
                    }
                    if(StringUtil.isNotBlank(areacode)) {
                    	condition += " and task_id in (select taskid from audit_task_delegate where areacode = '"+ areacode +"')";
                    }
                    if(StringUtil.isNotBlank(status)) {
                    	condition += " and a.status = '"+ status +"'";
                    }
                    if(StringUtil.isNotBlank(projectname)) {
                    	condition += " and a.projectname like '%"+ projectname +"%'";
                    }
                    String[] ouguids = null;
                    if(StringUtil.isNotBlank(ouguid)) {
                    	ouguids = ouguid.split(",");
                    	String ouguidsql = "";
                    	if (ouguids != null && ouguids.length > 0) {
                    		for (String taskidExp : ouguids) {
                    			ouguidsql += "'" + taskidExp + "',";
                    		}
                    		if (StringUtil.isNotBlank(ouguidsql)) {
                    			ouguidsql = ouguidsql.substring(0, ouguidsql.length() - 1);
                    			condition += " and a.ouguid in ("+ouguidsql+")";
                    		}
                    	}
                    }
                    String[] taskouguids = null;
                    if(StringUtil.isNotBlank(taskouguid)) {
                    	taskouguids = taskouguid.split(",");
                    	String ouguidsql = "";
                    	if (taskouguids != null && taskouguids.length > 0) {
                    		for (String taskidExp : taskouguids) {
                    			ouguidsql += "'" + taskidExp + "',";
                    		}
                    		if (StringUtil.isNotBlank(ouguidsql)) {
                    			ouguidsql = ouguidsql.substring(0, ouguidsql.length() - 1);
                    			condition += " and c.ouguid in ("+ouguidsql+")";
                    		}
                    	}
                    }
                    condition +=   " order by a.banjiedate desc,a.projectname ,a.ouguid,c.ouguid ";
                    List<Record> listnum = commservice.findList(oustr0 + strsql + condition ,first,pageSize,Record.class);
                    
                    for (Record auditProject : listnum) {
                        auditProject.put("sparetime",
                                getSpareTime(auditProject.getStr("rowguid"), auditProject.getInt("status"),auditProject.getStr("currentareacode")));
                    }
                    String countsql = (oustr0 + strsql + condition).replace("select a.currentareacode,a.rowguid,c.ouname as xqouname,a.ouname,a.projectname,a.flowsn,a.applyername,a.applydate,a.acceptuserdate,a.banjiedate,a.status", "select count(1)");
                    Integer listnumcount = 0;
                    if(ouguids != null) {
                    	listnumcount = ouguids.length;
                    }else{
                    	listnumcount = commservice.find(countsql, Integer.class, areacode);
                    }
                    this.setRowCount(listnumcount);
                  
                    return listnum;
                }
            };
        }
        return modelall;
    }
    
    
    public TreeModel getcheckboxModel() {
        if (checkboxtreeModel == null) {
            checkboxtreeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    // 每次点击树节点前的加号，进行加载
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        List<FrameOu> listRootOu = new ArrayList<>();
                        if(StringUtil.isNotBlank(objectGuid)) {
                        	listRootOu  =  ouService.listOUByGuid(objectGuid, 2);
                        }else {
                        	listRootOu = iHandleFrameOU.getWindowOUList("370800", true)
                        			.getResult();
                        }
                        // 部门的绑定
                        for (int i = 0; i < listRootOu.size(); i++) {
                            TreeNode node = new TreeNode();
                            node.setId(listRootOu.get(i).getOuguid());
                            node.setText(listRootOu.get(i).getOuname());
                            node.setPid(listRootOu.get(i).getParentOuguid());
                            node.setLeaf(true);
                            for (int j = 0; j < listRootOu.size(); j++) {
                                if (listRootOu.get(i).getOuguid().equals(listRootOu.get(j).getParentOuguid())) {
                                    node.setLeaf(false);
                                    break;
                                }
                            }
                            list.add(node);
                        }
                    }
                    return list;
                }
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                    //获取到tree原有的select
                    List<SelectItem> selectedItems = checkboxtreeModel.getSelectNode();
                    //获取到tree原有的select
                    if (selectedItems.size() != 0 && selectedItems.get(0).getValue().equals("请选择")) {
                        selectedItems.remove(0);
                    }
                    //复选框选中
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    String objectGuid = treeData.getObjectGuid();
                    if (treeNode.isChecked() == true) {
                        //利用标记的isOU做判断
                      //  if (treeNode.getColumns().get("isOU").equals("true")) {
                            List<FrameOu> listRootOu = new ArrayList<>();
                            if(StringUtil.isNotBlank(objectGuid)) {
                            	listRootOu  =  ouService.listOUByGuid(objectGuid, 2);
                            }else {
                            	listRootOu = iHandleFrameOU.getWindowOUList("370800", true)
                            			.getResult();
                            }
                            for (int i = 0; i < listRootOu.size(); i++) {
                                for (int j = 0; j < selectedItems.size(); j++) {
                                    if (listRootOu.get(i).getOuguid().equals(selectedItems.get(j).getValue())) {
                                      selectedItems.remove(j);
                                  }
                                }
                                    selectedItems.add(new SelectItem(listRootOu.get(i).getOuguid(),
                                            listRootOu.get(i).getOuname()));
                            }
                    }
                    //复选框取消选中
                    else {
                            List<FrameOu> listRootOu = new ArrayList<>();
                            if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                                AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                                        .getAreaByAreacode("370800").getResult();
                                if (auditOrgaArea != null) {
                                    List<FrameOu> frameOus = ouService.listDependOuByParentGuid(auditOrgaArea.getOuguid(),"", 2);
                                    // 筛选并排序
                                    listRootOu = frameOus.stream()
                                            .filter(x -> x.getOuname().contains(treeNode.getSearchCondition())
                                                    && !auditOrgaArea.getOuguid().equals(x.getOuguid()))
                                            .sorted((a, b) -> b.getOrderNumber().compareTo(a.getOrderNumber()))
                                            .collect(Collectors.toList());
                                }

                            }else{
                            	listRootOu = iHandleFrameOU.getWindowOUList("370800", true).getResult();
                            }
                            for (int i = 0; i < listRootOu.size(); i++) {
                                for (int j = 0; j < selectedItems.size(); j++) {
                                    if (listRootOu.get(i).getOuguid().equals(selectedItems.get(j).getValue())) {
                                      selectedItems.remove(j);
                                  }
                                }
                            }
                   }
                    return selectedItems;
                }
            };
        }

        return checkboxtreeModel;
    }
    public TreeModel getTaskcheckboxModel() {
    	String areacode = ZwfwUserSession.getInstance().getAreaCode();
    	if (checkTaskboxtreeModel == null) {
    		checkTaskboxtreeModel = new TreeModel()
    		{
    			private static final long serialVersionUID = 1L;
    			public List<TreeNode> fetch(TreeNode treeNode) {
    				TreeData treeData = TreeFunction9.getData(treeNode);
    				List<TreeNode> list = new ArrayList<>();
    				// 首次加载树结构
    				if (treeData == null) {
    					TreeNode root = new TreeNode();
    					root.setText("所有部门");
    					root.setId("");
    					root.setPid("-1");
    					list.add(root);
    					root.setExpanded(true);// 展开下一层节点
    					list.addAll(fetch(root));// 自动加载下一层树结构
    				}
    				// 每次点击树节点前的加号，进行加载
    				else {
    					String objectGuid = treeData.getObjectGuid();
    					List<FrameOu> listRootOu = new ArrayList<>();
    					if(StringUtil.isNotBlank(objectGuid)) {
    						listRootOu  =  ouService.listOUByGuid(objectGuid, 2);
    					}else {
    						listRootOu = iHandleFrameOU.getWindowOUList(StringUtil.isNotBlank(leftTreeNodeGuid)?leftTreeNodeGuid:areacode, true)
    								.getResult();
    					}
    					// 部门的绑定
    					for (int i = 0; i < listRootOu.size(); i++) {
    						TreeNode node = new TreeNode();
    						node.setId(listRootOu.get(i).getOuguid());
    						node.setText(listRootOu.get(i).getOuname());
    						node.setPid(listRootOu.get(i).getParentOuguid());
    						node.setLeaf(true);
    						for (int j = 0; j < listRootOu.size(); j++) {
    							if (listRootOu.get(i).getOuguid().equals(listRootOu.get(j).getParentOuguid())) {
    								node.setLeaf(false);
    								break;
    							}
    						}
    						list.add(node);
    					}
    				}
    				return list;
    			}
    			public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
    				//获取到tree原有的select
    				List<SelectItem> selectedItems = checkTaskboxtreeModel.getSelectNode();
    				//获取到tree原有的select
    				if (selectedItems.size() != 0 && selectedItems.get(0).getValue().equals("请选择")) {
    					selectedItems.remove(0);
    				}
    				//复选框选中
    				TreeData treeData = TreeFunction9.getData(treeNode);
    				String objectGuid = treeData.getObjectGuid();
    				if (treeNode.isChecked() == true) {
    					//利用标记的isOU做判断
    					//  if (treeNode.getColumns().get("isOU").equals("true")) {
    					List<FrameOu> listRootOu = new ArrayList<>();
    					if(StringUtil.isNotBlank(objectGuid)) {
    						listRootOu  =  ouService.listOUByGuid(objectGuid, 2);
    					}else {
    						listRootOu = iHandleFrameOU.getWindowOUList(StringUtil.isNotBlank(leftTreeNodeGuid)?leftTreeNodeGuid:areacode, true)
    								.getResult();
    					}
    					for (int i = 0; i < listRootOu.size(); i++) {
    						for (int j = 0; j < selectedItems.size(); j++) {
    							if (listRootOu.get(i).getOuguid().equals(selectedItems.get(j).getValue())) {
    								selectedItems.remove(j);
    							}
    						}
    						selectedItems.add(new SelectItem(listRootOu.get(i).getOuguid(),
    								listRootOu.get(i).getOuname()));
    					}
    				}
    				//复选框取消选中
    				else {
    					List<FrameOu> listRootOu = new ArrayList<>();
    					if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
    						AuditOrgaArea auditOrgaArea = auditOrgaAreaService
    								.getAreaByAreacode(StringUtil.isNotBlank(leftTreeNodeGuid)?leftTreeNodeGuid:areacode).getResult();
    						if (auditOrgaArea != null) {
    							List<FrameOu> frameOus = ouService.listDependOuByParentGuid(auditOrgaArea.getOuguid(),"", 2);
    							// 筛选并排序
    							listRootOu = frameOus.stream()
    									.filter(x -> x.getOuname().contains(treeNode.getSearchCondition())
    											&& !auditOrgaArea.getOuguid().equals(x.getOuguid()))
    									.sorted((a, b) -> b.getOrderNumber().compareTo(a.getOrderNumber()))
    									.collect(Collectors.toList());
    						}
    						
    					}else{
    						listRootOu = iHandleFrameOU.getWindowOUList(areacode, true).getResult();
    					}
    					for (int i = 0; i < listRootOu.size(); i++) {
    						for (int j = 0; j < selectedItems.size(); j++) {
    							if (listRootOu.get(i).getOuguid().equals(selectedItems.get(j).getValue())) {
    								selectedItems.remove(j);
    							}
    						}
    					}
    				}
    				return selectedItems;
    			}
    		};
    	}
    	
    	return checkTaskboxtreeModel;
    }
    
    public int strToInt(String numstr) {
    	if(StringUtil.isNotBlank(numstr)) {
    		try {
    			return Integer.valueOf(numstr);
			} catch (Exception e) {}
    	}
    	return 0;
    }

    public ExportModel getExportModel() {
       if (exportModel == null) {
            exportModel = new ExportModel("xqouname,ouname,projectname,flowsn,applyername,applydate,acceptuserdate,sparetime,banjiedate,status",
                    "业务办理部门,事项所属部门,事项名称,办件编号,申请人,申请时间,受理时间,办结剩余时间,办结时间,办件状态");
        }
        return exportModel;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
    public String getProjectname() {
    	return projectname;
    }
    
    public void setProjectname(String projectname) {
    	this.projectname = projectname;
    }

	public String getOuguid() {
		return ouguid;
	}

	public void setOuguid(String ouguid) {
		this.ouguid = ouguid;
	}
	public String getTaskOuguid() {
		return taskouguid;
	}
	
	public void setTaskOuguid(String taskouguid) {
		this.taskouguid = taskouguid;
	}

	public String getLeftTreeNodeGuid() {
		return leftTreeNodeGuid;
	}

	public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
		this.leftTreeNodeGuid = leftTreeNodeGuid;
	}
	
	 public void setStatusModel(List<SelectItem> statusModel) {
	        this.statusModel = statusModel;
	    }
	 
	 @SuppressWarnings("unchecked")
	    public List<SelectItem> getStatusModel() {
	        if (statusModel == null) {
	            statusModel = DataUtil.convertMap2ComboBox(
	                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, true));
	            if (statusModel != null) {
	                Iterator<SelectItem> it = statusModel.iterator();
	                if (it.hasNext()) {
	                    it.next();
	                }
	                while (it.hasNext()) {
	                    SelectItem item = it.next();
	                    // 删除列表中不需要的列表选项
	                    if ((Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_DJJ)
	                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWINIT)
	                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWWTJ)
	                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_INIT)
	                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_YDJ)
	                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWYSTU)
	                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWYSTG)) {
	                        it.remove();
	                    }
	                }
	            }
	        }
	        return this.statusModel;
	    }
	 
	  public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    /**
	     * 获取剩余时间
	     * 
	     * @return String
	     */
	    public String getSpareTime(String rowguid, int status,String areacode) {
	        String result = "";
	        // 默认剩余时间
	        String defaultSparaeTime = "";
	        if (StringUtil.isNotBlank(rowguid) && StringUtil.isNotBlank(status)) {
	            String fields = " rowguid,taskguid,projectname,tasktype,centerguid,banjiedate,Promise_day,acceptuserdate,promiseenddate ";
	            AuditProject auditProject = auditProjectService
	                    .getAuditProjectByRowGuid(fields, rowguid, null).getResult();
	            AuditProjectSparetime auditProjectSparetime = projectSparetimeService.getSparetimeByProjectGuid(rowguid)
	                    .getResult();
	            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
	                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
	            if (auditProjectSparetime == null) {
	                return defaultSparaeTime;
	            }
	            else if (auditProject.getTasktype() == Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ)) {
	                if ((status >= ZwfwConstant.BANJIAN_STATUS_YSL && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ)
	                        || (status == ZwfwConstant.BANJIAN_STATUS_YJJ
	                                && ZwfwConstant.CONSTANT_INT_ONE == auditTaskExtension.getIszijianxitong()
	                                && ZwfwConstant.ZIJIANMODE_JJMODE
	                                        .equals(String.valueOf(auditTaskExtension.getZijian_mode())))) {
	                    if (StringUtil.isNotBlank(auditProjectSparetime.getSpareminutes())) {
	                        int i = auditProjectSparetime.getSpareminutes();
	                        if (i > 0) {
	                            if (i < 1440){
	                                result = "剩余" + CommonUtil.getSpareTimes(i);
	                            }
	                            else{
	                                result = "剩余" + CommonUtil.getSpareTimes(i);
	                            }
	                        }
	                        else {
	                            i = -i;
	                            result = "超时" + CommonUtil.getSpareTimes(i);
	                        }
	                    }
	                    else {
	                        return defaultSparaeTime;
	                    }
	                }
	                else if (status >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
	                	if (StringUtil.isNotBlank(auditProjectSparetime.getSpareminutes())) {
	                        int i = auditProjectSparetime.getSpareminutes();
	                        if (i < 0) {// 如果剩余时间为负数:办结超时
	                            result = "已办结(超过" + CommonUtil.getSpareTimes(i);
	                        }
	                        else {
	                            result = "已办结";
	                        }
	                    }
	                    else {// 办结用时为空：默认--
	                        return defaultSparaeTime;
	                    }
	                }
	                else {
	                    return defaultSparaeTime;
	                }
	            }
	            else {//非承诺件
	                result = ZwfwConstant.getItemtypeKey(auditProject.getTasktype().toString());
	            }
	        }
	        else {
	            result = defaultSparaeTime;
	        }
	        return result;
	    }
}
