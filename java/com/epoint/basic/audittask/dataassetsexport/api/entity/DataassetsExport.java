package com.epoint.basic.audittask.dataassetsexport.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 导出记录实体
 * 
 * @作者  95453
 * @version [版本号, 2020-08-24 17:27:23]
 */
@Entity(table = "DATAASSETS_EXPORT", id = "rowguid")
public class DataassetsExport extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 生成时间
     */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }

    /**
    * 任务名称
    */
    public String getExportname() {
        return super.get("exportname");
    }

    public void setExportname(String exportname) {
        super.set("exportname", exportname);
    }

    /**
    * 添加人标识
    */
    public String getExportuserguid() {
        return super.get("exportuserguid");
    }

    public void setExportuserguid(String exportuserguid) {
        super.set("exportuserguid", exportuserguid);
    }

    /**
    * 添加人名称
    */
    public String getExportusername() {
        return super.get("exportusername");
    }

    public void setExportusername(String exportusername) {
        super.set("exportusername", exportusername);
    }

    /**
    * 流水号
    */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
    * 导出时间
    */
    public Date getExportdate() {
        return super.getDate("exportdate");
    }

    public void setExportdate(Date exportdate) {
        super.set("exportdate", exportdate);
    }

    /**
    * 密码
    */
    public String getPassword() {
        return super.get("password");
    }

    public void setPassword(String password) {
        super.set("password", password);
    }

    /**
    * 附件标识
    */
    public String getCliengguid() {
        return super.get("cliengguid");
    }

    public void setCliengguid(String cliengguid) {
        super.set("cliengguid", cliengguid);
    }

    /**
    * 数据资产类型
    */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
    }

    /**
    * 导出状态
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 流程实例标识
    */
    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

    /**
    * 操作人所属单位guid
    */
    public String getOperateuserbaseouguid() {
        return super.get("operateuserbaseouguid");
    }

    public void setOperateuserbaseouguid(String operateuserbaseouguid) {
        super.set("operateuserbaseouguid", operateuserbaseouguid);
    }

    /**
    * 操作人所属部门guid
    */
    public String getOperateuserouguid() {
        return super.get("operateuserouguid");
    }

    public void setOperateuserouguid(String operateuserouguid) {
        super.set("operateuserouguid", operateuserouguid);
    }

    /**
    * 操作人guid
    */
    public String getOperateuserguid() {
        return super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
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
     * 区分产品和项目
     */
     public String getProjecttype() {
         return super.get("projecttype");
     }

     public void setProjecttype(String projecttype) {
         super.set("projecttype", projecttype);
     }


}
