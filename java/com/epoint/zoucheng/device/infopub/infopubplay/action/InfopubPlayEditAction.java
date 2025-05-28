package com.epoint.zoucheng.device.infopub.infopubplay.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.device.infopub.infopubplay.api.entity.InfopubPlay;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity.InfopubPlayprogram;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.IInfopubPlayterminalService;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.entity.InfopubPlayterminal;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.impl.InfopubPlayterminalService;
import com.epoint.zoucheng.device.infopub.infopubprogram.impl.InfopubProgramService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 节目发布单修改页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-09-01 14:08:28]
 */
@RestController("infopubplayeditaction")
@Scope("request")
public class InfopubPlayEditAction extends BaseController
{
    private static final long serialVersionUID = -4307976919299840123L;

    private CommonService service = new CommonService();
    @Autowired
    private IInfopubPlayterminalService infopubTerminalService;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IHandleRabbitMQ handlemqservice;

    /**
     * 节目发布单实体对象
     */
    private InfopubPlay dataBean = null;
    private DataGridModel<InfopubPlayterminal> terminalmodel;
    private DataGridModel<InfopubPlayprogram> model1;
    private String guid;

    public void pageLoad() {
        guid = getRequestParameter("guid");
        dataBean = service.find(InfopubPlay.class, guid);
        if (dataBean == null) {
            dataBean = new InfopubPlay();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        try {
            JSONObject dataJson = new JSONObject();
            dataJson.put("status", "4");
            dataJson.put("type", "");
            List<InfopubPlayterminal> list = new InfopubPlayterminalService().getTerminalGuid(guid);
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

        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public DataGridModel<InfopubPlayprogram> getProgramData() {
        // 获得表格对象
        if (model1 == null) {
            model1 = new DataGridModel<InfopubPlayprogram>()
            {

                @Override
                public List<InfopubPlayprogram> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isNotBlank(guid)) {
                        conditionSql = conditionSql + " and playguid='" + guid + "' order by OperateDate asc";
                    }
                    List<InfopubPlayprogram> list = service.findList(
                            ListGenerator.generateSql("InfoPub_PlayProgram", conditionSql, sortField, sortOrder), first,
                            pageSize, InfopubPlayprogram.class, conditionList.toArray());
                    int count = service.find(ListGenerator.generateSql("InfoPub_PlayProgram", conditionSql),
                            Integer.class, conditionList.toArray());

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
                    if (StringUtil.isNotBlank(guid)) {
                        conditionSql = conditionSql + " and playguid='" + guid + "'";
                    }
                    List<InfopubPlayterminal> list = service.findList(
                            ListGenerator.generateSql("InfoPub_PlayTerminal", conditionSql, sortField, sortOrder),
                            first, pageSize, InfopubPlayterminal.class, conditionList.toArray());
                    for (InfopubPlayterminal info : list) {
                        info.put("terminalname", infopubTerminalService.getMacByGuid(info.getTerminalguid()));
                        info.put("location", infopubTerminalService.getLocationByGuid(info.getTerminalguid()));
                    }
                    int count = service.find(ListGenerator.generateSql("InfoPub_PlayTerminal", conditionSql),
                            Integer.class, conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return terminalmodel;
    }

    public void deleteSelect() {
        List<String> select = getProgramData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel, InfopubPlayprogram.class);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public void deleteTerminalSelect() {
        List<String> select = getTerminalDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel, InfopubPlayterminal.class);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public InfopubPlay getDataBean() {
        return dataBean;
    }

    public void setDataBean(InfopubPlay dataBean) {
        this.dataBean = dataBean;
    }

}
