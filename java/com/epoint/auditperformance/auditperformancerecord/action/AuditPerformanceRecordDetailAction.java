package com.epoint.auditperformance.auditperformancerecord.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordobject.domain.AuditPerformanceRecordObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;

/**
 * 考评记录详情页面对应的后台
 * 
 * @author 14408
 * @version [版本号, 2018-01-09 16:33:20]
 */
@RightRelation(AuditPerformanceRecordListAction.class)
@RestController("auditperformancerecorddetailaction")
@Scope("request")
public class AuditPerformanceRecordDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -2229313516816723363L;
    @Autowired
    private IAuditPerformanceRecord service;
    @Autowired
    private IAuditPerformanceRecordObject objectservice;
    /**
     * 考评记录实体对象
     */
    private AuditPerformanceRecord dataBean = null;
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

    public AuditPerformanceRecord getDataBean() {
        return dataBean;
    }
}
