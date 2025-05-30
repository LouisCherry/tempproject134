package com.epoint.fmgl.auditrsitembaseinfoextends.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 赋码项目基本信息表实体
 * 
 * @作者  Administrator
 * @version [版本号, 2020-09-23 15:19:11]
 */
@Entity(table = "audit_rs_item_baseinfo_extends", id = "rowguid")
public class AuditRsItemBaseinfoExtends extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 所属辖区号
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
    * 操作者名字
    */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    /**
    * 操作日期
    */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
    * 序号
    */
    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
    }

    /**
    * 年份标识
    */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    /**
    * 默认主键字段
    */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
    * 投资项目行业分类
    */
    public String getPermitindustry() {
        return super.get("permitindustry");
    }

    public void setPermitindustry(String permitindustry) {
        super.set("permitindustry", permitindustry);
    }

    /**
    * 行业核准目录
    */
    public String getPermititemcode() {
        return super.get("permititemcode");
    }

    public void setPermititemcode(String permititemcode) {
        super.set("permititemcode", permititemcode);
    }

    /**
    * 项目类型
    */
    public String getProjecttype() {
        return super.get("projecttype");
    }

    public void setProjecttype(String projecttype) {
        super.set("projecttype", projecttype);
    }

    /**
    * 建设性质
    */
    public String getConstructper() {
        return super.get("constructper");
    }

    public void setConstructper(String constructper) {
        super.set("constructper", constructper);
    }

    /**
    * 项目名称
    */
    public String getProjectname() {
        return super.get("projectname");
    }

    public void setProjectname(String projectname) {
        super.set("projectname", projectname);
    }

    /**
    * 项目负责人
    */
    public String getLinkman() {
        return super.get("linkman");
    }

    public void setLinkman(String linkman) {
        super.set("linkman", linkman);
    }

    /**
    * 项目负责人联系方式
    */
    public String getLinkphone() {
        return super.get("linkphone");
    }

    public void setLinkphone(String linkphone) {
        super.set("linkphone", linkphone);
    }

    /**
    * 拟开工时间
    */
    public Date getStartyear() {
        return super.getDate("startyear");
    }

    public void setStartyear(Date startyear) {
        super.set("startyear", startyear);
    }

    /**
    * 拟建成时间
    */
    public Date getEndyear() {
        return super.getDate("endyear");
    }

    public void setEndyear(Date endyear) {
        super.set("endyear", endyear);
    }

    /**
    * 总投资(万元)
    */
    public String getInvestment() {
        return super.get("investment");
    }

    public void setInvestment(String investment) {
        super.set("investment", investment);
    }

    /**
    * 建设地点
    */
    public String getPlacecode() {
        return super.get("placecode");
    }

    public void setPlacecode(String placecode) {
        super.set("placecode", placecode);
    }

    /**
    * 建设地点详情
    */
    public String getPlacedetailcode() {
        return super.get("placedetailcode");
    }

    public void setPlacedetailcode(String placedetailcode) {
        super.set("placedetailcode", placedetailcode);
    }

    /**
    * 国标行业
    */
    public String getIndustry() {
        return super.get("industry");
    }

    public void setIndustry(String industry) {
        super.set("industry", industry);
    }

    /**
    * 产业结构调整指导目录
    */
    public String getCyjgtzzdml() {
        return super.get("cyjgtzzdml");
    }

    public void setCyjgtzzdml(String cyjgtzzdml) {
        super.set("cyjgtzzdml", cyjgtzzdml);
    }

    /**
    * 所属行业
    */
    public String getTheindustry() {
        return super.get("theindustry");
    }

    public void setTheindustry(String theindustry) {
        super.set("theindustry", theindustry);
    }

    /**
    * 申报日期
    */
    public Date getApplydate() {
        return super.getDate("applydate");
    }

    public void setApplydate(Date applydate) {
        super.set("applydate", applydate);
    }

    /**
    * 建设规模及内容
    */
    public String getProjectcontent() {
        return super.get("projectcontent");
    }

    public void setProjectcontent(String projectcontent) {
        super.set("projectcontent", projectcontent);
    }

    /**
    * 项目阶段
    */
    public String getProjectstage() {
        return super.get("projectstage");
    }

    public void setProjectstage(String projectstage) {
        super.set("projectstage", projectstage);
    }

    /**
    * 项目属性
    */
    public String getProjectattributes() {
        return super.get("projectattributes");
    }

    public void setProjectattributes(String projectattributes) {
        super.set("projectattributes", projectattributes);
    }

    /**
    * 拟向民间资本推介项目
    */
    public String getTjxm() {
        return super.get("tjxm");
    }

    public void setTjxm(String tjxm) {
        super.set("tjxm", tjxm);
    }

    /**
    * 项目（法人）单位
    */
    public String getEnterprisename() {
        return super.get("enterprisename");
    }

    public void setEnterprisename(String enterprisename) {
        super.set("enterprisename", enterprisename);
    }

    /**
    * 项目法人证照类型
    */
    public String getLerepcerttypetext() {
        return super.get("lerepcerttypetext");
    }

    public void setLerepcerttypetext(String lerepcerttypetext) {
        super.set("lerepcerttypetext", lerepcerttypetext);
    }

    /**
    * 项目法人证照号码
    */
    public String getLerepcertno() {
        return super.get("lerepcertno");
    }

    public void setLerepcertno(String lerepcertno) {
        super.set("lerepcertno", lerepcertno);
    }

    /**
    * 法定代表人
    */
    public String getContactname() {
        return super.get("contactname");
    }

    public void setContactname(String contactname) {
        super.set("contactname", contactname);
    }

    /**
    * 项目单位性质
    */
    public String getEnterprisenature() {
        return super.get("enterprisenature");
    }

    public void setEnterprisenature(String enterprisenature) {
        super.set("enterprisenature", enterprisenature);
    }

    /**
    * 项目法人联系电话
    */
    public String getContacttel() {
        return super.get("contacttel");
    }

    public void setContacttel(String contacttel) {
        super.set("contacttel", contacttel);
    }

    /**
    * 项目法人联系人邮箱
    */
    public String getContactemail() {
        return super.get("contactemail");
    }

    public void setContactemail(String contactemail) {
        super.set("contactemail", contactemail);
    }

    /**
    * 项目法人联系人手机
    */
    public String getContactphone() {
        return super.get("contactphone");
    }

    public void setContactphone(String contactphone) {
        super.set("contactphone", contactphone);
    }

    /**
    * 项目法人传真
    */
    public String getContactfax() {
        return super.get("contactfax");
    }

    public void setContactfax(String contactfax) {
        super.set("contactfax", contactfax);
    }

    /**
    * 单位注册地址(企业法人)
    */
    public String getCorrespondenceaddress() {
        return super.get("correspondenceaddress");
    }

    public void setCorrespondenceaddress(String correspondenceaddress) {
        super.set("correspondenceaddress", correspondenceaddress);
    }

    /**
    * 项目（申报）单位
    */
    public String getEnterprisenamesb() {
        return super.get("enterprisenamesb");
    }

    public void setEnterprisenamesb(String enterprisenamesb) {
        super.set("enterprisenamesb", enterprisenamesb);
    }

    /**
    * 申报单位项目法人证照类型
    */
    public String getLerepcerttypesb() {
        return super.get("lerepcerttypesb");
    }

    public void setLerepcerttypesb(String lerepcerttypesb) {
        super.set("lerepcerttypesb", lerepcerttypesb);
    }

    /**
    * 申报单位项目法人证照号码
    */
    public String getLerepcertnosb() {
        return super.get("lerepcertnosb");
    }

    public void setLerepcertnosb(String lerepcertnosb) {
        super.set("lerepcertnosb", lerepcertnosb);
    }

    /**
    * 项目单位性质（申报单位）
    */
    public String getEnterprisenaturesb() {
        return super.get("enterprisenaturesb");
    }

    public void setEnterprisenaturesb(String enterprisenaturesb) {
        super.set("enterprisenaturesb", enterprisenaturesb);
    }

    /**
    * 申报单位法定代表人
    */
    public String getContactnamesb() {
        return super.get("contactnamesb");
    }

    public void setContactnamesb(String contactnamesb) {
        super.set("contactnamesb", contactnamesb);
    }

    /**
    * 申报单位联系电话
    */
    public String getContacttelsb() {
        return super.get("contacttelsb");
    }

    public void setContacttelsb(String contacttelsb) {
        super.set("contacttelsb", contacttelsb);
    }

    /**
    * 申报单位联系人邮箱
    */
    public String getContactemailsb() {
        return super.get("contactemailsb");
    }

    public void setContactemailsb(String contactemailsb) {
        super.set("contactemailsb", contactemailsb);
    }

    /**
    * 申报单位联系人手机
    */
    public String getContactphonesb() {
        return super.get("contactphonesb");
    }

    public void setContactphonesb(String contactphonesb) {
        super.set("contactphonesb", contactphonesb);
    }

    /**
    * 申报单位传真
    */
    public String getContactfaxsb() {
        return super.get("contactfaxsb");
    }

    public void setContactfaxsb(String contactfaxsb) {
        super.set("contactfaxsb", contactfaxsb);
    }

    /**
    * 单位注册地址（申报单位）
    */
    public String getCorrespondenceaddresssb() {
        return super.get("correspondenceaddresssb");
    }

    public void setCorrespondenceaddresssb(String correspondenceaddresssb) {
        super.set("correspondenceaddresssb", correspondenceaddresssb);
    }

    /**
    * 项目表主键
    */
    public String getXiangmubh() {
        return super.get("xiangmubh");
    }

    public void setXiangmubh(String xiangmubh) {
        super.set("xiangmubh", xiangmubh);
    }

    /**
    * 项目代码
    */
    public String getItemcode() {
        return super.get("itemcode");
    }

    public void setItemcode(String itemcode) {
        super.set("itemcode", itemcode);
    }

    /**
    * 状态
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 申请返回seqId
    */
    public String getSeqid() {
        return super.get("seqid");
    }

    public void setSeqid(String seqid) {
        super.set("seqid", seqid);
    }

    /**
    * 申请返回investId
    */
    public String getInvestid() {
        return super.get("investid");
    }

    public void setInvestid(String investid) {
        super.set("investid", investid);
    }

    /**
    * 备用字段
    */
    public String getId() {
        return super.get("id");
    }

    public void setId(String id) {
        super.set("id", id);
    }
    
    public String getXzqhdm() {
        return super.get("xzqhdm");
    }

    public void setXzqhdm(String xzqhdm) {
        super.set("xzqhdm", xzqhdm);
    }
    //项目所在地projectSite projectsite text
    public String getProjectsite() {
        return super.get("projectsite");
    }
    public void setProjectsite(String projectsite) {
        super.set("projectsite", projectsite);
    }
    //中方投资额(万元)chinaTotalMoney   chinatotalmoney
    public String getChinatotalmoney() {
        return super.get("chinatotalmoney");
    }
    public void setChinatotalmoney(String chinatotalmoney) {
        super.set("chinatotalmoney", chinatotalmoney);
    }
    //投资方式investmentMode  investmentmode
    public String getInvestmentmode() {
        return super.get("investmentmode");
    }
    public void setInvestmentmode(String investmentmode) {
        super.set("investmentmode", investmentmode);
    }
    public String getInvestmentmode2() {
        return super.get("investmentmode2");
    }
    public void setInvestmentmode2(String investmentmode2) {
        super.set("investmentmode2", investmentmode2);
    }
    //是否涉及国家安全isCountrySecurity  iscountrysecurity
    public String getIscountrysecurity() {
        return super.get("iscountrysecurity");
    }
    public void setIscountrysecurity(String iscountrysecurity) {
        super.set("iscountrysecurity", iscountrysecurity);
    }
    //安全审查决定文号 securityApprovalNumber securityapprovalnumber
    public String getSecurityapprovalnumber() {
        return super.get("securityapprovalnumber");
    }
    public void setSecurityapprovalnumber(String securityapprovalnumber) {
        super.set("securityapprovalnumber", securityapprovalnumber);
    }
    //总投资额折合美元(万元)totalMoneyDollar totalmoneydollar
    public String getTotalmoneydollar() {
        return super.get("totalmoneydollar");
    }
    public void setTotalmoneydollar(String totalmoneydollar) {
        super.set("totalmoneydollar", totalmoneydollar);
    }
    //总投资额使用的汇率（人民币/美元）totalMoneyDollarRate totalmoneydollarrate
    public String getTotalmoneydollarrate() {
        return super.get("totalmoneydollarrate");
    }
    public void setTotalmoneydollarrate(String totalmoneydollarrate) {
        super.set("totalmoneydollarrate", totalmoneydollarrate);
    }
    //项目资本金(万元)projectCapitalMoney projectcapitalmoney
    public String getProjectcapitalmoney() {
        return super.get("projectcapitalmoney");
    }
    public void setProjectcapitalmoney(String projectcapitalmoney) {
        super.set("projectcapitalmoney", projectcapitalmoney);
    }
    //项目资本金折合美元(万元) projectCapitalMoneyDollar  projectcapitalmoneydollar
    public String getProjectcapitalmoneydollar() {
        return super.get("projectcapitalmoneydollar");
    }
    public void setProjectcapitalmoneydollar(String projectcapitalmoneydollar) {
        super.set("projectcapitalmoneydollar", projectcapitalmoneydollar);
    }
    //项目资本金使用的汇率（人民币/美元） capitalMoneyDollarRate  capitalmoneydollarrate
    public String getCapitalmoneydollarrate() {
        return super.get("capitalmoneydollarrate");
    }
    public void setCapitalmoneydollarrate(String capitalmoneydollarrate) {
        super.set("capitalmoneydollarrate", capitalmoneydollarrate);
    }
    //适用产业政策条目代码 industrialPolicy  industrialpolicy
    public String getIndustrialpolicy() {
        return super.get("industrialpolicy");
    }
    public void setIndustrialpolicy(String industrialpolicy) {
        super.set("industrialpolicy", industrialpolicy);
    }
    public String getIndustrialpolicy2() {
        return super.get("industrialpolicy2");
    }
    public void setIndustrialpolicy2(String industrialpolicy2) {
        super.set("industrialpolicy2", industrialpolicy2);
    }
    //适用产业政策条目类型 industrialPolicyType industrialpolicytype
    public String getIndustrialpolicytype() {
        return super.get("industrialpolicytype");
    }
    public void setIndustrialpolicytype(String industrialpolicytype) {
        super.set("industrialpolicytype", industrialpolicytype);
    }
    //其他投资方式需予以申报的情况otherInvestmentApplyInfo  otherinvestmentapplyInfo
    public String getOtherinvestmentapplyInfo() {
        return super.get("otherinvestmentapplyInfo");
    }
    public void setOtherinvestmentapplyInfo(String otherinvestmentapplyInfo) {
        super.set("otherinvestmentapplyInfo", otherinvestmentapplyInfo);
    }
    //土地获取方式getLandMode  getlandmode
    public String getGetlandmode() {
        return super.get("getlandmode");
    }
    public void setGetlandmode(String getlandmode) {
        super.set("getlandmode", getlandmode);
    }
    //总用地面积（平方米） landArea landarea
    public String getLandarea() {
        return super.get("landarea");
    }
    public void setLandarea(String landarea) {
        super.set("landarea", landarea);
    }
    //总建筑面积（平方米） builtArea builtarea
    public String getBuiltarea() {
        return super.get("builtarea");
    }
    public void setBuiltarea(String builtarea) {
        super.set("builtarea", builtarea);
    }
    //是否新增设备  isAddDevice  isadddevice
    public String getIsadddevice() {
        return super.get("isadddevice");
    }
    public void setIsadddevice(String isadddevice) {
        super.set("isadddevice", isadddevice);
    }
    //其中：拟进口设备数量及金额 importDeviceNumberMoney importdevicenumbermoney
    public String getImportdevicenumbermoney() {
        return super.get("importdevicenumbermoney");
    }
    public void setImportdevicenumbermoney(String importdevicenumbermoney) {
        super.set("importdevicenumbermoney", importdevicenumbermoney);
    }
    //提供交易双方情况 transactionBothInfo  transactionbothinfo TEXT
    public String getTransactionbothinfo() {
        return super.get("transactionbothinfo");
    }
    public void setTransactionbothinfo(String transactionbothinfo) {
        super.set("transactionbothinfo", transactionbothinfo);
    }
    //并购安排： mergerPlan mergerplan text
    public String getMergerplan() {
        return super.get("mergerplan");
    }
    public void setMergerplan(String mergerplan) {
        super.set("mergerplan", mergerplan);
    }
    //mergerManagementModeScope并购后经营方式及经营范围：  mergermanagementmodescope
    public String getMergermanagementmodescope() {
        return super.get("mergermanagementmodescope");
    }
    public void setMergermanagementmodescope(String mergermanagementmodescope) {
        super.set("mergermanagementmodescope", mergermanagementmodescope);
    }
    //提供交易双方情况 transactionBothInfo  transactionbothinfo TEXT
    public String getTransactionbothinfo2() {
        return super.get("transactionbothinfo2");
    }
    public void setTransactionbothinfo2(String transactionbothinfo2) {
        super.set("transactionbothinfo2", transactionbothinfo2);
    }
    //并购安排： mergerPlan mergerplan text
    public String getMergerplan2() {
        return super.get("mergerplan2");
    }
    public void setMergerplan2(String mergerplan2) {
        super.set("mergerplan2", mergerplan2);
    }
    //mergerManagementModeScope并购后经营方式及经营范围：  mergermanagementmodescope
    public String getMergermanagementmodescope2() {
        return super.get("mergermanagementmodescope2");
    }
    public void setMergermanagementmodescope2(String mergermanagementmodescope2) {
        super.set("mergermanagementmodescope2", mergermanagementmodescope2);
    }
    //出资类型businessType  businesstype
    public String getBusinesstype() {
        return super.get("businesstype");
    }
    public void setBusinesstype(String businesstype) {
        super.set("businesstype", businesstype);
    }
    //投资者名称investmentName   investmentname
    public String getInvestmentname() {
        return super.get("investmentname");
    }
    public void setInvestmentname(String investmentname) {
        super.set("investmentname", investmentname);
    }
    //注册国别地区regCountry  regcountry
    public String getRegcountry() {
        return super.get("regcountry");
    }
    public void setRegcountry(String regcountry) {
        super.set("regcountry", regcountry);
    }
    //注册国别地区名称regCountryName   regcountryname
    public String getRegcountryname() {
        return super.get("regcountryname");
    }
    public void setRegcountryname(String regcountryname) {
        super.set("regcountryname", regcountryname);
    }
    //出资方式contributionMode   contributionmode
    public String getContributionmode() {
        return super.get("contributionmode");
    }
    public void setContributionmode(String contributionmode) {
        super.set("contributionmode", contributionmode);
    }
    //出资额(万元)contributionLimit   contributionlimit
    public String getContributionlimit() {
        return super.get("contributionlimit");
    }
    public void setContributionlimit(String contributionlimit) {
        super.set("contributionlimit", contributionlimit);
    }
    //出资比例contributionRatio    contributionratio
    public String getContributionratio() {
        return super.get("contributionratio");
    }
    public void setContributionratio(String contributionratio) {
        super.set("contributionratio", contributionratio);
    }
    
    
}
