package com.epoint.depassetinfo.domain;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;


/**
 * 采购基本信息表实体
 * 
 * @作者  yangle
 * @version [版本号, 2018-03-27 08:13:16]
 */
@Entity(table = "zj_deptask", id = "rowguid")
public class Deptask extends BaseEntity implements Cloneable
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
    public String getDeptask() {
        return super.get("deptask");
    }

    public void setDeptask(String deptask) {
        super.set("deptask", deptask);
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
    * 采购标识
    */
    public String getTask() {
        return super.get("task");
    }

    public void setTask(String task) {
        super.set("task", task);
    }
    /**
    * 请购人员
    */
    public String getItemid() {
        return super.get("itemid");
    }

    public void setItemid(String itemid) {
        super.set("itemid", itemid);
    }
    /**
    * 预算标识
    */
    public String getTaskguid() {
        return super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }
    public String getUnid() {
        return super.get("unid");
    }
    public void setUnid(String unid) {
        super.set("unid", unid);
    }
}
