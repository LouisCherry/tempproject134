package com.epoint.jn.equipment.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuewindow.domain.AuditQueueWindow;
import com.epoint.basic.auditqueue.auditqueuewindow.inter.IAuditQueueWindow;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 设备维护表修改页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-11-07 14:37:54]
 */
@RestController("ssauditznsbequipmenteditaction")
@Scope("request")
public class SsAuditZnsbEquipmentEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditQueueWindow queuewindowservice;
    @Autowired
    private IAuditOrgaWindow auditWindowservice;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    /**
     * 大厅基础服务api
     */
    @Autowired
    private IAuditOrgaHall orgahallservice;
    /**
     * 窗口基础服务api
     */
    @Autowired
    private IAuditOrgaWindow windowservice;

    /**
     * 设备维护表实体对象
     */
    private AuditZnsbEquipment dataBean = null;

    /**
     * 设备类型下拉列表model
     */
    private List<SelectItem> machinetypeModel = null;

    /**
     * 读卡设备类型类型下拉列表model
     */
    private List<SelectItem> readCardTypeModel = null;
    /**
     * 取号机首页地址下拉列表model
     */
    private List<SelectItem> homepageurlModel = null;

    /**
     * 热敏剩余数量值model
     */
    private List<SelectItem> rmleftpagerModel = null;
    /**
     * 当前状态单选按钮组model
     */
    private List<SelectItem> statusModel = null;
    /**
     * 当前状态单选按钮组model
     */
    private List<SelectItem> hallguidModel = new ArrayList<SelectItem>();;
    /**
     * 是否使用预约功能单选按钮组model
     */
    private List<SelectItem> isuseappointmentModel = null;
    /**
     * 引导屏是否有外设
     */
    private List<SelectItem> isperipheralsModel = null;
    /**
     * 是否允许跳过单选按钮组model
     */
    private List<SelectItem> isneedpassModel = null;

    /**
     * 彩色打印机model
     */
    private List<SelectItem> colorleftpaperModel = null;

    /**
     * 黑白打印机一号model
     */
    private List<SelectItem> bawleftpaperoneModel = null;

    /**
     * 黑白打印机二号model
     */
    private List<SelectItem> bawleftpapertwoModel = null;
    /**
     * 等待屏选择是否绑定窗口model
     */
    private List<SelectItem> isbindwindowModel = null;

    @Autowired
    private IHandleRabbitMQ handleRabbitMQ;

    /**
     * 所属公司下拉列表model
     */
    private List<SelectItem> getmachineCompanyModel = null;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IHandleConfig handleConfig;

    private String homepageurl1;
    private String url;

    public void pageLoad() {
        dataBean = equipmentservice.getEquipmentByRowguid(getRequestParameter("guid")).getResult();
        if (StringUtil.isBlank(getViewData("macaddress"))) {
            addViewData("macaddress", dataBean.getMacaddress());
        }
        // 获取分布图功能配置的系统参数,值为1启用
        String usefenbu = handleConfig
                .getFrameConfig("AS_USE_YTJ_FENBU_MAP", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        if (StringUtil.isNotBlank(usefenbu) && QueueConstant.CONSTANT_STR_ONE.equals(usefenbu)) {
            addCallbackParam("usefenbu", usefenbu);
        }
        addCallbackParam("isbindwindow", dataBean.getIsBindWindow());
        if (dataBean == null) {
            dataBean = new AuditZnsbEquipment();
        }
        else {

            if (StringUtil.isNotBlank(dataBean.getWindowguid())) {
                AuditOrgaWindow auditWindow = auditWindowservice.getWindowByWindowGuid(dataBean.getWindowguid())
                        .getResult();
                addCallbackParam("windowname", auditWindow == null ? "" : auditWindow.getWindowname());
            }
            else {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("EQUIPMENTGUID", dataBean.getRowguid());
                List<AuditQueueWindow> auditQueueWindows = queuewindowservice
                        .getQueueWindowByEquipmentguids(sql.getMap()).getResult();

                String windownames = "";
                String guids = "";
                StringBuffer sbBuffer = new StringBuffer();
                StringBuffer sbBuffer2 = new StringBuffer();
                for (AuditQueueWindow queuewindow : auditQueueWindows) {
                    sbBuffer.append(windowservice.getWindowByWindowGuid(queuewindow.getWindowguid()).getResult()
                            .getWindowname()).append(",");
                    sbBuffer2.append(queuewindow.getWindowguid()).append(",");
                }
                windownames = sbBuffer.toString();
                guids = sbBuffer2.toString();
                if (StringUtil.isNotBlank(guids)) {
                    dataBean.setWindowguid(guids.substring(0, guids.length() - 1));
                }
                if (StringUtil.isNotBlank(windownames)) {
                    addCallbackParam("windowname", windownames.substring(0, windownames.length() - 1));
                }

            }
            if (QueueConstant.EQUIPMENT_TYPE_QHJ.equals(dataBean.getMachinetype())) {
                homepageurl1 = dataBean.getHomepageurl();
            }
            if (QueueConstant.EQUIPMENT_TYPE_CKP.equals(dataBean.getMachinetype())
                    || QueueConstant.EQUIPMENT_TYPE_DDP.equals(dataBean.getMachinetype())
                    || QueueConstant.EQUIPMENT_TYPE_JHPAD.equals(dataBean.getMachinetype())
                    || QueueConstant.EQUIPMENT_TYPE_YDPS.equals(dataBean.getMachinetype())
                    || QueueConstant.EQUIPMENT_TYPE_YDPH.equals(dataBean.getMachinetype())) {
                url = dataBean.getUrl();
            }
            if (StringUtil.isBlank(dataBean.getReadCardType())) {
                dataBean.setReadCardType("1");
            }
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        String names = "";
        if (QueueConstant.EQUIPMENT_TYPE_QHJ.equals(dataBean.getMachinetype())) {
            dataBean.setHomepageurl(homepageurl1);
        }
        if (QueueConstant.EQUIPMENT_TYPE_CKP.equals(dataBean.getMachinetype())
                || QueueConstant.EQUIPMENT_TYPE_DDP.equals(dataBean.getMachinetype())) {
            if (url.contains("?")) {
                dataBean.setUrl(url);
            }
            else {
                dataBean.setUrl(url + "?MacAddress=" + dataBean.getMacaddress());

            }
        }
        else if (QueueConstant.EQUIPMENT_TYPE_JHPAD.equals(dataBean.getMachinetype())
                || QueueConstant.EQUIPMENT_TYPE_YDPS.equals(dataBean.getMachinetype())
                || QueueConstant.EQUIPMENT_TYPE_YDPH.equals(dataBean.getMachinetype())) {
            dataBean.setUrl(url);
        }
        // 一个窗口只允许对应一个窗口屏、评价器
        if (QueueConstant.EQUIPMENT_TYPE_CKP.equals(dataBean.getMachinetype())
                || QueueConstant.EQUIPMENT_TYPE_PJPAD.equals(dataBean.getMachinetype())) {
            List<String> lists = equipmentservice.getlistbyWindowGuidandType(dataBean.getRowguid(),
                    dataBean.getWindowguid(), dataBean.getMachinetype()).getResult();
            if (lists != null && !lists.isEmpty()) {
                StringBuffer nameStringBuffer = new StringBuffer();
                for (String str : lists) {
                    nameStringBuffer.append(str + ";");
                }
                names += nameStringBuffer.toString();
                addCallbackParam("msg", "该窗口已分配到其他设备，该类型的设备只能绑定一个窗口。设备名称：" + names);
                return;
            }
        }
        addCallbackParam("msg", saveAuditZnsbEquipment(dataBean));
    }

    public void emptyallpaperpiece() {
        if (!QueueConstant.Common_no_String.equals(dataBean.getAllPaperpiece())) {
            equipmentservice.emptyAuditZnsbEquipmentAllpaperpiece(dataBean);
            addCallbackParam("msg", "修改成功！");
        }
        else {
            addCallbackParam("msg", "总纸张已经清空！");
        }

    }

    public void getlogfile(String logtype) {

        handleRabbitMQ.sendMQLogFile(dataBean.getMacaddress(), logtype);
        int filenum = 0;
        if (StringUtil.isNotBlank(dataBean.getLogcliengguid())) {
            filenum = attachService.getAttachListByGuid(dataBean.getLogcliengguid()).size();
        }
        AuditZnsbEquipment newdatebean;
        try {
            for (int i = 0; i < 10; i++) {
                newdatebean = equipmentservice.getDetailbyMacaddress(dataBean.getMacaddress()).getResult();
                int newfilenum = 0;
                if (StringUtil.isNotBlank(newdatebean) && StringUtil.isNotBlank(newdatebean.getLogcliengguid())) {
                    newfilenum = attachService.getAttachListByGuid(newdatebean.getLogcliengguid()).size();
                }

                if (newfilenum > filenum) {
                    addCallbackParam("msg", "日志获取成功!");
                    return;
                }
                Thread.sleep(1000);
            }

            addCallbackParam("msg", "MQ发送失败!");
        }
        catch (Exception e) {
            log.info(e);
            addCallbackParam("msg", "MQ发送失败!");
        }
        equipmentservice.updateIsuploadlog(dataBean.getMacaddress(), logtype);

    }

    public String saveAuditZnsbEquipment(AuditZnsbEquipment dataBean) {
        AuditZnsbEquipment oldequipment = equipmentservice.getDetailbyMacaddress(dataBean.getMacaddress()).getResult();
        if (StringUtil.isBlank(oldequipment) || oldequipment.getRowguid().equals(dataBean.getRowguid())) {
            dataBean.setOperatedate(new Date());
            dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            // 删除此设备guid的智能化窗口相对应的信息，以至于重新建立和等待屏机顶盒的对应关系
            queuewindowservice.deleteWindowByEquipmentguid(dataBean.getRowguid());
            if (StringUtil.isNotBlank(dataBean.getIsBindWindow())
                    && QueueConstant.CONSTANT_STR_ONE.equals(dataBean.getIsBindWindow())) {
                if (QueueConstant.EQUIPMENT_TYPE_DDP.equals(dataBean.getMachinetype())) {
                    if (StringUtil.isNotBlank(dataBean.getWindowguid())) {
                        Date date = new Date();
                        String[] windowguidarr = dataBean.getWindowguid().split(",");
                        for (int i = 0; i < windowguidarr.length; i++) {
                            AuditQueueWindow auditqueuewindow = new AuditQueueWindow();
                            auditqueuewindow.setRowguid(UUID.randomUUID().toString());
                            auditqueuewindow.setOperatedate(date);
                            auditqueuewindow.setOperateusername(UserSession.getInstance().getDisplayName());
                            auditqueuewindow.setWindowguid(windowguidarr[i]);
                            auditqueuewindow.setEquipmentguid(dataBean.getRowguid());
                            queuewindowservice.insertWindow(auditqueuewindow);
                        }
                        dataBean.setWindowguid("");
                    }
                }
            }

            // 取号机、等待屏机顶盒、语音机顶盒配置多个大厅
            String[] hallguids = dataBean.getHallguid().split(",");
            StringBuilder sb = new StringBuilder();
            if (hallguids.length > 0) {
                for (String hallguid : hallguids) {
                    if ("all".equals(hallguid)) {
                        sb.append("所有大厅").append(",");
                        dataBean.setHallguid("all");
                        break;
                    }
                    else {
                        sb.append(orgahallservice.getAuditHallByRowguid(hallguid).getResult().getHallname())
                                .append(",");
                    }
                }
            }
            dataBean.setHallname(sb.toString().substring(0, sb.length() - 1));
            equipmentservice.updateAuditZnsbEquipment(dataBean);
            return "修改成功！";
        }
        else {
            return "该MAC地址已被本中心或其他中心的设备注册，请勿重复注册！";
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getMachinetypeModel() {
        if (machinetypeModel == null) {
            machinetypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "设备类型", null, false));
        }
        return this.machinetypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getReadCardTypeModel() {
        if (readCardTypeModel == null) {
            readCardTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "读卡设备类型", null, false));
        }
        return this.readCardTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getHomepageurlModel() {
        if (homepageurlModel == null) {
            homepageurlModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "取号机首页地址", null, false));
        }
        return this.homepageurlModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getmachineCompanyModel() {
        if (getmachineCompanyModel == null) {
            getmachineCompanyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属公司类型", null, false));
        }
        return this.getmachineCompanyModel;
    }

    public List<SelectItem> getHallguidModel() {
        hallguidModel.add(new SelectItem("all", "所有"));

        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
        List<AuditOrgaHall> halls = orgahallservice.getAllHall(sql.getMap()).getResult();
        for (AuditOrgaHall hall : halls) {
            SelectItem a = new SelectItem(hall.getRowguid(), hall.getHallname());
            hallguidModel.add(a);
        }
        return this.hallguidModel;
    }

    @SuppressWarnings({"unchecked" })
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "设备状态", null, false));
        }
        return this.statusModel;
    }

    @SuppressWarnings({"unchecked" })
    public List<SelectItem> getIsuseappointmentModel() {
        if (isuseappointmentModel == null) {
            isuseappointmentModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isuseappointmentModel;
    }

    @SuppressWarnings({"unchecked" })
    public List<SelectItem> getIsperipheralsModel() {
        if (isperipheralsModel == null) {
            isperipheralsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isperipheralsModel;
    }

    @SuppressWarnings({"unchecked" })
    public List<SelectItem> getIsneedpassModel() {
        if (isneedpassModel == null) {
            isneedpassModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isneedpassModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getRmleftpagerModel() {
        if (rmleftpagerModel == null) {
            rmleftpagerModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "热敏剩余数量值", null, false));
        }
        return this.rmleftpagerModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getColorleftpaperModel() {
        if (colorleftpaperModel == null) {
            colorleftpaperModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "纸盒剩余情况", null, false));
        }
        return this.colorleftpaperModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBawleftpaperoneModel() {
        if (bawleftpaperoneModel == null) {
            bawleftpaperoneModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "纸盒剩余情况", null, false));
        }
        return this.bawleftpaperoneModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBawleftpapertwoModel() {
        if (bawleftpapertwoModel == null) {
            bawleftpapertwoModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "纸盒剩余情况", null, false));
        }
        return this.bawleftpapertwoModel;
    }

    @SuppressWarnings({"unchecked" })
    public List<SelectItem> getIsbindwindowModel() {
        if (isbindwindowModel == null) {
            isbindwindowModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isbindwindowModel;
    }

    public AuditZnsbEquipment getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbEquipment dataBean) {
        this.dataBean = dataBean;
    }

    public String getHomepageurl1() {
        return homepageurl1;
    }

    public void setHomepageurl1(String homepageurl1) {
        this.homepageurl1 = homepageurl1;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
