package com.epoint.zwdt.xmgxh.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 住建部_项目申请材料及其他附件信息表
 * 
 * @作者  zhpengsy
 * @version [版本号, 2018-11-16 15:09:37]
 */
@Entity(table = "SPGL_XMSQCLJQTFJXXB_V3", id = "rowguid")
public class SpglxmsqcljqtfjxxbV3 extends BaseEntity implements Cloneable
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
    * 流水号
    */
    public Long getLsh() {
        return super.getLong("lsh");
    }

    public void setLsh(Long lsh) {
        super.set("lsh", lsh);
    }

    /**
    * 地方数据主键
    */
    public String getDfsjzj() {
        return super.get("dfsjzj");
    }

    public void setDfsjzj(String dfsjzj) {
        super.set("dfsjzj", dfsjzj);
    }
    
    /**
    * 行政区划代码
    */
    public String getXzqhdm() {
        return super.get("xzqhdm");
    }

    public void setXzqhdm(String xzqhdm) {
        super.set("xzqhdm", xzqhdm);
    }

    /**
    * 工程代码
    */
    public String getGcdm() {
        return super.get("gcdm");
    }

    public void setGcdm(String gcdm) {
        super.set("gcdm", gcdm);
    }

    /**
    * 审批事项实例编码
    */
    public String getSpsxslbm() {
        return super.get("spsxslbm");
    }

    public void setSpsxslbm(String spsxslbm) {
        super.set("spsxslbm", spsxslbm);
    }
    /**
     * 并联审批实例编码
     */
    public String getBlspslbm() {
        return super.get("blspslbm");
    }
    
    public void setBlspslbm(String blspslbm) {
        super.set("blspslbm", blspslbm);
    }
    /**
     * 材料目录编号
     */
    public String getClmlbh() {
         return super.get("clmlbh");
    }

    public void setClmlbh(String clmlbh) {
         super.set("clmlbh", clmlbh);
    }

    /**
    * 材料名称
    */
    public String getClmc() {
        return super.get("clmc");
    }

    public void setClmc(String clmc) {
        super.set("clmc", clmc);
    }
    /**
     * 材料分类
     */
    public Integer getClfl() {
        return super.getInt("clfl");
    }
    
    public void setClfl(Integer clfl) {
        super.set("clfl", clfl);
    }

    /**
    * 材料类型
    */
    public String getCllx() {
        return super.get("cllx");
    }

    public void setCllx(String cllx) {
        super.set("cllx", cllx);
    }

    /**
    * 材料ID
    */
    public String getClid() {
        return super.get("clid");
    }

    public void setClid(String clid) {
        super.set("clid", clid);
    }
    
    /**
     * 收取方式
     */
    public Integer getSqfs() {
         return super.get("sqfs");
    }

    public void setSqfs(Integer sqfs) {
         super.set("sqfs", sqfs);
    }
    
    /**
     * 收取数量
     */
    public Integer getSqsl() {
         return super.get("sqsl");
    }

    public void setSqsl(Integer sqsl) {
         super.set("sqsl", sqsl);
    }
    
    /**
     * 证照名称
     */
    public String getZzmc() {
         return super.get("zzmc");
    }

    public void setZzmc(String zzmc) {
         super.set("zzmc", zzmc);
    }
    
    
    /**
     * 证照编号
     */
    public String getZzbh() {
         return super.get("zzbh");
    }

    public void setZzbh(String zzbh) {
         super.set("zzbh", zzbh);
    }
    
    
    /**
     * 证照类型
     */
    public String getZzlx() {
         return super.get("zzlx");
    }

    public void setZzlx(String zzlx) {
         super.set("zzlx", zzlx);
    }
    
    /**
     * 证照标识
     */
    public String getZzbs() {
         return super.get("zzbs");
    }

    public void setZzbs(String zzbs) {
         super.set("zzbs", zzbs);
    }
    
    /**
     * 电子证照文件路径
     */
    public String getDzzzwjlj() {
         return super.get("dzzzwjlj");
    }

    public void setDzzzwjlj(String dzzzwjlj) {
         super.set("dzzzwjlj", dzzzwjlj);
    }


    /**
    * 数据有效标识
    */
    public Integer getSjyxbs() {
        return super.getInt("sjyxbs");
    }

    public void setSjyxbs(Integer sjyxbs) {
        super.set("sjyxbs", sjyxbs);
    }

    /**
    * 数据无效原因
    */
    public String getSjwxyy() {
        return super.get("sjwxyy");
    }

    public void setSjwxyy(String sjwxyy) {
        super.set("sjwxyy", sjwxyy);
    }

    /**
    * 数据上传状态
    */
    public Integer getSjsczt() {
        return super.getInt("sjsczt");
    }

    public void setSjsczt(Integer sjsczt) {
        super.set("sjsczt", sjsczt);
    }

    /**
    * 失败原因
    */
    public String getSbyy() {
        return super.get("sbyy");
    }

    public void setSbyy(String sbyy) {
        super.set("sbyy", sbyy);
    }

}
