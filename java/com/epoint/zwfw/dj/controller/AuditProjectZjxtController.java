package com.epoint.zwfw.dj.controller;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.zwfw.dj.api.IJnDjService;

@RestController
@RequestMapping("/business")
public class AuditProjectZjxtController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IJnDjService iAuditProjectZjxtService;
    @Autowired
    private ICodeItemsService iCodeItemsService;


    /**
     * 市畜局办件基本信息对接
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/receiveBasicInfo",method = RequestMethod.POST)
    public  String ReceiveBasicInfo(@RequestBody String params, @Context HttpServletRequest request){
        try {
            log.info("======开始调用ReceiveBasicInfo接口======");

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = request.getHeader("token");
           // String token = jsonObject.getString("token");
            //校验token
            if (!(ZwdtConstant.SysValidateData.equals(token))) {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
            //获取参数
            JSONObject obj = JSONObject.parseObject(jsonObject.getString("data"));
            //入参
            String orgbusno = obj.getString("orgbusno");
            if (StringUtil.isBlank(orgbusno)){
                return JsonUtils.zwdtRestReturn("0", "orgbusno字段为空！", "");
            }
            String projid = obj.getString("projid");
            if (StringUtil.isBlank(projid)){
                return JsonUtils.zwdtRestReturn("0", "projid字段为空！", "");
            }
            String projpwd = obj.getString("projpwd");
            if (StringUtil.isBlank(projpwd)){
                return JsonUtils.zwdtRestReturn("0", "projpwd字段为空！", "");
            }
            String regionid = obj.getString("regionid");
            if (StringUtil.isBlank(regionid)){
                return JsonUtils.zwdtRestReturn("0", "regionid字段为空！", "");
            }
            String stdver = obj.getString("stdver");
            if (StringUtil.isBlank(stdver)){
                return JsonUtils.zwdtRestReturn("0", "stdver字段为空！", "");
            }
            String itemno = obj.getString("itemno");
            if (StringUtil.isBlank(itemno)){
                return JsonUtils.zwdtRestReturn("0", "itemno字段为空！", "");
            }
            String subitemno = obj.getString("subitemno");
            String itemname = obj.getString("itemname");
            if (StringUtil.isBlank(itemname)){
                return JsonUtils.zwdtRestReturn("0", "itemname字段为空！", "");
            }
            String subitemname = obj.getString("subitemname");
            String projectname = obj.getString("projectname");
            if (StringUtil.isBlank(projectname)){
                return JsonUtils.zwdtRestReturn("0", "projectname字段为空！", "");
            }
            String applicant = obj.getString("applicant");
            if (StringUtil.isBlank(applicant)){
                return JsonUtils.zwdtRestReturn("0", "applicant字段为空！", "");
            }
            String applicantmobile = obj.getString("applicantmobile");
            String applicanttel = obj.getString("applicanttel");
            String applicantemail = obj.getString("applicantemail");
            String acceptdeptid = obj.getString("acceptdeptid");
            if (StringUtil.isBlank(acceptdeptid)){
                return JsonUtils.zwdtRestReturn("0", "acceptdeptid字段为空！", "");
            }
            String acceptdeptname = obj.getString("acceptdeptname");
            if (StringUtil.isBlank(acceptdeptname)){
                return JsonUtils.zwdtRestReturn("0", "acceptdeptname字段为空！", "");
            }
            String approvaltype = obj.getString("approvaltype");
            if (StringUtil.isBlank(approvaltype)){
                return JsonUtils.zwdtRestReturn("0", "approvaltype字段为空！", "");
            }
            String promisetimelimit = obj.getString("promisetimelimit");
            if (StringUtil.isBlank(promisetimelimit)){
                return JsonUtils.zwdtRestReturn("0", "promisetimelimit字段为空！", "");
            }
            String promisetimeunit = obj.getString("promisetimeunit");
            if (StringUtil.isBlank(promisetimeunit)){
                return JsonUtils.zwdtRestReturn("0", "promisetimeunit字段为空！", "");
            }
            String timelimit = obj.getString("timelimit");
            String timeunit = obj.getString("timeunit");
            String submit = obj.getString("submit");
            String occurtime = obj.getString("occurtime");
            if (StringUtil.isBlank(occurtime)){
                return JsonUtils.zwdtRestReturn("0", "occurtime字段为空！", "");
            }
            String transactor = obj.getString("transactor");
            String innerno = obj.getString("innerno");
            String itemregionid = obj.getString("itemregionid");
            if (StringUtil.isBlank(itemregionid)){
                return JsonUtils.zwdtRestReturn("0", "itemregionid字段为空！", "");
            }
            String acceptdeptcode = obj.getString("acceptdeptcode");
            if (StringUtil.isBlank(acceptdeptcode)){
                return JsonUtils.zwdtRestReturn("0", "acceptdeptcode字段为空！", "");
            }
            String acceptdeptcode1 = obj.getString("acceptdeptcode1");
            if (StringUtil.isBlank(acceptdeptcode1)){
                return JsonUtils.zwdtRestReturn("0", "acceptdeptcode1字段为空！", "");
            }
            String acceptdeptcode2 = obj.getString("acceptdeptcode2");
            if (StringUtil.isBlank(acceptdeptcode2)){
                return JsonUtils.zwdtRestReturn("0", "acceptdeptcode2字段为空！", "");
            }
            String projectcode = obj.getString("projectcode");
            String acceptlist = obj.getString("acceptlist");
            if (StringUtil.isBlank(acceptlist)){
                return JsonUtils.zwdtRestReturn("0", "acceptlist字段为空！", "");
            }
            String applicantcode = obj.getString("applicantcode");
            String applicanttype = obj.getString("applicanttype");
            String itemtype = obj.getString("itemtype");
            if (StringUtil.isBlank(itemtype)){
                return JsonUtils.zwdtRestReturn("0", "itemtype字段为空！", "");
            }

            String applyerpagetype = obj.getString("applyerpagetype");
            if (StringUtil.isBlank(applyerpagetype)){
                return JsonUtils.zwdtRestReturn("0", "applyerpagetype字段为空！", "");
            }
            String applyerpagecode = obj.getString("applyerpagecode");
            if (StringUtil.isBlank(applyerpagecode)){
                return JsonUtils.zwdtRestReturn("0", "applyerpagecode字段为空！", "");
            }
            String catalogcode = obj.getString("catalogcode");
            if (StringUtil.isBlank(catalogcode)){
                return JsonUtils.zwdtRestReturn("0", "catalogcode字段为空！", "");
            }
            String localcatalogcode = obj.getString("localcatalogcode");
            String taskcode = obj.getString("taskcode");
            if (StringUtil.isBlank(taskcode)){
                return JsonUtils.zwdtRestReturn("0", "taskcode字段为空！", "");
            }
            String localtaskcode = obj.getString("localtaskcode");
            String taskhandleitem = obj.getString("taskhandleitem");
            String applyertype = obj.getString("applyertype");
            if (StringUtil.isBlank(applyertype)){
                return JsonUtils.zwdtRestReturn("0", "applyertype字段为空！", "");
            }
            String legalman = obj.getString("legalman");//不确定是否必填

            String applicantname = obj.getString("applicantname");
            String isengcons = obj.getString("isengcons");
            String hcpcode = obj.getString("hcpcode");
            String datasources = obj.getString("datasources");
            if (StringUtil.isBlank(datasources)){
                return JsonUtils.zwdtRestReturn("0", "datasources字段为空！", "");
            }else {
                datasources = iCodeItemsService.getItemTextByCodeName("对接数据来源", datasources);
            }
            String otherdatasources = obj.getString("otherdatasources");//未说明是否必填
            String scxfsign = obj.getString("scxfsign");//未说明是否必填
            String regioncode = obj.getString("regioncode");//未说明是否必填
            String deptcode = obj.getString("deptcode");//未说明是否必填

            //判断事项是否为空
            AuditTask auditBasicInfo = iAuditProjectZjxtService.getAuditBasicInfo(itemname, regionid.substring(0, 6));
            if (ValidateUtil.isNull(auditBasicInfo)){
                //提示事项不存在
                return JsonUtils.zwdtRestReturn("0", "【"+itemname+"】事项不存在！", "");
            }
            //填充办件基本信息
            Record record = new Record();
            record.setSql_TableName("audit_project_zjxt");


            String taskname = auditBasicInfo.getTaskname();
            String task_id = auditBasicInfo.getTask_id();
            String taskguid = auditBasicInfo.getRowguid();
            String areacode = auditBasicInfo.getAreacode();
            String ouguid = auditBasicInfo.getOuguid();
            String ouname = auditBasicInfo.getOuname();


            //字段转换；并存储
            record.set("rowguid",orgbusno);
            record.set("flowsn",projid);
            record.set("acceptareacode",itemregionid);
            record.set("taskid",task_id);
            record.set("taskguid",taskguid);
            record.set("projectname",taskname);
            record.set("tasktype",itemtype);
            record.set("areacode",areacode);
            record.set("ouguid",ouguid);
            record.set("ouname",ouname);
            record.set("applyername",applicant);
            record.set("applyertype",applyertype);
            record.set("certtype",applyerpagetype);
            record.set("certnum",applyerpagecode);
            record.set("legal",legalman);
            record.set("contactmobile",applicantmobile);
            record.set("CONTACTPHONE",applicanttel);
            record.set("CONTACTEMAIL",applicantemail);
            record.set("ACCEPTUSERDATE",occurtime);
            record.set("applydate",occurtime);
            record.set("ACCEPTUSERNAME",transactor);
            record.set("contactperson",applicantname);
            record.set("datasource","001");
            record.set("OperateUserName","畜牧局数据同步");
            record.set("OperateDate",new Date());
            record.set("STATUS","90");

//            record.set("",);
            Record RtrRecord = iAuditProjectZjxtService.getAuditProjectZjxtByRowguid(orgbusno);
            if (StringUtil.isNotBlank(RtrRecord)){
                return JsonUtils.zwdtRestReturn("0", "rowguid（orgbusno）为"+orgbusno+"的数据已存在！", "");
            }

            iAuditProjectZjxtService.insert(record);






            return JsonUtils.zwdtRestReturn("1", "办件数据接收成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("======调用ReceiveBasicInfo接口失败！======");
            return JsonUtils.zwdtRestReturn("0", "办件数据接收失败！", "");
        } finally {
            log.info("======调用ReceiveBasicInfo接口结束======");
        }

    }


    /**
     * 市畜局办件审批流程对接
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/receiveProc",method = RequestMethod.POST)
    public  String ReceiveProc(@RequestBody String params, @Context HttpServletRequest request){
        try {
            log.info("======开始调用ReceiveProc接口======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
//            String token = jsonObject.getString("token");
            String token = request.getHeader("token");
            //校验token
            if (!(ZwdtConstant.SysValidateData.equals(token))) {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
            //获取参数
            JSONObject obj = JSONObject.parseObject(jsonObject.getString("data"));
            String orgbusno = obj.getString("orgbusno");
            if (StringUtil.isBlank(orgbusno)){
                return JsonUtils.zwdtRestReturn("0", "orgbusno字段为空！", "");
            }
            String projid = obj.getString("projid");
            if (StringUtil.isBlank(projid)){
                return JsonUtils.zwdtRestReturn("0", "projid字段为空！", "");
            }
            String projectcode = obj.getString("projectcode");
            String itemregionid = obj.getString("itemregionid");
            if (StringUtil.isBlank(itemregionid)){
                return JsonUtils.zwdtRestReturn("0", "itemregionid字段为空！", "");
            }
            String stdver = obj.getString("stdver");
            if (StringUtil.isBlank(stdver)){
                return JsonUtils.zwdtRestReturn("0", "stdver字段为空！", "");
            }
            String sn = obj.getString("sn");
            if (StringUtil.isBlank(sn)){
                return JsonUtils.zwdtRestReturn("0", "sn字段为空！", "");
            }
            String nodename = obj.getString("nodename");
            if (StringUtil.isBlank(nodename)){
                return JsonUtils.zwdtRestReturn("0", "nodename字段为空！", "");
            }
            String nodecode = obj.getString("nodecode");
            String nodetype = obj.getString("nodetype");
            if (StringUtil.isBlank(nodetype)){
                return JsonUtils.zwdtRestReturn("0", "nodetype字段为空！", "");
            }
            String nodeprocer = obj.getString("nodeprocer");
            if (StringUtil.isBlank(nodeprocer)){
                return JsonUtils.zwdtRestReturn("0", "nodeprocer字段为空！", "");
            }
            String nodeprocername = obj.getString("nodeprocername");
            if (StringUtil.isBlank(nodeprocername)){
                return JsonUtils.zwdtRestReturn("0", "nodeprocername字段为空！", "");
            }
            String nodeprocerarea = obj.getString("nodeprocerarea");
            if (StringUtil.isBlank(nodeprocerarea)){
                return JsonUtils.zwdtRestReturn("0", "nodeprocerarea字段为空！", "");
            }
            String regionid = obj.getString("regionid");
            if (StringUtil.isBlank(regionid)){
                return JsonUtils.zwdtRestReturn("0", "regionid字段为空！", "");
            }
            String procunit = obj.getString("procunit");
            if (StringUtil.isBlank(procunit)){
                return JsonUtils.zwdtRestReturn("0", "procunit字段为空！", "");
            }
            String nodestate = obj.getString("nodestate");
            if (StringUtil.isBlank(nodestate)){
                return JsonUtils.zwdtRestReturn("0", "nodestate字段为空！", "");
            }
            String nodestarttime = obj.getString("nodestarttime");
            if (StringUtil.isBlank(nodestarttime)){
                return JsonUtils.zwdtRestReturn("0", "nodestarttime字段为空！", "");
            }
            String nodeendtime = obj.getString("nodeendtime");
            String nodeadv = obj.getString("nodeadv");
            String timelimit = obj.getString("timelimit");
            String promisetimeunit = obj.getString("promisetimeunit");
            String noderesult = obj.getString("noderesult");
            if (StringUtil.isBlank(noderesult)){
                return JsonUtils.zwdtRestReturn("0", "noderesult字段为空！", "");
            }
            String occurtime = obj.getString("occurtime");
            if (StringUtil.isBlank(occurtime)){
                return JsonUtils.zwdtRestReturn("0", "occurtime字段为空！", "");
            }
            String notice = obj.getString("notice");
            String datasources = obj.getString("datasources");
            if (StringUtil.isBlank(datasources)){
                return JsonUtils.zwdtRestReturn("0", "datasources字段为空！", "");
            }
            String otherdatasources = obj.getString("otherdatasources");
            String scxfsign = obj.getString("scxfsign");
            String regioncode = obj.getString("regioncode");
            String deptcode = obj.getString("deptcode");
            String procunitname = obj.getString("procunitname");
            if (StringUtil.isBlank(procunitname)){
                return JsonUtils.zwdtRestReturn("0", "procunitname字段为空！", "");
            }


            //填充办件基本信息
            Record record = new Record();
            record.setSql_TableName("AUDIT_RS_APPLY_PROCESS_ZJXT");
            record.set("RowGuid",orgbusno);
            record.set("PROJECTID",projid);
            record.set("NODENAME",nodename);
            record.set("ORG_ID",procunit);
            record.set("HANDLEUSERNAME",procunitname);

            Record auditRsApplyZjxtByRowguid = iAuditProjectZjxtService.getAuditRsApplyZjxtByRowguid(orgbusno);
            if (StringUtil.isNotBlank(auditRsApplyZjxtByRowguid)){
                return JsonUtils.zwdtRestReturn("0", "rowguid（orgbusno）为"+orgbusno+"的数据已存在！", "");
            }

            iAuditProjectZjxtService.insert(record);

            return JsonUtils.zwdtRestReturn("1", "办件数据接收成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("======调用ReceiveProc接口失败======");
            return JsonUtils.zwdtRestReturn("0", "办件数据接收失败！", "");
        } finally {
            log.info("======调用ReceiveProc接口结束======");
        }



    }
}
