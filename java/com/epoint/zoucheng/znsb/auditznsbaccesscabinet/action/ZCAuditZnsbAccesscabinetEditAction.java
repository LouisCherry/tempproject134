package com.epoint.zoucheng.znsb.auditznsbaccesscabinet.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.epoint.basic.auditqueue.auditznsbaccesscabinet.domain.AuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.inter.IAuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zoucheng.znsb.worktablecomment.util.QueueConstant;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 智能化存取柜表修改页面对应的后台
 * 
 * @author 54201
 * @version [版本号, 2019-02-20 14:02:31]
 */

@RestController("zcauditznsbaccesscabineteditaction")
@Scope("request")
public class ZCAuditZnsbAccesscabinetEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 3606533606812952372L;

    @Autowired
    private IAuditZnsbAccesscabinet service;

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    /**
     * 智能化存取柜表实体对象
     */
    private AuditZnsbAccesscabinet dataBean = null;

    /**
    * 存取柜状态下拉列表model
    */
    private List<SelectItem> cabinetstatusModel = null;

    /**
     * 一体机下拉列表model
     */
    private List<SelectItem> machinelistModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbAccesscabinet();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("macaddress", dataBean.getMacaddress());
        List<AuditZnsbAccesscabinet> cabinetlist = service.getCabinetList(sql.getMap(), "rowguid,cabinetname,cabinetno")
                .getResult();
        /*if ((!cabinetlist.isEmpty()) && !cabinetlist.get(0).getRowguid().equals(dataBean.getRowguid())) {
            addCallbackParam("msg", "所选的一体机已有关联的存取柜,修改失败！");
        }
        else {*/
            dataBean.setOperatedate(new Date());
            service.update(dataBean);
            addCallbackParam("msg", "修改成功！");
        /*}*/

    }

    public AuditZnsbAccesscabinet getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbAccesscabinet dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getCabinetstatusModel() {
        if (cabinetstatusModel == null) {
            cabinetstatusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "存取柜状态", null, false));
        }
        return this.cabinetstatusModel;
    }

    public List<SelectItem> getMachineList() {
        List<AuditZnsbEquipment> machinelist = equipmentservice.getEquipmentListByCenterguid(
                QueueConstant.EQUIPMENT_TYPE_YTJ, ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        List<AuditZnsbEquipment> cabinetlist = equipmentservice.getEquipmentListByCenterguid(
                QueueConstant.EQUIPMENT_TYPE_CABINET, ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        List<AuditZnsbEquipment> ytj24list = equipmentservice.getEquipmentListByCenterguid(
                QueueConstant.EQUIPMENT_TYPE_YTJ24, ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        machinelist.addAll(ytj24list);
        machinelist.addAll(cabinetlist);
        List<Map<String, String>> machinemaplist = new ArrayList<Map<String, String>>();
        for (AuditZnsbEquipment auditZnsbEquipment : machinelist) {
            Map<String, String> machinemap = new HashMap<String, String>();
            machinemap.put(auditZnsbEquipment.getMachinename(), auditZnsbEquipment.getMacaddress());
            machinemaplist.add(machinemap);
        }
        if (machinelistModel == null) {
            machinelistModel = DataUtil.convertMap2ComboBox(machinemaplist);
        }
        return this.machinelistModel;
    }

}
