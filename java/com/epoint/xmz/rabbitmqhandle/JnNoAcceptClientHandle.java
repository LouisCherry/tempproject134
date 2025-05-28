package com.epoint.xmz.rabbitmqhandle;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.ywztdj.util.Sm4Util;
import com.epoint.ywztdj.util.YwztRestUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: JnNoAcceptClientHandle
 * @Author 成都研发4部-付荣煜
 * @Date: 2024/1/15 15:11
 */
public class JnNoAcceptClientHandle extends AuditClientMessageListener {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    /**
     *  发送办件结果到自建系统操作逻辑
     *  @param proMessage 参数
     *  @return
     * @exception/throws
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        //办件主键
        String projectID = messageContent[0];
        //区划代码
        String areaCode = messageContent[1];

        //获取事项信息
        //AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, areaCode).getResult();

        //获取事项信息
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();

        //@author fryu 2024年1月16日 调用三方接口
        log.info("开始调用三方receiveAcceptInfo接口");
        //当前三方接口名称
        String interfaceName = "接收部门业务受理信息";
        String url = ConfigUtil.getConfigValue("ywztrest","ywztUrl") + "receiveAcceptInfo";

        String xmlStr = getXmlStr(auditProject,auditTask);
        //判断xmlStr是否为空
        if (StringUtil.isBlank(xmlStr)){
            YwztRestUtil.insertApiYwztLog(url,xmlStr,2,"三方接口receiveAcceptInfo接口调用失败，请求入参xmlStr为空，请联系管理员检查！",interfaceName);
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
        log.info("三方接口receiveAcceptInfo接口返回：" + result);

        //处理返回结果
        if (StringUtil.isBlank(result)){
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(url,xmlStr,2,"三方接口receiveAcceptInfo接口调用失败，返回结果为空，请联系管理员检查！",interfaceName);
            return;
        }

        //解密接口返回数据
        JSONObject resultObj = YwztRestUtil.decodeXmlData(result);
        if (!"200".equals(resultObj.getString("status"))){
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(url,xmlStr,2,"三方接口receiveAcceptInfo接口调用失败，status不为200，请联系管理员检查！" + result,interfaceName);
            return;
        }

        //请求成功
        log.info("调用三方receiveAcceptInfo接口成功！");
    }

    private String getXmlStr(AuditProject auditProject, AuditTask auditTask) {
        IAuditProjectOperation iAuditProjectOperation = ContainerFactory.getContainInfo().getComponent(IAuditProjectOperation.class);
        IAuditOrgaArea iAuditOrgaArea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);

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

            //业务预审信息
            Element YUSHEN = approvedatainfo.addElement("YUSHEN");
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("projectguid",auditProject.getRowguid());
            sql.eq("operatetype","10");
            AuditProjectOperation auditProjectOperation = iAuditProjectOperation.getAuditOperationByCondition(sql.getMap()).getResult();
            if (auditProjectOperation == null){
                log.info("调用三方receiveAcceptInfo接口失败，未找到AuditProjectOperation信息，projectGuid = " + auditProject.getRowguid());
                return result;
            }
            //业务预审时间
            Element YWYSSJ = YUSHEN.addElement("YWYSSJ");
            YWYSSJ.setText(EpointDateUtil.convertDate2String(auditProjectOperation.getOperatedate(),EpointDateUtil.DATE_TIME_FORMAT));
            //业务预审人名称
            Element YWYSRMC = YUSHEN.addElement("YWYSRMC");
            YWYSRMC.setText(auditProjectOperation.getOperateusername());
            //业务预审人工号
            Element YWYSRBM = YUSHEN.addElement("YWYSRBM");
            YWYSRBM.setText(auditProjectOperation.getOperateUserGuid());
            //业务预审状态
            Element YWYSZT = YUSHEN.addElement("YWYSZT");
            YWYSZT.setText("1");
            //业务预审意见
            Element YWYSYJ= YUSHEN.addElement("YWYSYJ");
            YWYSYJ.setText(auditProjectOperation.getRemarks());
            //业务预审区划名称
            Element YWYSQHMC= YUSHEN.addElement("YWYSQHMC");
            String areacode = auditProjectOperation.getAreaCode();
            String areaName = "";
            AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAreaByAreacode(areacode).getResult();
            if (auditOrgaArea != null){
                areaName = auditOrgaArea.getXiaquname();
            }
            YWYSQHMC.setText(areaName);

            //业务预审区划编码
            Element YWYSQHBM = YUSHEN.addElement("YWYSQHBM");
            YWYSQHBM.setText(areacode);
            //业务预审部门名称
            Element YWYSBMMC = YUSHEN.addElement("YWYSBMMC");
            //业务预审部门编码
            Element YWYSBMBM = YUSHEN.addElement("YWYSBMBM");
            String userGuid = auditProjectOperation.getOperateUserGuid();
            FrameOu ou = iOuService.getOuByUserGuid(userGuid);
            String ouName = "";
            String orgCode = "";
            if (ou != null){
                ouName = ou.getOuname();
                FrameOuExtendInfo ouExtendInfo = iOuService.getFrameOuExtendInfo(ou.getOuguid());
                if (ouExtendInfo != null){
                    orgCode = ouExtendInfo.getStr("ORGCODE");
                }
            }
            YWYSBMMC.setText(ouName);
            YWYSBMBM.setText(orgCode);

            //业务受理信息
            Element SHOULI = approvedatainfo.addElement("SHOULI");
            //业务受理时间
            Element YWSLSJ = SHOULI.addElement("YWSLSJ");
            YWSLSJ.setText(EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(),EpointDateUtil.DATE_TIME_FORMAT));
            //业务受理人名称
            Element YWSLRMC = SHOULI.addElement("YWSLRMC");
            YWSLRMC.setText(auditProject.getAcceptusername());
            //业务受理人工号
            Element YWSLRBM = SHOULI.addElement("YWSLRBM");
            YWSLRBM.setText(auditProject.getAcceptuserguid());
            //业务受理状态
            Element YWSLZT = SHOULI.addElement("YWSLZT");
            YWSLZT.setText("1");
            //业务受理意见
            Element YWSLYJ = SHOULI.addElement("YWSLYJ");
            YWSLYJ.setText("受理完成");
            //业务受理区划名称
            Element YWSLQHMC = SHOULI.addElement("YWSLQHMC");
            //业务受理区划编码
            Element YWSLQHBM = SHOULI.addElement("YWSLQHBM");
            //业务受理部门名称
            Element YWSLBMMC = SHOULI.addElement("YWSLQHBM");
            //业务受理部门编码
            Element YWSLBMBM = SHOULI.addElement("YWSLBMBM");
            String acceptUserguid = auditProject.getAcceptuserguid();
            FrameOu acceptOu = iOuService.getOuByUserGuid(acceptUserguid);
            String acceptOuname = "";
            String acceptAreacode = "";
            String acceptAreaname = "";
            String acceptOrgaCode = "";
            if (acceptOu != null){
                acceptOuname = acceptOu.getOuname();

                FrameOuExtendInfo acceptExtendinfo = iOuService.getFrameOuExtendInfo(acceptOu.getOuguid());
                if (acceptExtendinfo != null){
                    acceptOrgaCode = acceptExtendinfo.getStr("ORGCODE");
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("xiaqucode",acceptExtendinfo.getStr("areacode"));
                    AuditOrgaArea acceptOrgaArea = iAuditOrgaArea.getAuditArea(sqlConditionUtil.getMap()).getResult();
                    if (acceptOrgaArea != null){
                        acceptAreaname = acceptOrgaArea.getXiaquname();
                        acceptAreacode = acceptOrgaArea.getXiaqucode();
                    }
                }
            }
            YWSLQHMC.setText(acceptAreaname);
            YWSLQHBM.setText(acceptAreacode);
            YWSLBMMC.setText(acceptOuname);
            YWSLBMBM.setText(acceptOrgaCode);

            result = document.asXML();
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("调用三方receiveAcceptInfo接口异常，封装入参报错");
        }
        return result;
    }
}
