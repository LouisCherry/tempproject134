package com.epoint.zoucheng.znsb.auditznsbcommentmatter.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.domain.ZCAuditZnsbCommentMatter;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.inter.IZCAuditZnsbCommentMatterService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 工作台评价事项记录表详情页面对应的后台
 * 
 * @author chencong
 * @version [版本号, 2020-04-08 13:37:36]
 */
@RightRelation(ZCAuditZnsbCommentMatterListAction.class)
@RestController("zcauditznsbcommentmatterdetailaction")
@Scope("request")
public class ZCAuditZnsbCommentMatterDetailAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = 6028383870879069606L;

    @Autowired
    private IZCAuditZnsbCommentMatterService service;
    @Autowired
    private IAuditOrgaServiceCenter centerservice;
    @Autowired
    private ICodeItemsService itemservice;
    /**
     * 工作台评价事项记录表实体对象
     */
    private ZCAuditZnsbCommentMatter dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if(StringUtil.isNotBlank(dataBean.getCenterguid())){
            dataBean.setCenterguid(centerservice.findAuditServiceCenterByGuid(dataBean.getCenterguid()).getResult().getCentername());
        }
        if(StringUtil.isNotBlank(dataBean.getSatisfiedtext())){
            dataBean.setSatisfiedtext(itemservice.getItemTextByCodeName("工作台评价事项满意度", dataBean.getSatisfiedtext()));
        }
        if (dataBean == null) {
            dataBean = new ZCAuditZnsbCommentMatter();
        }
    }

    public ZCAuditZnsbCommentMatter getDataBean() {
        return dataBean;
    }
}
