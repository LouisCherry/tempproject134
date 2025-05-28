package com.epoint.zoucheng.device.equiment.action;

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
import com.epoint.basic.auditqueue.auditqueuewindow.inter.IAuditQueueWindow;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.IInfopubPlayterminalService;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.entity.InfopubPlayterminal;

/**
 * 设备维护表list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-11-07 14:37:54]
 */
@RestController("zcauditznsbequipmentlistaction")
@Scope("request")
public class ZCAuditZnsbEquipmentListAction extends BaseController
{

    private static final long serialVersionUID = 5633310185213201266L;

    @Autowired
    private IAuditQueueWindow queuewindowservice;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IHandleRabbitMQ handlemqservice;
    
    @Autowired
    private IInfopubPlayterminalService infopubPlayterminalService;
    /**
     * 大厅基础服务api
     */
    @Autowired
    private IAuditOrgaHall orgahallservice;
    /**
     * 设备维护表实体对象
     */
    private AuditZnsbEquipment dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbEquipment> model;
    /**
     * 设备类型下拉列表model
     */
    private List<SelectItem> machinetypeModel = null;
    /**
     * 大厅类型下拉列表model
     */
    private List<SelectItem> hallguidModel = new ArrayList<SelectItem>();

    private String machinename;
    private String machinetype;
    private String machineno;
    private String macaddress;
    private String hallguid;

    public void pageLoad() {
        addCallbackParam("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            AuditZnsbEquipment equipment = equipmentservice.getEquipmentByRowguid(sel).getResult();
            queuewindowservice.deleteWindowByEquipmentguid(equipment.getRowguid());
            equipmentservice.delete(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 重启
     */
    public void restartSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            AuditZnsbEquipment equipment = equipmentservice.getEquipmentByRowguid(sel).getResult();
            if (equipment != null && StringUtil.isNotBlank(equipment.getMacaddress())) {
                handlemqservice.sendMQRestartbyWindow(equipment.getMacaddress());
            }
        }
        addCallbackParam("msg", "重启成功！");
    }

    public DataGridModel<AuditZnsbEquipment> getDataGridData() {

        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbEquipment>()
            {
                @Override
                public List<AuditZnsbEquipment> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(getRequestParameter("guid"))
                            && "NoPZ".equals(getRequestParameter("guid"))) {

                        sql.isBlank("centerguid");
                    }
                    else {
                        sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    }
                    if (StringUtil.isNotBlank(machinename)) {
                        sql.like("machinename", machinename);
                    }

                    if (StringUtil.isNotBlank(machinetype)) {
                        sql.eq("machinetype", machinetype);
                    }
                    if (StringUtil.isNotBlank(machineno)) {
                        sql.like("machineno", machineno);
                    }
                    if (StringUtil.isNotBlank(macaddress)) {
                        sql.like("macaddress", macaddress);
                    }

                    if (StringUtil.isNotBlank(hallguid)) {
                        sql.eq("hallguid", hallguid);
                    }
                    sortField = "machinetype asc,OperateDate";
                    PageData<AuditZnsbEquipment> pageData = equipmentservice
                            .getEquipmentByPage(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    //system.out.println(pageData.getList());
                    return pageData.getList();
                }

            };
        }
        return model;

    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getMachinetypeModel() {
        if (machinetypeModel == null) {
            machinetypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "设备类型", null, true));
        }
        return this.machinetypeModel;
    }
    
    public void saveSelect() {
        String playguid = getRequestParameter("playguid");
        InfopubPlayterminal infopubPlayterminal = new InfopubPlayterminal();
        List<String> select = getDataGridData().getSelectKeys();
        List<String> rowguids = new ArrayList<>();
        if (StringUtil.isNotBlank(select)) {
            for (String sel : select) {
                rowguids.add("'" + sel + "'");
            }
            List<String> guids = infopubPlayterminalService.getRowguidInService(rowguids);
            if (guids.size() > 0) {
                this.addCallbackParam("failed", 1);
                StringBuilder mac = new StringBuilder();
                for (String guid : guids) {
                    mac.append(" '" + infopubPlayterminalService.getMacByGuid(guid) + " ' ");
                }
                String msg = "mac地址为: " + mac + " 的设备正在使用,无法重复使用该设备!";
                this.addCallbackParam("msg", msg);
                return;
            }
            for (String sel : select) {
                int count = infopubPlayterminalService.getCount(playguid, sel);
                if (count == 0) {
                    infopubPlayterminal.setRowguid(UUID.randomUUID().toString());
                    infopubPlayterminal.setOperatedate(new Date());
                    infopubPlayterminal.setOperateusername(userSession.getDisplayName());
                    infopubPlayterminal.setPlayguid(playguid);
                    infopubPlayterminal.setTerminalguid(sel);
                    infopubPlayterminalService.insert(infopubPlayterminal);
                }
            }
            addCallbackParam("msg", "成功添加！");
        }else{
            addCallbackParam("msg", "请至少选择一条记录！");
        }
    }

    //	
    public List<SelectItem> getHallguidModel() {
        hallguidModel.add(new SelectItem("", "请选择..."));
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

    public AuditZnsbEquipment getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbEquipment();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbEquipment dataBean) {
        this.dataBean = dataBean;
    }

    public String getMachinename() {
        return machinename;
    }

    public void setMachinename(String machinename) {
        this.machinename = machinename;
    }

    public String getMachineno() {
        return machineno;
    }

    public void setMachineno(String machineno) {
        this.machineno = machineno;
    }

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype;
    }
    @Override
    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public String getHallguid() {
        return hallguid;
    }

    public void setHallguid(String hallguid) {
        this.hallguid = hallguid;
    }
}
