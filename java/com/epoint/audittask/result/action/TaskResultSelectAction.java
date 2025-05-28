package com.epoint.audittask.result.action;

import java.util.ArrayList;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.external.ICertConfigExternal;
import com.tzwy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
/**
 *  获取多结果action
 *
 * @author hjq
 * @version [版本号, 2021年3月26日]
 */
@RestController("taskresultselectaction")
@Scope("request")
public class TaskResultSelectAction extends BaseController
{

    private static final long serialVersionUID = 1617415106906145393L;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditTaskResult auditResultService;
    @Autowired
    private ICertConfigExternal iCertConfigExternal;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private ICertInfo iCertInfo;
    @Override
    public void pageLoad() {

    }

    /**
     * 拼接结果
     */
    public void getDataJson() {
        List<Record> rtnValue = new ArrayList<Record>();
        //查询办件结果
        AuditTaskResult auditresult = auditResultService.getAuditResultByTaskGuid(getRequestParameter("taskguid"), true).getResult();
       AuditTask auditTask = iAuditTask.getAuditTaskByGuid(getRequestParameter("taskguid"), true).getResult();
        if (auditresult == null ||auditTask==null) {
            this.addCallbackParam("html", JsonUtil.listToJson(rtnValue));
        }
        else {
            AuditProject auditProject= iAuditProject.getAuditProjectByRowGuid("*",getRequestParameter("projectguid"),auditTask.getAreacode()).getResult();
            //查询多个证照
            String[] matertalGuids=auditresult.getSharematerialguid().split(",");
            String[] certrowguids = null;
            if(auditProject!=null && StringUtils.isNotEmpty(auditProject.getCertrowguid())){
                certrowguids = auditProject.getCertrowguid().split(";");
            }
            int i=0;
            for (String matertalGuid : matertalGuids) {
                CertCatalog certCatalog = iCertConfigExternal.getCatalogByCatalogid(
                        matertalGuid, auditTask.getAreacode());
                if(certCatalog!=null){
                    Record record = new Record();
                    record.put("resultName", certCatalog.getCertname());
                    record.put("resultGuid", matertalGuid);
                    if(certrowguids!=null && certrowguids.length>=i+1 && StringUtils.isNotEmpty(certrowguids[i])){
                        CertInfo info = iCertInfo.getCertInfoByRowguid(certrowguids[i]);
                        if (info!=null && auditresult.getSharematerialguid().contains(info.getCertcatalogid())) {
                            record.put("certrowguid", certrowguids[i]);
                        }
                    }
                    rtnValue.add(record);
                }
                i++;
            }
            this.addCallbackParam("html", JsonUtil.listToJson(rtnValue));
        }
    }

}
