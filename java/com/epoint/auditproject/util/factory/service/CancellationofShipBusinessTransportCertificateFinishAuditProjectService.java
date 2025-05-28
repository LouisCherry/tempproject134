package com.epoint.auditproject.util.factory.service;

import com.epoint.auditproject.util.factory.AbstractFinishAuditProjectService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName CancellationofShipBusinessTransportCertificateFinishAuditProjectService
 * @Description《船舶营业运输证》注销
 * @Author rachel
 * @Date 2023/5/26 17:28
 **/

@Component
public class CancellationofShipBusinessTransportCertificateFinishAuditProjectService extends AbstractFinishAuditProjectService {

    @Override
    public void handleBusiness(AuditProject auditProject, AuditTask auditTask) {
        // 《船舶营业运输证》注销
        try {
            log.info(">>>>  船舶营业运输证》注销开始！");
            // 《船舶营业运输证》注销
            if (auditProject == null) {
                log.info("船舶营业运输证》注销，办件办结后事件，办件为空，请检查逻辑！");
                return;
            }
            log.info("船舶营业运输证》注销，办件办结后事件，当前办件唯一标识：" + auditProject.getRowguid() + "！");

            if (auditTask == null) {
                log.info("船舶营业运输证》注销，办件办结后事件，事项为空，请检查逻辑！");
                return;
            }

            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), false).getResult();
            if (auditTaskExtension == null) {
                log.info("船舶营业运输证》注销，办件办结后事件，事项拓展信息为空，请检查逻辑！");
                return;
            }

            String formId = auditTaskExtension.getStr("formid");
            if (StringUtil.isBlank(formId)) {
                log.info("船舶营业运输证》注销，办件办结后事件，事项拓展信息中未配置电子表单Id，请检查逻辑！");
                return;
            }

            // 获取电子表单的数据库表名
            String eFormTableName = super.iCxBusService.getSqlTableNameByFormId(formId);

            if (StringUtil.isBlank(eFormTableName)) {
                log.info("船舶营业运输证》注销，办件办结后事件，未获取到电子Id：" + formId + "获取到表单表名！");
                return;
            }
            String rowguid = auditProject.getRowguid();
            Record record = iCxBusService.getDzbdDetail(eFormTableName, rowguid);

            if (record == null) {
                log.info("船舶营业运输证》注销，办件办结后事件，未根据到表单表名：" + eFormTableName + "、办件唯一标识：" + rowguid + "获取到电子表单信息！");
                return;
            }

            log.info("电子表单信息：" + record);

            // 船舶营业运输证编号
            String cbyyyszbh = record.getStr("cbyyyszbh");
            CertCbyyysz certCbyyysz = iCertCbyyyszService.getYywszBhCertByCertno(record.getStr("cbyyyszbh"));

            if (certCbyyysz == null) {
                log.info("船舶营业运输证》注销，办件办结后事件，未根据船舶营业运输证编号：" + cbyyyszbh + "查询到港航船舶营业运输证本地库数据！");
                return;
            }

            certCbyyysz.setIs_cancel("1");
            certCbyyysz.setIs_enable("0");

            // 默认为已注销
            certCbyyysz.setZxqk("已注销");

            // 注销原因根据对应电子表单字段
            String zxyy = record.getStr("zxyy");
            zxyy = StringUtil.isNotBlank(zxyy) ? zxyy : "电子表单获取到的注销原因为空";
            certCbyyysz.setZxyy(zxyy);

            iCertCbyyyszService.update(certCbyyysz);

            try {
                Record ylgg = new Record();
                ylgg.setSql_TableName("EX_QSXKZ_1");
                ylgg.setPrimaryKeys("rowguid");
                ylgg.set("operatedate", new Date());
                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_QSXKZ_1", record.getStr("cbyyyszbh"));
                log.info("船舶营业运输证ylggrecord：" + ylggrecord);
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "cancel");
                    iJnProjectService.UpdateRecord(ylgg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            log.info(">>>>  船舶营业运输证》注销结束！");
        }
    }

}
