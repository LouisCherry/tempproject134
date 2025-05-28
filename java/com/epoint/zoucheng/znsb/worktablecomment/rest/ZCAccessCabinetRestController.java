package com.epoint.zoucheng.znsb.worktablecomment.rest;

import java.lang.invoke.MethodHandles;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditqueue.auditznsbaccessbox.domain.AuditZnsbAccessbox;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.domain.AuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.inter.IAuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbaccessmaterial.domain.AuditZnsbAccessMaterial;
import com.epoint.basic.auditqueue.auditznsbaccessmaterial.inter.IAuditZnsbAccessMaterial;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.znsb.util.HttpUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;
import com.epoint.zoucheng.znsb.auditznsbaccessbox.inter.IZCAuditZnsbAccessbox;
import com.epoint.zoucheng.znsb.auditznsbaccessmaterial.inter.IZCAuditZnsbAccessMaterial;
import com.epoint.zoucheng.znsb.worktablecomment.util.QueueConstant;

@SuppressWarnings("deprecation")
@RestController
@RequestMapping("/zcselfserviceaccesscabinet")
public class ZCAccessCabinetRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditZnsbAccessMaterial accessMaterialService;
    @Autowired
    private IZCAuditZnsbAccessMaterial zcaccessMaterialService;
    @Autowired
    private IAuditZnsbAccesscabinet accesscabinetService;
    @Autowired
    private IZCAuditZnsbAccessbox accessboxService;
    @Autowired
    private IMessagesCenterService messageservice;
    @Autowired
    private IHandleFlowSn flowsnservice;

    @Autowired
    private IUserService userservice;

    @Autowired
    private IAuditProject projectservice;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleRelationService roleRelationService;
    @Autowired
    private IMessagesCenterService messageCenterService;

    @Autowired
    private IAuditTask taskservice;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IAuditProjectMaterial projectMaterialservice;
    @Autowired
    private IAuditTaskMaterial taskMaterialservice;

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IHandleConfig handleConfigservice;

    // String cabinetIpAddress = ConfigUtil.getConfigValue("epointframe",
    // "cabinetIpAddress");

    // 获取列表 --->管理员存证照，获取所有未取件的,未关联柜子的
    @RequestMapping(value = "/getCertListIn", method = RequestMethod.POST)
    public String getCertListIN(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String currentpage = obj.getString("currentpage");
            String pagesize = obj.getString("pagesize");

            String handlerguid = obj.getString("handlerguid");

            String materialname = obj.getString("materialname");

            JSONObject dataJson = new JSONObject();

            SqlConditionUtil sql = new SqlConditionUtil();

            // 传入的是处理者的guid----->管理员存证照
            sql.eq("handleuserguid", handlerguid);
            sql.eq("materialtype", QueueConstant.FILE_CERTFILE);

            // 未关联的柜子的
            sql.isBlank("cabinetguid");
            // 未取件的
            sql.eq("isget", QueueConstant.CONSTANT_STR_ZERO);
            // 证件名
            if (StringUtil.isNotBlank(materialname)) {
                sql.like("materialname", materialname);
            }
            PageData<AuditZnsbAccessMaterial> accessMaterialPage = accessMaterialService
                    .getAccessMaterialPageData(sql.getMap(), Integer.parseInt(currentpage) * Integer.parseInt(pagesize),
                            Integer.parseInt(pagesize), "storetime", "desc")
                    .getResult();
            List<AuditZnsbAccessMaterial> accessMaterials = accessMaterialPage.getList();

            List<JSONObject> AccessMaterialJson = new ArrayList<JSONObject>();

            if (accessMaterials != null && !accessMaterials.isEmpty()) {
                for (AuditZnsbAccessMaterial accessMaterial : accessMaterials) {
                    JSONObject data = new JSONObject();
                    data.put("accessmaterialguid", accessMaterial.getRowguid());
                    data.put("applyername", accessMaterial.getApplyerName());
                    data.put("applyermobile", accessMaterial.getApplyerMobile());
                    data.put("materialname", accessMaterial.getMaterialName());
                    data.put("storetime",
                            EpointDateUtil.convertDate2String(accessMaterial.getStoreTime(), "yyyy-MM-dd"));
                    AccessMaterialJson.add(data);
                }
            }
            dataJson.put("totalcount", accessMaterialPage.getRowCount());
            dataJson.put("accessmateriallist", AccessMaterialJson);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info("=======结束调用initCloudDisk接口=======");
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    // 获取列表 --->管理员取申报材料，获取所有未取件的，已关联柜子的
    @RequestMapping(value = "/getCertListOut", method = RequestMethod.POST)
    public String getCertListOUT(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String currentpage = obj.getString("currentpage");
            String pagesize = obj.getString("pagesize");
            String sortfield = obj.getString("sortfield");
            String sortorder = obj.getString("sortorder");

            String handlerguid = obj.getString("handlerguid");

            String materialname = obj.getString("materialname");

            JSONObject dataJson = new JSONObject();

            SqlConditionUtil sql = new SqlConditionUtil();

            // 传入的是处理者的guid----->管理员取申报材料
            sql.eq("handleuserguid", handlerguid);
            sql.eq("materialtype", QueueConstant.FILE_APPLYFILE);

            // 关联的柜子的
            sql.isNotBlank("cabinetguid");
            // 未取件的
            sql.eq("isget", QueueConstant.CONSTANT_STR_ZERO);
            // 证件名
            if (StringUtil.isNotBlank(materialname)) {
                sql.like("materialname", materialname);
            }
            PageData<AuditZnsbAccessMaterial> accessMaterialPage = accessMaterialService
                    .getAccessMaterialPageData(sql.getMap(), Integer.parseInt(currentpage) * Integer.parseInt(pagesize),
                            Integer.parseInt(pagesize), sortfield, sortorder)
                    .getResult();
            List<AuditZnsbAccessMaterial> accessMaterials = accessMaterialPage.getList();

            List<JSONObject> accessMaterialJson = new ArrayList<JSONObject>();
            if (accessMaterials != null && !accessMaterials.isEmpty()) {
                for (AuditZnsbAccessMaterial accessMaterial : accessMaterials) {
                    JSONObject data = new JSONObject();
                    data.put("accessmaterialguid", accessMaterial.getRowguid());
                    data.put("applyername", accessMaterial.getApplyerName());
                    data.put("applyermobile", accessMaterial.getApplyerMobile());
                    data.put("materialname", accessMaterial.getMaterialName());
                    data.put("accessboxguid", accessMaterial.getBoxGuid());
                    data.put("storetime",
                            EpointDateUtil.convertDate2String(accessMaterial.getStoreTime(), "yyyy-MM-dd"));
                    accessMaterialJson.add(data);
                }
            }
            dataJson.put("accessmateriallist", accessMaterialJson);
            dataJson.put("totalcount", accessMaterialPage.getRowCount());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    // 获取列表 --->用户取证照，获取所有未取件的，已关联柜子的
    @RequestMapping(value = "/getCertListOutUser", method = RequestMethod.POST)
    public String getCertListOUTUser(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String currentpage = obj.getString("currentpage");
            String pagesize = obj.getString("pagesize");

            String userCertno = obj.getString("usercertno");

            String materialname = obj.getString("materialname");

            JSONObject dataJson = new JSONObject();

            SqlConditionUtil sql = new SqlConditionUtil();
            // 传入的是用户的证件号----->用户取证照
            sql.eq("applyercertno", userCertno);
            sql.eq("materialtype", QueueConstant.FILE_CERTFILE);

            // 关联的柜子的
            sql.isNotBlank("cabinetguid");
            // 未取件的
            sql.eq("isget", QueueConstant.CONSTANT_STR_ZERO);
            // 证件名
            if (StringUtil.isNotBlank(materialname)) {
                sql.like("materialname", materialname);
            }

            PageData<AuditZnsbAccessMaterial> accessMaterialPage = accessMaterialService
                    .getAccessMaterialPageData(sql.getMap(), Integer.parseInt(currentpage) * Integer.parseInt(pagesize),
                            Integer.parseInt(pagesize), "storetime", "desc")
                    .getResult();
            List<AuditZnsbAccessMaterial> accessMaterials = accessMaterialPage.getList();
            List<JSONObject> accessMaterialJson = new ArrayList<JSONObject>();
            if (accessMaterials != null && !accessMaterials.isEmpty()) {
                for (AuditZnsbAccessMaterial accessMaterial : accessMaterials) {
                    JSONObject data = new JSONObject();
                    data.put("accessmaterialguid", accessMaterial.getRowguid());
                    data.put("applyername", accessMaterial.getApplyerName());
                    data.put("applyermobile", accessMaterial.getApplyerMobile());
                    data.put("materialname", accessMaterial.getMaterialName());
                    data.put("accessboxguid", accessMaterial.getBoxGuid());
                    data.put("storetime",
                            EpointDateUtil.convertDate2String(accessMaterial.getStoreTime(), "yyyy-MM-dd"));
                    accessMaterialJson.add(data);
                }
            }
            dataJson.put("accessmateriallist", accessMaterialJson);
            dataJson.put("totalcount", accessMaterialPage.getRowCount());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    // 存放--->获取空盒子--->获取状态正常的，没有放东西的盒子，分配一个
    @RequestMapping(value = "/getEmptyBox", method = RequestMethod.POST)
    public String getEmptyBox(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String cabinetguid = obj.getString("cabinetguid");
            // String accessMaterialguid = obj.getString("accessmaterialguid");

            JSONObject dataJson = new JSONObject();

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("cabinetguid", cabinetguid);
            sql.eq("boxstatus", QueueConstant.CABINETBOX_NO_FILE);
            // 按盒子编号的顺序取出
            sql.setOrderAsc("boxno");

            // 存取柜最多48个盒子。
            List<AuditZnsbAccessbox> accessboxs = accessboxService
                    .getBoxList(sql.getMap(), "rowguid,boxno,abscissa,ordinate").getResult();
            if (accessboxs != null && !accessboxs.isEmpty()) {
                AuditZnsbAccessbox accessbox = accessboxs.get(0);
                dataJson.put("accessboxguid", accessbox.getRowguid());
                dataJson.put("accessboxno", accessbox.getBoxno());
                dataJson.put("abscissa", accessbox.getAbscissa());
                dataJson.put("ordinate", accessbox.getOrdinate());
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该取件柜盒子已满！", dataJson);
            }

            // AuditZnsbAccessMaterial accessMaterial =
            // accessMaterialService.getDetailByGuid(accessMaterialguid).getResult();

            // dataJson.put("AccessMaterialguid", AccessMaterial.getRowguid());
            // dataJson.put("applyermobile",accessMaterial.getApplyerMobile());
            // dataJson.put("applyername",accessMaterial.getApplyerName());
            // dataJson.put("materialname",accessMaterial.getMaterialName());

        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    // 取东西--->获取指定盒子
    @RequestMapping(value = "/getTheBox", method = RequestMethod.POST)
    public String getTheBox(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String accessboxguid = obj.getString("accessboxguid");

            JSONObject dataJson = new JSONObject();
            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessboxguid).getResult();

            dataJson.put("accessboxguid", accessbox.getRowguid());
            dataJson.put("accessboxno", accessbox.getBoxno());
            dataJson.put("abscissa", accessbox.getAbscissa());
            dataJson.put("ordinate", accessbox.getOrdinate());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    // 放好东西后，更新状态,发送提醒
    @RequestMapping(value = "/updateStatusAfterPut", method = RequestMethod.POST)
    public String updateStatusAfterPut(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String accesscabinetno = obj.getString("accesscabinetno");
            String accessboxguid = obj.getString("accessboxguid");
            String AccessMaterialguid = obj.getString("accessmaterialguid");

            JSONObject dataJson = new JSONObject();

            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessboxguid).getResult();
            AuditZnsbAccessMaterial accessMaterial = accessMaterialService.getDetailByGuid(AccessMaterialguid)
                    .getResult();
            // AuditZnsbAccesscabinet accesscabinet =
            // accesscabinetService.getDetailByGuid(cabinetguid).getResult();
            accessbox.setBoxstatus(QueueConstant.CABINETBOX_HAVE_FILE); // 更新状态->有单
            accessMaterial.setBoxGuid(accessboxguid);
            accessMaterial.setCabinetGuid(accessbox.getCabinetguid());
            // 放的是相关证照---->发送短信
            if (QueueConstant.FILE_CERTFILE.equals(accessMaterial.getMaterialType())) {
                // 生成唯一的随机6位数字
                Boolean isUnique = false;
                int pickupcode = 100000;
                while (!isUnique) {
                    try {
                        SecureRandom rand = SecureRandom.getInstanceStrong();
                        pickupcode = 100000 + rand.nextInt(900000);
                    }
                    catch (NoSuchAlgorithmException e) {
                        log.info(e);
                    }

                    isUnique = accessMaterialService.isUnique("" + pickupcode).getResult();
                }
                accessMaterial.setCode(pickupcode + "");
                // 发送短信
                if (StringUtil.isNotBlank(accessMaterial.getApplyerMobile())) {
                    messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                            "您办理的相关证照：" + accessMaterial.getMaterialName() + "，已由工作人员放入存取柜," + "存取柜编号："
                                    + accesscabinetno + "，取件码为：" + pickupcode,
                            new Date(), 0, new Date(), accessMaterial.getApplyerMobile(), UUID.randomUUID().toString(),
                            "", "", "", "", "", "", false, "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "申请人手机号为空，请检查数据！", "");
                }

            }
            else {
                // 生成唯一的随机6位数字
                Boolean isUnique = false;
                int pickupcode = 100000;
                while (!isUnique) {
                    try {
                        SecureRandom rand = SecureRandom.getInstanceStrong();
                        pickupcode = 100000 + rand.nextInt(900000);
                    }
                    catch (NoSuchAlgorithmException e) {
                        log.info(e);
                    }
                    isUnique = accessMaterialService.isUnique("" + pickupcode).getResult();
                }
                accessMaterial.setCode(pickupcode + "");

                // 放的是申报材料---->对办件部门下的存取柜管理员发送代表事宜
                String guid = UUID.randomUUID().toString();
                // 1、获取角色标识
                FrameRole frameRole = roleService.getRoleByRoleField("rolename", "存取柜管理员");
                if (frameRole != null) {
                    String roleguid = frameRole.getRoleGuid();
                    // 2、获取该角色的对应的人员
                    List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                            .getRelationListByField("roleGuid", roleguid, null, null);
                    if (frameuserrolerelationlist != null && !frameuserrolerelationlist.isEmpty()) {
                        // 3、发送待办给审核人员
                        for (FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                            FrameUser user = userservice.getUserByUserField("userguid",
                                    frameUserRoleRelation.getUserGuid());
                            if (accessMaterial.getProjectouguid().equals(user.getOuGuid())) {
                                String targetusername = userservice
                                        .getUserNameByUserGuid(frameUserRoleRelation.getUserGuid());
                                String title = "";
                                String handleUrl = "";
                                // String item = "";
                                // 待办名称
                                title = "【待办】 存取柜取件" + ",存取柜编号" + accesscabinetno + ",取件码为：" + pickupcode;
                                // 处理页面
                                handleUrl = "znsb/equipmentdisplay/selfservicemachine/accesscabinet/getcabinetinfo?guid="
                                        + AccessMaterialguid + "&msgguid=" + guid;

                                String messageItemGuid = UUID.randomUUID().toString();
                                /*
                                 * messageCenterService.insertWaitHandleMessage(
                                 * messageItemGuid, title,
                                 * IMessagesCenterService.MESSAGETYPE_READ,
                                 * frameUserRoleRelation.getUserGuid(),
                                 * targetusername,
                                 * frameUserRoleRelation.getUserGuid(),
                                 * userservice.getUserNameByUserGuid(
                                 * frameUserRoleRelation.getUserGuid()), "",
                                 * handleUrl,
                                 * frameUserRoleRelation.getUserGuid(), "",
                                 * ZwfwConstant.CONSTANT_INT_ONE, "", "", guid,
                                 * messageItemGuid.substring(0, 1),
                                 * new Date(),
                                 * frameUserRoleRelation.getUserGuid(),
                                 * frameUserRoleRelation.getUserGuid(), "", "");
                                 * break;
                                 */

                            }

                        }
                        dataJson.put("msg", guid);
                    }
                    else {
                        dataJson.put("msg", "请先确认系统是否存在'远程协助'角色人员！");
                    }
                }
                else {
                    dataJson.put("msg", "请先确认系统是否存在'远程协助'角色！");
                }
            }

            // 更新数据
            accessboxService.update(accessbox);
            accessMaterialService.update(accessMaterial);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 取了东西后，更新状态
    @RequestMapping(value = "/updateStatusAfterGet", method = RequestMethod.POST)
    public String updateStatusAfterGet(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String accessboxguid = obj.getString("accessboxguid");
            String accessMaterialguid = obj.getString("accessmaterialguid");

            JSONObject dataJson = new JSONObject();

            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessboxguid).getResult();
            AuditZnsbAccessMaterial accessMaterial = accessMaterialService.getDetailByGuid(accessMaterialguid)
                    .getResult();

            accessbox.setBoxstatus(QueueConstant.CABINETBOX_NO_FILE); // 更新状态->无单
            accessMaterial.setIsGet(QueueConstant.CONSTANT_STR_ONE); // 已取件
            accessMaterial.setTakeTime(new Date());

            accessboxService.update(accessbox);
            accessMaterialService.update(accessMaterial);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 根据取件码获取对应的盒子
    @RequestMapping(value = "/getBoxByCode", method = RequestMethod.POST)
    public String getBoxByCode(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String pickupCode = obj.getString("pickupcode");

            JSONObject dataJson = new JSONObject();

            AuditZnsbAccessMaterial accessMaterial = accessMaterialService.getAccessMaterialByCode(pickupCode)
                    .getResult();
            if (StringUtil.isBlank(accessMaterial)) {
                return JsonUtils.zwdtRestReturn("0", "请检查取件码是否正确！", "");
            }
            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessMaterial.getBoxGuid()).getResult();

            dataJson.put("accessmaterialguid", accessMaterial.getRowguid());
            dataJson.put("accessboxguid", accessbox.getRowguid());
            dataJson.put("abscissa", accessbox.getAbscissa());
            dataJson.put("ordinate", accessbox.getOrdinate());
            dataJson.put("accessboxno", accessbox.getBoxno());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 后台管理人员登录
    @RequestMapping(value = "/adminlogin", method = RequestMethod.POST)
    public String adminLogin(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String loginid = obj.getString("loginid");
            String password = obj.getString("password");

            JSONObject dataJson = new JSONObject();

            FrameUser user = userservice.getUserByUserField("LoginID", loginid);

            if (user != null) {
                if (userservice.checkPassword(loginid, password)) {
                    dataJson.put("handlerguid", user.getUserGuid());
                    dataJson.put("handlername", user.getDisplayName());
                    dataJson.put("msg", "success");
                }
                else {
                    dataJson.put("msg", "登陆失败，密码错误！");
                }
            }
            else {
                dataJson.put("msg", "登陆失败，用户不存在！");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 上报异常
    @RequestMapping(value = "/abnormalSub", method = RequestMethod.POST)
    public String abnomalSub(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String accessboxguid = obj.getString("accessboxguid");

            JSONObject dataJson = new JSONObject();
            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessboxguid).getResult();
            if (StringUtil.isNotBlank(accessbox)) {
                accessbox.setBoxstatus(QueueConstant.CABINETBOX_ABNORMAL);
                accessboxService.update(accessbox);
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 生成办件相关的存取材料记录
    @RequestMapping(value = "/creatematerialofproject", method = RequestMethod.POST)
    public String createMaterialOfProject(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String appylyername = obj.getString("appylyername");
            String applyercertno = obj.getString("applyercertno");
            String applyermobile = obj.getString("applyermobile");
            String projectguid = obj.getString("projectguid");// 办件guid
            String areacode = obj.getString("areacode");

            AuditZnsbAccessMaterial auditZnsbAccessMaterial = new AuditZnsbAccessMaterial();
            auditZnsbAccessMaterial.setRowguid(UUID.randomUUID().toString());
            auditZnsbAccessMaterial.setOperatedate(new Date());
            auditZnsbAccessMaterial.setFlowsn(flowsnservice.getFlowsn("智能设备存取材料流水号", "").getResult());
            auditZnsbAccessMaterial.setMaterialName("自助申报提交材料");
            auditZnsbAccessMaterial.setApplyerName(appylyername);
            auditZnsbAccessMaterial.setApplyerCertNo(applyercertno);
            auditZnsbAccessMaterial.setApplyerMobile(applyermobile);
            auditZnsbAccessMaterial.setStoreTime(new Date());
            auditZnsbAccessMaterial.setIsGet(QueueConstant.CONSTANT_STR_ZERO);
            auditZnsbAccessMaterial.setMaterialType(QueueConstant.MaterialType.申报材料.value);
            auditZnsbAccessMaterial.setProjectguid(projectguid);
            AuditProject project = projectservice.getAuditProjectByRowGuid(projectguid, areacode).getResult();
            auditZnsbAccessMaterial.setProjectouguid(StringUtil.isNotBlank(project) ? project.getOuguid() : "");

            accessMaterialService.insert(auditZnsbAccessMaterial);
            JSONObject dataJson = new JSONObject();
            dataJson.put("accessmaterialguid", auditZnsbAccessMaterial.getRowguid());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /****************************** 以下为新版存取柜代码 **********************************/
    // 生成存件记录
    @RequestMapping(value = "/createSaveLog", method = RequestMethod.POST)
    public String createSaveLog(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String projectguid = obj.getString("projectguid");// 办件guid
            String type = obj.getString("type");// 存件类型 0申请 1补正 2结果领取
            String status = obj.getString("status");// 状态 0待入柜 1已入柜 2已取出
            String usercard = obj.getString("usercard");// 办事人身份证
            String username = obj.getString("username");// 办事人姓名
            String userphone = obj.getString("userphone");// 办事人电话
            String areacode = obj.getString("areacode");// 辖区参数
            String taskguid = obj.getString("taskguid");// 辖区参数
            String projectname = obj.getString("projectname");// 办件名称

            AuditZnsbAccessMaterial auditZnsbAccessMaterial = new AuditZnsbAccessMaterial();
            auditZnsbAccessMaterial.setRowguid(UUID.randomUUID().toString());
            auditZnsbAccessMaterial.setOperatedate(new Date());
            auditZnsbAccessMaterial.setFlowsn(flowsnservice.getFlowsn("智能设备存取材料流水号", "").getResult());
            auditZnsbAccessMaterial.setMaterialName("自助申报提交材料");
            auditZnsbAccessMaterial.setApplyerName(username);
            auditZnsbAccessMaterial.setApplyerCertNo(usercard);
            auditZnsbAccessMaterial.setApplyerMobile(userphone);
            auditZnsbAccessMaterial.set("projectname", projectname);
            auditZnsbAccessMaterial.setBelongxiaqucode(areacode);
            // auditZnsbAccessMaterial.setStoreTime();
            // auditZnsbAccessMaterial.setIsGet(QueueConstant.CONSTANT_STR_ZERO);
            auditZnsbAccessMaterial.setMaterialType(QueueConstant.MaterialType.申报材料.value);
            auditZnsbAccessMaterial.setProjectguid(projectguid);
            AuditProject project = projectservice.getAuditProjectByRowGuid("OUGUID", projectguid, areacode).getResult();
            auditZnsbAccessMaterial.setProjectouguid(StringUtil.isNotBlank(project) ? project.getOuguid() : "");

            auditZnsbAccessMaterial.set("type", type);
            auditZnsbAccessMaterial.set("taskguid", taskguid);
            auditZnsbAccessMaterial.set("status", status);

            accessMaterialService.insert(auditZnsbAccessMaterial);
            JSONObject dataJson = new JSONObject();
            dataJson.put("accessmaterialguid", auditZnsbAccessMaterial.getRowguid());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 判断当前中心是否有空柜子
    @RequestMapping(value = "/checkEmpty", method = RequestMethod.POST)
    public String checkEmpty(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String centerguid = obj.getString("centerguid");// 中心guid
            int count = 0;
            if (StringUtil.isNotBlank(centerguid)) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("centerguid", centerguid);
                sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_CABINET);
                sql.eq("status", QueueConstant.EQUIPMENT_STATUS_ONLINE);
                List<AuditZnsbEquipment> equipmentlist = equipmentservice.getEquipmentList(sql.getMap()).getResult();
                for (AuditZnsbEquipment equipment : equipmentlist) {
                    AuditZnsbAccesscabinet cabinet = accesscabinetService
                            .getDetailByEquipmentMac(equipment.getMacaddress()).getResult();
                    if (StringUtil.isNotBlank(cabinet)) {
                        sql.clear();
                        sql.eq("cabinetguid", cabinet.getRowguid());
                        sql.eq("boxstatus", QueueConstant.CABINETBOX_NO_FILE);
                        // 按盒子编号的顺序取出
                        sql.setOrderAsc("boxno");

                        // 存取柜最多24个盒子。
                        List<AuditZnsbAccessbox> accessboxs = accessboxService
                                .getBoxList(sql.getMap(), "rowguid,boxno,abscissa,ordinate").getResult();
                        if (accessboxs != null && !accessboxs.isEmpty()) {
                            count++;// 如果有空盒子表示可以存入计数加一。
                        }
                    }
                    else {
                        log.info("macaddress:" + equipment.getMacaddress() + ",设备未绑定相关智能存取柜");
                    }
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "中心参数未传", "");
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("count", count);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 查询当前sfz是否有可取出、可存入信息
    @RequestMapping(value = "/getsaveortakelist", method = RequestMethod.POST)
    public String getSaveOrTakeList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String status = obj.getString("status");// 状态 0待入柜 1已入柜 2已取出
            String usercard = obj.getString("usercard");// 办事人身份证

            JSONObject dataJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("status", status);
            sql.eq("ApplyerCertNo", usercard);
            if ("0".equals(status)) {
                sql.nq("type", "2");
            }
            else {
                sql.eq("type", "2");
            }
            List<AuditZnsbAccessMaterial> materiallist = zcaccessMaterialService
                    .getAccessMaterialList(sql.getMap(), "rowguid").getResult();
            if (materiallist.size() > 0) {
                dataJson.put("iscontinue", "1");
            }
            else {
                dataJson.put("iscontinue", "0");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 分页查询存件list
    @RequestMapping(value = "/getmaterialpagedate", method = RequestMethod.POST)
    public String getMaterialPageDate(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String status = obj.getString("status");// 状态 0待入柜 1已入柜 2已取出
            String usercard = obj.getString("usercard");// 办事人身份证
            String type = obj.getString("type");// 类型 0申请 1补正 2结果
            String userguid = obj.getString("userguid");// 管理员guid
            String firstpage = obj.getString("firstpage");// 起始页码
            String pagesize = obj.getString("pagesize");// 每页数量
            String cabinetguid = obj.getString("cabinetguid");// 柜子guid

            JSONObject dataJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("status", status);// 存件状态

            if (StringUtil.isNotBlank(usercard)) {
                sql.eq("ApplyerCertNo", usercard);// 身份证

                if ("01".equals(type)) {
                    sql.nq("type", "2");
                }
                else {
                    sql.eq("type", "2");
                }
            }

            if (StringUtil.isNotBlank(cabinetguid)) {
                sql.eq("cabinetguid", cabinetguid);// 身份证
            }

            if ("2".equals(type)) {
                sql.eq("type", "2");
            }
            sql.setSelectFields(
                    "rowguid,OperateDate,taskguid,type,status,storetime,boxguid,projectouguid,ApplyerName,ApplyerCertNo,projectname");// 查询字段
            PageData<AuditZnsbAccessMaterial> materialpage = accessMaterialService
                    .getAccessMaterialPageData(sql.getMap(), Integer.parseInt(firstpage) * Integer.parseInt(pagesize),
                            Integer.parseInt(pagesize), "operatedate", "desc")
                    .getResult();
            List<AuditZnsbAccessMaterial> materiallist = materialpage.getList();
            for (AuditZnsbAccessMaterial accessmaterial : materiallist) {
                AuditTask task = taskservice.selectTaskByRowGuid(accessmaterial.getStr("taskguid")).getResult();
                if (StringUtil.isNotBlank(accessmaterial.getStr("projectname"))) {
                    accessmaterial.set("taskname", accessmaterial.getStr("projectname"));
                }
                else {
                    if (StringUtil.isNotBlank(task)) {
                        accessmaterial.set("taskname", task.getTaskname());
                    }
                    else {
                        accessmaterial.set("taskname", "暂未查到此事项");
                    }
                }
                AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessmaterial.getBoxGuid())
                        .getResult();
                if (StringUtil.isNotBlank(accessbox)) {
                    accessmaterial.set("boxno", accessbox.getBoxno());
                }
                if ("0".equals(accessmaterial.getStr("type")) || "3".equals(accessmaterial.getStr("type"))) {
                    accessmaterial.set("materialtype", "申请中");
                }
                else if ("1".equals(accessmaterial.getStr("type"))) {
                    accessmaterial.set("materialtype", "待补正");
                }
                else {
                    accessmaterial.set("materialtype", "结果领取");
                }

                if (StringUtil.isNotBlank(accessmaterial.getProjectouguid())) {
                    FrameOu ou = ouservice.getOuByOuGuid(accessmaterial.getProjectouguid());
                    if (StringUtil.isNotBlank(ou)) {
                        accessmaterial.set("ouname", ou.getOuname());
                    }
                    else {
                        accessmaterial.set("ouname", "");
                    }
                }
                else {
                    accessmaterial.set("ouname", "");
                }
                accessmaterial.set("operationdate", EpointDateUtil.convertDate2String(accessmaterial.getOperatedate(),
                        EpointDateUtil.DATE_TIME_FORMAT));
            }
            dataJson.put("materiallist", materiallist);
            dataJson.put("totalcount", materialpage.getRowCount());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 存放--->获取空盒子--->获取状态正常的，没有放东西的盒子，分配一个
    @RequestMapping(value = "/getCabinetEmptyBox", method = RequestMethod.POST)
    public String getCabinetEmptyBox(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String cabinetguid = obj.getString("cabinetguid");
            // String accessMaterialguid = obj.getString("accessmaterialguid");

            JSONObject dataJson = new JSONObject();

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("cabinetguid", cabinetguid);
            sql.eq("boxstatus", QueueConstant.CABINETBOX_NO_FILE);
            // 按盒子编号的顺序取出
            sql.setOrderAsc("boxno");

            // 存取柜最多48个盒子。
            List<AuditZnsbAccessbox> accessboxs = accessboxService
                    .getBoxList(sql.getMap(), "rowguid,boxno,abscissa,ordinate").getResult();
            if (accessboxs != null && !accessboxs.isEmpty()) {
                AuditZnsbAccessbox accessbox = accessboxs.get(0);
                dataJson.put("accessboxguid", accessbox.getRowguid());
                dataJson.put("accessboxno", accessbox.getBoxno());
                dataJson.put("abscissa", accessbox.getAbscissa());
                dataJson.put("ordinate", accessbox.getOrdinate());
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该取件柜盒子已满！", dataJson);
            }

        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    // 展示材料详情
    @RequestMapping(value = "/getMaterialDetail", method = RequestMethod.POST)
    public String getMaterialDetail(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String rowguid = obj.getString("rowguid");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            String zwfwurl = handleConfigservice.getFrameConfig("AS_CABINET_ZWFW_URL", centerguid).getResult();

            AuditZnsbAccessMaterial material = accessMaterialService.getDetailByGuid(rowguid).getResult();

            if (StringUtil.isNotBlank(material)) {
                String returnjson = null;
                List<AuditProjectMaterial> ProjectMateriallist = null;
                List<JSONObject> projectJsonlist = new ArrayList<JSONObject>();
                if ("3".equals(material.getStr("type"))) {

                    // 获取办件材料
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("projectguid", material.getProjectguid());
                    sql.eq("auditstatus", ZwfwConstant.Material_AuditStatus_WTJ);
                    ProjectMateriallist = projectMaterialservice.selectProjectMaterialByCondition(sql.getMap())
                            .getResult();

                }
                else if (StringUtil.isNotBlank(zwfwurl)) {
                    JSONObject postjson = new JSONObject();
                    JSONObject paramsjson = new JSONObject();
                    paramsjson.put("projectguid", material.getProjectguid());
                    paramsjson.put("type", material.getStr("type"));
                    postjson.put("token", "Epoint_WebSerivce_**##0601");
                    postjson.put("params", paramsjson);
                    // 向监控接口请求图片,连接超时时长10秒，读取超时时长10秒
                    returnjson = HttpUtil.sendCommonPost(zwfwurl + "cabinet/materialShow", postjson);
                    log.info(postjson);
                    log.info(returnjson);
                }

                if (StringUtil.isNotBlank(material.getStr("taskguid"))) {
                    AuditTask task = taskservice.selectTaskByRowGuid(material.getStr("taskguid")).getResult();
                    if (StringUtil.isNotBlank(material.getStr("projectname"))) {
                        dataJson.put("taskname", material.getStr("projectname"));
                    }
                    else {
                        if (StringUtil.isNotBlank(task)) {
                            dataJson.put("taskname", task.getTaskname());
                        }
                        else {
                            dataJson.put("taskname", "暂未查到此事项");
                        }
                    }

                    if (StringUtil.isNotBlank(returnjson)) {
                        JSONObject backjson = JSON.parseObject(returnjson);
                        JSONArray materialarray = backjson.getJSONObject("custom").getJSONArray("materiallist");
                        dataJson.put("materiallist", materialarray);
                    }
                    else if (StringUtil.isNotBlank(ProjectMateriallist)) {
                        for (AuditProjectMaterial auditProjectMaterial : ProjectMateriallist) {
                            AuditTaskMaterial auditTaskMaterial = taskMaterialservice
                                    .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                                    .getResult();
                            log.info(auditTaskMaterial);
                            if (StringUtil.isNotBlank(auditTaskMaterial)) {
                                JSONObject materialJson = new JSONObject();
                                materialJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 材料名称
                                projectJsonlist.add(materialJson);
                            }
                        }
                        dataJson.put("materiallist", projectJsonlist);
                    }
                    else {
                        dataJson.put("materiallist", "[]");
                    }
                    return JsonUtils.zwdtRestReturn("1", "", dataJson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "信息异常", dataJson);
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "信息异常", dataJson);
            }
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    // 放好东西后，更新状态,发送提醒
    @RequestMapping(value = "/updateSaveStatus", method = RequestMethod.POST)
    public String updateSaveStatus(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String accesscabinetno = obj.getString("accesscabinetno");
            String accessboxguid = obj.getString("accessboxguid");
            String AccessMaterialguid = obj.getString("accessmaterialguid");
            String cabinetguid = obj.getString("cabinetguid");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            String zwfwurl = handleConfigservice.getFrameConfig("AS_CABINET_ZWFW_URL", centerguid).getResult();
            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessboxguid).getResult();
            AuditZnsbAccessMaterial accessMaterial = accessMaterialService.getDetailByGuid(AccessMaterialguid)
                    .getResult();
            AuditZnsbAccesscabinet accesscabinet = accesscabinetService.getDetailByGuid(cabinetguid).getResult();
            int pickupcode = 100000;

            accessbox.setBoxstatus(QueueConstant.CABINETBOX_HAVE_FILE); // 更新状态->有单
            accessMaterial.setStoreTime(new Date());
            accessMaterial.setBoxGuid(accessboxguid);
            accessMaterial.setCabinetGuid(accessbox.getCabinetguid());
            accessMaterial.set("status", "1");
            // 放的是相关证照---->发送短信
            if (QueueConstant.FILE_CERTFILE.equals(accessMaterial.getMaterialType())
                    || "2".equals(accessMaterial.getStr("type"))) {
                //system.out.println("存入");
                // 生成唯一的随机6位数字
                Boolean isUnique = false;
                pickupcode = 100000;
                while (!isUnique) {
                    try {
                        SecureRandom rand = SecureRandom.getInstanceStrong();
                        pickupcode = 100000 + rand.nextInt(900000);
                    }
                    catch (NoSuchAlgorithmException e) {
                        log.info(e);
                    }

                    isUnique = accessMaterialService.isUnique("" + pickupcode).getResult();
                }
                accessMaterial.setCode(pickupcode + "");
                // 发送短信
                if (StringUtil.isNotBlank(accessMaterial.getApplyerMobile())) {
                    messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                            "您办理的相关证照：" + accessMaterial.getMaterialName() + "，已由工作人员放入存取柜," + "存取柜编号："
                                    + accesscabinetno + "，取件码为：" + pickupcode,
                            new Date(), 0, new Date(), accessMaterial.getApplyerMobile(), UUID.randomUUID().toString(),
                            "", "", "", "", "", "", false, "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "申请人手机号为空，请检查数据！", "");
                }

            }
            else {
                // 生成唯一的随机6位数字
                Boolean isUnique = false;

                while (!isUnique) {
                    try {
                        SecureRandom rand = SecureRandom.getInstanceStrong();
                        pickupcode = 100000 + rand.nextInt(900000);
                    }
                    catch (NoSuchAlgorithmException e) {
                        log.info(e);
                    }
                    isUnique = accessMaterialService.isUnique("" + pickupcode).getResult();
                }
                accessMaterial.setCode(pickupcode + "");

                // 放的是申报材料---->对办件部门下的存取柜管理员发送代表事宜
                String guid = UUID.randomUUID().toString();
                // 1、获取角色标识
                FrameRole frameRole = roleService.getRoleByRoleField("rolename", "存取柜管理员");
                if (frameRole != null && StringUtil.isBlank(zwfwurl)) {
                    String roleguid = frameRole.getRoleGuid();
                    // 2、获取该角色的对应的人员
                    List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                            .getRelationListByField("roleGuid", roleguid, null, null);
                    if (frameuserrolerelationlist != null && !frameuserrolerelationlist.isEmpty()) {
                        // 3、发送待办给审核人员
                        for (FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                            String targetusername = userservice
                                    .getUserNameByUserGuid(frameUserRoleRelation.getUserGuid());
                            String title = "";
                            String handleUrl = "";
                            // String item = "";
                            // 待办名称
                            title = "【待办】 存取柜取件" + ",存取柜编号" + accesscabinetno + ",取件码为：" + pickupcode;
                            // 处理页面
                            handleUrl = "znsb/equipmentdisplay/selfservicemachine/accesscabinet/getcabinetinfo?guid="
                                    + AccessMaterialguid + "&msgguid=" + guid;

                            String messageItemGuid = UUID.randomUUID().toString();
                            /*
                             * messageCenterService.insertWaitHandleMessage(
                             * messageItemGuid, title,
                             * IMessagesCenterService.MESSAGETYPE_READ,
                             * frameUserRoleRelation.getUserGuid(),
                             * targetusername,
                             * frameUserRoleRelation.getUserGuid(),
                             * userservice.getUserNameByUserGuid(
                             * frameUserRoleRelation.getUserGuid()), "",
                             * handleUrl, frameUserRoleRelation.getUserGuid(),
                             * "", ZwfwConstant.CONSTANT_INT_ONE,
                             * "", "", guid, messageItemGuid.substring(0, 1),
                             * new Date(),
                             * frameUserRoleRelation.getUserGuid(),
                             * frameUserRoleRelation.getUserGuid(), "", "");
                             */
                        }

                        dataJson.put("msg", guid);
                    }
                    else {
                        dataJson.put("msg", "请先确认系统是否存在'存取柜管理员'角色人员！");
                    }
                }
                else {
                    dataJson.put("msg", "请先确认系统是否存在'存取柜管理员'角色！");
                }
            }

            if (StringUtil.isNotBlank(zwfwurl)) {
                // 回推政务
                JSONObject postjson = new JSONObject();
                JSONObject paramsjson = new JSONObject();
                paramsjson.put("projectguid", accessMaterial.getProjectguid());
                paramsjson.put("inuserid", accessMaterial.getApplyerCertNo());
                paramsjson.put("type", accessMaterial.getStr("type"));
                paramsjson.put("qjcode", pickupcode);
                paramsjson.put("cabid", cabinetguid);
                paramsjson.put("cabaddress", accesscabinet.getStr("address"));
                postjson.put("token", "Epoint_WebSerivce_**##0601");
                postjson.put("params", paramsjson);
                // 向监控接口请求图片,连接超时时长10秒，读取超时时长10秒
                String returnjson = HttpUtil.sendCommonPost(zwfwurl + "cabinet/materialIn", postjson);
                log.info(postjson);
                log.info(returnjson);
                if (StringUtil.isNotBlank(returnjson)) {
                    JSONObject backjson = JSON.parseObject(returnjson);
                    String code = backjson.getJSONObject("custom").getString("code");
                    if ("1".equals(code)) {
                        dataJson.put("msg", "政务接口调用异常code:" + code);
                    }
                }
                else {
                    dataJson.put("msg", "政务接口调用异常");
                }
            }
            // 更新数据
            accessboxService.update(accessbox);
            accessMaterialService.update(accessMaterial);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 取东西--->获取指定盒子
    @RequestMapping(value = "/getTakeBox", method = RequestMethod.POST)
    public String getTakeBox(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String accessboxguid = obj.getString("boxguid");

            JSONObject dataJson = new JSONObject();
            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessboxguid).getResult();

            dataJson.put("accessboxguid", accessbox.getRowguid());
            dataJson.put("accessboxno", accessbox.getBoxno());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    // 取了东西后，更新状态
    @RequestMapping(value = "/updateTakeStatus", method = RequestMethod.POST)
    public String updateTakeStatus(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String accessboxguid = obj.getString("accessboxguid");
            String accessMaterialguid = obj.getString("accessmaterialguid");
            String centerguid = obj.getString("centerguid");

            JSONObject dataJson = new JSONObject();

            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessboxguid).getResult();
            AuditZnsbAccessMaterial accessMaterial = accessMaterialService.getDetailByGuid(accessMaterialguid)
                    .getResult();

            accessbox.setBoxstatus(QueueConstant.CABINETBOX_NO_FILE); // 更新状态->无单
            accessMaterial.setIsGet(QueueConstant.CONSTANT_STR_ONE); // 已取件
            accessMaterial.setTakeTime(new Date());
            accessMaterial.set("status", "2");// 状态更新为已取件

            String zwfwurl = handleConfigservice.getFrameConfig("AS_CABINET_ZWFW_URL", centerguid).getResult();

            if (StringUtil.isNotBlank(zwfwurl)) {
                // 回推政务
                JSONObject postjson = new JSONObject();
                JSONObject paramsjson = new JSONObject();
                paramsjson.put("projectguid", accessMaterial.getProjectguid());
                paramsjson.put("outuserid", accessMaterial.getApplyerCertNo());
                paramsjson.put("type", accessMaterial.getStr("type"));
                postjson.put("token", "Epoint_WebSerivce_**##0601");
                postjson.put("params", paramsjson);
                // 向监控接口请求图片,连接超时时长10秒，读取超时时长10秒
                String returnjson = HttpUtil.sendCommonPost(zwfwurl + "cabinet/materialOut", postjson);
                log.info(postjson);
                log.info(returnjson);
                if (StringUtil.isNotBlank(returnjson)) {
                    JSONObject backjson = JSON.parseObject(returnjson);
                    String code = backjson.getJSONObject("custom").getString("code");
                    if ("1".equals(code)) {
                        dataJson.put("msg", "政务接口调用异常code:" + code);
                    }
                }
                else {
                    dataJson.put("msg", "政务接口调用异常");
                }
            }

            accessboxService.update(accessbox);
            accessMaterialService.update(accessMaterial);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 根据取件码获取对应的盒子
    @RequestMapping(value = "/getBoxUseCode", method = RequestMethod.POST)
    public String getBoxUseCode(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String code = obj.getString("code");

            JSONObject dataJson = new JSONObject();

            AuditZnsbAccessMaterial accessMaterial = accessMaterialService.getAccessMaterialByCode(code).getResult();
            if (StringUtil.isBlank(accessMaterial)) {
                return JsonUtils.zwdtRestReturn("0", "请检查取件码是否正确！", "");
            }
            AuditZnsbAccessbox accessbox = accessboxService.getDetailByGuid(accessMaterial.getBoxGuid()).getResult();

            dataJson.put("accessmaterialguid", accessMaterial.getRowguid());
            dataJson.put("accessboxguid", accessbox.getRowguid());
            dataJson.put("accessboxno", accessbox.getBoxno());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 验证当前人员是否是管理员
    @RequestMapping(value = "/CheckAdmin", method = RequestMethod.POST)
    public String CheckAdmin(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String sfz = obj.getString("sfz");

            JSONObject dataJson = new JSONObject();

            // 1、获取角色标识
            FrameRole frameRole = roleService.getRoleByRoleField("rolename", "存取柜管理员");
            if (frameRole != null) {
                String roleguid = frameRole.getRoleGuid();
                // 2、获取该角色的对应的人员
                List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                        .getRelationListByField("roleGuid", roleguid, null, null);
                if (frameuserrolerelationlist != null && !frameuserrolerelationlist.isEmpty()) {
                    // 3、发送待办给审核人员
                    for (FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                        // 4、通过角色关系查到人员拓展实体
                        FrameUserExtendInfo userextend = userservice
                                .getUserExtendInfoByUserGuid(frameUserRoleRelation.getUserGuid());
                        if (StringUtil.isNotBlank(sfz) && StringUtil.isNotBlank(userextend)) {
                            if (sfz.equals(userextend.getIdentityCardNum())) {
                                dataJson.put("isadmin", "1");
                                dataJson.put("userguid", frameUserRoleRelation.getUserGuid());
                            }
                        }
                    }
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 取东西--->获取指定盒子
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public String sendCode(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String code = obj.getString("code");
            String phone = obj.getString("phone");
            JSONObject dataJson = new JSONObject();
            int count = 0;
            // 1、获取角色标识
            FrameRole frameRole = roleService.getRoleByRoleField("rolename", "存取柜管理员");
            if (frameRole != null) {
                String roleguid = frameRole.getRoleGuid();
                // 2、获取该角色的对应的人员
                List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                        .getRelationListByField("roleGuid", roleguid, null, null);
                if (frameuserrolerelationlist != null && !frameuserrolerelationlist.isEmpty()) {
                    // 3、发送待办给审核人员
                    for (FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                        // 4、通过角色关系查到人员实体
                        FrameUser user = userservice.getUserByUserField("userguid",
                                frameUserRoleRelation.getUserGuid());
                        if (StringUtil.isNotBlank(phone)) {
                            if (phone.equals(user.getMobile())) {
                                count++;
                                dataJson.put("isadmin", "1");
                                dataJson.put("userguid", frameUserRoleRelation.getUserGuid());
                            }
                        }
                    }
                }
            }
            // 发送短信
            if (count > 0) {
                messageservice.insertSmsMessage(UUID.randomUUID().toString(), "您的验证码为：" + code, new Date(), 0,
                        new Date(), phone, UUID.randomUUID().toString(), "", "", "", "", "", "", false, "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "不是管理员无法操作", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /****************************************************** 以下为手机端展示接口 ************************************************************/
    // 获取当前中心下所有智能柜列表
    @RequestMapping(value = "/getCenterAccessCabinet", method = RequestMethod.POST)
    public String getCenterAccessCabinet(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");

            JSONObject dataJson = new JSONObject();
            // 1、查询中心下关联的存取柜
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.setSelectFields("machinename,rowguid,macaddress");
            sql.eq("centerguid", centerguid);
            sql.eq("status", QueueConstant.EQUIPMENT_STATUS_ONLINE);
            sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_CABINET);
            List<AuditZnsbEquipment> equipmentlist = equipmentservice.getEquipmentByType(sql.getMap()).getResult();
            List<JSONObject> equipmentjsonlist = new ArrayList<JSONObject>();

            if (equipmentlist.size() > 0) {
                for (AuditZnsbEquipment eq : equipmentlist) {
                    AuditZnsbAccesscabinet cabinet = accesscabinetService.getDetailByEquipmentMac(eq.getMacaddress())
                            .getResult();

                    if (StringUtil.isNotBlank(cabinet)) {
                        JSONObject equipmentjson = new JSONObject();
                        equipmentjson.put("name", eq.getMachinename());
                        equipmentjson.put("rowguid", eq.getRowguid());
                        equipmentjson.put("macaddress", eq.getMacaddress());
                        equipmentjsonlist.add(equipmentjson);
                    }
                }
            }
            dataJson.put("accesscabinetlist", equipmentjsonlist);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 获取当前智能柜盒子信息
    @RequestMapping(value = "/getBoxDetail", method = RequestMethod.POST)
    public String getBoxDetail(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");

            JSONObject dataJson = new JSONObject();
            // 1、查询mac关联的存取柜
            AuditZnsbAccesscabinet cabinet = accesscabinetService.getDetailByEquipmentMac(macaddress).getResult();
            List<JSONObject> boxjsonlist = new ArrayList<JSONObject>();
            if (StringUtil.isNotBlank(cabinet)) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("cabinetguid", cabinet.getRowguid());
                sql.setOrderAsc("CAST(boxno as SIGNED)");
                // 2、查询盒子list
                List<AuditZnsbAccessbox> accessboxs = accessboxService
                        .getBoxList(sql.getMap(), "rowguid,boxno,boxstatus").getResult();

                if (accessboxs.size() > 0) {
                    for (AuditZnsbAccessbox box : accessboxs) {
                        JSONObject boxjson = new JSONObject();
                        boxjson.put("name", box.getBoxno());
                        boxjson.put("boxstatus", box.getBoxstatus());
                        boxjsonlist.add(boxjson);
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "当前智能柜配置信息异常", "");
            }
            dataJson.put("boxlist", boxjsonlist);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
}
