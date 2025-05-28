package com.epoint.xmz.jncertapplyconfig.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 证照申办配置表实体
 *
 * @version [版本号, 2023-03-21 15:06:54]
 * @作者 future
 */
@Entity(table = "jn_certapply_config", id = "rowguid")
public class JnCertapplyConfig extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 受理机构
     */
    public String getAccept_ou() {
        return super.get("accept_ou");
    }

    public void setAccept_ou(String accept_ou) {
        super.set("accept_ou", accept_ou);
    }

    /**
     * 申请人类型
     */
    public String getApplyuser_type() {
        return super.get("applyuser_type");
    }

    public void setApplyuser_type(String applyuser_type) {
        super.set("applyuser_type", applyuser_type);
    }

    /**
     * 申报条件
     */
    public String getApply_condition() {
        return super.get("apply_condition");
    }

    public void setApply_condition(String apply_condition) {
        super.set("apply_condition", apply_condition);
    }

    /**
     * 办理费用
     */
    public Integer getBanli_free() {
        return super.getInt("banli_free");
    }

    public void setBanli_free(Integer banli_free) {
        super.set("banli_free", banli_free);
    }

    /**
     * 证照名称
     */
    public String getCertname() {
        return super.get("certname");
    }

    public void setCertname(String certname) {
        super.set("certname", certname);
    }

    /**
     * 证照说明
     */
    public String getCert_explain() {
        return super.get("cert_explain");
    }

    public void setCert_explain(String cert_explain) {
        super.set("cert_explain", cert_explain);
    }

    /**
     * 封面图片
     */
    public String getCert_picture() {
        return super.get("cert_picture");
    }

    public void setCert_picture(String cert_picture) {
        super.set("cert_picture", cert_picture);
    }

    /**
     * 证照分类
     */
    public String getCert_type() {
        return super.get("cert_type");
    }

    public void setCert_type(String cert_type) {
        super.set("cert_type", cert_type);
    }

    /**
     * 咨询电话
     */
    public String getContact_phone() {
        return super.get("contact_phone");
    }

    public void setContact_phone(String contact_phone) {
        super.set("contact_phone", contact_phone);
    }

    /**
     * 受理机构地址
     */
    public String getShouli_address() {
        return super.get("shouli_address");
    }

    public void setShouli_address(String shouli_address) {
        super.set("shouli_address", shouli_address);
    }

    /**
     * 标签分类
     */
    public String getTagtype() {
        return super.get("tagtype");
    }

    public void setTagtype(String tagtype) {
        super.set("tagtype", tagtype);
    }

    /**
     * 跳转类型
     */
    public String getTarget_type() {
        return super.get("target_type");
    }

    public void setTarget_type(String target_type) {
        super.set("target_type", target_type);
    }

    /**
     * 跳转地址
     */
    public String getTarget_url() {
        return super.get("target_url");
    }

    public void setTarget_url(String target_url) {
        super.set("target_url", target_url);
    }

    /**
     * 关联事项
     */
    public String getTask_relation() {
        return super.get("task_relation");
    }

    public void setTask_relation(String task_relation) {
        super.set("task_relation", task_relation);
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