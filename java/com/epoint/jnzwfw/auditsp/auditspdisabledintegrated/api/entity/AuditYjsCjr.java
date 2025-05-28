package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 残疾人一件事表单实体
 *
 * @version [版本号, 2021-04-09 13:50:05]
 * @作者 ez
 */
@Entity(table = "audit_yjs_cjr", id = "rowguid")
public class AuditYjsCjr extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

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
     * 姓名
     */
    public String getName() {
        return super.get("name");
    }

    public void setName(String name) {
        super.set("name", name);
    }

    /**
     * 身份证号
     */
    public String getIdcard() {
        return super.get("idcard");
    }

    public void setIdcard(String idcard) {
        super.set("idcard", idcard);
    }

    /**
     * 性别
     */
    public String getSex() {
        return super.get("sex");
    }

    public void setSex(String sex) {
        super.set("sex", sex);
    }

    /**
     * 民族
     */
    public String getNation() {
        return super.get("nation");
    }

    public void setNation(String nation) {
        super.set("nation", nation);
    }

    /**
     * 文化程度
     */
    public String getEducation() {
        return super.get("education");
    }

    public void setEducation(String education) {
        super.set("education", education);
    }

    /**
     * 婚姻状况
     */
    public String getMarital() {
        return super.get("marital");
    }

    public void setMarital(String marital) {
        super.set("marital", marital);
    }

    /**
     * 户口性质
     */
    public String getResidence() {
        return super.get("residence");
    }

    public void setResidence(String residence) {
        super.set("residence", residence);
    }

    /**
     * 银行开户人
     */
    public String getBankaccountholder() {
        return super.get("bankaccountholder");
    }

    public void setBankaccountholder(String bankaccountholder) {
        super.set("bankaccountholder", bankaccountholder);
    }

    /**
     * 银行账号
     */
    public String getBankaccount() {
        return super.get("bankaccount");
    }

    public void setBankaccount(String bankaccount) {
        super.set("bankaccount", bankaccount);
    }

    /**
     * 开户行
     */
    public String getAccountbank() {
        return super.get("accountbank");
    }

    public void setAccountbank(String accountbank) {
        super.set("accountbank", accountbank);
    }

    /**
     * 低保证号
     */
    public String getDbzh() {
        return super.get("dbzh");
    }

    public void setDbzh(String dbzh) {
        super.set("dbzh", dbzh);
    }

    /**
     * 户籍性质
     */
    public String getHjxz() {
        return super.get("hjxz");
    }

    public void setHjxz(String hjxz) {
        super.set("hjxz", hjxz);
    }


  /**
   * 手机
   */
  public String getCon_phone() {
    return super.get("con_phone");
  }

  public void setCon_phone(String con_phone) {
    super.set("con_phone", con_phone);
  }

    /**
     * 固定电话
     */
    public String getCon_tel() {
        return super.get("con_tel");
    }

    public void setCon_tel(String con_tel) {
        super.set("con_tel", con_tel);
    }

    /**
     * 所属县区
     */
    public String getBelong_district() {
        return super.get("belong_district");
    }

    public void setBelong_district(String belong_district) {
        super.set("belong_district", belong_district);
    }

    /**
     * 所属乡镇
     */
    public String getBelong_town() {
        return super.get("belong_town");
    }

    public void setBelong_town(String belong_town) {
        super.set("belong_town", belong_town);
    }

    /**
     * 所属村居
     */
    public String getBelong_village() {
        return super.get("belong_village");
    }

    public void setBelong_village(String belong_village) {
        super.set("belong_village", belong_village);
    }

    /**
     * 居住地址
     */
    public String getResidence_area() {
        return super.get("residence_area");
    }

    public void setResidence_area(String residence_area) {
        super.set("residence_area", residence_area);
    }

    /**
     * 申请残疾类别
     */
    public String getDeformity_type() {
        return super.get("deformity_type");
    }

    public void setDeformity_type(String deformity_type) {
        super.set("deformity_type", deformity_type);
    }

    /**
     * 监护人姓名
     */
    public String getGuard_name() {
        return super.get("guard_name");
    }

    public void setGuard_name(String guard_name) {
        super.set("guard_name", guard_name);
    }

    /**
     * 与本人关系
     */
    public String getRelation() {
        return super.get("relation");
    }

    public void setRelation(String relation) {
        super.set("relation", relation);
    }

    /**
     * 监护人手机
     */
    public String getGuard_phone() {
        return super.get("guard_phone");
    }

    public void setGuard_phone(String guard_phone) {
        super.set("guard_phone", guard_phone);
    }

    /**
     * 监护人固话
     */
    public String getGuard_contelphone() {
        return super.get("guard_contelphone");
    }

    public void setGuard_contelphone(String guard_contelphone) {
        super.set("guard_contelphone", guard_contelphone);
    }

    /**
     * 出生日期
     */
    public Date getBrith_time() {
        return super.getDate("brith_time");
    }

    public void setBrith_time(Date brith_time) {
        super.set("brith_time", brith_time);
    }

    /**
     * 行政区域code
     */
    public String getNodeid() {
        return super.get("nodeid");
    }

    public void setNodeid(String nodeid) {
        super.set("nodeid", nodeid);
    }
    
    /**
     * 是否有监护人
     */
    public String getHasGuard() {
        return super.get("hasguard");
    }

    public void setHasGuard(String hasguard) {
        super.set("hasguard", hasguard);
    }
    
    /**
     * 申请理由
     */
    public String getapplyReason() {
        return super.get("applyreason");
    }

    public void setapplyReason(String applyreason) {
        super.set("applyreason", applyreason);
    }
    public String getGuard_idcard() {
        return super.get("guard_idcard");
    }

    public void setGuard_idcard(String guard_idcard) {
        super.set("guard_idcard", guard_idcard);
    }
    

}
