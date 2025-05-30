package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.danti.bizcommon.DantiBizcommon;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.auditsp.dwgcinfo.api.IDwgcInfoService;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.basic.auditsp.dwgcjlneed.api.IDwgcJlneedService;
import com.epoint.basic.auditsp.dwgcjlneed.entity.DwgcJlneed;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.xmz.auditspitaskcorp.api.IAuditSpITaskCorpService;
import com.epoint.xmz.auditspitaskcorp.api.entity.AuditSpITaskCorp;
import com.epoint.xmz.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.dantiinfov3.api.entity.DantiInfoV3;
import com.epoint.xmz.participatsinfo.api.GxhIParticipantsInfoService;
import com.epoint.zwdt.zwdtrest.project.api.IJnDantiinfoService;

/**
 * 并联审批相关接口
 *
 * @version [F9.3, 2019年7月18日]
 * @作者 徐李
 */
@RestController
@RequestMapping("/gxhZwdtItemUnit")
public class GxhAuditOnlineItemUnitController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    @Autowired
    private IDantiInfoV3Service iDantiInfoV3Service;

    @Autowired
    private IDantiInfoService iDantiInfoService;

    @Autowired
    private IDantiSubRelationService iDantiSubRelationService;

    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 单位工程API
     */
    @Autowired
    private IDwgcInfoService iDwgcInfoService;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IDwgcJlneedService iDwgcJlneedService;

    /**
     * 参建单位API
     */
    @Autowired
    private GxhIParticipantsInfoService gxhiParticipantsInfo;

    @Autowired
    private IParticipantsInfoService iParticipantsInfo;

    @Autowired
    private IAuditSpITaskCorpService iAuditSpITaskCorpService;

    @Autowired
    private IAuditTask auditTask;

    /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
    @Autowired
    private IJnDantiinfoService iJnDantiinfoService;
    /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/

    /* 事项关联单位3.0新增接口 */

    /**
     * 校验事项是否关联单位接口
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/checkTaskCorp", method = RequestMethod.POST)
    public String checkTaskCorp(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取checkTaskCorp接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String taskguids = obj.getString("taskguids");
                String subappguid = obj.getString("subappguid");
                if (StringUtil.isBlank(taskguids) || StringUtil.isBlank(subappguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                String[] taskguidarray = taskguids.split(";");
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("subappguid", subappguid);
                List<AuditSpITaskCorp> corpList = iAuditSpITaskCorpService.getAuditSpITaskCorpList(sql.getMap());
                List<String> taskGuidList = corpList.stream().map(AuditSpITaskCorp::getTaskguid)
                        .collect(Collectors.toList());
                List<JSONObject> tasklist = new ArrayList<>();
                for (String taskguid : taskguidarray) {
                    if (!taskGuidList.contains(taskguid)) {
                        JSONObject task = new JSONObject();
                        task.put("taskguid", taskguid);
                        AuditTask auditTask1 = auditTask.getAuditTaskByGuid(taskguid, false).getResult();
                        String taskname = auditTask1.getTaskname();
                        task.put("taskname", taskname);
                        tasklist.add(task);
                    }
                }
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("tasklist", tasklist);
                log.info("=======结束调用checkTaskCorp接口=======");
                return JsonUtils.zwdtRestReturn("1", "校验事项是否关联单位成功！",
                        JsonUtil.objectToJson(dataJson, EpointDateUtil.DATE_FORMAT));
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkTaskCorp接口参数：params【" + params + "】=======");
            log.info("=======checkTaskCorp接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "校验事项是否关联单位异常：" + e.getMessage(), "");
        }
    }

    /**
     * 查询事项单位列表
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTaskCorpList", method = RequestMethod.POST)
    public String getTaskCorpList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getTaskCorpList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String taskguid = obj.getString("taskguid");
                String subappguid = obj.getString("subappguid");
                String corpname = obj.getString("corpname");
                String corpcode = obj.getString("corpcode");
                String legal = obj.getString("legal");
                String keyword = obj.getString("keyword");
                String pagesize = obj.getString("pagesize");
                String currentpage = obj.getString("currentpage");
                if (StringUtil.isBlank(taskguid) || StringUtil.isBlank(subappguid) || StringUtil.isBlank(pagesize)
                        || StringUtil.isBlank(currentpage)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                int firstResult = -1;
                int maxResults = -1;
                if (StringUtil.isNotBlank(pagesize) && StringUtil.isNotBlank(currentpage)) {
                    firstResult = Integer.parseInt(pagesize) * Integer.parseInt(currentpage);
                    maxResults = Integer.parseInt(pagesize);
                }
                PageData<ParticipantsInfo> pageData = gxhiParticipantsInfo.getListByTaskguidAndSubappguid(firstResult,
                        maxResults, taskguid, subappguid, corpname, corpcode, legal, keyword);
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("total", pageData.getRowCount());
                JSONArray danweilist = new JSONArray();
                for (ParticipantsInfo participantsInfo : pageData.getList()) {
                    JSONObject o = new JSONObject();
                    o.put("corpguid", participantsInfo.getRowguid());
                    o.put("corpname", participantsInfo.getCorpname());
                    o.put("corptype", participantsInfo.getCorptype());
                    o.put("corptype_cn",
                            iCodeItemsService.getItemTextByCodeName("关联单位类型", participantsInfo.getCorptype()));
                    o.put("corpcode", participantsInfo.getCorpcode());
                    o.put("cert", participantsInfo.getCert());
                    o.put("cert_cn", iCodeItemsService.getItemTextByCodeName("国标_资质等级", participantsInfo.getCert()));
                    o.put("legal", participantsInfo.getLegal());
                    o.put("status", participantsInfo.getStr("status"));
                    o.put("corpguid", participantsInfo.getRowguid());
                    o.put("is_relateadd", participantsInfo.get("is_relateadd"));
                    danweilist.add(o);
                }
                dataJson.put("danweilist", danweilist);
                log.info("=======结束调用getTaskCorpList接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询事项单位列表成功！",
                        JsonUtil.objectToJson(dataJson, EpointDateUtil.DATE_FORMAT));
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskCorpList接口参数：params【" + params + "】=======");
            log.info("=======getTaskCorpList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询事项单位列表异常：" + e.getMessage(), "");
        }
    }

    /**
     * 9.23、事项取消关联单位接口
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/cancelTaskCorp", method = RequestMethod.POST)
    public String cancelTaskCorp(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取cancelTaskCorp接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String taskguid = obj.getString("taskguid");
                String subappguid = obj.getString("subappguid");
                JSONArray dwguids = obj.getJSONArray("dwguids");
                if (StringUtil.isBlank(taskguid) || StringUtil.isBlank(subappguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("taskguid", taskguid);
                sql.eq("subappguid", subappguid);
                if (dwguids != null && !dwguids.isEmpty()) {
                    sql.in("corpguid", StringUtil.joinSql(dwguids));
                }
                List<AuditSpITaskCorp> auditSpITaskCorpList = iAuditSpITaskCorpService
                        .getAuditSpITaskCorpList(sql.getMap());
                for (AuditSpITaskCorp auditSpITaskCorp : auditSpITaskCorpList) {
                    iAuditSpITaskCorpService.deleteByGuid(auditSpITaskCorp.getRowguid());
                }

                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                log.info("=======结束调用cancelTaskCorp接口=======");
                return JsonUtils.zwdtRestReturn("1", "事项取消关联单位成功！",
                        JsonUtil.objectToJson(dataJson, EpointDateUtil.DATE_FORMAT));
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======cancelTaskCorp接口参数：params【" + params + "】=======");
            log.info("=======cancelTaskCorp接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项取消关联单位异常：" + e.getMessage(), "");
        }
    }

    /**
     * 事项关联单位接口
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/confirmTaskCorp", method = RequestMethod.POST)
    public String confirmTaskCorp(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取confirmTaskCorp接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String taskguid = obj.getString("taskguid");
                String subappguid = obj.getString("subappguid");
                JSONArray dwguids = obj.getJSONArray("dwguids");
                if (StringUtil.isBlank(taskguid) || StringUtil.isBlank(subappguid)
                        || (StringUtil.isNotBlank(dwguids) && !dwguids.isEmpty())) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.in("rowguid", StringUtil.joinSql(dwguids));
                List<ParticipantsInfo> list = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                Date date = new Date();
                for (ParticipantsInfo participantsInfo : list) {
                    AuditSpITaskCorp auditSpITaskCorp = new AuditSpITaskCorp();
                    auditSpITaskCorp.setCorpguid(participantsInfo.getRowguid());
                    auditSpITaskCorp.setRowguid(UUID.randomUUID().toString());
                    auditSpITaskCorp.setCreatedate(date);
                    auditSpITaskCorp.setSubappguid(subappguid);
                    auditSpITaskCorp.setTaskguid(taskguid);
                    auditSpITaskCorp.setCorptyppe(participantsInfo.getCorptype());
                    iAuditSpITaskCorpService.insert(auditSpITaskCorp);
                }
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                log.info("=======结束调用confirmTaskCorp接口=======");
                return JsonUtils.zwdtRestReturn("1", "事项关联单位成功！",
                        JsonUtil.objectToJson(dataJson, EpointDateUtil.DATE_FORMAT));
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======confirmTaskCorp接口参数：params【" + params + "】=======");
            log.info("=======confirmTaskCorp接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项关联单位异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/delParticipantInfo", method = RequestMethod.POST)
    public String delParticipantInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取delParticipantInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String token = json.getString("token");
            String dwguid = obj.getString("dwguid");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                if (StringUtil.isNotBlank(dwguid)) {
                    iParticipantsInfo.deleteByGuid(dwguid);
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("corpguid", dwguid);
                    List<AuditSpITaskCorp> auditSpITaskCorpList = iAuditSpITaskCorpService
                            .getAuditSpITaskCorpList(sql.getMap());
                    for (AuditSpITaskCorp auditSpITaskCorp : auditSpITaskCorpList) {
                        iAuditSpITaskCorpService.deleteByGuid(auditSpITaskCorp.getRowguid());
                    }

                    /*
                     * SqlConditionUtil sqlc = new SqlConditionUtil();
                     * sqlc.eq("danweiguid", dwguid); // 同时删除相关单位人员
                     * List<ParticipantsUserInfo> userInfoList =
                     * iParticipantsUserInfoService.getListByCondition(
                     * sqlc.getMap()); if (userInfoList != null &&
                     * !userInfoList.isEmpty()) { for (ParticipantsUserInfo
                     * userInfo : userInfoList) {
                     * iParticipantsUserInfoService.deleteByGuid(userInfo.
                     * getRowguid()); } }
                     */
                }
                return JsonUtils.zwdtRestReturn("1", "删除成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }
    /* 事项关联单位3.0新增接口结束 */

    /**
     * 获取单体列表接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiList", method = RequestMethod.POST)
    public String getDantiList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String subappguid = obj.getString("subappguid");
                if (StringUtil.isBlank(subappguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数subappguid不能为空", "");
                }
                String itemguid = obj.getString("itemguid");
                if (StringUtil.isBlank(itemguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数itemguid不能为空", "");
                }
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    /* 3.0修改逻辑 */
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("subappguid", subappguid);
                    List<DantiSubRelation> dantiSubs = iDantiSubRelationService
                            .getDantiSubRelationListByCondition(sql.getMap());
                    List<JSONObject> materialList = new ArrayList<JSONObject>();
                    // V3单体数量
                    int v3ListCount = 0;
                    // V3总建筑面积
                    Double v3Zjzmjz = 0.0;
                    // V3最高高度
                    Double v3Zgjzgd = 0.0;
                    // 非V3单体数量
                    int listCount = 0;
                    // 非V3总建筑面积
                    Double zjzmjz = 0.0;
                    // 非V3最高高度
                    Double zgjzgd = 0.0;
                    for (DantiSubRelation dantiSubRelation : dantiSubs) {
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(dantiSubRelation.getStr("is_v3"))) {
                            DantiInfoV3 dantiInfo = iDantiInfoV3Service.find(dantiSubRelation.getDantiguid());
                            v3ListCount++;
                            if (dantiInfo.getJzmj() != null) {
                                v3Zjzmjz += dantiInfo.getJzmj();
                            }
                            if (dantiInfo.getJzgcgd() != null && dantiInfo.getJzgcgd() > v3Zgjzgd) {
                                v3Zgjzgd = dantiInfo.getJzgcgd();
                            }
                        }
                        else {
                            DantiInfo dantiInfo = iDantiInfoService.find(dantiSubRelation.getDantiguid());
                            listCount++;
                            if (dantiInfo.getZjzmj() != null) {
                                zjzmjz += dantiInfo.getZjzmj();
                            }
                            if (dantiInfo.getJzgd() != null && dantiInfo.getJzgd() > zgjzgd) {
                                zgjzgd = dantiInfo.getJzgd();
                            }
                        }
                    }
                    for (DantiSubRelation dantiSubRelation : dantiSubs) {
                        JSONObject jsonMaterial = new JSONObject();
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(dantiSubRelation.getStr("is_v3"))) {
                            DantiInfoV3 dantiInfo = iDantiInfoV3Service.find(dantiSubRelation.getDantiguid());
                            SqlConditionUtil sqlRelation = new SqlConditionUtil();
                            sqlRelation.eq("subappguid", subappguid);
                            sqlRelation.eq("dantiguid", dantiInfo.getRowguid());
                            List<DantiSubRelation> subRelations = iDantiSubRelationService
                                    .getDantiSubRelationListByCondition(sqlRelation.getMap());
                            jsonMaterial.put("isv3",
                                    ValidateUtil.isNotBlankCollection(subRelations)
                                            ? subRelations.get(0).getStr("is_v3")
                                            : ZwfwConstant.CONSTANT_STR_ZERO);
                            jsonMaterial.put("rowguid", dantiInfo.getRowguid());
                            jsonMaterial.put("dantiguid", dantiInfo.getRowguid());
                            jsonMaterial.put("dantiname", dantiInfo.getDtmc());
                            jsonMaterial.put("fllb", "");
                            jsonMaterial.put("gclb", dantiInfo.getGcyt());
                            jsonMaterial.put("gclbmc",
                                    iCodeItemsService.getItemTextByCodeName("国标_工程类别", dantiInfo.getGcyt()));
                            jsonMaterial.put("gcxz", "");
                            jsonMaterial.put("jzdts", v3ListCount);
                            jsonMaterial.put("zjzmjz", v3Zjzmjz);
                            jsonMaterial.put("zgjzgd", v3Zgjzgd);
                            jsonMaterial.put("zzmj", "");
                            jsonMaterial.put("zjzmj", dantiInfo.getJzmj());
                            jsonMaterial.put("gjmj", "");
                            jsonMaterial.put("dxgjmj", "");
                            jsonMaterial.put("dxckmj", "");
                            jsonMaterial.put("dishangmianji", dantiInfo.getDsjzmj());
                            jsonMaterial.put("dixiamianji", dantiInfo.getDxjzmj());
                            jsonMaterial.put("jzgd", dantiInfo.getJzgcgd());
                            jsonMaterial.put("dscs", dantiInfo.getDscs());
                            jsonMaterial.put("dxcs", dantiInfo.getDxcs());
                            jsonMaterial.put("price", dantiInfo.getDtgczzj());
                            jsonMaterial.put("gcguid", dantiInfo.getGongchengguid());
                            jsonMaterial.put("rguid", dantiSubRelation.getRowguid());
                            DwgcInfo dwgcInfo = new DwgcInfo();
                            if (StringUtil.isNotBlank(dantiInfo.getGongchengguid())) {
                                dwgcInfo = iDwgcInfoService.find(dantiInfo.getGongchengguid());
                                jsonMaterial.put("gongchengname", dwgcInfo.getGongchengname());
                            }
                        }
                        else {
                            DantiInfo dantiInfo = iDantiInfoService.find(dantiSubRelation.getDantiguid());
                            SqlConditionUtil sqlRelation = new SqlConditionUtil();
                            sqlRelation.eq("subappguid", subappguid);
                            sqlRelation.eq("dantiguid", dantiInfo.getRowguid());
                            List<DantiSubRelation> subRelations = iDantiSubRelationService
                                    .getDantiSubRelationListByCondition(sqlRelation.getMap());
                            jsonMaterial.put("isv3",
                                    ValidateUtil.isNotBlankCollection(subRelations)
                                            ? subRelations.get(0).getStr("is_v3")
                                            : ZwfwConstant.CONSTANT_STR_ZERO);
                            jsonMaterial.put("rowguid", dantiInfo.getRowguid());
                            jsonMaterial.put("dantiguid", dantiInfo.getRowguid());
                            jsonMaterial.put("dantiname", dantiInfo.getDantiname());
                            jsonMaterial.put("fllb", dantiInfo.getFllb());
                            jsonMaterial.put("gclb", dantiInfo.getGclb());
                            jsonMaterial.put("gclbmc",
                                    iCodeItemsService.getItemTextByCodeName("工程类别", dantiInfo.getGclb()));
                            jsonMaterial.put("gcxz", dantiInfo.getGcxz());
                            jsonMaterial.put("jzdts", listCount);
                            jsonMaterial.put("zjzmjz", zjzmjz);
                            jsonMaterial.put("zgjzgd", zgjzgd);
                            jsonMaterial.put("zzmj", dantiInfo.getZzmj());
                            jsonMaterial.put("zjzmj", dantiInfo.getZjzmj());
                            jsonMaterial.put("gjmj", dantiInfo.getGjmj());
                            jsonMaterial.put("dxgjmj", dantiInfo.getDxgjmj());
                            jsonMaterial.put("dxckmj", dantiInfo.getDxckmj());
                            jsonMaterial.put("dishangmianji", dantiInfo.getDishangmianji());
                            jsonMaterial.put("dixiamianji", dantiInfo.getDixiamianji());
                            jsonMaterial.put("jzgd", dantiInfo.getJzgd());
                            jsonMaterial.put("dscs", dantiInfo.getDscs());
                            jsonMaterial.put("dxcs", dantiInfo.getDxcs());
                            jsonMaterial.put("price", dantiInfo.getPrice());
                            jsonMaterial.put("gcguid", dantiInfo.getGongchengguid());
                            jsonMaterial.put("rguid", dantiInfo.getStr("rguid"));
                            DwgcInfo dwgcInfo = new DwgcInfo();
                            if (StringUtil.isNotBlank(dantiInfo.getGongchengguid())) {
                                dwgcInfo = iDwgcInfoService.find(dantiInfo.getGongchengguid());
                                jsonMaterial.put("gongchengname", dwgcInfo.getGongchengname());
                            }
                        }
                        /* 3.0结束逻辑 */
                        materialList.add(jsonMaterial);
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("dantilist", materialList);
                    return JsonUtils.zwdtRestReturn("1", " 单体列表查询成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "单体列表查询失败", "");
        }
    }

    /**
     * 获取单体单位列表接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiInfoList", method = RequestMethod.POST)
    public String getDantiInfoList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、子申报标识
                String subappguid = obj.getString("subappguid");
                if (StringUtil.isBlank(subappguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数subappguid不能为空", "");
                }
                // 1.2、项目标识
                String itemguid = obj.getString("itemguid");
                if (StringUtil.isBlank(itemguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数itemguid不能为空", "");
                }
                // 1.3、工程标识
                String gongchengguid = obj.getString("gongchengguid");
                // 1.4、页码
                String pageSize = obj.getString("pagesize");
                if (StringUtil.isBlank(pageSize)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数pageSize不能为空", "");
                }
                // 1.4、当前页
                String currentPage = obj.getString("currentpage");
                if (StringUtil.isBlank(currentPage)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数currentpage不能为空", "");
                }
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    SqlConditionUtil sqlRelation = new SqlConditionUtil();
                    sqlRelation.eq("subappguid", subappguid);
                    sqlRelation.setSelectFields("dantiguid");
                    List<DantiSubRelation> dantiSubRelationList = iDantiSubRelationService
                            .getDantiSubRelationListByCondition(sqlRelation.getMap());
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("itemguid", itemguid);
                    if (dantiSubRelationList != null && !dantiSubRelationList.isEmpty()) {
                        String dantiguidinstr = StringUtil.joinSql(dantiSubRelationList.stream()
                                .map(DantiSubRelation::getDantiguid).collect(Collectors.toList()));
                        sql.notin("rowguid", dantiguidinstr);
                    }
                    if (StringUtil.isNotBlank(gongchengguid)) {
                        sql.eq("gongchengguid", gongchengguid);
                    }
                    List<JSONObject> dantiList = new ArrayList<>(16);
                    /* 3.0修改逻辑 */
                    PageData<DantiInfoV3> pageData = iDantiInfoV3Service.getDantiInfoPageData(sql.getMap(),
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                            "operatedate", "desc");
                    for (DantiInfoV3 dantiInfo : pageData.getList()) {
                        JSONObject objReturn = new JSONObject();
                        objReturn.put("dantiguid", dantiInfo.getRowguid());
                        objReturn.put("dantiname", dantiInfo.getDtmc());
                        objReturn.put("gclbmc",
                                StringUtil.isNotBlank(dantiInfo.getGcyt())
                                        ? iCodeItemsService.getItemTextByCodeName("国标_工程类别", dantiInfo.getGcyt())
                                        : "");
                        objReturn.put("gclb", dantiInfo.getGcyt());
                        objReturn.put("price", dantiInfo.getDtgczzj());
                        sqlRelation.clear();
                        sqlRelation.eq("subappguid", subappguid);
                        sqlRelation.eq("dantiguid", dantiInfo.getRowguid());
                        List<DantiSubRelation> subRelations = iDantiSubRelationService
                                .getDantiSubRelationListByCondition(sqlRelation.getMap());
                        objReturn.put("isv3",
                                ValidateUtil.isNotBlankCollection(subRelations) ? subRelations.get(0).getStr("is_v3")
                                        : ZwfwConstant.CONSTANT_STR_ZERO);

                        objReturn.put("is_enable",
                                Integer.valueOf(
                                        StringUtil.isBlank(dantiInfo.getIs_enable()) ? ZwfwConstant.CONSTANT_STR_ZERO
                                                : dantiInfo.getIs_enable()));
                        dantiList.add(objReturn);
                    }
                    /* 3.0结束逻辑 */
                    JSONObject data = new JSONObject();
                    data.put("dantilist", dantiList);
                    data.put("total", pageData.getRowCount());
                    return JsonUtils.zwdtRestReturn("1", "查询单体单位成功", data.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "查询单体单位失败", "");
        }
    }

    /* 3.0新增逻辑 */

    /**
     * 查询单体单位详情接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiInfoDetail", method = RequestMethod.POST)
    public String getDantiInfoDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、单体唯一标识
                String dantiguid = obj.getString("dantiguid");
                String itemguid = obj.getString("itemguid");
                if (StringUtil.isBlank(itemguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数itemguid不能为空！", "");
                }
                String gcfl = obj.getString("gcfl");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    JSONObject dataJson = new JSONObject();
                    // 2.1、获取单体信息
                    DantiInfoV3 dantiInfo = null;

                    if (StringUtil.isNotBlank(dantiguid)) {
                        dantiInfo = iDantiInfoV3Service.find(dantiguid);
                        if (dantiInfo != null) {
                            dataJson.put("dantiguid", dantiguid);// 单体guid
                            dataJson.put("itemguid", dantiInfo.getItemguid());// 项目guid
                            dataJson.put("dtmc", dantiInfo.getDtmc());// 单体名称
                            dataJson.put("dtbm", dantiInfo.getDtbm());// 单体编码
                            dataJson.put("xkbabh", dantiInfo.getXkbabh());// 许可备案编号
                            dataJson.put("dtgczzj", dantiInfo.getDtgczzj());// 单体工程总造价
                            dataJson.put("jzmj", dantiInfo.getJzmj());// 建筑面积(m²)
                            dataJson.put("zdmj", dantiInfo.getZdmj());// 占地面积(m²)
                            dataJson.put("dsjzmj", dantiInfo.getDsjzmj());// 地上面积(m²)
                            dataJson.put("dxjzmj", dantiInfo.getDxjzmj());// 地下面积(m²)
                            dataJson.put("dscs", dantiInfo.getDscs());// 地上层数(层)
                            dataJson.put("dxcs", dantiInfo.getDxcs());// 地下层数(层)
                            dataJson.put("jzgcgd", dantiInfo.getJzgcgd());// 建筑高度(m)
                            dataJson.put("cd", dantiInfo.getCd());// 长度(m)
                            dataJson.put("kd", dantiInfo.getKd());// 跨度
                            dataJson.put("dtjwdzb", dantiInfo.getDtjwdzb());// 单体经纬度坐标
                            dataJson.put("gmzb", dantiInfo.getGmzb());// 规模指标

                            // ----------------------------------------------------------------------------------------------------
                        }
                    }
                    // dataJson.put("gclbmc", gclbmc);// 工程类别

                    JSONObject obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");

                    List<JSONObject> list_Jiegoutx = new ArrayList<>();
                    List<CodeItems> jiegoutx = iCodeItemsService.listCodeItemsByCodeName("国标_结构体系");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Jiegoutx.add(obj_choice);
                    for (CodeItems codeItems : jiegoutx) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getJgtx())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getJgtx()))) {
                            objCode.put("isselected", 1);
                            dataJson.put("jgtxtext", codeItems.getItemText());
                        }
                        list_Jiegoutx.add(objCode);
                    }
                    dataJson.put("jgtx", list_Jiegoutx);// 结构体系

                    List<JSONObject> list_Gclbtx = new ArrayList<>();
                    List<CodeItems> gclbtx = iCodeItemsService.listCodeItemsByCodeName("国标_工程类别");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Gclbtx.add(obj_choice);

                    gcfl = String.format("%2s", gcfl).replaceAll(" ", "0");
                    Iterator<CodeItems> iterator = gclbtx.iterator();
                    while (iterator.hasNext()) {
                        CodeItems next = iterator.next();
                        if (!next.getItemValue().startsWith(gcfl)) {
                            iterator.remove();
                        }
                    }
                    for (CodeItems codeItems : gclbtx) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getGcyt())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getGcyt()))) {
                            dataJson.put("gcyttext", codeItems.getItemText());
                            objCode.put("isselected", 1);
                        }
                        list_Gclbtx.add(objCode);
                    }
                    dataJson.put("gcyt", list_Gclbtx);// 工程类别

                    List<JSONObject> list_Firelevel = new ArrayList<>();
                    List<CodeItems> firelevel = iCodeItemsService.listCodeItemsByCodeName("国标_耐火等级");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Firelevel.add(obj_choice);
                    for (CodeItems codeItems : firelevel) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getNhdj())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getNhdj()))) {
                            objCode.put("isselected", 1);
                            dataJson.put("nhdjtext", codeItems.getItemText());
                        }
                        list_Firelevel.add(objCode);
                    }
                    dataJson.put("nhdj", list_Firelevel);// 耐火等级

                    List<JSONObject> list_Jzfs = new ArrayList<>();
                    List<CodeItems> jzfs = iCodeItemsService.listCodeItemsByCodeName("国标_建造方式");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Jzfs.add(obj_choice);
                    for (CodeItems codeItems : jzfs) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getJzfs())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getJzfs()))) {
                            objCode.put("isselected", 1);
                            dataJson.put("jzfstext", codeItems.getItemText());
                        }
                        list_Jzfs.add(objCode);
                    }
                    dataJson.put("jzfs", list_Jzfs);// 建造方式
                    return JsonUtils.zwdtRestReturn("1", " 查询成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 查询单体单位详情接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiInfoDetailNew", method = RequestMethod.POST)
    public String getDantiInfoDetailNew(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、单体唯一标识
                String dantiguid = obj.getString("dantiguid");
                String itemguid = obj.getString("itemguid");
                if (StringUtil.isBlank(itemguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数itemguid不能为空！", "");
                }
                String gcfl = obj.getString("gcfl");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    JSONObject dataJson = new JSONObject();
                    // 2.1、获取单体信息
                    DantiInfo dantiInfo = null;

                    if (StringUtil.isNotBlank(dantiguid)) {
                        dantiInfo = iDantiInfoService.find(dantiguid);
                        if (dantiInfo != null) {
                            dataJson.put("dantiguid", dantiguid);// 单体guid
                            dataJson.put("itemguid", dantiInfo.getProjectguid());// 项目guid
                            dataJson.put("dtmc", dantiInfo.getDantiname());// 单体名称
                            dataJson.put("dtbm", dantiInfo.getStr("dtbm"));// 单体编码
                            dataJson.put("xkbabh", dantiInfo.getStr("xkbabh"));// 许可备案编号
                            dataJson.put("dtgczzj", dantiInfo.getPrice());// 单体工程总造价
                            dataJson.put("jzmj", dantiInfo.getZjzmj());// 建筑面积(m²)
                            dataJson.put("zdmj", dantiInfo.getDouble("zdmj"));// 占地面积(m²)
                            dataJson.put("dsjzmj", dantiInfo.getDishangmianji());// 地上面积(m²)
                            dataJson.put("dxjzmj", dantiInfo.getDixiamianji());// 地下面积(m²)
                            dataJson.put("dscs", dantiInfo.getDscs());// 地上层数(层)
                            dataJson.put("dxcs", dantiInfo.getDxcs());// 地下层数(层)
                            dataJson.put("jzgcgd", dantiInfo.getJzgd());// 建筑高度(m)
                            dataJson.put("cd", dantiInfo.getStr("cd"));// 长度(m)
                            dataJson.put("kd", dantiInfo.getSpan());// 跨度
                            dataJson.put("dtjwdzb", dantiInfo.getStr("dtjwdzb"));// 单体经纬度坐标
                            dataJson.put("gmzb", dantiInfo.getStr("gmzb"));// 规模指标

                            // ----------------------------------------------------------------------------------------------------
                        }
                    }
                    // dataJson.put("gclbmc", gclbmc);// 工程类别

                    JSONObject obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");

                    List<JSONObject> list_Jiegoutx = new ArrayList<>();
                    List<CodeItems> jiegoutx = iCodeItemsService.listCodeItemsByCodeName("国标_结构体系");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Jiegoutx.add(obj_choice);
                    for (CodeItems codeItems : jiegoutx) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getJiegoutx())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getJiegoutx()))) {
                            objCode.put("isselected", 1);
                            dataJson.put("jgtxtext", codeItems.getItemText());
                        }
                        list_Jiegoutx.add(objCode);
                    }
                    dataJson.put("jgtx", list_Jiegoutx);// 结构体系

                    List<JSONObject> list_Gclbtx = new ArrayList<>();
                    List<CodeItems> gclbtx = iCodeItemsService.listCodeItemsByCodeName("国标_工程类别");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Gclbtx.add(obj_choice);

                    gcfl = String.format("%2s", gcfl).replaceAll(" ", "0");
                    Iterator<CodeItems> iterator = gclbtx.iterator();
                    while (iterator.hasNext()) {
                        CodeItems next = iterator.next();
                        if (!next.getItemValue().startsWith(gcfl)) {
                            iterator.remove();
                        }
                    }
                    for (CodeItems codeItems : gclbtx) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getGclb())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getGclb()))) {
                            dataJson.put("gcyttext", codeItems.getItemText());
                            objCode.put("isselected", 1);
                        }
                        list_Gclbtx.add(objCode);
                    }
                    dataJson.put("gcyt", list_Gclbtx);// 工程类别

                    List<JSONObject> list_Firelevel = new ArrayList<>();
                    List<CodeItems> firelevel = iCodeItemsService.listCodeItemsByCodeName("国标_耐火等级");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Firelevel.add(obj_choice);
                    for (CodeItems codeItems : firelevel) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getFirelevel())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getFirelevel()))) {
                            objCode.put("isselected", 1);
                            dataJson.put("nhdjtext", codeItems.getItemText());
                        }
                        list_Firelevel.add(objCode);
                    }
                    dataJson.put("nhdj", list_Firelevel);// 耐火等级

                    List<JSONObject> list_Jzfs = new ArrayList<>();
                    List<CodeItems> jzfs = iCodeItemsService.listCodeItemsByCodeName("国标_建造方式");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Jzfs.add(obj_choice);
                    for (CodeItems codeItems : jzfs) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getStr("jzfs"))
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getStr("jzfs")))) {
                            objCode.put("isselected", 1);
                            dataJson.put("jzfstext", codeItems.getItemText());
                        }
                        list_Jzfs.add(objCode);
                    }
                    dataJson.put("jzfs", list_Jzfs);// 建造方式
                    return JsonUtils.zwdtRestReturn("1", " 查询成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 保存单体单位接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveDantiInfo", method = RequestMethod.POST)
    public String saveDantiInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String itemguid = obj.getString("itemguid");// 工程标识
                if (StringUtil.isBlank(itemguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数itemguid不能为空", "");
                }
                String dantiguid = obj.getString("dantiguid");// 单体标识
                String dantiname = obj.getString("dtmc");// 单体名称
                if (StringUtil.isBlank(dantiname)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dtmc不能为空", "");
                }
                String gclb = obj.getString("gcyt");// 工程类别
                if (StringUtil.isBlank(gclb)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数gcyt不能为空", "");
                }
                String dtbm = obj.getString("dtbm");// 单体编码
                String jiegoutx = obj.getString("jgtx");// 结构体系
                String firelevel = obj.getString("nhdj");// 耐火等级
                String jzfs = obj.getString("jzfs");// 建造方式
                String dtgczzj = obj.getString("dtgczzj");// 单体工程总造价
                String jzmj = obj.getString("jzmj");// 建筑面积
                if (StringUtil.isBlank(jzmj)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数jzmj不能为空", "");
                }
                String zdmj = obj.getString("zdmj");// 占地面积
                if (StringUtil.isBlank(zdmj)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数zdmj不能为空", "");
                }
                String dishangmianji = obj.getString("dsjzmj");// 其中，地上建筑面积
                if (StringUtil.isBlank(dishangmianji)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dsjzmj不能为空", "");
                }
                String dixiamianji = obj.getString("dxjzmj");// 其中，地下建筑面积
                if (StringUtil.isBlank(dixiamianji)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dxjzmj不能为空", "");
                }
                String dscs = obj.getString("dscs");// 地上层数
                if (StringUtil.isBlank(dscs)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dscs不能为空", "");
                }
                String dxcs = obj.getString("dxcs");// 地下层数
                if (StringUtil.isBlank(dxcs)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dxcs不能为空", "");
                }
                String jzgcgd = obj.getString("jzgcgd");// 建筑工程高度
                if (StringUtil.isBlank(jzgcgd)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数jzgcgd不能为空", "");
                }
                String cd = obj.getString("cd");// 长度
                String kd = obj.getString("kd");// 跨度
                String dtjwdzb = obj.getString("dtjwdzb");// 单体经纬度坐标
                String gmzb = obj.getString("gmzb");// 规模指标
                String is_enable = obj.getString("is_enable");// 是否完善

                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    DantiInfoV3 dantiInfo = new DantiInfoV3();
                    dantiInfo.setItemguid(itemguid);
                    dantiInfo.setRowguid(StringUtil.isNotBlank(dantiguid) ? dantiguid : UUID.randomUUID().toString());
                    dantiInfo.setDtmc(dantiname);// 单体名称
                    dantiInfo.setGcyt(gclb);// 工程类别
                    dantiInfo.setDtbm(dtbm);// 单体编码
                    dantiInfo.setJgtx(jiegoutx);// 结构体系
                    if (StringUtil.isNotBlank(firelevel)) {
                        dantiInfo.setNhdj(Integer.valueOf(firelevel));// 耐火等级
                    }
                    if (StringUtil.isNotBlank(jzfs)) {
                        dantiInfo.setJzfs(Integer.valueOf(jzfs));// 建造方式
                    }
                    if (StringUtil.isNotBlank(dtgczzj)) {
                        dantiInfo.setDtgczzj(Double.valueOf(dtgczzj));// 单体工程总造价
                    }
                    if (StringUtil.isNotBlank(jzmj)) {
                        dantiInfo.setJzmj(Double.valueOf(jzmj));// 建筑面积
                    }
                    if (StringUtil.isNotBlank(zdmj)) {
                        dantiInfo.setZdmj(Double.valueOf(zdmj));// 占地面积
                    }
                    if (StringUtil.isNotBlank(dishangmianji)) {
                        dantiInfo.setDsjzmj(Double.valueOf(dishangmianji));// 地上建筑面积
                    }
                    if (StringUtil.isNotBlank(dixiamianji)) {
                        dantiInfo.setDxjzmj(Double.valueOf(dixiamianji));// 地下建筑面积
                    }

                    dantiInfo.setDscs(dscs);// 地上层数
                    dantiInfo.setDxcs(dxcs);// 地下层数
                    if (StringUtil.isNotBlank(jzgcgd)) {
                        dantiInfo.setJzgcgd(Double.valueOf(jzgcgd));// 建筑工程高度
                    }
                    if (StringUtil.isNotBlank(cd)) {
                        dantiInfo.setCd(Double.valueOf(cd));// 长度
                    }
                    if (StringUtil.isNotBlank(kd)) {
                        dantiInfo.setKd(Double.valueOf(kd));// 跨度
                    }
                    dantiInfo.setDtjwdzb(dtjwdzb);// 单体经纬度坐标
                    dantiInfo.setGmzb(gmzb);// 规模指标
                    DantiInfoV3 dantiInfoExit = iDantiInfoV3Service.find(dantiguid);
                    if (dantiInfoExit != null) {
                        if (StringUtil.isBlank(dantiInfoExit.getIs_enable())
                                || ZwfwConstant.CONSTANT_STR_ZERO.equals(dantiInfoExit.getIs_enable())) {
                            dantiInfo.setIs_enable(ZwfwConstant.CONSTANT_STR_ONE);
                        }
                        iDantiInfoV3Service.update(dantiInfo);
                        return JsonUtils.zwdtRestReturn("1", "修改成功", "");
                    }
                    else {
                        dantiInfo.setIs_enable(is_enable);// 是否完善
                        dantiInfo.setCreatedate(new Date());
                        iDantiInfoV3Service.insert(dantiInfo);
                        return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 保存单体单位接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveDantiInfoNew", method = RequestMethod.POST)
    public String saveDantiInfoNew(@RequestBody String params, @Context HttpServletRequest request) {
        return new DantiBizcommon().saveDantiInfo(params,request,false);
    }

    /**
     * 删除单体单位接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/deleteDantiInfo", method = RequestMethod.POST)
    public String deleteDantiInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 单体唯一标识
                String dantiguids = obj.getString("dantiguids");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    if (StringUtil.isNotBlank(dantiguids)) {
                        if (dantiguids.startsWith("[") && dantiguids.endsWith("]")) {
                            dantiguids = dantiguids.substring(1, dantiguids.length() - 2).replaceAll("\"", "");
                            String[] dantiguidArr = dantiguids.split(",");
                            for (String s : dantiguidArr) {
                                iDantiInfoV3Service.deleteByGuid(s);
                            }
                        }
                    }
                    return JsonUtils.zwdtRestReturn("1", " 删除成功！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 查询单体工程目录接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiTreeList", method = RequestMethod.POST)
    public String getDantiTreeList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getItemTreeList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 子申报标识
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    List<JSONObject> list_tree = new ArrayList<>();
                    String itemname = "";
                    if (auditRsItemBaseinfo != null && StringUtil.isNotBlank(auditRsItemBaseinfo.getItemname())) {
                        itemname = auditRsItemBaseinfo.getItemname();
                    }
                    JSONObject objTree = new JSONObject();
                    objTree.put("pId", "root");
                    objTree.put("id", itemguid);
                    objTree.put("name", StringUtil.isNotBlank(itemname) ? itemname : "单位工程");
                    objTree.put("open", true);
                    list_tree.add(objTree);
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("itemguid", itemguid);
                    sql.setSelectFields("gongchengguid");
                    List<DantiInfoV3> dantiInfos = iDantiInfoV3Service.getDantiInfoListByCondition(sql.getMap());
                    Map<String, String> map = new HashMap<>();
                    for (DantiInfoV3 dantiInfo : dantiInfos) {
                        map.put(dantiInfo.getGongchengguid(), "");
                    }
                    for (String key : map.keySet()) {
                        if (StringUtil.isNotBlank(key)) {
                            objTree = new JSONObject();
                            objTree.put("id", key);
                            objTree.put("pId", itemguid);
                            DwgcInfo dwgcInfo = iDwgcInfoService.find(key);
                            if (dwgcInfo != null) {
                                if (StringUtil.isNotBlank(dwgcInfo.getGongchengname())) {
                                    objTree.put("name", dwgcInfo.getGongchengname());
                                }
                                else {
                                    objTree.put("name", "未命名单体工程");
                                }
                            }
                            list_tree.add(objTree);
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("tree", list_tree);
                    return JsonUtils.zwdtRestReturn("1", " 查询成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 确认单体单位接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/confirmDantiInfo", method = RequestMethod.POST)
    public String confirmDantiInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 单体唯一标识数组
                String dantiguids = obj.getString("dantiguids");
                if (StringUtil.isBlank(dantiguids)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dantiguids不能为空", "");
                }
                // 1.2、 组建单位标识
                String subAppguid = obj.getString("subappguid");
                if (StringUtil.isBlank(subAppguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数subappguid不能为空", "");
                }
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    if (StringUtil.isNotBlank(dantiguids)) {
                        if (dantiguids.startsWith("[") && dantiguids.endsWith("]")) {
                            dantiguids = dantiguids.substring(1, dantiguids.length() - 2).replaceAll("\"", "");
                            String[] dantiguidArr = dantiguids.split(",");
                            StringBuilder sb = new StringBuilder();
                            for (String s : dantiguidArr) {
                                DantiInfoV3 dantiInfoV3 = iDantiInfoV3Service.find(s);
                                if (dantiInfoV3 == null) {
                                    DantiInfo dantiInfo = iDantiInfoService.find(s);
                                    if (dantiInfo != null) {
                                        sb.append(dantiInfo.getDantiname() + ",");
                                    }
                                }
                                else {
                                    if (StringUtil.isBlank(dantiInfoV3.getIs_enable())
                                            || ZwfwConstant.CONSTANT_STR_ZERO.equals(dantiInfoV3.getIs_enable())) {
                                        sb.append(dantiInfoV3.getDtmc() + ",");
                                    }
                                }
                            }
                            if (StringUtil.isBlank(sb.toString())) {
                                for (String s : dantiguidArr) {
                                    DantiSubRelation dantiSubRelation = new DantiSubRelation();
                                    dantiSubRelation.setSubappguid(subAppguid);
                                    dantiSubRelation.setDantiguid(s);
                                    dantiSubRelation.setRowguid(UUID.randomUUID().toString());
                                    dantiSubRelation.set("is_v3", ZwfwConstant.CONSTANT_STR_ONE);
                                    iDantiSubRelationService.insert(dantiSubRelation);
                                }
                            }
                            else {
                                JSONObject text = new JSONObject();
                                text.put("error", "请完善" + sb.toString() + "单体信息！");
                                return JsonUtils.zwdtRestReturn("1", " 确认失败！", text.toJSONString());
                            }
                        }
                    }
                    return JsonUtils.zwdtRestReturn("1", " 确认成功！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 组建单位监理信息接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveJlneed", method = RequestMethod.POST)
    public String saveJlneed(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、单体标识数组
                String dantiguids = obj.getString("dantiguids");
                String gongchengnum = obj.getString("gongchengnum");
                String gongchengname = obj.getString("gongchengname");
                String zjyneedcnt = obj.getString("zjyneedcnt");
                String gongchengguid = obj.getString("gongchengguid");
                // 1.2、 jl_infos
                JSONArray jl_infos = obj.getJSONArray("jl_infos");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    List<DwgcInfo> dwlist = iDwgcInfoService.findDwgcListByGongchengnum(gongchengnum);
                    if (ValidateUtil.isNotBlankCollection(dwlist)) {
                        return JsonUtils.zwdtRestReturn("0", "工程编号重复", "");
                    }
                    String rowguid = StringUtil.isNotBlank(gongchengguid) ? gongchengguid
                            : UUID.randomUUID().toString();
                    for (int i = 0; i < jl_infos.size(); i++) {
                        JSONObject object = (JSONObject) jl_infos.get(i);
                        DwgcJlneed dwgcJlneed = new DwgcJlneed();
                        String zhiwu = iCodeItemsService.getItemTextByCodeName("监理工程师职务", object.getString("jl_id"));
                        dwgcJlneed.setZhiwu(zhiwu);
                        dwgcJlneed.setNeedcnt(object.getInteger("jl_cnt"));
                        dwgcJlneed.setGongchengguid(rowguid);
                        dwgcJlneed.setRowguid(UUID.randomUUID().toString());
                        iDwgcJlneedService.insert(dwgcJlneed);
                    }
                    String[] dantiguidArr = dantiguids.split("_");
                    List<DantiInfoV3> dantiInfos = new ArrayList<>();
                    for (String dantiguid : dantiguidArr) {
                        dantiInfos.add(iDantiInfoV3Service.find(dantiguid));
                    }
                    Double totalprice = 0d;
                    Double buildarea = 0d;
                    List<String> jiegouTxList = new ArrayList<>();
                    List<String> gclbTxList = new ArrayList<>();
                    List<String> dishang = new ArrayList<>();
                    List<String> dixia = new ArrayList<>();
                    for (DantiInfoV3 dantiinfo : dantiInfos) {
                        if (dantiinfo.getJgtx() != null) {
                            String jiegoutixi = iCodeItemsService.getItemTextByCodeName("国标_结构体系",
                                    dantiinfo.getJgtx() + "");
                            jiegouTxList.add(jiegoutixi);
                        }
                        if (dantiinfo.getGcyt() != null) {
                            String gongchengleibie = iCodeItemsService.getItemTextByCodeName("国标_工程类别",
                                    dantiinfo.getGcyt() + "");
                            gclbTxList.add(gongchengleibie);
                        }
                        if (dantiinfo.getDtgczzj() != null) {
                            totalprice += dantiinfo.getDtgczzj();
                        }
                        if (dantiinfo.getJzmj() != null) {
                            buildarea += dantiinfo.getJzmj();
                        }
                        if (dantiinfo.getDscs() != null) {
                            dishang.add(dantiinfo.getDscs());
                        }
                        if (dantiinfo.getDxcs() != null) {
                            dixia.add(dantiinfo.getDxcs());
                        }
                    }
                    /*
                     * int dishangmax = 0; int dixiamax = 0; if
                     * (ValidateUtil.isNotBlankCollection(dishang)) { dishangmax
                     * = Collections.max(dishang); } if
                     * (ValidateUtil.isNotBlankCollection(dixia)) { dixiamax =
                     * Collections.max(dixia); }
                     */
                    String jianzhuarea = buildarea + "平方米";
                    DwgcInfo dwgcInfo = new DwgcInfo();
                    dwgcInfo.setGongchengnum(gongchengnum);
                    dwgcInfo.setGongchengname(gongchengname);
                    dwgcInfo.setZjyneedcnt(StringUtil.isNotBlank(zjyneedcnt) ? Integer.parseInt(zjyneedcnt) : 0);
                    dwgcInfo.setJiegoutype(StringUtil.join(jiegouTxList, ";"));
                    dwgcInfo.setProjecttype(StringUtil.join(gclbTxList, ";"));
                    dwgcInfo.setProjectprice(totalprice);
                    dwgcInfo.setBuildarea(jianzhuarea);
                    // dwgcInfo.setDishangcs(dishangmax);
                    // dwgcInfo.setDixiacs(dixiamax);
                    dwgcInfo.setRowguid(rowguid);
                    // 子单位工程组建
                    if (dantiguidArr != null) {
                        DantiInfoV3 dantiInfo = new DantiInfoV3();
                        for (String dantirowguid : dantiguidArr) {
                            dantiInfo = iDantiInfoV3Service.find(dantirowguid);
                            if (StringUtil.isBlank(dantiInfo.getGongchengguid())) {
                                dantiInfo.setGongchengguid(rowguid);
                                iDantiInfoV3Service.update(dantiInfo);
                            }
                        }
                    }
                    dwgcInfo.setOperatedate(new Date());
                    iDwgcInfoService.insert(dwgcInfo);
                    return JsonUtils.zwdtRestReturn("1", " 保存成功！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    @RequestMapping(value = "/initDanweiList", method = RequestMethod.POST)
    public String initDanweiList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 项目标识
                String itemguid = obj.getString("itemguid");
                // 1.2、 子申报标识
                String subAppguid = obj.getString("subappguid");
                // 1.3、 单位类型List
                String dwTypesStr = obj.getString("dwTypesStr");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // TODO API封装
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("corptype", "999");
                    sql.eq("subappguid", subAppguid);
                    List<ParticipantsInfo> infos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                    ParticipantsInfo info = null;
                    if (infos != null && !infos.isEmpty()) {
                        info = infos.get(0);
                    }
                    // 参见单位是否初始化判断
                    if (info == null) {
                        String[] dwTypes = dwTypesStr.split(",");
                        Date d = new Date();
                        for (String dwType : dwTypes) {
                            if ("jsdw".equals(dwType)) {
                                continue;
                            }
                            String danweiNum = "";
                            if (StringUtil.isNotBlank(dwType)) {
                                switch (dwType) {
                                    case "jsdw":
                                        danweiNum = "31";
                                        break;
                                    case "sgdw":
                                        danweiNum = "3";
                                        break;
                                    case "sjdw":
                                        danweiNum = "2";
                                        break;
                                    case "kcdw":
                                        danweiNum = "1";
                                        break;
                                    case "jldw":
                                        danweiNum = "4";
                                        break;
                                    case "jcdw":
                                        danweiNum = "10";
                                        break;
                                    default:
                                        break;
                                }
                            }
                            SqlConditionUtil sqlParticipantsInfo = new SqlConditionUtil();
                            sqlParticipantsInfo.eq("itemguid", itemguid);
                            sqlParticipantsInfo.eq("corptype", danweiNum);
                            /* 3.0新增逻辑 */
                            sqlParticipantsInfo.setSelectFields(
                                    "distinct jsdwlx,xkbabh,cert,legalcardtype,fzrzjlx,corptype,danweilxrsfz,fbsafenum,cbdanweitype,fbtime,fbscopeofcontract,fbqysettime,fbaqglry,fbaqglrysafenum,xmfzperson,xmfzrsafenum,qylxr,qylxdh,gdlxr,gdlxdh,xmfzrphonenum,danweilxr,danweilxrlxdh, corpname, corpcode, legal, phone, address, cert, xmfzr, xmfzr_idcard, xmfzr_zc, xmfzr_phone, xmfzr_certlevel, xmfzr_certnum, xmfzr_certid, jsfzr, jsfzr_zc, jsfzr_phone, itemguid ,fremail ,frphone ,legalpersonicardnum,itemlegaldept,itemlegalcertnum,itemlegalcerttype,legalproperty");
                            /* 3.0结束逻辑 */

                            List<ParticipantsInfo> list = iParticipantsInfo
                                    .getParticipantsInfoListByCondition(sqlParticipantsInfo.getMap());
                            for (ParticipantsInfo participantsInfo : list) {
                                participantsInfo.set("rowguid", UUID.randomUUID().toString());
                                participantsInfo.set("operatedate", d);
                                participantsInfo.set("subappguid", subAppguid);
                                iParticipantsInfo.insert(participantsInfo);
                                // iParticipantsInfo.insert(participantsInfo);
                            }
                        }
                        // 新增初始化数据
                        info = new ParticipantsInfo();
                        info.set("operatedate", new Date());
                        info.set("rowguid", UUID.randomUUID().toString());
                        info.set("subappguid", subAppguid);
                        info.set("corptype", "999");
                        info.set("corpname", "系统生成");
                        iParticipantsInfo.insert(info);
                    }
                    return JsonUtils.zwdtRestReturn("1", " 初始化成功！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "查询失败", "");
        }
    }

    /**
     * 查询单位列表接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDanwei", method = RequestMethod.POST)
    public String getDanwei(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveDanwei接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.11、 联系电话
                String dwguid = obj.getString("dwguid");
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ParticipantsInfo participantsInfo = null;
                    String corpname = "";
                    String corpcode = "";
                    String address = "";
                    String phone = "";
                    String legal = "";
                    // String legalproperty = "";
                    List<JSONObject> itemlegalcerttypeList = new ArrayList<>();
                    List<JSONObject> legalpropertyList = new ArrayList<>();
                    String itemlegalcertnum = "";
                    String itemlegaldept = "";
                    String legalpersonicardnum = "";
                    String frphone = "";
                    String fremail = "";
                    /* 3.0新增逻辑 */
                    String legalcardtype = "";
                    String fzrzjlx = "";
                    String jsdwlx = "";
                    String corpType = "";
                    /* 3.0结束逻辑 */
                    if (StringUtil.isNotBlank(dwguid)) {
                        participantsInfo = iParticipantsInfo.find(dwguid);
                    }

                    JSONObject data = new JSONObject();
                    if (participantsInfo != null) {
                        /* 3.0新增逻辑 */
                        String cert = participantsInfo.getStr("cert");
                        if (StringUtil.isNotBlank(cert)) {
                            data.put("cert", cert);
                            data.put("certdetail", iCodeItemsService.getItemTextByCodeName("国标_资质等级", cert));
                        }
                        data.put("xkbabh", participantsInfo.getStr("xkbabh"));
                        legalcardtype = participantsInfo.getStr("legalcardtype");
                        fzrzjlx = participantsInfo.getStr("fzrzjlx");
                        jsdwlx = participantsInfo.getStr("jsdwlx");
                        corpType = participantsInfo.getCorptype();
                        /* 3.0结束逻辑 */
                        corpname = participantsInfo.getCorpname();
                        corpcode = participantsInfo.getCorpcode();
                        address = participantsInfo.getAddress();
                        phone = participantsInfo.getPhone();
                        legal = participantsInfo.getLegal();
                        String itemlegalcerttype = "";
                        String legalproperty = "";
                        String cbtypledetail = "";
                        if (StringUtil.isNotBlank(participantsInfo.getItemlegalcerttype())) {
                            itemlegalcerttype = iCodeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                                    participantsInfo.getItemlegalcerttype());
                        }
                        if (StringUtil.isNotBlank(participantsInfo.getLegalproperty())) {
                            legalproperty = iCodeItemsService.getItemTextByCodeName("法人性质",
                                    participantsInfo.getLegalproperty());
                        }
                        data.put("itemlegalcerttypedetail", itemlegalcerttype);
                        data.put("legalpropertydetail", legalproperty);
                        if (StringUtil.isNotBlank(participantsInfo.getCbdanweitype())) {
                            cbtypledetail = iCodeItemsService.getItemTextByCodeName("单位类型",
                                    participantsInfo.getCbdanweitype());

                        }
                        data.put("cbtypledetail", cbtypledetail);
                        itemlegalcertnum = participantsInfo.getItemlegalcertnum();
                        itemlegaldept = participantsInfo.getItemlegaldept();
                        legalpersonicardnum = participantsInfo.getLegalpersonicardnum();
                        frphone = participantsInfo.getFrphone();
                        fremail = participantsInfo.getFremail();
                        data.put("xmfzr", participantsInfo.getXmfzr());// 项目负责人
                        data.put("xmfzr_zc", participantsInfo.getXmfzr_zc());// 职称
                        data.put("xmfzr_idcard", participantsInfo.getXmfzr_idcard());// 身份证
                        data.put("xmfzr_phone", participantsInfo.getXmfzr_phone());// 项目负责人联系电话
                        if (StringUtil.isNotBlank(participantsInfo.getCbdanweitype())) {
                            data.put("cbdanweitype", participantsInfo.getCbdanweitype());
                        }
                        data.put("jsfzr", participantsInfo.getJsfzr());
                        data.put("jsfzr_zc", participantsInfo.getJsfzr_zc());
                        data.put("jsfzr_phone", participantsInfo.getJsfzr_phone());
                        data.put("xmfzperson", participantsInfo.getXmfzperson());
                        data.put("xmfzrsafenum", participantsInfo.getXmfzrsafenum());
                        data.put("xmfzrphonenum", participantsInfo.getXmfzrphonenum());
                        data.put("qylxr", participantsInfo.getQylxr());
                        data.put("qylxdh", participantsInfo.getQylxdh());
                        data.put("gdlxr", participantsInfo.getGdlxr());
                        data.put("gdlxdh", participantsInfo.getGdlxdh());
                        data.put("fbsafenum", participantsInfo.getFbsafenum());
                        data.put("fbtime", participantsInfo.getFbtime());
                        data.put("fbscopeofcontract", participantsInfo.getFbscopeofcontract());
                        data.put("fbaqglrysafenum", participantsInfo.getFbaqglrysafenum());
                        data.put("fbqysettime",
                                EpointDateUtil.convertDate2String(participantsInfo.getFbqysettime(), "yyyy-MM-dd"));
                        data.put("fbaqglry", participantsInfo.getFbaqglry());
                        data.put("danweilxr", participantsInfo.getDanweilxr());
                        data.put("danweilxrlxdh", participantsInfo.getDanweilxrlxdh());
                        data.put("danweilxrsfz", participantsInfo.getDanweilxrsfz());
                        data.put("frphone", participantsInfo.getFrphone());
                        data.put("fremail", participantsInfo.getFremail());
                        data.put("legalproperty", participantsInfo.getLegalproperty());
                        data.put("legal", participantsInfo.getLegal());

                        List<CodeItems> itemlegalcerttypes = iCodeItemsService
                                .listCodeItemsByCodeName("申请人用来唯一标识的证照类型");
                        JSONObject objtype = new JSONObject();
                        objtype.put("itemvalue", "");
                        objtype.put("itemtext", "请选择");
                        itemlegalcerttypeList.add(objtype);
                        for (CodeItems codeItems : itemlegalcerttypes) {
                            if (Integer.parseInt(codeItems.getItemValue()) >= Integer
                                    .parseInt(ZwfwConstant.CERT_TYPE_SFZ)) {
                                continue;
                            }
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (codeItems.getItemValue().equals(participantsInfo.getItemlegalcerttype())) {
                                objJson.put("isselected", 1);
                            }
                            itemlegalcerttypeList.add(objJson);
                        }
                        List<CodeItems> legalpropertys = iCodeItemsService.listCodeItemsByCodeName("法人性质");
                        objtype = new JSONObject();
                        objtype.put("itemvalue", "");
                        objtype.put("itemtext", "请选择");
                        legalpropertyList.add(objtype);
                        for (CodeItems codeItems : legalpropertys) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (codeItems.getItemValue().equals(participantsInfo.getLegalproperty())) {
                                objJson.put("isselected", 1);
                            }
                            legalpropertyList.add(objJson);
                        }
                        data.put("corpname", corpname);
                        data.put("corpcode", corpcode);
                        data.put("address", address);
                        data.put("phone", phone);
                        data.put("legal", legal);
                        data.put("legalproperty", legalpropertyList);
                        data.put("itemlegalcerttype", itemlegalcerttypeList);
                        data.put("itemlegalcertnum", itemlegalcertnum);
                        data.put("itemlegaldept", itemlegaldept);
                        data.put("legalpersonicardnum", legalpersonicardnum);
                        data.put("frphone", frphone);
                        data.put("fremail", fremail);
                    }
                    else {
                        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                        if (auditRsItemBaseinfo != null) {
                            /* 3.0新增逻辑 */
                            // 国标_证件类型 身份证
                            legalcardtype = "1";
                            jsdwlx = auditRsItemBaseinfo.getStr("jsdwlx");
                            /* 3.0结束逻辑 */
                            // 获取统一社会信用代码/组织机构代码证
                            corpcode = auditRsItemBaseinfo.getStr("itemlegalcertnum");
                            // 通过这个字段查询企业信息
                            String codetype = auditRsItemBaseinfo.getStr("itemlegalcerttype");
                            String field = "creditcode";
                            if ("14".equals(codetype)) {
                                field = "organcode";
                            }
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                    .getCompanyByOneField(field, corpcode).getResult();
                            itemlegalcertnum = corpcode;
                            if (auditRsCompanyBaseinfo == null) {
                                return JsonUtils.zwdtRestReturn("0", "获取企业信息失败", "");
                            }
                            itemlegaldept = auditRsCompanyBaseinfo.getOrganname();
                            legalpersonicardnum = auditRsCompanyBaseinfo.getOrgalegal_idnumber(); // 法人身份证
                            corpname = auditRsCompanyBaseinfo.getOrganname(); // 单位名字
                            legal = auditRsCompanyBaseinfo.getOrganlegal();
                            address = auditRsItemBaseinfo.getConstructionaddress();
                            phone = auditRsItemBaseinfo.getContractphone();
                            corpcode = StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum();
                            List<CodeItems> itemlegalcerttypes = iCodeItemsService
                                    .listCodeItemsByCodeName("申请人用来唯一标识的证照类型");
                            JSONObject objtype = new JSONObject();
                            objtype.put("itemvalue", "");
                            objtype.put("itemtext", "请选择");
                            itemlegalcerttypeList.add(objtype);
                            for (CodeItems codeItems : itemlegalcerttypes) {
                                if (Integer.parseInt(codeItems.getItemValue()) >= Integer
                                        .parseInt(ZwfwConstant.CERT_TYPE_SFZ)) {
                                    continue;
                                }
                                JSONObject objJson = new JSONObject();
                                objJson.put("itemvalue", codeItems.getItemValue());
                                objJson.put("itemtext", codeItems.getItemText());
                                if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                    objJson.put("isselected", 1);
                                }
                                itemlegalcerttypeList.add(objJson);
                            }
                            List<CodeItems> legalproperty = iCodeItemsService.listCodeItemsByCodeName("法人性质");
                            objtype = new JSONObject();
                            objtype.put("itemvalue", "");
                            objtype.put("itemtext", "请选择");
                            legalpropertyList.add(objtype);
                            for (CodeItems codeItems : legalproperty) {
                                JSONObject objJson = new JSONObject();
                                objJson.put("itemvalue", codeItems.getItemValue());
                                objJson.put("itemtext", codeItems.getItemText());
                                if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getLegalproperty())) {
                                    objJson.put("isselected", 1);
                                }
                                legalpropertyList.add(objJson);
                            }
                            frphone = auditRsItemBaseinfo.getFrphone();
                            fremail = auditRsItemBaseinfo.getFremail();
                            data.put("item_corpname", corpname);
                            data.put("item_corpcode", corpcode);
                            data.put("item_address", address);
                            data.put("item_phone", phone);
                            data.put("item_legal", legal);
                            data.put("item_legalproperty", legalpropertyList);
                            data.put("item_itemlegalcerttype", itemlegalcerttypeList);
                            data.put("item_itemlegalcertnum", itemlegalcertnum);
                            data.put("item_itemlegaldept", itemlegaldept);
                            data.put("item_legalpersonicardnum", legalpersonicardnum);
                            data.put("item_frphone", frphone);
                            data.put("item_fremail", fremail);
                            data.put("danweilxr", auditRsItemBaseinfo.getContractperson());
                            data.put("danweilxrlxdh", auditRsItemBaseinfo.getContractphone());

                        }
                    }
                    List<JSONObject> cbtyple = new ArrayList<>();
                    List<CodeItems> cbtypleList = iCodeItemsService.listCodeItemsByCodeName("单位类型");
                    for (CodeItems codeItems : cbtypleList) {
                        JSONObject objJson = new JSONObject();
                        objJson.put("itemvalue", codeItems.getItemValue());
                        objJson.put("itemtext", codeItems.getItemText());
                        if (participantsInfo != null
                                && codeItems.getItemValue().equals(participantsInfo.getCbdanweitype())) {
                            objJson.put("isselected", 1);
                        }
                        cbtyple.add(objJson);
                    }
                    data.put("cbtyple", cbtyple);
                    /* 3.0新增逻辑 */
                    List<JSONObject> legalCardTypes = getSelectItemList("国标_证件类型", legalcardtype);
                    if (legalCardTypes != null && !legalCardTypes.isEmpty()) {
                        data.put("legalcardtypedetail",
                                iCodeItemsService.getItemTextByCodeName("国标_证件类型", legalcardtype));
                    }
                    else {
                        data.put("legalcardtypedetail", "--");
                    }
                    data.put("legalcardtype", legalCardTypes);
                    List<JSONObject> fzrzjlxs = getSelectItemList("国标_证件类型", fzrzjlx);
                    if (fzrzjlxs != null && !fzrzjlxs.isEmpty()) {
                        data.put("fzrzjlxdetail", iCodeItemsService.getItemTextByCodeName("国标_证件类型", fzrzjlx));
                    }
                    else {
                        data.put("fzrzjlxdetail", "--");
                    }
                    data.put("fzrzjlx", fzrzjlxs);
                    List<JSONObject> jsdwlxs = getSelectItemList("国标_建设单位类型", jsdwlx);
                    if (jsdwlxs != null && !jsdwlxs.isEmpty()) {
                        data.put("jsdwlxdetail", iCodeItemsService.getItemTextByCodeName("国标_建设单位类型", jsdwlx));
                    }
                    else {
                        data.put("jsdwlxdetail", "--");
                    }
                    data.put("jsdwlx", jsdwlxs);
                    List<JSONObject> corpTypes = getSelectItemList("关联单位类型", corpType);
                    if (corpTypes != null && !corpTypes.isEmpty()) {
                        data.put("corptypedetail", iCodeItemsService.getItemTextByCodeName("关联单位类型", corpType));
                    }
                    else {
                        data.put("corptypedetail", "--");
                    }
                    data.put("corptype", corpTypes);
                    /* 3.0结束逻辑 */
                    log.info("=======结束调用saveDanwei接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 保存成功！", data.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用saveDanwei接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveDanwei接口参数：params【" + params + "】=======");
            log.info("=======saveDanwei异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存失败", "");
        }
    }

    /**
     * 需要选择单位和人员的信息的单位新增
     */
    @RequestMapping(value = "/saveParticipants", method = RequestMethod.POST)
    public String saveParticipants(@RequestBody String params, @Context HttpServletRequest request) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String token = jsonObject.getString("token");
        if (ZwdtConstant.SysValidateData.equals(token)) {
            JSONObject param = jsonObject.getJSONObject("params");
            String dwguid = param.getString("dwguid");
            String itemguid = param.getString("itemguid");
            String subappguid = param.getString("subappguid");
            String corpname = param.getString("corpname");
            String corpcode = param.getString("corpcode");
            String legal = param.getString("legal");
            String phone = param.getString("phone");
            String address = param.getString("address");
            String xmfzr = param.getString("xmfzr");
            String xmfzr_zc = param.getString("xmfzr_zc");
            String xmfzr_idcard = param.getString("xmfzr_idcard");
            String xmfzr_phone = param.getString("xmfzr_phone");
            String corptype = param.getString("corptype");
            String legalproperty = param.getString("legalproperty");
            String itemlegalcerttype = param.getString("itemlegalcerttype");
            String itemlegalcertnum = param.getString("itemlegalcertnum");
            String itemlegaldept = param.getString("itemlegaldept");
            String legalpersonicardnum = param.getString("legalpersonicardnum");
            String frphone = param.getString("frphone");
            String fremail = param.getString("fremail");
            String danweilxr = param.getString("danweilxr");
            String danweilxrlxdh = param.getString("danweilxrlxdh");
            String danweilxrsfz = param.getString("danweilxrsfz");
            String cbtyple = param.getString("cbtyple");
            // 施工阶段的字段=============================================================================================
            String cbdanweitype = param.getString("cbdanweitype");
            String jsfzr = param.getString("jsfzr");
            String jsfzr_zc = param.getString("jsfzr_zc");
            String jsfzr_phone = param.getString("jsfzr_phone");
            String xmfzperson = param.getString("xmfzperson");
            String xmfzrsafenum = param.getString("xmfzrsafenum");
            String xmfzrphonenum = param.getString("xmfzrphonenum");
            String qylxr = param.getString("qylxr");
            String qylxdh = param.getString("qylxdh");
            String gdlxr = param.getString("gdlxr");
            String gdlxdh = param.getString("gdlxdh");
            String fbsafenum = param.getString("fbsafenum");
            String fbtime = param.getString("fbtime");
            String fbscopeofcontract = param.getString("fbscopeofcontract");
            String fbqysettime = param.getString("fbqysettime");
            String fbaqglry = param.getString("fbaqglry");
            String fbaqglrysafenum = param.getString("fbaqglrysafenum");
            /* 3.0新增逻辑 */
            String jsdwlx = param.getString("jsdwlx");// 敏感字段，前端不允许修改，默认取项目的
            String legalcardtype = param.getString("legalcardtype");
            String xkbabh = param.getString("xkbabh");
            String fzrzjlx = param.getString("fzrzjlx");
            String cert = param.getString("cert");
            String is_relateadd = param.getString("is_relateadd");
            // if ("31".equals(corptype) && StringUtil.isNotBlank(jsdwlx)) {
            // if ("1".equals(jsdwlx)) {
            // itemlegalcerttype = ZwfwConstant.CERT_TYPE_TYSHXYDM;
            // }
            // if ("2".equals(jsdwlx)) {
            // itemlegalcerttype = ZwfwConstant.CERT_TYPE_SFZ;
            // }
            // }
            /* 3.0结束逻辑 */
            // ----------------------------------------------------------------------------------------------------------
            // 判断统一社会信用代码是否重复
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("itemGuid", itemguid);
            sql.eq("corptype", corptype);
            sql.eq("subappguid", subappguid);
            sql.eq("corpcode", corpcode);
            sql.nq("rowguid", dwguid);
            List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                    .getParticipantsInfoListByCondition(sql.getMap());
            if (!participantsInfoList.isEmpty()) {
                return JsonUtils.zwdtRestReturn("0", "统一社会信用代码存在！", "");
            }
            if (StringUtil.isNotBlank(itemguid) && StringUtil.isNotBlank(subappguid)) {
                if ("31".equals(corptype)) {
                    // 项目敏感字段后台获取
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    // 获取统一社会信用代码/组织机构代码证
                    corpcode = auditRsItemBaseinfo.getStr("itemlegalcertnum");
                    // 通过这个字段查询企业信息
                    String codetype = auditRsItemBaseinfo.getStr("itemlegalcerttype");
                    itemlegalcerttype = codetype; // 项目证照类型
                    String field = "creditcode";
                    if ("14".equals(codetype)) {
                        field = "organcode";
                    }
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                            .getCompanyByOneField(field, corpcode).getResult();
                    itemlegalcertnum = corpcode;
                    if (auditRsCompanyBaseinfo == null) {
                        return JsonUtils.zwdtRestReturn("0", "获取企业信息失败,请检查统一社会信用代码或组织机构代码是否配置正确", "");
                    }
                    itemlegaldept = auditRsCompanyBaseinfo.getStr("organname");
                    legalpersonicardnum = auditRsCompanyBaseinfo.getStr("orgalegal_idnumber"); // 法人身份证
                    corpname = auditRsCompanyBaseinfo.getStr("organname"); // 单位名字
                    legal = auditRsCompanyBaseinfo.getStr("organlegal");
                    /* 3.0新增逻辑 */
                    jsdwlx = auditRsItemBaseinfo.getStr("jsdwlx");
                    /* 3.0结束逻辑 */
                    // 如果是建设单位则需要同步一些字段到项目表
                    AuditRsItemBaseinfo auditRsBaseItemInfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    auditRsBaseItemInfo.set("departname", corpname); // 建设单位名字
                    auditRsBaseItemInfo.set("legalperson", legal); // 建设法定代表人
                    auditRsBaseItemInfo.set("legalpersonicardnum", legalpersonicardnum); // 建设法定代表人身份证
                    auditRsBaseItemInfo.set("contractidcart", danweilxrsfz); // 联系人身份证
                    auditRsBaseItemInfo.set("constructionaddress", address); // 联系地址
                    auditRsBaseItemInfo.set("contractperson", danweilxr); // 联系人
                    auditRsBaseItemInfo.set("contractphone", danweilxrlxdh); // 联系人电话
                    auditRsBaseItemInfo.set("legalproperty", legalproperty); // 法人性质
                    auditRsBaseItemInfo.set("itemlegaldept", itemlegaldept); // 项目建设单位
                    auditRsBaseItemInfo.set("frphone", frphone); // 法人电话
                    auditRsBaseItemInfo.set("fremail", fremail); // 法人邮箱
                    iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsBaseItemInfo);
                }

                ParticipantsInfo participantsInfo = new ParticipantsInfo();
                if (StringUtil.isNotBlank(dwguid)) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("rowguid", dwguid);
                    List<ParticipantsInfo> infos = iParticipantsInfo
                            .getParticipantsInfoListByCondition(sqlConditionUtil.getMap());
                    if (!infos.isEmpty()) {
                        ParticipantsInfo info = infos.get(0);
                        corpname = info.getCorpname();
                        corpcode = info.getCorpcode();
                    }
                }
                participantsInfo.setRowguid(StringUtil.isNotBlank(dwguid) ? dwguid : UUID.randomUUID().toString());
                participantsInfo.setOperatedate(new Date());
                participantsInfo.setSubappguid(subappguid);// 子申报示例
                participantsInfo.setItemguid(itemguid);// 项目标识
                participantsInfo.setCorpname(corpname);// 单位名称
                participantsInfo.setCorpcode(corpcode);// 统一社会信用代码
                participantsInfo.setLegal(legal);// 法定代表人
                participantsInfo.setPhone(phone);// 联系电话
                participantsInfo.setAddress(address);// 单位地址
                participantsInfo.setXmfzr(xmfzr);// 项目负责人
                participantsInfo.setXmfzr_zc(xmfzr_zc);// 职称
                participantsInfo.setXmfzr_idcard(xmfzr_idcard);// 身份证
                participantsInfo.setXmfzr_phone(xmfzr_phone);// 项目负责任人联系电话
                participantsInfo.setCorptype(corptype);// 单位类型
                participantsInfo.setLegalproperty(legalproperty);
                participantsInfo.setItemlegalcerttype(itemlegalcerttype);
                participantsInfo.setItemlegalcertnum(itemlegalcertnum);
                participantsInfo.setItemlegaldept(itemlegaldept);
                participantsInfo.setLegalpersonicardnum(legalpersonicardnum);
                participantsInfo.setFrphone(frphone);
                participantsInfo.setFremail(fremail);
                participantsInfo.setDanweilxr(danweilxr);
                participantsInfo.setDanweilxrlxdh(danweilxrlxdh);
                participantsInfo.setDanweilxrsfz(danweilxrsfz);
                // 施工单位
                if ("3".equals(corptype)) {
                    participantsInfo.setCbdanweitype(cbdanweitype);
                    participantsInfo.setJsfzr(jsfzr);
                    participantsInfo.setJsfzr_zc(jsfzr_zc);
                    participantsInfo.setJsfzr_phone(jsfzr_phone);
                    participantsInfo.setXmfzperson(xmfzperson);
                    participantsInfo.setXmfzrsafenum(xmfzrsafenum);
                    participantsInfo.setXmfzrphonenum(xmfzrphonenum);
                    participantsInfo.setQylxr(qylxr);
                    participantsInfo.setQylxdh(qylxdh);
                    participantsInfo.setGdlxr(gdlxr);
                    participantsInfo.setGdlxdh(gdlxdh);
                    participantsInfo.setFbsafenum(fbsafenum);
                    participantsInfo.setFbtime(fbtime);
                    participantsInfo.setFbscopeofcontract(fbscopeofcontract);
                    if (StringUtil.isNotBlank(fbqysettime)) {
                        participantsInfo.setFbqysettime(EpointDateUtil.convertString2Date(fbqysettime, "yyyy-MM-dd"));
                    }
                    participantsInfo.setFbaqglry(fbaqglry);
                    participantsInfo.setFbaqglrysafenum(fbaqglrysafenum);
                }
                /* 3.0新增逻辑 */
                participantsInfo.set("jsdwlx", jsdwlx);
                participantsInfo.set("legalcardtype", legalcardtype);
                participantsInfo.set("xkbabh", xkbabh);
                participantsInfo.set("fzrzjlx", fzrzjlx);
                participantsInfo.set("cert", cert);
                participantsInfo.set("is_relateadd", is_relateadd);
                /* 3.0结束逻辑 */
                ParticipantsInfo participantsInfoExist = iParticipantsInfo.find(dwguid);
                if (participantsInfoExist != null) {
                    iParticipantsInfo.update(participantsInfo);
                    return JsonUtils.zwdtRestReturn("1", "修改成功", "");
                }
                else {
                    iParticipantsInfo.insert(participantsInfo);
                    return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "保存失败！", "");
            }
        }
        else {
            return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
        }
    }

    /**
     * 删除单体单位接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/deleteDanti", method = RequestMethod.POST)
    public String deleteDanti(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 单体唯一标识
                String relationguid = obj.getString("relationguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    if (StringUtil.isNotBlank(relationguid)) {
                        iDantiSubRelationService.deleteByGuid(relationguid);
                    }
                    return JsonUtils.zwdtRestReturn("1", " 删除成功！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 查询资质等级接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getZzdj", method = RequestMethod.POST)
    public String getZzdj(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("国标_资质等级");
                    List<JSONObject> jsonList = new ArrayList<>();
                    for (CodeItems codeItem : codeItems) {
                        if (codeItem.getItemValue().length() == 1) {
                            JSONObject obj = new JSONObject();
                            obj.put("id", codeItem.getItemValue());
                            obj.put("pId", "root");
                            obj.put("name", codeItem.getItemText());
                            jsonList.add(obj);
                        }
                        if (codeItem.getItemValue().length() == 3) {
                            JSONObject obj = new JSONObject();
                            obj.put("id", codeItem.getItemValue());
                            obj.put("pId", codeItem.getItemValue().substring(0, 1));
                            obj.put("name", codeItem.getItemText());
                            jsonList.add(obj);
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("tree_list", jsonList);
                    return JsonUtils.zwdtRestReturn("1", " 查询成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请您登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "查询失败", "");
        }
    }

    /**
     * 存在共同字段的, 先判断一下是否有值
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
        if (!isSelected) {
            resultJsonList.get(0).put("isselected", 0);
        }
        return resultJsonList;
    }

    /**
     * 获取用户唯一标识
     *
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }
    /* 3.0结束逻辑 */

    public String getNameByCode(String code) {
        // 显示父节点及子节点的内容
        String grandson = "";
        String son = "";
        String father = "";
        String grdfather = "";
        StringBuilder sb = new StringBuilder();
        String allRood = "";
        if (code.contains(",")) {
            String[] codes = code.split(",");
            for (String codevalue : codes) {
                if (StringUtil.isNotBlank(codevalue)) {
                    grandson = iCodeItemsService.getItemTextByCodeName("项目分类", codevalue);

                    if (codevalue.length() > 2) {
                        grdfather = iCodeItemsService.getItemTextByCodeName("项目分类", codevalue.substring(0, 2));
                    }
                    if (codevalue.length() > 4) {
                        father = iCodeItemsService.getItemTextByCodeName("项目分类", codevalue.substring(0, 4));
                    }
                    if (codevalue.length() > 6) {
                        son = iCodeItemsService.getItemTextByCodeName("项目分类", codevalue.substring(0, 6));
                    }
                    if (codevalue.length() == 8) {
                        allRood = grdfather + "·" + father + "·" + son + "·" + grandson;
                    }
                    if (codevalue.length() == 6) {
                        allRood = grdfather + "·" + father + "·" + grandson;
                    }
                    if (codevalue.length() == 4) {
                        allRood = grdfather + "·" + grandson;
                    }
                }

                sb.append(allRood);
                sb.append(";");
            }
        }
        else {
            if (StringUtil.isNotBlank(code)) {
                grandson = iCodeItemsService.getItemTextByCodeName("项目分类", code);

                if (code.length() > 2) {
                    grdfather = iCodeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 2));
                }
                if (code.length() > 2) {
                    father = iCodeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 2));
                }
                if (code.length() > 4) {
                    father = iCodeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 4));
                }
                if (code.length() > 6) {
                    son = iCodeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 6));
                }
                if (code.length() == 8) {
                    return grdfather + "·" + father + "·" + son + "·" + grandson;
                }
                if (code.length() == 6) {
                    return grdfather + "·" + father + "·" + grandson;
                }
                if (code.length() == 4) {
                    return grdfather + "·" + grandson;
                }
                else {
                    return grandson;
                }
            }
        }

        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public String getItemText(String itemvalue, String codename) {
        return iCodeItemsService.getItemTextByCodeName(codename, itemvalue);
    }

}
