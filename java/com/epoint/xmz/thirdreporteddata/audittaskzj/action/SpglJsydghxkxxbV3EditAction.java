package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
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
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsydghxkxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsydghxkxxbV3Service;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 建设用地规划许可信息表修改页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-01 14:34:54]
 */
@RightRelation(SpglJsydghxkxxbV3ListAction.class)
@RestController("spgljsydghxkxxbv3editaction")
@Scope("request")
public class SpglJsydghxkxxbV3EditAction extends BaseController {

    @Autowired
    private ISpglJsydghxkxxbV3Service service;

    @Autowired
    private ISpglCommon ispglcommon;

    @Autowired
    private ITaskCommonService iTaskCommonService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IOuService iOuService;

    @Autowired
    private IAuditRsItemBaseinfo iauditrsitembaseinfo;


    /**
     * 建设用地规划许可信息表实体对象
     */
    private SpglJsydghxkxxbV3 dataBean = null;

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
            dataBean = new SpglJsydghxkxxbV3();
            dataBean.setXzqhdm(xzqhdm);
            dataBean.setSpsxslbm(spsxslbm);
            dataBean.setGcdm(gcdm);
            dataBean.setFzrq(new Date());
            dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            dataBean.setDfsjzj(UUID.randomUUID().toString());

            AuditProject project = iAuditProject.getAuditProjectByFlowsn(spsxslbm, "").getResult();
            if (project != null) {
                FrameOuExtendInfo raExtendInfo = iOuService.getFrameOuExtendInfo(project.getOuguid());
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(project.getTaskguid(), false).getResult();
                if (auditTask != null) {
                    dataBean.setFzjg(auditTask.getOuname());
                    dataBean.setPzydjg(auditTask.getOuname());
                }

                if (raExtendInfo != null) {
                    dataBean.setFzjgtyshxydm(raExtendInfo.getStr("orgcode"));
                }
                dataBean.setLxr(project.getContactperson());
                dataBean.setLxrsjh(project.getContactmobile());
            }

            AuditRsItemBaseinfo result = iauditrsitembaseinfo.getAuditRsItemBaseinfoByItemcode(gcdm).getResult();
            if (result != null) {
                if (StringUtil.isNotBlank(result.getTdhqfs())) {
                    dataBean.setTdhqfs(result.getTdhqfs().toString());
                }
            }

            // 加载建设单位信息
            List<ParticipantsInfo> participantsInfos = iTaskCommonService.getJsdwInfor(gcdm).getResult();
            if (EpointCollectionUtils.isNotEmpty(participantsInfos)) {
                ParticipantsInfo participantsInfo = participantsInfos.get(0);
                // 初始化项目信息
                dataBean.setXmmc(participantsInfo.get("GCMC"));
                dataBean.setJsgm(participantsInfo.get("JSGM"));
                dataBean.setYddw(participantsInfo.getCorpname());
                dataBean.setYdtyshxydm(participantsInfo.getCorpcode());
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
                SpglJsydghxkxxbV3 oldDataBean = service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
                String dfsjzj = UUID.randomUUID().toString();
                dataBean.setDfsjzj(dfsjzj);
                ispglcommon.editToPushData(oldDataBean, dataBean, true);
            } else {
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
        } catch (Exception e) {
            msg = "上报失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } finally {
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
            } else {
                dataBean.setRowguid(UUID.randomUUID().toString());
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername(userSession.getDisplayName());
                dataBean.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                dataBean.set("sync", "-2");
                service.insertNoValidate(dataBean);
            }

        } catch (Exception e) {
            msg = "保存失败！";
            e.printStackTrace();
        } finally {
            addCallbackParam("msg", msg);
        }
    }

    public SpglJsydghxkxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglJsydghxkxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
