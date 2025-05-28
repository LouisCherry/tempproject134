package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 审批事项扩展信息表实体
 *
 * @version [版本号, 2023-09-19 17:17:50]
 * @作者 Epoint
 */
@Entity(table = "spgl_spsxkzxxb_V3", id = "rowguid")
public class SpglSpsxkzxxb extends BaseEntity implements Cloneable {
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
     * 是否进驻工程建设项目审批综合服务窗口
     */
    public Integer getSfjzzhck() {
        return super.getInt("sfjzzhck");
    }

    public void setSfjzzhck(Integer sfjzzhck) {
        super.set("sfjzzhck", sfjzzhck);
    }

    /**
     * 服务渠道
     */
    public String getFwqd() {
        return super.get("fwqd");
    }

    public void setFwqd(String fwqd) {
        super.set("fwqd", fwqd);
    }

    /**
     * 通办范围
     */
    public Integer getTbfw() {
        return super.getInt("tbfw");
    }

    public void setTbfw(Integer tbfw) {
        super.set("tbfw", tbfw);
    }

    /**
     * 联办机构
     */
    public String getLbjg() {
        return super.get("lbjg");
    }

    public void setLbjg(String lbjg) {
        super.set("lbjg", lbjg);
    }

    /**
     * 行政许可事项类型
     */
    public Integer getXzxksxlx() {
        return super.getInt("xzxksxlx");
    }

    public void setXzxksxlx(Integer xzxksxlx) {
        super.set("xzxksxlx", xzxksxlx);
    }

    /**
     * 准予行政许可的条件
     */
    public String getZyxzxkdtj() {
        return super.get("zyxzxkdtj");
    }

    public void setZyxzxkdtj(String zyxzxkdtj) {
        super.set("zyxzxkdtj", zyxzxkdtj);
    }

    /**
     * 规定行政许可条件的依据
     */
    public String getGdxzxktjdyj() {
        return super.get("gdxzxktjdyj");
    }

    public void setGdxzxktjdyj(String gdxzxktjdyj) {
        super.set("gdxzxktjdyj", gdxzxktjdyj);
    }

    /**
     * 规定收费标准的依据
     */
    public String getGdsfbzdyj() {
        return super.get("gdsfbzdyj");
    }

    public void setGdsfbzdyj(String gdsfbzdyj) {
        super.set("gdsfbzdyj", gdsfbzdyj);
    }

    /**
     * 有无中介服务事项
     */
    public Integer getYwzjfwsx() {
        return super.getInt("ywzjfwsx");
    }

    public void setYwzjfwsx(Integer ywzjfwsx) {
        super.set("ywzjfwsx", ywzjfwsx);
    }

    /**
     * 中介服务事项名称
     */
    public String getZjfwsxmc() {
        return super.get("zjfwsxmc");
    }

    public void setZjfwsxmc(String zjfwsxmc) {
        super.set("zjfwsxmc", zjfwsxmc);
    }

    /**
     * 设定中介服务事项的依据
     */
    public String getSdzjfwsxdyj() {
        return super.get("sdzjfwsxdyj");
    }

    public void setSdzjfwsxdyj(String sdzjfwsxdyj) {
        super.set("sdzjfwsxdyj", sdzjfwsxdyj);
    }

    /**
     * 审批结果类型
     */
    public String getSpjglx() {
        return super.get("spjglx");
    }

    public void setSpjglx(String spjglx) {
        super.set("spjglx", spjglx);
    }

    /**
     * 审批结果名称
     */
    public String getSpjgmc() {
        return super.get("spjgmc");
    }

    public void setSpjgmc(String spjgmc) {
        super.set("spjgmc", spjgmc);
    }

    /**
     * 审批结果样本
     */
    public String getSpjgyb() {
        return super.get("spjgyb");
    }

    public void setSpjgyb(String spjgyb) {
        super.set("spjgyb", spjgyb);
    }

    /**
     * 是否支持预约办理
     */
    public Integer getSfzcyybl() {
        return super.getInt("sfzcyybl");
    }

    public void setSfzcyybl(Integer sfzcyybl) {
        super.set("sfzcyybl", sfzcyybl);
    }

    /**
     * 是否支持网上支付
     */
    public Integer getSfzcwszf() {
        return super.getInt("sfzcwszf");
    }

    public void setSfzcwszf(Integer sfzcwszf) {
        super.set("sfzcwszf", sfzcwszf);
    }

    /**
     * 是否支持物流快递
     */
    public Integer getSfzcwlkd() {
        return super.getInt("sfzcwlkd");
    }

    public void setSfzcwlkd(Integer sfzcwlkd) {
        super.set("sfzcwlkd", sfzcwlkd);
    }

    /**
     * 是否支持自主终端办理
     */
    public Integer getSfzczzzdbl() {
        return super.getInt("sfzczzzdbl");
    }

    public void setSfzczzzdbl(Integer sfzczzzdbl) {
        super.set("sfzczzzdbl", sfzczzzdbl);
    }

    /**
     * 是否网办
     */
    public Integer getSfwb() {
        return super.getInt("sfwb");
    }

    public void setSfwb(Integer sfwb) {
        super.set("sfwb", sfwb);
    }

    /**
     * 网上办理深度
     */
    public String getWsblsd() {
        return super.get("wsblsd");
    }

    public void setWsblsd(String wsblsd) {
        super.set("wsblsd", wsblsd);
    }

    /**
     * 必须现场办理原因说明
     */
    public String getBxxcblyysm() {
        return super.get("bxxcblyysm");
    }

    public void setBxxcblyysm(String bxxcblyysm) {
        super.set("bxxcblyysm", bxxcblyysm);
    }

    /**
     * 是否实行告知承诺办理
     */
    public Integer getSfsxgzcnbl() {
        return super.getInt("sfsxgzcnbl");
    }

    public void setSfsxgzcnbl(Integer sfsxgzcnbl) {
        super.set("sfsxgzcnbl", sfsxgzcnbl);
    }

    /**
     * 是否需要现场勘验
     */
    public Integer getSfxyxcky() {
        return super.getInt("sfxyxcky");
    }

    public void setSfxyxcky(Integer sfxyxcky) {
        super.set("sfxyxcky", sfxyxcky);
    }

    /**
     * 是否需要组织听证
     */
    public Integer getSfxyzztz() {
        return super.getInt("sfxyzztz");
    }

    public void setSfxyzztz(Integer sfxyzztz) {
        super.set("sfxyzztz", sfxyzztz);
    }

    /**
     * 是否需要招标、拍卖、挂牌交易
     */
    public Integer getSfxyzbpmgpjy() {
        return super.getInt("sfxyzbpmgpjy");
    }

    public void setSfxyzbpmgpjy(Integer sfxyzbpmgpjy) {
        super.set("sfxyzbpmgpjy", sfxyzbpmgpjy);
    }

    /**
     * 是否需要检验、检测、检疫
     */
    public Integer getSfxyjyjcjy() {
        return super.getInt("sfxyjyjcjy");
    }

    public void setSfxyjyjcjy(Integer sfxyjyjcjy) {
        super.set("sfxyjyjcjy", sfxyjyjcjy);
    }

    /**
     * 是否需要鉴定
     */
    public Integer getSfxyjd() {
        return super.getInt("sfxyjd");
    }

    public void setSfxyjd(Integer sfxyjd) {
        super.set("sfxyjd", sfxyjd);
    }

    /**
     * 是否需要专家评审
     */
    public Integer getSfxyzjps() {
        return super.getInt("sfxyzjps");
    }

    public void setSfxyzjps(Integer sfxyzjps) {
        super.set("sfxyzjps", sfxyzjps);
    }

    /**
     * 是否需要向社会公示
     */
    public Integer getSfxyxshgs() {
        return super.getInt("sfxyxshgs");
    }

    public void setSfxyxshgs(Integer sfxyxshgs) {
        super.set("sfxyxshgs", sfxyxshgs);
    }

    /**
     * 行政许可证件名称
     */
    public String getXzxkzjmc() {
        return super.get("xzxkzjmc");
    }

    public void setXzxkzjmc(String xzxkzjmc) {
        super.set("xzxkzjmc", xzxkzjmc);
    }

    /**
     * 行政许可证件的有效期限
     */
    public String getXzxkzjdyxqx() {
        return super.get("xzxkzjdyxqx");
    }

    public void setXzxkzjdyxqx(String xzxkzjdyxqx) {
        super.set("xzxkzjdyxqx", xzxkzjdyxqx);
    }

    /**
     * 行政许可证件的有效期限单位
     */
    public Integer getXzxkzjdyxqxdw() {
        return super.getInt("xzxkzjdyxqxdw");
    }

    public void setXzxkzjdyxqxdw(Integer xzxkzjdyxqxdw) {
        super.set("xzxkzjdyxqxdw", xzxkzjdyxqxdw);
    }

    /**
     * 规定行政许可证件有效期限的依据
     */
    public String getGdxzxkzjyxqxdyj() {
        return super.get("gdxzxkzjyxqxdyj");
    }

    public void setGdxzxkzjyxqxdyj(String gdxzxkzjyxqxdyj) {
        super.set("gdxzxkzjyxqxdyj", gdxzxkzjyxqxdyj);
    }

    /**
     * 办理行政许可证件延续手续的要求
     */
    public String getBlxzxkzjyxsxdyq() {
        return super.get("blxzxkzjyxsxdyq");
    }

    public void setBlxzxkzjyxsxdyq(String blxzxkzjyxsxdyq) {
        super.set("blxzxkzjyxsxdyq", blxzxkzjyxsxdyq);
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
     * 同步标识
     */
    public Integer getSync() {
        return super.getInt("sync");
    }

    public void setSync(Integer sync) {
        super.set("sync", sync);
    }

}
