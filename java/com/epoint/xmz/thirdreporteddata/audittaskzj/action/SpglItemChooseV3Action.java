package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
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
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.spgl.domain.SpglXmqqyjxxb;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.sqgl.common.util.Zjconstant;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 阶段选择页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@RestController("spglitemchoosev3action")
@Scope("request")
public class SpglItemChooseV3Action extends BaseController
{

    private static final long serialVersionUID = 7487597167764665058L;

    private DataGridModel<AuditRsItemBaseinfo> model = null;

    @Autowired
    private IAuditRsItemBaseinfo iauditrsitembaseinfo;
    @Autowired
    private ISpglXmjbxxbV3 iSpglXmjbxxbV3;
    @Autowired
    private ISpglsplcxxb ispglsplcxxb;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    @Autowired
    private IAuditSpInstance iauditspinstance;
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    @Autowired
    private IAuditSpBusiness iauditspbusiness;
    @Autowired
    private ICodeItemsService icodeitemsservice;
    @Autowired
    private IAuditProject iauditproject;
    @Autowired
    private IAuditTask iaudittask;
    @Autowired
    private ISpglXmspsxblxxbV3 iSpglXmspsxblxxbV3;
    @Autowired
    private ISpglXmspsxblxxxxbV3 ispglxmspsxblxxxxbV3;
    @Autowired
    private IOuService iouservice;
    @Autowired
    private IUserService iuserservice;
    @Autowired
    private IAuditSpIReview iauditspireview;
    @Autowired
    private ISpglXmspsxzqyjxxbV3 ispglxmspsxzqyjxxbV3;
    @Autowired
    private ISpglsplcjdsxxxb ispglsplcjdsxxxb;
    @Autowired
    private IAuditProjectMaterial iauditprojectmaterial;
    @Autowired
    private IAttachService iattachservice;
    @Autowired
    private ISpglsqcljqtfjxxbV3 iSpglsqcljqtfjxxbV3;
    @Autowired
    private ISpglXmspsxpfwjxxbV3 ispglxmspsxpfwjxxbV3;
    @Autowired
    private IAuditOrgaArea iauditorgaarea;
    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;
    @Autowired
    private IConfigService frameConfigService;
    @Autowired
    private ISpglXmqqyjxxbV3 iSpglXmqqyjxxbV3;
    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    @Autowired
    private IOuService ouService;
    private String itemname;

    private String itemcode;

    @Override
    public void pageLoad() {

    }

    public DataGridModel<AuditRsItemBaseinfo> getDataGridData() {
        if (model == null) {
            model = new DataGridModel<AuditRsItemBaseinfo>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditRsItemBaseinfo> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(itemcode)) {
                        sqlc.like("itemcode", itemcode);
                    }
                    if (StringUtil.isNotBlank(itemname)) {
                        sqlc.like("itemname", itemname);
                    }
                    // 去除所有草稿项目
                    sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                    // 去除子项
                    sqlc.isBlank("parentid");
                    // 未同步主项
                    sqlc.isBlank("issendzj");
                    // sqlc.eq("rowguid", "db3498d5-008f-40a4-a613-10c2d2c96f71");

                    sqlc.setOrderAsc("operatedate");
                    PageData<AuditRsItemBaseinfo> pagadata = iauditrsitembaseinfo.getAuditRsItemBaseinfoByPage(
                                    AuditRsItemBaseinfo.class, sqlc.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    this.setRowCount(pagadata.getRowCount());
                    return pagadata.getList();
                }

            };
        }
        return model;
    }

    /*
     * 通过项目标识将审批库项目相关信息插入到审批本地库对应表
     */
    public void initXmAndBj(String itemguid) {
        String msg = "补录成功！";
        String xmjbxxguid = "";
        try {
            EpointFrameDsManager.begin(null);
            if (StringUtil.isNotBlank(itemguid)) {
                // 审批项目基本信息实体
                AuditRsItemBaseinfo auditRsItemBaseinfo = new AuditRsItemBaseinfo();
                auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                if (auditRsItemBaseinfo == null) {
                    msg = "项目信息不存在！";
                    return;
                }
                // 审批主题实例实体和审批主题配置实体
                AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid())
                        .getResult();
                // 行政区划代码
                String areacode = null;
                // 上报时间
                Date sbsj = null;
                // 审批流程编码
                String splcbm = null;
                // 主题配置
                AuditSpBusiness auditspbusiness = null;

                if (auditspinstance != null) {
                    areacode = auditspinstance.getAreacode();
                    sbsj = auditspinstance.getCreatedate();
                    splcbm = auditspinstance.getBusinessguid();
                    auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(auditspinstance.getBusinessguid())
                            .getResult();
                }

                // 子申报列表
                List<AuditSpISubapp> subappList = new ArrayList<>();
                SqlConditionUtil sUtil = new SqlConditionUtil();
                sUtil.eq("yewuguid", itemguid);
                subappList = iAuditSpISubapp.getSubappListByMap(sUtil.getMap()).getResult();
                // 子项目列表 
                AuditRsItemBaseinfo pauditRsItemBaseinfo = null;
                List<AuditRsItemBaseinfo> subItemList = null;
                // 递归子项目
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                    pauditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(
                            auditRsItemBaseinfo.getParentid()).getResult();
                }
                else {
                    // 主项目 
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("parentid", itemguid);
                    sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                    sqlc.isBlank("issendzj");
                    subItemList = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlc.getMap()).getResult();
                }

                String xmdm = "";
                if (pauditRsItemBaseinfo != null) {
                    xmdm = pauditRsItemBaseinfo.getItemcode();
                }
                else {
                    xmdm = auditRsItemBaseinfo.getItemcode();
                }

                int sjsczt = 0;
                StringBuilder sbyy = new StringBuilder();

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
                            sjsczt = -1;
                            sbyy.append("项目对应流程的流程类型在市级流程中不存在！;");
                        }
                        else {
                            sqlc.clear();
                            sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                            sqlc.eq("areacode", sjarea.getXiaqucode());
                            List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap())
                                    .getResult();
                            if (ValidateUtil.isNotBlankCollection(listb)) {
                                auditspbusiness = listb.get(0);
                                areacode = sjarea.getXiaqucode();
                                if (auditspbusiness != null) {
                                    splcbm = auditspbusiness.getRowguid();
                                }
                            }
                            else {
                                sjsczt = -1;
                                sbyy.append("项目对应流程的流程类型在市级流程中不存在！;");
                            }
                        }
                    }
                }

                // 项目基本信息
                SpglXmjbxxbV3 spglXmjbxxb = new SpglXmjbxxbV3();
                spglXmjbxxb.setRowguid(UUID.randomUUID().toString());
                spglXmjbxxb.setDfsjzj(auditRsItemBaseinfo.getRowguid());
                xmjbxxguid = spglXmjbxxb.getRowguid();
                spglXmjbxxb.setXzqhdm(areacode);
                spglXmjbxxb.setXmdm(xmdm);
                spglXmjbxxb.setGcfw(auditRsItemBaseinfo.getStr("GCFW"));// 工程范围
                spglXmjbxxb.setQjdgcdm(null);// 前阶段关联工程代码
                spglXmjbxxb.setGcdm(auditRsItemBaseinfo.getItemcode());

                if (!isInCode("项目投资来源", auditRsItemBaseinfo.getXmtzly(), true)) {
                    sjsczt = -1;
                    sbyy.append("项目投资来源的值不在代码项之中！;");
                    spglXmjbxxb.setXmtzly(null);
                }
                else {
                    spglXmjbxxb.setXmtzly(auditRsItemBaseinfo.getXmtzly());
                }

                if (!isInCode("土地获取方式", auditRsItemBaseinfo.getTdhqfs(), true)) {
                    sjsczt = -1;
                    sbyy.append("土地获取方式的值不在代码项之中！;");
                    spglXmjbxxb.setTdhqfs(null);
                }
                else {
                    spglXmjbxxb.setTdhqfs(auditRsItemBaseinfo.getTdhqfs());
                }

                if (!isInCode("土地是否带设计方案", auditRsItemBaseinfo.getTdsfdsjfa(), true)) {
                    sjsczt = -1;
                    sbyy.append("土地是否带设计方案的值不在代码项之中！;");
                    spglXmjbxxb.setTdsfdsjfa(null);
                }
                else {
                    spglXmjbxxb.setTdsfdsjfa(auditRsItemBaseinfo.getTdsfdsjfa());
                }

                if (!isInCode("是否完成区域评估", auditRsItemBaseinfo.getSfwcqypg(), true)) {
                    sjsczt = -1;
                    sbyy.append("是否完成区域评估的值不在代码项之中！;");
                    spglXmjbxxb.setSfwcqypg(null);
                }
                else {
                    spglXmjbxxb.setSfwcqypg(auditRsItemBaseinfo.getSfwcqypg());
                }

                if (auditspbusiness != null) {
                    if (!isInCode("审批流程类型", auditspbusiness.getSplclx(), true)) {
                        sjsczt = -1;
                        sbyy.append("审批流程类型的值不在代码项之中！;");
                        spglXmjbxxb.setSplclx(null);
                    }
                    else {
                        spglXmjbxxb.setSplclx(auditspbusiness.getSplclx());
                    }
                }
                else {
                    sjsczt = -1;
                    sbyy.append("审批流程类型的值不在代码项之中！;");
                    spglXmjbxxb.setSplclx(null);
                }

                spglXmjbxxb.setXmmc(auditRsItemBaseinfo.getItemname());
                spglXmjbxxb.setGcfl(23);
                spglXmjbxxb.setLxlx(Integer.parseInt(auditRsItemBaseinfo.getStr("LXLX")));
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getConstructionproperty())) {
                    if (!isInCode("建设性质", auditRsItemBaseinfo.getConstructionproperty(), true)) {
                        sjsczt = -1;
                        sbyy.append("建设性质的值不在代码项之中！;");
                        spglXmjbxxb.setJsxz(null);
                    }
                    else {
                        spglXmjbxxb.setJsxz(Integer.parseInt(auditRsItemBaseinfo.getConstructionproperty()));
                    }
                }
                else {
                    spglXmjbxxb.setJsxz(null);
                }

                spglXmjbxxb.setXmzjsx(Integer.valueOf(auditRsItemBaseinfo.getXmzjsx()));
                spglXmjbxxb.setGbhydmfbnd("2017");// 国标行业代码发布年代 默认2017
                spglXmjbxxb.setGbhy(auditRsItemBaseinfo.getGbhy());
                spglXmjbxxb.setNkgsj(auditRsItemBaseinfo.getItemstartdate());
                if (isNull(auditRsItemBaseinfo.getItemstartdate())) {
                    sjsczt = -1;
                    sbyy.append("拟开工时间为空！;");
                }
                spglXmjbxxb.setNjcsj(auditRsItemBaseinfo.getItemfinishdate());
                if (isNull(auditRsItemBaseinfo.getItemfinishdate())) {
                    sjsczt = -1;
                    sbyy.append("拟建成时间为空！;");
                }

                spglXmjbxxb.setXmsfwqbj(ZwfwConstant.CONSTANT_INT_ZERO); // 项目是否完全办结 默认尚未完全办结
                spglXmjbxxb.setXmwqbjsj(null); // 项目完全办结时间
                spglXmjbxxb.setJsddxzqh(auditRsItemBaseinfo.get("JSDDXZQH"));// 建设地点行政区划
                spglXmjbxxb.setZtze(auditRsItemBaseinfo.getTotalinvest());
                if (isNull(auditRsItemBaseinfo.getTotalinvest())) {
                    sjsczt = -1;
                    sbyy.append("总投资额为空！;");
                }
                spglXmjbxxb.setJsdd(auditRsItemBaseinfo.getConstructionsite());
                if (isNull(auditRsItemBaseinfo.getConstructionsite())) {
                    sjsczt = -1;
                    sbyy.append("建设地点为空！;");
                }
                spglXmjbxxb.setJsgmjnr(auditRsItemBaseinfo.getConstructionscaleanddesc());
                spglXmjbxxb.setYdmj(auditRsItemBaseinfo.getLandarea() != null ? auditRsItemBaseinfo.getLandarea() : 0);
                if (isNull(auditRsItemBaseinfo.getLandarea())) {
                    sjsczt = -1;
                    sbyy.append("用地面积为空！;");
                }
                spglXmjbxxb.setJzmj(auditRsItemBaseinfo.getJzmj());
                if (isNull(auditRsItemBaseinfo.getJzmj())) {
                    sjsczt = -1;
                    sbyy.append("建筑面积为空！;");
                }
                // 3.0新增变更字段开始
                spglXmjbxxb.setXmjwdzb(auditRsItemBaseinfo.get("XMJWDZB"));
                spglXmjbxxb.setJsdwdm(auditRsItemBaseinfo.getItemlegalcertnum());
                spglXmjbxxb.setJsdwlx(auditRsItemBaseinfo.getInt("JSDWLX"));
                spglXmjbxxb.setSfxxgc(auditRsItemBaseinfo.getInt("SFXXGC"));
                spglXmjbxxb.setCd(auditRsItemBaseinfo.getDouble("CD"));
                spglXmjbxxb.setJsdw(auditRsItemBaseinfo.getDepartname());
                spglXmjbxxb.setGchyfl(auditRsItemBaseinfo.getStr("GCHYFL"));
                // 3.0新增变更字段结束
                spglXmjbxxb.setSbsj(sbsj);
                spglXmjbxxb.setSplcbm(splcbm);
                Double verison = ispglsplcxxb.getMaxSplcbbh(splcbm);
                spglXmjbxxb.setSplcbbh(verison);
                spglXmjbxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                // 校验状态 本地校验失败为-1 校验没问题为0
                spglXmjbxxb.setSjsczt(sjsczt);
                spglXmjbxxb.set("sbyy", sbyy.toString());
                // 新增时必为草稿（不同步）的新增数据 上报后为未同步的新增数据
                spglXmjbxxb.set("sync", "-1");

                iSpglXmjbxxbV3.insert(spglXmjbxxb);
                // 已录入审批本地库
                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ONE);
                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);

                if (ValidateUtil.isNotBlankCollection(subappList)) {
                    for (AuditSpISubapp subapp : subappList) {
                        String subappguid = subapp.getRowguid();
                        // 初始化 办件信息
                        initSpsxblxx(areacode, subappguid, auditspbusiness);
                    }
                }

                // 项目一套信息初始化完成，若为主项目，则子项目需要遍历递归调用
                EpointFrameDsManager.commit();
                if (ValidateUtil.isNotBlankCollection(subItemList)) {
                    for (AuditRsItemBaseinfo info : subItemList) {
                        initXmAndBj(info.getRowguid());
                        EpointFrameDsManager.commit();
                    }
                }
                // 查询项目前期意见信息
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("qqyjcode", auditRsItemBaseinfo.getStr("chcode"));
                sqlConditionUtil.eq("sync", "-1");
                List<SpglXmqqyjxxbV3> list = iSpglXmqqyjxxbV3.getXmqqyjByCondition(SpglXmqqyjxxb.class,
                        sqlConditionUtil.getMap());
                if (list != null) {
                    for (SpglXmqqyjxxbV3 spglXmqqyjxxb : list) {
                        spglXmqqyjxxb.setXzqhdm(areacode);
                        spglXmqqyjxxb.setXmdm(xmdm);
                        spglXmqqyjxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                        spglXmqqyjxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                        spglXmqqyjxxb.set("sync", ZwfwConstant.CONSTANT_INT_ZERO);
                        iSpglXmqqyjxxbV3.update(spglXmqqyjxxb);
                    }
                }
            }
        }
        catch (Exception e) {
            // 不回滚
            msg = "部分信息补录失败！ 失败原因：" + e.getMessage();
            e.printStackTrace();
        }
        finally {
            addCallbackParam("msg", msg);
            addCallbackParam("xmjbxxguid", xmjbxxguid);
            EpointFrameDsManager.close();
        }
    }

    /**
     * 初始化项目审批事项办理信息,项目审批事项办理详细信息（接件 受理 办结）,项目审批事项征求意见信息
     *
     * @param areacode
     *         行政区划代码
     * @param subappguid
     *         子申报标识
     */
    private void initSpsxblxx(String areacode, String subappguid, AuditSpBusiness auditspbusiness) {
        AuditTask audittask = null;
        AuditRsItemBaseinfo rsitem = null;
        Double maxbbh = (double) 0;
        String splcbm = null;
        String gcdm = null;
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("subappguid", subappguid);
        List<AuditProject> listproject = iauditproject.getAuditProjectListByCondition(sqlc.getMap()).getResult();
        List<AuditSpIReview> listreview = iauditspireview.getReviewBySubappGuid(subappguid).getResult();
        AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
        if (sub != null) {
            sub.setBusinessguid(auditspbusiness.getRowguid());
            rsitem = iauditrsitembaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid()).getResult();
            if (rsitem != null) {
                gcdm = rsitem.getItemcode();
            }
            maxbbh = ispglsplcxxb.getMaxSplcbbh(sub.getBusinessguid());
            splcbm = sub.getBusinessguid();
        }
        if (ValidateUtil.isNotBlankCollection(listproject)) {
            Date date = new Date();
            for (AuditProject auditProject : listproject) {
                // 插入审批事项办理信息
                String spsxbm = null;
                Double spsxbbh = null;
                String spbmbm = null;
                String spbmmc = null;
                Integer sxblsx = null;

                audittask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                if (audittask != null) {
                    spsxbm = audittask.getItem_id();
                    spsxbbh = Double.valueOf(audittask.getVersion());
                    spbmbm = audittask.getOuguid();
                    spbmmc = Zjconstant.getOunamebyOuguid(audittask.getOuguid());
                    sxblsx = audittask.getPromise_day();
                }
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(
                        auditProject.getTaskguid(), true).getResult();
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getIszijianxitong().toString())) {
                    // 如果是自建系统办件、根据系统参数判断是否上报自建系统办件
                    String notreport = frameConfigService.getFrameConfigValue("AS_NOTREPORTED_ZJ");
                    if (StringUtil.isNotBlank(notreport) && ZwfwConstant.CONSTANT_STR_ONE.equals(notreport)) {
                        continue;
                    }
                }

                SpglXmspsxblxxbV3 spgl = new SpglXmspsxblxxbV3();
                spgl.setRowguid(UUID.randomUUID().toString());
                spgl.setDfsjzj(auditProject.getRowguid());
                spgl.setXzqhdm(areacode);
                spgl.setGcdm(gcdm);
                spgl.setSpsxbm(spsxbm);
                spgl.setSpsxbbh(spsxbbh);
                spgl.setSplcbm(splcbm);
                spgl.setSplcbbh(maxbbh);// 设置关联最新的版本号
                spgl.setSpsxslbm(auditProject.getFlowsn());
                spgl.setSpbmbm(iouservice.getOuByOuGuid(spbmbm).getOucode());
                spgl.setSpbmmc(spbmmc);
                if (Integer.valueOf(ZwfwConstant.APPLY_WAY_CKDJ).equals(auditProject.getApplyway())) {
                    spgl.setSlfs(ZwfwConstant.CONSTANT_INT_ONE); // 默认窗口受理
                }
                else {
                    spgl.setSlfs(4); // 全流程网上办理
                }
                spgl.setGkfs(ZwfwConstant.CONSTANT_INT_ONE);// 默认1主动公开
                // 根据batchguid去填并联审批实例标识
                AuditSpITask auditspitask = auditSpITaskService.getAuditSpITaskByProjectGuid(auditProject.getRowguid())
                        .getResult();
                if (StringUtil.isNotBlank(auditspitask.getBatchguid())) {
                    spgl.setBlspslbm(auditspitask.getBatchguid());
                }else{
                    spgl.setBlspslbm(auditProject.getSubappguid());
                }
                spgl.setSxblsx(sxblsx);
                spgl.setOperatedate(date);
                // 3.0新增变更字段
                spgl.setLxr(auditProject.getContactperson());
                spgl.setLxrsjh(auditProject.getContactmobile());
                spgl.setYwqx(audittask.getInt("YWQX"));
                // 3.0新增变更字段结束
                spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);// 默认为新增数据
                spgl.set("sync", "-1");// 必为草稿（不同步）
                // 数据验证，查询主题是否推送成功，事项版本号和流程版本号，流程编码是否能获取数据
                if (!ispglsplcjdsxxxb.isExistSplcSx(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbbh(),
                        spgl.getSpsxbm())) {
                    spgl.setSjsczt(-1);
                    spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
                }
                iSpglXmspsxblxxbV3.insert(spgl);

                // 同送征求信息，新增征求信息，根据部门查询征求
                if (ValidateUtil.isNotBlankCollection(listreview)) {
                    for (AuditSpIReview auditSpIReview : listreview) {
                        if (StringUtil.isNotBlank(spbmbm) && spbmbm.equals(auditSpIReview.getOuguid())) {
                            // 添加记录
                            SpglXmspsxzqyjxxbV3 spglxmspsxzqyjxxb = new SpglXmspsxzqyjxxbV3();
                            spglxmspsxzqyjxxb.setRowguid(UUID.randomUUID().toString());
                            spglxmspsxzqyjxxb.setDfsjzj(
                                    MDUtils.md5Hex(auditSpIReview.getRowguid() + auditProject.getRowguid()));
                            spglxmspsxzqyjxxb.setXzqhdm(areacode);
                            spglxmspsxzqyjxxb.setGcdm(gcdm);
                            spglxmspsxzqyjxxb.setSpsxslbm(auditProject.getFlowsn());
                            spglxmspsxzqyjxxb.setBldwdm(iouservice.getOuByOuGuid(spbmbm).getOucode());
                            spglxmspsxzqyjxxb.setBldwmc(spbmmc);
                            spglxmspsxzqyjxxb.setFksj(auditSpIReview.getPingshengdate());
                            spglxmspsxzqyjxxb.setBlr(
                                    iuserservice.getUserNameByUserGuid(auditSpIReview.getPingshenguserguid()));
                            spglxmspsxzqyjxxb.setFkyj(auditSpIReview.getPingshenopinion());
                            spglxmspsxzqyjxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            spglxmspsxzqyjxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            spglxmspsxzqyjxxb.set("sync", "-1");// 必为草稿（不同步）
                            // 判断办件信息是否推送成功
                            if (!iSpglXmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                                spglxmspsxzqyjxxb.setSjsczt(-1);
                                spglxmspsxzqyjxxb.setSbyy(
                                        "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                            ispglxmspsxzqyjxxbV3.insert(spglxmspsxzqyjxxb);
                        }
                    }
                }

                // 插入办理详细信息表
                // 接件
                if (auditProject.getApplydate() != null) {
                    SpglXmspsxblxxxxbV3 spgl1 = new SpglXmspsxblxxxxbV3();
                    spgl1.setRowguid(UUID.randomUUID().toString());
                    spgl1.setDfsjzj(auditProject.getRowguid());
                    spgl1.setXzqhdm(areacode);
                    spgl1.setGcdm(gcdm);
                    spgl1.setSpsxslbm(auditProject.getFlowsn());
                    String userguid = auditProject.getReceiveuserguid();
                    FrameUser frameuser = iuserservice.getUserByUserField("userguid", userguid);
                    spgl1.setBlcs(Zjconstant.getOunamebyuser(frameuser));
                    if (frameuser != null) {
                        spgl1.setBlr(frameuser.getDisplayName());
                    }
                    // 3.0新增上报字段
                    spgl1.setDwmc(auditProject.getOuname());
                    FrameOuExtendInfo raExtendInfo = ouService.getFrameOuExtendInfo(auditProject.getOuguid());
                    if (raExtendInfo != null) {
                        spgl1.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                    }
                    else {
                        spgl1.set("sjsczt", "-1");
                        spgl1.setSbyy("单位统一社会信用代码校验有误！");
                    }

                    if (auditTaskExtension != null) {
                        String SYSTEM_NAME = icodeitemsservice.getItemTextByCodeName("办理系统",
                                auditTaskExtension.getStr("handle_system"));
                        if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                            spgl1.setSjly(SYSTEM_NAME);
                        }
                        else {
                            spgl1.set("sjsczt", "-1");
                            spgl1.setSbyy("未配置系统参数SYSTEM_NAME！");
                        }
                    }
                    spgl1.setBlzt(ZwfwConstant.CONSTANT_INT_ONE);
                    spgl1.setBlyj(Zjconstant.SPGL_JJ);
                    spgl1.setBlsj(auditProject.getApplydate());
                    spgl1.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spgl1.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    spgl1.set("sync", "-1");
                    if (!iSpglXmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                        spgl1.setSjsczt(-1);
                        spgl1.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                    }

                    ispglxmspsxblxxxxbV3.insert(spgl1);
                }
                // 受理
                if (auditProject.getAcceptuserdate() != null) {
                    SpglXmspsxblxxxxbV3 spgl1 = new SpglXmspsxblxxxxbV3();
                    spgl1.setRowguid(UUID.randomUUID().toString());
                    spgl1.setDfsjzj(auditProject.getRowguid());
                    spgl1.setXzqhdm(areacode);
                    spgl1.setGcdm(gcdm);
                    spgl1.setSpsxslbm(auditProject.getFlowsn());
                    if (StringUtil.isNotBlank(auditProject.getAcceptuserguid())) {
                        FrameUser frameuser = iuserservice.getUserByUserField("userguid",
                                auditProject.getAcceptuserguid());
                        spgl1.setBlcs(Zjconstant.getOunamebyuser(frameuser));
                        if (frameuser != null) {
                            spgl1.setBlr(frameuser.getDisplayName());
                        }
                    }
                    else {
                        spgl1.setBlcs(null);
                        spgl1.setBlr(auditProject.getApplyername());
                        spgl1.setSjsczt(-1);
                        spgl1.setSbyy("未获取到办理人！");
                    }
                    // 3.0新增上报字段
                    spgl1.setDwmc(auditProject.getOuname());
                    FrameOuExtendInfo raExtendInfo = ouService.getFrameOuExtendInfo(auditProject.getOuguid());
                    if (raExtendInfo != null) {
                        spgl1.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                    }
                    else {
                        spgl1.set("sjsczt", "-1");
                        spgl1.setSbyy("单位统一社会信用代码校验有误！");
                    }

                    if (auditTaskExtension != null) {
                        String SYSTEM_NAME = icodeitemsservice.getItemTextByCodeName("办理系统",
                                auditTaskExtension.getStr("handle_system"));
                        if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                            spgl1.setSjly(SYSTEM_NAME);
                        }
                        else {
                            spgl1.set("sjsczt", "-1");
                            spgl1.setSbyy("未配置系统参数SYSTEM_NAME！");
                        }
                    }
                    spgl1.setBlzt(3);
                    spgl1.setBlyj(Zjconstant.SPGL_SL);
                    spgl1.setBlsj(auditProject.getAcceptuserdate());
                    spgl1.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spgl1.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    spgl1.set("sync", "-1");
                    if (!iSpglXmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                        spgl1.setSjsczt(-1);
                        spgl1.setSbyy(spgl1.getSbyy()
                                + "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                    }
                    ispglxmspsxblxxxxbV3.insert(spgl1);

                    // 插入部门受理
                    spgl1.setRowguid(UUID.randomUUID().toString());
                    spgl1.setBlzt(8);
                    ispglxmspsxblxxxxbV3.insert(spgl1);

                    // 其他附件信息
                    List<AuditProjectMaterial> materialList = iauditprojectmaterial.selectProjectMaterial(
                            auditProject.getRowguid()).getResult();
                    if (ValidateUtil.isNotBlankCollection(materialList)) {
                        for (AuditProjectMaterial material : materialList) {
                            List<FrameAttachInfo> attachlist = iattachservice.getAttachInfoListByGuid(
                                    material.getCliengguid());
                            if (ValidateUtil.isNotBlankCollection(attachlist)) {
                                for (FrameAttachInfo frameAttachInfo : attachlist) {
                                    SpglsqcljqtfjxxbV3 fxxb = new SpglsqcljqtfjxxbV3();
                                    fxxb.setRowguid(UUID.randomUUID().toString());
                                    fxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                                    fxxb.setXzqhdm(areacode);
                                    fxxb.setGcdm(gcdm);
                                    fxxb.setSpsxslbm(auditProject.getFlowsn());
                                    fxxb.setBlspslbm(auditProject.getSubappguid());
                                    fxxb.setClmc(frameAttachInfo.getAttachFileName());
                                    fxxb.setClfl(ZwfwConstant.CONSTANT_INT_ONE);// 默认为1，申报材料
                                    // 查询materialid
                                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(
                                            material.getTaskmaterialguid()).getResult();
                                    if (auditTaskMaterial != null) {
                                        fxxb.setClmlbh(StringUtil.isNotBlank(auditTaskMaterial.get("CLMLBH"))
                                                ? auditTaskMaterial.get("CLMLBH")
                                                : auditTaskMaterial.getMaterialid());
                                    }
                                    else {
                                        fxxb.setClmlbh("");
                                        fxxb.set("sjsczt", "-1");
                                        fxxb.setSbyy("材料目录校验失败！");
                                    }
                                    fxxb.setCllx(frameAttachInfo.getContentType());
                                    fxxb.setClid(frameAttachInfo.getAttachGuid());
                                    fxxb.setSqfs(2);
                                    fxxb.setSqsl(0);
                                    fxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                    fxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                    // 数据验证,项目代码是否在工程代码
                                    if (!iSpglXmjbxxbV3.isExistGcdm(gcdm)) {
                                        fxxb.set("sjsczt", "-1");
                                        fxxb.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                                    }
                                    if (!iSpglXmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                                        fxxb.set("sjsczt", "-1");
                                        if (StringUtil.isNotBlank(fxxb.getSbyy())) {
                                            fxxb.setSbyy(fxxb.getSbyy()
                                                    + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                        }
                                        else {
                                            fxxb.setSbyy(
                                                    "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                        }
                                    }
                                    iSpglsqcljqtfjxxbV3.insert(fxxb);
                                }
                            }
                        }
                    }
                }
                // 办结
                if (auditProject.getBanjiedate() != null) {
                    SpglXmspsxblxxxxbV3 spgl1 = new SpglXmspsxblxxxxbV3();
                    spgl1.setRowguid(UUID.randomUUID().toString());
                    spgl1.setDfsjzj(auditProject.getRowguid());
                    spgl1.setXzqhdm(areacode);
                    spgl1.setGcdm(gcdm);
                    spgl1.setSpsxslbm(auditProject.getFlowsn());
                    if (StringUtil.isNotBlank(auditProject.getBanjieuserguid())) {
                        FrameUser frameuser = iuserservice.getUserByUserField("userguid",
                                auditProject.getBanjieuserguid());
                        spgl1.setBlcs(Zjconstant.getOunamebyuser(frameuser));
                        if (frameuser != null) {
                            spgl1.setBlr(frameuser.getDisplayName());
                        }
                    }
                    else {
                        spgl1.setBlcs(null);
                        spgl1.setBlr(auditProject.getApplyername());
                        spgl1.setSjsczt(-1);
                        spgl1.setSbyy("未获取到办理人！");
                    }
                    // 3.0新增上报字段
                    spgl1.setDwmc(auditProject.getOuname());
                    FrameOuExtendInfo raExtendInfo = ouService.getFrameOuExtendInfo(auditProject.getOuguid());
                    if (raExtendInfo != null) {
                        spgl1.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                    }
                    else {
                        spgl1.set("sjsczt", "-1");
                        spgl1.setSbyy("单位统一社会信用代码校验有误！");
                    }

                    if (auditTaskExtension != null) {
                        String SYSTEM_NAME = icodeitemsservice.getItemTextByCodeName("办理系统",
                                auditTaskExtension.getStr("handle_system"));
                        if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                            spgl1.setSjly(SYSTEM_NAME);
                        }
                        else {
                            spgl1.set("sjsczt", "-1");
                            spgl1.setSbyy("未配置系统参数SYSTEM_NAME！");
                        }
                    }
                    spgl1.setBlyj(Zjconstant.SPGL_BJ);
                    spgl1.setBlsj(auditProject.getBanjiedate());
                    spgl1.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spgl1.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    if(auditProject.getBanjieresult() !=null){
                        if (auditProject.getBanjieresult() == 50) {
                            // 审核不通过办结
                            spgl1.setBlzt(13);
                        }
                        else if (auditProject.getBanjieresult() == 40) {
                            // 审核通过办结
                            spgl1.setBlzt(11);
                        }
                    }

                    spgl1.set("sync", "-1");
                    if (!iSpglXmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                        spgl1.setSjsczt(-1);
                        spgl1.setSbyy(spgl1.getSbyy()
                                + "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                    }
                    if (auditProject.getBanwandate() != null) {
                        // 推送暂停计时程序
                        SpglXmspsxblxxxxbV3 spgl2 = (SpglXmspsxblxxxxbV3) spgl1.clone();
                        spgl2.setRowguid(UUID.randomUUID().toString());
                        spgl2.setBlzt(9);
                        spgl2.setBlyj("特别程序（开始）");
                        spgl2.setBlsj(auditProject.getBanwandate());
                        ispglxmspsxblxxxxbV3.insert(spgl2);
                        spgl2.setRowguid(UUID.randomUUID().toString());
                        spgl2.setBlzt(10);
                        spgl2.setBlyj("特别程序（结束）");
                        spgl2.setBlsj(auditProject.getBanjiedate());
                        ispglxmspsxblxxxxbV3.insert(spgl2);
                    }

                    // 判断是否秒板
                    if (spgl1.getBlzt() == 11 || spgl1.getBlzt() == 13) {
                        if (auditProject.getBanwandate() != null) {
                            // 推送暂停计时程序
                            SpglXmspsxblxxxxbV3 spgl2 = (SpglXmspsxblxxxxbV3) spgl1.clone();
                            spgl2.setRowguid(UUID.randomUUID().toString());
                            spgl2.setBlzt(9);
                            spgl2.setBlyj("特别程序（开始）");
                            spgl2.setBlsj(auditProject.getBanwandate());
                            ispglxmspsxblxxxxbV3.insert(spgl2);
                            spgl2.setRowguid(UUID.randomUUID().toString());
                            spgl2.setBlzt(10);
                            spgl2.setBlyj("特别程序（结束）");
                            spgl2.setBlsj(auditProject.getBanjiedate());
                            ispglxmspsxblxxxxbV3.insert(spgl2);
                        }
                        // 承诺件
                        if (ZwfwConstant.CONSTANT_INT_TWO == auditProject.getTasktype()) {

                            Date allstart = auditProject.getReceivedate();
                            Date allend = date;
                            if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_ZCBJ
                                    && spgl1.getBlsj() != null) {
                                allend = auditProject.getBanjiedate();
                            }

                            long zttime = 0;
                            Date start = null;
                            sqlc.clear();
                            sqlc.eq("spsxslbm", spgl1.getSpsxslbm());
                            sqlc.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                            sqlc.setOrderAsc("blsj");
                            List<SpglXmspsxblxxxxbV3> listblxxxxb = ispglxmspsxblxxxxbV3.getListByCondition(
                                    sqlc.getMap()).getResult();
                            // 过滤草稿事项
                            listblxxxxb = listblxxxxb.stream()
                                    .filter(a -> !ZwfwConstant.CONSTANT_STR_NEGATIVE_ONE.equals(a.getStr("sync")))
                                    .collect(Collectors.toList());
                            for (SpglXmspsxblxxxxbV3 spglXmspsxblxxxxb : listblxxxxb) {
                                if (9 == spglXmspsxblxxxxb.getBlzt()) {
                                    start = spglXmspsxblxxxxb.getBlsj();
                                }
                                if (10 == spglXmspsxblxxxxb.getBlzt()) {
                                    // 计算非工作日耗时
                                    long diff = spglXmspsxblxxxxb.getBlsj().getTime() - start.getTime();
                                    // 计算工作日天数
                                    int daysnum = ZwfwUtil.weekDays(start, spglXmspsxblxxxxb.getBlsj());
                                    zttime += (diff - (daysnum * 1000 * 60 * 60 * 24));
                                    start = null;
                                }
                            }
                            int alldaysnum = ZwfwUtil.weekDays(allstart, allend);
                            long alltime = allend.getTime() - allstart.getTime() - (alldaysnum * 1000 * 60 * 60 * 24);
                            long usetime = (alltime - zttime) / (1000 * 60);
                            long hour = usetime / 60;
                            long days = hour / 24;
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(Zjconstant.validata_mb)
                                    && hour < Zjconstant.MB_HOUR) {
                                spgl1.set("sjsczt", "-1");
                                if (StringUtil.isNotBlank(spgl1.getSbyy())) {
                                    spgl1.setSbyy(spgl1.getSbyy() + ";受理和办结时间相差小于" + Zjconstant.MB_HOUR
                                            + "小时，时间较短判断为秒办！");
                                }
                                else {
                                    spgl1.setSbyy(
                                            "受理和办结时间相差小于" + Zjconstant.MB_HOUR + "小时，时间较短判断为秒办！");
                                }
                            }
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(Zjconstant.validata_yq)) {
                                // 判断办件信息表
                                SpglXmspsxblxxbV3 spglxmspsxblxxb = iSpglXmspsxblxxbV3.getSpglXmspsxblxxbBySlbm(
                                        auditProject.getFlowsn());
                                if (spglxmspsxblxxb != null) {
                                    if (days >= spglxmspsxblxxb.getSxblsxm()) {
                                        spgl1.set("sjsczt", "-1");
                                        if (StringUtil.isNotBlank(spgl1.getSbyy())) {
                                            spgl1.setSbyy(spgl1.getSbyy() + "; 办件以超期！");
                                        }
                                        else {
                                            spgl1.setSbyy("办件以超期！");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ispglxmspsxblxxxxbV3.insert(spgl1);
                }

                // 插入批复文件信息
                if (StringUtil.isNotBlank(auditProject.getWenhao())) {
                    SpglXmspsxpfwjxxbV3 jxxb = new SpglXmspsxpfwjxxbV3();
                    jxxb.setDfsjzj(auditProject.getRowguid());
                    jxxb.setXzqhdm(areacode);
                    jxxb.setGcdm(gcdm);
                    jxxb.setSpsxslbm(auditProject.getFlowsn());
                    jxxb.setPfrq(auditProject.getCertificatedate());
                    jxxb.setPfwh(auditProject.getWenhao());
                    jxxb.setPfwjbt("批文");
                    jxxb.setPfwjyxqx(EpointDateUtil.convertString2DateAuto("9999-01-01"));
                    List<FrameAttachInfo> attachlist = iattachservice.getAttachInfoListByGuid(
                            auditProject.getRowguid());
                    if (ValidateUtil.isNotBlankCollection(attachlist)) {
                        // 多个批文文件附件处理，插入多次
                        for (FrameAttachInfo frameAttachInfo : attachlist) {
                            jxxb.setRowguid(UUID.randomUUID().toString());
                            jxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                            jxxb.setFjid(frameAttachInfo.getAttachGuid());
                            jxxb.setFjmc(frameAttachInfo.getAttachFileName());
                            jxxb.setFjlx(frameAttachInfo.getContentType());
                            jxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            jxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            jxxb.set("sync", "-1");
                            // 数据验证,项目代码是否在工程代码
                            if (!iSpglXmjbxxbV3.isExistGcdm(gcdm)) {
                                jxxb.setSjsczt(-1);
                                jxxb.setSbyy("批文工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                            }
                            if (!iSpglXmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                                jxxb.setSjsczt(-1);
                                if (StringUtil.isNotBlank(jxxb.getSbyy())) {
                                    jxxb.setSbyy(jxxb.getSbyy()
                                            + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                }
                                else {
                                    jxxb.setSbyy(
                                            "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                }
                            }
                            ispglxmspsxpfwjxxbV3.insert(jxxb);
                        }
                    }
                }
            }
        }
    }

    private Integer getDwlxByCorptype(String corptype) {
        Integer dwlx = 7;
        if ("31".equals(corptype)) {
            dwlx = 1;
        }
        else if ("2".equals(corptype)) {
            dwlx = 4;
        }
        else if ("1".equals(corptype)) {
            dwlx = 3;
        }
        else if ("3".equals(corptype)) {
            dwlx = 2;
        }
        else if ("3".equals(corptype)) {
            dwlx = 5;
        }
        return dwlx;
    }

    /**
     * @param codename
     *         主项名称
     * @param value
     *         子项值
     * @param a
     *         是否必须
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isInCode(String codename, Object value, boolean a) {
        if (value == null) {
            return false;
        }
        String v = value.toString();
        codename = "国标_" + codename;
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        return StringUtil.isNotBlank(s);
    }

    public boolean isNull(Object o) {
        return o == null;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }
}
