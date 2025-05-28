package com.epoint.zoucheng.device.infopub.infopubplayprogram.action;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.IInfopubPlayprogramService;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity.InfopubPlayprogram;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.IInfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.InfopubProgram;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 发布单节目列表修改页面对应的后台
 * 
 * @author 12150
 * @version [版本号, 2017-09-01 14:05:48]
 */
@RestController("infopubplayprogrameditaction")
@Scope("request")
public class InfopubPlayprogramEditAction extends BaseController
{
    private static final long serialVersionUID = 2350518209086733206L;

    @Autowired
    private IInfopubProgramService infopubProgramService;
    @Autowired
    private IInfopubPlayprogramService service;

    /**
     * 发布单节目列表实体对象
     */
    private InfopubPlayprogram dataBean = null;
    private List<InfopubProgram> programguidList = null;
    private List<SelectItem> minutesecondModel = null;
    private List<SelectItem> hourModel = null;
    private String minute;
    private String second;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        String programguid = infopubProgramService.getProgramName(dataBean.getProgramguid());
        this.addCallbackParam("programguid", programguid);
        minute = dataBean.getShowtime() / 60000 + "";
        second = (dataBean.getShowtime() % 60000) / 1000 + "";
        if (dataBean == null) {
            dataBean = new InfopubPlayprogram();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setShowtime((Integer.parseInt(minute) * 60 + Integer.parseInt(second)) * 1000);
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public InfopubPlayprogram getDataBean() {
        return dataBean;
    }

    public void setDataBean(InfopubPlayprogram dataBean) {
        this.dataBean = dataBean;
    }

    public List<InfopubProgram> getProgramguidList() {
        if (programguidList == null) {
            programguidList = infopubProgramService.getGuidAndName();
        }
        return programguidList;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public List<SelectItem> getMinutesecondModel() {
        if (minutesecondModel == null) {
            minutesecondModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "分钟", null, false));
        }
        return this.minutesecondModel;
    }

    public List<SelectItem> getHourModel() {
        if (hourModel == null) {
            hourModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "小时", null, false));
        }
        return this.hourModel;
    }

}
