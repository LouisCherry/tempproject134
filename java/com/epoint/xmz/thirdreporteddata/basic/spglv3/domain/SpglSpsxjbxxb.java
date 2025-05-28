package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 住建部_审批事项基本信息表实体
 *
 * @version [版本号, 2018-11-16 15:09:56]
 * @作者 zhpengsy
 */
@Entity(table = "SPGL_SPSXJBXXB_V3", id = "rowguid")
public class SpglSpsxjbxxb extends BaseEntity implements Cloneable {
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
     * 审批事项编码
     */
    public String getSpsxbm() {
        return super.get("spsxbm");
    }

    public void setSpsxbm(String spsxbm) {
        super.set("spsxbm", spsxbm);
    }

    /**
     * 审批事项版本号
     */
    public Double getSpsxbbh() {
        return super.getDouble("spsxbbh");
    }

    public void setSpsxbbh(Double spsxbbh) {
        super.set("spsxbbh", spsxbbh);
    }


    /**
     * 审批事项名称
     */
    public String getSpsxmc() {
        return super.get("spsxmc");
    }

    public void setSpsxmc(String spsxmc) {
        super.set("spsxmc", spsxmc);
    }

    /**
     * 对应标准审批事项编码
     */
    public String getDybzspsxbm() {
        return super.get("dybzspsxbm");
    }

    public void setDybzspsxbm(String dybzspsxbm) {
        super.set("dybzspsxbm", dybzspsxbm);
    }

    /**
     * 事项办事指南地址
     */
    public String getSxbszndz() {
        return super.get("sxbszndz");
    }

    public void setSxbszndz(String sxbszndz) {
        super.set("sxbszndz", sxbszndz);
    }

    /**
     * 事项在线申报地址
     */
    public String getSxzxsbdz() {
        return super.get("sxzxsbdz");
    }

    public void setSxzxsbdz(String sxzxsbdz) {
        super.set("sxzxsbdz", sxzxsbdz);
    }

    /**
     * 基本编码
     */
    public String getJbbm() {
        return super.get("jbbm");
    }

    public void setJbbm(String jbbm) {
        super.set("jbbm", jbbm);
    }

    /**
     * 实施编码
     */
    public String getSsbm() {
        return super.get("ssbm");
    }

    public void setSsbm(String ssbm) {
        super.set("ssbm", ssbm);
    }

    /**
     * 业务办理项编码
     */
    public String getYwblxbm() {
        return super.get("ywblxbm");
    }

    public void setYwblxbm(String ywblxbm) {
        super.set("ywblxbm", ywblxbm);
    }

    /**
     * 业务办理项拓展码
     */
    public String getYwblxkzm() {
        return super.get("ywblxkzm");
    }

    public void setYwblxkzm(String ywblxkzm) {
        super.set("ywblxkzm", ywblxkzm);
    }

    /**
     * 事项类型
     */
    public String getSxlx() {
        return super.get("sxlx");
    }

    public void setSxlx(String sxlx) {
        super.set("sxlx", sxlx);
    }


    /**
     * 设定依据
     */
    public String getSdyj() {
        return super.get("sdyj");
    }

    public void setSdyj(String sdyj) {
        super.set("sdyj", sdyj);
    }

    /**
     * 权利来源
     */
    public Integer getQlly() {
        return super.get("qlly");
    }

    public void setQlly(Integer qlly) {
        super.set("qlly", qlly);
    }

    /**
     * 行使层级
     */
    public Integer getXscj() {
        return super.get("xscj");
    }

    public void setXscj(Integer xscj) {
        super.set("xscj", xscj);
    }

    /**
     * 事项状态
     */
    public Integer getSxzt() {
        return super.get("sxzt");
    }

    public void setSxzt(Integer sxzt) {
        super.set("sxzt", sxzt);
    }

    /**
     * 实施主体
     */
    public String getSszt() {
        return super.get("sszt");
    }

    public void setSszt(String sszt) {
        super.set("sszt", sszt);
    }

    /**
     * 实施主体性质
     */
    public String getSsztxz() {
        return super.get("ssztxz");
    }

    public void setSsztxz(String ssztxz) {
        super.set("ssztxz", ssztxz);
    }

    /**
     * 实施主体编码（统一社会信用代码）
     */
    public String getSsztbm() {
        return super.get("ssztbm");
    }

    public void setSsztbm(String ssztbm) {
        super.set("ssztbm", ssztbm);
    }

    /**
     * 实施部门所属地区行政区划代码
     */
    public String getSsbmssdqxzqhdm() {
        return super.get("ssbmssdqxzqhdm");
    }

    public void setSsbmssdqxzqhdm(String ssbmssdqxzqhdm) {
        super.set("ssbmssdqxzqhdm", ssbmssdqxzqhdm);
    }

    /**
     * 委托部门
     */
    public String getWtbm() {
        return super.get("wtbm");
    }

    public void setWtbm(String wtbm) {
        super.set("wtbm", wtbm);
    }


    /**
     * 法定办结时限
     */
    public Integer getFdbjsx() {
        return super.getInt("fdbjsx");
    }

    public void setFdbjsx(Integer fdbjsx) {
        super.set("fdbjsx", fdbjsx);
    }


    /**
     * 法定办结时限单位
     */
    public String getFdbjsxdw() {
        return super.get("fdbjsxdw");
    }

    public void setFdbjsxdw(String fdbjsxdw) {
        super.set("fdbjsxdw", fdbjsxdw);
    }


    /**
     * 法定办结时限说明
     */
    public String getFdbjsxsm() {
        return super.get("fdbjsxsm");
    }

    public void setFdbjsxsm(String fdbjsxsm) {
        super.set("fdbjsxsm", fdbjsxsm);
    }

    /**
     * 承诺办结时限
     */
    public Integer getCnbjsx() {
        return super.getInt("cnbjsx");
    }

    public void setCnbjsx(Integer cnbjsx) {
        super.set("cnbjsx", cnbjsx);
    }


    /**
     * 承诺办结时限单位
     */
    public String getCnbjsxdw() {
        return super.get("cnbjsxdw");
    }

    public void setCnbjsxdw(String cnbjsxdw) {
        super.set("cnbjsxdw", cnbjsxdw);
    }


    /**
     * 承诺办结时限说明
     */
    public String getCnbjsxsm() {
        return super.get("cnbjsxsm");
    }

    public void setCnbjsxsm(String cnbjsxsm) {
        super.set("cnbjsxsm", cnbjsxsm);
    }

    /**
     * 受理条件
     */
    public String getSltj() {
        return super.get("sltj");
    }

    public void setSltj(String sltj) {
        super.set("sltj", sltj);
    }


    /**
     * 办理流程
     */
    public String getBllc() {
        return super.get("bllc");
    }

    public void setBllc(String bllc) {
        super.set("bllc", bllc);
    }


    /**
     * 是否收费
     */
    public Integer getSfsf() {
        return super.getInt("sfsf");
    }

    public void setSfsf(Integer sfsf) {
        super.set("sfsf", sfsf);
    }


    /**
     * 收费依据
     */
    public String getSfyj() {
        return super.get("sfyj");
    }

    public void setSfyj(String sfyj) {
        super.set("sfyj", sfyj);
    }

    /**
     * 服务对象
     */
    public String getFwdx() {
        return super.get("fwdx");
    }

    public void setFwdx(String fwdx) {
        super.set("fwdx", fwdx);
    }


    /**
     * 办件类型
     */
    public Integer getBjlx() {
        return super.getInt("bjlx");
    }

    public void setBjlx(Integer bjlx) {
        super.set("bjlx", bjlx);
    }

    /**
     * 办理形式
     */
    public String getBlxs() {
        return super.get("blxs");
    }

    public void setBlxs(String blxs) {
        super.set("blxs", blxs);
    }

    /**
     * 到办事现场次数
     */
    public Integer getDbsxccs() {
        return super.getInt("dbsxccs");
    }

    public void setDbsxccs(Integer dbsxccs) {
        super.set("dbsxccs", dbsxccs);
    }

    /**
     * 特别程序
     */
    public String getTbcx() {
        return super.get("tbcx");
    }

    public void setTbcx(String tbcx) {
        super.set("tbcx", tbcx);
    }


    /**
     * 办理地点
     */
    public String getBldd() {
        return super.get("bldd");
    }

    public void setBldd(String bldd) {
        super.set("bldd", bldd);
    }

    /**
     * 办理时间
     */
    public String getBlsj() {
        return super.get("blsj");
    }

    public void setBlsj(String blsj) {
        super.set("blsj", blsj);
    }


    /**
     * 咨询方式
     */
    public String getZxfs() {
        return super.get("zxfs");
    }

    public void setZxfs(String zxfs) {
        super.set("zxfs", zxfs);
    }


    /**
     * 监督投诉方式
     */
    public String getJdtsfs() {
        return super.get("jdtsfs");
    }

    public void setJdtsfs(String jdtsfs) {
        super.set("jdtsfs", jdtsfs);
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


    /**
     * 生效时间
     */
    public Date getSxrq() {
        return super.getDate("sxrq");
    }

    public void setSxrq(Date sxrq) {
        super.set("sxrq", sxrq);
    }

    /**
     * 停用日期
     */
    public Date getTyrq() {
        return super.getDate("tyrq");
    }

    public void setTyrq(Date tyrq) {
        super.set("tyrq", tyrq);
    }
}
