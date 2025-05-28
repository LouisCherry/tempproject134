package com.epoint.xmz.homepagenotice.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.homepagenotice.api.entity.HomepageNotice;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.homepagenotice.api.IHomepageNoticeService;


/**
 * 首页弹窗表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-11-08 15:36:46]
 */
@RestController("homepagenoticelistaction")
@Scope("request")
public class HomepageNoticeListAction extends BaseController {
    @Autowired
    private IHomepageNoticeService service;

    /**
     * 首页弹窗表实体对象
     */
    private HomepageNotice dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<HomepageNotice> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;


    public void pageLoad() {
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<HomepageNotice> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<HomepageNotice>() {

                @Override
                public List<HomepageNotice> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<HomepageNotice> list = service.findList(
                            ListGenerator.generateSql("homepage_notice", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countHomepageNotice(ListGenerator.generateSql("homepage_notice", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public HomepageNotice getDataBean() {
        if (dataBean == null) {
            dataBean = new HomepageNotice();
        }
        return dataBean;
    }

    public void setDataBean(HomepageNotice dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


}
