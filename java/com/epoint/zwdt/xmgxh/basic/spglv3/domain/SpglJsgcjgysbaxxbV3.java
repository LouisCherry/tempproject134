package com.epoint.zwdt.xmgxh.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 建设工程竣工验收备案信息表实体
 * 
 * @作者 Epoint
 * @version [版本号, 2023-09-25 15:00:49]
 */
@Entity(table = "SPGL_JSGCJGYSBAXXB_V3", id = "rowguid")
public class SpglJsgcjgysbaxxbV3 extends BaseEntity implements Cloneable
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
     * 竣工验收备案编号
     */
    public String getJgysbabh() {
        return super.get("jgysbabh");
    }

    public void setJgysbabh(String jgysbabh) {
        super.set("jgysbabh", jgysbabh);
    }

    /**
     * 备案机关
     */
    public String getBajg() {
        return super.get("bajg");
    }

    public void setBajg(String bajg) {
        super.set("bajg", bajg);
    }

    /**
     * 备案机关统一社会信用代码
     */
    public String getBajgxydm() {
        return super.get("bajgxydm");
    }

    public void setBajgxydm(String bajgxydm) {
        super.set("bajgxydm", bajgxydm);
    }

    /**
     * 备案日期
     */
    public Date getBarq() {
        return super.getDate("barq");
    }

    public void setBarq(Date barq) {
        super.set("barq", barq);
    }

    /**
     * 备案范围
     */
    public String getBafw() {
        return super.get("bafw");
    }

    public void setBafw(String bafw) {
        super.set("bafw", bafw);
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
     * 是否实行联合验收
     */
    public Integer getSfsxlhys() {
        return super.getInt("sfsxlhys");
    }

    public void setSfsxlhys(Integer sfsxlhys) {
        super.set("sfsxlhys", sfsxlhys);
    }

    /**
     * 是否变更五方责任主体
     */
    public Integer getSfbgwfzt() {
        return super.getInt("sfbgwfzt");
    }

    public void setSfbgwfzt(Integer sfbgwfzt) {
        super.set("sfbgwfzt", sfbgwfzt);
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
     * 开工日期
     */
    public Date getKgrq() {
        return super.getDate("kgrq");
    }

    public void setKgrq(Date kgrq) {
        super.set("kgrq", kgrq);
    }

    /**
     * 竣工日期
     */
    public Date getJgrq() {
        return super.getDate("jgrq");
    }

    public void setJgrq(Date jgrq) {
        super.set("jgrq", jgrq);
    }

    /**
     * 实际造价
     */
    public Double getSjzj() {
        return super.getDouble("sjzj");
    }

    public void setSjzj(Double sjzj) {
        super.set("sjzj", sjzj);
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
     * 建设单位意见
     */
    public String getJsdwyj() {
        return super.get("jsdwyj");
    }

    public void setJsdwyj(String jsdwyj) {
        super.set("jsdwyj", jsdwyj);
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
     * 联系人手机号
     */
    public String getLxrsjh() {
        return super.get("lxrsjh");
    }

    public void setLxrsjh(String lxrsjh) {
        super.set("lxrsjh", lxrsjh);
    }

    /**
     * 档案移交状态
     */
    public Integer getDayszt() {
        return super.getInt("dayszt");
    }

    public void setDayszt(Integer dayszt) {
        super.set("dayszt", dayszt);
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

    /**
     * 档案验收意见
     */
    public String getDaysyj() {
        return super.get("daysyj");
    }

    public void setDaysyj(String daysyj) {
        super.set("daysyj", daysyj);
    }

}
