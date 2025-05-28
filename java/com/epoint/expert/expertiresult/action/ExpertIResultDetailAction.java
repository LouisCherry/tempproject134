package com.epoint.expert.expertiresult.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.expert.expertiresult.api.IExpertIResultService;
import com.epoint.expert.expertiresult.api.entity.ExpertIResult;

/**
 * 专家抽取结果表详情页面对应的后台
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:42:03]
 */
@RestController("expertiresultdetailaction")
@Scope("request")
public class ExpertIResultDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IExpertIResultService service;

    /**
     * 专家抽取结果表实体对象
     */
    private ExpertIResult dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ExpertIResult();
        }
    }

    public ExpertIResult getDataBean() {
        return dataBean;
    }
}
