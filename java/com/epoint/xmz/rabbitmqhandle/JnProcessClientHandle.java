package com.epoint.xmz.rabbitmqhandle;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.infapplyprocess.api.IInfApplyProcessRService;
import com.epoint.auditclient.infapplyprocess.api.entity.InfApplyProcessR;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.execute.WorkflowActivityInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.ywztdj.util.Sm4Util;
import com.epoint.ywztdj.util.YwztRestUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 接办分离办件过程消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class JnProcessClientHandle extends AuditClientMessageListener {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 办理环节操作逻辑
     *
     * @param proMessage 参数
     * @return
     * @exception/throws
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {
        IWFInstanceAPI9 wfInstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
        IInfApplyProcessRService infApplyProcessRService = ContainerFactory.getContainInfo()
                .getComponent(IInfApplyProcessRService.class);
        IWFDefineAPI9 wfDefine = ContainerFactory.getContainInfo().getComponent(IWFDefineAPI9.class);
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        //办件主键
        String projectID = messageContent[0];
        //流程实例标识
        String pviGuid = messageContent[1];
        //工作项标识
        String workitemGuid = messageContent[2];
        //变迁标识
        String transitionGuid = messageContent[3];
        ProcessVersionInstance pvi = wfInstance.getProcessVersionInstance(pviGuid);
        WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pvi, workitemGuid);
        WorkflowActivityInstance workflowActivityInstance = wfInstance.getActivityInstance(pvi,
                workflowWorkItem.getActivityInstanceGuid());
        WorkflowTransition workflowTransition = wfDefine.getTransition(pvi, transitionGuid);
        String nextNodeName = wfDefine.getActivity(pvi, workflowTransition.getToActivityGuid()).getActivityName();
        // TODO
        InfApplyProcessR infApplyProcessR = new InfApplyProcessR();
        infApplyProcessR.setRowguid(UUID.randomUUID().toString());
        infApplyProcessR.setProjectid(projectID);
        infApplyProcessR.setAction("通过");
        infApplyProcessR.setNodename(workflowWorkItem.getActivityName());
        infApplyProcessR.setNextnodename(nextNodeName);
        infApplyProcessR.setHandleusername(workflowWorkItem.getTransactorName());
        infApplyProcessR.setHandleopinion(workflowWorkItem.getOpinion());
        infApplyProcessR.setStarttime(workflowActivityInstance.getStartDate());
        infApplyProcessR.setEndtime(workflowActivityInstance.getEndTime());
        infApplyProcessR.setNote(null);
        infApplyProcessR.setVersiontime(new Date());
        infApplyProcessR.setVersion(null);
        infApplyProcessR.setEventname("");
        infApplyProcessRService.insert(infApplyProcessR);

        //@author fryu 2024年1月15日 增加新的三方接口逻辑
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, "")
                .getResult();

        //获取事项信息
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();

        //当前三方接口名称
        String interfaceName = "接收部门环节信息";
        String url = ConfigUtil.getConfigValue("ywztrest", "ywztUrl") + "receiveCourseInfo";

        String xmlStr = getXmlStr(auditProject, auditTask, workflowWorkItem);

        //判断xmlStr是否为空
        if (StringUtil.isBlank(xmlStr)) {
            YwztRestUtil.insertApiYwztLog(url, xmlStr, 2, "三方接口receiveCourseInfo接口调用失败，请求入参xmlStr为空，请联系管理员检查！", interfaceName);
            return;
        }

        //封装请求头
        Map<String, String> headerMap = YwztRestUtil.getHeaderMap();

        //请求体
        String sm4Key = ConfigUtil.getConfigValue("ywztrest", "sm4Key");

        JSONObject paramsMap = new JSONObject();
        if (StringUtil.isNotBlank(xmlStr)) {
            xmlStr = xmlStr.replace("\n", "");
            xmlStr = xmlStr.replace("\r", "");
            //处理换行的问题
            String encryptResult = Sm4Util.encryptCbcPadding(sm4Key, xmlStr);
            encryptResult = encryptResult.replace("\n", "").replace("\r", "");
            paramsMap.put("xmlStr", encryptResult);
        }
        String result = YwztRestUtil.post(url, paramsMap);

        //处理返回结果
        if (StringUtil.isBlank(result)) {
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(url, xmlStr, 2, "三方接口receiveCourseInfo接口调用失败，返回结果为空，请联系管理员检查！", interfaceName);
            return;
        }

        //解密接口返回数据
        JSONObject resultObj = YwztRestUtil.decodeXmlData(result);
        if (!"200".equals(resultObj.getString("status"))) {
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(url, xmlStr, 2, "三方接口receiveCourseInfo接口调用失败，status不为200，请联系管理员检查！" + result, interfaceName);
            return;
        }

    }

    private String getXmlStr(AuditProject auditProject, AuditTask auditTask, WorkflowWorkItem workflowWorkItem) {
        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IUserService iUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);

        String result = "";
        try {
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

            //过程信息
            Element GDBSSPCL = approvedatainfo.addElement("GDBSSPCL");
            //审批环节代码
            Element SPHJDM = GDBSSPCL.addElement("SPHJDM");
            SPHJDM.setText(workflowWorkItem.getActivityName());
            //审批环节名称
            Element SPHJMC = GDBSSPCL.addElement("SPHJMC");
            SPHJMC.setText(workflowWorkItem.getActivityName());
            //审批部门名称
            Element SPBMMC = GDBSSPCL.addElement("SPHJDM");
            //审批部门组织机构代码
            Element SPBMZZJDMD = GDBSSPCL.addElement("SPBMZZJDMD");
            //审批部门所在地行政区域代码
            Element XZQHDM = GDBSSPCL.addElement("XZQHDM");
            String ouguid = workflowWorkItem.getOuguid();
            FrameOu ou = iOuService.getOuByOuGuid(ouguid);
            String ouName = "";
            String orgCode = "";
            String areaCode = "";
            if (ou != null) {
                ouName = ou.getOuname();
                FrameOuExtendInfo ouExtendInfo = iOuService.getFrameOuExtendInfo(ouguid);
                if (ouExtendInfo != null) {
                    orgCode = ouExtendInfo.getStr("ORGCODE");
                    areaCode = ouExtendInfo.getStr("areacode");
                }
            }
            SPBMMC.setText(ouName);
            SPBMZZJDMD.setText(orgCode);
            XZQHDM.setText(areaCode);

            //@author fryu 2024年1月23日 获取审批人姓名
            String spUserName = iUserService.getUserNameByUserGuid(workflowWorkItem.getOperatorGuid());
            workflowWorkItem.setOperatorName(spUserName);
            //审批人姓名
            Element SPRXM = GDBSSPCL.addElement("SPRXM");
            SPRXM.setText(workflowWorkItem.getOperatorName());
            //审批人职务代码
            Element SPRZWDM = GDBSSPCL.addElement("SPRZWDM");
            SPRZWDM.setText("无");
            //审批人职务名称
            Element SPRZWMC = GDBSSPCL.addElement("SPRZWMC");
            SPRZWMC.setText("无");
            //审批环节状态代码
            Element SPHJZTDM = GDBSSPCL.addElement("SPHJZTDM");
            SPHJZTDM.setText("无");
            //审批意见
            Element SPYJ = GDBSSPCL.addElement("SPYJ");
            SPYJ.setText(workflowWorkItem.getOperatorName());
            //环节开始时间
            Element JSSJ = GDBSSPCL.addElement("JSSJ");
            JSSJ.setText(EpointDateUtil.convertDate2String(workflowWorkItem.getCreateDate(), EpointDateUtil.DATE_TIME_FORMAT));
            //环节结束时间
            Element SPSJ = GDBSSPCL.addElement("SPSJ");
            SPSJ.setText(EpointDateUtil.convertDate2String(workflowWorkItem.getEndDate(), EpointDateUtil.DATE_TIME_FORMAT));

            //@author fryu 2024年1月26日 补充字段
            //流水号
            Element GDBSSPCL_SBLSH_SHORT = GDBSSPCL.addElement("SBLSH_SHORT");
            GDBSSPCL_SBLSH_SHORT.setText(auditProject.getFlowsn());
            //事项编码
            Element GDBSSPCL_SXBM = GDBSSPCL.addElement("SXBM");
            GDBSSPCL_SXBM.setText(auditTask.getTaskcode());
            result = document.asXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
