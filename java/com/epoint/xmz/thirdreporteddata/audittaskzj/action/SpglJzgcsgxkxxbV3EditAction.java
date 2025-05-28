package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
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
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJzgcsgxkxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJzgcsgxkxxbV3Service;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 建筑工程施工许可信息表修改页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-01 15:33:12]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglJzgcsgxkxxbV3ListAction.class)
@RestController("spgljzgcsgxkxxbv3editaction")
@Scope("request")
public class SpglJzgcsgxkxxbV3EditAction extends BaseController {

    @Autowired
    private ISpglJzgcsgxkxxbV3Service service;

    @Autowired
    private ISpglCommon ispglcommon;

    /**
     * 建筑工程施工许可信息表实体对象
     */
    private SpglJzgcsgxkxxbV3 dataBean = null;

    @Autowired
    private ITaskCommonService iTaskCommonService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IOuService iOuService;

    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;

    private String xzqhdm;
    private String gcdm;
    private String spsxslbm;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        xzqhdm = getRequestParameter("xzqhdm");
        gcdm = getRequestParameter("gcdm");
        spsxslbm = getRequestParameter("spsxslbm");
        dataBean = service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
        if (dataBean == null) {
            dataBean = new SpglJzgcsgxkxxbV3();
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
                }

                if (raExtendInfo != null) {
                    dataBean.setFzjgtyshxydm(raExtendInfo.getStr("orgcode"));
                }
                dataBean.setLxr(project.getContactperson());
                dataBean.setLxdh(project.getContactmobile());

                AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(project.getSubappguid());
                if (auditSpSpSgxk != null) {
                    dataBean.setZjzmj(auditSpSpSgxk.getAllbuildarea()); // 总建筑面积（m²）
                    dataBean.setDsjzmj(auditSpSpSgxk.getOverloadarea()); // 地上
                    dataBean.setDxjzmj(auditSpSpSgxk.getDownloadarea()); // 地下
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
                dataBean.setJsxz(Integer.valueOf(participantsInfo.get("JSXZ")));
                // 初始化项目信息
                dataBean.setGcmc(participantsInfo.get("GCMC"));
                dataBean.setJsdz(participantsInfo.get("JSDZ"));
                dataBean.setJsgm(participantsInfo.get("JSGM"));
                dataBean.setXmjwdzb(participantsInfo.get("XMJWDZB"));
                dataBean.setJhkgrq(participantsInfo.get("JHKGRQ"));
                dataBean.setJhjgrq(participantsInfo.get("JHJGRQ"));
                if ("1".equals(participantsInfo.getStr("GCHYFL"))) {
                    addCallbackParam("flag", "1");
                }

            }
            dataBean.setSsqx(xzqhdm);
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
                SpglJzgcsgxkxxbV3 oldDataBean = service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
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

    public SpglJzgcsgxkxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglJzgcsgxkxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
