package com.epoint.jiningstimulsoftanalysis.ByTaskAnalysis.action;
import java.text.DecimalFormat;
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
import com.epoint.basic.bizlogic.mis.CommonService;
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
import com.epoint.jiningstimulsoftanalysis.ByTaskAnalysis.api.IJNReportProjectByTaskService;
@RestController("jnreportprojectbytasklistaction")
@Scope("request")
public class JNReportProjectByTaskList extends BaseController
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
    
    private CommonService commservice = new CommonService();
    
    /**
     * 左边树列表
     */
    private String leftTreeNodeGuid;
    
    private TreeModel checkboxtreeModel;
    
    @Autowired
    private IJNReportProjectByTaskService service;
   
    
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
//        if (StringUtil.isBlank(startdate)) {
//        	Calendar cale = Calendar.getInstance();
//            cale.add(Calendar.MONTH, 0);
//            cale.set(Calendar.DAY_OF_MONTH, 1);
//        	startdate = EpointDateUtil.convertDate2String(cale.getTime(), "YYYY-MM-dd");
//        }
//        if (StringUtil.isBlank(enddate)) {
//        	enddate = EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-dd");
//        }
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
				    List<Record> listnum = service.findPageList(startdate,enddate,ouguid,first, pageSize);
				    String sql = " select * from old_table_stais where ouguid = ?";
				    
				    Record oldrecord = commservice.find(sql, Record.class,ouguid);
				    
				    int count = 0;
                    if (listnum != null) {
                        for (Record Record : listnum) {
                            Record.put("ouname", Record.getStr("ouname"));
                            String ouguid = Record.getStr("ouguid");
                            Record record1 = service.getTaskCountByouguid(ouguid);
                            if (record1 != null) {
                            	Record.put("byapplytasknum", record1.getStr("total"));
                            }else {
                            	Record.put("byapplytasknum",  Record.getStr("有办件的依申请事项"));
                            }
                            if(oldrecord != null) {
                            	Record.put("withprojecttasknum", Record.getInt("有办件的依申请事项")+oldrecord.getInt("ct2"));
                                Record.put("bjbjbum", Record.getInt("办件办结量")+oldrecord.getInt("ct3"));
                                Record.put("wwnum", Record.getInt("外网申报数")+oldrecord.getInt("ct4"));
                                Record.put("wwlv", Record.getInt("外网申报率"));
                                Record.put("wwwslnum", Record.getInt("外网申报未受理数")+oldrecord.getInt("ct6"));
                                Record.put("aqbjnum", Record.getInt("按期办理数")+oldrecord.getInt("ct7"));
                                Record.put("bjaqbjlv", Record.getInt("办件按期办结率"));
                            }else {
                            	 Record.put("withprojecttasknum", Record.getStr("有办件的依申请事项"));
                                 Record.put("bjbjbum", Record.getStr("办件办结量"));
                                 Record.put("wwnum", Record.getStr("外网申报数"));
                                 Record.put("wwlv", Record.getStr("外网申报率"));
                                 Record.put("wwwslnum", Record.getStr("外网申报未受理数"));
                                 Record.put("aqbjnum", Record.getStr("按期办理数"));
                                 Record.put("bjaqbjlv", Record.getStr("办件按期办结率"));
                            }
                           
                        }
                    }
                    List<Record> listTotal = service.findAllList(startdate,enddate,ouguid);
                    if (listTotal != null) {
                        count = listTotal.size();
                    }
                    
                    this.setRowCount(count);
                    
                    
                    //控制double类型小数点后的位数（一位）
                    DecimalFormat df = new DecimalFormat( "0.0");
                    int byapplytasknum0  = 0;
                    int withprojecttasknum0 = 0;
                    int bjbjbum0 = 0;
                    int wwnum0 = 0;
                    double wwwslnum0 = 0;
                    int aqbjnum0 = 0;
                    for (Record ou:listnum) {
                        byapplytasknum0 += ou.getInt("byapplytasknum");
                        withprojecttasknum0 += ou.getInt("withprojecttasknum");
                        bjbjbum0 += ou.getInt("bjbjbum");
                        wwnum0 += ou.getInt("wwnum");
                        wwwslnum0 += ou.getDouble("wwwslnum");
                        aqbjnum0 += ou.getInt("aqbjnum");
                        ou.put("wwlv", ou.getDouble("wwlv")>0 ? df.format(ou.getDouble("wwlv") *100) +"%":"0%");
                        if (StringUtil.isBlank(ou.getDouble("bjaqbjlv"))||ou.getDouble("bjaqbjlv")==0) {
                            ou.put("bjaqbjlv","0%");
                        }else {
                            ou.put("bjaqbjlv",  df.format(ou.getDouble("bjaqbjlv") *100) +"%");
                        }
                    }
                    Record allcount = new Record();
                    allcount.put("byapplytasknum", byapplytasknum0);
                    allcount.put("withprojecttasknum", withprojecttasknum0);
                    allcount.put("bjbjbum", bjbjbum0);
                    allcount.put("wwnum", wwnum0);
                    allcount.put("wwwslnum", wwwslnum0);
                    allcount.put("aqbjnum", aqbjnum0);
                    allcount.put("wwlv", bjbjbum0>0 ? df.format(wwnum0 *100 /withprojecttasknum0)+"%":"0%");
                    allcount.put("bjaqbjlv", bjbjbum0>0 ? df.format(aqbjnum0 *100 /withprojecttasknum0)+"%":"0%");
                    allcount.put("ouname", "总计");
                    List<Record> endlist = new ArrayList<Record>();
                    endlist.add(allcount);
                    endlist.addAll(listnum);
                    return endlist;
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
                            if ("370800".equals(areacode)) {
                                List<String> allAreacode = service.getAllAreacode();
                                List<FrameOu> frameOus = new ArrayList<>();
                                for (String strAreacode : allAreacode) {
                                    frameOus = iHandleFrameOU.getWindowOUList(strAreacode, true)
                                            .getResult();
                                    listRootOu.addAll(frameOus);
                                }
                            }else {
                                listRootOu = iHandleFrameOU.getWindowOUList(areacode, true)
                                        .getResult();
                            }
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
            exportModel = new ExportModel("ouname,byapplytasknum,withprojecttasknum,bjbjbum,wwnum,wwlv,wwwslnum,aqbjnum,bjaqbjlv",
                    "部门名称,依申请事项总数,有办件的依申请事项,办件办结量,外网申报数,外网申报率,外网申报未受理数,按期办理数,办件按期办结率");
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
