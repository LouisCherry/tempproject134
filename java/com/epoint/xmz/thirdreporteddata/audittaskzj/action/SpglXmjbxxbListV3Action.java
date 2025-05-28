package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.domain.*;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.inter.ISpglXmdwxxb;
import com.epoint.basic.spgl.inter.ISpglXmspsxblxxb;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjbxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 项目基本信息表list页面对应的后台
 *
 * @author fwang
 * @version [版本号, 2010-07-05 15:59:26]
 */
@RestController("spglxmjbxxblistv3action")
@Scope("request")
public class SpglXmjbxxbListV3Action extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmjbxxbV3 iSpglXmjbxxbV3;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private ISpglXmspsxblxxb iSpglXmspsxblxxb;

    @Autowired
    private ISpglDfxmsplcjdsxxxb iSpglDfxmsplcjdsxxxb;
    @Autowired
    private ISpglXmdwxxb spglXmdwxxbservice;

    @Autowired
    private ISpglCommon ispglcommon;

    /**
     * 项目基本信息表实体对象
     */
    private SpglXmjbxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglXmjbxxbV3> model;

    private List<SelectItem> sbztModel;

    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new SpglXmjbxxbV3();
        }
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            iSpglXmjbxxbV3.deleteByGuid(sel);
            // 删除主项的同时需要删除子项?

        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<SpglXmjbxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglXmjbxxbV3>()
            {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmjbxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 页面上搜索条件
                    boolean flag = true;
                    if (StringUtil.isNotBlank(dataBean.getXmmc())) {
                        sql.like("xmmc", dataBean.getXmmc());
                        flag = false;
                    }
                    if (StringUtil.isNotBlank(dataBean.getXmdm())) {
                        sql.like("xmdm", dataBean.getXmdm());
                        flag = false;
                    }
                    if (dataBean.getSjsczt() != null) {
                        sql.eq("sjsczt", dataBean.getSjsczt().toString());
                        flag = false;
                    }
                    // 有效数据
                    sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // 去除草稿
                    sql.nq("IFNULL(sync,0)", "-1");
                    PageData<SpglXmjbxxbV3> pageData = iSpglXmjbxxbV3.getAllXmByPage(sql.getMap(), first, pageSize,
                            sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmjbxxbV3> returnList = new ArrayList<SpglXmjbxxbV3>();
                    List<SpglXmjbxxbV3> lists = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(lists)) {
                        for (SpglXmjbxxbV3 info : lists) {
                            if (StringUtil.isNotBlank(info.getXmdm()) && info.getXmdm().equals(info.getGcdm())) {
                                info.put("id", info.getRowguid());
                                info.put("pid", TreeFunction9.F9ROOT);
                                info.put("checked", false);
                                info.put("expanded", false);
                                info.put("isLeaf", true);
                                info.put("operatedateText", EpointDateUtil.convertDate2String(info.getOperatedate(),
                                        "yyyy-MM-dd HH:mm:ss"));
                                // 本地校验失败
                                if (info.getSjsczt() == -1) {
                                    info.put("sjscztText", "本地校验失败");
                                }
                                else {
                                    info.put("sjscztText", codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                            info.getSjsczt().toString()));
                                }
                                SqlConditionUtil sqlc = new SqlConditionUtil();
                                sqlc.eq("xmdm", info.getXmdm());
                                sqlc.eq("xzqhdm", info.getXzqhdm());
                                sqlc.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                                sqlc.nq("IFNULL(sync,0)", "-1");
                                //  XMDM!= GCDM的为子项
                                sqlc.nq("gcdm", info.getXmdm());
                                Integer subCount = iSpglXmjbxxbV3.getCountByCondition(sqlc.getMap()).getResult();
                                if (subCount > 0) {
                                    info.put("isLeaf", false);
                                }
                                // 获取项目下的单位（根据 xzqhdm ; gcdm;xmdm), 判断单位重推按钮是否显示红色
                                SqlConditionUtil sqldw = new SqlConditionUtil();
                                sqldw.eq("xzqhdm", info.getXzqhdm());
                                sqldw.eq("gcdm", info.getXmdm());
                                sqldw.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                                // 去除草稿
                                sqldw.nq("IFNULL(sync,0)", "-1");
                                sqldw.in("Sjsczt", "2,-1");
                                List<SpglXmdwxxb> listdw = spglXmdwxxbservice.getListByCondition(sqldw.getMap())
                                        .getResult();
                                if (!listdw.isEmpty()) {
                                    info.put("isdwshowred", 1);
                                }
                                // 获取项目下的办件（根据 xzqhdm ; gcdm), 判断办件重推按钮是否显示红色
                                SqlConditionUtil sUtil = new SqlConditionUtil();
                                sUtil.eq("xzqhdm", info.getXzqhdm());
                                sUtil.eq("gcdm", info.getXmdm());
                                sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                                // 不是草稿状态
                                sUtil.nq("IFNULL(sync,0)", "-1");
                                List<SpglXmspsxblxxb> list = iSpglXmspsxblxxb.getListByCondition(sUtil.getMap())
                                        .getResult();
                                if (ValidateUtil.isNotBlankCollection(list)) {
                                    for (SpglXmspsxblxxb bean : list) {
                                        // 是否存在异常数据
                                        if (ispglcommon.hasFailedRecord(SpglXmspsxblxxxxbV3.class, "spsxslbm",
                                                bean.getSpsxslbm())) {
                                            info.put("isshowred", 1);
                                            break;
                                        }
                                        if (ispglcommon.hasFailedRecord(SpglXmspsxzqyjxxbV3.class, "spsxslbm",
                                                bean.getSpsxslbm())) {
                                            info.put("isshowred", 1);
                                            break;
                                        }
                                        if (ispglcommon.hasFailedRecord(SpglXmspsxpfwjxxbV3.class, "spsxslbm",
                                                bean.getSpsxslbm())) {
                                            info.put("isshowred", 1);
                                            break;
                                        }
                                        if (ispglcommon.hasFailedRecord(SpglsqcljqtfjxxbV3.class, "spsxslbm",
                                                bean.getSpsxslbm())) {
                                            info.put("isshowred", 1);
                                            break;
                                        }
                                        if (ispglcommon.hasFailedRecord(SpglXmspsxbltbcxxxbV3.class, "spsxslbm",
                                                bean.getSpsxslbm())) {
                                            info.put("isshowred", 1);
                                            break;
                                        }
                                    }

                                }
                                returnList.add(info);
                            }
                        }
                    }
                    return returnList;
                }

                @Override
                public List<SpglXmjbxxbV3> fetchChildrenData(JSONObject t) {
                    List<SpglXmjbxxbV3> lists = new ArrayList<SpglXmjbxxbV3>();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("xmdm", t.getString("xmdm"));
                    sql.eq("xzqhdm", t.getString("xzqhdm"));
                    sql.nq("gcdm", t.getString("xmdm"));
                    sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    sql.nq("IFNULL(sync,0)", "-1");
                    lists = iSpglXmjbxxbV3.getListByCondition(sql.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(lists)) {
                        for (SpglXmjbxxbV3 info : lists) {
                            info.put("id", info.getRowguid());
                            info.put("pid", t.get("pid"));
                            info.put("checked", false);
                            info.put("expanded", false);
                            info.put("isLeaf", true);
                            // 本地校验失败
                            if (info.getSjsczt() == -1) {
                                info.put("sjscztText", "本地校验失败");
                            }
                            else {
                                info.put("sjscztText", codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                        info.getSjsczt().toString()));
                            }
                            info.put("operatedateText",
                                    EpointDateUtil.convertDate2String(info.getOperatedate(), "yyyy-MM-dd HH:mm:ss"));
                            // 获取项目下的单位（根据 xzqhdm ; gcdm;xmdm), 判断单位重推按钮是否显示红色
                            SqlConditionUtil sqldw = new SqlConditionUtil();
                            sqldw.eq("xzqhdm", info.getXzqhdm());
                            sqldw.eq("gcdm", info.getXmdm());
                            sqldw.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                            // 去除草稿
                            sqldw.nq("IFNULL(sync,0)", "-1");
                            sqldw.in("Sjsczt", "2,-1");
                            List<SpglXmdwxxb> listdw = spglXmdwxxbservice.getListByCondition(sqldw.getMap())
                                    .getResult();
                            if (!listdw.isEmpty()) {
                                info.put("isdwshowred", 1);
                            }
                            // 获取项目下的办件（根据 xzqhdm ; gcdm), 判断办件重推按钮是否显示红色
                            SqlConditionUtil sUtil = new SqlConditionUtil();
                            sUtil.eq("xzqhdm", info.getXzqhdm());
                            sUtil.eq("gcdm", info.getXmdm());
                            sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                            // 不是草稿状态
                            sUtil.nq("IFNULL(sync,0)", "-1");
                            List<SpglXmspsxblxxb> list = iSpglXmspsxblxxb.getListByCondition(sUtil.getMap())
                                    .getResult();
                            if (ValidateUtil.isNotBlankCollection(list)) {
                                for (SpglXmspsxblxxb bean : list) {
                                    // 是否存在异常数据
                                    if (ispglcommon.hasFailedRecord(SpglXmspsxblxxxxb.class, "spsxslbm",
                                            bean.getSpsxslbm())) {
                                        info.put("isshowred", 1);
                                        break;
                                    }
                                    if (ispglcommon.hasFailedRecord(SpglXmspsxzqyjxxb.class, "spsxslbm",
                                            bean.getSpsxslbm())) {
                                        info.put("isshowred", 1);
                                        break;
                                    }
                                    if (ispglcommon.hasFailedRecord(SpglXmspsxpfwjxxb.class, "spsxslbm",
                                            bean.getSpsxslbm())) {
                                        info.put("isshowred", 1);
                                        break;
                                    }
                                    if (ispglcommon.hasFailedRecord(SpglXmqtfjxxb.class, "spsxslbm",
                                            bean.getSpsxslbm())) {
                                        info.put("isshowred", 1);
                                        break;
                                    }
                                    if (ispglcommon.hasFailedRecord(SpglXmspsxbltbcxxxb.class, "spsxslbm",
                                            bean.getSpsxslbm())) {
                                        info.put("isshowred", 1);
                                        break;
                                    }
                                }

                            }
                        }
                    }
                    return lists;
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

    public SpglXmjbxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmjbxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
