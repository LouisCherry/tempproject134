package com.epoint.watersupply.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.authentication.UserSession;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.watersupply.impl.WaterSupplyProjectService;
import org.jsoup.helper.StringUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 供水办件进度同步服务
 */
@DisallowConcurrentExecution
public class JnSwProSyncProcessDataJob implements Job {

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("========== 开始执行JnSwProSyncProcessDataJob供水办件进度同步服务 ==========");
        // 获取供水事项未办结的办件
        List<AuditProject> auditProjectList = new WaterSupplyProjectService().getWaterSupplyAuditProjectList();
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        if(EpointCollectionUtils.isEmpty(auditProjectList)) {
            log.info("未查询到供水事项办件");
            return;
        }
        /*
            查询到办件，遍历办件调用获取单据办理进度接口
            根据返回的进度，新增spgl_xmspsxblxxxxb表的数据
            其中，接件步骤对应的blzt的值为1，这个由系统自动生成
            如果接口返回审核通过，新增blzt为3（已受理）和8（部门开始办理）的数据；如果审核不通过，新增blzt为5（不予受理）的数据
         */
        auditProjectList.forEach(auditProject -> {
            JSONArray data = getDealProcessInfo(auditProject, auditProject.getStr("billNo"));
            if(EpointCollectionUtils.isNotEmpty(data)) {
                EpointFrameDsManager.begin(null);
                data.forEach(stepItem -> {
                    JSONObject stepJson = (JSONObject) stepItem;
                    if ("申请".equals(stepJson.getString("stepname"))) {
                        if ("1".equals(stepJson.getString("dealresult"))) {
                            // 发送之前先生成audit_project_operate表的数据
                            log.info("发送mq之前生成audit_project_operate表数据");
                            String rowGuid = generateAuditProjectOperate(auditProject, "accept", "1");
                            log.info("生成operate数据的主键--->" + rowGuid);
                            // 调用mq发送消息，添加blzt为3和8的数据
                            log.info("申请步骤，发送mq消息，dealresult=1");
                            Integer count = new WaterSupplyProjectService().getBeforeFinishData(auditProject.getFlowsn());
                            log.info("查询出的该办件办结前步骤数据数量--->" + count);
                            if(count <= 0) {
                                log.info("没有生成过办结前步骤数据，发送mq");
                                sendMQ(auditProject, "accept");
                            }
                            else {
                                log.info("已经生成过办结前数据，不作任何操作");
                            }
                        }
                        else {
                            log.info("发送mq之前生成audit_project_operate表数据");
                            String rowGuid = generateAuditProjectOperate(auditProject, "notaccept", "0");
                            log.info("生成operate数据的主键--->" + rowGuid);
                            // 调用mq发送消息，添加blzt为5的数据
                            log.info("申请步骤，发送mq消息，dealresult=0");
                            sendMQ(auditProject, "notaccept");
                            // 将办件状态设置为不予受理
                            auditProject.setStatus(97);
                        }
                    }
                    if ("审核".equals(stepJson.getString("stepname"))) {
                        if ("1".equals(stepJson.getString("dealresult"))) {
                            log.info("发送mq之前生成audit_project_operate表数据");
                            String rowGuid = generateAuditProjectOperate(auditProject, "sendresult", "1");
                            log.info("生成operate数据的主键--->" + rowGuid);
                            // 调用mq发送消息，添加blzt为11的数据
                            log.info("审核步骤，发送mq消息，dealresult=1");
                            sendMQ(auditProject, "sendresult");
                            // 将办件状态设置为办件
                            auditProject.setStatus(90);
                        }
                        else {
                            log.info("发送mq之前生成audit_project_operate表数据");
                            String rowGuid = generateAuditProjectOperate(auditProject, "sendresult", "0");
                            log.info("生成operate数据的主键--->" + rowGuid);
                            // 调用mq发送消息，添加blzt为13的数据
                            log.info("审核步骤，发送mq消息，dealresult=0");
                            sendMQ(auditProject, "sendresult");
                            // 将办件状态设置为审批不通过
                            auditProject.setStatus(97);
                        }
                    }
                });
                // 更新办件状态
                auditProjectService.updateProject(auditProject);
                EpointFrameDsManager.commit();
            }
            else {
                log.info("办件" + auditProject.getRowguid() + "（单据编号：" + auditProject.getStr("billNo") + "）未获取到步骤数据");
            }
        });
        EpointFrameDsManager.close();
    }

    /**
     * 获取单据办理进度接口
     * @param auditProject 办件信息
     * @param billNo 单据编号
     */
    public JSONArray getDealProcessInfo(AuditProject auditProject, String billNo) {
        try {
            log.info("========== 开始调用获取单据办理进度接口 ==========");
            IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            String apiUrl = configService.getFrameConfigValue("dealProcessUrl");
            if(StringUtil.isBlank(apiUrl)) {
                log.info("未获取到单据办理进度接口地址");
                return null;
            }

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/x-www-form-urlencoded");
            Map<String, Object> param = new HashMap<>();
            param.put("billNo", billNo);

            log.info("获取单据办理进度接口地址--->" + apiUrl);
            log.info("请求头--->" + header);
            log.info("请求参数--->" + param);
            String resultStr = HttpUtil.doPost(apiUrl, param, header);
            log.info("接口返回值--->" + resultStr);
            if(StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return null;
            }

            JSONObject resultJson = JSON.parseObject(resultStr);
            if("200".equals(resultJson.getString("code"))) {
                log.info("请求成功");
                return resultJson.getJSONArray("data");
            }
            else {
                log.info("请求失败，" + resultJson.getString("msg"));
                return null;
            }
        }
        catch (Exception e) {
            log.info("调用获取单据办理进度接口出现异常");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送mq消息
     * @param auditProject 办件信息
     * @param operate 对应的mq操作
     */
    public void sendMQ(AuditProject auditProject, String operate) {
        try {
            log.info("========== 开始调用供水事项发送mq消息方法 ==========");
            ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                log.info("非测试件");
                // 接办分离 受理
                String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + ZwfwUserSession.getInstance().getAreaCode() + "." + operate + "." + auditProject.getTask_id());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("发送mq方法出现异常");
        }
    }

    /**
     * 生成办件操作表数据
     * 在发送mq消息之前执行
     * @param auditProject 办件信息
     * @param operate 操作
     * @param dealResult 处理结果
     */
    public String generateAuditProjectOperate(AuditProject auditProject, String operate, String dealResult) {
        IAuditProjectOperation auditProjectOperationService = ContainerFactory.getContainInfo().getComponent(IAuditProjectOperation.class);
        AuditProjectOperation operation = new AuditProjectOperation();
        String operationType;
        if("accept".equals(operate)) {
            // 受理
            operationType = ZwfwConstant.OPERATE_SL;
        }
        else if("notaccept".equals(operate)) {
            // 不予受理
            operationType = ZwfwConstant.OPERATE_BYSL;
        }
        else {
            // 办结或审批不通过
            if("1".equals(dealResult)) {
                operationType = ZwfwConstant.OPERATE_BJ;
            }
            else {
                operationType = ZwfwConstant.OPERATE_SPBTG;
            }
        }
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("projectguid", auditProject.getRowguid());
        sqlConditionUtil.eq("operatetype", operationType);
        AuditProjectOperation result = auditProjectOperationService.getAuditOperationByCondition(sqlConditionUtil.getMap()).getResult();
        if(EpointCollectionUtils.isEmptyMap(result)) {
            operation.setRowguid(UUID.randomUUID().toString());
            operation.setProjectGuid(auditProject.getRowguid());
            operation.setOperateType(operationType);
            operation.setOperateUserGuid(UserSession.getInstance().getUserGuid());
            operation.setOperateusername(UserSession.getInstance().getDisplayName());
            operation.setApplyerGuid(auditProject.getApplyeruserguid());
            operation.setApplyerName(auditProject.getApplyername());
            operation.setAreaCode(auditProject.getAreacode());
            operation.setTaskGuid(auditProject.getTaskguid());
            auditProjectOperationService.addProjectOperation(operation);
            return operation.getRowguid();
        }
        else {
            log.info("办件" + auditProject.getRowguid() + "的操作[" + operationType + "]的数据已存在，不再生成");
            return "";
        }
    }
}
