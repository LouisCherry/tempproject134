package com.epoint.expert.expertcompanyavoid.action;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.expert.expertcompanyavoid.api.entity.ExpertCompanyavoid;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;

/**
 * 专家回避单位表新增页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:37:07]
 */
@RightRelation(ExpertCompanyavoidListAction.class)
@RestController("expertcompanyavoidaddaction")
@Scope("request")
public class ExpertCompanyavoidAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 6275821964851667413L;
    @Autowired
    private IExpertCompanyavoidService service;
    /**
     * 专家回避单位表实体对象
     */
    private ExpertCompanyavoid dataBean = null;
    
    public void pageLoad() {
        dataBean = new ExpertCompanyavoid();
        // 取出该专家的rowguid
        String expertguid = getRequestParameter("rowguid");
        addViewData("expertguid", expertguid);
        addCallbackParam("expertguid", expertguid);
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setExpertguid(getViewData("expertguid"));
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new ExpertCompanyavoid();
    }

    public ExpertCompanyavoid getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertCompanyavoid();
        }
        return dataBean;
    }

    public void setDataBean(ExpertCompanyavoid dataBean) {
        this.dataBean = dataBean;
    }

}
