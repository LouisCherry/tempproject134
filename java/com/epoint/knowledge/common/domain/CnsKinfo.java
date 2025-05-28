package com.epoint.knowledge.common.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 知识信息表实体
 * 
 * @作者  xuyunhai
 * @version [版本号, 2017-02-13 10:28:44]
 */
@Entity(table = "CNS_KINFO", id = "rowguid")
public class CnsKinfo extends BaseEntity implements Cloneable
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
    * 类别标识
    */
    public String getCategoryguid() {
        return super.get("categoryguid");
    }

    public void setCategoryguid(String categoryguid) {
        super.set("categoryguid", categoryguid);
    }

    /**
    * 类别编码
    */
    public String getCategorycode() {
        return super.get("categorycode");
    }

    public void setCategorycode(String categorycode) {
        super.set("categorycode", categorycode);
    }

    /**
    * 类别名称
    */
    public String getCategoryname() {
        return super.get("categoryname");
    }

    public void setCategoryname(String categoryname) {
        super.set("categoryname", categoryname);
    }

    /**
    * 知识名称
    */
    public String getKname() {
        return super.get("kname");
    }

    public void setKname(String kname) {
        super.set("kname", kname);
    }

    /**
    * 知识内容
    */
    public String getKcontent() {
        return super.get("kcontent");
    }

    public void setKcontent(String kcontent) {
        super.set("kcontent", kcontent);
    }

    /**
    * 知识属性
    */
    public Integer getKtyype() {
        return super.getInt("ktyype");
    }

    public void setKtyype(Integer ktyype) {
        super.set("ktyype", ktyype);
    }

    /**
    * 发布部门
    */
    public String getOuname() {
        return super.get("ouname");
    }

    public void setOuname(String ouname) {
        super.set("ouname", ouname);
    }

    /**
    * 发布部门Guid
    */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    /**
    * 发布日期
    */
    public Date getCreatdate() {
        return super.getDate("creatdate");
    }

    public void setCreatdate(Date creatdate) {
        super.set("creatdate", creatdate);
    }

    /**
    * 生效时间
    */
    public Date getBegindate() {
        return super.getDate("begindate");
    }

    public void setBegindate(Date begindate) {
        super.set("begindate", begindate);
    }

    /**
    * 时效时间
    */
    public Date getEffectdate() {
        return super.getDate("effectdate");
    }

    public void setEffectdate(Date effectdate) {
        super.set("effectdate", effectdate);
    }

    /**
    * 发布人
    */
    public String getPublishperson() {
        return super.get("publishperson");
    }

    public void setPublishperson(String publishperson) {
        super.set("publishperson", publishperson);
    }

    /**
    * 发布人Guid
    */
    public String getPublishpguid() {
        return super.get("publishpguid");
    }

    public void setPublishpguid(String publishpguid) {
        super.set("publishpguid", publishpguid);
    }

    /**
    * 作者
    */
    public String getKauthor() {
        return super.get("kauthor");
    }

    public void setKauthor(String kauthor) {
        super.set("kauthor", kauthor);
    }

    /**
    * 知识状态
    */
    public String getKstatus() {
        return super.get("kstatus");
    }

    public void setKstatus(String kstatus) {
        super.set("kstatus", kstatus);
    }

    /**
    * 是否启用
    */
    public String getIsenable() {
        return super.get("isenable");
    }

    public void setIsenable(String isenable) {
        super.set("isenable", isenable);
    }

    /**
    * 个性化知识Guid
    */
    public String getExtrakguid() {
        return super.get("extrakguid");
    }

    public void setExtrakguid(String extrakguid) {
        super.set("extrakguid", extrakguid);
    }

    /**
    * 附件
    */
    public String getAttachguid() {
        return super.get("attachguid");
    }

    public void setAttachguid(String attachguid) {
        super.set("attachguid", attachguid);
    }

    /**
    * 所属辖区
    */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
    * 是否置顶
    */
    public Integer getIstop() {
        return super.getInt("istop");
    }

    public void setIstop(Integer istop) {
        super.set("istop", istop);
    }

    /**
     * 二位编码
     */
    public String getTwocode() {
        return super.get("twocode");
    }

    public void setTwocode(String twocode) {
        super.set("twocode", twocode);
    }

    /**
     * 四位编码
     */
    public String getFourcode() {
        return super.get("fourcode");
    }

    public void setFourcode(String fourcode) {
        super.set("fourcode", fourcode);
    }

    /**
     * 六位编码
     */
    public String getSixcode() {
        return super.get("sixcode");
    }

    public void setSixcode(String sixcode) {
        super.set("sixcode", sixcode);
    }

    /**以下编码为全文检索使用**/
    /**
     * 八位编码
     */
    public String getEightcode() {
        return super.get("eightcode");
    }

    public void setEightcode(String eightcode) {
        super.set("eightcode", eightcode);
    }

    /**
     * 十ss位编码
     */
    public String getTencode() {
        return super.get("tencode");
    }

    public void setTencode(String tencode) {
        super.set("tencode", tencode);
    }
    /**
     * 
     * 点击次数
     */
    public Integer getVisittimes(){
        
        return super.get("visittimes");
    }
    public void setVisttimes(Integer visittimes){
        super.set("visittimes", visittimes);
    }
    /**
     * 
     * 父类rowguid
     */
    public String getEditguid(){
        return super.get("editguid");
    }
    public void setEditguid(String editguid){
        super.set("editguid", editguid);
    }
    /**
     * 
     * 是否被删除
     */
    public String getIsdel(){
        return super.get("isdel");
    }
    public void setIsdel(String isdel){
        super.set("isdel", isdel);
    }
    
    /**
     * 
     * pviguid
     */
    public String getPviguid(){
        return super.get("pviguid");
    }
    public void setPviguid(String pviguid){
        super.set("pviguid", pviguid);
    }
    
    /**
     * 
     * 是否是知识提问
     */
    public String getIsquestion(){
        return super.get("isquestion");
    }
    public void setIsquestion(String isquestion){
        super.set("isquestion", isquestion);
    }
}
