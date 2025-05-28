package com.epoint.xmz.thirdreporteddata.task.commonapi.domain;

import com.epoint.core.BaseEntity;

/**
 * 地方规划控制线信息表实体
 *
 * @version [版本号, 2023-09-25 15:35:51]
 * @作者 Epoint
 */
public class YwxxRelationMapping extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;


    public YwxxRelationMapping(String formadress, Class<? extends BaseEntity> ywxxClass, String ywxxentityname) {
        setFormadress(formadress);
        setYwxxClass(ywxxClass);
        setYwxxentityname(ywxxentityname);
    }


    /**
     * form表单
     */
    public String getFormadress() {
        return super.get("formadress");
    }

    public void setFormadress(String formadress) {
        super.set("formadress", formadress);
    }

    /**
     * 对象类字节码
     */
    public Class<? extends BaseEntity> getYwxxClass() {
        return super.get("ywxxClass");
    }

    public void setYwxxClass(Class<? extends BaseEntity> ywxxClass) {
        super.set("basClass", ywxxClass);
    }


    /**
     * 业务信息表名
     */
    public String getYwxxentityname() {
        return super.get("ywxxentityname");
    }

    public void setYwxxentityname(String ywxxentityname) {
        super.set("ywxxentityname", ywxxentityname);
    }


}
