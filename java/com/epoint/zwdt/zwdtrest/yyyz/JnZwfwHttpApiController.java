package com.epoint.zwdt.zwdtrest.yyyz;

import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.xmz.xmycslspinfo.api.IXmYcslSpinfoService;
import com.epoint.xmz.xmycslspinfo.api.entity.XmYcslSpinfo;
import com.epoint.xmz.xmyyyzbjinfo.api.IXmYyyzBjinfoService;
import com.epoint.xmz.xmyyyzbjinfo.api.entity.XmYyyzBjinfo;
import com.epoint.zwdt.util.TARequestUtil;
import com.epoint.zwdt.zwdtrest.yyyz.api.IDaTing;

/**
 * 一窗受理综合受理平台对外发布的http接口
 * @author 李运昂
 * @time 2020年7月1日
 */
@RestController
@RequestMapping("/jnhttpapi")
public class JnZwfwHttpApiController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    private IOuService ouService;
    
    @Autowired
    private IDaTing daTingService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private ICodeItemsService codeItemsService;
    
    @Autowired
    private IAuditProject auditProjectService;
    
    @Autowired
    private IXmYyyzBjinfoService xmYyyzBjinfoService;
    
    @Autowired
    private IXmYcslSpinfoService xmYcslSpinfoService;
    
    @Autowired
    private ICodeItemsService codeItemService;
    
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;

    /**
     * 推送申报流水号入参报文
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/setAppplyProject", method = {RequestMethod.POST })
    public String setAppplyProject(@RequestBody String params) throws Exception {
        log.info("=====开始调用接口jnzwfwHttpApiController.setApplyProject=====");
        log.info("========params========" + params);
        try {
            JSONObject paramObj = JSONObject.parseObject(params);
            String token = paramObj.getString("token");
            log.info("========params========" + token);
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String flowsn = paramObj.getString("flowsn");
                if (StringUtil.isNotBlank(flowsn)) {
	                    String areacode = paramObj.getString("areacode");
	                    String newItemCode = paramObj.getString("newItemCode");
	                    String taskguid = paramObj.getString("taskId");
                        Record auditTask = daTingService.getAuditTaskTaianByTaskId(taskguid);
                        String url = "";
                        String type = "";
                        String [] sendParam = auditTask.getStr("yyyzSendUrl").split("_SPI_");
                        if(StringUtil.isNotBlank(sendParam) && sendParam.length>0) {
                            url = sendParam[0];
                            type = sendParam[1];
                        }
                        log.info("推送办件编号地址："+url+";type="+type);
                        
                        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        xml.append("<APPROVEDATAINFO>");
                        xml.append("<SBLSH_SHORT>" + flowsn + "</SBLSH_SHORT>");
                        xml.append("<SXBM>" + newItemCode + "</SXBM>");
                        xml.append("<XZQHDM>" + areacode + "</XZQHDM>"); //areacode
                        xml.append("<EXPRESSTYPE>3</EXPRESSTYPE>");
                        xml.append("<YWLX>0</YWLX>");
                        xml.append("<BZSLBLRXM></BZSLBLRXM>");
                        xml.append("<BZCLQD></BZCLQD>");
                        xml.append("<BZSJ></BZSJ>");
                        xml.append("<JINING>0</JINING>");
                        xml.append("</APPROVEDATAINFO>");
                        String xmlStr = xml.toString();
                        log.info("========开始输出拼接的xml:" + xmlStr + "========");
                        JSONObject sumbit = new JSONObject();
                        sumbit.put(type, xmlStr);

                        // 设置参数
                        //                        Map<String, String> map = new HashMap<>();
                        //                        map.put("param", xmlStr);
                        //                        Map<String, Object> paramMap = new HashMap<>();
                        //                        paramMap.putAll(map);
                        //                        String note = HttpUtil.doPost(url, paramMap);
                        /*String rtnstr ="";
                        try {
                            // 设置参数
                            Map<String, String> map = new HashMap<>(16);
                            map.put("param", xmlStr);
                            Map<String, Object> param = new HashMap<>();
                            param.putAll(map);
                            rtnstr = HttpUtil.doPost(url, param);
                            log.info("====rtnstr====" + rtnstr);
                        }
                        catch (Exception e1) {
                            e1.printStackTrace();
                        }*/
                        String postURL = url;
                        PostMethod postMethod = null;
                        postMethod = new PostMethod(postURL);
                        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                        //参数设置，需要注意的就是里边不能传NULL，要传空字符串
                        NameValuePair[] data = {new NameValuePair(type, xmlStr)};
                        postMethod.setRequestBody(data);
                        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
                        int response = httpClient.executeMethod(postMethod); // 执行POST方法
                        String result = postMethod.getResponseBodyAsString();
                        log.info("========开始输出result:" + result + "========");
                        return result;

                        //String note = TARequestUtil.sendPost(url, xmlStr, "", "");
                        /*log.info("========开始输出rtnstr:" + rtnstr + "========");
                        return rtnstr;*/
                }
                else {
                    log.info("=======结束调用jnzwfwHttpApiController.setApplyProject接口=======");
                    return generateErrorReturn("未获取传参！");
                }
            }
            else {
                log.info("=======结束调用jnzwfwHttpApiController.setApplyProject接口=======");
                return generateErrorReturn("身份验证失败！");
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
            log.info("=======结束调用jnzwfwHttpApiController.setApplyProject接口=======");
            log.error("-------------调用setApplyProject方法异常" + e.getMessage() + "--------------");
            return generateErrorReturn("调用接口失败");
        }
    }

    /**
     * 跟据申办编号获取申报信息入参报文
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/setApplyProjectInfo", method = {RequestMethod.POST })
    public String setApplyProjectInfo(@RequestBody String params) throws Exception {
        log.info("=====开始调用接口jnzwfwHttpApiController.setApplyProjectInfo=====");
        log.info("========params========" + params);
        try {
            String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT);
            String param1 = URLDecoder.decode(params,"utf-8");
            JSONObject paramObj = JSONObject.parseObject(param1);
            log.info("========paramObj========" + paramObj);
            String token = paramObj.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String xmlStr = paramObj.getString("xmlStr");
                if (StringUtil.isBlank(xmlStr)) {
                    log.info("========开始输出拼接的xml:" + xmlStr + "========");
                    return generateErrorReturn("xmlStr参数为空，请检查xmlStr！");
                }
                String flowsn = "";
                String newItemCode = "";
                // <?xml version="1.0" encoding="utf-8"?>
                // <APPROVEDATAINFO>
                // <PROJID>申报号 </PROJID>
                // <ITEMNO>事项编码</ITEMNO>
                //xmlStr = Base64Util.decode(xmlStr);
                //xmlStr = URLDecoder.decode(xmlStr, DEFAULT_CHARSET);
                log.info("=====setApplyProjectInfo.输入参数:" + xmlStr);
                Document document = DocumentHelper.parseText(xmlStr);
                Element rootElement = document.getRootElement();
                flowsn = rootElement.elementText("SBLSH_SHORT");
                newItemCode = rootElement.elementText("SXBM");

                if (StringUtil.isNotBlank(flowsn)) {
                    AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                    if (StringUtil.isNotBlank(auditProject)) {
                        String biGuid = auditProject.getBiguid();
                        String taskId = auditProject.getTask_id();
                        Record businessLicenseBaseInfo = daTingService.getBusinessLicenseBaseInfoByBiGuid(biGuid);
                        List<Record> yyyzMaterialList = xmYcslSpinfoService.findYyyzMaterialList(biGuid, taskId);
                        String selectItems = "";
                        String formsInfo = "";
                        if(businessLicenseBaseInfo != null) {
                            String baseInfoGuid = businessLicenseBaseInfo.getStr("rowguid");
                            Record businessLicenseExtension = daTingService
                                    .getBusinessLicenseExtensionByBaseInfoGuid(baseInfoGuid);
                            selectItems = businessLicenseExtension.getStr("selectItem");
                            formsInfo = businessLicenseExtension.getStr("formsInfo");
                        }
                        Record auditTaskTaian = daTingService.getAuditTaskTaianByTaskId(auditProject.getTaskguid());
                        //事项版本编码
                        String yyyzItemCode = auditTaskTaian.getStr("yyyzItemcode");
                        //事项表单编码
                        String sxbdbm = "";
                        if(StringUtil.isNotBlank(selectItems)) {
                            
                            //获取json数组
                            JSONArray array = JSON.parseArray(selectItems);
                            //遍历json数组，当其itemcode与事项版本编码相同时，取formids为事项表单编码
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject param = JSONObject.parseObject(array.get(i).toString());
                                if (param.getString("itemCode").equals(yyyzItemCode)) {
                                    sxbdbm = param.getString("formIds");
                                }
                            }
                        }
                        //事项表单编码数组
                        String [] sxbdbms = sxbdbm.split(",");
                        JSONObject paramList = new JSONObject();
                        if(StringUtil.isNotBlank(formsInfo)) {
                            
                            //获取json数组
                            JSONObject param = JSONObject.parseObject(formsInfo);
                            //遍历json数组，当其itemcode与事项版本编码相同时，取formids为事项表单编码
                            for (int i = 0; i < sxbdbms.length; i++) {
                                if(formsInfo.contains(sxbdbms[i])) {
                                    String param2 = param.getString(sxbdbms[i]);
                                    paramList.put(sxbdbms[i], param2);
                                }

                            }
                        }
                        //定义子表信息
                        String zbParam = "";
                        if(paramList != null) {
                            zbParam = paramList.toJSONString();
                        }
                        String certtype = codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                                auditProject.getCerttype());
                        String sqrlx = "";
                        String lxrxm = "";
                        if(StringUtil.isNotBlank(auditProject.getApplyertype())) {
                            if("10".equals(auditProject.getApplyertype())) {
                                sqrlx = "3";
                                lxrxm = auditProject.getLegal();
                            }
                            else {
                                sqrlx = "1";
                                lxrxm = auditProject.getApplyername();
                            }
                        }
                        List<String> materialNames = new ArrayList<String>();
                        if(StringUtil.isNotBlank(yyyzMaterialList) && yyyzMaterialList.size()>0) {
                            for(Record yyyzMaterial : yyyzMaterialList) {
                                materialNames.add(yyyzMaterial.getStr("materialname"));
                            }
                        }
                        String ouGuid = auditProject.getOuguid();
                        FrameOu frameOu = ouService.getOuByOuGuid(ouGuid);
                        String ouCode = frameOu.getOucode();
                        
                        String areaName = codeItemService.getItemTextByCodeName("泰安十二位辖区", auditProject.getAreacode());
                        String materialName = StringUtil.join(materialNames, ";");
                        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        xml.append("<RESULT>");
                        xml.append("<STATUS>200</STATUS>");
                        xml.append("<DESC>请求成功</DESC>");
                        xml.append("<TIME>" + date + "</TIME>");
                        xml.append("<DATA>");
                        xml.append("<SXBBBM>" + yyyzItemCode + "</SXBBBM>");
                        xml.append("<SXBDBM>" + sxbdbm + "</SXBDBM>");
                        xml.append("<SPRENYUAN>");
                        xml.append("</SPRENYUAN>");
                        xml.append("<SPQIYE>");
                        xml.append("<ORGNAME>" + auditProject.getApplyername() + "</ORGNAME>");
                        xml.append("<ORGCODE>" + auditProject.getCertnum() + "</ORGCODE>");
                        xml.append("<ORGTYPE></ORGTYPE>");
                        xml.append("<ORGACTUALITY></ORGACTUALITY>");
                        xml.append("<LEGALPERSON>" + auditProject.getLegal() + "</LEGALPERSON>");
                        xml.append("<LEGALPERSONTYPE></LEGALPERSONTYPE>");
                        xml.append("<CERTIFICATENAME>" + certtype + "</CERTIFICATENAME>");
                        xml.append("<CERTIFICATENO>" + auditProject.getCertnum() + "</CERTIFICATENO>");
                        xml.append("<RESPONSIBLEPERSON></RESPONSIBLEPERSON>");
                        xml.append("<RESPONSIBLEPERSONID></RESPONSIBLEPERSONID>");
                        xml.append("<INAREACODE>" + auditProject.getAreacode() + "</INAREACODE>");
                        xml.append("<INAREA></INAREA>");
                        xml.append("<CHARGEDEPARTMENT></CHARGEDEPARTMENT>");
                        xml.append("<REGISTERADDRESS>" + auditProject.getAddress() + "</REGISTERADDRESS>");
                        xml.append("<PRODUCEADDRESS></PRODUCEADDRESS>");
                        xml.append("<MAILINGADDRESS></MAILINGADDRESS>");
                        xml.append("<POSTALCODE>" + auditProject.getContactpostcode() + "</POSTALCODE>");
                        xml.append("<LINKMAN>" + auditProject.getContactperson() + "</LINKMAN>");
                        xml.append("<CONTACTPHONE>" + auditProject.getContactphone() + "</CONTACTPHONE>");
                        xml.append("<FAX>" + auditProject.getContactfax() + "</FAX>");
                        xml.append("<LINKMANEMAIL>" + auditProject.getContactemail() + "</LINKMANEMAIL>");
                        xml.append("<COMPANYLICENSE>" + certtype + "</COMPANYLICENSE>");
                        xml.append("</SPQIYE>");
                        xml.append("<SPXIANGMU>");
                        xml.append("</SPXIANGMU>");
                        xml.append("<SPSHENQIN>");
                        xml.append("<SBLSH_SHORT>" + flowsn + "</SBLSH_SHORT>");
                        xml.append("<SBLSH>" + flowsn + "</SBLSH>");
                        xml.append("<SXBM>" + newItemCode + "</SXBM>");
                        xml.append("<SXBM_SHORT>" + newItemCode + "</SXBM_SHORT>");
                        xml.append("<SXMC>" + auditProject.getProjectname() + "</SXMC>");
                        xml.append("<SXQXBM></SXQXBM>");
                        xml.append("<SQRLX>" + sqrlx + "</SQRLX>");
                        xml.append("<SQRMC>" + auditProject.getApplyername() + "</SQRMC>");
                        xml.append("<SQRZJHM>" + auditProject.getCertnum() + "</SQRZJHM>");
                        xml.append("<LXRXM>" + lxrxm + "</LXRXM>");
                        xml.append("<LXRSJ>"+auditProject.getContactphone()+"</LXRSJ>");
                        xml.append("<SBXMMC>"+ auditProject.getProjectname() +"</SBXMMC>");
                        xml.append("<SBCLQD>"+ materialName +"</SBCLQD>");
                        xml.append("<TJFS>01</TJFS>");
                        xml.append("<SBHZH>" + flowsn + "</SBHZH>");
                        xml.append("<SBSJ>" + auditProject.getApplydate() + "</SBSJ>");
                        xml.append("<XZQHDM>" +auditProject.getAreacode() + "</XZQHDM>");
                        xml.append("<YSBLSH></YSBLSH>");
                        xml.append("<REC_FLAG>" + 1 + "</REC_FLAG>");
                        xml.append("<XML_DATA><SPItemListDef><rows>"+zbParam+"</rows></SPItemListDef></XML_DATA>");
                        xml.append("<D_ZZJGDM>"+ouCode+"</D_ZZJGDM>");
                        xml.append("</SPSHENQIN>");
                        xml.append("<SPCAILIAOSHENHE>");
                        xml.append("</SPCAILIAOSHENHE>");
                        xml.append("<SPSHOULI>");
                        xml.append("<SBLSH_SHORT>" + flowsn + "</SBLSH_SHORT>");
                        xml.append("<SBLSH>" + flowsn + "</SBLSH>");
                        xml.append("<YWLSH></YWLSH>");
                        xml.append("<SXBM>"+ newItemCode +"</SXBM>");
                        xml.append("<SXBM_SHORT></SXBM_SHORT>");
                        xml.append("<SXQXBM></SXQXBM>");
                        xml.append("<SLBMMC>"+frameOu.getOuname()+"</SLBMMC>");//受理部门名称
                        xml.append("<SLBMZZJGDM>"+ouCode+"</SLBMZZJGDM>");
                        xml.append("<XZQHDM>"+ auditProject.getAreacode() +"</XZQHDM>");
                        xml.append("<XZQHMC>" + areaName + "</XZQHMC>");
                        xml.append("<BLRXM>"+auditProject.getAcceptusername()+"</BLRXM>");
                        xml.append("<SLZTDM>1</SLZTDM>");
                        xml.append("<SLHZH></SLHZH>");
                        xml.append("<SLSJ>"+auditProject.getAcceptuserdate()+"</SLSJ>");
                        xml.append("<GXDXZQHDM>"+ auditProject.getAreacode() +"</GXDXZQHDM>");
                        xml.append("<D_ZZJGDM>"+ouCode+"</D_ZZJGDM>");
                        xml.append("</SPSHOULI>");
                        xml.append("<SPBANJIE>");
                        xml.append("</SPBANJIE>");
                        xml.append("<SPTZS>");
                        xml.append("</SPTZS>");
                        xml.append("</DATA>");
                        xml.append("</RESULT>");
                        String newXmlStr = xml.toString();
                        //log.info("========开始输出拼接的xml:" + xmlStr + "========");
                        log.info("=======结束调用TaHttpApiController.setApplyProjectInfo接口======="
                                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        return newXmlStr;
                    }
                    else {
                        log.info("=======结束调用jnzwfwHttpApiController.setApplyProjectInfo接口=======");
                        return generateErrorReturn("未获取办件信息！");
                    }
                }
                else {
                    log.info("=======结束调用jnzwfwHttpApiController.setApplyProjectInfo接口=======");
                    return generateErrorReturn("申办编号为空！");
                }
            }
            else {
                log.info("=======结束调用jnzwfwHttpApiController.setApplyProjectInfo接口=======");
                return generateErrorReturn("token验证不通过！");
            }
        }
        catch (Exception e) {
            log.info("=======结束调用jnzwfwHttpApiController.setApplyProjectInfo接口=======");
            log.error("-------------调用setApplyProjectInfo方法异常：" + e.getMessage() + "--------------");
            return generateErrorReturn("调用接口发生异常!");
        }
    }

    /**
     * 据申办编号获取申报材料入参报文
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/getApplyProjectMaterial", method = {RequestMethod.POST })
    public String getApplyProjectMaterial(@RequestBody String params) throws Exception {
        log.info("=====开始调用接口jnzwfwHttpApiController.getApplyProjectMaterial=====");
        log.info("========params========" + params);
        try {
            String param1 = URLDecoder.decode(params,"utf-8");
            JSONObject paramObj = JSONObject.parseObject(param1);
            log.info("========paramObj========" + paramObj);
            String token = paramObj.getString("token");
            log.info("========token========" + token);
            //String url = "http://59.206.216.206:8089/sdfda/applyApi/receiveSno";
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String xmlStr = paramObj.getString("xmlStr");
                if (StringUtil.isBlank(xmlStr)) {
                    log.info("========输出xmlStr:" + xmlStr + "========");
                    return generateErrorReturn("xmlStr参数为空，请检查xmlStr！");
                }
                String flowsn = "";
                String newItemCode = "";
                String ywlx = "";
                // <?xml version="1.0" encoding="utf-8"?>
                // <APPROVEDATAINFO>
                // <PROJID>申报号 </PROJID>
                // <ITEMNO>事项编码</ITEMNO>
                //xmlStr = Base64Util.decode(xmlStr);
                //xmlStr = URLDecoder.decode(xmlStr, DEFAULT_CHARSET);
                log.info("=====setApplyProjectInfo.输入参数:" + xmlStr);
                Document document = DocumentHelper.parseText(xmlStr);
                Element rootElement = document.getRootElement();
                flowsn = rootElement.elementText("SBLSH_SHORT");
                newItemCode = rootElement.elementText("SXBM");
                ywlx = rootElement.elementText("YWLX");
                // String date = DateUtil.formatDate(new Date(), "yyyyMMddmihhss");
                String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT);
                if (StringUtil.isNotBlank(flowsn)) {
                    AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                    if (StringUtil.isNotBlank(auditProject)) {
                        String biGuid = auditProject.getBiguid();
                        String taskId = auditProject.getTask_id();
                        String areaCode = auditProject.getAreacode();
                        String ouGuid = auditProject.getOuguid();
                        Record frameOu = daTingService.getFramOuByOuGuid(ouGuid);
                        String oucode = "";
                        if (frameOu != null) {
                            oucode = frameOu.getStr("OUCODE");
                        }
                        List<Record> auditSpIYyyzMaterialList = daTingService
                                .getAuditSpIYyyzMaterialByBusinessGuidAndTaskId(biGuid, taskId);
                        if (StringUtil.isNotBlank(auditSpIYyyzMaterialList) && auditSpIYyyzMaterialList.size() > 0) {
                            StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                            xml.append("<RESULT>");
                            xml.append("<STATUS>200</STATUS>");
                            xml.append("<DESC>请求成功</DESC>");
                            xml.append("<TIME>" + date + "</TIME>");
                            xml.append("<DATA>");
                            xml.append("<RECORDS>");
                            for (Record auditSpIYyyzMaterial : auditSpIYyyzMaterialList) {
                                String yyyztype = auditSpIYyyzMaterial.getStr("yyyztype");
                                String materialName = auditSpIYyyzMaterial.getStr("materialname");
                                String cliengguid = auditSpIYyyzMaterial.getStr("cliengguid");
                                List<Record> frameAttachInfoList = daTingService.getFrameAttachInfoByCliengguid(cliengguid);
                                String attachGuid = "";
                                String uploadTime = "";
                                String attachFileName = "";
                                String contentType = "";
                                if(StringUtil.isNotBlank(frameAttachInfoList) && !frameAttachInfoList.isEmpty()) {
                                    for(Record frameAttachInfo : frameAttachInfoList) {
                                        attachGuid = frameAttachInfo.getStr("attachGuid");
                                        uploadTime = frameAttachInfo.getStr("UPLOADDATETIME");
                                        attachFileName = frameAttachInfo.getStr("ATTACHFILENAME");
                                        contentType = frameAttachInfo.getStr("CONTENTTYPE");
                                        xml.append("<RECORD>");
                                        xml.append("<SEQ>" + UUID.randomUUID().toString().replace("-", "") + "</SEQ>");
                                        xml.append("<SBLSH_SHORT>" + flowsn + "</SBLSH_SHORT>");
                                        xml.append("<SBLSH>" + flowsn + "</SBLSH>");
                                        xml.append("<WJLX>1</WJLX>");
                                        xml.append("<CLLX>2</CLLX>");
                                        xml.append("<CLSL>1</CLSL>");
                                        xml.append("<UPLOAD_TIME>"+uploadTime+"</UPLOAD_TIME>");
                                        xml.append("<ATTACH_NAME>" + attachFileName + "</ATTACH_NAME>");
                                        xml.append("<ATTACH_ID>" + attachGuid + "</ATTACH_ID>");
                                        xml.append("<REMARK></REMARK>");
                                        xml.append("<ATTACH_BODY></ATTACH_BODY>");
                                        xml.append("<SAVE_TYPE>1</SAVE_TYPE>");
                                        xml.append("<ATTACH_SIGN></ATTACH_SIGN>");
                                        xml.append("<ATTACH_TYPE>" + contentType + "</ATTACH_TYPE>");
                                        xml.append("<ATTACH_PATH></ATTACH_PATH>");
                                        xml.append("<STUFF_SEQ>" + yyyztype + "</STUFF_SEQ>");
                                        xml.append("<CLMC>" + materialName + "</CLMC> ");
                                        xml.append("<SLBMZZJDDM>" + oucode + "</SLBMZZJDDM>");
                                        xml.append("<XZQHDM>" + areaCode + "</XZQHDM>");//areaCode
                                        xml.append("<VERSION>1</VERSION>");
                                        xml.append("<REC_FLAG>1</REC_FLAG>");
                                        xml.append("</RECORD>");
                                    }
                                }
                            }
                            xml.append("</RECORDS>");
                            xml.append("</DATA>");
                            xml.append("</RESULT>");
                            String newXmlStr = xml.toString();
                            log.info("========开始输出拼接的xml:" + xmlStr + "========");

                            // 发送请求
                            // note = TARequestUtil.sendPost(
                            // "http://localhost:8070/epoint-web-zwdt/rest/appletController/getFaceAuthDetectInfo",
                            // params, "",
                            // "");
                            log.info("=======结束调用jnzwfwHttpApiController.getApplyProjectMaterial接口======="
                                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                            return newXmlStr;
                        }
                        else {
                            log.info("=======结束调用jnzwfwHttpApiController.getApplyProjectMaterial接口=======");
                            return generateErrorReturn("没有获取到材料信息！");
                        }
                    }
                    else {
                        log.info("=======结束调用jnzwfwHttpApiController.getApplyProjectMaterial接口=======");
                        return generateErrorReturn("获取办件信息失败");
                    }
                }
                else {
                    log.info("=======结束调用jnzwfwHttpApiController.getApplyProjectMaterial接口=======");
                    return generateErrorReturn("获取办件流水号失败");
                }
            }
            else {
                log.info("=======结束调用jnzwfwHttpApiController.getApplyProjectMaterial接口=======");
                return generateErrorReturn("身份验证失败！");
            }
        }
        catch (Exception e) {
            log.info("=======结束调用jnzwfwHttpApiController.getApplyProjectMaterial接口=======");
            log.error("-------------调用getApplyProjectMaterial方法异常：" + e.getMessage() + "--------------");
            return generateErrorReturn("调用接口失败");
        }
    }

    
    /**
     * 接收部门业务受理信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/acceptBusinessInfo", method = {RequestMethod.POST })
    public String acceptBusinessInfo(@RequestBody String params) throws Exception {
        log.info("=====开始调用接口jnzwfwHttpApiController.acceptBusinessInfo=====");
        log.info("========params========" + params);
        try {
            String param1 = URLDecoder.decode(params,"utf-8");
            JSONObject paramObj = JSONObject.parseObject(param1);
            log.info("========paramObj:" + paramObj + "========");
            String token = paramObj.getString("token");
            log.info("========token:" + token + "========");
            //String url = "http://59.206.216.206:8089/sdfda/applyApi/receiveSno";
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String xmlStr = paramObj.getString("xmlStr");
                if (StringUtil.isBlank(xmlStr)) {
                    log.info("========输出xmlStr:" + xmlStr + "========");
                    return generateErrorReturn("xmlStr参数为空，请检查xmlStr！");
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
                // <?xml version="1.0" encoding="utf-8"?>
                // <APPROVEDATAINFO>
                // <PROJID>申报号 </PROJID>
                // <ITEMNO>事项编码</ITEMNO>
                //xmlStr = Base64Util.decode(xmlStr);
                //xmlStr = URLDecoder.decode(xmlStr, DEFAULT_CHARSET);
                log.info("=====setApplyProjectInfo.输入参数:" + xmlStr);
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
                
                String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT);
                Date acceptuserdate = EpointDateUtil.convertString2DateAuto(ywslsj);
                if (StringUtil.isNotBlank(flowsn)) {
                    AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                    if (StringUtil.isNotBlank(auditProject)) {
                        auditProject.setAcceptuserdate(acceptuserdate);
                        auditProject.setAcceptusername(ywslrmc);
                        auditProject.setAcceptuserguid(ywslrbm);
                        if("0".equals(ywslzt)) {
                            String status = codeItemsService.getItemValueByCodeID("1015668", "不予受理");
                            auditProject.setStatus(Integer.parseInt(status));
                        }
                        else if("1".equals(ywslzt)){
                            String status = codeItemsService.getItemValueByCodeID("1015668", "已受理");
                            auditProject.setStatus(Integer.parseInt(status));
                        }
                        auditProject.setRemark(ywslyj);
                        auditProjectService.updateProject(auditProject);
                        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        xml.append("<RESULT>");
                        xml.append("<STATUS>200</STATUS>");
                        xml.append("<DESC>请求成功</DESC>");
                        xml.append("<TIME>" + date + "</TIME>");
                        xml.append("<DATA>");
                        xml.append("</DATA>");
                        xml.append("</RESULT>");
                        String newXmlStr = xml.toString();
                        log.info("========开始输出拼接的xml:" + xmlStr + "========");

                        // 发送请求
                        // note = TARequestUtil.sendPost(
                        // "http://localhost:8070/epoint-web-zwdt/rest/appletController/getFaceAuthDetectInfo",
                        // params, "",
                        // "");
                        log.info("=======结束调用jnzwfwHttpApiController.acceptBusinessInfo接口======="
                                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        return newXmlStr;
                    }
                    else {
                        log.info("=======结束调用jnzwfwHttpApiController.acceptBusinessInfo接口=======");
                        return generateErrorReturn("获取办件信息失败");
                    }
                }
                else {
                    log.info("=======结束调用jnzwfwHttpApiController.acceptBusinessInfo接口=======");
                    return generateErrorReturn("获取办件流水号失败");
                }
            }
            else {
                log.info("=======结束调用TaHttpApiController.acceptBusinessInfo接口=======");
                return generateErrorReturn("身份验证失败！");
            }
        }
        catch (Exception e) {
            log.info("=======结束调用TaHttpApiController.acceptBusinessInfo接口=======");
            log.error("-------------调用acceptBusinessInfo方法异常：" + e.getMessage() + "--------------");
            return generateErrorReturn("调用接口失败");
        }
    }
    /**
     * 接收部门业务办结信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/completeBusinessInfo", method = {RequestMethod.POST })
    public String completeBusinessInfo(@RequestBody String params) throws Exception {
        log.info("=====开始调用接口jnzwfwHttpApiController.completeBusinessInfo=====");
        log.info("========params========" + params);
        try {
            String param1 = URLDecoder.decode(params,"utf-8");
            JSONObject paramObj = JSONObject.parseObject(param1);
            log.info("========paramObj:" + paramObj + "========");
            String token = paramObj.getString("token");
            log.info("========token:" + token + "========");
            //String url = "http://59.206.216.206:8089/sdfda/applyApi/receiveSno";
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String xmlStr = paramObj.getString("xmlStr");
                if (StringUtil.isBlank(xmlStr)) {
                    log.info("========输出xmlStr:" + xmlStr + "========");
                    return generateErrorReturn("xmlStr参数为空，请检查xmlStr！");
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
               
                log.info("=====setApplyProjectInfo.输入参数:" + xmlStr);
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
                    bjjgdm = element.elementText("BJJGDM");
                    bjjgms = element.elementText("BJJGMS");
                    zfhthyy = element.elementText("ZFHTHYY");
                    zjgzmc = element.elementText("ZJGZMC");
                    zjbh = element.elementText("ZJBH");
                    zjyxqx = element.elementText("ZJYXQX");
                    fzgzdw = element.elementText("FZGZDW");
                    sfje = element.elementText("SFJE");
                    jedwdm = element.elementText("JEDWDM");
                    bjsj = element.elementText("BJSJ");
                    bz = element.elementText("BZ");
                    byzd = element.elementText("BYZD");
                }
                String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT);
                Date banjieDate =  EpointDateUtil.convertString2DateAuto(bjsj);
                if (StringUtil.isNotBlank(flowsn)) {
                    AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                    if (StringUtil.isNotBlank(auditProject)) {
                        auditProject.setBanjiedate(banjieDate);
                        auditProject.setBanjieareacode(xzqhdm);
                        auditProject.setBanjieuserguid(sprdm);
                        auditProject.setBanjieusername(sprxm);
                        String status = "";
                        String banJieResult = "";
                        switch (bjjgdm) {
                            case "0" :
                                status = "90";
                                banJieResult = "40";
                                break;
                            case "1" :
                                status = "99";
                                banJieResult = "50";
                                break;
                            case "2" :
                                status = "99";
                                banJieResult = "50";
                                break;
                            case "3" :
                                status = "99";
                                banJieResult = "50";
                                break;
                            case "4" :
                                status = "99";
                                banJieResult = "50";
                                break;
                            case "5" :
                                status = "99";
                                banJieResult = "50";
                                break;
                            case "7" :
                                status = "99";
                                banJieResult = "50";
                                break;
                            default :
                                status = "90";
                                banJieResult = "40";
                                break;
                        }
                        auditProject.setStatus(Integer.parseInt(status));
                        auditProject.setBanjieresult(Integer.parseInt(banJieResult));
                        XmYyyzBjinfo xmYyyzBjinfo = xmYyyzBjinfoService.findXmYyyzBjinfoByFlowsn(auditProject.getFlowsn());
                        if(xmYyyzBjinfo == null) {
                            xmYyyzBjinfo = new XmYyyzBjinfo();
                            xmYyyzBjinfo.setRowguid(UUID.randomUUID().toString());
                            xmYyyzBjinfo.setProjectguid(auditProject.getRowguid());
                            xmYyyzBjinfo.setOperatedate(new Date());
                            xmYyyzBjinfo.setFlowsn(flowsn);
                            xmYyyzBjinfo.setBjbmzzjddm(bjbmzzjddm);
                            xmYyyzBjinfo.setBjjgms(bjjgms);
                            xmYyyzBjinfo.setZfhthyy(zfhthyy);
                            xmYyyzBjinfo.setZjgzmc(zjgzmc);
                            xmYyyzBjinfo.setZjbh(zjbh);
                            xmYyyzBjinfo.setZjyxqx(zjyxqx);
                            xmYyyzBjinfo.setFzgzdw(fzgzdw);
                            xmYyyzBjinfo.setSfje(Double.parseDouble(sfje));
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
                            xmYyyzBjinfo.setSfje(Double.parseDouble(sfje));
                            xmYyyzBjinfo.setBz(bz);
                            xmYyyzBjinfo.setJedwdm(jedwdm);
                            xmYyyzBjinfo.setByzd(byzd);
                            xmYyyzBjinfoService.update(xmYyyzBjinfo);
                        }
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
                        log.info("========更新办件情况成功========");
                        xmYyyzBjinfoService.insert(xmYyyzBjinfo);
                        log.info("========插入办结信息表数据成功========");
                        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        xml.append("<RESULT>");
                        xml.append("<STATUS>200</STATUS>");
                        xml.append("<DESC>请求成功</DESC>");
                        xml.append("<TIME>" + date + "</TIME>");
                        xml.append("<DATA>");
                        xml.append("</DATA>");
                        xml.append("</RESULT>");
                        String newXmlStr = xml.toString();
                        log.info("========开始输出拼接的xml:" + xmlStr + "========");

                        // 发送请求
                        // note = TARequestUtil.sendPost(
                        // "http://localhost:8070/epoint-web-zwdt/rest/appletController/getFaceAuthDetectInfo",
                        // params, "",
                        // "");
                        log.info("=======结束调用jnzwfwHttpApiController.completeBusinessInfo接口======="
                                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        return newXmlStr;
                    }
                    else {
                        log.info("=======结束调用jnzwfwHttpApiController.completeBusinessInfo接口=======");
                        return generateErrorReturn("获取办件信息失败");
                    }
                }
                else {
                    log.info("=======结束调用jnzwfwHttpApiController.completeBusinessInfo接口=======");
                    return generateErrorReturn("获取办件流水号失败");
                }
            }
            else {
                log.info("=======结束调用TaHttpApiController.completeBusinessInfo接口=======");
                return generateErrorReturn("身份验证失败！");
            }
        }
        catch (Exception e) {
            log.info("=======结束调用TaHttpApiController.completeBusinessInfo接口=======");
            log.error("-------------调用completeBusinessInfo方法异常：" + e.getMessage() + "--------------");
            return generateErrorReturn("调用接口失败");
        }
    }
    
    /**
     * 接收业务审批环节信息
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jnzwfw/approve/receiveCourseInfoSsdj", method = {RequestMethod.POST })
    public String receiveCourseInfoSsdj(@RequestBody String params) throws Exception {
        log.info("==========开始调用receiveCourseInfoSsdj接口=========="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            log.info("==========params==========" + params);
            JSONObject paramObj = JSONObject.parseObject(params);
            String token = paramObj.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                JSONObject jsonObject = (JSONObject) paramObj.get("params");
                String accessToken = jsonObject.getString("accessToken");
                String xmlStr = jsonObject.getString("xmlStr");
                //暂时先不校验accessToken
                //                if (StringUtil.isBlank(accessToken)) {
                //                    return generateXmlReturn("300", "accessToken参数为空，请检查accessToken");
                //                }
                //                if (!tokenService.validateToken(accessToken)) {
                //                    return generateXmlReturn("300", "accessToken参数不正确");
                //                }
                if (StringUtil.isBlank(xmlStr)) {
                    return generateXmlReturn("300", "xmlStr参数为空，请检查xmlStr");
                }

                log.info("=====setApplyProjectInfo.输入参数:" + xmlStr);
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
                String clzt = "";
                String clsj = "";
                String byzd = "";
                String byzd1 = "";
                String byzd2 = "";
                String byzd3 = "";
                String byzd4 = "";
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
                    clzt = element.elementText("CLZT");
                    clsj = element.elementText("CLSJ");
                    byzd = element.elementText("BYZD");
                    byzd1 = element.elementText("BYZD1");
                    byzd2 = element.elementText("BYZD2");
                    byzd3 = element.elementText("BYZD3");
                    byzd4 = element.elementText("BYZD4");
                }
                if (StringUtil.isNotBlank(flowsn)) {
                    AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                    if (StringUtil.isNotBlank(auditProject)) {
                        auditProject.setStatus(50);
                        auditProjectService.updateProject(auditProject);
                        XmYcslSpinfo xmYcslSpinfo = xmYcslSpinfoService.findXmYcslSpinfoByFlowsn(auditProject.getFlowsn());
                        if(xmYcslSpinfo == null) {
                            xmYcslSpinfo = new XmYcslSpinfo();
                            xmYcslSpinfo.setRowguid(UUID.randomUUID().toString());
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
                            xmYcslSpinfo.setClzt(clzt);
                            xmYcslSpinfo.setClsj(EpointDateUtil.convertString2DateAuto(clsj));
                            xmYcslSpinfo.setByzd(byzd);
                            xmYcslSpinfo.setByzd1(byzd1);
                            xmYcslSpinfo.setByzd2(byzd2);
                            xmYcslSpinfo.setByzd3(byzd3);
                            xmYcslSpinfo.setByzd4(byzd4);
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
                            xmYcslSpinfo.setClzt(clzt);
                            xmYcslSpinfo.setClsj(EpointDateUtil.convertString2DateAuto(clsj));
                            xmYcslSpinfo.setByzd(byzd);
                            xmYcslSpinfo.setByzd1(byzd1);
                            xmYcslSpinfo.setByzd2(byzd2);
                            xmYcslSpinfo.setByzd3(byzd3);
                            xmYcslSpinfo.setByzd4(byzd4);
                            xmYcslSpinfoService.update(xmYcslSpinfo);
                        }
                    }
                }
                return generateXmlReturn("200", "请求成功");
            }
            else {
                log.info("==========开始调用receiveCourseInfoSsdj接口=========="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return generateXmlReturn("300", "token验证不通过");
            }
        }
        catch (

        Exception e) {
            log.info("==========开始调用receiveCourseInfoSsdj接口=========="
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
    private String generateErrorReturn(String description) {

        String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT);
        StringBuffer flowsnXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        flowsnXml.append("<RESULT>");
        flowsnXml.append("<STATUS>300</STATUS>");
        flowsnXml.append("<DESC>" + description + "</DESC>");
        flowsnXml.append("<TIME>" + date + "</TIME>");
        flowsnXml.append("<DATA></DATA>");
        flowsnXml.append("</RESULT>");
        return flowsnXml.toString();
    }
    
    /**
     * 返回值
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
}
