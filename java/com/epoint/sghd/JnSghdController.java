package com.epoint.sghd;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.sghd.auditjianguan.action.RenlingService;
import com.epoint.sghd.auditjianguan.impl.SgytSpxxProjectService;
import com.epoint.sghd.auditjianguan.inter.IGxhAuditJianguan;
import com.epoint.sghd.auditjianguan.renlingrecord.api.IRenlingRecordService;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;
import com.epoint.sghd.auditjianguancenter.api.IJnAuditJianGuanCenter;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import com.epoint.xmz.audittaskjn.api.IAuditTaskJnRenlingService;
import com.epoint.xmz.audittaskjn.api.entity.AuditTaskJnRenling;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 审管互动平台相关接口
 *
 */
@RestController
@RequestMapping("/jnsghd")
public class JnSghdController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 踏勘API
     */
    @Autowired
    private IKanyanmainService iKanyanmainService;

    @Autowired
    private IKanyanprojectService iKanyanprojectService;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IUserRoleRelationService iUserRoleRelationService;

    @Autowired
    private IJnAuditJianGuanCenter jnAuditJianGuanCenter;

    @Autowired
    private IGxhAuditJianguan gxhAuditJianguan;

    @Autowired
    private SgytSpxxProjectService sgytSpxxProjectService;

    @Autowired
    private IRenlingRecordService iRenlingRecordService;

    @Autowired
    private IAuditTaskJnRenlingService auditTaskJnRenlingService;


    @Autowired
    private IOuService iOuService;
     /**
     * 获取审管互动数据
     * TODO 优化下接口返回
     * @return
     */
    @RequestMapping(value = "/getsgnum", method = RequestMethod.POST)
    public String getsgnum(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getsgnum接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String access_token = obj.getString("access_token");
                log.info("access_token:"+access_token);
                //通过access_token 获取loginid
                String loginid="";
                String ssourl = ConfigUtil.getConfigValue("bigDataTokenUrl");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+access_token);
                String result = com.epoint.cert.commonutils.HttpUtil.doPost(ssourl + "/rest/oauth2/loginid", new HashMap<>(),headers);
                if (StringUtil.isNotBlank(result)) {
                    loginid =result;
                }
                log.info("loginid:"+loginid);
                FrameUser user= iUserService.getUserByUserField("loginid",loginid);
                int sp =0,wrl=0,yrl=0;
                log.info("user:"+user.getDisplayName());
                if(user!=null){
                    List<String> roles = iRoleService.listRoleGuidByUserGuid(user.getUserGuid());
                    AuditProject databean = new AuditProject();
                    databean.setAreacode("370800");
                    databean.setOuguid(user.getOuGuid());
                    databean.set("jg_userguid", user.getUserGuid());
                    int first=0;
                    int pageSize=10;
                    if(CollectionUtils.isNotEmpty(roles)){
                        log.info("roles:"+roles.size());
                        PageData<AuditProject> pageData = new PageData<>();
                        for(String roleguid:roles){
                            FrameRole role = iRoleService.getRoleByRoleField("roleguid", roleguid);
                            if (role != null) {
                                /*if ("部门审管".equals(role.getRoleName())) {
                                    sp = gxhAuditJianguan.getSpxxCount();
                                    log.info("sp:"+sp);
                                    yrl = gxhAuditJianguan.getYrlCount();
                                    log.info("wrl:"+wrl);
                                    wrl = gxhAuditJianguan.getWrlCount();
                                    log.info("yrl:"+yrl);
                                }
                                if ("中心审管".equals(role.getRoleName())) {
                                    sp = getSpxxCount("370800");
                                    log.info("sp:"+sp);
                                    wrl =  getWrl("370800");
                                    log.info("wrl:"+wrl);
                                    yrl = getYrl("370800");
                                    log.info("yrl:"+yrl);
                                }*/
                                if ("部门审管".equals(role.getRoleName())) {
                                    pageData = sgytSpxxProjectService.findProjectVOPage(first, pageSize,
                                            databean);
                                    sp =pageData.getRowCount();
                                    log.info("sp:"+sp);
                                    pageData = sgytSpxxProjectService.findWlrProjectVOPage(first, pageSize,
                                            databean);
                                    wrl =pageData.getRowCount();
                                    log.info("wrl:"+wrl);
                                    pageData = sgytSpxxProjectService.findYrlProjectVOPage(first, pageSize,
                                            databean);
                                    yrl =pageData.getRowCount();
                                    log.info("yrl:"+yrl);
                                }
                                if ("中心审管".equals(role.getRoleName())) {
                                    databean.setOuguid("");
                                    pageData = sgytSpxxProjectService.findProjectVOPageForCenter(first, pageSize,
                                            databean);
                                    sp =pageData.getRowCount();
                                    log.info("sp:"+sp);
                                    pageData = sgytSpxxProjectService.findWlrProjectVOPageForCenter(first, pageSize,
                                            databean);
                                    wrl =pageData.getRowCount();
                                    log.info("wrl:"+wrl);
                                    pageData = sgytSpxxProjectService.findYrlProjectVOPageForCenter(first, pageSize,
                                            databean);
                                    yrl =pageData.getRowCount();
                                    log.info("yrl:"+yrl);
                                }

                            }
                        }
                    }

                }
                dataJson.put("sp",sp);
                dataJson.put("wlr",wrl);
                dataJson.put("yrl",yrl);
                return JsonUtils.zwdtRestReturn("1", "获取数据成功",dataJson.toString());
            }else{
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getsgnum异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取中心审批办件数量
     *
     * @return
     */
    public int getSpxxCount(String handleareacode) {
        String sqlNum = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID where 1=1 ";

        sqlNum = sqlNum + " and p.handleareacode like '" + handleareacode + "%' ";
        sqlNum = sqlNum + " and p.areaCode = '" + handleareacode + "'";

        sqlNum = sqlNum + " and p.Banjieresult = 40 and p.status = 90 ";
        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
        }
        int xxcount = jnAuditJianGuanCenter.getSpxxCount(sqlNum);
        return xxcount;
    }

    /**
     * 中心获取已认领的办件数目
     *
     * @return
     */
    public int getYrl(String areaCode) {
        int num = 0;
        num = jnAuditJianGuanCenter.getYrl(areaCode);
        return num;

    }

    /**
     * 中心获取未认领的办件数目
     *
     * @return
     */
    public int getWrl(String areaCode) {
        int num = 0;
        num = jnAuditJianGuanCenter.getWrl(areaCode);
        return num;
    }


    /**
     * 获取列表数据
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getsglist", method = RequestMethod.POST)
    public String getsglist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getsglistn接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String access_token = obj.getString("access_token");
                log.info("access_token:"+access_token);
                //通过access_token 获取loginid
                String loginid="";
                String ssourl = ConfigUtil.getConfigValue("bigDataTokenUrl");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+access_token);
                String result = com.epoint.cert.commonutils.HttpUtil.doPost(ssourl + "/rest/oauth2/loginid", new HashMap<>(),headers);
                if (StringUtil.isNotBlank(result)) {
                    loginid =result;
                }
                log.info("loginid:"+loginid);
                //必填 1:审批信息 2:未认领 3:已认领
                String type = obj.getString("type");
                //开始记录
                Integer first = obj.getInteger("first");
                //每页记录数
                Integer pageSize = obj.getInteger("pageSize");
                //事项名字
                String name = obj.getString("name");
                //办件编号
                String flowsn = obj.getString("flowsn");
                //申请人名字
                String applyername = obj.getString("applyername");
                //办结开始时间
                Date banjiedateStart = obj.getDate("banjiedateStart");
                //办结结束时间
                Date banjiedateEnd = obj.getDate("banjiedateEnd");
                // 认领状态 全部传入空字符串  1:已认领 0:未认领
                String status = obj.getString("status");
                //认领人
                String renlinger = obj.getString("renlinger");
                //认领开始时间
                Date renlingdateStart = obj.getDate("renlingdateStart");
                //认领结束时间
                Date renlingdateEnd = obj.getDate("renlingdateEnd");
                //监管部门
                String jianguanbumen = obj.getString("jianguanbumen");
                if(StringUtils.isBlank(type)  || first==null || pageSize==null){
                    return JsonUtils.zwdtRestReturn("0", "type,pageSize 必须传！", "");
                }
                FrameUser user= iUserService.getUserByUserField("loginid",loginid);
                PageData<AuditProject> pageData = new PageData<>();
                JSONObject dataJson = new JSONObject();
                AuditProject databean = new AuditProject();
                if(StringUtils.isNotBlank(name)){
                    databean.setProjectname(name);
                }
                if(StringUtils.isNotBlank(flowsn)){
                    databean.setFlowsn(flowsn);
                }
                if(StringUtils.isNotBlank(applyername)){
                    databean.setApplyername(applyername);
                }
                if(banjiedateStart!=null){
                    databean.set("banjiedateStart",banjiedateStart);
                }
                if(banjiedateEnd!=null){
                    databean.set("banjiedateEnd",banjiedateEnd);
                }
                if(StringUtils.isNotBlank(status)){
                    databean.set("renlingtype",status);
                }
                if(StringUtils.isNotBlank(renlinger)){
                    databean.set("renling_username",renlinger);
                }
                if(renlingdateStart!=null){
                    databean.set("renlingdateStart",renlingdateStart);
                }
                if(renlingdateEnd!=null){
                    databean.set("renlingdateEnd",renlingdateEnd);
                }
                if(StringUtils.isNotBlank(jianguanbumen)){
                    databean.set("jg_ouname",jianguanbumen);
                }
                databean.setAreacode("370800");
                if(user!=null){
                    databean.setOuguid(user.getOuGuid());
                    databean.set("jg_userguid", user.getUserGuid());
                    List<String> roles = iRoleService.listRoleGuidByUserGuid(user.getUserGuid());
                    if(CollectionUtils.isNotEmpty(roles)){
                        for(String roleguid:roles){
                            FrameRole role = iRoleService.getRoleByRoleField("roleguid", roleguid);
                            if (role != null) {
                                if ("部门审管".equals(role.getRoleName())) {
                                    switch (type){
                                        case "1":
                                            pageData = sgytSpxxProjectService.findProjectVOPage(first, pageSize,
                                                    databean);
                                            break;
                                        case "2":
                                            pageData = sgytSpxxProjectService.findWlrProjectVOPage(first, pageSize,
                                                    databean);
                                            break;
                                        case "3":
                                            pageData = sgytSpxxProjectService.findYrlProjectVOPage(first, pageSize,
                                                    databean);
                                            break;
                                    }
                                }
                                if ("中心审管".equals(role.getRoleName())) {
                                    //兼容移动端接口
                                    databean.setOuguid("");
                                    switch (type){
                                        case "1":
                                            pageData = sgytSpxxProjectService.findProjectVOPageForCenter(first, pageSize,
                                                    databean);
                                            break;
                                        case "2":
                                            pageData = sgytSpxxProjectService.findWlrProjectVOPageForCenter(first, pageSize,
                                                    databean);
                                            break;
                                        case "3":
                                            pageData = sgytSpxxProjectService.findYrlProjectVOPageForCenter(first, pageSize,
                                                    databean);
                                            break;
                                    }
                                }

                            }
                        }
                    }

                }


                dataJson.put("count",pageData.getRowCount());
                dataJson.put("list",pageData.getList());
                log.info("=======结束调用getsglist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getsglist接口参数：params【" + params + "】=======");
            log.info("=======getsglist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败：" + e.getMessage(), "");
        }
    }


    /**
     * 认领
     *
     * @return
     */
    @RequestMapping(value = "/renling", method = RequestMethod.POST)
    public String renling(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用renling接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            RenlingService rlservice = new RenlingService();
            String token = jsonObject.getString("token");
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String rowguid = obj.getString("rowguid");
                String access_token = obj.getString("access_token");
                log.info("access_token:"+access_token);
                //通过access_token 获取loginid
                String loginid="";
                String ssourl = ConfigUtil.getConfigValue("bigDataTokenUrl");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+access_token);
                String result = com.epoint.cert.commonutils.HttpUtil.doPost(ssourl + "/rest/oauth2/loginid", new HashMap<>(),headers);
                if (StringUtil.isNotBlank(result)) {
                    loginid =result;
                }
                log.info("loginid:"+loginid);
                if(StringUtils.isBlank(rowguid)){
                    return JsonUtils.zwdtRestReturn("0", "rowguid为空！", "");
                }
                rlservice.renling(rowguid);
                // 新增认领记录表
                RenlingRecord renlingRecord = new RenlingRecord();
                renlingRecord.setRowguid(UUID.randomUUID().toString());
                renlingRecord.setProjectguid(rowguid);
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid("projectname,rowguid,task_id",rowguid,"370800").getResult();
                FrameUser user= iUserService.getUserByUserField("loginid",loginid);
                if (auditProject!=null) {
                    String ouname= "";
                    if(user!=null){
                        FrameOu frameOu = iOuService.getOuByOuGuid(user.getOuGuid());
                        ouname = frameOu.getOuname();
                    }
                    renlingRecord.setProjectname(auditProject.getProjectname());
                    renlingRecord.setRenlingtime(new Date());
                    renlingRecord.setOuguid(user.getUserGuid());
                    renlingRecord.setOuname(ouname);
                    renlingRecord.setUserguid(user.getUserGuid());
                    renlingRecord.setUsername(user.getDisplayName());
                    iRenlingRecordService.insert(renlingRecord);

                    AuditTaskJnRenling auditTaskJnRenling = new AuditTaskJnRenling();
                    auditTaskJnRenling.setOperatedate(new Date());
                    auditTaskJnRenling.setRowguid(UUID.randomUUID().toString());
                    auditTaskJnRenling.setTask_id(auditProject.getTask_id());
                    auditTaskJnRenling.setProjectguid(auditProject.getRowguid());
                    auditTaskJnRenling.setRenlingdate(new Date());
                    auditTaskJnRenling.setOperateusername(user.getDisplayName());
                    auditTaskJnRenling.setRenling_ouguid(user.getOuGuid());
                    auditTaskJnRenling.setRenling_ouname(ouname);
                    auditTaskJnRenling.setRenling_userguid(user.getUserGuid());
                    auditTaskJnRenling.setRenling_username(user.getDisplayName());
                    auditTaskJnRenlingService.insert(auditTaskJnRenling);
                }else{
                    return JsonUtils.zwdtRestReturn("0", "rowguid不正确！", "");
                }

                return JsonUtils.zwdtRestReturn("1", "认领成功",dataJson.toString());
            }else{
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======renling异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败：" + e.getMessage(), "");
        }
    }


}
