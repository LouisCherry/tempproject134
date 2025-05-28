package com.epoint.zoucheng.znsb.auditznsbcommentpeople.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.zoucheng.znsb.auditznsbcommentpeople.domain.ZCAuditZnsbCommentPeople;
import com.epoint.zoucheng.znsb.auditznsbcommentpeople.inter.IZCAuditZnsbCommentPeopleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 工作台评价窗口人员记录表详情页面对应的后台
 * 
 * @author chencong
 * @version [版本号, 2020-04-01 16:23:17]
 */
@RightRelation(ZCAuditZnsbCommentPeopleListAction.class)
@RestController("zcauditznsbcommentpeopledetailaction")
@Scope("request")
public class ZCAuditZnsbCommentPeopleDetailAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = -3738174241434157651L;

    @Autowired
    private IZCAuditZnsbCommentPeopleService service;
    @Autowired
    private IAuditOrgaServiceCenter centerservice;
    @Autowired
    private IOuService ouservice;
    @Autowired
    private IAuditOrgaWindowYjs auditorgawindow;
    @Autowired
    private ICodeItemsService itemservice;
    /**
     * 工作台评价窗口人员记录表实体对象
     */
    private ZCAuditZnsbCommentPeople dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if(StringUtil.isNotBlank(dataBean.getCenterguid())){
            dataBean.setCenterguid(centerservice.findAuditServiceCenterByGuid(dataBean.getCenterguid()).getResult().getCentername());
        }
        if(StringUtil.isNotBlank(dataBean.getOuguid())){
            dataBean.setOuguid(ouservice.getOuByOuGuid(dataBean.getOuguid()).getOuname());
        }
        if(StringUtil.isNotBlank(dataBean.getWindowguid())){
            dataBean.setWindowguid(auditorgawindow.getWindowByWindowGuid(dataBean.getWindowguid()).getResult().getWindowname());
        }
        if(StringUtil.isNotBlank(dataBean.getSatisfiedtext())){
            dataBean.setSatisfiedtext(itemservice.getItemTextByCodeName("工作台评价人员满意度", dataBean.getSatisfiedtext()));
        }
        if (dataBean == null) {
            dataBean = new ZCAuditZnsbCommentPeople();
        }
    }

    public ZCAuditZnsbCommentPeople getDataBean() {
        return dataBean;
    }
}
