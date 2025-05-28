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
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评记录新增页面对应的后台
 * 
 * @author 14408
 * @version [版本号, 2018-01-09 16:33:20]
 */
@RightRelation(AuditPerformanceRecordListAction.class)
@RestController("auditperformancerecordaddaction")
@Scope("request")
public class AuditPerformanceRecordAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 2307320305227886894L;
    @Autowired
    private IAuditPerformanceRecord service;
    @Autowired
    private IAuditPerformanceRecordObject objectservice;
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
        dataBean = new AuditPerformanceRecord();

    }

    /**
     * 保存并关闭
     * 
     */
    public void add(String objectid, String objectname) {
        String msg = "";
        String same="";
        //验证不重复
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
        sql.eq("recordname", dataBean.getRecordname());
        int num  = service.getNumByCondition(sql.getMap()).getResult();
        if (num > 0) {
            same = "考评记录名称已存在！";
            addCallbackParam("same", same);
            return;
        }
        else {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            dataBean.setIfenabled("1");
            dataBean.setStatus("1");
            service.insert(dataBean);
            if (StringUtil.isNotBlank(objectid) && StringUtil.isNotBlank(objectname)) {
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
            msg = dataBean.getRowguid();
            addCallbackParam("msg", msg);
        }
        
        dataBean = null;
    }

    public AuditPerformanceRecord getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceRecord();
        }
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

}
