package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglKcsjryxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSgtsjwjscxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSgtsjwjscxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglKcsjryxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSgtsjwjscxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSgtsjwjscxxxxbV3Service;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 施工图设计文件审查信息表修改页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-01 15:09:46]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglSgtsjwjscxxbV3ListAction.class)
@RestController("spglsgtsjwjscxxbv3editaction")
@Scope("request")
public class SpglSgtsjwjscxxbV3EditAction extends BaseController {

    @Autowired
    private ISpglSgtsjwjscxxbV3Service service;

    @Autowired
    private ISpglCommon ispglcommon;

    @Autowired
    private ITaskCommonService iTaskCommonService;

    @Autowired
    private ISpglKcsjryxxbV3Service spglKcsjryxxbV3Service;

    @Autowired
    private ISpglSgtsjwjscxxxxbV3Service iSpglSgtsjwjscxxxxbV3Service;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IParticipantsInfoService iParticipantsInfoService;

    /**
     * 勘察设计人员信息表实体对象
     */
    private SpglKcsjryxxbV3 sp;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglKcsjryxxbV3> model;

    /**
     * 施工图设计文件审查信息表实体对象
     */
    private SpglSgtsjwjscxxbV3 dataBean = null;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglSgtsjwjscxxxxbV3> sjDataGridModel;

    private String xzqhdm;
    private String gcdm;
    private String spsxslbm;

    private String ryxm;
    private Integer rylx;

    private String blr;
    private String blzt;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        xzqhdm = getRequestParameter("xzqhdm");
        gcdm = getRequestParameter("gcdm");
        spsxslbm = getRequestParameter("spsxslbm");
        dataBean = service.findDominByCondition(xzqhdm, gcdm);
        if (dataBean == null) {
            dataBean = new SpglSgtsjwjscxxbV3();
            dataBean.setXzqhdm(xzqhdm);
            dataBean.setGcdm(gcdm);
            dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            dataBean.setStywbh(spsxslbm);
            dataBean.setDfsjzj(UUID.randomUUID().toString());
            // 加载建设单位信息
            List<ParticipantsInfo> participantsInfos = iTaskCommonService.getJsdwInfor(gcdm).getResult();
            if (EpointCollectionUtils.isNotEmpty(participantsInfos)) {
                ParticipantsInfo participantsInfo = participantsInfos.get(0);
                // 初始化建设单位信息
                dataBean.setJsdw(participantsInfo.getCorpname());
                dataBean.setJstyshxydm(participantsInfo.getCorpcode());
                dataBean.setXmdm(participantsInfo.get("Xmdm"));
            }

            AuditProject project = iAuditProject.getAuditProjectByFlowsn(spsxslbm, "").getResult();
            if (project != null) {
                // 获取勘察单位信息
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("corptype", "1");
                sqlConditionUtil.eq("subappguid", project.getSubappguid());
                List<ParticipantsInfo> kclist = iParticipantsInfoService.getParticipantsInfoListByCondition(sqlConditionUtil.getMap());
                if (!kclist.isEmpty()) {
                    dataBean.setKcjg(kclist.get(0).getCorpname());
                    dataBean.setKcjgtyshxydm(kclist.get(0).getCorpcode());
                }
                sqlConditionUtil.clear();
                //获取设计单位
                sqlConditionUtil.eq("corptype", "2");
                sqlConditionUtil.eq("subappguid", project.getSubappguid());
                List<ParticipantsInfo> sjlist = iParticipantsInfoService.getParticipantsInfoListByCondition(sqlConditionUtil.getMap());
                if (!sjlist.isEmpty()) {
                    dataBean.setSjdw(sjlist.get(0).getCorpname());
                    dataBean.setSjtyshxydm(sjlist.get(0).getCorpcode());
                    dataBean.setSjdwxmfzr(sjlist.get(0).getXmfzr());
                    dataBean.setSjdwxmfzrhm(sjlist.get(0).getXmfzr_idcard());
                }
            }

            // 初始化其他字段
            AuditProject result = iAuditProject.getAuditProjectByFlowsn(spsxslbm, "").getResult();
            if (result != null) {
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(result.getTaskguid(), false).getResult();
                if (auditTask != null) {
                    dataBean.setYwqx(auditTask.get("ywqx"));
                    dataBean.setSpsxbm(auditTask.getItem_id());
                    //调整，如果是五位，改为截取后三位
                    double version;
                    if (StringUtil.isNotBlank(auditTask.getVersion()) && auditTask.getVersion().length() == 5) {
                        version = (Double.parseDouble(auditTask.getVersion().substring(auditTask.getVersion().length() - 3)));
                    } else {
                        version = Double.parseDouble(auditTask.getVersion());
                    }
                    dataBean.setSpsxbbh(version);
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
        try {
            EpointFrameDsManager.begin(null);
            if (StringUtil.isNotBlank(dataBean.getRowguid())) {
                SpglSgtsjwjscxxbV3 oldDataBean = service.findDominByCondition(xzqhdm, gcdm);
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

    /**
     * 删除选定
     */
    public void deleteSelect1() {
        List<String> select = getDataGridData1().getSelectKeys();
        for (String sel : select) {
            iSpglSgtsjwjscxxxxbV3Service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            spglKcsjryxxbV3Service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<SpglKcsjryxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglKcsjryxxbV3>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglKcsjryxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(ryxm)) {
                        sql.eq("ryxm", ryxm);
                    }
                    if (StringUtil.isNotBlank(String.valueOf(rylx))) {
                        sql.eq("rylx", String.valueOf(rylx));
                    }
                    if (StringUtil.isNotBlank(xzqhdm)) {
                        sql.eq("xzqhdm", xzqhdm);
                    }
                    if (StringUtil.isNotBlank(gcdm)) {
                        sql.eq("gcdm", gcdm);
                    }
                    PageData<SpglKcsjryxxbV3> pageProject = spglKcsjryxxbV3Service.getitemByPage(sql.getMap(), first,
                            pageSize, "operatedate", "DESC").getResult();
                    this.setRowCount(pageProject.getRowCount());
                    return pageProject.getList();
                }

            };
        }
        return model;
    }

    public DataGridModel<SpglSgtsjwjscxxxxbV3> getDataGridData1() {
        // 获得表格对象
        if (sjDataGridModel == null) {
            sjDataGridModel = new DataGridModel<SpglSgtsjwjscxxxxbV3>() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglSgtsjwjscxxxxbV3> fetchData(int first, int pageSize, String sortField,
                                                            String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(blr)) {
                        sql.eq("blr", blr);
                    }
                    if (StringUtil.isNotBlank(blzt)) {
                        sql.eq("blzt", String.valueOf(blzt));
                    }
                    if (StringUtil.isNotBlank(xzqhdm)) {
                        sql.eq("xzqhdm", xzqhdm);
                    }
                    if (StringUtil.isNotBlank(gcdm)) {
                        sql.eq("gcdm", gcdm);
                    }
                    PageData<SpglSgtsjwjscxxxxbV3> pageProject = iSpglSgtsjwjscxxxxbV3Service.getitemByPage(
                            sql.getMap(), first, pageSize, "operatedate", "DESC").getResult();
                    this.setRowCount(pageProject.getRowCount());
                    return pageProject.getList();
                }

            };
        }
        return sjDataGridModel;
    }

    /**
     * 查询主表数据有没有保存 [一句话功能简述]
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void checkData() {
        SpglSgtsjwjscxxbV3 spglSgtsjwjscxxbV3 = service.findDominByCondition(xzqhdm, gcdm);
        if (spglSgtsjwjscxxbV3 != null) {
            addCallbackParam("isTrue", "isTrue");
        }
    }

    public SpglSgtsjwjscxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglSgtsjwjscxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

    public String getRyxm() {
        return ryxm;
    }

    public void setRyxm(String ryxm) {
        this.ryxm = ryxm;
    }

    public Integer getRylx() {
        return rylx;
    }

    public void setRylx(Integer rylx) {
        this.rylx = rylx;
    }

    public String getBlr() {
        return blr;
    }

    public void setBlr(String blr) {
        this.blr = blr;
    }

    public String getBlzt() {
        return blzt;
    }

    public void setBlzt(String blzt) {
        this.blzt = blzt;
    }

}
