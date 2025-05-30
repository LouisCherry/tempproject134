package com.epoint.qypg;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.qypg.auditqypgtask.api.IAuditQypgTaskService;
import com.epoint.qypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.qypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import com.epoint.qypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.qypg.spglqypgxxb.api.entity.SpglQypgxxb;
import com.epoint.qypg.spglqypgxxbeditr.api.ISpglQypgxxbEditRService;
import com.epoint.qypg.spglqypgxxbeditr.api.entity.SpglQypgxxbEditR;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("qypg")
public class AuditRsItemQypgRest {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 注册用户API
     */
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    @Autowired
    private ISpglQypgxxbService iSpglQypgxxbService;
    @Autowired
    private ISpglQypgsxxxbService iSpglQypgsxxxbService;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private IAuditQypgTaskService iAuditQypgTaskService;
    @Autowired
    private ISpglQypgxxbEditRService iSpglQypgxxbEditRService;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    /**
     * 已关联区域评估信息查询
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getQypgInfoList", method = RequestMethod.POST)
    public String getQypgInfoList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getQypgInfoList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String pagesize = obj.getString("pagesize");
                String currentpage = obj.getString("currentpage");
                String itemguid = obj.getString("itemguid");
                String subappguid = obj.getString("subappguid");
                String qypgdybm = obj.getString("qypgdybm");
                String qypgqymc = obj.getString("qypgqymc");
                SqlConditionUtil sql = new SqlConditionUtil();
                if (StringUtil.isBlank(itemguid) || StringUtil.isBlank(subappguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                // 查询父项目的guid
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid)
                        .getResult();
                if (auditRsItemBaseinfo != null && StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                    AuditRsItemBaseinfo parentItem = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(
                            auditRsItemBaseinfo.getParentid()).getResult();
                    itemguid = parentItem.getRowguid();
                }
                sql.setLeftJoinTable("spgl_qypgxxb_edit_r b", "a.rowguid", "b.qypgguid");
                sql.eq("b.pre_itemguid", itemguid);
                sql.setSelectFields("a.*,b.rowguid rguid,b.subappguid");
                if (StringUtil.isNotBlank(qypgdybm)) {
                    sql.like("b.qypgdybm", qypgdybm);
                }
                if (StringUtil.isNotBlank(qypgqymc)) {
                    sql.like("b.qypgqymc", qypgqymc);
                }
                int firstResult = -1;
                int maxResults = -1;
                if (StringUtil.isNotBlank(pagesize) && StringUtil.isNotBlank(currentpage)) {
                    firstResult = Integer.parseInt(pagesize) * Integer.parseInt(currentpage);
                    maxResults = Integer.parseInt(pagesize);
                }
                sql.eq("subappguid", subappguid);
                PageData<SpglQypgxxb> pageData = iSpglQypgxxbService.getAuditSpDanitemByPage(sql.getMap(), firstResult,
                        maxResults, "b.createdate", "desc").getResult();
                List<SpglQypgxxb> list = pageData.getList();
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                JSONArray qypglist = new JSONArray();
                for (SpglQypgxxb spglQypgxxb : list) {
                    JSONObject qypginfo = new JSONObject();
                    //取消关联
                    qypginfo.put("isself", 1);
                    qypginfo.put("index", ++firstResult);
                    qypginfo.put("subappguid", spglQypgxxb.get("subappguid"));
                    qypginfo.put("row_id", spglQypgxxb.getRow_id());
                    qypginfo.put("rowguid", spglQypgxxb.getRowguid());
                    qypginfo.put("xzqhdm", spglQypgxxb.getXzqhdm());
                    qypginfo.put("qypgdybm", spglQypgxxb.getQypgdybm());
                    qypginfo.put("qypgqymc", spglQypgxxb.getQypgqymc());
                    qypginfo.put("qypgfwms", spglQypgxxb.getQypgfwms());
                    qypginfo.put("qypgmj", spglQypgxxb.getQypgmj());
                    qypginfo.put("qypgfwzbxx", spglQypgxxb.getQypgfwzbxx());
                    qypglist.add(qypginfo);
                }
                dataJson.put("qypglist", qypglist);
                dataJson.put("total", pageData.getRowCount());
                log.info("=======结束调用getQypgInfoList接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估信息查询成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getQypgInfoList接口参数：params【" + params + "】=======");
            log.info("=======getQypgInfoList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估信息查询异常：" + e.getMessage(), "");
        }
    }

    /**
     * 待关联区域评估信息查询
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getNotRelateQypgInfoList", method = RequestMethod.POST)
    public String getNotRelateQypgInfoList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getNotRelateQypgInfoList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String pagesize = obj.getString("pagesize");
                String currentpage = obj.getString("currentpage");
                String itemguid = obj.getString("itemguid");
                String subappguid = obj.getString("subappguid");
                String qypgdybm = obj.getString("qypgdybm");
                String qypgqymc = obj.getString("qypgqymc");
                if (StringUtil.isBlank(itemguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                int firstResult = -1;
                int maxResults = -1;
                if (StringUtil.isNotBlank(pagesize) && StringUtil.isNotBlank(currentpage)) {
                    firstResult = Integer.parseInt(pagesize) * Integer.parseInt(currentpage);
                    maxResults = Integer.parseInt(pagesize);
                }
                PageData<SpglQypgxxb> pageData = iSpglQypgxxbService.getNotAssociationPageData(itemguid, subappguid, firstResult,
                        maxResults, "operatedate", "desc");
                List<SpglQypgxxb> list = pageData.getList();
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                JSONArray qypglist = new JSONArray();
                for (SpglQypgxxb spglQypgxxb : list) {
                    JSONObject qypginfo = new JSONObject();
                    qypginfo.put("row_id", spglQypgxxb.getRow_id());
                    qypginfo.put("rowguid", spglQypgxxb.getRowguid());
                    qypginfo.put("xzqhdm", spglQypgxxb.getXzqhdm());
                    qypginfo.put("qypgdybm", spglQypgxxb.getQypgdybm());
                    qypginfo.put("qypgqymc", spglQypgxxb.getQypgqymc());
                    qypginfo.put("qypgfwms", spglQypgxxb.getQypgfwms());
                    qypginfo.put("qypgmj", spglQypgxxb.getQypgmj());
                    qypginfo.put("qypgfwzbxx", spglQypgxxb.getQypgfwzbxx());
                    qypglist.add(qypginfo);
                }
                dataJson.put("qypglist", qypglist);
                dataJson.put("total", pageData.getRowCount());
                log.info("=======结束调用getNotRelateQypgInfoList接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估信息查询成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getNotRelateQypgInfoList接口参数：params【" + params + "】=======");
            log.info("=======getNotRelateQypgInfoList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估信息查询异常：" + e.getMessage(), "");
        }
    }

    /**
     * 区域评估详细信息新增
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/addQypgInfo", method = RequestMethod.POST)
    public String addQypgInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取addQypgInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String rowguid = obj.getString("rowguid");
                String xzqhdm = obj.getString("xzqhdm");
                String qypgdybm = obj.getString("qypgdybm");
                String qypgqymc = obj.getString("qypgqymc");
                String qypgfwms = obj.getString("qypgfwms");
                String qypgmj = obj.getString("qypgmj");
                String qypgfwzbxx = obj.getString("qypgfwzbxx");
                String itemcode = obj.getString("itemcode");
                String areaname = obj.getString("areaname");
                String preitemguid = obj.getString("preitemguid");
                if (StringUtil.isBlank(xzqhdm) || StringUtil.isBlank(qypgdybm) || StringUtil.isBlank(qypgqymc)
                        || StringUtil.isBlank(qypgfwms) || StringUtil.isBlank(qypgmj) || StringUtil.isBlank(qypgfwzbxx)
                        || StringUtil.isBlank(itemcode) || StringUtil.isBlank(rowguid) || StringUtil.isBlank(
                        preitemguid) || StringUtil.isBlank(areaname)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数为空！", "");
                }
                SpglQypgxxb spglQypgxxb = iSpglQypgxxbService.find(rowguid);
                boolean flag = spglQypgxxb == null;
                if (flag) {
                    spglQypgxxb = new SpglQypgxxb();
                    spglQypgxxb.setRowguid(UUID.randomUUID().toString());
                }
                spglQypgxxb.setItemcode(itemcode);
                spglQypgxxb.setOperatedate(new Date());
                spglQypgxxb.setXzqhdm(xzqhdm);
                spglQypgxxb.setQypgdybm(qypgdybm);
                spglQypgxxb.setQypgqymc(qypgqymc);
                spglQypgxxb.setQypgfwms(qypgfwms);
                spglQypgxxb.setQypgmj(Double.valueOf(qypgmj));
                spglQypgxxb.setQypgfwzbxx(qypgfwzbxx);
                spglQypgxxb.setAreaname(areaname);
                spglQypgxxb.setPreItemGuid(preitemguid);
                if (flag) {
                    iSpglQypgxxbService.insert(spglQypgxxb);
                } else {
                    iSpglQypgxxbService.update(spglQypgxxb);
                }
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                log.info("=======结束调用addQypgInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估详细信息" + (flag ? "新增" : "修改") + "成功！",
                        dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addQypgInfo接口参数：params【" + params + "】=======");
            log.info("=======addQypgInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估详细信息新增异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/addQypgTask", method = RequestMethod.POST)
    public String addQypgTask(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取addQypgTask接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // todo qypgsxbm 和 taskguid存储的都是代码项值
                String rowguid = obj.getString("rowguid");
                String qypgdybm = obj.getString("qypgdybm");
                String qypgguid = obj.getString("qypgguid");
                String taskguid = obj.getString("taskguid");
                String taskname = obj.getString("taskname");// QYPGSXMC
                String qypgcgsxrq = obj.getString("qypgcgsxrq");
                String qypgcgjzrq = obj.getString("qypgcgjzrq");
                String jhspdfs = obj.getString("jhspdfs");
                String cliengguid = obj.getString("cliengguid");
                if (StringUtil.isBlank(taskguid) || StringUtil.isBlank(qypgdybm) || StringUtil.isBlank(taskname)
                        || StringUtil.isBlank(qypgcgsxrq) || StringUtil.isBlank(qypgcgjzrq) || StringUtil.isBlank(
                        jhspdfs) || StringUtil.isBlank(rowguid) || StringUtil.isBlank(qypgguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数为空！", "");
                }
                SpglQypgsxxxb spglQypgsxxxb = iSpglQypgsxxxbService.find(rowguid);
                boolean flag = spglQypgsxxxb == null;
                if (flag) {
                    spglQypgsxxxb = new SpglQypgsxxxb();
                    spglQypgsxxxb.setCliengguid(cliengguid);
                }
                spglQypgsxxxb.setOperatedate(new Date());
                spglQypgsxxxb.setOperateusername("政务大厅用户");
                spglQypgsxxxb.setRowguid(rowguid);
                spglQypgsxxxb.setQypgdybm(qypgdybm);
                spglQypgsxxxb.setQypgguid(qypgguid);
                spglQypgsxxxb.setTaskguid(taskguid);
                spglQypgsxxxb.setQypgsxbm(taskguid);
                spglQypgsxxxb.setQypgsxmc(taskname);
                spglQypgsxxxb.setQypgcgsxrq(EpointDateUtil.convertString2Date(qypgcgsxrq, EpointDateUtil.DATE_FORMAT));
                spglQypgsxxxb.setQypgcgjzrq(EpointDateUtil.convertString2Date(qypgcgjzrq, EpointDateUtil.DATE_FORMAT));
                spglQypgsxxxb.setJhspdfs(jhspdfs);
                if (flag) {
                    iSpglQypgsxxxbService.insert(spglQypgsxxxb);
                } else {
                    iSpglQypgsxxxbService.update(spglQypgsxxxb);
                }
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                log.info("=======结束调用addQypgTask接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估详细信息" + (flag ? "新增" : "修改") + "成功！",
                        dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addQypgTask接口参数：params【" + params + "】=======");
            log.info("=======addQypgTask接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估详细信息新增异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getQypgTaskList", method = RequestMethod.POST)
    public String getQypgTaskList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getQypgTaskList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String pagesize = obj.getString("pagesize");
                String currentpage = obj.getString("currentpage");
                String qypgdybm = obj.getString("qypgdybm");
                String qypgsxbm = obj.getString("qypgsxbm");
                String qypgsxmc = obj.getString("qypgsxmc");
                String qypgguid = obj.getString("qypgguid");
                SqlConditionUtil sql = new SqlConditionUtil();
                if (StringUtil.isBlank(qypgguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                sql.eq("qypgguid", qypgguid);
                if (StringUtil.isNotBlank(qypgsxbm)) {
                    sql.like("qypgsxbm", qypgsxbm);
                }
                if (StringUtil.isNotBlank(qypgsxmc)) {
                    sql.like("qypgsxmc", qypgsxmc);
                }
                int firstResult = -1;
                int maxResults = -1;
                if (StringUtil.isNotBlank(pagesize) && StringUtil.isNotBlank(currentpage)) {
                    firstResult = Integer.parseInt(pagesize) * Integer.parseInt(currentpage);
                    maxResults = Integer.parseInt(pagesize);
                }
                sql.setLeftJoinTable("audit_qypg_task b", "a.taskguid", "b.rowguid");
                sql.setSelectFields(
                        "a.rowguid,a.qypgcgsxrq,a.qypgcgjzrq,a.jhspdfs,a.cliengguid,b.taskname qypgsxmc,b.taskcode qypgsxbm");
                PageData<SpglQypgsxxxb> pageData = iSpglQypgsxxxbService.getAuditSpDanitemByPage(sql.getMap(),
                        firstResult, maxResults, "a.operatedate", "desc").getResult();
                List<SpglQypgsxxxb> list = pageData.getList();
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                JSONArray tasklist = new JSONArray();
                for (SpglQypgsxxxb spglQypgsxxxb : list) {
                    JSONObject taskinfo = new JSONObject();
                    taskinfo.put("index", ++firstResult);
                    taskinfo.put("row_id", spglQypgsxxxb.getRow_id());
                    taskinfo.put("rowguid", spglQypgsxxxb.getRowguid());
                    taskinfo.put("qypgdybm", spglQypgsxxxb.getQypgdybm());
                    taskinfo.put("qypgsxbm", spglQypgsxxxb.getQypgsxbm());
                    taskinfo.put("qypgsxmc", spglQypgsxxxb.getQypgsxmc());
                    taskinfo.put("dybzspsxbm", spglQypgsxxxb.getDybzspsxbm());
                    taskinfo.put("qypgcgsxrq", spglQypgsxxxb.getQypgcgsxrq());
                    taskinfo.put("qypgcgjzrq", spglQypgsxxxb.getQypgcgjzrq());
                    taskinfo.put("jhspdfs", spglQypgsxxxb.getJhspdfs());
                    String cliengguid = spglQypgsxxxb.getCliengguid();
                    ArrayList<JSONObject> attachList = new ArrayList<>();
                    if (StringUtil.isNotBlank(cliengguid)) {
                        List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengguid);
                        // 只能上传1个，所以这里只取一个
                        if (attachInfoList != null && !attachInfoList.isEmpty()) {
                            FrameAttachInfo info = attachInfoList.get(0);
                            JSONObject object = new JSONObject();
                            object.put("attachguid", info.getAttachGuid());
                            object.put("attachname", info.getAttachFileName());
                            object.put("attachurl", iAttachService.getAttachDownPath(info));
                            attachList.add(object);
                        }
                    }
                    taskinfo.put("attachList", attachList);
                    List<JSONObject> jhspdfsList = getSelectItemList("国标_应用区域评估成果简化审批方式",
                            spglQypgsxxxb.getJhspdfs());
                    taskinfo.put("jhspdfs", jhspdfsList);
                    tasklist.add(taskinfo);
                }
                dataJson.put("tasklist", tasklist);
                dataJson.put("total", pageData.getRowCount());
                log.info("=======结束调用getQypgTaskList接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估事项信息查询成功！",
                        JsonUtil.objectToJson(dataJson, EpointDateUtil.DATE_FORMAT));
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getQypgTaskList接口参数：params【" + params + "】=======");
            log.info("=======getQypgTaskList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估事项信息查询异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getQypgTaskInfo", method = RequestMethod.POST)
    public String getQypgTaskInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getQypgTaskInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String rowguid = obj.getString("rowguid");
                if (StringUtil.isBlank(rowguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                SpglQypgsxxxb spglQypgsxxxb = iSpglQypgsxxxbService.find(rowguid);
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (spglQypgsxxxb == null) {
                    return JsonUtils.zwdtRestReturn("0", "未查询到相关信息！", dataJson.toString());
                }
                JSONObject taskinfo = new JSONObject();
                taskinfo.put("taskguid", spglQypgsxxxb.getTaskguid());
                taskinfo.put("rowguid", spglQypgsxxxb.getRowguid());
                taskinfo.put("qypgdybm", spglQypgsxxxb.getQypgdybm());
                taskinfo.put("qypgsxbm", spglQypgsxxxb.getQypgsxbm());
                taskinfo.put("taskname", spglQypgsxxxb.getQypgsxmc());
                taskinfo.put("qypgcgsxrq", spglQypgsxxxb.getQypgcgsxrq());
                taskinfo.put("qypgcgjzrq", spglQypgsxxxb.getQypgcgjzrq());
                String cliengguid = spglQypgsxxxb.getCliengguid();
                taskinfo.put("cliengguid", cliengguid);
                ArrayList<JSONObject> attachList = new ArrayList<>();
                if (StringUtil.isNotBlank(cliengguid)) {
                    List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengguid);
                    for (FrameAttachInfo info : attachInfoList) {
                        JSONObject object = new JSONObject();
                        object.put("attachguid", info.getAttachGuid());
                        object.put("attachname", info.getAttachFileName());
                        object.put("attachurl", iAttachService.getAttachDownPath(info));
                        attachList.add(object);
                    }
                }
                taskinfo.put("attachList", attachList);
                List<JSONObject> jhspdfsList = getSelectItemList("国标_应用区域评估成果简化审批方式",
                        spglQypgsxxxb.getJhspdfs());
                taskinfo.put("jhspdfs", jhspdfsList);
                dataJson.put("taskinfo", taskinfo);
                log.info("=======结束调用getQypgTaskInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估事项信息查询成功！",
                        JsonUtil.objectToJson(dataJson, EpointDateUtil.DATE_FORMAT));
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getQypgTaskInfo接口参数：params【" + params + "】=======");
            log.info("=======getQypgTaskInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估事项信息查询异常：" + e.getMessage(), "");
        }
    }

    /**
     * 区域评估详细信息查询
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getQypgInfo", method = RequestMethod.POST)
    public String getQypgInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getQypgInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String rowguid = obj.getString("rowguid");
                if (StringUtil.isBlank(rowguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                SpglQypgxxb spglQypgxxb = iSpglQypgxxbService.find(rowguid);
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (spglQypgxxb == null) {
                    return JsonUtils.zwdtRestReturn("0", "未查询到相关信息！", dataJson.toString());
                }
                JSONObject qypginfo = new JSONObject();
                qypginfo.put("row_id", spglQypgxxb.getRow_id());
                qypginfo.put("rowguid", spglQypgxxb.getRowguid());
                qypginfo.put("xzqhdm", spglQypgxxb.getXzqhdm());
                qypginfo.put("qypgdybm", spglQypgxxb.getQypgdybm());
                qypginfo.put("qypgqymc", spglQypgxxb.getQypgqymc());
                qypginfo.put("qypgfwms", spglQypgxxb.getQypgfwms());
                qypginfo.put("qypgmj", spglQypgxxb.getQypgmj());
                qypginfo.put("qypgfwzbxx", spglQypgxxb.getQypgfwzbxx());
                AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAreaByAreacode(spglQypgxxb.getXzqhdm()).getResult();
                if (auditOrgaArea != null) {
                    qypginfo.put("xzqhdm", auditOrgaArea.getXiaquname());
                }
                dataJson.put("qypginfo", qypginfo);
                log.info("=======结束调用getQypgInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估详细信息查询成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getQypgInfo接口参数：params【" + params + "】=======");
            log.info("=======getQypgInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估详细信息查询异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取全部区域评估事项接口
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getAllQypgTaskList", method = RequestMethod.POST)
    public String getAllQypgTaskList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getAllQypgTaskList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // JSONObject obj = (JSONObject) json.get("params");
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();

                List<JSONObject> tasklist = iAuditQypgTaskService.findAllList().stream().map(a -> {
                    JSONObject objJson = new JSONObject();
                    objJson.put("taskguid", a.getTaskcode());
                    objJson.put("item_id", a.getTaskcode());
                    objJson.put("taskname", a.getTaskname());
                    return objJson;
                }).collect(Collectors.toList());

                dataJson.put("tasklist", tasklist);
                log.info("=======结束调用getAllQypgTaskList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取全部区域评估事项成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAllQypgTaskList接口参数：params【" + params + "】=======");
            log.info("=======getAllQypgTaskList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估详细信息查询异常：" + e.getMessage(), "");
        }
    }

    /**
     * 删除
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteQypgInfo", method = RequestMethod.POST)
    public String deleteQypgInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取deleteQypgInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String rowguid = obj.getString("rowguid");
                if (StringUtil.isBlank(rowguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                String[] rowguids = rowguid.split(";");
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                for (String s : rowguids) {
                    if (StringUtil.isNotBlank(s)) {
                        SpglQypgxxb spglQypgxxb = iSpglQypgxxbService.find(s);
                        if (spglQypgxxb == null) {
                            return JsonUtils.zwdtRestReturn("0", "未查询到相关信息！", dataJson.toString());
                        }
                        iSpglQypgxxbService.deleteByGuid(s);
                    }
                }
                log.info("=======结束调用deleteQypgInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估详细信息删除成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======deleteQypgInfo接口参数：params【" + params + "】=======");
            log.info("=======deleteQypgInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估详细信息删除异常：" + e.getMessage(), "");
        }
    }

    /**
     * 取消关联
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/cancelQypgInfo", method = RequestMethod.POST)
    public String cancelQypgInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取cancelQypgInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String itemguid = obj.getString("itemguid");
                String qypgguid = obj.getString("qypgguid");
                if (StringUtil.isBlank(qypgguid) || StringUtil.isBlank(itemguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("pre_itemguid", itemguid);
                sql.eq("qypgguid", qypgguid);
                SpglQypgxxbEditR spglQypgxxbEditR = iSpglQypgxxbEditRService.getSpglQypgxxbEditR(sql.getMap());
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (spglQypgxxbEditR == null) {
                    return JsonUtils.zwdtRestReturn("0", "未查询到相关信息！", dataJson.toString());
                }
                iSpglQypgxxbEditRService.deleteByGuid(spglQypgxxbEditR.getRowguid());
                log.info("=======结束调用cancelQypgInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "取消关联区域评估成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======cancelQypgInfo接口参数：params【" + params + "】=======");
            log.info("=======cancelQypgInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "取消关联区域评估异常：" + e.getMessage(), "");
        }
    }

    /**
     * 关联
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/relateQypgInfo", method = RequestMethod.POST)
    public String relateQypgInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取relateQypgInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String itemguid = obj.getString("itemguid");
                String subappguid = obj.getString("subappguid");
                JSONArray qypgguidArray = obj.getJSONArray("qypgguid");
                if (StringUtil.isBlank(itemguid) || StringUtil.isBlank(subappguid) || qypgguidArray == null) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                Date date = new Date();
                for (int i = 0; i < qypgguidArray.size(); i++) {
                    String qypgguid = qypgguidArray.getString(i);
                    SpglQypgxxbEditR spglQypgxxbEditR = new SpglQypgxxbEditR();
                    spglQypgxxbEditR.setCreatedate(date);
                    spglQypgxxbEditR.setQypgguid(qypgguid);
                    spglQypgxxbEditR.setSubappguid(subappguid);
                    spglQypgxxbEditR.setPre_itemguid(itemguid);
                    spglQypgxxbEditR.setRowguid(UUID.randomUUID().toString());
                    iSpglQypgxxbEditRService.insert(spglQypgxxbEditR);
                }
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                log.info("=======结束调用relateQypgInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "关联区域评估成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======relateQypgInfo接口参数：params【" + params + "】=======");
            log.info("=======relateQypgInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "关联区域评估异常：" + e.getMessage(), "");
        }
    }

    /**
     * 删除
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteQypgTask", method = RequestMethod.POST)
    public String deleteQypgTask(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取deleteQypgTask接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String rowguid = obj.getString("rowguid");
                if (StringUtil.isBlank(rowguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                String[] rowguids = rowguid.split(";");
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                for (String s : rowguids) {
                    SpglQypgsxxxb spglQypgsxxxb = iSpglQypgsxxxbService.find(s);
                    if (spglQypgsxxxb == null) {
                        return JsonUtils.zwdtRestReturn("0", "未查询到相关信息！", dataJson.toString());
                    }
                    iSpglQypgsxxxbService.deleteByGuid(s);
                }
                log.info("=======结束调用deleteQypgTask接口=======");
                return JsonUtils.zwdtRestReturn("1", "区域评估事项信息删除成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======deleteQypgTask接口参数：params【" + params + "】=======");
            log.info("=======deleteQypgTask接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "区域评估事项信息删除异常：" + e.getMessage(), "");
        }
    }

    /**
     * * 获取代码项接口
     *
     * @param params 代码项名称
     * @return
     */
    @RequestMapping(value = "/getItemList", method = RequestMethod.POST)
    public String getItemList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getItemList接口=======");
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取代码项名称
                String codeItemName = obj.getString("codeitemname");
                // 2、获取代码子项列表
                List<CodeItems> codeItemList = iCodeItemsService.listCodeItemsByCodeName(codeItemName);
                List<JSONObject> itemJsonList = new ArrayList<JSONObject>();
                if (codeItemList != null && !codeItemList.isEmpty()) {
                    for (CodeItems codeItems : codeItemList) {
                        JSONObject itemJson = new JSONObject();
                        itemJson.put("itemtext", codeItems.getItemText());
                        itemJson.put("itemvalue", codeItems.getItemValue());
                        itemJsonList.add(itemJson);
                    }
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("itemlist", itemJsonList);
                log.info("=======结束调用getItemList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取代码项列表成功", dataJson.toString());
            } else {
                log.info("=======结束调用getItemList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======调用getItemList接口异常=======");
            return JsonUtils.zwdtRestReturn("0", "获取代码项列表出现异常", "");
        }
    }

    /**
     * 获取代码项json，默认值为第一个
     *
     * @param codeName
     * @param filedValue
     * @return
     */
    public List<JSONObject> getSelectItemList(String codeName, String filedValue) {
        List<JSONObject> resultJsonList = new ArrayList<>();

        boolean isSelected = false;
        // 获取代码项目
        List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName(codeName);
        for (CodeItems codeItems : itemtypes) {
            JSONObject objJson = new JSONObject();
            objJson.put("itemvalue", codeItems.getItemValue());
            objJson.put("itemtext", codeItems.getItemText());
            // 判断是否有默认选中的
            if (codeItems.getItemValue().equals(filedValue)) {
                objJson.put("isselected", 1);
                isSelected = true;

            }
            resultJsonList.add(objJson);
        }
        if (resultJsonList != null && !resultJsonList.isEmpty() && !isSelected) {
            resultJsonList.get(0).put("isselected", 0);
        }
        return resultJsonList;
    }
}
