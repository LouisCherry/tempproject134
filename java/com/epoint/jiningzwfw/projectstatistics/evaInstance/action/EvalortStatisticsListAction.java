package com.epoint.jiningzwfw.projectstatistics.evaInstance.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@RestController("evalortstatisticslistaction")
@Scope("request")
public class EvalortStatisticsListAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<AuditProject> modelall;

    private ExportModel exportModel;
    private String startdate;
    private String enddate;

    private CommonService commservice = new CommonService();

    private TreeModel treeModel = null;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    @Autowired
    private IOuService ouService;

    private String areacode = "";

    private String evalevel="";

    /**
     * Evalevel下拉列表model
     */
    private List<SelectItem> evalevelModel = null;


    /**
     * Evalevel下拉列表model
     */
    private List<SelectItem> evalevelwayModel = null;

    private String evalevelway;
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

    }

    public DataGridModel<AuditProject> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<AuditProject>()
            {
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    String centerguid=ZwfwUserSession.getInstance().getCenterGuid();

                    String strsql = "";
                    StringBuffer conditionSb = new StringBuffer();
                    if(StringUtils.isBlank(evalevelway)){
                        evalevelway="002";
                    }
                    if(StringUtils.isNotBlank(evalevelway)){
                        switch (evalevelway){
                            case "001":
                                //默认评价
                                conditionSb.append("SELECT b.ouname,b.ouguid,SUM(CASE WHEN satisfaction='3' OR satisfaction='4' OR satisfaction='5' " +
                                        "OR satisfaction='0' THEN 1 ELSE 0 END) AS manyi,SUM(CASE WHEN satisfaction='1' OR satisfaction='2' THEN 1 ELSE 0 END) AS bumanyi," +
                                        "SUM(CASE WHEN satisfaction IS NOT NULL THEN 1 ELSE 0 END) AS zj FROM evainstance_ck AS a LEFT JOIN AUDIT_PROJECT AS b ON b.FLOWSN=a.projectno");
                                // 申请时间条件判断
                                if (StringUtil.isNotBlank(startdate)) {
                                    conditionSb.append(" and CreateDATE >= '")
                                            .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate), "yyyy-MM-dd HH:mm:ss")).append("' ");
                                }
                                if(StringUtil.isNotBlank(enddate)){
                                    conditionSb.append("' and CreateDATE <= '")
                                            .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate), "yyyy-MM-dd HH:mm:ss"))
                                            .append("' ");
                                }
                                if(StringUtils.isNotEmpty(evalevel)){
                                    conditionSb.append(" and satisfaction='"+evalevel+"'");
                                }
                                conditionSb.append(" and pf='1'");
                                conditionSb.append(" GROUP BY ouguid");
                                break;
                            case "002":
                                //网厅评价
                                strsql=  " SELECT ouname,ouguid,ACCEPTUSERNAME AS RECEIVEUSERNAME,ACCEPTUSERGUID,"
                                        + " SUM(case when satisfied = '3' OR satisfied = '4' or satisfied = '5' OR satisfied = '0' THEN 1 else 0 END) AS manyi,"
                                        + " SUM(case when satisfied = '1' or satisfied = '2' THEN 1 else 0 END) AS bumanyi,"
                                        + " SUM(case when satisfied is NOT NULL THEN 1 else 0 END) AS zj"
                                        + " from audit_online_evaluat e INNER JOIN audit_project p"
                                        + " on p.RowGuid = e.ClientIdentifier where 1=1"
                                        + " and ACCEPTUSERGUID is NOT NULL " ;
                                if(StringUtil.isNotBlank(centerguid)){
                                    strsql+=" and CENTERGUID='"+centerguid+"'";
                                }
                                if (StringUtil.isNotBlank(startdate)) {
                                    strsql += " and ACCEPTUSERDATE>=str_to_date('" + startdate + "', '%Y-%m-%d %H:%i:%s')";
                                }

                                if (StringUtil.isNotBlank(enddate)) {
                                    strsql += " and ACCEPTUSERDATE<=str_to_date('" + enddate + "', '%Y-%m-%d %H:%i:%s')";
                                }
                                if(StringUtils.isNotEmpty(evalevel)){
                                    strsql += " and satisfied='"+evalevel+"'";
                                }
                                strsql +=   " GROUP BY ouguid";
                                conditionSb.append(strsql);
                                break;
                            case "003":
                                //评价器评价
                                conditionSb.append("SELECT b.ouname,b.ouguid,SUM(CASE WHEN satisfaction='3' OR satisfaction='4' OR satisfaction='5' " +
                                        "OR satisfaction='0' THEN 1 ELSE 0 END) AS manyi,SUM(CASE WHEN satisfaction='1' OR satisfaction='2' THEN 1 ELSE 0 END) AS bumanyi," +
                                        "SUM(CASE WHEN satisfaction IS NOT NULL THEN 1 ELSE 0 END) AS zj FROM evainstance_ck AS a LEFT JOIN AUDIT_PROJECT AS b ON b.FLOWSN=a.projectno");
                                // 申请时间条件判断
                                if (StringUtil.isNotBlank(startdate)) {
                                    conditionSb.append(" and CreateDATE >= '")
                                            .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(startdate), "yyyy-MM-dd HH:mm:ss")).append("' ");
                                }
                                if(StringUtil.isNotBlank(enddate)){
                                    conditionSb.append("' and CreateDATE <= '")
                                            .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(enddate), "yyyy-MM-dd HH:mm:ss"))
                                            .append("' ");
                                }
                                if(StringUtils.isNotEmpty(evalevel)){
                                    conditionSb.append(" and satisfaction='"+evalevel+"'");
                                }
                                conditionSb.append(" and pf='4'");
                                conditionSb.append(" GROUP BY ouguid");
                                break;
                            case "004":
                                break;
                        }
                    }
                    List<AuditProject> list = commservice.findList(conditionSb.toString(), first, pageSize, AuditProject.class);

                    List<AuditProject> list1=commservice.findList(conditionSb.toString(), AuditProject.class);

                    this.setRowCount(list1.size());
                    //控制double类型小数点后的位数（一位）
                    DecimalFormat df = new DecimalFormat( "0.0");
                    for (Iterator<AuditProject> iterator = list.iterator(); iterator.hasNext();) {
                        AuditProject auditProject = (AuditProject) iterator.next();
                        BigDecimal b = new BigDecimal(auditProject.getDouble("manyi"));
                        b=b.divide(new BigDecimal(auditProject.getDouble("zj")),4,BigDecimal.ROUND_DOWN);
                        b=b.multiply(new BigDecimal(100.0));
                        Double d = b.doubleValue();
                        auditProject.put("fmanyilv",d+"%");
//                            auditProject.put("fmanyilv",(df.format(Double.valueOf(1.0*(Integer)auditProject.get("manyi")/(Integer)auditProject.get("zj"))*100)) + "%");
                    }
                    return list;
                }
            };
        }
        return modelall;
    }

    /*public TreeModel getTreeModel() {
        areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
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
                        root.getColumns().put("isOU", "true");// 标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        List<FrameOu> listRootOu;
                        // 当前登陆人员所在部门区域信息
                        AuditOrgaArea areanow = auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                        String areanowOuguid = areanow.getOuguid();
                        // 管理员
                        if ("370800".equals(areacode)) {
                            listRootOu = ouService.listOUByGuid("", 4);
                        }
                        else {
                            listRootOu = ouService.listOUByGuid(areanowOuguid, 4);
                        }

                        // 部门的绑定
                        for (FrameOu frameOu : listRootOu) {
                            TreeNode node = new TreeNode();
                            if (StringUtil.isBlank(frameOu.getParentOuguid())) {
                                FrameOuExtendInfo extendInfo = ouService.getFrameOuExtendInfo(frameOu.getOuguid());
                                node.setId(extendInfo.get("areacode"));
                                node.setText(frameOu.getOuname());
                                if (areanowOuguid.equals(frameOu.getOuguid())) {
                                    node.setPid("f9root");
                                }
                                else {
                                    node.setPid(StringUtil.isBlank(frameOu.getParentOuguid()) ? "f9root"
                                            : frameOu.getParentOuguid());
                                }
                                node.setLeaf(true);
                                list.add(node);
                            }

                        }

                    }

                    return list;
                }
            };
        }

        return treeModel;
    }*/

    public List<SelectItem> getEvalevelModel() {
        if (evalevelModel == null) {
            evalevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评满意度层级", null, true));
//            evalevelModel.remove(1);// 非常满意
//            evalevelModel.remove(1);// 满意

        }
        return this.evalevelModel;
    }

    public List<SelectItem> getEvalevelwayModel() {
        if (evalevelwayModel == null) {
            evalevelwayModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "济宁评价渠道", null, true));

        }
        return evalevelwayModel;
    }

    public ExportModel getExportModel() {
       if (exportModel == null) {
            exportModel = new ExportModel("ouname,zj,manyi,bumanyi,fmanyilv",
                    "部门,总评价数,满意数,不满意数,好评率");
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

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getEvalevel() {
        return evalevel;
    }

    public void setEvalevel(String evalevel) {
        this.evalevel = evalevel;
    }

    public String getEvalevelway() {
        return evalevelway;
    }

    public void setEvalevelway(String evalevelway) {
        this.evalevelway = evalevelway;
    }
}
