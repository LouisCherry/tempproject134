package com.epoint.auditsp.auditsphandle.action;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditresource.auditdggtview.domain.AuditDggtView;
import com.epoint.basic.auditresource.auditdggtview.inter.IAuditDggtView;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspireview.inter.IAuditSpIReview;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.basic.auditsp.auditspspgcjsxk.inter.IAuditSpSpGcjsxkService;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.auditspsplxydghxk.domain.AuditSpSpLxydghxk;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handlespbusiness.inter.IHandleSpBusiness;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目基本信息表新增页面对应的后台
 *
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("jnhandleitemregisteraction")
@Scope("request")
public class JNHandleItemRegisterAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1313301881620639339L;
    /**
     * 项目库API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    @Autowired
    private IHandleSpBusiness handleSpBusinessService;
    @Autowired
    private IAuditSpPhase auditSpPhaseService;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IOuService ouService;
    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IAuditSpISubapp auditSpISubappService;
    @Autowired
    private IAuditSpIReview auditSpIReviewService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IAuditSpTask iauditsptask;
    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private IAuditTask iaudittask;
    @Autowired
    private IConfigService configservice;
    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;
    @Autowired
    private IAuditSpBasetask iauditspbasetask;
    @Autowired
    private IAuditDggtView iauditDggtView;
    @Autowired
    private ICertAttachExternal certAttachService;
    @Autowired
    private IParticipantsInfoService iparticipantsInfoService;

    @Autowired
    private IAuditSpBusiness iauditspbusiness;

    @Autowired
    private IAuditSpSpLxydghxkService iauditspsplxydghxkservice;

    @Autowired
    private IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService;

    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;

    @Autowired
    private IAuditSpSpJgysService iauditspspjgysservice;
    /**
     * 区域配置接口的实现类
     */
    @Autowired
    private IAuditOrgaArea iauditorgaarea;
    @Autowired
    private ICodeItemsService icodeitemsservice;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;
    /**
     * 工程建设许可信息表实体对象
     */
    private AuditSpSpGcjsxk phase2 = null;
    /**
     * 施工许可信息表实体对象
     */
    private AuditSpSpSgxk phase3 = null;
    /**
     * 项目类型下拉列表model
     */
    private List<SelectItem> itemTypeModel = null;
    /**
     * 建设性质下拉列表model
     */
    private List<SelectItem> constructionPropertyModel = null;
    /**
     * 证照类型下拉列表model
     */
    private List<SelectItem> itemLegalCertTypeModel = null;
    /**
     * 所属行业下拉列表model
     */
    private List<SelectItem> belongtIndustryModel = null;
    /**
     * 法人性质下拉列表model
     */
    private List<SelectItem> legalPropertyModel = null;
    /**
     * 资金来源下拉列表model
     */
    private List<SelectItem> fundSourcesModel = null;
    /**
     * 财政资金来源下拉列表model
     */
    private List<SelectItem> financialResourcesModel = null;
    /**
     * 量化建设规模类别下拉列表model
     */
    private List<SelectItem> quantifyConstructTypeModel = null;
    /**
     * 土地获取方式
     */
    private List<SelectItem> tdhqfsModel = null;
    /**
     * 土地获取方式
     */
    private List<SelectItem> xmzjlymodel = null;
    /**
     * 是否技改项目单选按钮组model
     */
    private List<SelectItem> isImprovementModel = null;
    /**
     * 项目来源
     * 单选按钮组model
     */
    private List<SelectItem> itemSourcemodel = null;

    /**
     * 事项选择树modle
     */
    private DataGridModel<AuditSpTask> gridmodel = null;

    private DataGridModel<AuditDggtView> viewgridmodel = null;

    private List<SelectItem> OuListModel = null;

    private DataGridModel<Record> taskListModel = null;

    private List<SelectItem> xmlxmodel = null;

    private List<SelectItem> projectLevelmodel = null;

    private List<SelectItem> proTypedmodel = null;

    private List<SelectItem> investTypedmodel = null;

    private List<SelectItem> nhdjmodel = null;

    private List<SelectItem> fldjmodel = null;

    private List<SelectItem> sylxmodel = null;

    private List<SelectItem> xmzjsxmodel = null;

    private LazyTreeModal9 gbhymodel = null;

    //重点项目
    private List<SelectItem> zdxmlxmodel = null;

    private String phaseGuid = "";
    private String businessGuid = "";
    private String itemGuid = "";
    private String subappGuid = "";

    public AuditSpSpLxydghxk phase1 = null;

    public AuditSpSpJgys phase4 = null;

    public String zitemname;
    public String zitemcode;
    public String isadditem;

    public String citylevel;

    public List<String> sjcode;

    @Override
    public void pageLoad() {
        //设置辖区
        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
        if (area != null) {
            citylevel = area.getCitylevel();
        }

        //查询所有市级的areacode，和当前辖区
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
        List<AuditOrgaArea> listarea = iauditorgaarea.selectAuditAreaList(sqlc.getMap()).getResult();
        if (listarea != null && listarea.size() > 0) {
            sjcode = listarea.stream().map(AuditOrgaArea::getXiaqucode).collect(Collectors.toList());
            sjcode.add(ZwfwUserSession.getInstance().getAreaCode());
        } else {
            sjcode = new ArrayList<>();
        }
        businessGuid = getRequestParameter("businessGuid");
        itemGuid = getRequestParameter("itemGuid");
        subappGuid = getRequestParameter("subappGuid");

        //获取主题，设置默认值
        AuditSpBusiness bussiness = iauditspbusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();

        dataBean = new AuditRsItemBaseinfo();
        dataBean.setIsimprovement(ZwfwConstant.CONSTANT_STR_ZERO);
        dataBean.setTdsfdsjfa(ZwfwConstant.CONSTANT_INT_ZERO);
        dataBean.setSfwcqypg(ZwfwConstant.CONSTANT_INT_ZERO);
        if (bussiness != null) {
            dataBean.setItemtype(String.valueOf(bussiness.getSplclx()));
        }
        // dataBean.setJzmj((double) ZwfwConstant.CONSTANT_INT_ZERO);

        //判断是否是拉下来的项目，重新生成suappguid，并且拆分建设单位
        if (subappGuid.equals("newcreat")) {
            subappGuid = UUID.randomUUID().toString();
            boolean isExist = iparticipantsInfoService.isExistdwByItemCode(itemGuid, "31");
            if (!isExist) {
                addCallbackParam("isExist", isExist);
                insertJsdw();
            }
        }
        if (StringUtil.isNotBlank(itemGuid) || StringUtil.isNotBlank(getViewData("itemGuid"))) {
            dataBean = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
            if (dataBean != null && ZwfwConstant.CONSTANT_STR_ONE.equals(dataBean.getDraft())) {
                if (StringUtil.isNotBlank(dataBean.getParentid())) {
                    AuditRsItemBaseinfo item = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(dataBean.getParentid()).getResult();
                    if (item != null) {
                        addCallbackParam("xmbm", item.getItemcode());
                    }
                }
            }
            if (dataBean != null) {
                addViewData("itemGuid", itemGuid);
                addViewData("biguid", dataBean.getBiguid());
                //设置申报名称
                dataBean.set("subappname", dataBean.getItemname());
                if (StringUtil.isNotBlank(dataBean.getGbhy())) {
                    String gbhyText = icodeitemsservice.getItemTextByCodeName("国标行业2017", dataBean.getGbhy());
                    addCallbackParam("gbhy", gbhyText);
                }
            }
        }

        if (StringUtil.isNotBlank(subappGuid)) {
            //查询实体
            AuditSpISubapp sub = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
            phase1 = iauditspsplxydghxkservice.findAuditSpSpLxydghxkBysubappguid(subappGuid);
            phase2 = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappGuid);
            phase3 = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappGuid);
            phase4 = iauditspspjgysservice.findAuditSpSpJgysBySubappGuid(subappGuid);

            if (sub != null && dataBean != null) {
                dataBean.set("subappname", sub.getSubappname());
            }
            addViewData("subappGuid", subappGuid);
        }

        // 获取首个阶段，如果没有配置，就默认使用当前配置的第一个阶段
        phaseGuid = getRequestParameter("phaseGuid");
        if (StringUtil.isBlank(getViewData("itemGuid"))) {
            itemGuid = UUID.randomUUID().toString();
            addViewData("itemGuid", itemGuid);
        } else {
            itemGuid = getViewData("itemGuid");
        }
        if (StringUtil.isBlank(getViewData("subappGuid"))) {
            subappGuid = UUID.randomUUID().toString();
            addViewData("subappGuid", subappGuid);
        } else {
            subappGuid = getViewData("subappGuid");
        }
        if (StringUtil.isBlank(phaseGuid)) {
            List<AuditSpPhase> listPhase = auditSpPhaseService.getSpPaseByBusinedssguid(businessGuid).getResult();
            listPhase.sort((a, b) -> (b.getOrdernumber() != null ? b.getOrdernumber() : Integer.valueOf(0))
                    .compareTo(a.getOrdernumber() != null ? a.getOrdernumber() : Integer.valueOf(0)));
            if (listPhase.size() > 0) {
                /*
                 * for (AuditSpPhase phase : listPhase) { if
                 * (ZwfwConstant.CONSTANT_STR_ONE.equals(phase.getFirstphase()))
                 * phaseGuid = phase.getRowguid(); }
                 */
                if (StringUtil.isBlank(phaseGuid)) {
                    phaseGuid = listPhase.get(0).getRowguid();
                }
            }
        }
        // 查询该阶段是否包含牵头部门
        // 通过阶段主键获取实体类
        AuditSpPhase auditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseGuid).getResult();
        // 存放牵头部门的guid
        String leadOuGuid = "";
        if (auditSpPhase != null) {
            leadOuGuid = auditSpPhase.getLeadouguid();
            if (StringUtil.isNotBlank(auditSpPhase.getEformurl())) {
                this.addCallbackParam("eformurl", auditSpPhase.getEformurl());
            }
            addCallbackParam("subappGuid", subappGuid);
            addCallbackParam("phaseid", auditSpPhase.getPhaseId());
            //12阶段显示建设单位信息，34不显示
            if (StringUtil.isNotBlank(auditSpPhase.getPhaseId()) && (auditSpPhase.getPhaseId().equals("1") || auditSpPhase.getPhaseId().equals("2"))) {
                addCallbackParam("jsdwshow", true);
            } else {
                addCallbackParam("jsdwshow", false);
            }
            addCallbackParam("addOrEdit", true);
            addCallbackParam("itemGuid", itemGuid);
            if (StringUtil.isNotBlank(auditSpPhase.getPhasename())) {
                addCallbackParam("phasename", auditSpPhase.getPhasename());
            }
        }
        if (StringUtil.isNotBlank(leadOuGuid)) {
            addCallbackParam("isOwnQt", true);
        } else {
            addCallbackParam("isOwnQt", false);
        }
        //判断是否可选择事项
        String choosetask = configservice.getFrameConfigValue("AS_BLSP_CHOOSETASK");
        if (StringUtil.isNotBlank(choosetask)) {
            addCallbackParam("choosetask", choosetask);
        } else {
            addCallbackParam("choosetask", "0");
        }

        if (phase1 == null) {
            phase1 = new AuditSpSpLxydghxk();
        }
        if (phase2 == null) {
            phase2 = new AuditSpSpGcjsxk();
        }
        if (phase3 == null) {
            phase3 = new AuditSpSpSgxk();
        }
        if (phase4 == null) {
            phase4 = new AuditSpSpJgys();
        }
    }

    /**
     * 保存并关闭
     */
    public void add() {
        String msg = "发起征求成功！";
        boolean flag = true;
        if (StringUtil.isNotBlank(dataBean.getItemcode()) && StringUtil.isBlank(getViewData("itemcode"))) {
            addViewData("itemcode", dataBean.getItemcode());
        }
        if (StringUtil.isNotBlank(dataBean.getItemcode()) && !dataBean.getItemcode().equals(getViewData("itemcode"))) {
            if (iAuditRsItemBaseinfo.checkItemCodeRepeat(dataBean).getResult() > 0) {
                msg = "项目代码重复！";
                flag = false;
            }
        }
        //如果已经存在项目且项目中没哟itemcode,则需要限制
        AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
        if (baseinfo != null) {
            if (StringUtil.isBlank(baseinfo.getItemcode())) {
                if (iAuditRsItemBaseinfo.checkItemCodeRepeat(dataBean).getResult() > 0) {
                    msg = "项目代码重复！";
                    addCallbackParam("msg", msg);
                    return;
                }
            }
        }

        if (flag) {
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isadditem)) {
                //验证子项目编码的唯一性
                AuditRsItemBaseinfo zidatabean = new AuditRsItemBaseinfo();
                zidatabean.setParentid(dataBean.getRowguid());
                zidatabean.setRowguid(UUID.randomUUID().toString());
                ;
                zidatabean.setItemname(zitemname);
                zidatabean.setItemcode(zitemcode);
                if (iAuditRsItemBaseinfo.checkItemCodeRepeat(zidatabean).getResult() > 0) {
                    msg = "子项目代码重复！";
                    addCallbackParam("msg", msg);
                    return;
                }
            }
            // 保存项目信息
            dataBean.setRowguid(itemGuid);
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            if (StringUtil.isBlank(getViewData("biguid"))) {
                // 生成主题实例信息
                String biGuid = auditSpInstanceService
                        .addBusinessInstance(businessGuid, itemGuid, "", dataBean.getItemlegaldept(), "",
                                dataBean.getItemname(), ZwfwUserSession.getInstance().getAreaCode(), "1")
                        .getResult();
                addViewData("biguid", biGuid);
                dataBean.setBiguid(biGuid);
            } else {
                dataBean.setBiguid(getViewData("biguid"));
                //点击申报，之前保存过项目，修改instance状态
                auditSpInstanceService.updateBIStatus(getViewData("biguid"), null);
            }
            if (baseinfo != null) {
                //如果是更新设置不为草稿
                dataBean.setDraft(ZwfwConstant.CONSTANT_STR_ZERO);
                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(dataBean);
            } else {
                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(dataBean);
            }
            //声明变量记录主项标识
            String prowguid = dataBean.getRowguid();
            String issendzj = dataBean.getIssendzj();
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isadditem)) {
                //插入子项目
                dataBean.setParentid(dataBean.getRowguid());
                dataBean.setRowguid(UUID.randomUUID().toString());
                ;
                dataBean.setItemname(zitemname);
                dataBean.setItemcode(zitemcode);
                dataBean.setIssendzj("");
                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(dataBean);
            }
            AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
            auditSpISubapp.setApplyerguid(UserSession.getInstance().getUserGuid());
            auditSpISubapp.setApplyername(UserSession.getInstance().getDisplayName());
            auditSpISubapp.setBiguid(dataBean.getBiguid());
            auditSpISubapp.setBusinessguid(businessGuid);
            auditSpISubapp.setCreatedate(new Date());
            auditSpISubapp.setRowguid(subappGuid);
            auditSpISubapp.setPhaseguid(phaseGuid);
            auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_DPS);
            auditSpISubapp.setYewuguid(dataBean.getRowguid());
            auditSpISubapp.setSubappname(dataBean.getStr("subappname"));
            AuditSpISubapp sub = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
            if (sub != null) {
                auditSpISubappService.updateAuditSpISubapp(auditSpISubapp);
            } else {
                auditSpISubappService.addSubapp(auditSpISubapp);
            }
            savePhase(subappGuid);
            // 通过阶段主键获取实体类
            AuditSpPhase auditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseGuid).getResult();
            // 存放牵头部门的guid
            String leadOuGuid = null;
            if (auditSpPhase != null) {
                leadOuGuid = auditSpPhase.getLeadouguid();
            }
            if (StringUtil.isNotBlank(leadOuGuid)) {
                // 给牵头部门插入一条记录
                String reviewGuid = auditSpIReviewService
                        .addReview(dataBean.getBiguid(), phaseGuid, leadOuGuid, subappGuid, 1).getResult();
                // 给牵头部门发送通知提醒
                String roleGuid = roleService.listRole("窗口负责人", "").get(0).getRoleGuid();
                List<FrameUser> listUser = userService.listUserByOuGuid(leadOuGuid, roleGuid, "", "", false, true, true,
                        3);
                String targetUserGuid = userSession.getUserGuid();
                String url = "epointzwfw/auditsp/auditsphandle/handleitemregisterforleadou?reviewGuid=" + reviewGuid
                        + "&leadOuGuid=" + leadOuGuid + "&targetuserguid=" + targetUserGuid;
                if (listUser != null && listUser.size() > 0) {
                    for (FrameUser user : listUser) {
                        messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(),
                                "【事项征求】" + auditSpISubapp.getSubappname(), IMessagesCenterService.MESSAGETYPE_WAIT,
                                user.getUserGuid(), user.getDisplayName(), userSession.getUserGuid(),
                                userSession.getDisplayName(), "事项征求", url, leadOuGuid, "", 1, "", "", reviewGuid,
                                reviewGuid.substring(0, 1), new Date(), "", userSession.getUserGuid(), "", "");
                    }
                }
            } else {
                // 发起事项征求，没有牵头部门的话，走正常的通知所有窗口部门的流程
                String[] ous = getOus().split(",");
                //数组去重
                Map<String, Object> map = new HashMap<String, Object>();
                for (String str : ous) {
                    map.put(str, str);
                }
                //返回一个包含所有对象的指定类型的数组
                ous = map.keySet().toArray(new String[1]);
                String[] lists = getTasks().split(",");
                if (ous != null && ous.length > 0 && StringUtil.isNotBlank(ous[0])) {
                    for (String ou : ous) {
                        String reviewGuid = auditSpIReviewService
                                .addReview(dataBean.getBiguid(), phaseGuid, ou, subappGuid).getResult();
                        // 给窗口负责人发送通知提醒
                        String roleGuid = roleService.listRole("窗口负责人", "").get(0).getRoleGuid();
                        List<FrameUser> listUser = userService.listUserByOuGuid(ou, roleGuid, "", "", false, true, true,
                                3);
                        String targetUserGuid = userSession.getUserGuid();
                        String url = "epointzwfw/auditsp/auditsphandle/handlereview?reviewGuid=" + reviewGuid
                                + "&targetuserguid=" + targetUserGuid;
                        for (FrameUser user : listUser) {
                            messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(),
                                    "【事项征求】" + auditSpISubapp.getSubappname(), IMessagesCenterService.MESSAGETYPE_WAIT,
                                    user.getUserGuid(), user.getDisplayName(), userSession.getUserGuid(),
                                    userSession.getDisplayName(), "事项征求", url, ou, "", 1, "", "", reviewGuid,
                                    reviewGuid.substring(0, 1), new Date(), "", userSession.getUserGuid(), "", "");
                        }
                    }
                } else if (lists != null && lists.length > 0) {
                    //修改subapp的状态为已评审
                    auditSpISubappService.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YPS, null);
                    // 插入事项实例
                    for (String taskid : lists) {
                        AuditTask auditTask = iaudittask.selectUsableTaskByTaskID(taskid).getResult();
                        AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(taskid).getResult();
                        auditSpITaskService.addTaskInstance(businessGuid, dataBean.getBiguid(), phaseGuid,
                                auditTask.getRowguid(), auditTask.getTaskname(), subappGuid,
                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(), null, basetask.getSflcbsx());
                    }
                    addCallbackParam("url",
                            "epointzwfw/auditsp/auditsphandle/handlesubmitmaterial?subappGuid=" + subappGuid);
                } else {
                    msg = "发起征求失败！";
                }
            }

            //如果是新增项目，则需要把项目推送到住建系统
            String mqmsg;
            if (!dataBean.getRowguid().equals(prowguid) && !ZwfwConstant.CONSTANT_STR_ONE.equals(issendzj)) {
                //如果是新增项目，则需要把项目推送到住建系统
                mqmsg = prowguid + "." + ZwfwUserSession.getInstance().getAreaCode() + "."
                        + getViewData("subappGuid");
                sendMQMessageService.sendByExchange("exchange_handle", mqmsg, "blsp.rsitem." + businessGuid);
                sendMQMessageService.sendByExchange("exchange_handle", mqmsg, "blspV3.rsitem.1");
            }

            mqmsg = dataBean.getRowguid() + "." + ZwfwUserSession.getInstance().getAreaCode() + "." + subappGuid;
            sendMQMessageService.sendByExchange("exchange_handle", mqmsg, "blsp.rsitem." + businessGuid);
            sendMQMessageService.sendByExchange("exchange_handle", mqmsg, "blspV3.rsitem.1");
        }
        dataBean = null;
        addCallbackParam("msg", msg);
    }

    /**
     * 保存并关闭
     */
    public void save() {
        boolean flag = true;
        String msg = "保存成功！";
        if (StringUtil.isNotBlank(dataBean.getItemcode()) && StringUtil.isBlank(getViewData("itemcode"))) {
            if (iAuditRsItemBaseinfo.checkItemCodeRepeat(dataBean).getResult() > 0) {
                msg = "项目代码重复！";
                flag = false;
            } else {
                addViewData("itemcode", dataBean.getItemcode());
            }
        }
        if (StringUtil.isNotBlank(dataBean.getItemcode()) && !dataBean.getItemcode().equals(getViewData("itemcode"))) {
            if (iAuditRsItemBaseinfo.checkItemCodeRepeat(dataBean).getResult() > 0) {
                msg = "项目代码重复！";
                flag = false;
            }
        }
        if (flag) {
            // 保存项目信息
            dataBean.setRowguid(itemGuid);
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());

            if (StringUtil.isBlank(getViewData("biguid"))) {
                // 生成主题实例信息
                String biGuid = auditSpInstanceService
                        .addBusinessInstance(businessGuid, itemGuid, "", dataBean.getItemlegaldept(), "",
                                dataBean.getItemname(), ZwfwUserSession.getInstance().getAreaCode(), "1")
                        .getResult();
                //点击保存生成的为草稿状态
                auditSpInstanceService.updateBIStatus(biGuid, "-1");

                addViewData("biguid", biGuid);
                dataBean.setBiguid(biGuid);
            } else {
                dataBean.setBiguid(getViewData("biguid"));
            }
            AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
            if (baseinfo != null) {
                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(dataBean);
            } else {
                //如果新增项目信息设置为草稿
                dataBean.setDraft(ZwfwConstant.CONSTANT_STR_ONE);
                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(dataBean);
            }

            AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
            auditSpISubapp.setApplyerguid(UserSession.getInstance().getUserGuid());
            auditSpISubapp.setApplyername(UserSession.getInstance().getDisplayName());
            auditSpISubapp.setBiguid(dataBean.getBiguid());
            auditSpISubapp.setBusinessguid(businessGuid);
            auditSpISubapp.setCreatedate(new Date());
            auditSpISubapp.setRowguid(subappGuid);
            auditSpISubapp.setPhaseguid(phaseGuid);
            auditSpISubapp.setStatus("-1");
            auditSpISubapp.setYewuguid(dataBean.getRowguid());
            auditSpISubapp.setSubappname(dataBean.get("subappname"));
            AuditSpISubapp sub = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
            if (sub != null) {
                auditSpISubappService.updateAuditSpISubapp(auditSpISubapp);
            } else {
                auditSpISubappService.addSubapp(auditSpISubapp);
            }
            savePhase(subappGuid);
        }
        addCallbackParam("msg", msg);
    }

    public DataGridModel<AuditDggtView> getViewDataGridData() {
        if (viewgridmodel == null) {
            viewgridmodel = new DataGridModel<AuditDggtView>() {

                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditDggtView> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("ywguid", itemGuid);
                    PageData<AuditDggtView> pagadata = iauditDggtView.selectAuditDggtViewByPage(sqlc.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    if (pagadata != null) {
                        this.setRowCount(pagadata.getRowCount());
                        return pagadata.getList();
                    } else {
                        this.setRowCount(0);
                        return new ArrayList<AuditDggtView>();
                    }
                }
            };
        }
        return viewgridmodel;
    }

    public DataGridModel<Record> getouchoosemodel() {
        if (model == null) {
            model = new DataGridModel<Record>() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int arg0, int arg1, String arg2, String arg3) {
                    //获取当期阶段需要办理的审批事项的集合，供根据部门名称和主题标识，获取征求部门的集合
                    List<AuditSpTask> listsptask = iauditsptask.getAllAuditSpTaskByPhaseguid(phaseGuid).getResult();
                    List<String> basetaskguids = new ArrayList<>();
                    for (AuditSpTask auditsptask : listsptask) {
                        if (!basetaskguids.contains(auditsptask.getBasetaskguid())) {
                            basetaskguids.add(auditsptask.getBasetaskguid());
                        }
                    }
                    //根据basetaskguids 获取审批事项部门
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.in("rowguid", "'" + StringUtil.join(basetaskguids, "','") + "'");
                    List<Record> listou = iauditspbasetask.getDistinctOuByCondition(sqlc.getMap()).getResult();
                    //根据审批事项部门生成记录
                    List<Record> list = new ArrayList<>();
                    for (Record ou : listou) {
                        Record r = new Record();
                        r.set("ouname", ou.getStr("ouname"));
                        //处理征求部门的逻辑
                        List<Record> listrou = iauditspbasetaskr.getTaskidlistbyBasetaskOuname(ou.getStr("ouname"), basetaskguids).getResult();
                        //如果市级过滤掉所有他辖区的征求
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(citylevel)) {
                            listrou = listrou.stream().filter(a -> ZwfwUserSession.getInstance().getAreaCode().equals(a.getStr("areacode"))).collect(Collectors.toList());
                        } else if (ZwfwConstant.CONSTANT_STR_TWO.equals(citylevel)) {
                            //如果是区县的，过滤其他区县
                            listrou = listrou.stream().filter(a -> sjcode.contains(a.getStr("areacode"))).collect(Collectors.toList());
                        }
                        //转换部门多选的下拉数据
                        List<String> oustem = new ArrayList<>();
                        for (Record record : listrou) {
                            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(record.getStr("taskid")).getResult();
                            if (audittask == null || oustem.contains(audittask.getOuguid())) {
                                continue;
                            }
                            oustem.add(audittask.getOuguid());
                            record.set("id", audittask.getOuguid());
                            record.set("areacode", audittask.getAreacode());
                            record.set("text", record.getStr("xiaquname") + "-" + audittask.getOuname());
                        }
                        listrou.removeIf(a -> {
                            if (StringUtil.isBlank(a.getStr("id"))) {
                                return true;
                            } else {
                                return false;
                            }
                        });

                        Map<String, List<Record>> oulistmap = listrou.stream().collect(Collectors.groupingBy(x -> x.getStr("areacode")));
                        if (oulistmap.get(ZwfwUserSession.getInstance().getAreaCode()) != null && oulistmap.get(ZwfwUserSession.getInstance().getAreaCode()).size() > 0) {
                            r.set("ouchoosedata", JsonUtil.listToJson(oulistmap.get(ZwfwUserSession.getInstance().getAreaCode())));
                        } else {
                            r.set("ouchoosedata", "");
                        }


                        oulistmap.remove(ZwfwUserSession.getInstance().getAreaCode());

                        List<Record> otherarea = new ArrayList<>();
                        for (List<Record> string : oulistmap.values()) {
                            otherarea.addAll(string);
                        }
                        if (otherarea.size() > 0) {
                            r.set("otherarea", JsonUtil.listToJson(otherarea));
                        } else {
                            r.set("otherarea", "");
                        }
                        list.add(r);
                    }
                    list.removeIf(a -> {
                        String b = a.getStr("ouchoosedata");
                        String c = a.getStr("otherarea");
                        if (StringUtil.isBlank(b) && StringUtil.isBlank(c)) {
                            return true;
                        }
                        List<Record> r = JsonUtil.jsonToList(b, Record.class);
                        List<Record> r1 = JsonUtil.jsonToList(c, Record.class);
                        boolean rresult;
                        boolean r1result;
                        if (r != null && r.size() > 0) {
                            rresult = false;
                        } else {
                            rresult = true;
                        }
                        if (r1 != null && r1.size() > 0) {
                            r1result = false;
                        } else {
                            r1result = true;
                        }
                        if (rresult && r1result) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                    this.setRowCount(list.size());
                    return list;
                }

            };
        }
        return model;
    }

    //拆分建设单位信息到建设单位表中去
    public void insertJsdw() {
        if (StringUtil.isNotBlank(itemGuid)) {
            ParticipantsInfo participantsInfo = new ParticipantsInfo();
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
            participantsInfo.setRowguid(UUID.randomUUID().toString());
            participantsInfo.setItemguid(itemGuid);
            participantsInfo.setSubappguid(subappGuid);
            participantsInfo.setCorptype("31");
            participantsInfo.setOperatedate(new Date());
            participantsInfo.setOperateusername(userSession.getDisplayName());
            participantsInfo.setAddress(auditRsItemBaseinfo.getConstructionaddress());
            participantsInfo.setCorpname(auditRsItemBaseinfo.getItemlegaldept());
            participantsInfo.setCorpcode(auditRsItemBaseinfo.getItemlegalcertnum());
            participantsInfo.setLegalpersonicardnum(auditRsItemBaseinfo.getLegalpersonicardnum());
            participantsInfo.setLegal(auditRsItemBaseinfo.getLegalperson());
            participantsInfo.setDanweilxr(auditRsItemBaseinfo.getContractperson());
            participantsInfo.setDanweilxrlxdh(auditRsItemBaseinfo.getContractphone());
            participantsInfo.setXmfzr_idcard(auditRsItemBaseinfo.getContractidcart());
            participantsInfo.setLegalproperty(auditRsItemBaseinfo.getLegalproperty());
            participantsInfo.setItemlegaldept(auditRsItemBaseinfo.getItemlegaldept());
            participantsInfo.setItemlegalcerttype(auditRsItemBaseinfo.getItemlegalcerttype());
            participantsInfo.setItemlegalcertnum(auditRsItemBaseinfo.getItemlegalcertnum());
            participantsInfo.setLegalpersonicardnum(auditRsItemBaseinfo.getLegalpersonicardnum());
            participantsInfo.setFrphone(auditRsItemBaseinfo.getFrphone());
            participantsInfo.setFremail(auditRsItemBaseinfo.getFremail());
            int s = iparticipantsInfoService.insert(participantsInfo);
        }
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditRsItemBaseinfo();
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

    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemTypeModel() {
        if (itemTypeModel == null) {
            itemTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目类型", null, false));
        }
        return itemTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getConstructionPropertyModel() {
        if (constructionPropertyModel == null) {
            constructionPropertyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "建设性质", null, false));
        }
        return constructionPropertyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemLegalCertTypeModel() {
        if (itemLegalCertTypeModel == null) {
            itemLegalCertTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请人用来唯一标识的证照类型", null, false));
            //去除身份证等个人选择
            itemLegalCertTypeModel.removeIf(a -> Integer.parseInt(String.valueOf(a.getValue())) >= Integer
                    .parseInt(ZwfwConstant.CERT_TYPE_SFZ));
        }
        return itemLegalCertTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBelongtIndustryModel() {
        if (belongtIndustryModel == null) {
            belongtIndustryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属行业", null, false));
        }
        return belongtIndustryModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getLegalPropertyModel() {
        if (legalPropertyModel == null) {
            legalPropertyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "法人性质", null, false));
        }
        return legalPropertyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFundSourcesModel() {
        if (fundSourcesModel == null) {
            fundSourcesModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "资金来源", null, false));
        }
        return fundSourcesModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFinancialResourcesModel() {
        if (financialResourcesModel == null) {
            financialResourcesModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "财政资金来源", null, false));
        }
        return financialResourcesModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getQuantifyConstructTypeModel() {
        if (quantifyConstructTypeModel == null) {
            quantifyConstructTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "量化建设规模的类别", null, false));
        }
        return quantifyConstructTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTdhqfsModel() {
        if (tdhqfsModel == null) {
            tdhqfsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "土地获取方式", null, false));
        }
        return tdhqfsModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getxmzjlymodel() {
        if (xmzjlymodel == null) {
            xmzjlymodel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目投资来源", null, false));
        }
        return xmzjlymodel;
    }


    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsImprovementModel() {
        if (isImprovementModel == null) {
            isImprovementModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return isImprovementModel;
    }

    public List<SelectItem> getOuListModel() {
        if (OuListModel == null) {
            OuListModel = new ArrayList<SelectItem>();
            // 这里进行绑定部门列表
            List<String> listOu = handleSpBusinessService.getPhaseRelationOus(businessGuid, phaseGuid).getResult();
            for (String ou : listOu) {
                OuListModel.add(new SelectItem(ou, ou));
            }
        }
        return OuListModel;
    }

    public DataGridModel<Record> getTaskListModel() {
        if (taskListModel == null) {
            taskListModel = new DataGridModel<Record>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int arg0, int arg1, String arg2, String arg3) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("businessguid", businessGuid);
                    sqlc.eq("phaseguid", phaseGuid);
                    List<AuditSpTask> listsptask = iauditsptask.getAllAuditSpTask(sqlc.getMap()).getResult();
                    List<String> list = new ArrayList<>();
                    for (AuditSpTask sptask : listsptask) {
                        list.add(sptask.getBasetaskguid());
                    }
                    sqlc.clear();
                    String baseTaskGuids = "";
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        baseTaskGuids = "'" + StringUtil.join(list, "','") + "'";
                        sqlc.in("basetaskguid", baseTaskGuids);
                    } else {
                        return new ArrayList<>();
                    }
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(citylevel)) {
                        sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    } else if (ZwfwConstant.CONSTANT_STR_TWO.equals(citylevel)) {
                        //如果是区县的，过滤其他区县
                        sqlc.in("areacode", StringUtil.joinSql(sjcode));
                    }
                    sqlc.setOrderAsc("areacode");
                    List<AuditSpBasetaskR> auditSpBasetaskRList = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                    //获取辖区的部门部门

                    Map<String, Record> maprecord = new HashMap<>();
                    if (ValidateUtil.isNotBlankCollection(auditSpBasetaskRList)) {
                        for (AuditSpBasetaskR auditSpBasetaskR : auditSpBasetaskRList) {
                            // 获取辖区记录，如果没有获取新的辖区记录
                            Record r = maprecord.get(auditSpBasetaskR.getAreacode());
                            if (r == null) {
                                r = new Record();
                                r.set("xiaquname", auditSpBasetaskR.getXiaquname());
                                r.set("areacode", auditSpBasetaskR.getAreacode());
                                maprecord.put(auditSpBasetaskR.getAreacode(), r);
                            }
                            List<Record> tasklist = r.get("taskdata");
                            if (tasklist == null) {
                                tasklist = new ArrayList<>();
                                r.set("taskdata", tasklist);
                            }
                            Record taskdata = new Record();
                            taskdata.set("id", auditSpBasetaskR.getTaskid());
                            taskdata.set("text", auditSpBasetaskR.getTaskname());
                            tasklist.add(taskdata);
                        }
                    }
                    //转换数据
                    List<Record> returnlist = new ArrayList<>();
                    for (Record r : maprecord.values()) {
                        List<Record> tasklist = r.get("taskdata");
                        tasklist.sort((a, b) -> {
                            return a.getStr("text").compareTo(b.getStr("text"));
                        });
                        r.set("taskdata", JsonUtil.listToJson(tasklist));
                        returnlist.add(r);
                    }
                    return returnlist;

                }
            };
        }
        return taskListModel;
    }

    public void uploadOfficalDoc(String attachGuid) {
        String path = certAttachService.getAttachScan(attachGuid, ZwfwUserSession.getInstance().getAreaCode());
        if (StringUtil.isNotBlank(path)) {
            path = path.split("furl=")[1];
        }
        addCallbackParam("path", path);
    }

    public DataGridModel<AuditSpTask> getDataGridData() {
        if (gridmodel == null) {
            gridmodel = new DataGridModel<AuditSpTask>() {

                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditSpTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("businessguid", businessGuid);
                    sqlc.eq("phaseguid", phaseGuid);
                    PageData<AuditSpTask> pagadata = iauditsptask
                            .selectAuditSpTaskByPage(sqlc.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    if (pagadata != null) {
                        this.setRowCount(pagadata.getRowCount());
                        FrameOu ou;
                        for (AuditSpTask auditsptask : pagadata.getList()) {
                            ou = ouService.getOuByOuGuid(auditsptask.getOuguid());
                            if (ou != null) {
                                auditsptask.set("ouname", ou.getOuname());
                            }
                        }
                        return pagadata.getList();
                    } else {
                        this.setRowCount(0);
                        return new ArrayList<AuditSpTask>();
                    }
                }
            };
        }
        return gridmodel;
    }

    public AuditSpSpGcjsxk getPhase2() {
        return phase2;
    }

    public void setPhase2(AuditSpSpGcjsxk phase2) {
        this.phase2 = phase2;
    }

    public AuditSpSpSgxk getPhase3() {
        return phase3;
    }

    public void setPhase3(AuditSpSpSgxk phase3) {
        this.phase3 = phase3;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getxmlxModel() {
        if (xmlxmodel == null) {
            xmlxmodel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "审批流程类型", null, false));
        }
        return xmlxmodel;
    }

    private String ous;

    public String getOus() {
        return ous;
    }

    public void setOus(String ous) {
        this.ous = ous;
    }

    private String tasks;

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public AuditSpSpLxydghxk getPhase1() {
        return phase1;
    }

    public void setPhase1(AuditSpSpLxydghxk phase1) {
        this.phase1 = phase1;
    }

    public AuditSpSpJgys getPhase4() {
        return phase4;
    }

    public void setPhase4(AuditSpSpJgys phase4) {
        this.phase4 = phase4;
    }

    public List<SelectItem> getprojectLevelmodel() {
        if (projectLevelmodel == null) {
            projectLevelmodel = DataUtil.convertMap2ComboBox(
                    CodeModalFactory.factory("下拉列表", "项目等级", null, false));
        }
        return projectLevelmodel;
    }

    public List<SelectItem> getproTypedmodel() {
        if (proTypedmodel == null) {
            proTypedmodel = DataUtil.convertMap2ComboBox(
                    CodeModalFactory.factory("下拉列表", "立项类型", null, false));
        }
        return proTypedmodel;
    }

    public List<SelectItem> getinvestTypedmodel() {
        if (investTypedmodel == null) {
            investTypedmodel = DataUtil.convertMap2ComboBox(
                    CodeModalFactory.factory("下拉列表", "投资类型", null, false));
        }
        return investTypedmodel;
    }

    public List<SelectItem> getnhdjmodel() {
        if (nhdjmodel == null) {
            nhdjmodel = DataUtil.convertMap2ComboBox(
                    CodeModalFactory.factory("下拉列表", "耐火等级", null, false));
        }
        return nhdjmodel;
    }

    public List<SelectItem> getfldjmodel() {
        if (fldjmodel == null) {
            fldjmodel = DataUtil.convertMap2ComboBox(
                    CodeModalFactory.factory("下拉列表", "防雷等级", null, false));
        }
        return fldjmodel;
    }

    public List<SelectItem> getsylxmodel() {
        if (sylxmodel == null) {
            sylxmodel = DataUtil.convertMap2ComboBox(
                    CodeModalFactory.factory("下拉列表", "使用类型", null, false));
        }
        return sylxmodel;
    }

    public LazyTreeModal9 getgbhymodel() {
        if (gbhymodel == null) {
            gbhymodel = CodeModalFactory.factory("下拉单选树", "国标行业2017", null, false);
        }
        return gbhymodel;
    }

    public List<SelectItem> getzdxmlxmodel() {
        if (zdxmlxmodel == null) {
            zdxmlxmodel = DataUtil.convertMap2ComboBox(
                    CodeModalFactory.factory("下拉列表", "国标_重点项目", null, false));
        }
        return zdxmlxmodel;
    }

    public List<SelectItem> getxmzjsxmodel() {
        if (xmzjsxmodel == null) {
            xmzjsxmodel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "项目资金属性", null, false));
        }
        return xmzjsxmodel;
    }

    public List<SelectItem> getitemSourcemodel() {
        if (itemSourcemodel == null) {
            itemSourcemodel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "项目来源", null, false));
        }
        return itemSourcemodel;
    }

    public void savePhase(String subappguid) {
        //查询修需要提交的阶段表单信息
        AuditSpPhase auditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseGuid).getResult();
        if (StringUtil.isNotBlank(auditSpPhase.getPhaseId())) {
            String[] phaseids = auditSpPhase.getPhaseId().split(",");
            for (String string : phaseids) {
                switch (string) {
                    case ZwfwConstant.CONSTANT_STR_ONE:
                        dealpahseRecord(phase1, subappguid);
                        if (StringUtil.isBlank(phase1.getRowguid())) {
                            phase1.setRowguid(UUID.randomUUID().toString());
                            iauditspsplxydghxkservice.insert(phase1);
                        } else {
                            iauditspsplxydghxkservice.update(phase1);
                        }
                        break;
                    case ZwfwConstant.CONSTANT_STR_TWO:
                        dealpahseRecord(phase2, subappguid);
                        if (StringUtil.isBlank(phase2.getRowguid())) {
                            phase2.setRowguid(UUID.randomUUID().toString());
                            iAuditSpSpGcjsxkService.insert(phase2);
                        } else {
                            iAuditSpSpGcjsxkService.update(phase2);
                        }
                        break;
                    case ZwfwConstant.CONSTANT_STR_THREE:
                        dealpahseRecord(phase3, subappguid);
                        if (StringUtil.isBlank(phase3.getRowguid())) {
                            phase3.setRowguid(UUID.randomUUID().toString());
                            iAuditSpSpSgxkService.insert(phase3);
                        } else {
                            iAuditSpSpSgxkService.update(phase3);
                        }
                        break;
                    case "4":
                        dealpahseRecord(phase4, subappguid);
                        if (StringUtil.isBlank(phase4.getRowguid())) {
                            phase4.setRowguid(UUID.randomUUID().toString());
                            iauditspspjgysservice.insert(phase4);
                        } else {
                            iauditspspjgysservice.update(phase4);
                        }
                        break;
                    default:
                        break;
                }
            }
            //处理数据

        }
    }

    public void dealpahseRecord(Record record, String subappguid) {
        AuditSpISubapp subapp = auditSpISubappService.getSubappByGuid(subappguid).getResult();
        if (subapp != null) {
            AuditRsItemBaseinfo rsitem = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
            if (record != null) {
                record.set("itemname", rsitem.getItemname());
                record.set("subappname", subapp.getSubappname());
                record.set("itemcode", rsitem.getItemcode());
                record.set("moneysources", rsitem.getFundsources());
                record.set("allmoney", rsitem.getTotalinvest());
                record.set("areasources", rsitem.getTdhqfs());
                record.set("subappguid", subappguid);
            }
        }
    }

    ;

    public String getZitemname() {
        return zitemname;
    }

    public void setZitemname(String zitemname) {
        this.zitemname = zitemname;
    }

    public String getZitemcode() {
        return zitemcode;
    }

    public void setZitemcode(String zitemcode) {
        this.zitemcode = zitemcode;
    }

    public String getIsadditem() {
        return isadditem;
    }

    public void setIsadditem(String isadditem) {
        this.isadditem = isadditem;
    }

}
