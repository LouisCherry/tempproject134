package com.epoint.zczwfw.tsuimportinfo.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 导入反馈信息表实体
 * 
 * @author yrchan
 * @version [版本号, 2022-04-11 16:08:32]
 */
@Entity(table = "tsu_import_info", id = "rowguid")
public class TsuImportInfo extends BaseEntity implements Cloneable
{

    private static final long serialVersionUID = 5354080332606303614L;

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
     * 导入时间
     */
    public Date getImp_datetime() {
        return super.getDate("imp_datetime");
    }

    public void setImp_datetime(Date imp_datetime) {
        super.set("imp_datetime", imp_datetime);
    }

    /**
     * 导入人员
     */
    public String getImp_user() {
        return super.get("imp_user");
    }

    public void setImp_user(String imp_user) {
        super.set("imp_user", imp_user);
    }

    /**
     * 导入人员标识
     */
    public String getImp_user_guid() {
        return super.get("imp_user_guid");
    }

    public void setImp_user_guid(String imp_user_guid) {
        super.set("imp_user_guid", imp_user_guid);
    }

    /**
     * 导入处理进度
     */
    public Integer getImp_processing() {
        return super.getInt("imp_processing");
    }

    public void setImp_processing(Integer imp_processing) {
        super.set("imp_processing", imp_processing);
    }

    /**
     * 待处理附件
     */
    public String getClient_guid() {
        return super.get("client_guid");
    }

    public void setClient_guid(String client_guid) {
        super.set("client_guid", client_guid);
    }

    /**
     * 处理完成时间
     */
    public Date getFinish_datetime() {
        return super.getDate("finish_datetime");
    }

    public void setFinish_datetime(Date finish_datetime) {
        super.set("finish_datetime", finish_datetime);
    }

    /**
     * 解析数据说明
     */
    public String getAnalytical_desc() {
        return super.get("analytical_desc");
    }

    public void setAnalytical_desc(String analytical_desc) {
        super.set("analytical_desc", analytical_desc);
    }

}
