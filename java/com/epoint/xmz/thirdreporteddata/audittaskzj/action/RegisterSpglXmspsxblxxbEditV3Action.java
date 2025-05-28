package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
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
 * 项目审批事项办理信息表修改页面对应的后台
 *
 * @author 95453
 * @version [版本号, 2019-06-20 14:31:29]
 */
@RestController("registerspglxmspsxblxxbeditv3action")
@Scope("request")
public class RegisterSpglXmspsxblxxbEditV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxblxxbV3 service;
    @Autowired
    private ISpglsplcxxb iSpglDfxmsplcxxb;
    @Autowired
    private ISpglsplcjdsxxxb iSpglDfxmsplcjdsxxxb;
    @Autowired
    private ISpglXmspsxzqyjxxbV3 iSpglXmspsxzqyjxxb;
    @Autowired
    private ISpglXmspsxblxxxxbV3 iSpglXmspsxblxxxxb;
    @Autowired
    private ISpglsqcljqtfjxxbV3 iSpglXmqtfjxxb;
    @Autowired
    private ISpglXmspsxpfwjxxbV3 iSpglXmspsxpfwjxxb;
    @Autowired
    private ICodeItemsService codeItemsService;

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
        // 先把splcbbhStr得值放入splcbbh
        dataBean.setSplcbbh(Double.parseDouble(dataBean.get("splcbbhStr")));

        // 插入前判断一下是否审批流程阶段事项已对应
        if (iSpglDfxmsplcjdsxxxb.isExistSplcSx(dataBean.getSplcbbh(), dataBean.getSplcbm(), dataBean.getSpsxbbh(),
                dataBean.getSpsxbm())) {
            dataBean.setSbyy(null);
            dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
        }

        service.update(dataBean);

        if (service.isExistFlowsn(dataBean.getSpsxslbm())) {
            // 项目下相关的所有有效草稿数据
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("xzqhdm", dataBean.getXzqhdm());
            sUtil.eq("gcdm", dataBean.getGcdm());
            sUtil.eq("spsxslbm", dataBean.getSpsxslbm());
            sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
            sUtil.eq("sync", "-1");

            // 事项征求意见
            List<SpglXmspsxzqyjxxbV3> zqList = iSpglXmspsxzqyjxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(zqList)) {
                for (SpglXmspsxzqyjxxbV3 info : zqList) {
                    if (info.getSjsczt() != 0) {
                        if (service.isExistFlowsn(info.getSpsxslbm())) {
                            info.setSbyy(null);
                            info.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            iSpglXmspsxzqyjxxb.update(info);
                        }
                    }
                }
            }
            // 审批事项办理详细信息
            List<SpglXmspsxblxxxxbV3> blxxList = iSpglXmspsxblxxxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(blxxList)) {
                for (SpglXmspsxblxxxxbV3 info : blxxList) {
                    if (info.getSjsczt() != 0) {
                        if (service.isExistFlowsn(info.getSpsxslbm())) {
                            info.setSbyy(null);
                            info.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            iSpglXmspsxblxxxxb.update(info);
                        }
                    }
                }
            }
            // 其他附件
            List<SpglsqcljqtfjxxbV3> fjList = iSpglXmqtfjxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(fjList)) {
                for (SpglsqcljqtfjxxbV3 info : fjList) {
                    if (info.getSjsczt() != 0) {
                        // 先查询失败原因，是否包含两个报错
                        String[] sbyys = info.getSbyy().split(";");
                        if (sbyys != null) {
                            // 如果失败数为2，则后者审批事项校验删除
                            if (sbyys.length == 2) {
                                info.setSbyy(sbyys[0]);
                            }
                            else if (sbyys.length == 1) {
                                String sbyy = sbyys[0];
                                if (sbyy.contains("审批事项实例编码")) {
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
                            // 如果失败数为2，则后者审批事项校验删除
                            if (sbyys.length == 2) {
                                info.setSbyy(sbyys[0]);
                            }
                            else if (sbyys.length == 1) {
                                String sbyy = sbyys[0];
                                if (sbyy.contains("审批事项实例编码")) {
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

        addCallbackParam("msg", "保存成功");
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
            List<Spglsplcxxb> lcList = iSpglDfxmsplcxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(lcList)) {
                for (Spglsplcxxb lcxxb : lcList) {
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
        List<Spglsplcjdsxxxb> list = iSpglDfxmsplcjdsxxxb.getListByCondition(sUtil.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(list)) {
            Spglsplcjdsxxxb info = list.get(0);
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
