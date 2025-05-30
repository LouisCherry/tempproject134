package com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
* 实体类  
* 
* @作者 zhaoy
* @version [版本号, 2019/6/2 17:30:14]
*/
@Entity(table = "audit_project_form_sgxkz_danti", id = "rowguid")
public class AuditProjectFormSgxkzDanti extends BaseEntity implements Cloneable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
    *OperateUserName的set方法
    */
    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    /**
    *OperateUserName的get方法
    */
    public String getOperateusername() {
        return super.getStr("operateusername");
    }

    /**
    *OperateDate的set方法
    */
    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
    *OperateDate的get方法
    */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    /**
    *Row_ID的set方法
    */
    public void setRowid(Integer row_id) {
        super.set("row_id", row_id);
    }

    /**
    *Row_ID的get方法
    */
    public Integer getRowid() {
        return super.getInt("row_id");
    }

    /**
    *YearFlag的set方法
    */
    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    /**
    *YearFlag的get方法
    */
    public String getYearflag() {
        return super.getStr("yearflag");
    }

    /**
    *RowGuid的set方法
    */
    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
    *RowGuid的get方法
    */
    public String getRowguid() {
        return super.getStr("rowguid");
    }

    /**
    *ChangDuMi的set方法
    */
    public void setChangdumi(String changdumi) {
        super.set("changdumi", changdumi);
    }

    /**
    *ChangDuMi的get方法
    */
    public String getChangdumi() {
        return super.getStr("changdumi");
    }

    /**
    *DanTiJianGouZhuWuMingChen的set方法
    */
    public void setDantijiangouzhuwumingchen(String dantijiangouzhuwumingchen) {
        super.set("dantijiangouzhuwumingchen", dantijiangouzhuwumingchen);
    }

    /**
    *DanTiJianGouZhuWuMingChen的get方法
    */
    public String getDantijiangouzhuwumingchen() {
        return super.getStr("dantijiangouzhuwumingchen");
    }

    /**
    *DiShangCengShu的set方法
    */
    public void setDishangcengshu(String dishangcengshu) {
        super.set("dishangcengshu", dishangcengshu);
    }

    /**
    *DiShangCengShu的get方法
    */
    public String getDishangcengshu() {
        return super.getStr("dishangcengshu");
    }

    /**
    *DiShangChangDuMi的set方法
    */
    public void setDishangchangdumi(String dishangchangdumi) {
        super.set("dishangchangdumi", dishangchangdumi);
    }

    /**
    *DiShangChangDuMi的get方法
    */
    public String getDishangchangdumi() {
        return super.getStr("dishangchangdumi");
    }

    /**
    *DiShangJianZhuMianJiPingFangMi的set方法
    */
    public void setDishangjianzhumianjipingfangmi(String dishangjianzhumianjipingfangmi) {
        super.set("dishangjianzhumianjipingfangmi", dishangjianzhumianjipingfangmi);
    }

    /**
    *DiShangJianZhuMianJiPingFangMi的get方法
    */
    public String getDishangjianzhumianjipingfangmi() {
        return super.getStr("dishangjianzhumianjipingfangmi");
    }

    /**
    *DiXiaCengShu的set方法
    */
    public void setDixiacengshu(String dixiacengshu) {
        super.set("dixiacengshu", dixiacengshu);
    }

    /**
    *DiXiaCengShu的get方法
    */
    public String getDixiacengshu() {
        return super.getStr("dixiacengshu");
    }

    /**
    *DiXiaChangDuMi的set方法
    */
    public void setDixiachangdumi(String dixiachangdumi) {
        super.set("dixiachangdumi", dixiachangdumi);
    }

    /**
    *DiXiaChangDuMi的get方法
    */
    public String getDixiachangdumi() {
        return super.getStr("dixiachangdumi");
    }

    /**
    *DiXiaJianZhuMianJiPingFangMi的set方法
    */
    public void setDixiajianzhumianjipingfangmi(String dixiajianzhumianjipingfangmi) {
        super.set("dixiajianzhumianjipingfangmi", dixiajianzhumianjipingfangmi);
    }

    /**
    *DiXiaJianZhuMianJiPingFangMi的get方法
    */
    public String getDixiajianzhumianjipingfangmi() {
        return super.getStr("dixiajianzhumianjipingfangmi");
    }

    /**
    *GongChengZaoJiaWanYuan的set方法
    */
    public void setGongchengzaojiawanyuan(String gongchengzaojiawanyuan) {
        super.set("gongchengzaojiawanyuan", gongchengzaojiawanyuan);
    }

    /**
    *GongChengZaoJiaWanYuan的get方法
    */
    public String getGongchengzaojiawanyuan() {
        return super.getStr("gongchengzaojiawanyuan");
    }

    /**
    *JianZhuMianJiPingFangMi的set方法
    */
    public void setJianzhumianjipingfangmi(String jianzhumianjipingfangmi) {
        super.set("jianzhumianjipingfangmi", jianzhumianjipingfangmi);
    }

    /**
    *JianZhuMianJiPingFangMi的get方法
    */
    public String getJianzhumianjipingfangmi() {
        return super.getStr("jianzhumianjipingfangmi");
    }

    /**
    *KuaDuMi的set方法
    */
    public void setKuadumi(String kuadumi) {
        super.set("kuadumi", kuadumi);
    }

    /**
    *KuaDuMi的get方法
    */
    public String getKuadumi() {
        return super.getStr("kuadumi");
    }

    /**
    *form_id的set方法
    */
    public void setFormid(String form_id) {
        super.set("form_id", form_id);
    }

    /**
    *form_id的get方法
    */
    public String getFormid() {
        return super.getStr("form_id");
    }

    /**
    *formguid的set方法
    */
    public void setFormguid(String formguid) {
        super.set("formguid", formguid);
    }

    /**
    *formguid的get方法
    */
    public String getFormguid() {
        return super.getStr("formguid");
    }
    
    /**
    * 办件标识
    */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

}
