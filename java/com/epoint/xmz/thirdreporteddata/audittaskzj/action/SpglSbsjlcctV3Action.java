package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcxxb;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController("spglsbsjlcctv3action")
@Scope("request")
public class SpglSbsjlcctV3Action extends BaseController
{

    private static final long serialVersionUID = 8796953926146877276L;

    transient Logger log = LogUtil.getLog(SpglSbsjlcctV3Action.class);

    /**
     * 流程信息实体对象
     */
    private Spglsplcxxb dataBean;

    @Autowired
    private ISpglsplcxxb iSpglsplcxxb;

    @Autowired
    private ISpglCommon ispglcommon;

    private List<SelectItem> sbztModel = null;
    private List<SelectItem> splclxModel = null;
    private List<SelectItem> sjyxbsModel = null;

    private String rowguid;

    @Override
    public void pageLoad() {
        rowguid = getRequestParameter("rowguid");
        dataBean = iSpglsplcxxb.find(rowguid);
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

            Spglsplcxxb oldDataBean = iSpglsplcxxb.find(rowguid);

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

    public Spglsplcxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new Spglsplcxxb();
        }
        return dataBean;
    }

    public void setDataBean(Spglsplcxxb dataBean) {
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
}
