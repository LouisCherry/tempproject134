package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*;
import com.epoint.xmz.thirdreporteddata.common.GxhSpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 项目基本信息表list页面对应的后台
 *
 * @author fwang
 * @version [版本号, 2010-07-05 15:59:26]
 */
@RestController("spglxmspsxblxxblistv3action")
@Scope("request")
public class SpglXmspsxblxxbListV3Action extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxblxxbV3 iSpglXmspsxblxxbv3;
    @Autowired
    private ISpglsplcjdsxxxb iSpglsplcjdsxxxb;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private ISpglCommon ispglcommon;

    @Autowired
    private ISpglspsxjbxxb iSpglspsxjbxxb;

    @Autowired
    ISpglXmspsxblxxxxbV3 iSpglXmspsxblxxxxbV3;

    @Autowired
    ISpglsqcljqtfjxxbV3 iSpglsqcljqtfjxxbV3;

    @Autowired
    ISpglXmspsxpfwjxxbV3 iSpglXmspsxpfwjxxbV3;
    @Autowired
    ISpglXmspsxzqyjxxbV3 iSpglXmspsxzqyjxxbV3;
    @Autowired
    ISpglXmspsxbltbcxxxbV3 iSpglXmspsxbltbcxxxbV3;
    @Autowired
    ISpglZrztxxbV3Service iSpglZrztxxbV3Service;
    @Autowired
    ISpglXmdtxxbV3Service iSpglXmdtxxbV3Service;

    @Autowired
    ISpglGzcnzbjjgxxbV3Service iSpglGzcnzbjjgxxbV3Service;

    /**
     * 项目基本信息表实体对象
     */
    private SpglXmspsxblxxbV3 dataBean;

    private String shsxmc;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglXmspsxblxxbV3> model;

    private List<SelectItem> sbztModel;

    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new SpglXmspsxblxxbV3();
        }

    }

    public DataGridModel<SpglXmspsxblxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglXmspsxblxxbV3>()
            {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxblxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String xzqhdm = getRequestParameter("xzqhdm");
                    String gcdm = getRequestParameter("gcdm");
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", xzqhdm);
                    sUtil.eq("gcdm", gcdm);
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // 不是草稿状态
                    sUtil.nq("IFNULL(sync,0)", "-1");
                    PageData<SpglXmspsxblxxbV3> pageData = iSpglXmspsxblxxbv3.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxblxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxblxxbV3 bean : list) {
                            // 本地校验失败
                            if (bean.getSjsczt() == -1) {
                                bean.put("sjscztText", "本地校验失败");
                            }
                            else {
                                bean.put("sjscztText", codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                        bean.getSjsczt().toString()));
                            }
                            setSpsxmcSpsx(bean);
                            // 是否存在异常数据
                            bean.set("failedlc", ispglcommon.hasFailedRecord(SpglXmspsxblxxxxbV3.class, "spsxslbm",
                                    bean.getSpsxslbm()));
                            bean.set("failedyj", ispglcommon.hasFailedRecord(SpglXmspsxzqyjxxbV3.class, "spsxslbm",
                                    bean.getSpsxslbm()));
                            bean.set("failedpf", ispglcommon.hasFailedRecord(SpglXmspsxpfwjxxbV3.class, "spsxslbm",
                                    bean.getSpsxslbm()));
                            bean.set("failedfj", ispglcommon.hasFailedRecord(SpglsqcljqtfjxxbV3.class, "spsxslbm",
                                    bean.getSpsxslbm()));
                            bean.set("failedtb", ispglcommon.hasFailedRecord(SpglXmspsxbltbcxxxbV3.class, "spsxslbm",
                                    bean.getSpsxslbm()));
                            bean.set("faileddw",
                                    ispglcommon.hasFailedRecord(SpglZrztxxbV3.class, "spsxslbm", bean.getSpsxslbm()));
                            bean.set("faileddt",
                                    ispglcommon.hasFailedRecord(SpglXmdtxxbV3.class, "spsxslbm", bean.getSpsxslbm()));

                            // 根据审批事项编码查出业务信息表单
                            SqlConditionUtil sqlC = new SqlConditionUtil();
                            sqlC.eq("xzqhdm", bean.get("xzqhdm"));
                            sqlC.eq("spsxbm", bean.get("spsxbm"));
                            sqlC.eq("spsxbbh", bean.get("spsxbbh") == null ? null : bean.get("spsxbbh").toString());
                            List<SpglSpsxjbxxb> result = iSpglspsxjbxxb.getListByCondition(sqlC.getMap()).getResult();
                            if (ValidateUtil.isNotBlankCollection(result)) {
                                // bean.set("handleYwxxUrl", GxhSpConstant.sxMap.get(result.get(0).getDybzspsxbm()));
                                bean.set("handleYwxxUrl",
                                        GxhSpConstant.objectMap.get(result.get(0).getDybzspsxbm()).getFormadress());
                            }

                        }
                    }
                    return list;
                }

            };
        }
        return model;
    }

    private void setSpsxmcSpsx(Record info) {
        SqlConditionUtil sqlC = new SqlConditionUtil();
        sqlC.eq("xzqhdm", info.get("xzqhdm"));
        sqlC.eq("spsxbm", info.get("spsxbm"));
        sqlC.eq("spsxbbh", info.get("spsxbbh") == null ? null : info.get("spsxbbh").toString());
        sqlC.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
        sqlC.nq("IFNULL(sync,0)", "-1");
        List<SpglSpsxjbxxb> result = iSpglspsxjbxxb.getListByCondition(sqlC.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(result)) {
            info.set("spsxmc", result.get(0).getSpsxmc());
        }
    }

    // 审批流程类型
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSbztModel() {
        if (sbztModel == null) {
            sbztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
            sbztModel.add(new SelectItem("-1", "本地校验失败"));
        }
        return this.sbztModel;
    }

    public void rePush() {
        List<SpglXmspsxblxxbV3> spglXmspsxblxxlist = model.getWrappedData(true);
        for (SpglXmspsxblxxbV3 spglXmspsxblxxbV3 : spglXmspsxblxxlist) {
            // 错误进行数据质检验证
            if (GxhSpConstant.SPGL_SJSCZT_ERR == spglXmspsxblxxbV3.getSjsczt()) {
                SpglXmspsxblxxbV3 spglXmspsxblxxbV3new = (SpglXmspsxblxxbV3) spglXmspsxblxxbV3.clone();
                ispglcommon.editToPushData(spglXmspsxblxxbV3, spglXmspsxblxxbV3new, true);
                // 如果新数据为正常，则同时推送子表
                if (GxhSpConstant.SPGL_SJSCZT_ERR != spglXmspsxblxxbV3new.getSjsczt()) {
                    rePushProject(spglXmspsxblxxbV3new);
                }
            }
        }
        addCallbackParam("msg", "重推成功！");
    }

    public void rePushProject(SpglXmspsxblxxbV3 spglXmspsxblxxbV3new) {
        SqlConditionUtil sUtil = new SqlConditionUtil();
        sUtil.eq("spsxslbm", spglXmspsxblxxbV3new.getSpsxslbm());
        sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
        sUtil.eq("sjsczt", String.valueOf(GxhSpConstant.SPGL_SJSCZT_ERR));
        sUtil.nq("ifnull(sync,0)", "-1");
        iSpglXmspsxblxxxxbV3.getListByCondition(sUtil.getMap()).getResult().forEach(a -> {
            rePushEntity(a);
        });
        iSpglsqcljqtfjxxbV3.getListByCondition(sUtil.getMap()).getResult().forEach(a -> {
            rePushEntity(a);
        });
        iSpglXmspsxpfwjxxbV3.getListByCondition(sUtil.getMap()).getResult().forEach(a -> {
            rePushEntity(a);
        });
        iSpglXmspsxzqyjxxbV3.getListByCondition(sUtil.getMap()).getResult().forEach(a -> {
            rePushEntity(a);
        });
        iSpglXmspsxbltbcxxxbV3.getListByCondition(sUtil.getMap()).getResult().forEach(a -> {
            rePushEntity(a);
        });
        iSpglZrztxxbV3Service.getListByCondition(sUtil.getMap()).getResult().forEach(a -> {
            rePushEntity(a);
        });
        iSpglXmdtxxbV3Service.getListByCondition(sUtil.getMap()).getResult().forEach(a -> {
            rePushEntity(a);
        });
        sUtil.eq("spsxbm", dataBean.getSpsxslbm());
        sUtil.getMap().remove("spsxslbm" + "#zwfw#" + "eq" + "#zwfw#" + "S");
        iSpglGzcnzbjjgxxbV3Service.getListByCondition(sUtil.getMap()).getResult().forEach(a -> {
            rePushEntity(a);
        });
    }

    public void rePushEntity(BaseEntity record) {
        BaseEntity newrecord = (BaseEntity) record.clone();
        ispglcommon.editToPushData(record, newrecord, true);
    }

    public SpglXmspsxblxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmspsxblxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

    public String getShsxmc() {
        return shsxmc;
    }

    public void setShsxmc(String shsxmc) {
        this.shsxmc = shsxmc;
    }

}
