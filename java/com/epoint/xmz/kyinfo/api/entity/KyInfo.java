package com.epoint.xmz.kyinfo.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 勘验信息表实体
 * 
 * @作者 RaoShaoliang
 * @version [版本号, 2023-07-10 16:17:19]
 */
@Entity(table = "ky_info", id = "rowguid")
public class KyInfo extends BaseEntity implements Cloneable
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
     * 截止时间
     */
    public Date getEndtime() {
        return super.getDate("endtime");
    }

    public void setEndtime(Date endtime) {
        super.set("endtime", endtime);
    }

    /**
     * 勘验城市
     */
    public String getKycity() {
        return super.get("kycity");
    }

    public void setKycity(String kycity) {
        super.set("kycity", kycity);
    }

    /**
     * 详细地址
     */
    public String getKylocation() {
        return super.get("kylocation");
    }

    public void setKylocation(String kylocation) {
        super.set("kylocation", kylocation);
    }

    /**
     * 勘验时间
     */
    public Date getKytime() {
        return super.getDate("kytime");
    }

    public void setKytime(Date kytime) {
        super.set("kytime", kytime);
    }

    /**
     * 办件rowguid
     */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
     * 办件名称
     */
    public String getProjectname() {
        return super.get("projectname");
    }

    public void setProjectname(String projectname) {
        super.set("projectname", projectname);
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
     * 城管侧返回的tasknum
     */
    public String getTaskNum() {
        return super.get("taskNum");
    }

    public void setTaskNum(String taskNum) {
        super.set("taskNum", taskNum);
    }
    
    /**
     * 城管局返回的recID
     */
    public String getRecID() {
        return super.get("recID");
    }

    public void setRecID(String recID) {
        super.set("recID", recID);
    }
    
    /**
     * 城管局返回的issync
     */
    public String getIssync() {
        return super.get("issync");
    }

    public void setIssync(String issync) {
        super.set("issync", issync);
    }
}
