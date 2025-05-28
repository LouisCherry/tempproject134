package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglKcsjryxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglKcsjryxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 勘察设计人员信息表list页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-08 18:25:06]
 */
@RestController("spglkcsjryxxbv3listaction")
@Scope("request")
public class SpglKcsjryxxbV3ListAction extends BaseController
{
    @Autowired
    private ISpglKcsjryxxbV3Service service;

    /**
     * 勘察设计人员信息表实体对象
     */
    private SpglKcsjryxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglKcsjryxxbV3> model;

    

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
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<SpglKcsjryxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglKcsjryxxbV3>()
            {

                @Override
                public List<SpglKcsjryxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<SpglKcsjryxxbV3> list = service.findList(
                            ListGenerator.generateSql("SPGL_KCSJRYXXB_V3", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countSpglKcsjryxxbV3(
                            ListGenerator.generateSql("SPGL_KCSJRYXXB_V3", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public SpglKcsjryxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglKcsjryxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglKcsjryxxbV3 dataBean) {
        this.dataBean = dataBean;
    }



}
