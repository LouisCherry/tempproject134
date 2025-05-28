package com.epoint.ywztdj.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.ywztdj.apiywztlog.api.IApiYwztLogService;
import com.epoint.ywztdj.apiywztlog.api.entity.ApiYwztLog;
import com.epoint.ywztdj.util.Sm4Util;
import com.epoint.ywztdj.util.YwztRestUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @title: YwztProjectController
 * @Author 成都研发4部-付荣煜
 * @Date: 2024/1/8 16:00
 */
@RequestMapping("/ywztproject")
@RestController
public class YwztProjectController {

    private Logger log = Logger.getLogger(YwztProjectController.class);

    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    @Autowired
    private IConfigService configservice;
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    @Autowired
    private IAuditProject iAuditProject;
    @Autowired
    private IApiYwztLogService iApiYwztLog;
    @Autowired
    private IHandleProject iHandleProject;
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    /**
     * sendApplyNO 接收申报流水号接口
     *
     * @author 成都研发4部-付荣煜
     * @date 2024/1/8 16:01
     * @param params
     * @return String
     */
    @RequestMapping("/sendApplyNO")
    public String sendApplyNO(@RequestParam String xmlStr,HttpServletRequest request){
        log.info("========开始调用sendApplyNO接口========");
        int isSuccess = 2; //用于判断接口是否调用成功
        String msg = "报错信息";
        try {
            if (StringUtil.isBlank(xmlStr)){
                msg = "接口入参xmlStr为空，请联系管理员检查！";
                return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
            }
//            JSONArray xmlArr = JSONArray.parseArray(xmlStr);
//            String xmlStr = xmlArr.getString(0);
            /*if (StringUtil.isBlank(xmlStr)){
                msg = "接口必填入参xmlStr为空，请检查！";
                return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
            }*/

            String sm4Key = ConfigUtil.getConfigValue("ywztrest","sm4Key");
            /*log.info("接口入参中的xmlStr:" + xmlStrObj);
            xmlStrObj = Sm4Util.decryptCbcPadding(sm4Key,xmlStrObj);
            log.info("解密入参后:" + xmlStrObj);*/
            //解析xml
            Document document = DocumentHelper.parseText(xmlStr);
            Element root = document.getRootElement();

            //申办流水号
            String sblshShort = root.element("SBLSH_SHORT").getText();
            log.info("sblshShort:"+sblshShort);
            //事项编码
            String sxbm = root.element("SXBM").getText();
            //事项所属区划编码
            String xzqhdm = root.element("XZQHDM").getText();
            //办件类型
            String expresstype = root.element("EXPRESSTYPE").getText();
            //补正受理人姓名
            String bzslblrxm = root.element("BZSLBLRXM").getText();
            //补正受理清单
            String bzclqd = root.element("BZCLQD").getText();
            //补正材料时间
            String bzsj = root.element("BZSJ").getText();

            //判断办件类型，执行具体操作
            log.info("expresstype:" + expresstype);
            if ("0".equals(expresstype) || "2".equals(expresstype) || "3".equals(expresstype)){ //0-业务受理 2-即办件 3-收件后待受理业务
                //1.根据申办流水号（SBLSH_SHORT），调用 获取申报信息接口、获取申报材料信息接口，获取办件相关信息并保存包系统中
                //封装三方接口入参
                Document applyDocument = DocumentHelper.createDocument();
                //设置编码
                applyDocument.setXMLEncoding("UTF-8");
                //创建根节点
                Element approvedatainfo = applyDocument.addElement("APPROVEDATAINFO");
                //在根节点加入子节点
                Element SBLSH_SHORT = approvedatainfo.addElement("SBLSH_SHORT");
                SBLSH_SHORT.setText(sblshShort);

                Element SXBM = approvedatainfo.addElement("SXBM");
                SXBM.setText(sxbm);
                //将document对象转换成字符串
                String requestXml = applyDocument.asXML();
                Map<String,String> headerMap = YwztRestUtil.getHeaderMap();
                String applyUrl = ConfigUtil.getConfigValue("ywztrest","ywztUrl") + "getApplyInfo";
                //请求体
                JSONObject paramsMap = new JSONObject();
                if (StringUtil.isNotBlank(requestXml)){
                    log.info("构造请求头入参xmlStr：" + requestXml);
                    requestXml = requestXml.replace("\n","");
                    requestXml = requestXml.replace("\r","");
                    log.info("去除换行空格：" + requestXml);
                    //处理换行的问题
                    String encryptResult = Sm4Util.encryptCbcPadding(sm4Key,requestXml);
                    log.info("加密后1：" + encryptResult);
                    encryptResult = encryptResult.replace("\n","").replace("\r","");
                    log.info("去除加密空格：" + encryptResult);
                    paramsMap.put("xmlStr",encryptResult);
                }
                String applyResult = YwztRestUtil.post(applyUrl,paramsMap);
                log.info("三方接口getApplyInfo接口返回：" + applyResult);

                //当前三方接口名称
                String interfaceName = "根据申报编号获取申请基本信息";
                //处理返回结果
                if (StringUtil.isBlank(applyResult)){
                    //记录调用接口日志
                    YwztRestUtil.insertApiYwztLog(applyUrl,requestXml,2,"三方接口getApplyInfo接口调用失败，返回结果为空，请联系管理员检查！",interfaceName);
                    msg = "三方接口getApplyInfo接口调用失败，请联系管理员检查！";
                    return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
                }

                //解析xml
                Document documentApplyResult = DocumentHelper.parseText(applyResult);
                Element rootApplyResult = documentApplyResult.getRootElement();
                if (!"200".equals(rootApplyResult.elementText("code"))){
                    //记录调用接口日志
                    YwztRestUtil.insertApiYwztLog(applyUrl,requestXml,2,"三方接口getApplyInfo接口调用失败，status不为200，请联系管理员检查！" + applyResult,interfaceName);
                    msg = "三方接口getApplyInfo接口调用失败，请联系管理员检查！";
                    return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
                }

                //记录调用接口日志
                YwztRestUtil.insertApiYwztLog(applyUrl,requestXml,1,"调用成功！",interfaceName);

                //解析加密后的Data
                JSONObject applyData = YwztRestUtil.decodeXmlData(applyResult);
//                String applyData = rootApplyResult.elementText("data");

                String applyXml = applyData.getString("data");
                //解析xml
                Document applyResultDocument = DocumentHelper.parseText(applyXml);
                Element applyResultRoot = applyResultDocument.getRootElement();
                Element applyElementData = applyResultRoot.element("DATA");

                //构造办件信息
                AuditProject auditProject = new AuditProject();
                String rowguid=UUID.randomUUID().toString();
                auditProject.setRowguid(rowguid);
                auditProject.setOperatedate(new Date());
                auditProject.setInsertdate(new Date());
                auditProject.setFlowsn(sblshShort);

                //获取task_id
                String new_ITEM_CODE = applyElementData.element("SPSHENQIN").elementText("SXBM");
                if (StringUtil.isNotBlank(new_ITEM_CODE)){
                    log.info("new_ITEM_CODE不为空，继续查找audittask！");
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("new_ITEM_CODE",new_ITEM_CODE);
                    sql.eq("is_enable","1");
                    sql.eq("is_editafterimport","1");
                    sql.eq("is_history","0");
                    List<AuditTask> auditTaskList = iAuditTask.getAuditTaskList(sql.getMap()).getResult();
                    if (EpointCollectionUtils.isNotEmpty(auditTaskList)){
                        log.info("查找到auditTaskList！");
                        AuditTask auditTask = auditTaskList.get(0);
                        if (auditTask != null){
                            log.info("找到auditTask信息！");
                            auditProject.setTaskguid(auditTask.getRowguid());
                            auditProject.setOuname(auditTask.getOuname());
                            auditProject.setOuguid(auditTask.getOuguid());
                            auditProject.setAreacode(auditTask.getAreacode());
                            auditProject.setTask_id(auditTask.getTask_id());
                            auditProject.setTaskid(auditTask.getTask_id());

                            //个性化表单入库
                            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), false).getResult();
                            log.info("找到auditTaskExtension信息！==="+auditTaskExtension);
                            String epointsformurl = configservice.getFrameConfigValue("epointsformurl");
                            if (StringUtil.isNotBlank(epointsformurl) && !epointsformurl.endsWith("/")) {
                                epointsformurl = epointsformurl + "/";
                            }
                            if(auditTaskExtension!=null&&StringUtil.isNotBlank(auditTaskExtension.get("formid"))) {
                                //查询表单
                                JSONObject param = new JSONObject();
                                JSONObject taskparam = new JSONObject();
                                taskparam.put("formId", auditTaskExtension.getStr("formid"));
                                param.put("params", taskparam);
                                log.info("接口地址==="+epointsformurl);
                                log.info("表单接口getEpointSformInfo入参：===" + param.toJSONString());
                                String result = HttpUtil.doPost(epointsformurl + "rest/sform/getEpointSformInfo", param);
                                log.info("表单接口getEpointSformInfo返回：===" + result);
                                if(StringUtil.isNotBlank(result)){
                                    JSONObject jsonObject = JSONObject.parseObject(result);
                                    int intValue = jsonObject.getJSONObject("status").getIntValue("code");
                                    if(intValue==1){
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("custom").getJSONObject("formData").getJSONObject("formInfo");
                                        String sqltablename = jsonObject1.getString("sqltablename");
                                        String bjrowguid = applyElementData.elementText("SXBDBM");
                                        Element tablecols = applyElementData.element("SPSHENQIN").element("XML_DATA").element("SPItemListDef").element("TableCols");
                                        String sfz = tablecols.elementText("SFZHM82");
                                        if (StringUtil.isBlank(sfz)){
                                            sfz=UUID.randomUUID().toString();
                                        }
                                        List<Element> childElements = tablecols.elements();
                                        JSONArray array = new JSONArray();
                                        for (Element child : childElements) {
                                            String elementName = child.getName();
                                            String elementValue = child.getText();
                                            log.info("elementName==="+elementName+"==elementValue==="+elementValue);

                                            JSONObject jsonObject2 = new JSONObject();
                                            jsonObject2.put("name", elementName);
                                            jsonObject2.put("value", elementValue);
                                            array.add(jsonObject2);
                                        }
//                                        //所属地域
//                                        JSONArray array = new JSONArray();
//                                        JSONObject jsonObject2 = new JSONObject();
//                                        jsonObject2.put("name", "wbk3");
//                                        jsonObject2.put("value", tablecols.elementText("SSDY21"));
//                                        array.add(jsonObject2);
//                                        //申请人姓名
//                                        jsonObject2 = new JSONObject();
//                                        jsonObject2.put("name", "wbk4");
//                                        jsonObject2.put("value", tablecols.elementText("SQRXM32"));
//                                        array.add(jsonObject2);
//                                        //出生日期
//                                        jsonObject2 = new JSONObject();
//                                        jsonObject2.put("name", "qxz");
//                                        jsonObject2.put("value", tablecols.elementText("CSRQ99"));
//                                        array.add(jsonObject2);
//                                        //身份证号码
//                                        jsonObject2 = new JSONObject();
//                                        jsonObject2.put("name", "wbk5");
//                                        jsonObject2.put("value", tablecols.elementText("SFZHM82"));
//                                        array.add(jsonObject2);
//                                        //年龄
//                                        jsonObject2 = new JSONObject();
//                                        jsonObject2.put("name", "wbk2");
//                                        jsonObject2.put("value", tablecols.elementText("NL8"));
//                                        array.add(jsonObject2);
//                                        //性别
//                                        jsonObject2 = new JSONObject();
//                                        jsonObject2.put("name", "wbk1");
//                                        jsonObject2.put("value", tablecols.elementText("XB71"));
//                                        array.add(jsonObject2);
//                                        //联系方式
//                                        jsonObject2 = new JSONObject();
//                                        jsonObject2.put("name", "wbk6");
//                                        jsonObject2.put("value", tablecols.elementText("LXFS74"));
//                                        array.add(jsonObject2);

                                        param = new JSONObject();
                                        taskparam = new JSONObject();
                                        taskparam.put("tableName", sqltablename);
                                        taskparam.put("rowGuid", rowguid);
                                        taskparam.put("identityNum", sfz);
                                        taskparam.put("mainTable", array);
                                        taskparam.put("subTables", new JSONArray());
                                        taskparam.put("formId", auditTaskExtension.getStr("formid"));
                                        param.put("params", taskparam);
                                        log.info("表单接口savePageData入参：===" + param.toJSONString());
                                        result = HttpUtil.doPost(epointsformurl + "rest/sform/savePageData", param);
                                        log.info("表单接口savePageData返回：===" + result);
                                    }
                                }
                            }
                        }
                    }
                }
//                auditProject.setAreacode(applyElementData.elementText("ACTUALREGION"));

                //人员信息
                Element spRenYuan = applyElementData.element("SPRENYUAN");
                if (spRenYuan != null && StringUtil.isNotBlank(spRenYuan.elementText("IDENTITYTYPE"))){
                    auditProject.setCerttype("22");
                    auditProject.setCertnum(spRenYuan.elementText("IDCARDNO"));
                    auditProject.setApplyername(spRenYuan.elementText("NAME"));
                    auditProject.setContactmobile(spRenYuan.elementText("LINKPHONE"));
                    auditProject.setAddress(spRenYuan.elementText("HOMEADDRESS"));
                    auditProject.setApplyeruserguid(spRenYuan.elementText("USERID"));
                    auditProject.setApplyertype(20);
                }

                //企业信息
                Element spQiYe = applyElementData.element("SPQIYE");
                if (spQiYe != null && StringUtil.isNotBlank(spQiYe.elementText("ORGNAME"))){
                    auditProject.setCerttype("01");
                    auditProject.setCertnum(spQiYe.elementText("ORGCODE"));
                    auditProject.setApplyername(spQiYe.elementText("ORGNAME"));
                    auditProject.setLegalid(spQiYe.elementText("CERTIFICATENO"));
                    auditProject.setContactperson(spQiYe.elementText("LINKMAN"));
                    auditProject.setContactphone(spQiYe.elementText("CONTACTPHONE"));
                    auditProject.setContactfax(spQiYe.elementText("FAX"));
                    auditProject.setContactemail(spQiYe.elementText("LINKMANEMAIL"));
                    auditProject.setContactpostcode(spQiYe.elementText("LINKMANEMAIL"));
                    auditProject.setContactmobile(spQiYe.elementText("HANDLERPHONE"));
                    auditProject.setApplyertype(10);
                }

                //@author fryu 2024年3月6日 审批信息
                Element spshenqin = applyElementData.element("SPSHENQIN");
                if (spshenqin != null && StringUtil.isNotBlank(spshenqin.elementText("SXMC"))){
                    auditProject.setProjectname(spshenqin.elementText("SXMC"));
                    auditProject.setContactperson(spshenqin.elementText("LXRXM"));
                    auditProject.setContactcertnum(spshenqin.elementText("LXRSFZJHM"));
                    auditProject.setContactphone(spshenqin.elementText("LXRSJ"));
                    auditProject.setApplydate(EpointDateUtil.convertString2Date(spshenqin.elementText("SBSJ"),EpointDateUtil.DATE_TIME_FORMAT));
                }

                auditProject.setApplyway(10);
                auditProject.setStatus(12);
                auditProject.setPromise_day(0);
                auditProject.setTasktype(1);
                iAuditProject.addProject(auditProject);
                log.info("auditProject办件信息处理结束！rowguid = " + auditProject.getRowguid());


                //2.办件材料同步
                //构造入参
                Element YWLX = approvedatainfo.addElement("YWLX");
                YWLX.setText(expresstype);
                requestXml = applyDocument.asXML();

                headerMap = YwztRestUtil.getHeaderMap();
                String materialUrl = ConfigUtil.getConfigValue("ywztrest","ywztUrl") + "getApplyMaterialInfo";

                //请求体
                paramsMap = new JSONObject();
                if (StringUtil.isNotBlank(requestXml)){
                    log.info("构造请求头入参requestXml：" + requestXml);
                    requestXml = requestXml.replace("\n","");
                    requestXml = requestXml.replace("\r","");
                    log.info("去除换行空格：" + requestXml);
                    //处理换行的问题
                    String encryptResult = Sm4Util.encryptCbcPadding(sm4Key,requestXml);
                    log.info("加密后1：" + encryptResult);
                    encryptResult = encryptResult.replace("\n","").replace("\r","");
                    log.info("去除加密空格：" + encryptResult);
                    paramsMap.put("xmlStr",encryptResult);
                }
                String materialResult = YwztRestUtil.post(materialUrl,paramsMap);
                log.info("三方接口getApplyMaterialInfo接口返回：" + materialResult);

                interfaceName = "根据申报编号获取申请材料信息";
                //处理返回结果
                if (StringUtil.isBlank(materialResult)){
                    //记录调用接口日志
                    YwztRestUtil.insertApiYwztLog(materialUrl,requestXml,2,"三方接口getApplyMaterialInfo接口调用失败，返回结果为空，请联系管理员检查！",interfaceName);
                    msg = "三方接口getApplyMaterialInfo接口调用失败，请联系管理员检查！";
                    return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
                }

                //解析xml
                Document documentMaterialResult = DocumentHelper.parseText(materialResult);
                Element rootMaterialResult = documentMaterialResult.getRootElement();
//                JSONObject materialResultObj = JSONObject.parseObject(materialResult);
                if (!"200".equals(rootMaterialResult.elementText("code"))){
                    //记录调用接口日志
                    YwztRestUtil.insertApiYwztLog(materialUrl,requestXml,2,"三方接口getApplyMaterialInfo接口调用失败，status不为200，请联系管理员检查！" + materialResult,interfaceName);
                    msg = "三方接口getApplyMaterialInfo接口调用失败，请联系管理员检查！";
                    return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
                }

                //记录调用接口日志
                YwztRestUtil.insertApiYwztLog(materialUrl,requestXml,1,"调用成功！",interfaceName);

                //解析加密后的data
                JSONObject materialData = YwztRestUtil.decodeXmlData(materialResult);
//                String materialData = materialResultObj.getString("data");
                //解析加密的data数据
                String materialXml = materialData.getString("data");
                //解析xml
                Document materialResultDocument = DocumentHelper.parseText(materialXml);
                Element materialResultRoot = materialResultDocument.getRootElement();
                //从返回结果里找到材料list
                Element materialElementData = materialResultRoot.element("DATA");
                Element records = materialElementData.element("RECORDS");
                List<Element> recordList = records.elements("RECORD");
                if (EpointCollectionUtils.isNotEmpty(recordList)){
                    log.info("存在材料需要处理！");
                    Date now = new Date();
                    //循环遍历材料list
                    for (Element record : recordList) {
                        log.info("正在处理材料，SEQ:" + record.elementText("SEQ"));
                        AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                        auditProjectMaterial.setRowguid(record.elementText("SEQ"));
                        auditProjectMaterial.setProjectguid(auditProject.getRowguid());
                        //材料提交方式代码项转换
//                        String auditStatus = convertWjlxToAuditstatus(record.elementText("WJLX"));
//                        auditProjectMaterial.setAuditstatus(auditStatus);
                        //材料类型代码项转换
                        String auditStatus = convertCllxToAuditstatus(record.elementText("CLLX"));
                        auditProjectMaterial.setAuditstatus(auditStatus);

                        auditProjectMaterial.set("remark",record.elementText("REMARK"));

                        //材料编号
//                        String materialId = record.elementText("STUFF_SEQ");
                        String materialName = record.elementText("CLMC");
                        if (StringUtil.isNotBlank(materialName)){
                            //根据materialId找到audit_task_material数据
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("taskguid",auditProject.getTaskguid());
                            sql.eq("materialname",materialName);
                            List<AuditTaskMaterial> auditTaskMaterialList = iAuditTaskMaterial.selectMaterialListByCondition(sql.getMap()).getResult();
                            if (EpointCollectionUtils.isNotEmpty(auditTaskMaterialList)){
                                log.info("找到AuditTaskMaterial信息！");
                                AuditTaskMaterial auditTaskMaterial = auditTaskMaterialList.get(0);
                                auditProjectMaterial.setTaskguid(auditProject.getTaskguid());
                                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                            }
                        }

                        //下载附件到frame_attachinfo
                        String cliengguid = getDownLoadAttach(record,sm4Key,auditProjectMaterial);
                        auditProjectMaterial.setCliengguid(cliengguid);
                        auditProjectMaterial.setIs_rongque(0);
                        auditProjectMaterial.setOperatedate(now);

                        iAuditProjectMaterial.addProjectMateiral(auditProjectMaterial);
                    }
                    log.info("材料处理结束！");
                }
            }
            isSuccess = 1;
            msg = "";
            return YwztRestUtil.ywztRestXmlReturn("200","获取信息成功！","");
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("========调用sendApplyNO接口异常========");
            log.info("sendApplyNO接口入参：" + xmlStr);
            msg = e.getMessage();
        }
        finally {
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(request.getRequestURL().toString(),xmlStr,isSuccess,msg,"接收申报流水号接口");
            log.info("========结束调用sendApplyNO接口========");
        }
        return YwztRestUtil.ywztRestXmlReturn("0","sendApplyNO接口调用失败，请联系管理员检查！","");
    }

    /**
     * cancelProjectBeforeAccept 接收业务受理前撤销
     *
     * @author 成都研发4部-付荣煜
     * @date 2024/1/8 16:01
     * @param params
     * @return String
     */
    @RequestMapping("/cancelProjectBeforeAccept")
    public String cancelProjectBeforeAccept(@RequestBody String params, HttpServletRequest request){
        log.info("========开始调用cancelProjectBeforeAccept接口========");
        int isSuccess = 2; //用于判断接口是否调用成功
        String msg = "报错信息";
        try {
            if (StringUtil.isBlank(params)){
                msg = "接口入参为空，请联系管理员检查！";
                return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
            }

            JSONObject paramsObj = JSONObject.parseObject(params);
            String xmlStr = paramsObj.getString("xmlStr");
            if (StringUtil.isBlank(xmlStr)){
                msg = "接口必填入参xmlStr为空，请检查！";
                return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
            }

            String sm4Key = ConfigUtil.getConfigValue("ywztrest","sm4Key");
            log.info("接口入参中的xmlStr:" + xmlStr);
            xmlStr = Sm4Util.decryptCbcPadding(sm4Key,xmlStr);
            log.info("解密入参后:" + xmlStr);
            //解析xml
            Document document = DocumentHelper.parseText(xmlStr);
            Element root = document.getRootElement();

            //申办流水号
            String sblshShort = root.element("SBLSH_SHORT").getText();
            //撤销人姓名
            String cxrxm = root.element("CXRXM").getText();

            //根据办件流水号找到办件信息
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("flowsn",sblshShort);
            AuditProject auditProject = iAuditProject.getAuditProjectByCondition(sql.getMap());
            if (auditProject == null){
                msg = "未找到对应办件信息，请联系管理员检查！";
                return YwztRestUtil.ywztRestXmlReturn("0",msg,"");
            }

            auditProject.setStatus(98);
            iAuditProject.updateProject(auditProject);
            //撤销申请
            iHandleProject.handleReceive(auditProject,cxrxm,"","业务中台对接","");
            isSuccess = 1;
            msg = "";
            return YwztRestUtil.ywztRestXmlReturn("200","撤销成功！","");
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("========调用cancelProjectBeforeAccept接口异常========");
            log.info("cancelProjectBeforeAccept接口入参：" + params);
            msg = e.getMessage();
        }
        finally {
            log.info("========结束调用cancelProjectBeforeAccept接口========");
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(request.getRequestURL().toString(),params,isSuccess,msg,"接收业务受理前撤销");
        }
        return YwztRestUtil.ywztRestXmlReturn("0","cancelProjectBeforeAccept接口调用失败，请联系管理员检查！","");
    }

    /**
     * getDownLoadAttach 下载附件信息
     *
     * @author 成都研发4部-付荣煜
     * @date 2024/1/9 14:37
     * @param record
     * @return String
     */
    private String getDownLoadAttach(Element record,String sm4Key,AuditProjectMaterial auditProjectMaterial) {
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

        String attachName = record.elementText("ATTACH_NAME");
        String attachGuid = record.elementText("ATTACH_ID");
        String attachType = record.elementText("ATTACH_TYPE");
        String fileKey = record.elementText("ATTACH_PATH");
        String clientGuid = UUID.randomUUID().toString();
        //用于记录日志
        String msg = "";

        try {
            String url = ConfigUtil.getConfigValue("ywztrest","ywztDownFileUrl");

            //封装请求头
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("Content-Type","x-www-form-urlencoded");

            //请求体
            JSONObject paramsMap = new JSONObject();
            String downloadMode = "oss";
            //入参去除空格
            downloadMode = downloadMode.replace("\n","");
            downloadMode = downloadMode.replace("\r","");
            fileKey = fileKey.replace("\n","");
            fileKey = fileKey.replace("\r","");
            log.info("构造请求头入参downloadMode:" + downloadMode);
            log.info("构造请求头入参fileKey:" + fileKey);
            String encryptResultDownloadMode = Sm4Util.encryptCbcPadding(sm4Key,downloadMode);
            String encryptResultFileKey = Sm4Util.encryptCbcPadding(sm4Key,fileKey);
            log.info("加密后请求头入参downloadMode:" + encryptResultDownloadMode);
            log.info("加密后请求头入参fileKey:" + encryptResultFileKey);
            encryptResultDownloadMode = encryptResultDownloadMode.replace("\n","").replace("\r","");
            encryptResultFileKey = encryptResultFileKey.replace("\n","").replace("\r","");

            paramsMap.put("downloadMode",encryptResultDownloadMode);
            paramsMap.put("fileKey",encryptResultFileKey);
            String result = YwztRestUtil.post(url,paramsMap);
            log.info("三方接口附件下载接口返回：" + result);

            //处理返回结果
            if (StringUtil.isBlank(result)){
                //记录调用接口日志
                YwztRestUtil.insertApiYwztLog(url,paramsMap.toJSONString(),2,"三方接口getApplyMaterialInfo接口调用失败，返回结果为空，请联系管理员检查！",
                        "附件下载！");
                msg = "三方接口附件下载接口调用失败，请联系管理员检查！";
                //@author fryu 2024年3月28日 赋值材料状态
                auditProjectMaterial.setStatus(10);
                return clientGuid;
            }

            //解析json
            JSONObject resultObj = JSONObject.parseObject(result);

//            Document documentResult = DocumentHelper.parseText(result);
//            Element rootResult = documentResult.getRootElement();
//                JSONObject materialResultObj = JSONObject.parseObject(materialResult);
            if (!"200".equals(resultObj.getString("code"))){
                //记录调用接口日志
                YwztRestUtil.insertApiYwztLog(url,paramsMap.toJSONString(),2,"三方接口getApplyMaterialInfo接口调用失败，status不为200，请联系管理员检查！" + result,"附件下载！");
                msg = "三方接口附件下载接口调用失败，请联系管理员检查！";
                //@author fryu 2024年3月28日 赋值材料状态
                auditProjectMaterial.setStatus(10);
                return clientGuid;
            }

            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(url,paramsMap.toJSONString(),1,"调用成功！","附件下载！");

            //解析加密后的data
            JSONObject materialData = YwztRestUtil.decodeJSONData(result);
//                String materialData = materialResultObj.getString("data");
            //解析加密的data数据
            String materialJSON = materialData.getString("data");
            //解析json
            JSONObject materialObj = JSONObject.parseObject(materialJSON);
            String content = materialObj.getJSONObject("data").getString("fileContent");
            if (StringUtil.isBlank(content)){
                //@author fryu 2024年3月28日 赋值材料状态
                auditProjectMaterial.setStatus(10);
                log.info("当前无附件！");
                return clientGuid;
            }
            //保存附件到本地
            InputStream in = base2InputStream(content);
            FrameAttachInfo attachInfo = new FrameAttachInfo();
            attachInfo.setAttachGuid(attachGuid);
            attachInfo.setCliengGuid(clientGuid);
            attachInfo.setAttachFileName(attachName);
            attachInfo.setContentType(attachType);
            iAttachService.addAttach(attachInfo,in);
            //@author fryu 2024年3月28日 赋值材料状态
            auditProjectMaterial.setStatus(20);
            return clientGuid;
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("附件下载异常！");
            //@author fryu 2024年3月28日 赋值材料状态
            auditProjectMaterial.setStatus(10);
            return clientGuid;
        }
    }

    /**
     * base64 转 input流
     * @param base64字符串
     * @return 输入流
     */
    private static InputStream base2InputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }

    /**
     * convertCllxToAuditstatus 材料类型代码项转换
     *
     * @author 成都研发4部-付荣煜
     * @date 2024/1/9 14:14
     * @param cllx
     * @return String
     */
    private String convertCllxToAuditstatus(String cllx) {
        String result = "";
        if (StringUtil.isNotBlank(cllx)){
            switch (cllx){
                case "1":
                case "0"://纸质
                    result = "20";
                    break;
                case "2"://电子
                    result = "10";
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    /**
     * convertWjlxToAuditstatus 材料提交方式代码项转换
     *
     * @author 成都研发4部-付荣煜
     * @date 2024/1/9 14:13
     * @param wjlx
     * @return String
     */
    private String convertWjlxToAuditstatus(String wjlx) {
        String result = "";
        if (StringUtil.isNotBlank(wjlx)){
            switch (wjlx){
                case "0"://纸质
                    result = "20";
                    break;
                case "1":
                case "2":
                case "3"://电子
                    result = "10";
                    break;
                default:
                    break;
            }
        }
        return result;
    }
}
