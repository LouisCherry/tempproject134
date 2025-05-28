package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 项目申报页面对应的后台
 *
 * @author fwang
 * @version [版本号, 2010-07-16 15:59:26]
 */
@RestController("spglxmregisterv3action")
@Scope("request")
public class SpglXmRegisterV3Action extends BaseController
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
    private ISpglXmspsxzqyjxxbV3 iSpglXmspsxzqyjxxb;
    @Autowired
    private ISpglXmspsxpfwjxxbV3 iSpglXmspsxpfwjxxb;
    @Autowired
    private ISpglsqcljqtfjxxbV3 iSpglXmqtfjxxb;
    @Autowired
    private ISpglXmspsxbltbcxxxbV3 iSpglXmspsxbltbcxxxb;
    @Autowired
    private ISpglsplcjdsxxxb iSpglsplcjdsxxxb;
    @Autowired
    private ISpglsplcxxb iSpglsplcxxb;
    @Autowired
    private ICodeItemsService icodeitemsservice;
    @Autowired
    private ISpglspsxjbxxb iSpglspsxjbxxb;
    @Autowired
    private ISpglXmdtxxbV3Service spglXmdtxxbV3Service;

    /**
     * 项目基本信息表实体对象
     */
    private SpglXmjbxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglZrztxxbV3> spglZrztxxbModel;
    private DataGridModel<SpglXmdtxxbV3> XmdtxxbV3Model;
    private DataGridModel<SpglXmspsxblxxbV3> spglXmspsxblxxbModel;
    private DataGridModel<SpglXmspsxblxxxxbV3> spglXmspsxblxxxxbModel;
    private DataGridModel<SpglXmspsxzqyjxxbV3> spglXmspsxzqyjxxbModel;
    private DataGridModel<SpglXmspsxpfwjxxbV3> spglXmspsxpfwjxxbModel;
    private DataGridModel<SpglsqcljqtfjxxbV3> spglXmqtfjxxbModel;
    private DataGridModel<SpglXmspsxbltbcxxxbV3> spglXmspsxbltbcxxxbModel;

    // 下拉框组件Model
    private List<SelectItem> xmtzlyModel;
    private List<SelectItem> tdhqfsModel;
    private List<SelectItem> splclxModel;
    private List<SelectItem> lxlxModel;
    private List<SelectItem> gcflModel;
    private List<SelectItem> jsxzModel;
    private LazyTreeModal9 gbhyModel;
    private List<SelectItem> xmzjsxModel;
    private List<SelectItem> tdsfdsjfaModel;
    private List<SelectItem> sfwcqypgModel;
    private List<SelectItem> xmsfwqbjModel;
    private List<SelectItem> sbztModel;
    private List<SelectItem> splcbbhModel;

    private String xmjbxxguid;

    public void pageLoad() {
        xmjbxxguid = getRequestParameter("xmjbxxguid");
        dataBean = iSpglXmjbxxb.find(xmjbxxguid);
        if (dataBean == null) {
            dataBean = new SpglXmjbxxbV3();
        }
        addCallbackParam("sjsczt", dataBean.getSjsczt());
        String sbyy = "无";
        if (StringUtil.isNotBlank(dataBean.getSbyy())) {
            if (dataBean.getSbyy().length() > 80) {
                sbyy = dataBean.getSbyy().substring(0, 80) + "...";
            }
            else {
                sbyy = dataBean.getSbyy();
            }
        }
        addCallbackParam("sbyy", sbyy);
        // 解决打开直接保存double被js转成int的问题
        if (dataBean.getSplcbbh() != null) {
            dataBean.set("splcbbhStr", dataBean.getSplcbbh().toString());
        }
        if (StringUtil.isNotBlank(dataBean.getGbhy())) {
            String gbhyText = icodeitemsservice.getItemTextByCodeName("国标行业2017", dataBean.getGbhy());
            addCallbackParam("gbhy", gbhyText);
        }
    }

    public DataGridModel<SpglZrztxxbV3> getDanweiDataGridData() {
        // 获得表格对象
        if (spglZrztxxbModel == null) {
            spglZrztxxbModel = new DataGridModel<SpglZrztxxbV3>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglZrztxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isBlank(xmjbxxguid)) {
                        return null;
                    }
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                    // sUtil.eq("xmdm", dataBean.getXmdm());
                    sUtil.eq("gcdm", dataBean.getGcdm());
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    sUtil.eq("sync", "-1");
                    PageData<SpglZrztxxbV3> pageData = iSpglXmdwxxb.getAllByPage(sUtil.getMap(), first, pageSize,
                            sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return spglZrztxxbModel;
    }

    public DataGridModel<SpglXmdtxxbV3> getDantiDataGridData() {
        // 获得表格对象
        if (XmdtxxbV3Model == null) {
            XmdtxxbV3Model = new DataGridModel<SpglXmdtxxbV3>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmdtxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isBlank(xmjbxxguid)) {
                        return null;
                    }
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                    // sUtil.eq("xmdm", dataBean.getXmdm());
                    sUtil.eq("gcdm", dataBean.getGcdm());
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // sUtil.eq("sync", "-1");
                    PageData<SpglXmdtxxbV3> pageData = spglXmdtxxbV3Service.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return XmdtxxbV3Model;
    }

    public DataGridModel<SpglXmspsxblxxbV3> getBanjianDataGridData() {
        // 获得表格对象
        if (spglXmspsxblxxbModel == null) {
            spglXmspsxblxxbModel = new DataGridModel<SpglXmspsxblxxbV3>()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxblxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isBlank(xmjbxxguid)) {
                        return null;
                    }
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                    sUtil.eq("gcdm", dataBean.getGcdm());
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // sUtil.eq("sync", "-1");
                    PageData<SpglXmspsxblxxbV3> pageData = iSpglXmspsxblxxb.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxblxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxblxxbV3 info : list) {
                            setSpsxmcSpsx(info);
                        }
                    }
                    return list;
                }
            };
        }
        return spglXmspsxblxxbModel;
    }

    public DataGridModel<SpglXmspsxblxxxxbV3> getLiuchengDataGridData() {
        // 获得表格对象
        if (spglXmspsxblxxxxbModel == null) {
            spglXmspsxblxxxxbModel = new DataGridModel<SpglXmspsxblxxxxbV3>()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxblxxxxbV3> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    if (StringUtil.isBlank(xmjbxxguid)) {
                        return null;
                    }
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                    sUtil.eq("gcdm", dataBean.getGcdm());
                    sUtil.setOrderDesc("blsj");
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // sUtil.eq("sync", "-1");

                    PageData<SpglXmspsxblxxxxbV3> pageData = iSpglXmspsxblxxxxb.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxblxxxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxblxxxxbV3 info : list) {
                            setSpsxmcSxbj(info);
                        }
                    }
                    return list;
                }

            };
        }
        return spglXmspsxblxxxxbModel;
    }

    public DataGridModel<SpglXmspsxzqyjxxbV3> getYijianDataGridData() {
        // 获得表格对象
        if (spglXmspsxzqyjxxbModel == null) {
            spglXmspsxzqyjxxbModel = new DataGridModel<SpglXmspsxzqyjxxbV3>()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxzqyjxxbV3> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    if (StringUtil.isBlank(xmjbxxguid)) {
                        return null;
                    }
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                    sUtil.eq("gcdm", dataBean.getGcdm());
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // sUtil.eq("sync", "-1");

                    PageData<SpglXmspsxzqyjxxbV3> pageData = iSpglXmspsxzqyjxxb.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxzqyjxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxzqyjxxbV3 info : list) {
                            setSpsxmcSxbj(info);
                        }
                    }
                    return list;
                }
            };
        }
        return spglXmspsxzqyjxxbModel;
    }

    public DataGridModel<SpglXmspsxpfwjxxbV3> getPifuDataGridData() {
        // 获得表格对象
        if (spglXmspsxpfwjxxbModel == null) {
            spglXmspsxpfwjxxbModel = new DataGridModel<SpglXmspsxpfwjxxbV3>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxpfwjxxbV3> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    if (StringUtil.isBlank(xmjbxxguid)) {
                        return null;
                    }
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                    sUtil.eq("gcdm", dataBean.getGcdm());
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // sUtil.eq("sync", "-1");

                    PageData<SpglXmspsxpfwjxxbV3> pageData = iSpglXmspsxpfwjxxb.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxpfwjxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxpfwjxxbV3 info : list) {
                            setSpsxmcSxbj(info);
                        }
                    }
                    return list;
                }
            };
        }
        return spglXmspsxpfwjxxbModel;
    }

    public DataGridModel<SpglsqcljqtfjxxbV3> getFujianDataGridData() {
        // 获得表格对象
        if (spglXmqtfjxxbModel == null) {
            spglXmqtfjxxbModel = new DataGridModel<SpglsqcljqtfjxxbV3>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglsqcljqtfjxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isBlank(xmjbxxguid)) {
                        return null;
                    }
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                    sUtil.eq("gcdm", dataBean.getGcdm());
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // sUtil.eq("sync", "-1");

                    PageData<SpglsqcljqtfjxxbV3> pageData = iSpglXmqtfjxxb.getAllByPage(sUtil.getMap(), first, pageSize,
                            sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglsqcljqtfjxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglsqcljqtfjxxbV3 info : list) {
                            setSpsxmcSxbj(info);
                        }
                    }
                    return list;
                }
            };
        }
        return spglXmqtfjxxbModel;
    }

    public DataGridModel<SpglXmspsxbltbcxxxbV3> getTbcxDataGridData() {
        // 获得表格对象
        if (spglXmspsxbltbcxxxbModel == null) {
            spglXmspsxbltbcxxxbModel = new DataGridModel<SpglXmspsxbltbcxxxbV3>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxbltbcxxxbV3> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    if (StringUtil.isBlank(xmjbxxguid)) {
                        return null;
                    }
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                    sUtil.eq("gcdm", dataBean.getGcdm());
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // sUtil.eq("sync", "-1");

                    PageData<SpglXmspsxbltbcxxxbV3> pageData = iSpglXmspsxbltbcxxxb.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxbltbcxxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxbltbcxxxbV3 info : list) {
                            setSpsxmcSxbj(info);
                        }
                    }
                    return list;
                }
            };
        }
        return spglXmspsxbltbcxxxbModel;
    }

    public void save() {
        String msg = "修改成功！";
        boolean isValidSuccess = true;
        try {
            // 先把splcbbhStr得值放入splcbbh
            dataBean.setSplcbbh(Double.parseDouble(dataBean.get("splcbbhStr")));

            if (dataBean.getSjsczt() != 0) {
                String[] sbyys = dataBean.getSbyy().split(";");
                List<String> list = new ArrayList<String>();
                if (sbyys != null && sbyys.length > 0) {
                    for (String sbyy : sbyys) {
                        list.add(sbyy);
                    }
                }
                if (ValidateUtil.isNotBlankCollection(list)) {
                    if (isInCode("项目投资来源", dataBean.getXmtzly(), true)) {
                        if (list.contains("项目投资来源的值不在代码项之中！")) {
                            list.remove("项目投资来源的值不在代码项之中！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (isInCode("土地获取方式", dataBean.getTdhqfs(), true)) {
                        if (list.contains("土地获取方式的值不在代码项之中！")) {
                            list.remove("土地获取方式的值不在代码项之中！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (isInCode("土地是否带设计方案", dataBean.getTdsfdsjfa(), true)) {
                        if (list.contains("土地是否带设计方案的值不在代码项之中！")) {
                            list.remove("土地是否带设计方案的值不在代码项之中！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (isInCode("是否完成区域评估", dataBean.getSfwcqypg(), true)) {
                        if (list.contains("是否完成区域评估的值不在代码项之中！")) {
                            list.remove("是否完成区域评估的值不在代码项之中！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (isInCode("审批流程类型", dataBean.getSplclx(), true)) {
                        if (list.contains("审批流程类型的值不在代码项之中！")) {
                            list.remove("审批流程类型的值不在代码项之中！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (isInCode("建设性质", dataBean.getJsxz(), true)) {
                        if (list.contains("建设性质的值不在代码项之中！")) {
                            list.remove("建设性质的值不在代码项之中！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (!isNull(dataBean.getNkgsj())) {
                        if (list.contains("拟开工时间为空！")) {
                            list.remove("拟开工时间为空！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (!isNull(dataBean.getNjcsj())) {
                        if (list.contains("拟建成时间为空！")) {
                            list.remove("拟建成时间为空！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (!isNull(dataBean.getZtze())) {
                        if (list.contains("总投资额为空！")) {
                            list.remove("总投资额为空！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (!isNull(dataBean.getJsdd())) {
                        if (list.contains("建设地点为空！")) {
                            list.remove("建设地点为空！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (!isNull(dataBean.getYdmj())) {
                        if (list.contains("用地面积为空！")) {
                            list.remove("用地面积为空！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }

                    if (!isNull(dataBean.getJzmj())) {
                        if (list.contains("建筑面积为空！")) {
                            list.remove("建筑面积为空！");
                        }
                    }
                    else {
                        isValidSuccess = false;
                    }
                }
                if (isValidSuccess && EpointCollectionUtils.isEmpty(list)) {
                    dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    dataBean.setSbyy(null);
                }
                else {
                    dataBean.setSbyy(String.join(";", list));
                }
            }

            // 先更新项目信息
            iSpglXmjbxxb.update(dataBean);
            // 数据库该项目信息是否已校验成功,成功把因项目校验失败的单位 其他附件 批复文件 的失败原因中的项目失败删去
            if (iSpglXmjbxxb.isExistGcdm(dataBean.getGcdm())) {
                // 项目下相关的所有有效草稿数据
                SqlConditionUtil sUtil = new SqlConditionUtil();
                sUtil.eq("xzqhdm", dataBean.getXzqhdm());
                sUtil.eq("gcdm", dataBean.getGcdm());
                sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                sUtil.eq("sync", "-1");

                // 项目单位信息校验
                List<SpglZrztxxbV3> dwList = iSpglXmdwxxb.getListByCondition(sUtil.getMap()).getResult();
                if (ValidateUtil.isNotBlankCollection(dwList)) {
                    for (SpglZrztxxbV3 info : dwList) {
                        if (info.getSjsczt() != 0) {
                            info.setSbyy(null);
                            info.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            iSpglXmdwxxb.update(info);
                        }
                    }
                }
                // 其他附件校验
                List<SpglsqcljqtfjxxbV3> fjList = iSpglXmqtfjxxb.getListByCondition(sUtil.getMap()).getResult();
                if (ValidateUtil.isNotBlankCollection(fjList)) {
                    for (SpglsqcljqtfjxxbV3 info : fjList) {
                        if (info.getSjsczt() != 0) {
                            // 先查询失败原因，是否包含两个报错
                            String[] sbyys = info.getSbyy().split(";");
                            if (sbyys != null) {
                                // 如果失败数为2，则前者gcdm校验删除
                                if (sbyys.length == 2) {
                                    info.setSbyy(sbyys[1]);
                                }
                                else if (sbyys.length == 1) {
                                    String sbyy = sbyys[0];
                                    if (sbyy.contains("工程代码")) {
                                        info.setSbyy(null);
                                        info.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                    }
                                }
                                iSpglXmqtfjxxb.update(info);
                            }
                        }
                    }
                }
                // 批复文件校验
                List<SpglXmspsxpfwjxxbV3> pfList = iSpglXmspsxpfwjxxb.getListByCondition(sUtil.getMap()).getResult();
                if (ValidateUtil.isNotBlankCollection(pfList)) {
                    for (SpglXmspsxpfwjxxbV3 info : pfList) {
                        if (info.getSjsczt() != 0) {
                            // 先查询失败原因，是否包含两个报错
                            String[] sbyys = info.getSbyy().split(";");
                            if (sbyys != null) {
                                // 如果失败数为2，则前者gcdm校验删除
                                if (sbyys.length == 2) {
                                    info.setSbyy(sbyys[1]);
                                }
                                else if (sbyys.length == 1) {
                                    String sbyy = sbyys[0];
                                    if (sbyy.contains("工程代码")) {
                                        info.setSbyy(null);
                                        info.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                    }
                                }
                                iSpglXmspsxpfwjxxb.update(info);
                            }
                        }

                    }
                }
            }
        }
        catch (Exception e) {
            msg = "修改失败！ 失败原因： " + e.getMessage();
            e.printStackTrace();
        }
        finally {
            addCallbackParam("msg", msg);
            EpointFrameDsManager.close();
        }
    }

    public void dataSubmit() {
        // 推送主项
        if (dataBean.getXmdm().equals(dataBean.getGcdm())) {
            // 查询子项推送
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("xmdm", dataBean.getXmdm());
            sql.eq("xzqhdm", dataBean.getXzqhdm());
            sql.nq("gcdm", dataBean.getXmdm());
            sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
            sql.eq("sync", "-1");
            List<SpglXmjbxxbV3> lists = iSpglXmjbxxb.getListByCondition(sql.getMap()).getResult();
            for (SpglXmjbxxbV3 spglxmjbxxb : lists) {
                dataSubmitByRowguid(spglxmjbxxb.getRowguid(), false);
            }
        }
        dataSubmitByRowguid(xmjbxxguid, true);
    }

    /**
     * 上报
     */
    public void dataSubmitByRowguid(String xmjbxxguid, boolean needsave) {
        String msg = "上报成功！";
        String errorType = "0";
        boolean isValidSuccess = true;
        // 项目报错信息
        List<String> xmErrorList = new ArrayList<>();
        // 项目单位报错信息
        List<String> xmdwErrorList = new ArrayList<>();
        // 审批事项办理报错信息
        List<String> bjErrorList = new ArrayList<>();
        // 审批事项办理详细报错信息
        List<String> bjxxErrorList = new ArrayList<>();
        // 批复文件报错信息
        List<String> pfErrorList = new ArrayList<>();
        // 其他附件报错信息
        List<String> fjErrorList = new ArrayList<>();
        // 事项征求意见报错信息
        List<String> zqErrorList = new ArrayList<>();

        try {
            // 先保存提交，然后再上报开启事务
            if (needsave) {
                save();
            }
            EpointFrameDsManager.begin(null);

            // 项目基本信息
            SpglXmjbxxbV3 xmInfo = iSpglXmjbxxb.find(xmjbxxguid);
            if (xmInfo.getSjsczt() == -1) {
                String[] sbyys = xmInfo.getSbyy().split(";");
                if (sbyys != null && sbyys.length > 0) {
                    isValidSuccess = false;
                    for (String sbyy : sbyys) {
                        xmErrorList.add(sbyy);
                    }
                }
            }
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
                    if (info.getSjsczt() == -1) {
                        isValidSuccess = false;
                        xmdwErrorList.add(info.getDwmc() + "--" + info.getSbyy());
                    }
                    info.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                    iSpglXmdwxxb.update(info);
                }
            }

            // 审批事项办理信息
            List<SpglXmspsxblxxbV3> bjList = iSpglXmspsxblxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(bjList)) {
                for (SpglXmspsxblxxbV3 info : bjList) {
                    if (info.getSjsczt() == -1) {
                        isValidSuccess = false;
                        bjErrorList.add(info.getSpsxbm() + "--" + info.getSbyy());
                    }
                    info.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                    iSpglXmspsxblxxb.update(info);
                    // 审批事项实例编码
                    sUtil.eq("spsxslbm", info.getSpsxslbm());

                    // 审批事项办理详细信息
                    List<SpglXmspsxblxxxxbV3> bjxxList = iSpglXmspsxblxxxxb.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(bjxxList)) {
                        for (SpglXmspsxblxxxxbV3 info1 : bjxxList) {
                            if (info1.getSjsczt() == -1) {
                                isValidSuccess = false;
                                bjxxErrorList.add(info1.getSpsxslbm() + "--" + info1.getSbyy());
                            }
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxblxxxxb.update(info1);
                        }
                    }

                    // 审批事项征求信息
                    List<SpglXmspsxzqyjxxbV3> zqList = iSpglXmspsxzqyjxxb.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(zqList)) {
                        for (SpglXmspsxzqyjxxbV3 info1 : zqList) {
                            if (info1.getSjsczt() == -1) {
                                isValidSuccess = false;
                                zqErrorList.add(info1.getSpsxslbm() + "--" + info1.getSbyy());
                            }
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxzqyjxxb.update(info1);
                        }
                    }

                    // 审批事项批复文件信息
                    List<SpglXmspsxpfwjxxbV3> pfList = iSpglXmspsxpfwjxxb.getListByCondition(sUtil.getMap())
                            .getResult();
                    if (ValidateUtil.isNotBlankCollection(pfList)) {
                        for (SpglXmspsxpfwjxxbV3 info1 : pfList) {
                            if (info1.getSjsczt() == -1) {
                                isValidSuccess = false;
                                pfErrorList.add(info1.getSpsxslbm() + "--" + info1.getSbyy());
                            }
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmspsxpfwjxxb.update(info1);
                        }
                    }

                    // 审批事项其他附件信息
                    List<SpglsqcljqtfjxxbV3> fjList = iSpglXmqtfjxxb.getListByCondition(sUtil.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(fjList)) {
                        for (SpglsqcljqtfjxxbV3 info1 : fjList) {
                            if (info1.getSjsczt() == -1) {
                                isValidSuccess = false;
                                fjErrorList.add(info1.getSpsxslbm() + "--" + info1.getSbyy());
                            }
                            info1.set("sync", ZwfwConstant.CONSTANT_STR_ZERO);
                            iSpglXmqtfjxxb.update(info1);
                        }
                    }
                }
            }
            EpointFrameDsManager.commit();
            if (!isValidSuccess) {
                EpointFrameDsManager.rollback();
                errorType = "1";
                // 把所有的数据作为json传过去
                JSONObject json = new JSONObject();
                json.put("xmErrorList", xmErrorList);
                json.put("xmdwErrorList", xmdwErrorList);
                json.put("bjErrorList", bjErrorList);
                json.put("bjxxErrorList", bjxxErrorList);
                json.put("pfErrorList", pfErrorList);
                json.put("fjErrorList", fjErrorList);
                json.put("zqErrorList", zqErrorList);
                addCallbackParam("json", json.toJSONString());
            }

        }
        catch (Exception e) {
            errorType = "2";
            msg = "上报异常！ 原因： " + e.getMessage();
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("errorType", errorType);
            addCallbackParam("msg", msg);
            EpointFrameDsManager.close();
        }
    }

    private void setSpsxmcSpsx(Record info) {
        SqlConditionUtil sqlC = new SqlConditionUtil();
        sqlC.eq("xzqhdm", info.get("xzqhdm"));
        //        sqlC.eq("splcbm", info.get("splcbm"));
        //        sqlC.eq("splcbbh", info.get("splcbbh") == null ? null : info.get("splcbbh").toString());
        sqlC.eq("spsxbm", info.get("spsxbm"));
        sqlC.eq("spsxbbh", info.get("spsxbbh") == null ? null : info.get("spsxbbh").toString());
        sqlC.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
        sqlC.nq("IFNULL(sync,0)", "-1");
        List<SpglSpsxjbxxb> result = iSpglspsxjbxxb.getListByCondition(sqlC.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(result)) {
            info.set("spsxmc", result.get(0).getSpsxmc());
        }

    }

    private void setSpsxmcSpsx(Record info, Record info2) {
        SqlConditionUtil sqlC = new SqlConditionUtil();
        sqlC.eq("xzqhdm", info.get("xzqhdm"));
        //        sqlC.eq("splcbm", info.get("splcbm"));
        //        sqlC.eq("splcbbh", info.get("splcbbh") == null ? null : info.get("splcbbh").toString());
        sqlC.eq("spsxbm", info.get("spsxbm"));
        sqlC.eq("spsxbbh", info.get("spsxbbh") == null ? null : info.get("spsxbbh").toString());
        sqlC.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
        sqlC.nq("IFNULL(sync,0)", "-1");
        List<SpglSpsxjbxxb> result = iSpglspsxjbxxb.getListByCondition(sqlC.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(result)) {
            info2.set("spsxmc", result.get(0).getSpsxmc());
        }
    }

    private void setSpsxmcSxbj(Record info) {
        SqlConditionUtil sqlC = new SqlConditionUtil();
        sqlC.eq("xzqhdm", info.get("xzqhdm"));
        sqlC.eq("gcdm", info.get("gcdm"));
        sqlC.eq("spsxslbm", info.get("spsxslbm"));
        sqlC.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
        // sqlC.eq("sync", "-1");

        List<SpglXmspsxblxxbV3> list2 = iSpglXmspsxblxxb.getListByCondition(sqlC.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(list2)) {
            setSpsxmcSpsx(list2.get(0), info);
        }

    }

    // 审批流程版本号
    public List<SelectItem> getSplcbbhModel() {
        if (splcbbhModel == null) {
            splcbbhModel = new ArrayList<SelectItem>();
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("xzqhdm", dataBean.getXzqhdm());
            sUtil.eq("splcbm", dataBean.getSplcbm());
            sUtil.setOrderDesc("splcbbh");
            List<Spglsplcxxb> lcList = iSpglsplcxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(lcList)) {
                for (Spglsplcxxb lcxxb : lcList) {
                    splcbbhModel.add(new SelectItem(lcxxb.getSplcbbh().toString(), lcxxb.getSplcbbh().toString()));
                }
            }
        }
        return this.splcbbhModel;
    }

    // 上报状态
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSbztModel() {
        if (sbztModel == null) {
            sbztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
        }
        return this.sbztModel;
    }

    // 项目投资来源
    @SuppressWarnings("unchecked")
    public List<SelectItem> getXmtzlyModel() {
        if (xmtzlyModel == null) {
            xmtzlyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_项目投资来源", null, false));
        }
        return this.xmtzlyModel;
    }

    // 土地获取方式
    @SuppressWarnings("unchecked")
    public List<SelectItem> getTdhqfsModel() {
        if (tdhqfsModel == null) {
            tdhqfsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_土地获取方式", null, false));
        }
        return this.tdhqfsModel;
    }

    // 土地是否带设计方案
    @SuppressWarnings("unchecked")
    public List<SelectItem> getTdsfdsjfaModel() {
        if (tdsfdsjfaModel == null) {
            tdsfdsjfaModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_土地是否带设计方案", null,
                            false));
        }
        return this.tdsfdsjfaModel;
    }

    // 是否完成区域评估
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSfwcqypgModel() {
        if (sfwcqypgModel == null) {
            sfwcqypgModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_是否完成区域评估", null,
                            false));
        }
        return this.sfwcqypgModel;
    }

    // 项目是否完全办结
    @SuppressWarnings("unchecked")
    public List<SelectItem> getXmsfwqbjModel() {
        if (xmsfwqbjModel == null) {
            xmsfwqbjModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_项目是否完全办结", null,
                            false));
        }
        return this.xmsfwqbjModel;
    }

    // 审批流程类型
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSplclxModel() {
        if (splclxModel == null) {
            splclxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_审批流程类型", null, false));
        }
        return this.splclxModel;
    }

    // 立项类型
    @SuppressWarnings("unchecked")
    public List<SelectItem> getLxlxModel() {
        if (lxlxModel == null) {
            lxlxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_立项类型", null, false));
        }
        return this.lxlxModel;
    }

    // 工程分类
    @SuppressWarnings("unchecked")
    public List<SelectItem> getGcflModel() {
        if (gcflModel == null) {
            gcflModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_工程行业分类", null, false));
        }
        return this.gcflModel;
    }

    // 建设性质
    @SuppressWarnings("unchecked")
    public List<SelectItem> getJsxzModel() {
        if (jsxzModel == null) {
            jsxzModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_建设性质", null, false));
        }
        return this.jsxzModel;
    }

    // 项目资金属性
    @SuppressWarnings("unchecked")
    public List<SelectItem> getXmzjsxModel() {
        if (xmzjsxModel == null) {
            xmzjsxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_项目资金属性", null, false));
        }
        return this.xmzjsxModel;
    }

    // 国标行业
    public LazyTreeModal9 getGbhyModel() {
        if (gbhyModel == null) {
            gbhyModel = CodeModalFactory.factory("下拉单选树", "国标行业2017", null, false);
        }
        return gbhyModel;
    }

    /**
     * @param codename
     *         主项名称
     * @param value
     *         子项值
     * @param a
     *         是否必须
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isInCode(String codename, Object value, boolean a) {
        if (value == null) {
            return false;
        }
        String v = value.toString();
        codename = "国标_" + codename;
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        return StringUtil.isNotBlank(s);
    }

    public boolean isNull(Object o) {
        return o == null;
    }

    public SpglXmjbxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmjbxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
