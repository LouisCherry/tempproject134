package com.epoint.auditproject.audithandlecontrol.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.xmz.cxbus.api.ICxBusService;

/**
 * 个性化推送证照数据至浪潮前置库
 */
public class JnGxhSendUtils
{

    protected static final transient Logger log = Logger.getLogger(JnGxhSendUtils.class);

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }

    /**
     * 取水许可审批（设区的市级权限）（首次申请）
     *
     * @param certinfo
     * @param auditProject
     * @param auditTask
     */
    public void SendCertInfoToLc1(CertInfo certinfo, AuditProject auditProject, AuditTask auditTask) {
        IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IAuditOrgaArea areaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", certinfo.getRowguid());
        CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
        if (certinfoExtension == null) {
            certinfoExtension = new CertInfoExtension();
        }

        Record ylgg = new Record();
        ylgg.setSql_TableName("EX_QSXKZS_1");
        ylgg.set("ID", UUID.randomUUID().toString());
        // 照面编号
        ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
        String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
        // 发证时间
        ylgg.set("CERTIFICATE_DATE", rq);
        // 有效期开始
        ylgg.set("VALID_PERIOD_BEGIN", rq);
        // 有效期截止
        ylgg.set("VALID_PERIOD_END", "9999-12-30");

        FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(auditTask.getOuguid());
        if (frameOuExten != null) {
            // 部门编码
            ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
        }

        FrameOu frmaeOu = ouservice.getOuByOuGuid(auditTask.getOuguid());
        if (frmaeOu != null) {
            // 部门名称
            ylgg.set("DEPT_NAME", frmaeOu.getOuname());

        }

        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (areacode.length() == 6) {
            areacode = areacode + "000000";
        }
        else if (areacode.length() == 9) {
            areacode = areacode + "000";
        }
        // 持有者类型
        int APPLYERTYPE = auditProject.getApplyertype();
        if (StringUtil.isNotBlank(APPLYERTYPE)) {
            ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
            if ("20".equals(APPLYERTYPE + "")) {
                ylgg.set("HOLDER_TYPE", "自然人");
            }
        }
        else {
            ylgg.set("HOLDER_TYPE", "--");
        }
        // 持有者名称
        ylgg.set("HOLDER_NAME", auditProject.getApplyername());
        // 持有者证件编号
        ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
        // 持有者证件类型
        ylgg.set("CERTIFICATE_TYPE",
                codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
        // 联系方式
        ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

        // 行政区划名称
        ylgg.set("DISTRICTS_NAME",
                areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult().getXiaquname());
        // 行政区划编码
        ylgg.set("DISTRICTS_CODE", areacode);
        // 事项名称
        ylgg.set("PROJECT_NAME", auditTask.getTaskname());
        // 许可内容
        ylgg.set("PERMIT_CONTENT", "关于" + certinfoExtension.getStr("jsdw") + "取水许可申请准予水行政许可决定书");
        // 可信等级,填写‘A’-----前边永远固定
        ylgg.set("CERTIFICATE_LEVEL", "A");
        // 入库时间
        ylgg.set("operatedate", new Date());
        // 建设单位1
        ylgg.set("JIANSHEDANWEI1", certinfoExtension.getStr("jsdw"));
        // 建设单位2
        ylgg.set("JIANSHEDANWEI2", certinfoExtension.getStr("jsdw"));
        // 取水地址
        ylgg.set("QUSHUIDIZHI", certinfoExtension.getStr("qsdz"));
        // 取水量
        ylgg.set("QUSHUILIANG", certinfoExtension.getStr("ysl"));
        // 用水主要为
        ylgg.set("YONGSHUIZHUYAOWEI", certinfoExtension.getStr("yszyw"));

        ylgg.set("rowguid", UUID.randomUUID().toString());
        ylgg.setPrimaryKeys("rowguid");
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);
        Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_QSXKZS_1", ylgg.getStr("LICENSE_NUMBER"));

        if (ylggrecord != null) {
            ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
            ylgg.set("state", "update");
            iJnProjectService.UpdateRecord(ylgg);
        }
        else {
            ylgg.set("state", "insert");
            iJnProjectService.inserRecord(ylgg);
        }
    }

    /**
     * 公路建设项目施工许可
     *
     * @param certinfo
     * @param auditProject
     * @param auditTask
     */
    public void SendCertInfoToLc2(CertInfo certinfo, AuditProject auditProject, AuditTask auditTask) {
        IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IAuditOrgaArea areaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", certinfo.getRowguid());
        CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
        if (certinfoExtension == null) {
            certinfoExtension = new CertInfoExtension();
        }

        Record ylgg = new Record();
        ylgg.setSql_TableName("EX_GLJSXMSGXK_1");
        ylgg.set("ID", UUID.randomUUID().toString());
        // 照面编号
        ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
        String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
        // 发证时间
        ylgg.set("CERTIFICATE_DATE", rq);
        // 有效期开始
        ylgg.set("VALID_PERIOD_BEGIN", rq);
        // 有效期截止
        ylgg.set("VALID_PERIOD_END", "9999-12-30");

        FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(auditTask.getOuguid());
        if (frameOuExten != null) {
            // 部门编码
            ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
        }

        FrameOu frmaeOu = ouservice.getOuByOuGuid(auditTask.getOuguid());
        if (frmaeOu != null) {
            // 部门名称
            ylgg.set("DEPT_NAME", frmaeOu.getOuname());

        }

        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (areacode.length() == 6) {
            areacode = areacode + "000000";
        }
        else if (areacode.length() == 9) {
            areacode = areacode + "000";
        }
        // 持有者类型
        int APPLYERTYPE = auditProject.getApplyertype();
        if (StringUtil.isNotBlank(APPLYERTYPE)) {
            ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
            if ("20".equals(APPLYERTYPE + "")) {
                ylgg.set("HOLDER_TYPE", "自然人");
            }
        }
        else {
            ylgg.set("HOLDER_TYPE", "--");
        }
        // 持有者名称
        ylgg.set("HOLDER_NAME", auditProject.getApplyername());
        // 持有者证件编号
        ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
        // 持有者证件类型
        ylgg.set("CERTIFICATE_TYPE",
                codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
        // 联系方式
        ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

        // 行政区划名称
        ylgg.set("DISTRICTS_NAME",
                areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult().getXiaquname());
        // 行政区划编码
        ylgg.set("DISTRICTS_CODE", areacode);
        // 事项名称
        ylgg.set("PROJECT_NAME", auditTask.getTaskname());
        // 许可内容
        ylgg.set("PERMIT_CONTENT", certinfoExtension.getStr("xmmc") + "施工许可");
        // 可信等级,填写‘A’-----前边永远固定
        ylgg.set("CERTIFICATE_LEVEL", "A");
        // 入库时间
        ylgg.set("operatedate", new Date());
        // 建设单位
        ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
        // 项目名称1
        ylgg.set("XIANGMUMINGCHEN1", certinfoExtension.getStr("xmmc"));
        // 项目名称2
        ylgg.set("XIANGMUMINGCHEN2", certinfoExtension.getStr("xmmc"));
        // 项目名称3
        ylgg.set("XIANGMUMINGCHEN3", certinfoExtension.getStr("xmmc"));
        ylgg.set("rowguid", UUID.randomUUID().toString());
        ylgg.setPrimaryKeys("rowguid");
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);
        Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_GLJSXMSGXK_1", ylgg.getStr("LICENSE_NUMBER"));

        if (ylggrecord != null) {
            ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
            ylgg.set("state", "update");
            iJnProjectService.UpdateRecord(ylgg);
        }
        else {
            ylgg.set("state", "insert");
            iJnProjectService.inserRecord(ylgg);
        }
    }

    // 施工图审查机构
    public void SendCertInfoToSgt(UserSession userSession, AuditProject auditProject, AuditTask auditTask) {
        ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
        ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IAuditOrgaArea areaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);
        Record record = new Record();
        if ("11370800MB28559184337011700300001".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700300003".equals(auditTask.getItem_id())) {
            record = iCxBusService.getDzbdDetail("formtable20240306165745", auditProject.getRowguid());
        }
        else if ("11370800MB28559184337011700300004".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700300002".equals(auditTask.getItem_id())) {
            record = iCxBusService.getDzbdDetail("formtable20240319102336", auditProject.getRowguid());
        }
        else if ("11370800MB28559184337011700300005".equals(auditTask.getItem_id())) {
            record = iCxBusService.getDzbdDetail("formtable20240319101453", auditProject.getRowguid());
        }
        else if ("11370800MB28559184337011700300006".equals(auditTask.getItem_id())) {
            record = iCxBusService.getDzbdDetail("formtable20240319102835", auditProject.getRowguid());
        }

        if (record != null) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("ex_ssgtscjgrdzs_1");
            ylgg.set("PROJECT_NAME", auditTask.getTaskname());
            ylgg.set("PERMIT_CONTENT", record.getStr("zzlbjdj"));
            ylgg.set("CERTIFICATE_LEVEL", "A");
            ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
            ylgg.set("XIANGXIDIZHI", record.getStr("xxdz"));
            ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
            ylgg.set("ZIZHILEIBIEJIDENGJI", record.getStr("zzlbjdj"));
            ylgg.set("BEIZHU", "");
            ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", "11370800MB28559184");
            ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
            String areacode = ZwfwUserSession.getInstance().getAreaCode();
            if (areacode.length() == 6) {
                areacode = areacode + "000000";
            }
            else if (areacode.length() == 9) {
                areacode = areacode + "000";
            }
            ylgg.set("XINGZHENGQUHUADAIMA", areacode);
            ylgg.set("FADINGDAIBIAOREN", record.getStr("frdb"));

            ylgg.set("operatedate", new Date());
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                // 发证日期
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxq);
                ylgg.set("YOUXIAOQI", yxq);

            }
            int APPLYERTYPE = auditProject.getApplyertype();
            if (StringUtil.isNotBlank(APPLYERTYPE)) {
                ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                if ("20".equals(APPLYERTYPE + "")) {
                    ylgg.set("HOLDER_TYPE", "自然人");
                }
            }
            else {
                ylgg.set("HOLDER_TYPE", "--");
            }
            ylgg.set("HOLDER_NAME", auditProject.getApplyername());
            ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
            ylgg.set("CERTIFICATE_TYPE",
                    codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
            // 联系方式
            ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

            // 证照颁发单位
            ylgg.set("DEPT_NAME", userSession.getOuName());

            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
            if (frameOuExten != null) {
                // 证照办法单位组织机构代码
                ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
            }
            // 行政区划名称
            ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                    .getResult().getXiaquname());
            // 行政区划编码
            ylgg.set("DISTRICTS_CODE", areacode);

            ylgg.set("rowguid", UUID.randomUUID().toString());
            ylgg.setPrimaryKeys("rowguid");

            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_ssgtscjgrdzs_1",
                    ylgg.getStr("LICENSE_NUMBER"));

            if (ylggrecord != null) {
                ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                ylgg.set("state", "update");
                iJnProjectService.UpdateRecord(ylgg);
            }
            else {
                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }
    }

    public void SendCertInfoAxz1(AuditProject auditProject, AuditTask auditTask) {
        ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
        ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IAuditOrgaArea areaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);
        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
        log.info("certrowguid:" + auditProject.get("certrowguid"));
        if (certinfo != null) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JZSGQYAQSCXKZX_1");
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
            if (certinfoExtension == null) {
                certinfoExtension = new CertInfoExtension();
            }
            // 证照编号
            ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
            String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
            String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqs"), "yyyy-MM-dd");
            String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

            // 发证日期x
            ylgg.set("CERTIFICATE_DATE", fzrq);
            // 有效期开始
            ylgg.set("VALID_PERIOD_BEGIN", yxqs);
            // 有效期截止
            ylgg.set("VALID_PERIOD_END", yxqz);

            ylgg.set("PROJECT_NAME", auditTask.getTaskname());
            ylgg.set("PERMIT_CONTENT", "建筑施工");
            ylgg.set("CERTIFICATE_LEVEL", "A");

            ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyxydm"));
            ylgg.set("QIYEMINGCHEN", certinfoExtension.getStr("qymc"));
            ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("frdb"));
            ylgg.set("DANWEIDIZHI", certinfoExtension.getStr("xxdz"));
            ylgg.set("JINGJILEIXING", certinfoExtension.getStr("jjxz"));
            ylgg.set("XUKEFANWEI", "建筑施工");
            ylgg.set("XINGZHENGQUHUADAIMA", auditProject.getAreacode());
            ylgg.set("FDDBRSFZHM_13", auditProject.getLegalid());
            ylgg.set("FDDBRSFZHMLX_14", "111");
            ylgg.set("ERWEIMA", certinfo.getStr("erweima"));
            ylgg.set("ZHENGSHUZHUANGTAI", "有效");
            ylgg.set("ZHENGSHUZHUANGTAIDAIMA", "01");
            ylgg.set("ZHENGSHUZHUANGTAIMIAOSHU", "有效");
            ylgg.set("GUANLIANLEIXING", "关联类型");
            ylgg.set("GUANLIANZHENGZHAOBIAOSHI",
                    StringUtils.isNotBlank(certinfoExtension.getStr("glzzbs")) ? certinfoExtension.getStr("glzzbs")
                            : certinfo.getStr("associatedZzeCertID"));
            ylgg.set("YEWULEIXINGDAIMA", "业务代码");
            ylgg.set("YEWUXINXI", "业务类型");
            ylgg.set("BFJGTYSHXYDM_23", "113700000045030270");
            ylgg.set("SHOUCIFAZHENGRIQI", yxqs);

            String areacode = ZwfwUserSession.getInstance().getAreaCode();
            if (areacode.length() == 6) {
                areacode = areacode + "000000";
            }
            else if (areacode.length() == 9) {
                areacode = areacode + "000";
            }

            ylgg.set("operatedate", new Date());

            int APPLYERTYPE = auditProject.getApplyertype();
            if (StringUtil.isNotBlank(APPLYERTYPE)) {
                ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                if ("20".equals(APPLYERTYPE + "")) {
                    ylgg.set("HOLDER_TYPE", "自然人");
                }
            }
            else {
                ylgg.set("HOLDER_TYPE", "--");
            }
            ylgg.set("HOLDER_NAME", auditProject.getApplyername());
            ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
            ylgg.set("CERTIFICATE_TYPE",
                    codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
            // 联系方式
            ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

            // 证照颁发单位
            ylgg.set("DEPT_NAME", "山东省住房和城乡建设厅");

            // 证照办法单位组织机构代码
            ylgg.set("DEPT_ORGANIZE_CODE", "113700000045030270");

            // 行政区划名称
            ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                    .getResult().getXiaquname());
            // 行政区划编码
            ylgg.set("DISTRICTS_CODE", areacode);

            ylgg.set("rowguid", UUID.randomUUID().toString());
            ylgg.setPrimaryKeys("rowguid");

            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JZSGQYAQSCXKZX_1",
                    ylgg.getStr("LICENSE_NUMBER"));
            if (ylggrecord != null) {
                log.info("rowguid:" + ylggrecord.getStr("rowguid"));
                ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                ylgg.set("state", "update");
                iJnProjectService.UpdateRecord(ylgg);
            }
            else {
                ylgg.set("state", "insert");
                log.info("rowguid:" + ylgg.getStr("rowguid"));
                iJnProjectService.inserRecord(ylgg);
            }
        }
    }

    public void SendCertInfoAxz2(AuditProject auditProject, AuditTask auditTask) {
        ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
        ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IAuditOrgaArea areaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);
        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
        log.info("certrowguid:" + auditProject.get("certrowguid"));
        if (certinfo != null) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JZSGQYAQSCXKZX_1");
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
            if (certinfoExtension == null) {
                certinfoExtension = new CertInfoExtension();
            }
            // 证照编号
            ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
            String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
            String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqs"), "yyyy-MM-dd");
            String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

            // 发证日期x
            ylgg.set("CERTIFICATE_DATE", fzrq);
            // 有效期开始
            ylgg.set("VALID_PERIOD_BEGIN", yxqs);
            // 有效期截止
            ylgg.set("VALID_PERIOD_END", yxqz);

            ylgg.set("PROJECT_NAME", auditTask.getTaskname());
            ylgg.set("PERMIT_CONTENT", "建筑施工");
            ylgg.set("CERTIFICATE_LEVEL", "A");

            ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyxydm"));
            ylgg.set("QIYEMINGCHEN", certinfoExtension.getStr("qymc"));
            ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("frdb"));
            ylgg.set("DANWEIDIZHI", certinfoExtension.getStr("xxdz"));
            ylgg.set("JINGJILEIXING", certinfoExtension.getStr("jjxz"));
            ylgg.set("XUKEFANWEI", "建筑施工");
            ylgg.set("XINGZHENGQUHUADAIMA", auditProject.getAreacode());
            ylgg.set("FDDBRSFZHM_13", auditProject.getLegalid());
            ylgg.set("FDDBRSFZHMLX_14", "111");
            ylgg.set("ERWEIMA", certinfo.getStr("erweima"));
            ylgg.set("ZHENGSHUZHUANGTAI", "有效");
            ylgg.set("ZHENGSHUZHUANGTAIDAIMA", "01");
            ylgg.set("ZHENGSHUZHUANGTAIMIAOSHU", "有效");
            ylgg.set("GUANLIANLEIXING", "关联类型");
            ylgg.set("GUANLIANZHENGZHAOBIAOSHI",
                    StringUtils.isNotBlank(certinfoExtension.getStr("glzzbs")) ? certinfoExtension.getStr("glzzbs")
                            : certinfo.getStr("associatedZzeCertID"));
            ylgg.set("YEWULEIXINGDAIMA", "业务代码");
            ylgg.set("YEWUXINXI", "业务类型");
            ylgg.set("BFJGTYSHXYDM_23", "113700000045030270");
            ylgg.set("SHOUCIFAZHENGRIQI", yxqs);

            String areacode = ZwfwUserSession.getInstance().getAreaCode();
            if (areacode.length() == 6) {
                areacode = areacode + "000000";
            }
            else if (areacode.length() == 9) {
                areacode = areacode + "000";
            }

            ylgg.set("operatedate", new Date());

            int APPLYERTYPE = auditProject.getApplyertype();
            if (StringUtil.isNotBlank(APPLYERTYPE)) {
                ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                if ("20".equals(APPLYERTYPE + "")) {
                    ylgg.set("HOLDER_TYPE", "自然人");
                }
            }
            else {
                ylgg.set("HOLDER_TYPE", "--");
            }
            ylgg.set("HOLDER_NAME", auditProject.getApplyername());
            ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
            ylgg.set("CERTIFICATE_TYPE",
                    codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
            // 联系方式
            ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

            // 证照颁发单位
            ylgg.set("DEPT_NAME", "山东省住房和城乡建设厅");

            // 证照办法单位组织机构代码
            ylgg.set("DEPT_ORGANIZE_CODE", "113700000045030270");

            // 行政区划名称
            ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                    .getResult().getXiaquname());
            // 行政区划编码
            ylgg.set("DISTRICTS_CODE", areacode);

            ylgg.set("rowguid", UUID.randomUUID().toString());
            ylgg.setPrimaryKeys("rowguid");

            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JZSGQYAQSCXKZX_1",
                    ylgg.getStr("LICENSE_NUMBER"));
            if (ylggrecord != null) {
                log.info("rowguid:" + ylggrecord.getStr("rowguid"));
                ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                ylgg.set("state", "update");
                iJnProjectService.UpdateRecord(ylgg);
            }
            else {
                ylgg.set("state", "insert");
                log.info("rowguid:" + ylgg.getStr("rowguid"));
                iJnProjectService.inserRecord(ylgg);
            }
        }
    }

    /**
     * 房屋建筑工程和市政基础设施工程竣工验收备案前置库表
     * @param auditProject
     */
    public void SendCertInfoJgba(AuditProject auditProject) {
        ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class);
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);
        IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        log.info("开始推送房屋建筑和市政基础设施工程竣工验收备案到大数据");
        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
        if (certinfo != null) {
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
            if (certinfoExtension == null) {
                certinfoExtension = new CertInfoExtension();
            }
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_FWJZGCHSZJCSSGCJGYSBAB");
            String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");

            // 设置照面编号（CERTIFICATE_NUMBER）
            ylgg.set("CERTIFICATE_NUMBER",
                    StringUtils.isNotBlank(certinfoExtension.getStr("bh")) ? certinfoExtension.getStr("bh") : "空");

            // 设置发证时间（ISSUE_DATE）
            ylgg.set("ISSUE_DATE", StringUtils.isNotBlank(fzrq) ? fzrq : "空");

            // 设置有效期开始（VALIDITY_START）
            ylgg.set("VALIDITY_START", StringUtils.isNotBlank(fzrq) ? fzrq : "空");

            // 设置有效期截止（VALIDITY_END）
            ylgg.set("VALIDITY_END", "9999-12-30");
            // 设置颁发单位机构代码（ISSUE_DEPT_CODE）
            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(auditProject.getOuguid());
            if (frameOuExten != null) {
                // 证照办法单位组织机构代码
                ylgg.set("ISSUE_DEPT_CODE", frameOuExten.getStr("orgcode"));
            }
            // 设置颁发单位机构名称（ISSUE_DEPT）
            ylgg.set("ISSUE_DEPT", auditProject.getOuname());

            // 设置区划编码（CERTIFICATE_AREA_CODE）
            ylgg.set("CERTIFICATE_AREA_CODE", ZwfwUserSession.getInstance().getAreaCode() + "000000");
            // 设置持有者类型（CERTIFICATE_HOLDER_CATEGORY）
            ylgg.set("CERTIFICATE_HOLDER_CATEGORY", getJgysHolderCator(auditProject.getApplyertype() + ""));

            // 设置持有者名称（CERTIFICATE_HOLDER）
            ylgg.set("CERTIFICATE_HOLDER", auditProject.getApplyername());

            // 设置持有者证件类型（CERTIFICATE_HOLDER_TYPE）
            ylgg.set("CERTIFICATE_HOLDER_TYPE", getJgysHolderType(auditProject.getCerttype()));

            // 设置持有者证件编号（CERTIFICATE_HOLDER_CODE）
            ylgg.set("CERTIFICATE_HOLDER_CODE", auditProject.getCertnum());
            // 设置可信等级（CERTIFICATE_LEVEL）
            ylgg.set("CERTIFICATE_LEVEL", "A");

            // 设置工程名称（KZ_GONGCHENGMINGC）
            ylgg.set("KZ_GONGCHENGMINGC",
                    StringUtils.isNotBlank(certinfoExtension.getStr("gcmc")) ? certinfoExtension.getStr("gcmc") : "空");

            // 设置建设单位名称（KZ_JSDWMC）
            ylgg.set("KZ_JSDWMC",
                    StringUtils.isNotBlank(certinfoExtension.getStr("jsdwmc")) ? certinfoExtension.getStr("jsdwmc")
                            : "空");

            // 设置工程地点（KZ_GONGCHENGDIDIAN）
            ylgg.set("KZ_GONGCHENGDIDIAN",
                    StringUtils.isNotBlank(certinfoExtension.getStr("gcdz")) ? certinfoExtension.getStr("gcdz") : "空");

            // 设置工程用途（KZ_GONGCHENGYONGT）
            ylgg.set("KZ_GONGCHENGYONGT",
                    StringUtils.isNotBlank(certinfoExtension.getStr("gcyt")) ? certinfoExtension.getStr("gcyt") : "空");

            // 设置施工图设计文件审查合格书编号（KZ_SGTSJWJSCHGSBH）
            ylgg.set("KZ_SGTSJWJSCHGSBH",
                    StringUtils.isNotBlank(certinfoExtension.getStr("sgtschgsbh"))
                            ? certinfoExtension.getStr("sgtschgsbh")
                            : "空");

            // 设置施工许可证号（KZ_SHIGONGXUKEZHENGHAO）
            ylgg.set("KZ_SHIGONGXUKEZHENGHAO",
                    StringUtils.isNotBlank(certinfoExtension.getStr("sgxkzh")) ? certinfoExtension.getStr("sgxkzh")
                            : "空");

            // 设置开工日期（KZ_KAIGONGRQ）
            ylgg.set("KZ_KAIGONGRQ",
                    StringUtils.isNotBlank(certinfoExtension.getStr("kgrq")) ? certinfoExtension.getStr("kgrq") : "空");

            // 设置建筑面积（KZ_JIANZHUMIANJI）
            ylgg.set("KZ_JIANZHUMIANJI",
                    StringUtils.isNotBlank(certinfoExtension.getStr("jzmj")) ? certinfoExtension.getStr("jzmj") : "空");

            // 设置竣工验收日期（KZ_JUNGONGYANSHOURQ）
            ylgg.set("KZ_JUNGONGYANSHOURQ",
                    StringUtils.isNotBlank(certinfoExtension.getStr("jgysrq")) ? certinfoExtension.getStr("jgysrq")
                            : "空");

            // 设置结构类型（KZ_JIEGOULEIX）
            ylgg.set("KZ_JIEGOULEIX",
                    StringUtils.isNotBlank(certinfoExtension.getStr("jglx")) ? certinfoExtension.getStr("jglx") : "空");

            // 设置勘察单位名称（KZ_KANCHADANWEIMINGCHEN）
            ylgg.set("KZ_KANCHADANWEIMINGCHEN",
                    StringUtils.isNotBlank(certinfoExtension.getStr("kcdwmc")) ? certinfoExtension.getStr("kcdwmc")
                            : "空");

            // 设置勘察单位资质等级（KZ_KANCHADANWEIZIZHIDENGJI）
            ylgg.set("KZ_KANCHADANWEIZIZHIDENGJI",
                    StringUtils.isNotBlank(certinfoExtension.getStr("kcdwzzdj")) ? certinfoExtension.getStr("kcdwzzdj")
                            : "空");

            // 设置设计单位名称（KZ_SHEJIDANWEIMINGCHEN）
            ylgg.set("KZ_SHEJIDANWEIMINGCHEN",
                    StringUtils.isNotBlank(certinfoExtension.getStr("sjdwmc")) ? certinfoExtension.getStr("sjdwmc")
                            : "空");

            // 设置设计单位资质等级（KZ_SHEJIDANWEIZIZHIDENGJI）
            ylgg.set("KZ_SHEJIDANWEIZIZHIDENGJI",
                    StringUtils.isNotBlank(certinfoExtension.getStr("sjdwzzdj")) ? certinfoExtension.getStr("sjdwzzdj")
                            : "空");

            // 设置施工单位名称（KZ_SHIGONGDANWEIMINGCHEN）
            ylgg.set("KZ_SHIGONGDANWEIMINGCHEN",
                    StringUtils.isNotBlank(certinfoExtension.getStr("sgdwmc")) ? certinfoExtension.getStr("sgdwmc")
                            : "空");

            // 设置施工单位资质等级（KZ_SHIGONGDANWEIZIZHIDENGJI）
            ylgg.set("KZ_SHIGONGDANWEIZIZHIDENGJI",
                    StringUtils.isNotBlank(certinfoExtension.getStr("sgdwzzdj")) ? certinfoExtension.getStr("sgdwzzdj")
                            : "空");

            // 设置监理单位名称（KZ_JIANLIDANWEIMINGCHEN）
            ylgg.set("KZ_JIANLIDANWEIMINGCHEN",
                    StringUtils.isNotBlank(certinfoExtension.getStr("jldwmc")) ? certinfoExtension.getStr("jldwmc")
                            : "空");

            // 设置监理单位资质等级（KZ_JIANLIDANWEIZIZHIDENGJI）
            ylgg.set("KZ_JIANLIDANWEIZIZHIDENGJI",
                    StringUtils.isNotBlank(certinfoExtension.getStr("sgdwzzdj")) ? certinfoExtension.getStr("sgdwzzdj")
                            : "空");

            // 设置工程质量监督机构名称（KZ_GCZHILJIANDJGMC）
            ylgg.set("KZ_GCZHILJIANDJGMC",
                    StringUtils.isNotBlank(certinfoExtension.getStr("gczljdjgmc"))
                            ? certinfoExtension.getStr("gczljdjgmc")
                            : "空");

            // 设置备注（REMARK）
            ylgg.set("REMARK",
                    StringUtils.isNotBlank(certinfoExtension.getStr("")) ? certinfoExtension.getStr("") : "空");
            // 设置更新时间（UPDATE_TIME）
            ylgg.set("UPDATE_TIME", new Date());
            // 数据同步返回信息 默认推送汉字：空
            ylgg.set("RETURN_MSG", "空");
            ylgg.setPrimaryKeys("ID");
            log.info("CERTIFICATE_NUMBER:" + ylgg.getStr("CERTIFICATE_NUMBER"));
            Record ylggrecord = iJnProjectService.getDzbdDetailByfield("EX_FWJZGCHSZJCSSGCJGYSBAB",
                    "CERTIFICATE_NUMBER", ylgg.getStr("CERTIFICATE_NUMBER"));
            if (ylggrecord != null) {
                ylgg.set("ID", ylggrecord.getStr("ID"));
                ylgg.set("STATE", "update");
                iJnProjectService.UpdateRecord(ylgg);
            }
            else {
                ylgg.set("ID", UUID.randomUUID().toString());
                ylgg.set("STATE", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }
        else {
            log.info("certrowguid:" + auditProject.get("certrowguid"));
        }
    }
    /**
     * 映射办件certtype到推送竣工验收到大数据的映射
     * 大数据： 持有者证件类型:516港澳居民来往内地通行证,001统一社会信用代码,099其他法人证件,111居民身份证,999其他自然人证件
     *
     * @param certtype
     * @return
     */
    private String getJgysHolderType(String certtype) {
        String result = "999";
        switch (certtype) {
            case "22":
                result = "111";
            case "25":
                result = "516";
                break;
            case "16":
                result = "001";
                break;
        }
        return result;
    }
    /**
     * 映射办件applytype到推送竣工验收到大数据的映射
     * 大数据： 持有者类型:1-自然人、2-法人、3-混合、4-其他
     *
     * @param applytype
     * @return
     */
    private String getJgysHolderCator(String applytype) {
        String result = "4";
        switch (applytype) {
            case "20":
                result = "1";
            case "10":
                result = "2";
                break;
        }
        return result;
    }

    /**
     * 人力资源服务许可证 （新设/变更）
     *
     * @param certinfo
     * @param auditProject
     * @param auditTask
     */
    public void SendRlzyCertInfo(CertInfo certinfo, AuditProject auditProject, AuditTask auditTask) {
        IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IAuditOrgaArea areaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", certinfo.getRowguid());
        CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
        if (certinfoExtension == null) {
            certinfoExtension = new CertInfoExtension();
        }

        Record ylgg = new Record();
        ylgg.setSql_TableName("EX_RLZYFWXKZ");
        ylgg.set("ID", UUID.randomUUID().toString());
        // 照面编号
        ylgg.set("CERTIFICATE_NUMBER", certinfoExtension.getStr("bh"));

        String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
        // 发证时间
        ylgg.set("ISSUE_DATE", rq);

        // 有效期开始
        ylgg.set("VALIDITY_START", rq);
        // 有效期截止
        ylgg.set("VALIDITY_END", "9999-12-30");

        FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(auditTask.getOuguid());
        if (frameOuExten != null) {
            // 部门编码
            ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
        }

        FrameOu frmaeOu = ouservice.getOuByOuGuid(auditTask.getOuguid());
        if (frmaeOu != null) {
            // 部门名称
            ylgg.set("ISSUE_DEPT", frmaeOu.getOuname());
            // 颁发单位机构代码
            ylgg.set("ISSUE_DEPT_CODE", frmaeOu.getOucode());

        }

        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (areacode.length() == 6) {
            areacode = areacode + "000000";
        }
        else if (areacode.length() == 9) {
            areacode = areacode + "000";
        }
        // 行政区划编码
        ylgg.set("CERTIFICATE_AREA_CODE", areacode);

        // 持有者类型
        int APPLYERTYPE = auditProject.getApplyertype();
        if (StringUtil.isNotBlank(APPLYERTYPE)) {
            if ("20".equals(APPLYERTYPE + "")) {
                ylgg.set("CERTIFICATE_HOLDER_CATEGORY", "1");
            }
            else if ("10".equals(APPLYERTYPE + "")) {
                ylgg.set("CERTIFICATE_HOLDER_CATEGORY", "2");
            }
            else {
                ylgg.set("CERTIFICATE_HOLDER_CATEGORY", "3");
            }
        }
        else {
            ylgg.set("CERTIFICATE_HOLDER_CATEGORY", "4");
        }
        // 持有者名称
        ylgg.set("CERTIFICATE_HOLDER", auditProject.getApplyername());

        // 持有者证件类型
        String certype = codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype());
        // 持有者证件类型:516港澳居民来往内地通行证,001统一社会信用代码,099其他法人证件,111居民身份证,999其他自然人证件
        if (StringUtils.isNotBlank(certype)) {
            switch (certype) {
                case "22":
                    ylgg.set("CERTIFICATE_HOLDER_TYPE", "111");
                    break;
                case "25":
                    ylgg.set("CERTIFICATE_HOLDER_TYPE", "516");
                    break;
                case "16":
                    ylgg.set("CERTIFICATE_HOLDER_TYPE", "001");
                    break;
            }
        }
        // 持有者证件编号
        ylgg.set("CERTIFICATE_HOLDER_CODE", auditProject.getCertnum());

        // 可信等级,填写‘A’-----前边永远固定
        ylgg.set("CERTIFICATE_LEVEL", "A");

        // 照面项：机构名称
        ylgg.set("KZ_JIGOUMINGCHEN", certinfoExtension.getStr("jgmc"));
        // 照面项：统一社会信用代码
        ylgg.set("KZ_TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyshxydm"));
        // 照面项：地址
        ylgg.set("KZ_DIZHI", certinfoExtension.getStr("dz"));
        // 照面项：法定代表人
        ylgg.set("KZ_FADINGDAIBIAOREN", certinfoExtension.getStr("fddbr"));
        // 照面项：机构性质
        ylgg.set("KZ_JIGOUXINGZHI", certinfoExtension.getStr("jgxz"));
        // 照面项：许可文号
        ylgg.set("KZ_XUKEWENHAO", certinfoExtension.getStr("bh"));
        // 照面项：服务范围
        ylgg.set("KZ_FUWUFANWEI", certinfoExtension.getStr("fwfw"));
        String url = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/erweimacommon.html?certguid="
                + certinfo.getRowguid();
        // 照面项：二维码
        ylgg.set("KZ_ERWEIMA", url);

        ylgg.set("rowguid", UUID.randomUUID().toString());
        ylgg.setPrimaryKeys("rowguid");
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);

        // 入库时间
        ylgg.set("operatedate", new Date());
        // 查询方式确认
        Record ylggrecord = iJnProjectService.getDzbdDetailByfield("EX_RLZYFWXKZ", "CERTIFICATE_NUMBER",
                certinfoExtension.getStr("bh"));
        log.info("bh：" + certinfoExtension.getStr("bh"));
        if (ylggrecord != null) {
            ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
            ylgg.set("STATE", "update");
            iJnProjectService.UpdateRecord(ylgg);
        }
        else {
            ylgg.set("STATE", "insert");
            iJnProjectService.inserRecord(ylgg);
        }

    }

    /**
     * 劳务派遣经营许可证 （新设/变更）
     *
     * @param certinfo
     * @param auditProject
     * @param auditTask
     */
    public void SendLwpqjyCertInfo(CertInfo certinfo, AuditProject auditProject, AuditTask auditTask) {
        IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IAuditOrgaArea areaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", certinfo.getRowguid());
        CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
        if (certinfoExtension == null) {
            certinfoExtension = new CertInfoExtension();
        }

        Record ylgg = new Record();
        ylgg.setSql_TableName("EX_LWPQJYXKZ");
        ylgg.set("ID", UUID.randomUUID().toString());
        // 照面编号
        ylgg.set("CERTIFICATE_NUMBER", certinfoExtension.getStr("bh"));

        String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
        String sxrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sxrq"), "yyyy-MM-dd");
        String sxrqi = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sxrqi"), "yyyy-MM-dd");
        // 发证时间
        ylgg.set("ISSUE_DATE", rq);

        // 有效期开始
        ylgg.set("VALIDITY_START", sxrq);
        // 有效期截止
        ylgg.set("VALIDITY_END", sxrqi);

        FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(auditTask.getOuguid());
        if (frameOuExten != null) {
            // 部门编码
            ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
        }

        FrameOu frmaeOu = ouservice.getOuByOuGuid(auditTask.getOuguid());
        if (frmaeOu != null) {
            // 部门名称
            ylgg.set("ISSUE_DEPT", frmaeOu.getOuname());
            // 颁发单位机构代码
            ylgg.set("ISSUE_DEPT_CODE", frmaeOu.getOucode());

        }

        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (areacode.length() == 6) {
            areacode = areacode + "000000";
        }
        else if (areacode.length() == 9) {
            areacode = areacode + "000";
        }
        // 行政区划编码
        ylgg.set("CERTIFICATE_AREA_CODE", areacode);

        // 持有者类型
        int APPLYERTYPE = auditProject.getApplyertype();
        if (StringUtil.isNotBlank(APPLYERTYPE)) {
            if ("20".equals(APPLYERTYPE + "")) {
                ylgg.set("CERTIFICATE_HOLDER_CATEGORY", "1");
            }
            else if ("10".equals(APPLYERTYPE + "")) {
                ylgg.set("CERTIFICATE_HOLDER_CATEGORY", "2");
            }
            else {
                ylgg.set("CERTIFICATE_HOLDER_CATEGORY", "3");
            }
        }
        else {
            ylgg.set("CERTIFICATE_HOLDER_CATEGORY", "4");
        }
        // 持有者名称
        ylgg.set("CERTIFICATE_HOLDER", auditProject.getApplyername());

        // 持有者证件类型
        String certype = codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype());
        // 持有者证件类型:516港澳居民来往内地通行证,001统一社会信用代码,099其他法人证件,111居民身份证,999其他自然人证件
        if (StringUtils.isNotBlank(certype)) {
            switch (certype) {
                case "22":
                    ylgg.set("CERTIFICATE_HOLDER_TYPE", "111");
                    break;
                case "25":
                    ylgg.set("CERTIFICATE_HOLDER_TYPE", "516");
                    break;
                case "16":
                    ylgg.set("CERTIFICATE_HOLDER_TYPE", "001");
                    break;
            }
        }
        // 持有者证件编号
        ylgg.set("CERTIFICATE_HOLDER_CODE", auditProject.getCertnum());

        // 可信等级,填写‘A’-----前边永远固定
        ylgg.set("CERTIFICATE_LEVEL", "A");

        // KZ_BIANHAO 照面项：编号 ------------------固定值：编号
        ylgg.set("KZ_BIANHAO", "编号");
        // KZ_DANWEIMINGCHEN 照面项：单位名称 ------------------固定值：单位名称
        ylgg.set("KZ_DANWEIMINGCHEN", "单位名称");
        // KZ_YOUXIAOQIXIAN 照面项：有效期限 ------------------固定值：有效期限
        ylgg.set("KZ_YOUXIAOQIXIAN", "有效期限");
        // KZ_ZHUSUO 照面项：住所 ------------------固定值：住所
        ylgg.set("KZ_ZHUSUO", "住所");
        // KZ_FADINGDAIBIAOREN 照面项：法定代表人 ------------------固定值：法定代表人
        ylgg.set("KZ_FADINGDAIBIAOREN", "法定代表人");
        // KZ_ZHUCEZIBEN 照面项：注册资本 ------------------固定值：注册资本
        ylgg.set("KZ_ZHUCEZIBEN", "注册资本");
        // KZ_XUKEJINGYINGSHIXIANG 照面项：许可经营事项 ------------------固定值：许可经营事项
        ylgg.set("KZ_XUKEJINGYINGSHIXIANG", "许可经营事项");
        // KZ_FAZHENGJIGUAN 照面项：发证机关 ------------------固定值：发证机关
        ylgg.set("KZ_FAZHENGJIGUAN", "发证机关");
        // KZ_FAZHENGRIQI 照面项：发证日期 ------------------固定值：发证日期
        ylgg.set("KZ_FAZHENGRIQI", "发证日期");

        // KZ_DANWEIMINGCHENSHUJU 照面项：单位名称数据
        ylgg.set("KZ_DANWEIMINGCHENSHUJU", certinfoExtension.getStr("dwmc"));
        // KZ_ZHUSUOSHUJU 照面项：住所数据
        ylgg.set("KZ_ZHUSUOSHUJU", certinfoExtension.getStr("zs"));
        // KZ_FADINGDAIBIAORENSHUJU 照面项：法定代表人数据
        ylgg.set("KZ_FADINGDAIBIAORENSHUJU", certinfoExtension.getStr("fddbr"));
        // KZ_ZHUCEZIBENSHUJU 照面项：注册资本数据
        ylgg.set("KZ_ZHUCEZIBENSHUJU", certinfoExtension.getStr("zczb"));
        // KZ_XUKEJINGYINGSHIXIANGSHUJU 照面项：许可经营事项数据
        ylgg.set("KZ_XUKEJINGYINGSHIXIANGSHUJU", certinfoExtension.getStr("xkjysx"));
        // KZ_ZHI 照面项：至
        ylgg.set("KZ_ZHI", "-");
        // KZ_HEYANNIAN1 照面项：核验年1
        ylgg.set("KZ_HEYANNIAN1", "-");
        // KZ_HEYANNIAN2 照面项：核验年2
        ylgg.set("KZ_HEYANNIAN2", "-");
        // KZ_HEYANNIAN3 照面项：核验年3
        ylgg.set("KZ_HEYANNIAN3", "-");
        // KZ_FUBEN 照面项：副本
        ylgg.set("KZ_FUBEN", "-");
        // KZ_ERWEIMA 照面项：二维码
        String url = "http://218.59.158.56/jnzwdt/jnzwdt/pages/yyyzqrcode/licenseinfo/pages/erweimacommon.html?certguid="
                + certinfo.getRowguid();
        ylgg.set("KZ_ERWEIMA", url);

        ylgg.set("rowguid", UUID.randomUUID().toString());
        ylgg.setPrimaryKeys("rowguid");
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);

        // 入库时间
        ylgg.set("operatedate", new Date());
        // 查询方式确认
        Record ylggrecord = iJnProjectService.getDzbdDetailByfield("EX_LWPQJYXKZ", "CERTIFICATE_NUMBER",
                certinfoExtension.getStr("bh"));
        log.info("bh：" + certinfoExtension.getStr("bh"));
        if (ylggrecord != null) {
            ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
            ylgg.set("STATE", "update");
            iJnProjectService.UpdateRecord(ylgg);
        }
        else {
            ylgg.set("STATE", "insert");
            iJnProjectService.inserRecord(ylgg);
        }
    }
}
