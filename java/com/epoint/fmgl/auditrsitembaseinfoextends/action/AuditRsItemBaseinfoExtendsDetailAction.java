package com.epoint.fmgl.auditrsitembaseinfoextends.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.IAuditRsItemBaseinfoExtendsService;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;
import com.epoint.fmgl.tzxmmuqd.api.ITzxmmuqdService;
import com.epoint.fmgl.tzxmmuqd.api.entity.Tzxmmuqd;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 赋码项目基本信息表详情页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2020-09-23 15:19:11]
 */
@RightRelation(AuditRsItemBaseinfoExtendsListAction.class)
@RestController("auditrsitembaseinfoextendsdetailaction")
@Scope("request")
public class AuditRsItemBaseinfoExtendsDetailAction extends BaseController
{
    @Autowired
    private IAuditRsItemBaseinfoExtendsService service;
    @Autowired
    private ITzxmmuqdService tzxmmuqdservice;
    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 赋码项目基本信息表实体对象
     */
    private AuditRsItemBaseinfoExtends dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfoExtends();
        }
        else {
            String xzqhdm = dataBean.getXzqhdm();
            String permitindustry = dataBean.getPermitindustry();
            String projecttype = dataBean.getProjecttype();
            Tzxmmuqd tzxmmuqd = tzxmmuqdservice.selectTzxmmuqd(xzqhdm, permitindustry, projecttype);
            if (StringUtil.isNotBlank(tzxmmuqd)) {
                String mlmc = tzxmmuqd.getMlmc();
                dataBean.setPermititemcode(mlmc);
            }
            String permitindustryname = codeItemsService.getItemTextByCodeName("省发改_投资项目行业分类", permitindustry);
            addCallbackParam("permitindustryname", permitindustryname);
            if("境外投资".equals(permitindustryname)) {
                String investmentmode = dataBean.getInvestmentmode();
                if(StringUtil.isNotBlank(investmentmode)) {
                    addCallbackParam("investmentmodecode", investmentmode);
                }
            }else if("外商投资".equals(permitindustryname)) {
                String investmentmode2 = dataBean.getInvestmentmode2();
                if(StringUtil.isNotBlank(investmentmode2)) {
                    addCallbackParam("investmentmodecode2", investmentmode2);
                }
            }
            
        }

    }

    public AuditRsItemBaseinfoExtends getDataBean() {
        return dataBean;
    }
}
