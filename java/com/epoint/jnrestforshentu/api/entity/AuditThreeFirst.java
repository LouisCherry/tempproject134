package com.epoint.jnrestforshentu.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 人员在岗信息表实体
 * 
 * @作者  zhaoy
 * @version [版本号, 2019-05-04 17:10:14]
 */
@Entity(table = "audit_threefirst", id = "rowguid")
public class AuditThreeFirst extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 附件名字
     */
    public String getName() {
        return super.get("name");
    }

    public void setName(String name) {
        super.set("name", name);
    }

    /**
    * 附件ID
    */
    public String getAttachguid() {
        return super.get("attachguid");
    }

    public void setAttachguid(String attachguid) {
        super.set("attachguid", attachguid);
    }

   
    /**
    * 业务id
    */
    public String getYeWuGuid() {
        return super.get("yewuguid");
    }

    public void setYeWuGuid(String yewuguid) {
        super.set("yewuguid", yewuguid);
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
     * 附件类型
     */
     public String getType() {
         return super.get("type");
     }

     public void setType(String type) {
         super.set("type", type);
     }

}
