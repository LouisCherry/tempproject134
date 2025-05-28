package com.epoint.auditsp.auditspbusiness.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskjs.domain.AuditSpBasetaskJS;
import com.epoint.basic.auditsp.auditspbasetaskjs.inter.IAuditSpBasetaskJS;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdxxb;
import com.epoint.basic.spgl.domain.SpglDfxmsplcxxb;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcjdxxb;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcxxb;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 主题配置表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@SuppressWarnings("unchecked")
@RestController("jnauditspbusinesslistaction")
@Scope("request")
public class JNAuditSpBusinessListAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = 7487597167764665058L;

    @Autowired
    private IAuditSpBusiness businseeImpl;

    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private ISpglDfxmsplcxxb ispgldfxmsplcxxb;

    @Autowired
    private ISpglDfxmsplcjdxxb ispgldfxmsplcjdxxb;

    @Autowired
    private ISpglCommon ispglcommon;

    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private IAuditSpPhase auditspphaseImpl;

    @Autowired
    private IAuditSpBasetask iauditspbasetask;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;

    @Autowired
    private IOuService iouservice;

    @Autowired
    private ISpglDfxmsplcjdsxxxb ispgldfxmsplcjdsxxxb;

    @Autowired
    private IAuditSpTask iauditsptask;

    @Autowired
    private IAttachService frameAttachInfoService;

    @Autowired
    private IAuditSpBasetaskJS iauditspbasetaskjs;

    /**
     * 主题类别，这里主要是为了区分是建设项目的还是一般并联审批的下拉列表model
     */
    private List<SelectItem> businesstypeModel = null;
    /**
     * 主题配置表实体对象
     */
    private AuditSpBusiness dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpBusiness> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String businessname;

    private String businesstype;

    @Override
    public void pageLoad() {
        businesstype = getRequestParameter("businesstype");
        // auditSpTaskService.getAllAuditSpTaskByBusinessGuid("0c444c62-ecb4-4aae-8cd0-a67903220fe2");
        addCallbackParam("centerGuid", ZwfwUserSession.getInstance().getCenterGuid());
        //获取areacode
        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
        if (area != null) {
            addCallbackParam("citylevel", area.getCitylevel());
        }
    }

    /**
     * 更改事项开启状态
     *
     * @throws InterruptedException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeAuditTaskStatus(String rowguid) throws InterruptedException {
        EpointFrameDsManager.begin(null);
        dataBean = businseeImpl.getAuditSpBusinessByRowguid(rowguid).getResult();
        String msg = "";

        if (dataBean.getDate("TYRQ") != null && EpointDateUtil.compareDateOnDay(dataBean.getDate("TYRQ"), new Date()) < 0) {
            msg = "该主题停用日期已过期,启用前请先维护停用日期！";
            addCallbackParam("msg", msg);
            return;
        }

        if (ZwfwConstant.CONSTANT_STR_ONE.equals(dataBean.getDel())) {
            dataBean.setDel("0");
            msg = "启用成功！";
        } else {
            dataBean.setDel("1");
            msg = "禁用成功！";
        }
        businseeImpl.saveUpdateAuditSpBusiness(dataBean);
        EpointFrameDsManager.commit();
        if ("2".equals(dataBean.getBusinesstype())) {
            syncWindowBusiness("1".equals(dataBean.getDel()) ? "delete" : "enable", dataBean.getRowguid());
        }
        addCallbackParam("msg", msg);
    }

    public void syncWindowBusiness(String SendType, String businessGuid) {
        // TODO 事项变更之后需要使用通知的方式来处理，不能直接进行更新
        // 2017_4_7 CH 事项变更以后发送消息至RabbitMQ队列
        try {
            String RabbitMQMsg = "handleSerachIndexBusiness:" + SendType + ";" + businessGuid;
            ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SyncBusiness(String rowguid, String addbb) {
        String msg = "同步成功！";
        try {
            EpointFrameDsManager.begin(null);
            /* 3.0新增逻辑开始*/
            String is_open_v3 = configService.getFrameConfigValue("IS_OPEN_V3");
            if (("1").equals(is_open_v3) || ("2").equals(is_open_v3)) {
                // 异步调用SyncBusinessV3生成3.0上报数据
                ExecutorService executorService = Executors.newCachedThreadPool();
                try {
                    executorService.execute(new SyncBusinessThread(rowguid, addbb, ZwfwUserSession.getInstance().getAreaCode()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    executorService.shutdown();
                }
            }

            if (("1").equals(is_open_v3) || ("0").equals(is_open_v3)) {
                //走原2.0生成逻辑
                SyncBusiness1(rowguid, addbb);
            }
            /* 3.0新增逻辑结束*/
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            msg = "同步失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } finally {
            EpointFrameDsManager.close();
            addCallbackParam("msg", msg);
        }
    }

    public void SyncBusiness1(String rowguid, String addbb) {
        // 事务控制
        String msg = "同步成功！";
        try {
            EpointFrameDsManager.begin(null);

            // 判断是否有同步记录，获取同步版本号
            Double bbh = ispgldfxmsplcxxb.getMaxSplcbbh(rowguid);
            if (bbh == null) {
                bbh = 1d;
            } else {
                // 生成新版本同步
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(addbb)) {
                    bbh += 1;
                }
            }

            AuditSpBusiness business = businseeImpl.getAuditSpBusinessByRowguid(rowguid).getResult();
            SpglDfxmsplcxxb spglDfxmsplcxxb = new SpglDfxmsplcxxb();
            spglDfxmsplcxxb.setRowguid(UUID.randomUUID().toString());
            spglDfxmsplcxxb.setXzqhdm("370800");
            spglDfxmsplcxxb.setOperatedate(new Date());
            spglDfxmsplcxxb.setDfsjzj(business.getRowguid());
            spglDfxmsplcxxb.setSplcbm(business.getRowguid());
            spglDfxmsplcxxb.setSplcmc(business.getBusinessname());
            spglDfxmsplcxxb.setSplcbbh(bbh);
            spglDfxmsplcxxb.setSplcsxsj(new Date());
            spglDfxmsplcxxb.setSplcsm(business.getNote());
            spglDfxmsplcxxb.setFjid(business.getTaskoutimgguid());
            FrameAttachStorage attach = frameAttachInfoService.getAttach(business.getTaskoutimgguid());
            spglDfxmsplcxxb.setFjmc(attach.getAttachFileName());
            spglDfxmsplcxxb.setFjlx(attach.getContentType());
            spglDfxmsplcxxb.setSplclx(business.getSplclx());
            spglDfxmsplcxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
            spglDfxmsplcxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            if (!isInCode("审批流程类型", business.getSplclx(), true)) {
                spglDfxmsplcxxb.setSjsczt(-1);
                spglDfxmsplcxxb.set("sbyy", "审批流程类型的值不在代码项之中!");
            }
            SpglDfxmsplcxxb oldspglDfxmsplcxxb = ispgldfxmsplcxxb.getYxsjBySplcbm(business.getRowguid(), bbh);
            if (oldspglDfxmsplcxxb != null) {
                ispglcommon.editToPushData(oldspglDfxmsplcxxb, spglDfxmsplcxxb);
            } else {
                ispgldfxmsplcxxb.insert(spglDfxmsplcxxb);
            }
            List<AuditSpPhase> phases = auditspphaseImpl.getSpPaseByBusinedssguid(business.getRowguid()).getResult();
            List<String> listsptaskbxjd = iauditsptask
                    .getBasetaskBySplclxAndPhaseid(String.valueOf(business.getSplclx()), "5").getResult();
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("splcbbh", String.valueOf(bbh));
            sqlc.eq("splcbm", business.getRowguid());
            // 数据有效标识
            sqlc.nq("SJYXBS", ZwfwConstant.CONSTANT_STR_ZERO);
            List<SpglDfxmsplcjdxxb> listjdxxb = ispgldfxmsplcjdxxb.getListByCondition(sqlc.getMap()).getResult();
            if (phases != null && !phases.isEmpty()) {
                for (AuditSpPhase phase : phases) {
                    String sjsczt = "0";
                    StringBuilder sbyy = new StringBuilder();// 说明
                    SpglDfxmsplcjdxxb spglDfxmsplcjdxxb = new SpglDfxmsplcjdxxb();
                    spglDfxmsplcjdxxb.setRowguid(UUID.randomUUID().toString());
                    spglDfxmsplcjdxxb.setXzqhdm("370800");
                    spglDfxmsplcjdxxb.setOperatedate(new Date());
                    spglDfxmsplcjdxxb.setSplcbm(business.getRowguid());
                    spglDfxmsplcjdxxb.setSplcbbh(bbh);
                    spglDfxmsplcjdxxb.setSpjdbm(phase.getRowguid());
                    spglDfxmsplcjdxxb.setDfsjzj(phase.getRowguid());
                    spglDfxmsplcjdxxb.setSpjdmc(phase.getPhasename());
                    String[] phaseids = phase.getPhaseId().split(",");
                    Integer phaseid = 0;
                    if (phaseids.length > 1) {
                        phaseid = Integer.valueOf(phaseids[0]);
                    } else {
                        phaseid = Integer.valueOf(phase.getPhaseId());
                    }
                    ;
                    spglDfxmsplcjdxxb.setSpjdxh(phaseid);
                    spglDfxmsplcjdxxb.setDybzspjdxh(phase.getPhaseId());
                    // 验证审批时限
                    if (phase.getSpjdsx() <= 0 || phase.getSpjdsx() > 50) {
                        sjsczt = "-1";
                        sbyy.append("审批阶段时限设置不合理！");
                    }
                    spglDfxmsplcjdxxb.setSpjdsx(phase.getSpjdsx());
                    spglDfxmsplcjdxxb.setLcbsxlx(ZwfwConstant.CONSTANT_INT_ONE);// 里程碑事项类型，默认为1

                    if (spglDfxmsplcxxb.getSjsczt() == -1) {
                        sjsczt = "-1";
                        sbyy.append("对应审批流程信息表数据校验失败！");
                    }

                    spglDfxmsplcjdxxb.set("sjsczt", sjsczt);
                    spglDfxmsplcjdxxb.set("sbyy", sbyy.toString());
                    spglDfxmsplcjdxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    // 存在推送过的数据
                    if (!listjdxxb.isEmpty()) {
                        List<SpglDfxmsplcjdxxb> spgldfxmsplcjdxxbs = listjdxxb.stream()
                                .filter(a -> a.getDfsjzj().equals(spglDfxmsplcjdxxb.getDfsjzj()))
                                .collect(Collectors.toList());
                        if (spgldfxmsplcjdxxbs != null && !spgldfxmsplcjdxxbs.isEmpty()) {
                            // 存在数据，修改更新
                            ispglcommon.editToPushData(spgldfxmsplcjdxxbs.get(0), spglDfxmsplcjdxxb);

                        } else {// 阶段新增，直接察输入数据
                            ispgldfxmsplcjdxxb.insert(spglDfxmsplcjdxxb);
                        }
                    } else {
                        ispgldfxmsplcjdxxb.insert(spglDfxmsplcjdxxb);
                    }

                    // 修改为推送所有辖区这个，同一审批流程类型，同一阶段id下的所有
                    List<String> listsptask = iauditsptask
                            .getBasetaskBySplclxAndPhaseid(String.valueOf(business.getSplclx()), phase.getPhaseId())
                            .getResult();
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
                    List<SpglDfxmsplcjdsxxxb> listspgldfxmsplcjdsxxxb = ispgldfxmsplcjdsxxxb
                            .getListByCondition(sqlc.getMap()).getResult();
                    for (String auditSpTask : listsptask) {
                        sqlc.clear();
                        sqlc.eq("basetaskguid", auditSpTask);
                        log.info("工改事项的basetaskguid:" + auditSpTask);
                        List<AuditSpBasetaskR> taskrlist = iauditspbasetaskr
                                .getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                        for (AuditSpBasetaskR auditSpBasetaskR : taskrlist) {
                            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid())
                                    .getResult();
                            if (audittask == null) {
                                continue;
                            }
                            String sxsjsczt = "0";
                            StringBuilder sxsbyy = new StringBuilder();// 说明
                            SpglDfxmsplcjdsxxxb spgldfxmsplcjdsxxxb = new SpglDfxmsplcjdsxxxb();
                            spgldfxmsplcjdsxxxb.setRowguid(UUID.randomUUID().toString());
                            spgldfxmsplcjdsxxxb.setDfsjzj(auditSpTask);
                            spgldfxmsplcjdsxxxb.setXzqhdm("370800");
                            spgldfxmsplcjdsxxxb.setOperatedate(new Date());
                            spgldfxmsplcjdsxxxb.setSplcbm(business.getRowguid());

                            //调整，如果是五位，改为截取后三位
                            double version;
                            if (StringUtil.isNotBlank(audittask.getVersion()) && audittask.getVersion().length() == 5) {
                                version = (Double.parseDouble(audittask.getVersion().substring(audittask.getVersion().length() - 3)));
                            } else {
                                version = Double.parseDouble(audittask.getVersion());
                            }

                            spgldfxmsplcjdsxxxb.setSpsxbbh(version);
                            spgldfxmsplcjdsxxxb.setSplcbbh(bbh);
                            spgldfxmsplcjdsxxxb.setSpjdxh(phaseid);
                            spgldfxmsplcjdsxxxb.setSpsxmc(audittask.getTaskname());
                            spgldfxmsplcjdsxxxb.setSpsxbm(audittask.getItem_id());
                            List<AuditSpBasetask> basetasks = iauditspbasetask
                                    .getAuditSpBasetasksBytaskid(audittask.getTask_id(), businesstype);
                            if (ValidateUtil.isNotBlankCollection(basetasks)) {
                                AuditSpBasetask basetask = basetasks.get(0);
                                if (basetasks.size() > ZwfwConstant.CONSTANT_INT_ONE) {
                                    String taskcode = basetasks.stream().map(AuditSpBasetask::getTaskcode)
                                            .collect(Collectors.joining(","));
                                    basetask.setTaskcode(taskcode);
                                }
                                spgldfxmsplcjdsxxxb.setDybzspsxbm(basetask.getTaskcode());
                                spgldfxmsplcjdsxxxb.setSflcbsx(StringUtil.isNotBlank(basetask.getSflcbsx())
                                        ? Integer.valueOf(basetask.getSflcbsx()) : ZwfwConstant.CONSTANT_INT_ZERO); // 是否里程碑事项。默认0
                            } else {
                                spgldfxmsplcjdsxxxb.setDybzspsxbm("9990");
                            }
                            spgldfxmsplcjdsxxxb.setSfsxgzcnz(ZwfwConstant.CONSTANT_INT_ZERO);// 是否实行告知承诺制
                            // 默认0

                            spgldfxmsplcjdsxxxb.setBjlx(audittask.getType());

                            if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1
                                    && audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                                spgldfxmsplcjdsxxxb.setSqdx(3);
                            } else {
                                if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                                    spgldfxmsplcjdsxxxb.setSqdx(1);
                                } else if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1) {
                                    spgldfxmsplcjdsxxxb.setSqdx(2);
                                }
                            }
                            spgldfxmsplcjdsxxxb.setBljgsdfs("2"); // 办理送达方式 默认
                            // 申请对象窗口领取

                            if (audittask.getPromise_day() > phase.getSpjdsx()) {
                                sxsjsczt = "-1";
                                sxsbyy.append("事项承诺期限大于审批阶段时限！");// 说明
                            }
                            spgldfxmsplcjdsxxxb.setSpsxblsx(audittask.getPromise_day());
                            FrameOu frameOu = iouservice.getOuByOuGuid(audittask.getOuguid());
                            if (frameOu != null) {
                                spgldfxmsplcjdsxxxb.setSpbmbm(frameOu.getOucode());
                            }

                            spgldfxmsplcjdsxxxb.setSpbmmc(getOunamebyOuguid(audittask.getOuguid()));
                            spgldfxmsplcjdsxxxb.setQzspsxbm(null);

                            // 如果阶段的失败状态时-1，事项的失败哦状态也应该时-1
                            if (spglDfxmsplcjdxxb.getSjsczt() == -1) {
                                sxsjsczt = "-1";
                                sxsbyy.append("对应审批流程阶段信息表数据校验失败！");// 说明
                            }
                            spgldfxmsplcjdsxxxb.set("sjsczt", sxsjsczt);
                            spgldfxmsplcjdsxxxb.setSbyy(sxsbyy.toString());
                            spgldfxmsplcjdsxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            // 存在历史推送的数据
                            if (ValidateUtil.isNotBlankCollection(listspgldfxmsplcjdsxxxb)) {
                                List<SpglDfxmsplcjdsxxxb> spgldfxmsplcjdsxxxbs = listspgldfxmsplcjdsxxxb.stream()
                                        .filter(a -> (spgldfxmsplcjdsxxxb.getSpsxbm().equals(a.getSpsxbm())
                                                && spgldfxmsplcjdsxxxb.getSpsxbbh().equals(a.getSpsxbbh())))
                                        .collect(Collectors.toList());
                                // 获取对应事项
                                if (!spgldfxmsplcjdsxxxbs.isEmpty()) {
                                    log.info("事项版本发生变更:" + spgldfxmsplcjdsxxxb.getSpsxmc() + spgldfxmsplcjdsxxxb.getSpsxbm());
                                    SpglDfxmsplcjdsxxxb oldxxxb = spgldfxmsplcjdsxxxbs.get(0);
                                    if (StringUtil.isBlank(oldxxxb.getSflcbsx())) {
                                        oldxxxb.setSflcbsx(0);
                                    }
                                    if (StringUtil.isBlank(spgldfxmsplcjdsxxxb.getSflcbsx())) {
                                        spgldfxmsplcjdsxxxb.setSflcbsx(0);
                                    }
                                    ispglcommon.editToPushData(oldxxxb, spgldfxmsplcjdsxxxb);
                                } else {// 新增事项
                                    log.info("事项版本插入1:" + spgldfxmsplcjdsxxxb.getSpsxmc());
                                    ispgldfxmsplcjdsxxxb.insert(spgldfxmsplcjdsxxxb);
                                }
                            } else {
                                log.info("事项版本插入2:" + spgldfxmsplcjdsxxxb.getSpsxmc());
                                ispgldfxmsplcjdsxxxb.insert(spgldfxmsplcjdsxxxb);
                            }
                            // 推送过添加一条
                            listspgldfxmsplcjdsxxxb.add(spgldfxmsplcjdsxxxb);
                        }
                    }
                }
            }
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            msg = "同步失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } finally {
            addCallbackParam("msg", msg);
        }
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String yewutag : select) {
            EpointFrameDsManager.begin(null);
            //删除主题，同时删除关联联系
            dataBean = businseeImpl.getAuditSpBusinessByRowguid(yewutag).getResult();
            dataBean.setDel("1");
            businseeImpl.saveUpdateAuditSpBusiness(dataBean);
            EpointFrameDsManager.commit();
            if ("2".equals(dataBean.getBusinesstype())) {
                syncWindowBusiness("delete", dataBean.getRowguid());
            }
        }
        addCallbackParam("msg", "成功禁用！");
    }

    /**
     * 保存修改--主要是报存排序
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveAuditSpBusiness() {
        List<AuditSpBusiness> auditSpBusinesskList = this.getDataGridData().getWrappedData();
        for (AuditSpBusiness auditSpBusiness : auditSpBusinesskList) {
            if (auditSpBusiness.getOrdernumber() == null) {// null会跑到最前
                auditSpBusiness.setOrdernumber(0);
            }
            for (AuditSpBusiness asp : auditSpBusinesskList) {
                businseeImpl.saveUpdateAuditSpBusiness(asp);
            }
        }
        addCallbackParam("msg", "保存成功");
    }

    public DataGridModel<AuditSpBusiness> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpBusiness>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1873697847810068679L;

                @Override
                public List<AuditSpBusiness> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    SqlConditionUtil sql = new SqlConditionUtil();

                    if (StringUtil.isNotBlank(businessname)) {
                        sql.like("businessname", businessname);
                    }
                    if (StringUtil.isNotBlank(businesstype)) {
                        sql.in("businesstype", businesstype + ",5");
                    }

                    sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    sql.setOrder("ordernumber", sortOrder);
                    sql.setOrder("operatedate", sortOrder);

                    PageData<AuditSpBusiness> pageData = businseeImpl
                            .getAuditSpBusinessPageData(sql.getMap(), first, pageSize, "", "").getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
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

    public List<SelectItem> getBusinesstypeModel() {
        if (businesstypeModel == null) {
            businesstypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "auditsp_ztlb", null, false));
        }
        return this.businesstypeModel;
    }

    public String getOunamebyOuguid(String ouguid) {
        String ouname = "";
        FrameOuExtendInfo extendInfo = iouservice.getFrameOuExtendInfo(ouguid);
        if (extendInfo != null) {
            String areaCode = extendInfo.get("areacode");
            AuditOrgaArea auditOrgaArea = iauditorgaarea.getAreaByAreacode(areaCode).getResult();
            FrameOu frameou = iouservice.getOuByOuGuid(ouguid);
            if (frameou == null) {
                return null;
            }
            ouname = frameou.getOuname();
            if (auditOrgaArea != null) {
                ouname = auditOrgaArea.getXiaquname() + ouname;
                if (ZwfwConstant.AREA_TYPE_XQJ.equals(auditOrgaArea.getCitylevel())) {
                    //查找市级辖区
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("citylevel", ZwfwConstant.AREA_TYPE_SJ);
                    AuditOrgaArea area = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                    if (area != null) {
                        ouname = area.getXiaquname() + ouname;
                    }
                }
            }
        }
        return ouname;
    }

    public void dealDataToJs(Record record, String taskid) {
        AuditSpBasetaskJS auditspbasetaskjs = iauditspbasetaskjs.getAuditSpBasetaskJSByTaskid(taskid).getResult();
        if (auditspbasetaskjs != null) {
            record.set("spsxmc", auditspbasetaskjs.getTaskname());
            record.set("spsxbm", auditspbasetaskjs.getTaskcode());
        }
    }

    public AuditSpBusiness getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBusiness();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpBusiness dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

}
