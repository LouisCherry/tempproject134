package com.epoint.xmz.certczcjsybj.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 出租车驾驶员人员背景明细库实体
 * 
 * @作者 dyxin
 * @version [版本号, 2023-05-22 13:17:57]
 */
@Entity(table = "cert_czcjsybj", id = "rowguid")
public class CertCzcjsybj extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    public String getName() {
        return super.get("name");
    }

    public void setName(String name) {
        super.set("name", name);
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
     * 性别
     */
    public String getSex() {
        return super.get("sex");
    }

    public void setSex(String sex) {
        super.set("sex", sex);
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
     * 身份证号
     */
    public String getId() {
        return super.get("id");
    }

    public void setId(String id) {
        super.set("id", id);
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
     * 操作日期
     */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
     * 备注
     */
    public String getRemark() {
        return super.get("remark");
    }

    public void setRemark(String remark) {
        super.set("remark", remark);
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
     * 上传时间
     */
    public Date getUploadtime() {
        return super.getDate("uploadtime");
    }

    public void setUploadtime(Date uploadtime) {
        super.set("uploadtime", uploadtime);
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

}
