package com.epoint.auditperformance.auditperformancerecord.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordobject.domain.AuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.domain.AuditPerformanceRecordRuleDetail;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评记录修改页面对应的后台
 * 
 * @author 14408
 * @version [版本号, 2018-01-09 16:33:20]
 */
@RightRelation(AuditPerformanceRecordListAction.class)
@RestController("auditperformancerecordeditaction")
@Scope("request")
public class AuditPerformanceRecordEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditPerformanceRecord service;

    @Autowired
    private IAuditPerformanceRecordObject objectservice;

    @Autowired
    private IAuditPerformanceRecordRuleDetailService ruleDetailService;

   

    /**
     * 考评记录实体对象
     */
    private AuditPerformanceRecord dataBean = null;

    /**
    * 是否启用单选按钮组model
    */
    private List<SelectItem> ifenabledModel = null;
    /**
     * 考评对象类别下拉列表model
     */
    private List<SelectItem> objecttypeModel = null;
    /**
     * 考评记录状态下拉列表model
     */
    private List<SelectItem> statusModel = null;

    
    @Override
    public void pageLoad() {

        String guid = getRequestParameter("guid");
        dataBean = service.find(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditPerformanceRecord();
        }
        String[] objlist = getSelectObject(dataBean.getRowguid()).split("_");
        String namelist = objlist[0];
        if (StringUtil.isNotBlank(namelist) && StringUtil.isNotBlank(objlist)) {
            addCallbackParam("namelist", namelist);
            addCallbackParam("objlist", getSelectObject(dataBean.getRowguid()));
        }
    }



    /**
     * 保存修改
     * 
     */
    public void save(String objectid, String objectname) {
        //验证是否有重名记录
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
        sql.eq("recordname", dataBean.getRecordname());
        sql.nq("rowguid", dataBean.getRowguid());
        int num = service.getNumByCondition(sql.getMap()).getResult();
        if(num>0){
            addCallbackParam("same","考评记录名称已存在！");
            return;
        }
        String msg = "";
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        String recordrowguid = dataBean.getRowguid();
        if (StringUtil.isNotBlank(objectid) && StringUtil.isNotBlank(objectname)) {
            objectservice.delFieldByRecordRowguid(recordrowguid);
            String[] idlist = objectid.split(",");
            String[] namelist = objectname.split(",");
            AuditPerformanceRecordObject object;
            for (int i = 0; i < idlist.length; i++) {
                object = new AuditPerformanceRecordObject();
                object.setRowguid(UUID.randomUUID().toString());
                object.setOperatedate(new Date());
                object.setRecordname(dataBean.getRecordname());
                object.setRecordrowguid(dataBean.getRowguid());
                object.setObjecttype(dataBean.getObjecttype());
                object.setObjectname(namelist[i]);
                object.setObjectguid(idlist[i]);
                object.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
                objectservice.insert(object);
            }
        }
        else {
            objectservice.delFieldByRecordRowguid(recordrowguid);
        }
        msg = dataBean.getRowguid();
        addCallbackParam("msg", msg);
    }
    /**
     * 验证对象和细则
     * 
     */
    public void validate(String objectid, String objectname) {
        String msg = "";
        if (StringUtil.isNotBlank(dataBean.getRowguid())) {
            if (StringUtil.isBlank(objectid) || StringUtil.isBlank(objectname)) {
                msg = "nullobj";
            }else if (ruleDetailService.findListByRecordRowguid(dataBean.getRowguid()).size()<=0) {
                msg = "nulldetail";
            }else{
                List<AuditPerformanceRecordRuleDetail> details =ruleDetailService.findListByRecordRowguid(dataBean.getRowguid());
                for (AuditPerformanceRecordRuleDetail auditPerformanceRecordRuleDetail : details) {
                    if (auditPerformanceRecordRuleDetail.getSingleaddscore()==0 && auditPerformanceRecordRuleDetail.getSingleminusscore()==0) {
                        msg ="allzero";
                    }
                } 
            }
        }
        addCallbackParam("msg", msg);
    }

    /**
     * 开启考评
     * 
     */
    public void start() {
        dataBean.setStatus("2");
        service.update(dataBean);
    }
    public AuditPerformanceRecord getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditPerformanceRecord dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIfenabledModel() {
        if (ifenabledModel == null) {
            ifenabledModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.ifenabledModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getObjecttypeModel() {
        if (objecttypeModel == null) {
            objecttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评对象类别", null, false));
        }
        return this.objecttypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评记录状态", null, false));
        }
        return this.statusModel;
    }

    public String getSelectObject(String recordguid) {
        String namelist = "";
        String idlist = "";
        String objlist = "";
        if (StringUtil.isNotBlank(recordguid)) {
            List<AuditPerformanceRecordObject> lists = objectservice
                    .findFieldByRecordRowguid("objectname,objectguid", recordguid).getResult();
            String name = "";
            String id = "";
            if (lists != null && lists.size() > 0) {
                for (AuditPerformanceRecordObject list : lists) {
                    name += list.getObjectname() + ";";
                    id += list.getObjectguid() + ";";
                }
                namelist = name;
                idlist = id;
                objlist = namelist + "_" + idlist;
            }
        }
        return objlist;
    }
    
}
