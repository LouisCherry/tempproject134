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
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxbltbcxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxbltbcxxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 项目审批事项办理特别程序信息表list页面对应的后台
 *
 * @author lc
 * @version [版本号, 2010-08-07 15:59:26]
 */
@RestController("spglxmspsxtbcxxxblistv3action")
@Scope("request")
public class SpglXmspsxtbcxxxbListV3Action extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxbltbcxxxbV3 service;
    @Autowired
    private ICodeItemsService codeItemsService;
    /**
     * 实体对象
     */
    private SpglXmspsxbltbcxxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglXmspsxbltbcxxxbV3> model;
    private List<SelectItem> sbztModel;
    private List<SelectItem> tbcxModel;
    private List<SelectItem> tbcxsxlxModel;

    private String xzqhdm;
    private String gcdm;
    private String spsxslbm;

    public void pageLoad() {
        xzqhdm = getRequestParameter("xzqhdm");
        gcdm = getRequestParameter("gcdm");
        spsxslbm = getRequestParameter("spsxslbm");
        if (dataBean == null) {
            dataBean = new SpglXmspsxbltbcxxxbV3();
        }
    }

    public DataGridModel<SpglXmspsxbltbcxxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglXmspsxbltbcxxxbV3>()
            {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxbltbcxxxbV3> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getTbcxmc())) {
                        sUtil.like("tbcxmc", dataBean.getTbcxmc());
                    }
                    if (dataBean.getSjsczt() != null) {
                        sUtil.eq("sjsczt", dataBean.getSjsczt().toString());
                    }
                    if (dataBean.getTbcx() != null) {
                        sUtil.eq("tbcx", dataBean.getTbcx().toString());
                    }
                    if (dataBean.getTbcxsxlx() != null) {
                        sUtil.eq("tbcxsxlx", dataBean.getTbcxsxlx().toString());
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
                    PageData<SpglXmspsxbltbcxxxbV3> pageData = service.getAllByPage(sUtil.getMap(), first, pageSize,
                            sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxbltbcxxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxbltbcxxxbV3 bean : list) {
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

    // 特别程序类型
    @SuppressWarnings("unchecked")
    public List<SelectItem> getTbcxModel() {
        if (tbcxModel == null) {
            tbcxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_特别程序类型", null, false));
        }
        return this.tbcxModel;
    }

    // 特别程序时限类型
    @SuppressWarnings("unchecked")
    public List<SelectItem> getTbcxsxlxModel() {
        if (tbcxsxlxModel == null) {
            tbcxsxlxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_时限类型", null, false));
        }
        return this.tbcxsxlxModel;
    }

    public SpglXmspsxbltbcxxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmspsxbltbcxxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
