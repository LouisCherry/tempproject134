package com.epoint.expert.expertinfo.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 专家表实体
 * 
 * @作者  cqsong
 * @version [版本号, 2019-08-21 16:36:51]
 */
@Entity(table = "Expert_Info", id = "rowguid")
public class ExpertInfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 是否删除
     */
    public String getIs_del() {
        return super.get("is_del");
    }

    public void setIs_del(String is_del) {
        super.set("is_del", is_del);
    }

    /**
    * 附件
    */
    public String getCliengguid() {
        return super.get("cliengguid");
    }

    public void setCliengguid(String cliengguid) {
        super.set("cliengguid", cliengguid);
    }

    /**
    * 专家状态
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 备注
    */
    public String getRemark() {
        return super.get("remark");
    }

    public void setRemark(String remark) {
        super.set("remark", remark);
    }

    /**
    * 获奖状况
    */
    public String getAwards() {
        return super.get("awards");
    }

    public void setAwards(String awards) {
        super.set("awards", awards);
    }

    /**
    * 工作简历
    */
    public String getWorkexperience() {
        return super.get("workexperience");
    }

    public void setWorkexperience(String workexperience) {
        super.set("workexperience", workexperience);
    }

    /**
    * 是否在职
    */
    public Integer getIs_zaizhi() {
        return super.getInt("is_zaizhi");
    }

    public void setIs_zaizhi(Integer is_zaizhi) {
        super.set("is_zaizhi", is_zaizhi);
    }

    /**
    * 是否资深评委
    */
    public Integer getIs_zishen() {
        return super.getInt("is_zishen");
    }

    public void setIs_zishen(Integer is_zishen) {
        super.set("is_zishen", is_zishen);
    }

    /**
    * 是否应急评委
    */
    public Integer getIs_yingji() {
        return super.getInt("is_yingji");
    }

    public void setIs_yingji(Integer is_yingji) {
        super.set("is_yingji", is_yingji);
    }

    /**
    * 区
    */
    public String getCountry() {
        return super.get("country");
    }

    public void setCountry(String country) {
        super.set("country", country);
    }

    /**
    * 市
    */
    public String getCity() {
        return super.get("city");
    }

    public void setCity(String city) {
        super.set("city", city);
    }

    /**
    * 省
    */
    public String getProvince() {
        return super.get("province");
    }

    public void setProvince(String province) {
        super.set("province", province);
    }

    /**
    * 评标专业
    */
    public String getPingbzy() {
        return super.get("pingbzy");
    }

    public void setPingbzy(String pingbzy) {
        super.set("pingbzy", pingbzy);
    }

    /**
    * 邮政编码
    */
    public String getPostcode() {
        return super.get("postcode");
    }

    public void setPostcode(String postcode) {
        super.set("postcode", postcode);
    }

    /**
    * 专业特长
    */
    public String getZhuanytc() {
        return super.get("zhuanytc");
    }

    public void setZhuanytc(String zhuanytc) {
        super.set("zhuanytc", zhuanytc);
    }

    /**
    * 注册登记证书编号
    */
    public String getZcdjbh() {
        return super.get("zcdjbh");
    }

    public void setZcdjbh(String zcdjbh) {
        super.set("zcdjbh", zcdjbh);
    }

    /**
    * 职业证书编号
    */
    public String getZyzsbh() {
        return super.get("zyzsbh");
    }

    public void setZyzsbh(String zyzsbh) {
        super.set("zyzsbh", zyzsbh);
    }

    /**
    * 职业资格等级
    */
    public String getZyzgdj() {
        return super.get("zyzgdj");
    }

    public void setZyzgdj(String zyzgdj) {
        super.set("zyzgdj", zyzgdj);
    }

    /**
    * 职业资格序列
    */
    public String getZyzgxl() {
        return super.get("zyzgxl");
    }

    public void setZyzgxl(String zyzgxl) {
        super.set("zyzgxl", zyzgxl);
    }

    /**
    * 证书入库时间
    */
    public Date getZyzgzsdate() {
        return super.getDate("zyzgzsdate");
    }

    public void setZyzgzsdate(Date zyzgzsdate) {
        super.set("zyzgzsdate", zyzgzsdate);
    }

    /**
    * 职业资格证书号
    */
    public String getZyzgzsh() {
        return super.get("zyzgzsh");
    }

    public void setZyzgzsh(String zyzgzsh) {
        super.set("zyzgzsh", zyzgzsh);
    }

    /**
    * 职业资格证书名称
    */
    public String getZyzgzs() {
        return super.get("zyzgzs");
    }

    public void setZyzgzs(String zyzgzs) {
        super.set("zyzgzs", zyzgzs);
    }

    /**
    * 从事专业时间
    */
    public Date getCszydate() {
        return super.getDate("cszydate");
    }

    public void setCszydate(Date cszydate) {
        super.set("cszydate", cszydate);
    }

    /**
    * 从事专业
    */
    public String getCszy() {
        return super.get("cszy");
    }

    public void setCszy(String cszy) {
        super.set("cszy", cszy);
    }

    /**
    * 所属行业
    */
    public String getHangy() {
        return super.get("hangy");
    }

    public void setHangy(String hangy) {
        super.set("hangy", hangy);
    }

    /**
    * 职称通过时间
    */
    public Date getZhicdate() {
        return super.getDate("zhicdate");
    }

    public void setZhicdate(Date zhicdate) {
        super.set("zhicdate", zhicdate);
    }

    /**
    * 技术职称
    */
    public String getZhic() {
        return super.get("zhic");
    }

    public void setZhic(String zhic) {
        super.set("zhic", zhic);
    }

    /**
    * 职务
    */
    public String getZhiw() {
        return super.get("zhiw");
    }

    public void setZhiw(String zhiw) {
        super.set("zhiw", zhiw);
    }

    /**
    * 通讯地址
    */
    public String getAddress() {
        return super.get("address");
    }

    public void setAddress(String address) {
        super.set("address", address);
    }

    /**
    * 所属单位
    */
    public String getComanyguid() {
        return super.get("comanyguid");
    }

    public void setComanyguid(String comanyguid) {
        super.set("comanyguid", comanyguid);
    }

    /**
    * 外语语种
    */
    public String getLanguage() {
        return super.get("language");
    }

    public void setLanguage(String language) {
        super.set("language", language);
    }

    /**
    * 文化程度
    */
    public String getWenhuacd() {
        return super.get("wenhuacd");
    }

    public void setWenhuacd(String wenhuacd) {
        super.set("wenhuacd", wenhuacd);
    }

    /**
    * 学历
    */
    public String getXueli() {
        return super.get("xueli");
    }

    public void setXueli(String xueli) {
        super.set("xueli", xueli);
    }

    /**
    * 所学专业
    */
    public String getZhuany() {
        return super.get("zhuany");
    }

    public void setZhuany(String zhuany) {
        super.set("zhuany", zhuany);
    }

    /**
    * 毕业日期
    */
    public Date getBiyedate() {
        return super.getDate("biyedate");
    }

    public void setBiyedate(Date biyedate) {
        super.set("biyedate", biyedate);
    }

    /**
    * 学历证书编号
    */
    public String getXuelino() {
        return super.get("xuelino");
    }

    public void setXuelino(String xuelino) {
        super.set("xuelino", xuelino);
    }

    /**
    * 毕业院校
    */
    public String getBiyeyx() {
        return super.get("biyeyx");
    }

    public void setBiyeyx(String biyeyx) {
        super.set("biyeyx", biyeyx);
    }

    /**
    * 健康状况
    */
    public String getHealth() {
        return super.get("health");
    }

    public void setHealth(String health) {
        super.set("health", health);
    }

    /**
    * 邮箱
    */
    public String getEmail() {
        return super.get("email");
    }

    public void setEmail(String email) {
        super.set("email", email);
    }

    /**
    * 住宅电话
    */
    public String getHomephone() {
        return super.get("homephone");
    }

    public void setHomephone(String homephone) {
        super.set("homephone", homephone);
    }

    /**
    * 联系电话
    */
    public String getContactphone() {
        return super.get("contactphone");
    }

    public void setContactphone(String contactphone) {
        super.set("contactphone", contactphone);
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
    * 身份证号
    */
    public String getCertnum() {
        return super.get("certnum");
    }

    public void setCertnum(String certnum) {
        super.set("certnum", certnum);
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
    * 出生日期
    */
    public Date getBirthdate() {
        return super.getDate("birthdate");
    }

    public void setBirthdate(Date birthdate) {
        super.set("birthdate", birthdate);
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
    * 民族
    */
    public String getNation() {
        return super.get("nation");
    }

    public void setNation(String nation) {
        super.set("nation", nation);
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
    * 性别
    */
    public Integer getSex() {
        return super.getInt("sex");
    }

    public void setSex(Integer sex) {
        super.set("sex", sex);
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
    * 专家姓名
    */
    public String getName() {
        return super.get("name");
    }

    public void setName(String name) {
        super.set("name", name);
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
    * 专家编号
    */
    public String getExpertno() {
        return super.get("expertno");
    }

    public void setExpertno(String expertno) {
        super.set("expertno", expertno);
    }

}
