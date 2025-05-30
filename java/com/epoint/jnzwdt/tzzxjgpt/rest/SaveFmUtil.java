package com.epoint.jnzwdt.tzzxjgpt.rest;
import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.IAuditRsItemBaseinfoExtendsService;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;

public class SaveFmUtil
{
    
    public static void saveFmbaseinfo(String investId,String seqId,JSONObject j,JSONObject l,JSONObject p,Date startYear,Date endYear,Date applyDate,String jsddxq,String legalpersonicardnum) {
        IAuditRsItemBaseinfoExtendsService Service=ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfoExtendsService.class);
        IAuditRsItemBaseinfo itemService=ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
        AuditRsItemBaseinfoExtends baseinfo=new AuditRsItemBaseinfoExtends();
        AuditRsItemBaseinfo itemBaseinfo=new AuditRsItemBaseinfo();
        itemBaseinfo.setRowguid(UUID.randomUUID().toString());
        itemBaseinfo.setOperatedate(new Date());
        itemBaseinfo.setOperateusername("赋码申请");
        baseinfo.setRowguid(UUID.randomUUID().toString());
        baseinfo.setOperatedate(new Date());
//        permitIndustry : params.permitIndustry,//1
        baseinfo.setPermitindustry(j.getString("permitIndustry"));
//        permitItemCode : params.PermitItemCode,//2
        baseinfo.setPermititemcode(j.getString("permitItemCode"));
//        projectType : params.projectType,//3
        baseinfo.setProjecttype(j.getString("projectType"));
//        constructPer : params.constructPer,//4
        baseinfo.setConstructper(j.getString("constructPer"));
//        projectName : params.projectName,//5
        baseinfo.setProjectname(j.getString("projectName"));
//        linkMan : params.linkMan,//6
        baseinfo.setLinkman(j.getString("linkMan"));
//        linkPhone : params.linkPhone,//7
        baseinfo.setLinkphone(j.getString("linkPhone"));
//        startYear : params.startYear,//8
        baseinfo.setStartyear(startYear);
////        endYear : params.endYear,//9
        baseinfo.setEndyear(endYear);
//        investment : params.investment,//10
        baseinfo.setInvestment(j.getString("investment"));
//        placeCode : params.placeCode,//11
        baseinfo.setPlacecode(j.getString("placeCode"));
//        industry : params.GBHY,//12
        baseinfo.setIndustry(j.getString("industry"));
//        cyjgtzzdml:params.cyjgtzzdml,//13
        baseinfo.setCyjgtzzdml(j.getString("cyjgtzzdml"));
//        theIndustry : params.theIndustry,//14
        baseinfo.setTheindustry(j.getString("theIndustry"));
//        applyDate : params.applyDate,//15
        baseinfo.setApplydate(applyDate);
//        projectContent : params.projectContent,//16
        baseinfo.setProjectcontent(j.getString("projectContent"));
//        projectStage : params.projectStage,//17
        baseinfo.setProjectstage(j.getString("projectStage"));
//        projectAttributes : params.projectAttributes,//18
        baseinfo.setProjectattributes(j.getString("projectAttributes"));
//         tjxm : params.tjxm,//19
        baseinfo.setTjxm(j.getString("tjxm"));
//        enterpriseName : params.enterpriseName,//20
        
        
        
        
        
        baseinfo.setEnterprisename(l.getString("enterpriseName"));
//        lerepCerttype: params.lerepCerttype,//21
        baseinfo.setLerepcerttypesb(l.getString("lerepCerttype"));
//        lerepCertno: params.lerepCertno,//22
        baseinfo.setLerepcertno(l.getString("lerepCertno"));
//        enterpriseNature: params.enterpriseNature,//23
        baseinfo.setEnterprisenature(l.getString("enterpriseNature"));
//        contactName: params.contactName,//24
        baseinfo.setContactname(l.getString("contactName"));
//        contactTel: params.contactTel,//25
        baseinfo.setContacttel(l.getString("contactTel"));
//        contactEmail: params.contactEmail,//26
        baseinfo.setContactemail(l.getString("contactEmail"));
//        contactPhone: params.contactPhone,//27
        baseinfo.setContactphone(l.getString("contactPhone"));
//        contactFax: params.contactFax,//28
        baseinfo.setContactfax(l.getString("contactFax"));
//        correspondenceAddress : params.correspondenceAddress,//29
        baseinfo.setCorrespondenceaddress(l.getString("correspondenceAddress"));
//        enterpriseNamesb: params.enterpriseNamesb,//30
        
        baseinfo.setEnterprisenamesb(p.getString("enterpriseNamesb"));
//        lerepCerttypesb: params.lerepCerttypesb,//31
        baseinfo.setLerepcerttypesb(p.getString("lerepCerttypesb"));
//        lerepCertnosb: params.lerepCertnosb,//32
        baseinfo.setLerepcertnosb(p.getString("lerepCertnosb"));
//        enterpriseNaturesb: params.enterpriseNaturesb,//33
        baseinfo.setEnterprisenaturesb(p.getString("enterpriseNaturesb"));
//        contactNamesb: params.contactNamesb,//34
        baseinfo.setContactnamesb(p.getString("contactNamesb"));
//        contactTelsb: params.contactTelsb,//35
        baseinfo.setContacttelsb(p.getString("contactTelsb"));
//        contactEmailsb: params.contactEmailsb,//36
        baseinfo.setContactemailsb(p.getString("contactEmailsb"));
//        contactPhonesb: params.contactPhonesb,//37
        baseinfo.setContactphonesb(p.getString("contactPhonesb"));
//        contactFaxsb: params.contactFaxsb,//38
        baseinfo.setContactfaxsb(p.getString("contactFaxsb"));
//        correspondenceAddresssb: params.correspondenceAddresssb,//39
        baseinfo.setCorrespondenceaddresssb(p.getString("correspondenceAddresssb"));
        
        baseinfo.setLerepcerttypetext("统一社会信用代码");
        baseinfo.setPlacedetailcode(jsddxq);
        baseinfo.setId(legalpersonicardnum);
       
        if(StringUtil.isNotBlank(seqId)&&StringUtil.isNotBlank(investId)) {
            baseinfo.setStatus("1");
            baseinfo.setSeqid(seqId);
            baseinfo.setInvestid(investId);
            baseinfo.setXiangmubh(itemBaseinfo.getRowguid());
            //立项类型
            String projecttype = baseinfo.getProjecttype();
            String LXLX="";
            switch (projecttype) {
                case "A00001":
                    LXLX="1";
                    break;
                case "A00002":
                    LXLX="2";
                    break;
                case "A00003":
                    LXLX="3";
                    break;
                default:
                    LXLX="1";
                    break;
            }
            itemBaseinfo.set("LXLX", LXLX);
            //建设性质
            String constructper = baseinfo.getConstructper();
            String Constructionproperty="";
            switch (constructper) {
                case "0":
                    Constructionproperty="1";
                    break;
                case "1":
                    Constructionproperty="2";
                    break;
                case "2":
                    Constructionproperty="3";
                    break;
                case "3":
                    Constructionproperty="4";
                    break;
                default:
                    Constructionproperty="5";
                    break;
            }
            itemBaseinfo.setConstructionproperty(Constructionproperty);
            //项目名称
            itemBaseinfo.setItemname(baseinfo.getProjectname());
            //拟开工时间
            itemBaseinfo.setItemstartdate(startYear);
            //拟建成时间
            itemBaseinfo.setItemfinishdate(endYear);
            //总投资(万元)
            Double Totalinvest=null;
            if(StringUtil.isNotBlank(baseinfo.getInvestment())) {
                Totalinvest = Double.valueOf(baseinfo.getInvestment());
            }
            itemBaseinfo.setTotalinvest(Totalinvest);
            //建设地点（建设地点行政区划）
            
            itemBaseinfo.set("XZQHDM",baseinfo.getPlacecode());
            itemBaseinfo.set("jsddxzqh",baseinfo.getPlacecode());
            //建设地点详情
            itemBaseinfo.setConstructionsite(jsddxq);
            //国标行业
            itemBaseinfo.set("GBHY", baseinfo.getIndustry());
            //建设规模及内容
            itemBaseinfo.setConstructionscaleanddesc(baseinfo.getProjectcontent());
            //项目属性（项目资金属性）
            String projectattributes = baseinfo.getProjectattributes();
            String XMZJSX="";
            switch (projectattributes) {
                case "A00001":
                    XMZJSX="1";
                    break;
                case "A00002":
                    XMZJSX="2";
                    break;
                case "A00003":
                    XMZJSX="3";
                    break;
                default:
                    XMZJSX="3";
                    break;
            }
            itemBaseinfo.set("XMZJSX", XMZJSX);
            //项目（法人）单位（建设单位名称、项目（法人）单位）
            itemBaseinfo.setItemlegaldept(baseinfo.getEnterprisename());
            itemBaseinfo.setDepartname(baseinfo.getEnterprisename());
            //项目法人证照类型
            itemBaseinfo.setItemlegalcerttype("16");
            //统一社会信用代码
            itemBaseinfo.setItemlegalcreditcode(baseinfo.getLerepcertno());
            itemBaseinfo.setItemlegalcertnum(baseinfo.getLerepcertno());
            //法人
            itemBaseinfo.setLegalperson(baseinfo.getContactname());
            //法人身份证
            itemBaseinfo.setLegalpersonicardnum(legalpersonicardnum);
            //法人电话
            itemBaseinfo.setFrphone(baseinfo.getContactphone());
            //法人邮箱
            itemBaseinfo.setFremail(baseinfo.getContactemail());
            Service.insert(baseinfo);
            itemService.addAuditRsItemBaseinfo(itemBaseinfo);
           
        }else {
            baseinfo.setStatus("4");
            Service.insert(baseinfo);
        }
       
      
       
    }

}
