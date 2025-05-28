package com.epoint.jiningzwfw.projectstatistics.evaInstance.action;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jiningzwfw.projectstatistics.evaInstance.service.IEvaInstanceStatisticsService;
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
@RestController("deptevainstancestatisticslistaction")
@Scope("request")
public class DeptEvaInstanceStatisticsListAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<Record> modelall;

    private ExportModel exportModel;
    private String startdate;
    private String enddate;

    private String ouguid;
    private TreeModel checkboxtreeModel;

    private TreeModel treeModel = null;
    private String month;
    private String lyqd;
    private String sf;

    /**
     * 月份model
     */
    private List<Record> monthModel = null;

    /**
     * 是否同步
     */
    private List<SelectItem> sfModel = null;

    /**
     * 来源渠道
     */
    private List<SelectItem> lyqdModel = null;
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
    private IEvaInstanceStatisticsService service;

    private CommonService commservice = new CommonService();
    @Override
    public void pageLoad() {
        month = String.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
    }

    public DataGridModel<Record> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<Record>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    SqlConditionUtil sql = new SqlConditionUtil();

                    // 保留两位小数
                    DecimalFormat df = new DecimalFormat("0.00");
                    df.setRoundingMode(RoundingMode.HALF_UP);

                    // 部门
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)){
                        List<FrameOu> ouList;
                        if ("济宁市".equals(userSession.getOuName()) && leftTreeNodeGuid.equals(userSession.getOuGuid())){
                            ouList = ouService.listOUByGuid("", 4);
                        }else {
                            ouList = ouService.listOUByGuid(leftTreeNodeGuid, 4);
                        }
                        List<String> collect = ouList.stream().map(FrameOu::getOuguid).collect(Collectors.toList());
                        sql.in("deptcode", StringUtil.joinSql(collect));
                    }

                    // 来源渠道
                    if (StringUtil.isNotBlank(lyqd)){
                        sql.eq("pf", lyqd);
                    }
                    // 是否同步
                    if (StringUtil.isNotBlank(sf)){
                        sql.eq("sbsign", sf);
                    }

                    List<Record> list = service.findStatisticsPage(sql.getMap(), month,first,pageSize);

                    for (Record r : list) {
                        // 好评率
                        Double hpl = 0D;
                        Integer fcmynum = r.getInt("fcmynum") == null ? 0 : r.getInt("fcmynum");
                        Integer mynum = r.getInt("mynum") == null ? 0 : r.getInt("mynum");
                        Integer jbmynum = r.getInt("jbmynum") == null ? 0 : r.getInt("jbmynum");
                        Integer bmynum = r.getInt("bmynum") == null ? 0 : r.getInt("bmynum");
                        Integer fcbmynum = r.getInt("fcbmynum") == null ? 0 : r.getInt("fcbmynum");
                        Integer total = fcmynum + mynum + jbmynum + bmynum + fcbmynum;
                        r.set("total", total);
                        hpl = (total == 0 ? 0 : (fcmynum + mynum + jbmynum) * 100D / total);
                        r.set("hpl", df.format(hpl) + "%");
                    }

                    int sum = service.countStatistics(sql.getMap(), month);
                    setRowCount(sum);
                    return list;
                }
            };
        }
        return modelall;
    }

    /**
     * 构建树节点，济宁市为最上级
     * @return
     */
    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = -7089566877270145158L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();
                    if(StringUtil.isBlank(leftTreeNodeGuid)){
                        ouguid = userSession.getOuGuid();
                    }else{
                        ouguid = leftTreeNodeGuid;
                    }

                    if (treeNode == null) {

                        // 构建根节点
                        FrameOu ou = ouService.getOuByOuGuid(ouguid);
                        TreeNode root = new TreeNode();
                        root.setText(ou.getOuname());
                        root.setId(ou.getOuguid());
                        root.setPid("-1");
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构

                    }
                    else {

                        // 如果是济宁市需要加上区县
                        if ("济宁市".equals(treeNode.getText())){
                            List<FrameOu> ouList1 = ouService.listOUByGuid("", 1);
                            for (FrameOu frameOu : ouList1) {
                                if ("济宁市".equals(frameOu.getOuname())){
                                    continue;
                                }
                                TreeNode pNode = new TreeNode();
                                pNode.setText(frameOu.getOuname());
                                pNode.setId(frameOu.getOuguid());
                                pNode.setPid(ouguid);
                                int count = ouService.getOuCountByOuguid("", "", frameOu.getOuguid(), "", true);
                                pNode.setLeaf(count == 0);
                                list.add(pNode);
                            }
                        }


                        List<FrameOu> ouList = ouService.listOUByGuid(treeNode.getId(), 5);
                        //部门绑定
                        for (FrameOu ou : ouList) {
                            TreeNode node = new TreeNode();
                            node.setId(ou.getOuguid());
                            node.setText(ou.getOuname());
                            node.setPid(ouguid);
                            int count = ouService.getOuCountByOuguid("", "", ou.getOuguid(), "", true);
                            node.setCkr(true);
                            // count==0为true是叶子节点，没有下一级
                            node.setLeaf(count == 0);
                            list.add(node);
                        }

                    }
                    return list;
                }
            };
        }

        return treeModel;
    }

    public List<Record> getMonthModel(){
        if (monthModel == null){
            monthModel = new ArrayList<>();
            for (int i = 1; i < 13; i++) {
                Record r = new Record();
                r.set("value",i);
                r.set("text", i + "月");
                monthModel.add(r);
            }
        }
        return monthModel;
    }


    public TreeModel getcheckboxModel() {
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (checkboxtreeModel == null) {
            checkboxtreeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;
                @Override
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
                @Override
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
            exportModel = new ExportModel("proDepart,total,fcmynum,mynum,jbmynum,bmynum,fcbmynum,hpl,pf",
                    "部门名称,评价总数,非常满意数量,满意数量,基本满意数量,不满意数量,非常不满意数量,好评率,渠道");
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<SelectItem> getSfModel() {
        if (sfModel == null){
            sfModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return sfModel;
    }

    public List<SelectItem> getLyqdModel() {
        if (lyqdModel == null){
            lyqdModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "来源渠道", null, false));
        }
        return lyqdModel;
    }

    public String getLyqd() {
        return lyqd;
    }

    public void setLyqd(String lyqd) {
        this.lyqd = lyqd;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }
}
