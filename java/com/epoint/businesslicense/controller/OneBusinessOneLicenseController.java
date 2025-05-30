package com.epoint.businesslicense.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.businesslicense.api.entity.BusinessLicenseExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.common.util.*;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.wjw.api.ICxBusService;
import com.epoint.zwdt.util.TARequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 一业一证业务接口
 *
 * @author shibin
 * @description
 * @date 2020年5月18日 上午11:31:17
 */
@RestController
@RequestMapping("/OneBusinessOneLicense")
public class OneBusinessOneLicenseController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 基本信息API
     */
    @Autowired
    private IBusinessLicenseBaseinfo baseinfoService;

    @Autowired
    private IAuditProject auditProjectServcie;

    @Autowired
    private ICxBusService iCxBusService;

    @Autowired
    private IAttachService attachService;


    /**
     * 扩展信息API
     */
    @Autowired
    private IBusinessLicenseExtension extensionService;

    /**
     * 扩展信息API
     */
    @Autowired
    private ICertInfo certInfoService;

    /**
     * 保存申报信息
     *
     * @description
     * @author shibin
     * @date 2020年5月18日 上午11:36:34
     */
    @RequestMapping(value = "/submitApplicationInfo", method = RequestMethod.POST)
    public String submitApplicationInfo(@RequestBody String params) {

        String industryName = "";
        String industryCode = "";
        String regionCode = "";
        String applyNo = "";
        String serialNo = "";
        Date applyDate = null;

        JSONObject dataJson = new JSONObject();
        try {

            // 引入接口api
            IAuditSpIntegratedCompany iAuditSpIntegratedCompany = ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpIntegratedCompany.class);
            IAuditSpBusiness iAuditSpBusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
            IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
            IAuditSpPhase iAuditSpPhase = ContainerFactory.getContainInfo().getComponent(IAuditSpPhase.class);
            IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);

            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                log.info("=======开始调用submitApplicationInfo接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));

                JSONObject applicationObject = obj.getJSONObject("application");
                JSONObject formObject = obj.getJSONObject("form");
                JSONArray materialObject = obj.getJSONObject("material").getJSONArray("material");
                log.info("=======application=======" + applicationObject);

                JSONArray selectItem = null;
                if (applicationObject != null) {
                    industryName = applicationObject.getString("industryName");
                    industryCode = applicationObject.getString("industryCode");
                    regionCode = applicationObject.getString("regcode");
                    applyNo = applicationObject.getString("applyNo");
                    serialNo = applicationObject.getString("serialNo");
                    applyDate = applicationObject.getDate("applyDate");
                    selectItem = applicationObject.getJSONArray("item");
                } else {
                    dataJson.put("state", "300");// 业务标识
                    dataJson.put("error", "申请基本信息字段application为空！");// 业务标识
                    dataJson.put("bizId", "");// 业务标识
                    dataJson.put("receiveId", serialNo);// 业务流水号
                    return dataJson.toJSONString();
                }
                JSONObject applyerObject = formObject.getJSONObject("DC_YYYZ_DWJBXX");

                regionCode = regionCode.replaceAll("370801", "370800");

                //因浪潮系统辖区编码错误，导致需要对辖区编码转换
                //高新技术产业开发区：370833（对方）,转换为370890
                //经济开发区：370846 （对方） ,转换为370892
                regionCode = regionCode.replaceAll("370833", "370890");
                regionCode = regionCode.replaceAll("370846", "370892");
                // 获取businessGuid
                String businessGuid = ConfigUtil.getConfigValue("businesslicense", regionCode + industryCode);
                if (StringUtil.isBlank(businessGuid)) {

                    dataJson.put("state", "300");// 业务标识
                    dataJson.put("error", "该行业未配置相关主题套餐！" + regionCode + "===" + industryCode + "===" + industryName);// 业务标识
                    dataJson.put("bizId", "");// 业务标识
                    dataJson.put("receiveId", serialNo);// 业务流水号
                    log.info("=======结束调用submitApplicationInfo接口=======" + dataJson.toJSONString());
                    return dataJson.toJSONString();
                }
                // 2.2.1 生成主题实例信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                        .getResult();

                if (auditSpBusiness == null) {
                    dataJson.put("state", "300");// 业务标识
                    dataJson.put("error", "未查询到相关主题套餐！" + regionCode + "===" + industryCode + "===" + industryName);// 业务标识
                    dataJson.put("bizId", "");// 业务标识
                    dataJson.put("receiveId", serialNo);// 业务流水号
                    log.info("=======结束调用submitApplicationInfo接口=======" + dataJson.toJSONString());

                    return dataJson.toJSONString();
                }

                BusinessLicenseBaseinfo baseinfo = new BusinessLicenseBaseinfo();
                baseinfo.setOperatedate(new Date());
                baseinfo.setRowguid(UUID.randomUUID().toString());
                baseinfo.setIndustryName(industryName);
                baseinfo.setIndustryCode(industryCode);
                baseinfo.setRegionCode(regionCode);
                baseinfo.setApplyNo(applyNo);
                baseinfo.setSerialNo(serialNo);
                baseinfo.setServiceObj("10");
                baseinfo.setApplydate(applyDate);
                if (StringUtil.isNotBlank(applyerObject.getString("ENTNAME"))) {
                    baseinfo.setApplyername(applyerObject.getString("ENTNAME"));
                } else {
                    baseinfo.setApplyername(applyerObject.getString("ZBDW"));
                }
                baseinfo.setOperatedate(new Date());
                baseinfoService.insert(baseinfo);

                BusinessLicenseExtension extension = new BusinessLicenseExtension();
                extension.setOperatedate(new Date());
                extension.setRowguid(UUID.randomUUID().toString());
                extension.setBaseinfoGuid(baseinfo.getRowguid());
                if (selectItem != null) {
                    extension.setSelectItem(selectItem.toJSONString());
                }
                if (materialObject != null) {
                    extension.setMaterialsInfo(materialObject.toJSONString());
                }
                if (formObject != null) {
                    extension.setFormsInfo(formObject.toJSONString());
                }
                extensionService.insert(extension);

                // 初始化套餐
                // 2.1 保存基本信息
                AuditSpIntegratedCompany dataBean = new AuditSpIntegratedCompany();// 套餐式开公司实体对象
                String yewuGuid = UUID.randomUUID().toString().replace("-", "");
                dataBean.setRowguid(yewuGuid);
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername("一业一证对接");
                if (StringUtil.isNotBlank(applyerObject.getString("ENTNAME"))) {
                    dataBean.setCompanyname(applyerObject.getString("ENTNAME"));// 企业名称--用户名称
                } else {
                    dataBean.setCompanyname(applyerObject.getString("ZBDW"));// 企业名称--用户名称
                }
                dataBean.setZzjgdmz(applyerObject.getString("UNISCID"));// 统一社会信用代码--证件号码
                dataBean.setContactperson(applyerObject.getString("LEREP"));// 联系人
                dataBean.setContactphone(applyerObject.getString("TEL"));// 联系电话--联系人电话
                dataBean.setCapital(null);// 注册资本（万元）
                dataBean.setLegalpersonemail("");// 法人Email
                dataBean.setContactpostcode("");// 邮编
                dataBean.setFundssource("");// 经费来源 选择
                dataBean.setAddress(applyerObject.getString("OPLOC"));// 经营场所地址
                dataBean.setCompanytype("");// 企业类型 选择
                dataBean.setScope("");// 经营范围
                dataBean.setLegalpersonduty("");// 法人职务 请选择
                iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(dataBean);

                String businesstype = "3";
                if (industryCode.contains("ZZLB")) {
                    regionCode = regionCode.substring(0, 6);
                    businesstype = "6";
                }

                String biGuid = iAuditSpInstance.addBusinessInstance(businessGuid, yewuGuid, "",
                        dataBean.getCompanyname(), "", auditSpBusiness.getBusinessname(), regionCode, businesstype).getResult();

                String phaseGuid = "";
                List<AuditSpPhase> listPhase = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult();
                if (listPhase != null && listPhase.size() > 0) {
                    for (AuditSpPhase auditSpPhase : listPhase) {
                        if (!"1".equals(auditSpPhase.get("isbigphase"))) {
                            phaseGuid = auditSpPhase.getRowguid();
                            break;
                        }
                    }
                    // 2.2.2 生成子申报信息
                    String subappGuid = iAuditSpISubapp.addSubapp(businessGuid, biGuid, phaseGuid,
                                    dataBean.getCompanyname(), dataBean.getCompanyname(), "", ZwfwConstant.APPLY_WAY_CKDJ)
                            .getResult();

                    iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_DYS, null);
                }

                baseinfo.setBusinessGuid(businessGuid);
                baseinfo.setBiGuid(biGuid);
                baseinfoService.update(baseinfo);

                dataJson.put("state", "200");// 业务标识
                dataJson.put("error", "");// 业务标识
                dataJson.put("bizId", yewuGuid);// 业务标识
                dataJson.put("receiveId", serialNo);// 业务流水号
                log.info("=======结束调用submitApplicationInfo接口=======" + dataJson.toJSONString());
                return dataJson.toJSONString();

            } else {
                dataJson.put("state", "300");// 业务标识
                dataJson.put("error", "token验证失败！");// 业务标识
                dataJson.put("bizId", "");// 业务标识
                dataJson.put("receiveId", serialNo);// 业务流水号

                return dataJson.toJSONString();
            }
        } catch (Exception e) {
            log.info("=======结束调用submitApplicationInfo接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======submitApplicationInfo异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    /**
     * 获取证照信息
     *
     * @description
     * @author shibin
     * @date 2020年5月18日 上午11:36:34
     */
    @RequestMapping(value = "/getAllLicenceInfo", method = RequestMethod.POST)
    public String getAllLicenceInfo(@RequestBody String params) {

        log.info("=======开始调用getAllLicenceInfo接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));

        String certno = "";

        JSONObject dataJson = new JSONObject();
        JSONObject returnJson = null;
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();

        List<JSONObject> list = new ArrayList<JSONObject>();
        List<String> listCertguid = new ArrayList<String>();

        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                //行业许可证编号
                certno = obj.getString("certno");
                if (StringUtil.isNotBlank(certno)) {

                    listCertguid = baseinfoService.getCertguidByCerno(certno);

                    for (String rowguid : listCertguid) {
                        CertInfo certInfo = certInfoService.getCertInfoByRowguid(rowguid);
                        param.put("certno", certInfo.getCertno());
                        param.put("certcatalogid", "");
                        param.put("tycertcatcode", certInfo.getTycertcatcode());
                        sumbit.put("appkey", "de8afc3b-9c48-11ea-bac1-00ff8ea5ec12");
                        sumbit.put("params", param);
                        log.info("======tycertcatcode======:" + certInfo.getTycertcatcode());
                        //发送请求 
                        String note = TARequestUtil.sendPost(
                                "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                                sumbit.toJSONString(), "", "");
                        JSONObject object = JSONObject.parseObject(note);
                        JSONObject object2 = object.getJSONObject("custom");
                        JSONObject object3 = object2.getJSONObject("certinfoextension");
                        returnJson = new JSONObject();
                        returnJson.put("certname", certInfo.getCertname());
                        returnJson.put("certno", certInfo.getCertno());
                        returnJson.put("certinfo", object3);
                        list.add(returnJson);
                    }
                    log.info("=======结束调用getAllLicenceInfo接口======="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));

                    dataJson.put("dataList", list);
                    dataJson.put("state", "1");
                    dataJson.put("retCode", "SUCCESS");
                    dataJson.put("statusCode", "200");
                    dataJson.put("error", "");

                    return dataJson.toJSONString();
                } else {
                    dataJson.put("dataList", "");
                    dataJson.put("state", "1");
                    dataJson.put("retCode", "SUCCESS");
                    dataJson.put("statusCode", "200");
                    dataJson.put("error", "证照编号为空！");
                    return dataJson.toJSONString();
                }
            } else {
                return dataJson.toJSONString();

            }
        } catch (Exception e) {
            log.info("=======结束调用submitApplicationInfo接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======submitApplicationInfo异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    /**
     * 下载证照图片
     *
     * @description
     * @author shibin
     * @date 2020年5月18日 上午11:36:34
     */
    @RequestMapping(value = "/downloadCertFile", method = RequestMethod.POST)
    public String downloadCertFile(@RequestBody String params) {

        log.info("=======开始调用downloadCertFile接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));

        String certAttachguid = "";
        JSONObject dataJson = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                certAttachguid = obj.getString("certAttachguid");

                log.info("=======结束调用downloadCertFile接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return dataJson.toJSONString();

            } else {
                dataJson.put("state", "300");// 业务标识
                dataJson.put("error", "token验证失败！");// 业务标识
                dataJson.put("bizId", "");// 业务标识

                return dataJson.toJSONString();
            }
        } catch (Exception e) {
            log.info("=======结束调用downloadCertFile接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======downloadCertFile异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    /**
     * H5页面接口
     *
     * @description
     * @author shibin
     * @date 2020年5月22日 下午11:35:31
     */
    @RequestMapping(value = "/getCertinfoList", method = RequestMethod.POST)
    public String getCertinfoList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                        sumbit.toJSONString(), "", "");
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                String jyzmc = object3.getString("jyzmc");
                String tyxy = object3.getString("tyxy");
                String xkxm = object3.getString("xkxm");
                String fzjg = object3.getString("fzjg");

                if (StringUtil.isNotBlank(jyzmc)) {
                    object3.put("entname", jyzmc);
                    object3.put("socialcode", tyxy);
                    object3.put("jyxm", xkxm);
                    object3.put("issuedept", fzjg);
                }
                List<String> list = baseinfoService.findCertRowguidByCertno(certInfo.getCertno(),
                        certInfo.getRowguid());
                JSONArray jsonArray = new JSONArray();

                for (String url : list) {
                    Map<String, String> materguidurl = new HashMap<String, String>();
                    materguidurl.put("materguidurl", url);
                    jsonArray.add(materguidurl);
                }

                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }


    /**
     * H5页面接口
     *
     * @description
     * @author shibin
     * @date 2020年5月22日 下午11:35:31
     */
    @RequestMapping(value = "/getCertinfoYlggscList", method = RequestMethod.POST)
    public String getCertinfoYlggscList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoYlggscList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));
                if (certInfo != null && certInfo.getIshistory() == 1 && certInfo.getCertname().contains("建筑业企业资质")) {
                    return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
                }

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);


                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                String jyzmc = object3.getString("jyzmc");
                String tyxy = object3.getString("tyxy");
                String xkxm = object3.getString("xkxm");
                String fzjg = object3.getString("fzjg");

                if (StringUtil.isNotBlank(jyzmc)) {
                    object3.put("entname", jyzmc);
                    object3.put("socialcode", tyxy);
                    object3.put("jyxm", xkxm);
                    object3.put("issuedept", fzjg);
                }
                List<Record> attachlist = baseinfoService.findMaterialsAndCert(certInfo.getCertno(),
                        certInfo.getRowguid());
                    JSONArray jsonArray = new JSONArray();


                for (Record rec : attachlist) {
                    Map<String, String> materguidurl = new HashMap<String, String>();
                    materguidurl.put("attachguid", rec.getStr("attachguid"));
                    materguidurl.put("attachname", rec.getStr("attachname"));
                    jsonArray.add(materguidurl);
                }



                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoYlggscList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoYlggscList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoYlggscList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getCertinfoYljgfsscList", method = RequestMethod.POST)
    public String getCertinfoYljgfsscList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoYljgfsscList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                if (certInfo.getIshistory() == 1) {
                    return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
                }

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                String bh = object3.getString("bh");
                String yljgmc = object3.getString("yljgmc");
                String xmmc = object3.getString("xmmc");
                String xmdz = object3.getString("xmdz");
                String xmxz = object3.getString("xmxz");
                String fddbr = object3.getString("fddbr");
                String lxr = object3.getString("lxr");
                String xmfzr = object3.getString("xmfzr");
                String lxdh = object3.getString("lxdh");
                String RQ = object3.getString("RQ");

                if (StringUtil.isNotBlank(bh)) {
                    object3.put("bh", bh);
                    object3.put("yljgmc", yljgmc);
                    object3.put("xmmc", xmmc);
                    object3.put("xmdz", xmdz);
                    object3.put("xmxz", xmxz);
                    object3.put("fddbr", fddbr);
                    object3.put("lxr", lxr);
                    object3.put("xmfzr", xmfzr);
                    object3.put("lxdh", lxdh);
                    object3.put("RQ", RQ);
                }
                List<Record> attachlist = baseinfoService.findMaterialsAndCert(certInfo.getCertno(),
                        certInfo.getRowguid());
                JSONArray jsonArray = new JSONArray();

                for (Record rec : attachlist) {
                    Map<String, String> materguidurl = new HashMap<String, String>();
                    materguidurl.put("attachguid", rec.getStr("attachguid"));
                    materguidurl.put("attachname", rec.getStr("attachname"));
                    jsonArray.add(materguidurl);
                }

                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoYljgfsscList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoYljgfsscList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoYljgfsscList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getCertinfoYljgfsjgList", method = RequestMethod.POST)
    public String getCertinfoYljgfsjgList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoYljgfsjgList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                if (certInfo.getIshistory() == 1) {
                    return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
                }

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                String bh = object3.getString("bh");
                String yljgmc = object3.getString("yljgmc");
                String xmmc = object3.getString("xmmc");
                String xmdz = object3.getString("xmdz");
                String xmxz = object3.getString("xmxz");
                String fddbr = object3.getString("fddbr");
                String lxr = object3.getString("lxr");
                String xmfzr = object3.getString("xmfzr");
                String lxdh = object3.getString("lxdh");
                String RQ = object3.getString("RQ");

                if (StringUtil.isNotBlank(bh)) {
                    object3.put("bh", bh);
                    object3.put("yljgmc", yljgmc);
                    object3.put("xmmc", xmmc);
                    object3.put("xmdz", xmdz);
                    object3.put("xmxz", xmxz);
                    object3.put("fddbr", fddbr);
                    object3.put("lxr", lxr);
                    object3.put("xmfzr", xmfzr);
                    object3.put("lxdh", lxdh);
                    object3.put("RQ", RQ);
                }
                List<Record> attachlist = baseinfoService.findMaterialsAndCert(certInfo.getCertno(),
                        certInfo.getRowguid());
                JSONArray jsonArray = new JSONArray();

                List<CertInfo> jgcerts = baseinfoService.getCertListByCertno(certInfo.getCertownerno());

                for (CertInfo cert : jgcerts) {
                    List<Record> attachs = baseinfoService.findMaterialsAndCert(certInfo.getCertno(),
                            cert.getRowguid());
                    for (Record rec : attachs) {
                        Map<String, String> materguidurl = new HashMap<String, String>();
                        materguidurl.put("attachguid", rec.getStr("attachguid"));
                        materguidurl.put("attachname", rec.getStr("attachname"));
                        jsonArray.add(materguidurl);
                    }
                }

                for (Record rec : attachlist) {
                    Map<String, String> materguidurl = new HashMap<String, String>();
                    materguidurl.put("attachguid", rec.getStr("attachguid"));
                    materguidurl.put("attachname", rec.getStr("attachname"));
                    jsonArray.add(materguidurl);
                }

                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoYljgfsjgList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoYljgfsjgList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoYljgfsjgList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    /**
     * H5页面接口
     *
     * @description
     * @author shibin
     * @date 2020年5月22日 下午11:35:31
     */
    @RequestMapping(value = "/getCertinfoDetail", method = RequestMethod.POST)
    public String getCertinfoDetail(@RequestBody String params) {

        log.info("=======开始调用getCertinfoDetail接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String keyvalue = obj.getString("keyvalue");

            if (ZwdtConstant.SysValidateData.equals(token)) {


                String appkey = ConfigUtil.getConfigValue("businesslicense", "370800");

                param.put("zsbh", keyvalue);
                param.put("certcatalogid", "");
                param.put("tycertcatcode", "");
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======appkey=======" + appkey);

                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionJzjn",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");
                if (object3 == null || StringUtils.isBlank(object3.getString("zsbh"))) {
                    CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));
                    if (certInfo != null && certInfo.getIshistory() == 1) {
                        return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
                    }
                    appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());
                    param.clear();
                    param.put("certno", certInfo.getCertno());
                    param.put("certcatalogid", "");
                    param.put("tycertcatcode", certInfo.getTycertcatcode());
                    sumbit.clear();
                    sumbit.put("appkey", appkey);
                    sumbit.put("params", param);
                    log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                    log.info("=======appkey=======" + appkey);
                    //发送请求
                    note = TARequestUtil.sendPost(
                            "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                            sumbit.toJSONString(), "", "");
                    log.info("返回的证照信息：" + note);
                    object = JSONObject.parseObject(note);
                    object2 = object.getJSONObject("custom");
                    object3 = object2.getJSONObject("certinfoextension");
                }
                String zsbh = object3.getString("zsbh");
                Date newfzrq = object3.getDate("newfzrq");
                Date newyxqx = object3.getDate("newyxqx");
                String sqdw = object3.getString("sqdw");
                String cpmc = object3.getString("cpmc");
                String zxbz = object3.getString("zxbz");
                String qybzbh = object3.getString("qybzbh");
                String qybzcpm = object3.getString("qybzcpm");

                if ("企业标准产品名称".equals(cpmc)) {
                    cpmc = qybzcpm;
                }

                if ("企业标准".equals(zxbz)) {
                    zxbz = qybzbh;
                }

                if (StringUtil.isNotBlank(zsbh)) {
                    object3.put("zsbh", zsbh);
                    object3.put("newfzrq", EpointDateUtil.convertDate2String(newfzrq, EpointDateUtil.DATE_FORMAT));
                    object3.put("newyxqx", EpointDateUtil.convertDate2String(newyxqx, EpointDateUtil.DATE_FORMAT));
                    object3.put("sqdw", sqdw);
                    object3.put("cpmc", cpmc);
                    object3.put("zxbz", zxbz);
                }

                dataJson.put("certinfoextension", object3);

                log.info("=======结束调用getCertinfoDetail接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoDetail接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            e.printStackTrace();
            log.info("=======getCertinfoDetail异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    /**
     * 公共卫生许可证照信息渲染
     *
     * @description
     * @author shibin
     * @date 2020年5月22日 下午11:35:31
     */
    @RequestMapping(value = "/getCertinfoGgcswsxkList", method = RequestMethod.POST)
    public String getCertinfoGgcswsxkList(@RequestBody String params) {
        log.info("=======开始调用getCertinfoGgcswsxkList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                if (certInfo.getIshistory() == 1) {
                    return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
                }

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                String babh = object3.getString("babh");
                String xkxm = object3.getString("xkxm");
                String dwmc = object3.getString("dwmc");
                String dz = object3.getString("dz");
                String fzr = object3.getString("fzr");
                String fzrqn = object3.getString("fzrqn");
                String fzrqy = object3.getString("fzrqy");
                String fzrqr = object3.getString("fzrqr");
                String yxqzn = object3.getString("yxqzn");
                String yxqzy = object3.getString("yxqzy");
                String yxqzr = object3.getString("yxqzr");
                String jyxz = object3.getString("jyxz");

                object3.put("babh", babh);
                object3.put("xkxm", xkxm);
                object3.put("dwmc", dwmc);
                object3.put("dz", dz);
                object3.put("fzr", fzr);
                object3.put("jyxz", jyxz);
                object3.put("fzrq", fzrqn + "年" + fzrqy + "月" + fzrqr + "日");
                object3.put("yxqs", fzrqn + "年" + fzrqy + "月" + fzrqr + "日");
                object3.put("yxqz", yxqzn + "年" + yxqzy + "月" + yxqzr + "日");

                List<Record> attachlist = baseinfoService.findMaterialsAndCert(certInfo.getCertno(),
                        certInfo.getRowguid());
                JSONArray jsonArray = new JSONArray();


                for (Record rec : attachlist) {
                    Map<String, String> materguidurl = new HashMap<String, String>();
                    materguidurl.put("attachguid", rec.getStr("attachguid"));
                    materguidurl.put("attachname", rec.getStr("attachname"));
                    jsonArray.add(materguidurl);
                }

                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoGgcswsxkList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoGgcswsxkList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoGgcswsxkList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }


    /**
     * 公共卫生许可证照信息渲染
     *
     * @description
     * @author shibin
     * @date 2020年5月22日 下午11:35:31
     */
    @RequestMapping(value = "/getCertinfoSjzqyzzzList", method = RequestMethod.POST)
    public String getCertinfoSjzqyzzzList(@RequestBody String params) {
        log.info("=======开始调用getCertinfoSjzqyzzzList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                if (certInfo.getIshistory() == 1) {
                    return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
                }

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");


                String zsbh = object3.getString("zsbh");
                String qymc = object3.getString("qymc");
                String tyxydm = object3.getString("tyxydm");
                String xxdz = object3.getString("xxdz");
                String jjxz = object3.getString("jjxz");
                String zzlbjdj = object3.getString("zzlbjdj");
                Date yxq = object3.getDate("yxq");
                Date fzrq = object3.getDate("fzrq");
                String frdb = object3.getString("frdb");
                String zczbj = object3.getString("zczbj");

                if (StringUtil.isNotBlank(zsbh)) {
                    object3.put("zsbh", zsbh);
                    object3.put("qymc", qymc);
                    object3.put("tyxydm", tyxydm);
                    object3.put("xxdz", xxdz);
                    object3.put("jjxz", jjxz);
                    object3.put("zzlbjdj", zzlbjdj);
                    object3.put("jyxz", jjxz);
                    object3.put("fzrq", EpointDateUtil.convertDate2String(fzrq, EpointDateUtil.DATE_FORMAT));
                    object3.put("yxq", EpointDateUtil.convertDate2String(yxq, EpointDateUtil.DATE_FORMAT));
                    object3.put("frdb", frdb);
                    object3.put("zczbj", zczbj);
                }

                object3.put("fzjg", "山东省住房和城乡建设厅");

                JSONArray jsonArray = new JSONArray();

                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoSjzqyzzzList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoSjzqyzzzList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoSjzqyzzzList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    /**
     * 公共卫生许可证照信息渲染
     *
     * @description
     * @author shibin
     * @date 2020年5月22日 下午11:35:31
     */
    @RequestMapping(value = "/getCertinfoCsjzljpzsList", method = RequestMethod.POST)
    public String getCertinfoCsjzljpzsList(@RequestBody String params) {
        log.info("=======开始调用getCertinfoCsjzljpzsList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                /*if (certInfo.getIshistory() == 1) {
    				return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
    			}*/

                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("certrowguid", obj.getString("rowguid"));
                AuditProject project = auditProjectServcie.getAuditProjectByCondition(sql.getMap());

                Record record = null;
                if (project != null) {
                    record = iCxBusService.getDzbdDetail("formtable20220420093558", project.getRowguid());
                }
                log.info("城市建筑实体信息：" + record);
                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");


                if (record != null) {
                    Date sqrq = record.getDate("sqrq");
                    String dwmc = record.getStr("dwmc");
                    String tyshxydm = record.getStr("tyshxydm");
                    String xm = record.getStr("xm");
                    String lxdh = record.getStr("lxdh");
                    String sfzh = record.getStr("sfzh");
                    String wtdlrxm = record.getStr("wtdlrxm");
                    String wtdlrlxdh = record.getStr("wtdlrlxdh");
                    String wtdlrsfzh = record.getStr("wtdlrsfzh");
                    String gdmc = record.getStr("gdmc");
                    String gdwz = record.getStr("gdwz");
                    String jsdwmc = record.getStr("jsdwmc");
                    String jsdwfddbr = record.getStr("jsdwfddbr");
                    String jsdwlxdh = record.getStr("jsdwlxdh");
                    String sgdwmc = record.getStr("sgdwmc");
                    String sgdwfddbr = record.getStr("sgdwfddbr");
                    String sgdwdh = record.getStr("sgdwdh");
                    String tf = record.getStr("tf");
                    String jzlj = record.getStr("jzlj");
                    Date czqxrq = record.getDate("czqxrq");
                    Date czqxrqend = record.getDate("czqxrqend");
                    String yssd = record.getStr("yssd");
                    String yslx = record.getStr("yslx");
                    String wbk22 = record.getStr("wbk22");
                    String lxdh1 = record.getStr("lxdh1");
                    String xncs2 = record.getStr("xncs2");
                    String lxdh2 = record.getStr("lxdh2");
                    String xncs3 = record.getStr("xncs3");
                    String lxdh3 = record.getStr("lxdh3");

                    object3.put("sqrq", EpointDateUtil.convertDate2String(sqrq, EpointDateUtil.DATE_FORMAT));
                    object3.put("dwmc", dwmc);
                    object3.put("tyshxydm", tyshxydm);
                    object3.put("xm", xm);
                    object3.put("lxdh", lxdh);
                    object3.put("sfzh", sfzh);
                    object3.put("wtdlrxm", wtdlrxm);
                    object3.put("wtdlrlxdh", wtdlrlxdh);
                    object3.put("wtdlrsfzh", wtdlrsfzh);
                    object3.put("gdmc", gdmc);
                    object3.put("gdwz", gdwz);
                    object3.put("jsdwmc", jsdwmc);
                    object3.put("jsdwfddbr", jsdwfddbr);
                    object3.put("jsdwlxdh", jsdwlxdh);
                    object3.put("sgdwmc", sgdwmc);
                    object3.put("sgdwfddbr", sgdwfddbr);
                    object3.put("sgdwdh", sgdwdh);
                    object3.put("tf", tf);
                    object3.put("jzlj", jzlj);
                    object3.put("czqxrq", EpointDateUtil.convertDate2String(czqxrq, EpointDateUtil.DATE_FORMAT));
                    object3.put("czqxrqend", EpointDateUtil.convertDate2String(czqxrqend, EpointDateUtil.DATE_FORMAT));
                    object3.put("yssd", yssd);
                    object3.put("yslx", yslx);
                    object3.put("wbk22", wbk22);
                    object3.put("lxdh1", lxdh1);
                    object3.put("xncs2", xncs2);
                    object3.put("lxdh2", lxdh2);
                    object3.put("xncs3", xncs3);
                    object3.put("lxdh3", lxdh3);
                }


                JSONArray jsonArray = new JSONArray();

                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoCsjzljpzsList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoCsjzljpzsList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoCsjzljpzsList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getCertinfoWykwycDetail", method = RequestMethod.POST)
    public String getCertinfoWykwycDetail(@RequestBody String params) {

        log.info("=======开始调用getCertinfoWykwycDetail接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                if (certInfo.getIshistory() == 1) {
                    return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
                }

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certinfoguid", certInfo.getRowguid());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求 
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionCertid",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                object3.put("xingming", object3.getString("xingming"));
                object3.put("zhuzhi", object3.getString("zhuzhi"));
                object3.put("zhenghao", object3.getString("zhenghao"));
                object3.put("xingbie", object3.getString("xingbie"));
                object3.put("zhunjcx", object3.getString("zhunjcx"));
                object3.put("cslzrq", object3.getString("cslzrq"));
                object3.put("chusrq", object3.getString("chusrq"));
                object3.put("guoji", object3.getString("guoji"));
                object3.put("youxqsrq", object3.getString("youxqsrq"));

                JSONArray jsonArray = new JSONArray();
                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);
                log.info("=======结束调用getCertinfoWykwycDetail接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoWykwycDetail接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoWykwycDetail异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getCertinfoGhysjyxkzList", method = RequestMethod.POST)
    public String getCertinfoGhysjyxkzList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoGhysjyxkzList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                if (certInfo.getIshistory() == 1) {
                    return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
                }

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certinfoguid", certInfo.getRowguid());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionCertid",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                String ksrq = object3.getString("qn") + "-" + object3.getString("qy") + "-" + object3.getString("qr");
                String jsrq = object3.getString("zn") + "-" + object3.getString("zy") + "-" + object3.getString("zr");
                String fzrq = object3.getString("n") + "-" + object3.getString("y") + "-" + object3.getString("r");

                object3.put("ksrq", ksrq);
                object3.put("jsrq", jsrq);
                object3.put("fzrq", fzrq);

                JSONArray jsonArray = new JSONArray();
                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);
                log.info("=======结束调用getCertinfoGhysjyxkzList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoGhysjyxkzList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoGhysjyxkzList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getCertinfoCbyyyszsqList", method = RequestMethod.POST)
    public String getCertinfoCbyyyszsqList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoCbyyyszsqList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certinfoguid", certInfo.getRowguid());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionCertid",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                String jsrq = object3.getString("zn") + object3.getString("zy") + object3.getString("zr");

                object3.put("jsrq", jsrq);

                JSONArray jsonArray = new JSONArray();
                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);
                log.info("=======结束调用getCertinfoCbyyyszsqList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoCbyyyszsqList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoCbyyyszsqList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }


    @RequestMapping(value = "/getCertinfoJnjzjnrdList", method = RequestMethod.POST)
    public String getCertinfoJnjzjnrdList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoJnjzjnrdList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certinfoguid", certInfo.getRowguid());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionCertid",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");


                JSONArray jsonArray = new JSONArray();
                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);
                log.info("=======结束调用getCertinfoCbyyyszsqList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoJnjzjnrdList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoJnjzjnrdList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }


    @RequestMapping(value = "/getCertinfoGckcsjzzList", method = RequestMethod.POST)
    public String getCertinfoGckcsjzzList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoGckcsjzzList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certinfoguid", certInfo.getRowguid());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionCertid",
                        sumbit.toJSONString(), "", "");
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");


                JSONArray jsonArray = new JSONArray();
                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);
                log.info("=======结束调用getCertinfoGckcsjzzList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoGckcsjzzList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoGckcsjzzList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getCertinfoXdcpwsxkList", method = RequestMethod.POST)
    public String getCertinfoXdcpwsxkList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoXdcpwsxkList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certinfoguid", certInfo.getRowguid());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionCertid",
                        sumbit.toJSONString(), "", "");
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");


                JSONArray jsonArray = new JSONArray();
                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);
                log.info("=======结束调用getCertinfoXdcpwsxkList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoXdcpwsxkList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoXdcpwsxkList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getCertinfoPwList", method = RequestMethod.POST)
    public String getCertinfoPwList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoPwList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certinfoguid", certInfo.getRowguid());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionCertid",
                        sumbit.toJSONString(), "", "");
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                String pwskbh = object3.getString("pwskbh");
                String psqx = object3.getString("psqx");
                String psl = object3.getString("psl");
                String wszzqx = object3.getString("wszzqx");
                String paiwskbh = object3.getString("paiwskbh");
                String paisqx = object3.getString("paisqx");
                String paisl = object3.getString("paisl");
                String wuszzqx = object3.getString("wuszzqx");
                String paiwuskbh = object3.getString("paiwuskbh");
                String paishuiqx = object3.getString("paishuiqx");
                String pshuil = object3.getString("pshuil");
                String wshuizzqx = object3.getString("wshuizzqx");
                String paiwushuikbh = object3.getString("paiwushuikbh");
                String paishueiqux = object3.getString("paishueiqux");
                String psliang = object3.getString("psliang");
                String wszhzqx = object3.getString("wszhzqx");
                String paiwushuikobh = object3.getString("paiwushuikobh");
                String pshuiquxiang = object3.getString("pshuiquxiang");
                String paisliang = object3.getString("paisliang");
                String wszzqux = object3.getString("wszzqux");
                String pwushuikbh = object3.getString("pwushuikbh");
                String paishuiquxiang = object3.getString("paishuiquxiang");
                String pshuiliang = object3.getString("pshuiliang");
                String wszzqxiang = object3.getString("wszzqxiang");
                if (StringUtil.isNotBlank(pwskbh)) {
                    object3.put("xknr1", "排污水口编号:" + pwskbh + ",排水去向:" + psqx + ",排水量：" + psl + ",污水最终去向:" + wszzqx);
                }
                if (StringUtil.isNotBlank(paiwskbh)) {
                    object3.put("xknr2", "排污水口编号:" + paiwskbh + ",排水去向:" + paisqx + ",排水量：" + paisl + ",污水最终去向:" + wuszzqx);
                }
                if (StringUtil.isNotBlank(paiwuskbh)) {
                    object3.put("xknr3", "排污水口编号:" + paiwuskbh + ",排水去向:" + paishuiqx + ",排水量：" + pshuil + ",污水最终去向:" + wshuizzqx);
                }
                if (StringUtil.isNotBlank(paiwushuikbh)) {
                    object3.put("xknr4", "排污水口编号:" + paiwushuikbh + ",排水去向:" + paishueiqux + ",排水量：" + psliang + ",污水最终去向:" + wszhzqx);
                }
                if (StringUtil.isNotBlank(paiwushuikobh)) {
                    object3.put("xknr5", "排污水口编号:" + paiwushuikobh + ",排水去向:" + pshuiquxiang + ",排水量：" + paisliang + ",污水最终去向:" + wszzqux);
                }
                if (StringUtil.isNotBlank(pwushuikbh)) {
                    object3.put("xknr6", "排污水口编号:" + pwushuikbh + ",排水去向:" + paishuiquxiang + ",排水量：" + pshuiliang + ",污水最终去向:" + wszzqxiang);
                }

                JSONArray jsonArray = new JSONArray();
                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);
                log.info("=======结束调用getCertinfoXdcpwsxkList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoPwList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoPwList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }


    @RequestMapping(value = "/getCertinfoPhoto", method = RequestMethod.POST)
    public String getCertinfoPhoto(@RequestBody String params) {

        log.info("=======开始调用getCertinfoPhoto接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));
                String qrcodeurl = "";
                if (certInfo != null) {
                    String url = ConfigUtil.getConfigValue("officeweb365url");
                    String documentType = ".pdf";
                    List<FrameAttachInfo> attachList = attachService.getAttachInfoListByGuid(certInfo.getCertcliengguid());
                    if (attachList != null && attachList.size() > 0) {
                        FrameAttachInfo attachInfo = attachList.get(0);
                        log.info("建筑节能事项证照材料：" + attachInfo.getAttachGuid());
                        String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                + attachInfo.getAttachGuid();
                        String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(downloadUrl, documentType);
                        qrcodeurl = url + encryptUrl;
                    }
                }


                JSONArray jsonArray = new JSONArray();
                dataJson.put("qrcodeurl", qrcodeurl);
                dataJson.put("materials", jsonArray);
                log.info("=======结束调用getCertinfoXdcpwsxkList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoPhoto接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoPhoto异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }


    /**
     * H5页面接口
     *
     * @description
     * @author shibin
     * @date 2020年5月22日 下午11:35:31
     */
    @RequestMapping(value = "/getCertinfoJsxmydList", method = RequestMethod.POST)
    public String getCertinfoJsxmydList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoJsxmydList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certinfoguid", certInfo.getRowguid());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionCertid",
                        sumbit.toJSONString(), "", "");
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                List<Record> attachlist = baseinfoService.findMaterialsAndCert(certInfo.getCertno(),
                        certInfo.getRowguid());
                JSONArray jsonArray = new JSONArray();


                for (Record rec : attachlist) {
                    Map<String, String> materguidurl = new HashMap<String, String>();
                    materguidurl.put("attachguid", rec.getStr("attachguid"));
                    materguidurl.put("attachname", rec.getStr("attachname"));
                    jsonArray.add(materguidurl);
                }

                dataJson.put("certinfoextension", object3);
                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoJsxmydList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用getCertinfoJsxmydList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoJsxmydList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

}
