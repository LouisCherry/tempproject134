package com.epoint.zoucheng.device.infopub.infopubpublish.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.IInfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.impl.InfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.IInfopubPublishService;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.entity.InfopubPublish;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 节目发布表list页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-23 08:42:26]
 */
@RestController("infopubpublishlistaction")
@Scope("request")
public class InfopubPublishListAction extends BaseController
{
    private static final long serialVersionUID = 5528676166257641323L;
    @Autowired
    private IInfopubPublishService service;
    @Autowired
    private IInfopubProgramService infopubProgramService;

    /**
     * 节目发布表实体对象
     */
    private InfopubPublish dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<InfopubPublish> model;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<InfopubPublish> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<InfopubPublish>()
            {

                @Override
                public List<InfopubPublish> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    String ledGuid = getRequestParameter("ledguid");
                    if (StringUtil.isNotBlank(ledGuid)) {
                        conditionSql = conditionSql + " and ledguid='" + ledGuid + "'";
                    }
                    List<InfopubPublish> list = service.findList(
                            ListGenerator.generateSql("InfoPub_Publish", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());

                    //展示界面的节目标识显示为节目 名称
                    if (list != null) {
                        for (InfopubPublish info : list) {
                            info.put("programguid", infopubProgramService.getProgramName(info.getProgramguid()));
                        }

                        for (InfopubPublish info2 : list) {
                            String startHour = "";
                            String endHour = "";
                            String startMinute = "";
                            String endMinute = "";
                            int startTimeHour = info2.getStarttimehour();
                            int endTimeHour = info2.getEndtimehour();
                            int startTimeMinute = info2.getStarttimeminute();
                            int endTimeMinute = info2.getEndtimeminute();
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
                            info2.put("starttime", startHour + ":" + startMinute);
                            info2.put("endtime", endHour + ":" + endMinute);
                        }

                        for (InfopubPublish info3 : list) {
                            if (info3.getDuration() / 60000 < 1) {
                                info3.put("duration", (info3.getDuration() / 1000) + "秒");
                            }
                            else {
                                info3.put("duration",
                                        info3.getDuration() / 60000 + "分" + (info3.getDuration() % 60000) / 1000 + "秒");
                            }
                        }
                    }
                    int count = service.findList(
                            ListGenerator.generateSql("InfoPub_Publish", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
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

}
