package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglspjdxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspjdxxb;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController("spglsbsjlcjdcllistv3action")
@Scope("request")
public class SpglSbsjlcjdclListV3Action extends BaseController
{

    private static final long serialVersionUID = 8796953926146877276L;

    transient Logger log = LogUtil.getLog(SpglSbsjlcjdclListV3Action.class);

    /**
     * 流程信息实体对象
     */
    private Spglspjdxxb dataBean;

    @Autowired
    private ISpglspjdxxb iSpglspjdxxb;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 表格控件model
     */
    private DataGridModel<Spglspjdxxb> model;

    private List<SelectItem> sbztModel = null;

    private String splcbm = null;
    private String splcbbh = null;
    private String xzqhdm = null;

    @Override
    public void pageLoad() {
        splcbm = getRequestParameter("splcbm");
        splcbbh = getRequestParameter("splcbbh");
        xzqhdm = getRequestParameter("xzqhdm");
    }

    public DataGridModel<Spglspjdxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Spglspjdxxb>()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 5925727181973702200L;

                @Override
                public List<Spglspjdxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getSpjdmc())) {
                        sql.like("spjdmc", dataBean.getSpjdmc());
                    }
                    if (dataBean.getSjsczt() != null) {
                        sql.eq("sjsczt", dataBean.getSjsczt().toString());
                    }
                    if (StringUtil.isNotBlank(splcbm)) {
                        sql.eq("splcbm", splcbm);
                    }
                    if (StringUtil.isNotBlank(splcbbh)) {
                        sql.eq("splcbbh", splcbbh);
                    }
                    if (StringUtil.isNotBlank(xzqhdm)) {
                        sql.eq("xzqhdm", xzqhdm);
                    }
                    // 有效数据
                    sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // 去除草稿
                    sql.nq("IFNULL(sync,0)", "-1");
                    // 阶段序号降序
                    sql.setOrderAsc("spjdxh");
                    PageData<Spglspjdxxb> pageData = iSpglspjdxxb.getAllByPage(sql.getMap(), first, pageSize, sortField,
                            sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<Spglspjdxxb> lists = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(lists)) {
                        for (Spglspjdxxb spglspjdxxb : lists) {
                            // 本地校验失败
                            if (spglspjdxxb.getSjsczt() == -1) {
                                spglspjdxxb.put("sjscztText", "本地校验失败");
                            }
                            else {
                                spglspjdxxb.put("sjscztText",
                                        codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                                spglspjdxxb.getSjsczt().toString()));
                            }
                        }
                    }
                    return lists;
                }
            };
        }
        return model;
    }

    public Spglspjdxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new Spglspjdxxb();
        }
        return dataBean;
    }

    public void setDataBean(Spglspjdxxb dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSbztModel() {
        if (sbztModel == null) {
            sbztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
            sbztModel.add(new SelectItem("-1", "本地校验失败"));
        }
        return this.sbztModel;
    }
}
