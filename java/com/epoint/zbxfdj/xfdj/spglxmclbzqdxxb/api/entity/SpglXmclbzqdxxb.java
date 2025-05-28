package com.epoint.zbxfdj.xfdj.spglxmclbzqdxxb.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 项目审批过程信息表实体
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
@Entity(table = "370800_SPGL_XMCLBZQDXXB", id = "id")
public class SpglXmclbzqdxxb extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据主键
     */
    public String getId() {
        return super.get("id");
    }

    public void setId(String id) {
        super.set("id", id);
    }

    /**
     * 流水号
     */
    public String getLsh() {
        return super.get("lsh");
    }

    public void setLsh(String lsh) {
        super.set("lsh", lsh);
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
     * 数据状态标识
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

}
