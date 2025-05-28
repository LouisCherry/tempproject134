package com.epoint.zoucheng.device.infopub.infopubplay.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.rabbitmq.Producer;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.device.infopub.infopubplay.api.IInfopubPlayService;
import com.epoint.zoucheng.device.infopub.infopubplay.api.entity.InfopubPlay;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.IInfopubPlayprogramService;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity.InfopubPlayprogram;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.IInfopubPlayterminalService;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.entity.InfopubPlayterminal;
import com.epoint.zoucheng.device.infopub.infopubprogram.impl.InfopubProgramService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
/**
 * 节目发布单新增页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-09-01 14:08:28]
 */
@RestController("infopubplayaddaction")
@Scope("request")
public class InfopubPlayAddAction extends BaseController
{
    private static final long serialVersionUID = -9057804680494832254L;
    @Autowired
    private IInfopubPlayprogramService playProgramService;
    @Autowired
    private IInfopubPlayService service;
    @Autowired
    private IInfopubPlayterminalService terminalService;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IHandleRabbitMQ handlemqservice;
    
    /**
     * 节目发布单实体对象
     */
    private InfopubPlay dataBean = null;
    private String rowGuid = "";
    private DataGridModel<InfopubPlayterminal> terminalmodel;
    private DataGridModel<InfopubPlayprogram> model1;

    public void pageLoad() {
        dataBean = new InfopubPlay();
        if (StringUtil.isBlank(getRowGuid())) {
            setRowGuid(UUID.randomUUID().toString());
        }
        dataBean.setRowguid(this.rowGuid);
    }

    public void deleteSelect() {
        List<String> select = getProgramData().getSelectKeys();
        for (String sel : select) {
            playProgramService.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<InfopubPlayprogram> getProgramData() {
        // 获得表格对象
        if (model1 == null) {
            model1 = new DataGridModel<InfopubPlayprogram>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 8399732683035140836L;

                @Override
                public List<InfopubPlayprogram> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isNotBlank(rowGuid)) {
                        conditionSql += " and playguid = ?  order by OperateDate asc ";
                    }
                    conditionList.add(rowGuid);
                    List<InfopubPlayprogram> list = playProgramService.findList(
                            ListGenerator.generateSql("InfoPub_PlayProgram", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = playProgramService.findList(
                            ListGenerator.generateSql("InfoPub_PlayProgram", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray()).size();

                    if (list != null) {
                        for (InfopubPlayprogram info2 : list) {
                            info2.put("programname",
                                    new InfopubProgramService().getProgramName(info2.getProgramguid()));
                        }
                        for (InfopubPlayprogram info : list) {
                            String startHour = "";
                            String endHour = "";
                            String startMinute = "";
                            String endMinute = "";
                            int startTimeHour = info.getStarttimehour();
                            int endTimeHour = info.getEndtimehour();
                            int startTimeMinute = info.getStarttimeminute();
                            int endTimeMinute = info.getEndtimeminute();
                            if (startTimeHour < 10) {
                                startHour = "0" + startTimeHour;
                            }
                            else {
                                startHour = startTimeHour + "";
                            }
                            if (endTimeHour < 10) {
                                endHour = "0" + endTimeHour;
                            }
                            else {
                                endHour = endTimeHour + "";
                            }
                            if (startTimeMinute < 10) {
                                startMinute = "0" + startTimeMinute;
                            }
                            else {
                                startMinute = startTimeMinute + "";
                            }
                            if (endTimeMinute < 10) {
                                endMinute = "0" + endTimeMinute;
                            }
                            else {
                                endMinute = endTimeMinute + "";
                            }
                            info.put("starttime", startHour + ":" + startMinute);
                            info.put("endtime", endHour + ":" + endMinute);
                        }

                        for (InfopubPlayprogram info1 : list) {
                            if (info1.getShowtime() / 60000 < 1) {
                                info1.put("showtime", (info1.getShowtime() / 1000) + "秒");
                            }
                            else {
                                info1.put("showtime",
                                        info1.getShowtime() / 60000 + "分" + (info1.getShowtime() % 60000) / 1000 + "秒");
                            }
                        }
                    }

                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model1;
    }

    public DataGridModel<InfopubPlayterminal> getTerminalDataGridData() {
        // 获得表格对象
        if (terminalmodel == null) {
            terminalmodel = new DataGridModel<InfopubPlayterminal>()
            {

                @Override
                public List<InfopubPlayterminal> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isNotBlank(rowGuid)) {
                        conditionSql = conditionSql + " and playguid='" + rowGuid + "'";
                    }
                    List<InfopubPlayterminal> list = terminalService.findList(
                            ListGenerator.generateSql("InfoPub_PlayTerminal", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    for (InfopubPlayterminal info : list) {
                        info.put("terminalname", terminalService.getMacByGuid(info.getTerminalguid()));
                        info.put("location", terminalService.getLocationByGuid(info.getTerminalguid()));
                        info.put("ip", terminalService.getIPByGuid(info.getTerminalguid()));
                    }
                    int count = terminalService.findList(
                            ListGenerator.generateSql("InfoPub_PlayTerminal", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return terminalmodel;
    }

    public void add() {
        try {
            JSONObject dataJson = new JSONObject();
            dataJson.put("status", "4");
            dataJson.put("type", "");
            List<InfopubPlayterminal> list = terminalService.getTerminalGuid(rowGuid);
            for (InfopubPlayterminal info : list) {
                //发送刷新指令
                AuditZnsbEquipment equipment = equipmentservice.getEquipmentByRowguid(info.getTerminalguid()).getResult();
                if (equipment != null && StringUtil.isNotBlank(equipment.getMacaddress())) {
                    
                    handlemqservice.sendMQDevice(equipment.getMacaddress(),dataJson.toString());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        dataBean.setPlayuser(userSession.getDisplayName());
        dataBean.setPlaytime(new Date());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    public void deleteTerminalSelect() {
        List<String> select = getTerminalDataGridData().getSelectKeys();
        for (String sel : select) {
            terminalService.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public InfopubPlay getDataBean() {
        if (dataBean == null) {
            dataBean = new InfopubPlay();
        }
        return dataBean;
    }

    public void setDataBean(InfopubPlay dataBean) {
        this.dataBean = dataBean;
    }

    public String getRowGuid() {
        if (StringUtil.isBlank(this.rowGuid)) {
            if (!StringUtil.isBlank(getViewData("mainRowGuid"))) {
                this.rowGuid = getViewData("mainRowGuid");
            }
            setRowGuid(this.rowGuid);
        }
        return this.rowGuid;
    }

    private void setRowGuid(String value) {
        this.rowGuid = value;
        if (!StringUtil.isBlank(this.rowGuid)) {
            addViewData("mainRowGuid", value);
        }
    }

}
