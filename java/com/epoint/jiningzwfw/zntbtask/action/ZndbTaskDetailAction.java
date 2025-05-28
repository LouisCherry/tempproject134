package com.epoint.jiningzwfw.zntbtask.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.jiningzwfw.zntbtask.api.IZndbTaskService;
import com.epoint.jiningzwfw.zntbtask.api.entity.ZndbTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 智能导办办理事项表详情页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2023-09-18 14:43:48]
 */
@RightRelation(ZndbTaskListAction.class)
@RestController("zndbtaskdetailaction")
@Scope("request")
public class ZndbTaskDetailAction extends BaseController {
    @Autowired
    private IZndbTaskService service;

    /**
     * 智能导办办理事项表实体对象
     */
    private ZndbTask dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ZndbTask();
        }

        if ("1".equals(dataBean.getTasktype())) {
            addCallbackParam("showphase", true);
        }

    }


    public ZndbTask getDataBean() {
        return dataBean;
    }
}