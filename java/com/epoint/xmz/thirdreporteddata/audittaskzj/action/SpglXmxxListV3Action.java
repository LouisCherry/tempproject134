package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController("spglxmxxlistv3action")
@Scope("request")
public class SpglXmxxListV3Action extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmjbxxbV3 iSpglXmjbxxb;
    @Autowired
    private ISpglZrztxxbV3Service iSpglXmdwxxb;
    @Autowired
    private ISpglXmspsxblxxbV3 iSpglXmspsxblxxb;
    @Autowired
    private ISpglXmspsxblxxxxbV3 iSpglXmspsxblxxxxb;
    @Autowired
    private ISpglsqcljqtfjxxbV3 iSpglsqcljqtfjxxbV3;
    @Autowired
    private ISpglXmspsxpfwjxxbV3 iSpglXmspsxpfwjxxb;
    @Autowired
    private ISpglXmspsxzqyjxxbV3 iSpglXmspsxzqyjxxb;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 项目基本信息表实体对象
     */
    private SpglXmjbxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglXmjbxxbV3> model;

    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new SpglXmjbxxbV3();
        }
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        String msg = "成功删除！";
        List<String> select = getDataGridData().getSelectKeys();
        for (String xmguid : select) {
            try {
                EpointFrameDsManager.begin(null);

                SpglXmjbxxbV3 xmInfo = iSpglXmjbxxb.find(xmguid);
                iSpglXmjbxxb.deleteByGuid(xmguid);
                // 级联删除
                // 项目单位信息
                SqlConditionUtil sUtil = new SqlConditionUtil();
                sUtil.eq("xzqhdm", xmInfo.getXzqhdm());
                sUtil.eq("gcdm", xmInfo.getGcdm());
                sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                sUtil.eq("sync", "-1");
                List<SpglZrztxxbV3> dwList = iSpglXmdwxxb.getListByCondition(sUtil.getMap()).getResult();
                if (ValidateUtil.isNotBlankCollection(dwList)) {
                    for (SpglZrztxxbV3 info : dwList) {
                        iSpglXmdwxxb.deleteByGuid(info.getRowguid());
                    }
                }

                // 审批事项办理信息
                List<SpglXmspsxblxxbV3> bjList = iSpglXmspsxblxxb.getListByCondition(sUtil.getMap()).getResult();
                if (ValidateUtil.isNotBlankCollection(bjList)) {
                    for (SpglXmspsxblxxbV3 info : bjList) {
                        // 审批事项实例编码
                        sUtil.eq("spsxslbm", info.getSpsxslbm());

                        iSpglXmspsxblxxb.deleteByGuid(info.getRowguid());

                        // 审批事项办理详细信息
                        List<SpglXmspsxblxxxxbV3> bjxxList = iSpglXmspsxblxxxxb.getListByCondition(sUtil.getMap())
                                .getResult();
                        if (ValidateUtil.isNotBlankCollection(bjxxList)) {
                            for (SpglXmspsxblxxxxbV3 info1 : bjxxList) {
                                iSpglXmspsxblxxxxb.deleteByGuid(info1.getRowguid());
                            }
                        }

                        // 审批事项征求信息
                        List<SpglXmspsxzqyjxxbV3> zqList = iSpglXmspsxzqyjxxb.getListByCondition(sUtil.getMap())
                                .getResult();
                        if (ValidateUtil.isNotBlankCollection(zqList)) {
                            for (SpglXmspsxzqyjxxbV3 info1 : zqList) {
                                iSpglXmspsxzqyjxxb.deleteByGuid(info1.getRowguid());
                            }
                        }

                        // 审批事项批复文件信息
                        List<SpglXmspsxpfwjxxbV3> pfList = iSpglXmspsxpfwjxxb.getListByCondition(sUtil.getMap())
                                .getResult();
                        if (ValidateUtil.isNotBlankCollection(pfList)) {
                            for (SpglXmspsxpfwjxxbV3 info1 : pfList) {
                                iSpglXmspsxpfwjxxb.deleteByGuid(info1.getRowguid());
                            }
                        }

                        // 审批事项其他附件信息
                        List<SpglsqcljqtfjxxbV3> fjList = iSpglsqcljqtfjxxbV3.getListByCondition(sUtil.getMap())
                                .getResult();
                        if (ValidateUtil.isNotBlankCollection(fjList)) {
                            for (SpglsqcljqtfjxxbV3 info1 : fjList) {
                                iSpglsqcljqtfjxxbV3.deleteByGuid(info1.getRowguid());
                            }
                        }
                    }
                }

                EpointFrameDsManager.commit();
            }
            catch (Exception e) {
                msg = "删除失败！";
                e.printStackTrace();
                EpointFrameDsManager.rollback();
            }
            finally {
                addCallbackParam("msg", msg);
                EpointFrameDsManager.close();
            }
        }
    }

    /**
     * 上报
     */
    public void dataSubmit(String rowguid) {
        String msg = "上报成功！";
        try {
            EpointFrameDsManager.begin(null);

            // 项目基本信息
            SpglXmjbxxbV3 xmInfo = iSpglXmjbxxb.find(rowguid);
            xmInfo.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
            iSpglXmjbxxb.update(xmInfo);

            // 项目单位信息
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("xzqhdm", xmInfo.getXzqhdm());
            sUtil.eq("gcdm", xmInfo.getGcdm());
            sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
            sUtil.eq("sync", "-1");
            List<SpglZrztxxbV3> dwList = iSpglXmdwxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(dwList)) {
                for (SpglZrztxxbV3 info : dwList) {
                    info.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                    iSpglXmdwxxb.update(info);
                }
            }

            // 审批事项办理信息
            List<SpglXmspsxblxxbV3> bjList = iSpglXmspsxblxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(bjList)) {
                for (SpglXmspsxblxxbV3 info : bjList) {
                    info.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                    iSpglXmspsxblxxb.update(info);
                    // 审批事项实例编码
                    sUtil.eq("spsxslbm", info.getSpsxslbm());

                    // 审批事项办理详细信息
                    List<SpglXmspsxblxxxxbV3> bjxxList = iSpglXmspsxblxxxxb.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(bjxxList)) {
                        for (SpglXmspsxblxxxxbV3 info1 : bjxxList) {
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxblxxxxb.update(info1);
                        }
                    }

                    // 审批事项征求信息
                    List<SpglXmspsxzqyjxxbV3> zqList = iSpglXmspsxzqyjxxb.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(zqList)) {
                        for (SpglXmspsxzqyjxxbV3 info1 : zqList) {
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxzqyjxxb.update(info1);
                        }
                    }

                    // 审批事项批复文件信息
                    List<SpglXmspsxpfwjxxbV3> pfList = iSpglXmspsxpfwjxxb.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(pfList)) {
                        for (SpglXmspsxpfwjxxbV3 info1 : pfList) {
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxpfwjxxb.update(info1);
                        }
                    }

                    // 审批事项其他附件信息
                    List<SpglsqcljqtfjxxbV3> fjList = iSpglsqcljqtfjxxbV3.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(fjList)) {
                        for (SpglsqcljqtfjxxbV3 info1 : fjList) {
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglsqcljqtfjxxbV3.update(info1);
                        }
                    }
                }
            }

            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            msg = "上报失败！ 原因： " + e.getMessage();
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("msg", msg);
            EpointFrameDsManager.close();
        }
    }

    public DataGridModel<SpglXmjbxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglXmjbxxbV3>()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 5925727181973702200L;

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
                    // 有效数据
                    sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // 草稿状态
                    sql.eq("sync", "-1");
                    PageData<SpglXmjbxxbV3> pageData = iSpglXmjbxxb.getAllByPage(sql.getMap(), first, pageSize,
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
                                sqlc.eq("sync", "-1");
                                //  XMDM!= GCDM的为子项
                                sqlc.nq("gcdm", info.getXmdm());
                                Integer subCount = iSpglXmjbxxb.getCountByCondition(sqlc.getMap()).getResult();
                                if (flag && subCount > 0) {
                                    info.put("isLeaf", false);
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
                    sql.eq("sync", "-1");
                    lists = iSpglXmjbxxb.getListByCondition(sql.getMap()).getResult();
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
                        }
                    }
                    return lists;
                }
            };
        }
        return model;
    }

    public SpglXmjbxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmjbxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
