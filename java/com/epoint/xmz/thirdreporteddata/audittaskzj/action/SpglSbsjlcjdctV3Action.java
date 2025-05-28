package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglspjdxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspjdxxb;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController("spglsbsjlcjdctv3action")
@Scope("request")
public class SpglSbsjlcjdctV3Action extends BaseController
{

    private static final long serialVersionUID = 8796953926146877276L;

    transient Logger log = LogUtil.getLog(SpglSbsjlcjdctV3Action.class);

    /**
     * 流程信息实体对象
     */
    private Spglspjdxxb dataBean;

    @Autowired
    private ISpglspjdxxb iSpglspjdxxb;

    @Autowired
    private ISpglCommon ispglcommon;

    private List<SelectItem> sbztModel = null;
    private List<SelectItem> splclxModel = null;
    private List<SelectItem> sjyxbsModel = null;
    private List<SelectItem> lcbsxlxModel = null;

    private String rowguid;

    @Override
    public void pageLoad() {
        rowguid = getRequestParameter("rowguid");
        dataBean = iSpglspjdxxb.find(rowguid);
        if (dataBean != null) {
            addCallbackParam("sjsczt", dataBean.getSjsczt().toString());
            addCallbackParam("sbyy", StringUtil.isNotBlank(dataBean.getSbyy()) ? dataBean.getSbyy() : "无");
            addCallbackParam("sync", dataBean.getStr("sync"));
        }
    }

    public void reSyncBusiness() {
        // 事务控制
        String msg = "上报成功！";
        try {
            EpointFrameDsManager.begin(null);

            Spglspjdxxb oldDataBean = iSpglspjdxxb.find(rowguid);

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

    public Spglspjdxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new Spglspjdxxb();
        }
        return dataBean;
    }

    public void setDataBean(Spglspjdxxb dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSbztModel() {
        if (sbztModel == null) {
            sbztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
            sbztModel.add(new SelectItem("-1", "本地校验失败"));
        }
        return this.sbztModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSplclxModel() {
        if (splclxModel == null) {
            splclxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_审批流程类型", null, false));
        }
        return this.splclxModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSjyxbsModel() {
        if (sjyxbsModel == null) {
            sjyxbsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据有效标识", null, false));
        }
        return this.sjyxbsModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getLcbsxlxModel() {
        if (lcbsxlxModel == null) {
            lcbsxlxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_里程碑事项类型", null,
                            false));
        }
        return this.lcbsxlxModel;
    }
}
