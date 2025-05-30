package com.epoint.smsinfo.rest;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.smsinfo.api.ISmsInfoService;
import com.epoint.smsinfo.api.entity.EvaluateProject;
import com.epoint.smsinfo.api.entity.SmsInfo;

/**
 * 
 * 短信状态报告相关接口
 * 
 * @author yrchan
 * @version 2022年4月14日
 */
@RestController
@RequestMapping("/smsApi")
public class SmsApi
{
    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ISmsInfoService iSmsInfoService;

    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 短信状态报告
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/pushStatus", method = RequestMethod.POST)
    public String pushStatus(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用pushStatus接口=======" + new Date());
            JSONObject resultJSON = new JSONObject();
            // 1、入参转化为JSON对象
            JSONObject paramsJson = JSONObject.parseObject(params);
            log.info("=======调用pushStatus接口入参：=======" + paramsJson.toJSONString());

            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "成功", resultJSON.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("=======pushStatus接口参数：params【" + params + "】=======");
            log.error("=======pushStatus异常信息：" + e.getMessage() + "=======", e);
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取上行短信接口
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/pushSms", method = RequestMethod.POST)
    public String pushSms(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用pushSms接口=======" + new Date());
            JSONObject resultJSON = new JSONObject();
            // 1、入参转化为JSON对象
            JSONObject paramsJson = JSONObject.parseObject(params);
            log.info("=======调用pushSms接口入参：=======" + paramsJson.toJSONString());

            String mobile = paramsJson.getString("mobile");
            String smsContent = paramsJson.getString("smsContent");
            String sendTime = paramsJson.getString("sendTime");
            String addSerial = paramsJson.getString("addSerial");

            Date sendDate = EpointDateUtil.convertString2Date(sendTime, EpointDateUtil.DATE_TIME_FORMAT);

            // 2、将返回短信内容入库在上行短信内容（sms_info）表中
            // 把上行短信内容保存下来
            SmsInfo smsInfo = new SmsInfo();
            smsInfo.setRowguid(UUID.randomUUID().toString());
            smsInfo.setOperatedate(new Date());
            smsInfo.setReq_date(new Date());
            smsInfo.setMobile(mobile);
            smsInfo.setSmscontent(smsContent);
            smsInfo.setSendtime(sendDate);
            smsInfo.setAddSerial(addSerial);
            iSmsInfoService.insert(smsInfo);

            // 上行短信内--对应--评价结果，若传过来的是代码项文本，则保存为代码项值。
            List<CodeItems> codeList = iCodeItemsService.listCodeItemsByCodeName("评价结果");
            if (!codeList.isEmpty()) {
                for (CodeItems codeItems : codeList) {
                    if (smsContent.equals(codeItems.getItemText())) {
                        smsContent = codeItems.getItemValue();
                        break;
                    }
                }
            }

            String handleDate = EpointDateUtil.convertDate2String(sendDate, EpointDateUtil.DATE_TIME_FORMAT);

            // 更新评价信息，获取同一手机号未评价办件时，时间筛选条件,查询是否：存在已评价，需要再加上大于已评价办件短信发送时间
            SqlConditionUtil countSql = new SqlConditionUtil();
            countSql.ge("handle_date", handleDate);
            countSql.eq("link_phone", mobile);
            countSql.eq("is_evaluate", ZwfwConstant.CONSTANT_STR_ONE);
            int count = iSmsInfoService.countByCondition(EvaluateProject.class, countSql.getMap());
            if (count == 0) {
                // 如果不存在已评价，则查询没有评价的办件，最新的一条数据进下更新
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.le("handle_date", handleDate);
                sql.eq("link_phone", mobile);
                sql.isBlankOrValue("is_evaluate", ZwfwConstant.CONSTANT_STR_ZERO);
                sql.setOrderDesc("handle_date");
                sql.setSelectFields("*");
                // 3、根据上行手机号和上行短信发送时间到评价办件信息表（evaluate_project）获取对应办结时间（按日）、手机号的办件信息。判断没有评价的的办件：
                EvaluateProject evaluateProject = iSmsInfoService.findEvaluateProjectByCondition(sql.getMap());
                if (evaluateProject != null) {
                    // 更新保存
                    evaluateProject.setEvaluate_date(new Date());
                    evaluateProject.setEvaluate_result(smsContent);
                    evaluateProject.setIs_evaluate(ZwfwConstant.CONSTANT_INT_ONE);
                    iSmsInfoService.updateEvaluateProject(evaluateProject);
                    EpointFrameDsManager.commit();
                }
            }

            log.info("=======调用pushSms接口入参：=======" + paramsJson.toJSONString());

            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "成功", resultJSON.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("=======pushSms接口参数：params【" + params + "】=======");
            log.error("=======pushSms异常信息：" + e.getMessage() + "=======", e);
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "失败：" + e.getMessage(), "");
        }
    }

}
