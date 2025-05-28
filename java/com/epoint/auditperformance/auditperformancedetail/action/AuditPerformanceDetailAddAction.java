package com.epoint.auditperformance.auditperformancedetail.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancedetail.doman.AuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancedetail.inter.IAuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordobject.domain.AuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordrule.domain.AuditPerformanceRecordRule;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.domain.AuditPerformanceRecordRuleDetail;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.CompareUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;

/**
 * 考评明细新增页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2018-01-09 10:31:38]
 */
@RestController("auditperformancedetailaddaction")
@Scope("request")
public class AuditPerformanceDetailAddAction extends BaseController
{
    private static final long serialVersionUID = -3873903876058473434L;
    @Autowired
    private IAuditPerformanceDetail service;

    @Autowired
    private IAuditPerformanceRecord recordservice;

    @Autowired
    private IAuditPerformanceRecordRuleService recordruleservice;

    @Autowired
    private IAuditPerformanceRecordRuleDetailService ruledetailservice;

    @Autowired
    private IAuditPerformanceRecordObject objectservice;

    /**
     * 考评明细实体对象
     */
    private AuditPerformanceDetail dataBean = null;

    private FileUploadModel9 fileUploadModel = null;

    private TreeModel detailrulemodel = null;

    private String recordrowguid;

    private List<SelectItem> objectmodel = null;

    private String objectchange;

    private String treechange;

    private String selecttreeguid;
    

    @Override
    public void pageLoad() {
        if (StringUtil.isNotBlank(this.getRequestParameter("detailguid"))) {
            dataBean = service.findDetailByRowguid(this.getRequestParameter("detailguid")).getResult();
            if(dataBean!=null){
                recordrowguid = dataBean.getRecordrowguid();
                selecttreeguid = dataBean.getRecorddetailrulerowguid();

                addCallbackParam("operation", "edit");
                if (!ZwfwConstant.CONSTANT_STR_ONE.equals(dataBean.getDatasourcetype())) {
                    addCallbackParam("datasourcetype", "other");
                }
                if (StringUtil.isBlank(dataBean.getUpdaterecord())) {
                    addCallbackParam("updaterecord", "true");
                }
                else {
                    addCallbackParam("updaterecord", "false");
                }
            }else{
               addCallbackParam("msg","该考评明细以删除"); 
            }
        }
        else {
            dataBean = new AuditPerformanceDetail();
            addCallbackParam("operation", "add");
            recordrowguid = this.getRequestParameter("recordrowguid");
            AuditPerformanceRecord record = recordservice.find(recordrowguid).getResult();
            if (record != null) {
                dataBean.setRecordrowguid(recordrowguid);
                dataBean.setRecordname(record.getRecordname());
                dataBean.setObjecttype(record.getObjecttype());
            }
            else {
                addCallbackParam("msg", "您没有选择考评记录！");
            }
        }
        if (StringUtil.isBlank(dataBean.getGradecliengguid()) && !isPostback()) {
            dataBean.setGradecliengguid(UUID.randomUUID().toString());
            addViewData("cliengguid", dataBean.getGradecliengguid());
        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        if (StringUtil.isNotBlank(dataBean.getRowguid())) {
            //修改的相关数据
            if (detailrulemodel.getSelectNode().size()>0) {
                SelectItem detailitem = detailrulemodel.getSelectNode().get(0);
                String detalruleguid = (String) detailitem.getValue();
                AuditPerformanceRecordRuleDetail ruledetail = ruledetailservice.find(detalruleguid);
                dataBean.setRecordrulerowguid(ruledetail.getRecordrulerowguid());
                dataBean.setRecordrulename(ruledetail.getRecordrulename());
                dataBean.setRecordrulename(ruledetail.getRecordrulename());
                dataBean.setRecorddetailrulerowguid(ruledetail.getRowguid());
                dataBean.setRecorddetailrulename(ruledetail.getDetailrulename());
            }
            dataBean.setObjectname(objectservice.findObjectnameByobjectguid(dataBean.getObjectguid()).getResult());
            //添加历史记录
            String compare = compareBean(dataBean);
            if (StringUtil.isNotBlank(compare)) {
                SimpleDateFormat sfg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sfg.format(new Date());
                String updaterecord = "【" + userSession.getDisplayName() + "】于" + date + "修改" + compare + "<br/>";
                dataBean.setUpdaterecord(
                        (dataBean.getUpdaterecord() == null ? "" : dataBean.getUpdaterecord()) + updaterecord);
            }
            service.updata(dataBean);
            addCallbackParam("msg", "修改成功！");
        }
        else {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            //从考评细则树中获取数据
            SelectItem detailitem = detailrulemodel.getSelectNode().get(0);
            String detalruleguid = (String) detailitem.getValue();
            AuditPerformanceRecordRuleDetail ruledetail = ruledetailservice.find(detalruleguid);
            dataBean.setRecordrulerowguid(ruledetail.getRecordrulerowguid());
            dataBean.setRecordrulename(ruledetail.getRecordrulename());
            dataBean.setRecordrulename(ruledetail.getRecordrulename());
            dataBean.setRecorddetailrulerowguid(ruledetail.getRowguid());
            dataBean.setRecorddetailrulename(ruledetail.getDetailrulename());

            //从对象树中获取数据
            dataBean.setObjectname(objectservice.findObjectnameByobjectguid(dataBean.getObjectguid()).getResult());
            //放入评分人的信息
            dataBean.setGradeuserguid(userSession.getUserGuid());
            dataBean.setGradeusername(userSession.getDisplayName());
            dataBean.setGradeouguid(userSession.getOuGuid());
            dataBean.setGradeouname(userSession.getOuName());
            dataBean.setGradecliengguid(getViewData("cliengguid"));
            dataBean.setGradetime(new Date());
            dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            dataBean.setDatasourcetype(ZwfwConstant.CONSTANT_STR_ONE);
            dataBean.setUsable(ZwfwConstant.CONSTANT_STR_ONE);
            service.insert(dataBean);
            addCallbackParam("msg", "保存成功！");
            dataBean = null;
        }

    }

    //历史版本对比
    public String compareBean(AuditPerformanceDetail dataBean) {
        String result = "";
        //获取老的实体
        AuditPerformanceDetail oldbean = service.findDetailByRowguid(dataBean.getRowguid()).getResult();
        //老的实体和新的作对比
        CompareUtil compareUtil = new CompareUtil();
        Map<String, Object> map = compareUtil.compare2Bean(dataBean, oldbean);
        for (String in : map.keySet()) {
            if ("gradedesc".equals(in)) {
                result += "【评分说明】,修改前" + ":" + map.get(in) + ",修改后:" + dataBean.getGradedesc() + ";";
            }
            else if ("objectname".equals(in)) {
                result += "【考评对象】,修改前" + ":" + map.get(in) + ",修改后:" + dataBean.getObjectname() + ";";
            }
            else if ("recorddetailrulename".equals(in)) {
                result += "【考评细则】,修改前" + ":" + map.get(in) + ",修改后:" + dataBean.getRecorddetailrulename() + ";";
            }
            else if ("score".equals(in)) {
                result += "【得分】,修改前" + ":" + map.get(in) + ",修改后:" + dataBean.getScore() + ";";
            }
        }
        return result;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditPerformanceDetail();
        dataBean.setGradecliengguid(UUID.randomUUID().toString());
        addViewData("cliengguid", dataBean.getGradecliengguid());
    }

    
    public List<SelectItem> getRadiobuttonlist() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem item;
        if (StringUtil.isNotBlank(selecttreeguid)) {
            if (!selecttreeguid.equals(dataBean.getRecorddetailrulerowguid())) {
                dataBean.setScore(null);
            }
            AuditPerformanceRecordRuleDetail ruledetail = ruledetailservice.find(selecttreeguid);
            Integer addscore = Integer.valueOf(ruledetail.getSingleaddscore());
            Integer minuscore = Integer.valueOf(ruledetail.getSingleminusscore());
            if (addscore!=null && addscore!=0) {
                item = new SelectItem();
                item.setText("加分(+" + addscore + ")");
                item.setValue(addscore);
                list.add(item);
            }
            if (minuscore!=null && minuscore!=0) {
                item = new SelectItem();    
                item.setText("减分(-" + minuscore + ")");
                item.setValue("-" + minuscore);
                list.add(item);
            }
        }
        return list;
    }

    public TreeModel getDetailRuleModel() {
        if (detailrulemodel == null) {
            detailrulemodel = new TreeModel()
            {
                private static final long serialVersionUID = 8512995069106971847L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<TreeNode>();
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setId("");
                        root.setPid("-1");
                        // list.add(root);
                        root.setExpanded(true);
                        list.addAll(fetch(root));
                    }
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        TreeNode node;
                        if (StringUtil.isBlank(objectGuid)) {
                            List<AuditPerformanceRecordRule> listrule = recordruleservice
                                    .findFiledByRecordRowguid("rulename,rowguid", dataBean.getRecordrowguid())
                                    .getResult();
                            if (listrule != null && listrule.size() > 0) {
                                Integer num;
                                for (AuditPerformanceRecordRule rule : listrule) {
                                    node = new TreeNode();
                                    node.setId(rule.getRowguid());
                                    node.setText(rule.getRulename());
                                    node.setPid(objectGuid);
                                    node.setLeaf(false);
                                    num = ruledetailservice.findNumByRecordRowguidAndRuleguid(
                                            dataBean.getRecordrowguid(), rule.getRowguid()).getResult();
                                    if (num != null && num > 0) {
                                        list.add(node);
                                        List<AuditPerformanceRecordRuleDetail> listdetail = ruledetailservice
                                                .findFiledByRecordRowguidAndRuleguid("detailrulename,type,rowguid",
                                                        dataBean.getRecordrowguid(), rule.getRowguid())
                                                .getResult();
                                        for (AuditPerformanceRecordRuleDetail auditPerformanceRecordRuleDetail : listdetail) {
                                            node = new TreeNode();
                                            node.setId(auditPerformanceRecordRuleDetail.getRowguid());
                                            node.setText(auditPerformanceRecordRuleDetail.getDetailrulename());
                                            node.setPid(rule.getRowguid());
                                            node.setLeaf(true);
                                            if (!"2".equals(auditPerformanceRecordRuleDetail.getType())) {
                                                list.add(node);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    return list;
                }
            };
        }
        if (StringUtil.isNotBlank(dataBean.getRecorddetailrulename())
                && StringUtil.isNotBlank(dataBean.getRecorddetailrulerowguid())) {
            List<SelectItem> list = new ArrayList<SelectItem>();
            SelectItem item = new SelectItem();
            item.setText(ruledetailservice.find(dataBean.getRecorddetailrulerowguid()).getDetailrulename());
            item.setValue(dataBean.getRecorddetailrulerowguid());
            list.add(item);
            detailrulemodel.setSelectNode(list);
        }
        return detailrulemodel;
    }

    public List<SelectItem> getObejctModel() {
        if(objectmodel==null){
            objectmodel= new ArrayList<SelectItem>();
            List<AuditPerformanceRecordObject> listobject = objectservice
                    .findFieldByRecordRowguid("objectguid,objectname", recordrowguid).getResult();
            SelectItem item;
            if (listobject != null && listobject.size() > 0) {
                for (AuditPerformanceRecordObject object : listobject) {
                    item = new SelectItem();
                    item.setText(object.getObjectname());
                    item.setValue(object.getObjectguid());
                    objectmodel.add(item);
                }
            }
            
        }
        return objectmodel;
   }

    public FileUploadModel9 getUploadmodel() {
        String cliengguid = getViewData("cliengguid");
        if (StringUtil.isNotBlank(dataBean.getGradecliengguid())) {
            cliengguid = dataBean.getGradecliengguid();
        }
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;

    }

    public AuditPerformanceDetail getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceDetail();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceDetail dataBean) {
        this.dataBean = dataBean;
    }

    public IAuditPerformanceDetail getService() {
        return service;
    }

    public void setService(IAuditPerformanceDetail service) {
        this.service = service;
    }

    public String getObjectchange() {
        return objectchange;
    }

    public void setObjectchange(String objectchange) {
        this.objectchange = objectchange;
    }

    public String getTreechange() {
        return treechange;
    }

    public void setTreechange(String treechange) {
        this.treechange = treechange;
    }

    public String getSelecttreeguid() {
        return selecttreeguid;
    }

    public void setSelecttreeguid(String selecttreeguid) {
        this.selecttreeguid = selecttreeguid;
    }

}
