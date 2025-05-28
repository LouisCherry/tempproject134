package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcjgysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcjgysbaxxbV3Service;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;

/**
 * 建设工程竣工验收备案信息表修改页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-02 09:20:12]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglJsgcjgysbaxxbV3ListAction.class)
@RestController("spgljsgcjgysbaxxbv3editaction")
@Scope("request")
public class SpglJsgcjgysbaxxbV3EditAction extends BaseController
{

    @Autowired
    private ISpglJsgcjgysbaxxbV3Service service;

    @Autowired
    private ISpglCommon ispglcommon;

    /**
     * 建设工程竣工验收备案信息表实体对象
     */
    private SpglJsgcjgysbaxxbV3 dataBean = null;

    @Autowired
    private ITaskCommonService iTaskCommonService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IOuService iOuService;

    @Autowired
    private IAuditSpSpJgysService iAuditSpSpJgysService;

    @Autowired
    private IDantiInfoService dantiInfoService;
    @Autowired
    private IAuditSpISubapp auditSpISubapp;
    @Autowired
    private IAuditSpPhase auditSpPhase;

    private String xzqhdm;
    private String gcdm;
    private String spsxslbm;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        xzqhdm = getRequestParameter("xzqhdm");
        gcdm = getRequestParameter("gcdm");
        spsxslbm = getRequestParameter("spsxslbm");
        dataBean = service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
        if (dataBean == null) {
            dataBean = new SpglJsgcjgysbaxxbV3();
            dataBean.setXzqhdm(xzqhdm);
            dataBean.setSpsxslbm(spsxslbm);
            dataBean.setGcdm(gcdm);
            dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            dataBean.setDfsjzj(UUID.randomUUID().toString());
            dataBean.setBarq(new Date());

            AuditProject project = iAuditProject.getAuditProjectByFlowsn(spsxslbm, "").getResult();
            if (project != null) {
                FrameOuExtendInfo raExtendInfo = iOuService.getFrameOuExtendInfo(project.getOuguid());
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(project.getTaskguid(), false).getResult();
                if (auditTask != null) {
                    dataBean.setBajg(auditTask.getOuname());
                }

                if (raExtendInfo != null) {
                    dataBean.setBajgxydm(raExtendInfo.getStr("orgcode"));
                }
                dataBean.setLxr(project.getContactperson());
                dataBean.setLxrsjh(project.getContactmobile());

                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                        .findAuditSpSpJgysBySubappGuid(project.getSubappguid());
                if (auditSpSpJgys != null) {
                    dataBean.setSgxkzbh(auditSpSpJgys.getJzzh());// 施工许可证编号
                    dataBean.setZjzmj(auditSpSpJgys.getAllbuildarea()); // 总建筑面积（m²）
                    dataBean.setDsjzmj(auditSpSpJgys.getOverloadarea()); // 地上
                    dataBean.setDxjzmj(auditSpSpJgys.getDownloadarea()); // 地下
                    dataBean.setKgrq(auditSpSpJgys.getStartdate());
                    dataBean.setJgrq(auditSpSpJgys.getEnddate());
                }
            }

            // 加载建设单位信息
            List<ParticipantsInfo> participantsInfos = iTaskCommonService.getJsdwInfor(gcdm).getResult();
            if (EpointCollectionUtils.isNotEmpty(participantsInfos)) {
                ParticipantsInfo participantsInfo = participantsInfos.get(0);
                // 初始化建设单位信息
                dataBean.setJsdw(participantsInfo.getCorpname());
                dataBean.setJsdwdm(participantsInfo.getCorpcode());
                dataBean.setJsdwlx(participantsInfo.get("JSDWLX"));
                dataBean.setJsdwxmfzr(participantsInfo.getXmfzr());
                dataBean.setJsfzrzjhm(participantsInfo.getXmfzr_idcard());
                dataBean.setJsfzrzjlx(participantsInfo.get("FRZZJLX"));
                dataBean.setJsfzrlxdh(participantsInfo.getXmfzr_phone());
                // 初始化项目信息
                dataBean.setGcmc(participantsInfo.get("GCMC"));
                dataBean.setJsdz(participantsInfo.get("JSDZ"));
                dataBean.setJsgm(participantsInfo.get("JSGM"));
                dataBean.setXmjwdzb(participantsInfo.get("XMJWDZB"));
                dataBean.setSsqx(participantsInfo.get("SSQX"));
                if ("1".equals(participantsInfo.getStr("GCHYFL"))) {
                    addCallbackParam("flag", "1");
                }
            }

            // gxh:自动赋值为该项目所选单体（danti_info）表的对应字段之和
            String flowsn = this.getRequestParameter("spsxslbm");
            String aracode = this.getRequestParameter("xzqhdm");
            if (StringUtil.isNotBlank(flowsn)) {
                AuditProject projectSum = iAuditProject.getAuditProjectByFlowsn(flowsn, aracode).getResult();
                if (projectSum != null) {
                    // phaseid为4（竣工验收）
                    AuditSpISubapp subapp = auditSpISubapp.getSubappByGuid(projectSum.getSubappguid()).getResult();
                    if (subapp != null) {
                        AuditSpPhase spphase = auditSpPhase.getAuditSpPhaseByRowguid(subapp.getPhaseguid()).getResult();
                        if (spphase != null && "4".equals(spphase.getPhaseId())) {
                            List<DantiInfo> dantiList = dantiInfoService
                                    .findDantiListBySubappguid(new Object[] {projectSum.getSubappguid() });
                            BigDecimal PriceAll = BigDecimal.ZERO;
                            BigDecimal ZjzmjAll = BigDecimal.ZERO;
                            BigDecimal DishangmianjiAll = BigDecimal.ZERO;
                            BigDecimal DixiamianjiAll = BigDecimal.ZERO;
                            // 加和
                            for (DantiInfo danti : dantiList) {
                                PriceAll = PriceAll.add(BigDecimal.valueOf((danti.getPrice()==null || StringUtil.isBlank(danti.getPrice()) ? 0 : danti.getPrice())));
                                ZjzmjAll = ZjzmjAll.add(BigDecimal.valueOf((danti.getZjzmj()==null || StringUtil.isBlank(danti.getZjzmj()) ? 0 : danti.getZjzmj())));
                                DishangmianjiAll = DishangmianjiAll
                                        .add(BigDecimal.valueOf(Double.valueOf((danti.getDishangmianji()==null || StringUtil.isBlank(danti.getDishangmianji())) ? "0" : danti.getDishangmianji())));
                                DixiamianjiAll = DixiamianjiAll
                                        .add(BigDecimal.valueOf(Double.valueOf((danti.getDixiamianji()==null || StringUtil.isBlank(danti.getDixiamianji()) ? "0" : danti.getDixiamianji()))));
                            }
                            dataBean.setSjzj(PriceAll.doubleValue());
                            dataBean.setZjzmj(ZjzmjAll.doubleValue());
                            dataBean.setDsjzmj(DishangmianjiAll.doubleValue());
                            dataBean.setDxjzmj(DixiamianjiAll.doubleValue());
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        // 事务控制
        String msg = "上报成功！";
        String rowguid = getRequestParameter("guid");
        try {
            EpointFrameDsManager.begin(null);
            if (StringUtil.isNotBlank(dataBean.getRowguid())) {
                String dfsjzj = UUID.randomUUID().toString();
                dataBean.setDfsjzj(dfsjzj);
                SpglJsgcjgysbaxxbV3 oldDataBean = service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
                ispglcommon.editToPushData(oldDataBean, dataBean, true);
            }
            else {
                String dfsjzj = UUID.randomUUID().toString();
                dataBean.setRowguid(dfsjzj);
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername(userSession.getDisplayName());
                dataBean.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                dataBean.set("sync", "0");
                dataBean.setDfsjzj(dfsjzj);
                service.insert(dataBean);
            }

            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            msg = "上报失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("msg", msg);
            EpointFrameDsManager.close();
        }
    }

    /**
     * 保存
     */
    public void saveNoValidate() {
        // 事务控制
        String msg = "保存成功！";
        try {
            if (StringUtil.isNotBlank(dataBean.getRowguid())) {
                dataBean.set("sync", "0");
                service.updateNoValidate(dataBean);
            }
            else {
                dataBean.setRowguid(UUID.randomUUID().toString());
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername(userSession.getDisplayName());
                dataBean.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                dataBean.set("sync", "-2");
                service.insertNoValidate(dataBean);
            }

        }
        catch (Exception e) {
            msg = "保存失败！";
            e.printStackTrace();
        }
        finally {
            addCallbackParam("msg", msg);
        }
    }

    public SpglJsgcjgysbaxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglJsgcjgysbaxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
