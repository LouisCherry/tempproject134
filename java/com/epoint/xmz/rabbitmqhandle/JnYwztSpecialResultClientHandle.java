package com.epoint.xmz.rabbitmqhandle;

import java.lang.invoke.MethodHandles;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.infapplyspecialresult.api.IInfApplySpecialresultRService;
import com.epoint.auditclient.infapplyspecialresult.api.entity.InfApplySpecialresultR;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.ywztdj.util.Sm4Util;
import com.epoint.ywztdj.util.YwztRestUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 接办分离特殊操作结果消费者客户端
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class JnYwztSpecialResultClientHandle extends AuditClientMessageListener{

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    /**
     *  特殊操作结果逻辑
     *  @param proMessage 参数
     *  @return    
     * @exception/throws 
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {
        IAuditProjectUnusual auditProjectUnusualService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        IInfApplySpecialresultRService infApplySpecialresultRService = ContainerFactory.getContainInfo()
                .getComponent(IInfApplySpecialresultRService.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo()
                .getComponent(ICodeItemsService.class);

        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        
        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        String projectID = messageContent[0];
        //特别程序标识
        String unusualGuid = messageContent[1];
        Thread.sleep(5 * 1000);
        InfApplySpecialresultR infApplySpecialresultR = new InfApplySpecialresultR();
        AuditProjectUnusual auditProjectUnusual = auditProjectUnusualService
                .getAuditProjectUnusualByRowguid(unusualGuid).getResult();

        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, "").getResult();

        //获取事项信息
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();

        // TODO
        infApplySpecialresultR.setRowguid(UUID.randomUUID().toString());
        infApplySpecialresultR.setProjectid(projectID);
        infApplySpecialresultR.setHandleusername(null);
        if (auditProjectUnusual != null) {
            infApplySpecialresultR.setSpecialname(codeItemsService.getItemTextByCodeName("办件特殊操作",
                    String.valueOf(auditProjectUnusual.getOperatetype())));
        }

        // 延期申请
        if (auditProjectUnusual!=null&&String.valueOf(auditProjectUnusual.getOperatetype()).equals(ZwfwConstant.BANJIANOPERATE_TYPE_YQ)) {
            infApplySpecialresultR.setSpecialtype("A");
            if ("1".equals(auditProjectUnusual.getAuditresult().toString())) {
                infApplySpecialresultR.setSpecialresult("审核通过");
            }
            else {
                infApplySpecialresultR.setSpecialresult("审核不通过");
            }
        }
        else {
            // 除延期申请没有审核流程
            infApplySpecialresultR.setSpecialtype("B");
            infApplySpecialresultR.setSpecialresult("通过");
        }
        infApplySpecialresultR.setSpecialopinion(null);

        infApplySpecialresultR.setEndtime(null);
        infApplySpecialresultR.setVersion(null);
        infApplySpecialresultR.setVersiontime(new Date());
        infApplySpecialresultR.setSpecialtime(new Date());
        infApplySpecialresultRService.insert(infApplySpecialresultR);

        //@author fryu 2024年1月15日 增加新的三方接口逻辑
        log.info("开始调用三方receiveSuspendInfo接口");
        //当前三方接口名称
        String interfaceName = "接收部门业务受理信息";
        String url = ConfigUtil.getConfigValue("ywztrest","ywztUrl") + "receiveSuspendInfo";

        String xmlStr = getXmlStr(auditProject,auditTask,auditProjectUnusual);
        //判断xmlStr是否为空
        if (StringUtil.isBlank(xmlStr)){
            YwztRestUtil.insertApiYwztLog(url,xmlStr,2,"三方接口receiveSuspendInfo接口调用失败，请求入参xmlStr为空，请联系管理员检查！",interfaceName);
            return;
        }

        //封装请求头
        Map<String,String> headerMap = YwztRestUtil.getHeaderMap();

        //请求体
        String sm4Key = ConfigUtil.getConfigValue("ywztrest","sm4Key");
        /*Map<String,Object> paramsMap = new HashMap<>();
        if (StringUtil.isNotBlank(xmlStr)){
            log.info("构造请求头入参xmlStr：" + xmlStr);
            xmlStr = xmlStr.replace("\n","");
            xmlStr = xmlStr.replace("\r","");
            log.info("去除换行空格：" + xmlStr);
            //处理换行的问题
            String encryptResult = Sm4Util.encryptCbcPadding(sm4Key,xmlStr);
            log.info("加密后：" + encryptResult);
            paramsMap.put("xmlStr", encryptResult);
        }
        String result = HttpUtil.doHttp(url,headerMap,paramsMap,"post",HttpUtil.RTN_TYPE_STRING);*/
        JSONObject paramsMap = new JSONObject();
        if (StringUtil.isNotBlank(xmlStr)){
            log.info("构造请求头入参xmlStr：" + xmlStr);
            xmlStr = xmlStr.replace("\n","");
            xmlStr = xmlStr.replace("\r","");
            log.info("去除换行空格：" + xmlStr);
            //处理换行的问题
            String encryptResult = Sm4Util.encryptCbcPadding(sm4Key,xmlStr);
            log.info("加密后：" + encryptResult);
            encryptResult = encryptResult.replace("\n","").replace("\r","");
            log.info("去除加密空格：" + encryptResult);
            paramsMap.put("xmlStr",encryptResult);
        }
        String result = YwztRestUtil.post(url,paramsMap);
        log.info("三方接口receiveSuspendInfo接口返回：" + result);

        //处理返回结果
        if (StringUtil.isBlank(result)){
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(url,xmlStr,2,"三方接口receiveSuspendInfo接口调用失败，返回结果为空，请联系管理员检查！",interfaceName);
            return;
        }

        //解密接口返回数据
        JSONObject resultObj = YwztRestUtil.decodeXmlData(result);
        if (!"200".equals(resultObj.getString("status"))){
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(url,xmlStr,2,"三方接口receiveSuspendInfo接口调用失败，status不为200，请联系管理员检查！" + result,interfaceName);
            return;
        }

        //请求成功
        log.info("调用三方receiveSuspendInfo接口成功！");
    }

    private String getXmlStr(AuditProject auditProject, AuditTask auditTask, AuditProjectUnusual unusual) {

        String result = "";
        try {
            //构造接口入参
            Document document = DocumentHelper.createDocument();
            //设置编码
            document.setXMLEncoding("utf-8");
            //创建根节点
            Element approvedatainfo = document.addElement("APPROVEDATAINFO");
            //在根节点加入子节点
            //流水号
            Element SBLSH_SHORT = approvedatainfo.addElement("SBLSH_SHORT");
            SBLSH_SHORT.setText(auditProject.getFlowsn());
            //事项编码
            Element SXBM = approvedatainfo.addElement("SXBM");
            SXBM.setText(auditTask.getTaskcode());
            Element SUSPENDTYPE = approvedatainfo.addElement("SUSPENDTYPE");
            SUSPENDTYPE.setText("0");

            //特别程序申请结果
            Element SPTEBIEJIEGUO = approvedatainfo.addElement("SPTEBIEJIEGUO");
            //特别程序结果
            Element TBCXJG = SPTEBIEJIEGUO.addElement("TBCXJG");
            TBCXJG.setText(unusual.getNote());
            //结果产生日期
            Element JGCSRQ = SPTEBIEJIEGUO.addElement("JGCSRQ");
            JGCSRQ.setText(EpointDateUtil.convertDate2String(unusual.getOperatedate(),EpointDateUtil.DATE_TIME_FORMAT));
            //特别程序结束日期
            Element TBCXJSRQ = SPTEBIEJIEGUO.addElement("TBCXJSRQ");
            TBCXJSRQ.setText(EpointDateUtil.convertDate2String(unusual.getOperatedate(),EpointDateUtil.DATE_TIME_FORMAT));
            //特别程序收费金额
            Element TBCXSFJE = SPTEBIEJIEGUO.addElement("TBCXSFJE");
            TBCXSFJE.setText("0");
            //金额单位代码
            Element JEDWDM = SPTEBIEJIEGUO.addElement("JEDWDM");
            JEDWDM.setText("无");
            //特别程序申请部门所在地行政区划代码
            Element XZQHDM = SPTEBIEJIEGUO.addElement("XZQHDM");
            XZQHDM.setText(auditProject.getAreacode());

            result = document.asXML();
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("调用三方receiveSuspendInfo接口异常，封装入参报错");
        }
        return result;
    }

}
