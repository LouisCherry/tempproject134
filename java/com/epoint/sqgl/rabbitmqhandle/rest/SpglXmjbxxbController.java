package com.epoint.sqgl.rabbitmqhandle.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspireview.domain.AuditSpIReview;
import com.epoint.basic.auditsp.auditspireview.inter.IAuditSpIReview;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.spgl.domain.SpglXmdwxxb;
import com.epoint.basic.spgl.domain.SpglXmjbxxb;
import com.epoint.basic.spgl.domain.SpglXmqqyjxxb;
import com.epoint.basic.spgl.inter.*;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.CertInfoExternalImpl;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.common.util.*;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.mq.spgl.api.IJnSpglDfxmsplcjdsxxxb;
import com.epoint.sqgl.common.util.Zjconstant;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*;
import com.epoint.xmz.thirdreporteddata.common.GxhSpConstant;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spglxmjbxxbrest")
public class SpglXmjbxxbController {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IAuditSpInstance iauditspinstance;

    @Autowired
    private IAuditSpBusiness iauditspbusiness;

    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private ISpglDfxmsplcxxb ispgldfxmsplcxxb;

    @Autowired
    private ISpglXmjbxxb iSpglXmjbxxbService;

    @Autowired
    private ISpglXmdwxxb iSpglXmdwxxb;

    @Autowired
    private ISpglXmqqyjxxb iSpglXmqqyjxxb;

    @Autowired
    private IParticipantsInfoService iparticipantsinfoservice;

    @Autowired
    private ISpglsplcxxb iSpglsplcxxb;

    @Autowired
    private ISpglQypgsxxxbService iSpglQypgsxxxbService;

    @Autowired
    private ISpglQypgxxbService iSpglQypgxxbService;

    @Autowired
    private ISpglQypgxxbV3Service iSpglQypgxxbV3Service;

    @Autowired
    private ISpglQypgsxxxbV3Service iSpglQypgsxxxbV3Service;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private ISpglXmjbxxbV3 iSpglXmjbxxbV3;

    @Autowired
    private ISpglCommon iSpglCommon;

    @Autowired
    private IAuditProject iauditproject;

    @Autowired
    private IAuditSpISubapp iauditspisubapp;

    @Autowired
    private IAuditSpIReview iauditspireview;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private ISpglXmspsxblxxbV3 ispglxmspsxblxxbV3;

    @Autowired
    private IOuService iouservice;

    @Autowired
    private IUserService iuserservice;

    @Autowired
    private IConfigService frameConfigService;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    @Autowired
    private ISpglXmspsxzqyjxxbV3 iSpglXmspsxzqyjxxbV3;

    @Autowired
    private ISpglXmspsxblxxxxbV3 iSpglXmspsxblxxxxbV3;

    @Autowired
    private ISpglsplcjdsxxxb iSpglsplcjdsxxxb;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IJnSpglDfxmsplcjdsxxxb jnSpglDfxmsplcjdsxxxb;

    @Autowired
    private ITaskCommonService iTaskCommonService;

    @Autowired
    private ISpglZrztxxbV3Service iSpglZrztxxbV3Service;

    @Autowired
    private IDantiSubRelationService iDantiSubRelationService;

    @Autowired
    private IDantiInfoV3Service iDantiInfoV3Service;

    @Autowired
    private ISpglXmdtxxbV3Service iSpglXmdtxxbV3Service;

    @Autowired
    private IAuditTaskResult iAuditTaskResult;

    @Autowired
    private CertInfoExternalImpl certInfoExternalImpl;

    @Autowired
    private ICertAttachExternal iCertAttachExternal;

    @Autowired
    private IAuditProjectOperation operationservice;

    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private IAuditProjectMaterial iauditprojectmaterial;

    @Autowired
    private ISpglsqcljqtfjxxbV3 iSpglsqcljqtfjxxbV3;

    @Autowired
    private ISpglXmspsxpfwjxxbV3 ispglxmspsxpfwjxxb;
    @Autowired
    private ISpglSgtsjwjscxxbV3Service sgtsjwjscxxbV3Service;
    @Autowired
    private ISpglJzgcsgxkxxbV3Service jzgcsgxkxxbV3Service;
    @Autowired
    private ISpglJsgcjgysbaxxbV3Service jsgcjgysbaxxbV3Service;

    /**
     * 生成项目信息表2.0接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public String Generate(@RequestBody String params) {
        try {
            log.info("=======开始调用generate接口=======");
            // 1.解析入参
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                AuditRsItemBaseinfo auditRsItemBaseinfo = new AuditRsItemBaseinfo();
                SpglXmjbxxb spglXmjbxxb = new SpglXmjbxxb();
                //项目主键
                String gcdm = obj.getString("gcdm");
                String areacode = obj.getString("areacode");
                String spsxslbm = obj.getString("spsxslbm");
                auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByItemcode(gcdm).getResult();
                if (auditRsItemBaseinfo == null) {
                    return JsonUtils.zwdtRestReturn("0", "项目不存在！", "");
                }
                AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid())
                        .getResult();
                if (auditspinstance == null) {
                    return JsonUtils.zwdtRestReturn("0", "申报实例不存在！", "");
                }
                AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(auditspinstance.getBusinessguid()).getResult();

                if (auditspbusiness == null) {
                    return JsonUtils.zwdtRestReturn("0", "主题不存在！", "");
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
                spglXmjbxxb.setJsddxzqh(areacode);//建设地点行政区划  默认当前辖区的区划

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

                String sql = "select SUBAPPGUID from audit_project where flowsn = ? ";
                AuditProject auditProject = CommonDao.getInstance().find(sql, AuditProject.class, spsxslbm);

                if (auditProject != null && StringUtil.isNotBlank(auditProject.getSubappguid())) {
                    //项目单位信息
                    List<ParticipantsInfo> participantsinfolist = iparticipantsinfoservice
                            .listParticipantsInfoBySubappGuid(auditProject.getSubappguid());
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
            }
            log.info("=======结束调用generate接口=======");
            return JsonUtils.zwdtRestReturn("1", "生成项目上报表成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "失败！", "");
        }
    }


    /**
     * 生成项目信息表3.0接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/generateV3", method = RequestMethod.POST)
    public String GenerateV3(@RequestBody String params) {
        try {
            log.info("=======开始调用generateV3接口=======");
            // 1.解析入参
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                AuditRsItemBaseinfo auditRsItemBaseinfo = new AuditRsItemBaseinfo();
                SpglXmjbxxbV3 spglXmjbxxb = new SpglXmjbxxbV3();
                //项目主键
                String gcdm = obj.getString("gcdm");
                String areacode = obj.getString("areacode");
                auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByItemcode(gcdm).getResult();
                if (auditRsItemBaseinfo == null) {
                    return JsonUtils.zwdtRestReturn("0", "项目不存在！", "");
                }
                AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid())
                        .getResult();
                if (auditspinstance == null) {
                    return JsonUtils.zwdtRestReturn("0", "申报实例不存在！", "");
                }
                AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(auditspinstance.getBusinessguid()).getResult();

                if (auditspbusiness == null) {
                    return JsonUtils.zwdtRestReturn("0", "主题不存在！", "");
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
                            } else {
                                sjsczt = "-1";
                                sbyy.append("该主题类型未存在市级主题中！");
                            }
                        }
                    }
                }

                String xmdm = "";
                String jsdw = "";
                String jsdwdm = "";
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                    AuditRsItemBaseinfo pauditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(
                            auditRsItemBaseinfo.getParentid()).getResult();
                    xmdm = pauditRsItemBaseinfo.getItemcode();
                    jsdw = pauditRsItemBaseinfo.getItemlegaldept();
                    jsdwdm = pauditRsItemBaseinfo.getItemlegalcertnum();
                } else {
                    xmdm = auditRsItemBaseinfo.getItemcode();
                    jsdw = auditRsItemBaseinfo.getItemlegaldept();
                    jsdwdm = auditRsItemBaseinfo.getItemlegalcertnum();
                }
                spglXmjbxxb.setDfsjzj(auditRsItemBaseinfo.getRowguid());
                spglXmjbxxb.setRowguid(UUID.randomUUID().toString());
                spglXmjbxxb.setXzqhdm("370800");
                spglXmjbxxb.setXmdm(xmdm);
                spglXmjbxxb.setOperatedate(new Date());
                spglXmjbxxb.setOperateusername("mq逻辑生成");
                spglXmjbxxb.setGcfw(auditRsItemBaseinfo.getStr("GCFW"));// 工程范围
                spglXmjbxxb.setQjdgcdm(null);// 前阶段关联工程代码
                spglXmjbxxb.setGcdm(auditRsItemBaseinfo.getItemcode());
                spglXmjbxxb.setSplcbm(auditspbusiness.getRowguid());
                Double verison = iSpglsplcxxb.getMaxSplcbbh(auditspbusiness.getRowguid());
                spglXmjbxxb.setSplcbbh(verison);

                spglXmjbxxb.setXmtzly(auditRsItemBaseinfo.getXmtzly());
                // 0 不在范围内，特殊处理
                if (0 == auditRsItemBaseinfo.getXmtzly()) {
                    if (1 == auditspbusiness.getSplclx() || 2 == auditspbusiness.getSplclx()) {
                        spglXmjbxxb.setXmtzly(1);
                    } else {
                        spglXmjbxxb.setXmtzly(2);
                    }
                }

                if (!isInCode("项目投资来源", spglXmjbxxb.getXmtzly(), true)) {
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

                spglXmjbxxb.setSfwcqypg(auditRsItemBaseinfo.getSfwcqypg());
                if (!isInCode("是否完成区域评估", auditRsItemBaseinfo.getSfwcqypg(), true)) {
                    sjsczt = "-1";
                    sbyy.append("是否完成区域评估的值不在代码项之中！");
                }

                //如果完成区域评估则上报关联的区域评估数据
                if (auditRsItemBaseinfo.getSfwcqypg() == 1) {
                    List<SpglQypgxxb> SpglQypgxxblist = iSpglQypgxxbService.getSpglQypgxxbListByitemguid(auditRsItemBaseinfo.getRowguid());
                    if (SpglQypgxxblist.isEmpty()) {
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                            SpglQypgxxblist = iSpglQypgxxbService.getSpglQypgxxbListByitemguid(auditRsItemBaseinfo.getParentid());
                        }
                    }
                    if (!SpglQypgxxblist.isEmpty()) {
                        for (SpglQypgxxb sqQypgxxbedit : SpglQypgxxblist) {
                            SpglQypgxxbV3 spQypgxxbV3 = new SpglQypgxxbV3();
                            spQypgxxbV3.setRowguid(UUID.randomUUID().toString());
                            spQypgxxbV3.setDfsjzj(sqQypgxxbedit.getRowguid());
                            spQypgxxbV3.setXzqhdm("370800");
                            spQypgxxbV3.setSync(ZwfwConstant.CONSTANT_INT_ZERO);
                            spQypgxxbV3.setQypgdybm(sqQypgxxbedit.getQypgdybm());
                            spQypgxxbV3.setQypgqymc(sqQypgxxbedit.getQypgqymc());
                            spQypgxxbV3.setQypgfwms(sqQypgxxbedit.getQypgfwms());
                            spQypgxxbV3.setQypgfwzbxx(sqQypgxxbedit.getQypgfwzbxx());
                            spQypgxxbV3.setQypgmj(sqQypgxxbedit.getQypgmj());
                            // 查询是否有同步过的数据
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("dfsjzj", sqQypgxxbedit.getRowguid());
                            sqlConditionUtil.eq("sjyxbs", "1");
                            List<SpglQypgxxbV3> list = iSpglQypgxxbV3Service.getAuditSpDanitemByPage(sqlConditionUtil.getMap(), -1, -1, null, null).getResult().getList();
                            if (EpointCollectionUtils.isNotEmpty(list)) {
                                //若存在则说明【同步过】
                                spQypgxxbV3.set("sjsczt", ZwfwConstant.CONSTANT_INT_ZERO);
                                iSpglCommon.editToPushData(list.get(0), spQypgxxbV3, true);
                            } else {
                                //不存在则说明【未同步过】则直接将数据插入至【区域评估信息上报表(SPGL_QYPGXXB_V3)】
                                spQypgxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                spQypgxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                iSpglQypgxxbV3Service.insert(spQypgxxbV3);
                            }
                            sqQypgxxbedit.setSync(ZwfwConstant.CONSTANT_INT_ONE);
                            iSpglQypgxxbService.update(sqQypgxxbedit);

                            // 处理区域评估事项信息
                            sqlConditionUtil.clear();
                            sqlConditionUtil.eq("qypgdybm", sqQypgxxbedit.getQypgdybm());
                            List<SpglQypgsxxxb> listByMap = iSpglQypgsxxxbService.getListByMap(sqlConditionUtil.getMap());
                            if (EpointCollectionUtils.isNotEmpty(listByMap)) {
                                for (SpglQypgsxxxb spglQypgsxxxb : listByMap) {
                                    SpglQypgsxxxbV3 spglQypgsxxxbV3 = new SpglQypgsxxxbV3();
                                    spglQypgsxxxbV3.setRowguid(UUID.randomUUID().toString());
                                    spglQypgsxxxbV3.setDfsjzj(spglQypgsxxxb.getRowguid());
                                    spglQypgsxxxbV3.setXzqhdm("370800");
                                    spglQypgsxxxbV3.setQypgdybm(spglQypgsxxxb.getQypgdybm());
                                    spglQypgsxxxbV3.setQypgsxbm(spglQypgsxxxb.getQypgsxbm());
                                    spglQypgsxxxbV3.setQypgsxmc(spglQypgsxxxb.getQypgsxmc());
                                    spglQypgsxxxbV3.setDybzspsxbm(spglQypgsxxxb.getDybzspsxbm());
                                    spglQypgsxxxbV3.setQypgcgsxrq(spglQypgsxxxb.getQypgcgsxrq());
                                    spglQypgsxxxbV3.setQypgcgjzrq(spglQypgsxxxb.getQypgcgjzrq());
                                    spglQypgsxxxbV3.setJhspdfs(spglQypgsxxxb.getJhspdfs());
                                    // 查询附件
                                    List<FrameAttachInfo> attachInfos = attachService
                                            .getAttachInfoListByGuid(spglQypgsxxxb.getStr("cliengguid"));
                                    if (EpointCollectionUtils.isNotEmpty(attachInfos)) {
                                        spglQypgsxxxbV3.setQypgcgcllx(attachInfos.get(0).getContentType());
                                        spglQypgsxxxbV3.setQypgcgclmc(attachInfos.get(0).getAttachFileName());
                                        spglQypgsxxxbV3.setQypgcgfjid(attachInfos.get(0).getAttachGuid());
                                    }
                                    spglQypgsxxxbV3.setSync(ZwfwConstant.CONSTANT_STR_ZERO);
                                    // 查询是否有同步过的数据
                                    SpglQypgsxxxbV3 spglQypgsxxxbV3edit = iSpglQypgsxxxbV3Service.getSpglQypgsxxxbByDfsjzj(spglQypgsxxxb.getRowguid());
                                    if (spglQypgsxxxbV3edit != null) {
                                        // 若存在则说明【同步过】
                                        iSpglCommon.editToPushData(spglQypgsxxxbV3edit, spglQypgsxxxbV3);
                                    } else {
                                        // 不存在则说明【未同步过】则直接将数据插入至【区域评估事项信息上报表(SPGL_QYPGSXXXB_V3)】
                                        spglQypgsxxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                        spglQypgsxxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                        iSpglQypgsxxxbV3Service.insert(spglQypgsxxxbV3);
                                    }
                                    spglQypgsxxxb.setSync(ZwfwConstant.CONSTANT_STR_ONE);
                                    iSpglQypgsxxxbService.update(spglQypgsxxxb);

                                }
                            }
                        }
                    }
                }

                spglXmjbxxb.setSplclx(auditspbusiness.getSplclx());
                if (!isInCode("审批流程类型", auditspbusiness.getSplclx(), true)) {
                    sjsczt = "-1";
                    sbyy.append("审批流程类型的值不在代码项之中！");
                }

                spglXmjbxxb.setXmmc(auditRsItemBaseinfo.getItemname());
                spglXmjbxxb.setGcfl(31);
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getStr("LXLX"))) {
                    spglXmjbxxb.setLxlx(Integer.parseInt(auditRsItemBaseinfo.getStr("LXLX")));
                } else {
                    spglXmjbxxb.setLxlx(1);
                }
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getConstructionproperty())) {
                    spglXmjbxxb.setJsxz(Integer.parseInt(auditRsItemBaseinfo.getConstructionproperty()));
                }
                if (!isInCode("建设性质", auditRsItemBaseinfo.getConstructionproperty(), true)) {
                    sjsczt = "-1";
                    sbyy.append("建设性质的值不在代码项之中！");
                }

                spglXmjbxxb.setXmzjsx(Integer.valueOf(auditRsItemBaseinfo.getXmzjsx()));
                spglXmjbxxb.setGbhydmfbnd("2017");// 国标行业代码发布年代 默认2017
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

                spglXmjbxxb.setXmsfwqbj(ZwfwConstant.CONSTANT_INT_ZERO); // 项目是否完全办结
                // 默认尚未完全办结
                spglXmjbxxb.setXmwqbjsj(null); // 项目完全办结时间
                // 个性化：使用新字段
                spglXmjbxxb.setJsddxzqh(areacode);

                spglXmjbxxb.setZtze(auditRsItemBaseinfo.getTotalinvest());
                checkBack("总投资额（万元", auditRsItemBaseinfo.getTotalinvest());

                spglXmjbxxb.setJsdd(auditRsItemBaseinfo.getConstructionsite());
                checkBack("建设地点", auditRsItemBaseinfo.getConstructionsite());
                // 3.0新增变更字段开始
                spglXmjbxxb.setXmjwdzb(auditRsItemBaseinfo.get("XMJWDZB"));
                spglXmjbxxb.setJsdwdm(jsdwdm);
                spglXmjbxxb.setJsdw(jsdw);
                spglXmjbxxb.setJsdwlx(1);
                spglXmjbxxb.setSfxxgc(auditRsItemBaseinfo.getInt("SFXXGC"));
                spglXmjbxxb.setCd(auditRsItemBaseinfo.getDouble("CD"));
                spglXmjbxxb.setGchyfl(auditRsItemBaseinfo.getStr("GCHYFL"));
                // 3.0新增变更字段结束
                spglXmjbxxb.setJsgmjnr(auditRsItemBaseinfo.getConstructionscaleanddesc());
                checkBack("建设规模及内容", auditRsItemBaseinfo.getConstructionscaleanddesc());

                spglXmjbxxb.setYdmj(auditRsItemBaseinfo.getLandarea() != null ? auditRsItemBaseinfo.getLandarea() : 0);
                spglXmjbxxb.setJzmj(auditRsItemBaseinfo.getJzmj());
                checkBack("用地面积", auditRsItemBaseinfo.getLandarea());
                checkBack("建筑面积", auditRsItemBaseinfo.getJzmj());

                spglXmjbxxb.setSbsj(auditspinstance.getCreatedate());
                spglXmjbxxb.setSplcbm(auditspbusiness.getRowguid());
                spglXmjbxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spglXmjbxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                spglXmjbxxb.set("sjsczt", sjsczt);
                spglXmjbxxb.set("sbyy", sbyy.toString());

                //瀚高库新增字段 -- 默认塞值非重点项目
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getStr("zdxmlx"))) {
                    spglXmjbxxb.set("zdxmlx", auditRsItemBaseinfo.getStr("zdxmlx"));
                } else {
                    spglXmjbxxb.set("zdxmlx", "0");
                }

                // 没向住建发送过发送过信息   新字段issendzj_v3
                if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditRsItemBaseinfo.getStr("issendzj_v3"))) {
                    iSpglXmjbxxbV3.insert(spglXmjbxxb);
                    // 更新状态为已更新
                    auditRsItemBaseinfo.set("issendzj_v3", ZwfwConstant.CONSTANT_STR_ONE);
                    iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                } else {
                    int count = iSpglXmjbxxbV3.getCountByGcdm(auditRsItemBaseinfo.getItemcode());
                    if (count == 0) {
                        iSpglXmjbxxbV3.insert(spglXmjbxxb);
                    }
                }

            }
            log.info("=======结束调用generateV3接口=======");
            return JsonUtils.zwdtRestReturn("1", "生成项目上报表成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "失败！", "");
        }
    }


    /**
     * 生成办件信息表3.0接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/generatexxbV3", method = RequestMethod.POST)
    public String GeneratexxbV3(@RequestBody String params) {
        try {
            log.info("=======开始调用generatexxbV3接口=======");
            // 1.解析入参
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 主题阶段唯一标识
                String subappguid = obj.getString("subappguid");
                String rowguid = obj.getString("rowguid");
                AuditProject auditProject = iauditproject.getAuditProjectByRowGuid(rowguid, "").getResult();
                AuditTask audittask;
                if (auditProject == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询办件失败！", "");
                }
                AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(subappguid).getResult();
                if (sub == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询子申报实例失败！", "");
                }
                AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid())
                        .getResult();
                if (auditspbusiness == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询主题失败！", "");
                }

                String businessareacode = auditspbusiness.getAreacode();
                AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
                String sjsczt = "0";
                String sbyy = "";
                SqlConditionUtil sqlc = new SqlConditionUtil();
                if (area != null) {
                    // 如果是县级，查找市级主题
                    if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                        sqlc.clear();
                        sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                        // 查找市级辖区
                        AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                        if (sjarea == null) {
                            sjsczt = "-1";
                            sbyy = "该主题类型未存在市级主题中！";
                        } else {
                            sqlc.clear();
                            sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                            sqlc.eq("areacode", sjarea.getXiaqucode());
                            List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
                            if (ValidateUtil.isNotBlankCollection(listb)) {
                                auditspbusiness = listb.get(0);
                            } else {
                                sjsczt = "-1";
                                sbyy = "该主题类型未存在市级主题中！";
                            }
                        }
                    }
                }
                // 重新设置subapp的bussuid
                sub.setBusinessguid(auditspbusiness.getRowguid());

                AuditRsItemBaseinfo rsitem = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid()).getResult();
                if (rsitem == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询项目失败！", "");
                }

                Double maxbbh = iSpglsplcxxb.getMaxSplcbbh(sub.getBusinessguid());
                List<AuditSpIReview> listreview = iauditspireview.getReviewBySubappGuid(subappguid).getResult();
                Date date = new Date();

                // 插入批事项办理信息
                audittask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                if (audittask == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询事项失败！", "");
                }
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(
                        auditProject.getTaskguid(), true).getResult();
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getIszijianxitong().toString())) {
                    // 如果是自建系统办件、根据系统参数判断是否上报自建系统办件
                    String notreport = frameConfigService.getFrameConfigValue("AS_NOTREPORTED_ZJ");
                    if (StringUtil.isNotBlank(notreport) && ZwfwConstant.CONSTANT_STR_ONE.equals(notreport)) {
                        return JsonUtils.zwdtRestReturn("0", "自建系统不推送！", "");
                    }
                }
                SpglXmspsxblxxbV3 spgl = new SpglXmspsxblxxbV3();
                spgl.setRowguid(UUID.randomUUID().toString());
                spgl.setDfsjzj(auditProject.getRowguid());
                spgl.setXzqhdm("370800");
                spgl.setGcdm(rsitem.getItemcode());
                spgl.setSpsxbm(audittask.getItem_id());
                Zjconstant.dealDataToJs(spgl, audittask.getTask_id());

                spgl.setSplcbm(sub.getBusinessguid());
                spgl.setSplcbbh(maxbbh);// 设置关联最新的版本号

                //重新取事项版本
                String spsxbbh = jnSpglDfxmsplcjdsxxxb.getMaxSpsxbbhV3(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbm());
                if (StringUtil.isNotBlank(spsxbbh)) {
                    spgl.setSpsxbbh(Double.valueOf(spsxbbh));
                } else {
                    spgl.set("sjsczt", "-1");
                    spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
                }

                spgl.setSpsxslbm(auditProject.getFlowsn());
                spgl.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                spgl.setSpbmmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
                spgl.setSlfs(4); // 全流程网上申请办理
                spgl.setGkfs(ZwfwConstant.CONSTANT_INT_ONE);// 默认1主动公开

                // 根据batchguid去填并联审批实例标识
                AuditSpITask auditspitask = auditSpITaskService.getAuditSpITaskByProjectGuid(auditProject.getRowguid())
                        .getResult();
                if (StringUtil.isNotBlank(auditspitask.getBatchguid())) {
                    spgl.setBlspslbm(auditspitask.getBatchguid());
                }
                spgl.setSxblsx(audittask.getPromise_day());
                spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);

                // 数据验证，查询主题是否推送成功，事项版本号和流程版本号，流程编码是否能获取数据
                if (!iSpglsplcjdsxxxb.isExistSplcSx(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbbh(),
                        spgl.getSpsxbm())) {
                    spgl.set("sjsczt", "-1");
                    spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
                }
                if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(sjsczt)) {
                    spgl.set("sjsczt", sjsczt);
                    spgl.setSbyy(spgl.getSbyy() + sbyy);
                }
                spgl.setOperatedate(date);
                spgl.setOperateusername("mq生成数据");
                // 3.0新增变更字段
                spgl.setLxr(auditProject.getContactperson());
                spgl.setLxrsjh(auditProject.getContactphone());
                spgl.setYwqx(audittask.getInt("YWQX"));
                // 3.0新增变更字段结束
                ispglxmspsxblxxbV3.insert(spgl);

                // 同送征求信息，新增征求信息，根据部门查询征求
                for (AuditSpIReview auditSpIReview : listreview) {
                    if (audittask.getOuguid().equals(auditSpIReview.getOuguid())) {
                        // 添加记录
                        SpglXmspsxzqyjxxbV3 spglxmspsxzqyjxxb = new SpglXmspsxzqyjxxbV3();
                        spglxmspsxzqyjxxb.setRowguid(UUID.randomUUID().toString());
                        spglxmspsxzqyjxxb.setDfsjzj(
                                MDUtils.md5Hex(auditSpIReview.getRowguid() + auditProject.getRowguid()));
                        spglxmspsxzqyjxxb.setXzqhdm("370800");
                        spglxmspsxzqyjxxb.setGcdm(rsitem.getItemcode());
                        spglxmspsxzqyjxxb.setSpsxslbm(auditProject.getFlowsn());
                        spglxmspsxzqyjxxb.setBldwdm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                        spglxmspsxzqyjxxb.setBldwmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
                        spglxmspsxzqyjxxb.setFksj(auditSpIReview.getPingshengdate());
                        spglxmspsxzqyjxxb.setBlr(
                                iuserservice.getUserNameByUserGuid(auditSpIReview.getPingshenguserguid()));
                        spglxmspsxzqyjxxb.setFkyj(auditSpIReview.getPingshenopinion());
                        spglxmspsxzqyjxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                        spglxmspsxzqyjxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                        // 判断办件信息是否推送成功
                        if (!ispglxmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                            spglxmspsxzqyjxxb.set("sjsczt", "-1");
                            spglxmspsxzqyjxxb.setSbyy(
                                    "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                        iSpglXmspsxzqyjxxbV3.insert(spglxmspsxzqyjxxb);
                    }
                }

                // 插入接件的办理详细信息表
                SpglXmspsxblxxxxbV3 spgl1 = new SpglXmspsxblxxxxbV3();
                spgl1.setRowguid(UUID.randomUUID().toString());
                spgl1.setDfsjzj(auditProject.getRowguid());
                spgl1.setXzqhdm("370800");
                spgl1.setGcdm(rsitem.getItemcode());
                spgl1.setSpsxslbm(auditProject.getFlowsn());

                FrameOu ou = iouservice.getOuByOuGuid(auditProject.getOuguid());
                if (ou != null) {
                    spgl1.setDwmc(ou.getOuname());
                } else {
                    spgl1.setDwmc("错误数据");
                    spgl1.set("sjsczt", "-1");
                    spgl1.setSbyy("单位名称校验有误！");
                }

                FrameOuExtendInfo raExtendInfo = iouservice.getFrameOuExtendInfo(auditProject.getOuguid());
                if (raExtendInfo != null) {
                    spgl1.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                } else {
                    spgl1.set("sjsczt", "-1");
                    spgl1.setSbyy("单位统一社会信用代码校验有误！");
                }
                if (auditTaskExtension != null) {
                    String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                            auditTaskExtension.getStr("handle_system"));
                    if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                        spgl1.setSjly(SYSTEM_NAME);
                    } else {
                        //塞入自己系统默认值
                        spgl1.setSjly("济宁市工程建设项目网上申报系统");
                    }
                }

                String userguid = auditProject.getReceiveuserguid();

                FrameUser frameuser = iuserservice.getUserByUserField("userguid", userguid);
                if (frameuser != null) {
                    spgl1.setBlcs(Zjconstant.getOunamebyuser(frameuser));
                    spgl1.setBlr(frameuser.getDisplayName());
                }
                spgl1.setBlzt(ZwfwConstant.CONSTANT_INT_ONE);
                spgl1.setBlyj(Zjconstant.SPGL_JJ);
                spgl1.setBlsj(auditProject.getApplydate());
                spgl1.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spgl1.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                spgl1.setOperatedate(date);
                spgl.setOperateusername("SpglXmspsxblxxbV3ClientHandle");
                if (!ispglxmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                    spgl1.set("sjsczt", "-1");
                    spgl1.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                }
                iSpglXmspsxblxxxxbV3.insert(spgl1);

            }
            log.info("=======结束调用generatexxbV3接口=======");
            return JsonUtils.zwdtRestReturn("1", "生成办件表成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "失败！", "");
        }
    }

    /**
     * 生成办件流程信息表3.0接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/generatexxxxbV3", method = RequestMethod.POST)
    public String GeneratexxxxbV3(@RequestBody String params) {
        try {
            log.info("=======开始调用generatexxxxbV3接口=======");
            // 1.解析入参
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 办件主键
                String projectID = obj.getString("rowguid");
                // 获取操作
                String operate = obj.getString("operate");
                //办理人
                String opereateuser = obj.getString("opereateuser");
                //办理时间
                String operatedate = obj.getString("operatedate");

                String operatetype = "";

                AuditProject project = iauditproject.getAuditProjectByRowGuid(projectID, "").getResult();
                if (StringUtil.isBlank(project.getBiguid())) {
                    return JsonUtils.zwdtRestReturn("0", "办件没有biguid！", "");
                }
                AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(project.getBiguid()).getResult();
                if (auditspinstance == null) {
                    return JsonUtils.zwdtRestReturn("0", "办件没有instance实例！", "");
                }
                AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(project.getSubappguid()).getResult();
                AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid())
                        .getResult();
                if (baseinfo == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询项目失败！", "");
                }
                AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid())
                        .getResult();
                if (auditspbusiness == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询主题失败！", "");
                }
                String xkbabh = project.getWenhao();
                if (sub != null) {
                    // 判断此事项需不需要传责任主体信息  是则插入责任主体信息
                    List<AuditSpBasetask> result = iTaskCommonService.getAuditSpTask(sub.getBusinessguid(),
                            project.getTask_id()).getResult();
                    if (EpointCollectionUtils.isNotEmpty(result)) {
                        if ("0090001".equals(result.get(0).getStr("taskcodeV3")) ||
                                "0090002".equals(result.get(0).getStr("taskcodeV3")) ||
                                "0090003".equals(result.get(0).getStr("taskcodeV3")) ||
                                "0090004".equals(result.get(0).getStr("taskcodeV3"))) {
                            SpglSgtsjwjscxxbV3 sgtsjwjscxxbV3 = sgtsjwjscxxbV3Service.findDominByCondition("370800", baseinfo.getItemcode());
                            if (sgtsjwjscxxbV3 != null) {
                                //施工图设计文件审查信息表 的 施工图审查业务编号 字段
                                xkbabh = sgtsjwjscxxbV3.getStywbh();
                            }
                        } else if ("0120001".equals(result.get(0).getStr("taskcodeV3")) ||
                                "0120002".equals(result.get(0).getStr("taskcodeV3")) ||
                                "0120003".equals(result.get(0).getStr("taskcodeV3"))) {
                            SpglJzgcsgxkxxbV3 jzgcsgxkxxbV3 = jzgcsgxkxxbV3Service.findDominByCondition("370800", baseinfo.getItemcode(), project.getFlowsn());
                            if (jzgcsgxkxxbV3 != null) {
                                //建筑工程施工许可信息表 的 施工许可证编号 字段
                                xkbabh = jzgcsgxkxxbV3.getSgxkzbh();
                            }
                        } else if ("0180000".equals(result.get(0).getStr("taskcodeV3"))) {
                            SpglJsgcjgysbaxxbV3 jsgcjgysbaxxbV3 = jsgcjgysbaxxbV3Service.findDominByCondition("370800", baseinfo.getItemcode(), project.getFlowsn());
                            if (jsgcjgysbaxxbV3 != null) {
                                //建设工程消防验收备案信息表 的 竣工验收备案编号 字段
                                xkbabh = jsgcjgysbaxxbV3.getJgysbabh();
                            }
                        }
                    }
                }
                    String sjsczt = "0";
                    StringBuilder sbyy = new StringBuilder();// 说明

                    String businessareacode = auditspbusiness.getAreacode();
                    AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
                    if (area != null) {
                        // 如果是县级，查找市级主题
                        if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                            SqlConditionUtil sqlc = new SqlConditionUtil();
                            sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                            // 查找市级辖区
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
                                } else {
                                    sjsczt = "-1";
                                    sbyy.append("该主题类型未存在市级主题中！");
                                }
                            }
                        }
                    }
                    sub.setBusinessguid(auditspbusiness.getRowguid());
                    AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(project.getTaskguid(),
                            false).getResult();
                    FrameOuExtendInfo raExtendInfo = iouservice.getFrameOuExtendInfo(project.getOuguid());

                    String xmdm = "";
                    if (StringUtil.isNotBlank(baseinfo.getParentid())) {
                        AuditRsItemBaseinfo pauditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(
                                baseinfo.getParentid()).getResult();
                        xmdm = pauditRsItemBaseinfo.getItemcode();
                    } else {
                        xmdm = baseinfo.getItemcode();
                    }

                    // 根据mq的中的操作类型处理数据
                    if (StringUtil.isNotBlank(operate)) {
                        switch (operate) {
                            case "accept":
                                operatetype = ZwfwConstant.OPERATE_SL;
                                break;
                            case "notaccept":
                                operatetype = ZwfwConstant.OPERATE_BYSL;
                                break;
                            case "tbcxks":
                                SpglXmspsxblxxxxbV3 ztjs = new SpglXmspsxblxxxxbV3();
                                ztjs.setRowguid(UUID.randomUUID().toString());
                                ztjs.setDfsjzj(projectID);
                                ztjs.setXzqhdm("370800");
                                ztjs.setGcdm(baseinfo.getItemcode());
                                ztjs.setSpsxslbm(project.getFlowsn());
                                ztjs.setBlcs(Zjconstant.getOunamebyuser(opereateuser));
                                ztjs.setBlr(iuserservice.getUserNameByUserGuid(opereateuser));
                                if (StringUtil.isNotBlank(operatedate)) {
                                    ztjs.setBlsj(EpointDateUtil.convertString2Date(operate, EpointDateUtil.DATE_TIME_FORMAT));
                                }
                                ztjs.setBlyj("特别程序（开始）");
                                ztjs.setBlzt(9);
                                ztjs.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                ztjs.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                ztjs.setOperatedate(new Date());
                                // 3.0新增上报字段
                                if (StringUtil.isNotBlank(project.getOuname())) {
                                    ztjs.setDwmc(project.getOuname());
                                } else {
                                    ztjs.setDwmc("错误数据");
                                    ztjs.set("sjsczt", "-1");
                                    ztjs.setSbyy("单位名称校验有误！");
                                }
                                if (raExtendInfo != null) {
                                    ztjs.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                                } else {
                                    ztjs.set("sjsczt", "-1");
                                    ztjs.setSbyy("单位统一社会信用代码校验有误！");
                                }

                                if (auditTaskExtension != null) {
                                    String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                                            auditTaskExtension.getStr("handle_system"));
                                    if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                                        ztjs.setSjly(SYSTEM_NAME);
                                    } else {
                                        //默认塞自己系统
                                        ztjs.setSjly("济宁市工程建设项目网上申报系统");
                                    }
                                }
                                iSpglXmspsxblxxxxbV3.insert(ztjs);
                                operatetype = "tbcxks";
                                break;
                            case "tbcxjs":
                                // 插入结束计时，审核通过的暂停计时
                                SpglXmspsxblxxxxbV3 hfjs = new SpglXmspsxblxxxxbV3();
                                hfjs.setRowguid(UUID.randomUUID().toString());
                                hfjs.setDfsjzj(projectID);
                                hfjs.setXzqhdm("370800");
                                hfjs.setGcdm(baseinfo.getItemcode());
                                hfjs.setSpsxslbm(project.getFlowsn());
                                hfjs.setBlcs(Zjconstant.getOunamebyuser(opereateuser));
                                hfjs.setBlr(iuserservice.getUserNameByUserGuid(opereateuser));
                                if (StringUtil.isNotBlank(operatedate)) {
                                    hfjs.setBlsj(EpointDateUtil.convertString2Date(operate, EpointDateUtil.DATE_TIME_FORMAT));
                                }
                                hfjs.setBlyj("特别程序（结束）");
                                hfjs.setBlzt(10);
                                hfjs.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                hfjs.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                // 3.0新增上报字段
                                hfjs.setDwmc(project.getOuname());
                                if (raExtendInfo != null) {
                                    hfjs.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                                } else {
                                    hfjs.set("sjsczt", "-1");
                                    hfjs.setSbyy("单位统一社会信用代码校验有误！");
                                }

                                if (auditTaskExtension != null) {
                                    String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                                            auditTaskExtension.getStr("handle_system"));
                                    if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                                        hfjs.setSjly(SYSTEM_NAME);
                                    } else {
                                        //默认塞自己系统
                                        hfjs.setSjly("济宁市工程建设项目网上申报系统");
                                    }
                                }
                                iSpglXmspsxblxxxxbV3.insert(hfjs);
                                operatetype = "tbcxjs";
                                break;
                            case "sendresult":
                                operatetype = ZwfwConstant.OPERATE_BJ;
                                // 判断此事项需不需要传责任主体信息  是则插入责任主体信息
                                List<AuditSpBasetask> result = iTaskCommonService.getAuditSpTask(sub.getBusinessguid(),
                                        project.getTask_id()).getResult();
                                if (EpointCollectionUtils.isNotEmpty(result)) {
                                    if (GxhSpConstant.SX_LIST.contains(result.get(0).getStr("taskcodeV3"))) {
                                        //直接推送五方责任主体
                                        List<ParticipantsInfo> participantsinfolist = iSpglZrztxxbV3Service.getParticipantsInfoListBySubappguid(project.getSubappguid());
                                        if (!participantsinfolist.isEmpty()) {
                                            for (ParticipantsInfo participantsInfo : participantsinfolist) {
                                                Integer dwlx = getDwlxByCorptype(participantsInfo.getCorptype());
                                                SpglZrztxxbV3 spglZrztxxbV3 = new SpglZrztxxbV3();
                                                spglZrztxxbV3.setDfsjzj(participantsInfo.getRowguid());
                                                spglZrztxxbV3.setRowguid(UUID.randomUUID().toString());
                                                spglZrztxxbV3.setXzqhdm("370800");
                                                spglZrztxxbV3.setXmdm(xmdm);
                                                spglZrztxxbV3.setGcdm(baseinfo.getItemcode());
                                                spglZrztxxbV3.setSpsxslbm(project.getFlowsn());
                                                spglZrztxxbV3.setXkbabh(xkbabh);
                                                spglZrztxxbV3.setDwtyshxydm(participantsInfo.getCorpcode());
                                                spglZrztxxbV3.setDwmc(participantsInfo.getCorpname());
                                                checkBack("单位名称", participantsInfo.getCorpname());
                                                checkBack("单位统一社会信用代码", participantsInfo.getCorpcode());
                                                spglZrztxxbV3.setDwlx(dwlx);
                                                spglZrztxxbV3.setZzdj(participantsInfo.getStr("CERT"));
                                                spglZrztxxbV3.setFddbr(participantsInfo.getStr("legal"));
                                                if (StringUtil.isNotBlank(participantsInfo.get("legalcardtype"))) {
                                                    spglZrztxxbV3.setFrzjlx(
                                                            Integer.parseInt(participantsInfo.get("legalcardtype")));
                                                }
                                                spglZrztxxbV3.setFrzjhm(participantsInfo.getStr("legalpersonicardnum"));
                                                spglZrztxxbV3.setFzrxm(participantsInfo.getStr("XMFZR"));
                                                if (StringUtil.isNotBlank(participantsInfo.get("fzrzjlx"))) {
                                                    spglZrztxxbV3.setFzrzjlx(
                                                            Integer.parseInt(participantsInfo.get("fzrzjlx")));
                                                }
                                                spglZrztxxbV3.setFzrzjhm(participantsInfo.getStr("xmfzr_idcard"));
                                                spglZrztxxbV3.setFzrlxdh(participantsInfo.getStr("xmfzr_phone"));
                                                spglZrztxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                                spglZrztxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                                spglZrztxxbV3.setSync("0");

                                                // 数据验证,项目代码是否在工程代码
                                                if (!iSpglXmjbxxbV3.isExistGcdm(baseinfo.getItemcode())) {
                                                    spglZrztxxbV3.set("sjsczt", "-1");
                                                    spglZrztxxbV3.setSbyy(
                                                            "单位工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                                                }
                                                iSpglZrztxxbV3Service.insert(spglZrztxxbV3);
                                            }
                                        }

                                        // 生成单体信息
                                        List<DantiSubRelation> findListBySubappGuid = iDantiSubRelationService.findListBySubappGuid(
                                                project.getSubappguid());
                                        for (DantiSubRelation dantiSubRelation : findListBySubappGuid) {
                                            DantiInfoV3 dantiInfoV3 = iDantiInfoV3Service.find(dantiSubRelation.getDantiguid());
                                            if (dantiInfoV3 != null) {
                                                SpglXmdtxxbV3 spglXmdtxxbV3 = new SpglXmdtxxbV3();
                                                spglXmdtxxbV3.setRowguid(UUID.randomUUID().toString());
                                                spglXmdtxxbV3.setDfsjzj(dantiInfoV3.getRowguid());
                                                spglXmdtxxbV3.setXzqhdm("370800");
                                                spglXmdtxxbV3.setXmdm(xmdm);
                                                spglXmdtxxbV3.setGcdm(baseinfo.getItemcode());
                                                spglXmdtxxbV3.setSpsxslbm(project.getFlowsn());
                                                spglXmdtxxbV3.setXkbabh(xkbabh);
                                                spglXmdtxxbV3.setDtmc(dantiInfoV3.getDtmc());
                                                spglXmdtxxbV3.setDtbm(dantiInfoV3.getDtbm());
                                                spglXmdtxxbV3.setGcyt(dantiInfoV3.getGcyt());
                                                spglXmdtxxbV3.setGmzb(dantiInfoV3.getGmzb());
                                                spglXmdtxxbV3.setJgtx(dantiInfoV3.getJgtx());
                                                spglXmdtxxbV3.setNhdj(dantiInfoV3.getNhdj());
                                                spglXmdtxxbV3.setJzfs(dantiInfoV3.getJzfs());
                                                spglXmdtxxbV3.setDtjwdzb(dantiInfoV3.getDtjwdzb());
                                                spglXmdtxxbV3.setDtgczzj(dantiInfoV3.getDtgczzj());
                                                spglXmdtxxbV3.setJzmj(dantiInfoV3.getJzmj());
                                                spglXmdtxxbV3.setDsjzmj(dantiInfoV3.getDsjzmj());
                                                spglXmdtxxbV3.setDxjzmj(dantiInfoV3.getDxjzmj());
                                                spglXmdtxxbV3.setZdmj(dantiInfoV3.getZdmj());
                                                spglXmdtxxbV3.setJzgcgd(dantiInfoV3.getJzgcgd());
                                                spglXmdtxxbV3.setDscs(dantiInfoV3.getDscs());
                                                spglXmdtxxbV3.setDxcs(dantiInfoV3.getDxcs());
                                                spglXmdtxxbV3.setCd(dantiInfoV3.getCd());
                                                spglXmdtxxbV3.setKd(dantiInfoV3.getKd());
                                                spglXmdtxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                                spglXmdtxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                                spglXmdtxxbV3.setSync("0");
                                                spglXmdtxxbV3.setSbyy("");
                                                iSpglXmdtxxbV3Service.insert(spglXmdtxxbV3);
                                            }

                                        }

                                    }
                                }
                                break;
                            case "finishfile":
                                // 查入批复文件信息
                                SpglXmspsxpfwjxxbV3 jxxb = new SpglXmspsxpfwjxxbV3();
                                jxxb.setDfsjzj(project.getRowguid());
                                jxxb.setXzqhdm("370800");
                                jxxb.setGcdm(baseinfo.getItemcode());
                                jxxb.setSpsxslbm(project.getFlowsn());
                                jxxb.setPfrq(project.getCertificatedate());
                                jxxb.setPfwh(project.getWenhao());
                                jxxb.setPfwjbt("批文");
                                jxxb.setPfwjyxqx(EpointDateUtil.convertString2DateAuto("9999-01-01"));
                                // 3.0上报新增字段
                                AuditTaskResult auditTaskResult = iAuditTaskResult.getAuditResultByTaskGuid(
                                        project.getTaskguid(), false).getResult();
                                if (auditTaskResult != null) {
                                    jxxb.setSpjglx(("40").equals(auditTaskResult.getResulttype().toString())
                                            ? "30"
                                            : auditTaskResult.getResulttype().toString());
                                }
                                CertInfo certInfo = certInfoExternalImpl.getCertInfoByRowguid(project.getCenterguid());
                                String picUrl = "";
                                if (certInfo != null) {
                                    jxxb.setZzbh(certInfo.getCertno());
                                    jxxb.setZzbs(project.getCenterguid());
                                    List<JSONObject> certAttachList = iCertAttachExternal.getAttachList(
                                            certInfo.getCertcliengguid(), project.getAreacode());
                                    if (EpointCollectionUtils.isNotEmpty(certAttachList)) {
                                        JSONObject info = certAttachList.get(0);
                                        picUrl = iCertAttachExternal.getAttachScan(info.get("attachguid").toString(), "");

                                    }
                                    jxxb.setDzzzwjlj(picUrl);
                                }
                                // 3.0上报新增字段结束
                                List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(projectID);
                                if (attachlist == null) {
                                    break;
                                }
                                // 多个批文文件附件处理，插入多次
                                for (FrameAttachInfo frameAttachInfo : attachlist) {
                                    jxxb.setRowguid(UUID.randomUUID().toString());
                                    jxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                                    jxxb.setFjid(frameAttachInfo.getAttachGuid());
                                    jxxb.setFjmc(frameAttachInfo.getAttachFileName());
                                    jxxb.setFjlx(frameAttachInfo.getContentType());
                                    jxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                    jxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                    // 数据验证,项目代码是否在工程代码
                                    if (!iSpglXmjbxxbV3.isExistGcdm(baseinfo.getItemcode())) {
                                        jxxb.set("sjsczt", "-1");
                                        jxxb.setSbyy("批文工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                                    }
                                    if (!ispglxmspsxblxxbV3.isExistFlowsn(project.getFlowsn())) {
                                        jxxb.set("sjsczt", "-1");
                                        if (StringUtil.isNotBlank(jxxb.getSbyy())) {
                                            jxxb.setSbyy(jxxb.getSbyy()
                                                    + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                        } else {
                                            jxxb.setSbyy(
                                                    "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                        }
                                    }
                                    ispglxmspsxpfwjxxb.insert(jxxb);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    if (StringUtil.isBlank(operatetype)) {
                        return JsonUtils.zwdtRestReturn("0", "操作无数据！", "");
                    }
                    if ("tbcxks".equals(operatetype) || "tbcxjs".equals(operatetype)) {
                        return JsonUtils.zwdtRestReturn("1", "生成流程表成功！", "");
                    }
                    AuditProjectOperation auditProjectOperation = operationservice.getAuditOperation(projectID, operatetype)
                            .getResult();
                    if (auditProjectOperation == null) {
                        return JsonUtils.zwdtRestReturn("0", "无本地流程表数据！", "");
                    }
                    SpglXmspsxblxxxxbV3 spgl = new SpglXmspsxblxxxxbV3();
                    spgl.setRowguid(UUID.randomUUID().toString());
                    spgl.setOperatedate(new Date());
                    spgl.setDfsjzj(project.getRowguid());
                    spgl.setGcdm(baseinfo.getItemcode());
                    spgl.setSpsxslbm(project.getFlowsn());
                    spgl.setBlcs(Zjconstant.getOunamebyuser(auditProjectOperation.getOperateUserGuid()));
                    spgl.setBlr(auditProjectOperation.getOperateusername());
                    spgl.setBlsj(auditProjectOperation.getOperatedate());
                    spgl.setBlyj(auditProjectOperation.getRemarks());
                    // 3.0新增上报字段
                    if (StringUtil.isNotBlank(project.getOuname())) {
                        spgl.setDwmc(project.getOuname());
                    } else {
                        spgl.setDwmc("错误数据");
                        spgl.set("sjsczt", "-1");
                        spgl.setSbyy("单位名称校验有误！");
                    }
                    if (raExtendInfo != null) {
                        spgl.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                    } else {
                        spgl.set("sjsczt", "-1");
                        spgl.setSbyy("单位统一社会信用代码校验有误！");
                    }

                    if (auditTaskExtension != null) {
                        String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                                auditTaskExtension.getStr("handle_system"));
                        if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                            spgl.setSjly(SYSTEM_NAME);
                        } else {
                            //默认塞自己系统
                            spgl.setSjly("济宁市工程建设项目网上申报系统");
                        }
                    }

                    if (ZwfwConstant.OPERATE_SL.equals(operatetype)) {
                        spgl.setBlzt(3);
                        // 受理查询工作流意见
                        spgl.setBlyj(Zjconstant.SPGL_SL);
                        // 受理时推送所有办件材料
                        List<AuditProjectMaterial> materil = iauditprojectmaterial.selectProjectMaterial(project.getRowguid())
                                .getResult();
                        if (materil == null) {
                            return JsonUtils.zwdtRestReturn("0", "办件无材料！", "");
                        }
                        for (AuditProjectMaterial auditProjectMaterial : materil) {
                            List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(
                                    auditProjectMaterial.getCliengguid());
                            for (FrameAttachInfo frameAttachInfo : attachlist) {
                                SpglsqcljqtfjxxbV3 fxxb = new SpglsqcljqtfjxxbV3();
                                fxxb.setRowguid(UUID.randomUUID().toString());
                                fxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                                fxxb.setXzqhdm("370800");
                                fxxb.setGcdm(baseinfo.getItemcode());
                                fxxb.setSpsxslbm(project.getFlowsn());
                                fxxb.setBlspslbm(project.getSubappguid());
                                // 查询materialid
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(
                                        auditProjectMaterial.getTaskmaterialguid()).getResult();
                                if (auditTaskMaterial != null) {
                                    fxxb.setClmlbh(
                                            StringUtil.isNotBlank(auditTaskMaterial.get("CLMLBH")) ? auditTaskMaterial.get(
                                                    "CLMLBH") : auditTaskMaterial.getMaterialid());
                                } else {
                                    fxxb.setClmlbh("");
                                    fxxb.set("sjsczt", "-1");
                                    fxxb.setSbyy("材料目录编号校验失败!");
                                }
                                fxxb.setClmc(frameAttachInfo.getAttachFileName());
                                fxxb.setClfl(ZwfwConstant.CONSTANT_INT_ONE);// 默认为1，申报材料

                                fxxb.setCllx(frameAttachInfo.getContentType());
                                fxxb.setClid(frameAttachInfo.getAttachGuid());
                                fxxb.setSqfs(2);
                                fxxb.setSqsl(0);
                                fxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                fxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                // 数据验证,项目代码是否在工程代码
                                if (!iSpglXmjbxxbV3.isExistGcdm(baseinfo.getItemcode())) {
                                    fxxb.set("sjsczt", "-1");
                                    fxxb.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                                }
                                if (!ispglxmspsxblxxbV3.isExistFlowsn(project.getFlowsn())) {
                                    fxxb.set("sjsczt", "-1");
                                    if (StringUtil.isNotBlank(fxxb.getSbyy())) {
                                        fxxb.setSbyy(fxxb.getSbyy()
                                                + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                    } else {
                                        fxxb.setSbyy(
                                                "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                    }
                                }
                                iSpglsqcljqtfjxxbV3.insert(fxxb);
                            }
                        }

                    } else if (ZwfwConstant.OPERATE_BJ.equals(operatetype)) {
                        AuditProjectOperation operation = operationservice.getAuditOperation(projectID,
                                ZwfwConstant.OPERATE_SPBTG).getResult();
                        spgl.setBlyj(Zjconstant.SPGL_BJ);
                        // 没有审批不通过，则为审批通过
                        if (operation == null) {
                            spgl.setBlzt(11);
                        } else {
                            spgl.setBlzt(13);
                        }
                    }
                    spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    if (!iSpglXmjbxxbV3.isExistGcdm(spgl.getGcdm())) {
                        spgl.set("sjsczt", "-1");
                        spgl.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                    }
                    if (!ispglxmspsxblxxbV3.isExistFlowsn(spgl.getSpsxslbm())) {
                        spgl.set("sjsczt", "-1");
                        if (StringUtil.isNotBlank(spgl.getSbyy())) {
                            spgl.setSbyy(spgl.getSbyy()
                                    + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        } else {
                            spgl.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                    }

                    // 判断是否秒板
                    if (spgl.getBlzt() == 11 || spgl.getBlzt() == 13) {
                        // 承诺件
                        if (ZwfwConstant.CONSTANT_INT_TWO == project.getTasktype()) {

                            Date allstart = project.getReceivedate();
                            Date allend = new Date();
                            if (project.getStatus() >= ZwfwConstant.BANJIAN_STATUS_ZCBJ && spgl.getBlsj() != null) {
                                allend = project.getBanjiedate();
                            }
                            long zttime = 0;
                            Date start = null;
                            SqlConditionUtil sqlc = new SqlConditionUtil();
                            sqlc.eq("spsxslbm", spgl.getSpsxslbm());
                            sqlc.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                            sqlc.setOrderAsc("blsj");
                            List<SpglXmspsxblxxxxbV3> listblxxxxb = iSpglXmspsxblxxxxbV3.getListByCondition(sqlc.getMap())
                                    .getResult();
                            // 过滤草稿事项
                            listblxxxxb = listblxxxxb.stream()
                                    .filter(a -> !ZwfwConstant.CONSTANT_STR_NEGATIVE_ONE.equals(a.getStr("sync")))
                                    .collect(Collectors.toList());
                            for (SpglXmspsxblxxxxbV3 spglXmspsxblxxxxb : listblxxxxb) {
                                if (9 == spglXmspsxblxxxxb.getBlzt()) {
                                    start = spglXmspsxblxxxxb.getBlsj();
                                }
                                if (10 == spglXmspsxblxxxxb.getBlzt() && start != null) {
                                    // 计算非工作日耗时
                                    long diff = spglXmspsxblxxxxb.getBlsj().getTime() - start.getTime();
                                    // 计算工作日天数
                                    int daysnum = ZwfwUtil.weekDays(start, spglXmspsxblxxxxb.getBlsj());
                                    zttime += (diff - (daysnum * 1000 * 60 * 60 * 24));
                                    start = null;
                                }
                            }
                            // 暂停计时中，还没有回复计时
                            if (start != null) {
                                // 插入结束计时，审核通过的暂停计时
                                SpglXmspsxblxxxxbV3 hfjs = new SpglXmspsxblxxxxbV3();
                                hfjs.setRowguid(UUID.randomUUID().toString());
                                hfjs.setDfsjzj(projectID);
                                hfjs.setXzqhdm("370800");
                                hfjs.setGcdm(baseinfo.getItemcode());
                                hfjs.setSpsxslbm(project.getFlowsn());
                                hfjs.setBlcs(spgl.getBlcs());
                                hfjs.setBlr(spgl.getBlr());
                                hfjs.setBlsj(spgl.getBlsj());
                                hfjs.setBlyj("特别程序（结束）");
                                hfjs.setBlzt(10);
                                hfjs.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                hfjs.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                // 3.0新增上报字段
                                hfjs.setDwmc(project.getOuname());
                                if (raExtendInfo != null) {
                                    hfjs.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                                } else {
                                    hfjs.set("sjsczt", "-1");
                                    hfjs.setSbyy("单位统一社会信用代码校验有误！");
                                }

                                if (auditTaskExtension != null) {
                                    String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                                            auditTaskExtension.getStr("handle_system"));
                                    if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                                        hfjs.setSjly(SYSTEM_NAME);
                                    } else {
                                        //默认塞自己系统
                                        hfjs.setSjly("济宁市工程建设项目网上申报系统");
                                    }
                                }
                                if (!iSpglXmjbxxbV3.isExistGcdm(hfjs.getGcdm())) {
                                    hfjs.set("sjsczt", "-1");
                                    hfjs.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                                }
                                if (!ispglxmspsxblxxbV3.isExistFlowsn(hfjs.getSpsxslbm())) {
                                    hfjs.set("sjsczt", "-1");
                                    if (StringUtil.isNotBlank(hfjs.getSbyy())) {
                                        hfjs.setSbyy(hfjs.getSbyy()
                                                + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                    } else {
                                        hfjs.setSbyy(
                                                "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                    }
                                }
                                iSpglXmspsxblxxxxbV3.insert(hfjs);

                                // 计算非工作日耗时
                                long diff = spgl.getBlsj().getTime() - start.getTime();
                                // 计算工作日天数
                                int daysnum = ZwfwUtil.weekDays(start, spgl.getBlsj());
                                zttime += (diff - (daysnum * 1000 * 60 * 60 * 24));
                                start = null;
                            }
                            int alldaysnum = ZwfwUtil.weekDays(allstart, allend);
                            long alltime = allend.getTime() - allstart.getTime() - (alldaysnum * 1000 * 60 * 60 * 24);
                            long usetime = (alltime - zttime) / (1000 * 60);
                            long hour = usetime / 60;
                            long days = hour / 24;
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(Zjconstant.validata_mb) && hour < Zjconstant.MB_HOUR) {
                                spgl.set("sjsczt", "-1");
                                if (StringUtil.isNotBlank(spgl.getSbyy())) {
                                    spgl.setSbyy(spgl.getSbyy() + ";受理和办结时间相差小于" + Zjconstant.MB_HOUR
                                            + "小时，时间较短判断为秒办！");
                                } else {
                                    spgl.setSbyy("受理和办结时间相差小于" + Zjconstant.MB_HOUR + "小时，时间较短判断为秒办！");
                                }
                            }
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(Zjconstant.validata_yq)) {
                                // 判断办件信息表
                                SpglXmspsxblxxbV3 spglxmspsxblxxb = ispglxmspsxblxxbV3.getSpglXmspsxblxxbBySlbm(
                                        project.getFlowsn());
                                if (spglxmspsxblxxb != null) {
                                    if (days >= spglxmspsxblxxb.getSxblsxm()) {
                                        spgl.set("sjsczt", "-1");
                                        if (StringUtil.isNotBlank(spgl.getSbyy())) {
                                            spgl.setSbyy(spgl.getSbyy() + "; 办件以超期！");
                                        } else {
                                            spgl.setSbyy("办件以超期！");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    spgl.setXzqhdm("370800");
                    spgl.setSpsxslbm(project.getFlowsn());
                    iSpglXmspsxblxxxxbV3.insert(spgl);
                    if (3 == spgl.getBlzt()) {
                        spgl.setRowguid(UUID.randomUUID().toString());
                        spgl.setBlzt(8);
                        iSpglXmspsxblxxxxbV3.insert(spgl);
                    } else {
                        iSpglXmspsxblxxxxbV3.insert(spgl);
                    }
                }
                log.info("=======结束调用generatexxxxbV3接口=======");
                return JsonUtils.zwdtRestReturn("1", "生成流程表成功！", "");
            } catch(Exception e){
                e.printStackTrace();
                return JsonUtils.zwdtRestReturn("0", "失败！", "");
            }
        }

        /**
         * 生成批复文件信息表3.0接口
         *
         * @param params 接口的入参
         * @return
         */
        @RequestMapping(value = "/generatesqclV3", method = RequestMethod.POST)
        public String GeneratesqclV3 (@RequestBody String params){
            try {
                log.info("=======开始调用generatesqclV3接口=======");
                // 1.解析入参
                JSONObject jsonObject = JSONObject.parseObject(params);
                String token = jsonObject.getString("token");
                JSONObject obj = (JSONObject) jsonObject.get("params");
                if (ZwdtConstant.SysValidateData.equals(token)) {
                    String flowsn = obj.getString("flowsn");
                    AuditProject project = iauditproject.getAuditProjectByFlowsn(flowsn, "").getResult();
                    if (project != null) {
                        AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(project.getSubappguid()).getResult();
                        if (sub == null) {
                            return JsonUtils.zwdtRestReturn("0", "查询子申报实例失败！", "");
                        }

                        AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid()).getResult();
                        if (baseinfo == null) {
                            return JsonUtils.zwdtRestReturn("0", "查询项目失败！", "");
                        }
                        // 受理时推送所有办件材料
                        List<AuditProjectMaterial> materil = iauditprojectmaterial.selectProjectMaterial(project.getRowguid())
                                .getResult();
                        if (materil == null) {
                            return JsonUtils.zwdtRestReturn("0", "办件查询材料失败！", "");
                        }
                        for (AuditProjectMaterial auditProjectMaterial : materil) {
                            List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(
                                    auditProjectMaterial.getCliengguid());
                            for (FrameAttachInfo frameAttachInfo : attachlist) {
                                SpglsqcljqtfjxxbV3 fxxb = new SpglsqcljqtfjxxbV3();
                                fxxb.setRowguid(UUID.randomUUID().toString());
                                fxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                                fxxb.setXzqhdm("370800");
                                fxxb.setGcdm(baseinfo.getItemcode());
                                fxxb.setSpsxslbm(project.getFlowsn());
                                fxxb.setBlspslbm(project.getSubappguid());
                                // 查询materialid
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(
                                        auditProjectMaterial.getTaskmaterialguid()).getResult();
                                if (auditTaskMaterial != null) {
                                    fxxb.setClmlbh(
                                            StringUtil.isNotBlank(auditTaskMaterial.get("CLMLBH")) ? auditTaskMaterial.get(
                                                    "CLMLBH") : auditTaskMaterial.getMaterialid());
                                } else {
                                    fxxb.setClmlbh("");
                                    fxxb.set("sjsczt", "-1");
                                    fxxb.setSbyy("材料目录编号校验失败!");
                                }
                                fxxb.setClmc(frameAttachInfo.getAttachFileName());
                                fxxb.setClfl(ZwfwConstant.CONSTANT_INT_ONE);// 默认为1，申报材料
                                fxxb.setCllx(frameAttachInfo.getContentType());
                                fxxb.setClid(frameAttachInfo.getAttachGuid());
                                fxxb.setSqfs(2);
                                fxxb.setSqsl(0);
                                fxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                fxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                iSpglsqcljqtfjxxbV3.insert(fxxb);
                            }
                        }
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "办件查询失败！", "");
                    }
                }
                log.info("=======结束调用generatesqclV3接口=======");
                return JsonUtils.zwdtRestReturn("1", "生成申请材料表成功！", "");
            } catch (Exception e) {
                e.printStackTrace();
                return JsonUtils.zwdtRestReturn("0", "失败！", "");
            }
        }

        /**
         * 生成批复文件信息表3.0接口
         *
         * @param params 接口的入参
         * @return
         */
        @RequestMapping(value = "/generatepfwjV3", method = RequestMethod.POST)
        public String GeneratePfwjV3 (@RequestBody String params){
            try {
                log.info("=======开始调用generatepfwjV3接口=======");
                // 1.解析入参
                JSONObject jsonObject = JSONObject.parseObject(params);
                String token = jsonObject.getString("token");
                JSONObject obj = (JSONObject) jsonObject.get("params");
                if (ZwdtConstant.SysValidateData.equals(token)) {
                    String flowsn = obj.getString("flowsn");
                    AuditProject project = iauditproject.getAuditProjectByFlowsn(flowsn, "").getResult();
                    if (project != null) {
                        // 查入批复文件信息
                        SpglXmspsxpfwjxxbV3 jxxb = new SpglXmspsxpfwjxxbV3();
                        jxxb.setDfsjzj(project.getRowguid());
                        jxxb.setOperatedate(new Date());
                        jxxb.setXzqhdm("370800");
                        AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(project.getBiguid()).getResult();
                        if (auditspinstance != null) {
                            AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(project.getSubappguid()).getResult();
                            if (sub != null) {
                                AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid())
                                        .getResult();
                                if (baseinfo != null) {
                                    jxxb.setGcdm(baseinfo.getItemcode());
                                }
                            }
                        }
                        jxxb.setSpsxslbm(project.getFlowsn());
                        jxxb.setPfrq(project.getCertificatedate());
                        jxxb.setPfwh(flowsn);
                        jxxb.setPfwjyxqx(EpointDateUtil.convertString2DateAuto("9999-01-01"));
                        jxxb.setSpjglx("30");
                        // 3.0上报新增字段
                        AuditTask auditTask = iaudittask.getAuditTaskByGuid(
                                project.getTaskguid(), false).getResult();
                        if (auditTask != null) {
                            jxxb.setPfwjbt(auditTask.getTaskname());
                        }
                        CertInfo certInfo = certInfoExternalImpl.getCertInfoByRowguid(project.getCenterguid());
                        String picUrl = "";
                        if (certInfo != null) {
                            jxxb.setZzbh(certInfo.getCertno());
                            jxxb.setZzbs(project.getCenterguid());
                            List<JSONObject> certAttachList = iCertAttachExternal.getAttachList(
                                    certInfo.getCertcliengguid(), project.getAreacode());
                            if (EpointCollectionUtils.isNotEmpty(certAttachList)) {
                                JSONObject info = certAttachList.get(0);
                                picUrl = iCertAttachExternal.getAttachScan(info.get("attachguid").toString(), "");

                            }
                            jxxb.setDzzzwjlj(picUrl);
                        }
                        // 3.0上报新增字段结束
                        List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(project.getRowguid());
                        if (!attachlist.isEmpty()) {
                            // 多个批文文件附件处理，插入多次
                            for (FrameAttachInfo frameAttachInfo : attachlist) {
                                jxxb.setRowguid(UUID.randomUUID().toString());
                                jxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                                jxxb.setFjid(frameAttachInfo.getAttachGuid());
                                jxxb.setFjmc(frameAttachInfo.getAttachFileName());
                                jxxb.setFjlx(frameAttachInfo.getContentType());
                                jxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                jxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                ispglxmspsxpfwjxxb.insert(jxxb);
                            }
                        } else {
                            jxxb.setRowguid(UUID.randomUUID().toString());
                            jxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            jxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            ispglxmspsxpfwjxxb.insert(jxxb);
                        }
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "办件查询失败！", "");
                    }
                }
                log.info("=======结束调用generatepfwjV3接口=======");
                return JsonUtils.zwdtRestReturn("1", "生成批复文件表成功！", "");
            } catch (Exception e) {
                e.printStackTrace();
                return JsonUtils.zwdtRestReturn("0", "失败！", "");
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
        public boolean isInCode (String codename, Object value,boolean a){
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

        public void checkBack (String field, Object o){
            if (o == null) {
                throw new RuntimeException(field + "的值不能为空！");
            }
        }

        private Integer getDwlxByCorptype (String corptype){
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
