package com.epoint.auditsp.yqxm.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 工程基本信息表实体
 * 
 * @作者  scr
 * @version [版本号, 2019-07-02 19:33:49]
 */
@Entity(table = "ST_SPGL_GCJBXXB", id = "rowguid")
public class StSpglGcjbxxb extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * CREATETIMESTAMP
     */
    public Date getCreatetimestamp() {
        return super.getDate("createtimestamp");
    }

    public void setCreatetimestamp(Date createtimestamp) {
        super.set("createtimestamp", createtimestamp);
    }

    /**
    * DATASOURCE
    */
    public String getDatasource() {
        return super.get("datasource");
    }

    public void setDatasource(String datasource) {
        super.set("datasource", datasource);
    }

    /**
    * TIMESTAMP
    */
    public Date getTimestamp() {
        return super.getDate("timestamp");
    }

    public void setTimestamp(Date timestamp) {
        super.set("timestamp", timestamp);
    }

    /**
    * SBYY
    */
    public String getSbyy() {
        return super.get("sbyy");
    }

    public void setSbyy(String sbyy) {
        super.set("sbyy", sbyy);
    }

    /**
    * SJSCZT
    */
    public Integer getSjsczt() {
        return super.getInt("sjsczt");
    }

    public void setSjsczt(Integer sjsczt) {
        super.set("sjsczt", sjsczt);
    }

    /**
    * SJWXYY
    */
    public String getSjwxyy() {
        return super.get("sjwxyy");
    }

    public void setSjwxyy(String sjwxyy) {
        super.set("sjwxyy", sjwxyy);
    }

    /**
    * SJYXBS
    */
    public Integer getSjyxbs() {
        return super.getInt("sjyxbs");
    }

    public void setSjyxbs(Integer sjyxbs) {
        super.set("sjyxbs", sjyxbs);
    }

    /**
    * SPLCBBH
    */
    public Double getSplcbbh() {
        return super.getDouble("splcbbh");
    }

    public void setSplcbbh(Double splcbbh) {
        super.set("splcbbh", splcbbh);
    }

    /**
    * SPLCBM
    */
    public String getSplcbm() {
        return super.get("splcbm");
    }

    public void setSplcbm(String splcbm) {
        super.set("splcbm", splcbm);
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
    * JZMJ
    */
    public Double getJzmj() {
        return super.getDouble("jzmj");
    }

    public void setJzmj(Double jzmj) {
        super.set("jzmj", jzmj);
    }

    /**
    * YDMJ
    */
    public Double getYdmj() {
        return super.getDouble("ydmj");
    }

    public void setYdmj(Double ydmj) {
        super.set("ydmj", ydmj);
    }

    /**
    * JSGMJNR
    */
    public String getJsgmjnr() {
        return super.get("jsgmjnr");
    }

    public void setJsgmjnr(String jsgmjnr) {
        super.set("jsgmjnr", jsgmjnr);
    }

    /**
    * XMJSDDY
    */
    public Double getXmjsddy() {
        return super.getDouble("xmjsddy");
    }

    public void setXmjsddy(Double xmjsddy) {
        super.set("xmjsddy", xmjsddy);
    }

    /**
    * XMJSDDX
    */
    public Double getXmjsddx() {
        return super.getDouble("xmjsddx");
    }

    public void setXmjsddx(Double xmjsddx) {
        super.set("xmjsddx", xmjsddx);
    }

    /**
    * JSDD
    */
    public String getJsdd() {
        return super.get("jsdd");
    }

    public void setJsdd(String jsdd) {
        super.set("jsdd", jsdd);
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
    * ZTZE
    */
    public Double getZtze() {
        return super.getDouble("ztze");
    }

    public void setZtze(Double ztze) {
        super.set("ztze", ztze);
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
    * XMSFWQBJ
    */
    public Integer getXmsfwqbj() {
        return super.getInt("xmsfwqbj");
    }

    public void setXmsfwqbj(Integer xmsfwqbj) {
        super.set("xmsfwqbj", xmsfwqbj);
    }

    /**
    * NJCSJ
    */
    public Date getNjcsj() {
        return super.getDate("njcsj");
    }

    public void setNjcsj(Date njcsj) {
        super.set("njcsj", njcsj);
    }

    /**
    * NKGSJ
    */
    public Date getNkgsj() {
        return super.getDate("nkgsj");
    }

    public void setNkgsj(Date nkgsj) {
        super.set("nkgsj", nkgsj);
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
    * GBHYDMFBND
    */
    public String getGbhydmfbnd() {
        return super.get("gbhydmfbnd");
    }

    public void setGbhydmfbnd(String gbhydmfbnd) {
        super.set("gbhydmfbnd", gbhydmfbnd);
    }

    /**
    * XMZJSX
    */
    public Integer getXmzjsx() {
        return super.getInt("xmzjsx");
    }

    public void setXmzjsx(Integer xmzjsx) {
        super.set("xmzjsx", xmzjsx);
    }

    /**
    * JSXZ
    */
    public Integer getJsxz() {
        return super.getInt("jsxz");
    }

    public void setJsxz(Integer jsxz) {
        super.set("jsxz", jsxz);
    }

    /**
    * GCFL
    */
    public Integer getGcfl() {
        return super.getInt("gcfl");
    }

    public void setGcfl(Integer gcfl) {
        super.set("gcfl", gcfl);
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
    * SPLCLX
    */
    public Integer getSplclx() {
        return super.getInt("splclx");
    }

    public void setSplclx(Integer splclx) {
        super.set("splclx", splclx);
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
    * TDSFDSJFA
    */
    public Integer getTdsfdsjfa() {
        return super.getInt("tdsfdsjfa");
    }

    public void setTdsfdsjfa(Integer tdsfdsjfa) {
        super.set("tdsfdsjfa", tdsfdsjfa);
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
    * XMTZLY
    */
    public Integer getXmtzly() {
        return super.getInt("xmtzly");
    }

    public void setXmtzly(Integer xmtzly) {
        super.set("xmtzly", xmtzly);
    }

    /**
    * QJDGCDM
    */
    public String getQjdgcdm() {
        return super.get("qjdgcdm");
    }

    public void setQjdgcdm(String qjdgcdm) {
        super.set("qjdgcdm", qjdgcdm);
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
    * GCDM
    */
    public String getGcdm() {
        return super.get("gcdm");
    }

    public void setGcdm(String gcdm) {
        super.set("gcdm", gcdm);
    }

    /**
    * XMMC
    */
    public String getXmmc() {
        return super.get("xmmc");
    }

    public void setXmmc(String xmmc) {
        super.set("xmmc", xmmc);
    }

    /**
    * XMDM
    */
    public String getXmdm() {
        return super.get("xmdm");
    }

    public void setXmdm(String xmdm) {
        super.set("xmdm", xmdm);
    }

    /**
    * XZQHDM
    */
    public String getXzqhdm() {
        return super.get("xzqhdm");
    }

    public void setXzqhdm(String xzqhdm) {
        super.set("xzqhdm", xzqhdm);
    }

    /**
    * DFSJZJ
    */
    public String getDfsjzj() {
        return super.get("dfsjzj");
    }

    public void setDfsjzj(String dfsjzj) {
        super.set("dfsjzj", dfsjzj);
    }

    /**
    * LSH
    */
    public Integer getLsh() {
        return super.getInt("lsh");
    }

    public void setLsh(Integer lsh) {
        super.set("lsh", lsh);
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
    * YearFlag
    */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
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
    * OperateDate
    */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
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
    * BelongXiaQuCode
    */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

}
