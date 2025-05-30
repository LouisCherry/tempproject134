package com.epoint.listener;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.listener.util.JnRestUtil;
import com.epoint.listener.util.SM2;
import com.rabbitmq.client.Channel;

import cn.hutool.core.lang.UUID;

/**
 * 
 * 办件状态变更消息队列监听器
 * 
 * @author ZhangWenJie
 * @version 2024年8月19日
 */
public class OfficeStatusChangeListener implements ChannelAwareMessageListener
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void onMessage(Message messageByte, Channel channel) throws IOException, InterruptedException {

        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

        IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);

        IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineRegister.class);

        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IAuditOnlineProject iAuditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineProject.class);
        Thread.sleep(10000);
        long currentTime = System.currentTimeMillis();
        log.info("---------从" + messageByte.getMessageProperties().getConsumerQueue() + "开始消费消息！----------");
        String message = new String(messageByte.getBody());
        log.info("---------消息体内容如下：" + message + "！----------");
        try {
            // 解析mq消息内容
            String[] messageContent = message.split("@");

            String areacode = "";
            // 查询办件信息
            String guid = messageContent[1];
            if (messageContent.length > 2) {
                areacode = messageContent[2];
            }

            System.out.println(guid);
            System.out.println(areacode);
            AuditProject project = null;
            if (StringUtil.isBlank(areacode)) {
                project = auditProjectService.getAuditProjectByRowGuid(guid, null).getResult();
            }
            else {
                AuditCommonResult<AuditProject> result = auditProjectService.getAuditProjectByRowGuid("*", guid,
                        areacode);

                if (result != null) {
                    project = auditProjectService.getAuditProjectByRowGuid("*", guid, areacode).getResult();
                }
                else {
                    log.info("---------rowguid：" + guid + " 没有查到数据！----------");
                }
            }

            if (project != null) {
                AuditTask auditTask = auditTaskService.getAuditTaskByGuid(project.getTaskguid(), true).getResult();

                AuditOnlineRegister onlineRegister = iAuditOnlineRegister
                        .getRegisterByIdNumber(project.getContactcertnum()).getResult();

                String areasign = ConfigUtil.getConfigValue("areasign");
                if (StringUtil.isBlank(areasign)) {
                    areasign = "jizczwfw";
                }

                String areaurl = ConfigUtil.getConfigValue("jnlogin", areasign);
                areasign = StringUtil.isNotBlank(areaurl) ? areasign + "_" : "wt";
                String appmark = ConfigUtil.getConfigValue("jnlogin1", areasign + "appmark");

                String baseInfoSubmitUrl = ConfigUtil.getConfigValue("baseInfoSubmitUrl");
                if (StringUtil.isBlank(baseInfoSubmitUrl)) {
                    baseInfoSubmitUrl = "http://www.shandong.gov.cn/icity/newApi/api.UniteCenter/baseInfoSubmit/encrypt";
                }
                Map<String, String> headerMap = new HashMap<String, String>();
                String token = ConfigUtil.getConfigValue("skj_token");
                if (StringUtil.isBlank(token)) {
                    token = "03C2C36FE22549D5B8809D1706081460";
                }
                headerMap.put("token", token);
                JSONObject params = new JSONObject();
                JSONObject requestParameter = new JSONObject();
                String action = "";
                if (project.getStatus() != null) {
                    if (project.getApplyway() != 10 && project.getApplyway() != 40) {
                        log.info("不属于省空间对接办件;办件编号为：" + messageContent[1]);
                        return;

                    }
                    switch (project.getStatus()) {
                        case 14:
                            action = "04";
                            break;
                        case 12:
                        case 26:
                            action = "02";
                            break;
                        case 30:
                            action = "05";
                            break;
                        case 28:
                        case 37:
                            action = "06";
                            break;
                        case 97:
                            action = "07";
                            break;
                        case 90:
                            action = "10";
                            break;
                        case 99:
                            action = "12";
                            break;
                        case 98:
                        case 200:
                            action = "14";
                            break;
                        case 40:
                            action = "15";
                            break;
                        case 999:
                            action = "09";
                            break;
                        default:
                            break;
                    }
                }
                else {
                    log.info("省空间对接办件信息状态异常;办件编号为：" + messageContent[1]);
                }
                if (StringUtil.isBlank(action)) {
                    log.info("省空间对接办件信息状态无需推送;办件编号为：" + messageContent[1]);
                    return;
                }
                requestParameter.put("isChain", "0");
                requestParameter.put("action", action);

                JSONObject data = new JSONObject();
                if (StringUtil.isNotBlank(project.getFlowsn())) {
                    data.put("receiveNum", project.getFlowsn());
                }
                else {
                    data.put("receiveNum", "1111111");
                }

                data.put("receivePwd", UUID.randomUUID().toString());
                data.put("projectname", project.getProjectname());
                data.put("sqrxm", project.getApplyername());
                data.put("lxrxm", project.getContactperson());
                data.put("email", project.getContactemail());
                String userType = "";
                if (project.getApplyertype() != null) {

                    if (project.getApplyertype() == 20) {
                        userType = "1";
                    }
                    if (project.getApplyertype() == 10) {
                        userType = "2";
                    }
                }
                data.put("userType", userType);
                data.put("identityType",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", project.getCerttype()));
                data.put("idcardNo", project.getContactcertnum());
                data.put("creditCode", project.getCertnum());
                data.put("linkPhone", project.getContactphone());

                if (onlineRegister != null) {
                    data.put("userName", onlineRegister.getLoginid());
                    data.put("uuid", onlineRegister.getStr("dhuuid"));
                }

                data.put("itemId", auditTask.getItem_id());
                data.put("itemCode", auditTask.getTaskcode());
                data.put("itemName", auditTask.getTaskname());

                data.put("orgCode", project.getOuguid());
                data.put("orgName", project.getOuname());

                data.put("regionCode", project.getAreacode() + "000000");
                data.put("regionName", codeItemsService.getItemTextByCodeName("辖区对应关系", project.getAreacode()));

                data.put("receiveTime",
                        EpointDateUtil.convertDate2String(project.getApplydate(), EpointDateUtil.DATE_TIME_FORMAT));
                data.put("appMark", appmark);

                String goUrl = "http://jizwfw.sd.gov.cn/jnzwdt/";
                AuditOnlineProject onlineproject = null;
                AuditCommonResult<AuditOnlineProject> auditCommonResult = iAuditOnlineProject
                        .getOnlineProjectByApplyerGuid(project.getRowguid(), project.getOnlineapplyerguid());
                if (auditCommonResult != null) {
                    onlineproject = auditCommonResult.getResult();
                }

                switch (project.getStatus()) {
                    // 待提交
                    // http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/onlinedeclaration/declarationinfo?projectguid=2a765788-486e-4fa5-9e8b-d4b410ba3364&taskguid=bc548d60-531d-476f-8b4d-342f03a1eb63&centerguid=&taskcaseguid=
                    case 10:
                    case 14:
                    case 200:
                        goUrl += "epointzwmhwz/pages/myspace/submit";
                        // goUrl += "projectguid=" + project.getRowguid() + "&"
                        // + "taskguid=" + auditTask.getRowguid();
                        break;

                    // 待补正
                    // http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/myspace/detail?projectguid=4b6ab931-ca37-4a9a-9aac-cfdba013a524&tabtype=2&taskguid=bc548d60-531d-476f-8b4d-342f03a1eb63&taskcaseguid=&applyertype=20
                    case 28:

                        if (onlineproject != null) {
                            goUrl += "epointzwmhwz/pages/myspace/detail?";
                            goUrl += "projectguid=" + project.getRowguid() + "&" + "taskguid=" + auditTask.getRowguid()
                                    + "&tasktype=2" + "&applyertype=" + project.getApplyertype() + "&taskcaseguid="
                                    + onlineproject.getTaskcaseguid();
                            break;
                        }
                        break;

                    // 审核中
                    // http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/myspace/businessdetail?projectguid=bfa05be6-9c96-4e04-84ea-b0993cbd8918&taskguid=ce26ae5c-c988-4dd8-8450-5805b2515ab1&tabtype=5
                    case 12:
                    case 16:
                    case 24:
                    case 26:
                    case 30:
                    case 40:
                    case 50:
                    case 101:
                        if (onlineproject != null
                                && ZwdtConstant.ONLINEPROJECTTYPE_BUSINESS.equals(onlineproject.getType())) {
                            goUrl += "epointzwmhwz/pages/myspace/businessdetail?";
                            goUrl += "projectguid=" + project.getRowguid() + "&" + "taskguid=" + auditTask.getRowguid()
                                    + "&tabtype=5";
                        }
                        else {
                            goUrl += "epointzwmhwz/pages/myspace/detail?";
                            goUrl += "projectguid=" + project.getRowguid() + "&" + "taskguid=" + auditTask.getRowguid()
                                    + "&tabtype=5";
                            if (onlineproject != null) {
                                goUrl += "&taskcaseguid=" + onlineproject.getTaskcaseguid();
                            }
                        }

                        break;

                    // 已办结
                    // http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/myspace/detail?projectguid=6b2e40fb-df36-487e-9b35-17f8562aa536&tabtype=8&taskguid=34c7965d-e219-4661-8316-ee4e811c662d&taskcaseguid=
                    case 90:
                        goUrl += "epointzwmhwz/pages/myspace/detail?";
                        goUrl += "projectguid=" + project.getRowguid() + "&" + "taskguid=" + auditTask.getRowguid()
                                + "&tabtype=8";
                        break;

                    // 其他
                    // http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/myspace/detail?projectguid=9ee5bb52-25e4-45bd-96c4-6c3484ced3aa&tabtype=9&taskguid=bc548d60-531d-476f-8b4d-342f03a1eb63&taskcaseguid=
                    case 97:
                    case 98:
                    case 99:
                        goUrl += "epointzwmhwz/pages/myspace/detail?";
                        goUrl += "projectguid=" + project.getRowguid() + "&" + "taskguid=" + auditTask.getRowguid()
                                + "&tabtype=9";
                        break;

                    default:
                        break;
                }
                data.put("gotoUrl", goUrl);
                data.put("isMiddlePlatform", "0");

                requestParameter.put("data", data);
                
                log.info(requestParameter.toJSONString());
                String publicKey = "044C52572D44DCB8D6B871C3C78043081F1954EDC3F6E7964B4F8B745F64D893C78633A0336369377BBE67915E1B0982E102B003C8C3DC4A69EF8ADE005ECF0F7C";
                String priKeyHexString = "7E557E1AF3167AF8D74CCDB87A82AEDBA48680BF905E164B57850EFAD6B0F291";
                params.put("requestKey", "JNZWFW1724037176");
                params.put("requestParameter", SM2.encrypt(publicKey, requestParameter.toJSONString()));

                String rtnStr = HttpUtil.doHttp(baseInfoSubmitUrl, headerMap, params.toJSONString(), "post", 2);
                log.info("三方接口receiveSuspendInfo接口返回：" + rtnStr);

                // 处理返回结果
                if (StringUtil.isBlank(rtnStr)) {
                    // 记录调用接口日志
                    JnRestUtil.insertJnDockingLog(baseInfoSubmitUrl, params.toJSONString(), 2,
                            "三方接口receiveSuspendInfo接口调用失败，返回结果为空，请联系管理员检查！", baseInfoSubmitUrl);
                    return;
                }

                // 解密接口返回数据
                JSONObject res = JSONObject.parseObject(rtnStr);
                if (!"200".equals(res.getString("code"))) {
                    // 记录调用接口日志
                    JnRestUtil.insertJnDockingLog(baseInfoSubmitUrl, params.toJSONString(), 2,
                            "三方接口receiveSuspendInfo接口调用失败，code不为200，请联系管理员检查！" + rtnStr, baseInfoSubmitUrl);
                    return;
                }

                // 请求成功
                log.info("调用三方receiveSuspendInfo接口成功！");
            }
            else {
                log.info("省空间对接办件信息未查询到对应办件;办件编号为：" + messageContent[1]);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // 在处理完消息时，返回应答状态。第二个参数值为false代表关闭RabbitMQ的自动应答机制，改为手动应答
            channel.basicAck(messageByte.getMessageProperties().getDeliveryTag(), false);
            EpointFrameDsManager.close();
        }
    }

}
