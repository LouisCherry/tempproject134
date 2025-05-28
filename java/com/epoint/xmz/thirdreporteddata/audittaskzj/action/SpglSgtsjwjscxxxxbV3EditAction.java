package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSgtsjwjscxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSgtsjwjscxxxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 施工图设计文件审查详细信息表修改页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-08 18:24:01]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglSgtsjwjscxxxxbV3ListAction.class)
@RestController("spglsgtsjwjscxxxxbv3editaction")
@Scope("request")
public class SpglSgtsjwjscxxxxbV3EditAction extends BaseController
{

    @Autowired
    private ISpglSgtsjwjscxxxxbV3Service service;

    /**
     * 施工图设计文件审查详细信息表实体对象
     */
    private SpglSgtsjwjscxxxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglSgtsjwjscxxxxbV3();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public SpglSgtsjwjscxxxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglSgtsjwjscxxxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
