package com.epoint.expert.expertcompanyavoid.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;
import com.epoint.expert.expertcompanyavoid.api.entity.ExpertCompanyavoid;

/**
 * 专家回避单位表修改页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:37:07]
 */
@RightRelation(ExpertCompanyavoidListAction.class)
@RestController("expertcompanyavoideditaction")
@Scope("request")
public class ExpertCompanyavoidEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 980752144633382663L;

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

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public ExpertCompanyavoid getDataBean() {
        return dataBean;
    }

    public void setDataBean(ExpertCompanyavoid dataBean) {
        this.dataBean = dataBean;
    }

}
