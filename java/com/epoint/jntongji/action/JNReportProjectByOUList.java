package com.epoint.jntongji.action;

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
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController("jnreportprojectbyoulistaction")
@Scope("request")
public class JNReportProjectByOUList extends BaseController {
    private static final long serialVersionUID = 1L;
    private DataGridModel<Record> modelall;

    private ExportModel exportModel;
    private String startdate;
    private String enddate;

    private String ouguid;
    private TreeModel checkboxtreeModel;
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
    @Autowired
    private IRoleService iRoleService;

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
            modelall = new DataGridModel<Record>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String oustr0 = "SELECT o.OUCODE,o.ouname,o.ouguid,t.allnum,t.wwnum,t.bjnum,t.cqbjnum,t.slnum,t.cqslnum,t.bslnum,t.bxknum from( ";
                    //增加如果只选择根节点，展示各区县的数字
                    String oustr0_1 = "SELECT a.XiaQuCode AS ouguid,a.XiaQuName AS ouname,t.allnum,t.wwnum,t.bjnum,t.cqbjnum,t.slnum,t.cqslnum,t.bslnum,t.bxknum from( ";

                    //总计数字
                    String oustr0_2 = "SELECT SUM(t.allnum) AS allnum,SUM(t.wwnum) AS wwnum,SUM(t.bjnum) AS bjnum,SUM(t.cqbjnum) AS cqbjnum,SUM(t.slnum) AS slnum,SUM(t.cqslnum) AS cqslnum,SUM(t.bslnum) AS bslnum,SUM(t.bxknum) AS bxknum from( ";

                    String strsql = "select ouguid, "
                            + "SUM(CASE WHEN (STATUS >= 12 and STATUS != 20) THEN 1 ELSE 0 END ) as allnum, "
                            + "SUM(CASE WHEN (STATUS >= 30 and applyway<>20) THEN 1 ELSE 0 END ) as wwnum, "
                            + "SUM(CASE WHEN STATUS >= 90  THEN 1 ELSE 0 END ) as bjnum, "
                            + "SUM(CASE WHEN STATUS >= 90 and status!=97 and PROMISEENDDATE is not null and PROMISEENDDATE>'1800-01-01' and BANJIEDATE > PROMISEENDDATE THEN 1 ELSE 0 END ) as cqbjnum, "
                            + "SUM(CASE WHEN STATUS >= 30 THEN 1 ELSE 0 END ) as slnum, "
                            + "SUM(CASE WHEN ifnull(ISOVERTIMESHOULI,0)=1 THEN 1 ELSE 0 END) as cqslnum, "
                            + "SUM(CASE WHEN STATUS = 97 THEN 1 ELSE 0 END ) as bslnum, "
                            + "SUM(CASE WHEN STATUS = 40 THEN 1 ELSE 0 END ) as bxknum "
                            + "from audit_project where 1=1 ";
                    //增加如果只选择根节点，展示各区县的数字
                    String strsql1 = "select p.areacode, "
                            + "SUM(CASE WHEN (STATUS >= 12 and STATUS != 20) THEN 1 ELSE 0 END ) as allnum, "
                            + "SUM(CASE WHEN (STATUS >= 30 and applyway<>20) THEN 1 ELSE 0 END ) as wwnum, "
                            + "SUM(CASE WHEN STATUS >= 90  THEN 1 ELSE 0 END ) as bjnum, "
                            + "SUM(CASE WHEN STATUS >= 90 and status!=97 and PROMISEENDDATE is not null and PROMISEENDDATE>'1800-01-01' and BANJIEDATE > PROMISEENDDATE THEN 1 ELSE 0 END ) as cqbjnum, "
                            + "SUM(CASE WHEN STATUS >= 30 THEN 1 ELSE 0 END ) as slnum, "
                            + "SUM(CASE WHEN ifnull(ISOVERTIMESHOULI,0)=1 THEN 1 ELSE 0 END) as cqslnum, "
                            + "SUM(CASE WHEN STATUS = 97 THEN 1 ELSE 0 END ) as bslnum, "
                            + "SUM(CASE WHEN STATUS = 40 THEN 1 ELSE 0 END ) as bxknum "
                            + "from audit_project p INNER JOIN frame_ou_extendinfo e ON p.ouguid = e.ouguid where 1=1 ";
                    String condition = "";
                    //增加如果只选择根节点，展示各区县的数字
                    String condition1 = " and  e.IS_WINDOWOU = 1 ";
                    condition += " and APPLYDATE BETWEEN '"
                            + EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate))
                            + " 00:00:00' and '"
                            + EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate))
                            + " 23:59:59' ";
                    condition += " and STATUS >= 12";
                    condition1 +=condition;
                    String userRoles = userSession.getUserRoles();
                    FrameRole role = iRoleService.getRoleByRoleField("ROLENAME", "全量查看");
                    boolean isquanliangchakan = false;
                    if (role != null){
                        String roleGuid = role.getRoleGuid();
                        if (StringUtil.isNotBlank(userRoles) && userRoles.contains(roleGuid)){
                            isquanliangchakan = true;
                        }
                    }
//                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    //左树
                    String areacode = "";
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        areacode = leftTreeNodeGuid;
                    }

                    if(!isquanliangchakan){
                        areacode=ZwfwUserSession.getInstance().getAreaCode();
                    }

                    String oustr1 = ")t RIGHT JOIN frame_ou o on t.ouguid = o.ouguid INNER JOIN frame_ou_extendinfo e on o.ouguid = e.ouguid where e.IS_WINDOWOU=1 ";
                    //增加如果只选择根节点，展示各区县的数字
                    String oustr2 = ")t RIGHT JOIN audit_orga_area as a ON a.XiaQuCode=t.areacode ";

                    //左树筛选
                    if (StringUtil.isNotBlank(areacode)) {
                        oustr1 += " and e.areacode='" + areacode + "'";
                        condition += " and areacode='" + areacode + "'";
                    }
                    String[] ouguids = null;
                    if (StringUtil.isNotBlank(ouguid)) {
                        ouguids = ouguid.split(",");
                        String ouguidsql = "";
                        if (ouguids != null && ouguids.length > 0) {
                            for (String taskidExp : ouguids) {
                                ouguidsql += "'" + taskidExp + "',";
                            }
                            if (StringUtil.isNotBlank(ouguidsql)) {
                                ouguidsql = ouguidsql.substring(0, ouguidsql.length() - 1);
                                oustr1 += " and o.ouguid in (" + ouguidsql + ")";
                            }
                        }
                    }
                    condition += " GROUP BY OUGUID";
                    condition1 += " GROUP BY areacode";
                    String orderby =" ORDER BY t.allnum desc, t.slnum desc";
                    oustr2 += " where a.CityLevel<=2";
                    String orderby2 = " ORDER BY a.XiaQuCode asc";
                    List<Record> listnum =null;
                    if(StringUtils.isNotBlank(areacode)){

                        listnum = commservice.findList(oustr0 + strsql + condition + oustr1+orderby, first, pageSize,
                                Record.class);
                    }else{
                        //增加如果只选择根节点，展示各区县的数字
                        listnum = commservice.findList(oustr0_1 + strsql1 + condition1 + oustr2+orderby2, first, pageSize,
                                Record.class);

                    }

                    String countsql = " select count(1) from frame_ou o INNER JOIN frame_ou_extendinfo e on o.ouguid = e.ouguid where e.IS_WINDOWOU=1 and areacode=?";
                    String countsql1 = " select count(1) from audit_orga_area where CityLevel<=2";

                    Integer listnumcount = 0;
                    Integer listnumcount1 = 0;
                    if (ouguids != null) {
                        listnumcount = ouguids.length;
                    } else {
                        listnumcount = commservice.find(countsql, Integer.class, areacode);
                    }
                    if(StringUtils.isNotBlank(areacode)){
                        this.setRowCount(listnumcount);
                    }else{
                        listnumcount1 = commservice.find(countsql1, Integer.class);
                        this.setRowCount(listnumcount1);
                    }

                    // 控制double类型小数点后的位数（一位）
                    DecimalFormat df = new DecimalFormat("0.0");
                    int allnum0 = 0;
                    int slnum0 = 0;
                    int cqslnum0 = 0;
                    int bjnum0 = 0;
                    int aqbjnum0 = 0;
                    int cqbjnum0 = 0;
                    int wwnum0 = 0;
                    int bslnum0 = 0;
                    int bxknum0 = 0;
                    int zjslnum0 = 0;
                    String ouguid = "";

                    for (Record ou : listnum) {
                        int zjslnum = 0;
                        // 导入办件数获取
                        String twobjsql = "select count(*) from lc_project_two where 1=1 ";
                        String tenbjsql = "select count(*) from lc_project_ten where 1=1 ";
                        if (startdate != null && enddate != null && !"".equals(startdate) && !"".equals(enddate)) {
                            twobjsql += " and APPLYDATE BETWEEN '"
                                    + EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate))
                                    + " 00:00:00' and '"
                                    + EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate))
                                    + " 23:59:59' ";
                            tenbjsql += " and APPLYDATE BETWEEN '"
                                    + EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate))
                                    + " 00:00:00' and '"
                                    + EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate))
                                    + " 23:59:59' ";
                        }
                        ouguid = ou.getStr("ouguid");
                        if(StringUtils.isNotBlank(areacode)){
                            if (StringUtil.isNotBlank(ouguid)) {
                                twobjsql += " and ouguid='" + ouguid + "'";
                                tenbjsql += " and ouguid='" + ouguid + "'";
                            }
                        }else{
                            if (StringUtil.isNotBlank(ouguid)) {
                                twobjsql += " and areacode='" + ouguid + "'";
                                tenbjsql += " and areacode='" + ouguid + "'";
                            }
                        }

                        Integer twobjCount = commservice.find(twobjsql, Integer.class);
                        Integer tenbjCount = commservice.find(tenbjsql, Integer.class);

                        if (twobjCount != 0 && tenbjCount != 0) {
                            zjslnum = twobjCount + tenbjCount;
                        } else if (twobjCount != 0) {
                            zjslnum = twobjCount;
                        } else if (tenbjCount != 0) {
                            zjslnum = tenbjCount;
                        } else {
                            zjslnum = 0;
                        }

                        int allnum = strToInt(ou.getStr("allnum"));
                        int slnum = strToInt(ou.getStr("slnum"));
                        int cqslnum = strToInt(ou.getStr("cqslnum"));
                        int bjnum = strToInt(ou.getStr("bjnum"));
                        int cqbjnum = strToInt(ou.getStr("cqbjnum"));
                        int aqbjnum = bjnum - cqbjnum;
                        int wwnum = strToInt(ou.getStr("wwnum"));
                        int bslnum = strToInt(ou.getStr("bslnum"));
                        int bxknum = strToInt(ou.getStr("bxknum"));
                        ou.put("zjslnum", zjslnum);
                        ou.put("ouguid", ouguid);
                        ou.put("allnum", allnum);
                        ou.put("slnum", slnum);
                        ou.put("cqslnum", cqslnum);
                        ou.put("bjnum", bjnum);
                        ou.put("cqbjnum", cqbjnum);
                        ou.put("aqbjnum", aqbjnum);
                        ou.put("wwnum", wwnum);
                        ou.put("bslnum", bslnum);
                        ou.put("bxknum", bxknum);
                        ou.put("bjlv", slnum > 0 ? df.format(bjnum * 100 / slnum) + "%" : "0%");
                        ou.put("wwlv", slnum > 0 ? df.format(wwnum * 100 / slnum) + "%" : "0%");
                        ou.put("aqbjlv", bjnum > 0 ? df.format(aqbjnum * 100 / bjnum) + "%" : "0%");
//                        allnum0 += allnum;
//                        slnum0 += slnum;
//                        cqslnum0 += cqslnum;
//                        bjnum0 += bjnum;
//                        aqbjnum0 += aqbjnum;
//                        cqbjnum0 += cqbjnum;
//                        wwnum0 += wwnum;
//                        bslnum0 += bslnum;
//                        bxknum0 += bxknum;
//                        zjslnum0 += zjslnum;
                    }
                    //导入办件的总计计算
                    String twobjsql_1 = "select count(*) from lc_project_two where 1=1 ";
                    String tenbjsql_1 = "select count(*) from lc_project_ten where 1=1 ";
                    if (startdate != null && enddate != null && !"".equals(startdate) && !"".equals(enddate)) {
                        twobjsql_1 += " and APPLYDATE BETWEEN '"
                                + EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate))
                                + " 00:00:00' and '"
                                + EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate))
                                + " 23:59:59' ";
                        tenbjsql_1 += " and APPLYDATE BETWEEN '"
                                + EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate))
                                + " 00:00:00' and '"
                                + EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate))
                                + " 23:59:59' ";
                    }
                    if(StringUtils.isNotBlank(areacode)){
                        twobjsql_1 += " and areacode='" + areacode + "'";
                        tenbjsql_1 += " and areacode='" + areacode + "'";
                    }

                    Integer twobjCount_1 = commservice.find(twobjsql_1, Integer.class);
                    Integer tenbjCount_1 = commservice.find(tenbjsql_1, Integer.class);
                    if (twobjCount_1 != 0 && tenbjCount_1 != 0) {
                        zjslnum0 = twobjCount_1 + tenbjCount_1;
                    } else if (twobjCount_1 != 0) {
                        zjslnum0 = twobjCount_1;
                    } else if (tenbjCount_1 != 0) {
                        zjslnum0 = tenbjCount_1;
                    } else {
                        zjslnum0 = 0;
                    }
                    
                    //总计一列计算
                    Record ou = null;
                    if(StringUtils.isNotBlank(areacode)){
                        ou = commservice.find(oustr0_2 + strsql + condition + oustr1,
                                Record.class);
                    }else{
                        //增加如果只选择根节点，展示各区县的数字
                        ou = commservice.find(oustr0_2 + strsql1 + condition1 + oustr2,
                                Record.class);
                    }
                    allnum0=strToInt(ou.getStr("allnum"));
                    slnum0=strToInt(ou.getStr("slnum"));
                    cqslnum0=strToInt(ou.getStr("cqslnum"));
                    bjnum0=strToInt(ou.getStr("bjnum"));
                    aqbjnum0=strToInt(ou.getStr("aqbjnum"));
                    cqbjnum0=strToInt(ou.getStr("cqbjnum"));
                    wwnum0=strToInt(ou.getStr("wwnum"));
                    bslnum0=strToInt(ou.getStr("bslnum"));
                    bxknum0=strToInt(ou.getStr("bxknum"));
                    zjslnum0=strToInt(ou.getStr("zjslnum"));
                    Record allcount = new Record();
                    allcount.put("ouguid", "ALL");
                    allcount.put("zjslnum", zjslnum0);
                    allcount.put("allnum",allnum0 );
                    allcount.put("slnum", slnum0);
                    allcount.put("cqslnum", cqslnum0);
                    allcount.put("bjnum", bjnum0);
                    allcount.put("aqbjnum", bjnum0-cqbjnum0);
                    allcount.put("cqbjnum", cqbjnum0);
                    allcount.put("wwnum", wwnum0);
                    allcount.put("bslnum", bslnum0);
                    allcount.put("bxknum", bxknum0);
                    allcount.put("bjlv", slnum0 > 0 ? df.format(bjnum0 * 100 / slnum0) + "%" : "0%");
                    allcount.put("wwlv", slnum0 > 0 ? df.format(wwnum0 * 100 / slnum0) + "%" : "0%");
                    allcount.put("aqbjlv", bjnum0 > 0 ? df.format(aqbjnum0 * 100 / bjnum0) + "%" : "0%");
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
            checkboxtreeModel = new TreeModel() {
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
                        if (StringUtil.isNotBlank(objectGuid)) {
                            listRootOu = ouService.listOUByGuid(objectGuid, 2);
                        } else {
                            listRootOu = iHandleFrameOU
                                    .getWindowOUList(
                                            StringUtil.isNotBlank(leftTreeNodeGuid) ? leftTreeNodeGuid : areacode, true)
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
                    // 获取到tree原有的select
                    List<SelectItem> selectedItems = checkboxtreeModel.getSelectNode();
                    // 获取到tree原有的select
                    if (selectedItems.size() != 0 && selectedItems.get(0).getValue().equals("请选择")) {
                        selectedItems.remove(0);
                    }
                    // 复选框选中
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    String objectGuid = treeData.getObjectGuid();
                    if (treeNode.isChecked() == true) {
                        // 利用标记的isOU做判断
                        // if (treeNode.getColumns().get("isOU").equals("true"))
                        // {
                        List<FrameOu> listRootOu = new ArrayList<>();
                        if (StringUtil.isNotBlank(objectGuid)) {
                            listRootOu = ouService.listOUByGuid(objectGuid, 2);
                        } else {
                            listRootOu = iHandleFrameOU
                                    .getWindowOUList(
                                            StringUtil.isNotBlank(leftTreeNodeGuid) ? leftTreeNodeGuid : areacode, true)
                                    .getResult();
                        }
                        for (int i = 0; i < listRootOu.size(); i++) {
                            for (int j = 0; j < selectedItems.size(); j++) {
                                if (listRootOu.get(i).getOuguid().equals(selectedItems.get(j).getValue())) {
                                    selectedItems.remove(j);
                                }
                            }
                            selectedItems
                                    .add(new SelectItem(listRootOu.get(i).getOuguid(), listRootOu.get(i).getOuname()));
                        }
                    }
                    // 复选框取消选中
                    else {
                        List<FrameOu> listRootOu = new ArrayList<>();
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                                    .getAreaByAreacode(
                                            StringUtil.isNotBlank(leftTreeNodeGuid) ? leftTreeNodeGuid : areacode)
                                    .getResult();
                            if (auditOrgaArea != null) {
                                List<FrameOu> frameOus = ouService.listDependOuByParentGuid(auditOrgaArea.getOuguid(),
                                        "", 2);
                                // 筛选并排序
                                listRootOu = frameOus.stream()
                                        .filter(x -> x.getOuname().contains(treeNode.getSearchCondition())
                                                && !auditOrgaArea.getOuguid().equals(x.getOuguid()))
                                        .sorted((a, b) -> b.getOrderNumber().compareTo(a.getOrderNumber()))
                                        .collect(Collectors.toList());
                            }

                        } else {
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
        if (StringUtil.isNotBlank(numstr)) {
            try {
                return Integer.valueOf(numstr);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "ouname,allnum,wwnum,wwlv,slnum,bjnum,bjlv,aqbjnum,aqbjlv,cqbjnum,bslnum,bxknum",
                    "部门,申请数,外网申报数,外网申报率,受理数,办结数,办结率,按期办结数,按期办结率,超期办结数,不予受理,不予许可");
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
