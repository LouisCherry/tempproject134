package com.epoint.auditproject.util.factory.service;

import com.epoint.auditproject.util.factory.AbstractFinishAuditProjectService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName ReissueShipBusinessTransportCertificateFinishAuditProjectService
 * @Description 光租已取得水路运输经营资格的船舶申领《船舶营业运输证》事项、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》事项、新增客船申领《船舶营业运输证》事项、新增货船申领《船舶营业运输证》事项
 * @Author rachel
 * @Date 2023/5/26 17:28
 **/

@Component
public class FourTransportCertificateFinishAuditProjectService extends AbstractFinishAuditProjectService {

    @Override
    public void handleBusiness(AuditProject auditProject, AuditTask auditTask) {
        // 光租已取得水路运输经营资格的船舶申领《船舶营业运输证》
        try {
            log.info(">>>>  光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》开始！");
            if (auditProject == null) {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，办件为空，请检查逻辑！");
                return;
            }

            log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，当前办件唯一标识：" + auditProject.getRowguid() + "！");

            if (auditTask == null) {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，事项为空，请检查逻辑！");
                return;
            }

            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), false).getResult();
            if (auditTaskExtension == null) {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，事项拓展信息为空，请检查逻辑！");
                return;
            }

            String formId = auditTaskExtension.getStr("formid");
            if (StringUtil.isBlank(formId)) {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，事项拓展信息中未配置电子表单Id，请检查逻辑！");
                return;
            }

            // 获取电子表单的数据库表名
            String eFormTableName = super.iCxBusService.getSqlTableNameByFormId(formId);

            if (StringUtil.isBlank(eFormTableName)) {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，未根据到电子表单Id：" + formId + "获取到表单表名！");
                eFormTableName = "cbyyyszsq";
                log.info("使用默认电子表单表名：" + eFormTableName);
            }

            String rowGuid = auditProject.getRowguid();
            Record record = iCxBusService.getDzbdDetail(eFormTableName, auditProject.getRowguid());
            if (record == null) {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，未根据到表单表名：" + eFormTableName + "、办件唯一标识：" + rowGuid + "获取到电子表单信息！");
                return;
            }
            CertInfo certinfo = null;
            String certrowguid = auditProject.getStr("certrowguid");
            try {
                certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
            }
            catch (Exception e) {
                log.error(">>>>  获取证照信息时出现异常！！");
                e.printStackTrace();
            }

            if (certinfo == null) {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，未根据certrowguid：" + certrowguid + "查询到证照数据！");
                return;
            }

            CertCbyyysz cbyy = new CertCbyyysz();
            cbyy.setRowguid(UUID.randomUUID().toString());
            cbyy.setOperatedate(new Date());
            cbyy.setCbdjh(record.getStr("cbdjh"));
            cbyy.setCym(record.getStr("cym"));
            cbyy.setCbjyr(record.getStr("cbjyr"));
            cbyy.setJyxkzbh(record.getStr("xkzbh"));
            cbyy.setCjdjh(record.getStr("cjdjh"));
            cbyy.setCbsyr(record.getStr("cbsyr"));
            cbyy.setCbglr(record.getStr("cbglr"));
            cbyy.setGlxkzbh(record.getStr("GLRXKZHM"));
            cbyy.setJcrq(record.getDate("GCRQ"));
            cbyy.setGjrq(record.getDate("GJRQ"));
            cbyy.setBcjyfw(record.getStr("BCJYFW"));
            cbyy.setXkzjyfw(record.getStr("HDJYFW"));
            cbyy.setZcts(record.getStr("ZJ"));
            cbyy.setZjgl(record.getStr("QW"));
            cbyy.setZzd(record.getStr("ZZD"));
            cbyy.setTeu(record.getStr("TEU"));
            cbyy.setLfm(record.getStr("LFM"));
            cbyy.setCw(record.getStr("CW"));
            cbyy.setKw(record.getStr("KW"));
            cbyy.setZwcm(record.getStr("zwcm"));

            cbyy.setIs_enable("1");
            cbyy.setIs_cancel("0");

            // 电子表单已填写的某些字段信息在本地库中未新增显示出来
            String eFormFields = "jd,xk,xs,zc,zyhl,slyyzyj";
            String localEFormFields = "jd,cbxk,cbxs,cbcd,zyhl,xgzsh";
            String[] eFormFieldArr = eFormFields.split(",");
            String[] localEFormFieldArr = localEFormFields.split(",");
            for (int i = 0; i < localEFormFieldArr.length; i++) {
                String key = localEFormFieldArr[i];
                String value = record.getStr(eFormFieldArr[i]);
                cbyy.put(key, value);
            }

            // 是否标准船型
            String sfbzcx = record.getStr("sfbzcx");
            sfbzcx = "是".equals(sfbzcx) ? "1" : "0";
            cbyy.put("sfbzcx", sfbzcx);

            // 是否自有船舶
            String sfzycb = record.getStr("sfzycb");
            sfzycb = "是".equals(sfzycb) ? "1" : "0";
            cbyy.put("sfzycb", sfzycb);

            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
            if (certinfoExtension == null) {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，未根据证照信息唯一标识：" + certinfo.getRowguid() + "查询到照面信息数据！");
                certinfoExtension = new CertInfoExtension();
            }

            log.info("电子表单信息：" + record);

            log.info("证照照面信息：" + certinfoExtension);

            String yearz = certinfoExtension.getStr("ZN");
            String monthz = certinfoExtension.getStr("ZY");
            String dayz = certinfoExtension.getStr("ZR");

            cbyy.setCjg(certinfoExtension.getStr("cjg"));
            cbyy.setFzjg(certinfoExtension.getStr("fzjg"));
            cbyy.setFzrq(certinfoExtension.getDate("fzrq"));
            cbyy.set("cblx", certinfoExtension.getDate("cblx"));
            cbyy.setZzd(certinfoExtension.getStr("zzd"));
            cbyy.setCbcl(certinfoExtension.getStr("CBCL"));
            cbyy.setTeu(certinfoExtension.getStr("TEU"));
            cbyy.set("cbzz", certinfoExtension.getStr("CBZD"));
            cbyy.setLfm(certinfoExtension.getStr("LFM"));
            cbyy.setJcrq(certinfoExtension.getDate("GCRQ"));
            cbyy.setCw(certinfoExtension.getStr("CW"));
            cbyy.setGjrq(certinfoExtension.getDate("GJRQ"));
            cbyy.setZcts(certinfoExtension.getStr("ZJ"));
            cbyy.setZjgl(certinfoExtension.getStr("QW"));
            cbyy.setKw(certinfoExtension.getStr("KW"));
            cbyy.set("hc", certinfoExtension.getStr("HC"));
            cbyy.setXkzjyfw(certinfoExtension.getStr("HDJYFW"));
            cbyy.setBcjyfw(certinfoExtension.getStr("BCJYFW"));
            cbyy.set("bgjl", certinfoExtension.getStr("BGJL"));

            cbyy.setYxrq(EpointDateUtil.convertString2DateAuto(yearz + "-" + monthz + "-" + dayz));

            // 电子证照的某些字段信息在本地库中未新增显示出来
            String certFields = "CBDJH,CM,CBZD,BH,GLRXKZHM,CBGLR,CBLX,FZJG";
            String localCertFields = "cbdjh,zwcm,zd,yyzbh,glxkzbh,cbglr,cbzl,fzjg";
            String[] certFieldArr = certFields.split(",");
            String[] localCertFieldArr = localCertFields.split(",");
            for (int i = 0; i < localCertFieldArr.length; i++) {
                String key = localCertFieldArr[i];
                String value = certinfoExtension.getStr(certFieldArr[i]);
                cbyy.put(key, value);
            }

            // 本地库根据经营许可证编号需要获取的信息：内河沿海，经营区域，发证机构没有获取过来
            CertHwslysjyxk certJyxkzbh = iCertHwslysjyxkService.getCertByCertno(cbyy.getJyxkzbh());
            if (certJyxkzbh != null) {
                // 内河沿海
                cbyy.put("nhyh", certJyxkzbh.getStr("nhyh"));
                // 经营区域
                cbyy.put("jyfw", certJyxkzbh.getStr("jyqy"));
                // 发证机构
                cbyy.put("fzjg", certJyxkzbh.getStr("fzjg"));
            }
            else {
                log.info("光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》，办件办结后事件，未根据经营许可证编号：" + cbyy.getJyxkzbh() + "查询到水路运输许可证数据！");
            }

            //新增到公共许可本地库
            iCertCbyyyszService.insert(cbyy);
        }
        catch (Exception e) {
            log.error(">>>> 光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》出现异常！");
            e.printStackTrace();
        }
        finally {
            log.info(">>>>  光租已取得水路运输经营资格的船舶申领《船舶营业运输证》、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》、新增客船申领《船舶营业运输证》、新增货船申领《船舶营业运输证》结束！");
        }
    }

}
