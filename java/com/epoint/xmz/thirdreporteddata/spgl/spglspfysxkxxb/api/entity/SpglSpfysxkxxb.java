package com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 商品房预售信息许可表实体
 *
 * @version [版本号, 2024-04-02 15:25:51]
 * @作者 Administrator
 */
@Entity(table = "SPGL_SPFYSXKXXB", id = "rowguid")
public class SpglSpfysxkxxb extends BaseEntity implements Cloneable {
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
    public Integer getLsh() {
        return super.getInt("lsh");
    }

    public void setLsh(Integer lsh) {
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
     * 开发单位名称
     */
    public String getKfdwmc() {
        return super.get("kfdwmc");
    }

    public void setKfdwmc(String kfdwmc) {
        super.set("kfdwmc", kfdwmc);
    }

    /**
     * 开发单位代码
     */
    public String getKfdwdm() {
        return super.get("kfdwdm");
    }

    public void setKfdwdm(String kfdwdm) {
        super.set("kfdwdm", kfdwdm);
    }

    /**
     * 商品房预售许可证编号
     */
    public String getSpfysxkzbh() {
        return super.get("spfysxkzbh");
    }

    public void setSpfysxkzbh(String spfysxkzbh) {
        super.set("spfysxkzbh", spfysxkzbh);
    }

    /**
     * 项目名称
     */
    public String getXmmc() {
        return super.get("xmmc");
    }

    public void setXmmc(String xmmc) {
        super.set("xmmc", xmmc);
    }

    /**
     * 项目坐落
     */
    public String getXmzl() {
        return super.get("xmzl");
    }

    public void setXmzl(String xmzl) {
        super.set("xmzl", xmzl);
    }

    /**
     * 商品房预售资金监管银行
     */
    public String getZjjgyh() {
        return super.get("zjjgyh");
    }

    public void setZjjgyh(String zjjgyh) {
        super.set("zjjgyh", zjjgyh);
    }

    /**
     * 商品房预售资金监管账号
     */
    public String getZjjgzh() {
        return super.get("zjjgzh");
    }

    public void setZjjgzh(String zjjgzh) {
        super.set("zjjgzh", zjjgzh);
    }

    /**
     * 备注
     */
    public String getBz() {
        return super.get("bz");
    }

    public void setBz(String bz) {
        super.set("bz", bz);
    }

    /**
     * 批准预售面积
     */
    public Double getYszmj() {
        return super.getDouble("yszmj");
    }

    public void setYszmj(Double yszmj) {
        super.set("yszmj", yszmj);
    }

    /**
     * 预售总栋数
     */
    public Integer getYszds() {
        return super.getInt("yszds");
    }

    public void setYszds(Integer yszds) {
        super.set("yszds", yszds);
    }

    /**
     * 预售总套数
     */
    public Integer getYszts() {
        return super.getInt("yszts");
    }

    public void setYszts(Integer yszts) {
        super.set("yszts", yszts);
    }

    /**
     * 其中
     */
    public String getQz() {
        return super.get("qz");
    }

    public void setQz(String qz) {
        super.set("qz", qz);
    }

    /**
     * 批准楼号
     */
    public String getBzlh() {
        return super.get("bzlh");
    }

    public void setBzlh(String bzlh) {
        super.set("bzlh", bzlh);
    }

    /**
     * 层数
     */
    public Integer getCs() {
        return super.getInt("cs");
    }

    public void setCs(Integer cs) {
        super.set("cs", cs);
    }

    /**
     * 不动产单元代码
     */
    public String getBdcdydm() {
        return super.get("bdcdydm");
    }

    public void setBdcdydm(String bdcdydm) {
        super.set("bdcdydm", bdcdydm);
    }

    /**
     * 发证机关
     */
    public String getFzjg() {
        return super.get("fzjg");
    }

    public void setFzjg(String fzjg) {
        super.set("fzjg", fzjg);
    }

    /**
     * 发证机关统一社会信用代码
     */
    public String getFzjgtyshxydm() {
        return super.get("fzjgtyshxydm");
    }

    public void setFzjgtyshxydm(String fzjgtyshxydm) {
        super.set("fzjgtyshxydm", fzjgtyshxydm);
    }

    /**
     * 发证日期
     */
    public Date getFzrq() {
        return super.getDate("fzrq");
    }

    public void setFzrq(Date fzrq) {
        super.set("fzrq", fzrq);
    }

    /**
     * 证照有效期起始日期
     */
    public Date getYxqqssj() {
        return super.getDate("yxqqssj");
    }

    public void setYxqqssj(Date yxqqssj) {
        super.set("yxqqssj", yxqqssj);
    }

    /**
     * 证照有效期截止日期
     */
    public Date getYxqjzsj() {
        return super.getDate("yxqjzsj");
    }

    public void setYxqjzsj(Date yxqjzsj) {
        super.set("yxqjzsj", yxqjzsj);
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