package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJzgcsgxkxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJzgcsgxkxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 建筑工程施工许可信息表list页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-01 15:33:12]
 */
@RestController("spgljzgcsgxkxxbv3listaction")
@Scope("request")
public class SpglJzgcsgxkxxbV3ListAction extends BaseController
{
    @Autowired
    private ISpglJzgcsgxkxxbV3Service service;

    /**
     * 建筑工程施工许可信息表实体对象
     */
    private SpglJzgcsgxkxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglJzgcsgxkxxbV3> model;

    

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

    public DataGridModel<SpglJzgcsgxkxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglJzgcsgxkxxbV3>()
            {

                @Override
                public List<SpglJzgcsgxkxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<SpglJzgcsgxkxxbV3> list = service.findList(
                            ListGenerator.generateSql("SPGL_JZGCSGXKXXB_V3", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countSpglJzgcsgxkxxbV3(
                            ListGenerator.generateSql("SPGL_JZGCSGXKXXB_V3", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public SpglJzgcsgxkxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglJzgcsgxkxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglJzgcsgxkxxbV3 dataBean) {
        this.dataBean = dataBean;
    }



}
