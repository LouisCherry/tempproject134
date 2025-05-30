package com.epoint.departdocking.provinceselfbuild.rest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.container.ContainerFactory;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.departdocking.provinceselfbuild.api.IAccessTokenService;
import com.epoint.departdocking.provinceselfbuild.api.IProviceSelfBuild;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.specialprogram.specialapply.api.ISpecialApplyService;
import com.epoint.specialprogram.specialapply.api.entity.SpecialApply;
import com.epoint.specialprogram.specialresult.api.ISpecialResultService;
import com.epoint.specialprogram.specialresult.api.entity.SpecialResult;
import com.epoint.xmz.auditspyyyzmaterial.api.IAuditSpYyyzMaterialService;
import com.epoint.xmz.xmrsjbuzheng.api.IXmRsjBuzhengService;
import com.epoint.xmz.xmrsjbuzheng.api.entity.XmRsjBuzheng;
import com.epoint.xmz.xmycslspinfo.api.IXmYcslSpinfoService;
import com.epoint.xmz.xmycslspinfo.api.entity.XmYcslSpinfo;
import com.epoint.xmz.xmyyyzbjinfo.api.IXmYyyzBjinfoService;
import com.epoint.xmz.xmyyyzbjinfo.api.entity.XmYyyzBjinfo;
import com.epoint.zwdt.util.TARequestUtil;
import com.epoint.zwdt.zwdtrest.yyyz.api.IDaTing;

/**
 * 省自建系统对接相关接口
 * @author shibin
 * @date  2020年7月17日 下午1:36:09
 */
@RestController
@RequestMapping("/jnrshttpapi")
public class ProviceSelfBuildController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    
    private static String TOKENURL = ConfigUtil.getConfigValue("datasyncjdbc", "rstokendturl");
    private static String APPLYURL = ConfigUtil.getConfigValue("datasyncjdbc", "rsapplyurl");
    private static String API = ConfigUtil.getConfigValue("datasyncjdbc", "rsapi");
    private static String KEY = ConfigUtil.getConfigValue("datasyncjdbc", "rskey");
    private static String SECRET = ConfigUtil.getConfigValue("datasyncjdbc", "rssecret");
    
    
    /**
     * 系统参数API
     */
    @Autowired
    private IConfigService iConfigService;


    @Autowired
    private IDaTing daTingService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension auditTaskExtension;
    
    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditProject auditProjectService;   

    @Autowired
    private IProviceSelfBuild proviceSelfBuildService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IAccessTokenService tokenService;

    @Autowired
    private IXmYyyzBjinfoService xmYyyzBjinfoService;
    
    @Autowired
    private IXmYcslSpinfoService xmYcslSpinfoService;
    
    @Autowired
    private IAuditSpYyyzMaterialService yyyzMaterialService;
    
    @Autowired
    private IConfigService configServicce;
    // ----------分隔符
    @Autowired
    private IXmRsjBuzhengService xmRsjBuzhengService;
    
    @Autowired
    private IAuditProjectMaterial auditProjectMaterialService;

    @Autowired
    private IAuditProjectOperation auditProjectOperationService;
    
    @Autowired
    private IAuditProjectUnusual auditProjectUnusualService;
    
    @Autowired
    private IAuditOnlineProject auditOnlineProjectService;
    @Autowired
    private ISpecialApplyService applyService;
    @Autowired
    private ISpecialResultService resultService;
    @Autowired
    private IConfigService configService;

    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;
    
    /**
     * 获取受理平台接口令牌，由我方分配给他们appKey,appSecret使用
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/token/getXmlToken", method = {RequestMethod.POST })
    public String getXmlToken(@RequestBody String params) {

        log.info("==========开始调用接口getXmlToken=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            log.info("==========params==========" + params);
            JSONObject paramObj = JSONObject.parseObject(params);
            String appKey = paramObj.getString("key");
            String appSecret = paramObj.getString("secret");
            if (StringUtil.isNotBlank(appKey) && StringUtil.isNotBlank(appSecret)) {
                String accessToken = tokenService.getToken(appKey, appSecret);
                StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                xml.append("<RESULT>");
                xml.append("<STATUS>200</STATUS>");
                xml.append("<DESC>请求成功！</DESC>");
                xml.append("<TIME>" + new Date() + "</TIME>");
                xml.append("<DATA>" + accessToken + "</DATA>");
                xml.append("<TOKEN>" + accessToken + "</TOKEN>");
                xml.append("</RESULT>");
                String xmlStr = xml.toString();

                log.info("==========结束调用接口getXmlToken=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return xmlStr;
            }
            else {
                log.info("==========结束调用接口getXmlToken=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return generateXmlReturn("300", "参数为空");
            }
            
        }
        catch (Exception e) {
            log.info(e.getMessage());
            log.info("==========结束调用接口getXmlToken=========="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            return generateXmlReturn("300", "调用接口异常");
        }
    }

    
    /**
     * 推送申报流水号
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/sendApplyNO", method = {RequestMethod.POST })
    public String sendApplyNO(@RequestBody String params) {
        log.info("==========开始调用接口sendApplyNO=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            String accessToken = getAccessToken();
            //String accessToken = "1FD93907A7D007C90C614BD0FAF81062";
            log.info("==========params==========" + params);
            JSONObject paramObj = JSONObject.parseObject(params);
            String flowsn = paramObj.getString("flowsn");
            if (StringUtil.isNotBlank(flowsn)) {
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if (StringUtil.isNotBlank(auditProject)) {
                    AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                            .getResult();
                    String areacode = auditTask.getAreacode();
                    String newItemCode = auditTask.getStr("INNER_CODE");
                    //String url = extension.getStr("selfBuildSendApplyNOUrl");
                    String exchangeno="";
                    if(StringUtil.isNotBlank(auditTask.getStr("scxtcode"))) {
                        exchangeno =auditTask.getStr("scxtcode");
                    }
                    StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
                    xml.append("<APPROVEDATAINFO>");
                    xml.append("<SBLSH_SHORT>" + flowsn + "</SBLSH_SHORT>");
                    xml.append("<SXBM>" + newItemCode + "</SXBM>");
                    xml.append("<XZQHDM>" + areacode + "</XZQHDM>");
                    xml.append("<EXCHANGENO>"+exchangeno+"</EXCHANGENO>");
                    xml.append("<EXPRESSTYPE>3</EXPRESSTYPE>");//0:业务受理； 1：补齐补正； 2：即办件；3：收件后待受理业务；9:审批退回后修改再次提交；
                    xml.append("</APPROVEDATAINFO>");
                    String xmlStr = xml.toString();
                    String result = sendPostRequest(accessToken,xmlStr);
                    log.info("==========结束调用接口sendApplyNO=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return JsonUtils.zwdtRestReturn("1", "接口调用成功", result);
                }
                else {
                    log.info("==========结束调用接口sendApplyNO=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return JsonUtils.zwdtRestReturn("0", "未查询到相应的办件", "");
                }
            }
            else {
                log.info("==========结束调用接口sendApplyNO=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("0", "参数flowsn为空", "");
            }
            
        }
        catch (Exception e) {
            log.info(e.getMessage());
            log.info("==========结束调用接口sendApplyNO=========="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            return JsonUtils.zwdtRestReturn("0", "接口调用异常", "");
        }
    }

    /**
     * 根据申报编号从市级政务服务平台获取申报信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/getApplyInfoSsdj", method = {RequestMethod.POST })
    public String getApplyInfoSsdj(HttpServletRequest  params) {

        log.info("==========开始调用getApplyInfoSsdj接口=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            log.info("========params========" + params);
            String accessToken = params.getParameter("accessToken");
            String xmlstrold = params.getParameter("xmlStr");
            String xmlStr = "";
            if (xmlstrold.contains("version")) {
            	xmlStr = xmlstrold.substring(38);
            }else {
            	return generateXmlReturn("300", "请求入参的XmlStr不正确");
            }
            
            String sysAccessToken = configServicce.getFrameConfigValue("rsjAccessToken");
            //暂时先不校验accessToken
            if (StringUtil.isBlank(accessToken)) {
                return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
            }
            if (!accessToken.equals(sysAccessToken)) {
                return generateXmlReturn("300", "accessToken参数不正确");
            }
            if (StringUtil.isBlank(xmlStr)) {
                return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr！");
            }

            Document document = DocumentHelper.parseText(xmlStr);
            Element rootElement = document.getRootElement();
            String flowsn = rootElement.elementText("SBLSH_SHORT"); // 申办流水号
            String newItemCode = rootElement.elementText("SXBM"); // 事项编码

            if (StringUtil.isNotBlank(flowsn)) {
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if (StringUtil.isNotBlank(auditProject)) {
                    String taskId = auditProject.getTask_id();
                    AuditTask auditTask = auditTaskService.getUseTaskAndExtByTaskid(taskId).getResult();
                    //事项版本编码
                    String itemCode = auditTask.getStr("INNER_CODE");
                    if(!itemCode.equals(newItemCode)) {
                        return generateXmlReturn("300", "该事项编码与根据申报流水号查询到的事项编码不一致！");
                    }
                    //事项表单编码
                    String sxbdbm = "";
                    String sxbbbm =auditTask.getRowguid();
                    String taskname = auditTask.getTaskname();//事项名称
                    String sqrlx = ""; //申请人类型
                    String sqrmc = auditProject.getApplyername(); //申请人姓名
                    String certtype = ""; //申请人证照类型
                    String certnum = auditProject.getCertnum();//申请人证照号码
                    String lxrxm = auditProject.getApplyername();//联系人姓名
                    String lxrsj = auditProject.getContactphone(); //联系人手机
                    String lxryx = auditProject.getContactemail(); //联系人邮箱
                    String sbclqd = "";//申报材料清单
                    String oucode = "";//数据提供部门组织机构代码/社会信用代码
                    Date sbsj = auditProject.getApplydate(); //申办时间
                    List<AuditProjectMaterial> materialList = proviceSelfBuildService
                            .findProjectMaterialListByProjectguid(auditProject.getRowguid());
                    if(StringUtil.isNotBlank(materialList)) {
                        List<String> materialName= new ArrayList<String>();
                        for (AuditProjectMaterial auditProjectMaterial : materialList) {
                            materialName.add(auditProjectMaterial.getTaskmaterial());
                        }
                        sbclqd = StringUtil.join(materialName,";");
                    }
                    String sbjtwd ="";//申办具体网点
                    FrameOu frameOu = ouService.getOuByOuGuid(auditProject.getOuguid());
                    if(StringUtil.isNotBlank(frameOu)) {
                        sbjtwd =frameOu.getOuname()+","+ auditProject.getWindowname();
                        oucode = frameOu.getOucode();
                    }
                   
                    String formid ="";
                    String areacode = codeItemsService.getItemTextByCodeName("辖区对应关系", auditProject.getAreacode());

                    AuditTaskExtension extension =  auditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), false).getResult();
                    if(StringUtil.isNotBlank(extension)) {
                        formid = extension.getStr("formid");
                    }
                    //法人办理业务使用 证照类型 1:社会信用代码   2：组织机构代码 1. 工商注册号  4：税务登记号 5 ： 新 设 企 业  6：无证照 
                    if (auditProject.getApplyertype() == 20) {
                        //个人
                        /*certtype = codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                                auditProject.getCerttype());*/
                        switch (auditProject.getCerttype()) {
                            case "22"://身份证
                                certtype = "10";
                                break;
                            case "24"://台湾居民来往大陆通行证
                                certtype = "15";
                                break;
                            case "26"://香港澳门通行证
                                certtype = "14";
                                break;
                            case "23"://护照
                                certtype = "20";
                                break;
                            default:
                                certtype = "40";
                                break;
                        }
                        sqrlx = "1";
                    }
                    else {
                        sqrlx = "3";
                        switch (auditProject.getCerttype()) {
                            case "14"://组织机构代码证
                                certtype = "2";
                                break;
                            case "16"://统一社会信用代码
                                certtype = "1";
                                break;
                            case "15"://登记号
                                certtype = "4";
                                break;
                            default:
                                certtype = "6";
                                break;
                        }
                    }
                    String date = EpointDateUtil.convertDate2String(new Date(),
                            EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT);

                    String eformXml = getFormElement(auditProject.getRowguid(),formid);
                    String formXml = (String) JSONObject.parseObject(eformXml).getJSONObject("custom").get("eform");
                    StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    xml.append("<RESULT>");
                    xml.append("<STATUS>200</STATUS>");
                    xml.append("<DESC>请求成功</DESC>");
                    xml.append("<TIME>" + date + "</TIME>");
                    xml.append("<DATA>");
                    xml.append("<SXBBBM>" + sxbbbm + "</SXBBBM>");
                    xml.append("<SXBDBM>" + sxbdbm + "</SXBDBM>");
                    xml.append("<YWLY>01</YWLY>");
                    xml.append("<BJMC>"+auditProject.getProjectname()+"</BJMC>");
                    if (auditProject.getApplyertype() == 20) {
                        //<!—自然人申报信息--> 
                        Map<String, String> map = getBirAgeSex(auditProject.getCertnum());
                        xml.append("<SPRENYUAN>");
                        xml.append("<IDENTITYTYPE>" + auditProject.getCerttype() + "</IDENTITYTYPE>");//证件类型      非空
                        xml.append("<IDCARDNO>" + auditProject.getCertnum() + "</IDCARDNO>");//证件号码          非空
                        xml.append("<NAME>" + auditProject.getApplyername() + "</NAME>");//姓名        非空
                        xml.append("<SEX>" + map.get("sexCode") + "</SEX>");//性别         非空
                        xml.append("<NATION></NATION>");//民族
                        xml.append("<POLITICALSTATUS></POLITICALSTATUS>");//政治面貌
                        xml.append("<EDUCATION></EDUCATION>");//学历
                        xml.append("<NATIVEPLACE></NATIVEPLACE>");//籍贯
                        xml.append("<BIRTHDAY>" + map.get("birthday") + "</BIRTHDAY>");//出生日期
                        xml.append("<COUNTRY></COUNTRY>");//国籍
                        xml.append("<HOMEADDRESS>" + auditProject.getAddress() + "</HOMEADDRESS>");//居住地址
                        xml.append("<LINKPHONE>" + auditProject.getContactphone() + "</LINKPHONE>");//联系电话        非空
                        xml.append("<LINKADDRESS>" + auditProject.getAddress() + "</LINKADDRESS>");//联系地址
                        xml.append("<POSTCODE></POSTCODE>");//邮政编码
                        xml.append("<PROVINCE></PROVINCE>");//省
                        xml.append("<CITY></CITY>");//市
                        xml.append("<COUNTY></COUNTY>");//县
                        xml.append("<EMAIL></EMAIL>");//邮箱
                        xml.append("<ISSUEDAT></ISSUEDAT>");//发证机关
                        xml.append("<EFFECTEDDATE></EFFECTEDDATE>");//有效期开始
                        xml.append("<EXPIREDATE></EXPIREDATE>");//有效期结束
                        xml.append("</SPRENYUAN>");
                        //<!—法人人申报信息-->
                        xml.append("<SPQIYE></SPQIYE>");
                    }
                    else {
                        //<!—自然人申报信息--> 
                        xml.append("<SPRENYUAN></SPRENYUAN>");
                        //<!—法人人申报信息-->
                        xml.append("<SPQIYE>");
                        xml.append("<ORGNAME>" + auditProject.getApplyername() + "</ORGNAME>");//组织机构名称     非空
                        xml.append("<ORGCODE>" + auditProject.getCertnum() + "</ORGCODE>");//组织机构代码     非空
                        xml.append("<ORGTYPE>Enterprise</ORGTYPE>");//组织机构类型      非空      SocialOrg:社会团 体; GovDept: 政府 机关; Enterprise: 企业; Institutions: 事 业单位 
                        xml.append("<ORGACTUALITY></ORGACTUALITY>");//组织机构现状        Register:注册； Change：变更； Deregister：注 销；Revoke：吊销 
                        xml.append("<LEGALPERSON>" + auditProject.getLegal() + "</LEGALPERSON>");//法定代表人
                        xml.append("<LEGALPERSONTYPE></LEGALPERSONTYPE>");//法定代表人类型
                        xml.append("<CERTIFICATENAME>身份证</CERTIFICATENAME>");//法人证件名称
                        xml.append("<CERTIFICATENO>" + auditProject.getLegalid() + "</CERTIFICATENO>");//法人证件号码
                        xml.append("<RESPONSIBLEPERSON></RESPONSIBLEPERSON>");// 单位负责人姓名
                        xml.append("<RESPONSIBLEPERSONID></RESPONSIBLEPERSONID>");//单位负责人身份 证 
                        xml.append("<INAREACODE>" + auditProject.getAreacode()+"000000" + "</INAREACODE>");//所属行政区划代 码 
                        xml.append("<INAREA>" + areacode + "</INAREA>");//所属行政区划名称 
                        xml.append("<CHARGEDEPARTMENT></CHARGEDEPARTMENT>");//上级主管单位 
                        xml.append("<REGISTERADDRESS>" + auditProject.getAddress() + "</REGISTERADDRESS>");//单位注册地址 
                        xml.append("<PRODUCEADDRESS></PRODUCEADDRESS>");//单位生产地址 
                        xml.append("<MAILINGADDRESS></MAILINGADDRESS>");//单位通讯地址 
                        xml.append("<POSTALCODE>" + auditProject.getContactpostcode() + "</POSTALCODE>");//邮政编码 
                        xml.append("<LINKMAN>" + auditProject.getContactperson() + "</LINKMAN>");//联系人 
                        xml.append("<CONTACTPHONE>" + auditProject.getContactphone() + "</CONTACTPHONE>");//联系电话 
                        xml.append("<FAX>" + auditProject.getContactfax() + "</FAX>");//传真 
                        xml.append("<LINKMANEMAIL>" + auditProject.getContactemail() + "</LINKMANEMAIL>");//联系人邮箱 
                        xml.append("<BANK></BANK>");//银行名称 
                        xml.append("<ORGANIZATIONKIND></ORGANIZATIONKIND>");//经济性质 
                        xml.append("<EMPLOYEENUM></EMPLOYEENUM>");//单位人数 
                        xml.append("<REGISTERCAPITAL></REGISTERCAPITAL>");//注册资本(金) 
                        xml.append("<FLOAT></FLOAT>");//（单位：万元） 
                        xml.append("<CURRENCYKIND></CURRENCYKIND>");//货币种类 
                        xml.append("<GROUNDAREA></GROUNDAREA>");//占地面积 
                        xml.append("<BUSINESSSCOPE></BUSINESSSCOPE>");//经营(生产)范围 (主营） 
                        xml.append("<BUSINESSSCOPEPART></BUSINESSSCOPEPART>");//经营范围 
                        xml.append("<MAINPRODUCT></MAINPRODUCT>");//主要产品 
                        xml.append("<OPERATINGMODE></OPERATINGMODE>");//经营方式 
                        xml.append("<REGISTERDATE></REGISTERDATE>");//登记注册日期 
                        xml.append("<ORGFOUNDDATE></ORGFOUNDDATE>");//单位成立日期 
                        xml.append("<BUSINESSLICENSE></BUSINESSLICENSE>");//工商注册号 
                        xml.append("<AICAWARDDATE></AICAWARDDATE>");//工商营业执照发 证日期 
                        xml.append("<NATIONTAXREGISTERNO></NATIONTAXREGISTERNO>");//税务登记证号 
                        xml.append("<NATIONTAXAWARDDATE></NATIONTAXAWARDDATE>");//国税税务登记证 发证日期 
                        xml.append("<LOCATIONTAXREGISTERNO></LOCATIONTAXREGISTERNO>");//地税登记证号 
                        xml.append("<LOCATIONTAXAWARDDATE></LOCATIONTAXAWARDDATE>");// 地税税务登记证 发证日期 
                        xml.append("<NATIONINVESTRATE></NATIONINVESTRATE>");//国家投资额百分 比例 
                        xml.append("<CORPORATIONINVESTRATE></CORPORATIONINVESTRATE>");//企业投资额百分 比例 
                        xml.append("<FOREIGNINVESTRATE></FOREIGNINVESTRATE>");//外资投资额百分 比例 
                        xml.append("<NATURALMANINVESTRATE></NATURALMANINVESTRATE>");//自然人投资额百 分比例 
                        xml.append("<BANKLOANRATE></BANKLOANRATE>");//银行贷款额百分 比例 
                        xml.append("<COMPANYLICENSE>" + certtype + "</COMPANYLICENSE>");//法人办理业务使 用证照类型       非空 1:社会信用代码   2：组织机构代码 1. 工商注册号  4：税务登记号 5 ： 新 设 企 业  6：无证照 
                        xml.append("<HANDLERNAME>" + auditProject.getApplyername() + "</HANDLERNAME>");//办理人名称       非空
                        xml.append("<HANDLERPHONE>" + auditProject.getContactphone() + "</HANDLERPHONE>");//办理人联系电话       非空
                        xml.append("<HANDLERIDTYPE>身份证</HANDLERIDTYPE>");//办理人证件类型         非空
                        xml.append("<HANDLERID>" + auditProject.getCertnum() + "</HANDLERID>");//办理人证件号码         非空
                        xml.append("<OTHERPERSON></OTHERPERSON>");//其它申请人 
                        xml.append("<COMPANYIDENTITYTYPE></COMPANYIDENTITYTYPE>");//企业证件类型 
                        xml.append("<ENTERPRISESORTNAME></ENTERPRISESORTNAME>");//企业类别名称 
                        xml.append("<ENTERPRISESORTCODE></ENTERPRISESORTCODE>");//企业类别编码 
                        xml.append("<ORGCODEAWARDDATE></ORGCODEAWARDDATE>");//组织机构代码证 发证日期 
                        xml.append("<ORGCODEAWARDORG></ORGCODEAWARDORG>");//组织机构代码证 发证机构 
                        xml.append("<ORGCODEVALIDPERIODSTART></ORGCODEVALIDPERIODSTART>");//组织机构代码证 有效期起 
                        xml.append("<ORGCODEVALIDPERIODEND></ORGCODEVALIDPERIODEND>");//组织机构代码证 有效期止 
                        xml.append("<ORGENGLISHNAME></ORGENGLISHNAME>");//组织英文名 
                        xml.append("<REMARK></REMARK>");//备注信息 
                        xml.append("</SPQIYE>");
                    }
                    xml.append("<SPXIANGMU>");//<!—项目类申报信息--> 
                    xml.append("</SPXIANGMU>");
                    xml.append("<SPSHENQIN>");//<!—-审批申请基本信息--> 
                    xml.append("<SBLSH_SHORT>"+flowsn+"</SBLSH_SHORT>");        //申办流水号       
                    xml.append("<SBLSH>"+flowsn+"</SBLSH>");         //备用流水号
                    xml.append("<SXBM>"+newItemCode+"</SXBM>");              //事项编码
                    xml.append("<SXBM_SHORT>"+itemCode+"</SXBM_SHORT>");         //事项简码
                    xml.append("<SXMC>"+taskname+"</SXMC>");           //事项名称
                    xml.append("<SXQXBM></SXQXBM>"); //事项情形编码
                    xml.append("<FSXBM></FSXBM>"); //父事项编码
                    xml.append("<FSXMC></FSXMC>"); //父事项名称
                    xml.append("<SQRLX>"+sqrlx+"</SQRLX>");        //申请人类型
                    xml.append("<SQRMC>"+sqrmc+"</SQRMC>");        //申请人名称
                    xml.append("<SQRZJLX>"+certtype+"</SQRZJLX>");           //申请人证件类型
                    xml.append("<SQRZJHM>"+certnum+"</SQRZJHM>");          //申请人证件号码
                    xml.append("<LXRXM>"+lxrxm+"</LXRXM>");        //联系人姓名
                    xml.append("<LXRZJLX>"+certtype+"</LXRZJLX>");           //联系人证件类型
                    xml.append("<LXRSFZJHM>"+certnum+"</LXRSFZJHM>");          //联系人身份证件号码
                    xml.append("<LXRSJ>"+lxrsj+"</LXRSJ>");        //联系人手机
                    xml.append("<LXRYX>"+lxryx+"</LXRYX>");        //联系人邮箱
                    xml.append("<SBXMMC>"+taskname+"</SBXMMC>");           //申办项目名称
                    xml.append("<SBCLQD>"+sbclqd+"</SBCLQD>");         //申报材料清单
                    xml.append("<TJFS></TJFS>");  //提交方式
                    xml.append("<SBHZH>"+flowsn+"</SBHZH>");         //申办回执号
                    xml.append("<SBSJ>"+EpointDateUtil.convertDate2String(sbsj, "YYYY-MM-DD hh24:mi:ss")+"</SBSJ>");       //申办时间
                    xml.append("<SBJTWD>"+sbjtwd+"</SBJTWD>");         //申办具体网点
                    xml.append("<XZQHDM>"+auditProject.getAreacode()+"000000"+"</XZQHDM>");           //业务发生所在地区编号
                    xml.append("<YSBLSH></YSBLSH>");  //原申办流水号
                    xml.append("<USERIDCODE></USERIDCODE>");  //网厅统一身份认证平台账号
                    xml.append("<PROJECT_CODE></PROJECT_CODE>");  //投资项目统一编码
                    xml.append("<WSSBQD></WSSBQD>");  //网上申办渠道
                    xml.append("<OBTAIN_PAPER_WAY>"+0+"</OBTAIN_PAPER_WAY>");     //取证方式
                    xml.append("<OBTAIN_DELIVER_WAY>"+0+"</OBTAIN_DELIVER_WAY>"); //材料递交方式
                    xml.append("<VERSION>"+1+"</VERSION>");    //数据版本号
                    xml.append("<REC_FLAG>"+0+"</REC_FLAG>");    //接收标识
                    xml.append("<D_ZZJGDM>"+oucode+"</D_ZZJGDM>");         //数据提供部门组织机构代码/社会信用代码
                    xml.append("<BZ></BZ>");   //备注
                    xml.append("<XML_DATA>"+formXml+"</XML_DATA>"); //电子表单信息
                    xml.append("</SPSHENQIN>");
                    xml.append("<SPCAILIAOSHENHE>");//<!—-网上预审信息数-->
                    xml.append("<SBLSH>"+flowsn+"</SBLSH>");
                    xml.append("<SBLSH_SHORT>"+flowsn+"</SBLSH_SHORT>");
                    xml.append("<SXBM_SHORT>"+newItemCode+"</SXBM_SHORT>");
                    xml.append("<ITEM_CODE>"+newItemCode+"</ITEM_CODE>");
                    xml.append("<YWLSH>"+auditProject.getRowguid()+"</YWLSH>");
                    xml.append("<SXMC>"+auditProject.getProjectname()+"</SXMC>");
                    xml.append("<SXQXBM>"+auditProject.getRowguid()+"</SXQXBM>");
                    xml.append("<YSLBMMC>"+auditProject.getOuname()+"</YSLBMMC>");
                    xml.append("<YSLBMZZJGDM>"+oucode+"</YSLBMZZJGDM>");
                    xml.append("<XZQHDM>"+auditProject.getAreacode()+"000000"+"</XZQHDM>");
                    xml.append("<BLRXM>自动预审</BLRXM>");
                    xml.append("<BLRGH>00000000000000000000000000000</BLRGH>");
                    xml.append("<YSLZTDM>1</YSLZTDM>");
                    xml.append("<BSLYY></BSLYY>");
                    xml.append("<BJBZSM></BJBZSM>");
                    xml.append("<YSLSJ>"+EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-DD hh24:mi:ss")+"</YSLSJ>");
                    xml.append("<YSLJTDD></YSLJTDD>");
                    xml.append("<BZ></BZ>");
                    xml.append("<F_XZQHDM></F_XZQHDM>");
                    xml.append("<VERSION>1</VERSION>");
                    xml.append("<PROJECT_CODE></PROJECT_CODE>");
                    xml.append("<D_ZZJGDM>"+oucode+"</D_ZZJGDM>");
                    xml.append("<REC_FLAG>0</REC_FLAG>");
                    xml.append("</SPCAILIAOSHENHE>");
                    xml.append("<SPSHOULI>"); //<!—受理信息数据-->
                    
                    xml.append("<SBLSH>"+flowsn+"</SBLSH>");
                    xml.append("<SBLSH_SHORT>"+flowsn+"</SBLSH_SHORT>");
                    xml.append("<SXBM_SHORT>"+newItemCode+"</SXBM_SHORT>");
                    xml.append("<SXBM>"+newItemCode+"</SXBM>");
                    xml.append("<YWLSH>"+auditProject.getRowguid()+"</YWLSH>");
                    xml.append("<SXMC>"+auditProject.getProjectname()+"</SXMC>");
                    xml.append("<SXQXBM>"+auditProject.getRowguid()+"</SXQXBM>");
                    xml.append("<YWCXMM></YWCXMM>");
                    xml.append("<CKCLBM></CKCLBM>");
                    xml.append("<SLBMMC>"+auditProject.getOuname()+"</SLBMMC>");
                    xml.append("<SLBMZZJGDM>"+oucode+"</SLBMZZJGDM>");
                    xml.append("<XZQHDM>"+auditProject.getAreacode()+"000000"+"</XZQHDM>");
                    xml.append("<XZQHMC>济宁市</XZQHMC>");
                    xml.append("<BLRXM>自动预审</BLRXM>");
                    xml.append("<BLRGH>00000000000000000000000000000</BLRGH>");
                    xml.append("<SLZTDM>1</SLZTDM>");
                    xml.append("<SFYDBR>0</SFYDBR>");
                    xml.append("<DBRXM></DBRXM>");
                    xml.append("<DBRZJHM></DBRZJHM>");
                    xml.append("<DBRZJLX></DBRZJLX>");
                    xml.append("<DBRLXDH></DBRLXDH>");
                    xml.append("<BSLYY/>");
                    xml.append("<BLYJ>自动预审通过</BLYJ>");
                    xml.append("<SLHZH>"+flowsn+"</SLHZH>");
                    xml.append("<SLSJ>"+EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-DD hh24:mi:ss")+"</SLSJ>");
                    xml.append("<GXDXZQHDM/>");
                    xml.append("<BZ/>");
                    xml.append("<F_XZQHDM/>");
                    xml.append("<PROJECT_CODE/>");
                    xml.append("<VERSION>1</VERSION>");
                    xml.append("<REC_FLAG>0</REC_FLAG>");
                    xml.append("<BJCKMC>null</BJCKMC>");
                    xml.append("<BJCKBM>null</BJCKBM>");
                    xml.append("<BJDTBM>null</BJDTBM>");
                    xml.append("<BMDJMC>null</BMDJMC>");
                    
                    xml.append("</SPSHOULI>");
                    xml.append("<SPBANJIE>");//<!-存储即办件的业务办结信息 -->
                    xml.append("</SPBANJIE>");
                    xml.append("<SPTZS>");//<!--受理平台产生的通知书-->
                    xml.append("</SPTZS>");
                    xml.append("</DATA>");
                    xml.append("</RESULT>");
                    String newXmlStr = xml.toString();
//                    String newXmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><RESULT><STATUS>200</STATUS><DESC>请求成功！</DESC><DATA><SXBBBM>c39be28a-a5a1-4fcd-b6b1-5d09e10e1104</SXBBBM><SXBDBM>ShiYeBaoXianJinShenLing</SXBDBM><FORM_DATA_ID>20210129140650863400</FORM_DATA_ID><PROMISE_DAYS>0</PROMISE_DAYS><LIMIT_TIME>-</LIMIT_TIME><BATCH_ID>WB3C2C5F43B2884944889EF5F5AF096A35</BATCH_ID><YWLY>01</YWLY><BJMC>关于王永军失业保险金申领的申请</BJMC><ACTUALREGION>null</ACTUALREGION><SPRENYUAN><COUNTRY></COUNTRY><LINKPHONE>15663488889</LINKPHONE><EDUCATION></EDUCATION><EFFECTEDDATE></EFFECTEDDATE><NATION></NATION><CITY></CITY><POLITICALSTATUS></POLITICALSTATUS><SEX></SEX><COUNTY></COUNTY><IDCARDNO>230183197809164415</IDCARDNO><LINKADDRESS></LINKADDRESS><EXPRIREDDATE></EXPRIREDDATE><PROVINCE></PROVINCE><IDENTITYTYPE>10</IDENTITYTYPE><BRITHDAY></BRITHDAY><NAME>王永军</NAME><NATIVEPLACE></NATIVEPLACE><POSTCODE></POSTCODE><ISSUEDAT></ISSUEDAT><EMAIL></EMAIL><SEQID>219E8BC2EACE4E0CABD2321FF9679E24</SEQID><HOMEADDRESS></HOMEADDRESS></SPRENYUAN><BZ></BZ><SPQIYE></SPQIYE><SPXIANGMU></SPXIANGMU><SPSHENQIN><SBLSH_SHORT>0601205722713</SBLSH_SHORT><SBLSH>0601205722713</SBLSH><SXBM>202911</SXBM><SXBM_SHORT>202911</SXBM_SHORT><SXMC>失业保险金申领</SXMC><SXQXBM></SXQXBM><FSXBM></FSXBM><FSXMC></FSXMC><SQRLX>1</SQRLX><SQRMC>王永军</SQRMC><SQRZJLX>10</SQRZJLX><SQRZJHM>230183197809164415</SQRZJHM><LXRXM>王永军</LXRXM><LXRZJLX>10</LXRZJLX><LXRSFZJHM>230183197809164415</LXRSFZJHM><LXRSJ>15663488889</LXRSJ><LXRYX></LXRYX><SBXMMC>失业保险金申领</SBXMMC><SQRZHBS>7936202a7d9c4825b12dd03e9800e3a6</SQRZHBS><SQRZHMC>王永军</SQRZHMC><SBCLQD>失业信息采集表;</SBCLQD><TJFS>0</TJFS><SBHZH>0601205722713</SBHZH><SBSJ>2021-01-29 14:04:37</SBSJ><SBJTWD>null,null</SBJTWD><XZQHDM>370600000000</XZQHDM><YSBLSH>0601205722713</YSBLSH><BZ></BZ><F_XZQHDM></F_XZQHDM><USERIDCODE></USERIDCODE><PROJECT_CODE></PROJECT_CODE><WSSBQD>0</WSSBQD><OBTAIN_PAPER_WAY>0</OBTAIN_PAPER_WAY><OBTAIN_DELIVER_WAY>0</OBTAIN_DELIVER_WAY><VERSION>1</VERSION><REC_FLAG>0</REC_FLAG><D_ZZJGDM>37060001018</D_ZZJGDM><XML_DATA><SPItemListDef><TableCols name=\"ShiYeBaoXianJinShenLing\" id=\"20210129140650863400\"><csrq></csrq><dwid>10500000003706291975</dwid><scsyxsys>0</scsyxsys><yhzh>6217566000022394134</yhzh><dacsrq>1978-09-16</dacsrq><dwmc>海阳朗丽纺织有限公司</dwmc><gmsfhm>230183197809164415</gmsfhm><rydjid>05370629000000616427</rydjid><gzbh></gzbh><sclqys>0</sclqys><ShiFuZhuanChu>0</ShiFuZhuanChu><bcnhjfys>12</bcnhjfys><rsxtid>3756</rsxtid><biz>37069G01</biz><syrq></syrq><syzhm></syzhm><ryid>05370629000000616427</ryid><bz></bz><MAIN_TBL_PK>20210129140650863400</MAIN_TBL_PK><bcczjfys>17</bcczjfys><tbrq>20210129</tbrq><jfqsny>201504</jfqsny><khyh>中国银行海阳支行</khyh><tszclb></tszclb><cbd>烟台市海阳市企业</cbd><syqcjgzrq>2015-04-01</syqcjgzrq><sydjbh></sydjbh><sjhm>15663488889</sjhm><xb>1</xb><syyy>20</syyy><nhtzjfys></nhtzjfys><BuKeJiXuBanLi></BuKeJiXuBanLi><cztzjfys></cztzjfys><hkxz></hkxz><xm>王永军</xm><zyqyfw></zyqyfw><slrbh>01</slrbh><gzmc></gzmc><jtzz></jtzz><bljgid>37069G01</bljgid><jfzzny>201608</jfzzny><syrylb>01</syrylb></TableCols></SPItemListDef></XML_DATA></SPSHENQIN><SPCAILIAOSHENHE><SBLSH>0601205722713</SBLSH><SBLSH_SHORT>0601205722713</SBLSH_SHORT><SXBM_SHORT>202911</SXBM_SHORT><ITEM_CODE>202911</ITEM_CODE><YWLSH>WB3C2C5F43B2884944889EF5F5AF096A35</YWLSH><SXMC>失业保险金申领</SXMC><SXQXBM>c39be28a-a5a1-4fcd-b6b1-5d09e10e1104</SXQXBM><YSLBMMC>烟台市人力资源和社会保障局</YSLBMMC><YSLBMZZJGDM>37060001018</YSLBMZZJGDM><XZQHDM>370600000000</XZQHDM><BLRXM>自动预审</BLRXM><BLRGH>88888888888888888888888888888888</BLRGH><YSLZTDM>1</YSLZTDM><BSLYY></BSLYY><BJBZSM></BJBZSM><YSLSJ>2021-01-29 14:04:40</YSLSJ><YSLJTDD>null,null</YSLJTDD><BZ></BZ><F_XZQHDM></F_XZQHDM><VERSION>1</VERSION><PROJECT_CODE></PROJECT_CODE><D_ZZJGDM>37060001018</D_ZZJGDM><REC_FLAG>0</REC_FLAG></SPCAILIAOSHENHE><SPSHOULI><SBLSH>0601205722713</SBLSH><SBLSH_SHORT>0601205722713</SBLSH_SHORT><SXBM_SHORT>202911</SXBM_SHORT><SXBM>202911</SXBM><YWLSH>WB3C2C5F43B2884944889EF5F5AF096A35</YWLSH><SXMC>失业保险金申领</SXMC><SXQXBM>c39be28a-a5a1-4fcd-b6b1-5d09e10e1104</SXQXBM><YWCXMM>686540</YWCXMM><CKCLBM>370600010182101291562</CKCLBM><SLBMMC>烟台市人力资源和社会保障局</SLBMMC><SLBMZZJGDM>37060001018</SLBMZZJGDM><XZQHDM>370600000000</XZQHDM><XZQHMC>烟台市</XZQHMC><BLRXM>自动预审</BLRXM><BLRGH>88888888888888888888888888888888</BLRGH><SLZTDM>1</SLZTDM><SFYDBR>0</SFYDBR><DBRXM>null</DBRXM><DBRZJHM>null</DBRZJHM><DBRZJLX>null</DBRZJLX><DBRLXDH>null</DBRLXDH><BSLYY></BSLYY><BLYJ>自动预审通过</BLYJ><SLHZH>0601205722713</SLHZH><SLSJ>2021-01-29 14:04:37</SLSJ><GXDXZQHDM></GXDXZQHDM><BZ></BZ><F_XZQHDM></F_XZQHDM><PROJECT_CODE></PROJECT_CODE><VERSION>1</VERSION><REC_FLAG>0</REC_FLAG><BJCKMC>null</BJCKMC><BJCKBM>null</BJCKBM><BJDTBM>null</BJDTBM><BMDJMC>null</BMDJMC></SPSHOULI><SPFUWUQINGDAN><SERVICE_LIST_ID>4126B1CBD67F4CD994727A45429CB956</SERVICE_LIST_ID><APPLICANT_NAME>王永军</APPLICANT_NAME><APPLICANT_TYPE>10</APPLICANT_TYPE><APPLICANT_ID>230183197809164415</APPLICANT_ID><APPLICANT_MOBILE>15663488889</APPLICANT_MOBILE><APPLICANT_ADDR>null</APPLICANT_ADDR><SEND_TYPE>0</SEND_TYPE><TAKE_HALL_CODE>null</TAKE_HALL_CODE><TAKE_HALL_NAME>null</TAKE_HALL_NAME><SEND_MAIL_ADDR>null</SEND_MAIL_ADDR><MAIL_ADDR>null</MAIL_ADDR><TRUE_HANDLE_AREACODE>null</TRUE_HANDLE_AREACODE><TRUE_HANDLE_AREANAME>null</TRUE_HANDLE_AREANAME><TRUE_HANDLE_DEPTCODE>null</TRUE_HANDLE_DEPTCODE><TRUE_HANDLE_DEPTNAME>null</TRUE_HANDLE_DEPTNAME><MAIL_TIMES>null</MAIL_TIMES><MAIL_COST>null</MAIL_COST><GET_TYPE>null</GET_TYPE><GET_HALL_CODE>null</GET_HALL_CODE><GET_HALL_NAME>null</GET_HALL_NAME><GET_MAIL_ADDR>null</GET_MAIL_ADDR><GET_MAIL_PROVINCE>null</GET_MAIL_PROVINCE><GET_MAIL_PROVINCE_CODE>null</GET_MAIL_PROVINCE_CODE><GET_MAIL_CITY>null</GET_MAIL_CITY><GET_MAIL_CITY_CODE>null</GET_MAIL_CITY_CODE><GET_MAIL_DISTRICT>null</GET_MAIL_DISTRICT><GET_MAIL_DISTRICT_CODE>null</GET_MAIL_DISTRICT_CODE><GET_MAIL_BLOCK>null</GET_MAIL_BLOCK><GET_MAIL_BLOCK_CODE>null</GET_MAIL_BLOCK_CODE><SEND_MAIL_PROVINCE>null</SEND_MAIL_PROVINCE><SEND_MAIL_PROVINCE_CODE>null</SEND_MAIL_PROVINCE_CODE><SEND_MAIL_CITY>null</SEND_MAIL_CITY><SEND_MAIL_CITY_CODE>null</SEND_MAIL_CITY_CODE><SEND_MAIL_DISTRICT>null</SEND_MAIL_DISTRICT><SEND_MAIL_DISTRICT_CODE>null</SEND_MAIL_DISTRICT_CODE><SEND_MAIL_BLOCK>null</SEND_MAIL_BLOCK><SEND_MAIL_BLOCK_CODE>null</SEND_MAIL_BLOCK_CODE><SEND_MAIL_POSTCODE>null</SEND_MAIL_POSTCODE><EMS_NAME>null</EMS_NAME><EMS_MOBILE>null</EMS_MOBILE><MAIL_GET_MAIL_TEL>null</MAIL_GET_MAIL_TEL><PAY_TYPE>null</PAY_TYPE><CREATE_TIME>2021-01-29 14:05:47.0</CREATE_TIME><MAIL_WEIGHT>null</MAIL_WEIGHT></SPFUWUQINGDAN></DATA></RESULT>";
                    log.info("==========结束调用getApplyInfoSsdj接口=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return newXmlStr;
                }
                else {
                    log.info("==========结束调用getApplyInfoSsdj接口=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return generateXmlReturn("300", "未获取办件信息");
                }
            }
            else {
                log.info("==========结束调用getApplyInfoSsdj接口=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return generateXmlReturn("300", "申办编号SBLSH_SHORT为空");
            }
            
        }
        catch (Exception e) {
            log.info("==========结束调用getApplyInfoSsdj接口=========="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            return generateXmlReturn("300", "调用接口发生异常");
        }
    }

    /**
     * 根据申办编号获取申报材料入参报文
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/getApplyAttaInfoSsdj", method = {RequestMethod.POST })
    public String getApplyAttaInfoSsdj(HttpServletRequest params) throws Exception {
        log.info("==========开始调用getApplyAttaInfoSsdj接口=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            log.info("==========params==========" + params);
            
            String accessToken = params.getParameter("accessToken");
    		
            String xmlstrold = params.getParameter("xmlStr");
            String xmlStr = "";
            if (xmlstrold.contains("version")) {
            	xmlStr = xmlstrold.substring(38);
            }else {
            	return generateXmlReturn("300", "请求入参的XmlStr不正确");
            }
            
            
            String sysAccessToken = configServicce.getFrameConfigValue("rsjAccessToken");
            //暂时先不校验accessToken
            if (StringUtil.isBlank(accessToken)) {
                return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
            }
            if (!accessToken.equals(sysAccessToken)) {
                return generateXmlReturn("300", "accessToken参数不正确");
            }
            if (StringUtil.isBlank(xmlStr)) {
                return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr");
            }

            log.info("=====getApplyAttaInfoSsdj.输入参数:" + xmlStr);
            Document document = DocumentHelper.parseText(xmlStr);
            Element rootElement = document.getRootElement();
            String flowsn = rootElement.elementText("SBLSH_SHORT"); // 申办流水号
            String newItemCode = rootElement.elementText("SXBM"); // 事项编码
            String ywlx = rootElement.elementText("YWLX"); // 业务类型     0：业务申请  1：补正受理
            if (StringUtil.isNotBlank(flowsn)) {
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if (StringUtil.isNotBlank(auditProject)) {

                    List<AuditProjectMaterial> materialList = proviceSelfBuildService
                            .findProjectMaterialListByProjectguid(auditProject.getRowguid());
                    String areaCode = auditProject.getAreacode();
                    String ouGuid = auditProject.getOuguid();
                    FrameOu frameOu = ouService.getOuByOuGuid(ouGuid);
                    String oucode = "";
                    if (frameOu != null) {
                        oucode = frameOu.getStr("OUCODE");
                    }
                    if (materialList != null && materialList.size() > 0) {
                        String date = EpointDateUtil.convertDate2String(new Date(),
                                EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT);

                        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        xml.append("<RESULT>");
                        xml.append("<STATUS>200</STATUS>");
                        xml.append("<DESC>请求成功</DESC>");
                        xml.append("<TIME>" + date + "</TIME>");
                        xml.append("<DATA>");
                        xml.append("<RECORDS>");

                        for (AuditProjectMaterial auditProjectMaterial : materialList) {
                            String materialno = auditProjectMaterial.getTaskmaterialguid();
                            String materialName = auditProjectMaterial.getTaskmaterial();
                            String cliengguid = auditProjectMaterial.getCliengguid();
                            List<FrameAttachInfo> attachInfos = attachService.getAttachInfoListByGuid(cliengguid);

                            String attachGuid = "";
                            String attachFileName = "";
                            String contentType = "";
                            if (attachInfos != null && attachInfos.size() > 0) {
                                attachGuid = attachInfos.get(0).getAttachGuid();
                                attachFileName = attachInfos.get(0).getAttachFileName();
                                contentType = attachInfos.get(0).getContentType();
                            }
                            xml.append("<RECORD>");
                            xml.append("<SEQ>" + UUID.randomUUID().toString().replace("-", "") + "</SEQ>");//主键，uuid 32位        非空
                            xml.append("<SBLSH_SHORT>" + flowsn + "</SBLSH_SHORT>");//申办流水号     非空
                            xml.append("<SBLSH>" + flowsn + "</SBLSH>");//备用流水号     非空
                            if(StringUtil.isBlank(attachGuid)) {
                                xml.append("<WJLX>0</WJLX>");//文件类型     非空   0 纸质  1电子
                                xml.append("<CLLX>1</CLLX>");//材料类型     非空   0:原件  1  纸质   2 电子
                            }else {
                                xml.append("<WJLX>1</WJLX>");//文件类型     非空   0 纸质  1电子
                                xml.append("<CLLX>2</CLLX>");//材料类型     非空   0:原件  1  纸质   2 电子
                            }
                            xml.append("<CLSL>1</CLSL>");//材料数量     非空
                            xml.append("<ATTACH_NAME>" + attachFileName + "</ATTACH_NAME>");//附件名称
                            xml.append("<ATTACH_ID>" + attachGuid + "</ATTACH_ID>");//附件uuid
                            xml.append("<REMARK></REMARK> ");//材料备注
                            xml.append("<ATTACH_BODY></ATTACH_BODY>");//附件文件流
                            xml.append("<SAVE_TYPE>1</SAVE_TYPE>");//存储类型
                            xml.append("<ATTACH_SIGN></ATTACH_SIGN>");//附件签名
                            xml.append("<ATTACH_TYPE>" + contentType + "</ATTACH_TYPE>");//附件类型
                            xml.append("<ATTACH_PATH>" + attachGuid + "</ATTACH_PATH>");// 网盘ID/附件路径
                            xml.append("<STUFF_SEQ>" + materialno + "</STUFF_SEQ>");// 材料编码    非空
                            xml.append("<CLMC>" + materialName + "</CLMC> ");// 材料名称        非空
                            xml.append("<SLBMZZJDDM>" + oucode + "</SLBMZZJDDM>");//部门组织机构代码
                            xml.append("<XZQHDM>" + areaCode + "</XZQHDM>"); //部门所在行政区划
                            xml.append("<VERSION>1</VERSION>");//版本
                            xml.append("<REC_FLAG></REC_FLAG>");//接收标识
                            xml.append("</RECORD>");
                        }
                        xml.append("</RECORDS>");
                        xml.append("</DATA>");
                        xml.append("</RESULT>");
                        String newXmlStr = xml.toString();
//                        String newXmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><RESULT><STATUS>200</STATUS><DESC>请求成功！</DESC><DATA><RECORDS><RECORD><SEQ>6DF4010D724B4E36AF7F8CB72F94290B</SEQ><SBLSH_SHORT>0601205722713</SBLSH_SHORT><UPLOAD_TIME>2021-01-29 14:04:37</UPLOAD_TIME><WJLX>1</WJLX><CLLX>2</CLLX><CLSL>1</CLSL><ATTACH_NAME>54fac066c3234dfda2df35132dd49aea.docx</ATTACH_NAME><ATTACH_ID>6DF4010D724B4E36AF7F8CB72F94290B</ATTACH_ID><SAVE_TYPE>0</SAVE_TYPE><ATTACH_SIGN></ATTACH_SIGN><ATTACH_TYPE>.docx</ATTACH_TYPE><DISTRIBUTEID></DISTRIBUTEID><ATTACH_PATH>4285418</ATTACH_PATH><WEBDISK_ADDRESSS>http://59.224.6.4:8390/WebDiskServerDemo/doc?doc_id=</WEBDISK_ADDRESSS><REMARK>无</REMARK><STUFF_SEQ>1907B187ABE74883A7061E7C8D689DD0</STUFF_SEQ><CLMC>失业信息采集表</CLMC><SLBMZZJDDM></SLBMZZJDDM><XZQHDM></XZQHDM><VERSION>1</VERSION><REC_FLAG></REC_FLAG><STEP_ID></STEP_ID></RECORD></RECORDS></DATA></RESULT>";
                        log.info("==========开始调用getApplyAttaInfoSsdj接口=========="
                                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        return newXmlStr;
                    }
                    else {
                        log.info("==========开始调用getApplyAttaInfoSsdj接口=========="
                                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        return generateXmlReturn("300", "没有获取到材料信息");
                    }
                }
                else {
                    log.info("==========开始调用getApplyAttaInfoSsdj接口=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return generateXmlReturn("300", "获取办件信息失败");
                }
            }
            else {
                log.info("==========开始调用getApplyAttaInfoSsdj接口=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return generateXmlReturn("300", "申办流水号为空");
            }
        }
        catch (Exception e) {
            log.info("==========开始调用getApplyAttaInfoSsdj接口=========="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            return generateXmlReturn("300", "调用接口失败");
        }
    }

    /**
     * 接收部门业务受理信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/receiveBusinessAcceptSsdj", method = {RequestMethod.POST })
    public String receiveBusinessAcceptSsdj(HttpServletRequest params) throws Exception {
        log.info("==========开始调用receiveBusinessAcceptSsdj接口=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            log.info("==========params==========" + params);
            String accessToken = params.getParameter("accessToken");
            String xmlstrold = params.getParameter("xmlStr");
            String xmlStr = "";
            if (xmlstrold.contains("version")) {
            	xmlStr = xmlstrold.substring(38);
            }else {
            	return generateXmlReturn("300", "请求入参的XmlStr不正确");
            }
            String sysAccessToken = configServicce.getFrameConfigValue("rsjAccessToken");
            //暂时先不校验accessToken
            if (StringUtil.isBlank(accessToken)) {
                return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
            }
            if (!accessToken.equals(sysAccessToken)) {
                return generateXmlReturn("300", "accessToken参数不正确");
            }
            if (StringUtil.isBlank(xmlStr)) {
                return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr！");
            }

            // 申办流水号
            String flowsn = "";
            // 事项编码
            String newItemCode = "";
            // 业务受理时间
            String ywslsj = "";
            // 业务受理人名称
            String ywslrmc = "";
            // 业务受理人工号
            String ywslrbm = "";
            // 业务受理状态
            String ywslzt = "";
            // 业务受理意见
            String ywslyj = "";
            // 业务受理区划名称
            String ywslqhmc = "";
            // 业务受理区划编码
            String ywslqhbm = "";
            // 业务受理部门名称
            String ywslbmmc = "";
            // 业务受理部门编码
            String ywslbmbm = "";

            Document document = DocumentHelper.parseText(xmlStr);
            Element rootElement = document.getRootElement();
            flowsn = rootElement.elementText("SBLSH_SHORT");
            newItemCode = rootElement.elementText("SXBM");
            List<Element> childNodes = rootElement.elements("SHOULI");
            for (Element element : childNodes) {
                ywslsj = element.elementText("YWSLSJ");
                ywslrmc = element.elementText("YWSLRMC");
                ywslrbm = element.elementText("YWSLRBM");
                ywslzt = element.elementText("YWSLZT");
                ywslyj = element.elementText("YWSLYJ");
                ywslqhmc = element.elementText("YWSLQHMC");
                ywslqhbm = element.elementText("YWSLQHBM");
                ywslbmmc = element.elementText("YWSLBMMC");
                ywslbmbm = element.elementText("YWSLBMBM");
            }

            Date acceptuserdate = EpointDateUtil.convertString2DateAuto(ywslsj);
            if (StringUtil.isNotBlank(flowsn)) {
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if (StringUtil.isNotBlank(auditProject)) {
                    String projectguid = auditProject.getRowguid();
                    AuditOnlineProject auditOnlineProject = daTingService.getAuditOnlineProjectByProjectguid(projectguid);
                    auditProject.setAcceptuserdate(acceptuserdate);
                    auditProject.setAcceptusername(ywslrmc);
                    int status = 0;
                    if ("0".equals(ywslzt)) {
                        auditProject.setStatus(ZwdtConstant.BANJIAN_STATUS_BYSL);
                        status=ZwdtConstant.BANJIAN_STATUS_BYSL;
                    }
                    if ("1".equals(ywslzt)) {
                        auditProject.setStatus(ZwdtConstant.BANJIAN_STATUS_YSL);
                        status=ZwdtConstant.BANJIAN_STATUS_YSL;
                    }
                    auditProject.setRemark(ywslyj);
                    auditProjectService.updateProject(auditProject);
                    daTingService.updateAuditProjectByProjectguid(projectguid, status, 0);
                    if(StringUtil.isNotBlank(auditOnlineProject)) {
                        auditOnlineProject.setStatus(status+"");
                        auditOnlineProject.setFlowsn(flowsn);
                        auditOnlineProjectService.updateProject(auditOnlineProject);
                    }
                    
                    AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                    auditProjectOperation.setOperatedate(new Date());
                    auditProjectOperation.setProjectGuid(auditProject.getRowguid());
                    auditProjectOperation.setPVIGuid(auditProject.getPviguid());
                    auditProjectOperation.setOperateusername(ywslrmc);
                    auditProjectOperation.setApplyerName(auditProject.getApplyername());
                    auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                    auditProjectOperation.setOperateType("23");
                    auditProjectOperation.setAreaCode(auditProject.getAreacode());
                    auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                    auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                    auditProjectOperationService.addProjectOperation(auditProjectOperation);
                    
                    String xmlReturn = generateXmlReturn("200", "请求成功");

                    log.info("==========xmlReturn==========" + xmlReturn);
                    log.info("==========结束调用receiveBusinessAcceptSsdj接口=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return xmlReturn;
                }
                else {
                    log.info("==========结束调用receiveBusinessAcceptSsdj接口=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return generateXmlReturn("300", "获取办件信息失败");
                }
            }
            else {
                log.info("==========结束调用receiveBusinessAcceptSsdj接口=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return generateXmlReturn("300", "获取办件流水号失败");
            }
            
        }
        catch (Exception e) {
            log.info("==========结束调用receiveBusinessAcceptSsdj接口=========="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            return generateXmlReturn("300", "调用接口失败");
        }
    }

    /**
     * 接收业务审批环节信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/receiveCourseInfoSsdj", method = {RequestMethod.POST })
    public String receiveCourseInfoSsdj(HttpServletRequest params) throws Exception {
        log.info("==========开始调用receiveCourseInfoSsdj接口=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            log.info("==========params==========" + params);
            
            String accessToken = params.getParameter("accessToken");
            String xmlstrold = params.getParameter("xmlStr");
            String xmlStr = "";
            if (xmlstrold.contains("version")) {
            	xmlStr = xmlstrold.substring(38);
            }else {
            	return generateXmlReturn("300", "请求入参的XmlStr不正确");
            }
            
            String sysAccessToken = configServicce.getFrameConfigValue("rsjAccessToken");
            //暂时先不校验accessToken
            if (StringUtil.isBlank(accessToken)) {
                return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
            }
            if (!accessToken.equals(sysAccessToken)) {
                return generateXmlReturn("300", "accessToken参数不正确");
            }
            if (StringUtil.isBlank(xmlStr)) {
                return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr");
            }

            log.info("=====receiveCourseInfoSsdj.输入参数:" + xmlStr);
            Document document = DocumentHelper.parseText(xmlStr);
            Element rootElement = document.getRootElement();
            String flowsn = rootElement.elementText("SBLSH_SHORT"); // 申办流水号
            String newItemCode = rootElement.elementText("SXBM"); // 事项编码
            String sblsh_short = "";
            String sblsh = "";
            String sxbm = "";
            String sxbm_short = "";
            String sxqxbm = "";
            String sphjdm = "";
            String sphjmc = "";
            String spbmmc = "";
            String spbmzzjdmd = "";
            String xzqhdm = "";
            String sprxm = "";
            String sprzwdm= "";
            String sprzwmc= "";
            String spyj = "";
            String spsj = "";
            String sphjztdm = "";
            String rec_flag = "";
            String d_zzjgdm = "";
            String bz = "";
            /*String clzt = "";
            String clsj = "";
            String byzd = "";
            String byzd1 = "";
            String byzd2 = "";
            String byzd3 = "";
            String byzd4 = "";*/
            List<Element> childNodes = rootElement.elements("GDBSSPCL");
            for (Element element : childNodes) {
                sblsh_short = element.elementText("SBLSH_SHORT");
                sblsh = element.elementText("SBLSH");
                sxbm = element.elementText("SXBM");
                sxbm_short = element.elementText("SXBM_SHORT");
                sxqxbm = element.elementText("SXQXBM");
                sphjdm = element.elementText("SPHJDM");
                sphjmc = element.elementText("SPHJMC");
                spbmmc = element.elementText("SPBMMC");
                spbmzzjdmd = element.elementText("SPBMZZJDMD");
                xzqhdm = element.elementText("XZQHDM");
                sprxm = element.elementText("SPRXM");
                sprzwdm = element.elementText("SPRZWDM");
                sprzwmc = element.elementText("SPRZWMC");
                spyj = element.elementText("SPYJ");
                spsj = element.elementText("SPSJ");
                sphjztdm = element.elementText("SPHJZTDM");
                rec_flag = element.elementText("REC_FLAG");
                d_zzjgdm = element.elementText("D_ZZJGDM");
                bz = element.elementText("BZ");
                /*clzt = element.elementText("CLZT");
                clsj = element.elementText("CLSJ");
                byzd = element.elementText("BYZD");
                byzd1 = element.elementText("BYZD1");
                byzd2 = element.elementText("BYZD2");
                byzd3 = element.elementText("BYZD3");
                byzd4 = element.elementText("BYZD4");*/
            }
            if (StringUtil.isNotBlank(flowsn)) {
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if (StringUtil.isNotBlank(auditProject)) {
                    String projectguid = auditProject.getRowguid();
                    AuditOnlineProject auditOnlineProject = daTingService.getAuditOnlineProjectByProjectguid(projectguid);
                    int status = 50;
                    daTingService.updateAuditProjectByProjectguid(projectguid, status, 0);
                    if(StringUtil.isNotBlank(auditOnlineProject)) {
                        auditOnlineProject.setStatus(status+"");
                        auditOnlineProject.setFlowsn(flowsn);
                        auditOnlineProjectService.updateProject(auditOnlineProject);
                    }
                    AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                    auditProjectOperation.setOperatedate(new Date());
                    auditProjectOperation.setProjectGuid(auditProject.getRowguid());
                    auditProjectOperation.setPVIGuid(auditProject.getPviguid());
                    auditProjectOperation.setOperateusername(sprxm);
                    auditProjectOperation.setApplyerName(auditProject.getApplyername());
                    auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                    auditProjectOperation.setOperateType("35");
                    auditProjectOperation.setAreaCode(auditProject.getAreacode());
                    auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                    auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                    auditProjectOperationService.addProjectOperation(auditProjectOperation);
                    log.info("==================更新办件信息成功===============");
                    XmYcslSpinfo xmYcslSpinfo = xmYcslSpinfoService.findXmYcslSpinfoByFlowsn(auditProject.getFlowsn());
                    if(xmYcslSpinfo == null) {
                        xmYcslSpinfo = new XmYcslSpinfo();
                        xmYcslSpinfo.setRowguid(UUID.randomUUID().toString());
                        xmYcslSpinfo.setOperatedate(new Date());
                        xmYcslSpinfo.setProjectguid(auditProject.getRowguid());
                        xmYcslSpinfo.setFlowsn(flowsn);
                        xmYcslSpinfo.setSblsh_short(sblsh_short);
                        xmYcslSpinfo.setSblsh(sblsh);
                        xmYcslSpinfo.setSxbm(sxbm);
                        xmYcslSpinfo.setSxbm_short(sxbm_short);
                        xmYcslSpinfo.setSxqxbm(sxqxbm);
                        xmYcslSpinfo.setSphjdm(sphjdm);
                        xmYcslSpinfo.setSphjmc(sphjmc);
                        xmYcslSpinfo.setSpbmmc(spbmmc);
                        xmYcslSpinfo.setSpbmzzjdmd(spbmzzjdmd);
                        xmYcslSpinfo.setXzqhdm(xzqhdm);
                        xmYcslSpinfo.setSprxm(sprxm);
                        xmYcslSpinfo.setSprzwdm(sprzwdm);
                        xmYcslSpinfo.setSprzwmc(sprzwmc);
                        xmYcslSpinfo.setSpyj(spyj);
                        xmYcslSpinfo.setSpsj(EpointDateUtil.convertString2DateAuto(spsj));
                        xmYcslSpinfo.setSphjztdm(sphjztdm);
                        xmYcslSpinfo.setRec_flag(rec_flag);
                        xmYcslSpinfo.setD_zzjgdm(d_zzjgdm);
                        xmYcslSpinfo.setBz(bz);
                        /*xmYcslSpinfo.setClzt(clzt);
                        xmYcslSpinfo.setClsj(EpointDateUtil.convertString2DateAuto(clsj));
                        xmYcslSpinfo.setByzd(byzd);
                        xmYcslSpinfo.setByzd1(byzd1);
                        xmYcslSpinfo.setByzd2(byzd2);
                        xmYcslSpinfo.setByzd3(byzd3);
                        xmYcslSpinfo.setByzd4(byzd4);*/
                        xmYcslSpinfoService.insert(xmYcslSpinfo);
                    }
                    else {
                        xmYcslSpinfo.setSblsh_short(sblsh_short);
                        xmYcslSpinfo.setSblsh(sblsh);
                        xmYcslSpinfo.setSxbm(sxbm);
                        xmYcslSpinfo.setSxbm_short(sxbm_short);
                        xmYcslSpinfo.setSxqxbm(sxqxbm);
                        xmYcslSpinfo.setSphjdm(sphjdm);
                        xmYcslSpinfo.setSphjmc(sphjmc);
                        xmYcslSpinfo.setSpbmmc(spbmmc);
                        xmYcslSpinfo.setSpbmzzjdmd(spbmzzjdmd);
                        xmYcslSpinfo.setXzqhdm(xzqhdm);
                        xmYcslSpinfo.setSprxm(sprxm);
                        xmYcslSpinfo.setSprzwdm(sprzwdm);
                        xmYcslSpinfo.setSprzwmc(sprzwmc);
                        xmYcslSpinfo.setSpyj(spyj);
                        xmYcslSpinfo.setSpsj(EpointDateUtil.convertString2DateAuto(spsj));
                        xmYcslSpinfo.setSphjztdm(sphjztdm);
                        xmYcslSpinfo.setRec_flag(rec_flag);
                        xmYcslSpinfo.setD_zzjgdm(d_zzjgdm);
                        xmYcslSpinfo.setBz(bz);
                        /*xmYcslSpinfo.setClzt(clzt);
                        xmYcslSpinfo.setClsj(EpointDateUtil.convertString2DateAuto(clsj));
                        xmYcslSpinfo.setByzd(byzd);
                        xmYcslSpinfo.setByzd1(byzd1);
                        xmYcslSpinfo.setByzd2(byzd2);
                        xmYcslSpinfo.setByzd3(byzd3);
                        xmYcslSpinfo.setByzd4(byzd4);*/
                        xmYcslSpinfoService.update(xmYcslSpinfo);
                    }
                }
                return generateXmlReturn("200", "请求成功");
            }
            else {
                log.info("==========结束调用receiveCourseInfoSsdj接口=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return generateXmlReturn("300", "调用接口失败");
            }
        }
        catch (

        Exception e) {
            log.info("==========结束调用receiveCourseInfoSsdj接口==========");
            return generateXmlReturn("300", "调用接口失败");
        }
    }

    /**
     * 接收业务系统特别程序申请和结束信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/getSuspendInfoSsdj", method = {RequestMethod.POST })
    public String getSuspendInfoSsdj(HttpServletRequest params) throws Exception {
    	 log.info("==========开始调用getSuspendInfoSsdj接口=========="
                 + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
         try {
                 log.info("==========params==========" + params);
                 String accessToken = params.getParameter("accessToken");
                 String xmlstrold = params.getParameter("xmlStr");
                 String xmlStr = "";
                 if (xmlstrold.contains("version")) {
                 	xmlStr = xmlstrold.substring(38);
                 }else {
                 	return generateXmlReturn("300", "请求入参的XmlStr不正确");
                 }
                 String sysAccessToken = configServicce.getFrameConfigValue("rsjAccessToken");
                // 暂时先不校验accessToken
                 if (StringUtil.isBlank(accessToken)) {
                     return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
                 }
                 if (!accessToken.equals(sysAccessToken)) {
                     return generateXmlReturn("300", "accessToken参数不正确");
                 }
                 if (StringUtil.isBlank(xmlStr)) {
                     return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr");
                 }

                 Document document = DocumentHelper.parseText(xmlStr);
                 Element rootElement = document.getRootElement();
                 String flowsn = rootElement.elementText("SBLSH_SHORT"); // 申办流水号 
                 String itemid = rootElement.elementText("SXBM"); // 事项编码
                 String subspendtype = rootElement.elementText("SUSPENDTYPE"); // 特别程序类型： 0：特别程序申请；1：特别程序结果
                 
                 String  splshShort = "";//申办流水号 
                 String sblsh = "";//备用流水号
                 String sxbm = "";//事项编码
                 String sxbm_short = "";//事项简码
                 String sxqxbm = "";//事项情形编码
                 String xh = "";//序号
                 String tbcxzl = "";//特别程序种类
                 String tbcxzlmc = "";//特别程序种类名
                 String tbcxksrq = "";//特别程序开始日期
                 String tbcxpzr = "";//特别程序批准人
                 String tbcxqdly = "";//特别程序启动理 由或依据
                 String sqnr = "";//申请内容
                 String tbcxsx = "";//特别程序时限
                 String tbcxsxdw = "";//特别程序时限单 位
                 String xzqhdm = "";//特别程序申请部 门所在地行政区 划代码
                 String bz  = "";//备注
                 String clzt = "";//处理状态
                 String clsj = "";//处理时间
                 String byzd = "";//备用字段
                 String byzd1 = "";//备用字段
                 String byzd2 = "";//备用字段
                 String byzd3 = "";//备用字段
                 String byzd4 = "";//备用字段
                 String tbcxjg = "";//特别程序结果
                 String jgcsrq = "";//结果产生日期
                 String tbcxjsrq = "";//特别程序结束日期
                 String tbcxsfje = "";//特别程序收费金额
                 String jedwdm = "";//金额单位代码
                 List<Element> childNodes = rootElement.elements("SPTEBIESHENQING");
                 for (Element element : childNodes) {
 					 splshShort = element.elementText("SBLSH_SHORT");//申办流水号 
 					 sblsh = element.elementText("SBLSH");//备用流水号
 					 sxbm = element.elementText("SXBM");//事项编码
 					 sxbm_short = element.elementText("SXBM_SHORT");//事项简码
 					 sxqxbm = element.elementText("SXQXBM");//事项简码
 					 xh = element.elementText("XH");//序号
 					 tbcxzl = element.elementText("TBCXZL");//特别程序种类
 					 tbcxzlmc = element.elementText("TBCXZLMC");//特别程序种类名称
 					 tbcxksrq = element.elementText("TBCXKSRQ");//特别程序开始日期
 					 tbcxpzr = element.elementText("TBCXPZR");//特别程序批准人
 					 tbcxqdly = element.elementText("TBCXQDLY");//特别程序启动理 由或依据
 					 sqnr = element.elementText("SQNR");//申请内容
 					 tbcxsx = element.elementText("TBCXSX");//特别程序时限
 					 tbcxsxdw = element.elementText("TBCXSXDW");//特别程序时限单位
 					 xzqhdm = element.elementText("XZQHDM");//特别程序申请部 门所在地行政区 划代码
 					 bz = element.elementText("BZ");//备注
 					 clzt = element.elementText("CLZT");//处理状态
 					 clsj = element.elementText("CLSJ");//处理时间
 					 byzd = element.elementText("BYZD");//备用字段
 					 byzd1 = element.elementText("BYZD1");//处理时间
 					 byzd2 = element.elementText("BYZD2");//处理时间
 					 byzd3 = element.elementText("BYZD3");//处理时间
 					 byzd4 = element.elementText("BYZD4");//处理时间
 					 SpecialApply apply = new SpecialApply();
 					 apply.setRowguid(UUID.randomUUID().toString());
 					 apply.setSuspendtype(subspendtype);
 					 apply.setSblsh_short(splshShort);
 					 apply.setSblsh(sblsh);
 					 apply.setSxbm(sxbm_short);
 					 apply.setSxbm_short(sxbm_short);
 					 apply.setSxqxbm(sxqxbm);
 					 apply.setXh(xh);
 					 apply.setTbcxzl(tbcxzl);
 					 apply.setTbcxzlmc(tbcxzlmc);
 					 apply.setTbcxksrq(tbcxksrq);
 					 apply.setTbcxpzr(tbcxpzr);
 					 apply.setTbcxqdly(tbcxqdly);
 					 apply.setSqnr(sqnr);
 					 apply.setTbcxsx(tbcxsx);
 					 apply.setTbcxsxdw(tbcxsxdw);
 					 apply.setXzqhdm(xzqhdm);
 					 apply.setBz(bz);
 					 apply.setClzt(clzt);
 					 apply.setClsj(clsj);
 					 apply.setByzd(byzd);
 					 apply.setByzd1(byzd1);
 					 apply.setByzd2(byzd2);
 					 apply.setByzd3(byzd3);
 					 apply.setByzd4(byzd4);
 					 applyService.insert(apply);
 					 
 				}
                 List<Element> spresult = rootElement.elements("SPTEBIEJIEGUO");
                 for (Element element : spresult) {
                 	 splshShort = element.elementText("SBLSH_SHORT");//申办流水号 
                 	 sblsh = element.elementText("SBLSH");//备用流水号
 					 sxbm = element.elementText("SXBM");//事项编码
 					 sxbm_short = element.elementText("SXBM_SHORT");//事项简码
 					 sxqxbm = element.elementText("SXQXBM");//事项简码
 					 xh = element.elementText("XH");//序号
 					 tbcxjg =  element.elementText("TBCXJG");//特别程序结果
 					 jgcsrq = element.elementText("JGCSRQ");//结果产生日期 
 					 tbcxjsrq = element.elementText("TBCXJSRQ");//特别程序结束日期
 					 tbcxsfje  = element.elementText("TBCXSFJE");//特别程序收费金额
 					 jedwdm = element.elementText("JEDWDM");//金额单位代码
 					 xzqhdm = element.elementText("XZQHDM");//特别程序申请部 门所在地行政区 划代码
 					 bz = element.elementText("BZ");//备注
 					 clzt = element.elementText("CLZT");//处理状态
 					 clsj = element.elementText("CLSJ");//处理时间
 					 byzd = element.elementText("BYZD");//备用字段
 					 byzd1 = element.elementText("BYZD1");//处理时间
 					 byzd2 = element.elementText("BYZD2");//处理时间
 					 byzd3 = element.elementText("BYZD3");//处理时间
 					 byzd4 = element.elementText("BYZD4");//处理时间
 					 SpecialResult result = new SpecialResult();
 					 
 					 result.setRowguid(UUID.randomUUID().toString());
 					 result.setSuspendtype(subspendtype);
 					 result.setSblsh_short(splshShort);
 					 result.setSblsh(sblsh);
 					 result.setSxbm(sxbm);
 					 result.setSxbm_short(sxbm_short);
 					 result.setSxqxbm(sxqxbm);
 					 result.setXh(xh);
 					 result.setTbcxjg(tbcxjg);
 					 result.setJgcsrq(jgcsrq);
 					 result.setTbcxjsrq(tbcxjsrq);
 					 result.setTbcxsfje(tbcxsfje);
 					 result.setJedwdm(jedwdm);
 					 result.setXzqhdm(xzqhdm);
 					 result.setBz(bz);
 					 result.setClzt(clzt);
 					 result.setClsj(clsj);
 					 result.setByzd(byzd);
 					 result.setByzd1(byzd1);
 					 result.setByzd2(byzd2);
 					 result.setByzd3(byzd3);
 					 result.setByzd4(byzd4);
 					 resultService.insert(result);
 					
 					 
 				}
                 return generateXmlReturn("200", "请求成功");
         }
         catch (Exception e) {
             log.info("==========开始调用getSuspendInfoSsdj接口=========="
                     + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
             return generateXmlReturn("300", "调用接口失败");
         }
    }

    /**
     * 接收业务系统补正告知信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/receiveCorrectionSsdj", method = {RequestMethod.POST })
    public String receiveCorrectionSsdj(HttpServletRequest params) throws Exception {
        log.info("==========开始调用receiveCorrectionSsdj接口=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            log.info("==========params==========" + params);
            String accessToken = params.getParameter("accessToken");
            String xmlstrold = params.getParameter("xmlStr");
            String xmlStr = "";
            if (xmlstrold.contains("version")) {
            	xmlStr = xmlstrold.substring(38);
            }else {
            	return generateXmlReturn("300", "请求入参的XmlStr不正确");
            }
            String sysAccessToken = configServicce.getFrameConfigValue("rsjAccessToken");
            //暂时先不校验accessToken
            if (StringUtil.isBlank(accessToken)) {
                return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
            }
            if (!accessToken.equals(sysAccessToken)) {
                return generateXmlReturn("300", "accessToken参数不正确");
            }
            if (StringUtil.isBlank(xmlStr)) {
                return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr");
            }

            Document document = DocumentHelper.parseText(xmlStr);
            Element rootElement = document.getRootElement();
            String flowsn = rootElement.elementText("SBLSH_SHORT"); // 申办流水号
            String newItemCode = rootElement.elementText("SXBM"); // 事项编码  SPBUZHENGGAOZHI      
            String bzgzfcrxm = "";  
            String bzgzyy = "";       
            String bzclqd = "";       
            String bzgzsx = "";       
            String bzgzsxdw = "";     
            String bqbzclbm = "";     
            String bzgzsj = "";       
            String xzqhdm = "";       
            String bz = "";           
            String project_code = ""; 
            String d_zzjgdm = "";     
            List<Element> childNodes = rootElement.elements("SPBUZHENGGAOZHI");
            for (Element element : childNodes) {
                bzgzfcrxm = element.elementText("BZGZFCRXM");//补正告知发出人姓名                               
                bzgzyy = element.elementText("BZGZYY");//补正告知原因                                               
                bzclqd = element.elementText("BZCLQD");//补正材料清单                                               
                bzgzsx = element.elementText("BZGZSX");//补正告知时限                                               
                bzgzsxdw = element.elementText("BZGZSXDW");//补正告知时限单位                                       
                bqbzclbm = element.elementText("BQBZCLBM");//补正告知材料编码 市统一权责清单材料 编码 以半角分号隔 开
                bzgzsj = element.elementText("BZGZSJ");//补正告知时间                                               
                xzqhdm = element.elementText("XZQHDM");//补正告知部门所在地行政区划代码                             
                bz = element.elementText("BZ");//备注                                                               
                project_code = element.elementText("PROJECT_CODE");//投资项目统一编码                               
                d_zzjgdm = element.elementText("D_ZZJGDM");//数据提供部门组织机构代码/社会信用代码                  
            }
            if(StringUtil.isNotBlank(flowsn)) {
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if(StringUtil.isNotBlank(auditProject)) {
                    log.info("========更新办件状态为补正========");
                    String projectGuid = auditProject.getRowguid();
                    AuditOnlineProject auditOnlineProject = daTingService.getAuditOnlineProjectByProjectguid(projectGuid);
                    int status=28;
                    auditProject.setBubandate(new Date());
                    auditProjectService.updateProject(auditProject);              
                    daTingService.updateAuditProjectByProjectguid(projectGuid, status, 0);
                    
                    if(StringUtil.isNotBlank(auditOnlineProject)) {
                        auditOnlineProject.setStatus(status+"");
                        auditOnlineProject.setFlowsn(flowsn);
                        auditOnlineProjectService.updateProject(auditOnlineProject);
                    }
                    log.info("========更新办件状态为补正更新成功========");
                    String[] materialguids = bqbzclbm.split(";");
                    log.info("========更新办件材料的补正状态=========");
                    for (String materialguid : materialguids) {
                        AuditProjectMaterial auditProjectMaterial = auditProjectMaterialService.selectProjectMaterialByMaterialGuid(projectGuid, materialguid).getResult();
                        if(StringUtil.isNotBlank(auditProjectMaterial)) {
                            auditProjectMaterial.setAuditstatus("30");
                            auditProjectMaterial.setIsbuzheng("1");
                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                        }
                    }
                    log.info("========更新办件材料的补正状态成功=========");
                    log.info("========插入办件审批操作表=========");
                    AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                    auditProjectOperation.setProjectGuid(projectGuid);
                    auditProjectOperation.setPVIGuid(auditProject.getPviguid());
                    auditProjectOperation.setOperateusername(bzgzfcrxm);
                    auditProjectOperation.setApplyerName(auditProject.getApplyername());
                    auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                    auditProjectOperation.setOperateType("28");
                    auditProjectOperation.setAreaCode(auditProject.getAreacode());
                    auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                    auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                    auditProjectOperation.setOperatedate(new Date());
                    auditProjectOperationService.addProjectOperation(auditProjectOperation);
                    log.info("========插入办件审批操作表=========");
                    //获取人社申办补正信息表信息
                    XmRsjBuzheng rsbzRecord = xmRsjBuzhengService.findXmRsjBuzhengByflowsn(flowsn);
                    Boolean isInsert= true;
                    if(StringUtil.isNotBlank(rsbzRecord) && StringUtil.isNotBlank(rsbzRecord.getFlowsn())) {
                        isInsert = false;
                        rsbzRecord.setFlowsn(flowsn);
                    }
                    else {
                        rsbzRecord = new XmRsjBuzheng();
                        rsbzRecord.setRowguid(UUID.randomUUID().toString());
                        rsbzRecord.setFlowsn(flowsn);
                    }
                    if(StringUtil.isNotBlank(bzgzfcrxm)) {
                        rsbzRecord.setBzgzfcrxm(bzgzfcrxm);
                    }
                    if(StringUtil.isNotBlank(bzgzyy)) {
                        rsbzRecord.setBzgzyy(bzgzyy);      
                    }
                    if(StringUtil.isNotBlank(bzclqd)) {
                        rsbzRecord.setBzclqd(bzclqd);
                    }
                    if(StringUtil.isNotBlank(bzgzsx)) {
                        rsbzRecord.setBzgzsx(Integer.parseInt(bzgzsx));
                    }
                    if(StringUtil.isNotBlank(bzgzsxdw)) {
                        rsbzRecord.setBzgzsxdw(bzgzsxdw);
                    }
                    if(StringUtil.isNotBlank(bqbzclbm)) {
                        rsbzRecord.setBqbzclbm(bqbzclbm);
                    }
                    if(StringUtil.isNotBlank(bzgzsj)) {
                        rsbzRecord.setBzgzsj(EpointDateUtil.convertString2DateAuto(bzgzsj));
                    }
                    if(StringUtil.isNotBlank(xzqhdm)) {
                        rsbzRecord.setXzqhdm(xzqhdm);
                    }
                    if(StringUtil.isNotBlank(bz)) {
                        rsbzRecord.setBz(bz);
                    }
                    if(StringUtil.isNotBlank(project_code)) {
                        rsbzRecord.setProject_code(project_code);
                    }
                    if(StringUtil.isNotBlank(d_zzjgdm)) {
                        rsbzRecord.setD_zzjgdm(d_zzjgdm);
                    }
                    if(isInsert) {
                        xmRsjBuzhengService.insert(rsbzRecord);
                    }
                    else {
                        xmRsjBuzhengService.update(rsbzRecord);
                    }
                    return generateXmlReturn("200", "请求成功");
                }
                else {
                    return generateXmlReturn("300", "根据申办流水号未能查询到办件信息");
                }
            }
            else {
               return generateXmlReturn("300", "未获取到申办流水号");
            }
        }
        catch (

        Exception e) {
            log.info("==========结束调用receiveCorrectionSsdj接口=========="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            return generateXmlReturn("300", "调用接口失败");
        }
    }

    /**
     *接收部门审批结果文件
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/receiveBusinessApproveResultSsdj", method = {RequestMethod.POST })
    public String receiveBusinessApproveResultSsdj(HttpServletRequest params) throws Exception {
        log.info("==========开始调用receiveBusinessApproveResultSsdj接口=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
                log.info("==========params==========" + params);
                String accessToken = params.getParameter("accessToken");
                String xmlstrold = params.getParameter("xmlStr");
                String xmlStr = "";
                if (xmlstrold.contains("version")) {
                	xmlStr = xmlstrold.substring(38);
                }else {
                	return generateXmlReturn("300", "请求入参的XmlStr不正确");
                }
                String sysAccessToken = configServicce.getFrameConfigValue("rsjAccessToken");
                //暂时先不校验accessToken
                if (StringUtil.isBlank(accessToken)) {
                    return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
                }
                if (!accessToken.equals(sysAccessToken)) {
                    return generateXmlReturn("300", "accessToken参数不正确");
                }
                if (StringUtil.isBlank(xmlStr)) {
                    return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr");
                }

                Document document = DocumentHelper.parseText(xmlStr);
                Element rootElement = document.getRootElement();
                String flowsn = rootElement.elementText("SBLSH_SHORT"); // 申办流水号
                String newItemCode = rootElement.elementText("SXBM");
                String seq_id = "";     
                String sblsh_short = "";
                String zzbm = "";       
                String zzmc = "";       
                String attach_name = "";
                String attach_id = "";  
                String attach_sign = "";
                String attach_type = "";
                String save_type = "";  
                String attach_body = "";
                String attach_path = "";
                String slbmzzjgdm = ""; 
                String xzqhdm = "";
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if(StringUtil.isNotBlank(auditProject)) {    
                    List<Element> childNodes = rootElement.elements("SPSHENPIJIEGUODATA");
                    for (Element element : childNodes) {
                        List<Element> childrenNodes = element.elements("SPSHENPIJIEGUO");
                        for (Element element2 : childrenNodes) {
                            seq_id = element2.elementText("SEQ_ID");
                            sblsh_short = element2.elementText("SBLSH_SHORT");
                            zzbm = element2.elementText("ZZBM");
                            zzmc = element2.elementText("ZZMC");
                            attach_name = element2.elementText("ATTACH_NAME");
                            attach_id = element2.elementText("ATTACH_ID");
                            attach_sign = element2.elementText("ATTACH_SIGN");
                            attach_type = element2.elementText("ATTACH_TYPE");
                            save_type = element2.elementText("SAVE_TYPE");
                            attach_body = element2.elementText("ATTACH_BODY");
                            attach_path = element2.elementText("ATTACH_PATH");
                            slbmzzjgdm = element2.elementText("SLBMZZJGDM");
                            xzqhdm = element2.elementText("XZQHDM");
                            FrameAttachInfo frameInfo = attachService.getAttachInfoDetail(attach_id);
                            if(StringUtil.isNotBlank(frameInfo)) {
                                frameInfo.setCliengGuid(auditProject.getRowguid());
                                attachService.updateAttach(frameInfo, null);
                            }
                        }
                    }
                }
                else {
                    return generateXmlReturn("300", "通过申报流水号未查询到办件"); 
                }
                return generateXmlReturn("200", "请求成功");
        }
        catch (Exception e) {
            log.info("==========结束调用receiveBusinessApproveResultSsdj接口=========="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            return generateXmlReturn("300", "调用接口失败");
        }
    }
    
    /**
     * 接收部门业务办结信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/receiveBusinessFinishSsdj", method = {RequestMethod.POST })
    public String receiveBusinessFinishSsdj(HttpServletRequest params) throws Exception {
        log.info("==========开始调用receiveBusinessFinishSsdj接口=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("========params========" + params);
        try {
            String accessToken = params.getParameter("accessToken");
            String xmlstrold = params.getParameter("xmlStr");
            String xmlStr = "";
            if (xmlstrold.contains("version")) {
            	xmlStr = xmlstrold.substring(38);
            }else {
            	return generateXmlReturn("300", "请求入参的XmlStr不正确");
            }
            String sysAccessToken = configServicce.getFrameConfigValue("rsjAccessToken");
            //暂时先不校验accessToken
            if (StringUtil.isBlank(accessToken)) {
                return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
            }
            if (!accessToken.equals(sysAccessToken)) {
                return generateXmlReturn("300", "accessToken参数不正确");
            }
            if (StringUtil.isBlank(xmlStr)) {
                return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr！");
            }
            // 申办流水号
            String flowsn = "";
            // 事项编码
            String newItemCode = "";
            // 申办流水号
            String sblsh_short = "";
            // 备用流水号
            String sblsh = "";
            // 事项简码
            String sxbm_short = "";
            // 事项编码
            String sxbm = "";
            // 事项情形编码
            String sxqxbm = "";
            // 办结部门名称
            String bjbmmc = "";
            // 办结部门组织机构代码
            String bjbmzzjddm = "";
            // 办结部门行政区划代码
            String xzqhdm = "";
            // 审批人姓名
            String sprxm = "";
            // 审批人代码
            String sprdm = "";
            // 办结结果代码
            String bjjgdm = "";
            // 办结结果描述
            String bjjgms = "";
            // 作废或退回原因
            String zfhthyy = "";
            // 证件/盖章名称
            String zjgzmc = "";
            // 证件编号
            String zjbh = "";
            // 证件有效期限
            String zjyxqx = "";
            // 发证/盖章单位
            String fzgzdw = "";
            // 收费金额
            String sfje = "";
            // 金额单位代码
            String jedwdm = "";
            // 办结时间
            String bjsj = "";
            // 备注
            String bz = "";
            // 备用字段
            String byzd = "";

            Document document = DocumentHelper.parseText(xmlStr);
            Element rootElement = document.getRootElement();
            flowsn = rootElement.elementText("SBLSH_SHORT");
            newItemCode = rootElement.elementText("SXBM");
            List<Element> childNodes = rootElement.elements("SPBANJIE");
            for (Element element : childNodes) {
                sblsh_short = element.elementText("SBLSH_SHORT");
                sblsh = element.elementText("SBLSH");
                sxbm_short = element.elementText("SXBM_SHORT");
                sxbm = element.elementText("SXBM");
                sxqxbm = element.elementText("SXQXBM");
                bjbmmc = element.elementText("BJBMMC");
                bjbmzzjddm = element.elementText("BJBMZZJDDM");
                xzqhdm = element.elementText("XZQHDM");
                sprxm = element.elementText("SPRXM");
                sprdm = element.elementText("SPRDM");
                bjjgdm = element.elementText("BJJGDM");//办件结果代码
                bjjgms = element.elementText("BJJGMS");//办件结果描述
                zfhthyy = element.elementText("ZFHTHYY");//退回原因
                zjgzmc = element.elementText("ZJGZMC");//办件结果 12345必填
                zjbh = element.elementText("ZJBH");
                zjyxqx = element.elementText("ZJYXQX");
                fzgzdw = element.elementText("FZGZDW");
                sfje = element.elementText("SFJE");
                jedwdm = element.elementText("JEDWDM");
                bjsj = element.elementText("BJSJ");
                bz = element.elementText("BZ");
                byzd = element.elementText("BYZD");
            }
            Date banjieDate = EpointDateUtil.convertString2DateAuto(bjsj);
            if (StringUtil.isNotBlank(flowsn)) {
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if (StringUtil.isNotBlank(auditProject)) {
                    String projectguid = auditProject.getRowguid();
                    AuditOnlineProject  auditOnlineProject =daTingService.getAuditOnlineProjectByProjectguid(projectguid);
                    auditProject.setBanjiedate(banjieDate);
                    auditProject.setBanjieareacode(xzqhdm);
                    auditProject.setBanjieuserguid(sprdm);
                    auditProject.setBanjieusername(sprxm);
                    int status = 0;
                    int banJieResult = 0;
                    String onlineStatus ="90";
                    switch (bjjgdm) {
                        case "0":
                            status = 90;
                            banJieResult = 40;
                            break;
                        case "6":
                            status = 90;
                            banJieResult = 40;
                            break;
                        default:
                            status = 99;
                            onlineStatus= "99";
                            banJieResult = 50;
                            break;
                    }
                    if(status == 99) {
                        AuditProjectUnusual auditProjectUnusual = new AuditProjectUnusual();
                        auditProjectUnusual.setNote(zfhthyy);
                        auditProjectUnusual.setProjectguid(auditProject.getRowguid());
                        auditProjectUnusual.setPviguid(auditProject.getPviguid());
                        auditProjectUnusual.setRowguid(UUID.randomUUID().toString());
                        auditProjectUnusual.setOperatedate(new Date());
                        auditProjectUnusual.setOperateusername("人社返回");
                        auditProjectUnusualService.addProjectUnusual(auditProjectUnusual);
                        
                    }
                    log.info("申报流水号是："+flowsn+"，办件状态时："+status+",办结状态是："+banJieResult);

                    //查看有无centerguid
                    if(StringUtils.isBlank(auditProject.getCenterguid())){
                        AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
                        if(auditOrgaServiceCenter!=null){
                            log.info("centerguid:"+auditOrgaServiceCenter.getRowguid());
                            if("undefined".equals(auditOrgaServiceCenter.getRowguid())){
                                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "中心标记有问题", "");
                            }
                            auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
                        }
                    }
                    //更新承诺办结时间
                    AuditTask  auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
                    if(auditTask!=null) {
                        List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
                        Date acceptdat = auditProject.getAcceptuserdate();
                        Date shouldEndDate;
                        if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                            IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                                    .getComponent(IAuditOrgaWorkingDay.class);
                            shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                                    auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
                            log.info("shouldEndDate:"+shouldEndDate);
                        } else {
                            shouldEndDate = null;
                        }
                        if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                            Duration totalDuration = Duration.ZERO;  // 用于累加时间差（以秒为单位）
                            LocalDateTime currentTime = null;
                            for(AuditProjectUnusual auditProjectUnusual:auditProjectUnusuals) {
                                // 将Date转换为Instant
                                Instant instant = auditProjectUnusual.getOperatedate().toInstant();
                                if(10==auditProjectUnusual.getOperatetype()){
                                    // 通过Instant和系统默认时区获取LocalDateTime
                                    currentTime= LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                                }
                                if(currentTime!=null && 11==auditProjectUnusual.getOperatetype()){
                                    LocalDateTime nextTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                                    Duration danci = Duration.between(currentTime, nextTime);
                                    totalDuration = totalDuration.plus(danci);
                                    currentTime = null;
                                }
                            }
                            // 将累加的时间差加到初始的Date类型的shouldEndDate上
                            Instant instant = shouldEndDate.toInstant();
                            Instant newInstant = instant.plus(totalDuration);
                            shouldEndDate = Date.from(newInstant);
                            log.info("shouldEndDate:"+shouldEndDate);
                        }
                        if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
    auditProject.setPromiseenddate(shouldEndDate);
}
                    }

                    auditProjectService.updateProject(auditProject);
                    daTingService.updateAuditProjectByProjectguid(projectguid, status, banJieResult);
                    if(StringUtil.isNotBlank(auditOnlineProject)) {
                        log.info("====有auditOnlineProject实体====");
                        auditOnlineProject.setStatus(status+"");
                        auditOnlineProject.setFlowsn(flowsn);
                        auditOnlineProjectService.updateProject(auditOnlineProject);
                    }
                    AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                    auditProjectOperation.setOperatedate(new Date());
                    auditProjectOperation.setProjectGuid(auditProject.getRowguid());
                    auditProjectOperation.setPVIGuid(auditProject.getPviguid());
                    auditProjectOperation.setOperateusername(sprxm);
                    auditProjectOperation.setApplyerName(auditProject.getApplyername());
                    auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                    auditProjectOperation.setOperateType("60");
                    auditProjectOperation.setAreaCode(auditProject.getAreacode());
                    auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                    auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                    auditProjectOperationService.addProjectOperation(auditProjectOperation);
                    log.info("========更新办件情况成功========");
                    XmYyyzBjinfo xmYyyzBjinfo = xmYyyzBjinfoService.findXmYyyzBjinfoByFlowsn(auditProject.getFlowsn());
                    if(xmYyyzBjinfo == null) {
                        xmYyyzBjinfo = new XmYyyzBjinfo();
                        xmYyyzBjinfo.setRowguid(UUID.randomUUID().toString());
                        xmYyyzBjinfo.setOperatedate(new Date());
                        xmYyyzBjinfo.setFlowsn(flowsn);
                        xmYyyzBjinfo.setProjectguid(auditProject.getRowguid());
                        xmYyyzBjinfo.setBjbmzzjddm(bjbmzzjddm);
                        xmYyyzBjinfo.setBjjgms(bjjgms);
                        xmYyyzBjinfo.setZfhthyy(zfhthyy);
                        xmYyyzBjinfo.setZjgzmc(zjgzmc);
                        xmYyyzBjinfo.setZjbh(zjbh);
                        xmYyyzBjinfo.setZjyxqx(zjyxqx);
                        xmYyyzBjinfo.setFzgzdw(fzgzdw);
                        if(StringUtil.isNotBlank(sfje)) {
                            xmYyyzBjinfo.setSfje(Double.parseDouble(sfje));
                        }
                        xmYyyzBjinfo.setBz(bz);
                        xmYyyzBjinfo.setJedwdm(jedwdm);
                        xmYyyzBjinfo.setByzd(byzd);
                        xmYyyzBjinfoService.insert(xmYyyzBjinfo);
                    }
                    else {
                        xmYyyzBjinfo.setOperatedate(new Date());
                        xmYyyzBjinfo.setBjbmzzjddm(bjbmzzjddm);
                        xmYyyzBjinfo.setBjjgms(bjjgms);
                        xmYyyzBjinfo.setZfhthyy(zfhthyy);
                        xmYyyzBjinfo.setZjgzmc(zjgzmc);
                        xmYyyzBjinfo.setZjbh(zjbh);
                        xmYyyzBjinfo.setZjyxqx(zjyxqx);
                        xmYyyzBjinfo.setFzgzdw(fzgzdw);
                        if(StringUtil.isNotBlank(sfje)) {
                            xmYyyzBjinfo.setSfje(Double.parseDouble(sfje));
                        }
                        xmYyyzBjinfo.setBz(bz);
                        xmYyyzBjinfo.setJedwdm(jedwdm);
                        xmYyyzBjinfo.setByzd(byzd);
                        xmYyyzBjinfoService.update(xmYyyzBjinfo);
                    }
                    log.info("==========办结表数据已获取==========");
                    String xmlReturn = generateXmlReturn("200", "请求成功");

                    log.info("==========xmlReturn==========" + xmlReturn);
                    log.info("==========结束调用receiveBusinessFinishSsdj接口=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return xmlReturn;
                }
                else {
                    log.info("==========结束调用receiveBusinessFinishSsdj接口=========="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return generateXmlReturn("300", "获取办件信息失败");
                }
            }
            else {
                log.info("==========结束调用receiveBusinessFinishSsdj接口=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return generateXmlReturn("300", "获取办件流水号失败");
            }
        }
        catch (Exception e) {
            log.info("==========结束调用receiveBusinessFinishSsdj接口=========="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            return generateXmlReturn("300", "调用接口失败");
        }
    }

    /**
     * 错误返回值
     * @description
     * @author shibin
     * @date  2020年6月30日 下午2:26:38
     */
    private String generateXmlReturn(String status, String description) {

        String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT);
        StringBuffer flowsnXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        flowsnXml.append("<RESULT>");
        flowsnXml.append("<STATUS>" + status + "</STATUS>");
        flowsnXml.append("<DESC>" + description + "</DESC>");
        flowsnXml.append("<TIME>" + date + "</TIME>");
        flowsnXml.append("<DATA></DATA>");
        flowsnXml.append("</RESULT>");
        return flowsnXml.toString();
    }

    /**
     * 请求方法
     * @description
     * @author shibin
     * @date  2020年7月17日 下午2:08:20
     */
    
    private String sendPostRequest(String accesstoken, String xmlStr) {
        PostMethod postMethod = null;
        String applyApi = configService.getFrameConfigValue("rsapplyapi");
        postMethod = new PostMethod(APPLYURL);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        postMethod.setRequestHeader("ApiKey", applyApi);
        //参数设置，需要注意的就是里边不能传NULL，要传空字符串
        NameValuePair[] data = {new NameValuePair("xmlStr", xmlStr), new NameValuePair("accessToken", accesstoken)};
        postMethod.setRequestBody(data);
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        String result = null;
        try {
            int response = httpClient.executeMethod(postMethod);
            result = postMethod.getResponseBodyAsString();
            log.info("推送办件流水号返回结果为："+result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 请求方法获取accessToken
     * @description
     * @author shibin
     * @date  2020年7月17日 下午2:08:20
     */
    public String getAccessToken() {
        PostMethod postMethod = null;
        postMethod = new PostMethod(TOKENURL);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        postMethod.setRequestHeader("ApiKey", API);
        //参数设置，需要注意的就是里边不能传NULL，要传空字符串
        NameValuePair[] data = {new NameValuePair("key", KEY), new NameValuePair("secret", SECRET) };
        postMethod.setRequestBody(data);
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        String result = null;
        String accessToken = "";
        try {
        int response = httpClient.executeMethod(postMethod);
        result = postMethod.getResponseBodyAsString();
        JSONObject paramObj = JSONObject.parseObject(result);
        String code = paramObj.getString("code");
        log.info("人社返回token结果为:"+paramObj);
        if ("200".equals(code)) {
        	JSONObject accessTokenData = paramObj.getJSONObject("data");
        	if ("200".equals(accessTokenData.getString("code"))) {
        		String accessTokenData1 = accessTokenData.getString("data");
        		 if (StringUtil.isNotBlank(accessTokenData)) {
                     Document document = DocumentHelper.parseText(accessTokenData1);
                     Element rootElement = document.getRootElement();
                     accessToken = rootElement.elementText("TOKEN"); // 申办流水号
                 }
        	}
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }
    /**
     * @param urlAll
     *            :请求接口
     * @param httpArg
     *            :参数
     * @return 返回结果
     */
    public static String request(String httpUrl, String httpArg, String ApiKey) {

        /*httpUrl = "http://59.206.202.180:443/gateway/api/1/barcode";
        httpArg = "code=8888888";
        String jsonResult = request(httpUrl, httpArg, ApiKey);
        System.out.println(jsonResult);*/

        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?ApiKey="+ ApiKey;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 填入api的请求方法
            connection.setRequestMethod("GET");
            HttpUtil.doGet(httpUrl);
            // 填入appkey到HTTP header
            connection.setRequestProperty("ApiKey", ApiKey);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过身份证号码获取出生日期、性别、年龄
     * @param certificateNo
     * @return 返回的出生日期格式：1990-01-01   性别格式：F-女，M-男
     */
    public static Map<String, String> getBirAgeSex(String certificateNo) {
        String birthday = "";
        String age = "";
        String sexCode = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag)
                    return new HashMap<String, String>();
                flag = Character.isDigit(number[x]);
            }
        }
        else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag)
                    return new HashMap<String, String>();
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-" + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3, certificateNo.length()))
                    % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        }
        else if (flag && certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-" + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1))
                    % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("birthday", birthday);
        map.put("age", age);
        map.put("sexCode", sexCode);
        return map;
    }

    /**
     * 
     *  [根据电子表单id和办件标识获取电子表单数据] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
//    public String getFormElement(String projectGuid,String formId) {
//        try {
//            JSONObject paramsJson = new JSONObject();
//            JSONObject dataJson = new JSONObject();
//            paramsJson.put("rowGuid", projectGuid);
//            paramsJson.put("formId", formId);
//    //        paramsJson.put("rowGuid", "daa098f4-8f02-4a51-850c-e911120f8f9f");
//    //        paramsJson.put("formId", "279");
//            paramsJson.put("tableName", "");
//            Map<String, String> paramsMap = new HashMap<String, String>();
//            //获取办件电子表单数据
//            paramsMap.put("params", paramsJson.toJSONString());
//            paramsMap.put("sessionGuid", "");
//            //String epointUrl = "http://117.73.252.120:8088/epoint-zwfwsform-web";
//            String epointUrl = iConfigService.getFrameConfigValue("dzbdurl");
//            String formDataStr="";
//            formDataStr = TARequestUtil.getResult(epointUrl+"/rest/sform/getPageData", paramsMap);
//            if(StringUtil.isNotBlank(formDataStr)) {
//                log.info("===成功获取电子表单数据formDataStr==="+formDataStr);
//                List<JSONObject> mainList = (List<JSONObject>) JSON.parseObject(formDataStr).getJSONObject("custom").getJSONObject("recordData").get("mainList");
//                List<JSONObject> rowList = new ArrayList<>();
//                if(!mainList.isEmpty()) {
//                	JSONObject main = mainList.get(0);
//                    rowList = (List<JSONObject>) main.get("rowList");
//                    StringBuilder stringBuilder = new StringBuilder();
//                    
//                    if(rowList != null && rowList.size() > 0) {
//                        stringBuilder.append("<SPItemListDef><TableCols>");
//                        for (JSONObject jsonObject : rowList) {
//                            String nodeName= jsonObject.getString("FieldName");
//                            String nodeValue = jsonObject.getString("value");
//                            if("BelongXiaQuCode".equals(nodeName)||"OperateUserName".equals(nodeName)||"OperateDate".equals(nodeName)||
//                                    "Row_ID".equals(nodeName)||"RowGuid".equals(nodeName)||"OperateUserGuid".equals(nodeName)||
//                                    "OperateUserOUGuid".equals(nodeName)||"OperateUserBaseOUGuid".equals(nodeName)||"PVIGuid".equals(nodeName)||
//                                    "YearFlag".equals(nodeName)) {
//                                continue;
//                            }
//                            if(StringUtil.isBlank(nodeValue)) {
//                                nodeValue="";
//                            }
//                            stringBuilder.append("<"+nodeName+">"+nodeValue+"</"+nodeName+">");
//                        }
//                        stringBuilder.append("</TableCols></SPItemListDef>");
//                        dataJson.put("eform", stringBuilder.toString());
//                        return JsonUtils.zwdtRestReturn("1", "推送电子表单成功", dataJson.toString());
//                    }else {
//                        dataJson.put("errflag", "1");
//                        dataJson.put("errtext", "电子表单该条数据未空");
//                        return JsonUtils.zwdtRestReturn("0", "推送电子表单信息失败", dataJson.toString());
//                    }
//                }else {
//                	 dataJson.put("errflag", "1");
//                     dataJson.put("errtext", "电子表单该条数据未空");
//                     return JsonUtils.zwdtRestReturn("0", "推送电子表单信息失败", dataJson.toString());
//                }
//                
//
//            }else {
//                dataJson.put("errflag", "1");
//                dataJson.put("errtext", "未获取到电子表单信息");
//                return JsonUtils.zwdtRestReturn("0", "推送电子表单信息失败", dataJson.toString());
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            JSONObject dataJson = new JSONObject();
//            dataJson.put("errflag", "1");
//            dataJson.put("errtext", "获取电子表单信息失败");
//            return JsonUtils.zwdtRestReturn("0", "获取电子表单信息失败", dataJson.toString());
//        }
//    }
    public String getFormElement(String projectGuid,String formId) {
        try {
            JSONObject paramsJson = new JSONObject();
            JSONObject dataJson = new JSONObject();
            paramsJson.put("rowGuid", projectGuid);
            paramsJson.put("formId", formId);
    //        paramsJson.put("rowGuid", "daa098f4-8f02-4a51-850c-e911120f8f9f");
    //        paramsJson.put("formId", "279");
            paramsJson.put("tableName", "");
            Map<String, String> paramsMap = new HashMap<String, String>();
            //获取办件电子表单数据
            paramsMap.put("params", paramsJson.toJSONString());
            paramsMap.put("sessionGuid", "");
            //String epointUrl = "http://117.73.252.120:8088/epoint-zwfwsform-web";
            String epointUrl = iConfigService.getFrameConfigValue("dzbdurl");
            String formDataStr="";
            log.info("===开始调用电子表单接口获取电子表单数据==="+EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            formDataStr = TARequestUtil.getResult(epointUrl+"/rest/sform/getPageData", paramsMap);
            log.info("===结束调用电子表单接口获取电子表单数据==="+EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            if(StringUtil.isNotBlank(formDataStr)) {
                log.info("===成功获取电子表单数据formDataStr==="+formDataStr);
                //主表数据
                List<JSONObject> mainList = (List<JSONObject>) JSON.parseObject(formDataStr).getJSONObject("custom").getJSONObject("recordData").get("mainList");
                //子表数据
                List<JSONObject> subList = (List<JSONObject>) JSON.parseObject(formDataStr).getJSONObject("custom").getJSONObject("recordData").get("subList");
                List<JSONObject> rowList = new ArrayList<JSONObject>();
                JSONObject main = mainList.get(0);
                rowList = (List<JSONObject>) main.get("rowList");
                StringBuilder stringBuilder = new StringBuilder();
                
                if(rowList != null && rowList.size() > 0) {
                    stringBuilder.append("<SPItemListDef><TableCols>");
                    String gargoOne = "";
                    String gargoOneName = "";
                    String gargoTwo = "";
                    String gargoTwoName = "";
                    for (JSONObject jsonObject : rowList) {
                        String nodeName= jsonObject.getString("FieldName");
                       // String nodeName= jsonObject.getString("FieldName").toUpperCase();
                        String nodeValue = jsonObject.getString("value");
                        String nodeChinessName = jsonObject.getString("FieldChineseName");
                        if("BelongXiaQuCode".equals(nodeName)||"OperateUserName".equals(nodeName)||"OperateDate".equals(nodeName)||
                                "Row_ID".equals(nodeName)||"RowGuid".equals(nodeName)||"OperateUserGuid".equals(nodeName)||
                                "OperateUserOUGuid".equals(nodeName)||"OperateUserBaseOUGuid".equals(nodeName)||"PVIGuid".equals(nodeName)||
                                "YearFlag".equals(nodeName)) {
                            continue;
                        }
                        if(nodeChinessName.indexOf("DATE") != -1) {
                            if(StringUtil.isBlank(nodeValue)) {
                                nodeValue="";
                            }
                            else {
                                if(nodeValue.contains("-")) {
                                    if(nodeValue.contains(" ")) {
                                        nodeValue = nodeValue.split(" ")[0].replace("-", "");
                                    }
                                    else {
                                        nodeValue=nodeValue.replace("-", "");
                                    }
                                }
                            }
                        }else {
                            if(StringUtil.isBlank(nodeValue)) {
                                nodeValue="";
                            }
                        }
                        if("CARGO_CATE".equals(nodeName)) {
                            gargoOne = nodeValue;
                            gargoOneName = codeItemsService.getItemTextByCodeID("215", gargoOne);
                            
                        }
                        else if("CARGO_TYPE".equals(nodeName)) {
                            gargoTwo = nodeValue;
                            gargoTwoName = codeItemsService.getItemTextByCodeID("215", gargoTwo);
                        }
                        else{
                            stringBuilder.append("<"+nodeName+">"+nodeValue+"</"+nodeName+">");
                        }
                    }
                    if(StringUtil.isNotBlank(subList) && !subList.isEmpty()) {
                        for (JSONObject jsonObject1 : subList) {
                            List<JSONObject> mainRecordList = (List<JSONObject>)jsonObject1.get("mainRecordList");
                            if(StringUtil.isNotBlank(mainRecordList) && !mainRecordList.isEmpty()) {
                                for (JSONObject jsonObject2 : mainRecordList) {
                                    List<JSONObject> subRecordList = (List<JSONObject>)jsonObject2.get("subRecordList");
                                    if(StringUtil.isNotBlank(subRecordList) && !subRecordList.isEmpty()) {
                                        stringBuilder.append("<List name=\"TuJingDi\" id=\"TuJingDi\">");
                                        int index = 0;
                                        for (JSONObject jsonObject3 : subRecordList) {
                                            stringBuilder.append("<TableCols id=\""+index+"\">");
                                            List<JSONObject> zbRowList = (List<JSONObject>)jsonObject3.get("rowList");
                                            if(StringUtil.isNotBlank(zbRowList) && !zbRowList.isEmpty()) {
                                                for (JSONObject jsonObject4 : zbRowList) {
                                                    if("PASS_DISTS".equals(jsonObject4.getString("FieldName"))) {
                                                        stringBuilder.append("<dynWidgetName>途径地</dynWidgetName>");
                                                        stringBuilder.append("<PASS_DISTS>"+jsonObject4.getString("value")+"</PASS_DISTS>");
                                                    }
                                                    if("ROADS".equals(jsonObject4.getString("FieldName"))) {
                                                        stringBuilder.append("<ROADS>"+jsonObject4.getString("value")+"</ROADS>");
                                                    }
                                                    
                                                }
                                            }
                                            stringBuilder.append("</TableCols>");
                                            index++;
                                        }
                                        stringBuilder.append("</List>");
                                    }
                                }
                            }
                        }
                    }
                    if(StringUtil.isNotBlank(gargoOne) && StringUtil.isNotBlank(gargoOneName) && StringUtil.isNotBlank(gargoTwo) && StringUtil.isNotBlank(gargoTwoName)) {
                        stringBuilder.append("<CARGO_CATE>"+gargoTwoName+"-"+gargoOneName+"</CARGO_CATE>");
                        stringBuilder.append("<CARGO_CATE_treeInfo>"+gargoTwo+":"+gargoTwoName+"|"+gargoOne+":"+gargoOneName+"</CARGO_CATE_treeInfo>");
                    }
                    stringBuilder.append("</TableCols></SPItemListDef>");
                    dataJson.put("eform", stringBuilder.toString());
                    return JsonUtils.zwdtRestReturn("1", "推送电子表单成功", dataJson.toString());
                }else {
                    dataJson.put("errflag", "1");
                    dataJson.put("errtext", "电子表单该条数据未空");
                    return JsonUtils.zwdtRestReturn("0", "推送电子表单信息失败", dataJson.toString());
                }

            }else {
                dataJson.put("errflag", "1");
                dataJson.put("errtext", "未获取到电子表单信息");
                return JsonUtils.zwdtRestReturn("0", "推送电子表单信息失败", dataJson.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            JSONObject dataJson = new JSONObject();
            dataJson.put("errflag", "1");
            dataJson.put("errtext", "获取电子表单信息失败");
            return JsonUtils.zwdtRestReturn("0", "获取电子表单信息失败", dataJson.toString());
        }
    }
}
