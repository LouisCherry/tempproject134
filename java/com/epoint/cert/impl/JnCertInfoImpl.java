package com.epoint.cert.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.core.utils.code.DESEncrypt;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcghxkxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcjgysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcghxkxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcjgysbaxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZrztxxbV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.cert.api.IJnCertInfo;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.util.CertCheckUtil;
import com.epoint.cert.util.CertCheckUtilsNew;
import com.epoint.cert.util.PushTokenUtil;
import com.epoint.cert.util.PushUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.xmz.cxbus.api.ICxBusService;

/**
 * 一业一证基本信息实现类
 *
 * @author shibin
 * @description
 * @date 2020年5月19日 下午2:52:35
 */
@Component
@Service
public class JnCertInfoImpl implements IJnCertInfo {

    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(JnCertInfoImpl.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public List<CertInfo> getUnPublishCertInfo() {
        return new IJnCertInfoService().getUnPublishCertInfo();
    }

    @Override
    public Record getCertInfoByTyshxydm(String tyshxydm) {
        return new IJnCertInfoService().getCertInfoByTyshxydm(tyshxydm);
    }

    @Override
    public AuditProject getProjectByCertnum(String certnum) {
        return new IJnCertInfoService().getProjectByCertnum(certnum);
    }

    /**
     * 推送证照
     * @param fieldNames
     * @param values
     * @param certInfo
     * @param ifgjfm
     * @return
     */
    public CertInfo pushfuma(String[] fieldNames, Object[] values, CertInfo certInfo, boolean ifgjfm) {
        ICertConfigExternal iCertConfigExternal = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo()
                .getComponent(ICodeItemsService.class);
        String areacode = "";
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        CertCatalog certCatalog = iCertConfigExternal.getCatalogByCatalogid(certInfo.getCertcatalogid(), areacode);
        log.info("certCatalog" + certCatalog.getRowguid());
        // 是否推送证照
        if ("1".equals(certCatalog.get("is_push"))) {
            if(StringUtils.isNotBlank(certCatalog.get("pushcerttype"))){
                String codename = iCodeItemsService.getItemTextByCodeName("推送证照类型",certCatalog.get("pushcerttype"));
                //推送证照类型
                if(StringUtils.isNotBlank(codename)){
                    switch (codename){
                        case "建筑施工企业安全生产许可证":
                            certInfo = pushanxu(fieldNames,values,certInfo,ifgjfm);
                            break;
                        case "房屋建筑和市政基础设施工程竣工验收备案":
                            certInfo = pushjgys(fieldNames,values,certInfo,ifgjfm);
                            break;
                        default:
                            break;
                    }
                }else{
                    log.info(certCatalog.get("pushcerttype"));
                }
            }
        }else{
            log.info("no is_push");
        }
        return certInfo;
    }
    
    /**
     * 推送安许
     *
     * @param ifgjfm 是否归集赋码
     */
    @Override
    public CertInfo pushanxu(String[] fieldNames, Object[] values, CertInfo certInfo, boolean ifgjfm) {
        ICertCatalog iCertCatalog = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);

        IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

        IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

        ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);

        ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);

        ICertConfigExternal iCertConfigExternal = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        if (certInfo != null && StringUtils.isNotBlank(certInfo.getCertcatalogid())) {
            String areacode = "";
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                    .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
            } else {
                areacode = ZwfwUserSession.getInstance().getAreaCode();
            }
            CertCatalog certCatalog = iCertConfigExternal.getCatalogByCatalogid(certInfo.getCertcatalogid(), areacode);
            log.info("certCatalog:" + certCatalog.getRowguid());
            String qrcoderas = "";
            StringBuffer pushText = new StringBuffer();
            // 插入图片（包括二维码）
            String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
            // 是否安许推送证照
            if ("1".equals(certCatalog.get("is_push"))) {
                log.info("===========进入个性化推送方法=======");
                // 个性化操作，针对安许证不走校验，直接走赋码
                String gxh = certInfo.getStr("gxh");
                if ("1".equals(gxh)) {
                    // 直接走赋码接口
                    String certtype = certCatalog.get("pushcerttype");
                    String projectGuid = certInfo.get("projectguid");
                    log.info("certtype" + certtype + "projectGuid" + projectGuid);
                    if (StringUtil.isNotBlank(certtype) && StringUtil.isNotBlank(projectGuid)) {
                        // 获取token
                        String token = PushTokenUtil.getCertToken();
                        String certGuid = certInfo.getRowguid();
                        // 获取办件
                        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectGuid, "").getResult();
                        if (StringUtil.isNotBlank(token)) {
                            if (auditProject != null) {
                                // 校验
                                if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getAreaCode())) {
                                    // 统一社会信用代码
                                    String creditCode = "";
                                    String ecertid = "";
                                    for (int i = 0; i < fieldNames.length; i++) {
                                        if ("统一社会信用代码".equals(fieldNames[i])) {
                                            // 统一社会信用代码
                                            creditCode = values[i].toString();
                                        }
                                        if ("原业务ID".equals(fieldNames[i])) {
                                            // 原业务ID
                                            ecertid = values[i].toString();
                                        }
                                    }

                                    String type = "0";
                                    Record info_new = getCertInfoByTyshxydm(creditCode);
                                    if (info_new != null) {
                                        type = "1";
                                    }

                                    // 成功和预警进行下一步
                                    JSONObject obj = new JSONObject();
                                    obj.put("eCertID", ecertid);
                                    obj.put("provinceNum", "370000");
                                    obj.put("issuAuth", "山东省住房和城乡建设厅");
                                    obj.put("issuAuthCode", "113700000045030270");
                                    // 发证日期
                                    obj.put("issuDate", EpointDateUtil.convertDate2String(certInfo.getAwarddate(),
                                            EpointDateUtil.DATE_FORMAT));
                                    String citynum = "";
                                    // 所属地市
                                    if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getAreaCode())) {
                                        citynum = ZwfwUserSession.getInstance().getAreaCode().substring(0, 6);
                                    }

                                    // 从业务数据中区
                                    // 详细地址
                                    String CertNum = "";
                                    String certnum = "";
                                    for (int i = 0; i < fieldNames.length; i++) {
                                        if ("证书编号".equals(fieldNames[i])) {
                                            // 证书编号
                                            obj.put("certNum", values[i]);
                                            CertNum = values[i].toString();
                                            certnum = values[i].toString();
                                        }
                                        if ("企业名称".equals(fieldNames[i])) {
                                            // 企业名称
                                            obj.put("companyName", values[i]);
                                        }
                                        if ("统一社会信用代码".equals(fieldNames[i])) {
                                            // 统一社会信用代码
                                            obj.put("creditCode", values[i]);
                                        }
                                        if ("详细地址".equals(fieldNames[i])) {
                                            // 单位地址
                                            obj.put("companyAddress", values[i]);
                                        }
                                        if ("经济性质".equals(fieldNames[i])) {
                                            // 经济类型
                                            obj.put("economicType", values[i]);
                                        }
                                        if ("法人代表".equals(fieldNames[i])) {
                                            // 法定代表人
                                            obj.put("responsiblePerson", values[i]);
                                            if (StringUtil.isBlank(values[i])) {
                                                // 法定代表人
                                                obj.put("responsiblePerson", auditProject.getLegal());
                                            }
                                        }
                                        if ("有效期始".equals(fieldNames[i])) {
                                            String str = values[i].toString();
                                            String regEx = "[^0-9]";
                                            Pattern p = Pattern.compile(regEx);
                                            Matcher m = p.matcher(str);
                                            String result = m.replaceAll("").trim();
                                            // 有效期起始日期
                                            obj.put("effectiveDate", result.substring(0, 4) + "-"
                                                    + result.substring(4, 6) + "-" + result.substring(6, 8));
                                        }
                                        if ("有效期止".equals(fieldNames[i])) {
                                            String str = values[i].toString();
                                            String regEx = "[^0-9]";
                                            Pattern p = Pattern.compile(regEx);
                                            Matcher m = p.matcher(str);
                                            String result = m.replaceAll("").trim();
                                            // 有效期起始日期
                                            obj.put("expiringDate", result.substring(0, 4) + "-"
                                                    + result.substring(4, 6) + "-" + result.substring(6, 8));
                                        }
                                        if ("首次发证日期".equals(fieldNames[i])) {
                                            String str = values[i].toString();
                                            String regEx = "[^0-9]";
                                            Pattern p = Pattern.compile(regEx);
                                            Matcher m = p.matcher(str);
                                            String result = m.replaceAll("").trim();
                                            // 有效期起始日期
                                            obj.put("firstIssuDate", result.substring(0, 4) + "-"
                                                    + result.substring(4, 6) + "-" + result.substring(6, 8));
                                        }
                                    }
                                    // 法定代表人身份证件号码
                                    obj.put("responsiblePersonID", auditProject.getLegalid());
                                    // 法定代表人身份证件号码类型
                                    obj.put("responsiblePersonIDType", "111");
                                    // 许可范围
                                    obj.put("licenseScope", "建筑施工");

                                    AuditTask audittask = iAuditTask
                                            .getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();

                                    // 证书状态
                                    // String status = "99";
                                    // String statusText = "";
                                    // if
                                    // (StringUtil.isNotBlank(certInfo.getStatus()))
                                    // {
                                    // switch (certInfo.getStatus()) {
                                    // case "10":
                                    // status = "01";
                                    // break;
                                    // case "20":
                                    // statusText = "年检";
                                    // break;
                                    // case "30":
                                    // statusText = "过期";
                                    // break;
                                    // case "40":
                                    // statusText = "挂失";
                                    // break;
                                    // case "50":
                                    // status = "05";
                                    // break;
                                    // case "60":
                                    // statusText = "草稿";
                                    // break;
                                    // }
                                    // }
                                    //
                                    // // 如果是注销事项，更新status
                                    // if
                                    // ("11370800MB285591843370117002017".equals(audittask.getItem_id()))
                                    // {
                                    // status = "05";
                                    // certInfo.setIshistory(1);// 注销二维码
                                    // certInfo.setRemark("建筑注销事项证照");
                                    // }
                                    // obj.put("certState", status);
                                    // // 证书状态描述
                                    // if("99".equals(status)){
                                    // obj.put("certStatusDescription", "其他");
                                    // }else{
                                    // obj.put("certStatusDescription",
                                    // statusText);
                                    // }

                                    Record record = iCxBusService.getDzbdDetail("formtable20220422102306",
                                            auditProject.getRowguid());

                                    if (record != null) {
                                        String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                        Integer idnum = record.getInt("idnum");
                                        idnum = idnum + 3623;
                                        String idnumv = String.valueOf(idnum);

                                        String code = CertCheckUtil.getCheckCode(
                                                "11100000000013338W050113700000045030270" + year + idnumv + "001");
                                        String associatedZzeCertID = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                + year + idnumv + ".001." + code;

                                        // 这两事项不推associatedZzeCertID
                                        if (!"11370800MB285591843370117002011".equals(audittask.getItem_id())
                                                && !"11370800MB285591843370117002016".equals(audittask.getItem_id())) {
                                            // 关联证照标识
                                            obj.put("associatedZzeCertID", associatedZzeCertID);
                                        }
                                        certInfo.set("associatedZzeCertID", associatedZzeCertID);
                                    }

                                    // 新申请
                                    if ("11370800MB285591843370117002011".equals(audittask.getItem_id())
                                            || "11370800MB28559184300011711200001".equals(audittask.getItem_id())
                                            || "11370800MB285591843370117002016".equals(audittask.getItem_id())) {
                                        String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");

                                        if (CertNum.length() > 6) {
                                            CertNum = CertNum.substring(CertNum.length() - 6, CertNum.length());
                                        }

                                        if (certnum.length() > 14) {
                                            // 安许证截取证照年份
                                            // 例如：（鲁）JZ安许证字[2023]087974
                                            // 截取中括号中的数字
                                            year = certnum.substring(10, 14);
                                        }

                                        CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                        String code = utils.getXy(
                                                "11100000000013338W050113700000045030270" + year + CertNum + "001");

                                        String associatedZzeCertID = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                + year + CertNum + ".001." + code;

                                        // 这两事项不推associatedZzeCertID
                                        if (!"11370800MB285591843370117002011".equals(audittask.getItem_id())
                                                && !"11370800MB285591843370117002016".equals(audittask.getItem_id())) {
                                            // 关联证照标识
                                            obj.put("associatedZzeCertID", associatedZzeCertID);
                                        }
                                        certInfo.set("associatedZzeCertID", associatedZzeCertID);
                                    }
                                    // 延续，变更逻辑一致
                                    else if ("11370800MB285591843370117002013".equals(audittask.getItem_id())
                                            || "11370800MB28559184300011711200002".equals(audittask.getItem_id())
                                            || "11370800MB285591843370117002014".equals(audittask.getItem_id())
                                            || "11370800MB28559184300011711200003".equals(audittask.getItem_id())
                                            || "11370800MB285591843370117002015".equals(audittask.getItem_id())
                                            || "11370800MB285591843370117002024".equals(audittask.getItem_id())
                                            || "11370800MB285591843370117002027".equals(audittask.getItem_id())
                                            || "11370800MB285591843370117002026".equals(audittask.getItem_id())
                                            || "11370800MB285591843370117002025".equals(audittask.getItem_id())
                                            || "11370800MB28559184300011711200004".equals(audittask.getItem_id())) {
                                        if (info_new != null) {
                                            String associatedZzeCertID = info_new.getStr("GUANLIANZHENGZHAOBIAOSHI");
                                            obj.put("associatedZzeCertID", associatedZzeCertID);
                                            if (StringUtil.isNotBlank(associatedZzeCertID)) {
                                                associatedZzeCertID = associatedZzeCertID.substring(
                                                        associatedZzeCertID.length() - 5, associatedZzeCertID.length());
                                                // 取版本号
                                                String bbh = associatedZzeCertID.substring(0, 3);
                                                if (StringUtil.isNotBlank(bbh)) {
                                                    int bbh_new = Integer.parseInt(bbh) + 1;
                                                    String bbh1 = String.valueOf(bbh_new);
                                                    if (bbh1.length() == 1) {
                                                        bbh = "00" + bbh1;
                                                    } else if (bbh1.length() == 2) {
                                                        bbh = "0" + bbh1;
                                                    } else if (bbh1.length() == 3) {
                                                        bbh = bbh1;
                                                    }

                                                    String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                                    String zzbh = "";
                                                    if (certnum.length() > 6) {
                                                        zzbh = certnum.substring(certnum.length() - 6,
                                                                certnum.length());
                                                    }

                                                    if (certnum.length() > 14) {
                                                        // 安许证截取证照年份
                                                        // 例如：（鲁）JZ安许证字[2023]087974
                                                        // 截取中括号中的数字
                                                        year = certnum.substring(10, 14);
                                                    }

                                                    CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                                    String code = utils.getXy("11100000000013338W050113700000045030270"
                                                            + year + zzbh + bbh);
                                                    String associatedZzeCertID_new = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                            + year + zzbh + "." + bbh + "." + code;
                                                    certInfo.set("associatedZzeCertID", associatedZzeCertID_new);
                                                }
                                            }
                                            // 如果前置库所取的记录没对应值，按新申请重新生成一个值
                                            else {
                                                String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");

                                                String zzbh = "";

                                                if (certnum.length() > 6) {
                                                    zzbh = certnum.substring(certnum.length() - 6, certnum.length());
                                                }

                                                if (certnum.length() > 14) {
                                                    // 安许证截取证照年份
                                                    // 例如：（鲁）JZ安许证字[2023]087974
                                                    // 截取中括号中的数字
                                                    year = certnum.substring(10, 14);
                                                }

                                                CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                                String code = utils.getXy("11100000000013338W050113700000045030270"
                                                        + year + zzbh + "001");

                                                String associatedZzeCertID_new = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                        + year + zzbh + ".001." + code;
                                                certInfo.set("associatedZzeCertID", associatedZzeCertID_new);
                                            }
                                        }
                                        // 前置库都是空的，重新生成值
                                        else {
                                            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                            String zzbh = "";
                                            if (certnum.length() > 6) {
                                                zzbh = certnum.substring(certnum.length() - 6, certnum.length());
                                            }

                                            if (certnum.length() > 14) {
                                                // 安许证截取证照年份
                                                // 例如：（鲁）JZ安许证字[2023]087974
                                                // 截取中括号中的数字
                                                year = certnum.substring(10, 14);
                                            }

                                            CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                            String code = utils.getXy(
                                                    "11100000000013338W050113700000045030270" + year + zzbh + "001");

                                            String associatedZzeCertID_new = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                    + year + zzbh + ".001." + code;
                                            certInfo.set("associatedZzeCertID", associatedZzeCertID_new);
                                        }
                                    }

                                    // 业务信息
                                    // JSONArray jsonArray = new JSONArray();
                                    // obj.put("businessInformation",
                                    // jsonArray);
                                    // 操作类型
                                    // 01 新发电子证照
                                    // 02 延续
                                    // 03 暂扣
                                    // 04 过期失效
                                    // 05 注销（撤销、吊销）
                                    // 06 暂扣发还
                                    // 07 其他
                                    // 08 变更
                                    // 获取事项
                                    if (audittask != null) {
                                        switch (audittask.getItem_id()) {
                                            case "11370800MB285591843370117002011":
                                            case "11370800MB28559184300011711200001":
                                            case "11370800MB285591843370117002016":
                                                obj.put("operateType", "01");
                                                obj.put("certState", "01");
                                                break;
                                            case "11370800MB285591843370117002013":
                                            case "11370800MB28559184300011711200002":
                                            case "11370800MB285591843370117002014":
                                            case "11370800MB28559184300011711200003":
                                                obj.put("operateType", "02");
                                                obj.put("certState", "01");
                                                break;
                                            case "11370800MB285591843370117002017":
                                            case "11370800MB28559184300011711200005":
                                                obj.put("operateType", "05");
                                                obj.put("certState", "05");
                                                break;
                                            case "11370800MB285591843370117002015":
                                            case "11370800MB285591843370117002024":
                                            case "11370800MB285591843370117002027":
                                            case "11370800MB285591843370117002026":
                                            case "11370800MB285591843370117002025":
                                            case "11370800MB28559184300011711200004":
                                                obj.put("operateType", "08");
                                                obj.put("certState", "01");
                                                break;
                                            default:
                                                log.info("===========事项不在操作中=======");
                                                obj.put("operateType", "");
                                                break;
                                        }
                                    } else {
                                        log.info("===========找不到事项=======");
                                    }

                                    JSONObject pushJson = PushUtil.getCertPush(projectGuid, certGuid, obj, token,
                                            certtype, citynum, type, ifgjfm);
                                    // 更正
                                    if (!ifgjfm) {
                                        // 查找当前单位，ERWEIMA不为空的数据
                                        // SqlConditionUtil sqlConditionUtil =
                                        // new SqlConditionUtil();
                                        log.info("certInfo.getCertownerno():" + certInfo.getCertownerno());
                                        // sqlConditionUtil.eq("certownerno",certInfo.getCertownerno());
                                        // sqlConditionUtil.isNotBlank("ERWEIMA");
                                        List<CertInfo> certInfos = new IJnCertInfoService()
                                                .getCertWithErwm(certInfo.getCertownerno());
                                        log.info("certInfos.size():" + certInfos.size());
                                        if (CollectionUtils.isNotEmpty(certInfos)) {
                                            CertInfo precer = certInfos.get(0);
                                            if (StringUtils.isNotBlank(precer.getStr("ERWEIMA"))) {
                                                certInfo.set("ERWEIMA", precer.get("ERWEIMA"));
                                            }
                                            if (StringUtils.isNotBlank(precer.get("ecertid"))) {
                                                certInfo.set("ecertid", precer.get("ecertid"));
                                            }
                                        }
                                        log.info("certInfo.get(\"ERWEIMA\")" + certInfo.get("ERWEIMA"));

                                    } else {
                                        log.info("安许证照pushJson:" + pushJson);
                                        String returnCode1 = pushJson.getString("returnCode");
                                        if ("1".equals(returnCode1) || "2".equals(returnCode1)) {
                                            if ("2".equals(returnCode1)) {
                                                // 预警提示
                                                pushText.append("赋码归集预警：" + pushJson.getString("text") + "!");
                                            }
                                            // 二维码插入
                                            qrcoderas = pushJson.getString("qrcoderas");
                                            certInfo.set("ERWEIMA", qrcoderas);
                                            // 给certinfo塞值 ecertid
                                            certInfo.set("ecertid", pushJson.getString("ecertid"));
                                        } else {
                                            pushText.append(pushJson.getString("text") + "!");
                                        }
                                    }

                                } else {
                                    log.info("===========没有areacode=======");
                                }
                            } else {
                                log.info("===========找不到auditProject=======");
                            }
                        } else {
                            log.info("===========对接系统获取token有误=======");
                        }
                    } else {
                        log.info("===========pushcerttype没有数据,或者没有projectGuid传入=======");
                    }
                    // 如果有就插入
                    if (StringUtil.isNotBlank(qrcoderas)) {
                        qrcodeurl = qrcoderas;
                    } else {
                        qrcodeurl = certInfo.getCertname();
                    }
                } else {
                    // TODO 推送的证照
                    String certtype = certCatalog.get("pushcerttype");
                    String projectGuid = certInfo.get("projectguid");
                    log.info("projectGuid:" + projectGuid);
                    if (StringUtil.isNotBlank(certtype) && StringUtil.isNotBlank(projectGuid)) {
                        // 获取token
                        String token = PushTokenUtil.getCertToken();
                        String certGuid = certInfo.getRowguid();
                        // 获取办件
                        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectGuid, "").getResult();
                        if (StringUtil.isNotBlank(token)) {
                            if (auditProject != null) {
                                // 校验
                                if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getAreaCode())) {
                                    // 统一社会信用代码
                                    String creditCode = "";
                                    String ecertid = UUID.randomUUID().toString();
                                    // 从业务数据中取证照编号
                                    String certno = "";
                                    for (int i = 0; i < fieldNames.length; i++) {
                                        if ("证书编号".equals(fieldNames[i])) {
                                            // 证书编号
                                            certno = values[i].toString();
                                        }
                                        if ("统一社会信用代码".equals(fieldNames[i])) {
                                            // 单位地址
                                            creditCode = values[i].toString();
                                        }
                                    }

                                    String type = "0";
                                    Record info_new = getCertInfoByTyshxydm(creditCode);
                                    if (info_new != null) {
                                        type = "1";
                                    }

                                    JSONObject checkJson = PushUtil.getCertCheck(auditProject, certGuid, areacode,
                                            creditCode, token, ecertid, certno, type);
                                    String returnCode = checkJson.getString("returnCode");
                                    if ("1".equals(returnCode) || "2".equals(returnCode)) {
                                        if ("2".equals(returnCode)) {
                                            // 预警提示
                                            pushText.append("校验预警：" + checkJson.getString("text") + "!");
                                        }
                                        // 成功和预警进行下一步
                                        JSONObject obj = new JSONObject();
                                        obj.put("eCertID", ecertid);
                                        obj.put("provinceNum", "370000");
                                        obj.put("issuAuth", "山东省住房和城乡建设厅");
                                        obj.put("issuAuthCode", "113700000045030270");
                                        // 发证日期
                                        obj.put("issuDate", EpointDateUtil.convertDate2String(certInfo.getAwarddate(),
                                                EpointDateUtil.DATE_FORMAT));
                                        String citynum = "";
                                        // 所属地市
                                        if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getAreaCode())) {
                                            citynum = ZwfwUserSession.getInstance().getAreaCode().substring(0, 6);
                                        }

                                        // 从业务数据中区
                                        // 详细地址
                                        String CertNum = "";
                                        String certnum = "";
                                        for (int i = 0; i < fieldNames.length; i++) {
                                            if ("证书编号".equals(fieldNames[i])) {
                                                // 证书编号
                                                obj.put("certNum", values[i]);
                                                CertNum = values[i].toString();
                                                certnum = values[i].toString();
                                            }
                                            if ("企业名称".equals(fieldNames[i])) {
                                                // 企业名称
                                                obj.put("companyName", values[i]);
                                            }
                                            if ("统一社会信用代码".equals(fieldNames[i])) {
                                                // 统一社会信用代码
                                                obj.put("creditCode", values[i]);
                                            }
                                            if ("详细地址".equals(fieldNames[i])) {
                                                // 单位地址
                                                obj.put("companyAddress", values[i]);
                                            }
                                            if ("经济性质".equals(fieldNames[i])) {
                                                // 经济类型
                                                obj.put("economicType", values[i]);
                                            }
                                            if ("法人代表".equals(fieldNames[i])) {
                                                // 法定代表人
                                                obj.put("responsiblePerson", values[i]);
                                                if (StringUtil.isBlank(values[i])) {
                                                    // 法定代表人
                                                    obj.put("responsiblePerson", auditProject.getLegal());
                                                }
                                            }
                                            if ("有效期始".equals(fieldNames[i])) {
                                                String str = values[i].toString();
                                                String regEx = "[^0-9]";
                                                Pattern p = Pattern.compile(regEx);
                                                Matcher m = p.matcher(str);
                                                String result = m.replaceAll("").trim();
                                                // 有效期起始日期
                                                obj.put("effectiveDate", result.substring(0, 4) + "-"
                                                        + result.substring(4, 6) + "-" + result.substring(6, 8));
                                            }
                                            if ("有效期止".equals(fieldNames[i])) {
                                                String str = values[i].toString();
                                                String regEx = "[^0-9]";
                                                Pattern p = Pattern.compile(regEx);
                                                Matcher m = p.matcher(str);
                                                String result = m.replaceAll("").trim();
                                                // 有效期起始日期
                                                obj.put("expiringDate", result.substring(0, 4) + "-"
                                                        + result.substring(4, 6) + "-" + result.substring(6, 8));
                                            }
                                            if ("首次发证日期".equals(fieldNames[i])) {
                                                String str = values[i].toString();
                                                String regEx = "[^0-9]";
                                                Pattern p = Pattern.compile(regEx);
                                                Matcher m = p.matcher(str);
                                                String result = m.replaceAll("").trim();
                                                // 有效期起始日期
                                                obj.put("firstIssuDate", result.substring(0, 4) + "-"
                                                        + result.substring(4, 6) + "-" + result.substring(6, 8));
                                            }
                                        }
                                        // 法定代表人身份证件号码
                                        obj.put("responsiblePersonID", auditProject.getLegalid());
                                        // 法定代表人身份证件号码类型
                                        obj.put("responsiblePersonIDType", "111");
                                        // 许可范围
                                        obj.put("licenseScope", "建筑施工");

                                        AuditTask audittask = iAuditTask
                                                .getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();

                                        // 证书状态
                                        // String status = "";
                                        // String statusText = "";
                                        // if
                                        // (StringUtil.isNotBlank(certInfo.getStatus()))
                                        // {
                                        // switch (certInfo.getStatus()) {
                                        // case "10":
                                        // status = "01";
                                        // break;
                                        // case "20":
                                        // statusText = "年检";
                                        // break;
                                        // case "30":
                                        // statusText = "过期";
                                        // break;
                                        // case "40":
                                        // statusText = "挂失";
                                        // break;
                                        // case "50":
                                        // status = "05";
                                        // break;
                                        // case "60":
                                        // statusText = "草稿";
                                        // break;
                                        // }
                                        // }

                                        // 如果是注销事项，更新status
                                        // if
                                        // ("11370800MB285591843370117002017".equals(audittask.getItem_id()))
                                        // {
                                        // certInfo.setIshistory(1);// 注销二维码
                                        // certInfo.setRemark("建筑注销事项证照");
                                        // obj.put("certState", "05");
                                        // }

                                        // // 证书状态描述
                                        // if("99".equals(status)){
                                        // obj.put("certStatusDescription",
                                        // "其他");
                                        // }else{
                                        // obj.put("certStatusDescription",
                                        // statusText);
                                        // }

                                        Record record = iCxBusService.getDzbdDetail("formtable20220422102306",
                                                auditProject.getRowguid());

                                        if (record != null) {
                                            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                            Integer idnum = record.getInt("idnum");
                                            idnum = idnum + 3623;
                                            String idnumv = String.valueOf(idnum);

                                            String code = CertCheckUtil.getCheckCode(
                                                    "11100000000013338W050113700000045030270" + year + idnumv + "001");
                                            String associatedZzeCertID = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                    + year + idnumv + ".001." + code;

                                            // 这两事项不推associatedZzeCertID
                                            if (!"11370800MB285591843370117002011".equals(audittask.getItem_id())
                                                    && !"11370800MB285591843370117002016"
                                                    .equals(audittask.getItem_id())) {
                                                // 关联证照标识
                                                obj.put("associatedZzeCertID", associatedZzeCertID);
                                            }
                                            certInfo.set("associatedZzeCertID", associatedZzeCertID);
                                        }

                                        // 新申请
                                        if ("11370800MB285591843370117002011".equals(audittask.getItem_id())
                                                || "11370800MB28559184300011711200001".equals(audittask.getItem_id())
                                                || "11370800MB285591843370117002016".equals(audittask.getItem_id())) {
                                            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");

                                            if (CertNum.length() > 6) {
                                                CertNum = CertNum.substring(CertNum.length() - 6, CertNum.length());
                                            }

                                            if (certnum.length() > 14) {
                                                // 安许证截取证照年份
                                                // 例如：（鲁）JZ安许证字[2023]087974
                                                // 截取中括号中的数字
                                                year = certnum.substring(10, 14);
                                            }

                                            CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                            String code = utils.getXy(
                                                    "11100000000013338W050113700000045030270" + year + CertNum + "001");

                                            String associatedZzeCertID = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                    + year + CertNum + ".001." + code;

                                            // 这两事项不推associatedZzeCertID
                                            if (!"11370800MB285591843370117002011".equals(audittask.getItem_id())
                                                    && !"11370800MB285591843370117002016"
                                                    .equals(audittask.getItem_id())) {
                                                // 关联证照标识
                                                obj.put("associatedZzeCertID", associatedZzeCertID);
                                            }
                                            certInfo.set("associatedZzeCertID", associatedZzeCertID);
                                        }
                                        // 延续，变更逻辑一致
                                        else if ("11370800MB285591843370117002013".equals(audittask.getItem_id())
                                                || "11370800MB28559184300011711200002".equals(audittask.getItem_id())
                                                || "11370800MB285591843370117002014".equals(audittask.getItem_id())
                                                || "11370800MB28559184300011711200003".equals(audittask.getItem_id())
                                                || "11370800MB285591843370117002015".equals(audittask.getItem_id())
                                                || "11370800MB285591843370117002024".equals(audittask.getItem_id())
                                                || "11370800MB285591843370117002027".equals(audittask.getItem_id())
                                                || "11370800MB285591843370117002026".equals(audittask.getItem_id())
                                                || "11370800MB285591843370117002025".equals(audittask.getItem_id())
                                                || "11370800MB28559184300011711200004".equals(audittask.getItem_id())) {
                                            if (info_new != null) {
                                                String associatedZzeCertID = info_new
                                                        .getStr("GUANLIANZHENGZHAOBIAOSHI");
                                                obj.put("associatedZzeCertID", associatedZzeCertID);
                                                if (StringUtil.isNotBlank(associatedZzeCertID)) {
                                                    if(associatedZzeCertID.length()>=5){
                                                        associatedZzeCertID = associatedZzeCertID.substring(
                                                                associatedZzeCertID.length() - 5,
                                                                associatedZzeCertID.length());
                                                    }else{
                                                        log.info("associatedZzeCertID 不符合长度要求："+associatedZzeCertID);
                                                        throw new StringIndexOutOfBoundsException();
                                                    }

                                                    // 取版本号
                                                    String bbh = associatedZzeCertID.substring(0, 3);
                                                    if (StringUtil.isNotBlank(bbh)) {
                                                        int bbh_new = Integer.parseInt(bbh) + 1;
                                                        String bbh1 = String.valueOf(bbh_new);
                                                        if (bbh1.length() == 1) {
                                                            bbh = "00" + bbh1;
                                                        } else if (bbh1.length() == 2) {
                                                            bbh = "0" + bbh1;
                                                        } else if (bbh1.length() == 3) {
                                                            bbh = bbh1;
                                                        }

                                                        String year = EpointDateUtil.convertDate2String(new Date(),
                                                                "yyyy");
                                                        String zzbh = "";
                                                        if (certnum.length() > 6) {
                                                            zzbh = certnum.substring(certnum.length() - 6,
                                                                    certnum.length());
                                                        }

                                                        if (certnum.length() > 14) {
                                                            // 安许证截取证照年份
                                                            // 例如：（鲁）JZ安许证字[2023]087974
                                                            // 截取中括号中的数字
                                                            year = certnum.substring(10, 14);
                                                        }

                                                        CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                                        String code = utils
                                                                .getXy("11100000000013338W050113700000045030270" + year
                                                                        + zzbh + bbh);
                                                        String associatedZzeCertID_new = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                                + year + zzbh + "." + bbh + "." + code;
                                                        certInfo.set("associatedZzeCertID", associatedZzeCertID_new);
                                                    }
                                                }
                                                // 如果前置库所取的记录没对应值，按新申请重新生成一个值
                                                else {
                                                    String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");

                                                    String zzbh = "";

                                                    if (certnum.length() > 6) {
                                                        zzbh = certnum.substring(certnum.length() - 6,
                                                                certnum.length());
                                                    }

                                                    if (certnum.length() > 14) {
                                                        // 安许证截取证照年份
                                                        // 例如：（鲁）JZ安许证字[2023]087974
                                                        // 截取中括号中的数字
                                                        year = certnum.substring(10, 14);
                                                    }

                                                    CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                                    String code = utils.getXy("11100000000013338W050113700000045030270"
                                                            + year + zzbh + "001");

                                                    String associatedZzeCertID_new = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                            + year + zzbh + ".001." + code;
                                                    certInfo.set("associatedZzeCertID", associatedZzeCertID_new);
                                                }
                                            }
                                            // 前置库都是空的，重新生成值
                                            else {
                                                String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                                String zzbh = "";
                                                if (certnum.length() > 6) {
                                                    zzbh = certnum.substring(certnum.length() - 6, certnum.length());
                                                }

                                                if (certnum.length() > 14) {
                                                    // 安许证截取证照年份
                                                    // 例如：（鲁）JZ安许证字[2023]087974
                                                    // 截取中括号中的数字
                                                    year = certnum.substring(10, 14);
                                                }

                                                CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                                String code = utils.getXy("11100000000013338W050113700000045030270"
                                                        + year + zzbh + "001");

                                                String associatedZzeCertID_new = "1.2.156.3005.2.11100000000013338W050.113700000045030270."
                                                        + year + zzbh + ".001." + code;
                                                certInfo.set("associatedZzeCertID", associatedZzeCertID_new);
                                            }
                                        }

                                        // 业务信息
                                        // JSONArray jsonArray = new
                                        // JSONArray();
                                        // obj.put("businessInformation",
                                        // jsonArray);
                                        // 操作类型
                                        // 01 新发电子证照
                                        // 02 延续
                                        // 03 暂扣
                                        // 04 过期失效
                                        // 05 注销（撤销、吊销）
                                        // 06 暂扣发还
                                        // 07 其他
                                        // 08 变更
                                        // 获取事项
                                        if (audittask != null) {
                                            switch (audittask.getItem_id()) {
                                                case "11370800MB285591843370117002011":
                                                case "11370800MB28559184300011711200001":
                                                case "11370800MB285591843370117002016":
                                                    obj.put("operateType", "01");
                                                    obj.put("certState", "01");
                                                    break;
                                                case "11370800MB285591843370117002013":
                                                case "11370800MB28559184300011711200002":
                                                case "11370800MB285591843370117002014":
                                                case "11370800MB28559184300011711200003":
                                                    obj.put("operateType", "02");
                                                    obj.put("certState", "01");
                                                    break;
                                                case "11370800MB285591843370117002017":
                                                case "11370800MB28559184300011711200005":
                                                    obj.put("operateType", "05");
                                                    obj.put("certState", "05");
                                                    break;
                                                case "11370800MB285591843370117002015":
                                                case "11370800MB285591843370117002024":
                                                case "11370800MB285591843370117002027":
                                                case "11370800MB285591843370117002026":
                                                case "11370800MB285591843370117002025":
                                                case "11370800MB28559184300011711200004":
                                                    obj.put("operateType", "08");
                                                    obj.put("certState", "01");
                                                    break;
                                                default:
                                                    log.info("===========事项不在操作中=======");
                                                    obj.put("operateType", "");
                                                    break;
                                            }
                                        } else {
                                            log.info("===========找不到事项=======");
                                        }

                                        JSONObject pushJson = PushUtil.getCertPush(projectGuid, certGuid, obj, token,
                                                certtype, citynum, type, ifgjfm);
                                        log.info("安许证照pushJson:" + pushJson);
                                        String returnCode1 = pushJson.getString("returnCode");
                                        if ("1".equals(returnCode1) || "2".equals(returnCode1)) {
                                            if ("2".equals(returnCode1)) {
                                                // 预警提示
                                                pushText.append("赋码归集预警：" + pushJson.getString("text") + "!");
                                            }
                                            // 二维码插入
                                            qrcoderas = pushJson.getString("qrcoderas");
                                            certInfo.set("ERWEIMA", qrcoderas);
                                            // 给certinfo塞值 ecertid
                                            certInfo.set("ecertid", pushJson.getString("ecertid"));
                                        } else {
                                            pushText.append(pushJson.getString("text") + "!");
                                        }
                                    } else {
                                        pushText.append(checkJson.getString("text") + "!");
                                    }
                                } else {
                                    log.info("===========没有areacode=======");
                                }
                            } else {
                                log.info("===========找不到auditProject=======");
                            }
                        } else {
                            log.info("===========对接系统获取token有误=======");
                        }
                    } else {
                        log.info("===========pushcerttype没有数据,或者没有projectGuid传入=======");
                    }
                    // 如果有就插入
                    if (StringUtil.isNotBlank(qrcoderas)) {
                        qrcodeurl = qrcoderas;
                    } else {
                        qrcodeurl = certInfo.getCertname();
                    }
                    if (StringUtils.isNotBlank(pushText.toString())) {
                        certInfo.put("pushText", pushText.toString());
                    }
                }
            }
        }
        return certInfo;
    }

    /**
     * 推送竣工验收
     *
     * @param ifgjfm 是否归集赋码
     */
    @Override
    public CertInfo pushjgys(String[] fieldNames, Object[] values, CertInfo certInfo, boolean ifgjfm) {
        IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        
        ICertConfigExternal iCertConfigExternal = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);

        IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);

        IAuditSpSpJgysService iAuditSpSpJgysService = ContainerFactory.getContainInfo().getComponent(IAuditSpSpJgysService.class);
        ISpglJsgcjgysbaxxbV3Service jsgcjgysbaxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglJsgcjgysbaxxbV3Service.class);

        IAuditProjectMaterial iAuditProjectMaterial = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);

        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

        IConfigService configServicce = ContainerFactory.getContainInfo().getComponent(IConfigService.class);

        ISpglJsgcghxkxxbV3Service iSpglJsgcghxkxxbV3Service = ContainerFactory.getContainInfo().getComponent(ISpglJsgcghxkxxbV3Service.class);

        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);

        IDantiInfoV3Service dantiInfoService = ContainerFactory.getContainInfo().getComponent(IDantiInfoV3Service.class);
        ISpglZrztxxbV3Service iSpglZrztxxbV3Service = ContainerFactory.getContainInfo().getComponent(ISpglZrztxxbV3Service.class);
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        log.info("推送竣工验收");
        if (certInfo != null && StringUtils.isNotBlank(certInfo.getCertcatalogid())) {
            String areacode = "";
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                    .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
            } else {
                areacode = ZwfwUserSession.getInstance().getAreaCode();
            }
            CertCatalog certCatalog = iCertConfigExternal.getCatalogByCatalogid(certInfo.getCertcatalogid(), areacode);
            log.info("certCatalog" + certCatalog.getRowguid());
            String qrcoderas = "";
            StringBuffer pushText = new StringBuffer();
            // 插入图片（包括二维码）
            String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode");
            // 是否推送证照
            if ("1".equals(certCatalog.get("is_push"))) {
                log.info("===========进入个性化推送方法=======");
                String projectGuid = certInfo.get("projectguid");
                if (StringUtil.isNotBlank(projectGuid)) {
                    //获取token
                    String token = PushUtil.getCertToken(2);
                    String certGuid = certInfo.getRowguid();
                    // 获取办件
                    AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectGuid, "").getResult();
                    if (StringUtil.isNotBlank(token)) {
                        if (auditProject != null) {
                            // 校验
                            if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getAreaCode())) {
//                                // 统一社会信用代码
//                                String creditCode = "";
//                                String ecertid = UUID.randomUUID().toString();
//                                // 从业务数据中取证照编号
//                                String certno = "";
//                                for (int i = 0; i < fieldNames.length; i++) {
//                                    if ("证书编号".equals(fieldNames[i])) {
//                                        // 证书编号
//                                        certno = values[i].toString();
//                                    }
//                                    if ("统一社会信用代码".equals(fieldNames[i])) {
//                                        // 单位地址
//                                        creditCode = values[i].toString();
//                                    }
//                                }

                                String type = "0";
//                                Record info_new = getCertInfoByTyshxydm(creditCode);
//                                if (info_new != null) {
//                                    type = "1";
//                                }
                                String xmdm = "";
                                String JGYSBABH = "";
                                AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (sub != null) {
                                    AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo
                                            .getAuditRsItemBaseinfoByRowguid(sub.getYewuguid()).getResult();
                                    SpglJsgcjgysbaxxbV3 spglJsgcjgysbaxxbV3 = jsgcjgysbaxxbV3Service.findDominByCondition("370800", baseinfo.getItemcode(), auditProject.getFlowsn());
                                    SpglJsgcghxkxxbV3 spglJsgcghxkxxbV3 = iSpglJsgcghxkxxbV3Service.findDominByCondition("370800", baseinfo.getItemcode(), auditProject.getFlowsn());

                                    xmdm = baseinfo.getItemcode();
                                    JGYSBABH = spglJsgcjgysbaxxbV3.getJgysbabh();
                                    JSONObject checkJson = PushUtil.getCertCheck1(auditProject, certGuid, areacode, token, xmdm, JGYSBABH);
                                    String returnCode = checkJson.getString("returnCode");
                                    //工程质量监督机构名称（JDJGMC）
                                    Map<String, Object> filter = new HashMap<>();


                                    // 设置基本信息guid
                                    filter.put("certinfoguid", certInfo.getRowguid());
                                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                                    //1:成功，2:预警
                                    if ("1".equals(returnCode) || "2".equals(returnCode)) {
                                        if ("2".equals(returnCode)) {
                                            // 预警提示
                                            pushText.append("校验预警：" + checkJson.getString("text") + "!");
                                        }
                                        // 成功和预警进行下一步
                                        JSONObject acceptJson = new JSONObject();
                                        String citynum = "";
                                        // 所属地市
                                        if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getAreaCode())) {
                                            citynum = ZwfwUserSession.getInstance().getAreaCode().substring(0, 6);
                                        }
                                        if (baseinfo != null) {

                                            // 处理必填字段
                                            // 行政区划代码（XZQHDM），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "XZQHDM", auditProject.getAreacode());
                                            // 项目代码（XMDM），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "XMDM", spglJsgcjgysbaxxbV3.getGcdm());
                                            // 竣工验收备案编号（JGYSBABH），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "JGYSBABH", spglJsgcjgysbaxxbV3.getJgysbabh());
                                            // 建设单位（JSDW），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "JSDW", spglJsgcjgysbaxxbV3.getJsdw());
                                            // 建设单位地点（JSDWDD），如果获取的对象不为空且地址字段不为空则设置，否则设置为"空"
                                            handleRequiredField(acceptJson, "JSDWDD", auditSpSpJgys != null ? auditSpSpJgys.getItemaddress() : null);
                                            // 建设单位代码（JSDWDM），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "JSDWDM", spglJsgcjgysbaxxbV3.getJsdwdm());
                                            // 建设单位类型（JSDWLX），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "JSDWLX", spglJsgcjgysbaxxbV3.getJsdwlx() + "");
                                            // 施工许可证编号（SGXKZBH），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "SGXKZBH", spglJsgcjgysbaxxbV3.getSgxkzbh());
                                            // 备案机关（BAJG），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "BAJG", spglJsgcjgysbaxxbV3.getBajg());
                                            // 备案机关统一社会信用代码（BAJGXYDM），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "BAJGXYDM", spglJsgcjgysbaxxbV3.getBajgxydm());
                                            // 备案日期（BARQ），如果日期不为空则转换格式后设置，否则设置为"空"
                                            handleRequiredDateField(acceptJson, "BARQ", spglJsgcjgysbaxxbV3.getBarq());
                                            // 备案范围（BAFW），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "BAFW", spglJsgcjgysbaxxbV3.getBafw());
                                            // 工程名称（GCMC），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "GCMC", spglJsgcjgysbaxxbV3.getGcmc());
                                            // 工程地点（GCDD），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "GCDD", spglJsgcjgysbaxxbV3.getJsdz());

                                            //工程规划许可证号（GCGHXKZH），如果为空则设置为"空"
                                            if (spglJsgcghxkxxbV3 != null) {
                                                if(StringUtil.isNotBlank(spglJsgcghxkxxbV3.getGcghxkzbh())){
                                                    handleRequiredField(acceptJson, "GCGHXKZH", spglJsgcghxkxxbV3.getGcghxkzbh());
                                                }else{
                                                    handleRequiredField(acceptJson, "GCGHXKZH", certinfoExtension.get("gcghxkzh")); 
                                                }
                                                
                                            }else{
                                                handleRequiredField(acceptJson, "GCGHXKZH", certinfoExtension.get("gcghxkzh"));
                                            }

                                            // 是否实行联合验收（SFSXLHYS），如果为空则设置为"空"
                                            acceptJson.put("SFSXLHYS",spglJsgcjgysbaxxbV3.getSfsxlhys());
                                            // 是否变更五方责任主体（SFBGWFZT），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "SFBGWFZT", spglJsgcjgysbaxxbV3.getSfbgwfzt() + "");
                                            // 建设地址（JSDZ），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "JSDZ", spglJsgcjgysbaxxbV3.getJsdz());
                                            // 所属县区（SSQX），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "SSQX", spglJsgcjgysbaxxbV3.getSsqx());
                                            // 建设规模（JSGM），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "JSGM", spglJsgcjgysbaxxbV3.getJsgm());
                                            // 开工日期（KGRQ），如果日期不为空则转换格式后设置，否则设置为"空"
                                            handleRequiredDateField(acceptJson, "KGRQ", spglJsgcjgysbaxxbV3.getKgrq());
                                            // 竣工验收日期（JGYSRQ），如果日期不为空则转换格式后设置，否则设置为"空"
                                            handleRequiredDateField(acceptJson, "JGYSRQ", spglJsgcjgysbaxxbV3.getJgrq());
                                            // 实际造价（SJZJ），如果为空则设置为"空"
                                            BigDecimal sjzj = new BigDecimal(spglJsgcjgysbaxxbV3.getSjzj());
                                            acceptJson.put("SJZJ",sjzj.toPlainString());
                                            // 建设单位意见（JSDWYJ），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "JSDWYJ", spglJsgcjgysbaxxbV3.getJsdwyj());
                                            // 联系人/代理人（LXR），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "LXR", spglJsgcjgysbaxxbV3.getLxr());
                                            // 联系人手机号（LXRSJH），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "LXRSJH", spglJsgcjgysbaxxbV3.getLxrsjh());
                                            // 工程竣工验收备案文件目录（BAWJML），设置固定的文件目录字符串
                                            handleRequiredField(acceptJson, "BAWJML", "1.竣工验收意见表;\n" +
                                                    "2.工程竣工验收报告;\n" +
                                                    "3.规划、消防验收等部门出具的认可文件或者准许使用的\n" +
                                                    "文件;\n" +
                                                    "4.施工单位签署的工程质量保修书;             5.住宅工程的《住宅质量保证书》和《住宅使用说明书》:\n" +
                                                    "6.法规、规章规定必须提供的其他文件。");
                                            // 地方数据主键（DFSJZJ），如果为空则设置为"空"
                                            handleRequiredField(acceptJson, "DFSJZJ", spglJsgcjgysbaxxbV3.getDfsjzj());
                                            //项目经纬度坐标
                                            handleRequiredField(acceptJson, "XMJWDZB", spglJsgcjgysbaxxbV3.getXmjwdzb());
                                            // 建设单位项目负责人
                                            acceptJson.put("JSDWXMFZR", spglJsgcjgysbaxxbV3.getJsdwxmfzr());

                                            // 处理非必填字段
                                            // 建设单位项目负责人身份证件号码（JSFZRZJHM）
                                            acceptJson.put("JSFZRZJHM", spglJsgcjgysbaxxbV3.getJsfzrzjhm());
                                            // 建设单位项目负责人身份证件类型（JSFZRZJLX）
                                            acceptJson.put("JSFZRZJLX", spglJsgcjgysbaxxbV3.getJsfzrzjlx()+"");
                                            // 建设单位项目负责人联系电话（JSFZRLXDH）
                                            acceptJson.put("JSFZRLXDH", spglJsgcjgysbaxxbV3.getJsfzrlxdh());
                                            // 施工图审查合格书编号（SCHGSBH）
                                            acceptJson.put("SCHGSBH", spglJsgcjgysbaxxbV3.getSchgsbh());
                                            // 档案验收意见（DAYSYJ）
                                            acceptJson.put("DAYSYJ", spglJsgcjgysbaxxbV3.getDaysyj());
                                            // 档案移交状态（DAYSZT）
                                            acceptJson.put("DAYSZT", spglJsgcjgysbaxxbV3.getDayszt()+"");

                                            
                                            if (certinfoExtension != null) {
                                                acceptJson.put("JDJGMC", certinfoExtension.getStr("gczljdjgmc"));
                                            }

                                            //竣工验收意见预览地址（JGYSYJYLDZ）
                                            List<AuditProjectMaterial> list = iAuditProjectMaterial.selectProjectMaterial(auditProject.getRowguid())
                                                    .getResult();
                                            if (!list.isEmpty()) {
                                                for (AuditProjectMaterial auditProjectMaterial : list) {
                                                    if ("《工程竣工验收备案表》".equals(auditProjectMaterial.getTaskmaterial())) {
                                                        List<FrameAttachInfo> attachlist = iAttachService
                                                                .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                                        if (!attachlist.isEmpty()) {
                                                            String attachguid = attachlist.get(0).getAttachGuid();
                                                            String zwfwUrl = configServicce.getFrameConfigValue("AS_ZWFW_ATTACH_URL");
                                                            // 获取系统参数中office365地址参数(172.20.138.153:8088)
                                                            String officeurl = configService.getFrameConfigValue("AS_OfficeWeb365_Server_Zwdt");
                                                            String previewUrl = "http://" + officeurl + "/?furl=";
                                                            String fileurl = zwfwUrl + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + attachguid;
                                                            //加密key
                                                            String deskey = ConfigUtil.getConfigValue("uploadpreview.encrypt.key");
                                                            //加密向量
                                                            String desiv = ConfigUtil.getConfigValue("uploadpreview.encrypt.iv");
                                                            try {
                                                                DESEncrypt des = new DESEncrypt(deskey, desiv);
                                                                acceptJson.put("JGYSYJYLDZ",  previewUrl+des.encode(fileurl).replaceAll("(\r\n|\n)", ""));
                                                            }catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                        break;
                                                    }
                                                }
                                            }

                                            //证照关联标识（Zzcertid）
                                            FrameOuExtendInfo frameOuExten = iOuService.getFrameOuExtendInfo(auditProject.getOuguid());
                                            CertCheckUtilsNew utils = new CertCheckUtilsNew();
                                            String associatedZzeCertID = "";
                                            //固定位：电子证照根代码+证照类型代码
                                            StringBuffer sb = new StringBuffer();
                                            //需要校验
                                            StringBuffer sb1 = new StringBuffer();
                                            sb.append("1.2.156.3005.2.");
                                            sb1.append("11100000000013338W087.");
                                            //发证机关代码
                                            sb1.append(frameOuExten.getStr("orgcode"));
                                            sb1.append(".");
                                            //顺序号/竣工验收备案表编号
                                            sb1.append(spglJsgcjgysbaxxbV3.getJgysbabh());
                                            sb1.append(".");
                                            //版本号
                                            sb1.append("001");
                                            String code = utils.getXy(sb1.toString());
                                            //校验位
                                            sb1.append(".");
                                            sb1.append(code);
                                            sb.append(sb1.toString());
                                            associatedZzeCertID = sb.toString();
                                            certInfo.set("associatedZzeCertID", associatedZzeCertID);
//                                            acceptJson.put("Zzcertid", associatedZzeCertID);
                                            
                                            //五方主体
                                            List<ParticipantsInfo> participantsinfolist = iSpglZrztxxbV3Service
                                                    .getParticipantsInfoListBySubappguid(auditProject.getSubappguid());
                                            if(EpointCollectionUtils.isNotEmpty(participantsinfolist)){
                                                handleCorpInfoList(acceptJson, participantsinfolist);
                                            }
                                            //单体
                                            List<DantiInfoV3> alldantiInfo = dantiInfoService.findListBySubAppguid(auditProject.getSubappguid());
                                            if(EpointCollectionUtils.isNotEmpty(alldantiInfo)){
                                                handleMonomerList(acceptJson, alldantiInfo);
                                            }
                                            
                                            log.info("校验请求参数" + acceptJson.toJSONString());
                                        }
                
                                        JSONObject pushJson = PushUtil.getCertPush1(projectGuid, certGuid, acceptJson, token, citynum, type);
                                        log.info("竣工验收pushJson:" + pushJson);
                                        String returnCode1 = pushJson.getString("returnCode");
                                        if ("1".equals(returnCode1) || "2".equals(returnCode1)) {
                                            if ("2".equals(returnCode1)) {
                                                // 预警提示
                                                pushText.append("赋码归集预警：" + pushJson.getString("text") + "!");
                                            }
                                            // 二维码插入
                                            qrcoderas = pushJson.getString("qrcoderas");
                                            certInfo.set("ERWEIMA", qrcoderas);
                                            // 给certinfo塞值 ecertid
                                            log.info("ecertid:"+pushJson.getString("ecertid"));
                                            certInfo.set("ecertid", pushJson.getString("ecertid"));
                                            //标记竣工验收备案证照待归集
                                            certInfo.set("ifjgysgj","0");
                                            certInfo.set("defaultwidth","25");
                                            certInfo.set("defaultheight","25");
                                        } else {
                                            pushText.append(pushJson.getString("text") + "!");
                                        }
                                    } else {
                                        pushText.append(checkJson.getString("text") + "!");
                                    }
                                }
                                //TODO 


                            } else {
                                log.info("===========没有areacode=======");
                            }
                        } else {
                            log.info("===========找不到auditProject=======");
                        }
                    } else {
                        log.info("===========对接系统获取token有误=======");
                    }
                } else {
                    log.info("===========没有projectGuid传入=======");
                }
                // 如果有就插入
                if (StringUtil.isNotBlank(qrcoderas)) {
                    qrcodeurl = qrcoderas;
                } else {
                    qrcodeurl = certInfo.getCertname();
                }
                if (StringUtils.isNotBlank(pushText.toString())) {
                    certInfo.put("pushText", pushText.toString());
                }
            }
        }
        return certInfo;
    }

    /**
     * 竣工验收备案主体单位类型
     * 1	勘察企业
     * 2	设计企业
     * 3	施工企业
     * 4	监理企业
     * 6	质量检测机构
     * 7	工程总承包单位
     * @param corptype 代码项:关联单位类型
     * @return
     */
    private String getDwlxByCorptype(String corptype) {
        String dwlx = "99";
        switch (corptype){
            case "1":
                dwlx="1";
                break;
            case "2":
                dwlx="2";
                break;
            case "3":
                dwlx="3";
                break;
            case "4":
                dwlx="4";
                break;
            case "10":
                dwlx="6";
                break;
            case "6":
                dwlx="7";
                break;
        }
        return dwlx;
    }

    // 处理CorpInfoList的方法，将ParticipantsInfo列表转换为符合接口要求的JSON格式并放入外层JSON对象
    private void handleCorpInfoList(JSONObject jsonObject, List<ParticipantsInfo> corpInfoList) {
        JSONArray corpInfoArray = new JSONArray();
        for (ParticipantsInfo corpInfo : corpInfoList) {
            JSONObject corpJsonObject = new JSONObject();
            // 处理地方数据主键（DFSJZJ），若为空则设置为"空"
            corpJsonObject.put("DFSJZJ", StringUtils.isNotBlank(corpInfo.getRowguid())? corpInfo.getRowguid() : "空");
            // 处理单位类型（DWLX），若获取的值为空则设置为"空"
            String dwlxValue = getDwlxByCorptype(corpInfo.getCorptype());
            corpJsonObject.put("DWLX", StringUtils.isNotBlank(dwlxValue)? dwlxValue : "空");
            // 处理单位名称（DWMC），若为空则设置为"空"
            corpJsonObject.put("DWMC", StringUtils.isNotBlank(corpInfo.getCorpname())? corpInfo.getCorpname() : "空");
            // 处理单位统一社会信用代码（DWTYSHXYDM），若为空则设置为"空"
            corpJsonObject.put("DWTYSHXYDM", StringUtils.isNotBlank(corpInfo.getCorpcode())? corpInfo.getCorpcode() : "空");
            // 处理资质等级（ZZDJ），若为空则设置为"空"
            corpJsonObject.put("ZZDJ", StringUtils.isNotBlank(corpInfo.getStr("CERT"))? corpInfo.getStr("CERT") : "空");
            // 处理单位法定代表人姓名（FDDBR），若为空则设置为"空"
            corpJsonObject.put("FDDBR", StringUtils.isNotBlank(corpInfo.getStr("legal"))? corpInfo.getStr("legal") : "空");
            // 处理单位法定代表人证件类型（FRZJLX），若为空则设置为"空"
            corpJsonObject.put("FRZJLX", corpInfo.get("legalcardtype")!= null? corpInfo.get("legalcardtype") : "1");
            // 处理单位法定代表人证件号码（FRZJHM），若为空则设置为"空"
            corpJsonObject.put("FRZJHM", StringUtils.isNotBlank(corpInfo.getStr("legalpersonicardnum"))? corpInfo.getStr("legalpersonicardnum") : "空");
            // 处理项目负责人姓名（FZRXM），若为空则设置为"空"
            corpJsonObject.put("FZRXM", StringUtils.isNotBlank(corpInfo.getStr("XMFZR"))? corpInfo.getStr("XMFZR") : "空");
            // 处理项目负责人身份证件类型（FZRZJLX），若为空则设置为"空"
            corpJsonObject.put("FZRZJLX", corpInfo.get("fzrzjlx")!= null? corpInfo.get("fzrzjlx") : "1");
            // 处理项目负责人证件号码（FZRZJHM），若为空则设置为"空"
            corpJsonObject.put("FZRZJHM", StringUtils.isNotBlank(corpInfo.getStr("xmfzr_idcard"))? corpInfo.getStr("xmfzr_idcard") : "空");
            // 处理项目负责人联系电话（FZRLXDH），若为空则设置为"空"
            corpJsonObject.put("FZRLXDH", StringUtils.isNotBlank(corpInfo.getStr("xmfzr_phone"))? corpInfo.getStr("xmfzr_phone") : "空");
            corpInfoArray.add(corpJsonObject);
        }
        jsonObject.put("CorpInfoList", corpInfoArray);
    }

    // 处理MonomerList的方法，将DantiInfoV3列表转换为符合接口要求的JSON格式并放入外层JSON对象
    private void handleMonomerList(JSONObject jsonObject, List<DantiInfoV3> monomerList) {
        JSONArray monomerInfoArray = new JSONArray();
        for (DantiInfoV3 monomerInfo : monomerList) {
            JSONObject monomerJsonObject = new JSONObject();
            // 处理地方数据主键（DFSJZJ），若为空则设置为"空"
            monomerJsonObject.put("DFSJZJ", StringUtils.isNotBlank(monomerInfo.getRowguid())? monomerInfo.getRowguid() : "空");
            // 处理单体名称（DTMC），若为空则设置为"空"
            monomerJsonObject.put("DTMC", StringUtils.isNotBlank(monomerInfo.getDtmc())? monomerInfo.getDtmc() : "空");
            // 处理单体编码（DTBM），若为空则设置为"空"
            monomerJsonObject.put("DTBM", StringUtils.isNotBlank(monomerInfo.getDtbm())? monomerInfo.getDtbm() : "空");
            // 处理工程用途（GCYT），若为空则设置为"空"
            monomerJsonObject.put("GCYT", StringUtils.isNotBlank(monomerInfo.getGcyt())? monomerInfo.getGcyt() : "空");
            // 处理规模指标（GMZB），若为空则设置为"空"
            monomerJsonObject.put("GMZB", StringUtils.isNotBlank(monomerInfo.getGmzb())? monomerInfo.getGmzb() : "空");
            // 处理结构体系（JGTX），若为空则设置为"空"
            monomerJsonObject.put("JGTX", StringUtils.isNotBlank(monomerInfo.getJgtx())? monomerInfo.getJgtx() : "空");
            // 处理耐火等级（NHDJ），若为空则设置为"空"
            monomerJsonObject.put("NHDJ", StringUtils.isNotBlank(monomerInfo.getNhdj()+"")? monomerInfo.getNhdj() : "空");
            // 处理建造方式（JZFS），若为空则设置为"空"
            monomerJsonObject.put("JZFS", StringUtils.isNotBlank(monomerInfo.getJzfs()+"")? monomerInfo.getJzfs()+"" : "空");
            // 处理单体经纬度坐标（DTJWDZB），若为空则设置为"空"
            monomerJsonObject.put("DTJWDZB", StringUtils.isNotBlank(monomerInfo.getDtjwdzb())? monomerInfo.getDtjwdzb() : "空");
            // 处理单体工程总造价（DTGCZZJ），若为空则设置为"空"
            monomerJsonObject.put("DTGCZZJ", StringUtils.isNotBlank(monomerInfo.getDtgczzj()+"")? monomerInfo.getDtgczzj() : "空");
            // 处理建筑面积（JZMJ），若为空则设置为"空"
            monomerJsonObject.put("JZMJ", StringUtils.isNotBlank(monomerInfo.getJzmj()+"")? monomerInfo.getJzmj() : "空");
            // 处理地上建筑面积（DSJZMJ），若为空则设置为"空"
            monomerJsonObject.put("DSJZMJ", StringUtils.isNotBlank(monomerInfo.getDsjzmj()+"")? monomerInfo.getDsjzmj() : "空");
            // 处理地下建筑面积（DXJZMJ），若为空则设置为"空"
            monomerJsonObject.put("DXJZMJ", StringUtils.isNotBlank(monomerInfo.getDxjzmj()+"")? monomerInfo.getDxjzmj() : "空");
            // 处理占地面积（ZDMJ），若为空则设置为"空"
            monomerJsonObject.put("ZDMJ", StringUtils.isNotBlank(monomerInfo.getZdmj()+"")? monomerInfo.getZdmj() : "空");
            // 处理建筑工程高度（JZGCGD），若为空则设置为"空"
            monomerJsonObject.put("JZGCGD", StringUtils.isNotBlank(monomerInfo.getJzgcgd()+"")? monomerInfo.getJzgcgd() : "空");
            // 处理地上层数（DSCS），若为空则设置为"空"
            monomerJsonObject.put("DSCS", StringUtils.isNotBlank(monomerInfo.getDscs())? Integer.parseInt(monomerInfo.getDscs()): 0);
            // 处理地下层数（DXCS），若为空则设置为"空"
            monomerJsonObject.put("DXCS", StringUtils.isNotBlank(monomerInfo.getDxcs())? Integer.parseInt(monomerInfo.getDxcs()) : 0);
            // 处理长度（CD），若为空则设置为"空"
            monomerJsonObject.put("CD", StringUtils.isNotBlank(monomerInfo.getCd()+"")? monomerInfo.getCd() : "空");
            // 处理跨度（KD），若为空则设置为"空"
            monomerJsonObject.put("KD", StringUtils.isNotBlank(monomerInfo.getKd()+"")? monomerInfo.getKd() : "空");
            monomerInfoArray.add(monomerJsonObject);
        }
        jsonObject.put("MonomerList", monomerInfoArray);
    }

    private void handleRequiredField(JSONObject jsonObject, String fieldName, String value) {
        if (StringUtils.isNotBlank(value)) {
            jsonObject.put(fieldName, value);
        } else {
            jsonObject.put(fieldName, "空");
        }
    }

    private void handleRequiredDateField(JSONObject jsonObject, String fieldName, Date value) {
        if (value != null) {
            jsonObject.put(fieldName, EpointDateUtil.convertDate2String(value, EpointDateUtil.DATE_FORMAT));
        } else {
            jsonObject.put(fieldName, "空");
        }
    }

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }

    @Override
    public List<FrameUser> getuserbyouguid(String ouguid, String roleguid) {
        return new IJnCertInfoService().getuserbyouguid(ouguid, roleguid);
    }

}
