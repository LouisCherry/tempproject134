package com.epoint.jnzwfw.lcproject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.individual.domain.AuditRsIndividualBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jnycsl.utils.HttplcUtils;
import com.epoint.jnycsl.utils.WavePushInterfaceUtils;
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.epoint.xmz.cxbus.impl.CxBusService;
import com.epoint.xmz.lcprojecterror.api.ILcprojectErrorService;
import com.epoint.xmz.lcprojecterror.api.entity.LcprojectError;
import org.apache.log4j.Logger;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/lcprojectrest")
public class LcProjectRestController {

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditProjectMaterial auditprojectmaterial;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private ILcprojectErrorService iLcprojectErrorService;

    @Autowired
    private IConfigService frameconfigservice;

    @Autowired
    private IAuditProjectUnusual auditProjectUnusual;

    @Autowired
    private ICxBusService iCxBusService;

    /**
     * 办件推送浪潮工具类
     */
    WavePushInterfaceUtils wavePushInterfaceUtils = new WavePushInterfaceUtils();

    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial auditTaskMaterialService;

    @Autowired
    private IAuditProject auditProjectServcie;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static final String lc_url = ConfigUtil.getConfigValue("epointframe", "lcurl");

    /**
     * 查询企业信息接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/otherSystemAccpet", method = RequestMethod.POST)
    public String otherSystemAccpet(@RequestBody String params) {
        try {
            log.info("=======开始调用otherSystemAccpet接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            JSONObject jsonObject1 = jsonObject.getJSONObject("params");
            String url = lc_url + "/web/approval/otherSystemAccpet";
            String projectguid = jsonObject1.getString("projectguid");
            String areacode = jsonObject1.getString("areacode");
            String acceptusername = jsonObject1.getString("acceptusername");
            String fields = " * ";
            AuditProject auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
            JSONObject postdata = new JSONObject();

            if (auditProject != null) {
                AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                if (auditTask != null) {
                    if (StringUtil.isBlank(auditTask.getStr("INNER_CODE"))) {
                        return JsonUtils.zwdtRestReturn("0", "事项INNER_CODE未配置！", "");
                    }

                    //公共服务类的事项是9090，其他权力类的事项是7090
                    if ("11".equals(auditTask.getShenpilb())) {
                        url = "http://59.206.96.200:9090/web/approval/otherSystemAccpet";
                    } else if ("10".equals(auditTask.getShenpilb()) && "07".equals(auditTask.getShenpilb())) {
                        url = "http://59.206.96.200:7090/web/approval/otherSystemAccpet";
                    }

                    postdata.put("itemCode",
                            StringUtil.isNotBlank(auditTask.getStr("INNER_CODE")) ? auditTask.getStr("INNER_CODE") : "无");
                    postdata.put("bussReceiveNum", auditProject.getFlowsn());
                    postdata.put("isMatchUser", "0");
                    postdata.put("acceptUserName", acceptusername);
                    postdata.put("submitTime", EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                            EpointDateUtil.DATE_TIME_FORMAT));
                    postdata.put("isMatchFlow", "0");

                    if (StringUtil.isNotBlank(auditTask.getStr("lcformid"))) {
                        postdata.put("formId", auditTask.getStr("lcformid"));
                    } else {
                        postdata.put("formId", "XinYeWuXinXiBiaoDan");
                    }

                    if ("申请省际普通货物水路运输经营许可".equals(auditTask.getTaskname())) {
                        postdata.put("formId", "ShuiLuYunShuQiYe");
                    }

                    if ("建筑节能技术产品初次认定".equals(auditTask.getTaskname())) {
                        postdata.put("formId", "JianZhuJieNenJiShuChanPinRenDi");
                    }

                    int objectType = 2;
                    if (auditProject.getApplyertype() == 10) {
                        objectType = 3;
                    } else if (auditProject.getApplyertype() == 20) {
                        objectType = 1;
                    }
                    postdata.put("objectType", Integer.toString(objectType));

                    JSONObject formInfo = new JSONObject();

                    // 推送浪潮新增加字段
                    formInfo.put("DianZiYouXiang", "");
                    formInfo.put("ShouLiRiQi",
                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    formInfo.put("ShenFenZhengHaoSheHuiTongYiXin", auditProject.getCertnum());
                    formInfo.put("ShenQingRenXingMingDanWeiMingC", auditProject.getApplyername());
                    formInfo.put("JieShouDuanXinShouJiHaoMa", auditProject.getContactmobile());
                    formInfo.put("ShenQingRenDanWeiDiZhi", auditProject.getAddress());
                    formInfo.put("SuoShuXiaQu", "济宁市");
                    formInfo.put("LianXiDianHua", auditProject.getContactphone());

                    // 材料相关的数据
                    JSONArray materialArray = new JSONArray();
                    // 办件材料列表
                    List<AuditProjectMaterial> projectmaterialList = auditprojectmaterial.selectProjectMaterial(projectguid).getResult();

                    if ((projectmaterialList != null) && (!projectmaterialList.isEmpty())) {
                        for (AuditProjectMaterial auditProjectMaterial : projectmaterialList) {
                            AuditTaskMaterial auditTaskMaterial = auditTaskMaterialService.getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                            JSONObject materialJsonObj1 = new JSONObject();

                            List<FrameAttachInfo> frameAttachInfo = attachService.getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                            if ((frameAttachInfo != null) && (!frameAttachInfo.isEmpty())) {
                                for (FrameAttachInfo info : frameAttachInfo) {
                                    // 操作人ID
                                    JSONObject materialJsonObj = new JSONObject();

                                    materialJsonObj.put("OPERATOR_ID", "");
                                    // 操作人
                                    materialJsonObj.put("OPERATOR_NAME", "统一接件人员");
                                    // 材料ID
                                    materialJsonObj.put("DOCUMENT_ID", auditTaskMaterial.getMaterialid());
                                    // 材料名称
                                    materialJsonObj.put("DOCUMENT_NAME", auditTaskMaterial.getMaterialname());
                                    Map<String, String> params1 = new HashMap<String, String>();
                                    String jnuserid = "";
                                    String langchaouid = frameconfigservice.getFrameConfigValue("AS_IS_LANGCHAOUID");
                                    if (StringUtil.isNotBlank(langchaouid)) {
                                        String[] uidlist = langchaouid.split(";");
                                        int length = uidlist.length - 1;
                                        Random random = new SecureRandom();
                                        int rand = random.nextInt(length);
                                        jnuserid = uidlist[rand];
                                        params1.put("uid", jnuserid);
                                    } else {
                                        params1.put("uid", "jnuserid");
                                    }
                                    params1.put("type", "doc");
                                    params1.put("folder_name", "jiningycsl");
                                    String file = info.getFilePath() + info.getAttachFileName();
                                    // 上传浪潮网盘
                                    String materialresult = wavePushInterfaceUtils.startUploadService(params1, file,
                                            "http://59.206.96.197:8080/WebDiskServerDemo/upload");
                                    JSONObject jsonObject2 = JSON.parseObject(materialresult);
                                    String msg = jsonObject2.getString("code");
                                    if ("0000".equals(msg)) {
                                        materialJsonObj.put("TYPE", "1");
                                        materialJsonObj.put("FILE_NAME", info.getAttachFileName());
                                        String id = jsonObject2.getString("docid");
                                        materialJsonObj.put("FILE_PATH", jsonObject2.getString("docid"));
                                    } else {
                                        materialJsonObj.put("TYPE", "0");
                                        materialJsonObj.put("FILE_NAME", auditTaskMaterial.getMaterialname());
                                        materialJsonObj.put("FILE_PATH", "");
                                    }
                                    materialArray.add(materialJsonObj);
                                }
                            } else {
                                // 操作人ID
                                materialJsonObj1.put("OPERATOR_ID", "");
                                // 操作人
                                materialJsonObj1.put("OPERATOR_NAME", "统一接件人员");
                                // 材料ID
                                materialJsonObj1.put("DOCUMENT_ID", auditTaskMaterial.getMaterialid());
                                // 材料名称
                                materialJsonObj1.put("DOCUMENT_NAME", auditTaskMaterial.getMaterialname());
                                materialJsonObj1.put("TYPE", "0");
                                materialJsonObj1.put("FILE_NAME", auditTaskMaterial.getMaterialname());
                                materialJsonObj1.put("FILE_PATH", "");
                                materialArray.add(materialJsonObj1);

                            }
                        }
                    }
                    postdata.put("material", materialArray);

                    postdata.put("isMatchMaterial", "1");

                    Date Promiseenddate = auditProject.getPromiseenddate();

                    if (StringUtil.isNotBlank(Promiseenddate)) {

                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(Promiseenddate);
                        calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                        Date warningDate = calendar.getTime();
                        postdata.put("timeLimit",
                                EpointDateUtil.convertDate2String(Promiseenddate, EpointDateUtil.DATE_TIME_FORMAT));
                        postdata.put("warningTime",
                                EpointDateUtil.convertDate2String(warningDate, EpointDateUtil.DATE_TIME_FORMAT));

                    } else {

                        Integer promiseday = auditTask.getPromise_day();

                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_MONTH, promiseday);
                        Date timeLimit = calendar.getTime();

                        Calendar calendar2 = new GregorianCalendar();
                        calendar2.setTime(new Date());
                        calendar2.add(Calendar.DAY_OF_MONTH, promiseday - 1);
                        Date warningDate = calendar2.getTime();

                        postdata.put("timeLimit",
                                EpointDateUtil.convertDate2String(timeLimit, EpointDateUtil.DATE_TIME_FORMAT));
                        postdata.put("warningTime",
                                EpointDateUtil.convertDate2String(warningDate, EpointDateUtil.DATE_TIME_FORMAT));
                    }

                    JSONObject personInfoJson = new JSONObject();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    if (objectType == 1) {
                        AuditRsIndividualBaseinfo info = null;
                        String sqlindivi = "select * from audit_rs_individual_baseinfo where projectguid=?";
                        info = CommonDao.getInstance().find(sqlindivi, AuditRsIndividualBaseinfo.class,
                                auditProject.getRowguid());
                        if (info == null) {
                            String sqlindivibyid = "select * from audit_rs_individual_baseinfo where idnumber=?";
                            info = CommonDao.getInstance().find(sqlindivibyid, AuditRsIndividualBaseinfo.class,
                                    auditProject.getCertnum());
                        }
                        String certType = auditProject.getCerttype();
                        Log.info("推送浪潮certType:" + certType);
                        String tocertType = "";
                        if ("16".equals(certType)) {
                            tocertType = "01";
                        } else if ("14".equals(certType)) {
                            tocertType = "02";
                        } else if ("30".equals(certType)) {
                            tocertType = "03";
                        } else if ("22".equals(certType)) {
                            tocertType = "111";
                        } else if ("26".equals(certType)) {
                            tocertType = "414";
                        } else {
                            tocertType = "01";
                        }
                        if (info != null && info.size() > 0) {

                            // 个人信息参数构造
                            // 证件类型
                            personInfoJson.put("identityType", tocertType);
                            // 证件号
                            personInfoJson.put("idcardNo", info.getIdnumber());
                            // 姓名
                            personInfoJson.put("name", auditProject.getApplyername());
                            // 性别
                            personInfoJson.put("sex", info.getSex());
                            // 民族
                            personInfoJson.put("nation", info.getNation());
                            // 政治面貌
                            personInfoJson.put("politicalStatus", info.get("POLITICALSTATUS"));
                            // 学历
                            personInfoJson.put("education", info.get("EDUCATION"));
                            // 籍贯
                            personInfoJson.put("nativePlace", info.get("NATIVEPLACE"));
                            // 出生日期
                            if (StringUtil.isNotBlank(info.get("BIRTHDAY"))) {
                                personInfoJson.put("birthday", formatter.format(info.get("BIRTHDAY")));
                            } else {
                                personInfoJson.put("birthday", "");
                            }
                            // 国籍
                            personInfoJson.put("country", info.get("COUNTRY"));
                            // 居住地址
                            personInfoJson.put("homeAddress", info.get("HOMEADDRESS"));
                            // 联系电话
                            personInfoJson.put("linkPhone", info.getContactmobile());
                            // 联系地址
                            personInfoJson.put("linkAddress", auditProject.getAddress());
                            // 邮政编码
                            personInfoJson.put("postCode", info.getContactpostcode());
                            // 省
                            personInfoJson.put("province", info.get("PROVINCE"));
                            // 市
                            personInfoJson.put("city", info.get("CITY"));
                            // 县
                            personInfoJson.put("county", info.get("COUNTY"));
                            // 电子邮箱
                            personInfoJson.put("email", info.get("CONTACTEMAIL"));
                        } else {
                            // 个人信息参数构造
                            // 证件类型

                            personInfoJson.put("identityType", tocertType);
                            // 证件号
                            personInfoJson.put("idcardNo", auditProject.getCertnum());
                            // 姓名
                            personInfoJson.put("name", auditProject.getApplyername());
                            // 性别
                            personInfoJson.put("sex", "");
                            // 民族
                            personInfoJson.put("nation", "");
                            // 政治面貌
                            personInfoJson.put("politicalStatus", "");
                            // 学历
                            personInfoJson.put("education", "");
                            // 籍贯
                            personInfoJson.put("nativePlace", "");
                            // 出生日期
                            personInfoJson.put("birthday", "");
                            // 国籍
                            personInfoJson.put("country", "");
                            // 居住地址
                            personInfoJson.put("homeAddress", "");
                            // 联系电话
                            personInfoJson.put("linkPhone", auditProject.getContactmobile());
                            // 联系地址
                            personInfoJson.put("linkAddress", auditProject.getAddress());
                            // 邮政编码
                            personInfoJson.put("postCode", auditProject.getContactpostcode());
                            // 省
                            personInfoJson.put("province", "");
                            // 市
                            personInfoJson.put("city", "");
                            // 县
                            personInfoJson.put("county", "");
                            // 邮箱
                            personInfoJson.put("email", auditProject.getContactemail());
                        }
                        postdata.put("info", personInfoJson);
                    } else {
                        JSONObject orgInfoJson = new JSONObject();
                        String companysql = "select * from Audit_Rs_Company_Baseinfo where creditcode=? ORDER BY VERSION DESC LIMIT 1";
                        AuditRsCompanyBaseinfo company = CommonDao.getInstance().find(companysql,
                                AuditRsCompanyBaseinfo.class, auditProject.getCertnum());
                        if (company != null && company.size() > 0) {
                            orgInfoJson.put("orgName",
                                    StringUtil.isBlank(company.getOrganname()) ? "无" : company.getOrganname());
                            if ("14".equals(auditProject.getCerttype())) {
                                orgInfoJson.put("orgCode", company.getCreditcode());
                            } else {
                                orgInfoJson.put("orgCode", "");
                            }
                            if ("16".equals(auditProject.getCerttype())) {
                                orgInfoJson.put("creditCode", auditProject.getCertnum());
                            } else {
                                orgInfoJson.put("creditCode", "");
                            }
                            orgInfoJson.put("orgCodeAwardDate", "");
                            orgInfoJson.put("orgCodeAwardOrg", "");
                            orgInfoJson.put("orgCodeValidPeriodStart", "");
                            orgInfoJson.put("orgCodeValidPeriodEnd_str", "");
                            orgInfoJson.put("orgCodeValidPeriodEnd", "");
                            orgInfoJson.put("orgEnglishName", "");
                            orgInfoJson.put("orgType",
                                    StringUtil.isNotBlank(company.getOrgantype()) ? company.getOrgantype() : "无");// 必填
                            orgInfoJson.put("enterpriseSortCode", "");
                            orgInfoJson.put("enterpriseSortName", "");
                            orgInfoJson.put("enterpriseTypeCode", "");
                            orgInfoJson.put("enterpriseTypeName", "");
                            orgInfoJson.put("legalPerson", company.getOrganlegal());
                            orgInfoJson.put("legalPersonType", company.get("LEGALPERSONTYPE"));
                            orgInfoJson.put("certificateName",
                                    StringUtil.isNotBlank(company.get("CERTIFICATENAME"))
                                            ? company.get("CERTIFICATENAME")
                                            : "无");
                            orgInfoJson.put("certificateNo", auditProject.getContactcertnum());
                            orgInfoJson.put("responsiblePerson", company.get("responsiblePerson"));
                            orgInfoJson.put("responsiblePersonId", company.get("responsiblePersonId"));
                            orgInfoJson.put("inAreaCode", company.get("inAreaCode"));
                            orgInfoJson.put("inArea", company.get("inArea"));
                            orgInfoJson.put("chargeDepartment", company.get("chargeDepartment"));
                            orgInfoJson.put("registerAddress", auditProject.getAddress());
                            orgInfoJson.put("produceAddress", company.get("produceAddress"));
                            orgInfoJson.put("mailingAddress", company.get("mailingAddress"));
                            orgInfoJson.put("postalCode", company.get("postalCode"));
                            orgInfoJson.put("linkMan", auditProject.getContactperson());
                            orgInfoJson.put("contactPhone", auditProject.getContactmobile());
                            orgInfoJson.put("fax", auditProject.getContactfax());
                            orgInfoJson.put("linkManEmail", auditProject.getContactemail());
                            orgInfoJson.put("bank", company.get("bank"));
                            orgInfoJson.put("bankAccount", "");
                            orgInfoJson.put("organizationKind", company.get("organizationKind"));
                            orgInfoJson.put("employeeNum", company.get("employeeNum"));
                            orgInfoJson.put("registerCapital", "");
                            orgInfoJson.put("currencyKind", "");
                            orgInfoJson.put("groundArea", "");
                            orgInfoJson.put("businessScope",
                                    StringUtil.isNotBlank(company.get("businessScope")) ? company.get("businessScope")
                                            : "无");// 必填
                            orgInfoJson.put("businessScopePart", "");
                            orgInfoJson.put("mainProduct", "");
                            orgInfoJson.put("operatingMode", "");
                            orgInfoJson.put("registerDate", "");// 必填
                            orgInfoJson.put("orgFoundDate", "");
                            orgInfoJson.put("businessLicense", "");
                            orgInfoJson.put("aicAwardDate", "");
                            orgInfoJson.put("aicValidPeriodStart", "");
                            orgInfoJson.put("aicValidPeriodEnd", "");
                            orgInfoJson.put("aicCertAwardOrg", "");
                            orgInfoJson.put("taxManager", "");
                            orgInfoJson.put("nationTaxRegisterNo", "");
                            orgInfoJson.put("nationTaxAwardDate", "");
                            orgInfoJson.put("locationTaxRegisterNo", "");
                            orgInfoJson.put("locationTaxAwardDate", "");
                            orgInfoJson.put("nationInvestRate", "");
                            orgInfoJson.put("corporationInvestRate", "");
                            orgInfoJson.put("foreignInvestRate", "");
                            orgInfoJson.put("naturalManInvestRate", "");
                            orgInfoJson.put("bankLoanRate", "");
                            orgInfoJson.put("remark", company.get("remark"));
                            orgInfoJson.put("HANDLERNAME", company.get("HANDLERNAME"));// 必填
                            orgInfoJson.put("HANDLERPHONE", company.get("HANDLERPHONE"));// 必填
                            orgInfoJson.put("HANDLERIDTYPE", company.get("HANDLERIDTYPE"));// 必填
                            orgInfoJson.put("HANDLERID", company.get("HANDLERID"));// 必填
                            orgInfoJson.put("ORGACTUALITY",
                                    StringUtil.isNotBlank(company.get("ORGACTUALITY")) ? company.get("ORGACTUALITY")
                                            : "无");// 必填
                        } else {
                            orgInfoJson.put("orgName", auditProject.getApplyername());
                            if ("14".equals(auditProject.getCerttype())) {
                                orgInfoJson.put("orgCode", auditProject.getCertnum());
                            } else {
                                orgInfoJson.put("orgCode", "");
                            }
                            if ("16".equals(auditProject.getCerttype())) {
                                orgInfoJson.put("creditCode", auditProject.getCertnum());
                            } else {
                                orgInfoJson.put("creditCode", "");
                            }
                            orgInfoJson.put("orgCodeAwardDate", "");
                            orgInfoJson.put("orgCodeAwardOrg", "");
                            orgInfoJson.put("orgCodeValidPeriodStart", "");
                            orgInfoJson.put("orgCodeValidPeriodEnd_str", "");
                            orgInfoJson.put("orgCodeValidPeriodEnd", "");
                            orgInfoJson.put("orgEnglishName", "");
                            orgInfoJson.put("orgType", "Enterprise");// 必填
                            orgInfoJson.put("enterpriseSortCode", "");
                            orgInfoJson.put("enterpriseSortName", "");
                            orgInfoJson.put("enterpriseTypeCode", "");
                            orgInfoJson.put("enterpriseTypeName", "");
                            orgInfoJson.put("legalPerson", auditProject.getLegal());
                            orgInfoJson.put("legalPersonType", "legal_person");
                            orgInfoJson.put("certificateName", "身份证");
                            orgInfoJson.put("certificateNo", auditProject.getContactcertnum());
                            orgInfoJson.put("responsiblePerson", "");
                            orgInfoJson.put("responsiblePersonId", "");
                            orgInfoJson.put("inAreaCode", "");
                            orgInfoJson.put("inArea", "");
                            orgInfoJson.put("chargeDepartment", "");
                            orgInfoJson.put("registerAddress", auditProject.getAddress());
                            orgInfoJson.put("produceAddress", "");
                            orgInfoJson.put("mailingAddress", "");
                            orgInfoJson.put("postalCode", "");
                            orgInfoJson.put("linkMan", auditProject.getContactperson());
                            orgInfoJson.put("contactPhone", auditProject.getContactmobile());
                            orgInfoJson.put("fax", auditProject.getContactfax());
                            orgInfoJson.put("linkManEmail", auditProject.getContactemail());
                            orgInfoJson.put("bank", "");
                            orgInfoJson.put("bankAccount", "");
                            orgInfoJson.put("organizationKind", "");
                            orgInfoJson.put("employeeNum", "");
                            orgInfoJson.put("registerCapital", "");
                            orgInfoJson.put("currencyKind", "");
                            orgInfoJson.put("groundArea", "");
                            orgInfoJson.put("businessScope", "无");// 必填
                            orgInfoJson.put("businessScopePart", "");
                            orgInfoJson.put("mainProduct", "");
                            orgInfoJson.put("operatingMode", "");
                            orgInfoJson.put("registerDate", "");// 必填
                            orgInfoJson.put("orgFoundDate", "");
                            orgInfoJson.put("businessLicense", "");
                            orgInfoJson.put("aicAwardDate", "");
                            orgInfoJson.put("aicValidPeriodStart", "");
                            orgInfoJson.put("aicValidPeriodEnd", "");
                            orgInfoJson.put("aicCertAwardOrg", "");
                            orgInfoJson.put("taxManager", "");
                            orgInfoJson.put("nationTaxRegisterNo", "");
                            orgInfoJson.put("nationTaxAwardDate", "");
                            orgInfoJson.put("locationTaxRegisterNo", "");
                            orgInfoJson.put("locationTaxAwardDate", "");
                            orgInfoJson.put("nationInvestRate", "");
                            orgInfoJson.put("corporationInvestRate", "");
                            orgInfoJson.put("foreignInvestRate", "");
                            orgInfoJson.put("naturalManInvestRate", "");
                            orgInfoJson.put("bankLoanRate", "");
                            orgInfoJson.put("remark", "");
                            orgInfoJson.put("orgActuality", "Change");// 必填
                            // 项目信息
                            orgInfoJson.put("projectName", auditProject.getStr("xiangmuname"));
                            orgInfoJson.put("projectCode", auditProject.getStr("xiangmubm"));
                            orgInfoJson.put("location", auditProject.getAddress());
                            orgInfoJson.put("linkMan", auditProject.getContactperson());
                            orgInfoJson.put("linkPhone", auditProject.getContactphone());
                            orgInfoJson.put("projectContent", auditProject.getStr("PROJECTCONTENT"));
                            orgInfoJson.put("areaAll", auditProject.getStr("AREAALL"));
                            orgInfoJson.put("areaBuild", auditProject.getStr("AREABUILD"));
                            orgInfoJson.put("investment", auditProject.getStr("INVESTMENT"));
                            orgInfoJson.put("projectAllowedNo", auditProject.getStr("PROJECTALLOWEDNO"));
                        }
                        postdata.put("info", orgInfoJson);
                    }

                    if (auditProject.getProjectname().contains("在设区的市范围内跨区、县进行公路超限运输许可")||auditProject.getProjectname().contains("超限运输车辆行驶公路许可新办")) {
                        String data_object = auditProject.getStr("dataObj_baseinfo");
                        if (StringUtil.isNotBlank(data_object)) {
                            JSONObject otherInfoJson = JSONObject.parseObject(data_object);
                            formInfo.put("qylx", otherInfoJson.getString("qylx"));
                            formInfo.put("hwcd", otherInfoJson.getString("hwcd"));
                            formInfo.put("chzzl", otherInfoJson.getString("chzzl"));
                            formInfo.put("regaddress", otherInfoJson.getString("regaddress"));
                            formInfo.put("xssjksrq", otherInfoJson.getString("xssjksrq"));
                            formInfo.put("hwlxmc", otherInfoJson.getString("hwlxmc"));
                            formInfo.put("gcc", otherInfoJson.getString("gcc"));
                            formInfo.put("gcg", otherInfoJson.getString("gcg"));
                            formInfo.put("qyclsbm", otherInfoJson.getString("qyclsbm"));
                            formInfo.put("gck", otherInfoJson.getString("gck"));
                            formInfo.put("xkzyxqq", otherInfoJson.getString("xkzyxqq"));
                            formInfo.put("cydwmc", otherInfoJson.getString("cydwmc"));
                            formInfo.put("xkzyxqz", otherInfoJson.getString("xkzyxqz"));
                            formInfo.put("gclcpxh", otherInfoJson.getString("gclcpxh"));
                            formInfo.put("jbrxb", otherInfoJson.getString("jbrxb"));
                            formInfo.put("qyck", otherInfoJson.getString("qyck"));
                            formInfo.put("gczs", otherInfoJson.getString("gczs"));
                            formInfo.put("txlx", otherInfoJson.getString("txlx"));
                            formInfo.put("gczx", otherInfoJson.getString("gczx"));
                            formInfo.put("jbrxm", otherInfoJson.getString("jbrxm"));

                            formInfo.put("mdd",
                                    iCodeItemsService.getItemTextByCodeName("地域分类", otherInfoJson.getString("mdd")));
                            formInfo.put("cfd",
                                    iCodeItemsService.getItemTextByCodeName("地域分类", otherInfoJson.getString("cfd")));

                            String tjd = otherInfoJson.getString("tjd").replace("[", "").replace("]", "");
                            String resulttjd = "";
                            if (StringUtil.isNotBlank(tjd)) {
                                if (tjd.contains(",")) {
                                    String[] tjds = tjd.split(",");
                                    for (String jdd : tjds) {
                                        String itemtext = iCodeItemsService.getItemTextByCodeID("1015852",
                                                jdd.replace("\"", ""));
                                        if (StringUtil.isNotBlank(itemtext)) {
                                            resulttjd += itemtext + ',';
                                        }
                                    }
                                } else {
                                    resulttjd = iCodeItemsService.getItemTextByCodeID("1015852", tjd.replace("\"", ""));
                                }

                                if (resulttjd.endsWith(",")) {
                                    resulttjd = resulttjd.replace('\"', '\'').substring(0, resulttjd.length() - 1);
                                } else {
                                    resulttjd = resulttjd.replace('\"', '\'');
                                }
                            }
                            formInfo.put("tjd", resulttjd);
                            formInfo.put("hwzl", otherInfoJson.getString("hwzl"));
                            formInfo.put("zj", otherInfoJson.getString("zj"));
                            formInfo.put("whzdg", otherInfoJson.getString("whzdg"));
                            formInfo.put("qycg", otherInfoJson.getString("qycg"));
                            formInfo.put("zs", otherInfoJson.getString("zs"));
                            formInfo.put("jbrdh", otherInfoJson.getString("jbrdh"));
                            formInfo.put("qyclzbzl", otherInfoJson.getString("qyclzbzl"));
                            formInfo.put("qycc", otherInfoJson.getString("qycc"));
                            formInfo.put("mastercs", otherInfoJson.getString("mastercs"));
                            formInfo.put("gclhp", otherInfoJson.getString("gclhp"));
                            formInfo.put("gcllx", otherInfoJson.getString("gcllx"));
                            formInfo.put("xssjjssj", otherInfoJson.getString("xssjjssj"));
                            formInfo.put("chzdc", otherInfoJson.getString("chzdc"));
                            formInfo.put("zhfb", otherInfoJson.getString("zhfb"));
                            formInfo.put("hwmc", otherInfoJson.getString("hwmc"));
                            formInfo.put("gclts", otherInfoJson.getString("gclts"));
                            formInfo.put("gclzbzl", otherInfoJson.getString("gclzbzl"));
                            formInfo.put("chzdk", otherInfoJson.getString("chzdk"));
                            formInfo.put("chzdg", otherInfoJson.getString("chzdg"));

                            formInfo.put("hwzdk", otherInfoJson.getString("hwzdk"));
                            formInfo.put("gcclsbdm", otherInfoJson.getString("gcclsbdm"));
                            formInfo.put("qyclcpxh", otherInfoJson.getString("qyclcpxh"));
                            formInfo.put("qycczs", otherInfoJson.getString("qycczs"));
                            formInfo.put("lts", otherInfoJson.getString("lts"));
                            formInfo.put("tyshxydm", otherInfoJson.getString("tyshxydm"));
                            formInfo.put("jbrsfzh", otherInfoJson.getString("jbrsfzh"));
                            formInfo.put("masterId", otherInfoJson.getString("masterId"));
                            formInfo.put("qyclhp", otherInfoJson.getString("qyclhp"));
                            formInfo.put("qycllx", otherInfoJson.getString("qycllx"));
                            formInfo.put("qyclts", otherInfoJson.getString("qyclts"));
                            formInfo.put("dlysjyxkzh", otherInfoJson.getString("dlysjyxkzh"));
                        }
                    }
                    // 申请省际普通货物水路运输经营许可事项
                    String task_itme = auditTask.getItem_id();
                    if (StringUtil.isNotBlank(task_itme) && "11370800MB285591843370118009001".equals(task_itme)
                            && StringUtil.isNotBlank(projectguid)) {
                        Record record = iCxBusService.getSlysqykysq(projectguid);
                        List<Record> sons = iCxBusService.getSlysqykysqSon(projectguid);
                        if (record != null && StringUtil.isNotBlank(record)) {
                            formInfo.put("XuKeBianHao", record.getStr("qit"));
                            formInfo.put("NaHeYanHai", record.getStr("neihyh"));
                            formInfo.put("JingYingQuYu", record.getStr("jingyqy"));
                            formInfo.put("ShouJiHaoMa", record.getStr("shoujhm"));
                            formInfo.put("GuDingZiChanWanYuan", record.getStr("gudzc"));
                            formInfo.put("LiuDongZiChanWanYuan", record.getStr("liudzc"));
                            formInfo.put("ZiJinShuEWanYuan", record.getStr("zijse"));
                            formInfo.put("ZhuCeZiJinWanYuan", record.getStr("zhuczj"));
                            formInfo.put("JingYingFangShi", record.getStr("jingyfs"));
                            formInfo.put("JingJiLeiXing", record.getStr("jingjlx"));
                            formInfo.put("KeDuLvYou", record.getStr("kedly"));
                            formInfo.put("ShenQingDanWeiMingChen", record.getStr("shenqdwmc"));
                            formInfo.put("ZhuGuanDanWeiMingChen", record.getStr("zhugdwmc"));
                            formInfo.put("ShenQingDanWeiDiZhi", record.getStr("shenqdwdz"));
                            formInfo.put("JingYingFanWeiZhuYingKeYunHang", record.getStr("jingyfwzykyhx"));
                            formInfo.put("JingYingFanWeiJianYing", record.getStr("jingyfwjy"));
                            formInfo.put("ZiJinLaiYuan", record.getStr("zijly"));
                            formInfo.put("FuJianMingChen", record.getStr("fujmc"));
                            formInfo.put("ShenQingLiYou", record.getStr("shenqly"));
                            formInfo.put("ShenPiJiGuanYiJian", record.getStr("shenpjgyj"));
                            formInfo.put("ChaoBaoDanWei", record.getStr("chaobdw"));
                            formInfo.put("ChuanChangRenShu", record.getStr("chuanzrs"));
                            formInfo.put("LunJiChang", record.getStr("lunjzrs"));
                            formInfo.put("JiaShiYuanRenShu", record.getStr("jiasyrs"));
                            formInfo.put("LunJiYuan", record.getStr("lunjyrs"));
                            formInfo.put("HeJiChuanYuanRenShu", record.getStr("hejcyrs"));
                            formInfo.put("KeHuoLunSouCi", record.getStr("kehlsc"));
                            formInfo.put("KeHuoLunGongLv", record.getStr("kehlgl"));
                            formInfo.put("KeHuoLunZaiZhong", record.getStr("kehlzzd"));
                            formInfo.put("KeHuoLunZongDun", record.getStr("kehlzd"));
                            formInfo.put("KeHuoLunKeWei", record.getStr("kehlkw"));
                            formInfo.put("HuoLunSouCi", record.getStr("huolsc1"));
                            formInfo.put("HuoLunGongLv", record.getStr("huolgl1"));
                            formInfo.put("HuoLunZaiZhong", record.getStr("huolzzd1"));
                            formInfo.put("HuoLunZongDun", record.getStr("huolzd1"));
                            formInfo.put("HuoLunCangRong", record.getStr("huolcr"));
                            formInfo.put("TuoLunSouCi", record.getStr("tuolsc"));
                            formInfo.put("TuoLunGongLv", record.getStr("tuolgl"));
                            formInfo.put("TuoLunZaiZhong", record.getStr("tlzd"));
                            formInfo.put("TuoLunZongDun", record.getStr("tuolzd"));
                            formInfo.put("BoChuanSouCi", record.getStr("bocsc"));
                            formInfo.put("BoChuanZaiZhong", record.getStr("boczzd"));
                            formInfo.put("BoChuanZongDun", record.getStr("boczd"));
                            formInfo.put("BoChuanKeWei", record.getStr("bockw"));
                            formInfo.put("BoChuanCangRong", record.getStr("boccr"));
                            formInfo.put("ChengYaZhouSouCi", record.getStr("chengyzsc"));
                            formInfo.put("ChengYaZhouZaiZhong", record.getStr("chengyzzzd"));
                            formInfo.put("ChengYaZhouZongDun", record.getStr("chengyzzd"));
                            formInfo.put("QiTaChuanSouCi", record.getStr("qitcsc"));
                            formInfo.put("HeJi", record.getStr("zongdhj"));
                            formInfo.put("QiTa", record.getStr("qit2"));
                            formInfo.put("ShenFenZhengHaoMa", record.getStr("sfzhm"));
                            formInfo.put("XingMing", record.getStr("xm"));
                            formInfo.put("RenYuanLeiBie", record.getStr("rylb"));
                            formInfo.put("XingBie", record.getStr("xb"));

                            JSONArray persons = new JSONArray();
                            if (sons != null && sons.size() > 0) {
                                for (Record rec : sons) {
                                    JSONObject object = new JSONObject();
                                    object.put("BuMen", rec.getStr("bm"));
                                    object.put("LianXiDianHua", rec.getStr("lxdh"));
                                    object.put("ZhiWu", rec.getStr("zw"));
                                    object.put("ZhiChen", rec.getStr("zc"));
                                    object.put("XueLi", rec.getStr("xl"));
                                    object.put("SuoXueZhuanYe", rec.getStr("sxzy"));
                                    object.put("HaiChuanShiRenZhengShuLeiBie", rec.getStr("hcsrzslb"));
                                    object.put("ShiRenZhengShuBianHao", rec.getStr("srzsbh"));
                                    object.put("ShiRenZhengShuZhiWu", rec.getStr("srzszw"));
                                    object.put("ShiRenZhengShuDengJi", rec.getStr("srzsdj"));
                                    object.put("FaZhengJiGou", rec.getStr("fzjg"));
                                    object.put("QiTaZhengShu", rec.getStr("qtzs"));
                                    object.put("QiTaZhengShuBianHao", rec.getStr("qtzsbh"));
                                    object.put("HeTongYouXiaoQi", rec.getStr("htyxqend"));
                                    object.put("ZiLi", rec.getStr("zl"));
                                    persons.add(object);
                                }
                            }

                            formInfo.put("ShuiLuRenYuanXinXi", persons);
                        }
                    }

                    // 推送建筑节能技术产品初次认定
                    if (StringUtil.isNotBlank(task_itme) && "1137080000431212413370717666897".equals(task_itme)
                            && StringUtil.isNotBlank(projectguid)) {
                        Record record = iCxBusService.getJzjnjscprdwbxtbdByRowguid(projectguid);
                        if (record != null && StringUtil.isNotBlank(record)) {
                            formInfo.put("ShenQingDanWei", record.getStr("sqdw"));
                            formInfo.put("ShenQingNaRong", record.getStr("sqnr"));
                            Record cplb = iCxBusService.getCodeItemTextByValue(record.getStr("cplb"));
                            if (cplb != null) {
                                formInfo.put("ChanPinLeiBie", cplb.getStr("itemtext"));
                            }
                            Record cpmc = iCxBusService.getCodeItemTextByValue(record.getStr("cpmc"));
                            if (cpmc != null) {
                                formInfo.put("ChanPinMingChen", cpmc.getStr("itemtext"));
                            }
                            Record bzlb = iCxBusService.getCodeItemTextByValue(record.getStr("bzlb"));
                            if (bzlb != null) {
                                formInfo.put("BiaoZhunLeiBie", bzlb.getStr("itemtext"));
                            }
                            Record zxbz = iCxBusService.getCodeItemTextByValue(record.getStr("zxbz"));
                            if (zxbz != null) {
                                formInfo.put("ZhiXingBiaoZhun", zxbz.getStr("itemtext"));
                            }
                            formInfo.put("QiYeBiaoZhunChanPinMing", record.getStr("qybzcpm"));
                            formInfo.put("QiYeBiaoZhunBianHao", record.getStr("qybzbh"));
                            formInfo.put("JianCeJiGouMingChen", record.getStr("jcjgmc"));
                            formInfo.put("XingShiJianCeBaoGaoBianHao", record.getStr("xsjcbgbh"));
                            formInfo.put("QiYeZhuCeDiZhi", record.getStr("qyzcdz"));
                            formInfo.put("QiYeShengChanDiZhi", record.getStr("qyscdz"));
                            formInfo.put("QiYeJiGouDaiMa", record.getStr("qyjgdm"));
                            formInfo.put("QiYeTongXunDiZhi", record.getStr("qytxdz"));
                            formInfo.put("FaRen", record.getStr("fr"));
                            formInfo.put("FaRenShouJi", record.getStr("frsj"));
                            formInfo.put("LianXiRen", record.getStr("lxr"));
                            formInfo.put("LianXiRenShouJi", record.getStr("lxrsj"));
                            formInfo.put("ZhuCeZiBenJin", record.getStr("zczbj"));
                            formInfo.put("ZuoJi", record.getStr("zj"));
                            formInfo.put("ZongTouZi", record.getStr("ztz"));
                            formInfo.put("YouBian", record.getStr("yb"));
                            formInfo.put("NianShengChanNenLi", record.getStr("nscnl"));
                            formInfo.put("YouXiang", record.getStr("yx"));
                            formInfo.put("ShenQingChengNuoZhi", record.getStr("sqcn"));

                            url = "http://59.206.96.200:7090/web/approval/otherSystemAccpet";
                        }
                    }

                    postdata.put("formInfo", formInfo);

                    Map<String, Object> acceptJsonRequest = new HashMap<String, Object>();
                    acceptJsonRequest.put("postdata", postdata);

                    try {
                        JSONObject result = HttplcUtils.postHttp(url, acceptJsonRequest);

                        if (StringUtil.isNotBlank(result)) {
                            String state = result.getString("state");
                            if ("200".equals(state)) {
                                log.info("=======结束调用otherSystemAccpet接口=======");
                                LcprojectError error = new LcprojectError();
                                error.setRowguid(UUID.randomUUID().toString());
                                error.setApplyername(auditProject.getApplyername());
                                error.setProjectguid(auditProject.getRowguid());
                                error.setAccepttime(auditProject.getAcceptuserdate());
                                error.setOperatedate(new Date());
                                error.setAreacode(auditProject.getAreacode());
                                error.setFlowsn(auditProject.getFlowsn());
                                error.set("remark", "推送成功");
                                error.set("record", params);
                                error.setStatus("1");
                                error.set("type", "otherSystemAccpet");
                                iLcprojectErrorService.insert(error);
                                return JsonUtils.zwdtRestReturn("1", "推送业务基本信息成功", dataJson.toString());
                            } else {
                                LcprojectError error = new LcprojectError();
                                error.setRowguid(UUID.randomUUID().toString());
                                error.setApplyername(auditProject.getApplyername());
                                error.setProjectguid(auditProject.getRowguid());
                                error.setAccepttime(auditProject.getAcceptuserdate());
                                error.setOperatedate(new Date());
                                error.setAreacode(auditProject.getAreacode());
                                error.setFlowsn(auditProject.getFlowsn());
                                error.set("remark", result.getString("error"));
                                error.set("record", params);
                                error.setStatus("0");
                                error.set("type", "otherSystemAccpet");
                                iLcprojectErrorService.insert(error);
                                return JsonUtils.zwdtRestReturn("0", "推送业务基本信息失败", dataJson.toString());
                            }
                        } else {
                            LcprojectError error = new LcprojectError();
                            error.setRowguid(UUID.randomUUID().toString());
                            error.setApplyername(auditProject.getApplyername());
                            error.setProjectguid(auditProject.getRowguid());
                            error.setAccepttime(auditProject.getAcceptuserdate());
                            error.setOperatedate(new Date());
                            error.setAreacode(auditProject.getAreacode());
                            error.setFlowsn(auditProject.getFlowsn());
                            error.set("remark", "推送业务基本信息接口失败");
                            error.set("record", params);
                            error.setStatus("0");
                            error.set("type", "otherSystemAccpet");
                            iLcprojectErrorService.insert(error);
                            return JsonUtils.zwdtRestReturn("0", "推送业务基本信息失败", "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LcprojectError error = new LcprojectError();
                        error.setRowguid(UUID.randomUUID().toString());
                        error.setApplyername(auditProject.getApplyername());
                        error.setProjectguid(auditProject.getRowguid());
                        error.setAccepttime(auditProject.getAcceptuserdate());
                        error.setOperatedate(new Date());
                        error.setAreacode(auditProject.getAreacode());
                        error.setFlowsn(auditProject.getFlowsn());
                        error.set("remark", "接口响应超时");
                        error.set("record", params);
                        error.setStatus("0");
                        error.set("type", "otherSystemAccpet");
                        iLcprojectErrorService.insert(error);
                        return JsonUtils.zwdtRestReturn("0", "推送业务基本信息失败", "");
                    }

                } else {
                    LcprojectError error = new LcprojectError();
                    error.setRowguid(UUID.randomUUID().toString());
                    error.setApplyername(auditProject.getApplyername());
                    error.setProjectguid(auditProject.getRowguid());
                    error.setAccepttime(auditProject.getAcceptuserdate());
                    error.setOperatedate(new Date());
                    error.setAreacode(auditProject.getAreacode());
                    error.setFlowsn(auditProject.getFlowsn());
                    error.set("record", params);
                    error.set("remark", "事项不存在");
                    error.setStatus("0");
                    error.set("type", "otherSystemAccpet");
                    iLcprojectErrorService.insert(error);
                    return JsonUtils.zwdtRestReturn("0", "事项不存在！", "");
                }

            } else {
                LcprojectError error = new LcprojectError();
                error.setRowguid(UUID.randomUUID().toString());
                error.setProjectguid(projectguid);
                error.setOperatedate(new Date());
                error.set("record", params);
                error.set("remark", "办件不存在");
                error.setStatus("0");
                error.set("type", "otherSystemAccpet");
                iLcprojectErrorService.insert(error);
                return JsonUtils.zwdtRestReturn("0", "办件不存在！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======otherSystemAccpet接口参数：params【" + params + "】=======");
            log.info("=======otherSystemAccpet异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "推送业务基本信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件表的列表清单
     *
     * @param params
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/otherSystemAddCourse", method = RequestMethod.POST)
    public String otherSystemAddCourse(@RequestBody String params, HttpServletRequest request) throws IOException {
        JSONObject json = JSON.parseObject(params);
        try {
            String areacode = json.getString("areaCode");
            String projectGuid = json.getString("projectGuid");
            String acceptusername = json.getString("acceptusername");

            if (StringUtil.isBlank(acceptusername)) {
                acceptusername = "无";
            }

            // 办件标识 正常01 挂起 02 取消挂起03 准予许可04 作废05
            String status = json.getString("status");
            String url = lc_url + "/web/approval/otherSystemAddCourse";

            String fields = " * ";
            AuditProject auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectGuid, areacode).getResult();

            JSONObject postdata = new JSONObject();

            if (auditProject != null) {
                AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                if (auditTask != null) {
                    if (StringUtil.isBlank(auditTask.getStr("INNER_CODE"))) {
                        return JsonUtils.zwdtRestReturn("0", "事项INNER_CODE未配置！", "");
                    }

                    //公共服务类的事项是9090，其他权力类的事项是7090
                    if ("11".equals(auditTask.getShenpilb())) {
                        url = "http://59.206.96.200:9090/web/approval/otherSystemAddCourse";
                    } else if ("10".equals(auditTask.getShenpilb()) && "07".equals(auditTask.getShenpilb())) {
                        url = "http://59.206.96.200:7090/web/approval/otherSystemAddCourse";
                    }

                    postdata.put("bussReceiveNum", auditProject.getFlowsn());
                    Date Promiseenddate = auditProject.getPromiseenddate();
                    if ("1".equals(auditTask.getType())) {
                        Promiseenddate = auditProject.getAcceptuserdate();
                    }

                    if (StringUtil.isBlank(Promiseenddate)) {
                        Promiseenddate = new Date();
                    }

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(Promiseenddate);
                    calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                    Date warningDate = calendar.getTime();

                    postdata.put("bussLimitTime",
                            EpointDateUtil.convertDate2String(Promiseenddate, EpointDateUtil.DATE_TIME_FORMAT));
                    postdata.put("bussWarningTime",
                            EpointDateUtil.convertDate2String(warningDate, EpointDateUtil.DATE_TIME_FORMAT));
                    postdata.put("isMatchFlow", "0");
                    postdata.put("isMatchPrevNode", "0");
                    postdata.put("isMatchUser", "0");
                    postdata.put("isMatchMaterial", "0");
                    // 办件受理后进行此操作

                    Date Acceptuserdate = auditProject.getAcceptuserdate();
                    if (StringUtil.isBlank(Acceptuserdate)) {
                        Date userdate = new Date();
                        Calendar calendar2 = new GregorianCalendar();
                        calendar2.setTime(userdate);
                        calendar2.add(Calendar.MINUTE, -1); // 把日期向前调整一天
                        Acceptuserdate = calendar2.getTime();

                    }

                    Date Banjiedate = auditProject.getBanjiedate();
                    if (StringUtil.isBlank(Banjiedate)) {
                        Banjiedate = new Date();
                    }

                    String Acceptusername = auditProject.getAcceptusername();
                    if (StringUtil.isBlank(Acceptusername)) {
                        Acceptusername = acceptusername;
                    }

                    String Banjieusername = auditProject.getBanjieusername();
                    if (StringUtil.isBlank(Banjieusername)) {
                        Banjieusername = acceptusername;
                    }

                    if ("01".equals(status)) {
                        postdata.put("prevNodeName", "");
                        postdata.put("prevNodeSendTime", "");
                        postdata.put("prevNodeOpinion", "");
                        postdata.put("prevNodeStatus", "");
                        postdata.put("currentNodeName", "正常");
                        postdata.put("currentNodeType", "2");
                        postdata.put("currentNodeReceiveTime",
                                EpointDateUtil.convertDate2String(Acceptuserdate, EpointDateUtil.DATE_TIME_FORMAT));
                        postdata.put("currentNodeLimitTime",
                                EpointDateUtil.convertDate2String(Promiseenddate, EpointDateUtil.DATE_TIME_FORMAT));
                        postdata.put("currentNodeWarningTime",
                                EpointDateUtil.convertDate2String(warningDate, EpointDateUtil.DATE_TIME_FORMAT));
                        postdata.put("currentNodeUserName", Acceptusername);
                        postdata.put("currentNodeStatus", "01");
                    } else if ("02".equals(status)) {
                        postdata.put("prevNodeName", "正常");
                        postdata.put("prevNodeSendTime",
                                EpointDateUtil.convertDate2String(Acceptuserdate, EpointDateUtil.DATE_TIME_FORMAT));
                        postdata.put("prevNodeOpinion", "办件受理");
                        postdata.put("prevNodeStatus", "23");
                        postdata.put("currentNodeName", "挂起");
                        postdata.put("currentNodeType", "1");
                        List<AuditProjectUnusual> unusuals = auditProjectUnusual
                                .getProjectUnusualByProjectGuid(projectGuid).getResult();
                        for (AuditProjectUnusual unusal : unusuals) {
                            if ("10".equals(unusal.getOperatetype())) {
                                postdata.put("currentNodeReceiveTime", EpointDateUtil
                                        .convertDate2String(unusal.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
                                postdata.put("currentNodeLimitTime", EpointDateUtil.convertDate2String(Promiseenddate,
                                        EpointDateUtil.DATE_TIME_FORMAT));
                                postdata.put("currentNodeWarningTime", EpointDateUtil.convertDate2String(warningDate,
                                        EpointDateUtil.DATE_TIME_FORMAT));
                                postdata.put("currentNodeUserName", unusal.getOperateusername());
                                postdata.put("currentNodeStatus", "01");
                                continue;
                            }
                        }

                    } else if ("03".equals(status)) {
                        postdata.put("prevNodeName", "挂起");
                        postdata.put("prevNodeOpinion", "办件挂起");
                        postdata.put("prevNodeStatus", "01");
                        postdata.put("currentNodeName", "正常");
                        postdata.put("currentNodeType", "1");
                        List<AuditProjectUnusual> unusuals = auditProjectUnusual
                                .getProjectUnusualByProjectGuid(projectGuid).getResult();
                        for (AuditProjectUnusual unusal : unusuals) {
                            if ("11".equals(unusal.getOperatetype())) {
                                postdata.put("currentNodeReceiveTime", EpointDateUtil
                                        .convertDate2String(unusal.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
                                postdata.put("currentNodeLimitTime", EpointDateUtil.convertDate2String(Promiseenddate,
                                        EpointDateUtil.DATE_TIME_FORMAT));
                                postdata.put("currentNodeWarningTime", EpointDateUtil.convertDate2String(warningDate,
                                        EpointDateUtil.DATE_TIME_FORMAT));
                                postdata.put("currentNodeUserName", unusal.getOperateusername());
                                postdata.put("currentNodeStatus", "23");
                            } else if ("10".equals(unusal.getOperatetype())) {
                                postdata.put("prevNodeSendTime", EpointDateUtil
                                        .convertDate2String(unusal.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));

                            }
                        }

                    } else if ("04".equals(status)) {
                        postdata.put("prevNodeName", "正常");
                        postdata.put("prevNodeOpinion", "办件受理");
                        postdata.put("prevNodeStatus", "99");
                        postdata.put("currentNodeName", "");
                        postdata.put("currentNodeType", "");
                        List<AuditProjectUnusual> unusuals = auditProjectUnusual
                                .getProjectUnusualByProjectGuid(projectGuid).getResult();
                        if (unusuals != null && unusuals.size() > 0) {
                            for (AuditProjectUnusual unusal : unusuals) {
                                if ("11".equals(unusal.getOperatetype())) {
                                    postdata.put("prevNodeSendTime", EpointDateUtil.convertDate2String(
                                            unusal.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
                                    postdata.put("currentNodeReceiveTime", EpointDateUtil.convertDate2String(Banjiedate,
                                            EpointDateUtil.DATE_TIME_FORMAT));
                                    postdata.put("currentNodeLimitTime", EpointDateUtil
                                            .convertDate2String(Promiseenddate, EpointDateUtil.DATE_TIME_FORMAT));
                                    postdata.put("currentNodeWarningTime", EpointDateUtil
                                            .convertDate2String(warningDate, EpointDateUtil.DATE_TIME_FORMAT));
                                    postdata.put("currentNodeUserName", auditProject.getBanjieusername());
                                    postdata.put("currentNodeStatus", "");
                                }
                            }
                        } else {
                            postdata.put("prevNodeSendTime",
                                    EpointDateUtil.convertDate2String(Acceptuserdate, EpointDateUtil.DATE_TIME_FORMAT));
                            postdata.put("currentNodeReceiveTime",
                                    EpointDateUtil.convertDate2String(Banjiedate, EpointDateUtil.DATE_TIME_FORMAT));
                            postdata.put("currentNodeLimitTime",
                                    EpointDateUtil.convertDate2String(Promiseenddate, EpointDateUtil.DATE_TIME_FORMAT));
                            postdata.put("currentNodeWarningTime",
                                    EpointDateUtil.convertDate2String(warningDate, EpointDateUtil.DATE_TIME_FORMAT));
                            postdata.put("currentNodeUserName", Banjieusername);
                            postdata.put("currentNodeStatus", "");
                        }
                    }

                    Map<String, Object> acceptJsonRequest = new HashMap<String, Object>();
                    acceptJsonRequest.put("postdata", postdata);
                    try {
                        JSONObject result = HttplcUtils.postHttp(url, acceptJsonRequest);
                        if ("200".equals(result.getString("state"))) {
                            LcprojectError error = new LcprojectError();
                            error.setRowguid(UUID.randomUUID().toString());
                            error.setApplyername(auditProject.getApplyername());
                            error.setProjectguid(auditProject.getRowguid());
                            error.setAccepttime(auditProject.getAcceptuserdate());
                            error.setOperatedate(new Date());
                            error.setAreacode(auditProject.getAreacode());
                            error.setFlowsn(auditProject.getFlowsn());
                            error.set("remark", "推送成功");
                            error.set("record", params);
                            error.setStatus("1");
                            error.set("type", "otherSystemAddCourse");
                            iLcprojectErrorService.insert(error);
                            return JsonUtils.zwdtRestReturn("1", "推送流程成功！", result.getString("error"));
                        } else {
                            LcprojectError error = new LcprojectError();
                            error.setRowguid(UUID.randomUUID().toString());
                            error.setApplyername(auditProject.getApplyername());
                            error.setProjectguid(auditProject.getRowguid());
                            error.setAccepttime(auditProject.getAcceptuserdate());
                            error.setOperatedate(new Date());
                            error.setAreacode(auditProject.getAreacode());
                            error.setFlowsn(auditProject.getFlowsn());
                            error.setStatus("0");
                            error.set("record", params);
                            error.set("remark", result.getString("error"));
                            error.set("type", "otherSystemAddCourse");
                            iLcprojectErrorService.insert(error);
                            return JsonUtils.zwdtRestReturn("0", "推送流程失败！", "");
                        }
                    } catch (Exception e) {
                        LcprojectError error = new LcprojectError();
                        error.setRowguid(UUID.randomUUID().toString());
                        error.setApplyername(auditProject.getApplyername());
                        error.setProjectguid(auditProject.getRowguid());
                        error.setAccepttime(auditProject.getAcceptuserdate());
                        error.setOperatedate(new Date());
                        error.setAreacode(auditProject.getAreacode());
                        error.setFlowsn(auditProject.getFlowsn());
                        error.set("remark", "接口响应超时");
                        error.setStatus("0");
                        error.set("record", params);
                        error.set("type", "otherSystemAddCourse");
                        iLcprojectErrorService.insert(error);
                        e.printStackTrace();
                        return JsonUtils.zwdtRestReturn("0", "出现异常：", "");
                    }

                } else {
                    LcprojectError error = new LcprojectError();
                    error.setRowguid(UUID.randomUUID().toString());
                    error.setApplyername(auditProject.getApplyername());
                    error.setProjectguid(auditProject.getRowguid());
                    error.setAccepttime(auditProject.getAcceptuserdate());
                    error.setOperatedate(new Date());
                    error.setAreacode(auditProject.getAreacode());
                    error.setFlowsn(auditProject.getFlowsn());
                    error.set("remark", "事项不存在");
                    error.setStatus("0");
                    error.set("record", params);
                    error.set("type", "otherSystemAddCourse");
                    iLcprojectErrorService.insert(error);
                    return JsonUtils.zwdtRestReturn("0", "出现异常：", "");
                }
            } else {
                LcprojectError error = new LcprojectError();
                error.setRowguid(UUID.randomUUID().toString());
                error.setProjectguid(projectGuid);
                error.setOperatedate(new Date());
                error.set("remark", "办件不存在");
                error.setStatus("0");
                error.set("record", params);
                error.set("type", "otherSystemAddCourse");
                iLcprojectErrorService.insert(error);
                return JsonUtils.zwdtRestReturn("0", "出现异常：", "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    public static void main(String[] args) {
        // 申请省际普通货物水路运输经营许可事项
        JSONObject formInfo = new JSONObject();
        CxBusService iCxBusService = new CxBusService();
        Record record = iCxBusService.getSlysqykysq("3bd5b470-b348-450d-9f6f-7dc92f00514d");
        if (record != null) {
            formInfo.put("XuKeBianHao", record.getStr("qit"));
            formInfo.put("NaHeYanHai", record.getStr("neihyh"));
            formInfo.put("JingYingQuYu", record.getStr("jingyqy"));
            formInfo.put("ShouJiHaoMa", record.getStr("shoujhm"));
            formInfo.put("GuDingZiChanWanYuan", record.getStr("gudzc"));
            formInfo.put("LiuDongZiChanWanYuan", record.getStr("liudzc"));
            formInfo.put("ZiJinShuEWanYuan", record.getStr("zijse"));
            formInfo.put("ZhuCeZiJinWanYuan", record.getStr("zhuczj"));
            formInfo.put("JingYingFangShi", record.getStr("jingyfs"));
            formInfo.put("JingJiLeiXing", record.getStr("jingjlx"));
            formInfo.put("KeDuLvYou", record.getStr("kedly"));
            formInfo.put("ShenQingDanWeiMingChen", record.getStr("shenqdwmc"));
            formInfo.put("ZhuGuanDanWeiMingChen", record.getStr("zhugdwmc"));
            formInfo.put("ShenQingDanWeiDiZhi", record.getStr("shenqdwdz"));
            formInfo.put("JingYingFanWeiZhuYingKeYunHang", record.getStr("jingyfwzykyhx"));
            formInfo.put("JingYingFanWeiJianYing", record.getStr("jingyfwjy"));
            formInfo.put("ZiJinLaiYuan", record.getStr("zijly"));
            formInfo.put("FuJianMingChen", record.getStr("fujmc"));
            formInfo.put("ShenQingLiYou", record.getStr("shenqly"));
            formInfo.put("ShenPiJiGuanYiJian", record.getStr("shenpjgyj"));
            formInfo.put("ChaoBaoDanWei", record.getStr("chaobdw"));
            formInfo.put("ChuanChangRenShu", record.getStr("chuanzrs"));
            formInfo.put("LunJiChang", record.getStr("lunjzrs"));
            formInfo.put("JiaShiYuanRenShu", record.getStr("jiasyrs"));
            formInfo.put("LunJiYuan", record.getStr("lunjyrs"));
            formInfo.put("HeJiChuanYuanRenShu", record.getStr("hejcyrs"));
            formInfo.put("KeHuoLunSouCi", record.getStr("kehlsc"));
            formInfo.put("KeHuoLunGongLv", record.getStr("kehlgl"));
            formInfo.put("KeHuoLunZaiZhong", record.getStr("kehlzzd"));
            formInfo.put("KeHuoLunZongDun", record.getStr("kehlzd"));
            formInfo.put("KeHuoLunKeWei", record.getStr("kehlkw"));
            formInfo.put("HuoLunSouCi", record.getStr("huolsc1"));
            formInfo.put("HuoLunGongLv", record.getStr("huolgl1"));
            formInfo.put("HuoLunZaiZhong", record.getStr("huolzzd1"));
            formInfo.put("HuoLunZongDun", record.getStr("huolzd1"));
            formInfo.put("HuoLunCangRong", record.getStr("huolcr"));
            formInfo.put("TuoLunSouCi", record.getStr("tuolsc"));
            formInfo.put("TuoLunGongLv", record.getStr("tuolgl"));
            formInfo.put("TuoLunZaiZhong", record.getStr("tlzd"));
            formInfo.put("TuoLunZongDun", record.getStr("tuolzd"));
            formInfo.put("BoChuanSouCi", record.getStr("bocsc"));
            formInfo.put("BoChuanZaiZhong", record.getStr("boczzd"));
            formInfo.put("BoChuanZongDun", record.getStr("boczd"));
            formInfo.put("BoChuanKeWei", record.getStr("bockw"));
            formInfo.put("BoChuanCangRong", record.getStr("boccr"));
            formInfo.put("ChengYaZhouSouCi", record.getStr("chengyzsc"));
            formInfo.put("ChengYaZhouZaiZhong", record.getStr("chengyzzzd"));
            formInfo.put("ChengYaZhouZongDun", record.getStr("chengyzzd"));
            formInfo.put("QiTaChuanSouCi", record.getStr("qitcsc"));
            formInfo.put("HeJi", record.getStr("zongdhj"));
            formInfo.put("QiTa", record.getStr("qit2"));
            formInfo.put("ShenFenZhengHaoMa", record.getStr("sfzhm"));
            formInfo.put("XingMing", record.getStr("xm"));
            formInfo.put("RenYuanLeiBie", record.getStr("rylb"));
            formInfo.put("XingBie", record.getStr("xb"));
            formInfo.put("BuMen", record.getStr("bm"));
            formInfo.put("LianXiDianHua", record.getStr("lxdh"));
            formInfo.put("ZhiWu", record.getStr("zw"));
            formInfo.put("ZhiChen", record.getStr("zc"));
            formInfo.put("XueLi", record.getStr("xl"));
            formInfo.put("SuoXueZhuanYe", record.getStr("sxzy"));
            formInfo.put("HaiChuanShiRenZhengShuLeiBie", record.getStr("hcsrzslb"));
            formInfo.put("ShiRenZhengShuBianHao", record.getStr("srzsbh"));
            formInfo.put("ShiRenZhengShuZhiWu", record.getStr("srzszw"));
            formInfo.put("ShiRenZhengShuDengJi", record.getStr("srzsdj"));
            formInfo.put("FaZhengJiGou", record.getStr("fzjg"));
            formInfo.put("QiTaZhengShu", record.getStr("qtzs"));
            formInfo.put("QiTaZhengShuBianHao", record.getStr("qtzsbh"));
            formInfo.put("HeTongYouXiaoQi", record.getStr("htyxqend"));
            formInfo.put("ZiLi", record.getStr("zl"));
            // //system.out.println(formInfo);

        }
    }

}
