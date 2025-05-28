package com.epoint.zoucheng.device.infopub.infopubplayprogram.action;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
 * 发布单节目列表新增页面对应的后台
 * 
 * @author 12150
 * @version [版本号, 2017-09-01 14:05:47]
 */
@RestController("infopubplayprogramaddaction")
@Scope("request")
public class InfopubPlayprogramAddAction extends BaseController
{
    private static final long serialVersionUID = -90751015699600432L;
    @Autowired
    private IInfopubProgramService infopubProgramService;
    @Autowired
    private IInfopubPlayprogramService infopubPlayProgramService;

    /**
     * 发布单节目列表实体对象
     */
    private InfopubPlayprogram dataBean = null;
    private List<InfopubProgram> programguidList = null;
    private List<SelectItem> minutesecondModel = null;
    private List<SelectItem> hourModel = null;
    private String playGuid;
    private String minute;
    private String second;

    public void pageLoad() {
        dataBean = new InfopubPlayprogram();
        playGuid = getRequestParameter("playGuid");
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        List<InfopubPlayprogram> list = infopubPlayProgramService.getAll(playGuid);
        String programGuid = dataBean.getProgramguid();
        int startTimeHour = dataBean.getStarttimehour();
        int endTimeHour = dataBean.getEndtimehour();
        int startTimeMinute = dataBean.getStarttimeminute();
        int endTimeMinute = dataBean.getEndtimeminute();
        for (InfopubPlayprogram info : list) {
            if (programGuid.equals(info.getProgramguid())) {
                if (!(endTimeHour < info.getStarttimehour() || startTimeHour > info.getEndtimehour()
                        || (endTimeHour == info.getStarttimehour() && endTimeMinute < info.getStarttimeminute())
                        || (startTimeHour == info.getEndtimehour() && startTimeMinute > info.getEndtimeminute()))) {
                    addCallbackParam("msg", "该段时间内已有此节目，请重新输入播放时间");
                    return;
                }
            }
        }

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setPlayguid(getRequestParameter("playguid"));
        dataBean.setShowtime((Integer.parseInt(minute) * 60 + Integer.parseInt(second)) * 1000);
        infopubPlayProgramService.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new InfopubPlayprogram();
    }

    public InfopubPlayprogram getDataBean() {
        if (dataBean == null) {
            dataBean = new InfopubPlayprogram();
        }
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
