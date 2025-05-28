package com.epoint.expert.experttask.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.expert.experttask.api.IExpertTaskService;
import com.epoint.expert.experttask.api.entity.ExpertTask;

/**
 * 专家关联事项表修改页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:57]
 */
@RightRelation(ExpertTaskListAction.class)
@RestController("experttaskeditaction")
@Scope("request")
public class ExpertTaskEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 3906496256364886221L;

    @Autowired
    private IExpertTaskService service;

    /**
     * 专家关联事项表实体对象
     */
    private ExpertTask dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ExpertTask();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public ExpertTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(ExpertTask dataBean) {
        this.dataBean = dataBean;
    }

}
