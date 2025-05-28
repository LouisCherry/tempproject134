package com.epoint.xmz.xmycslspinfo.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 一窗受理审批环节信息表实体
 * 
 * @作者  LYA
 * @version [版本号, 2020-07-22 16:31:09]
 */
@Entity(table = "Xm_ycsl_spInfo", id = "rowguid")
public class XmYcslSpinfo extends BaseEntity implements Cloneable
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
    * 申办流水号
    */
    public String getSblsh_short() {
        return super.get("sblsh_short");
    }

    public void setSblsh_short(String sblsh_short) {
        super.set("sblsh_short", sblsh_short);
    }

    /**
    * 备用流水号
    */
    public String getSblsh() {
        return super.get("sblsh");
    }

    public void setSblsh(String sblsh) {
        super.set("sblsh", sblsh);
    }

    /**
    * 事项编码
    */
    public String getSxbm() {
        return super.get("sxbm");
    }

    public void setSxbm(String sxbm) {
        super.set("sxbm", sxbm);
    }

    /**
    * 事项简码
    */
    public String getSxbm_short() {
        return super.get("sxbm_short");
    }

    public void setSxbm_short(String sxbm_short) {
        super.set("sxbm_short", sxbm_short);
    }

    /**
    * 事项情形编码
    */
    public String getSxqxbm() {
        return super.get("sxqxbm");
    }

    public void setSxqxbm(String sxqxbm) {
        super.set("sxqxbm", sxqxbm);
    }

    /**
    * 审批环节代码
    */
    public String getSphjdm() {
        return super.get("sphjdm");
    }

    public void setSphjdm(String sphjdm) {
        super.set("sphjdm", sphjdm);
    }

    /**
    * 审批环节名称
    */
    public String getSphjmc() {
        return super.get("sphjmc");
    }

    public void setSphjmc(String sphjmc) {
        super.set("sphjmc", sphjmc);
    }

    /**
    * 审批部门名称
    */
    public String getSpbmmc() {
        return super.get("spbmmc");
    }

    public void setSpbmmc(String spbmmc) {
        super.set("spbmmc", spbmmc);
    }

    /**
    * 审批部门组织机构代码
    */
    public String getSpbmzzjdmd() {
        return super.get("spbmzzjdmd");
    }

    public void setSpbmzzjdmd(String spbmzzjdmd) {
        super.set("spbmzzjdmd", spbmzzjdmd);
    }

    /**
    * 审批部门所在地行政区域代码
    */
    public String getXzqhdm() {
        return super.get("xzqhdm");
    }

    public void setXzqhdm(String xzqhdm) {
        super.set("xzqhdm", xzqhdm);
    }

    /**
    * 审批人姓名
    */
    public String getSprxm() {
        return super.get("sprxm");
    }

    public void setSprxm(String sprxm) {
        super.set("sprxm", sprxm);
    }

    /**
    * 审批人职务代码
    */
    public String getSprzwdm() {
        return super.get("sprzwdm");
    }

    public void setSprzwdm(String sprzwdm) {
        super.set("sprzwdm", sprzwdm);
    }

    /**
    * 审批人职务名称
    */
    public String getSprzwmc() {
        return super.get("sprzwmc");
    }

    public void setSprzwmc(String sprzwmc) {
        super.set("sprzwmc", sprzwmc);
    }

    /**
    * 审批意见
    */
    public String getSpyj() {
        return super.get("spyj");
    }

    public void setSpyj(String spyj) {
        super.set("spyj", spyj);
    }

    /**
    * 审批时间
    */
    public Date getSpsj() {
        return super.getDate("spsj");
    }

    public void setSpsj(Date spsj) {
        super.set("spsj", spsj);
    }

    /**
    * 审批环节状态代码
    */
    public String getSphjztdm() {
        return super.get("sphjztdm");
    }

    public void setSphjztdm(String sphjztdm) {
        super.set("sphjztdm", sphjztdm);
    }

    /**
    * 接收标识
    */
    public String getRec_flag() {
        return super.get("rec_flag");
    }

    public void setRec_flag(String rec_flag) {
        super.set("rec_flag", rec_flag);
    }

    /**
    * 数据提供部门组织机构代码/社会信用代码
    */
    public String getD_zzjgdm() {
        return super.get("d_zzjgdm");
    }

    public void setD_zzjgdm(String d_zzjgdm) {
        super.set("d_zzjgdm", d_zzjgdm);
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
    * 处理状态
    */
    public String getClzt() {
        return super.get("clzt");
    }

    public void setClzt(String clzt) {
        super.set("clzt", clzt);
    }

    /**
    * 处理时间
    */
    public Date getClsj() {
        return super.getDate("clsj");
    }

    public void setClsj(Date clsj) {
        super.set("clsj", clsj);
    }

    /**
    * 备用字段
    */
    public String getByzd() {
        return super.get("byzd");
    }

    public void setByzd(String byzd) {
        super.set("byzd", byzd);
    }

    /**
    * 备用字段 1
    */
    public String getByzd1() {
        return super.get("byzd1");
    }

    public void setByzd1(String byzd1) {
        super.set("byzd1", byzd1);
    }

    /**
    * 备用字段 2
    */
    public String getByzd2() {
        return super.get("byzd2");
    }

    public void setByzd2(String byzd2) {
        super.set("byzd2", byzd2);
    }

    /**
    * 备用字段3
    */
    public String getByzd3() {
        return super.get("byzd3");
    }

    public void setByzd3(String byzd3) {
        super.set("byzd3", byzd3);
    }

    /**
    * 备用字段4
    */
    public String getByzd4() {
        return super.get("byzd4");
    }

    public void setByzd4(String byzd4) {
        super.set("byzd4", byzd4);
    }
    /**
     * 办件编号
     */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }
    /**
     * 申报流水号
     */
     public String getFlowsn() {
         return super.get("flowsn");
     }

     public void setFlowsn(String flowsn) {
         super.set("flowsn", flowsn);
     }

}
