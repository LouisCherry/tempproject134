package com.epoint.jnauditprojectdata.action;

import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
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
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
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
@RestController("jnauditprojectdataaction")
@Scope("request")
public class JNAuditProjectDataAction extends BaseController {
    private static final long serialVersionUID = 1L;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IJNAuditProject ijnAuditProject;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();

    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> statusModel = null;


    private List<SelectItem> applywayModel = null;

    /**
     * 申请时间
     */
    private String applydateStart;
    private String applydateEnd;
    /**
     * 办结时间
     */
    private String finishdateStart;
    private String finishdateEnd;


    private DataGridModel<Record> modelall;

    private TreeModel treeModel = null;

    private String areacode = "";

    private ExportModel exportModel;

    /**
     * 左边树列表
     */
    private String leftTreeNodeGuid;

    @Autowired
    private IRoleService iRoleService;


    @Override
    public void pageLoad() {
        if (StringUtil.isBlank(applydateStart)) {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            applydateStart = EpointDateUtil.convertDate2String(cale.getTime(), "YYYY-MM-dd HH:mm:ss");
        }
        if (StringUtil.isBlank(applydateEnd)) {
            applydateEnd = EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-dd HH:mm:ss");
        }
    }

    public DataGridModel<Record> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<Record>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
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
                    log.info("开始查询办件量：" + dataBean + "===" + applydateStart + "===" + applydateEnd + "==="
                            + areacode + "===" + searchOUGuidList);
                    List<Record> listNum = ijnAuditProject.findDataList(dataBean, applydateStart, applydateEnd
                            , finishdateStart, finishdateEnd, areacode, searchOUGuidList, first, pageSize);
                    log.info("办件量查询结果：" + listNum);
                    int listNumCount = ijnAuditProject.getAuditDataCount(areacode, searchOUGuidList);
                    // 加上总量一行
                    this.setRowCount(listNumCount + 1);
                    // 查询总量
                    int allRecordNum = ijnAuditProject.findTotalDataCount(dataBean, applydateStart, applydateEnd
                            , finishdateStart, finishdateEnd, areacode, searchOUGuidList);
                    String ouguid = "";

                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil = bulidConditionUtil(conditionUtil);
                    int Lcprojectnum = 0;
                    for (Record ou : listNum) {
                        ouguid = ou.getStr("ouguid");
                        int allNum = strToInt(ou.getStr("allnum"));
                        ou.put("ouguid", ouguid);
                        ou.put("allnum", allNum);

                        conditionUtil.eq("ouguid",ouguid);
                        Lcprojectnum = ijnAuditProject.getLcprojectTwoListCount(conditionUtil.getMap())
                                + ijnAuditProject.getLcprojectTenListCount(conditionUtil.getMap());
                        ou.put("importnum",Lcprojectnum);
                    }
                    conditionUtil = bulidConditionUtil(conditionUtil);
                    conditionUtil.in("ouguid",StringUtil.joinSql(searchOUGuidList));
                     int allImportNum = ijnAuditProject.getLcprojectTwoListCount(conditionUtil.getMap())
                            + ijnAuditProject.getLcprojectTenListCount(conditionUtil.getMap());
                    // 总量记录
                    Record allRecord = new Record();
                    allRecord.put("ouguid", "ALL");
                    allRecord.put("allnum", allRecordNum);
                    allRecord.put("importnum", allImportNum);
                    allRecord.put("ouname", "总计");
                    List<Record> endlist = new ArrayList<>();
                    endlist.add(allRecord);
                    endlist.addAll(listNum);
                    return endlist;
                }
            };
        }
        return modelall;
    }

    private SqlConditionUtil bulidConditionUtil(SqlConditionUtil conditionUtil){
        conditionUtil.clear();
        if(StringUtil.isNotBlank(applydateStart)){
            conditionUtil.gt("APPLYDATE",EpointDateUtil.getBeginOfDateStr(applydateStart));
        }
        if(StringUtil.isNotBlank(applydateEnd)){
            conditionUtil.gt("APPLYDATE",EpointDateUtil.getEndOfDateStr(applydateEnd));
        }
        if(StringUtil.isNotBlank(finishdateStart)){
            conditionUtil.gt("BANJIEDATE",EpointDateUtil.getBeginOfDateStr(finishdateStart));
        }
        if(StringUtil.isNotBlank(finishdateEnd)){
            conditionUtil.gt("BANJIEDATE",EpointDateUtil.getEndOfDateStr(finishdateEnd));
        }
        return conditionUtil;
    }

    public TreeModel getTreeModel() {
        boolean isadmin = userSession.isAdmin();
        boolean isquanliangview = false;
        List<String> roles = userSession.getUserRoleList();
        if (roles != null && !roles.isEmpty()) {
            for (String roleid : roles) {
                FrameRole role = iRoleService.getRoleByRoleField("roleguid", roleid);
                if (role != null) {
                    if ("全量查看".equals(role.getRoleName())) {
                        isquanliangview=true;
                        break;
                    }
                }
            }
        }
        areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (treeModel == null) {
            boolean finalIsquanliangview = isquanliangview;
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
                        if (isadmin || finalIsquanliangview) {
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
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请方式", null, false));
        }
        return this.applywayModel;
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
            exportModel = new ExportModel("ouname,allnum,importnum",
                    "部门,办件量,导出量");
        }
        return exportModel;
    }


    public AuditProject getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditProject();
        }
        return dataBean;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public String getApplydateStart() {
        return applydateStart;
    }

    public void setApplydateStart(String applydateStart) {
        this.applydateStart = applydateStart;
    }

    public String getApplydateEnd() {
        return applydateEnd;
    }

    public void setApplydateEnd(String applydateEnd) {
        this.applydateEnd = applydateEnd;
    }

    public String getFinishdateStart() {
        return finishdateStart;
    }

    public void setFinishdateStart(String finishdateStart) {
        this.finishdateStart = finishdateStart;
    }

    public String getFinishdateEnd() {
        return finishdateEnd;
    }

    public void setFinishdateEnd(String finishdateEnd) {
        this.finishdateEnd = finishdateEnd;
    }
}
