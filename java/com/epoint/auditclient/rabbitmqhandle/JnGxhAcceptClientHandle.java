package com.epoint.auditclient.rabbitmqhandle;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
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
 * 接办分离受理操作消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class JnGxhAcceptClientHandle extends AuditClientMessageListener {

    private static final Logger logger = Logger.getLogger(JnGxhAcceptClientHandle.class);

    /**
     * 受理操作逻辑
     *
     * @param proMessage 参数
     * @return
     * @exception/throws
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IAuditElectricDataService electricDataService = ContainerFactory.getContainInfo().getComponent(IAuditElectricDataService.class);


        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        //办件主键
        String projectID = messageContent[0];
        //区划代码
        String areaCode = messageContent[1];


        //获取办件信息
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, areaCode).getResult();
        if (auditProject == null) {
            //logger.info("未找到对应的办件");
            return;
        }
        // 推送电力数据
        try {
            int count = electricDataService.countDataByProjectGuid(projectID);
            if (count > 0) {
                logger.info("开始组装受理接口入参：" + count);

                // 推送单位id
                String fromId = "";
                FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(auditProject.getOuguid());
                if (ou != null) {
                    fromId = ou.getStr("orgcode");
                }
                JSONObject data = new JSONObject();
                data.put("BLHJ", "03");
                data.put("BLRMC", auditProject.getAcceptusername());
                data.put("BLRZWDM", "002");
                data.put("BLRZWMC", "科长");
                data.put("BLSJ", EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(), EpointDateUtil.DATE_TIME_FORMAT));
                data.put("BLYJ", "同意");
                data.put("BQSXRQ", "");
                data.put("BYZDA", "");
                data.put("BYZDB", "");
                data.put("BYZDC", "");
                data.put("BYZDD", "");
                data.put("BZ", "");
                data.put("CNBJRQ", auditProject.getPromiseenddate());
                data.put("CNBLSX", auditProject.getPromise_day());
                data.put("DYFORMDATA", "");
                data.put("HZBH", "");
                data.put("RQSL", "0");
                data.put("SJBBH", "");
                data.put("SPDWDM", fromId);
                data.put("SPDWMC", auditProject.getOuname());
                data.put("DJSPDWDM", fromId);
                data.put("DJSPDWMC", fromId);
                String dlFlowSn = electricDataService.getDlFlowSnByProjectGuid(projectID);
                data.put("YWLSH", dlFlowSn);

                AuditElectricData electricData = new AuditElectricData();
                electricData.setRowguid(UUID.randomUUID().toString());
                electricData.setUpdatetime(new Date());
                electricData.setFlowsn(auditProject.getFlowsn());
                electricData.setProjectguid(projectID);
                electricData.setType("受理");
                // 组装请求数据
                JSONObject requestParam = new JSONObject();
                requestParam.put("data", PowerDataUtil.buildXmlStr(fromId, projectID, projectID, data, null, "SPGC_SL"));
                requestParam.put("serviceCode", "20001791");
                requestParam.put("source", "010264");
                requestParam.put("target", "37101");

                electricData.setParams(requestParam.toJSONString());
                logger.info("电力对接受理接口入参：" + requestParam.toJSONString());
                // 调用三方接口
                String result = HttpUtil.doPostJson(ConfigUtil.getFrameConfigValue("dldjUrl"), requestParam.toJSONString());
                logger.info("电力对接受理接口调用结果：" + result);
                electricData.setResult(result);
                electricDataService.insert(electricData);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            AuditElectricData electricData = new AuditElectricData();
            electricData.setRowguid(UUID.randomUUID().toString());
            electricData.setUpdatetime(new Date());
            electricData.setFlowsn(auditProject.getFlowsn());
            electricData.setProjectguid(auditProject.getRowguid());
            electricData.setType("受理");
            electricDataService.insert(electricData);
            electricData.setError(e.getMessage());
            electricDataService.insert(electricData);
        }
    }

}
