package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 区域评估事项信息表修改页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 14:21:45]
 */
@RestController("spglqypgsxxxbeditaction")
@Scope("request")
public class SpglQypgsxxxbEditAction extends BaseController {

    @Autowired
    private ISpglQypgsxxxbService service;

    /**
     * 区域评估事项信息表实体对象
     */
    private SpglQypgsxxxb dataBean = null;

    private String cliengguid;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglQypgsxxxb();
        }
        cliengguid = dataBean.getCliengguid();
        if (StringUtil.isBlank(getViewData("cliengguid"))) {
            if (StringUtil.isBlank(cliengguid)) {
                cliengguid = UUID.randomUUID().toString();
            }
            addViewData("cliengguid", cliengguid);
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public SpglQypgsxxxb getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglQypgsxxxb dataBean) {
        this.dataBean = dataBean;
    }

}
