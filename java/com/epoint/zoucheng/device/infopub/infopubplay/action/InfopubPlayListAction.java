package com.epoint.zoucheng.device.infopub.infopubplay.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.zoucheng.device.infopub.infopubplay.api.IInfopubPlayService;
import com.epoint.zoucheng.device.infopub.infopubplay.api.entity.InfopubPlay;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.IInfopubPlayprogramService;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.IInfopubPlayterminalService;

/**
 * 节目发布单list页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-09-01 14:08:28]
 */
@RestController("infopubplaylistaction")
@Scope("request")
public class InfopubPlayListAction extends BaseController
{
    private static final long serialVersionUID = 3726218328702232502L;

    @Autowired
    private IInfopubPlayService service;
    @Autowired
    private IInfopubPlayprogramService infopubPlayprogramService;
    @Autowired
    private IInfopubPlayterminalService infopubPlayterminalService;

    /**
     * 节目发布单实体对象
     */
    private InfopubPlay dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<InfopubPlay> model;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            infopubPlayterminalService.deleteByPlayGuid(sel);
            infopubPlayprogramService.deleteByPlayGuid(sel);
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<InfopubPlay> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<InfopubPlay>()
            {

                @Override
                public List<InfopubPlay> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<InfopubPlay> list = service.findList(
                            ListGenerator.generateSql("InfoPub_Play", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service
                            .findList(ListGenerator.generateSql("InfoPub_Play", conditionSql, sortField, sortOrder),
                                    conditionList.toArray())
                            .size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
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

}
