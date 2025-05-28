package com.epoint.auditproject.util.factory.service;

import com.epoint.auditproject.util.factory.AbstractFinishAuditProjectService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SqhfslService extends AbstractFinishAuditProjectService {
    @Override
    public void handleBusiness(AuditProject auditProject, AuditTask auditTask) {
        if (auditProject != null) {
            AuditTaskExtension extension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
            log.info(String.format("申请换发补发《国内水路运输经营许可证》事项扩展信息：%s", extension));
            if (extension != null) {
                String formid = extension.getStr("formid");
                String formTable = super.getFormTable(formid);
                log.info(String.format("申请换发补发《国内水路运输经营许可证》表单Sqltablename：%s", formTable));
                if (StringUtil.isNotBlank(formTable)) {
                    Record record = iCxBusService.getDzbdDetail(formTable, auditProject.getRowguid());
                    log.info(String.format("申请换发补发《国内水路运输经营许可证》表单数据：%s", record));
                    if (record != null) {
                        CertHwslysjyxk certCbyyysz = iCertHwslysjyxkService.getCertByCertno(record.getStr("xkzbh"));
                        log.info(String.format("申请换发补发《国内水路运输经营许可证》本地库数据：%s", certCbyyysz));
                        if (certCbyyysz != null) {
                            Record ylgg = new Record();
                            ylgg.setPrimaryKeys("rowguid");
                            ylgg.set("operatedate", new Date());
                            certCbyyysz.setFzjg(record.getStr("fzjg"));
                            ylgg.setSql_TableName("ex_sjpthwslysjyxk_1");
                            String certrowguid = auditProject.getCertrowguid();
                            if (StringUtil.isNotBlank(certrowguid) && certrowguid.contains(";")) {
                                certrowguid = certrowguid.split(";")[0];
                            }
                            if (StringUtil.isNotBlank(certrowguid)) {
                                CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
                                if (certInfo != null) {
                                    Map<String, Object> filter = new HashMap<String, Object>(){{
                                        put("certinfoguid", certInfo.getRowguid());
                                    }};
                                    NoSQLSevice noSQLSevice = new NoSQLSevice();
                                    CertInfoExtension certInfoExtension = noSQLSevice.find(CertInfoExtension.class, filter, false);
                                    log.info(String.format("申请换发补发《国内水路运输经营许可证》证照照面信息：%s", certInfoExtension));
                                    if (certInfoExtension != null) {
                                        certCbyyysz.setPzjg(certInfoExtension.getStr("PJJGJWH"));
                                        String yearQ = certInfoExtension.getStr("QN");
                                        String monthQ = certInfoExtension.getStr("QY");
                                        String dayQ = certInfoExtension.getStr("QR");
                                        String yearz = certInfoExtension.getStr("ZN");
                                        String monthz = certInfoExtension.getStr("ZY");
                                        String dayz = certInfoExtension.getStr("ZR");
                                        String year = certInfoExtension.getStr("N");
                                        String month = certInfoExtension.getStr("Y");
                                        String day = certInfoExtension.getStr("R");

                                        certCbyyysz.setKsrq(EpointDateUtil.convertString2DateAuto(yearQ + "-" + monthQ + "-" + dayQ));
                                        certCbyyysz.setJzrq(EpointDateUtil.convertString2DateAuto(yearz + "-" + monthz + "-" + dayz));
                                        certCbyyysz.setFzrq(EpointDateUtil.convertString2DateAuto(year + "-" + month + "-" + day));
                                        // 发证日期
                                        ylgg.set("CERTIFICATE_DATE", year + "-" + month + "-" + day);
                                        // 有效期开始
                                        ylgg.set("VALID_PERIOD_BEGIN", yearQ + "-" + monthQ + "-" + dayQ);
                                        // 有效期截止
                                        ylgg.set("VALID_PERIOD_END", yearz + "-" + monthz + "-" + dayz);
                                    }
                                }
                            }
                            iCertHwslysjyxkService.update(certCbyyysz);
                            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjpthwslysjyxk_1", record.getStr("xkzbh"));
                            log.info(String.format("申请换发补发《国内水路运输经营许可证》大数据中心数据：%s", ylggrecord));
                            if (ylggrecord != null) {
                                ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                                iJnProjectService.UpdateRecord(ylgg);
                            }
                        }
                    }
                }
            }
        }
    }
}
