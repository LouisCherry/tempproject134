package com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.ISpglSpfysxkxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.entity.SpglSpfysxkxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品房预售信息许可表修改页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:51]
 */
@RightRelation(SpglSpfysxkxxbListAction.class)
@RestController("spglspfysxkxxbeditaction")
@Scope("request")
public class SpglSpfysxkxxbEditAction extends BaseController
{

    @Autowired
    private ISpglSpfysxkxxbService service;

    /**
     * 商品房预售信息许可表实体对象
     */
    private SpglSpfysxkxxb dataBean = null;

    /**
     * 数据上传状态下拉列表model
     */
    private List<SelectItem> sjscztModel = null;

    private List<SelectItem> gcjdjdModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglSpfysxkxxb();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setSjsczt(0);
        dataBean.setSjyxbs(1);
        dataBean.set("sync", "0");
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public SpglSpfysxkxxb getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglSpfysxkxxb dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getSjscztModel() {
        if (sjscztModel == null) {
            sjscztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
        }
        return this.sjscztModel;
    }

    public List<SelectItem> getGcjdjdModel() {
        if (gcjdjdModel == null) {
            gcjdjdModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "工程进度节点", null, false));
        }
        return this.gcjdjdModel;
    }

}
