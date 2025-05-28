package com.epoint.xmz.rabbitmqhandle;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.ywztdj.util.Sm4Util;
import com.epoint.ywztdj.util.YwztRestUtil;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.util.TARequestUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 接办分离特殊操作消费者客户端
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class JnSpecialClientHandle extends AuditClientMessageListener{
	
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String lclcurl= ConfigUtil.getConfigValue("epointframe", "lclcurl");
	
	
    /**
     *  特殊操作逻辑
     *  @param proMessage 参数
     *  @return    
     * @exception/throws 
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {

    	IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
    	IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
    	
    	IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);
    	IAuditProjectUnusual iAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IAuditProjectUnusual.class);

        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        String projectID = messageContent[0];
        //申请人姓名
        String areaCode = messageContent[1];
        //特别程序（特殊操作）的标识
        String unusualGuid = messageContent[2];
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, areaCode).getResult();
        
        //获取事项信息
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
        
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
        
        String resultsign2 = "";
        		
        JSONObject submit2 = new JSONObject();
        
        submit2.put("projectGuid", projectID);
        submit2.put("areaCode", areaCode);
        submit2.put("status", "02");
        
        if("1".equals(auditTaskExtension.getStr("is_jianguanlc"))) {
        	  resultsign2 = TARequestUtil.sendPostInner(lclcurl, submit2.toJSONString(), "", "");
              log.info("推送浪潮受理流程结果："+resultsign2+";办件编号为："+projectID);
        }

        //@author fryu 2024年1月15日 增加新的三方接口逻辑
        log.info("开始调用三方receiveSuspendInfo接口");
        AuditProjectUnusual unusual = iAuditProjectUnusual.getAuditProjectUnusualByRowguid(unusualGuid).getResult();
        //当前三方接口名称
        String interfaceName = "接收部门特别程序信息";
        String url = ConfigUtil.getConfigValue("ywztrest","ywztUrl") + "receiveSuspendInfo";

        String xmlStr = getXmlStr(auditProject,auditTask,unusual);
        //判断xmlStr是否为空
        if (StringUtil.isBlank(xmlStr)){
            YwztRestUtil.insertApiYwztLog(url,xmlStr,2,"三方接口receiveSuspendInfo接口调用失败，请求入参xmlStr为空，请联系管理员检查！",interfaceName);
            return;
        }

        //封装请求头
        Map<String,String> headerMap = YwztRestUtil.getHeaderMap();

        //请求体
        String sm4Key = ConfigUtil.getConfigValue("ywztrest","sm4Key");
       /* Map<String,Object> paramsMap = new HashMap<>();
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
        IWFInstanceAPI9 wfInstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
        IAuditTaskRiskpoint iAuditTaskRiskpoint = ContainerFactory.getContainInfo().getComponent(IAuditTaskRiskpoint.class);

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

            //特别程序申请
            Element SPTEBIESHENQING = approvedatainfo.addElement("SPTEBIESHENQING");
            //特别程序种类
            Element TBCXZL = SPTEBIESHENQING.addElement("TBCXZL");
            TBCXZL.setText("A");
            //特别程序种类名称
            Element TBCXZLMC = SPTEBIESHENQING.addElement("TBCXZLMC");
            TBCXZLMC.setText("延长审批");
            //特别程序开始日期
            Element TBCXKSRQ = SPTEBIESHENQING.addElement("TBCXKSRQ");
            TBCXKSRQ.setText(EpointDateUtil.convertDate2String(unusual.getOperatedate(),EpointDateUtil.DATE_TIME_FORMAT));
            //特别程序批准人
            Element TBCXPZR = SPTEBIESHENQING.addElement("TBCXPZR");
            TBCXPZR.setText("无");
            //特别程序启动理由或依据
            Element TBCXQDLY = SPTEBIESHENQING.addElement("TBCXQDLY");
            TBCXQDLY.setText(unusual.getNote());
            //申请内容
            Element SQNR = SPTEBIESHENQING.addElement("SQNR");
            String sqnrText = convertSqnrToText(unusual.getOperatetype());
            SQNR.setText(sqnrText);

            //特别程序时限
            Element TBCXSX = SPTEBIESHENQING.addElement("TBCXSX");
            String anticipate_day = "";
            //查找流程信息
            ProcessVersionInstance pvi = wfInstance.getProcessVersionInstance(auditProject.getPviguid());
            WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pvi, unusual.getWorkitemguid());
            //查找audit_task_riskpoint信息
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("taskguid",auditProject.getTaskguid());
            sql.eq("activityguid",workflowWorkItem.getActivityGuid());
            List<AuditTaskRiskpoint> riskpointList = iAuditTaskRiskpoint.getResultRiskpointByCondition(sql.getMap()).getResult();
            if (EpointCollectionUtils.isNotEmpty(riskpointList)){
                AuditTaskRiskpoint riskpoint = riskpointList.get(0);
                anticipate_day = riskpoint.getAnticipate_day() + "";
            }
            TBCXSX.setText(anticipate_day);

            //特别程序时限单位
            Element TBCXSXDW = SPTEBIESHENQING.addElement("TBCXSXDW");
            TBCXSXDW.setText("G");
            //特别程序申请部门所在地行政区划代码
            Element XZQHDM = SPTEBIESHENQING.addElement("XZQHDM");
            XZQHDM.setText(auditProject.getAreacode());

            result = document.asXML();
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("调用三方receiveSuspendInfo接口异常，封装入参报错");
        }
        return result;
    }

    private String convertSqnrToText(Integer operatetype) {
        String result = "其他";
        switch (operatetype){
            case 10:
                result = "暂停计时";
                break;
            case 11:
                result = "恢复";
                break;
            case 20:
                result = "延期";
                break;
            case 21:
                result = "延期退回";
                break;
            case 14:
                result = "预审退回";
                break;
            case 97:
            case 31:
                result = "不予受理";
                break;
            case 99:
                result = "异常终止";
                break;
            case 98:
                result = "撤销申请";
                break;
            default:
                break;
        }
        return result;
    }

}
