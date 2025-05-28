package com.epoint.ggyjstj;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcjgysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcjgysbaxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZrztxxbV3Service;
import com.epoint.xmz.thirdreporteddata.common.GxhSpConstant;
import com.epoint.xmz.thirdreporteddata.task.commonapi.domain.YwxxRelationMapping;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController("taskprojectdetailaction")
@Scope("request")
public class TaskProjectDetailAction extends BaseController {
    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;
    @Autowired
    private IAuditProject auditProjectService;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private ITaskCommonService iTaskCommonService;
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    @Autowired
    private IAuditSpPhase iauditspphase;

    @Autowired
    private ISpglJsgcjgysbaxxbV3Service spglJsgcjgysbaxxbV3Service;
    @Autowired
    private IOuService iOuService;

    @Autowired
    private IAuditSpSpJgysService iAuditSpSpJgysService;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    @Autowired
    private IConfigService configServicce;
    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;
    /**
     * 办件表实体对象
     */
    private AuditProject auditproject = null;
    private SpglJsgcjgysbaxxbV3 spgljsgcjgysbaxxbv3 = null;
    /**
     * biguid
     */
    private String biGuid = "";
    private String projectguid = "";
    private String applyWay_gexinghua = "";
    private String certtype_gexinghua = "";
    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("biguid");
        AuditSpInstance spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        if (spInstance != null) {
            dataBean = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(spInstance.getYewuguid()).getResult();
        }
        String fields = "ACCEPTUSERDATE,is_miaopi,xiangmubm,xiangmuname,AREAALL,AREABUILD,INVESTMENT,PROJECTCONTENT,PROJECTALLOWEDNO,businessguid,biguid,subappguid,rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,tasktype,spendtime,banjieresult,flowsn,sparetime,is_test,applydate,applyertype,certnum,certtype,address,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,contactcertnum,remark,banjiedate,If_express,legal,centerguid,is_charge,hebingshoulishuliang,Certrowguid,Certdocguid,acceptareacode,legalid,ISSYNACWAVE,dataObj_baseinfo,task_id";
        String areacode = "";
        projectguid = getRequestParameter("projectguid");
        auditproject = auditProjectService.getAuditProjectByRowGuid(fields, projectguid, "")
                .getResult();
        if (auditproject!=null){
            applyWay_gexinghua = codeItemsService.getItemTextByCodeName("申请方式", auditproject.getApplyway().toString());
            if (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_SFZ)) {
                certtype_gexinghua = "身份证";
            }
            else if (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_GSYYZZ)) {
                certtype_gexinghua = "工商营业执照";
            }
            else if (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_TYSHXYDM)) {
                certtype_gexinghua = "统一社会信用代码";
            }
            else if (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_ZZJGDMZ)) {
                certtype_gexinghua = "组织机构代码证";
            }
            if ("".equals(applyWay_gexinghua)) {
                applyWay_gexinghua = "未知方式";
            }
            // 3.0个性化表单 展示在办结页面
            //房屋建筑和市政基础设施工程竣工验收备案事项特殊处理：导出字段为项目基本信息 + 个性化表单信息（spgl_jsgcjgysbaxxb_v3）表
            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
            if (auditTask==null){
                return;
            }
            if (auditTask!=null && !"房屋建筑和市政基础设施工程竣工验收备案".equals(auditTask.getTaskname())){
                addCallbackParam("hiddenform","1");
            }
            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(),false).getResult();
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditproject.getSubappguid()).getResult();
            if (auditSpISubapp != null) {
                AuditRsItemBaseinfo auditRsItemBaseinfo = rsItemBaseinfoService
                        .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                if (auditRsItemBaseinfo != null) {
                    String xzqhdm = "370800";
                    String gcdm = auditRsItemBaseinfo.getItemcode();
                    String spsxslbm =auditproject.getFlowsn();
                    spgljsgcjgysbaxxbv3 = spglJsgcjgysbaxxbV3Service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
                    if (spgljsgcjgysbaxxbv3 == null) {
                        spgljsgcjgysbaxxbv3 = new SpglJsgcjgysbaxxbV3();
                        spgljsgcjgysbaxxbv3.setXzqhdm(xzqhdm);
                        spgljsgcjgysbaxxbv3.setSpsxslbm(spsxslbm);
                        spgljsgcjgysbaxxbv3.setGcdm(gcdm);
                        spgljsgcjgysbaxxbv3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                        spgljsgcjgysbaxxbv3.setDfsjzj(UUID.randomUUID().toString());
                        spgljsgcjgysbaxxbv3.setBarq(new Date());
                        if (auditproject != null) {
                            FrameOuExtendInfo raExtendInfo = iOuService.getFrameOuExtendInfo(auditproject.getOuguid());
                            if (auditTask != null) {
                                spgljsgcjgysbaxxbv3.setBajg(auditTask.getOuname());
                            }

                            if (raExtendInfo != null) {
                                spgljsgcjgysbaxxbv3.setBajgxydm(raExtendInfo.getStr("orgcode"));
                            }
                            spgljsgcjgysbaxxbv3.setLxr(auditproject.getContactperson());
                            spgljsgcjgysbaxxbv3.setLxrsjh(auditproject.getContactmobile());

                            AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(auditproject.getSubappguid());
                            if (auditSpSpJgys != null) {
                                spgljsgcjgysbaxxbv3.setSgxkzbh(auditSpSpJgys.getJzzh());//施工许可证编号
                                spgljsgcjgysbaxxbv3.setZjzmj(auditSpSpJgys.getAllbuildarea()); // 总建筑面积（m²）
                                spgljsgcjgysbaxxbv3.setDsjzmj(auditSpSpJgys.getOverloadarea()); // 地上
                                spgljsgcjgysbaxxbv3.setDxjzmj(auditSpSpJgys.getDownloadarea()); // 地下
                                spgljsgcjgysbaxxbv3.setKgrq(auditSpSpJgys.getStartdate());
                                spgljsgcjgysbaxxbv3.setJgrq(auditSpSpJgys.getEnddate());
                            }
                        }

                        // 加载建设单位信息
                        List<ParticipantsInfo> participantsInfos = iTaskCommonService.getJsdwInfor(gcdm).getResult();
                        if (EpointCollectionUtils.isNotEmpty(participantsInfos)) {
                            ParticipantsInfo participantsInfo = participantsInfos.get(0);
                            // 初始化建设单位信息
                            spgljsgcjgysbaxxbv3.setJsdw(participantsInfo.getCorpname());
                            spgljsgcjgysbaxxbv3.setJsdwdm(participantsInfo.getCorpcode());
                            spgljsgcjgysbaxxbv3.setJsdwlx(participantsInfo.get("JSDWLX"));
                            spgljsgcjgysbaxxbv3.setJsdwxmfzr(participantsInfo.getXmfzr());
                            spgljsgcjgysbaxxbv3.setJsfzrzjhm(participantsInfo.getXmfzr_idcard());
                            spgljsgcjgysbaxxbv3.setJsfzrzjlx(participantsInfo.get("FRZZJLX"));
                            spgljsgcjgysbaxxbv3.setJsfzrlxdh(participantsInfo.getXmfzr_phone());
                            // 初始化项目信息
                            spgljsgcjgysbaxxbv3.setGcmc(participantsInfo.get("GCMC"));
                            spgljsgcjgysbaxxbv3.setJsdz(participantsInfo.get("JSDZ"));
                            spgljsgcjgysbaxxbv3.setJsgm(participantsInfo.get("JSGM"));
                            spgljsgcjgysbaxxbv3.setXmjwdzb(participantsInfo.get("XMJWDZB"));
                            spgljsgcjgysbaxxbv3.setSsqx(participantsInfo.get("SSQX"));
                            if ("1".equals(participantsInfo.getStr("GCHYFL"))) {
                                addCallbackParam("flag", "1");
                            }
                        }
                    }
                }
            }
            if (auditTaskExtension != null) {
                addCallbackParam("newformid", auditTaskExtension.get("formid")); //
//                addCallbackParam("newformid", "777"); //
                addCallbackParam("eformCommonPage", configServicce.getFrameConfigValue("eformCommonPage"));
                addCallbackParam("epointUrl", configServicce.getFrameConfigValue("epointsformurl"));
            }

            // 工改单事项表单展示
            if (StringUtil.isNotBlank(auditproject.getSubappguid())) {
                addCallbackParam("subappguid", auditproject.getSubappguid());
                if (auditTaskExtension!=null) {
                    String formids = auditTaskExtension.getStr("formid");
                    if (StringUtil.isNotBlank(formids)) {
                        AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditproject.getSubappguid()).getResult();
                        AuditSpPhase auditSpPhase = iauditspphase.getAuditSpPhaseByRowguid(subapp.getPhaseguid()).getResult();
                        if (StringUtil.isNotBlank(auditSpPhase.getStr("formid"))) {
                            addCallbackParam("formid", auditSpPhase.getStr("formid"));
                            addCallbackParam("formids", formids);
                            addCallbackParam("yewuGuid", subapp.getRowguid());
                            addCallbackParam("eformCommonPage", configServicce.getFrameConfigValue("eformCommonPage"));
                            addCallbackParam("epointUrl", configServicce.getFrameConfigValue("epointsformurl"));
                        }
                    }
                }
            }
        }
        else {
            addCallbackParam("hiddenform","1");
        }
    }

    public AuditRsItemBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean) {
        this.dataBean = dataBean;
    }
    public AuditProject getAuditproject() {
        return auditproject;
    }

    public void setAuditproject(AuditProject auditproject) {
        this.auditproject = auditproject;
    }
    public SpglJsgcjgysbaxxbV3 getSpgljsgcjgysbaxxbv3() {
        return spgljsgcjgysbaxxbv3;
    }

    public void setSpgljsgcjgysbaxxbv3(SpglJsgcjgysbaxxbV3 spgljsgcjgysbaxxbv3) {
        this.spgljsgcjgysbaxxbv3 = spgljsgcjgysbaxxbv3;
    }
    public String getApplyWay_gexinghua() {
        return applyWay_gexinghua;
    }
    public void setApplyWay_gexinghua(String applyWay_gexinghua) {
        this.applyWay_gexinghua = applyWay_gexinghua;
    }
    public void setCerttype_gexinghua(String certtype_gexinghua) {
        this.certtype_gexinghua = certtype_gexinghua;
    }

    public String getCerttype_gexinghua() {
        return certtype_gexinghua;
    }
}

