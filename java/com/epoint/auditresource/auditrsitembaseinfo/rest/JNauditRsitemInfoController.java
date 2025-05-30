package com.epoint.auditresource.auditrsitembaseinfo.rest;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditresource.auditrsitembaseinfo.api.IJNauditRsItemBaseinfoservice;
import com.epoint.auditresource.auditspisubappopinion.api.IAuditSpISubappOpinionService;
import com.epoint.auditresource.auditspisubappopinion.api.entity.AuditSpISubappOpinion;
import com.epoint.auditsp.auditsphandle.api.IIndividualAuditRsitemBaseinfo;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspireview.domain.AuditSpIReview;
import com.epoint.basic.auditsp.auditspireview.inter.IAuditSpIReview;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.bizlogic.sysconf.systemparameters.service.ConfigServiceImpl;
import com.epoint.basic.controller.api.ApiBaseController;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.convert.ChineseToPinyin;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.user.api.IUserService;

@RestController
@RequestMapping("/JointInfo")
public class JNauditRsitemInfoController extends ApiBaseController
{

    @Autowired
    private IAuditSpInstance auditSpInstance;
    @Autowired
    private IAuditSpISubapp auditSpISubapp;
    @Autowired
    private IIndividualAuditRsitemBaseinfo auditRsItemBaseinfo;
    @Autowired
    private IAuditSpIReview auditSpIReviewService;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IAuditSpISubappOpinionService auditSpISubappOpinionService;
    @Autowired
    private IAttachService attachService;
    @Autowired
    private IUserService userservice;
    @Autowired
    private IJNauditRsItemBaseinfoservice jnauditRsItemBaseinfoservice;
    /**
    * 日志
    */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取项目阶段信息，pc端使用
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getPhaseInfo", method = RequestMethod.POST)
    public String getPhaseInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用getPhaseInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String xiangmuguid = obj.getString("xiangmuguid");
            List<Record> list = jnauditRsItemBaseinfoservice.getphase(xiangmuguid);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            JSONObject dataJson = new JSONObject();
            List<JSONObject> infoList = new ArrayList<JSONObject>();
            if (list != null) {
                for (Record phase : list) {
                    Date createdate = phase.getDate("CREATEDATE");
                    Date finishdate = phase.getDate("FINISHDATE");
                    JSONObject infoJson = new JSONObject();
                    infoJson.put("phasename", phase.get("phasename"));
                    infoJson.put("phaseguid", phase.get("rowguid"));
                    infoJson.put("createdate", sdf.format(createdate));
                    if (finishdate != null) {
                        infoJson.put("sumday", ((finishdate.getTime() - createdate.getTime()) / (1000 * 60 * 60 * 24)));
                    }
                    else {
                        infoJson.put("sumday", ((new Date().getTime() - createdate.getTime()) / (1000 * 60 * 60 * 24)));
                    }
                    infoJson.put("total", phase.get("cum"));
                    infoJson.put("sum", phase.get("su"));
                    infoList.add(infoJson);
                }
                dataJson.put("infolist", infoList);
                dataJson.put("projectname", list.get(0).getStr("itemname"));
                dataJson.put("themename", list.get(0).getStr("BUSINESSNAME"));
            }
            log.info("=======结束调用getPhaseInfo接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取项目阶段信息成功！", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getPhaseInfo接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目阶段信息异常：" + e.getMessage(), "");
        }
    }

    /**
     *  获取阶段性信息，pc端使用
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getPhaseSonInfo", method = RequestMethod.POST)
    public String getPhaseSonInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用getPhaseInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String phaseguid = obj.getString("phaseguid");
            String xiangmuguid = obj.getString("xiangmuguid");
            List<AuditSpITask> tasklist = jnauditRsItemBaseinfoservice.gettaskbybigguid(xiangmuguid, phaseguid);
            System.out.println("tasklist:" + tasklist);
            if (tasklist.isEmpty()) {
            	tasklist = jnauditRsItemBaseinfoservice.gettaskbybigguidOld(xiangmuguid, phaseguid);
            }
            JSONObject dataJson = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            List<JSONObject> infoList = new ArrayList<JSONObject>();
            JSONObject ouinfo = new JSONObject();
            int endproject = 0;
            if (tasklist != null) {
                Date finishdate = null;
                Date begindate = null;
                for (AuditSpITask phase : tasklist) {
                    if (StringUtil.isNotBlank(phase.get("projectguid"))) {
                        JSONObject infoJson = new JSONObject();
                        infoJson.put("biguid", phase.getBiguid());
                        infoJson.put("taskname", phase.get("taskname"));
                        infoJson.put("ouname", phase.get("ouname"));
                        String oupy = ChineseToPinyin.getPingYin(phase.get("ouname"));
                        System.out.println("phase:" + phase);
                        infoJson.put("ounamepy", oupy);
                        ouinfo.put(oupy,
                                (ouinfo == null || ouinfo.getInteger(oupy) == null) ? 1 : ouinfo.getInteger(oupy) + 1);
                        infoJson.put("taskguid", phase.get("taskguid"));
                        int status = 0;
                        String statu = phase.getStr("status");
                        if (statu != null) {
                            status = Integer.parseInt(statu);
                        }
                        else {
                            status = 99;
                        }
                        infoJson.put("status", status);
                        if (status < 26) {
                            infoJson.put("state", "todo");
                        }
                        else if (status == 26) {
                            infoJson.put("state", "receive");
                        }
                        else if (status > 26 && status < 90) {
                            infoJson.put("state", "doing");
                        }
                        else if (status >= 90) {
                            ouinfo.put(oupy + "end", (ouinfo == null || ouinfo.getInteger(oupy + "end") == null) ? 1
                                    : ouinfo.getInteger(oupy + "end") + 1);
                            infoJson.put("state", "done");
                            endproject++;
                        }
                        else {
                            infoJson.put("state", "doing");
                        }

                        infoJson.put("projectguid", phase.get("projectguid"));
                        infoJson.put("num", "");
                        String date = "开始时间：";
                        Date start = phase.getDate("RECEIVEDATE");
                        Date end = phase.getDate("BANJIEDATE");

                        
                        infoJson.put("begindate", EpointDateUtil.convertDate2String(start, "yyyy-MM-dd HH:mm"));
                        infoJson.put("enddate", EpointDateUtil.convertDate2String(end, "yyyy-MM-dd HH:mm"));
                        if (start != null) {
                            date += sdf.format(start) + "<br/>结束时间：";
                            if (begindate == null || begindate.after(start)) {
                                begindate = start;
                            }
                        }
                        if (end != null) {
                            long date0 = start.getTime();
                            long date1 = end.getTime();
                            long day = (date1 - date0) / (1000 * 60 * 60 * 24);
                            long hour = (date1 - date0) / (60 * 60 * 1000) - day * 24;
                            long min = (date1 - date0) / (60 * 1000) - hour * 60 - day * 24 * 60;
                            date = sdf.format(end) + "<br/>合计用时：" + day + "天" + hour + "小时" + min + "分钟";
                            
                            infoJson.put("usertime",  day + "天" + hour + "小时" + min + "分钟");
                            
                            if (finishdate == null || end.after(finishdate)) {
                                finishdate = end;
                            }
                        }
                        else if (phase.getInt("status") >= 90) {
                            date += sdf.format(start);
                        }
                        infoJson.put("date", date);
                        String time = phase.getStr("SPAREMINUTES");
                        if (StringUtil.isNotBlank(time) && status < 90) {
                            int i = Integer.parseInt(time);
                            int syts = i / 60 / 24;
                            int ss = i / 60 % 24;
                            if (syts < 0 && ss > 0) {
                                infoJson.put("light", "warn");
                            }
                            if (i < 0) {
                                infoJson.put("light", "danger");
                            }
                        }
                        infoList.add(infoJson);

                    }
                }
                if (endproject == tasklist.size()) {
                    if (endproject == 0) {
                        dataJson.put("status", "todo");
                    }
                    else {
                        dataJson.put("status", "done");
                    }
                    if (tasklist.size() == 0) {
                        dataJson.put("sumday", 0);
                    }
                    else if (finishdate != null && begindate != null && finishdate.after(begindate)) {
                        long usertime = (finishdate.getTime() - begindate.getTime()) / (1000 * 60 * 60 * 24);
                        dataJson.put("sumday", usertime == 0 ? 1 : usertime);
                    }
                }
                else {
                    dataJson.put("status", "doing");
                }
                dataJson.put("infolist", infoList);
                dataJson.put("ouinfo", ouinfo);
            }
            log.info("=======结束调用getPhaseInfo接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取项目阶段详情成功！", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getPhaseInfo接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目阶段详情异常：" + e.getMessage(), "");
        }
    }

    /**
     *  封装阶段性事项申报办理信息，用于app项目详情接口
     *  @param phaseguid
     *  @param xiangmuguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public JSONObject getTasklistByPhase(String phaseguid, String xiangmuguid) {
        List<AuditSpITask> tasklist = jnauditRsItemBaseinfoservice.gettaskbybigguid(xiangmuguid, phaseguid);
        JSONObject dataJson = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        List<JSONObject> infoList = new ArrayList<JSONObject>();
        JSONObject ouinfo = new JSONObject();
        int endproject = 0;
        if (tasklist != null) {
            Date finishdate = null;
            Date begindate = null;
            for (AuditSpITask phase : tasklist) {
                JSONObject infoJson = new JSONObject();
                infoJson.put("biguid", phase.getBiguid());
                infoJson.put("taskname", phase.get("taskname"));
                infoJson.put("ouname", phase.get("ouname"));
                String oupy = ChineseToPinyin.getPingYin(phase.get("ouname"));
                infoJson.put("ounamepy", oupy);
                System.out.println("oupy:" + oupy);
                System.out.println("ouinfo:" + ouinfo);
                ouinfo.put(oupy, (ouinfo == null || ouinfo.getInteger(oupy) == null) ? 1 : ouinfo.getInteger(oupy) + 1);
                infoJson.put("taskguid", phase.get("taskguid"));
                int status = 0;
                String statu = phase.getStr("status");
                if (statu != null) {
                    status = Integer.parseInt(statu);
                }
                else {
                    status = 99;
                }
                infoJson.put("status", status);
                if (status < 26) {
                    infoJson.put("state", "todo");
                }
                else if (status == 26) {
                    infoJson.put("state", "receive");
                }
                else if (status > 26 && status < 90) {
                    infoJson.put("state", "doing");
                }
                else if (status == 90) {
                    ouinfo.put(oupy + "end", (ouinfo == null || ouinfo.getInteger(oupy + "end") == null) ? 1
                            : ouinfo.getInteger(oupy + "end") + 1);
                    infoJson.put("state", "done");
                    endproject++;
                }
                else {
                    infoJson.put("state", "doing");
                }

                infoJson.put("projectguid", phase.get("projectguid"));
                infoJson.put("num", "");
                String date = "开始时间：";
                Date start = phase.getDate("RECEIVEDATE");
                Date end = phase.getDate("BANJIEDATE");
                if (start != null) {
                    date += sdf.format(start) + "<br/>结束时间：";
                    if (begindate == null || begindate.after(start)) {
                        begindate = start;
                    }
                }
                if (end != null) {
                    long date0 = start.getTime();
                    long date1 = end.getTime();
                    long day = (date1 - date0) / (1000 * 60 * 60 * 24);
                    long hour = (date1 - date0) / (60 * 60 * 1000) - day * 24;
                    long min = (date1 - date0) / (60 * 1000) - hour * 60 - day * 24 * 60;
                    date += sdf.format(end) + "<br/>合计用时：" + day + "天" + hour + "小时" + min + "分钟";
                    if (finishdate == null || end.after(finishdate)) {
                        finishdate = end;
                    }
                }
                else if (phase.getInt("status") >= 90) {
                    date += sdf.format(start);
                }
                infoJson.put("date", date);
                String time = phase.getStr("SPAREMINUTES");
                if (status < 90 && StringUtil.isNotBlank(time)) {
                    int i = Integer.parseInt(time);
                    int syts = i / 60 / 24;
                    int ss = i / 60 % 24;
                    if (syts < 0 && ss > 0) {
                        infoJson.put("light", "warn");
                    }
                    if (i < 0) {
                        infoJson.put("light", "danger");
                    }
                }
                infoList.add(infoJson);
                if (endproject == tasklist.size()) {
                    dataJson.put("status", "done");
                    if (finishdate != null && begindate != null && finishdate.after(begindate)) {
                        long usertime = finishdate.getTime() - begindate.getTime();
                        dataJson.put("sumday", usertime / (1000 * 60 * 60 * 24));
                        dataJson.put("finishdate", finishdate);
                        dataJson.put("begindate", begindate);
                    }
                }
                else {
                    dataJson.put("status", "doing");
                }
            }
            if (tasklist.size() == 0) {
                dataJson.put("sumday", -1);
            }
            dataJson.put("infolist", infoList);
            dataJson.put("ouinfo", ouinfo);
        }
        return dataJson;
    }

    /**
     * 获取并联审批办件信息
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getProjectbyTaskguid", method = RequestMethod.POST)
    public String getProjectbyTaskguid(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectbyTaskguid接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String taskguid = obj.getString("taskguid");
            String biguid = obj.getString("biguid");
            AuditProject project = jnauditRsItemBaseinfoservice.getProjectByTaskguid(taskguid, biguid);
            JSONObject dataJson = new JSONObject();
            if (project != null) {
                String attachurl = new ConfigServiceImpl().getFrameConfigValue("attach_url");
                Date begindate = project.getReceivedate();
                Date enddate = project.getBanjiedate();
                String usetime = "";
                //计算办件用时
                if (enddate != null) {
                    long date0 = begindate.getTime();
                    long date1 = enddate.getTime();
                    long day = (date1 - date0) / (1000 * 60 * 60 * 24);
                    long hour = (date1 - date0) / (60 * 60 * 1000) - day * 24;
                    long min = (date1 - date0) / (60 * 1000) - hour * 60 - day * 24 * 60;
                    usetime = day + "天" + hour + "小时" + min + "分钟";
                    project.put("usertime", usetime);
                }
                List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(project.getRowguid());
                JSONArray attajson = new JSONArray();
                for (FrameAttachInfo frameAttachInfo : attachlist) {
                    JSONObject json = new JSONObject();
                    json.put("name", frameAttachInfo.getAttachFileName());
                    json.put("attachguid", frameAttachInfo.getAttachGuid());
                    json.put("url", attachurl + frameAttachInfo.getAttachGuid());
                    attajson.add(json);
                }
                dataJson.put("resultlist", attajson);
            }
            dataJson.put("project", project);
            log.info("=======结束调用getProjectbyTaskguid接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取办件信息成功！", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getProjectbyTaskguid接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取子阶段信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取项目详情接口，包括项目基本信息、阶段信息、项目图片、视频等
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getRsItemInfo", method = RequestMethod.POST)
    public String getRsItemInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用getRsItemInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String xiangmuguid = obj.getString("xiangmuguid");
            JSONObject dataJson = new JSONObject();
            String attachurl = new ConfigServiceImpl().getFrameConfigValue("attach_url");
            //获取项目基本信息
            AuditRsItemBaseinfo iteminfo = auditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(xiangmuguid).getResult();
            iteminfo.setItemtype(codeItemsService.getItemTextByCodeName("项目类型", iteminfo.getItemtype()));
            iteminfo.setConstructionproperty(
                    codeItemsService.getItemTextByCodeName("建设性质", iteminfo.getConstructionproperty()));
            iteminfo.setItemlegalcerttype(
                    codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", iteminfo.getItemlegalcerttype()));
            String biGuid = iteminfo.getBiguid();
            AuditSpInstance spInstance = auditSpInstance.getDetailByBIGuid(biGuid).getResult();
            Date createdate = iteminfo.getOperatedate();
            Date pingshengdate = iteminfo.getOperatedate();
            Date finishdate = null;
            int time = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (spInstance != null) {
                createdate = spInstance.getCreatedate();
                List<AuditSpIReview> auditSpIReviews = auditSpIReviewService.getReviewByBIGuid(biGuid).getResult();
                if (auditSpIReviews != null && auditSpIReviews.size() > 0) {
                    pingshengdate = auditSpIReviews.get(0).getPingshengdate();
                }
                SqlConditionUtil sql1 = new SqlConditionUtil();
                sql1.eq("subapp.biguid", biGuid);
                sql1.isNotBlank("finishdate");

                iteminfo.put("createdate", createdate);
                iteminfo.put("pingshengdate", pingshengdate);
                //领导意见
                String sqlopinion = "select username,createdate,content from audit_sp_i_subapp_opinion where biguid=? and type='1' order by createdate desc";
                List<AuditSpISubappOpinion> opintlist = auditSpISubappOpinionService.findList(sqlopinion, biGuid);
                //faq答疑
                String sqlfaq = "select username,createdate,faqcontent,faqanswer,faqanswerdate,faqansweruser from audit_sp_i_subapp_opinion where biguid=? and type='2' order by createdate desc";
                List<AuditSpISubappOpinion> faqlist = auditSpISubappOpinionService.findList(sqlfaq, biGuid);

                dataJson.put("opinionlist", opintlist);
                dataJson.put("faqlist", faqlist);
                String imgcliengguid = spInstance.getStr("imgcliengguid");
                JSONObject videojson = new JSONObject();
                JSONObject imgjson = new JSONObject();
                if (StringUtil.isNotBlank(imgcliengguid) && imgcliengguid != null) {
                    //获取项目实例图片信息
                    List<FrameAttachInfo> imglist = attachService
                            .getAttachInfoListByGuid(spInstance.getStr("imgcliengguid"));
                    for (FrameAttachInfo frameAttachInfo : imglist) {
                        JSONObject img = new JSONObject();
                        String uploaddate = sdf.format(frameAttachInfo.getUploadDateTime());
                        JSONArray dateimg = imgjson.getJSONArray(uploaddate);
                        if (dateimg == null) {
                            dateimg = new JSONArray();
                        }
                        img.put("name", frameAttachInfo.getAttachFileName());
                        img.put("attachguid", frameAttachInfo.getAttachGuid());
                        img.put("url", attachurl + frameAttachInfo.getAttachGuid());
                        dateimg.add(img);
                        imgjson.put(uploaddate, dateimg);
                    }
                }
                String videocliengguid = spInstance.getStr("videocliengguid");
                if (StringUtil.isNotBlank(videocliengguid) && videocliengguid != null) {
                    //获取项目实例视频信息
                    List<FrameAttachInfo> videolist = attachService
                            .getAttachInfoListByGuid(spInstance.getStr("videocliengguid"));
                    for (FrameAttachInfo frameAttachInfo : videolist) {
                        JSONObject video = new JSONObject();
                        String uploaddate = sdf.format(frameAttachInfo.getUploadDateTime());
                        JSONArray date = imgjson.getJSONArray(uploaddate);
                        if (date == null) {
                            date = new JSONArray();
                        }
                        video.put("name", frameAttachInfo.getAttachFileName());
                        video.put("attachguid", frameAttachInfo.getAttachGuid());
                        video.put("url", attachurl + frameAttachInfo.getAttachGuid());
                        date.add(video);
                        videojson.put(uploaddate, date);
                    }
                }
                dataJson.put("imglist", imgjson);
                dataJson.put("videolist", videojson);
                //阶段信息
                List<Record> list = jnauditRsItemBaseinfoservice.getphase(xiangmuguid);
                SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
                List<JSONObject> infoList = new ArrayList<JSONObject>();
                int phaseindex = 0;
                if (list != null) {
                    for (Record phase : list) {
                        Date createdate0 = phase.getDate("CREATEDATE");
                        JSONObject infoJson = new JSONObject();
                        infoJson.put("phasename", phase.get("phasename"));
                        infoJson.put("phaseguid", phase.get("rowguid"));
                        infoJson.put("createdate", sdf0.format(createdate0));

                        //获取阶段性事项信息
                        JSONObject rtn = getTasklistByPhase(phase.getStr("rowguid"), xiangmuguid);
                        if (rtn != null && StringUtil.isNotBlank(rtn.getString("sumday"))) {
                            String sumday = rtn.getString("sumday");
                            switch (sumday) {
                                case "-1":
                                    sumday = "0";
                                    break;
                                case "0":
                                    sumday = "1";
                                    break;
                                default:
                                    break;
                            }
                            infoJson.put("sumday", sumday);
                            rtn.put("sumday", sumday);
                            Date finishdate2 = rtn.getDate("finishdate");
                            if (finishdate == null || (finishdate2 != null && finishdate.before(finishdate2))) {
                                finishdate = rtn.getDate("finishdate");
                                phaseindex++;
                            }
                        }
                        else {
                            infoJson.put("sumday",
                                    ((new Date().getTime() - createdate0.getTime()) / (1000 * 60 * 60 * 24)));
                        }
                        infoJson.put("tasklist", rtn);
                        infoJson.put("total", phase.get("cum"));
                        infoJson.put("sum", phase.get("su"));
                        infoList.add(infoJson);
                        iteminfo.put("phasename", phase.get("phasename"));
                    }
                    if (phaseindex == 4) {
                        if (finishdate != null) {
                            time = (int) ((finishdate.getTime() - createdate.getTime()) / (1000 * 60 * 60 * 24));
                            iteminfo.put("time", time == 0 ? "1" : time);
                        }
                    }
                    else {
                        iteminfo.put("time", (new Date().getTime() - createdate.getTime()) / (1000 * 60 * 60 * 24));
                    }
                    dataJson.put("phasedata", infoList);
                }
            }
            dataJson.put("iteminfo", iteminfo);
            log.info("=======结束调用getRsItemInfo接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取项目详情成功！", dataJson.toString());

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getRsItemInfo接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 添加领导批复接口
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/setOpinion", method = RequestMethod.POST)
    public String setOpinion(@RequestBody String params) {
        try {
            log.info("=======开始调用setOpinion接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String xiangmuguid = obj.getString("xiangmuguid");
            String rowguid = obj.getString("rowguid");
            String content = obj.getString("content");
            String userguid = getUserguidFromOauth();
            String username = obj.getString("username");
            if (StringUtil.isBlank(userguid)) {
                log.info("=======setOpinion接口用户数据未获取到=======");
            }
            else {
                username = userservice.getUserNameByUserGuid(userguid);
            }
            String type = obj.getString("type");
            JSONObject dataJson = new JSONObject();
            AuditRsItemBaseinfo iteminfo = auditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(xiangmuguid).getResult();
            if (iteminfo != null) {
                AuditSpISubappOpinion opinion = new AuditSpISubappOpinion();
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                    opinion.setRowguid(UUID.randomUUID().toString());
                    opinion.setBiguid(iteminfo.getBiguid());
                    opinion.setCreatedate(new Date());
                    opinion.setUserguid(userguid);
                    opinion.setUsername(username);
                    opinion.setContent(content);
                    opinion.setType(type);
                    auditSpISubappOpinionService.insert(opinion);
                }
                else {
                    //else中内容app不用，用于手动调接口修改其它类型信息内容
                    opinion = auditSpISubappOpinionService.find(rowguid);
                    if (opinion != null) {
                        opinion.setFaqanswer(content);
                        opinion.setType(type);
                        opinion.setFaqansweruser(username);
                        opinion.setFaqansweruserguid(userguid);
                        opinion.setFaqanswerdate(new Date());
                        auditSpISubappOpinionService.update(opinion);
                    }
                }
            }
            else {
                log.info("=======结束调用setOpinion接口=======");
                return JsonUtils.zwdtRestReturn("0", "查无改项目", dataJson.toString());
            }
            log.info("=======结束调用setOpinion接口=======");
            return JsonUtils.zwdtRestReturn("1", "领导批复成功！", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======setOpinion接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "领导批复异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取项目实例列表数据
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getSpInstanceinfoList", method = RequestMethod.POST)
    public String getSpInstanceinfoList(@RequestBody String params) {
        try {
            log.info("=======开始调用getSpInstanceinfoList接口=======");
            JSONObject jsonObject = JSON.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String areacode = obj.getString("areacode");
            int first = obj.getIntValue("first");
            int pageSize = obj.getIntValue("pageSize");
            SqlConditionUtil sql = new SqlConditionUtil();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            //搜索条件
            sql.eq("businesstype", "1");
            sql.eq("areacode", areacode);
            String sortField = "createdate";
            String sortOrder = "desc";
            PageData<AuditSpInstance> pageData = auditSpInstance
                    .getAuditSpInstanceByPage(sql.getMap(), first * pageSize, pageSize, sortField, sortOrder)
                    .getResult();
            List<AuditSpInstance> instancelist = pageData.getList();
            for (AuditSpInstance ins : instancelist) {
                SqlConditionUtil sql1 = new SqlConditionUtil();
                sql1.eq("subapp.biguid", ins.getRowguid());
                PageData<Record> rec = auditSpISubapp.getWaitHandleSubapp(0, 1, sql1.getMap()).getResult();
                if (rec != null && rec.getList().size() > 0) {
                    ins.set("phasename", rec.getList().get(0).getStr("phasename"));
                }
                else {
                    ins.set("phasename", "阶段申报未开始");
                }
                //获取个性化的项目图片内容
                List<FrameAttachInfo> slt = new ArrayList<>();
                if (StringUtil.isNotBlank(ins.getStr("sltcliengguid"))) {
                    slt = attachService.getAttachInfoListByGuid(ins.getStr("sltcliengguid"));
                }
                if (slt != null && slt.size() > 0) {
                    String attachurl = new ConfigServiceImpl().getFrameConfigValue("attach_url");
                    ins.set("imgurl", attachurl + slt.get(0).getAttachGuid());
                }
                else {
                    ins.set("imgurl", "");
                }

            }
            Record sumall = jnauditRsItemBaseinfoservice.getSumOfInstance(areacode, ZwfwConstant.CONSTANT_STR_ONE);

            JSONObject dataJson = new JSONObject();
            JSONObject infoJson = new JSONObject();

            infoJson.put("backmonthsum", sumall.getInt("backmonthsum"));
            infoJson.put("curmonthsum", sumall.getInt("curmonthsum"));
            infoJson.put("total", pageData.getRowCount());
            infoJson.put("infolist", instancelist);
            dataJson.put("data", infoJson);
            log.info("=======结束调用getSpInstanceinfoList接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取项目信息列表成功！", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getSpInstanceinfoList接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目信息列表异常：" + e.getMessage(), "");
        }
    }

    //原接口内容，已废弃不使用
    @RequestMapping(value = "/getprojectbyguid", method = RequestMethod.POST)
    public String getprojectbyguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPhaseInfo接口=======");
            //                TaJointGuidanceInfoService service = new TaJointGuidanceInfoService();
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String id = obj.getString("id");

            Record project = jnauditRsItemBaseinfoservice.getAuditProjectbyrowguid(id);
            JSONObject dataJson = new JSONObject();
            JSONObject infoJson = new JSONObject();
            infoJson.put("taskname", project.getStr("taskname"));
            infoJson.put("windowname", project.getStr("windowname"));
            infoJson.put("promise", project.getStr("promise_day"));
            infoJson.put("acceptuserdate", project.getStr("acceptuserdate"));
            infoJson.put("Banjiedate", project.get("banjiedate"));
            String status = project.getStr("status");
            String statusname = jnauditRsItemBaseinfoservice.getstatusnamebyguid(status);
            infoJson.put("status", statusname);
            String time = project.getStr("SPAREMINUTES");
            String result = "";
            if (!time.equals("0")) {

                int i = Integer.parseInt(time);
                if (i > 0) {
                    if (i < 1440) {
                        result = "剩余" + CommonUtil.getSpareTimes(i);

                    }
                    else {
                        result = "剩余" + CommonUtil.getSpareTimes(i);

                    }
                }
                else {
                    i = -i;
                    result = "超时" + CommonUtil.getSpareTimes(i);

                }
            }
            infoJson.put("Banjieenddate", result);
            dataJson.put("infoJson", infoJson);
            log.info("=======结束调用getPhaseInfo接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取项子目阶段信息成功！", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getPhaseInfo接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取子阶段信息异常：" + e.getMessage(), "");
        }
    }

}
