package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglKcsjryxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglKcsjryxxbV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 勘察设计人员信息表新增页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-08 18:25:06]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglKcsjryxxbV3ListAction.class)
@RestController("spglkcsjryxxbv3addaction")
@Scope("request")
public class SpglKcsjryxxbV3AddAction extends BaseController
{
    @Autowired
    private ISpglKcsjryxxbV3Service service;
    @Autowired
    private IDantiInfoV3Service dantiInfoV3Service;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    /**
     * 勘察设计人员信息表实体对象
     */
    private SpglKcsjryxxbV3 dataBean = null;

    // 下拉框组件Model
    private List<SelectItem> ryzylxModel;

    // 下拉框组件Model
    private List<SelectItem> dantiModel;

    private String xzqhdm;
    private String gcdm;
    private String xmdm;
    private String stywbh;

    public void pageLoad() {
        xzqhdm = getRequestParameter("xzqhdm");
        gcdm = getRequestParameter("gcdm");
        xmdm = getRequestParameter("xmdm");
        stywbh = getRequestParameter("stywbh");
        dataBean = new SpglKcsjryxxbV3();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setXmdm(xmdm);
        dataBean.setXzqhdm(xzqhdm);
        dataBean.setGcdm(gcdm);
        dataBean.setStywbh(stywbh);
        dataBean.setDfsjzj(UUID.randomUUID().toString());
        dataBean.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
        dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new SpglKcsjryxxbV3();
    }

    // 单体
    @SuppressWarnings("unchecked")
    public List<SelectItem> getdantiModel() {
        List<SelectItem> selectItems = new ArrayList<>();
        // 查询当前业务信息所对应的单体编码
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        AuditRsItemBaseinfo result = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByItemcode(gcdm).getResult();
        if (result != null) {
            sqlConditionUtil.eq("itemguid", result.getRowguid());
            List<DantiInfoV3> dantiInfoListByCondition = dantiInfoV3Service.getDantiInfoListByCondition(
                    sqlConditionUtil.getMap());
            if (EpointCollectionUtils.isNotEmpty(dantiInfoListByCondition)) {
                for (DantiInfoV3 dantiInfoV3 : dantiInfoListByCondition) {
                    SelectItem selectItem = new SelectItem();
                    selectItem.setText(dantiInfoV3.getDtmc());
                    selectItem.setValue(dantiInfoV3.getDtbm());
                    selectItems.add(selectItem);
                }
                dantiModel = selectItems;
            }
        }

        return this.dantiModel;
    }

    // 人员专业类型
    @SuppressWarnings("unchecked")
    public List<SelectItem> getRyzylxModel() {
        if (ryzylxModel == null) {
            ryzylxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_人员专业类型", null, false));
        }
        return this.ryzylxModel;
    }

    public SpglKcsjryxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglKcsjryxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglKcsjryxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
