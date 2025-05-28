package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.xmz.thirdreporteddata.auditggconfig.api.IAuditGgConfigService;
import com.epoint.xmz.thirdreporteddata.basic.audittask.meterial.inter.GxhIAuditTaskMaterial;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController("spglbusinesschoosev3action")
@Scope("request")
public class SpglBusinessChooseV3Action extends BaseController
{

    private static final long serialVersionUID = 8796953926146877276L;

    transient Logger log = LogUtil.getLog(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglSbsjlcclListV3Action.class);

    /**
     * 流程信息实体对象
     */
    private Spglsplcxxb dataBean;

    @Autowired
    private ISpglsplcxxb iSpglDfxmsplcxxb;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditSpBusiness businseeImpl;

    @Autowired
    private ISpglspjdxxb ispgldfxmsplcjdxxb;

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
    private ISpglsplcjdsxxxb ispgldfxmsplcjdsxxxb;

    @Autowired
    private IAuditSpTask iauditsptask;

    @Autowired
    private IAttachService frameAttachInfoService;

    @Autowired
    private IAuditTaskResult iAuditTaskResult;

    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;

    @Autowired
    private GxhIAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private ISpglSpsxclmlxxbService iSpglSpsxclmlxxbService;

    @Autowired
    private ISpglSpsxkzxxbService iSpglSpsxkzxxbService;

    @Autowired
    private ISpglspsxjbxxb iSpglspsxjbxxb;

    @Autowired
    private IAuditSpBasetask iAuditSpBasetask;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IConfigService frameConfigService;
    @Autowired
    private IAuditGgConfigService iAuditGgConfigService;

    /**
     * 表格控件model
     */
    private DataGridModel<Spglsplcxxb> model;

    private List<SelectItem> sbztModel = null;

    @Override
    public void pageLoad() {
    }

    public DataGridModel<Spglsplcxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Spglsplcxxb>()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 5925727181973702200L;

                @Override
                public List<Spglsplcxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 页面上搜索条件
                    boolean flag = true;
                    if (StringUtil.isNotBlank(dataBean.getSplcmc())) {
                        sql.like("splcmc", dataBean.getSplcmc());
                        flag = false;
                    }
                    if (dataBean.getSjsczt() != null) {
                        sql.eq("sjsczt", dataBean.getSjsczt().toString());
                        flag = false;
                    }
                    // 有效数据
                    sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // 去除草稿
                    sql.nq("IFNULL(sync,0)", "-1");
                    // 版本号降序
                    sql.setOrderDesc("splcbbh");
                    // 操作时间降序
                    sql.setOrderDesc("operatedate");
                    PageData<Spglsplcxxb> pageData = iSpglDfxmsplcxxb.getAllByPage(sql.getMap(), first, pageSize,
                            sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<Spglsplcxxb> returnList = new ArrayList<Spglsplcxxb>();
                    List<Spglsplcxxb> lists = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(lists)) {
                        for (Spglsplcxxb lcxxb : lists) {
                            Double maxSplcbbh = iSpglDfxmsplcxxb.getMaxSplcbbh(lcxxb.get("splcbm"));
                            if (maxSplcbbh.toString().equals(lcxxb.getSplcbbh().toString())) {
                                lcxxb.put("id", lcxxb.getRowguid());
                                lcxxb.put("pid", TreeFunction9.F9ROOT);
                                lcxxb.put("checked", false);
                                lcxxb.put("expanded", false);
                                lcxxb.put("isLeaf", true);
                                lcxxb.put("maxSplcbbh", maxSplcbbh);
                                lcxxb.put("operatedateText", EpointDateUtil.convertDate2String(lcxxb.getOperatedate(),
                                        "yyyy-MM-dd HH:mm:ss"));
                                // 本地校验失败
                                if (lcxxb.getSjsczt() == -1) {
                                    lcxxb.put("sjscztText", "本地校验失败");
                                }
                                else {
                                    lcxxb.put("sjscztText", codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                            lcxxb.getSjsczt().toString()));
                                }
                                SqlConditionUtil sqlc = new SqlConditionUtil();
                                sqlc.eq("splcbm", lcxxb.getSplcbm());
                                sqlc.notin("splcbbh", maxSplcbbh.toString());
                                sqlc.eq("xzqhdm", lcxxb.getXzqhdm());
                                sqlc.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.nq("IFNULL(sync,0)", "-1");
                                sqlc.setOrderDesc("splcbbh");
                                Integer subCount = iSpglDfxmsplcxxb.getCountByCondition(sqlc.getMap()).getResult();
                                if (flag && subCount > 0) {
                                    lcxxb.put("isLeaf", false);
                                }
                                returnList.add(lcxxb);
                            }
                        }
                    }
                    return returnList;
                }

                @Override
                public List<Spglsplcxxb> fetchChildrenData(JSONObject t) {
                    List<Spglsplcxxb> lists = new ArrayList<Spglsplcxxb>();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    Double maxSplcbbh = t.getDouble("maxSplcbbh");
                    sql.eq("splcbm", t.getString("splcbm"));
                    sql.notin("splcbbh", maxSplcbbh.toString());
                    sql.eq("xzqhdm", t.getString("xzqhdm"));
                    sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    sql.nq("IFNULL(sync,0)", "-1");
                    sql.setOrderDesc("splcbbh");
                    lists = iSpglDfxmsplcxxb.getListByCondition(sql.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(lists)) {
                        for (Spglsplcxxb spglDfxmsplcxxb : lists) {
                            spglDfxmsplcxxb.put("id", spglDfxmsplcxxb.getRowguid());
                            spglDfxmsplcxxb.put("pid", t.get("pid"));
                            spglDfxmsplcxxb.put("checked", false);
                            spglDfxmsplcxxb.put("expanded", false);
                            spglDfxmsplcxxb.put("isLeaf", true);
                            // 本地校验失败
                            if (spglDfxmsplcxxb.getSjsczt() == -1) {
                                spglDfxmsplcxxb.put("sjscztText", "本地校验失败");
                            }
                            else {
                                spglDfxmsplcxxb.put("sjscztText",
                                        codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                                spglDfxmsplcxxb.getSjsczt().toString()));
                            }
                            spglDfxmsplcxxb.put("operatedateText",
                                    EpointDateUtil.convertDate2String(spglDfxmsplcxxb.getOperatedate(),
                                            "yyyy-MM-dd HH:mm:ss"));
                        }
                    }
                    return lists;
                }
            };
        }
        return model;
    }

    public void SyncBusiness(String rowguid) {
        // 事务控制
        String msg = "同步成功！";
        try {
            EpointFrameDsManager.begin(null);
            String OfficeWeb365_NET_URL = frameConfigService.getFrameConfigValue("OfficeWeb365_NET_URL");
            // 内网地址
            String zwfwUrl = frameConfigService.getFrameConfigValue("AS_ZWFW_ATTACH_URL");

            // 判断是否有同步记录，获取同步版本号
            Double bbh = iSpglDfxmsplcxxb.getMaxSplcbbh(rowguid);
            if (bbh == null) {
                bbh = 1d;
            }
            else {
                bbh += 1;
            }
            String xmqhdm = ZwfwUserSession.getInstance().getAreaCode();
            AuditSpBusiness business = businseeImpl.getAuditSpBusinessByRowguid(rowguid).getResult();
            Spglsplcxxb spglsplcxxb = new Spglsplcxxb();
            spglsplcxxb.setRowguid(UUID.randomUUID().toString());
            spglsplcxxb.setXzqhdm(xmqhdm);
            spglsplcxxb.setDfsjzj(business.getRowguid());
            spglsplcxxb.setSplcbm(business.getRowguid());
            spglsplcxxb.setSplcmc(business.getBusinessname());
            spglsplcxxb.setSplcbbh(bbh);
            spglsplcxxb.setSxrq(business.getDate("SXRQ"));
            spglsplcxxb.setTyrq(business.getDate("TYRQ"));
            spglsplcxxb.setSplcsm(business.getNote());
            spglsplcxxb.setDybzsplclx(business.getSplclx());
            spglsplcxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
            spglsplcxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            spglsplcxxb.setSyfw(business.getInt("ggsyfw"));
            spglsplcxxb.setSplctdz(business.getStr("Processimgurl"));
            spglsplcxxb.setOperatedate(new Date());
            iSpglDfxmsplcxxb.insert(spglsplcxxb);

            List<AuditSpPhase> phases = auditspphaseImpl.getSpPaseByBusinedssguid(business.getRowguid()).getResult();
            if (phases != null && !phases.isEmpty()) {
                for (AuditSpPhase phase : phases) {
                    Spglspjdxxb spglspjdxxb = new Spglspjdxxb();
                    spglspjdxxb.setRowguid(UUID.randomUUID().toString());
                    spglspjdxxb.setXzqhdm(xmqhdm);
                    spglspjdxxb.setSplcbm(business.getRowguid());
                    spglspjdxxb.setSplcbbh(bbh);
                    spglspjdxxb.setSpjdbm(phase.getRowguid());
                    spglspjdxxb.setDfsjzj(phase.getRowguid());
                    spglspjdxxb.setSpjdmc(phase.getPhasename());
                    String[] phaseids = phase.getPhaseId().split(",");
                    Integer phaseid = 0;
                    if (phaseids.length > 1) {
                        phaseid = Integer.valueOf(phaseids[0]);
                    }
                    else {
                        phaseid = Integer.valueOf(phase.getPhaseId());
                    }
                    spglspjdxxb.setSpjdxh(phaseid);
                    spglspjdxxb.setDybzspjdxh(phase.getPhaseId());

                    spglspjdxxb.setSpjdsx(phase.getSpjdsx());
                    String JDBSZNDZ = iAuditGgConfigService.getConfigValueByName("jdbszndz");
                    String JDBLSBDZ = iAuditGgConfigService.getConfigValueByName("jdblsbdz");
                    if (StringUtil.isNotBlank(JDBSZNDZ)) {
                        spglspjdxxb.setJdbszndz(JDBSZNDZ);
                    }
                    if (StringUtil.isNotBlank(JDBLSBDZ)) {
                        spglspjdxxb.setJdblsbdz(JDBLSBDZ);
                    }

                    spglspjdxxb.set("sjsczt", ZwfwConstant.CONSTANT_INT_ZERO);
                    spglspjdxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    ispgldfxmsplcjdxxb.insert(spglspjdxxb);

                    List<AuditSpTask> listsptask = iauditsptask.getAllAuditSpTaskByPhaseguid(phase.getRowguid())
                            .getResult();
                    for (AuditSpTask auditSpTask : listsptask) {
                        SqlConditionUtil sqlc = new SqlConditionUtil();
                        sqlc.eq("basetaskguid", auditSpTask.getBasetaskguid());
                        sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                        List<AuditSpBasetaskR> taskrlist = iauditspbasetaskr.getAuditSpBasetaskrByCondition(
                                sqlc.getMap()).getResult();
                        for (AuditSpBasetaskR auditSpBasetaskR : taskrlist) {
                            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid())
                                    .getResult();
                            if (audittask == null) {
                                continue;
                            }

                            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(
                                    audittask.getRowguid(), false).getResult();
                            AuditTaskResult auditTaskResult = iAuditTaskResult.getAuditResultByTaskGuid(
                                    audittask.getRowguid(), false).getResult();
                            List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial.getUsableMaterialListByTaskguid(
                                    audittask.getRowguid()).getResult();
                            Spglsplcjdsxxxb spglsplcjdsxxxb = new Spglsplcjdsxxxb();
                            spglsplcjdsxxxb.setRowguid(UUID.randomUUID().toString());
                            spglsplcjdsxxxb.setDfsjzj(auditSpBasetaskR.getBasetaskguid());
                            spglsplcjdsxxxb.setXzqhdm(xmqhdm);
                            spglsplcjdsxxxb.setSplcbm(business.getRowguid());
                            spglsplcjdsxxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                            spglsplcjdsxxxb.setSplcbbh(bbh);
                            spglsplcjdsxxxb.setSpjdxh(phaseid);
                            spglsplcjdsxxxb.setSpsxbm(audittask.getItem_id());
                            spglsplcjdsxxxb.set("sjsczt", ZwfwConstant.CONSTANT_INT_ZERO);
                            spglsplcjdsxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            spglsplcjdsxxxb.set("sync", 0);
                            ispgldfxmsplcjdsxxxb.insert(spglsplcjdsxxxb);

                            StringBuilder sxsbyyjbxx = new StringBuilder();// 基本信息失败原因
                            StringBuilder sxsbyykzxx = new StringBuilder();// 扩展信息失败原因
                            StringBuilder sxsbyyclml = new StringBuilder();// 材料目录失败原因
                            SpglSpsxjbxxb sqSpsxjbxxb = new SpglSpsxjbxxb();
                            sqSpsxjbxxb.setRowguid(UUID.randomUUID().toString());
                            sqSpsxjbxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            sqSpsxjbxxb.setDfsjzj(audittask.getRowguid());
                            sqSpsxjbxxb.setXzqhdm(audittask.getAreacode());
                            sqSpsxjbxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                            sqSpsxjbxxb.setSpsxbm(audittask.getItem_id());
                            sqSpsxjbxxb.setSpsxmc(audittask.getTaskname());
                            List<AuditSpBasetask> basetasks = iAuditSpBasetask.getAuditSpBasetasksBytaskid(
                                    audittask.getTask_id(),business.getBusinesstype());
                            if (ValidateUtil.isNotBlankCollection(basetasks)) {
                                AuditSpBasetask basetask = basetasks.get(0);
                                if (basetasks.size() > ZwfwConstant.CONSTANT_INT_ONE) {
                                    String taskcode = basetasks.stream().map(AuditSpBasetask::getTaskcode)
                                            .collect(Collectors.joining(","));
                                    basetask.setTaskcode(taskcode);
                                }
                                sqSpsxjbxxb.setDybzspsxbm(basetask.getTaskcode());
                            }
                            else {
                                sqSpsxjbxxb.setDybzspsxbm("9990000");
                            }
                            sqSpsxjbxxb.setSxbszndz(audittask.getStr("SXBSZNDZ"));
                            sqSpsxjbxxb.setSxzxsbdz(audittask.getStr("SXZXSBDZ"));
                            sqSpsxjbxxb.setJbbm(audittask.getCatalogcode());
                            sqSpsxjbxxb.setSsbm(audittask.getTaskcode());
                            sqSpsxjbxxb.setYwblxbm(audittask.getTaskcode());
                            sqSpsxjbxxb.setYwblxkzm(audittask.getStr("YWBLXKZM"));
                            sqSpsxjbxxb.setSxlx(audittask.getStr("SXLX"));
                            if (!isInCode("事项类型", audittask.getStr("SXLX"), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("事项类型的值不在代码项之中!");
                            }
                            sqSpsxjbxxb.setSdyj(audittask.getBy_law());
                            if (StringUtil.isNotBlank(audittask.get("QLLY"))) {
                                sqSpsxjbxxb.setQlly(Integer.parseInt(audittask.get("QLLY")));
                                if (!isInCode("权利来源", audittask.getStr("QLLY"), true)) {
                                    sqSpsxjbxxb.setSjsczt(-1);
                                    sxsbyyjbxx.append("权利来源的值不在代码项之中!");
                                }
                            }

                            if (StringUtil.isNotBlank(auditTaskExtension.getUse_level())) {
                                sqSpsxjbxxb.setXscj(Integer.parseInt(auditTaskExtension.getUse_level()));
                                if (!isInCode("行使层级", auditTaskExtension.getUse_level(), true)) {
                                    sqSpsxjbxxb.setSjsczt(-1);
                                    sxsbyyjbxx.append("行使层级的值不在代码项之中!");
                                }
                            }

                            sqSpsxjbxxb.setSxzt(0 == audittask.getIs_enable() ? 2 : audittask.getIs_enable()); // 0转2
                            sqSpsxjbxxb.setSszt(audittask.getOuname());
                            sqSpsxjbxxb.setSsztxz(auditTaskExtension.getSubjectnature());
                            if (StringUtil.isNotBlank(audittask.getDeptcode())) {
                                sqSpsxjbxxb.setSsztbm(audittask.getDeptcode());
                            }
                            else {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("实施主体编码验证失败!");
                            }
                            sqSpsxjbxxb.setSsbmssdqxzqhdm(audittask.getAreacode());
                            sqSpsxjbxxb.setWtbm(audittask.getEntrust_name());
                            sqSpsxjbxxb.setFdbjsx(audittask.getAnticipate_day());
                            sqSpsxjbxxb.setFdbjsxdw(audittask.getAnticipate_type());

                            if (!isInCode("时限单位", audittask.getAnticipate_type(), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("法定办结时限单位的值不在代码项之中!");
                            }

                            sqSpsxjbxxb.setFdbjsxsm(audittask.getAnticipate_day_remark());
                            sqSpsxjbxxb.setCnbjsx(audittask.getPromise_day());
                            sqSpsxjbxxb.setCnbjsxdw(audittask.getPromise_type());

                            if (!isInCode("时限单位", audittask.getPromise_type(), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("承诺办结时限单位的值不在代码项之中!");
                            }

                            sqSpsxjbxxb.setCnbjsxsm(audittask.getPromise_day_remark());
                            sqSpsxjbxxb.setSltj(audittask.getAcceptcondition());
                            if (StringUtil.isNotBlank(audittask.getStr("handle_flow"))) {
                                // 拼附件预览地址
                                List<FrameAttachInfo> attachInfo = attachService.getAttachInfoListByGuid(
                                        auditTaskExtension.getFinishproductsamples());
                                if (EpointCollectionUtils.isNotEmpty(attachInfo)) {
                                    String nwattachurl = zwfwUrl
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attachInfo.get(0).getAttachGuid();
                                    sqSpsxjbxxb.setBllc("办理流程说明:" + audittask.getStr("BLLC") + ".办理流程图:"
                                            + OfficeWeb365_NET_URL + "fname=" + attachInfo.get(0).getAttachFileName()
                                            + "&furl=" + nwattachurl);
                                }

                            }
                            else {
                                sqSpsxjbxxb.setBllc("办理流程说明:" + audittask.getStr("BLLC"));
                            }

                            sqSpsxjbxxb.setSfsf(audittask.getCharge_flag());// 质检吗
                            if (!isInCodeYesOrNo("是否", audittask.getCharge_flag(), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("是否收费的值不在代码项之中!");
                            }
                            sqSpsxjbxxb.setSfyj(audittask.getCharge_basis());
                            sqSpsxjbxxb.setFwdx(audittask.getStr("fwdx"));
                            if (!isInCode("服务对象", audittask.getStr("fwdx"), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("服务对象的值不在代码项之中!");
                            }
                            sqSpsxjbxxb.setBjlx(
                                    audittask.getType() == 3 ? 4 : audittask.getType() == 4 ? 6 : audittask.getType());
                            sqSpsxjbxxb.setBlxs(audittask.getHandletype());

                            if (!isInCode("办理形式", audittask.getHandletype(), true)) {
                                spglsplcxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("办理形式的值不在代码项之中!");
                            }

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
                            iSpglspsxjbxxb.insert(sqSpsxjbxxb);

                            // 事项扩展信息
                            SpglSpsxkzxxb spglSpsxkzxxb = new SpglSpsxkzxxb();
                            spglSpsxkzxxb.setRowguid(UUID.randomUUID().toString());
                            spglSpsxkzxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            spglSpsxkzxxb.setDfsjzj(auditTaskExtension.getRowguid());
                            spglSpsxkzxxb.setXzqhdm(audittask.getAreacode());
                            spglSpsxkzxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                            spglSpsxkzxxb.setSpsxbm(audittask.getItem_id());
                            spglSpsxkzxxb.setSfjzzhck(auditTaskExtension.get("SFJZZHCK"));
                            if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFJZZHCK"), true)) {
                                spglSpsxkzxxb.setSjsczt(-1);
                                sxsbyykzxx.append("是否进驻建设窗口的值不在代码项之中!");
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("FWQD"))) {
                                spglSpsxkzxxb.setFwqd(auditTaskExtension.getStr("FWQD"));
                                if (!isInCode("服务渠道", auditTaskExtension.getStr("FWQD"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("服务渠道的值不在代码项之中!");
                                }
                            }

                            if (StringUtil.isNotBlank(audittask.getHandlearea())) {
                                spglSpsxkzxxb.setTbfw(Integer.parseInt(audittask.getHandlearea()));
                                if (!isInCode("通办范围", audittask.getHandlearea(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("通办范围的值不在代码项之中!");
                                }
                            }

                            spglSpsxkzxxb.setLbjg(audittask.getOther_togetdept());
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("XZXKSXLX"))) {
                                spglSpsxkzxxb.setXzxksxlx(Integer.parseInt(auditTaskExtension.getStr("XZXKSXLX")));
                                if (!isInCode("行政许可类型", auditTaskExtension.getStr("XZXKSXLX"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("行政许可类型的值不在代码项之中!");
                                }
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
                                spglSpsxkzxxb.setSpjglx(("40").equals(auditTaskResult.getResulttype().toString())
                                        ? "30"
                                        : auditTaskResult.getResulttype().toString());
                                if (!isInCodeYesOrNo("审批结果类型", auditTaskResult.getResulttype(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("审批结果类型的值不在代码项之中!");
                                }
                            }
                            spglSpsxkzxxb.setSpjgmc(auditTaskExtension.getFinishfilename());
                            List<FrameAttachInfo> attachInfo = attachService.getAttachInfoListByGuid(
                                    auditTaskExtension.getFinishproductsamples());
                            if (EpointCollectionUtils.isNotEmpty(attachInfo)) {
                                String nwattachurl = zwfwUrl
                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + attachInfo.get(0).getAttachGuid();
                                spglSpsxkzxxb.setSpjgyb(
                                        OfficeWeb365_NET_URL + "fname=" + attachInfo.get(0).getAttachFileName()
                                                + "&furl=" + nwattachurl);
                            }

                            if (StringUtil.isNotBlank(auditTaskExtension.getReservationmanagement())) {
                                spglSpsxkzxxb.setSfzcyybl(
                                        Integer.parseInt(auditTaskExtension.getReservationmanagement()));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.getReservationmanagement(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否支持预约办理的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.getOnlinepayment())) {
                                spglSpsxkzxxb.setSfzcwszf(Integer.parseInt(auditTaskExtension.getOnlinepayment()));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.getOnlinepayment(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否支持网上支付的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.getIf_express())) {
                                spglSpsxkzxxb.setSfzcwlkd(Integer.parseInt(auditTaskExtension.getIf_express()));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.getIf_express(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否支持物流快递的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFZCZZZDBL"))) {
                                spglSpsxkzxxb.setSfzczzzdbl(auditTaskExtension.get("SFZCZZZDBL"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFZCZZZDBL"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否支持自主终端办理的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFWB"))) {
                                spglSpsxkzxxb.setSfwb(auditTaskExtension.get("SFWB"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.getStr("SFWB"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否网办的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("WSBLSD"))) {
                                spglSpsxkzxxb.setWsblsd(auditTaskExtension.get("WSBLSD"));
                                if (!isInCode("网上办理深度", auditTaskExtension.get("WSBLSD"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("网上办理深度的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFSXGZCNBL"))) {
                                spglSpsxkzxxb.setSfsxgzcnbl(auditTaskExtension.get("SFSXGZCNBL"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFSXGZCNBL"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否实行告知承诺办理的值不在代码项之中!");
                                }
                            }
                            spglSpsxkzxxb.setBxxcblyysm(auditTaskExtension.get("BXXCBLYYSM"));
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYXCKY"))) {
                                spglSpsxkzxxb.setSfxyxcky(auditTaskExtension.get("SFXYXCKY"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYXCKY"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要现场勘验的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYZZTZ"))) {
                                spglSpsxkzxxb.setSfxyzztz(auditTaskExtension.get("SFXYZZTZ"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYZZTZ"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要组织听证的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYZZTZ"))) {
                                spglSpsxkzxxb.setSfxyzbpmgpjy(auditTaskExtension.get("SFXYZZTZ"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYZBPMGPJY"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要招标、拍卖、挂牌交易的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYJYJCJY"))) {
                                spglSpsxkzxxb.setSfxyjyjcjy(auditTaskExtension.get("SFXYJYJCJY"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYJYJCJY"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要检验、检测、检疫的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYJD"))) {
                                spglSpsxkzxxb.setSfxyjd(auditTaskExtension.get("SFXYJD"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYJD"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要鉴定的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYZJPS"))) {
                                spglSpsxkzxxb.setSfxyzjps(auditTaskExtension.get("SFXYZJPS"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYZJPS"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要专家评审的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYXSHGS"))) {
                                spglSpsxkzxxb.setSfxyxshgs(auditTaskExtension.get("SFXYXSHGS"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYXSHGS"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要向社会公示的值不在代码项之中!");
                                }
                            }

                            spglSpsxkzxxb.setXzxkzjmc(auditTaskExtension.getStr("XZXKZJMC"));
                            spglSpsxkzxxb.setXzxkzjdyxqx(auditTaskExtension.getStr("XZXKZJDYXQX"));
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("XZXKZJDYXQXDW"))) {

                                if (!isInCode("有效期限单位", auditTaskExtension.getStr("XZXKZJDYXQXDW"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("有效期限单位的值不在代码项之中!");
                                }
                            }

                            spglSpsxkzxxb.setGdxzxkzjyxqxdyj(auditTaskExtension.getStr("GDXZXKZJYXQXDYJ"));
                            spglSpsxkzxxb.setBlxzxkzjyxsxdyq(auditTaskExtension.getStr("BLXZXKZJYXSXDYQ"));
                            spglSpsxkzxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            spglSpsxkzxxb.setSbyy(sxsbyykzxx.toString());
                            spglSpsxkzxxb.setSjwxyy(sxsbyykzxx.toString());
                            spglSpsxkzxxb.set("sync", "0");
                            iSpglSpsxkzxxbService.insert(spglSpsxkzxxb);
                            if (EpointCollectionUtils.isNotEmpty(auditTaskMaterials)) {
                                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                                    // 事项材料目录信息
                                    SpglSpsxclmlxxb spglSpsxclmlxxb = new SpglSpsxclmlxxb();
                                    spglSpsxclmlxxb.setRowguid(UUID.randomUUID().toString());
                                    spglSpsxclmlxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                    spglSpsxclmlxxb.setDfsjzj(auditTaskMaterial.getRowguid());
                                    spglSpsxclmlxxb.setXzqhdm(audittask.getAreacode());
                                    spglSpsxclmlxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                                    spglSpsxclmlxxb.setSpsxbm(audittask.getItem_id());
                                    spglSpsxclmlxxb.setPx(auditTaskMaterial.getOrdernum());
                                    spglSpsxclmlxxb.setClmlbh(StringUtil.isNotBlank(auditTaskMaterial.get("CLMLBH"))
                                            ? auditTaskMaterial.get("CLMLBH")
                                            : auditTaskMaterial.getMaterialid());
                                    spglSpsxclmlxxb.setClmc(auditTaskMaterial.getMaterialname());
                                    if (StringUtil.isNotBlank(auditTaskMaterial.get("CLXS"))) {
                                        spglSpsxclmlxxb.setCllx(Integer.parseInt(auditTaskMaterial.getStr("CLXS")));
                                    }
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getSubmittype())) {
                                        spglSpsxclmlxxb.setClxs(
                                                Integer.parseInt(auditTaskMaterial.getSubmittype()) == 10
                                                        ? 2
                                                        : Integer.parseInt(auditTaskMaterial.getSubmittype()) == 20
                                                                ? 1
                                                                : 3);
                                    }

                                    spglSpsxclmlxxb.setClbyx((auditTaskMaterial.getIs_rongque() == 1
                                            ? 3
                                            : (auditTaskMaterial.getNecessity() == 10
                                                    ? 1
                                                    : (auditTaskMaterial.getNecessity() == 20 ? 2 : 0))));
                                    List<FrameAttachInfo> attachInfos = attachService.getAttachInfoListByGuid(
                                            auditTaskMaterial.getTemplateattachguid());
                                    if (EpointCollectionUtils.isNotEmpty(attachInfos)) {
                                        String nwattachurl = zwfwUrl
                                                + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + attachInfos.get(0).getAttachGuid();
                                        spglSpsxclmlxxb.setKbbg(
                                                OfficeWeb365_NET_URL + "fname=" + attachInfos.get(0).getAttachFileName()
                                                        + "&furl=" + nwattachurl);

                                    }
                                    List<FrameAttachInfo> attachInfo1s = attachService.getAttachInfoListByGuid(
                                            auditTaskMaterial.getExampleattachguid());
                                    if (EpointCollectionUtils.isNotEmpty(attachInfo1s)) {
                                        String nwattachurl1 = zwfwUrl
                                                + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + attachInfo1s.get(0).getAttachGuid();
                                        spglSpsxclmlxxb.setSlyb(OfficeWeb365_NET_URL + "fname=" + attachInfo1s.get(0)
                                                .getAttachFileName() + "&furl=" + nwattachurl1);
                                    }
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getFile_source())) {
                                        spglSpsxclmlxxb.setLyqd(Integer.parseInt(auditTaskMaterial.getFile_source()));
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
                                    spglSpsxclmlxxb.setSync(0);
                                    iSpglSpsxclmlxxbService.insert(spglSpsxclmlxxb);
                                }
                            }
                        }
                    }
                }
            }
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            msg = "同步失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("msg", msg);
        }

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
        if (a) {
            if (value == null) {
                throw new RuntimeException(codename + "的值不能为空！");
            }
        }
        String v = value.toString();
        codename = "国标_" + codename;
        ICodeItemsService icodeitemsservice = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        return StringUtil.isNotBlank(s);
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

    public Spglsplcxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new Spglsplcxxb();
        }
        return dataBean;
    }

    public void setDataBean(Spglsplcxxb dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSbztModel() {
        if (sbztModel == null) {
            sbztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
            sbztModel.add(new SelectItem("-1", "本地校验失败"));
        }
        return this.sbztModel;
    }
}
