package com.epoint.zwdt.zwdtrest.cdzyjs;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

/**
 * 
 *  充电桩报装一件事对接第三方系统
 * @author future
 * @version 2023年3月24日
 */
@RestController
@RequestMapping("/jncdzyjs")
public class JNCdzYjsController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IConfigService iConfigService;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IAuditOnlineProject iauditonlineproject;
    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    /**
     * 
     *  预审通过提交【充电桩一件事申报】给第三方
     *  @param params
     *  @return
     */
    @RequestMapping(value = "/submitCdzyjsApply", method = RequestMethod.POST)
    public String submitCdzyjsApply(@RequestBody String params) {
        try {
            log.info("=======开始调用submitCdzyjsApply接口=======params=" + params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject paramJson = jsonObject.getJSONObject("params");

            String subappGuid = paramJson.getString("subappGuid"); // subappGuid
            String capRunCap = paramJson.getString("sqydrl"); // 申请用电量，新装、增容时必填，申请运行容量
            String sfktfgfs = paramJson.getString("sfktfgfs"); // 是否开通峰谷分时
            String countyCode = paramJson.getString("countyCode"); // 县区编码（如果是济宁市，则根据情形选择传输），详见代码项 370811 任城区370812 兖州区370826 微山县370827 鱼台县370828 金乡县370829 嘉祥县370830 汶上县370831 泗水县370832 梁山县370871 济宁高新技术产业开发区370881 曲阜市370883 邹城市
            String elecAddr = paramJson.getString("elecAddr"); // 详细地址名称,一件事“地址”
            String consName = paramJson.getString("consName"); // 传申请人名称
            String consIDcard = paramJson.getString("consIDcard"); // 传申请人身份证号
            JSONArray custContactLists = paramJson.getJSONArray("custContactLists"); // 客户联系信息结果集列表

            /**
             * 默认值区域开始--------------------------------------
             */
            // 预申请编号渠道提供，可作为回执依据。即 唯一标识 （规则：环境编码3位+日期12位+供电单位6位+业务自增分布式id 2位 ）环境编码：DL37408001 时间格式：yymmddhhmmss 年两位+月两位+日两位+时分秒六位）行政编码：XXXXX(370800)
            String preAppNo = "DL37408001"
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT) + "370800"
                    + (int) (Math.random() * 90 + 10);

            String channelNo = "64"; // 服务渠道，标准代码“服务渠道”，默认64
            String proCode = "370000"; // 省编码，标准代码“省编码”,必填,默认传“370000”
            String cityCode = "370800"; // 地市编码，标准代码“地市编码”,新装必填,默认传“370800”
            String busiTypeCode = "0102"; // 业务类型，标准代码“业务类型”默认0102
            String reduceCapMode = "010202"; // 业务子类 ，标准代码“业务子类”，默认010202
            String custType = "02"; // 客户类型，标准代码“客户类型”：01-高压，02-低压非居民，03-低压居民，默认02
            // 供电单位，根据街道取映射表中供电单位
            String orgNo = "3740820"; // 默认任城区
            if (StringUtil.isNotBlank(countyCode)) {
                orgNo = iCodeItemsService.getItemTextByCodeName("充电桩一件事供电单位", countyCode);
            }

            String userMarketSort = "01"; // 用户性质,01个人、02企业 , 默认“01”
            if (StringUtil.isBlank(sfktfgfs)) {
                sfktfgfs = "否";
            }
            if (StringUtil.isBlank(consName)) {
                consName = "";
            }

            if (StringUtil.isBlank(consIDcard)) {
                consIDcard = "";
            }
            if (custContactLists == null) {
                custContactLists = new JSONArray();
            }
            String remark = consName + consIDcard + "是否执行分时:" + sfktfgfs; // 姓名+身份证号+是否执行峰谷分时 张三370302198608070543+执行分时/不执行分时
            String ischcEqp = "1"; // 是否个人充电桩，标准代码“是否标志”：1是，0否；默认1
            String serviceTime = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT); // 预约服务日期（低压非居民时为必传）默认当天
            // 接口地址
            String cdzyjs_InterfaceUrl = iConfigService.getFrameConfigValue("Cdzyjs_InterfaceUrl"); // http://10.156.160.49:14001/transformation?serCode=getOneCardSLJN
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("preAppNo", preAppNo);
            jsonParam.put("channelNo", channelNo);
            jsonParam.put("proCode", proCode);
            jsonParam.put("cityCode", cityCode);
            jsonParam.put("countyCode", countyCode == null ? "" : countyCode);
            jsonParam.put("elecAddr", elecAddr == null ? "" : elecAddr);
            jsonParam.put("busiTypeCode", busiTypeCode);
            jsonParam.put("reduceCapMode", reduceCapMode);
            jsonParam.put("custType", custType);
            jsonParam.put("consName", consName);
            jsonParam.put("orgNo", orgNo);
            jsonParam.put("userMarketSort", userMarketSort);
            jsonParam.put("remark", remark);
            jsonParam.put("ischcEqp", ischcEqp);
            jsonParam.put("capRunCap", capRunCap == null ? "" : capRunCap);
            jsonParam.put("contractCap", capRunCap == null ? "" : capRunCap); // 申请合同容量，第三方要求传这个字段
            jsonParam.put("serviceTime", serviceTime);
            jsonParam.put("meaningtaskguid", "BZ2"); // 报装意向基本事项标识
            jsonParam.put("custContactLists", custContactLists);

            JSONObject json = new JSONObject();
            json.put("data", jsonParam);
            json.put("serviceCode", "20001780");
            json.put("source", "010307");
            json.put("target", "37101");
            log.info("=======submitCdzyjsApply接口=======json=" + json.toJSONString());
            String result = HttpUtil.doPostJson(cdzyjs_InterfaceUrl, json.toJSONString());
            log.info("=======submitCdzyjsApply接口=======result=" + result);
            if (StringUtil.isNotBlank(result)) {
                JSONObject obj = JSONObject.parseObject(result);
                JSONObject data = obj.getJSONObject("data");
                JSONObject data2 = data.getJSONObject("data");
                String code = data2.getString("code");
                if ("00000".equals(code)) {
                    if (StringUtil.isNotBlank(subappGuid)) {
                        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
                        if (auditSpISubapp != null) {
                            auditSpISubapp.set("preAppNo", preAppNo);
                            iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                        }
                    }
                    log.info("=======结束调用submitCdzyjsApply接口=======");
                    return JsonUtils.zwdtRestReturn("1", "接收办电预申请成功", "");
                }
            }
            return JsonUtils.zwdtRestReturn("0", "接收办电预申请失败", "");
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitCdzyjsApply接口参数：params【" + params + "】=======");
            log.info("=======submitCdzyjsApply异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "接口出现异常", "");
        }
    }

    /**
     * 
     *  获取第三方推回的状态回传:办电预申请审批反馈
     *  @param params
     *  @return
     */
    @RequestMapping(value = "/getCdzyjsApplyStatus", method = RequestMethod.POST)
    public String getCdzyjsApplyStatus(@RequestBody String params) {
        try {
            log.info("=======开始调用getCdzyjsApplyStatus接口=======params=" + params);
            JSONObject paramJson = JSONObject.parseObject(params);
            JSONObject data = paramJson.getJSONObject("data");
            // String serialNo = paramJson.getString("serialNo"); // 请求唯一标识
            String preAppNo = data.getString("preAppNo"); // 渠道的申请编号，渠道为政府服务平台
            String sendElectType = data.getString("sendElectType"); // 1：已接件6：补正（开始）4：不受理3：已受理8：部门开始办理9：特别程序（开始）10：特别程序（结束）11：办结（通过）
            String reasonName = data.getString("reasonName"); // 01-照片不清；02-照片不对；03-业务未上线；04-客户重复申请；05-资料审核不通过
            // String sudXXX = paramJson.getString("sudXXX"); //受理人
            // String appNo = paramJson.getString("appNo"); //正式申请编号,审核类型为：8、9、10、11时为必传
            // String consNo = paramJson.getString("consNo"); // 用户编号
            // String consName = paramJson.getString("consName"); // 用户名称
            // String auditTime = paramJson.getString("auditTime"); // 审核时间YYYY-MM-DD HH:MI:SS
            // String reasonNo = paramJson.getString("reasonNo"); // 不通过原因代码,01-照片不清；02-照片不对；03-业务未上线；04-客户重复申请；05-资料审核不通过
            // String auditContent = paramJson.getString("auditContent"); // 审核意见:营销字段为备注，如果申请编号为空，此字段必填（不通过原因
            // String auditResult = paramJson.getString("auditResult"); // 审核结果,1通过0不通过
            // String orgNo = paramJson.getString("orgNo"); // 供电单位编码
            // String serialNumber = paramJson.getString("serialNumber"); // 办件流水号
            // String itemNo = paramJson.getString("itemNo"); // 事项编号
            // String DWDM = paramJson.getString("DWDM"); // 单位代码
            // String CNSX = paramJson.getString("CNSX"); // 承诺时限
            // String projectNo = paramJson.getString("projectNo"); // 项目代码
            // String orgName = paramJson.getString("orgName"); // 单位名称

            if (StringUtil.isBlank(preAppNo) || StringUtil.isBlank(sendElectType)) {
                JSONObject res = new JSONObject();
                res.put("code", "0009");
                res.put("msg", "preAppNo和sendElectType不能为空");
                res.put("data", new JSONObject());
                return res.toString();
            }
            // 查询一件事 变更状态
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("preAppNo", preAppNo);
            List<AuditSpISubapp> result = iAuditSpISubapp.getSubappListByMap(sqlConditionUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(result)) {
                AuditSpISubapp auditSpISubapp = result.get(0);
                if ("11".equals(sendElectType) || "3".equals(sendElectType)) {
                    auditSpISubapp.setStatus("40"); // 办结
                    iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                    // 更新外网申办的状态
                    Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                    updateFieldMap.put("status=", "90");// 办结
                    SqlConditionUtil conditionsql = new SqlConditionUtil();
                    conditionsql.eq("sourceguid", auditSpISubapp.getBiguid());
                    if (StringUtil.isNotBlank(auditSpISubapp.getBiguid())) {
                        iauditonlineproject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16),
                                conditionsql.getMap());
                    }
                }
                else if ("4".equals(sendElectType)) {
                    auditSpISubapp.setStatus("99"); // 不受理
                    auditSpISubapp.setReason(reasonName == null ? "" : reasonName);
                    iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                    // 更新外网申办的状态
                    Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                    updateFieldMap.put("status=", "97");// 办结
                    SqlConditionUtil conditionsql = new SqlConditionUtil();
                    conditionsql.eq("sourceguid", auditSpISubapp.getBiguid());
                    if (StringUtil.isNotBlank(auditSpISubapp.getBiguid())) {
                        iauditonlineproject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16),
                                conditionsql.getMap());
                    }

                }

                log.info("=======结束调用getCdzyjsApplyStatus接口=======");
                JSONObject res = new JSONObject();
                res.put("code", "0000");
                res.put("msg", "成功");
                res.put("data", new JSONObject());
                return res.toString();
            }
            JSONObject res = new JSONObject();
            res.put("code", "0001");
            res.put("msg", "失败，找不到preAppNo=" + preAppNo + "的申请");
            res.put("data", new JSONObject());
            return res.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCdzyjsApplyStatus接口参数：params【" + params + "】=======");
            log.info("=======getCdzyjsApplyStatus异常信息：" + e.getMessage() + "=======");
            JSONObject res = new JSONObject();
            res.put("code", "0002");
            res.put("msg", "接口异常");
            res.put("data", new JSONObject());
            return res.toString();
        }
    }
}
