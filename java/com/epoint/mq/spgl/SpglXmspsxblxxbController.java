package com.epoint.mq.spgl;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspireview.domain.AuditSpIReview;
import com.epoint.basic.auditsp.auditspireview.inter.IAuditSpIReview;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.spgl.domain.*;
import com.epoint.basic.spgl.inter.*;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.mq.spgl.api.IJnSpglDfxmsplcjdsxxxb;
import com.epoint.sqgl.common.util.Zjconstant;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/spglxmspsxblxxbrest")
public class SpglXmspsxblxxbController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 补生成办件
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertspglxmspsxblxxb", method = RequestMethod.POST)
    public String insertSpglxmspsxblxxb(@RequestBody String params, @Context HttpServletRequest request) {
        IAuditProject iauditproject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditSpISubapp iauditspisubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditRsItemBaseinfo iauditrsitembaseinfo = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
        ISpglDfxmsplcxxb ispgldfxmsplcxxb = ContainerFactory.getContainInfo().getComponent(ISpglDfxmsplcxxb.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        ISpglXmspsxblxxb ispglxmspsxblxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmspsxblxxb.class);
        ISpglXmspsxblxxxxb ispglxmspsxblxxxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmspsxblxxxxb.class);
        IOuService iouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IUserService iuserservice = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IAuditSpIReview iauditspireview = ContainerFactory.getContainInfo().getComponent(IAuditSpIReview.class);
        ISpglXmspsxzqyjxxb ispglxmspsxzqyjxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmspsxzqyjxxb.class);
        IJnSpglDfxmsplcjdsxxxb jnSpglDfxmsplcjdsxxxb = ContainerFactory.getContainInfo().getComponent(IJnSpglDfxmsplcjdsxxxb.class);
        try {
            log.info("=======开始调用insertspglxmspsxblxxb接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            //流水号
            String flowsn = obj.getString("flowsn");
            AuditProject auditProject = iauditproject.getAuditProjectByFlowsn(flowsn, "").getResult();
            if (auditProject != null) {
                //主题阶段唯一标识
                String subappguid = auditProject.getSubappguid();
                String userguid = "";
                AuditTask audittask;
                AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(subappguid).getResult();
                if (sub == null) {
                    return JsonUtils.zwdtRestReturn("0", "SpISubapp表无数据", "");
                }
                AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid()).getResult();
                if (auditspbusiness == null) {
                    return JsonUtils.zwdtRestReturn("0", "auditspbusiness表无数据", "");
                }
                AuditRsItemBaseinfo rsitem = iauditrsitembaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid()).getResult();
                if (rsitem == null) {
                    return JsonUtils.zwdtRestReturn("0", "项目表无数据", "");
                }

                String businessareacode = auditspbusiness.getAreacode();
                AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
                if (area != null) {
                    //如果是县级，查找市级主题
                    if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                        SqlConditionUtil sqlc = new SqlConditionUtil();
                        sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                        //查找市级辖区
                        AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                        if (sjarea != null) {
                            sqlc.clear();
                            sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                            sqlc.eq("areacode", sjarea.getXiaqucode());
                            sqlc.eq("del", "0");
                            sqlc.eq("businesstype", "1");
                            List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
                            if (ValidateUtil.isNotBlankCollection(listb)) {
                                auditspbusiness = listb.get(0);
                            }
                        }
                    }
                }
                //重新设置subapp的bussuid
                sub.setBusinessguid(auditspbusiness.getRowguid());
                Double maxbbh = ispgldfxmsplcxxb.getMaxSplcbbh(sub.getBusinessguid());
                log.info("SpglXmspsxblxxbClientHandle获取最大流程版本号：" + maxbbh + "，主题标识:" + sub.getBusinessguid());
                List<AuditSpIReview> listreview = iauditspireview.getReviewBySubappGuid(subappguid).getResult();
                //插入批事项办理信息
                audittask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                if (audittask == null) {
                    return JsonUtils.zwdtRestReturn("0", "事项表无数据", "");
                }
                SpglXmspsxblxxb spgl = new SpglXmspsxblxxb();
                spgl.setRowguid(UUID.randomUUID().toString());
                spgl.setDfsjzj(auditProject.getRowguid());
                spgl.setXzqhdm("370800");
                spgl.setGcdm(rsitem.getItemcode());
                spgl.setSpsxbm(audittask.getItem_id());
                Zjconstant.dealDataToJs(spgl, audittask.getTask_id());
                spgl.setSplcbm(sub.getBusinessguid());
                spgl.setSplcbbh(maxbbh);//设置关联最新的版本号
                spgl.setSpsxslbm(auditProject.getFlowsn());
                spgl.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                spgl.setSpbmmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
                spgl.setSlfs(4); // 全流程网上申请办理
                spgl.setGkfs(ZwfwConstant.CONSTANT_INT_ONE);//默认1主动公开
                spgl.setBlspslbm(auditProject.getSubappguid());
                spgl.setSxblsx(audittask.getPromise_day());
                spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                spgl.set("sjsczt", "0");

                //重新取逻辑
                String spsxbbh = jnSpglDfxmsplcjdsxxxb.getMaxSpsxbbh(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbm());
                //调整，如果是五位，改为截取后三位
                double version;
                if (StringUtil.isNotBlank(spsxbbh)) {
                    if (spsxbbh.length() > 5) {
                        version = Double.parseDouble(spsxbbh.substring(spsxbbh.length() - 5));
                    } else {
                        version = Double.parseDouble(spsxbbh);
                    }
                    spgl.setSpsxbbh(version);
                } else {
                    spgl.set("sjsczt", "-1");
                    spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
                }
                ispglxmspsxblxxb.insert(spgl);
                //同送征求信息，新增征求信息，根据部门查询征求
                for (AuditSpIReview auditSpIReview : listreview) {
                    if (audittask.getOuguid().equals(auditSpIReview.getOuguid())) {
                        //添加记录
                        SpglXmspsxzqyjxxb spglxmspsxzqyjxxb = new SpglXmspsxzqyjxxb();
                        spglxmspsxzqyjxxb.setRowguid(UUID.randomUUID().toString());
                        spglxmspsxzqyjxxb.setDfsjzj(MDUtils.md5Hex(auditSpIReview.getRowguid() + auditProject.getRowguid()));
                        spglxmspsxzqyjxxb.setXzqhdm("370800");
                        spglxmspsxzqyjxxb.setGcdm(rsitem.getItemcode());
                        spglxmspsxzqyjxxb.setSpsxslbm(auditProject.getFlowsn());
                        spglxmspsxzqyjxxb.setBldwdm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                        spglxmspsxzqyjxxb.setBldwmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
                        spglxmspsxzqyjxxb.setFksj(auditSpIReview.getPingshengdate());
                        spglxmspsxzqyjxxb.setBlr(iuserservice.getUserNameByUserGuid(auditSpIReview.getPingshenguserguid()));
                        spglxmspsxzqyjxxb.setFkyj(auditSpIReview.getPingshenopinion());
                        spglxmspsxzqyjxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                        spglxmspsxzqyjxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                        //判断办件信息是否推送成功
                        if (!ispglxmspsxblxxb.isExistFlowsn(auditProject.getFlowsn())) {
                            spglxmspsxzqyjxxb.set("sjsczt", "-1");
                            spglxmspsxzqyjxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                        ispglxmspsxzqyjxxb.insert(spglxmspsxzqyjxxb);
                    }
                }

                //插入接件的办理详细信息表
                SpglXmspsxblxxxxb spgl1 = new SpglXmspsxblxxxxb();
                spgl1.setRowguid(UUID.randomUUID().toString());
                spgl1.setDfsjzj(auditProject.getRowguid());
                spgl1.setXzqhdm("370800");
                spgl1.setGcdm(rsitem.getItemcode());
                spgl1.setSpsxslbm(auditProject.getFlowsn());

                if (StringUtil.isBlank(userguid)) {
                    userguid = auditProject.getReceiveuserguid();
                }
                FrameUser frameuser = iuserservice.getUserByUserField("userguid", userguid);
                String ouname = iouservice.getOuNameByUserGuid(userguid);
                spgl1.setBlcs(ouname);
                if (frameuser != null) {
                    spgl1.setBlr(frameuser.getDisplayName());
                }
                spgl1.setBlzt(ZwfwConstant.CONSTANT_INT_ONE);
                spgl1.setBlyj("接件");
                spgl1.setBlsj(auditProject.getApplydate());
                spgl1.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spgl1.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                if (!ispglxmspsxblxxb.isExistFlowsn(auditProject.getFlowsn())) {
                    spgl1.set("sjsczt", "-1");
                    spgl1.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                }
                ispglxmspsxblxxxxb.insert(spgl1);
                log.info("=======结束调用insertspglxmspsxblxxb接口=======");
                return JsonUtils.zwdtRestReturn("1", "数据生成成功！", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "办件查询无结果！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======insertspglxmspsxblxxb接口参数：params【" + params + "】=======");
            log.info("=======insertspglxmspsxblxxb异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "生成上报数据失败：" + e.getMessage(), "");
        }

    }


    /**
     * 补生成项目
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertspglxmjbxxb", method = RequestMethod.POST)
    public String insertSpglxmjbxxb(@RequestBody String params, @Context HttpServletRequest request) {
        ISpglXmjbxxb iSpglXmjbxxbService = ContainerFactory.getContainInfo().getComponent(ISpglXmjbxxb.class);
        ISpglDfxmsplcxxb ispgldfxmsplcxxb = ContainerFactory.getContainInfo().getComponent(ISpglDfxmsplcxxb.class);
        ISpglXmdwxxb iSpglXmdwxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmdwxxb.class);
        ISpglXmqqyjxxb iSpglXmqqyjxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmqqyjxxb.class);
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IAuditSpInstance iauditspinstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IParticipantsInfoService iparticipantsinfoservice = ContainerFactory.getContainInfo()
                .getComponent(IParticipantsInfoService.class);
        try {
            log.info("=======开始调用insertspglxmjbxxb接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            //项目主键
            String rowGuid = obj.getString("rowGuid");
            String userareacode = obj.getString("userareacode");
            String subappguid = obj.getString("subappguid");
            String areacode = "";
            SpglXmjbxxb spglXmjbxxb = new SpglXmjbxxb();
            AuditRsItemBaseinfo auditRsItemBaseinfo = new AuditRsItemBaseinfo();
            auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(rowGuid).getResult();
            if (auditRsItemBaseinfo == null) {
                return JsonUtils.zwdtRestReturn("0", "项目查询无结果！", "");
            }
            AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid())
                    .getResult();
            if (auditspinstance == null) {
                return JsonUtils.zwdtRestReturn("0", "实例查询无结果！", "");
            }
            AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(auditspinstance.getBusinessguid()).getResult();

            if (auditspbusiness == null) {
                return JsonUtils.zwdtRestReturn("0", "主题查询无结果！", "");
            }

            String sjsczt = "0";
            StringBuilder sbyy = new StringBuilder();//说明

            String businessareacode = auditspbusiness.getAreacode();
            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
            if (area != null) {
                //如果是县级，查找市级主题
                if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                    //查找市级辖区
                    AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                    if (sjarea == null) {
                        sjsczt = "-1";
                        sbyy.append("该主题类型未存在市级主题中！");
                    } else {
                        sqlc.clear();
                        sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                        sqlc.eq("areacode", sjarea.getXiaqucode());
                        List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
                        if (ValidateUtil.isNotBlankCollection(listb)) {
                            auditspbusiness = listb.get(0);
                            areacode = sjarea.getXiaqucode();
                        } else {
                            sjsczt = "-1";
                            sbyy.append("该主题类型未存在市级主题中！");
                        }
                    }
                }
            }

            String xmdm = "";
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                AuditRsItemBaseinfo pauditRsItemBaseinfo = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByRowguid(auditRsItemBaseinfo.getParentid()).getResult();
                xmdm = pauditRsItemBaseinfo.getItemcode();
            } else {
                xmdm = auditRsItemBaseinfo.getItemcode();
            }
            spglXmjbxxb.setDfsjzj(auditRsItemBaseinfo.getRowguid());
            spglXmjbxxb.setRowguid(UUID.randomUUID().toString());
            spglXmjbxxb.setOperatedate(new Date());
            spglXmjbxxb.setXzqhdm("370800");
            spglXmjbxxb.setXmdm(xmdm);
            spglXmjbxxb.setGcfw(null);//工程范围
            spglXmjbxxb.setQjdgcdm(null);//前阶段关联工程代码
            spglXmjbxxb.setGcdm(auditRsItemBaseinfo.getItemcode());

            spglXmjbxxb.setXmtzly(auditRsItemBaseinfo.getXmtzly());
            if (!isInCode("项目投资来源", auditRsItemBaseinfo.getXmtzly(), true)) {
                sjsczt = "-1";
                sbyy.append("项目投资来源的值不在代码项之中！");
            }

            spglXmjbxxb.setTdhqfs(auditRsItemBaseinfo.getTdhqfs());
            if (!isInCode("土地获取方式", auditRsItemBaseinfo.getTdhqfs(), true)) {
                sjsczt = "-1";
                sbyy.append("土地获取方式的值不在代码项之中！");
            }
            spglXmjbxxb.setTdsfdsjfa(auditRsItemBaseinfo.getTdsfdsjfa());
            if (!isInCode("土地是否带设计方案", auditRsItemBaseinfo.getTdsfdsjfa(), true)) {
                sjsczt = "-1";
                sbyy.append("土地是否带设计方案的值不在代码项之中！");
            }

            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getSfwcqypg())) {
                spglXmjbxxb.setSfwcqypg(auditRsItemBaseinfo.getSfwcqypg());
            } else {
                spglXmjbxxb.setSfwcqypg(0);
            }

            if (!isInCode("是否完成区域评估", auditRsItemBaseinfo.getSfwcqypg(), true)) {
                sjsczt = "-1";
                sbyy.append("是否完成区域评估的值不在代码项之中！");
            }

            spglXmjbxxb.setSplclx(auditspbusiness.getSplclx());
            if (!isInCode("审批流程类型", auditspbusiness.getSplclx(), true)) {
                sjsczt = "-1";
                sbyy.append("审批流程类型的值不在代码项之中！");
            }

            spglXmjbxxb.setXmmc(auditRsItemBaseinfo.getItemname());
            spglXmjbxxb.setGcfl(23);
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getGcfl())) {
                spglXmjbxxb.setGcfl(Integer.parseInt(auditRsItemBaseinfo.getGcfl()));
            }
            spglXmjbxxb.setLxlx(1);
            spglXmjbxxb.setJsxz(Integer.parseInt(auditRsItemBaseinfo.getConstructionproperty()));
            if (!isInCode("建设性质", auditRsItemBaseinfo.getConstructionproperty(), true)) {
                sjsczt = "-1";
                sbyy.append("建设性质的值不在代码项之中！");
            }

            spglXmjbxxb.setXmzjsx(Integer.valueOf(auditRsItemBaseinfo.getXmzjsx()));
            spglXmjbxxb.setGbhydmfbnd("2017");//国标行业代码发布年代 默认2017
            //子项目可能没有国标行业，统一用主项目
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                AuditRsItemBaseinfo parentinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditRsItemBaseinfo.getParentid()).getResult();
                spglXmjbxxb.setGbhy(parentinfo.getGbhy());
            } else {
                spglXmjbxxb.setGbhy(auditRsItemBaseinfo.getGbhy());
            }
            spglXmjbxxb.setNkgsj(auditRsItemBaseinfo.getItemstartdate());
            spglXmjbxxb.setNjcsj(auditRsItemBaseinfo.getItemfinishdate());
            checkBack("拟开工时间", auditRsItemBaseinfo.getItemstartdate());
            checkBack("拟建成时间", auditRsItemBaseinfo.getItemfinishdate());

            spglXmjbxxb.setXmsfwqbj(ZwfwConstant.CONSTANT_INT_ZERO); //项目是否完全办结 默认尚未完全办结
            spglXmjbxxb.setXmwqbjsj(null); //项目完全办结时间
            spglXmjbxxb.setJsddxzqh(userareacode);//建设地点行政区划  默认当前辖区的区划

            spglXmjbxxb.setZtze(auditRsItemBaseinfo.getTotalinvest());
            checkBack("总投资额（万元", auditRsItemBaseinfo.getTotalinvest());

            spglXmjbxxb.setJsdd(auditRsItemBaseinfo.getConstructionsite());
            checkBack("建设地点", auditRsItemBaseinfo.getConstructionsite());

            spglXmjbxxb.setXmjsddx(null); //项目建设地点X坐标
            spglXmjbxxb.setXmjsddy(null); //项目建设地点y坐标
            spglXmjbxxb.setJsgmjnr(auditRsItemBaseinfo.getConstructionscaleanddesc());
            checkBack("建设规模及内容", auditRsItemBaseinfo.getConstructionscaleanddesc());

            spglXmjbxxb.setYdmj(auditRsItemBaseinfo.getLandarea() != null ? auditRsItemBaseinfo.getLandarea() : 0);
            spglXmjbxxb.setJzmj(auditRsItemBaseinfo.getJzmj());
            checkBack("用地面积", auditRsItemBaseinfo.getLandarea());
            checkBack("建筑面积", auditRsItemBaseinfo.getJzmj());

            spglXmjbxxb.setSbsj(auditspinstance.getCreatedate());
            spglXmjbxxb.setSplcbm(auditspbusiness.getRowguid());
            Double verison = ispgldfxmsplcxxb.getMaxSplcbbh(auditspbusiness.getRowguid());
            spglXmjbxxb.setSplcbbh(verison);
            spglXmjbxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
            spglXmjbxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            spglXmjbxxb.set("sjsczt", sjsczt);
            spglXmjbxxb.set("sbyy", sbyy.toString());

            //没向住建发送过发送过信息
            if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditRsItemBaseinfo.getIssendzj())) {
                iSpglXmjbxxbService.insert(spglXmjbxxb);
                //更新状态为已更新
                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ONE);
                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
            }

            //查询推送过的单位信息
            List<Record> dwInfos = iSpglXmdwxxb
                    .getAddedDwInfo(auditspinstance.getAreacode(), auditRsItemBaseinfo.getItemcode()).getResult();

            //项目单位信息
            List<ParticipantsInfo> participantsinfolist = iparticipantsinfoservice
                    .listParticipantsInfoBySubappGuid(subappguid);
            String sqlexcute = "select * from participants_info where corptype = ? and itemguid = ?";
            List<ParticipantsInfo> listp = iparticipantsinfoservice.findList(sqlexcute, "31",
                    auditRsItemBaseinfo.getRowguid());
            participantsinfolist.addAll(listp);

            //根据统一社会代码确定是否重复上传
            for (ParticipantsInfo participantsInfo : participantsinfolist) {
                //如果新增数据则插入信息
                boolean added = false;
                Integer dwlx = getDwlxByCorptype(participantsInfo.getCorptype());
                if (ValidateUtil.isNotBlankCollection(dwInfos)) {
                    for (Record info : dwInfos) {
                        if (participantsInfo.getCorpcode().equals(info.get("dwtyshxydm"))
                                && dwlx.toString().equals(info.getStr("dwlx"))) {
                            added = true;
                        }
                    }
                }
                if (added) {
                    continue;
                } else {
                    Record record = new Record();
                    record.set("dwtyshxydm", participantsInfo.getCorpcode());
                    record.set("dwlx", getDwlxByCorptype(participantsInfo.getCorptype()));
                    dwInfos.add(record);
                }
                SpglXmdwxxb spglXmdwxxb = new SpglXmdwxxb();
                spglXmdwxxb.setDfsjzj(participantsInfo.getRowguid());
                spglXmdwxxb.setRowguid(UUID.randomUUID().toString());
                spglXmdwxxb.setXzqhdm("370800");
                spglXmdwxxb.setXmdm(xmdm);
                spglXmdwxxb.setGcdm(auditRsItemBaseinfo.getItemcode());
                spglXmdwxxb.setDwtyshxydm(participantsInfo.getCorpcode());
                spglXmdwxxb.setDwmc(participantsInfo.getCorpname());
                checkBack("单位名称", participantsInfo.getCorpname());
                checkBack("单位统一社会信用代码", participantsInfo.getCorpcode());
                spglXmdwxxb.setDwlx(dwlx);
                spglXmdwxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spglXmdwxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);

                //数据验证,项目代码是否在工程代码
                if (!iSpglXmjbxxbService.isExistGcdm(auditRsItemBaseinfo.getItemcode())) {
                    spglXmdwxxb.set("sjsczt", "-1");
                    spglXmdwxxb.setSbyy("单位工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                }
                iSpglXmdwxxb.insert(spglXmdwxxb);
            }

            //查询项目前期意见信息
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("qqyjcode", auditRsItemBaseinfo.getStr("chcode"));
            sqlConditionUtil.eq("sync", "-1");
            List<SpglXmqqyjxxb> list = iSpglXmqqyjxxb.getXmqqyjByCondition(SpglXmqqyjxxb.class, sqlConditionUtil.getMap());
            if (list != null) {
                for (SpglXmqqyjxxb spglXmqqyjxxb : list) {
                    spglXmqqyjxxb.setXzqhdm("370800");
                    spglXmqqyjxxb.setXmdm(xmdm);
                    spglXmqqyjxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spglXmqqyjxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    spglXmqqyjxxb.set("sync", ZwfwConstant.CONSTANT_INT_ZERO);
                    iSpglXmqqyjxxb.update(spglXmqqyjxxb);
                }
            }
            log.info("=======结束调用insertspglxmjbxxb接口=======");
            return JsonUtils.zwdtRestReturn("1", "数据生成成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======insertspglxmjbxxb接口参数：params【" + params + "】=======");
            log.info("=======insertspglxmjbxxb异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "生成上报数据失败：" + e.getMessage(), "");
        }

    }


    /**
     * @param codename 主项名称
     * @param value    子项值
     * @param a        是否必须
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isInCode(String codename, Object value, boolean a) {
        if (a) {
            if (value == null) {
                throw new RuntimeException(codename + "的值不能为空！");
            }
        }
        String v = value.toString();
        codename = "国标_" + codename;
        ICodeItemsService icodeitemsservice = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        if (StringUtil.isBlank(s)) {
            return false;
        } else {
            return true;
        }
    }

    public void checkBack(String field, Object o) {
        if (o == null) {
            throw new RuntimeException(field + "的值不能为空！");
        }
    }

    private Integer getDwlxByCorptype(String corptype) {
        Integer dwlx = 7;
        if ("31".equals(corptype)) {
            dwlx = 1;
        } else if ("2".equals(corptype)) {
            dwlx = 4;
        } else if ("1".equals(corptype)) {
            dwlx = 3;
        } else if ("3".equals(corptype)) {
            dwlx = 2;
        } else if ("4".equals(corptype)) {
            dwlx = 5;
        }
        return dwlx;
    }


}
