package com.epoint.zoucheng.device.infopub.infopubprogram.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.IInfopubPlayprogramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.IInfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.InfopubProgram;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 节目表list页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-16 15:39:32]
 */
@RestController("infopubprogramlistaction")
@Scope("request")
public class InfopubProgramListAction extends BaseController
{
    private static final long serialVersionUID = 8456894867901998794L;
    @Autowired
    private IInfopubProgramService infopubProgramService;
    @Autowired
    private IInfopubProgramService infopubPublishService;
    @Autowired
    private IInfopubPlayprogramService infopubPlayprogramService;

    /** 
     * 节目表实体对象
     */
    private InfopubProgram dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<InfopubProgram> model;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        int flag = 0;
        for (String sel : select) {
            if (infopubPublishService.getProgramCount(sel) == 0
                    && infopubPlayprogramService.getProgramCount(sel) == 0) {
                infopubProgramService.deleteByGuid(sel);
            }
            else {
                flag = 1;
            }
        }
        if (flag == 0) {
            addCallbackParam("msg", "成功删除！");
        }
        else {
            addCallbackParam("msg", "该节目已经播放，请先在LED大屏或节目单上删除该节目！");
        }
    }

    public DataGridModel<InfopubProgram> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<InfopubProgram>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<InfopubProgram> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<InfopubProgram> list = infopubProgramService.findList(
                            ListGenerator.generateSql("InfoPub_Program", conditionSql, "updatetime", sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = infopubProgramService.findList(ListGenerator.generateSql("InfoPub_Program", conditionSql, sortField, sortOrder), conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public InfopubProgram getDataBean() {
        if (dataBean == null) {
            dataBean = new InfopubProgram();
        }
        return dataBean;
    }

    public void setDataBean(InfopubProgram dataBean) {
        this.dataBean = dataBean;
    }

}
