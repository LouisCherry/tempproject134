package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.domain.SpglDfxmsplcxxb;
import com.epoint.basic.spgl.inter.*;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目审批事项办理信息表修改页面对应的后台
 *
 * @author 95453
 * @version [版本号, 2019-06-20 14:31:29]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglXmspsxblxxbListV3Action.class)
@RestController("spglxmspsxblxxbeditv3action")
@Scope("request")
public class SpglXmspsxblxxbEditV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxblxxbV3 service;
    @Autowired
    private ISpglDfxmsplcxxb iSpglDfxmsplcxxb;
    @Autowired
    private ISpglDfxmsplcjdsxxxb iSpglDfxmsplcjdsxxxb;
    @Autowired
    private ISpglXmspsxzqyjxxb iSpglXmspsxzqyjxxb;
    @Autowired
    private ISpglXmspsxblxxxxb iSpglXmspsxblxxxxb;
    @Autowired
    private ISpglXmqtfjxxb iSpglXmqtfjxxb;
    @Autowired
    private ISpglXmspsxpfwjxxb iSpglXmspsxpfwjxxb;
    @Autowired
    private ISpglXmspsxbltbcxxxb iSpglXmspsxbltbcxxxb;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private ISpglCommon ispglcommon;

    // 下拉框组件Model
    private List<SelectItem> gkfsMode;
    private List<SelectItem> slfsMode;
    private List<SelectItem> shifouModel;
    private List<SelectItem> splcbbhModel;

    /**
     * 实体对象
     */
    private SpglXmspsxblxxbV3 dataBean = null;

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
            SpglXmspsxblxxbV3 oldDataBean = service.find(rowguid);

            ispglcommon.editToPushData(oldDataBean, dataBean, true);
        }
        catch (

                Exception e) {
            msg = "上报失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("msg", msg);
            EpointFrameDsManager.close();
        }
    }

    // 受理方式
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSlfsModel() {
        if (slfsMode == null) {
            slfsMode = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_受理方式", null, false));
        }
        return this.slfsMode;
    }

    // 公开方式
    @SuppressWarnings("unchecked")
    public List<SelectItem> getGkfsModel() {
        if (gkfsMode == null) {
            gkfsMode = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_公开方式", null, false));
        }
        return this.gkfsMode;
    }

    // 是否
    @SuppressWarnings("unchecked")
    public List<SelectItem> getShifouModel() {
        if (shifouModel == null) {
            shifouModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据有效标识", null, false));
        }
        return this.shifouModel;
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

    public void getSpsxbbh(String splcbbh) {
        SqlConditionUtil sUtil = new SqlConditionUtil();
        sUtil.eq("xzqhdm", dataBean.getXzqhdm());
        sUtil.eq("splcbm", dataBean.getSplcbm());
        sUtil.eq("spsxbm", dataBean.getSpsxbm());
        sUtil.eq("splcbbh", splcbbh);
        sUtil.setOrderDesc("spsxbbh");
        List<SpglDfxmsplcjdsxxxb> list = iSpglDfxmsplcjdsxxxb.getListByCondition(sUtil.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(list)) {
            SpglDfxmsplcjdsxxxb info = list.get(0);
            if (info != null) {
                addCallbackParam("spsxbbh", info.getSpsxbbh());
            }
        }
    }

    public SpglXmspsxblxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmspsxblxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
