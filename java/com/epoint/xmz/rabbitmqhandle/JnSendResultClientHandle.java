package com.epoint.xmz.rabbitmqhandle;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.ywztdj.util.Sm4Util;
import com.epoint.ywztdj.util.YwztRestUtil;
import org.apache.commons.codec.digest.DigestUtils;
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
 * 接办分离办件结果消费者客户端
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class JnSendResultClientHandle extends AuditClientMessageListener{
	
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String lclcurl= ConfigUtil.getConfigValue("epointframe", "lclcurl");
	

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
    	
    	IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);
        
        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        //办件主键
        String projectID = messageContent[0];
        //区划代码
        String areaCode = messageContent[1];
        //办理人员姓名
        String userName = messageContent[2];
        
        JSONObject submit2 = new JSONObject();
        
        submit2.put("projectGuid", projectID);
        submit2.put("areaCode", areaCode);
        submit2.put("status", "04");
        
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, areaCode).getResult();
        
        //获取事项信息
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
        
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
        
        String resultsign2 = "";
        		
        if("1".equals(auditTaskExtension.getStr("is_jianguanlc"))) {
        	resultsign2 = TARequestUtil.sendPostInner(lclcurl, submit2.toJSONString(), "", "");
        	  log.info("推送浪潮受理流程结果："+resultsign2+";办件编号为："+projectID);
        }

        //@author fryu 2024年1月16日 调用三方接口
        log.info("开始调用三方receiveFinishInfo接口");
        //当前三方接口名称
        String interfaceName = "接收部门办结信息";
        String finishUrl = ConfigUtil.getConfigValue("ywztrest","ywztUrl") + "receiveFinishInfo";

        String xmlStr = getFinishXmlStr(auditProject,auditTask);
        //判断xmlStr是否为空
        if (StringUtil.isBlank(xmlStr)){
            YwztRestUtil.insertApiYwztLog(finishUrl,xmlStr,2,"三方接口receiveFinishInfo接口调用失败，请求入参xmlStr为空，请联系管理员检查！",interfaceName);
            return;
        }

        //封装请求头
        //封装请求头
        Map<String,String> headerMap = YwztRestUtil.getHeaderMap();

        //请求体
        String sm4Key = ConfigUtil.getConfigValue("ywztrest","sm4Key");

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
        String result = YwztRestUtil.post(finishUrl,paramsMap);
        log.info("三方接口receiveFinishInfo接口返回：" + result);

        //处理返回结果
        if (StringUtil.isBlank(result)){
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(finishUrl,xmlStr,2,"三方接口receiveFinishInfo接口调用失败，返回结果为空，请联系管理员检查！",interfaceName);
            return;
        }

        //解密接口返回数据
        JSONObject resultObj = YwztRestUtil.decodeXmlData(result);
        if (!"200".equals(resultObj.getString("status"))){
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(finishUrl,xmlStr,2,"三方接口receiveFinishInfo接口调用失败，status不为200，请联系管理员检查！" + result,interfaceName);
            return;
        }

        //请求成功
        log.info("调用三方receiveFinishInfo接口成功！");

        log.info("开始调用三方receiveResultFileInfo接口");
        //当前三方接口名称
        interfaceName = "接收部门审批结果文件";
        String fileUrl = ConfigUtil.getConfigValue("ywztrest","ywztUrl") + "receiveResultFileInfo";

        xmlStr = getFileXmlStr(auditProject,auditTask);
        //判断xmlStr是否为空
        if (StringUtil.isBlank(xmlStr)){
            YwztRestUtil.insertApiYwztLog(fileUrl,xmlStr,2,"三方接口receiveResultFileInfo接口调用失败，请求入参xmlStr为空，请联系管理员检查！",interfaceName);
            return;
        }

        //封装请求头
        headerMap = YwztRestUtil.getHeaderMap();

        //请求体
        paramsMap = new JSONObject();
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
        result = YwztRestUtil.post(fileUrl,paramsMap);
        /*if (StringUtil.isNotBlank(xmlStr)){
            log.info("构造请求头入参xmlStr：" + xmlStr);
            xmlStr = xmlStr.replace("\n","");
            xmlStr = xmlStr.replace("\r","");
            log.info("去除换行空格：" + xmlStr);
            //处理换行的问题
            String encryptResult = Sm4Util.encryptCbcPadding(sm4Key,xmlStr);
            log.info("加密后：" + encryptResult);
            paramsMap.put("xmlStr", encryptResult);
        }
        result = HttpUtil.doHttp(fileUrl,headerMap,paramsMap,"post",HttpUtil.RTN_TYPE_STRING);*/
        log.info("三方接口receiveResultFileInfo接口返回：" + result);

        //处理返回结果
        if (StringUtil.isBlank(result)){
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(fileUrl,xmlStr,2,"三方接口receiveResultFileInfo接口调用失败，返回结果为空，请联系管理员检查！",interfaceName);
            return;
        }

        //解密接口返回数据
        resultObj = YwztRestUtil.decodeXmlData(result);
        if (!"200".equals(resultObj.getString("status"))){
            //记录调用接口日志
            YwztRestUtil.insertApiYwztLog(fileUrl,xmlStr,2,"三方接口receiveResultFileInfo接口调用失败，status不为200，请联系管理员检查！" + result,interfaceName);
            return;
        }

        //请求成功
        log.info("调用三方receiveResultFileInfo接口成功！");
        
    }

    private String getFileXmlStr(AuditProject auditProject, AuditTask auditTask) {
        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

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

            //数据集合
            Element SPSHENPIJIEGUODATA = approvedatainfo.addElement("SPSHENPIJIEGUODATA");

            //找到证照信息
            String certGuid = auditProject.getCertrowguid();
            CertInfo certInfo = iCertInfo.getCertInfoByRowguid(certGuid);
            if (certInfo == null){
                log.info("未找到证照信息！");
                return result;
            }

            //部门信息
            FrameOu ou = iOuService.getOuByOuGuid(auditProject.getOuguid());
            String ouOrgCode = "";
            String ouAreacode = "";
            if (ou != null){
                FrameOuExtendInfo banjieOuExtend = iOuService.getFrameOuExtendInfo(ou.getOuguid());
                if (banjieOuExtend != null){
                    ouOrgCode = banjieOuExtend.getStr("ORGCODE");
                    ouAreacode = banjieOuExtend.getStr("areacode");
                }
            }

            //找到材料信息
            String cliengguid = certInfo.getCertcliengguid();
            List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengguid);
            if (EpointCollectionUtils.isNotEmpty(attachInfoList)){
                for (FrameAttachInfo attachInfo : attachInfoList) {
                    Element SPSHENPIJIEGUO = SPSHENPIJIEGUODATA.addElement("SPSHENPIJIEGUO");
                    //证照编码
                    Element ZZBM = SPSHENPIJIEGUO.addElement("ZZBM");
                    ZZBM.setText(certInfo.getCertno());
                    //证照名称
                    Element ZZMC = SPSHENPIJIEGUO.addElement("ZZMC");
                    ZZMC.setText(certInfo.getCertname());
                    //证照名称
                    Element ATTACH_NAME = SPSHENPIJIEGUO.addElement("ATTACH_NAME");
                    ATTACH_NAME.setText(attachInfo.getAttachFileName());
                    //附件UUID
                    Element ATTACH_ID = SPSHENPIJIEGUO.addElement("ATTACH_ID");
                    ATTACH_ID.setText(attachInfo.getAttachGuid());
                    //附件签名
                    Element ATTACH_SIGN = SPSHENPIJIEGUO.addElement("ATTACH_SIGN");
                    String attach_sign = DigestUtils.md5Hex(iAttachService.getInputStreamByInfo(attachInfo));
                    ATTACH_SIGN.setText(attach_sign);
                    //附件类型
                    Element ATTACH_TYPE = SPSHENPIJIEGUO.addElement("ATTACH_TYPE");
                    ATTACH_TYPE.setText(attachInfo.getAttachFileName());
                    //存储类型
                    Element SAVE_TYPE = SPSHENPIJIEGUO.addElement("SAVE_TYPE");
                    SAVE_TYPE.setText("1");
                    //附件文件流
                    Element ATTACH_BODY = SPSHENPIJIEGUO.addElement("ATTACH_BODY");
                    ATTACH_BODY.setText("无");
                    //附件路径/网盘ID
                    Element ATTACH_PATH = SPSHENPIJIEGUO.addElement("ATTACH_PATH");
                    ATTACH_PATH.setText("无");
                    //部门组织机构代码)
                    Element SLBMZZJGDM = SPSHENPIJIEGUO.addElement("SLBMZZJGDM");
                    SLBMZZJGDM.setText(ouOrgCode);
                    //部门所在行政区划代码
                    Element XZQHDM = SPSHENPIJIEGUO.addElement("XZQHDM");
                    XZQHDM.setText(ouAreacode);

                }
            }

            result = document.asXML();
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("调用三方receiveFinishInfo接口异常，封装入参报错");
        }
        return result;
    }

    private String getFinishXmlStr(AuditProject auditProject, AuditTask auditTask) {
        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);

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
            Element SPBANJIE = approvedatainfo.addElement("SPBANJIE");
            //办结部门名称
            Element BJBMMC = SPBANJIE.addElement("BJBMMC");
            //办结部门组织机构代码
            Element BJBMZZJDDM = SPBANJIE.addElement("BJBMZZJDDM");
            //办结部门行政区划代码
            Element XZQHDM = SPBANJIE.addElement("XZQHDM");
            String banjieUserGuid = auditProject.getBanjieuserguid();
            FrameOu banjieOu = iOuService.getOuByUserGuid(banjieUserGuid);
            String banjieOuName = "";
            String banjieOrgCode = "";
            String banjieAreacode = "";
            if (banjieOu != null){
                banjieOuName = banjieOu.getOuname();
                FrameOuExtendInfo banjieOuExtend = iOuService.getFrameOuExtendInfo(banjieOu.getOuguid());
                if (banjieOuExtend != null){
                    banjieOrgCode = banjieOuExtend.getStr("ORGCODE");
                    banjieAreacode = banjieOuExtend.getStr("areacode");
                }
            }
            BJBMMC.setText(banjieOuName);
            BJBMZZJDDM.setText(banjieOrgCode);
            XZQHDM.setText(banjieAreacode);

            //审批人姓名
            Element SPRXM = SPBANJIE.addElement("SPRXM");
            SPRXM.setText(auditProject.getBanjieusername());
            //审批人代码
            Element SPRDM = SPBANJIE.addElement("YWSPRDMYSRBM");
            SPRDM.setText(auditProject.getBanjieuserguid());
            //办结结果代码
            Element BJJGDM = SPBANJIE.addElement("BJJGDM");
            String bjjgdm = convertBjjgdmToText(auditProject.getBanjieresult() == null ? 0: auditProject.getBanjieresult());
            BJJGDM.setText(bjjgdm);
            //办结结果描述
            Element BJJGMS = SPBANJIE.addElement("BJJGMS");
            String banjieResultText = iCodeItemsService.getItemTextByCodeName("办结类型",auditProject.getBanjieresult() == null ? "0": auditProject.getBanjieresult() + "");
            BJJGMS.setText(banjieResultText);
            //作废或退回原因
            Element ZFHTHYY = SPBANJIE.addElement("ZFHTHYY");
            ZFHTHYY.setText(banjieResultText);
            //证件/盖章名称
            Element ZJGZMC = SPBANJIE.addElement("ZJGZMC");
            ZJGZMC.setText("无");
            //证件编号
            Element ZJBH = SPBANJIE.addElement("ZJBH");
            ZJBH.setText("无");
            //证件有效期限
            Element ZJYXQX = SPBANJIE.addElement("ZJYXQX");
            ZJYXQX.setText("无");
            //发证/盖章单位
            Element FZGZDW = SPBANJIE.addElement("FZGZDW");
            FZGZDW.setText("无");
            //收费金额
            Element SFJE = SPBANJIE.addElement("SFJE");
            SFJE.setText("无");
            //金额单位代码
            Element JEDWDM = SPBANJIE.addElement("JEDWDM");
            JEDWDM.setText("无");
            //办结时间)
            Element BJSJ = SPBANJIE.addElement("BJSJ");
            BJSJ.setText(EpointDateUtil.convertDate2String(auditProject.getBanjiedate(),EpointDateUtil.DATE_TIME_FORMAT));

            result = document.asXML();
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("调用三方receiveFinishInfo接口异常，封装入参报错");
        }
        return result;
    }

    private String convertBjjgdmToText(Integer banjieresult) {
        String result = "";
        switch (banjieresult){
            case 40: //准予许可
                result = "6";
                break;
            case 50: //不予许可
                result = "7";
                break;
            case 99: //异常终止
                result = "2";
                break;
            case 98: //撤销申请
                result = "3";
                break;
            default:
                break;
        }
        return result;
    }

}
