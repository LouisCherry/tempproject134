package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglGzcnzbjjgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglGzcnzbjjgxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 告知承诺制办件监管信息表list页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-07 09:44:38]
 */
@RestController("spglgzcnzbjjgxxbv3listaction")
@Scope("request")
public class SpglGzcnzbjjgxxbV3ListAction extends BaseController
{
    @Autowired
    private ISpglGzcnzbjjgxxbV3Service service;

    /**
     * 告知承诺制办件监管信息表实体对象
     */
    private SpglGzcnzbjjgxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglGzcnzbjjgxxbV3> model;

    

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

    public DataGridModel<SpglGzcnzbjjgxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglGzcnzbjjgxxbV3>()
            {

                @Override
                public List<SpglGzcnzbjjgxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<SpglGzcnzbjjgxxbV3> list = service.findList(
                            ListGenerator.generateSql("SPGL_GZCNZBJJGXXB_V3", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countSpglGzcnzbjjgxxbV3(
                            ListGenerator.generateSql("SPGL_GZCNZBJJGXXB_V3", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public SpglGzcnzbjjgxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglGzcnzbjjgxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglGzcnzbjjgxxbV3 dataBean) {
        this.dataBean = dataBean;
    }



}
