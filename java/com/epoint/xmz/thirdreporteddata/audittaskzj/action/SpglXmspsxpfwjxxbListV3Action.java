package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxpfwjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxpfwjxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 项目审批事项办理详细信息表list页面对应的后台
 *
 * @author fwang
 * @version [版本号, 2010-07-05 15:59:26]
 */
@RestController("spglxmspsxpfwjxxblistv3action")
@Scope("request")
public class SpglXmspsxpfwjxxbListV3Action extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxpfwjxxbV3 iSpglXmspsxpfwjxxbv3;
    @Autowired
    private ICodeItemsService codeItemsService;
    /**
     * 实体对象
     */
    private SpglXmspsxpfwjxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglXmspsxpfwjxxbV3> model;

    private List<SelectItem> sbztModel;
    private String xzqhdm;
    private String gcdm;
    private String spsxslbm;

    public void pageLoad() {
        xzqhdm = getRequestParameter("xzqhdm");
        gcdm = getRequestParameter("gcdm");
        spsxslbm = getRequestParameter("spsxslbm");
        if (dataBean == null) {
            dataBean = new SpglXmspsxpfwjxxbV3();
        }
    }

    public DataGridModel<SpglXmspsxpfwjxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglXmspsxpfwjxxbV3>()
            {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxpfwjxxbV3> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getPfwjbt())) {
                        sUtil.like("pfwjbt", dataBean.getPfwjbt());
                    }
                    if (dataBean.getSjsczt() != null) {
                        sUtil.eq("sjsczt", dataBean.getSjsczt().toString());
                    }
                    if (StringUtil.isNotBlank(xzqhdm)) {
                        sUtil.eq("xzqhdm", xzqhdm);
                    }
                    if (StringUtil.isNotBlank(gcdm)) {
                        sUtil.eq("gcdm", gcdm);
                    }
                    if (StringUtil.isNotBlank(spsxslbm)) {
                        sUtil.eq("spsxslbm", spsxslbm);
                    }
                    // 数据有效
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // 去除草稿
                    sUtil.nq("IFNULL(sync,0)", "-1");
                    PageData<SpglXmspsxpfwjxxbV3> pageData = iSpglXmspsxpfwjxxbv3.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxpfwjxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxpfwjxxbV3 bean : list) {
                            // 本地校验失败
                            if (bean.getSjsczt() == -1) {
                                bean.put("sjscztText", "本地校验失败");
                            }
                            else {
                                bean.put("sjscztText", codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                        bean.getSjsczt().toString()));
                            }
                        }
                    }
                    return list;
                }

            };
        }
        return model;
    }

    // 上报状态
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSbztModel() {
        if (sbztModel == null) {
            sbztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
            sbztModel.add(new SelectItem("-1", "本地校验失败"));
        }
        return this.sbztModel;
    }

    public SpglXmspsxpfwjxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmspsxpfwjxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
