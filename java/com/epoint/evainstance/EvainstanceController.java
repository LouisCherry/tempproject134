package com.epoint.evainstance;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.HttpRequestUtils;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping({"/evainstance"})
public class EvainstanceController {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IEvainstanceService service;
    @Autowired
    private IAuditProject projectService;

    @Autowired
    private IAuditTask auditTaskService;


    //获取好差评评价详情接口
    @RequestMapping(value = "/getEvalDetail", method = RequestMethod.POST)
    public String getEvalDetail(@RequestBody String params) {
        try {
            log.debug("=======开始调用getEvalDetail接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            String grade = object.getString("grade");
            List<Record> list = service.getEvalDetail(grade);

            JSONObject dataJson = new JSONObject();
            dataJson.put("evaldetail", list);
            return JsonUtils.zwdtRestReturn("1", "评价详情调用成功", dataJson.toString());
        } catch (Exception e) {
            log.debug("=======getEvalDetail接口参数：params【" + params + "】=======");
            log.debug("=======getEvalDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "调用失败" + e.getMessage(), "");
        }
    }

    //保存好差评评价数据接口
    @RequestMapping(value = "/addEvainstance", method = RequestMethod.POST)
    public String addEvainstance(@RequestBody String params) {
        try {
            this.log.debug("=======开始调用addEvainstance接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            String projectno = object.getString("projectno");
            String projectno1 = object.getString("projectNo");
            String pf = "4";
            String satisfaction = object.getString("satisfaction");
            Integer assessNumber = object.getInteger("assessNumber");
            String winNo = object.getString("winNo");
            String winName = object.getString("winName");
            String evalDetail = object.getString("evalDetail");
            String writingEvaluation = object.getString("writingEvaluation");
            String areacode = object.getString("areacode");

            String projectguid = "";
            if (StringUtil.isNotBlank(projectno)) {
                projectguid = projectno;
            }

            if (StringUtil.isNotBlank(projectno1)) {
                projectguid = projectno1;
            }

            if (StringUtil.isBlank(projectguid)) {
                return JsonUtils.zwdtRestReturn("0", "办件信息未查询到！", "");
            } else {
                if (projectguid.length() < 2) {
                    return JsonUtils.zwdtRestReturn("0", "办件信息未查询到！", "");
                }
            }

            if ("37".equals(projectguid.substring(0, 2))) {
                projectguid = projectguid.substring(2);
            }
            Record record = service.findAuditProjectByFlown(projectguid);

            if (record == null) {
                return JsonUtils.zwdtRestReturn("0", "办件信息未查询到！", "");
            }

            String userName = record.get("applyername");
            Integer applyertype = record.getInt("applyertype");
            String creditNum = record.get("certnum");
            JSONObject evJson = new JSONObject();
            evJson.put("commented", "0");
            evJson.put("mark", "0");
            evJson.put("userName", userName);
            evJson.put("creditType", 111);
            evJson.put("creditNum", creditNum);
            Record auditProject = service.findAuditProjectByFlown(projectguid);

            assessNumber = 1;

            if ((StringUtil.isBlank(projectguid)) || (StringUtil.isBlank(pf)) || (StringUtil.isBlank(satisfaction))
                    || (assessNumber == null)) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            int grade = Integer.parseInt(satisfaction);
            if ((grade < 3) && (StringUtil.isBlank(evalDetail)) && (StringUtil.isBlank(writingEvaluation))) {
                return JsonUtils.zwdtRestReturn("0", "差评必须勾选详情或填写文字评价", "");
            }

            List<Record> es = service.getServiceByProjectno("37" + projectguid);
            if (es.isEmpty()) {
                es = service.getServiceByProjectno(projectguid);
                return JsonUtils.zwdtRestReturn("0", "没有对应的服务数据", "");
            }

            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(record.getStr("taskguid"), true).getResult();


            String assessTime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
            String url = ConfigUtil.getConfigValue("zwdtparam", "addEvaluateTemporary");

            JSONObject auditobject = new JSONObject();
            auditobject.put("projectno", "37" + projectguid);
            auditobject.put("pf", pf);
            auditobject.put("satisfaction", satisfaction);
            auditobject.put("assessTime", assessTime);
            auditobject.put("assessNumber", assessNumber);
            auditobject.put("serviceNumber", assessNumber);
            auditobject.put("writingEvaluation", writingEvaluation);
            auditobject.put("evalDetail", evalDetail);
            auditobject.put("Areacode", areacode);
            auditobject.put("winNo", winNo);
            auditobject.put("winName", winName);
            auditobject.put("evaluateLevel", satisfaction);
            auditobject.put("type", "ck");
            auditobject.put("Taskname", es.get(0).getStr("Taskname"));
            auditobject.put("Taskcode", es.get(0).getStr("taskCode"));
            auditobject.put("Deptcode", es.get(0).getStr("orgcode"));
            auditobject.put("Prodepart", es.get(0).getStr("orgName"));
            auditobject.put("Userprop", es.get(0).getStr("userProp"));
            auditobject.put("Username", es.get(0).getStr("Username"));
            auditobject.put("Applicant", es.get(0).getStr("Username"));
            auditobject.put("Certkey", es.get(0).getStr("certKey"));
            auditobject.put("Certkeygov", es.get(0).getStr("certKeyGOV"));
            auditobject.put("Acceptdate", es.get(0).getStr("acceptDate"));
            auditobject.put("mobile", es.get(0).getStr("mobile"));

            if (auditTask != null) {
                String taskType = auditTask.getShenpilb();
                // 公共服务类型不一致，优先转换
                if ("11".equals(taskType)) {
                    taskType = "20";
                }
                switch (taskType) {
                    case "1":
                        taskType = "01";
                        break;
                    case "2":
                        taskType = "02";
                        break;
                    case "3":
                        taskType = "03";
                        break;
                    case "4":
                        taskType = "04";
                        break;
                    case "5":
                        taskType = "05";
                        break;
                    case "6":
                        taskType = "06";
                        break;
                    case "7":
                        taskType = "07";
                        break;
                    case "8":
                        taskType = "08";
                        break;
                    case "9":
                        taskType = "09";
                        break;
                    case "10":
                    case "11":
                    case "12":
                    case "13":
                    case "14":
                    case "15":
                        break;
                    default:
                        taskType = "99";
                        break;
                }

                auditobject.put("taskType", taskType);
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("params", auditobject);
            String str = "";

            //暂时关闭评价器评价  网厅换个接口，只生成数据不调用好差评省里接口

            try {
                str = HttpRequestUtils.sendPost(url, dataJson.toJSONString(),
                        new String[]{"application/json;charset=utf-8"});
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtil.isNotBlank(str)) {
                JSONObject jsonobject = JSONObject.parseObject(str);
                JSONObject jsonstatus = (JSONObject) jsonobject.get("status");
                if ("200".equals(jsonstatus.get("code").toString())) {
                    JSONObject jsoncustom = (JSONObject) jsonobject.get("custom");
                    if ("1".equals(jsoncustom.get("code").toString())) {
                        this.log.info("=====办件数据推送成功！");
                    } else
                        this.log.info("=====办件数据推送失败！==传参：" + dataJson.toJSONString() + ";===原因："
                                + jsoncustom.get("text").toString());
                } else {
                    this.log.info("=====请求失败！");
                }
            } else {
                this.log.info("=====网厅连接失败");
            }

            return JsonUtils.zwdtRestReturn("1", "评价数据新增成功", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("=======addEvainstance接口参数：params【" + params + "】=======");
            log.debug("=======addEvainstance异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "调用失败" + e.getMessage(), "");
        }
    }
}