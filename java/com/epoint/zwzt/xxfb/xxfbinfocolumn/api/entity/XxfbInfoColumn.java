package com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 信息栏目表实体
 *
 * @author D0Be
 * @version [版本号, 2022-04-25 13:57:31]
 */
@Entity(table = "xxfb_info_column", id = "rowguid")
public class XxfbInfoColumn extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 栏目名称
     */
    public String getInfo_column_name() {
        return super.get("info_column_name");
    }

    public void setInfo_column_name(String info_column_name) {
        super.set("info_column_name", info_column_name);
    }

    /**
     * 父栏目标识
     */
    public String getParent_column_rowguid() {
        return super.get("parent_column_rowguid");
    }

    public void setParent_column_rowguid(String parent_column_rowguid) {
        super.set("parent_column_rowguid", parent_column_rowguid);
    }

    /**
     * 父栏目标识
     */
    public String getParent_column_number() {
        return super.get("parent_column_number");
    }

    public void setParent_column_number(String parent_column_number) {
        super.set("parent_column_number", parent_column_number);
    }

    /**
     * 栏目编号
     */
    public String getColumn_number() {
        return super.get("column_number");
    }

    public void setColumn_number(String column_number) {
        super.set("column_number", column_number);
    }

    /**
     * 启用
     */
    public String getIs_enable() {
        return super.get("is_enable");
    }

    public void setIs_enable(String is_enable) {
        super.set("is_enable", is_enable);
    }

    /**
     * 栏目类型
     */
    public String getInfo_column_type() {
        return super.get("info_column_type");
    }

    public void setInfo_column_type(String info_column_type) {
        super.set("info_column_type", info_column_type);
    }

    /**
     * 创建时间
     */
    public Date getCreate_time() {
        return super.getDate("create_time");
    }

    public void setCreate_time(Date create_time) {
        super.set("create_time", create_time);
    }

    /**
     * 创建人标识
     */
    public String getCreate_userguid() {
        return super.get("create_userguid");
    }

    public void setCreate_userguid(String create_userguid) {
        super.set("create_userguid", create_userguid);
    }

    /**
     * 创建人姓名
     */
    public String getCreate_username() {
        return super.get("create_username");
    }

    public void setCreate_username(String create_username) {
        super.set("create_username", create_username);
    }

    /**
     * 排序号
     */
    public Integer getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
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
     * 操作人所在独立部门Guid
     */
    public String getOperateuserbaseouguid() {
        return super.get("operateuserbaseouguid");
    }

    public void setOperateuserbaseouguid(String operateuserbaseouguid) {
        super.set("operateuserbaseouguid", operateuserbaseouguid);
    }

    /**
     * 所属辖区编号
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
     * 主键guid
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * 操作人所在部门Guid
     */
    public String getOperateuserouguid() {
        return super.get("operateuserouguid");
    }

    public void setOperateuserouguid(String operateuserouguid) {
        super.set("operateuserouguid", operateuserouguid);
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
     * 操作人姓名
     */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
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
     * 操作人Guid
     */
    public String getOperateuserguid() {
        return super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
    }

    /**
     * 流程标识
     */
    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

}
