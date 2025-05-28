package com.epoint.auditclient.rabbitmqhandle;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.power.util.PowerDataUtil;
import com.epoint.xmz.auditelectricdata.api.IAuditElectricDataService;
import com.epoint.xmz.auditelectricdata.api.entity.AuditElectricData;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.UUID;

/**
 * 接办分离办件结果消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class JnGxhSendResultClientHandle extends AuditClientMessageListener {

    private static final Logger logger = Logger.getLogger(JnGxhAcceptClientHandle.class);

    /**
     * 发送办件结果到自建系统操作逻辑
     *
     * @param proMessage 参数
     * @return
     * @exception/throws
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditTask taskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditElectricDataService electricDataService = ContainerFactory.getContainInfo().getComponent(IAuditElectricDataService.class);
        IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);

        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        //办件主键
        String projectID = messageContent[0];
        //区划代码
        String areaCode = messageContent[1];
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, areaCode).getResult();
        if (auditProject == null) {
            return;
        }
        // 推送电力数据
        try {
            int count = electricDataService.countDataByProjectGuid(projectID);
            if (count > 0) {
                // 推送单位id
                String fromId = "";
                FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(auditProject.getOuguid());
                if (ou != null) {
                    fromId = ou.getStr("orgcode");
                }
                JSONObject data = new JSONObject();
                data.put("BLHJ", "07");
                data.put("BLRMC", auditProject.getAcceptusername());
                data.put("BLRZWDM", "002");
                data.put("BLRZWMC", "科长");
                data.put("BLSJ", EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(), EpointDateUtil.DATE_TIME_FORMAT));
                data.put("BLYJ", "同意");
                data.put("BQCLQD", "");
                data.put("BQSXRQ", "");
                data.put("BYZDA", "");
                data.put("BYZDB", "");
                data.put("BYZDC", "");
                data.put("BYZDD", "");
                data.put("BZ", "");
                data.put("BZDWDM", fromId);
                data.put("BZDWMC", auditProject.getOuname());
                data.put("CWCSDWID", "");
                data.put("CWSJ", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                data.put("DYFORMDATA", "");
                data.put("FILEIDS", "");
                data.put("PWBH", auditProject.getWenhao());
                AuditTask auditTask = taskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                data.put("PWMC", auditTask.getResult_file_name());
                data.put("SFHG", "1");
                data.put("SHBJ", "1");
                data.put("SJBBH", "1");
                data.put("SPDWDM", fromId);
                data.put("SPDWMC", auditProject.getOuname());
                data.put("SPSXBH", auditTask.getItem_id());
                data.put("YQLY", "");
                data.put("YQLY", "");
                String dlFlowSn = electricDataService.getDlFlowSnByProjectGuid(projectID);
                data.put("YWLSH", dlFlowSn);
                data.put("YXXBS", "1");
                // 证照编号
                data.put("ZZBH", "");
                data.put("SFYDSD", "0");
                data.put("ZZDM", "");
                data.put("ZZKZXX", "");
                data.put("ZZYXJZSJ", "");
                data.put("ZZYXKSSJ", "");

                //JSONArray files = new JSONArray();
                //
                //CertInfo certInfo = certInfoService.getCertInfoByRowguid(auditProject.getCertrowguid());
                //if (certInfo != null) {
                //    data.put("ZZBH", certInfo.getCertno());
                //    data.put("ZZDM", certInfo.getCertno());
                //    List<FrameAttachInfo> attachList = attachService.getAttachInfoListByGuid(certInfo.getCertcliengguid());
                //    if (CollectionUtils.isNotEmpty(attachList)) {
                //        attachList.forEach(attach -> {
                //            FrameAttachStorage attachStorage = attachService.getAttach(attach.getAttachGuid());
                //            JSONObject file = new JSONObject();
                //            file.put("FILENAME", attach.getAttachFileName());
                //            file.put("DATAHANDLER", PowerDataUtil.inputStream2Base64(attachStorage.getContent()));
                //            files.add(file);
                //        });
                //
                //    }
                //}

                AuditElectricData electricData = new AuditElectricData();
                electricData.setRowguid(UUID.randomUUID().toString());
                electricData.setUpdatetime(new Date());
                electricData.setFlowsn(auditProject.getFlowsn());
                electricData.setProjectguid(projectID);
                electricData.setType("办结");
                // 组装请求数据
                JSONObject requestParam = new JSONObject();
                requestParam.put("data", PowerDataUtil.buildXmlStr(fromId, projectID, projectID, data, null, "SPGC_NCW"));
                requestParam.put("serviceCode", "20001792");
                requestParam.put("source", "010264");
                requestParam.put("target", "37101");
                electricData.setParams(requestParam.toJSONString());
                // 调用三方接口
                String result = HttpUtil.doPostJson(ConfigUtil.getFrameConfigValue("dldjUrl"), requestParam.toJSONString());
                electricData.setResult(result);
                electricDataService.insert(electricData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AuditElectricData electricData = new AuditElectricData();
            electricData.setRowguid(UUID.randomUUID().toString());
            electricData.setUpdatetime(new Date());
            electricData.setFlowsn(auditProject.getFlowsn());
            electricData.setProjectguid(auditProject.getRowguid());
            electricData.setType("办结");
            electricDataService.insert(electricData);
            electricData.setError(e.getMessage());
            electricDataService.insert(electricData);
        }
    }

}
