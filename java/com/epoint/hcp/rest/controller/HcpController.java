package com.epoint.hcp.rest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.hcp.HcpUtil;
import com.epoint.hcp.api.IHcpService;
import com.epoint.zwdt.util.TARequestUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.*;

@RestController
@RequestMapping("/hcp")
public class HcpController {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IHcpService service;

    @Autowired
    private IOuService ouService;

    private static String HCPSIGNURL = ConfigUtil.getConfigValue("hcp", "HcpSignUrl");
    private static String HCPCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeUrl");
    private static String HCPARRAYCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeArrayUrl");
    private static String HCPTEMPURL = ConfigUtil.getConfigValue("hcp", "HcpTempUrl");
    private static String HCPOFFLINETEMPURL = ConfigUtil.getConfigValue("hcp", "HcpOfflineTempUrl");
    private static String HCPPROJECTSUBMITURL = ConfigUtil.getConfigValue("hcp", "HcpProjectSubmitUrl");
    private static String HCPNORMURL = ConfigUtil.getConfigValue("hcp", "HcpNormUrl");
    private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "HcpAppMark");
    private static String HCPAPPWORD = ConfigUtil.getConfigValue("hcp", "HcpAppWord");
    private static String HcpServiceNumber = ConfigUtil.getConfigValue("hcp", "HcpServiceNumber");
    private static String HcpDecrypt = ConfigUtil.getConfigValue("hcp", "HcpDecrypt");

    //获取国家指标接口
    @RequestMapping(value = "/hcpsign", method = RequestMethod.POST)
    public String hcpsign(@RequestBody String params) {
        try {

            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }

            Map<String, String> contentOnlineMap = new HashMap<String, String>();
            contentOnlineMap.put("appMark", HCPAPPMARK);
            String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
            contentOnlineMap.put("time", time);
            //即将发送时间、应用标识、应用密钥按顺序连接并进行SM2加密。time+appMark+appWord
            //TODO
            JSONObject submit = new JSONObject();
            Map<String, String> contentsign = new HashMap<String, String>();
            contentsign.put("content", time + HCPAPPMARK + HCPAPPWORD);
            submit.put("params", contentsign);

            String resultsign = TARequestUtil.sendPostInner(HCPCODEURL, submit.toJSONString(), "", "");
            JSONObject json = new JSONObject();
            if (!"修改用户默认地址失败".equals(resultsign)) {
                json = JSON.parseObject(resultsign);
            }

            contentOnlineMap.put("sign", json.getString("signcontent"));
            contentOnlineMap.put("userName", object.getString("userName"));
            contentOnlineMap.put("creditType", object.getString("creditType"));
            contentOnlineMap.put("creditNum", object.getString("creditNum"));
            String contentType = "utf-8";

            String resultOnline = HcpUtil.doPost(HCPSIGNURL, contentOnlineMap, contentType);
            JSONObject result = JSONObject.parseObject(resultOnline);
            return result.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getnorm接口参数：params【" + params + "】=======");
            log.info("=======getnorm异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "修改用户默认地址失败" + e.getMessage(), "");
        }
    }

    //获取验证码
    @RequestMapping(value = "/gethcpcode", method = RequestMethod.POST)
    public String gethcpcode(@RequestBody String params) {
        try {

            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            String projectNo = object.getString("projectNo");
            String serviceNumber = object.getString("serviceNumber");
            FrameAttachInfo attachinfo = new FrameAttachInfo();
            attachinfo.setAttachGuid(UUID.randomUUID().toString());
            attachinfo.setCliengGuid(projectNo);
            attachinfo.setCliengTag("hcpcode");
            attachinfo.setAttachFileName("好差评验证码");
            try {
                IAttachService iAttachService = ContainerFactory.getContainInfo()
                        .getComponent(IAttachService.class);
                Map<String, Object> map = new HashMap<>();
                map = downloadFile(HCPSIGNURL + "?projectNo=" + projectNo + "&serviceNumber=" + serviceNumber);
                if (map != null) {
                    attachinfo.setAttachLength((Long) map.get("length"));
                    iAttachService.addAttach(attachinfo, (InputStream) map.get("stream"));
                }
                return JsonUtils.zwdtRestReturn("0", "获取办件验证码成功", "");
            } catch (Exception e) {
                log.info("【事项同步附件信息异常】" + HCPSIGNURL + "?projectNo=" + projectNo + "&serviceNumber=" + serviceNumber);
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======gethcpcode接口参数：params【" + params + "】=======");
            log.info("=======gethcpcode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件验证码失败" + e.getMessage(), "");
        }
        return params;
    }

    /**
     * 线上评价新增接口
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/submitTemp", method = RequestMethod.POST)
    public String submitTemp(@RequestBody String params) {
        try {
            log.info("=======开始调用submitTemp接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            String projectno = object.getString("projectno");
            String pf = object.getString("pf");
            String satisfaction = object.getString("satisfaction");
            int assessNumber = object.getInteger("assessNumber");
            String evalDetail = object.getString("evalDetail");
            String Areacode = object.getString("Areacode");
            String evaluateLevel = object.getString("evaluateLevel");
            String Taskname = object.getString("Taskname");
            String Taskcode = object.getString("Taskcode");
            String Promisetime = object.getString("Promisetime");
            String Deptcode = object.getString("Deptcode");
            String Prodepart = object.getString("Prodepart");
            String Userprop = object.getString("Userprop");
            String Username = object.getString("Username");
            String Applicant = object.getString("Applicant");
            String Certkey = object.getString("Certkey");
            String Certkeygov = object.getString("Certkeygov");
            String Mobile = object.getString("Mobile");
            String Acceptdate = object.getString("Acceptdate");
            String month = object.getString("month");
            String writingEvaluation = object.getString("writingEvaluation");
            if (StringUtil.isBlank(projectno) || StringUtil.isBlank(pf) || StringUtil.isBlank(satisfaction) || StringUtil.isBlank(object.getString("assessNumber"))) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            int grade = Integer.parseInt(satisfaction);
            if (grade < 3) {
                if (StringUtil.isBlank(evalDetail) && StringUtil.isBlank(writingEvaluation)) {
                    return JsonUtils.zwdtRestReturn("0", "差评必须勾选详情或填写文字评价", "");
                }
            }
            Boolean bool = service.findEva(projectno, assessNumber);
            if (bool) {
                return JsonUtils.zwdtRestReturn("0", "本次服务已存在评价，请勿重复评价！", "");
            }
            Record es = service.getServiceByProjectno(projectno, assessNumber, month);
            //log.info("办件服务信息：" + es);
            if (es == null) {
                return JsonUtils.zwdtRestReturn("0", "没有对应的服务数据", "");
            }
            //按照省里标准今天添加
            projectno = "37" + projectno;

            Record r = new Record();
            r.setSql_TableName("evainstance");
            String[] primarykeys = {"projectno", "assessNumber"};
            r.setPrimaryKeys(primarykeys);
            r.set("Flag", "I");
            r.set("Appstatus", 0);
            r.set("projectno", projectno);
            r.set("Datasource", "165");
            r.set("Assessnumber", 1);
            r.set("isdefault", "0");
            r.set("EffectivEvalua", "1");
            r.set("Areacode", Areacode);
            r.set("Prostatus", "3");
            r.set("Evalevel", evaluateLevel);
            r.set("Evacontant", evaluateLevel);
            r.set("evalDetail", evalDetail);
            r.set("writingEvaluation", writingEvaluation);
            r.set("Taskname", Taskname);
            r.set("Taskcode", Taskcode);
            r.set("Taskhandleitem", Taskcode);
            r.set("Promisetime", Promisetime);
            r.set("Deptcode", Deptcode);
            r.set("Prodepart", Prodepart);
            r.set("Userprop", Userprop);
            r.set("Username", Username);
            r.set("Applicant", Applicant);
            r.set("Certkey", Certkey);
            r.set("Certkeygov", Certkeygov);
            r.set("Mobile", Mobile);
            r.set("Acceptdate", Acceptdate);
            r.set("createDate", new Date());
            r.set("sync_sign", "0");
            r.set("answerStatus", "0");
            r.set("pf", pf);
            r.set("satisfaction", satisfaction);
            String assessTime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
            r.set("assessTime", assessTime);
            r.set("assessNumber", assessNumber);
            int i = service.addEvaluate(r);
            if (i > 0) {
                // 将评价信息上报到国家
                JSONObject json = new JSONObject();
                //办件编号
                json.put("projectNo", projectno);
                //评价渠道
                json.put("pf", pf);
                //统一社会信用代码
                json.put("deptCode", es.getStr("orgcode"));
                //事项编码       
                json.put("taskCode", es.getStr("taskCode"));
                if (StringUtil.isNotBlank(es.getStr("taskHandleItem"))) {
                    //业务办理项
                    json.put("taskHandleItem", es.getStr("taskHandleItem"));
                }
                json.put("taskName", es.getStr("taskName"));
                //事项主题
                json.put("subMatter", es.getStr("subMatter"));

                //办件状态
                json.put("proStatus", es.getStr("proStatus"));
                //受理部门
                json.put("proDepart", es.getStr("orgName"));
                String certkey = es.getStr("certKey");
                if (StringUtil.isNotBlank(certkey)) {
                    certkey = es.getStr("certKeyGOV");
                }
                //评价人身份唯一标识
                json.put("certKey", certkey);
                //申请人名称
                json.put("userName", es.getStr("userName"));
                //申请人类型
                json.put("userProp", es.getStr("userProp"));
                //申请人证件类型
                json.put("userPageType", es.getStr("userPageType"));
                //代理人名称
                json.put("handleUserName", es.getStr("handleUserName"));
                //代理人证件类型
                json.put("handleUserPageType", es.getStr("handleUserPageType"));
                //代理人证件号码
                json.put("handleUserPageCode", es.getStr("handleUserPageCode"));

                //整体满意度
                json.put("satisfaction", satisfaction);
                //评价详情
                json.put("evalDetail", evalDetail);
                //验证码
                //json.put("vcode", vcode);
                json.put("serviceNumber", es.getStr("serviceNumber"));
                //文字评价
                json.put("writingEvaluation", writingEvaluation);
                //评价时间
                json.put("assessTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                //评价次数
                json.put("assessNumber", assessNumber);
                //数据来源
                json.put("dataSource", "165");
                //办件类型
                json.put("promisetime", es.getInt("projectType"));
                //收件时间
                json.put("acceptDate", es.getStr("acceptDate"));

                JSONObject jsonObjectOnline = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                List<JSONObject> list = new ArrayList<>();
                List<String> list1 = new ArrayList<>();
                list.add(json);
                //log.info("办件数据加密前入参：" + list.toString());
                JSONObject submit = new JSONObject();
                Map<String, String> contentsign = new HashMap<String, String>();
                contentsign.put("evaluate", list.toString());
                jsonObject1.put("content", contentsign);
                submit.put("params", jsonObject1);
                String resultsign = TARequestUtil.sendPostInner(HCPARRAYCODEURL, submit.toJSONString(), "", "");
                JSONObject json1 = new JSONObject();
                if (!"修改用户默认地址失败".equals(resultsign)) {
                    json1 = JSON.parseObject(resultsign);
                }
                list1.add(json1.getString("signcontent"));
                jsonObjectOnline.put("evaluate", list1);

                //log.info("办件数据加密后入参：" + resultsign);
                JSONObject contentOnlineMap = new JSONObject();
                String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
                //log.info("办件数据加密前时间：" + time);
                //即将发送时间、应用标识、应用密钥按顺序连接并进行SM2加密。time+appMark+appWord
                JSONObject submit1 = new JSONObject();
                Map<String, String> contentsign1 = new HashMap<String, String>();
                contentsign1.put("content", time + HCPAPPMARK + HCPAPPWORD);
                submit1.put("params", contentsign1);

                String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
                JSONObject json2 = new JSONObject();
                if (!"修改用户默认地址失败".equals(resultsign1)) {
                    json2 = JSON.parseObject(resultsign1);
                }

                contentOnlineMap.put("sign", json2.getString("signcontent"));
                contentOnlineMap.put("params", jsonObjectOnline);
                contentOnlineMap.put("time", time);
                contentOnlineMap.put("appMark", HCPAPPMARK);
                JSONObject submitString = new JSONObject();
                submitString.put("txnBodyCom", contentOnlineMap);
                submitString.put("txnCommCom", new JSONObject());
                //log.info("办件数据所有入参：" + contentOnlineMap.toString());
                //log.info("办件数据url：" + HCPPROJECTSUBMITURL);

                String resultOnline = "";
                //线上超期提交接口
                try {
                    resultOnline = com.epoint.core.utils.httpclient.HttpUtil.doPostJson(HCPPROJECTSUBMITURL, submitString.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject result = new JSONObject();
                if (StringUtil.isNotBlank(resultOnline)) {
                    result = JSONObject.parseObject(resultOnline);
                    String code = result.getString("code");
                    String success = result.getString("success");
                    if ("0".equals(code) && "true".equals(success)) {
                        return JsonUtils.zwdtRestReturn("1", "新增评价成功", "");
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "新增评价失败", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "新增评价失败", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "新增评价失败", "");
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitTemp接口参数：params【" + params + "】=======");
            log.info("=======submitTemp异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "修改用户默认地址失败" + e.getMessage(), "");
        }
    }

    //获取国家指标接口
    @RequestMapping(value = "/getnorm", method = RequestMethod.POST)
    public String getnorm(@RequestBody String params) {
        try {

            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }

            Map<String, String> contentOnlineMap = new HashMap<String, String>();
            contentOnlineMap.put("appMark", "XINJIANG");
            String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
            contentOnlineMap.put("time", time);
            //即将发送时间、应用标识、应用密钥按顺序连接并进行SM2加密。time+appMark+appWord
            JSONObject submit1 = new JSONObject();
            submit1.put("params", time + HCPAPPMARK + HCPAPPWORD);
            String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
            contentOnlineMap.put("sign", resultsign1);
            String contentType = "utf-8";
            //线上超期提交接口
            String resultOnline = HcpUtil.doPost(HCPNORMURL, contentOnlineMap, contentType);
            JSONObject result = JSONObject.parseObject(resultOnline);
            return result.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getnorm接口参数：params【" + params + "】=======");
            log.info("=======getnorm异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "修改用户默认地址失败" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getServiceNumber", method = RequestMethod.POST)
    public String getServiceNumber(@RequestBody String params) {
        try {
            JSONObject object = JSONObject.parseObject(params);

            String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
            //即将发送时间、应用标识、应用密钥按顺序连接并进行SM2加密。time+appMark+appWord
            JSONObject submit1 = new JSONObject();
            Map<String, String> contentsign1 = new HashMap<String, String>();
            contentsign1.put("content", time + HCPAPPMARK + HCPAPPWORD);
            submit1.put("params", contentsign1);
            String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
            JSONObject json2 = new JSONObject();
            if (!"修改用户默认地址失败".equals(resultsign1)) {
                json2 = JSON.parseObject(resultsign1);
            }
            JSONObject json = new JSONObject();
            json.put("mark", "0");
            json.put("userName", object.getString("userName"));
            json.put("creditType", object.getString("creditType"));
            json.put("creditNum", object.getString("creditNum"));
            // json.put("commented", "0" );
            JSONObject jsonObjectOnline = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            List<JSONObject> list = new ArrayList<>();
            List<String> list1 = new ArrayList<>();
            list.add(json);
            //log.info("getServiceNumber数据加密前入参：" + list.toString());
            JSONObject submit = new JSONObject();
            Map<String, String> contentsign = new HashMap<String, String>();
            contentsign.put("evaluate", list.toString());
            jsonObject1.put("content", contentsign);
            submit.put("params", jsonObject1);
            String resultsign = TARequestUtil.sendPostInner(HCPARRAYCODEURL, submit.toJSONString(), "", "");
            JSONObject json1 = new JSONObject();
            if (!"修改用户默认地址失败".equals(resultsign)) {
                json1 = JSON.parseObject(resultsign);
            }
            list1.add(json1.getString("signcontent"));
            jsonObjectOnline.put("evaluate", list1);

            JSONObject contentOnlineMap = new JSONObject();
            contentOnlineMap.put("sign", json2.getString("signcontent"));
            contentOnlineMap.put("params", jsonObjectOnline);
            contentOnlineMap.put("time", time);
            contentOnlineMap.put("appMark", HCPAPPMARK);
            JSONObject submitString = new JSONObject();
            submitString.put("txnBodyCom", contentOnlineMap);
            submitString.put("txnCommCom", new JSONObject());
            String resultOnline = "";
            try {
                resultOnline = TARequestUtil.sendPostInner(HcpServiceNumber, submitString.toString(), "", "");
                JSONObject rtnjson = JSONObject.parseObject(resultOnline);
                if (rtnjson != null && "success".equals(rtnjson.getString("C-Response-Desc"))) {
                    String cResponseBody = rtnjson.getString("C-Response-Body");
                    JSONObject cResponseBodyJson = JSONObject.parseObject(cResponseBody);
                    if (cResponseBodyJson != null) {
                        JSONArray serviceList = cResponseBodyJson.getJSONArray("serviceList");
                        //即将发送时间、应用标识、应用密钥按顺序连接并进行SM2加密。time+appMark+appWord
                        JSONObject submit2 = new JSONObject();
                        Map<String, String> contentsign2 = new HashMap<String, String>();
                        contentsign2.put("content", serviceList.getString(0));
                        submit2.put("params", contentsign2);
                        return TARequestUtil.sendPostInner(HcpDecrypt, submit2.toJSONString(), "", "");
                    }
                }
                //log.info("getServiceNumber评价服务返回结果如下：" + resultOnline);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject result = JSONObject.parseObject(resultOnline);
            return result.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getServiceNumber接口参数：params【" + params + "】=======");
            log.info("=======getServiceNumber异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "修改用户默认地址失败" + e.getMessage(), "");
        }
    }

    /**
     * 新增办件服务
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/addEvaluateService", method = RequestMethod.POST)
    public String addEvaluateService(@RequestBody String params) {
        try {
            log.info("=======开始调用addEvaluateService接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            // 事项code
            String taskCode = object.getString("taskCode");
            JSONObject result = new JSONObject();
            // 实施编码
            String taskHandleItem = object.getString("taskHandleItem");
            String taskName = object.getString("taskName");
            String subMatter = object.getString("subMatter");
            String projectNo = "37" + object.getString("projectNo");
            String proStatus = object.getString("proStatus");
            String orgcode = object.getString("orgcode");
            String orgName = object.getString("orgName");
            String acceptDate = object.getString("acceptDate");
            String userProp = object.getString("userProp");
            String userName = object.getString("userName");
            String userPageType = object.getString("userPageType");
            String certKey = object.getString("certKey");
            String certKeyGOV = object.getString("certKeyGOV");
            String handleUserName = object.getString("handleUserName");
            String handleUserPageType = object.getString("handleUserPageType");
            String handleUserPageCode = object.getString("handleUserPageCode");
            String serviceTime = object.getString("serviceTime");
            String serviceName = object.getString("serviceName");
            int serviceNumber = object.getInteger("serviceNumber");
            String projectType = object.getString("projectType");
            String resultDate = object.getString("resultDate");
            String areacode = object.getString("areaCode");
            String mobile = object.getString("mobile");
            String deptCode = object.getString("deptCode");
            String ouguid = object.getString("ouguid");
            String projectName = object.getString("projectName");
            String creditNum = object.getString("creditNum");
            String creditType = object.getString("creditType");
            String promiseDay = object.getString("promiseDay");
            String anticipateDay = object.getString("anticipateDay");
            String proChannel = object.getString("proChannel");
            String promiseTime = object.getString("promiseTime");
            String proManager = object.getString("proManager");
            String month = object.getString("month");
            String taskType = object.getString("taskType");
            if (StringUtil.isBlank(mobile) || StringUtil.isBlank(serviceTime) || StringUtil.isBlank(areacode) || StringUtil.isBlank(taskCode) || StringUtil.isBlank(taskName) || StringUtil.isBlank(projectNo) || StringUtil.isBlank(proStatus) || StringUtil.isBlank(orgcode) || StringUtil.isBlank(orgName) || StringUtil.isBlank(userProp) || StringUtil.isBlank(userName) || StringUtil.isBlank(userPageType) || (StringUtil.isBlank(certKey) && StringUtil.isBlank(certKeyGOV)) || StringUtil.isBlank(serviceName) || StringUtil.isBlank(object.getString("serviceNumber")) || StringUtil.isBlank(projectType)) {
                log.info("参数结构不正确，请检查参数结构" + projectNo);
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }

            //好差评推送的自建事项进行限制推送（8个0以上的为自建事项）00000000
            String sjyxbs = "1";
            if (StringUtil.isNotBlank(taskCode) && taskCode.contains("00000000")) {
                sjyxbs = "0";
            }

            //针对taskCode、taskHandleItem赋值
            //①如果是主项，taskcode不变，taskHandleItem为null；②不是主项，taskcode取值前31位，taskHandleItem为33位
            if (taskCode.length() == 33) {
                taskHandleItem = taskCode;
                taskCode = taskCode.substring(0, 31);
            }

            Record es = null;
            Date date = EpointDateUtil.convertString2Date(serviceTime);

            Calendar c = new GregorianCalendar();
            c.setTime(date);//设置参数时间
            c.add(Calendar.MINUTE, -5);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            Date newTime = c.getTime(); //这个时间就是日期往后推一天的结果
            String newserviceTime = EpointDateUtil.convertDate2String(newTime, "yyyy-MM-dd HH:mm:ss");

            es = service.getServiceByProjectno(projectNo, serviceNumber, month);

            if (es != null) {
                log.info("已存在该条服务信息，请勿重复提交" + projectNo);
                return JsonUtils.zwdtRestReturn("0", "已存在该条服务信息，请勿重复提交！", "");
            }

            areacode = areacode.replace("370882", "370812");
            //补足12位
            if (areacode.length() == 6) {
                areacode = areacode + "000000";
            }

            // 公共服务类型不一致，优先转换
            if ("11".equals(taskType)) {
                taskType = "20";
            }

            if (StringUtil.isNotBlank(taskType)) {
                switch (taskType) {
                    case "01":
                    case "05":
                    case "07":
                    case "08":
                    case "09":
                    case "10":
                    case "20":
                        break;
                    default:
                        taskType = "99";
                        break;
                }
            }


            Record r = new Record();

            if ("1".equals(month)) {
                r.setSql_TableName("evaluateservice_1");
            } else if ("2".equals(month)) {
                r.setSql_TableName("evaluateservice_2");
            } else if ("3".equals(month)) {
                r.setSql_TableName("evaluateservice_3");
            } else if ("4".equals(month)) {
                r.setSql_TableName("evaluateservice_4");
            } else if ("5".equals(month)) {
                r.setSql_TableName("evaluateservice_5");
            } else if ("6".equals(month)) {
                r.setSql_TableName("evaluateservice_6");
            } else if ("7".equals(month)) {
                r.setSql_TableName("evaluateservice_7");
            } else if ("8".equals(month)) {
                r.setSql_TableName("evaluateservice_8");
            } else if ("9".equals(month)) {
                r.setSql_TableName("evaluateservice_9");
            } else if ("10".equals(month)) {
                r.setSql_TableName("evaluateservice_10");
            } else if ("11".equals(month)) {
                r.setSql_TableName("evaluateservice_11");
            } else if ("12".equals(month)) {
                r.setSql_TableName("evaluateservice_12");
            } else if ("ck".equals(month)) {
                r.setSql_TableName("evaluateservice_ck");
            } else {
                r.setSql_TableName("evaluateservice");
            }

            String[] primarykeys = {"projectno", "serviceNumber"};
            r.setPrimaryKeys(primarykeys);
            r.set("taskCode", taskCode);
            r.set("taskHandleItem", taskHandleItem);
            r.set("taskName", taskName);
            r.set("subMatter", subMatter);
            r.set("projectNo", projectNo);
            r.set("proStatus", proStatus);
            r.set("orgcode", ouguid);
            r.set("orgName", orgName);
            r.set("acceptDate", acceptDate);
            r.set("userProp", userProp);
            r.set("userName", userName);
            r.set("userPageType", userPageType);
            r.set("certKey", certKey);
            r.set("certKeyGOV", certKeyGOV);
            r.set("handleUserName", handleUserName);
            r.set("handleUserPageType", handleUserPageType);
            r.set("handleUserPageCode", handleUserPageCode);
            r.set("serviceName", serviceName);
            r.set("serviceNumber", serviceNumber);
            r.set("taskType", taskType);

            // 服务时间
            r.set("serviceTime", newserviceTime);

            r.set("dataSource", "165");
            r.set("projectType", projectType);
            r.set("resultDate", resultDate);
            r.set("createDate", new Date());
            r.set("areacode", areacode.substring(0, 6));
            r.set("mobile", mobile);
            int i = service.addEvaluateService(r);
            if ("1".equals(sjyxbs)) {
                if (i > 0) {
                    // 将评价信息上报到国家
                    JSONObject json = new JSONObject();
                    json.put("areaCode", areacode);
                    json.put("deptCode", deptCode);
                    json.put("taskType", taskType);
                    //事项编码
                    json.put("taskCode", taskCode);
                    json.put("proTaskCode", taskCode);
                    json.put("taskHandleItem", taskHandleItem);
                    //事项主题
                    json.put("subMatter", subMatter);
                    json.put("taskName", taskName);
                    json.put("proTaskName", taskName);
                    //办件编号
                    json.put("projectNo", projectNo);
                    json.put("proProjectNo", projectNo);
                    json.put("projectName", projectName);
                    //办件状态
                    json.put("proStatus", proStatus);
                    //受理部门
                    json.put("proDepart", orgName);
                    //手机号码
                    json.put("mobile", mobile);
                    //申请人名称
                    json.put("userName", userName);
                    //用户身份编号
                    json.put("creditNum", creditNum);
                    //证件类型
                    json.put("creditType", creditType);
                    //申请人类型 自然人=1，企业法人=2，事业法人=3，社会组织法人=4，非法人企业=5，行政机关=6，其他组织=9。
                    json.put("userProp", userProp);
                    //收件时间
                    json.put("acceptDate", acceptDate);
                    //承诺办结时间（默认0）
                    json.put("promiseDay", promiseDay);
                    //承诺办结时间单位（0=天，1=工作日，2=月，3=年）
                    json.put("promiseType", "1");
                    //法定办结时间（默认0）
                    json.put("anticipateDay", anticipateDay);
                    //法定办结时间单位（0=天，1=工作日，2=月，3=年）
                    json.put("anticipateType", "1");
                    //实施单位
                    json.put("deptName", orgName);
                    //办件渠道(1=线上，2=线下)
                    json.put("proChannel", proChannel);
                    //即办件=1，承诺办件=2
                    json.put("promiseTime", promiseTime);
                    //申请人证件号码
                    if ("1".equals(proChannel)) {
                        json.put("certKey", certKey);
                    } else {
                        json.put("certkeyGOV", certKeyGOV);
                    }
                    //受理部门编码
                    json.put("orgcode", orgcode);
                    //受理部门名称
                    json.put("orgName", orgName);
                    //办理人姓名(用户名)
                    json.put("proManager", proManager);
                    //申请人证件类型
                    json.put("userPageType", userPageType);
                    //代理人名称
                    json.put("handleUserName", handleUserName);
                    //代理人证件类型
                    json.put("handleUserPageType", handleUserPageType);
                    //代理人证件号码
                    json.put("handleUserPageCode", handleUserPageCode);
                    //服务名称
                    json.put("serviceName", serviceName);
                    //服务时间
                    // 服务时间
                    json.put("serviceTime", newserviceTime);
                    //数据来源
                    json.put("dataSource", "165");
                    //办件类型
                    json.put("projectType", projectType);
                    //办结时间
                    json.put("resultDate", resultDate);

                    JSONObject jsonObjectOnline = new JSONObject();
                    JSONObject jsonObject1 = new JSONObject();
                    List<JSONObject> list = new ArrayList<>();
                    List<String> list1 = new ArrayList<>();
                    list.add(json);
                    JSONObject submit = new JSONObject();
                    Map<String, String> contentsign = new HashMap<String, String>();
                    contentsign.put("evaluate", list.toString());
                    jsonObject1.put("content", contentsign);
                    submit.put("params", jsonObject1);
                    String resultsign = TARequestUtil.sendPostInner(HCPARRAYCODEURL, submit.toJSONString(), "", "");
                    JSONObject json1 = new JSONObject();
                    if (!"修改用户默认地址失败".equals(resultsign)) {
                        json1 = JSON.parseObject(resultsign);
                    }
                    list1.add(json1.getString("signcontent"));
                    jsonObjectOnline.put("evaluate", list1);

                    JSONObject contentOnlineMap = new JSONObject();
                    String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
                    //即将发送时间、应用标识、应用密钥按顺序连接并进行SM2加密。time+appMark+appWord
                    //TODO
                    JSONObject submit1 = new JSONObject();
                    Map<String, String> contentsign1 = new HashMap<String, String>();
                    contentsign1.put("content", time + HCPAPPMARK + HCPAPPWORD);
                    submit1.put("params", contentsign1);

                    String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
                    JSONObject json2 = new JSONObject();
                    if (!"修改用户默认地址失败".equals(resultsign1)) {
                        json2 = JSON.parseObject(resultsign1);
                    }

                    contentOnlineMap.put("sign", json2.getString("signcontent"));
                    contentOnlineMap.put("params", jsonObjectOnline);
                    contentOnlineMap.put("time", time);
                    contentOnlineMap.put("appMark", HCPAPPMARK);
                    JSONObject submitString = new JSONObject();
                    submitString.put("txnBodyCom", contentOnlineMap);
                    submitString.put("txnCommCom", new JSONObject());

                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("C-Tenancy-Id", "370000000000");
                    //办件提交接口
                    String resultOnline = "";
                    try {
                        resultOnline = com.epoint.cert.commonutils.HttpUtil.doPostJson(HCPPROJECTSUBMITURL, submitString.toString(), headers);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (StringUtil.isNotBlank(resultOnline)) {
                        result = JSONObject.parseObject(resultOnline);
                        String code = result.getString("C-Response-Desc");
                        if ("success".equals(code)) {
                            //log.info("新增办件服务对象数据成功");
                            Record rec = new Record();
                            rec.set("sbsign", "1");
                            rec.set("sberrordesc", "同步成功");
                            rec.set("projectno", projectNo);
                            rec.set("servicenumber", serviceNumber);
                            service.updateProService(rec, month);

                            return JsonUtils.zwdtRestReturn("1", "新增服务对象成功", result.toString());
                        } else {
                            Record rec = new Record();
                            rec.set("sbsign", "0");
                            rec.set("sberrordesc", code);
                            rec.set("projectno", projectNo);
                            rec.set("servicenumber", serviceNumber);
                            service.updateProService(rec, month);
                            log.info("添加评价服务返回结果如下：" + resultOnline);
                            return JsonUtils.zwdtRestReturn("0", "新增服务对象失败", result.toString());
                        }

                    } else {
                        Record rec = new Record();
                        rec.set("sbsign", "99");
                        rec.set("sberrordesc", "接口响应异常");
                        rec.set("projectno", projectNo);
                        rec.set("servicenumber", serviceNumber);
                        service.updateProService(rec, month);
                        log.info("添加评价服务返回结果如下：" + resultOnline);
                        return JsonUtils.zwdtRestReturn("0", "新增服务对象失败", result.toString());

                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "新增服务对象失败", result.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "自建事项不进行推送", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "调用失败" + e.getMessage(), "");
        }
    }

    /**
     * 新增办件服务
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/addEvaluateServiceOld", method = RequestMethod.POST)
    public String addEvaluateServiceOld(@RequestBody String params) {
        try {
//            log.info("=======开始调用addEvaluateService接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            // 事项code
            String taskCode = object.getString("taskCode");
            JSONObject result = new JSONObject();
            // 实施编码
            String taskHandleItem = object.getString("taskHandleItem");
            String taskName = object.getString("taskName");
            String subMatter = object.getString("subMatter");
            String projectNo = "37" + object.getString("projectNo");
            String proStatus = object.getString("proStatus");
            String orgcode = object.getString("orgcode");
            String orgName = object.getString("orgName");
            String acceptDate = object.getString("acceptDate");
            String userProp = object.getString("userProp");
            String userName = object.getString("userName");
            String userPageType = object.getString("userPageType");
            String certKey = object.getString("certKey");
            String certKeyGOV = object.getString("certKeyGOV");
            String handleUserName = object.getString("handleUserName");
            String handleUserPageType = object.getString("handleUserPageType");
            String handleUserPageCode = object.getString("handleUserPageCode");
            String serviceTime = object.getString("serviceTime");
            String serviceName = object.getString("serviceName");
            int serviceNumber = object.getInteger("serviceNumber");
            String projectType = object.getString("projectType");
            String resultDate = object.getString("resultDate");
            String areacode = object.getString("areaCode");
            String tasktype = object.getString("tasktype");
            String mobile = object.getString("mobile");
            String deptCode = object.getString("deptCode");
            String ouguid = object.getString("ouguid");
            String projectName = object.getString("projectName");
            String creditNum = object.getString("creditNum");
            String creditType = object.getString("creditType");
            String promiseDay = object.getString("promiseDay");
            String anticipateDay = object.getString("anticipateDay");
            String proChannel = object.getString("proChannel");
            String promiseTime = object.getString("promiseTime");
            String proManager = object.getString("proManager");
            String month = object.getString("month");
            String taskType = object.getString("taskType");
            if (StringUtil.isBlank(mobile) || StringUtil.isBlank(serviceTime) || StringUtil.isBlank(areacode) || StringUtil.isBlank(tasktype) || StringUtil.isBlank(taskCode) || StringUtil.isBlank(taskName) || StringUtil.isBlank(projectNo) || StringUtil.isBlank(proStatus) || StringUtil.isBlank(orgcode) || StringUtil.isBlank(orgName) || StringUtil.isBlank(userProp) || StringUtil.isBlank(userName) || StringUtil.isBlank(userPageType) || (StringUtil.isBlank(certKey) && StringUtil.isBlank(certKeyGOV)) || StringUtil.isBlank(serviceName) || StringUtil.isBlank(object.getString("serviceNumber")) || StringUtil.isBlank(projectType)) {
                log.info("参数结构不正确，请检查参数结构" + projectNo);
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            Record es = null;


            Date date = EpointDateUtil.convertString2Date(serviceTime);

            Calendar c = new GregorianCalendar();
            c.setTime(date);//设置参数时间
            c.add(Calendar.MINUTE, -5);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            Date newTime = c.getTime(); //这个时间就是日期往后推一天的结果
            String newserviceTime = EpointDateUtil.convertDate2String(newTime, "yyyy-MM-dd HH:mm:ss");

            if ("one".equals(month)) {
                es = service.getServiceByProjectnooneold(projectNo, serviceNumber);
            } else if ("two".equals(month)) {
                es = service.getServiceByProjectnotwo(projectNo, serviceNumber);
            } else if ("three".equals(month)) {
                es = service.getServiceByProjectnothree(projectNo, serviceNumber);
            } else if ("four".equals(month)) {
                es = service.getServiceByProjectnofour(projectNo, serviceNumber);
            } else if ("five".equals(month)) {
                es = service.getServiceByProjectnofive(projectNo, serviceNumber);
            } else if ("six".equals(month)) {
                es = service.getServiceByProjectnosix(projectNo, serviceNumber);
            } else if ("seven".equals(month)) {
                es = service.getServiceByProjectnoseven(projectNo, serviceNumber);
            } else if ("eight".equals(month)) {
                es = service.getServiceByProjectnoeight(projectNo, serviceNumber);
            } else if ("nine".equals(month)) {
                es = service.getServiceByProjectnonine(projectNo, serviceNumber);
            } else if ("ten".equals(month)) {
                es = service.getServiceByProjectnoten(projectNo, serviceNumber);
            }

            if (es != null) {
                log.info("已存在该条服务信息，请勿重复提交" + projectNo);
                return JsonUtils.zwdtRestReturn("0", "已存在该条服务信息，请勿重复提交！", "");
            }

            areacode = areacode.replace("370882", "370812");
            Record r = new Record();

            if ("one".equals(month)) {
                r.setSql_TableName("evaluateserviceone");
            } else if ("two".equals(month)) {
                r.setSql_TableName("evaluateservicetwo");
            } else if ("three".equals(month)) {
                r.setSql_TableName("evaluateservicethree");
            } else if ("four".equals(month)) {
                r.setSql_TableName("evaluateservicefour");
            } else if ("five".equals(month)) {
                r.setSql_TableName("evaluateservicefive");
            } else if ("six".equals(month)) {
                r.setSql_TableName("evaluateservicesix");
            } else if ("seven".equals(month)) {
                r.setSql_TableName("evaluateserviceseven");
            } else if ("eight".equals(month)) {
                r.setSql_TableName("evaluateserviceeight");
            } else if ("nine".equals(month)) {
                r.setSql_TableName("evaluateservicenine");
            } else if ("ten".equals(month)) {
                r.setSql_TableName("evaluateserviceten");
            } else {
                r.setSql_TableName("evaluateserviceone");
            }

            String[] primarykeys = {"projectno", "serviceNumber"};
            r.setPrimaryKeys(primarykeys);
            r.set("taskCode", taskCode);
            r.set("taskHandleItem", taskHandleItem);
            r.set("taskName", taskName);
            r.set("subMatter", subMatter);
            r.set("projectNo", projectNo);
            r.set("proStatus", proStatus);
            r.set("orgcode", ouguid);
            r.set("orgName", orgName);
            r.set("acceptDate", acceptDate);
            r.set("userProp", userProp);
            r.set("userName", userName);
            r.set("userPageType", userPageType);
            r.set("certKey", certKey);
            r.set("certKeyGOV", certKeyGOV);
            r.set("handleUserName", handleUserName);
            r.set("handleUserPageType", handleUserPageType);
            r.set("handleUserPageCode", handleUserPageCode);
            r.set("serviceName", serviceName);
            r.set("serviceNumber", serviceNumber);
            r.set("taskType", taskType);

            // 服务时间
            r.set("serviceTime", newserviceTime);

            r.set("dataSource", "165");
            r.set("projectType", projectType);
            r.set("resultDate", resultDate);
            r.set("createDate", new Date());
            r.set("areacode", areacode.substring(0, 6));
            r.set("tasktype", tasktype);
            r.set("mobile", mobile);
            int i = service.addEvaluateService(r);
            if (i > 0) {
                // 将评价信息上报到国家
                JSONObject json = new JSONObject();
                //log.info("办件数据接口入参信息：" + object);
                json.put("areaCode", areacode);
                json.put("deptCode", deptCode);
                json.put("taskType", taskType);
                //事项编码 
                json.put("taskCode", taskCode);
                json.put("proTaskCode", taskCode);
                //json.put("taskHandleItem", taskHandleItem);
                //事项主题
                json.put("subMatter", subMatter);
                json.put("taskName", taskName);
                json.put("proTaskName", taskName);
                //办件编号
                json.put("projectNo", projectNo);
                json.put("proProjectNo", projectNo);
                json.put("projectName", projectName);
                //办件状态
                json.put("proStatus", proStatus);
                //受理部门
                json.put("proDepart", orgName);
                //手机号码
                json.put("mobile", mobile);
                //申请人名称
                json.put("userName", userName);
                //用户身份编号
                json.put("creditNum", creditNum);
                //证件类型
                json.put("creditType", creditType);
                //申请人类型 自然人=1，企业法人=2，事业法人=3，社会组织法人=4，非法人企业=5，行政机关=6，其他组织=9。
                json.put("userProp", userProp);
                //收件时间
                json.put("acceptDate", acceptDate);
                //承诺办结时间（默认0）
                json.put("promiseDay", promiseDay);
                //承诺办结时间单位（0=天，1=工作日，2=月，3=年）
                json.put("promiseType", "1");
                //法定办结时间（默认0）
                json.put("anticipateDay", anticipateDay);
                //法定办结时间单位（0=天，1=工作日，2=月，3=年）
                json.put("anticipateType", "1");
                //实施单位
                json.put("deptName", orgName);
                //办件渠道(1=线上，2=线下)
                json.put("proChannel", proChannel);
                //即办件=1，承诺办件=2
                json.put("promiseTime", promiseTime);
                //申请人证件号码
                if ("1".equals(proChannel)) {
                    json.put("certKey", certKey);
                } else {
                    json.put("certkeyGOV", certKeyGOV);
                }
                //受理部门编码
                json.put("orgcode", orgcode);
                //受理部门名称
                json.put("orgName", orgName);
                //办理人姓名(用户名)
                json.put("proManager", proManager);
                //申请人证件类型
                json.put("userPageType", userPageType);
                //代理人名称
                json.put("handleUserName", handleUserName);
                //代理人证件类型
                json.put("handleUserPageType", handleUserPageType);
                //代理人证件号码
                json.put("handleUserPageCode", handleUserPageCode);
                //服务名称
                json.put("serviceName", serviceName);
                //服务时间
                // 服务时间
                json.put("serviceTime", newserviceTime);
                //数据来源
                json.put("dataSource", "165");
                //办件类型
                json.put("projectType", projectType);
                //办结时间
                json.put("resultDate", resultDate);

                JSONObject jsonObjectOnline = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                List<JSONObject> list = new ArrayList<>();
                List<String> list1 = new ArrayList<>();
                list.add(json);
                //log.info("办件数据加密前入参：" + list.toString());
                JSONObject submit = new JSONObject();
                Map<String, String> contentsign = new HashMap<String, String>();
                contentsign.put("evaluate", list.toString());
                jsonObject1.put("content", contentsign);
                submit.put("params", jsonObject1);
                String resultsign = TARequestUtil.sendPostInner(HCPARRAYCODEURL, submit.toJSONString(), "", "");
                JSONObject json1 = new JSONObject();
                if (!"修改用户默认地址失败".equals(resultsign)) {
                    json1 = JSON.parseObject(resultsign);
                }
                list1.add(json1.getString("signcontent"));
                jsonObjectOnline.put("evaluate", list1);

                //log.info("办件数据加密后入参：" + resultsign);
                JSONObject contentOnlineMap = new JSONObject();
                String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
                //log.info("办件数据加密前时间：" + time);
                //即将发送时间、应用标识、应用密钥按顺序连接并进行SM2加密。time+appMark+appWord
                //TODO
                JSONObject submit1 = new JSONObject();
                Map<String, String> contentsign1 = new HashMap<String, String>();
                contentsign1.put("content", time + HCPAPPMARK + HCPAPPWORD);
                submit1.put("params", contentsign1);

                String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
                JSONObject json2 = new JSONObject();
                if (!"修改用户默认地址失败".equals(resultsign1)) {
                    json2 = JSON.parseObject(resultsign1);
                }

                contentOnlineMap.put("sign", json2.getString("signcontent"));
                contentOnlineMap.put("params", jsonObjectOnline);
                contentOnlineMap.put("time", time);
                contentOnlineMap.put("appMark", HCPAPPMARK);
                JSONObject submitString = new JSONObject();
                submitString.put("txnBodyCom", contentOnlineMap);
                submitString.put("txnCommCom", new JSONObject());
                //log.info("办件数据所有入参：" + contentOnlineMap.toString());
                //log.info("办件数据url：" + HCPPROJECTSUBMITURL);

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("C-Tenancy-Id", "370000000000");
                //办件提交接口
                String resultOnline = "";
                try {
                    resultOnline = com.epoint.cert.commonutils.HttpUtil.doPostJson(HCPPROJECTSUBMITURL, submitString.toString(), headers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtil.isNotBlank(resultOnline)) {
                    result = JSONObject.parseObject(resultOnline);
                    String code = result.getString("C-Response-Desc");
                    if ("success".equals(code)) {
                        //log.info("新增办件服务对象数据成功");
                        Record rec = new Record();
                        rec.set("sbsign", "1");
                        rec.set("sberrordesc", "同步成功");
                        rec.set("projectno", projectNo);
                        rec.set("servicenumber", serviceNumber);
                        if ("one".equals(month)) {
                            service.updateProServiceoneold(rec);
                        } else if ("two".equals(month)) {
                            service.updateProServicetwo(rec);
                        } else if ("three".equals(month)) {
                            service.updateProServicethree(rec);
                        } else if ("four".equals(month)) {
                            service.updateProServicefour(rec);
                        } else if ("five".equals(month)) {
                            service.updateProServicefive(rec);
                        } else if ("six".equals(month)) {
                            service.updateProServicesix(rec);
                        } else if ("seven".equals(month)) {
                            service.updateProServiceseven(rec);
                        } else if ("eight".equals(month)) {
                            service.updateProServiceeight(rec);
                        } else if ("nine".equals(month)) {
                            service.updateProServicenine(rec);
                        } else if ("ten".equals(month)) {
                            service.updateProServiceten(rec);
                        } else {
                            service.updateProServiceoneold(rec);
                        }
                        return JsonUtils.zwdtRestReturn("1", "新增服务对象成功", result.toString());
                    } else {
                        Record rec = new Record();
                        rec.set("sbsign", "0");
                        rec.set("sberrordesc", code);
                        rec.set("projectno", projectNo);
                        rec.set("servicenumber", serviceNumber);
                        if ("one".equals(month)) {
                            service.updateProServiceoneold(rec);
                        } else if ("two".equals(month)) {
                            service.updateProServicetwo(rec);
                        } else if ("three".equals(month)) {
                            service.updateProServicethree(rec);
                        } else if ("four".equals(month)) {
                            service.updateProServicefour(rec);
                        } else if ("five".equals(month)) {
                            service.updateProServicefive(rec);
                        } else if ("six".equals(month)) {
                            service.updateProServicesix(rec);
                        } else if ("seven".equals(month)) {
                            service.updateProServiceseven(rec);
                        } else if ("eight".equals(month)) {
                            service.updateProServiceeight(rec);
                        } else if ("nine".equals(month)) {
                            service.updateProServicenine(rec);
                        } else if ("ten".equals(month)) {
                            service.updateProServiceten(rec);
                        } else {
                            service.updateProServiceoneold(rec);
                        }

                        log.info("添加评价服务返回结果如下：" + resultOnline);
                        return JsonUtils.zwdtRestReturn("0", "新增服务对象失败", result.toString());
                    }

                } else {
                    Record rec = new Record();
                    rec.set("sbsign", "99");
                    rec.set("sberrordesc", "接口响应异常");
                    rec.set("projectno", projectNo);
                    rec.set("servicenumber", serviceNumber);
                    if ("one".equals(month)) {
                        service.updateProServiceoneold(rec);
                    } else if ("two".equals(month)) {
                        service.updateProServicetwo(rec);
                    } else if ("three".equals(month)) {
                        service.updateProServicethree(rec);
                    } else if ("four".equals(month)) {
                        service.updateProServicefour(rec);
                    } else if ("five".equals(month)) {
                        service.updateProServicefive(rec);
                    } else if ("six".equals(month)) {
                        service.updateProServicesix(rec);
                    } else if ("seven".equals(month)) {
                        service.updateProServiceseven(rec);
                    } else if ("eight".equals(month)) {
                        service.updateProServiceeight(rec);
                    } else if ("nine".equals(month)) {
                        service.updateProServicenine(rec);
                    } else if ("ten".equals(month)) {
                        service.updateProServiceten(rec);
                    } else {
                        service.updateProServiceoneold(rec);
                    }

                    log.info("添加评价服务返回结果如下：" + resultOnline);
                    return JsonUtils.zwdtRestReturn("0", "新增服务对象失败", result.toString());

                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "新增服务对象失败", result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("=======addEvaluateService接口参数：params【" + params + "】=======");
            //log.info("=======addEvaluateService异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "调用失败" + e.getMessage(), "");
        }
    }


    /**
     * 新增线下办件评价
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/addEvaluate", method = RequestMethod.POST)
    public String addEvaluate(@RequestBody String params) {
        try {
            log.info("=======开始调用addEvaluate接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            String projectno = object.getString("projectno");
            String pf = object.getString("pf");
            String satisfaction = object.getString("satisfaction");
            int assessNumber = object.getInteger("assessNumber").intValue();
            String evalDetail = object.getString("evalDetail");
            String Areacode = object.getString("Areacode");
            String evaluateLevel = object.getString("evaluateLevel");
            String Taskname = object.getString("Taskname");
            String Taskcode = object.getString("Taskcode");
            String Deptcode = object.getString("Deptcode");
            String Prodepart = object.getString("Prodepart");
            String Userprop = object.getString("Userprop");
            String Username = object.getString("Username");
            String Applicant = object.getString("Applicant");
            String Certkey = object.getString("Certkey");
            String Certkeygov = object.getString("Certkeygov");
            String Acceptdate = object.getString("Acceptdate");
            String writingEvaluation = object.getString("writingEvaluation");
            String type = object.getString("type");
            String taskType = object.getString("taskType");
            String assessTime = object.getString("assessTime");
            Date date = new Date();
            Calendar c = new GregorianCalendar();
            c.setTime(date);//设置参数时间
            c.add(Calendar.MINUTE, -3);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            date = c.getTime(); //这个时间就是日期往后推一天的结果
            String newassessTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");
            log.info("评价时间hcp:" + newassessTime);
            if ((StringUtil.isBlank(projectno)) || (StringUtil.isBlank(pf)) || (StringUtil.isBlank(satisfaction)) || (StringUtil.isBlank(Integer.valueOf(assessNumber))) || (StringUtil.isBlank(assessTime))) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            int grade = Integer.parseInt(satisfaction);
            if (grade < 3) {
                if (StringUtil.isBlank(evalDetail) && StringUtil.isBlank(writingEvaluation)) {
                    return JsonUtils.zwdtRestReturn("0", "差评必须勾选详情或填写文字评价", "");
                }
            }

            if ("ck".equals(type)) {
                Record bool = service.findEvaluateserviceck(projectno, String.valueOf(assessNumber));
                if (bool != null) {
                    return JsonUtils.zwdtRestReturn("0", "本次服务已存在评价，请勿重复评价！", "");
                }
            } else {
                Boolean bool = service.findEva(projectno, assessNumber);
                if (bool) {
                    return JsonUtils.zwdtRestReturn("0", "本次服务已存在评价，请勿重复评价！", "");
                }
            }
            log.info("projectno:" + projectno + ",assessNumber:" + assessNumber + ",type:" + type);
            Record es = service.getServiceByProjectno(projectno, assessNumber, type);
            if (es == null) {
                return JsonUtils.zwdtRestReturn("0", "没有对应的服务数据", "");
            }
            Record evaluate = service.findEvaluateserviceck(projectno, String.valueOf(assessNumber));
            if (evaluate != null) {
                return JsonUtils.zwdtRestReturn("0", "已经存在对应的评价数据", "");
            }
            Areacode = Areacode.replace("370882", "370812");
            Record r = new Record();
            if ("ck".equals(type)) {
                r.setSql_TableName("evainstance_ck");
            } else {
                r.setSql_TableName("evainstance");
            }
            String[] primarykeys = {"projectno", "assessNumber"};
            r.setPrimaryKeys(primarykeys);
            r.set("Rowguid", UUID.randomUUID().toString());
            r.set("Flag", "I");
            r.set("Appstatus", Integer.valueOf(0));
            r.set("projectno", projectno);
            r.set("Datasource", "165");
            r.set("Assessnumber", Integer.valueOf(1));
            r.set("isdefault", "0");
            r.set("EffectivEvalua", "1");
            r.set("Areacode", Areacode);
            r.set("Prostatus", "3");
            r.set("Evalevel", evaluateLevel);
            r.set("Evacontant", evaluateLevel);
            r.set("evalDetail", evalDetail);
            r.set("writingEvaluation", writingEvaluation);
            r.set("Taskname", Taskname);
            r.set("Taskcode", Taskcode);
            r.set("Taskhandleitem", Taskcode);
            r.set("Promisetime", "1");
            r.set("Deptcode", Deptcode);
            r.set("Prodepart", Prodepart);
            r.set("Userprop", Userprop);
            r.set("Username", Username);
            r.set("Applicant", Applicant);
            r.set("Certkey", Certkey);
            r.set("Certkeygov", Certkeygov);
            r.set("Acceptdate", Acceptdate);
            r.set("createDate", new Date());
            r.set("sync_sign", "0");
            r.set("answerStatus", "0");
            r.set("pf", pf);
            r.set("satisfaction", satisfaction);
            r.set("assessTime", newassessTime);
            r.set("assessNumber", Integer.valueOf(assessNumber));
            r.set("taskType", taskType);
            // 判断是否为医保事项(济宁市医疗保障局)
            FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(Deptcode);
            if (ou != null) {
                if ("11370800MB2855926Y".equals(ou.get("orgcode"))) {
                    r.set("is_yb", "1");
                } else {
                    r.set("is_yb", "0");
                }
            }
            if ("11370800MB2855926Y".equals(Deptcode)) {
                r.set("is_yb", "1");
            }
            else {
                r.set("is_yb", "0");
            }
            int i = this.service.addEvaluate(r);
            if (i > 0) {
                JSONObject json = new JSONObject();
                json.put("taskType", taskType);
                json.put("projectNo", projectno);
                json.put("satisfaction", satisfaction);
                json.put("pf", pf);
                json.put("name", es.getStr("userName"));
                json.put("evalDetail", evalDetail);
                json.put("writingEvaluation", writingEvaluation);
                json.put("assessTime", newassessTime);
                json.put("serviceNumber", Integer.valueOf(assessNumber));
                JSONObject jsonObjectOnline = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                List<JSONObject> list = new ArrayList<>();
                List<String> list1 = new ArrayList<>();
                list.add(json);
                JSONObject submit = new JSONObject();
                Map<String, String> contentsign = new HashMap<String, String>();
                contentsign.put("evaluate", list.toString());
                jsonObject1.put("content", contentsign);
                submit.put("params", jsonObject1);
                String resultsign = TARequestUtil.sendPostInner(HCPARRAYCODEURL, submit.toJSONString(), "", "");
                JSONObject json1 = new JSONObject();
                if (!"修改用户默认地址失败".equals(resultsign)) {
                    json1 = JSON.parseObject(resultsign);
                }
                list1.add(json1.getString("signcontent"));
                jsonObjectOnline.put("evaluate", list1);
                JSONObject contentOnlineMap = new JSONObject();
                String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
                JSONObject submit1 = new JSONObject();
                Map<String, String> contentsign1 = new HashMap<String, String>();
                contentsign1.put("content", time + HCPAPPMARK + HCPAPPWORD);
                submit1.put("params", contentsign1);
                String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
                JSONObject json2 = new JSONObject();
                if (!"修改用户默认地址失败".equals(resultsign1)) {
                    json2 = JSON.parseObject(resultsign1);
                }
                contentOnlineMap.put("sign", json2.getString("signcontent"));
                contentOnlineMap.put("params", jsonObjectOnline);
                contentOnlineMap.put("time", time);
                contentOnlineMap.put("appMark", HCPAPPMARK);
                JSONObject submitString = new JSONObject();
                submitString.put("txnBodyCom", contentOnlineMap);
                submitString.put("txnCommCom", new JSONObject());
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("C-Tenancy-Id", "370000000000");

                String resultOnline = "";
                try {
                    resultOnline = com.epoint.cert.commonutils.HttpUtil.doPostJson(HCPOFFLINETEMPURL, submitString.toString(), headers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject result = new JSONObject();
                JSONObject dataJson = new JSONObject();
                if (StringUtil.isNotBlank(resultOnline)) {
                    result = JSONObject.parseObject(resultOnline);
                    String code = result.getString("C-Response-Desc");
                    if ("success".equals(code) || code.contains("评价数据不可重复")) {
                        Record rec = new Record();
                        rec.set("sbsign", "1");
                        rec.set("sberrordesc", "同步成功");
                        rec.set("projectno", projectno);
                        rec.set("assessNumber", assessNumber);
                        if ("ck".equals(type)) {
                            service.updateEvaCk(rec);
                        } else {
                            service.updateEva(rec);
                        }
                        dataJson.put("success", "success");
                        return JsonUtils.zwdtRestReturn("1", "新增评价数据成功", dataJson.toString());
                    } else if (code.contains("评价数据不可重复")) {
                        Record rec = new Record();
                        rec.set("sbsign", "1");
                        rec.set("sberrordesc", code);
                        rec.set("projectno", projectno);
                        rec.set("assessNumber", assessNumber);
                        if ("ck".equals(type)) {
                            service.updateEvaCk(rec);
                        } else {
                            service.updateEva(rec);
                        }
                        return JsonUtils.zwdtRestReturn("1", "办件已经被评价！", "");
                    } else {
                        Record rec = new Record();
                        rec.set("sbsign", "0");
                        rec.set("sberrordesc", code);
                        rec.set("projectno", projectno);
                        rec.set("assessNumber", assessNumber);
                        if ("ck".equals(type)) {
                            service.updateEvaCk(rec);
                        } else {
                            service.updateEva(rec);
                        }
                        log.info("hcpcontrollor添加评价数据返回结果如下：" + resultOnline + "，对应办件编号：" + projectno);
                        return JsonUtils.zwdtRestReturn("0", "新增评价数据失败", "");
                    }

                } else {
                    Record rec = new Record();
                    rec.set("sbsign", "0");
                    rec.set("sberrordesc", "同步失败");
                    rec.set("projectno", projectno);
                    rec.set("assessNumber", assessNumber);
                    if ("ck".equals(type)) {
                        service.updateEvaCk(rec);
                    } else {
                        service.updateEva(rec);
                    }
                    return JsonUtils.zwdtRestReturn("0", "新增评价数据失败", "");

                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "新增评价数据失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addEvaluate接口参数：params【" + params + "】=======");
            log.info("=======addEvaluate异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "调用失败" + e.getMessage(), "");
        }
    }

    /**
     * 新增线下办件评价（临时方法）
     * 只用于生成数据，不调用省里好差评接口
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/addEvaluateTemporary", method = RequestMethod.POST)
    public String addEvaluateTemporary(@RequestBody String params) {
        try {
            log.info("=======开始调用addEvaluateTemporary接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            String projectno = object.getString("projectno");
            String pf = object.getString("pf");
            String satisfaction = object.getString("satisfaction");
            int assessNumber = object.getInteger("assessNumber").intValue();
            String evalDetail = object.getString("evalDetail");
            String Areacode = object.getString("Areacode");
            String evaluateLevel = object.getString("evaluateLevel");
            String Taskname = object.getString("Taskname");
            String Taskcode = object.getString("Taskcode");
            String Deptcode = object.getString("Deptcode");
            String Prodepart = object.getString("Prodepart");
            String Userprop = object.getString("Userprop");
            String Username = object.getString("Username");
            String Applicant = object.getString("Applicant");
            String Certkey = object.getString("Certkey");
            String Certkeygov = object.getString("Certkeygov");
            String Acceptdate = object.getString("Acceptdate");
            String writingEvaluation = object.getString("writingEvaluation");
            String type = object.getString("type");
            String taskType = object.getString("taskType");

            String assessTime = object.getString("assessTime");

            Date date = new Date();

            Calendar c = new GregorianCalendar();
            c.setTime(date);//设置参数时间
            c.add(Calendar.MINUTE, -3);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            date = c.getTime(); //这个时间就是日期往后推一天的结果
            String newassessTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");

            log.info("评价时间hcp:" + newassessTime);

            if ((StringUtil.isBlank(projectno)) || (StringUtil.isBlank(pf)) || (StringUtil.isBlank(satisfaction)) || (StringUtil.isBlank(Integer.valueOf(assessNumber))) || (StringUtil.isBlank(assessTime))) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            int grade = Integer.parseInt(satisfaction);
            if (grade < 3) {
                if (StringUtil.isBlank(evalDetail) && StringUtil.isBlank(writingEvaluation)) {
                    return JsonUtils.zwdtRestReturn("0", "差评必须勾选详情或填写文字评价", "");
                }
            }

            if ("ck".equals(type)) {
                Record bool = service.findEvaluateserviceck(projectno, String.valueOf(assessNumber));
                if (bool != null) {
                    return JsonUtils.zwdtRestReturn("0", "本次服务已存在评价，请勿重复评价！", "");
                }
            } else {
                Boolean bool = service.findEva(projectno, assessNumber);
                if (bool) {
                    return JsonUtils.zwdtRestReturn("0", "本次服务已存在评价，请勿重复评价！", "");
                }
            }
            log.info("projectno:" + projectno + ",assessNumber:" + assessNumber + ",type:" + type);
            Record es = service.getServiceByProjectno(projectno, assessNumber, type);
            if (es == null) {
                return JsonUtils.zwdtRestReturn("0", "没有对应的服务数据", "");
            }

            Record evaluate = service.findEvaluateserviceck(projectno, String.valueOf(assessNumber));

            if (evaluate != null) {
                return JsonUtils.zwdtRestReturn("0", "已经存在对应的评价数据", "");
            }

            Areacode = Areacode.replace("370882", "370812");

            // 公共服务类型不一致，优先转换
            if ("11".equals(taskType)) {
                taskType = "20";
            }
            if (StringUtil.isNotBlank(taskType)) {
                switch (taskType) {
                    case "01":
                    case "05":
                    case "07":
                    case "08":
                    case "09":
                    case "10":
                    case "20":
                        break;
                    default:
                        taskType = "99";
                        break;
                }
            }

            Record r = new Record();
            if ("ck".equals(type)) {
                r.setSql_TableName("evainstance_ck");
            } else {
                r.setSql_TableName("evainstance");
            }
            String[] primarykeys = {"projectno", "assessNumber"};
            r.setPrimaryKeys(primarykeys);
            r.set("Rowguid", UUID.randomUUID().toString());
            r.set("Flag", "I");
            r.set("Appstatus", Integer.valueOf(0));
            r.set("projectno", projectno);
            r.set("Datasource", "165");
            r.set("Assessnumber", Integer.valueOf(1));
            r.set("isdefault", "0");
            r.set("EffectivEvalua", "1");
            r.set("Areacode", Areacode);
            r.set("Prostatus", "3");
            r.set("Evalevel", evaluateLevel);
            r.set("Evacontant", evaluateLevel);
            r.set("evalDetail", evalDetail);
            r.set("writingEvaluation", writingEvaluation);
            r.set("Taskname", Taskname);
            r.set("Taskcode", Taskcode);
            r.set("Taskhandleitem", Taskcode);
            r.set("Promisetime", "1");
            r.set("Deptcode", Deptcode);
            r.set("Prodepart", Prodepart);
            r.set("Userprop", Userprop);
            r.set("Username", Username);
            r.set("Applicant", Applicant);
            r.set("Certkey", Certkey);
            r.set("Certkeygov", Certkeygov);
            r.set("Acceptdate", Acceptdate);
            r.set("createDate", new Date());
            r.set("sync_sign", "0");
            r.set("answerStatus", "0");
            r.set("pf", pf);
            r.set("satisfaction", satisfaction);
            r.set("assessTime", newassessTime);
            r.set("assessNumber", Integer.valueOf(assessNumber));
            r.set("taskType", taskType);
            int i = this.service.addEvaluate(r);
            if (i > 0) {
                return JsonUtils.zwdtRestReturn("1", "新增评价数据成功！", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "新增评价数据失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addEvaluate接口参数：params【" + params + "】=======");
            log.info("=======addEvaluate异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "调用失败" + e.getMessage(), "");
        }
    }

    /**
     * 新增线下办件评价
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/addEvaluateRecord", method = RequestMethod.POST)
    public String addEvaluateRecord(@RequestBody String params) {
//            log.info("=======开始调用addEvaluate接口=======");
        JSONObject jsonObject = JSONObject.parseObject(params);

        JSONObject object = (JSONObject) jsonObject.get("params");
        if (object == null) {
            return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
        }
        String projectno = object.getString("projectno");
        String userName = object.getString("name");
        String serviceNumber = object.getString("serviceNumber");
        String areacode = object.getString("areacode");
        String taskType = object.getString("taskType");

        JSONObject json = new JSONObject();
        json.put("taskType", taskType);
        json.put("projectNo", projectno);
        json.put("satisfaction", "5");
        json.put("pf", "1");
        json.put("name", userName);
        json.put("evalDetail", "510,517");
        json.put("writingEvaluation", "");
        Date date = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(date);//设置参数时间
        c.add(Calendar.MINUTE, -2);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date = c.getTime(); //这个时间就是日期往后推一天的结果
        String newassessTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");
        json.put("assessTime", newassessTime);
        json.put("serviceNumber", serviceNumber);

        JSONObject jsonObjectOnline = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        List<JSONObject> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list.add(json);
        //log.info("线下新增评价加密前入参：" + list.toString());
        JSONObject submit = new JSONObject();
        Map<String, String> contentsign = new HashMap<String, String>();
        contentsign.put("evaluate", list.toString());
        jsonObject1.put("content", contentsign);
        submit.put("params", jsonObject1);
        String resultsign = TARequestUtil.sendPostInner(HCPARRAYCODEURL, submit.toJSONString(), "", "");
        JSONObject json1 = new JSONObject();
        if (!"修改用户默认地址失败".equals(resultsign)) {
            json1 = JSON.parseObject(resultsign);
        }
        list1.add(json1.getString("signcontent"));
        jsonObjectOnline.put("evaluate", list1);

        //log.info("办件数据加密后入参：" + resultsign);
        JSONObject contentOnlineMap = new JSONObject();
        String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
        //log.info("办件数据加密前时间：" + time);

        JSONObject submit1 = new JSONObject();
        Map<String, String> contentsign1 = new HashMap<String, String>();
        contentsign1.put("content", time + HCPAPPMARK + HCPAPPWORD);
        submit1.put("params", contentsign1);

        String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
        JSONObject json2 = new JSONObject();
        if (!"修改用户默认地址失败".equals(resultsign1)) {
            json2 = JSON.parseObject(resultsign1);
        }

        contentOnlineMap.put("sign", json2.getString("signcontent"));
        contentOnlineMap.put("params", jsonObjectOnline);
        contentOnlineMap.put("time", time);
        contentOnlineMap.put("appMark", HCPAPPMARK);
        JSONObject submitString = new JSONObject();
        submitString.put("txnBodyCom", contentOnlineMap);
        submitString.put("txnCommCom", new JSONObject());
        //log.info("办件数据所有入参：" + contentOnlineMap.toString());
        //log.info("办件数据url：" + HCPOFFLINETEMPURL);
        Record r = new Record();
        r.setSql_TableName("evainstanceeight");
        String[] primarykeys = {"projectno", "assessNumber"};
        r.setPrimaryKeys(primarykeys);
        r.set("Rowguid", UUID.randomUUID().toString());
        r.set("Flag", "I");
        r.set("Appstatus", Integer.valueOf(0));
        r.set("projectno", projectno);
        r.set("areacode", areacode);
        r.set("Datasource", "165");
        r.set("isdefault", "0");
        r.set("EffectivEvalua", "1");
        r.set("Evalevel", "5");
        r.set("Evacontant", "");
        r.set("evalDetail", "510,517");
        r.set("writingEvaluation", "");
        r.set("Promisetime", "1");
        r.set("createDate", new Date());
        r.set("sync_sign", "0");
        r.set("answerStatus", "0");
        r.set("pf", "1");
        r.set("satisfaction", "5");
        r.set("assessTime", newassessTime);
        r.set("assessNumber", Integer.valueOf(serviceNumber));
        r.set("taskType", taskType);

        JSONObject result = new JSONObject();
        String resultOnline = "";
        try {
            resultOnline = HttpUtil.doPostJson(HCPOFFLINETEMPURL, submitString.toString());
            //log.info("添加评价数据返回结果如下：" + resultOnline);
            if (StringUtil.isNotBlank(resultOnline)) {
                result = JSONObject.parseObject(resultOnline);
                String code = result.getString("C-Response-Desc");
                if ("success".equals(code)) {
                    r.set("sbsign", "1");
                    r.set("sberrordesc", "同步成功");
                    service.addEvaluate(r);
                    return JsonUtils.zwdtRestReturn("1", "新增评价数据成功", "");
                } else {
                    r.set("sbsign", "99");
                    r.set("sberrordesc", code);
                    service.addEvaluate(r);
                    log.info("评价数据推送失败！");
                    return JsonUtils.zwdtRestReturn("0", code, "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "新增评价数据失败", "");
            }
        } catch (Exception e) {
            r.set("sbsign", "98");
            r.set("sberrordesc", "接口调用失败");
            service.addEvaluate(r);
            log.info("评价数据推送失败！");
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "新增评价数据失败", "");
        }
    }

    /**
     * 根据用户标识获取是否存在待评价数据
     *
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getisdpj", method = RequestMethod.POST)
    public String getisdpj(HttpServletRequest request) {
        try {
            String applyername = "";
            String applyerpagecode = "";

            String header = request.getHeader("Authorization");
            String access_token = header.substring(7);
            if (StringUtil.isNotBlank(access_token)) {
                String userInfo = HcpUtil.getUserDataZwdt(access_token);
                JSONObject user = JSONObject.parseObject(userInfo).getJSONObject("custom");
                applyername = user.getString("clientname");
                applyerpagecode = user.getString("idnumber");
            }

            //String isdpj = service.getisdpj(applyername, applyerpagecode);

            JSONObject dataJson = new JSONObject();
            //dataJson.put("isdpj", isdpj);
            return JsonUtils.zwdtRestReturn("1", "获取是否待评价成功", dataJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getisdpj异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取是否待评价失败" + e.getMessage(), "");
        }
    }

    /**
     * 获取我的评价
     *
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getMyEvaluate", method = RequestMethod.POST)
    public String getMyEvaluate(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyEvaluate接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            //办件名
            String projectname = object.getString("projectname");
            // 证件号码code
            String applyerpagecode = object.getString("applyerpagecode");
            // 申请人名
            String applyername = object.getString("applyername");
            // 状态  1.待评价  2.已评价
            Integer status = object.getInteger("status");
            Integer currentpage = object.getInteger("currentpage");
            Integer pagesize = object.getInteger("pagesize");

            List<Record> projectlist = service.getMyEvaluate(projectname, applyername, applyerpagecode, status, currentpage, pagesize);
            int totalcount = service.getMyEvaluateCount(projectname, applyername, applyerpagecode, status);

            HcpUtil util = new HcpUtil();
            for (Record project : projectlist) {
                String proStatus = project.get("projectstatus");
                project.set("projectstatus", util.projectStatus(proStatus));
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("projectlist", projectlist);
            dataJson.put("totalcount", totalcount);
            return JsonUtils.zwdtRestReturn("1", "获取评价成功", JSON.toJSONStringWithDateFormat(dataJson, "yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMyEvaluate异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取评价失败" + e.getMessage(), "");
        }
    }


    /**
     * 办件详情查询
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getProService", method = RequestMethod.POST)
    public String getProService(@RequestBody String params) {
        try {

            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }

            //办件no
            String projectno = object.getString("projectno");
            int servicenumber = object.getInteger("servicenumber");
            if (StringUtil.isBlank(projectno)) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }

            Record project = service.getProService(projectno, servicenumber);
            JSONObject projectjson = new JSONObject();
            projectjson.put("project", project);

            return JsonUtils.zwdtRestReturn("1", "获取办件服务详情成功",
                    JSON.toJSONStringWithDateFormat(projectjson, "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProService接口参数：params【" + params + "】=======");
            log.info("=======getProService异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件服务详情失败" + e.getMessage(), "");
        }
    }

    /**
     * 山东附件库
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static Map<String, Object> downloadFile(String url) throws Exception {
        Map<String, Object> map = new HashMap<>();
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        GetMethod getMethod = new GetMethod(url);
        client.executeMethod(getMethod);
        long len = getMethod.getResponseContentLength();
        InputStream inputStream = getMethod.getResponseBodyAsStream();
        map.put("length", len);
        map.put("stream", inputStream);
        return map;
    }


}
