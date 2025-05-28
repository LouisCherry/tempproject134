package com.epoint.gassupply.job;

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
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.gassupply.impl.GasSupplyProjectService;
import com.epoint.gassupply.util.V2AuthUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@DisallowConcurrentExecution
public class JnRqProSyncProcessDataJob implements Job {

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("========== 开始执行JnRqProSyncProcessDataJob燃气办件进度同步服务 ==========");
        // 获取燃气事项为办结的办件
        List<AuditProject> auditProjectList = new GasSupplyProjectService().getWaterSupplyAuditProjectList();
//        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IHandleProject handleProjectService = ContainerFactory.getContainInfo().getComponent(IHandleProject.class);
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
        // 获取窗口guid
        String windowguid = configService.getFrameConfigValue("yxdj_windowguid");
        if(EpointCollectionUtils.isEmpty(auditProjectList)) {
            log.info("未查询到燃气事项办件");
            return;
        }

        // accountId和身份证号绑定，如果一个用户有多个办件数据，及一个billNo对应多个办件，所以先根据billNo进行分组
        Map<String, List<AuditProject>> auditProjectMap = auditProjectList.stream().collect(Collectors.groupingBy(item -> item.getStr("billno")));
        auditProjectMap.forEach((key, value) -> {
            // map中的每一个key就是一个billNo
            log.info("遍历分组办件集合，accountId--->" + key);
            // 调用查询报装列表接口
            JSONArray data = cxbzlb(key);
            if(EpointCollectionUtils.isNotEmpty(data)) {
                log.info("查询到accountId为" + key + "的报装数据");
                // value就是每一个billNo对应的办件list
                value.forEach(project -> {
                    for (Object item : data) {
                        // 将办件的projectGuid和接口返回的数组中每一个json对象的serialId进行匹配，相同的就是该办件的审核数据
                        String serialId = ((JSONObject) item).getString("serialId");
                        log.info("遍历的每一条报装数据的serialId--->" + serialId);
                        if(project.getRowguid().equals(serialId)) {
                            log.info("办件审核进度匹配成功，serialId--->" + serialId);
                            // 获取接口返回的审核状态，1-待审核、2-审核通过、3-驳回
                            String status = ((JSONObject) item).getString("status");
                            log.info("status状态--->" + status + "[" + serialId + "]");
                            if("1".equals(status)) {
                                log.info("待审核[" + serialId + "]");
////                                generateAuditProjectOperate(project, "0");
//                                // 对于没有审核通过或者驳回的办件，生成办结前的数据
//                                // 本次扫描出来没有办件的，先判断是否已经生成了办结前的数据，如果有则不再生成
//                                Integer count = new GasSupplyProjectService().getBeforeFinishData(project.getFlowsn());
//                                log.info("查询出的该办件办结前步骤数据数量--->" + count);
//                                if(count <= 0) {
//                                    log.info("没有生成过办结前步骤数据，发送mq");
//                                    log.info("处理办件状态");
//                                    handleProjectService.handleAccept(project, "", "华润燃气", userguid, "综合窗口", windowguid);
//                                    log.info("处理完成");
//                                    sendMQ(project, "accept");
//                                }
//                                else {
//                                    log.info("已经生成过办结前数据，不作任何操作");
//                                }
                                log.info("已经生成过受理数据，不做任何操作");
                            }
                            else {
                                log.info("审核通过或驳回[" + serialId + "]");
                                // 生成办件操作数据，办结或者审批不通过
//                                generateAuditProjectOperate(project, status);
                                if("2".equals(status)) {
                                    log.info("status[审核通过]--->" + status);
                                    log.info("处理办件状态");
                                    // 通过
                                    handleProjectService.handleFinish(project, "华润燃气", userguid, "");
                                    log.info("处理完成");
                                }
                                else {
                                    log.info("status[驳回]--->" + status);
                                    log.info("处理办件状态");
                                    // 不通过
                                    handleProjectService.handleProjectNotPass(project, userguid, "");
                                    log.info("处理完成");
                                    // 不通过后将状态置为办结
                                    handleProjectService.handleFinish(project, "华润燃气", userguid, "");
                                }
                                // 审核通过或者驳回，生成办结数据
                                sendMQ(project, "sendresult");
                                // 对于本次已经办结的办件，之后不会再次扫描出来，所以不再判断
                            }
                            // 每当找到匹配的办件，执行完之后跳出循环
                            break;
                        }
                    }
                });
            }
            else {
                log.info("未查询到accountId为" + key + "的报装数据，不做任何操作");
            }
        });
    }


    /**
     * 查询报装列表接口
     */
    public JSONArray cxbzlb(String accountId) {
        try {
            log.info("========== 开始调用网厅查询报装列表接口 ==========");
            IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
//            String rqEnv = configService.getFrameConfigValue("rqEnv");
//            if(StringUtil.isBlank(rqEnv)) {
//                log.info("未获取到燃气接口环境系统参数");
//                return null;
//            }
//            String rqUrlPrefixDev = configService.getFrameConfigValue("rqUrlPrefixDev");
//            String rqUrlPrefixProd = configService.getFrameConfigValue("rqUrlPrefixProd");
//            if(StringUtil.isBlank(rqUrlPrefixDev) || StringUtil.isBlank(rqUrlPrefixProd)) {
//                log.info("未获取到燃气接口地址前缀");
//                return null;
//            }
//            String rqUrlPrefix = "0".equals(rqEnv) ? rqUrlPrefixDev : rqUrlPrefixProd;
//            String apiUrl = rqUrlPrefix + "/com/common/getInstallList?authVersion=v2";
//            log.info("查询报装列表接口地址--->" + apiUrl);
//
//            JSONObject params = new JSONObject();
//            params.put("accountId", accountId);
//            String encryptParams = Base64.getEncoder().encodeToString(params.toJSONString().getBytes(StandardCharsets.UTF_8));
//            JSONObject finalParams = new JSONObject();
//            finalParams.put("request", encryptParams);
//
//            // 由于请求头签名有效期1分钟，所以在调用接口之前设置
//            Map<String, String> headers = new HashMap<>();
//            // 获取请求头签名
//            String sign = generateRqHeader();
//            headers.put("param", sign);
//            log.info("查询报装列表接口地址--->" + apiUrl);
//            log.info("查询报装列表接口请求头--->" + headers);
//            log.info("查询报装列表接口请求参数明文--->" + params);
//            log.info("查询报装列表接口请求参数密文--->" + finalParams);
//            String resultStr = HttpUtil.doPostJson(apiUrl, finalParams.toJSONString(), headers);
//            log.info("接口返回值--->" + resultStr);
//
//            if(StringUtil.isBlank(resultStr)) {
//                log.info("未获取到接口返回值");
//                return null;
//            }
//
//            JSONObject resultJson = JSON.parseObject(resultStr);
//            if("0".equals(resultJson.getString("errcode"))) {
//                // 请求成功
//                JSONArray data = resultJson.getJSONArray("data");
//                log.info("报装数据列表--->" + data);
//                if(EpointCollectionUtils.isNotEmpty(data)) {
//                    return data;
//                }
//                else {
//                    log.info("未获取到报装数据");
//                    return null;
//                }
//            }
//            else {
//                log.info("请求失败，原因：" + resultJson.getString("errmsg"));
//                return null;
//            }
            // 查询报装列表接口
            String apiUrl = configService.getFrameConfigValue("rqzwdtcxUrl");
            if(StringUtil.isBlank(apiUrl)) {
                log.info("未获取到网厅查询报装接口");
                return null;
            }

            JSONObject params = new JSONObject();
            params.put("accountId", accountId);
            String resultStr = HttpUtil.doPostJson(apiUrl, params.toJSONString(), new HashMap<>());
            log.info("接口返回值--->" +resultStr);

            if(StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return null;
            }
            JSONArray data = JSONArray.parseArray(resultStr);
            return data;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用查询报装列表接口异常");
            return null;
        }
    }

    public String generateRqHeader() {
        try {
            log.info("开始生成燃气接口请求头");
            IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            String userName = configService.getFrameConfigValue("rqUserName");
            String password = configService.getFrameConfigValue("rqPassword");
            String publicKey = configService.getFrameConfigValue("rqPublicKey");
            String param = V2AuthUtil.getParam(userName, password, publicKey);
            log.info("生成的签名--->" + param);
            return param;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("生成燃气接口签名失败");
            return "";
        }
    }


    /**
     * 生成办件操作表数据
     * 在发送mq消息之前执行
     * @param auditProject 办件信息
     * @param status 三方接口返回的审核状态
     */
    public String generateAuditProjectOperate(AuditProject auditProject, String status) {
        IAuditProjectOperation auditProjectOperationService = ContainerFactory.getContainInfo().getComponent(IAuditProjectOperation.class);
        AuditProjectOperation operation = new AuditProjectOperation();
        String operationType;
        if("0".equals(status)) {
            operationType = ZwfwConstant.OPERATE_SL;
        }
        else if("2".equals(status)) {
            operationType = ZwfwConstant.OPERATE_BJ;
        }
        else {
            operationType = ZwfwConstant.OPERATE_SPBTG;
        }
        // 生成数据前判断一下该办件的数据是否存在，防止遍历的时候查出重复办件重复生成数据
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

    /**
     * 发送mq消息
     * @param auditProject 办件信息
     * @param operate 对应的mq操作
     */
    public void sendMQ(AuditProject auditProject, String operate) {
        try {
            log.info("========== 开始调用供水事项发送mq消息方法 ==========");
            log.info("projectGuid--->" + auditProject.getRowguid());
            log.info("对应操作--->" + operate);
            ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                log.info("非测试件");
                log.info("办件信息--->" + auditProject);
                // 接办分离 受理
                String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project." + "370800" + "." + operate + "." + auditProject.getTask_id());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("发送mq方法出现异常");
        }
    }
}
