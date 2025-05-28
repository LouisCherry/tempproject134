package com.epoint.zczwfw.auditxmcert.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 项目证照表实体
 * 
 * @作者 吕闯
 * @version [版本号, 2022-06-10 15:47:30]
 */
@Entity(table = "audit_xm_cert", id = "rowguid")
public class AuditXmCert extends BaseEntity implements Cloneable
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
     * 事项版本唯一标识
     */
    public String getItemguid() {
        return super.get("itemguid");
    }

    public void setItemguid(String itemguid) {
        super.set("itemguid", itemguid);
    }

    /**
     * 证照类型
     */
    public String getCert() {
        return super.get("cert");
    }

    public void setCert(String cert) {
        super.set("cert", cert);
    }

    /**
     * 附件标识
     */
    public String getClengguid() {
        return super.get("clengguid");
    }

    public void setClengguid(String clengguid) {
        super.set("clengguid", clengguid);
    }

}
