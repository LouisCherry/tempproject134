package com.epoint.jnhospital.hospitalinfo.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 定点医院名单实体
 * 
 * @作者  JFei
 * @version [版本号, 2019-09-05 11:21:46]
 */
@Entity(table = "hospitalinfo", id = "rowguid")
public class Hospitalinfo extends BaseEntity implements Cloneable
{
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
    * 医院编码
    */
    public String getHospital_id() {
        return super.get("hospital_id");
    }

    public void setHospital_id(String hospital_id) {
        super.set("hospital_id", hospital_id);
    }

    /**
    * 医院名称
    */
    public String getHospital_name() {
        return super.get("hospital_name");
    }

    public void setHospital_name(String hospital_name) {
        super.set("hospital_name", hospital_name);
    }

    /**
    * 医疗机构全称
    */
    public String getFull_name() {
        return super.get("full_name");
    }

    public void setFull_name(String full_name) {
        super.set("full_name", full_name);
    }

    /**
    * 医院级别
    */
    public String getHospital_level() {
        return super.get("hospital_level");
    }

    public void setHospital_level(String hospital_level) {
        super.set("hospital_level", hospital_level);
    }

    /**
    * 医院等级
    */
    public String getHospital_grade() {
        return super.get("hospital_grade");
    }

    public void setHospital_grade(String hospital_grade) {
        super.set("hospital_grade", hospital_grade);
    }

    /**
    * 医院类型
    */
    public String getHospital_type() {
        return super.get("hospital_type");
    }

    public void setHospital_type(String hospital_type) {
        super.set("hospital_type", hospital_type);
    }

    /**
    * 医疗机构性质
    */
    public String getHospital_nature() {
        return super.get("hospital_nature");
    }

    public void setHospital_nature(String hospital_nature) {
        super.set("hospital_nature", hospital_nature);
    }

    /**
    * 医疗机构类别
    */
    public String getOrgan_type() {
        return super.get("organ_type");
    }

    public void setOrgan_type(String organ_type) {
        super.set("organ_type", organ_type);
    }

    /**
    * 集团医院编码
    */
    public String getGroup_hospital_code() {
        return super.get("group_hospital_code");
    }

    public void setGroup_hospital_code(String group_hospital_code) {
        super.set("group_hospital_code", group_hospital_code);
    }

    /**
    * 集团医院全称
    */
    public String getGroup_full_name() {
        return super.get("group_full_name");
    }

    public void setGroup_full_name(String group_full_name) {
        super.set("group_full_name", group_full_name);
    }

    /**
    * 联系人
    */
    public String getContacts() {
        return super.get("contacts");
    }

    public void setContacts(String contacts) {
        super.set("contacts", contacts);
    }

    /**
    * 联系人电话
    */
    public String getContant_phone() {
        return super.get("contant_phone");
    }

    public void setContant_phone(String contant_phone) {
        super.set("contant_phone", contant_phone);
    }

    /**
    * 地址
    */
    public String getAddress() {
        return super.get("address");
    }

    public void setAddress(String address) {
        super.set("address", address);
    }

    /**
    * 法人代表
    */
    public String getLegal_person() {
        return super.get("legal_person");
    }

    public void setLegal_person(String legal_person) {
        super.set("legal_person", legal_person);
    }

    /**
    * 街道
    */
    public String getStreet() {
        return super.get("street");
    }

    public void setStreet(String street) {
        super.set("street", street);
    }

    /**
    * 基本医疗保险管理部门
    */
    public String getBasic_medical_insurance_department() {
        return super.get("basic_medical_insurance_department");
    }

    public void setBasic_medical_insurance_department(String basic_medical_insurance_department) {
        super.set("basic_medical_insurance_department", basic_medical_insurance_department);
    }

    /**
    * 床位数
    */
    public Integer getBed_num() {
        return super.getInt("bed_num");
    }

    public void setBed_num(Integer bed_num) {
        super.set("bed_num", bed_num);
    }

    /**
    * 服务内容
    */
    public String getService_content() {
        return super.get("service_content");
    }

    public void setService_content(String service_content) {
        super.set("service_content", service_content);
    }

}
