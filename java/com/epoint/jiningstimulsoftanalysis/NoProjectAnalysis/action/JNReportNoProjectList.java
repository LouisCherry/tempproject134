package com.epoint.jiningstimulsoftanalysis.NoProjectAnalysis.action;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jiningstimulsoftanalysis.NoProjectAnalysis.api.IJNReportNoProjectService;
@RestController("jnreportnoprojectlistaction")
@Scope("request")
public class JNReportNoProjectList extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<Record> modelall;

    private ExportModel exportModel;
    private String startdate;
    private String enddate;
    
    private String ouguid;
    
    /**
     * 左边树列表
     */
    @Autowired
    private IOuService ouService;    
    @Autowired
    private IHandleFrameOU iHandleFrameOU;
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    private TreeModel treeModel = null;
    /**
     * 左边树列表
     */
    private String leftTreeNodeGuid;
    
    private TreeModel checkboxtreeModel;
    
    @Autowired
    private IJNReportNoProjectService service;
   
    
    private String areacode= "";
    private String centerguid= "";
  
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
        areacode = this.getRequestParameter("areacode");
        centerguid = this.getRequestParameter("guid");
        if(StringUtil.isBlank(areacode)){
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        if(StringUtil.isBlank(centerguid)){
            centerguid = ZwfwUserSession.getInstance().getCenterGuid();
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
				    //根据申请时间范围和部门分页查找数据列表
				    List<Record> recordList = service.findPageList(startdate,enddate,ouguid,first, pageSize);
				    int count = 0;
                    if (recordList != null) {
                        for (Record Record : recordList) {
                            Record.put("ouname", Record.getStr("ouname"));
                            Record.put("taskname", Record.getStr("taskname"));
                            Record.put("item_id", Record.getStr("item_id"));
                        }
                    }
                    List<Record> alllist = service.findAllList(startdate,enddate,ouguid);
                    if (alllist != null) {
                        count = alllist.size();
                    }
                    this.setRowCount(count);
                    return recordList;
                }
            };
        }
        return modelall;
    }
    public TreeModel getcheckboxModel() {
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
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
                            listRootOu = iHandleFrameOU.getWindowOUList(areacode, true)
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

        return checkboxtreeModel;
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
            exportModel = new ExportModel("ouname,taskname,item_id",
                    "部门名称,事项名称,事项编码");
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

	public String getOuguid() {
		return ouguid;
	}

	public void setOuguid(String ouguid) {
		this.ouguid = ouguid;
	}

	public String getLeftTreeNodeGuid() {
		return leftTreeNodeGuid;
	}

	public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
		this.leftTreeNodeGuid = leftTreeNodeGuid;
	}


}
