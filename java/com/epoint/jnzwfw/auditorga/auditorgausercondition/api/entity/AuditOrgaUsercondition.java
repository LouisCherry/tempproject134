package com.epoint.jnzwfw.auditorga.auditorgausercondition.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 人员在岗信息表实体
 * 
 * @作者  zhaoy
 * @version [版本号, 2019-05-04 17:10:14]
 */
@Entity(table = "audit_orga_usercondition", id = "rowguid")
public class AuditOrgaUsercondition extends BaseEntity implements Cloneable
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
    * 科室名称
    */
    public String getDepartment() {
        return super.get("department");
    }

    public void setDepartment(String department) {
        super.set("department", department);
    }

    /**
    * 人员名称
    */
    public String getUsername() {
        return super.get("username");
    }

    public void setUsername(String username) {
        super.set("username", username);
    }

    /**
    * 在岗情况
    */
    public String getUserstate() {
        return super.get("userstate");
    }

    public void setUserstate(String userstate) {
        super.set("userstate", userstate);
    }
    
    /**
    * 在岗情况
    */
    public Integer getOrdernumber() {
        return super.getInt("ordernumber");
    }

    public void setOrdernumber(Integer ordernumber) {
        super.set("ordernumber", ordernumber);
    }

}
