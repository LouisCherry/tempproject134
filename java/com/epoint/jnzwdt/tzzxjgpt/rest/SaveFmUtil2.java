package com.epoint.jnzwdt.tzzxjgpt.rest;

import java.util.Date;
import java.util.UUID;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.IAuditRsItemBaseinfoExtendsService;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;

public class SaveFmUtil2
{

    public static void saveFmbaseinfo(AuditRsItemBaseinfoExtends baseinfo) {
        IAuditRsItemBaseinfoExtendsService Service = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfoExtendsService.class);
        IAuditRsItemBaseinfo itemService = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
        AuditRsItemBaseinfo itemBaseinfo = new AuditRsItemBaseinfo();
        itemBaseinfo.setRowguid(UUID.randomUUID().toString());
        itemBaseinfo.setOperatedate(new Date());
        itemBaseinfo.setOperateusername("赋码申请");
        baseinfo.setXiangmubh(itemBaseinfo.getRowguid());
        // 立项类型
        String projecttype = baseinfo.getProjecttype();
        String LXLX = "";
        switch (projecttype) {
            case "A00001":
                LXLX = "1";
                break;
            case "A00002":
                LXLX = "2";
                break;
            case "A00003":
                LXLX = "3";
                break;
            default:
                LXLX = "1";
                break;
        }
        itemBaseinfo.set("LXLX", LXLX);
        // 建设性质
        String constructper = baseinfo.getConstructper();
        String Constructionproperty = "";
        switch (constructper) {
            case "0":
                Constructionproperty = "1";
                break;
            case "1":
                Constructionproperty = "2";
                break;
            case "2":
                Constructionproperty = "3";
                break;
            case "3":
                Constructionproperty = "4";
                break;
            default:
                Constructionproperty = "5";
                break;
        }
        itemBaseinfo.setConstructionproperty(Constructionproperty);
        // 项目名称
        itemBaseinfo.setItemname(baseinfo.getProjectname());
        // 拟开工时间
        itemBaseinfo.setItemstartdate(baseinfo.getStartyear());
        // 拟建成时间
        itemBaseinfo.setItemfinishdate(baseinfo.getEndyear());
        // 总投资(万元)
        Double Totalinvest = null;
        if (StringUtil.isNotBlank(baseinfo.getInvestment())) {
            Totalinvest = Double.valueOf(baseinfo.getInvestment());
        }
        itemBaseinfo.setTotalinvest(Totalinvest);
        // 建设地点（建设地点行政区划）

        itemBaseinfo.set("XZQHDM", baseinfo.getXzqhdm());
        itemBaseinfo.set("jsddxzqh", baseinfo.getPlacecode());
        // 建设地点详情
        //itemBaseinfo.setConstructionsite(baseinfo.getPlacecode());
        // 国标行业
        itemBaseinfo.set("GBHY", baseinfo.getIndustry());
        // 建设规模及内容
        itemBaseinfo.setConstructionscaleanddesc(baseinfo.getProjectcontent());
        // 项目属性（项目资金属性）
        String projectattributes = baseinfo.getProjectattributes();
        String XMZJSX = "";
        switch (projectattributes) {
            case "A00001":
                XMZJSX = "1";
                break;
            case "A00002":
                XMZJSX = "2";
                break;
            case "A00003":
                XMZJSX = "3";
                break;
            default:
                XMZJSX = "3";
                break;
        }
        itemBaseinfo.set("XMZJSX", XMZJSX);
        // 项目（法人）单位（建设单位名称、项目（法人）单位）
        itemBaseinfo.setItemlegaldept(baseinfo.getEnterprisename());
        itemBaseinfo.setDepartname(baseinfo.getEnterprisename());
        // 项目法人证照类型
        itemBaseinfo.setItemlegalcerttype("16");
        // 统一社会信用代码
        itemBaseinfo.setItemlegalcreditcode(baseinfo.getLerepcertno());
        itemBaseinfo.setItemlegalcertnum(baseinfo.getLerepcertno());
        // 法人
        itemBaseinfo.setLegalperson(baseinfo.getContactname());
        // 法人身份证
        itemBaseinfo.setLegalpersonicardnum(baseinfo.getId());
        // 法人电话
        itemBaseinfo.setFrphone(baseinfo.getContactphone());
        // 法人邮箱
        itemBaseinfo.setFremail(baseinfo.getContactemail());
        itemBaseinfo.setItemcode(baseinfo.getItemcode());
        Service.update(baseinfo);
        itemService.addAuditRsItemBaseinfo(itemBaseinfo);

    }

}
