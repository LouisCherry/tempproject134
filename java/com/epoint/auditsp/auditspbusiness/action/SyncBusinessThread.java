package com.epoint.auditsp.auditspbusiness.action;

import com.epoint.auditclient.rabbitmqhandle.AcceptClientHandle;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.mq.spgl.api.IJnSpglDfxmsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.auditggconfig.api.IAuditGgConfigService;
import com.epoint.xmz.thirdreporteddata.basic.audittask.meterial.inter.GxhIAuditTaskMaterial;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

@Service
public class SyncBusinessThread extends Thread implements ThreadFactory {
    private static Logger log = LogUtil.getLog(AcceptClientHandle.class);
    private String rowguid;
    private String addbb;
    private String xmqhdm;

    public SyncBusinessThread() {

    }

    public  SyncBusinessThread(String rowguid, String addbb, String xmqhdm) {
        this.rowguid = rowguid;
        this.addbb = addbb;
        this.xmqhdm = xmqhdm;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }

    @Override
    public void run() {
        ISpglsplcxxb iSpglsplcxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcxxb.class);
        IAuditSpBusiness businseeImpl = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditSpPhase auditspphaseImpl = ContainerFactory.getContainInfo().getComponent(IAuditSpPhase.class);
        IAuditSpTask iauditsptask = ContainerFactory.getContainInfo().getComponent(IAuditSpTask.class);
        IJnSpglDfxmsplcjdsxxxb iJnSpglDfxmsplcjdsxxxb = ContainerFactory.getContainInfo().getComponent(IJnSpglDfxmsplcjdsxxxb.class);
        ISpglCommon ispglcommon = ContainerFactory.getContainInfo().getComponent(ISpglCommon.class);
        ISpglspjdxxb iSpglspjdxxb = ContainerFactory.getContainInfo().getComponent(ISpglspjdxxb.class);
        ISpglsplcjdsxxxb iSpglsplcjdsxxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcjdsxxxb.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IAuditSpBasetaskR iauditspbasetaskr = ContainerFactory.getContainInfo().getComponent(IAuditSpBasetaskR.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        ISpglspsxjbxxb iSpglspsxjbxxb = ContainerFactory.getContainInfo().getComponent(ISpglspsxjbxxb.class);
        ISpglSpsxkzxxbService iSpglSpsxkzxxbService = ContainerFactory.getContainInfo()
                .getComponent(ISpglSpsxkzxxbService.class);
        ISpglSpsxclmlxxbService iSpglSpsxclmlxxbService = ContainerFactory.getContainInfo()
                .getComponent(ISpglSpsxclmlxxbService.class);
        IAuditSpBasetask iAuditSpBasetask = ContainerFactory.getContainInfo().getComponent(IAuditSpBasetask.class);
        IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IAuditTaskResult iAuditTaskResult = ContainerFactory.getContainInfo().getComponent(IAuditTaskResult.class);
        GxhIAuditTaskMaterial iAuditTaskMaterial = ContainerFactory.getContainInfo()
                .getComponent(GxhIAuditTaskMaterial.class);
        IConfigService frameConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IAuditGgConfigService iAuditGgConfigService = ContainerFactory.getContainInfo().getComponent(IAuditGgConfigService.class);
        String OfficeWeb365_NET_URL = frameConfigService.getFrameConfigValue("OfficeWeb365_NET_URL");
        // 内网地址
        String zwfwUrl = frameConfigService.getFrameConfigValue("AS_ZWFW_ATTACH_URL");
        // 事务控制
        String msg = "同步成功！";
        String businesstype = "";
        try {
            EpointFrameDsManager.begin(null);
            log.info("3.0主题审批流程阶段同步开始----------");
            // 判断是否有同步记录，获取同步版本号
            Double bbh = iSpglsplcxxb.getMaxSplcbbh(rowguid);
            if (bbh == null) {
                bbh = 1d;
            } else {
                // 生成新版本同步
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(addbb)) {
                    bbh += 1;
                }
            }

            AuditSpBusiness business = businseeImpl.getAuditSpBusinessByRowguid(rowguid).getResult();

            businesstype = business.getBusinesstype();
            Spglsplcxxb spglsplcxxb = new Spglsplcxxb();
            spglsplcxxb.setRowguid(UUID.randomUUID().toString());
            spglsplcxxb.setXzqhdm("370800");
            spglsplcxxb.setDfsjzj(business.getRowguid());
            spglsplcxxb.setSplcbm(business.getRowguid());
            spglsplcxxb.setSplcmc(business.getBusinessname());
            spglsplcxxb.setSplcbbh(bbh);
            spglsplcxxb.setSxrq(business.getDate("SXRQ"));
            spglsplcxxb.setTyrq(business.getDate("TYRQ"));
            spglsplcxxb.setSplcsm(business.getNote());
            spglsplcxxb.setDybzsplclx(Integer.valueOf(business.getStr("splclxV3")));
            spglsplcxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
            spglsplcxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            spglsplcxxb.setSyfw(business.getInt("ggsyfw"));
            spglsplcxxb.setSplctdz(business.getStr("Processimgurl"));
            spglsplcxxb.setOperatedate(new Date());
            if (!isInCode("审批流程类型_3.0", business.getStr("splclxV3"), true)) {
                spglsplcxxb.setSjsczt(-1);
                spglsplcxxb.set("sbyy", "审批流程类型的值不在代码项之中!");
            }
            if (!isInCode("适用范围", business.getInt("ggsyfw"), true)) {
                spglsplcxxb.setSjsczt(-1);
                spglsplcxxb.set("sbyy", "适用范围的值不在代码项之中!");
            }
            Spglsplcxxb oldspglsplcxxb = iSpglsplcxxb.getYxsjBySplcbm(business.getRowguid(), bbh);
            if (oldspglsplcxxb != null) {
                ispglcommon.editToPushData(oldspglsplcxxb, spglsplcxxb);
            } else {
                iSpglsplcxxb.insert(spglsplcxxb);
            }

            // 审批阶段信息
            List<AuditSpPhase> phases = auditspphaseImpl.getSpPaseByBusinedssguid(business.getRowguid()).getResult();
            List<String> listsptaskbxjd = iauditsptask
                    .getBasetaskBySplclxAndPhaseid(business.getStr("splclxV3"), "5").getResult();
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("splcbbh", String.valueOf(bbh));
            sqlc.eq("splcbm", business.getRowguid());
            // 数据有效标识
            sqlc.nq("SJYXBS", ZwfwConstant.CONSTANT_STR_ZERO);
            List<Spglspjdxxb> listjdxxb = iSpglspjdxxb.getListByCondition(sqlc.getMap()).getResult();
            if (phases != null && !phases.isEmpty()) {
                for (AuditSpPhase phase : phases) {
                    String sjsczt = "0";
                    StringBuilder sbyy = new StringBuilder();// 说明
                    Spglspjdxxb spglspjdxxb = new Spglspjdxxb();
                    spglspjdxxb.setRowguid(UUID.randomUUID().toString());
                    spglspjdxxb.setOperatedate(new Date());
                    spglspjdxxb.setXzqhdm("370800");
                    spglspjdxxb.setSplcbm(business.getRowguid());
                    spglspjdxxb.setSplcbbh(bbh);
                    spglspjdxxb.setSpjdbm(phase.getRowguid());
                    spglspjdxxb.setDfsjzj(phase.getRowguid());
                    spglspjdxxb.setSpjdmc(phase.getPhasename());
                    String[] phaseids = phase.getPhaseId().split(",");
                    Integer phaseid = 0;
                    if (phaseids.length > 1) {
                        phaseid = Integer.valueOf(phaseids[0]);
                    } else {
                        phaseid = Integer.valueOf(phase.getPhaseId());
                    }
                    spglspjdxxb.setSpjdxh(phaseid);
                    spglspjdxxb.setDybzspjdxh(phase.getPhaseId());
                    // 验证审批时限
                    String itemText = codeItemsService.getItemTextByCodeName("审批阶段", phase.getPhaseId());
                    if (!"并行推进".equals(itemText)) {
                        if (phase.getSpjdsx() <= 0 || phase.getSpjdsx() > 50) {
                            sjsczt = "-1";
                            sbyy.append("审批阶段时限设置不合理！");
                        }
                    }
                    spglspjdxxb.setSpjdsx(phase.getSpjdsx());

                    if (spglsplcxxb.getSjsczt() == -1) {
                        sjsczt = "-1";
                        sbyy.append("对应审批流程信息表数据校验失败！");
                    }
                    String JDBSZNDZ = iAuditGgConfigService.getConfigValueByName("jdbszndz");
                    String JDBLSBDZ = iAuditGgConfigService.getConfigValueByName("jdblsbdz");
                    if (StringUtil.isNotBlank(JDBSZNDZ)) {
                        spglspjdxxb.setJdbszndz(JDBSZNDZ);
                    }
                    if (StringUtil.isNotBlank(JDBLSBDZ)) {
                        spglspjdxxb.setJdblsbdz(JDBLSBDZ);
                    }

                    spglspjdxxb.set("sjsczt", sjsczt);
                    spglspjdxxb.set("sbyy", sbyy.toString());
                    spglspjdxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    // 存在推送过的数据
                    if (!listjdxxb.isEmpty()) {
                        List<Spglspjdxxb> spglspjdxxbs = listjdxxb.stream()
                                .filter(a -> a.getDfsjzj().equals(spglspjdxxb.getDfsjzj()))
                                .collect(Collectors.toList());
                        if (spglspjdxxbs != null && !spglspjdxxbs.isEmpty()) {
                            ispglcommon.editToPushData(spglspjdxxbs.get(0), spglspjdxxb);
                        } else {
                            iSpglspjdxxb.insert(spglspjdxxb);
                        }
                    } else {
                        iSpglspjdxxb.insert(spglspjdxxb);
                    }

                    // 修改为推送所有辖区这个，同一审批流程类型，同一阶段id下的所有
                    List<String> listsptask = iJnSpglDfxmsplcjdsxxxb
                            .getBasetaskBySplclxAndPhaseid(business.getStr("splclxV3"), phase.getPhaseId());
                    // 去除空串
                    listsptask = listsptask.stream().filter(a -> StringUtil.isNotBlank(a)).collect(Collectors.toList());

                    // 去除所有的主题的并行阶段事项
                    if (!"5".equals(phase.getPhaseId())) {
                        listsptask = listsptask.stream().filter(a -> !listsptaskbxjd.contains(a))
                                .collect(Collectors.toList());
                    }
                    sqlc.clear();
                    sqlc.eq("splcbm", business.getRowguid());
                    sqlc.eq("Splcbbh", String.valueOf(bbh.toString()));
                    sqlc.eq("spjdxh", String.valueOf(phaseid));
                    // 数据有效标识
                    sqlc.nq("SJYXBS", ZwfwConstant.CONSTANT_STR_ZERO);
                    List<Spglsplcjdsxxxb> listspgldfxmsplcjdsxxxb = iSpglsplcjdsxxxb.getListByCondition(sqlc.getMap())
                            .getResult();
                    for (String auditSpTask : listsptask) {
                        sqlc.clear();
                        sqlc.eq("basetaskguid", auditSpTask);
                        List<AuditSpBasetaskR> taskrlist = iauditspbasetaskr
                                .getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                        for (AuditSpBasetaskR auditSpBasetaskR : taskrlist) {
                            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid())
                                    .getResult();
                            if (audittask != null) {
                                AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                        .getTaskExtensionByTaskGuid(audittask.getRowguid(), false).getResult();
                                AuditTaskResult auditTaskResult = iAuditTaskResult
                                        .getAuditResultByTaskGuid(audittask.getRowguid(), false).getResult();
                                List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                        .getUsableMaterialListByTaskguid(audittask.getRowguid()).getResult();
                                if (audittask == null || audittask.getIs_enable() == null
                                        || audittask.getIs_enable() == 0) {
                                    continue;
                                }
                                String sxsjsczt = "0";
                                StringBuilder sxsbyy = new StringBuilder();// 说明
                                Spglsplcjdsxxxb spglsplcjdsxxxb = new Spglsplcjdsxxxb();
                                spglsplcjdsxxxb.setRowguid(UUID.randomUUID().toString());
                                spglsplcjdsxxxb.setDfsjzj(auditSpTask);
                                spglsplcjdsxxxb.setXzqhdm("370800");
                                spglsplcjdsxxxb.setSplcbm(business.getRowguid());
                                //调整，如果是五位，改为截取后三位
                                double version;
                                if (StringUtil.isNotBlank(audittask.getVersion()) && audittask.getVersion().length() == 5) {
                                    version = (Double.parseDouble(audittask.getVersion().substring(audittask.getVersion().length() - 3)));
                                } else {
                                    version = Double.parseDouble(audittask.getVersion());
                                }
                                spglsplcjdsxxxb.setSpsxbbh(version);
                                spglsplcjdsxxxb.setSplcbbh(bbh);
                                spglsplcjdsxxxb.setSpjdxh(phaseid);
                                spglsplcjdsxxxb.setSpsxbm(audittask.getItem_id());

                                // 如果阶段的失败状态时-1，事项的失败哦状态也应该时-1
                                if (spglspjdxxb.getSjsczt() == -1) {
                                    sxsjsczt = "-1";
                                    sxsbyy.append("对应审批流程阶段信息表数据校验失败！");// 说明
                                }
                                spglsplcjdsxxxb.set("sjsczt", sxsjsczt);
                                spglsplcjdsxxxb.setSbyy(sxsbyy.toString());
                                spglsplcjdsxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                spglsplcjdsxxxb.set("sync", 0);
                                // 存在历史推送的数据
                                if (ValidateUtil.isNotBlankCollection(listspgldfxmsplcjdsxxxb)) {
                                    List<Spglsplcjdsxxxb> spgldfxmsplcjdsxxxbs = listspgldfxmsplcjdsxxxb.stream()
                                            .filter(a -> (spglsplcjdsxxxb.getSpsxbm().equals(a.getSpsxbm())
                                                    && String.valueOf(spglsplcjdsxxxb.getSpsxbbh()).equals(String.valueOf(a.getSpsxbbh()))))
                                            .collect(Collectors.toList());
                                    // 获取对应事项
                                    if (spgldfxmsplcjdsxxxbs != null && !spgldfxmsplcjdsxxxbs.isEmpty()) {

                                        ispglcommon.editToPushData(spgldfxmsplcjdsxxxbs.get(0), spglsplcjdsxxxb);
                                    } else {// 新增事项

                                        iSpglsplcjdsxxxb.insert(spglsplcjdsxxxb);
                                    }
                                } else {
                                    iSpglsplcjdsxxxb.insert(spglsplcjdsxxxb);
                                    iSpglsplcjdsxxxb.close();
                                }
                                // 推送过添加一条
                                listspgldfxmsplcjdsxxxb.add(spglsplcjdsxxxb);

                                // 根据 行政区划代码、审批事项编码、审批事项版本号
                                // 查询事项基本信息、事项扩展信息、事项材料目录信息
                                // 1.1 事项基本信息
                                sqlc.clear();
                                sqlc.eq("xzqhdm", xmqhdm);
                                sqlc.eq("spsxbm", audittask.getItem_id());
                                sqlc.eq("spsxbbh", Double.valueOf(audittask.getVersion()).toString());
                                sqlc.nq("SJYXBS", ZwfwConstant.CONSTANT_STR_ZERO);
                                sqlc.nq("sjsczt", "-1");
                                List<SpglSpsxjbxxb> spglSpsxjbxxbs = iSpglspsxjbxxb.getListByCondition(sqlc.getMap())
                                        .getResult();
                                // 1.2事项扩展信息
                                List<SpglSpsxkzxxb> spglSpsxkzxxbs = iSpglSpsxkzxxbService
                                        .getListByCondition(sqlc.getMap()).getResult();
                                // 1.3事项材料目录信息
                                List<SpglSpsxclmlxxb> spglSpsxclmlxxbs = iSpglSpsxclmlxxbService
                                        .getListByCondition(sqlc.getMap()).getResult();
                                // 1.4查询事项有没有重复推送 如果包含了就不进行更新操作
                                List<String> collect = spglSpsxjbxxbs.stream().map(item -> item.getDfsjzj())
                                        .collect(Collectors.toList());
                                if (EpointCollectionUtils.isNotEmpty(collect)
                                        && collect.contains(audittask.getRowguid())) {
                                    continue;
                                }

                                StringBuilder sxsbyyjbxx = new StringBuilder();// 基本信息失败原因
                                StringBuilder sxsbyykzxx = new StringBuilder();// 扩展信息失败原因
                                StringBuilder sxsbyyclml = new StringBuilder();// 材料目录失败原因
                                SpglSpsxjbxxb sqSpsxjbxxb = new SpglSpsxjbxxb();
                                sqSpsxjbxxb.setRowguid(UUID.randomUUID().toString());
                                sqSpsxjbxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                sqSpsxjbxxb.setDfsjzj(audittask.getRowguid());
                                sqSpsxjbxxb.setXzqhdm("370800");
                                sqSpsxjbxxb.setSpsxbbh(version);
                                sqSpsxjbxxb.setSpsxbm(audittask.getItem_id());
                                sqSpsxjbxxb.setSpsxmc(audittask.getTaskname());
                                List<AuditSpBasetask> basetasks = iAuditSpBasetask
                                        .getAuditSpBasetasksBytaskid(audittask.getTask_id(), businesstype);
                                if (ValidateUtil.isNotBlankCollection(basetasks)) {
                                    String taskcode = "";
                                    if (basetasks.size() > ZwfwConstant.CONSTANT_INT_ZERO) {
                                        for (AuditSpBasetask auditspbasetask : basetasks) {
                                            if (StringUtil.isNotBlank(taskcode)) {
                                                taskcode += "," + auditspbasetask.getStr("taskcodeV3");
                                            } else {
                                                taskcode = auditspbasetask.getStr("taskcodeV3");
                                            }
                                        }
                                    }
                                    sqSpsxjbxxb.setDybzspsxbm(taskcode);
                                } else {
                                    sqSpsxjbxxb.setDybzspsxbm("9990000");
                                }
                                sqSpsxjbxxb.setSxbszndz(audittask.getStr("SXBSZNDZ"));
                                sqSpsxjbxxb.setSxzxsbdz(audittask.getStr("SXZXSBDZ"));
                                String jbbm = audittask.getCatalogcode();
                                if (StringUtil.isNotBlank(audittask.getCatalogcode()) && audittask.getCatalogcode().length() > 12) {
                                    jbbm = audittask.getCatalogcode().substring(0, 12);
                                }
                                sqSpsxjbxxb.setJbbm(jbbm);

                                String ssbm = audittask.getTaskcode();
                                if (StringUtil.isNotBlank(audittask.getTaskcode()) && audittask.getTaskcode().length() > 31) {
                                    ssbm = audittask.getTaskcode().substring(0, 31);
                                }
                                sqSpsxjbxxb.setSsbm(ssbm);
                                sqSpsxjbxxb.setYwblxbm(ssbm);
                                sqSpsxjbxxb.setYwblxkzm(audittask.getStr("YWBLXKZM"));
                                //事项类型
                                sqSpsxjbxxb.setSxlx(audittask.getStr("SXLX"));
                                sqSpsxjbxxb.setSdyj(audittask.getBy_law());
                                //权力来源
                                if (StringUtil.isNotBlank(audittask.get("QLLY"))) {
                                    sqSpsxjbxxb.setQlly(Integer.parseInt(audittask.get("QLLY")));
                                }

                                //行使层级
                                if (StringUtil.isNotBlank(auditTaskExtension.getUse_level())) {
                                    sqSpsxjbxxb.setXscj(Integer.parseInt(auditTaskExtension.getUse_level()));
                                }

                                sqSpsxjbxxb.setSxzt(0 == audittask.getIs_enable() ? 2 : audittask.getIs_enable()); // 0转2
                                sqSpsxjbxxb.setSszt(audittask.getOuname());
                                sqSpsxjbxxb.setSsztxz(auditTaskExtension.getSubjectnature());
                                if (StringUtil.isNotBlank(ouService.getFrameOuExtendInfo(audittask.getOuguid()).get("ORGCODE"))) {
                                    sqSpsxjbxxb.setSsztbm(ouService.getFrameOuExtendInfo(audittask.getOuguid()).get("ORGCODE"));
                                } else {
                                    sqSpsxjbxxb.setSjsczt(-1);
                                    sxsbyyjbxx.append("实施主体编码验证失败!");
                                }
                                sqSpsxjbxxb.setSsbmssdqxzqhdm(audittask.getAreacode());
                                sqSpsxjbxxb.setWtbm(audittask.getEntrust_name());
                                sqSpsxjbxxb.setFdbjsx(audittask.getAnticipate_day());
                                //法定办结时限单位
                                sqSpsxjbxxb.setFdbjsxdw(audittask.getAnticipate_type());
                                sqSpsxjbxxb.setFdbjsxsm(audittask.getAnticipate_day_remark());
                                sqSpsxjbxxb.setCnbjsx(audittask.getPromise_day());
                                //承诺办结时限单位
                                sqSpsxjbxxb.setCnbjsxdw(audittask.getPromise_type());
                                sqSpsxjbxxb.setCnbjsxsm(audittask.getPromise_day_remark());
                                sqSpsxjbxxb.setSltj(audittask.getAcceptcondition());
                                if (StringUtil.isNotBlank(audittask.getStr("handle_flow"))) {
                                    // 拼附件预览地址
                                    List<FrameAttachInfo> attachInfo = attachService
                                            .getAttachInfoListByGuid(auditTaskExtension.getFinishproductsamples());
                                    if (EpointCollectionUtils.isNotEmpty(attachInfo)) {
                                        String nwattachurl = zwfwUrl
                                                + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + attachInfo.get(0).getAttachGuid();
                                        sqSpsxjbxxb.setBllc("办理流程说明:" + audittask.getStr("BLLC") + ".办理流程图:" + OfficeWeb365_NET_URL + "fname="
                                                + attachInfo.get(0).getAttachFileName() + "&furl=" + nwattachurl);
                                    }

                                } else {
                                    sqSpsxjbxxb.setBllc("办理流程说明:" + audittask.getStr("BLLC"));
                                }

                                //是否收费
                                sqSpsxjbxxb.setSfsf(audittask.getCharge_flag());
                                sqSpsxjbxxb.setSfyj(audittask.getCharge_basis());
                                //服务对象
                                sqSpsxjbxxb.setFwdx(audittask.getStr("fwdx"));
                                sqSpsxjbxxb.setBjlx(audittask.getType() == 3 ? 4
                                        : audittask.getType() == 4 ? 6 : audittask.getType());
                                //办理形式
                                sqSpsxjbxxb.setBlxs(audittask.getHandletype());

                                if (StringUtil.isNotBlank(auditTaskExtension.getDao_xc_num())) {
                                    sqSpsxjbxxb.setDbsxccs(Integer.parseInt(auditTaskExtension.getDao_xc_num()));
                                }
                                sqSpsxjbxxb.setTbcx(audittask.getSpecialprocedure());
                                sqSpsxjbxxb.setBldd(audittask.getTransact_addr());
                                sqSpsxjbxxb.setBlsj(audittask.getTransact_time());
                                sqSpsxjbxxb.setZxfs(audittask.getLink_tel());
                                sqSpsxjbxxb.setJdtsfs(audittask.getSupervise_tel());
                                sqSpsxjbxxb.setSxrq(audittask.getEffectplan());
                                sqSpsxjbxxb.setTyrq(audittask.getCancelplan());
                                sqSpsxjbxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                sqSpsxjbxxb.setSbyy(sxsbyyjbxx.toString());
                                sqSpsxjbxxb.setSjwxyy(sxsbyyjbxx.toString());
                                sqSpsxjbxxb.set("sync", 0);
                                // 存在历史推送的数据
                                if (ValidateUtil.isNotBlankCollection(spglSpsxjbxxbs)) {
                                    List<SpglSpsxjbxxb> sqSpsxjbxxbs = spglSpsxjbxxbs.stream()
                                            .filter(a -> (sqSpsxjbxxb.getDfsjzj().equals(a.getDfsjzj())))
                                            .collect(Collectors.toList());
                                    // 获取对应项目基本信息
                                    if (sqSpsxjbxxbs != null && !sqSpsxjbxxbs.isEmpty()) {

                                        ispglcommon.editToPushData(sqSpsxjbxxbs.get(0), sqSpsxjbxxb);
                                    } else {// 新增事项

                                        iSpglspsxjbxxb.insert(sqSpsxjbxxb);
                                    }
                                } else {

                                    iSpglspsxjbxxb.insert(sqSpsxjbxxb);
                                }

                                // 事项扩展信息
                                SpglSpsxkzxxb spglSpsxkzxxb = new SpglSpsxkzxxb();
                                spglSpsxkzxxb.setRowguid(UUID.randomUUID().toString());
                                spglSpsxkzxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                spglSpsxkzxxb.setDfsjzj(auditTaskExtension.getRowguid());
                                spglSpsxkzxxb.setXzqhdm("370800");
                                spglSpsxkzxxb.setSpsxbbh(version);
                                spglSpsxkzxxb.setSpsxbm(audittask.getItem_id());
                                //是否进驻建设窗口
                                spglSpsxkzxxb.setSfjzzhck(auditTaskExtension.get("SFJZZHCK"));
                                //服务渠道
                                spglSpsxkzxxb.setFwqd(auditTaskExtension.getStr("FWQD"));
                                spglSpsxkzxxb.setLbjg(audittask.getOther_togetdept());
                                //行政许可类型
                                if (StringUtil.isNotBlank(auditTaskExtension.getStr("XZXKSXLX"))) {
                                    spglSpsxkzxxb.setXzxksxlx(Integer.parseInt(auditTaskExtension.getStr("XZXKSXLX")));
                                }

                                spglSpsxkzxxb.setZyxzxkdtj(auditTaskExtension.getStr("ZYXZXKDTJ"));
                                spglSpsxkzxxb.setGdxzxktjdyj(auditTaskExtension.getStr("GDXZXKTJDYJ"));
                                spglSpsxkzxxb.setGdsfbzdyj(auditTaskExtension.getStr("GDXZXKTJDYJ"));
                                if (StringUtil.isNotBlank(auditTaskExtension.getStr("YWZJFWSX"))) {
                                    spglSpsxkzxxb.setYwzjfwsx(Integer.parseInt(auditTaskExtension.getStr("YWZJFWSX")));
                                }
                                spglSpsxkzxxb.setZjfwsxmc(auditTaskExtension.getStr("ZJFWSXMC"));
                                spglSpsxkzxxb.setSdzjfwsxdyj(auditTaskExtension.getStr("SDZJFWSXDYJ"));
                                if (auditTaskResult != null) {
                                    spglSpsxkzxxb
                                            .setSpjglx(("40").equals(auditTaskResult.getResulttype().toString()) ? "30"
                                                    : auditTaskResult.getResulttype().toString());
                                    if (!isInCodeYesOrNo("审批结果类型", auditTaskResult.getResulttype(), true)) {
                                        spglSpsxkzxxb.setSjsczt(-1);
                                        sxsbyykzxx.append("审批结果类型的值不在代码项之中!");
                                    }
                                }
                                spglSpsxkzxxb.setSpjgmc(auditTaskExtension.getFinishfilename());
                                List<FrameAttachInfo> attachInfo = attachService
                                        .getAttachInfoListByGuid(auditTaskExtension.getFinishproductsamples());

                                if (EpointCollectionUtils.isNotEmpty(attachInfo)) {
                                    String nwattachurl = zwfwUrl
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attachInfo.get(0).getAttachGuid();
                                    spglSpsxkzxxb.setSpjgyb(OfficeWeb365_NET_URL + "fname="
                                            + attachInfo.get(0).getAttachFileName() + "&furl=" + nwattachurl);
                                }

                                //是否支持预约办理
                                if (StringUtil.isNotBlank(auditTaskExtension.getReservationmanagement())) {
                                    spglSpsxkzxxb.setSfzcyybl(Integer.parseInt(auditTaskExtension.getReservationmanagement()));
                                }

                                //是否支持网上支付
                                if (StringUtil.isNotBlank(auditTaskExtension.getOnlinepayment())) {
                                    spglSpsxkzxxb.setSfzcwszf(Integer.parseInt(auditTaskExtension.getOnlinepayment()));
                                }

                                //是否支持物流快递
                                if (StringUtil.isNotBlank(auditTaskExtension.getIf_express())) {
                                    spglSpsxkzxxb.setSfzcwlkd(Integer.parseInt(auditTaskExtension.getIf_express()));
                                } else {
                                    spglSpsxkzxxb.setSfzcwlkd(0);
                                }

                                //是否支持自主终端办理
                                spglSpsxkzxxb.setSfzczzzdbl(auditTaskExtension.get("SFZCZZZDBL"));

                                //是否网办
                                spglSpsxkzxxb.setSfwb(auditTaskExtension.get("SFWB"));

                                if (StringUtil.isNotBlank(audittask.get("WSBLSD"))) {
                                    spglSpsxkzxxb.setWsblsd(audittask.get("WSBLSD"));
                                }

                                //是否实行告知承诺办理
                                spglSpsxkzxxb.setSfsxgzcnbl(auditTaskExtension.get("SFSXGZCNBL"));

                                spglSpsxkzxxb.setBxxcblyysm(auditTaskExtension.get("BXXCBLYYSM"));

                                //是否需要现场勘验
                                spglSpsxkzxxb.setSfxyxcky(auditTaskExtension.get("SFXYXCKY"));

                                //是否需要组织听证
                                spglSpsxkzxxb.setSfxyzztz(auditTaskExtension.get("SFXYZZTZ"));

                                //是否需要招标、拍卖、挂牌交易
                                spglSpsxkzxxb.setSfxyzbpmgpjy(auditTaskExtension.get("SFXYZZTZ"));

                                //是否需要检验、检测、检疫
                                spglSpsxkzxxb.setSfxyjyjcjy(auditTaskExtension.get("SFXYJYJCJY"));

                                //是否需要鉴定
                                spglSpsxkzxxb.setSfxyjd(auditTaskExtension.get("SFXYJD"));

                                //是否需要专家评审
                                spglSpsxkzxxb.setSfxyzjps(auditTaskExtension.get("SFXYZJPS"));

                                //是否需要向社会公示
                                spglSpsxkzxxb.setSfxyxshgs(auditTaskExtension.get("SFXYXSHGS"));

                                spglSpsxkzxxb.setXzxkzjmc(auditTaskExtension.getStr("XZXKZJMC"));
                                spglSpsxkzxxb.setXzxkzjdyxqx(auditTaskExtension.getStr("XZXKZJDYXQX"));

                                //有效期限单位
                                if (StringUtil.isNotBlank(auditTaskExtension.getStr("XZXKZJDYXQXDW"))) {
                                    spglSpsxkzxxb.setXzxkzjdyxqxdw(Integer.parseInt(auditTaskExtension.getStr("XZXKZJDYXQXDW")));
                                }

                                spglSpsxkzxxb.setGdxzxkzjyxqxdyj(auditTaskExtension.getStr("GDXZXKZJYXQXDYJ"));
                                spglSpsxkzxxb.setBlxzxkzjyxsxdyq(auditTaskExtension.getStr("BLXZXKZJYXSXDYQ"));
                                spglSpsxkzxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                spglSpsxkzxxb.setSbyy(sxsbyykzxx.toString());
                                spglSpsxkzxxb.setSjwxyy(sxsbyykzxx.toString());
                                spglSpsxkzxxb.set("sync", "0");
                                // 存在历史推送的数据
                                if (ValidateUtil.isNotBlankCollection(spglSpsxkzxxbs)) {
                                    List<SpglSpsxkzxxb> spsxkzxxbs = spglSpsxkzxxbs.stream()
                                            .filter(a -> (spglSpsxkzxxb.getDfsjzj().equals(a.getDfsjzj())))
                                            .collect(Collectors.toList());
                                    // 获取对应事项扩展信息
                                    if (spsxkzxxbs != null && !spsxkzxxbs.isEmpty()) {

                                        ispglcommon.editToPushData(spsxkzxxbs.get(0), spglSpsxkzxxb);
                                    } else {// 新增事项

                                        iSpglSpsxkzxxbService.insert(spglSpsxkzxxb);
                                    }
                                } else {

                                    iSpglSpsxkzxxbService.insert(spglSpsxkzxxb);
                                }
                                if (EpointCollectionUtils.isNotEmpty(auditTaskMaterials)) {
                                    for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                                        // 事项材料目录信息
                                        SpglSpsxclmlxxb spglSpsxclmlxxb = new SpglSpsxclmlxxb();
                                        spglSpsxclmlxxb.setRowguid(UUID.randomUUID().toString());
                                        spglSpsxclmlxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                        spglSpsxclmlxxb.setDfsjzj(auditTaskMaterial.getRowguid());
                                        spglSpsxclmlxxb.setXzqhdm("370800");
                                        spglSpsxclmlxxb.setSpsxbbh(version);
                                        spglSpsxclmlxxb.setSpsxbm(audittask.getItem_id());
                                        spglSpsxclmlxxb.setPx(auditTaskMaterial.getOrdernum());
                                        spglSpsxclmlxxb.setClmlbh(StringUtil.isNotBlank(auditTaskMaterial.get("CLMLBH")) ? auditTaskMaterial.get("CLMLBH") : auditTaskMaterial.getMaterialid());
                                        spglSpsxclmlxxb.setClmc(auditTaskMaterial.getMaterialname());
                                        if (StringUtil.isNotBlank(auditTaskMaterial.get("CLXS"))) {
                                            spglSpsxclmlxxb.setCllx(Integer.parseInt(auditTaskMaterial.getStr("CLXS")));
                                        }
                                        if (StringUtil.isNotBlank(auditTaskMaterial.getSubmittype())) {
                                            spglSpsxclmlxxb.setClxs(
                                                    Integer.parseInt(auditTaskMaterial.getSubmittype()) == 10 ? 2
                                                            : Integer.parseInt(auditTaskMaterial.getSubmittype()) == 20 ? 1 : 3);
                                        }

                                        spglSpsxclmlxxb.setClbyx((auditTaskMaterial.getIs_rongque() == 1 ? 3
                                                : (auditTaskMaterial.getNecessity() == 10 ? 1
                                                : (auditTaskMaterial.getNecessity() == 20 ? 2 : 0))));
                                        if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                            List<FrameAttachInfo> attachInfos = attachService
                                                    .getAttachInfoListByGuid(auditTaskMaterial.getTemplateattachguid());
                                            if (EpointCollectionUtils.isNotEmpty(attachInfos)) {
                                                String nwattachurl = zwfwUrl
                                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                        + attachInfos.get(0).getAttachGuid();
                                                spglSpsxclmlxxb.setKbbg(OfficeWeb365_NET_URL + "fname="
                                                        + attachInfos.get(0).getAttachFileName() + "&furl=" + nwattachurl);

                                            }
                                        }

                                        if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                            List<FrameAttachInfo> attachInfo1s = attachService
                                                    .getAttachInfoListByGuid(auditTaskMaterial.getExampleattachguid());
                                            if (EpointCollectionUtils.isNotEmpty(attachInfo1s)) {
                                                String nwattachurl1 = zwfwUrl
                                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                        + attachInfo1s.get(0).getAttachGuid();
                                                spglSpsxclmlxxb.setSlyb(OfficeWeb365_NET_URL + "fname="
                                                        + attachInfo1s.get(0).getAttachFileName() + "&furl=" + nwattachurl1);
                                            }
                                        }

                                        if (StringUtil.isNotBlank(auditTaskMaterial.getFile_source())) {
                                            if (isNumeric(auditTaskMaterial.getFile_source())) {
                                                spglSpsxclmlxxb.setLyqd(Integer.parseInt(auditTaskMaterial.getFile_source()));
                                            }
                                        }
                                        spglSpsxclmlxxb.setLyqdsm(auditTaskMaterial.getStr("LYQDSM"));
                                        spglSpsxclmlxxb.setZzclfs(auditTaskMaterial.getPage_num());
                                        spglSpsxclmlxxb.setZzclgg(auditTaskMaterial.getStr("ZZCLGG"));
                                        spglSpsxclmlxxb.setTbxz(auditTaskMaterial.getFile_explian());
                                        spglSpsxclmlxxb.setSlbz(auditTaskMaterial.getStandard());
                                        spglSpsxclmlxxb.setYqtgcldyj(auditTaskMaterial.getStr("YQTGCLDYJ"));
                                        spglSpsxclmlxxb.setBz(auditTaskMaterial.getStr("BZ"));
                                        spglSpsxclmlxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                        spglSpsxclmlxxb.setSbyy(sxsbyyclml.toString());
                                        spglSpsxclmlxxb.setSjwxyy(sxsbyyclml.toString());
                                        spglSpsxclmlxxb.setSync(0);
                                        // 存在历史推送的数据
                                        if (ValidateUtil.isNotBlankCollection(spglSpsxclmlxxbs)) {
                                            List<SpglSpsxclmlxxb> spsxclmlxxbs = spglSpsxclmlxxbs.stream()
                                                    .filter(a -> (spglSpsxclmlxxb.getDfsjzj().equals(a.getDfsjzj())))
                                                    .collect(Collectors.toList());
                                            // 获取对应事项材料目录信息
                                            if (spsxclmlxxbs != null && !spsxclmlxxbs.isEmpty()) {

                                                ispglcommon.editToPushData(spsxclmlxxbs.get(0), spglSpsxclmlxxb);
                                            } else {// 新增事项材料目录信息

                                                iSpglSpsxclmlxxbService.insert(spglSpsxclmlxxb);
                                            }
                                        } else {
                                            iSpglSpsxclmlxxbService.insert(spglSpsxclmlxxb);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            log.info("3.0主题审批流程阶段同步结束----------");
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            msg = "同步失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } finally {
            EpointFrameDsManager.close();
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
        /*if (a) {
            if (value == null) {
                throw new RuntimeException(codename + "的值不能为空！");
            }
        }*/
        String v = value.toString();
        codename = "国标_" + codename;
        ICodeItemsService icodeitemsservice = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        return StringUtil.isNotBlank(s);
    }

    /**
     * @param codename 主项名称
     * @param value    子项值
     * @param a        是否必须
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isInCodeYesOrNo(String codename, Object value, boolean a) {
        if (a) {
            if (value == null) {
                throw new RuntimeException(codename + "的值不能为空！");
            }
        }
        String v = value.toString();
        ICodeItemsService icodeitemsservice = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        return StringUtil.isNotBlank(s);
    }

    //判断是否是数字
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

}
