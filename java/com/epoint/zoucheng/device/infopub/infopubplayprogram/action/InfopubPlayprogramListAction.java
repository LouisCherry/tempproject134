package com.epoint.zoucheng.device.infopub.infopubplayprogram.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.IInfopubPlayprogramService;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity.InfopubPlayprogram;
import com.epoint.zoucheng.device.infopub.infopubprogram.impl.InfopubProgramService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 发布单节目列表list页面对应的后台
 * 
 * @author 12150
 * @version [版本号, 2017-09-01 14:05:48]
 */
@RestController("infopubplayprogramlistaction")
@Scope("request")
public class InfopubPlayprogramListAction extends BaseController
{
    private static final long serialVersionUID = -1089272212573017342L;

    @Autowired
    private IInfopubPlayprogramService infopubPlayprogramService;
    /**
     * 发布单节目列表实体对象
     */
    private InfopubPlayprogram dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<InfopubPlayprogram> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            infopubPlayprogramService.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<InfopubPlayprogram> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<InfopubPlayprogram>()
            {

                @Override
                public List<InfopubPlayprogram> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<InfopubPlayprogram> list = infopubPlayprogramService.findList(
                            ListGenerator.generateSql("InfoPub_PlayProgram", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = infopubPlayprogramService.findList(
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
        return model;
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

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
