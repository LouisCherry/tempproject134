package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaire.rest;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.IAuditZnsbQuestionnaireQuestionService;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.entity.AuditZnsbQuestionnaireQuestion;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.inter.IZCAuditZnsbQuestioninfoService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain.AuditZnsbQuestionnaire;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.inter.IZCAuditZnsbQuestionnaireService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.domain.AuditZnsbQuestionnaireresult;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.inter.IZCAuditZnsbQuestionnaireresultService;

@RestController
@RequestMapping("/zcquestionnairerest")
public class ZCAuditZnsbQuestionnaireRestController
{

    @Autowired
    private IZCAuditZnsbQuestionnaireService questionnaireService;
    @Autowired
    private IZCAuditZnsbQuestioninfoService questioninfoService;
    @Autowired
    private IZCAuditZnsbQuestiondetailService questiondetailservice;
    @Autowired
    private IZCAuditZnsbQuestionnaireresultService questionnaireReresultService;
    @Autowired
    private IAuditZnsbQuestionnaireQuestionService questionnaireQuestionService;

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    List<String> strings;
    JSONObject dataJsonjk = new JSONObject();

    /**
     * 获取问卷列表
     *
     * @param params
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/getquestionnairelist", method = RequestMethod.POST)
    public String getQuestionnaireList(@RequestBody String params, HttpServletRequest request) throws ParseException {
        try {
            log.info("====获取数据=====");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String searchname = obj.getString("searchname");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            if ("0".equals(pageSize)) {
                return JsonUtils.zwdtRestReturn("0", "入参不符规定", "");
            }
            String centerguid = obj.getString("centerguid");
            int firstint = 0;
            if (StringUtil.isNotBlank(currentPage)) {
                firstint = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
            }
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.setSelectFields("rowguid,questionnairename,is_use,createtime as time");
            if (StringUtil.isNotBlank(searchname)) {
                sqlConditionUtil.like("questionnairename", searchname);
            }
            sqlConditionUtil.eq("is_use", "1");
            sqlConditionUtil.eq("centerguid", centerguid);
            sqlConditionUtil.setOrderDesc("sort");
            sqlConditionUtil.setOrderDesc("operatedate");
            PageData<AuditZnsbQuestionnaire> listpagedate = questionnaireService.getQuestionnaireListPageData(
                    sqlConditionUtil.getMap(), firstint, Integer.parseInt(pageSize), "sort", "asc").getResult();
            if (listpagedate == null) {
                return JsonUtils.zwdtRestReturn("0", "查询失败", "");
            }
            for (AuditZnsbQuestionnaire questionnaire : listpagedate.getList()) {
                List<AuditZnsbQuestionnaireQuestion> questionlist = questionnaireQuestionService
                        .findListByQuestionNaireGuid(questionnaire.getRowguid());
                if (questionlist != null && !questionlist.isEmpty()) {
                    questionnaire.set("num", questionlist.size());
                }
                else {
                    questionnaire.set("num", 5);
                }
                questionnaire.set("time",
                        EpointDateUtil.convertDate2String(questionnaire.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("list", listpagedate.getList());
            dataJson.put("totalcount", listpagedate.getRowCount());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取问卷详情
     *
     * @param params
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/getquestionnairedetail", method = RequestMethod.POST)
    public String getQuestionnaireDetail(@RequestBody String params, HttpServletRequest request) throws ParseException {
        try {
            log.info("====获取数据=====");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String questionnairerowguid = obj.getString("questionnairerowguid");

            List<JSONObject> questionList = new ArrayList<>();
            AuditZnsbQuestionnaire questionnaire = null;
            JSONObject questionnaireInfo = new JSONObject();
            strings = new ArrayList<>();
            if (StringUtil.isNotBlank(questionnairerowguid)) {
                questionnaire = questionnaireService.find(questionnairerowguid);
                if (questionnaire != null) {
                    List<AuditZnsbQuestioninfo> questioninfoList = questionnaireQuestionService
                            .findQuestionInfo(questionnairerowguid);
                    if (questioninfoList.isEmpty()) {
                        return JsonUtils.zwdtRestReturn("0", "问卷是空的！", "");
                    }
                    for (AuditZnsbQuestioninfo question : questioninfoList) {
                        JSONObject questionInfo = new JSONObject();
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.clear();
                        sql.eq("questionguid", question.getRowguid());
                        sql.setSelectFields("rowguid,answeroption");
                        sql.setOrderDesc("sort");
                        List<AuditZnsbQuestiondetail> answeroption = questiondetailservice
                                .findListByQuestionInfoguid(sql.getMap(), 0, 0, "sort", "asc").getResult().getList();
                        questionInfo.put("question", question);
                        questionInfo.put("answeroption", answeroption);
                        strings.add(question.getQuestion());
                        for (AuditZnsbQuestiondetail questiondetail : answeroption) {
                            strings.add(questiondetail.getAnsweroption());
                        }

                        if (answeroption.isEmpty()) {
                            return JsonUtils.zwdtRestReturn("0", "选项未配置！", "");
                        }

                        if (!answeroption.isEmpty()) {
                            questionList.add(questionInfo);
                        }
                    }
                    questionnaireInfo.put("questionnairename", questionnaire.getQuestionnairename());
                    if (StringUtil.isNotBlank(questionnaire.getQuestionnairedescription())) {
                        questionnaireInfo.put("questionnairedescription", questionnaire.getQuestionnairedescription());
                    }
                    else {
                        questionnaireInfo.put("questionnairedescription", "");
                    }

                    questionnaireInfo.put("questionlist", questionList);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "问卷不存在！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数不能为空！", "");
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("questionnaire", questionnaireInfo);
            dataJsonjk = dataJson;
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 增加问卷答案
     *
     * @param params
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/addquestionnaireinfo", method = RequestMethod.POST)
    public String add(@RequestBody String params) throws ParseException {
        try {
            log.info("====接口执行=====");
            JSONObject dataJson = new JSONObject();
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String MacAddress = (String) obj.get("MacAddress");
            String centerguid = (String) obj.get("centerguid");
            if (StringUtil.isBlank(centerguid)) {
                return JsonUtils.zwdtRestReturn("0", "中心参数不能为空", "");
            }
            String questionnaireguid = (String) obj.get("questionnaireguid");
            JSONObject questionnaireinfo = (JSONObject) obj.get("questionnaireinfo");
            String questionnaire = questionnaireinfo.getString("questionnaire");
            String optionarr = questionnaireinfo.getString("optionarr");
            if (StringUtil.isBlank(MacAddress)) {
                dataJson.put("msg", "设备地址不能为空！");
            }
            else if (StringUtil.isBlank(optionarr)) {
                dataJson.put("msg", "问卷答案不能为空！");
            }
            else {
                JSONArray questionlist = JSONObject.parseObject(questionnaire).getJSONArray("questionlist");
                String[] split = optionarr.split(",");
                if (questionlist.size() > split.length) {
                    return JsonUtils.zwdtRestReturn("0", "问题答案不能为空", "");
                }
                AuditZnsbQuestionnaireresult result = new AuditZnsbQuestionnaireresult();
                result.setRowguid(UUID.randomUUID().toString());
                result.setOperatedate(new Date());
                result.setOperateusername(null);
                result.setQuestionnairereinfo(questionnaire);
                result.setQuestionnairereanswer(optionarr);
                result.setMacaddress(MacAddress);
                result.setCenterguid(centerguid);
                result.setQuestionnaireguid(questionnaireguid);

                // 判断接口入参的json有没有被修改
                String o = String.valueOf(dataJsonjk.get("questionnaire"));
                String replaceAll = questionnaire.replaceAll(",\"typeText\":\"单选题\"", "")
                        .replaceAll(",\"typeText\":\"多选题\"", "").replaceAll(",\"type\":\"only\"", "")
                        .replaceAll(",\"type\":\"all\"", "");
                if (o.equals(replaceAll)) {
                    JSONObject questionnaireJson = JSONObject.parseObject(questionnaire);
                    // 判断问卷是否被删除
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("rowguid", questionnaireguid);
                    PageData<AuditZnsbQuestionnaire> result1 = questionnaireService
                            .getQuestionnaireListPageData(sql.getMap(), 0, 0, null, null).getResult();
                    if (result1.getRowCount() > 0) {
                        result.setQuestionnairename((String) questionnaireJson.get("questionnairename"));
                        // 判断问卷是否被修改
                        List<AuditZnsbQuestioninfo> questioninfos = questionnaireQuestionService
                                .findQuestionInfo(questionnaireguid);
                        List<String> list = new ArrayList<>();
                        List<String> listarr = new ArrayList<>();
                        for (AuditZnsbQuestioninfo questioninfo : questioninfos) {
                            list.add(questioninfo.getQuestion());
                            sql.clear();
                            sql.eq("questionguid", questioninfo.getRowguid());
                            sql.setOrderDesc("sort");
                            PageData<AuditZnsbQuestiondetail> result2 = questiondetailservice
                                    .findListByQuestionInfoguid(sql.getMap(), 0, 0, null, null).getResult();
                            if (StringUtil.isNotBlank(result2.getList())) {
                                for (AuditZnsbQuestiondetail questiondetail : result2.getList()) {
                                    list.add(questiondetail.getAnsweroption());
                                    listarr.add(questiondetail.getRowguid());
                                }
                            }
                        }
                        if (strings.equals(list)) {
                            boolean flag = false;
                            List<String> objects = new ArrayList<>();
                            String[] arr = optionarr.replaceAll("\"", "").replaceAll("]", "").replaceAll("\\[", "")
                                    .split(",");
                            // 判断答案选项的guid是否都正确
                            for (String s : arr) {
                                if (listarr.contains(s)) {
                                    AuditZnsbQuestiondetail questiondetail = questiondetailservice.find(s);
                                    if ("0".equals(questioninfoService.find(questiondetail.getQuestionguid())
                                            .getQuestiontype())) {
                                        objects.add(questiondetail.getQuestionguid());
                                    }
                                    flag = true;
                                }
                                else {
                                    flag = false;
                                    break;
                                }
                            }

                            if (flag) {
                                // 判断是否存在单选问题答案多选
                                Set<String> stringSet = new HashSet<>(objects);
                                if (objects.size() == stringSet.size()) {
                                    questionnaireReresultService.insert(result);
                                    JSONArray answerList = JSONArray.parseArray(result.getQuestionnairereanswer());
                                    int rightcount = 0;
                                    int errorcount = 0;
                                    if (questionlist != null && !questionlist.isEmpty()) {
                                        // 遍历判断答案是否正确
                                        for (int i = 0; i < questionlist.size(); i++) {
                                            JSONObject questioninfo = questionlist.getJSONObject(i);
                                            JSONObject question = questioninfo.getJSONObject("question");
                                            List<AuditZnsbQuestiondetail> questiondetails = questiondetailservice
                                                    .findListByQuestionGuid(question.getString("rowguid"));
                                            if (questiondetails != null && !questiondetails.isEmpty()) {
                                                // 遍历所有选项
                                                for (AuditZnsbQuestiondetail questiondetail : questiondetails) {
                                                    if ("1".equals(questiondetail.getAnswer())) {
                                                        // 此选项是正确答案,提交答案中没有此选项则为错误
                                                        if (!answerList.contains(questiondetail.getRowguid())) {
                                                            errorcount++;
                                                            questionlist.getJSONObject(i).put("isright", 0);
                                                            break;
                                                        }
                                                    }
                                                    else {
                                                        // 此选项非正确答案，提交的答案中有此选项则为错误
                                                        if (answerList.contains(questiondetail.getRowguid())) {
                                                            errorcount++;
                                                            questionlist.getJSONObject(i).put("isright", 0);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        rightcount = questionlist.size() - errorcount;
                                    }

                                    dataJson.put("resultrowguid", result.getRowguid());
                                    dataJson.put("rightcount", rightcount);
                                    dataJson.put("errorcount", errorcount);
                                    dataJson.put("msg", "提交成功");
                                }
                                else {
                                    dataJson.put("msg", "参数有误");
                                }
                            }
                            else {
                                dataJson.put("msg", "参数有误");
                            }
                        }
                        else {
                            dataJson.put("msg", "问卷修改");
                        }
                    }
                    else {
                        dataJson.put("msg", "问卷删除");
                    }
                }
                else {
                    dataJson.put("msg", "参数有误");
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取问卷结果
     *
     * @param params
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/questionnaireresult", method = RequestMethod.POST)
    public String getQuestionnaireResult(@RequestBody String params, HttpServletRequest request) throws ParseException {
        try {
            log.info("====获取数据=====");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String rowguid = obj.getString("resultrowguid");
            JSONObject dataJson = new JSONObject();
            AuditZnsbQuestionnaireresult questionnaireresult = questionnaireReresultService.find(rowguid);
            if (questionnaireresult != null) {
                JSONObject questionnairereinfo = JSONObject.parseObject(questionnaireresult.getQuestionnairereinfo());
                JSONArray answerList = JSONArray.parseArray(questionnaireresult.getQuestionnairereanswer());
                JSONArray questionlist = (JSONArray) questionnairereinfo.get("questionlist");
                int rightcount = 0;
                int errorcount = 0;
                if (questionlist != null && !questionlist.isEmpty()) {
                    // 遍历判断答案是否正确
                    for (int i = 0; i < questionlist.size(); i++) {
                        JSONObject questioninfo = questionlist.getJSONObject(i);
                        JSONObject question = questioninfo.getJSONObject("question");
                        List<AuditZnsbQuestiondetail> questiondetails = questiondetailservice
                                .findListByQuestionGuid(question.getString("rowguid"));
                        List<String> rightlist = questiondetailservice
                                .findRightListByQuestionGuid(question.getString("rowguid"));
                        questionlist.getJSONObject(i).put("rightlist", rightlist);
                        if (questiondetails != null && !questiondetails.isEmpty()) {
                            questionlist.getJSONObject(i).put("isright", 1);
                            // 遍历所有选项
                            for (AuditZnsbQuestiondetail questiondetail : questiondetails) {
                                if ("1".equals(questiondetail.getAnswer())) {
                                    // 此选项是正确答案,提交答案中没有此选项则为错误
                                    if (!answerList.contains(questiondetail.getRowguid())) {
                                        errorcount++;
                                        questionlist.getJSONObject(i).put("isright", 0);
                                        break;
                                    }
                                }
                                else {
                                    // 此选项非正确答案，提交的答案中有此选项则为错误
                                    if (answerList.contains(questiondetail.getRowguid())) {
                                        errorcount++;
                                        questionlist.getJSONObject(i).put("isright", 0);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    rightcount = questionlist.size() - errorcount;
                }
                dataJson.put("rightcount", rightcount);
                dataJson.put("errorcount", errorcount);
                dataJson.put("question", questionlist);
                dataJson.put("answer", answerList);
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "未查询到该问卷！", "");
            }

        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取导出问卷记录
     */
    @RequestMapping(value = "/exportRoutinginspect", method = RequestMethod.POST)
    public String exportRootingInspect(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String guid = obj.getString("guid");
            List<JSONObject> jsonObjects = new ArrayList<>();
            SqlConditionUtil sql1 = new SqlConditionUtil();
            // SqlConditionUtil sql2 = new SqlConditionUtil();
            SqlConditionUtil sql3 = new SqlConditionUtil();
            String questionnaireguid = questionnaireReresultService.find(guid).getQuestionnaireguid();
            String questionnairename = questionnaireService.find(questionnaireguid).getQuestionnairename();
            sql1.eq("questionnaireguid", questionnaireguid);
            JSONObject dataobj1;
            JSONObject dataobj2;
            JSONObject dataobj3;
            List<String> strings = new ArrayList<>();
            List<String> stringList = new ArrayList<>();
            PageData<AuditZnsbQuestionnaireresult> result = questionnaireReresultService
                    .getAuditZnsbQuestionnaireResult(sql1.getMap(), 0, 0, "", "").getResult();
            List<AuditZnsbQuestionnaireresult> list = result.getList();
            // 拿问卷选择数量
            for (AuditZnsbQuestionnaireresult questionnaireresult : list) {
                String questionnairereanswer = questionnaireresult.getQuestionnairereanswer();
                JSONArray answers = JSONObject.parseArray(questionnairereanswer);
                for (int i = 0; i < answers.size(); i++) {
                    stringList.add(String.valueOf(answers.get(i)));
                }
            }
            sql1.clear();
            sql1.eq("rowguid", questionnaireguid);
            List<AuditZnsbQuestionnaire> znsbQuestionnaires = questionnaireService
                    .getQuestionnaireListPageData(sql1.getMap(), 0, 0, "", "").getResult().getList();
            for (AuditZnsbQuestionnaire questionnaire : znsbQuestionnaires) {
                dataobj1 = new JSONObject();
                dataobj1.put("leixing", "问卷名称");
                dataobj1.put("questionnairename", questionnairename);
                dataobj1.put("countxuanxiang", list.size());
                jsonObjects.add(dataobj1);
                List<AuditZnsbQuestioninfo> znsbQuestioninfos = questionnaireQuestionService
                        .findQuestionInfo(questionnaireguid);
                int wenti = 0;
                for (AuditZnsbQuestioninfo questioninfo : znsbQuestioninfos) {
                    dataobj2 = new JSONObject();
                    wenti++;
                    dataobj2.put("leixing", "问题" + wenti);
                    if ("0".equals(questioninfo.getQuestiontype())) {
                        dataobj2.put("questionnairename", questioninfo.getQuestion() + "(单选)");
                    }
                    else if ("2".equals(questioninfo.getQuestiontype())) {
                        dataobj2.put("questionnairename", questioninfo.getQuestion() + "(多选)");
                    }
                    else {
                        dataobj2.put("questionnairename", questioninfo.getQuestion() + "(判断)");
                    }
                    jsonObjects.add(dataobj2);
                    sql3.eq("questionguid", questioninfo.getRowguid());
                    sql3.setOrderDesc("sort");
                    List<AuditZnsbQuestiondetail> znsbQuestiondetails = questiondetailservice
                            .findListByQuestionInfoguid(sql3.getMap(), 0, 0, "", "").getResult().getList();
                    for (AuditZnsbQuestiondetail questiondetail : znsbQuestiondetails) {
                        strings.add(questiondetail.getRowguid());
                        dataobj3 = new JSONObject();
                        dataobj3.put("questionnairename", questiondetail.getAnsweroption());
                        dataobj3.put("name", questiondetail.getRowguid());
                        for (int i = 0; i < strings.size(); i++) {
                            Integer count = getCount(strings.get(i), stringList);
                            dataobj3.put("countxuanxiang", count);
                            jsonObjects.add(dataobj3);
                        }
                        jsonObjects.add(dataobj3);
                    }
                }
            }
            List<JSONObject> objects = jsonObjects.stream().distinct().collect(Collectors.toList());
            dataJson.put("list", objects);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 判断数组元素个数
    public Integer getCount(String rowguid, List<String> strings) {
        int count = 0;
        for (int i = 0; i < strings.size(); i++) {
            if (rowguid.equals(strings.get(i))) {
                count++;
            }
        }
        return count;
    }
}
