package com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.ISpglSpfxsbaxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.entity.SpglSpfxsbaxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 商品房现售备案信息表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:55]
 */
@RightRelation(SpglSpfxsbaxxbListAction.class)
@RestController("spglspfxsbaxxbaddaction")
@Scope("request")
public class SpglSpfxsbaxxbAddAction extends BaseController {
    @Autowired
    private ISpglSpfxsbaxxbService service;
    /**
     * 商品房现售备案信息表实体对象
     */
    private SpglSpfxsbaxxb dataBean = null;


    public void pageLoad() {
        dataBean = new SpglSpfxsbaxxb();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setDfsjzj(UUID.randomUUID().toString());
        dataBean.setXzqhdm("370800");
        dataBean.setSjsczt(0);
        dataBean.setSjyxbs(1);
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new SpglSpfxsbaxxb();
    }

    public SpglSpfxsbaxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglSpfxsbaxxb();
        }
        return dataBean;
    }

    public void setDataBean(SpglSpfxsbaxxb dataBean) {
        this.dataBean = dataBean;
    }


}
