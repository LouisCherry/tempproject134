package com.epoint.expert.experttask.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.expert.experttask.api.entity.ExpertTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.expert.experttask.api.IExpertTaskService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 专家关联事项表详情页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:57]
 */
@RightRelation(ExpertTaskListAction.class)
@RestController("experttaskdetailaction")
@Scope("request")
public class ExpertTaskDetailAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = 8331853768454756768L;

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

    public ExpertTask getDataBean() {
        return dataBean;
    }
}
