package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
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


@RestController("spglxmxxlrlistv3action")
@Scope("request")
public class SpglXmxxlrListV3Action extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmjbxxbV3 iSpglXmjbxxbV3;
    @Autowired
    private ISpglZrztxxbV3Service iSpglZrztxxbV3Service;
    @Autowired
    private ISpglXmspsxblxxbV3 iSpglXmspsxblxxbv3;
    @Autowired
    private ISpglXmspsxblxxxxbV3 iSpglXmspsxblxxxxbv3;
    @Autowired
    private ISpglsqcljqtfjxxbV3 iSpglXmqtfjxxb;
    @Autowired
    private ISpglXmspsxpfwjxxbV3 iSpglXmspsxpfwjxxbv3;
    @Autowired
    private ISpglXmspsxzqyjxxbV3 iSpglXmspsxzqyjxxbv3;

    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

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

                SpglXmjbxxbV3 xmInfo = iSpglXmjbxxbV3.find(xmguid);
                iSpglXmjbxxbV3.deleteByGuid(xmguid);
                // TODO 项目删除-issendzj从1回复为null（加条件可更为仅区县辖区的删除issendzj从1回复为null）
                AuditRsItemBaseinfo itemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(
                        xmInfo.getDfsjzj()).getResult();
                if (itemBaseinfo != null) {
                    itemBaseinfo.setIssendzj(null);
                    iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(itemBaseinfo);
                }

                // 级联删除
                // 项目单位信息
                SqlConditionUtil sUtil = new SqlConditionUtil();
                sUtil.eq("xzqhdm", xmInfo.getXzqhdm());
                sUtil.eq("gcdm", xmInfo.getGcdm());
                sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                sUtil.eq("sync", "-1");
                List<SpglZrztxxbV3> dwList = iSpglZrztxxbV3Service.getListByCondition(sUtil.getMap()).getResult();
                if (ValidateUtil.isNotBlankCollection(dwList)) {
                    for (SpglZrztxxbV3 info : dwList) {
                        iSpglZrztxxbV3Service.deleteByGuid(info.getRowguid());
                    }
                }

                // 审批事项办理信息
                List<SpglXmspsxblxxbV3> bjList = iSpglXmspsxblxxbv3.getListByCondition(sUtil.getMap()).getResult();
                if (ValidateUtil.isNotBlankCollection(bjList)) {
                    for (SpglXmspsxblxxbV3 info : bjList) {
                        // 审批事项实例编码
                        sUtil.eq("spsxslbm", info.getSpsxslbm());

                        iSpglXmspsxblxxbv3.deleteByGuid(info.getRowguid());

                        // 审批事项办理详细信息
                        List<SpglXmspsxblxxxxbV3> bjxxList = iSpglXmspsxblxxxxbv3.getListByCondition(sUtil.getMap())
                                .getResult();
                        if (ValidateUtil.isNotBlankCollection(bjxxList)) {
                            for (SpglXmspsxblxxxxbV3 info1 : bjxxList) {
                                iSpglXmspsxblxxxxbv3.deleteByGuid(info1.getRowguid());
                            }
                        }

                        // 审批事项征求信息
                        List<SpglXmspsxzqyjxxbV3> zqList = iSpglXmspsxzqyjxxbv3.getListByCondition(sUtil.getMap())
                                .getResult();
                        if (ValidateUtil.isNotBlankCollection(zqList)) {
                            for (SpglXmspsxzqyjxxbV3 info1 : zqList) {
                                iSpglXmspsxzqyjxxbv3.deleteByGuid(info1.getRowguid());
                            }
                        }

                        // 审批事项批复文件信息
                        List<SpglXmspsxpfwjxxbV3> pfList = iSpglXmspsxpfwjxxbv3.getListByCondition(sUtil.getMap())
                                .getResult();
                        if (ValidateUtil.isNotBlankCollection(pfList)) {
                            for (SpglXmspsxpfwjxxbV3 info1 : pfList) {
                                iSpglXmspsxpfwjxxbv3.deleteByGuid(info1.getRowguid());
                            }
                        }

                        // 审批事项其他附件信息
                        List<SpglsqcljqtfjxxbV3> fjList = iSpglXmqtfjxxb.getListByCondition(sUtil.getMap()).getResult();
                        if (ValidateUtil.isNotBlankCollection(fjList)) {
                            for (SpglsqcljqtfjxxbV3 info1 : fjList) {
                                iSpglXmqtfjxxb.deleteByGuid(info1.getRowguid());
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
            SpglXmjbxxbV3 xmInfo = iSpglXmjbxxbV3.find(rowguid);
            xmInfo.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
            iSpglXmjbxxbV3.update(xmInfo);

            // 项目单位信息
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("xzqhdm", xmInfo.getXzqhdm());
            sUtil.eq("gcdm", xmInfo.getGcdm());
            sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
            sUtil.eq("sync", "-1");
            /*
             * List<SpglXmdwxxb> dwList =
             * iSpglXmdwxxb.getListByCondition(sUtil.getMap()).getResult(); if
             * (ValidateUtil.isNotBlankCollection(dwList)) { for (SpglXmdwxxb
             * info : dwList) { info.set("sync",
             * ZwfwConstant.CONSTANT_STR_ZERO); iSpglXmdwxxb.update(info); } }
             */

            // 审批事项办理信息
            List<SpglXmspsxblxxbV3> bjList = iSpglXmspsxblxxbv3.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(bjList)) {
                for (SpglXmspsxblxxbV3 info : bjList) {
                    info.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                    iSpglXmspsxblxxbv3.update(info);
                    // 审批事项实例编码
                    sUtil.eq("spsxslbm", info.getSpsxslbm());

                    // 审批事项办理详细信息
                    List<SpglXmspsxblxxxxbV3> bjxxList = iSpglXmspsxblxxxxbv3.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(bjxxList)) {
                        for (SpglXmspsxblxxxxbV3 info1 : bjxxList) {
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxblxxxxbv3.update(info1);
                        }
                    }

                    // 审批事项征求信息
                    List<SpglXmspsxzqyjxxbV3> zqList = iSpglXmspsxzqyjxxbv3.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(zqList)) {
                        for (SpglXmspsxzqyjxxbV3 info1 : zqList) {
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxzqyjxxbv3.update(info1);
                        }
                    }

                    // 审批事项批复文件信息
                    List<SpglXmspsxpfwjxxbV3> pfList = iSpglXmspsxpfwjxxbv3.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(pfList)) {
                        for (SpglXmspsxpfwjxxbV3 info1 : pfList) {
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxpfwjxxbv3.update(info1);
                        }
                    }

                    // 审批事项其他附件信息
                    List<SpglsqcljqtfjxxbV3> fjList = iSpglXmqtfjxxb.getListByCondition(sUtil.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(fjList)) {
                        for (SpglsqcljqtfjxxbV3 info1 : fjList) {
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmqtfjxxb.update(info1);
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
                    PageData<SpglXmjbxxbV3> pageData = iSpglXmjbxxbV3.getAllByPage(sql.getMap(), first, pageSize,
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
                                // 区县辖区编码 是否存在市级辖区编码
                                if (StringUtil.isNotBlank(info.getSbyy()) && info.getSbyy()
                                        .contains("该主题类型未存在市级主题中！")) {
                                    info.put("areacodeError", "1");
                                }

                                SqlConditionUtil sqlc = new SqlConditionUtil();
                                sqlc.eq("xmdm", info.getXmdm());
                                sqlc.eq("xzqhdm", info.getXzqhdm());
                                sqlc.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                                sqlc.eq("sync", "-1");
                                //  XMDM!= GCDM的为子项
                                sqlc.nq("gcdm", info.getXmdm());
                                Integer subCount = iSpglXmjbxxbV3.getCountByCondition(sqlc.getMap()).getResult();
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
                            // 区县辖区编码 是否存在市级辖区编码
                            if (StringUtil.isNotBlank(info.getSbyy()) && info.getSbyy()
                                    .contains("该主题类型未存在市级主题中！")) {
                                info.set("areacodeError", "1");
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
