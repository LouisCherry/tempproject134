package com.epoint.auditproject.util.factory;

import com.epoint.auditproject.util.factory.service.CancellationofShipBusinessTransportCertificateFinishAuditProjectService;
import com.epoint.auditproject.util.factory.service.ChangeShipBusinessTransportCertificateFinishAuditProjectService;
import com.epoint.auditproject.util.factory.service.FourTransportCertificateFinishAuditProjectService;
import com.epoint.auditproject.util.factory.service.ReissueShipBusinessTransportCertificateFinishAuditProjectService;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.core.grammar.Record;
import com.epoint.auditproject.util.factory.service.SqhfslService;
import com.epoint.auditproject.util.factory.service.SqzzhwService;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @ClassName FactoryUtil
 * @Description
 * @Author rachel
 * @Date 2023/5/26 17:27
 **/
public class FactoryUtil {

    private static IConfigService configService;

    public static final transient Logger log = Logger.getLogger(AbstractFinishAuditProjectService.class);

    static {
        configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
    }

    private FactoryUtil() {

    }

    public static FinishAuditProjectInterface getFinishAuditProjectService(String taskId) {
        log.info(">>>> 根据taskId:" + taskId + " 尝试匹配处理类！");
        FinishAuditProjectInterface finishAuditProjectInterface = null;

        if (StringUtil.isBlank(taskId)) {
            return finishAuditProjectInterface;
        }
        // 申请终止经营省际普通货物水路运输业务事项
        String sqzzhwTaskId = configService.getFrameConfigValue("sqzzhw_taskid");

        // 申请换发《国内水路运输经营许可证》事项
        String sqhfslTaskId = configService.getFrameConfigValue("sqhfsl_taskid");

        // 申请补发《国内水路运输经营许可证》事项
        String sqbfslTaskId = configService.getFrameConfigValue("sqbfsl_taskid");

        // 《船舶营业运输证》注销事项
        String cbyyyszxTaskId = configService.getFrameConfigValue("cbyyyszx_taskid");

        // 《船舶营业运输证》换发事项
        String cbyyyshfTaskId = configService.getFrameConfigValue("cbyyyshf_taskid");

        // 《船舶营业运输证》补发事项
        String cbyyysbfTaskId = configService.getFrameConfigValue("cbyyysbf_taskid");

        // 光租已取得水路运输经营资格的船舶申领《船舶营业运输证》事项
        String gzuyyysTaskId = configService.getFrameConfigValue("gzuyyys_taskid");

        // 购置已取得水路运输经营资格的船舶申领《船舶营业运输证》事项
        String gzhiyyysTaskId = configService.getFrameConfigValue("gzhiyyys_taskid");

        // 新增客船申领《船舶营业运输证》事项
        String kcyyysTaskId = configService.getFrameConfigValue("kcyyys_taskid");

        // 新增货船申领《船舶营业运输证》事项
        String hcyyysTaskId = configService.getFrameConfigValue("hcyyys_taskid");

        if (taskId.equals(sqzzhwTaskId)) {
            finishAuditProjectInterface = new SqzzhwService();
        }
        else if (taskId.equals(sqhfslTaskId)) {
            finishAuditProjectInterface = new SqhfslService();
        }
        else if (taskId.equals(sqbfslTaskId)) {
            finishAuditProjectInterface = new SqhfslService();
        }
        // 《船舶营业运输证》注销事项
        else if (taskId.equals(cbyyyszxTaskId)) {
            finishAuditProjectInterface = new CancellationofShipBusinessTransportCertificateFinishAuditProjectService();
        }
        // 《船舶营业运输证》换发事项
        else if (taskId.equals(cbyyyshfTaskId)) {
            finishAuditProjectInterface = new ChangeShipBusinessTransportCertificateFinishAuditProjectService();
        }
        // 《船舶营业运输证》补发事项
        else if (taskId.equals(cbyyysbfTaskId)) {
            finishAuditProjectInterface = new ReissueShipBusinessTransportCertificateFinishAuditProjectService();
        }
        // 光租已取得水路运输经营资格的船舶申领《船舶营业运输证》事项、购置已取得水路运输经营资格的船舶申领《船舶营业运输证》事项、新增客船申领《船舶营业运输证》事项、新增货船申领《船舶营业运输证》事项
        else if (taskId.equals(gzuyyysTaskId) || taskId.equals(gzhiyyysTaskId) || taskId.equals(kcyyysTaskId) || taskId.equals(hcyyysTaskId)) {
            finishAuditProjectInterface = new FourTransportCertificateFinishAuditProjectService();
        }

        return finishAuditProjectInterface;
    }

    public static void setDataFromEform(CertCbyyysz certCbyyysz, Record record) {
        certCbyyysz.setOperatedate(new Date());
        certCbyyysz.setCbdjh(record.getStr("cbdjh"));
        certCbyyysz.setCym(record.getStr("cym"));
        certCbyyysz.setCbjyr(record.getStr("cbjyr"));
        certCbyyysz.setJyxkzbh(record.getStr("xkzbh"));
        certCbyyysz.setCjdjh(record.getStr("cjdjh"));
        certCbyyysz.setCbsyr(record.getStr("cbsyr"));
        certCbyyysz.setCbglr(record.getStr("cbglr"));
        certCbyyysz.setGlxkzbh(record.getStr("GLRXKZHM"));
        certCbyyysz.setJcrq(record.getDate("GCRQ"));
        certCbyyysz.setGjrq(record.getDate("GJRQ"));
        certCbyyysz.setBcjyfw(record.getStr("BCJYFW"));
        certCbyyysz.setXkzjyfw(record.getStr("HDJYFW"));
        certCbyyysz.setZcts(record.getStr("ZJ"));
        certCbyyysz.setZjgl(record.getStr("QW"));
        certCbyyysz.setZzd(record.getStr("ZZD"));
        certCbyyysz.setTeu(record.getStr("TEU"));
        certCbyyysz.setLfm(record.getStr("LFM"));
        certCbyyysz.setCw(record.getStr("CW"));
        certCbyyysz.setKw(record.getStr("KW"));
        certCbyyysz.setZwcm(record.getStr("zwcm"));

        // 电子表单已填写的某些字段信息在本地库中未新增显示出来
        String eFormFields = "jd,sfbzcx,xk,xs,zc,zyhl";
        String localEFormFields = "jd,sfbzcx,cbxk,cbxs,cbcd,zyhl";
        String[] eFormFieldArr = eFormFields.split(",");
        String[] localEFormFieldArr = localEFormFields.split(",");
        for (int i = 0; i < localEFormFieldArr.length; i++) {
            String key = localEFormFieldArr[i];
            String value = record.getStr(eFormFieldArr[i]);
            certCbyyysz.put(key, value);
        }

    }

    public static void setDataFromCert(CertCbyyysz certCbyyysz, CertInfoExtension certinfoExtension) {
        String yearz = certinfoExtension.getStr("ZN");
        String monthz = certinfoExtension.getStr("ZY");
        String dayz = certinfoExtension.getStr("ZR");

        certCbyyysz.setYxrq(EpointDateUtil.convertString2DateAuto(yearz + "-" + monthz + "-" + dayz));

        certCbyyysz.setCjg(certinfoExtension.getStr("cjg"));
        certCbyyysz.setFzjg(certinfoExtension.getStr("fzjg"));
        certCbyyysz.setFzrq(certinfoExtension.getDate("fzrq"));
        certCbyyysz.set("cblx", certinfoExtension.getDate("cblx"));
        certCbyyysz.setZzd(certinfoExtension.getStr("zzd"));
        certCbyyysz.setCbcl(certinfoExtension.getStr("CBCL"));
        certCbyyysz.setTeu(certinfoExtension.getStr("TEU"));
        certCbyyysz.set("cbzz", certinfoExtension.getStr("CBZD"));
        certCbyyysz.setLfm(certinfoExtension.getStr("LFM"));
        certCbyyysz.setJcrq(certinfoExtension.getDate("GCRQ"));
        certCbyyysz.setCw(certinfoExtension.getStr("CW"));
        certCbyyysz.setGjrq(certinfoExtension.getDate("GJRQ"));
        certCbyyysz.setZcts(certinfoExtension.getStr("ZJ"));
        certCbyyysz.setZjgl(certinfoExtension.getStr("QW"));
        certCbyyysz.setKw(certinfoExtension.getStr("KW"));
        certCbyyysz.set("hc", certinfoExtension.getStr("HC"));
        certCbyyysz.setXkzjyfw(certinfoExtension.getStr("HDJYFW"));
        certCbyyysz.setBcjyfw(certinfoExtension.getStr("BCJYFW"));
        certCbyyysz.set("bgjl", certinfoExtension.getStr("BGJL"));

        // 电子证照的某些字段信息在本地库中未新增显示出来
        String certFields = "CBDJH,CM,CBZD,BH,GLRXKZHM,CBGLR,CBLX,FZJG";
        String localCertFields = "cbdjh,zwcm,zd,yyzbh,glxkzbh,cbglr,cbzl,fzjg";
        String[] certFieldArr = certFields.split(",");
        String[] localCertFieldArr = localCertFields.split(",");
        for (int i = 0; i < localCertFieldArr.length; i++) {
            String key = localCertFieldArr[i];
            String value = certinfoExtension.getStr(certFieldArr[i]);
            certCbyyysz.put(key, value);
        }
    }

}
