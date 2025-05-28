package com.epoint.xmz.kyperson.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 勘验人员表实体
 * 
 * @作者 RaoShaoliang
 * @version [版本号, 2023-07-10 16:34:44]
 */
@Entity(table = "ky_person", id = "rowguid")
public class KyPerson extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 附件
     */
    public String getClientguid() {
        return super.get("clientguid");
    }

    public void setClientguid(String clientguid) {
        super.set("clientguid", clientguid);
    }

    /**
     * 勘验rowguid
     */
    public String getKyguid() {
        return super.get("kyguid");
    }

    public void setKyguid(String kyguid) {
        super.set("kyguid", kyguid);
    }

    /**
     * 手机
     */
    public String getMobile() {
        return super.get("mobile");
    }

    public void setMobile(String mobile) {
        super.set("mobile", mobile);
    }

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
     * 理由描述
     */
    public String getReason() {
        return super.get("reason");
    }

    public void setReason(String reason) {
        super.set("reason", reason);
    }

    /**
     * 勘验结果
     */
    public String getResult() {
        return super.get("result");
    }

    public void setResult(String result) {
        super.set("result", result);
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
    
    /**
     * 反馈时间
     */
    
    public Date getRepplytime() {
        return super.getDate("repplytime");
    }

    public void setRepplytime(Date repplytime) {
        super.set("repplytime", repplytime);
    }


}
