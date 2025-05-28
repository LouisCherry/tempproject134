package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.domain.SpglDfxmsplcxxb;
import com.epoint.basic.spgl.inter.*;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjbxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjbxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接口申请方信息表修改页面对应的后台
 *
 * @author 95453
 * @version [版本号, 2019-06-20 14:31:29]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglXmjbxxbListV3Action.class)
@RestController("spglxmjbxxbeditv3action")
@Scope("request")
public class SpglXmjbxxbEditV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmjbxxbV3 service;
    @Autowired
    private ISpglCommon ispglcommon;
    @Autowired
    private ISpglDfxmsplcxxb iSpglDfxmsplcxxb;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private ISpglXmdwxxb iSpglXmdwxxb;
    @Autowired
    private ISpglXmspsxpfwjxxb iSpglXmspsxpfwjxxb;
    @Autowired
    private ISpglXmqtfjxxb iSpglXmqtfjxxb;
    @Autowired
    private ISpglXmspsxbltbcxxxb iSpglXmspsxbltbcxxxb;
    @Autowired
    private ICodeItemsService icodeitemsservice;

    // 下拉框组件Model
    private List<SelectItem> xmtzlyModel;
    private List<SelectItem> tdhqfsModel;
    private List<SelectItem> splclxModel;
    private List<SelectItem> lxlxModel;
    private List<SelectItem> gcflModel;
    private List<SelectItem> jsxzModel;
    private LazyTreeModal9 gbhymodel = null;
    private List<SelectItem> sjscztModel;
    private List<SelectItem> xmzjsxModel;
    private List<SelectItem> tdsfdsjfaModel;
    private List<SelectItem> sfwcqypgModel;
    private List<SelectItem> xmsfwqbjModel;
    private List<SelectItem> splcbbhModel;

    /**
     * 住建部——项目基本信息表实体对象
     */
    private SpglXmjbxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);

        if (dataBean != null) {
            addCallbackParam("sjsczt", dataBean.getSjsczt().toString());
            addCallbackParam("sbyy", StringUtil.isNotBlank(dataBean.getSbyy()) ? dataBean.getSbyy() : "无");
            addCallbackParam("sync", dataBean.getStr("sync"));

            int sjsczt = dataBean.getSjsczt();
            String sjscztText = "";
            if (sjsczt == -1) {
                sjscztText = "本地校验失败";
            }
            else {
                sjscztText = codeItemsService.getItemTextByCodeName("国标_数据上传状态", String.valueOf(sjsczt));
            }
            addCallbackParam("sjscztText", sjscztText);
            // 解决打开直接保存double被js转成int的问题
            if (dataBean.getSplcbbh() != null) {
                dataBean.set("splcbbhStr", dataBean.getSplcbbh().toString());
            }
            if (StringUtil.isNotBlank(dataBean.getGbhy())) {
                String gbhyText = icodeitemsservice.getItemTextByCodeName("国标行业2017", dataBean.getGbhy());
                addCallbackParam("gbhy", gbhyText);
            }
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        // 事务控制
        String msg = "上报成功！";
        String rowguid = getRequestParameter("guid");
        try {
            EpointFrameDsManager.begin(null);
            // 先把splcbbhStr得值放入splcbbh
            dataBean.setSplcbbh(Double.parseDouble(dataBean.get("splcbbhStr")));
            dataBean.set("operatedate", new Date());
            SpglXmjbxxbV3 oldDataBean = service.find(rowguid);
            ispglcommon.editToPushData(oldDataBean, dataBean, true);
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            msg = "上报失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("msg", msg);
            EpointFrameDsManager.close();
        }
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
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_工程分类", null, false));
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

    public LazyTreeModal9 getGbhyModel() {
        if (gbhymodel == null) {
            gbhymodel = CodeModalFactory.factory("下拉单选树", "国标行业2017", null, false);
        }
        return gbhymodel;
    }

    // 审批流程版本号
    public List<SelectItem> getSplcbbhModel() {
        if (splcbbhModel == null) {
            splcbbhModel = new ArrayList<SelectItem>();
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("xzqhdm", dataBean.getXzqhdm());
            sUtil.eq("splcbm", dataBean.getSplcbm());
            sUtil.setOrderDesc("splcbbh");
            List<SpglDfxmsplcxxb> lcList = iSpglDfxmsplcxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(lcList)) {
                for (SpglDfxmsplcxxb lcxxb : lcList) {
                    splcbbhModel.add(new SelectItem(lcxxb.getSplcbbh().toString(), lcxxb.getSplcbbh().toString()));
                }
            }
        }
        return this.splcbbhModel;
    }

    // 数据上传状态
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSjscztModel() {
        if (sjscztModel == null) {
            sjscztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
        }
        return this.sjscztModel;
    }

    public SpglXmjbxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmjbxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
