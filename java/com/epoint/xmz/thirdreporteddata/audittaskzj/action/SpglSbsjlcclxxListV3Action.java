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
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxclmlxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSpsxclmlxxbService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspsxjbxxb;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController("spglsbsjlcclxxlistv3action")
@Scope("request")
public class SpglSbsjlcclxxListV3Action extends BaseController
{

    private static final long serialVersionUID = 8796953926146877276L;

    transient Logger log = LogUtil.getLog(SpglSbsjlcclxxListV3Action.class);

    /**
     * 流程信息实体对象
     */
    private SpglSpsxclmlxxb dataBean;

    @Autowired
    private ISpglsplcjdsxxxb iSpglsplcjdsxxxb;

    @Autowired
    private ISpglSpsxclmlxxbService iSpglSpsxclmlxxbService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private ISpglspsxjbxxb iSpglspsxjbxxb;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglSpsxclmlxxb> model;

    private List<SelectItem> sbztModel = null;

    private String rowguid = null;
    private String spsxbm = null;
    private String spsxbbh = null;
    private String xzqhdm = null;

    @Override
    public void pageLoad() {
        rowguid = getRequestParameter("rowguid");
        Spglsplcjdsxxxb find = iSpglsplcjdsxxxb.find(rowguid);
        if (find != null) {
            spsxbm = find.getSpsxbm();
            spsxbbh = String.valueOf(find.getSpsxbbh());
            xzqhdm = find.getXzqhdm();
        }
    }

    public DataGridModel<SpglSpsxclmlxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglSpsxclmlxxb>()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 5925727181973702200L;

                @Override
                public List<SpglSpsxclmlxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getClmc())) {
                        sql.eq("clmc", dataBean.getClmc());
                    }
                    if (dataBean.getSjsczt() != null) {
                        sql.eq("sjsczt", dataBean.getSjsczt().toString());
                    }
                    if (StringUtil.isNotBlank(spsxbm)) {
                        sql.eq("spsxbm", spsxbm);
                    }
                    if (StringUtil.isNotBlank(spsxbbh)) {
                        sql.eq("spsxbbh", spsxbbh);
                    }
                    if (StringUtil.isNotBlank(xzqhdm)) {
                        sql.eq("xzqhdm", xzqhdm);
                    }
                    // 有效数据
                    sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // 去除草稿
                    sql.nq("IFNULL(sync,0)", "-1");
                    // 再按照同步时间排序
                    sql.setOrderDesc("operatedate");
                    PageData<SpglSpsxclmlxxb> pageData = iSpglSpsxclmlxxbService.getAllByPage(sql.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglSpsxclmlxxb> lists = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(lists)) {
                        for (SpglSpsxclmlxxb spglSpsxclmlxxb : lists) {
                            // 本地校验失败
                            if (spglSpsxclmlxxb.getSjsczt() == -1) {
                                spglSpsxclmlxxb.put("sjscztText", "本地校验失败");
                            }
                            else {
                                spglSpsxclmlxxb.put("sjscztText",
                                        codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                                spglSpsxclmlxxb.getSjsczt().toString()));
                            }

                        }
                    }
                    return lists;
                }
            };
        }
        return model;
    }

    public SpglSpsxclmlxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglSpsxclmlxxb();
        }
        return dataBean;
    }

    public void setDataBean(SpglSpsxclmlxxb dataBean) {
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
