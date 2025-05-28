package com.epoint.expert.expertcompanyavoid.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.expert.expertcompanyavoid.api.entity.ExpertCompanyavoid;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 专家回避单位表详情页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:37:07]
 */
@RightRelation(ExpertCompanyavoidListAction.class)
@RestController("expertcompanyavoiddetailaction")
@Scope("request")
public class ExpertCompanyavoidDetailAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = -9045335079498964065L;

    @Autowired
    private IExpertCompanyavoidService service;

    /**
     * 专家回避单位表实体对象
     */
    private ExpertCompanyavoid dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ExpertCompanyavoid();
        }
    }

    public ExpertCompanyavoid getDataBean() {
        return dataBean;
    }
}
