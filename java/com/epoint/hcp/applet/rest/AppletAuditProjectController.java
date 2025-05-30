package com.epoint.hcp.applet.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.hcp.api.IHcpService;
import com.epoint.hcp.api.entity.Evainstance;
import com.epoint.hcp.applet.api.IOnlineUserService;
import com.epoint.zwdt.util.TARequestUtil;
import com.epoint.zwdt.zwdtrest.space.api.ISpaceAcceptService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

/**
 * 小程序获取办件信息接口
 *
 * @author shibin
 * @description
 * @date 2020年4月14日 上午10:22:46
 */
@RestController
@RequestMapping("/appletAuditProjectController")
public class AppletAuditProjectController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ISpaceAcceptService iSpaceAcceptService;
    @Autowired
    private IHcpService service;
    @Autowired
    private IOnlineUserService onlineUserService;
    @Autowired
    private IAuditTask auditTaskService;
    @Autowired
    private IAuditOrgaWindow auditOrgaWindow;
    private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "ZwfwHcpAddEvaluate");

    /**
     * 根据分值获取未评价指标
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getGradeInfo", method = RequestMethod.POST)
    public String getGradeInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用getgradeinfo接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            String openid = "";
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject object = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            JSONObject dataJson = new JSONObject();
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String grade = object.getString("grade");
                if (StringUtil.isNotBlank(grade)) {
                    List<Record> list = iSpaceAcceptService.getGradinfo(grade);
                    dataJson.put("applyinfo", list);
                    return JsonUtils.zwdtRestReturn("1", "获取评价指标信息成功", dataJson.toJSONString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "参数grade有空值，请检查参数结构", "");
                }
            } else {
                log.info("=======结束调用getAppletProject接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份认证失败！", dataJson.toJSONString());
            }

        } catch (Exception e) {
            log.debug("=======getGradeInfo接口参数：params【" + params + "】=======");
            log.debug("=======getGradeInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取评价指标失败" + e.getMessage(), "");
        }
    }


    /**
     * 获取办件详情信息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getAuditProjectDetail1", method = RequestMethod.POST)
    public String getAuditProjectDetail1(@RequestBody String params) {
        try {
            log.info("=======开始调用getAuditProjectDetail接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            String openid = "";
            String projectguid = "";
            JSONObject json = JSONObject.parseObject(params);
            log.info("params入参====" + json.toString());
            JSONObject jsonObject = (JSONObject) json.get("params");
            String token = json.getString("token");
            JSONObject dataJson = new JSONObject();
            if (jsonObject == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            Record rec = new Record();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                projectguid = jsonObject.getString("projectguid");
                AuditProject auditProject = onlineUserService.getAuditProjectByRowGuid(projectguid);
                if (auditProject != null) {
                    if (StringUtil.isBlank(auditProject.getStr("hcpstatus"))) {
                        rec.put("taskname", auditProject.getProjectname());
                        rec.put("projectguid", auditProject.getRowguid());
                        rec.put("is_evaluat", 0);
                        rec.put("flowsn", auditProject.getFlowsn());
                        rec.put("ouname", auditProject.getOuname());
                        rec.put("applydate", EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                "yyyy-MM-dd hh:mm:ss"));
                    } else {
                        rec.put("taskname", auditProject.getProjectname());
                        rec.put("projectguid", auditProject.getRowguid());
                        rec.put("is_evaluat", auditProject.getStr("hcpstatus"));
                        rec.put("flowsn", auditProject.getFlowsn());
                        rec.put("ouname", auditProject.getOuname());
                        rec.put("applydate", EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                "yyyy-MM-dd hh:mm:ss"));

                        List<Evainstance> recordzb = onlineUserService.getZbByFlowsn(auditProject.get("flowsn"));
                        JSONArray js = new JSONArray();
                        String grade = "";
                        String writingEvaluation = "";
                        if (StringUtil.isNotBlank(recordzb) && recordzb.size() > 0) {
                            if (recordzb.get(0).get("evalDetail") != null
                                    && recordzb.get(0).get("evalDetail") != "" && StringUtil.isNotBlank(recordzb.get(0).get("evalDetail"))) {
                                String zhibiao = recordzb.get(0).get("evalDetail");
                                String[] arr = zhibiao.split(",");
                                for (int i = 0; i < arr.length; i++) {
                                    Record zb = onlineUserService.getZhibiao(arr[i]);
                                    JSONObject jzb = new JSONObject();
                                    if (StringUtil.isNotBlank(zb)) {
                                        jzb.put("itemtext", zb.get("itemtext"));
                                        jzb.put("ITEMVALUE", zb.get("ITEMVALUE"));
                                    }
                                    js.add(jzb);
                                }
                            }
                            if (StringUtil.isBlank(recordzb.get(0).get("writingEvaluation"))) {
                                rec.put("writingEvaluation", "nrwk");
                            } else {
                                rec.put("writingEvaluation", recordzb.get(0).get("writingEvaluation"));
                            }
                            rec.put("zhibiaolist", js);
                            if (StringUtil.isNotBlank(recordzb.get(0).get("satisfaction"))) {
                                rec.put("grade", Integer.parseInt(recordzb.get(0).getStr("satisfaction")));
                            } else {
                                rec.put("grade", "");
                            }
                        }

                    }
                    log.info(rec);
                    dataJson.put("applyinfo", rec);
                    log.info("=======结束调用getAuditProjectDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
                } else {
                    log.info("=======结束调用getAuditProjectDetail接口=======");
                    return JsonUtils.zwdtRestReturn("0", "该办件不存在！", dataJson.toJSONString());
                }

            } else {
                log.info("=======结束调用getAuditProjectDetail接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份认证失败！", dataJson.toJSONString());
            }
        } catch (Exception e) {
            log.info("=======错误信息=========:" + e.getMessage());
            log.info("=======结束调用getAuditProjectDetail接口=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！", e.getMessage());
        }
    }

    /**
     * 获取办件详情信息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/addEvaluate", method = RequestMethod.POST)
    public String addEvaluate(@RequestBody String params) {
        try {
            log.info("=======开始调用getAuditProjectDetail接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            String openid = "";
            String projectguid = "";
            JSONObject json = JSONObject.parseObject(params);
            log.info("params入参====" + json.toString());
            JSONObject jsonObject = (JSONObject) json.get("params");
            String token = json.getString("token");
            JSONObject dataJson = new JSONObject();
            if (jsonObject == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }
            if (ZwdtConstant.SysValidateData.equals(token)) {
                projectguid = jsonObject.getString("projectguid");
                String projectno = jsonObject.getString("projectno");
                String satisfaction = jsonObject.getString("satisfaction");
                String evalDetail = jsonObject.getString("evalDetail");
                String Taskname = jsonObject.getString("taskname");
                String writingEvaluation = jsonObject.getString("writingEvaluation");
                int pf = jsonObject.getIntValue("pf");
                AuditProject auditproject = onlineUserService.getAuditProjectByRowGuid(projectguid);
                AuditTask audittask = auditTaskService.selectTaskByRowGuid(auditproject.getTaskguid()).getResult();
                JSONObject paramJson = new JSONObject();
                paramJson.put("projectNo", "37" + auditproject.getFlowsn());
                paramJson.put("pf", pf);
                paramJson.put("areacode", auditproject.getAreacode());
                List<Evainstance> hcpRecord = onlineUserService.getZbByFlowsn("37" + projectno);
                if (hcpRecord.size() > 0 && StringUtil.isNotBlank(hcpRecord)) {
                    paramJson.put("proStatus", hcpRecord.get(0).get("prostatus"));
                    paramJson.put("assessNumber", hcpRecord.get(0).get("servicenumber"));
                } else {
                    paramJson.put("proStatus", 3);
                    paramJson.put("assessNumber", 1);
                }
                paramJson.put("satisfaction", satisfaction);
                paramJson.put("evalDetail", evalDetail);
                paramJson.put("writingEvaluation", writingEvaluation);
                AuditOrgaWindow window = auditOrgaWindow.getWindowByWindowGuid(auditproject.getWindowguid()).getResult();
                if (StringUtil.isNotBlank(window)) {
                    paramJson.put("winNo", window.getWindowno());
                    paramJson.put("winName", window.getWindowname());
                }
                // paramJson.put("wt", true);
                JSONObject dataParam = new JSONObject();
                dataParam.put("params", paramJson);
                String dataResult = TARequestUtil.sendPost(HCPAPPMARK, dataParam.toString(), "", "");
                log.info("====dataResult===" + dataResult);
                dataJson.put("msg", "ok");
                return JsonUtils.zwdtRestReturn("1", "提交成功！", dataJson.toJSONString());
            } else {
                log.info("=======结束调用getAuditProjectDetail接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份认证失败！", dataJson.toJSONString());
            }
        } catch (Exception e) {
            log.info("=======错误信息=========:" + e.getMessage());
            log.info("=======结束调用getAuditProjectDetail接口=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！", e.getMessage());
        }
    }


}
