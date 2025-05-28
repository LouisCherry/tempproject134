package com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.ISpglSpfxsbaxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.entity.SpglSpfxsbaxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 商品房现售备案信息表修改页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:55]
 */
@RightRelation(SpglSpfxsbaxxbListAction.class)
@RestController("spglspfxsbaxxbeditaction")
@Scope("request")
public class SpglSpfxsbaxxbEditAction extends BaseController {

    @Autowired
    private ISpglSpfxsbaxxbService service;

    /**
     * 商品房现售备案信息表实体对象
     */
    private SpglSpfxsbaxxb dataBean = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglSpfxsbaxxb();
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

    public SpglSpfxsbaxxb getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglSpfxsbaxxb dataBean) {
        this.dataBean = dataBean;
    }

}
