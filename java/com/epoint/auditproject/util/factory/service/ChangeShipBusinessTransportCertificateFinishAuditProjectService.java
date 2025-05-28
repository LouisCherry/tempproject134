package com.epoint.auditproject.util.factory.service;

import com.epoint.auditproject.util.factory.AbstractFinishAuditProjectService;
import com.epoint.auditproject.util.factory.FactoryUtil;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ChangeShipBusinessTransportCertificateFinishAuditProjectService
 * @Description《船舶营业运输证》换发
 * @Author rachel
 * @Date 2023/5/26 17:28
 **/

@Component
public class ChangeShipBusinessTransportCertificateFinishAuditProjectService extends AbstractFinishAuditProjectService {

    @Override
    public void handleBusiness(AuditProject auditProject, AuditTask auditTask) {
        // 《船舶营业运输证》换发
        try {
            log.info(">>>>  《船舶营业运输证》换发开始！");

            if (auditProject == null) {
                log.info("《船舶营业运输证》换发，办件办结后事件，办件为空，请检查逻辑！");
                return;
            }
            log.info("船舶营业运输证》换发，办件办结后事件，当前办件唯一标识：" + auditProject.getRowguid() + "！");

            if (auditTask == null) {
                log.info("《船舶营业运输证》换发，办件办结后事件，事项为空，请检查逻辑！");
                return;
            }

            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), false).getResult();
            if (auditTaskExtension == null) {
                log.info("《船舶营业运输证》换发，办件办结后事件，事项拓展信息为空，请检查逻辑！");
                return;
            }

            String formId = auditTaskExtension.getStr("formid");
            if (StringUtil.isBlank(formId)) {
                log.info("《船舶营业运输证》换发，办件办结后事件，事项拓展信息中未配置电子表单Id，请检查逻辑！");
                return;
            }

            // 获取电子表单的数据库表名
            String eFormTableName = super.iCxBusService.getSqlTableNameByFormId(formId);

            if (StringUtil.isBlank(eFormTableName)) {
                log.info("《船舶营业运输证》换发，办件办结后事件，未获取到电子Id：" + formId + "获取到表单表名！");
                return;
            }

            String rowGuid = auditProject.getRowguid();
            Record record = iCxBusService.getDzbdDetail(eFormTableName, auditProject.getRowguid());
            if (record == null) {
                log.info("《船舶营业运输证》换发，办件办结后事件，未根据到表单表名：" + eFormTableName + "、办件唯一标识：" + rowGuid + "获取到电子表单信息！");
                return;
            }

            // 船舶营业运输证编号
            String cbyyyszbh = record.getStr("cbyyyszbh");
            CertCbyyysz certCbyyysz = iCertCbyyyszService.getYywszBhCertByCertno(cbyyyszbh);

            if (certCbyyysz == null) {
                log.info("《船舶营业运输证》换发，办件办结后事件，未根据船舶营业运输证编号：" + cbyyyszbh + "查询到港航船舶营业运输证本地库数据！");
                return;
            }

            String certrowguid = auditProject.getStr("certrowguid");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);

            if (certinfo == null) {
                log.info("《船舶营业运输证》换发，办件办结后事件，未根据certrowguid：" + certrowguid + "查询到证照数据！");
                return;
            }

            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter,
                    false);
            if (certinfoExtension == null) {
                log.info("《船舶营业运输证》换发，办件办结后事件，未根据证照信息唯一标识：" + certinfo.getRowguid() + "查询到照面信息数据！");
                certinfoExtension = new CertInfoExtension();
            }

            log.info("电子表单信息：" + record);

            log.info("证照照面信息：" + certinfoExtension);

            // 从证照中获取数据
            // 止年
            String yearz = certinfoExtension.getStr("ZN");
            // 止月
            String monthz = certinfoExtension.getStr("ZY");
            // 止日
            String dayz = certinfoExtension.getStr("ZR");

            // 有效日期
            certCbyyysz.setYxrq(EpointDateUtil.convertString2DateAuto(yearz + "-" + monthz + "-" + dayz));

            //  营运证编号 ->  编号
            certCbyyysz.setYyzbh(certinfoExtension.getStr("bh"));

            //  建成日期 ->  建成日期
            certCbyyysz.setJcrq(EpointDateUtil.convertString2DateAuto(certinfoExtension.getStr("gcrq")));

            //  改建成期 ->  改建日期
            certCbyyysz.setGjrq(EpointDateUtil.convertString2DateAuto(certinfoExtension.getStr("gjrq")));

            // 发证日期 更新为当前日期
            certCbyyysz.setFzrq(new Date());

            log.info(">>>>  本地库信息：" + JsonUtil.objectToJson(certCbyyysz));

            iCertCbyyyszService.update(certCbyyysz);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            log.info(">>>>  《船舶营业运输证》换发结束！");
        }
    }


}
