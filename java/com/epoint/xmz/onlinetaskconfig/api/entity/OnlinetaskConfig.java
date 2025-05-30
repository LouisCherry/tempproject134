package com.epoint.xmz.onlinetaskconfig.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 居民办事配置实体
 *
 * @version [版本号, 2023-10-17 15:38:09]
 * @作者 RaoShaoliang
 */
@Entity(table = "onlinetask_config", id = "rowguid")
public class OnlinetaskConfig extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 预览图
     */
    public String getClientguid() {
        return super.get("clientguid");
    }

    public void setClientguid(String clientguid) {
        super.set("clientguid", clientguid);
    }

    /**
     * 相关描述
     */
    public String getDescribeinfo() {
        return super.get("describeinfo");
    }

    public void setDescribeinfo(String describeinfo) {
        super.set("describeinfo", describeinfo);
    }

    /**
     * 所属分类
     */
    public String getKind() {
        return super.get("kind");
    }

    public void setKind(String kind) {
        super.set("kind", kind);
    }

    /**
     * 部门guid
     */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    /**
     * 部门名称
     */
    public String getOuname() {
        return super.get("ouname");
    }

    public void setOuname(String ouname) {
        super.set("ouname", ouname);
    }

    /**
     * 网站名称
     */
    public String getTitle() {
        return super.get("title");
    }

    public void setTitle(String title) {
        super.set("title", title);
    }

    /**
     * 网站地址
     */
    public String getUrl() {
        return super.get("url");
    }

    public void setUrl(String url) {
        super.set("url", url);
    }

    /**
     * 信息类型
     */
    public String getInfoKind() {
        return super.get("infokind");
    }

    public void setInfoKind(String infokind) {
        super.set("infokind", infokind);
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
     * 年份标识
     */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
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
     * 操作者名字
     */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
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