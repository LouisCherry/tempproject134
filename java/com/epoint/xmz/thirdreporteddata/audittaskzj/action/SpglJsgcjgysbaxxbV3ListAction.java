package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcjgysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcjgysbaxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 建设工程竣工验收备案信息表list页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-02 09:20:12]
 */
@RestController("spgljsgcjgysbaxxbv3listaction")
@Scope("request")
public class SpglJsgcjgysbaxxbV3ListAction extends BaseController
{
    @Autowired
    private ISpglJsgcjgysbaxxbV3Service service;

    /**
     * 建设工程竣工验收备案信息表实体对象
     */
    private SpglJsgcjgysbaxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglJsgcjgysbaxxbV3> model;

    

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

    public DataGridModel<SpglJsgcjgysbaxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglJsgcjgysbaxxbV3>()
            {

                @Override
                public List<SpglJsgcjgysbaxxbV3> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<SpglJsgcjgysbaxxbV3> list = service.findList(
                            ListGenerator.generateSql("SPGL_JSGCJGYSBAXXB_V3", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countSpglJsgcjgysbaxxbV3(
                            ListGenerator.generateSql("SPGL_JSGCJGYSBAXXB_V3", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public SpglJsgcjgysbaxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglJsgcjgysbaxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglJsgcjgysbaxxbV3 dataBean) {
        this.dataBean = dataBean;
    }



}
