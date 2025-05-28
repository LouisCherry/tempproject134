package com.epoint.zbxfdj.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditprojectunusual.utils.AuditProjectUnusualUtils;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.spgl.domain.SpglXmspsxblxxxxb;
import com.epoint.basic.spgl.inter.*;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handleproject.service.HandleProjectService;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjbxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxxxbV3;
import com.epoint.zbxfdj.auditdocking.auditspcompany.api.IAuditSpCompanyService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/spproject")
public class SpprojectController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IHandleSPIMaterial spiMaterialService;
    @Autowired
    private IAuditSpISubapp auditSpISubappService;

    @Autowired
    private ISpglXmspsxpfwjxxb ispglxmspsxpfwjxxb;

    @Autowired
    private IAuditProjectSparetime iAuditProjectSparetime;
    @Autowired
    private IAuditProjectUnusual iAuditProjectUnusual;
    @Autowired
    private IAuditProjectMaterial projectMaterialService;
    @Autowired
    private IAuditRsItemBaseinfo iauditrsitembaseinfo;
    @Autowired
    private IAuditTask iaudittask;
    @Autowired
    private IAuditSpISubapp iauditspisubapp;
    @Autowired
    private IAuditProject projectService;
    @Autowired
    private IAuditProjectMaterial iauditprojectmaterial;
    @Autowired
    private IAuditTaskMaterial iaudittaskmaterial;
    @Autowired
    private IAttachService iattachservice;
    @Autowired
    private ISpglXmjbxxb iSpglXmjbxxbService;
    @Autowired
    private ISpglXmspsxblxxb ispglxmspsxblxxb;
    @Autowired
    private ISpglXmqtfjxxb ispglxmqtfjxxb;
    @Autowired
    private ISpglXmspsxbltbcxxxb ispglxmspsxbltbcxxxb;
    @Autowired
    private ISpglXmspsxblxxxxb ispglxmspsxblxxxxb;
    @Autowired
    private IAuditSpIMaterial spIMaterialService;
    @Autowired
    private IAuditSpITask iAuditSpITask;
    @Autowired
    private IHandleSPIMaterial handleSPIMaterial;
    @Autowired
    private ISendMQMessage sendMQMessageService;
    @Autowired
    private IAuditSpShareMaterialRelation shareMaterialRelation;
    @Autowired
    private IAuditSpCompanyService auditSpCompanyService;
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private ISpglXmspsxblxxxxbV3 xxxxbV3Service;

    @Autowired
    private ISpglXmjbxxbV3 xmjbxxbV3Service;

    @Autowired
    private ISpglXmspsxblxxbV3 xxbV3Service;

    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;


    /**
     * 接口用来接收省平台返回的办件状态和结果
     */
    @RequestMapping(value = "/changeProjectStatusAndResult", method = RequestMethod.POST)
    public String changeProjectStatusAndResult(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("=======开始调用changeProjectStatusAndResult接口=======");
        try {
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 办件唯一标识
                String projectguid = obj.getString("projectguid");
                // 办理科室
                String blks = obj.getString("blks");
                // 办理人
                String blr = obj.getString("blr");
                // "办理状态"
                String blzt = obj.getString("blzt");
                // "特别程序值"
                String tbcx = obj.getString("tbcx");
                // 办理意见
                String blyj = obj.getString("blyj");
                // 办理时间
                String blsj = obj.getString("blsj");
                Date blsjDate = EpointDateUtil.convertString2Date(blsj); // 办理时间（时间类型）
                // "特别程序名称" 当特别程序为8其他环节时必填
                String tbcxmc = obj.getString("tbcxmc");
                // 办理结果
                JSONArray resultlist = obj.getJSONArray("resultlist");
                // 附件名称
                String attachfilename = obj.getString("attachfilename");
                // 附件下载地址
                String attachurl = obj.getString("attachurl");
                // 补正材料，当办理状态是6（补正开始）时，必须要传
                JSONArray bzlist = obj.getJSONArray("bzlist");
                // 补正材料名称
                String bzmaterialname = obj.getString("bzmaterialname");
                // 补正材料唯一标识
                String bzmaterialguid = obj.getString("bzmaterialguid");
                //系统来源
                String source = obj.getString("source");
                if (StringUtil.isBlank(projectguid)) {
                    return JsonUtils.zwdtRestReturn("0", "办件唯一标识不能为空！", "");
                }
                if (StringUtil.isBlank(blks)) {
                    return JsonUtils.zwdtRestReturn("0", "办理科室不能为空！", "");
                }
                if (StringUtil.isBlank(blr)) {
                    return JsonUtils.zwdtRestReturn("0", "办理人不能为空！", "");
                }
                String fields = " rowguid,subappguid,businessguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,is_test,currentareacode,handleareacode,taskid,task_id,is_delay,onlineapplyerguid,legalid,charge_when,is_cert,certnum,certrowguid,receiveuserguid";
                // 查询办件
                AuditProject auditProject = projectService.getAuditProjectByRowGuid(fields, projectguid, "")
                        .getResult();
                if (auditProject == null) {
                    return JsonUtils.zwdtRestReturn("0", " 获取办件信息失败！", "");
                }
                // 办件子申报校验
                AuditSpISubapp auditSpISubapp = iauditspisubapp.getSubappByGuid(auditProject.getSubappguid())
                        .getResult();
                if (auditSpISubapp == null) {
                    return JsonUtils.zwdtRestReturn("0", " 获取办件子申报信息失败！", "");
                }
                // 办件项目信息校验
                AuditRsItemBaseinfo auditRsItemBaseinfo = iauditrsitembaseinfo
                        .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                if (auditRsItemBaseinfo == null) {
                    return JsonUtils.zwdtRestReturn("0", " 获取办件项目信息失败！", "");
                }
                // 获取办件材料列表
                List<AuditProjectMaterial> projectMaterialList = iauditprojectmaterial
                        .selectProjectMaterial(projectguid).getResult();

                // 状态装换
                int spglXmspsxblxxxxbStatus = getProvStatus(Integer.parseInt(blzt));
                //办理状态未22，办理意见为 未抽中，特殊处理，状态为办结
                if("22".equals(blzt) && "未抽中".equals(blyj)) {
                    spglXmspsxblxxxxbStatus=11;
                    blzt="11";
                    //为保证省数据完善，额外添加状态8的审批数据
                    SpglXmspsxblxxxxb blxxxxb = new SpglXmspsxblxxxxb();
                    blxxxxb.setRowguid(UUID.randomUUID().toString());
                    blxxxxb.setDfsjzj(projectguid); // 办件标识
                    blxxxxb.setXzqhdm("370800"); // 办件辖区编码（写死市本级）
                    blxxxxb.setGcdm(auditRsItemBaseinfo.getItemcode()); // 办件子申报项目编码
                    blxxxxb.setSpsxslbm(auditProject.getFlowsn()); // 办件流水号
                    blxxxxb.setBlcs(blks); // 办理科室
                    blxxxxb.setBlr(blr); // 办理人
                    blxxxxb.setBlsj(blsjDate); // 办理时间
                    blxxxxb.setBlyj(blyj); // 办理意见
                    blxxxxb.setBlzt(8); // 办理状态
                    blxxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    blxxxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    if (!iSpglXmjbxxbService.isExistGcdm(blxxxxb.getGcdm())) {
                        blxxxxb.set("sjsczt", "-1");
                        blxxxxb.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                    }
                    if (!ispglxmspsxblxxb.isExistFlowsn(blxxxxb.getSpsxslbm())) {
                        blxxxxb.set("sjsczt", "-1");
                        if (StringUtil.isNotBlank(blxxxxb.getSbyy())) {
                            blxxxxb.setSbyy(blxxxxb.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        } else {
                            blxxxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                    }
                    ispglxmspsxblxxxxb.insert(blxxxxb);
                    //存3.0流程 ，状态为8
                    addXmspsxblxxxxbV3(projectguid, auditRsItemBaseinfo, auditProject, blks, blr, blsjDate, blyj, 8);
                }

                // 上报数据：项目审批事项办理详细信息表
                SpglXmspsxblxxxxb blxxxxb = new SpglXmspsxblxxxxb();
                blxxxxb.setRowguid(UUID.randomUUID().toString());
                blxxxxb.setDfsjzj(projectguid); // 办件标识
                blxxxxb.setXzqhdm("370800"); // 办件辖区编码（写死市本级）
                blxxxxb.setGcdm(auditRsItemBaseinfo.getItemcode()); // 办件子申报项目编码
                blxxxxb.setSpsxslbm(auditProject.getFlowsn()); // 办件流水号
                blxxxxb.setBlcs(blks); // 办理科室
                blxxxxb.setBlr(blr); // 办理人
                blxxxxb.setBlsj(blsjDate); // 办理时间
                blxxxxb.setBlyj(blyj); // 办理意见
                blxxxxb.setBlzt(spglXmspsxblxxxxbStatus); // 办理状态
                blxxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                blxxxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                if (!iSpglXmjbxxbService.isExistGcdm(blxxxxb.getGcdm())) {
                    blxxxxb.set("sjsczt", "-1");
                    blxxxxb.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                }
                if (!ispglxmspsxblxxb.isExistFlowsn(blxxxxb.getSpsxslbm())) {
                    blxxxxb.set("sjsczt", "-1");
                    if (StringUtil.isNotBlank(blxxxxb.getSbyy())) {
                        blxxxxb.setSbyy(blxxxxb.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                    } else {
                        blxxxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                    }
                }
                ispglxmspsxblxxxxb.insert(blxxxxb);

                //存3.0流程
                addXmspsxblxxxxbV3(projectguid, auditRsItemBaseinfo, auditProject, blks, blr, blsjDate, blyj, spglXmspsxblxxxxbStatus);

                // 处理结果附件信息
                if (ValidateUtil.isNotBlankCollection(resultlist)) {
                    // 工改审批材料实例的附件标识，用于和办件结果绑定相同附件
                    String spIMaterialCliengguid = "";
                    // 先获取主题配置的事项结果共享材料标识
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("BUSINESSGUID", auditProject.getBusinessguid());
                    sUtil.eq("TASKID", auditProject.getTask_id());
                    sUtil.eq("MATERIALTYPE", "20"); // 结果材料
                    List<AuditSpShareMaterialRelation> shareMaterialRelations = shareMaterialRelation
                            .getAuditSpShareMaterialByMap(sUtil.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(shareMaterialRelations)) {
                        // 有仅有一条数据
                        AuditSpShareMaterialRelation shareMaterialR = shareMaterialRelations.get(0);
                        String sharematerialguid = shareMaterialR.getSharematerialguid();
                        AuditSpIMaterial spIMaterial = spIMaterialService
                                .getSpIMaterialByMaterialGuid(auditProject.getSubappguid(), sharematerialguid)
                                .getResult();
                        if (spIMaterial != null) {
                            spIMaterialCliengguid = spIMaterial.getCliengguid();

                            // 更新工改材料的状态
                            if (StringUtil.isNotBlank(spIMaterialCliengguid)) {
                                String status = spIMaterial.getStatus();
                                // 默认未提交
                                if (StringUtil.isBlank(status)) {
                                    status = "10";
                                }
                                Integer oldStatus = Integer.parseInt(status);
                                // 如果是未提交/纸质，则加10更新为电子/电子和纸质；如果是电子、电子和纸质则保持不变
                                if (oldStatus == ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT
                                        || oldStatus == ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER) {
                                    spIMaterial.setStatus(String.valueOf(oldStatus + 10));
                                    spIMaterialService.updateSpIMaterial(spIMaterial);
                                }
                            }
                        }
                    }

                    for (Object resultObj : resultlist) {
                        JSONObject resultJson = (JSONObject) resultObj;
                        if (resultJson != null && !resultJson.isEmpty()) {
                            JSONObject datajson = new JSONObject();
                            datajson.put("cliengguid", projectguid);
                            datajson.put("fjurl", resultJson.getString("attachurl"));
                            datajson.put("fjmc", resultJson.getString("attachfilename"));
                            // 传递工改实例材料附件标识，绑定办件结果的附件流
                            datajson.put("synccliengguid", spIMaterialCliengguid);
                            sendMQMessageService.sendByExchange("exchange_handle", datajson.toJSONString(),
                                    "attach.common.result.*");
                        }
                    }
                }

                // 根据办理状态，更新办件状态
                //根据系统来源转换blzt
                String newblzt = convertblzt(source,blzt);
                isUpdateProjectStatus(source,blzt,newblzt, auditProject, bzlist);

            }
            log.info("=======结束调用changeProjectStatusAndResult接口=======");
            return JsonUtils.zwdtRestReturn("1", "更新办件状态成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======changeProjectStatusAndResult接口参数：params【" + params + "】=======");
            log.info("=======changeProjectStatusAndResult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "changeProjectStatusAndResult异常信息：" + e.getMessage(), "");
        }
    }

    /**
     * 转换办理状态
     * @param source
     * @param oldblzt
     * @return
     */
    public String convertblzt(String source,String oldblzt){
        String blzt = oldblzt;
        if(StringUtils.isNotBlank(source)){
            switch (source){
                //消防前置库
                case "xfqzk" :
                {
                    switch (oldblzt) {
                        // 已撤件
                        case "2":
                            blzt = "61";
                            break;
                        // 已受理
                        case "3":
                            blzt = "23";
                            break;
                        // 不受理
                        case "4":
                            blzt = "24";
                            break;
                        // 不予受理
                        case "5":
                            blzt = "24";
                            break;
                        // 补正（开始）
                        case "6":
                            blzt = "22";
                            break;
                        // 补正（结束）
                        case "7":
                            blzt = "22";
                            break;
                        // 特别程序（开始）
                        case "9":
                            blzt = "40";
                            break;
                        // 特别程序（结束）
                        case "10":
                            blzt = "41";
                            break;
                        // 办结（通过）
                        case "11":
                            blzt = "60";
                            break;
                        // 办结（不通过）
                        case "13":
                            blzt = "36";
                            break;
                        // 撤回
                        case "14":
                            blzt = "61";
                            break;
                        // 撤销
                        case "15":
                            blzt = "62";
                            break;
                        // 抽中
                        case "21":
                            blzt = "抽中";
                            break;
                        // 未抽中
                        case "22":
                            // 这里没有合适的对应，可根据实际情况修改
                            blzt = "未抽中";
                            break;
                        // 审核
                        case "101":
                            blzt = "35"; // 假设审核对应审核通过，可按需调整
                            break;
                    }
                }
                    break;
            }
        }
        return blzt;
    }

    /**
     * 转换过程状态到办件状态
     * @param source
     * @param oldblzt
     * @return
     */
    public String convertblztToStatus(String source,String oldblzt){
        String status = "";
        if(StringUtils.isNotBlank(source)){
            switch (source){
                //消防前置库
                case "xfqzk" :
                {
                    switch (oldblzt) {
                        // 已撤件
                        case "2":
                            status = "98";
                            break;
                        // 已受理
                        case "3":
                            status = "30";
                            break;
                        // 不受理
                        case "4":
                        // 不予受理
                        case "5":
                            status = "97";
                            break;
                        // 补正（开始）
                        case "6":
                        // 补正（结束）
                        case "7":
                            status = "28";
                            break;
                        // 办结（通过）
                        case "11":
                            status = "90";
                            break;
                        // 办结（不通过）
                        case "13":
                            status = "90";
                            break;
                        // 撤回
                        case "14":
                        // 撤销
                        case "15":
                            status = "98";
                            break;
                        // 审核
                        case "101":
                            status = "80"; // 假设审核对应审核通过，可按需调整
                            break;
                    }
                }
                default:
                    status = oldblzt;
                break;
            }
        }
        return status;
    }

    //存3.0流程
    public void addXmspsxblxxxxbV3(String projectguid, AuditRsItemBaseinfo auditRsItemBaseinfo, AuditProject auditProject,
                                   String blks, String blr, Date blsjDate, String blyj, int spglXmspsxblxxxxbStatus) {
        IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        SpglXmspsxblxxxxbV3 xxxbV3 = new SpglXmspsxblxxxxbV3();
        xxxbV3.setRowguid(UUID.randomUUID().toString());
        xxxbV3.setOperatedate(new Date());
        xxxbV3.setOperateusername("消防同步");
        xxxbV3.setDfsjzj(projectguid); // 办件标识
        xxxbV3.setXzqhdm("370800"); // 办件辖区编码（写死市本级）
        xxxbV3.setGcdm(auditRsItemBaseinfo.getItemcode()); // 办件子申报项目编码
        xxxbV3.setSpsxslbm(auditProject.getFlowsn()); // 办件流水号
        xxxbV3.setBlcs(blks); // 办理科室
        xxxbV3.setBlr(blr); // 办理人
        xxxbV3.setBlsj(blsjDate); // 办理时间
        xxxbV3.setBlyj(blyj); // 办理意见
        xxxbV3.setBlzt(spglXmspsxblxxxxbStatus); // 办理状态
        xxxbV3.setSjly("山东省建设工程消防设计审查及验收管理信息系统");//办理系统
        xxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
        xxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);

        FrameOu ou = ouService.getOuByOuGuid(auditProject.getOuguid());
        if (ou != null) {
            xxxbV3.setDwmc(ou.getOuname());
        } else {
            xxxbV3.setDwmc("错误数据");
            xxxbV3.set("sjsczt", "-1");
            xxxbV3.setSbyy("单位名称校验有误！");
        }

        FrameOuExtendInfo raExtendInfo = ouService.getFrameOuExtendInfo(auditProject.getOuguid());
        if (raExtendInfo != null) {
            xxxbV3.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
        } else {
            xxxbV3.set("sjsczt", "-1");
            xxxbV3.setSbyy("单位统一社会信用代码校验有误！");
        }

        if (!xmjbxxbV3Service.isExistGcdm(xxxbV3.getGcdm())) {
            xxxbV3.set("sjsczt", "-1");
            xxxbV3.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
        }
        if (!xxbV3Service.isExistFlowsn(xxxbV3.getSpsxslbm())) {
            xxxbV3.set("sjsczt", "-1");
            if (StringUtil.isNotBlank(xxxbV3.getSbyy())) {
                xxxbV3.setSbyy(xxxbV3.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
            } else {
                xxxbV3.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
            }
        }
        xxxxbV3Service.insert(xxxbV3);
    }

    private int getProvStatus(int parseInt) {
        if (parseInt == 21 || parseInt == 22 || parseInt == 101) {
            return 8;
        } else {
            return parseInt;
        }
    }

    private String getSpglXmspsxblxxxxbStatus(int parseInt) {
        String status = ZwfwConstant.CONSTANT_STR_ZERO;
        switch (parseInt) {
            // 部门开始办理 -- 已受理
            case ZwfwConstant.BANJIAN_STATUS_YSL:
                status = ZwfwConstant.CENTER_TYPE_FZX;
                break;
            // 已接件
            case ZwfwConstant.BANJIAN_STATUS_YJJ:
                status = ZwfwConstant.CONSTANT_STR_ONE;
                break;
            // 已撤销
            case ZwfwConstant.BANJIAN_STATUS_CXSQ:
                status = ZwfwConstant.LHSP_Status_YSTH;
                break;
            // 不予受理
            case ZwfwConstant.BANJIAN_STATUS_BYSL:
                status = ZwfwConstant.LHSP_Status_WWDYS;
                break;
            // 办结通过/办结容缺通过 -- 正常办结；准予许可
            case ZwfwConstant.BANJIAN_STATUS_ZCBJ:
                status = ZwfwConstant.TASK_SHENPILB_FW;
                break;
            default:
                status = ZwfwConstant.TASK_SHENPILB_FW;
                break;
        }
        return status;
    }

    /**
     * 是否更新办件状态
     *
     * @param blzt         数字建设传入的办理状态
     * @param auditProject 办件
     * @return
     */
    private void isUpdateProjectStatus(String source,String oldblzt,String blzt, AuditProject auditProject, JSONArray bzList) {
        if (StringUtil.isNotBlank(blzt)) {
            String operatename = "";
            String operateguid = "";
            if (StringUtil.isNotBlank(auditProject.getTask_id())) {

                List<Record> listWindowUserByTaskid = auditSpCompanyService
                        .findListWindowUserByTaskid(auditProject.getTask_id());

                if (CollectionUtils.isNotEmpty(listWindowUserByTaskid)) {
                    operatename = listWindowUserByTaskid.get(0).getStr("DISPLAYNAME");
                    operateguid = listWindowUserByTaskid.get(0).getStr("USERGUID");
                }
                HandleProjectService handleProjectService = new HandleProjectService();
                handleProjectService.HandProjectLog(auditProject, blzt, operateguid, operatename, null);

            }
            // 更新办件状态
            String status = convertblztToStatus(source,oldblzt);
            if(StringUtils.isNotBlank(status)){
                auditProject.setStatus(Integer.parseInt(blzt));
            }

            switch (Integer.parseInt(oldblzt)) {
                // 部门开始办理 -- 已受理
                case 3:
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YSL);
                    auditProject.setAcceptuserdate(new Date());
                    auditProject.setAcceptuserguid(operateguid);
                    auditProject.setAcceptusername(operatename);
                    break;
                // 4不受理，5不予受理
                case 4:
                case 5:
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_BYSL);
                    auditProject.setBanjiedate(new Date());
                    auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_BYSL);
                    auditProject.setBanjieuserguid(operateguid);
                    auditProject.setBanjieusername(operatename);
                    break;
                //特别程序开始
                case 9:
                    specialstart(auditProject);
                    break;
                //特别程序结束
                case 10:
                    specialend(auditProject);
                    break;
                //补正
                case 6:
                    materialbuzheng(auditProject, bzList);
                    break;
                // 通过
                case 101:
                    auditProject.setBanwandate(new Date());
                    break;
                // 11 办结通过/办结容缺通过 -- 正常办结；准予许可
                // 13 办结不通通过 -- 正常办结；不予许可
                case 11:
                    auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_ZYXK);
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_ZCBJ);
                    auditProject.setBanjiedate(new Date());
                    auditProject.setBanjieuserguid(operateguid);
                    auditProject.setBanjieusername(operatename);
                    break;
                case 13:
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_ZCBJ);
                    auditProject.setBanjiedate(new Date());
                    auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_BYXK);
                    auditProject.setBanjieuserguid(operateguid);
                    auditProject.setBanjieusername(operatename);
                default:
                    break;
            }
        }

        //查看有无centerguid
        if(StringUtils.isBlank(auditProject.getCenterguid())){
            AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
            if(auditOrgaServiceCenter!=null){
                auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
            }
        }
        IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaWorkingDay.class);
        //更新承诺办结时间
        AuditTask auditTask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
        if(auditTask!=null) {
            List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
            Date acceptdat = auditProject.getAcceptuserdate();
            Date shouldEndDate;
            if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {

                shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                        auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
                log.info("shouldEndDate:"+shouldEndDate);
            } else {
                shouldEndDate = null;
            }
            if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                                AuditProjectUnusualUtils auditProjectUnusualUtils= new AuditProjectUnusualUtils();
                int totalWorkingDaysPaused  = auditProjectUnusualUtils.calculateTotalWorkingDaysPaused(auditProjectUnusuals,auditProject.getCenterguid());
                if (totalWorkingDaysPaused > 0 && shouldEndDate != null) {
                    // 重新计算包含暂停时间的预计结束日期
                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                            auditProject.getCenterguid(), shouldEndDate, (int) totalWorkingDaysPaused).getResult();
                    log.info("考虑暂停时间后的预计结束日期 shouldEndDate: " + shouldEndDate);
                }
                log.info("shouldEndDate:"+shouldEndDate);
            }
            if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
                   auditProject.setPromiseenddate(shouldEndDate);
            }
        }
        
        projectService.updateProject(auditProject);
        if (ZwfwConstant.BANJIAN_STATUS_ZCBJ == auditProject.getStatus()) {
            // 拿到当前办件对应的子申报
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
            if (auditSpISubapp != null) {
                // 修改办结时间和状态
                auditSpISubapp.setFinishdate(new Date());
                auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_YBJ);
                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
            }
        }
    }


    public void specialstart(AuditProject auditProject) {
        String operatename = "";
        String operateguid = "";
        if (StringUtil.isNotBlank(auditProject.getTask_id())) {
            List<Record> listWindowUserByTaskid = auditSpCompanyService.findListWindowUserByTaskid(auditProject.getTask_id());
            if (CollectionUtils.isNotEmpty(listWindowUserByTaskid)) {
                operatename = listWindowUserByTaskid.get(0).getStr("DISPLAYNAME");
                operateguid = listWindowUserByTaskid.get(0).getStr("USERGUID");
            }

        }
        //先暂停计时
        auditProject.setStatus(ZwfwConstant.DOC_TYPE_BYSLJDS);
        auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
        projectService.updateProject(auditProject);
        // 更新时间表状态 恢复计时0 暂停计时1
        AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
        if (auditProjectSparetime != null) {
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
            iAuditProjectSparetime.updateSpareTime(auditProjectSparetime);
        }
        AuditProjectUnusual auditProjectUnusual = new AuditProjectUnusual();
        auditProjectUnusual.setNote("消防对接特别程序开始");
        auditProjectUnusual.setProjectguid(auditProject.getRowguid());
        auditProjectUnusual.setPviguid(auditProject.getPviguid());
        auditProjectUnusual.setRowguid(UUID.randomUUID().toString());
        auditProjectUnusual.setOperatetype(Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_ZT));
        auditProjectUnusual.setOperatedate(new Date());
        auditProjectUnusual.setOperateusername(operatename);
        auditProjectUnusual.setOperateuserguid(operateguid);
        iAuditProjectUnusual.addProjectUnusual(auditProjectUnusual);
    }

    public void specialend(AuditProject auditProject) {
        String operatename = "";
        String operateguid = "";
        if (StringUtil.isNotBlank(auditProject.getTask_id())) {
            List<Record> listWindowUserByTaskid = auditSpCompanyService.findListWindowUserByTaskid(auditProject.getTask_id());
            if (CollectionUtils.isNotEmpty(listWindowUserByTaskid)) {
                operatename = listWindowUserByTaskid.get(0).getStr("DISPLAYNAME");
                operateguid = listWindowUserByTaskid.get(0).getStr("USERGUID");
            }

        }
        auditProject.setStatus(ZwfwConstant.DOC_TYPE_BYXKJDS);
        // 更新办件状态
        auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);// 0恢复
        // 恢复计时
        IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        iAuditProject.updateProject(auditProject);
        // 更新时间表状态 恢复计时0 暂停计时1
        AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
        if (auditProjectSparetime != null) {
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ZERO);
            iAuditProjectSparetime.updateSpareTime(auditProjectSparetime);
        }
        AuditProjectUnusual auditProjectUnusual = new AuditProjectUnusual();
        auditProjectUnusual.setNote("消防对接特别程序结束");
        auditProjectUnusual.setProjectguid(auditProject.getRowguid());
        auditProjectUnusual.setPviguid(auditProject.getPviguid());
        auditProjectUnusual.setRowguid(UUID.randomUUID().toString());
        auditProjectUnusual.setOperatetype(Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_HF));
        auditProjectUnusual.setOperatedate(new Date());
        auditProjectUnusual.setOperateusername(operatename);
        auditProjectUnusual.setOperateuserguid(operateguid);
        iAuditProjectUnusual.addProjectUnusual(auditProjectUnusual);
    }

    public void materialbuzheng(AuditProject auditProject, JSONArray ary) {
        String materialnamelist = "";
        if (CollectionUtils.isNotEmpty(ary)) {
            for (int i = 0; i < ary.size(); i++) {
                JSONObject obj = ary.getJSONObject(i);
                if (StringUtil.isNotBlank(materialnamelist)) {
                    materialnamelist += "," + obj.getString("Name");
                } else {
                    materialnamelist += obj.getString("Name");
                }
            }
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_DBB);
            IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            iAuditProject.updateProject(auditProject);

            // 需要补正的材料rowGuid
            String materialsGuid = "";
            // 获取办件材料列表
            List<AuditProjectMaterial> projectMaterialList = iauditprojectmaterial.selectProjectMaterial(auditProject.getRowguid())
                    .getResult();
            for (AuditProjectMaterial auditprojectmaterial : projectMaterialList) {
                if (materialnamelist.contains(auditprojectmaterial.getTaskmaterial())) {
                    projectMaterialService.updateProjectMaterialAuditStatus(auditprojectmaterial.getRowguid(),
                            Integer.parseInt(ZwfwConstant.Material_AuditStatus_DBZ), auditProject.getRowguid());
                    materialsGuid += auditprojectmaterial.getRowguid() + ",";
                }
            }
            spiMaterialService.updateIMaterialBuzheng(auditProject.getSubappguid(), materialsGuid, auditProject.getRowguid());
            auditSpISubappService.updateSubapp(auditProject.getSubappguid(), ZwfwConstant.LHSP_Status_DBJ, null);
        }
    }
}
