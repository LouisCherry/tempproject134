package com.epoint.banjiantongjibaobiao.api;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 事项办件统计表实体
 *
 * @author Epoint
 * @version [版本号, 2022-01-10 10:20:33]
 */
@Entity(table = "TJFX_TASK_PROJECT", id = "rowguid")
public class TjfxTaskProject extends BaseEntity implements Cloneable {
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
     * 部门
     */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    /**
     * 事项名称
     */
    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
    }

    /**
     * 事项GUID
     */
    public String getTaskguid() {
        return super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }

    /**
     * 事项类型
     */
    public Integer getTasktype() {
        return super.getInt("tasktype");
    }

    public void setTasktype(Integer tasktype) {
        super.set("tasktype", tasktype);
    }

    /**
     * 区划
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
     * 日期
     */
    public Date getDate() {
        return super.getDate("date");
    }

    public void setDate(Date date) {
        super.set("date", date);
    }

    /**
     * 办件量
     */
    public Integer getProjectcount() {
        return super.getInt("projectcount");
    }

    public void setProjectcount(Integer projectcount) {
        super.set("projectcount", projectcount);
    }

    /**
     * 外网申报已提交量
     */
    public Integer getWwsbytj() {
        return super.getInt("wwsbytj");
    }

    public void setWwsbytj(Integer wwsbytj) {
        super.set("wwsbytj", wwsbytj);
    }

    /**
     * 外网申报预审退回量
     */
    public Integer getWwsbysth() {
        return super.getInt("wwsbysth");
    }

    public void setWwsbysth(Integer wwsbysth) {
        super.set("wwsbysth", wwsbysth);
    }

    /**
     * 待接件量
     */
    public Integer getDjj() {
        return super.getInt("djj");
    }

    public void setDjj(Integer djj) {
        super.set("djj", djj);
    }

    /**
     * 已接件量
     */
    public Integer getYjj() {
        return super.getInt("yjj");
    }

    public void setYjj(Integer yjj) {
        super.set("yjj", yjj);
    }

    /**
     * 待补办量
     */
    public Integer getDbb() {
        return super.getInt("dbb");
    }

    public void setDbb(Integer dbb) {
        super.set("dbb", dbb);
    }

    /**
     * 已受理量
     */
    public Integer getYsl() {
        return super.getInt("ysl");
    }

    public void setYsl(Integer ysl) {
        super.set("ysl", ysl);
    }

    /**
     * 审批不通过量
     */
    public Integer getSpbtg() {
        return super.getInt("spbtg");
    }

    public void setSpbtg(Integer spbtg) {
        super.set("spbtg", spbtg);
    }

    /**
     * 审批通过量
     */
    public Integer getSptg() {
        return super.getInt("sptg");
    }

    public void setSptg(Integer sptg) {
        super.set("sptg", sptg);
    }

    /**
     * 正常办结量
     */
    public Integer getZcbj() {
        return super.getInt("zcbj");
    }

    public void setZcbj(Integer zcbj) {
        super.set("zcbj", zcbj);
    }

    /**
     * 不予受理量
     */
    public Integer getBysl() {
        return super.getInt("bysl");
    }

    public void setBysl(Integer bysl) {
        super.set("bysl", bysl);
    }

    /**
     * 撤销申请量
     */
    public Integer getCxsq() {
        return super.getInt("cxsq");
    }

    public void setCxsq(Integer cxsq) {
        super.set("cxsq", cxsq);
    }

    /**
     * 异常终止量
     */
    public Integer getYczz() {
        return super.getInt("yczz");
    }

    public void setYczz(Integer yczz) {
        super.set("yczz", yczz);
    }

    /**
     * 已受理待补办量
     */
    public Integer getYsldbb() {
        return super.getInt("ysldbb");
    }

    public void setYsldbb(Integer ysldbb) {
        super.set("ysldbb", ysldbb);
    }

    /**
     * 审批中量
     */
    public Integer getSpz() {
        return super.getInt("spz");
    }

    public void setSpz(Integer spz) {
        super.set("spz", spz);
    }

    /**
     * 暂停中量
     */
    public Integer getZtz() {
        return super.getInt("ztz");
    }

    public void setZtz(Integer ztz) {
        super.set("ztz", ztz);
    }

    /**
     * 导入办件量
     *
     * @param drbjl
     */
    public void setDrbjl(Integer drbjl) {
        super.set("drbjl", drbjl);
    }

    public int getDrbjl() {
        return super.getInt("drbjl");
    }
}
