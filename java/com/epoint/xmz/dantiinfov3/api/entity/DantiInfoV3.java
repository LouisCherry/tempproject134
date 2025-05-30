package com.epoint.xmz.dantiinfov3.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 项目单体信息表实体
 *
 * @version [版本号, 2023-10-18 10:39:39]
 * @作者 ysai
 */
@Entity(table = "danti_info_v3", id = "rowguid")
public class DantiInfoV3 extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    public DantiInfoV3() {
        setIs_enable("1");
        setCreatedate(new Date());
    }

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
     * 项目标识
     */
    public String getItemguid() {
        return super.get("itemguid");
    }

    public void setItemguid(String itemguid) {
        super.set("itemguid", itemguid);
    }

    /**
     * 许可（备案、技术审查）编号
     */
    public String getXkbabh() {
        return super.get("xkbabh");
    }

    public void setXkbabh(String xkbabh) {
        super.set("xkbabh", xkbabh);
    }

    /**
     * 单体名称
     */
    public String getDtmc() {
        return super.get("dtmc");
    }

    public void setDtmc(String dtmc) {
        super.set("dtmc", dtmc);
    }

    /**
     * 单体编码
     */
    public String getDtbm() {
        return super.get("dtbm");
    }

    public void setDtbm(String dtbm) {
        super.set("dtbm", dtbm);
    }

    /**
     * 工程类别
     */
    public String getGcyt() {
        return super.get("gcyt");
    }

    public void setGcyt(String gcyt) {
        super.set("gcyt", gcyt);
    }

    /**
     * 规模指标
     */
    public String getGmzb() {
        return super.get("gmzb");
    }

    public void setGmzb(String gmzb) {
        super.set("gmzb", gmzb);
    }

    /**
     * 结构体系
     */
    public String getJgtx() {
        return super.get("jgtx");
    }

    public void setJgtx(String jgtx) {
        super.set("jgtx", jgtx);
    }

    /**
     * 耐火等级
     */
    public Integer getNhdj() {
        return super.getInt("nhdj");
    }

    public void setNhdj(Integer nhdj) {
        super.set("nhdj", nhdj);
    }

    /**
     * 建造方式
     */
    public Integer getJzfs() {
        return super.getInt("jzfs");
    }

    public void setJzfs(Integer jzfs) {
        super.set("jzfs", jzfs);
    }

    /**
     * 单体经纬度坐标
     */
    public String getDtjwdzb() {
        return super.get("dtjwdzb");
    }

    public void setDtjwdzb(String dtjwdzb) {
        super.set("dtjwdzb", dtjwdzb);
    }

    /**
     * 单体工程总造价
     */
    public Double getDtgczzj() {
        return super.getDouble("dtgczzj");
    }

    public void setDtgczzj(Double dtgczzj) {
        super.set("dtgczzj", dtgczzj);
    }

    /**
     * 建筑面积
     */
    public Double getJzmj() {
        return super.getDouble("jzmj");
    }

    public void setJzmj(Double jzmj) {
        super.set("jzmj", jzmj);
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
     * 占地面积
     */
    public Double getZdmj() {
        return super.getDouble("zdmj");
    }

    public void setZdmj(Double zdmj) {
        super.set("zdmj", zdmj);
    }

    /**
     * 建筑工程高度
     */
    public Double getJzgcgd() {
        return super.getDouble("jzgcgd");
    }

    public void setJzgcgd(Double jzgcgd) {
        super.set("jzgcgd", jzgcgd);
    }

    /**
     * 地上层数
     */
    public String getDscs() {
        return super.get("dscs");
    }

    public void setDscs(String dscs) {
        super.set("dscs", dscs);
    }

    /**
     * 地下层数
     */
    public String getDxcs() {
        return super.get("dxcs");
    }

    public void setDxcs(String dxcs) {
        super.set("dxcs", dxcs);
    }

    /**
     * 长度
     */
    public Double getCd() {
        return super.getDouble("cd");
    }

    public void setCd(Double cd) {
        super.set("cd", cd);
    }

    /**
     * 跨度
     */
    public Double getKd() {
        return super.getDouble("kd");
    }

    public void setKd(Double kd) {
        super.set("kd", kd);
    }

    /**
     * 创建日期
     */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }

    /**
     * 是否完善
     */
    public String getIs_enable() {
        return super.get("is_enable");
    }

    public void setIs_enable(String is_enable) {
        super.set("is_enable", is_enable);
    }

    public String getGongchengguid() {
        return super.get("gongchengguid");
    }

    /**
     * 单位工程guid
     */
    public void setGongchengguid(String gongchengguid) {
        super.set("gongchengguid", gongchengguid);
    }

    /**
     * 主键单体id
     */
    public String getZjid() {
        return super.get("zjid");
    }

    public void setZjid(String zjid) {
        super.set("zjid", zjid);
    }
    
}
