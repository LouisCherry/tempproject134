package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 建筑工程施工许可信息表实体
 *
 * @version [版本号, 2023-09-25 11:55:36]
 * @作者 Epoint
 */
@Entity(table = "SPGL_JZGCSGXKXXB_V3", id = "rowguid")
public class SpglJzgcsgxkxxbV3 extends BaseEntity implements Cloneable {
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
     * 建设用地规划许可证编号
     */
    public String getYdghxkzbh() {
        return super.get("ydghxkzbh");
    }

    public void setYdghxkzbh(String ydghxkzbh) {
        super.set("ydghxkzbh", ydghxkzbh);
    }

    /**
     * 建设工程规划许可证编号
     */
    public String getGcghxkzbh() {
        return super.get("gcghxkzbh");
    }

    public void setGcghxkzbh(String gcghxkzbh) {
        super.set("gcghxkzbh", gcghxkzbh);
    }

    /**
     * 中标通知书编号
     */
    public String getZbtzsbh() {
        return super.get("zbtzsbh");
    }

    public void setZbtzsbh(String zbtzsbh) {
        super.set("zbtzsbh", zbtzsbh);
    }

    /**
     * 施工图审查合格书编号
     */
    public String getSchgsbh() {
        return super.get("schgsbh");
    }

    public void setSchgsbh(String schgsbh) {
        super.set("schgsbh", schgsbh);
    }

    /**
     * 施工许可证编号
     */
    public String getSgxkzbh() {
        return super.get("sgxkzbh");
    }

    public void setSgxkzbh(String sgxkzbh) {
        super.set("sgxkzbh", sgxkzbh);
    }

    /**
     * 建设性质
     */
    public Integer getJSxz() {
        return super.getInt("jsxz");
    }

    public void setJsxz(Integer jsxz) {
        super.set("jsxz", jsxz);
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
     * 管理属地
     */
    public String getGlsd() {
        return super.get("glsd");
    }

    public void setGlsd(String glsd) {
        super.set("glsd", glsd);
    }

    /**
     * 建设单位
     */
    public String getJsdw() {
        return super.get("jsdw");
    }

    public void setJsdw(String jsdw) {
        super.set("jsdw", jsdw);
    }

    /**
     * 建设单位代码
     */
    public String getJsdwdm() {
        return super.get("jsdwdm");
    }

    public void setJsdwdm(String jsdwdm) {
        super.set("jsdwdm", jsdwdm);
    }

    /**
     * 建设单位类型
     */
    public Integer getJsdwlx() {
        return super.getInt("jsdwlx");
    }

    public void setJsdwlx(Integer jsdwlx) {
        super.set("jsdwlx", jsdwlx);
    }

    /**
     * 建设单位项目负责人
     */
    public String getJsdwxmfzr() {
        return super.get("jsdwxmfzr");
    }

    public void setJsdwxmfzr(String jsdwxmfzr) {
        super.set("jsdwxmfzr", jsdwxmfzr);
    }

    /**
     * 建设单位项目负责人身份证件号码
     */
    public String getJsfzrzjhm() {
        return super.get("jsfzrzjhm");
    }

    public void setJsfzrzjhm(String jsfzrzjhm) {
        super.set("jsfzrzjhm", jsfzrzjhm);
    }

    /**
     * 建设单位项目负责人身份证件类型
     */
    public Integer getJsfzrzjlx() {
        return super.getInt("jsfzrzjlx");
    }

    public void setJsfzrzjlx(Integer jsfzrzjlx) {
        super.set("jsfzrzjlx", jsfzrzjlx);
    }

    /**
     * 建设单位项目负责人联系电话
     */
    public String getJsfzrlxdh() {
        return super.get("jsfzrlxdh");
    }

    public void setJsfzrlxdh(String jsfzrlxdh) {
        super.set("jsfzrlxdh", jsfzrlxdh);
    }

    /**
     * 工程名称
     */
    public String getGcmc() {
        return super.get("gcmc");
    }

    public void setGcmc(String gcmc) {
        super.set("gcmc", gcmc);
    }

    /**
     * 建设地址
     */
    public String getJsdz() {
        return super.get("jsdz");
    }

    public void setJsdz(String jsdz) {
        super.set("jsdz", jsdz);
    }

    /**
     * 所属县区
     */
    public String getSsqx() {
        return super.get("ssqx");
    }

    public void setSsqx(String ssqx) {
        super.set("ssqx", ssqx);
    }

    /**
     * 项目经纬度坐标
     */
    public String getXmjwdzb() {
        return super.get("xmjwdzb");
    }

    public void setXmjwdzb(String xmjwdzb) {
        super.set("xmjwdzb", xmjwdzb);
    }

    /**
     * 建设规模
     */
    public String getJsgm() {
        return super.get("jsgm");
    }

    public void setJsgm(String jsgm) {
        super.set("jsgm", jsgm);
    }

    /**
     * 计划开工日期
     */
    public Date getJhkgrq() {
        return super.getDate("jhkgrq");
    }

    public void setJhkgrq(Date jhkgrq) {
        super.set("jhkgrq", jhkgrq);
    }

    /**
     * 计划竣工日期
     */
    public Date getJhjgrq() {
        return super.getDate("jhjgrq");
    }

    public void setJhjgrq(Date jhjgrq) {
        super.set("jhjgrq", jhjgrq);
    }

    /**
     * 合同价格
     */
    public Double getHtjg() {
        return super.getDouble("htjg");
    }

    public void setHtjg(Double htjg) {
        super.set("htjg", htjg);
    }

    /**
     * 总建筑面积
     */
    public Double getZjzmj() {
        return super.getDouble("zjzmj");
    }

    public void setZjzmj(Double zjzmj) {
        super.set("zjzmj", zjzmj);
    }

    /**
     * 其中，地上建筑面积
     */
    public Double getDsjzmj() {
        return super.getDouble("dsjzmj");
    }

    public void setDsjzmj(Double dsjzmj) {
        super.set("dsjzmj", dsjzmj);
    }

    /**
     * 其中，地下建筑面积
     */
    public Double getDxjzmj() {
        return super.getDouble("dxjzmj");
    }

    public void setDxjzmj(Double dxjzmj) {
        super.set("dxjzmj", dxjzmj);
    }

    /**
     * 其中，人防面积
     */
    public Double getRfmj() {
        return super.getDouble("rfmj");
    }

    public void setRfmj(Double rfmj) {
        super.set("rfmj", rfmj);
    }

    /**
     * 证照备注
     */
    public String getZzbz() {
        return super.get("zzbz");
    }

    public void setZzbz(String zzbz) {
        super.set("zzbz", zzbz);
    }

    /**
     * 附件备注
     */
    public String getFjbz() {
        return super.get("fjbz");
    }

    public void setFjbz(String fjbz) {
        super.set("fjbz", fjbz);
    }

    /**
     * 联系人/代理人
     */
    public String getLxr() {
        return super.get("lxr");
    }

    public void setLxr(String lxr) {
        super.set("lxr", lxr);
    }

    /**
     * 联系电话
     */
    public String getLxdh() {
        return super.get("lxdh");
    }

    public void setLxdh(String lxdh) {
        super.set("lxdh", lxdh);
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
     * 是否上报
     */
    public String getSync() {
        return super.get("sync");
    }

    public void setSync(String sync) {
        super.set("sync", sync);
    }

}
