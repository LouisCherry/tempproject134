package com.epoint.xmz.jncertrecord.action;

import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
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
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.xmz.jncertrecord.api.IJnCertRecordService;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author Yally
 * @Description
 * @Date 2022/1/6 16:16
 * @Version 1.0
 **/
@RestController("jnrecordlistaction")
@Scope("request")
public class JNRecordListAction extends BaseController {
    private static final long serialVersionUID = 1L;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IJNAuditProject ijnAuditProject;
    
    @Autowired
    private IJnCertRecordService service;
    

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;


    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> statusModel = null;


    private List<SelectItem> applywayModel = null;
    
    private List<SelectItem> certModel = null;


    private DataGridModel<JnCertRecord> modelall;

    private TreeModel treeModel = null;

    private String areacode = "";

    private ExportModel exportModel;
    
    public String bapplydate;
    public String eapplydate;
    public String type;
    public String taskname;
    public String certtype;
    

    /**
     * 左边树列表
     */
    private String leftTreeNodeGuid;


    @Override
    public void pageLoad() {
        if (StringUtil.isBlank(bapplydate)) {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            bapplydate = EpointDateUtil.convertDate2String(cale.getTime(), "YYYY-MM-dd");
        }
        if (StringUtil.isBlank(eapplydate)) {
        	eapplydate = EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-dd");
        }
    }

    public DataGridModel<JnCertRecord> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<JnCertRecord>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<JnCertRecord> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    // 左侧区域条件
                    List<String> searchOUGuidList = new ArrayList<>();
                    // 选择部门ougui，即lleftTreeNodeGuid非空时
                    log.info("leftTreeNodeGuid是：" + leftTreeNodeGuid);
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        // 查询区域编码
                        FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(leftTreeNodeGuid);
                        areacode = frameOuExtendInfo.getStr("AREACODE");
                        // 获取当前部门信息
                        FrameOu ouByOuGuid = ouService.getOuByOuGuid(leftTreeNodeGuid);
                        if (areacode.length() == 6 && StringUtil.isNotBlank(ouByOuGuid.getParentOuguid())) {
                            searchOUGuidList.add(leftTreeNodeGuid);
                        }
                        else if (areacode.length() == 6 && StringUtil.isBlank(ouByOuGuid.getParentOuguid())) {
                            searchOUGuidList = ijnAuditProject.findOUGuidList(areacode, leftTreeNodeGuid);
                        }
                        else {
                            searchOUGuidList = ijnAuditProject.findOUGuidList(areacode, leftTreeNodeGuid);
                        }
                    }
                    else {
                        searchOUGuidList = ijnAuditProject.findOUGuidList(areacode, userSession.getOuGuid());
                    }
                    
                    String sql = "select areacode,count(1) as total,ouname,taskname,certtype from jn_cert_record where 1=1 ";
                    
                    if (StringUtil.isNotBlank(bapplydate)) {
                    	sql += " and OperateDate >= '"+EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(bapplydate)) +" 00:00:00'";
                    }
                    
                    if (StringUtil.isNotBlank(eapplydate)) {
                    	sql += " and OperateDate <= '"+EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(eapplydate)) +" 23:59:59'";
                    }
                    
                    if (searchOUGuidList.size() > 0) {
                    	sql += " and ouguid in (" + StringUtil.joinSql(searchOUGuidList) + ")";
                    }
                    
                    if (StringUtil.isNotBlank(taskname)) {
                    	sql += " and taskname like '%" +taskname+"%'";
                    }
                    
                    if (StringUtil.isNotBlank(type)) {
                    	sql += " and type = '" +type+"'";
                    }
                    
                    if (StringUtil.isNotBlank(certtype)) {
                    	sql += " and certtype = '" +certtype+"'";
                    }
                    
                    sql += " and taskname is not null";
                    
                    sql += " group by ouname,taskname,certtype";
                    
                    List<JnCertRecord> list = service.findList(sql, first, pageSize,null);
                   List<JnCertRecord> records = service.findList(sql, null);
                    this.setRowCount(records.size());
                    return list;
                    
                }
            };
        }
        return modelall;
    }

    public TreeModel getTreeModel() {
        boolean isadmin = userSession.isAdmin();
        areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (treeModel == null) {
            treeModel = new TreeModel() {
                private static final long serialVersionUID = -7089566877270145158L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("f9root");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "true");//标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }
                    else {
                        List<FrameOu> listRootOu;
                        // 当前登陆人员所在部门区域信息
                        AuditOrgaArea areanow = auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                        String areanowOuguid = areanow.getOuguid();
                        // 管理员
                        if (isadmin) {
                            listRootOu = ouService.listOUByGuid("", 4);
                        }
                        else {
                            listRootOu = ouService.listOUByGuid(areanowOuguid, 4);
                        }

                        // 部门的绑定
                        for (FrameOu frameOu : listRootOu) {
                            TreeNode node = new TreeNode();
                            node.setId(frameOu.getOuguid());
                            node.setText(frameOu.getOuname());
                            if (areanowOuguid.equals(frameOu.getOuguid())) {
                                node.setPid("f9root");
                            }
                            else {
                                node.setPid(StringUtil.isBlank(frameOu.getParentOuguid()) ? "f9root" : frameOu.getParentOuguid());
                            }
                            node.setLeaf(true);
                            list.add(node);
                        }

                    }

                    return list;
                }
            };
        }

        return treeModel;
    }

    public List<SelectItem> getApplywayModel() {
        if (applywayModel == null) {
            applywayModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "电子证照调用方式", null, false));
        }
        return this.applywayModel;
    }
    
    public List<SelectItem> getCertModel() {
    	if (certModel == null) {
    		certModel = DataUtil.convertMap2ComboBox(
    				(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "大数据证照类型", null, false));
    	}
    	return this.certModel;
    }

    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, false));
            if (statusModel != null) {
                Iterator<SelectItem> it = statusModel.iterator();
                while (it.hasNext()) {
                    SelectItem item = it.next();
                    // 删除列表中不需要的列表选项
                    int i = Integer.parseInt(item.getValue().toString());
                    if (i < ZwfwConstant.BANJIAN_STATUS_YJJ) {
                        it.remove();
                    }
                }
            }
        }
        return this.statusModel;
    }

    public int strToInt(String numstr) {
        if (StringUtil.isNotBlank(numstr)) {
            try {
                return Integer.valueOf(numstr);
            }
            catch (Exception e) {
            }
        }
        return 0;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("areacode,total,ouname,allnum,taskname,certtype",
                    "辖区编码,合计,部门,办件量,事项名称,证照类型");
        }
        return exportModel;
    }


    
    public String getBapplydate() {
		return bapplydate;
	}

	public void setBapplydate(String bapplydate) {
		this.bapplydate = bapplydate;
	}

	public String getEapplydate() {
		return eapplydate;
	}

	public void setEapplydate(String eapplydate) {
		this.eapplydate = eapplydate;
	}


    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getCerttype() {
		return certtype;
	}

	public void setCerttype(String certtype) {
		this.certtype = certtype;
	}

	public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }
}
