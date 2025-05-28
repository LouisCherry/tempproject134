package com.epoint.auditsp.auditspbasetask.action;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.BlspConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.CodeTreeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 并联审批事项表list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2019-04-25 10:07:31]
 */
@RestController("jnauditspbasetasklistaction")
@Scope("request")
public class JnAuditSpBasetaskListAction extends BaseController
{
    
    private static final long serialVersionUID = 7798753811284837045L;

    @Autowired
    private IAuditSpBasetask service;
    
    @Autowired
    private IAuditSpBasetaskR servicer;

    /**
     * 并联审批事项表实体对象
     */
    private AuditSpBasetask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpBasetask> model;
    
    private String taskname;
    
    private String ouname;

    private String phaseid;
    
    private  List<SelectItem> oumodel;
    //所属区县代码项
    private List<SelectItem> belongToAreaModel;
    private ExportModel exportModel;
    private LazyTreeModal9 treeModel = null;
    
    /**
     * 套餐类型
     */
    private String businessType;

    public void pageLoad() {
        businessType = getRequestParameter("businesstype");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.delAuditSpBasetaskByrowguid(sel);
            //同时删除关联关系
            servicer.delByBasetaskguid(sel);
        }
        addCallbackParam("msg", "删除成功！");
    }

    public DataGridModel<AuditSpBasetask> getDataGridData() {
        //确保加载列表时阶段已经加载完成
        getTreeModel();
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpBasetask>()
            {
           
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditSpBasetask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    if(StringUtil.isNotBlank(taskname)){
                        sqlc.like("taskname", taskname);
                    }
                    if(StringUtil.isNotBlank(ouname)){
                        sqlc.eq("ouname", ouname);
                    }
                    if(StringUtil.isNotBlank(dataBean.getStr("belongto_area"))){
                        sqlc.eq("belongto_area", dataBean.getStr("belongto_area"));
                    }
                    sqlc.eq("businesstype", businessType);
                    if (BlspConstant.BUSSINESS_TYPE_SP.equals(businessType)) {
                        if(StringUtil.isNotBlank(phaseid)){
                            sqlc.eq("phaseid", phaseid);
                        }
                    }
                    
                    sqlc.setOrderDesc("operatedate");
                    PageData<AuditSpBasetask> pagedata = service.getAuditSpBasetaskByPage(sqlc.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    StringBuilder sb = new StringBuilder();
                    StringBuilder exportsb = new StringBuilder();
                    for (AuditSpBasetask auditspbasetask : pagedata.getList()) {
                        sb.setLength(0);
                        exportsb.setLength(0);
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("basetaskguid", auditspbasetask.getRowguid());
                        List<AuditSpBasetaskR> taskrlist = servicer.getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                        int i = 0;
                        for (AuditSpBasetaskR auditSpBasetaskR : taskrlist) {
                            if (i <= 6) {
                                sb.append("(").append(auditSpBasetaskR.getXiaquname()).append(")").append(auditSpBasetaskR.getTaskname()).append("<br/>");
                            }
                            exportsb.append("(").append(auditSpBasetaskR.getXiaquname()).append(")").append(auditSpBasetaskR.getTaskname()).append("；");
                            i++;
                        }
                        if(sb.length()>5){
                            auditspbasetask.set("chooseTaskname", sb.substring(0, sb.length()-5));
                            auditspbasetask.set("export_chooseTaskname",exportsb.toString());
                        }
                    }
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }

            };
        }
        return model;
    }
    
    public LazyTreeModal9 getTreeModel() {
        if(treeModel== null){
            treeModel = new LazyTreeModal9(new CodeTreeHandler("审批阶段",false));
            treeModel.setRootName("审批阶段");
        }
        if(!isPostback()){
            if(DataUtil.convertMap2ComboBox(CodeModalFactory.factory("单选按钮组", "审批阶段", null, false)) != null){
                treeModel.setSelectNode(DataUtil.convertMap2ComboBox(CodeModalFactory.factory("单选按钮组", "审批阶段", null, false)).subList(0, 1));
                phaseid = (String) DataUtil.convertMap2ComboBox(CodeModalFactory.factory("单选按钮组", "审批阶段", null, false)).subList(0, 1).get(0).getValue();
            }
        }
        return treeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getOuModel() {
        if (oumodel == null) {
            oumodel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "审批部门", null, false));
        }
        return this.oumodel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getBelongToAreaModel() {
        if (belongToAreaModel == null) {
            belongToAreaModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "所属区县", null, false));
        }
        return this.belongToAreaModel;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("taskname,taskcode,belongto_area,ouname,is_enable,export_chooseTaskname",
                    "标准事项名称,标准事项编码,所属区县,中央指导业务部门,是否可办,已选事项");
        }
        return exportModel;
    }

    public AuditSpBasetask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBasetask();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpBasetask dataBean) {
        this.dataBean = dataBean;
    }
    
    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getOuname() {
        return ouname;
    }

    public void setOuname(String ouname) {
        this.ouname = ouname;
    }
    
    public String getPhaseid() {
        return phaseid;
    }

    public void setPhaseid(String phaseid) {
        this.phaseid = phaseid;
    }
}
