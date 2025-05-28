package com.epoint.auditproject.util.factory.service;

import com.epoint.auditproject.util.factory.AbstractFinishAuditProjectService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SqzzhwService extends AbstractFinishAuditProjectService {
    @Override
    public void handleBusiness(AuditProject auditProject, AuditTask auditTask) {
        if (auditProject != null) {
            AuditTaskExtension extension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
            if (extension != null) {
                String formid = extension.getStr("formid");
                if (StringUtil.isNotBlank(formid)) {
                    String formTable = super.getFormTable(formid);
                    if (StringUtil.isNotBlank(formTable)) {
                        Record record = iCxBusService.getDzbdDetail(formTable, auditProject.getRowguid());
                        if (record != null) {
                            CertHwslysjyxk certCbyyysz = iCertHwslysjyxkService.getCertByCertno(record.getStr("wbk1"));
                            if (certCbyyysz != null) {
                                certCbyyysz.setIs_cancel("1");
                                certCbyyysz.setIs_enable("0");
                                certCbyyysz.setZxqk("已注销");
                                iCertHwslysjyxkService.update(certCbyyysz);
                                Record ylgg = new Record();
                                ylgg.setSql_TableName("ex_sjpthwslysjyxk_1");
                                ylgg.setPrimaryKeys("rowguid");
                                ylgg.set("operatedate", new Date());
                                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjpthwslysjyxk_1", record.getStr("wbk1"));
                                log.info("营运注销ylggrecord：" + ylggrecord);
                                if (ylggrecord != null) {
                                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                                    ylgg.set("state", "cancel");
                                    iJnProjectService.UpdateRecord(ylgg);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
