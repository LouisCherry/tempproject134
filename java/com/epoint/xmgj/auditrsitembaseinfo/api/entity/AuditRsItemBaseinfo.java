package com.epoint.xmgj.auditrsitembaseinfo.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 项目表实体
 * 
 * @作者 pansh
 * @version [版本号, 2025-02-12 17:31:53]
 */
@Entity(table = "AUDIT_RS_ITEM_BASEINFO", id = "rowguid")
public class AuditRsItemBaseinfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * BelongXiaQuCode
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
     * OperateUserName
     */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    /**
     * OperateDate
     */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
     * Row_ID
     */
    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
    }

    /**
     * YearFlag
     */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    /**
     * RowGuid
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
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
     * 项目名称
     */
    public String getItemname() {
        return super.get("itemname");
    }

    public void setItemname(String itemname) {
        super.set("itemname", itemname);
    }

    /**
     * ITEMTYPE
     */
    public String getItemtype() {
        return super.get("itemtype");
    }

    public void setItemtype(String itemtype) {
        super.set("itemtype", itemtype);
    }

    /**
     * CONSTRUCTIONPROPERTY
     */
    public String getConstructionproperty() {
        return super.get("constructionproperty");
    }

    public void setConstructionproperty(String constructionproperty) {
        super.set("constructionproperty", constructionproperty);
    }

    /**
     * 建设单位
     */
    public String getItemlegaldept() {
        return super.get("itemlegaldept");
    }

    public void setItemlegaldept(String itemlegaldept) {
        super.set("itemlegaldept", itemlegaldept);
    }

    /**
     * ITEMLEGALCREDITCODE
     */
    public String getItemlegalcreditcode() {
        return super.get("itemlegalcreditcode");
    }

    public void setItemlegalcreditcode(String itemlegalcreditcode) {
        super.set("itemlegalcreditcode", itemlegalcreditcode);
    }

    /**
     * ITEMLEGALCERTTYPE
     */
    public String getItemlegalcerttype() {
        return super.get("itemlegalcerttype");
    }

    public void setItemlegalcerttype(String itemlegalcerttype) {
        super.set("itemlegalcerttype", itemlegalcerttype);
    }

    /**
     * ITEMLEGALCERTNUM
     */
    public String getItemlegalcertnum() {
        return super.get("itemlegalcertnum");
    }

    public void setItemlegalcertnum(String itemlegalcertnum) {
        super.set("itemlegalcertnum", itemlegalcertnum);
    }

    /**
     * 建设开始时间
     */
    public Date getItemstartdate() {
        return super.getDate("itemstartdate");
    }

    public void setItemstartdate(Date itemstartdate) {
        super.set("itemstartdate", itemstartdate);
    }

    /**
     * 建设结束时间
     */
    public Date getItemfinishdate() {
        return super.getDate("itemfinishdate");
    }

    public void setItemfinishdate(Date itemfinishdate) {
        super.set("itemfinishdate", itemfinishdate);
    }

    /**
     * 总投资（万元）
     */
    public Double getTotalinvest() {
        return super.getDouble("totalinvest");
    }

    public void setTotalinvest(Double totalinvest) {
        super.set("totalinvest", totalinvest);
    }

    /**
     * CONSTRUCTIONSITE
     */
    public String getConstructionsite() {
        return super.get("constructionsite");
    }

    public void setConstructionsite(String constructionsite) {
        super.set("constructionsite", constructionsite);
    }

    /**
     * CONSTRUCTIONSITEDESC
     */
    public String getConstructionsitedesc() {
        return super.get("constructionsitedesc");
    }

    public void setConstructionsitedesc(String constructionsitedesc) {
        super.set("constructionsitedesc", constructionsitedesc);
    }

    /**
     * BELONGTINDUSTRY
     */
    public String getBelongtindustry() {
        return super.get("belongtindustry");
    }

    public void setBelongtindustry(String belongtindustry) {
        super.set("belongtindustry", belongtindustry);
    }

    /**
     * CONSTRUCTIONSCALEANDDESC
     */
    public String getConstructionscaleanddesc() {
        return super.get("constructionscaleanddesc");
    }

    public void setConstructionscaleanddesc(String constructionscaleanddesc) {
        super.set("constructionscaleanddesc", constructionscaleanddesc);
    }

    /**
     * CONTRACTPERSON
     */
    public String getContractperson() {
        return super.get("contractperson");
    }

    public void setContractperson(String contractperson) {
        super.set("contractperson", contractperson);
    }

    /**
     * CONTRACTPHONE
     */
    public String getContractphone() {
        return super.get("contractphone");
    }

    public void setContractphone(String contractphone) {
        super.set("contractphone", contractphone);
    }

    /**
     * CONTRACTEMAIL
     */
    public String getContractemail() {
        return super.get("contractemail");
    }

    public void setContractemail(String contractemail) {
        super.set("contractemail", contractemail);
    }

    /**
     * LEGALPROPERTY
     */
    public String getLegalproperty() {
        return super.get("legalproperty");
    }

    public void setLegalproperty(String legalproperty) {
        super.set("legalproperty", legalproperty);
    }

    /**
     * LANDAREA
     */
    public Double getLandarea() {
        return super.getDouble("landarea");
    }

    public void setLandarea(Double landarea) {
        super.set("landarea", landarea);
    }

    /**
     * NEWLANDAREA
     */
    public Double getNewlandarea() {
        return super.getDouble("newlandarea");
    }

    public void setNewlandarea(Double newlandarea) {
        super.set("newlandarea", newlandarea);
    }

    /**
     * AGRICULTURALLANDAREA
     */
    public Double getAgriculturallandarea() {
        return super.getDouble("agriculturallandarea");
    }

    public void setAgriculturallandarea(Double agriculturallandarea) {
        super.set("agriculturallandarea", agriculturallandarea);
    }

    /**
     * ITEMCAPITAL
     */
    public Double getItemcapital() {
        return super.getDouble("itemcapital");
    }

    public void setItemcapital(Double itemcapital) {
        super.set("itemcapital", itemcapital);
    }

    /**
     * FUNDSOURCES
     */
    public String getFundsources() {
        return super.get("fundsources");
    }

    public void setFundsources(String fundsources) {
        super.set("fundsources", fundsources);
    }

    /**
     * FINANCIALRESOURCES
     */
    public String getFinancialresources() {
        return super.get("financialresources");
    }

    public void setFinancialresources(String financialresources) {
        super.set("financialresources", financialresources);
    }

    /**
     * QUANTIFYCONSTRUCTTYPE
     */
    public String getQuantifyconstructtype() {
        return super.get("quantifyconstructtype");
    }

    public void setQuantifyconstructtype(String quantifyconstructtype) {
        super.set("quantifyconstructtype", quantifyconstructtype);
    }

    /**
     * QUANTIFYCONSTRUCTCOUNT
     */
    public Double getQuantifyconstructcount() {
        return super.getDouble("quantifyconstructcount");
    }

    public void setQuantifyconstructcount(Double quantifyconstructcount) {
        super.set("quantifyconstructcount", quantifyconstructcount);
    }

    /**
     * QUANTIFYCONSTRUCTDEPT
     */
    public String getQuantifyconstructdept() {
        return super.get("quantifyconstructdept");
    }

    public void setQuantifyconstructdept(String quantifyconstructdept) {
        super.set("quantifyconstructdept", quantifyconstructdept);
    }

    /**
     * ISIMPROVEMENT
     */
    public String getIsimprovement() {
        return super.get("isimprovement");
    }

    public void setIsimprovement(String isimprovement) {
        super.set("isimprovement", isimprovement);
    }

    /**
     * VERSIONTIME
     */
    public Date getVersiontime() {
        return super.getDate("versiontime");
    }

    public void setVersiontime(Date versiontime) {
        super.set("versiontime", versiontime);
    }

    /**
     * VERSION
     */
    public Integer getVersion() {
        return super.getInt("version");
    }

    public void setVersion(Integer version) {
        super.set("version", version);
    }

    /**
     * IS_HISTORY
     */
    public String getIs_history() {
        return super.get("is_history");
    }

    public void setIs_history(String is_history) {
        super.set("is_history", is_history);
    }

    /**
     * BIGUID
     */
    public String getBiguid() {
        return super.get("biguid");
    }

    public void setBiguid(String biguid) {
        super.set("biguid", biguid);
    }

    /**
     * xmguid
     */
    public String getXmguid() {
        return super.get("xmguid");
    }

    public void setXmguid(String xmguid) {
        super.set("xmguid", xmguid);
    }

    /**
     * dlguid
     */
    public String getDlguid() {
        return super.get("dlguid");
    }

    public void setDlguid(String dlguid) {
        super.set("dlguid", dlguid);
    }

    /**
     * clientguid
     */
    public String getClientguid() {
        return super.get("clientguid");
    }

    public void setClientguid(String clientguid) {
        super.set("clientguid", clientguid);
    }

    /**
     * bbdb
     */
    public String getBbdb() {
        return super.get("bbdb");
    }

    public void setBbdb(String bbdb) {
        super.set("bbdb", bbdb);
    }

    /**
     * userguid
     */
    public String getUserguid() {
        return super.get("userguid");
    }

    public void setUserguid(String userguid) {
        super.set("userguid", userguid);
    }

    /**
     * kcinput
     */
    public String getKcinput() {
        return super.get("kcinput");
    }

    public void setKcinput(String kcinput) {
        super.set("kcinput", kcinput);
    }

    /**
     * kc
     */
    public String getKc() {
        return super.get("kc");
    }

    public void setKc(String kc) {
        super.set("kc", kc);
    }

    /**
     * ps
     */
    public String getPs() {
        return super.get("ps");
    }

    public void setPs(String ps) {
        super.set("ps", ps);
    }

    /**
     * psinput
     */
    public String getPsinput() {
        return super.get("psinput");
    }

    public void setPsinput(String psinput) {
        super.set("psinput", psinput);
    }

    /**
     * gg
     */
    public String getGg() {
        return super.get("gg");
    }

    public void setGg(String gg) {
        super.set("gg", gg);
    }

    /**
     * gginput
     */
    public String getGginput() {
        return super.get("gginput");
    }

    public void setGginput(String gginput) {
        super.set("gginput", gginput);
    }

    /**
     * other
     */
    public String getOther() {
        return super.get("other");
    }

    public void setOther(String other) {
        super.set("other", other);
    }

    /**
     * delve
     */
    public String getDelve() {
        return super.get("delve");
    }

    public void setDelve(String delve) {
        super.set("delve", delve);
    }

    /**
     * XiangmuSource
     */
    public String getXiangmusource() {
        return super.get("xiangmusource");
    }

    public void setXiangmusource(String xiangmusource) {
        super.set("xiangmusource", xiangmusource);
    }

    /**
     * DEPARTNAME
     */
    public String getDepartname() {
        return super.get("departname");
    }

    public void setDepartname(String departname) {
        super.set("departname", departname);
    }

    /**
     * constructionaddress
     */
    public String getConstructionaddress() {
        return super.get("constructionaddress");
    }

    public void setConstructionaddress(String constructionaddress) {
        super.set("constructionaddress", constructionaddress);
    }

    /**
     * legalperson
     */
    public String getLegalperson() {
        return super.get("legalperson");
    }

    public void setLegalperson(String legalperson) {
        super.set("legalperson", legalperson);
    }

    /**
     * legalpersonicardnum
     */
    public String getLegalpersonicardnum() {
        return super.get("legalpersonicardnum");
    }

    public void setLegalpersonicardnum(String legalpersonicardnum) {
        super.set("legalpersonicardnum", legalpersonicardnum);
    }

    /**
     * frphone
     */
    public String getFrphone() {
        return super.get("frphone");
    }

    public void setFrphone(String frphone) {
        super.set("frphone", frphone);
    }

    /**
     * fremail
     */
    public String getFremail() {
        return super.get("fremail");
    }

    public void setFremail(String fremail) {
        super.set("fremail", fremail);
    }

    /**
     * contractidcart
     */
    public String getContractidcart() {
        return super.get("contractidcart");
    }

    public void setContractidcart(String contractidcart) {
        super.set("contractidcart", contractidcart);
    }

    /**
     * parentid
     */
    public String getParentid() {
        return super.get("parentid");
    }

    public void setParentid(String parentid) {
        super.set("parentid", parentid);
    }

    /**
     * sync
     */
    public String getSync() {
        return super.get("sync");
    }

    public void setSync(String sync) {
        super.set("sync", sync);
    }

    /**
     * TDHQFS
     */
    public Integer getTdhqfs() {
        return super.getInt("tdhqfs");
    }

    public void setTdhqfs(Integer tdhqfs) {
        super.set("tdhqfs", tdhqfs);
    }

    /**
     * TDSFDSJFA
     */
    public Integer getTdsfdsjfa() {
        return super.getInt("tdsfdsjfa");
    }

    public void setTdsfdsjfa(Integer tdsfdsjfa) {
        super.set("tdsfdsjfa", tdsfdsjfa);
    }

    /**
     * SFWCQYPG
     */
    public Integer getSfwcqypg() {
        return super.getInt("sfwcqypg");
    }

    public void setSfwcqypg(Integer sfwcqypg) {
        super.set("sfwcqypg", sfwcqypg);
    }

    /**
     * JZMJ
     */
    public Double getJzmj() {
        return super.getDouble("jzmj");
    }

    public void setJzmj(Double jzmj) {
        super.set("jzmj", jzmj);
    }

    /**
     * XMTZLY
     */
    public Integer getXmtzly() {
        return super.getInt("xmtzly");
    }

    public void setXmtzly(Integer xmtzly) {
        super.set("xmtzly", xmtzly);
    }

    /**
     * XMZJLY
     */
    public Integer getXmzjly() {
        return super.getInt("xmzjly");
    }

    public void setXmzjly(Integer xmzjly) {
        super.set("xmzjly", xmzjly);
    }

    /**
     * DRAFT
     */
    public String getDraft() {
        return super.get("draft");
    }

    public void setDraft(String draft) {
        super.set("draft", draft);
    }

    /**
     * ISSENDZJ
     */
    public String getIssendzj() {
        return super.get("issendzj");
    }

    public void setIssendzj(String issendzj) {
        super.set("issendzj", issendzj);
    }

    /**
     * GBHY
     */
    public String getGbhy() {
        return super.get("gbhy");
    }

    public void setGbhy(String gbhy) {
        super.set("gbhy", gbhy);
    }

    /**
     * XMZJSX
     */
    public String getXmzjsx() {
        return super.get("xmzjsx");
    }

    public void setXmzjsx(String xmzjsx) {
        super.set("xmzjsx", xmzjsx);
    }

    /**
     * xmzb
     */
    public String getXmzb() {
        return super.get("xmzb");
    }

    public void setXmzb(String xmzb) {
        super.set("xmzb", xmzb);
    }

    /**
     * GCFL
     */
    public String getGcfl() {
        return super.get("gcfl");
    }

    public void setGcfl(String gcfl) {
        super.set("gcfl", gcfl);
    }

    /**
     * is_qianqi
     */
    public String getIs_qianqi() {
        return super.get("is_qianqi");
    }

    public void setIs_qianqi(String is_qianqi) {
        super.set("is_qianqi", is_qianqi);
    }

    /**
     * excavationsite
     */
    public String getExcavationsite() {
        return super.get("excavationsite");
    }

    public void setExcavationsite(String excavationsite) {
        super.set("excavationsite", excavationsite);
    }

    /**
     * excavationarea
     */
    public String getExcavationarea() {
        return super.get("excavationarea");
    }

    public void setExcavationarea(String excavationarea) {
        super.set("excavationarea", excavationarea);
    }

    /**
     * excavationreasons
     */
    public String getExcavationreasons() {
        return super.get("excavationreasons");
    }

    public void setExcavationreasons(String excavationreasons) {
        super.set("excavationreasons", excavationreasons);
    }

    /**
     * excavationdate
     */
    public String getExcavationdate() {
        return super.get("excavationdate");
    }

    public void setExcavationdate(String excavationdate) {
        super.set("excavationdate", excavationdate);
    }

    /**
     * excavationnote
     */
    public String getExcavationnote() {
        return super.get("excavationnote");
    }

    public void setExcavationnote(String excavationnote) {
        super.set("excavationnote", excavationnote);
    }

    /**
     * occupationsite
     */
    public String getOccupationsite() {
        return super.get("occupationsite");
    }

    public void setOccupationsite(String occupationsite) {
        super.set("occupationsite", occupationsite);
    }

    /**
     * occupationarea
     */
    public String getOccupationarea() {
        return super.get("occupationarea");
    }

    public void setOccupationarea(String occupationarea) {
        super.set("occupationarea", occupationarea);
    }

    /**
     * occupationreasons
     */
    public String getOccupationreasons() {
        return super.get("occupationreasons");
    }

    public void setOccupationreasons(String occupationreasons) {
        super.set("occupationreasons", occupationreasons);
    }

    /**
     * occupationdate
     */
    public String getOccupationdate() {
        return super.get("occupationdate");
    }

    public void setOccupationdate(String occupationdate) {
        super.set("occupationdate", occupationdate);
    }

    /**
     * occupationnote
     */
    public String getOccupationnote() {
        return super.get("occupationnote");
    }

    public void setOccupationnote(String occupationnote) {
        super.set("occupationnote", occupationnote);
    }

    /**
     * chcode
     */
    public String getChcode() {
        return super.get("chcode");
    }

    public void setChcode(String chcode) {
        super.set("chcode", chcode);
    }

    /**
     * gdjstatus
     */
    public String getGdjstatus() {
        return super.get("gdjstatus");
    }

    public void setGdjstatus(String gdjstatus) {
        super.set("gdjstatus", gdjstatus);
    }

    /**
     * is_cehuiorg
     */
    public String getIs_cehuiorg() {
        return super.get("is_cehuiorg");
    }

    public void setIs_cehuiorg(String is_cehuiorg) {
        super.set("is_cehuiorg", is_cehuiorg);
    }

    /**
     * lxpfcliengguid
     */
    public String getLxpfcliengguid() {
        return super.get("lxpfcliengguid");
    }

    public void setLxpfcliengguid(String lxpfcliengguid) {
        super.set("lxpfcliengguid", lxpfcliengguid);
    }

    /**
     * jsgcfliengguid
     */
    public String getJsgcfliengguid() {
        return super.get("jsgcfliengguid");
    }

    public void setJsgcfliengguid(String jsgcfliengguid) {
        super.set("jsgcfliengguid", jsgcfliengguid);
    }

    /**
     * swstatus
     */
    public String getSwstatus() {
        return super.get("swstatus");
    }

    public void setSwstatus(String swstatus) {
        super.set("swstatus", swstatus);
    }

    /**
     * SBSJ
     */
    public Date getSbsj() {
        return super.getDate("sbsj");
    }

    public void setSbsj(Date sbsj) {
        super.set("sbsj", sbsj);
    }

    /**
     * JSDWLX
     */
    public Integer getJsdwlx() {
        return super.getInt("jsdwlx");
    }

    public void setJsdwlx(Integer jsdwlx) {
        super.set("jsdwlx", jsdwlx);
    }

    /**
     * GBHYDMFBND
     */
    public String getGbhydmfbnd() {
        return super.get("gbhydmfbnd");
    }

    public void setGbhydmfbnd(String gbhydmfbnd) {
        super.set("gbhydmfbnd", gbhydmfbnd);
    }

    /**
     * XMSFWQBJ
     */
    public Integer getXmsfwqbj() {
        return super.getInt("xmsfwqbj");
    }

    public void setXmsfwqbj(Integer xmsfwqbj) {
        super.set("xmsfwqbj", xmsfwqbj);
    }

    /**
     * XMWQBJSJ
     */
    public Date getXmwqbjsj() {
        return super.getDate("xmwqbjsj");
    }

    public void setXmwqbjsj(Date xmwqbjsj) {
        super.set("xmwqbjsj", xmwqbjsj);
    }

    /**
     * SFXXGC
     */
    public Integer getSfxxgc() {
        return super.getInt("sfxxgc");
    }

    public void setSfxxgc(Integer sfxxgc) {
        super.set("sfxxgc", sfxxgc);
    }

    /**
     * CD
     */
    public Double getCd() {
        return super.getDouble("cd");
    }

    public void setCd(Double cd) {
        super.set("cd", cd);
    }

    /**
     * GCFW
     */
    public String getGcfw() {
        return super.get("gcfw");
    }

    public void setGcfw(String gcfw) {
        super.set("gcfw", gcfw);
    }

    /**
     * GCHYFL
     */
    public String getGchyfl() {
        return super.get("gchyfl");
    }

    public void setGchyfl(String gchyfl) {
        super.set("gchyfl", gchyfl);
    }

    /**
     * XMJWDZB
     */
    public String getXmjwdzb() {
        return super.get("xmjwdzb");
    }

    public void setXmjwdzb(String xmjwdzb) {
        super.set("xmjwdzb", xmjwdzb);
    }

    /**
     * JSDDXZQH
     */
    public String getJsddxzqh() {
        return super.get("jsddxzqh");
    }

    public void setJsddxzqh(String jsddxzqh) {
        super.set("jsddxzqh", jsddxzqh);
    }

    /**
     * LXLX
     */
    public Integer getLxlx() {
        return super.getInt("lxlx");
    }

    public void setLxlx(Integer lxlx) {
        super.set("lxlx", lxlx);
    }

    /**
     * issendzj_v3
     */
    public String getIssendzj_v3() {
        return super.get("issendzj_v3");
    }

    public void setIssendzj_v3(String issendzj_v3) {
        super.set("issendzj_v3", issendzj_v3);
    }

    /**
     * zdxmlx
     */
    public String getZdxmlx() {
        return super.get("zdxmlx");
    }

    public void setZdxmlx(String zdxmlx) {
        super.set("zdxmlx", zdxmlx);
    }

    /**
     * zdxmnf
     */
    public String getZdxmnf() {
        return super.get("zdxmnf");
    }

    public void setZdxmnf(String zdxmnf) {
        super.set("zdxmnf", zdxmnf);
    }

    /**
     * zdxmyj
     */
    public String getZdxmyj() {
        return super.get("zdxmyj");
    }

    public void setZdxmyj(String zdxmyj) {
        super.set("zdxmyj", zdxmyj);
    }

    /**
     * bbryxm
     */
    public String getBbryxm() {
        return super.get("bbryxm");
    }

    public void setBbryxm(String bbryxm) {
        super.set("bbryxm", bbryxm);
    }

    /**
     * bbrylxfs
     */
    public String getBbrylxfs() {
        return super.get("bbrylxfs");
    }

    public void setBbrylxfs(String bbrylxfs) {
        super.set("bbrylxfs", bbrylxfs);
    }

    /**
     * bbrydw
     */
    public String getBbrydw() {
        return super.get("bbrydw");
    }

    public void setBbrydw(String bbrydw) {
        super.set("bbrydw", bbrydw);
    }

    /**
     * isusewater
     */
    public String getIsusewater() {
        return super.get("isusewater");
    }

    public void setIsusewater(String isusewater) {
        super.set("isusewater", isusewater);
    }

    /**
     * ismovewater
     */
    public String getIsmovewater() {
        return super.get("ismovewater");
    }

    public void setIsmovewater(String ismovewater) {
        super.set("ismovewater", ismovewater);
    }

    /**
     * isuseelec
     */
    public String getIsuseelec() {
        return super.get("isuseelec");
    }

    public void setIsuseelec(String isuseelec) {
        super.set("isuseelec", isuseelec);
    }

    /**
     * isgas
     */
    public String getIsgas() {
        return super.get("isgas");
    }

    public void setIsgas(String isgas) {
        super.set("isgas", isgas);
    }

    /**
     * iswarm
     */
    public String getIswarm() {
        return super.get("iswarm");
    }

    public void setIswarm(String iswarm) {
        super.set("iswarm", iswarm);
    }

    /**
     * istx
     */
    public String getIstx() {
        return super.get("istx");
    }

    public void setIstx(String istx) {
        super.set("istx", istx);
    }

    /**
     * isgd
     */
    public String getIsgd() {
        return super.get("isgd");
    }

    public void setIsgd(String isgd) {
        super.set("isgd", isgd);
    }

    /**
     * iswaijie
     */
    public String getIswaijie() {
        return super.get("iswaijie");
    }

    public void setIswaijie(String iswaijie) {
        super.set("iswaijie", iswaijie);
    }

    /**
     * islhysyjs
     */
    public String getIslhysyjs() {
        return super.get("islhysyjs");
    }

    public void setIslhysyjs(String islhysyjs) {
        super.set("islhysyjs", islhysyjs);
    }

    /**
     * xfyslevel
     */
    public String getXfyslevel() {
        return super.get("xfyslevel");
    }

    public void setXfyslevel(String xfyslevel) {
        super.set("xfyslevel", xfyslevel);
    }

    /**
     * xfystype
     */
    public String getXfystype() {
        return super.get("xfystype");
    }

    public void setXfystype(String xfystype) {
        super.set("xfystype", xfystype);
    }

    /**
     * sgdepartment
     */
    public String getSgdepartment() {
        return super.get("sgdepartment");
    }

    public void setSgdepartment(String sgdepartment) {
        super.set("sgdepartment", sgdepartment);
    }

    /**
     * sgdepartmentaddress
     */
    public String getSgdepartmentaddress() {
        return super.get("sgdepartmentaddress");
    }

    public void setSgdepartmentaddress(String sgdepartmentaddress) {
        super.set("sgdepartmentaddress", sgdepartmentaddress);
    }

    /**
     * sgdepartmentcreditcode
     */
    public String getSgdepartmentcreditcode() {
        return super.get("sgdepartmentcreditcode");
    }

    public void setSgdepartmentcreditcode(String sgdepartmentcreditcode) {
        super.set("sgdepartmentcreditcode", sgdepartmentcreditcode);
    }

    /**
     * sgdepartmentlegal
     */
    public String getSgdepartmentlegal() {
        return super.get("sgdepartmentlegal");
    }

    public void setSgdepartmentlegal(String sgdepartmentlegal) {
        super.set("sgdepartmentlegal", sgdepartmentlegal);
    }

    /**
     * sgdepartmentlegalid
     */
    public String getSgdepartmentlegalid() {
        return super.get("sgdepartmentlegalid");
    }

    public void setSgdepartmentlegalid(String sgdepartmentlegalid) {
        super.set("sgdepartmentlegalid", sgdepartmentlegalid);
    }

    /**
     * sbreason
     */
    public String getSbreason() {
        return super.get("sbreason");
    }

    public void setSbreason(String sbreason) {
        super.set("sbreason", sbreason);
    }

    /**
     * ghxk_cliengguid
     */
    public String getGhxk_cliengguid() {
        return super.get("ghxk_cliengguid");
    }

    public void setGhxk_cliengguid(String ghxk_cliengguid) {
        super.set("ghxk_cliengguid", ghxk_cliengguid);
    }

    /**
     * sgfa_cliengguid
     */
    public String getSgfa_cliengguid() {
        return super.get("sgfa_cliengguid");
    }

    public void setSgfa_cliengguid(String sgfa_cliengguid) {
        super.set("sgfa_cliengguid", sgfa_cliengguid);
    }

    /**
     * syt_cliengguid
     */
    public String getSyt_cliengguid() {
        return super.get("syt_cliengguid");
    }

    public void setSyt_cliengguid(String syt_cliengguid) {
        super.set("syt_cliengguid", syt_cliengguid);
    }

    /**
     * zgrname
     */
    public String getZgrname() {
        return super.get("zgrname");
    }

    public void setZgrname(String zgrname) {
        super.set("zgrname", zgrname);
    }

    /**
     * zgrid
     */
    public String getZgrid() {
        return super.get("zgrid");
    }

    public void setZgrid(String zgrid) {
        super.set("zgrid", zgrid);
    }

    /**
     * zgrmobile
     */
    public String getZgrmobile() {
        return super.get("zgrmobile");
    }

    public void setZgrmobile(String zgrmobile) {
        super.set("zgrmobile", zgrmobile);
    }

    /**
     * jbrname
     */
    public String getJbrname() {
        return super.get("jbrname");
    }

    public void setJbrname(String jbrname) {
        super.set("jbrname", jbrname);
    }

    /**
     * jbrid
     */
    public String getJbrid() {
        return super.get("jbrid");
    }

    public void setJbrid(String jbrid) {
        super.set("jbrid", jbrid);
    }

    /**
     * jbrmobile
     */
    public String getJbrmobile() {
        return super.get("jbrmobile");
    }

    public void setJbrmobile(String jbrmobile) {
        super.set("jbrmobile", jbrmobile);
    }

    /**
     * itemid
     */
    public String getItemid() {
        return super.get("itemid");
    }

    public void setItemid(String itemid) {
        super.set("itemid", itemid);
    }

    /**
     * 项目所属辖区
     */
    public String getProjectdistrict() {
        return super.get("projectdistrict");
    }

    public void setProjectdistrict(String projectdistrict) {
        super.set("projectdistrict", projectdistrict);
    }

    /**
     * 重点项目类型
     */
    public String getKeyprojecttype() {
        return super.get("keyprojecttype");
    }

    public void setKeyprojecttype(String keyprojecttype) {
        super.set("keyprojecttype", keyprojecttype);
    }

    /**
     * 投资方式
     */
    public String getInvestmentmethod() {
        return super.get("investmentmethod");
    }

    public void setInvestmentmethod(String investmentmethod) {
        super.set("investmentmethod", investmentmethod);
    }

    /**
     * 项目性质
     */
    public String getProjectnature() {
        return super.get("projectnature");
    }

    public void setProjectnature(String projectnature) {
        super.set("projectnature", projectnature);
    }

    /**
     * 项目类型
     */
    public String getProjectcategory() {
        return super.get("projectcategory");
    }

    public void setProjectcategory(String projectcategory) {
        super.set("projectcategory", projectcategory);
    }

    /**
     * 形象进度
     */
    public String getImageprogress() {
        return super.get("imageprogress");
    }

    public void setImageprogress(String imageprogress) {
        super.set("imageprogress", imageprogress);
    }

    /**
     * 进度详情
     */
    public String getProgressdetails() {
        return super.get("progressdetails");
    }

    public void setProgressdetails(String progressdetails) {
        super.set("progressdetails", progressdetails);
    }

    /**
     * 变更状态
     */
    public String getChangestatus() {
        return super.get("changestatus");
    }

    public void setChangestatus(String changestatus) {
        super.set("changestatus", changestatus);
    }

    /**
     * 支持类型
     */
    public String getSupporttype() {
        return super.get("supporttype");
    }

    public void setSupporttype(String supporttype) {
        super.set("supporttype", supporttype);
    }

    /**
     * 资金到位情况说明
     */
    public String getFundsreceived() {
        return super.get("fundsreceived");
    }

    public void setFundsreceived(String fundsreceived) {
        super.set("fundsreceived", fundsreceived);
    }

    /**
     * 所属产业链
     */
    public String getIndustrialchain() {
        return super.get("industrialchain");
    }

    public void setIndustrialchain(String industrialchain) {
        super.set("industrialchain", industrialchain);
    }

    /**
     * 所属园区
     */
    public String getIndustrialpark() {
        return super.get("industrialpark");
    }

    public void setIndustrialpark(String industrialpark) {
        super.set("industrialpark", industrialpark);
    }

    public String getIndustrialparkremark() {
        return super.get("industrialparkremark");
    }

    public void setIndustrialparkremark(String industrialparkremark) {
        super.set("industrialparkremark", industrialparkremark);
    }

    public String getInvestment_percentage() {
        return super.get("investment_percentage");
    }

    public void setInvestment_percentage(String investment_percentage) {
        super.set("investment_percentage", investment_percentage);
    }
}
