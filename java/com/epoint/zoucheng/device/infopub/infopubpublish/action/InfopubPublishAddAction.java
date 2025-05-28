package com.epoint.zoucheng.device.infopub.infopubpublish.action;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.IInfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.InfopubProgram;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.IInfopubPublishService;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.entity.InfopubPublish;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 节目发布表新增页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-23 08:42:26]
 */
@RestController("infopubpublishaddaction")
@Scope("request")
public class InfopubPublishAddAction extends BaseController
{

    private static final long serialVersionUID = 7179656401434045768L;
    @Autowired
    private IInfopubPublishService service;
    @Autowired
    private IInfopubProgramService infopubProgramService;
    /**
     * 节目发布表实体对象
     */
    private InfopubPublish dataBean = null;
    private List<SelectItem> minutesecondModel = null;

    private List<SelectItem> hourModel = null;

    private List<InfopubProgram> programguidList = null;
    private String ledGuid;
    private String minute;
    private String second;

    public void pageLoad() {
        dataBean = new InfopubPublish();
        ledGuid = getRequestParameter("ledguid");
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        List<InfopubPublish> list = service.getAll(ledGuid);
        String programGuid = dataBean.getProgramguid();
        int startTimeHour = dataBean.getStarttimehour();
        int endTimeHour = dataBean.getEndtimehour();
        int startTimeMinute = dataBean.getStarttimeminute();
        int endTimeMinute = dataBean.getEndtimeminute();
        for (InfopubPublish info : list) {
            if (programGuid.equals(info.getProgramguid())) {
                if (!(endTimeHour < info.getStarttimehour() || startTimeHour > info.getEndtimehour()
                        || (endTimeHour == info.getStarttimehour() && endTimeMinute < info.getStarttimeminute())
                        || (startTimeHour == info.getEndtimehour() && startTimeMinute > info.getEndtimeminute()))) {
                    addCallbackParam("msg", "此节目已在该段时间内播放，请重新输入播放时间");
                    return;
                }
            }
        }

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setLedguid(ledGuid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setDuration((Integer.parseInt(minute) * 60 + Integer.parseInt(second)) * 1000);
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new InfopubPublish();
    }

    public List<InfopubProgram> getProgramguidList() {
        if (programguidList == null) {
            programguidList = infopubProgramService.getGuidAndName();
        }
        return programguidList;
    }

    public InfopubPublish getDataBean() {
        if (dataBean == null) {
            dataBean = new InfopubPublish();
        }
        return dataBean;
    }

    public void setDataBean(InfopubPublish dataBean) {
        this.dataBean = dataBean;
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
