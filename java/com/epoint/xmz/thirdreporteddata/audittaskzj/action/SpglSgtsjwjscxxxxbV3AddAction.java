package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSgtsjwjscxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSgtsjwjscxxxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 施工图设计文件审查详细信息表新增页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-08 18:24:01]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglSgtsjwjscxxxxbV3ListAction.class)
@RestController("spglsgtsjwjscxxxxbv3addaction")
@Scope("request")
public class SpglSgtsjwjscxxxxbV3AddAction extends BaseController
{
    @Autowired
    private ISpglSgtsjwjscxxxxbV3Service service;
    /**
     * 施工图设计文件审查详细信息表实体对象
     */
    private SpglSgtsjwjscxxxxbV3 dataBean = null;

    private String xzqhdm;
    private String gcdm;
    private String xmdm;
    private String stywbh;

    public void pageLoad() {
        dataBean = new SpglSgtsjwjscxxxxbV3();
        xzqhdm = getRequestParameter("xzqhdm");
        gcdm = getRequestParameter("gcdm");
        xmdm = getRequestParameter("xmdm");
        stywbh = getRequestParameter("stywbh");
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
        dataBean = new SpglSgtsjwjscxxxxbV3();
    }

    public SpglSgtsjwjscxxxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglSgtsjwjscxxxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglSgtsjwjscxxxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
