package com.epoint.evainstance.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 评价表实体
 * 
 * @作者  lizhenjie
 * @version [版本号, 2020-09-06 14:00:35]
 */
@Entity(table = "evainstance_ck", id = "rowguid")
public class EvainstanceCk extends BaseEntity implements Cloneable
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
    * 办件编号
    */
    public String getProjectno() {
        return super.get("projectno");
    }

    public void setProjectno(String projectno) {
        super.set("projectno", projectno);
    }

    /**
    * Datasource
    */
    public String getDatasource() {
        return super.get("datasource");
    }

    public void setDatasource(String datasource) {
        super.set("datasource", datasource);
    }

    /**
    * Appstatus
    */
    public String getAppstatus() {
        return super.get("appstatus");
    }

    public void setAppstatus(String appstatus) {
        super.set("appstatus", appstatus);
    }

    /**
    * Flag
    */
    public String getFlag() {
        return super.get("flag");
    }

    public void setFlag(String flag) {
        super.set("flag", flag);
    }

    /**
    * 评价次数
    */
    public Double getAssessnumber() {
        return super.getDouble("assessnumber");
    }

    public void setAssessnumber(Double assessnumber) {
        super.set("assessnumber", assessnumber);
    }

    /**
    * isdefault
    */
    public String getIsdefault() {
        return super.get("isdefault");
    }

    public void setIsdefault(String isdefault) {
        super.set("isdefault", isdefault);
    }

    /**
    * EffectivEvalua
    */
    public String getEffectivevalua() {
        return super.get("effectivevalua");
    }

    public void setEffectivevalua(String effectivevalua) {
        super.set("effectivevalua", effectivevalua);
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
    * Prostatus
    */
    public String getProstatus() {
        return super.get("prostatus");
    }

    public void setProstatus(String prostatus) {
        super.set("prostatus", prostatus);
    }

    /**
    * Evalevel
    */
    public String getEvalevel() {
        return super.get("evalevel");
    }

    public void setEvalevel(String evalevel) {
        super.set("evalevel", evalevel);
    }

    /**
    * Evacontant
    */
    public String getEvacontant() {
        return super.get("evacontant");
    }

    public void setEvacontant(String evacontant) {
        super.set("evacontant", evacontant);
    }

    /**
    * 评价详情
    */
    public String getEvaldetail() {
        return super.get("evaldetail");
    }

    public void setEvaldetail(String evaldetail) {
        super.set("evaldetail", evaldetail);
    }

    /**
    * 输入评价详情
    */
    public String getWritingevaluation() {
        return super.get("writingevaluation");
    }

    public void setWritingevaluation(String writingevaluation) {
        super.set("writingevaluation", writingevaluation);
    }

    /**
    * 事项名称
    */
    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
    }

    /**
    * 事项编码
    */
    public String getTaskcode() {
        return super.get("taskcode");
    }

    public void setTaskcode(String taskcode) {
        super.set("taskcode", taskcode);
    }

    /**
    * Taskhandleitem
    */
    public String getTaskhandleitem() {
        return super.get("taskhandleitem");
    }

    public void setTaskhandleitem(String taskhandleitem) {
        super.set("taskhandleitem", taskhandleitem);
    }

    /**
    * Promisetime
    */
    public String getPromisetime() {
        return super.get("promisetime");
    }

    public void setPromisetime(String promisetime) {
        super.set("promisetime", promisetime);
    }
    
    
    /**
     * 回访信息
     */
     public String getHfxx() {
         return super.get("hfxx");
     }

     public void setHfxx(String hfxx) {
         super.set("hfxx", hfxx);
     }

    /**
    * 部门编码
    */
    public String getDeptcode() {
        return super.get("deptcode");
    }

    public void setDeptcode(String deptcode) {
        super.set("deptcode", deptcode);
    }

    /**
    * Prodepart
    */
    public String getProdepart() {
        return super.get("prodepart");
    }

    public void setProdepart(String prodepart) {
        super.set("prodepart", prodepart);
    }

    /**
    * Userprop
    */
    public String getUserprop() {
        return super.get("userprop");
    }

    public void setUserprop(String userprop) {
        super.set("userprop", userprop);
    }

    /**
    * 评价人
    */
    public String getUsername() {
        return super.get("username");
    }

    public void setUsername(String username) {
        super.set("username", username);
    }

    /**
    * Applicant
    */
    public String getApplicant() {
        return super.get("applicant");
    }

    public void setApplicant(String applicant) {
        super.set("applicant", applicant);
    }

    /**
    * Certkey
    */
    public String getCertkey() {
        return super.get("certkey");
    }

    public void setCertkey(String certkey) {
        super.set("certkey", certkey);
    }

    /**
    * Certkeygov
    */
    public String getCertkeygov() {
        return super.get("certkeygov");
    }

    public void setCertkeygov(String certkeygov) {
        super.set("certkeygov", certkeygov);
    }

    /**
    * 受理时间
    */
    public Date getAcceptdate() {
        return super.getDate("acceptdate");
    }

    public void setAcceptdate(Date acceptdate) {
        super.set("acceptdate", acceptdate);
    }

    /**
    * 创建时间
    */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }

    /**
    * sync_sign
    */
    public String getSync_sign() {
        return super.get("sync_sign");
    }

    public void setSync_sign(String sync_sign) {
        super.set("sync_sign", sync_sign);
    }

    /**
    * answerStatus
    */
    public String getAnswerstatus() {
        return super.get("answerstatus");
    }

    public void setAnswerstatus(String answerstatus) {
        super.set("answerstatus", answerstatus);
    }

    /**
    * pf
    */
    public String getPf() {
        return super.get("pf");
    }

    public void setPf(String pf) {
        super.set("pf", pf);
    }

    /**
    * 满意度
    */
    public String getSatisfaction() {
        return super.get("satisfaction");
    }

    public void setSatisfaction(String satisfaction) {
        super.set("satisfaction", satisfaction);
    }

    /**
    * 时间
    */
    public Date getAssesstime() {
        return super.getDate("assesstime");
    }

    public void setAssesstime(Date assesstime) {
        super.set("assesstime", assesstime);
    }

    /**
    * 窗口名称
    */
    public String getWinname() {
        return super.get("winname");
    }

    public void setWinname(String winname) {
        super.set("winname", winname);
    }

    /**
    * 窗口人员
    */
    public String getWinusername() {
        return super.get("winusername");
    }

    public void setWinusername(String winusername) {
        super.set("winusername", winusername);
    }

    /**
    * 联系方式
    */
    public String getMobile() {
        return super.get("mobile");
    }

    public void setMobile(String mobile) {
        super.set("mobile", mobile);
    }

    /**
    * 评价渠道
    */
    public String getPjqd() {
        return super.get("pjqd");
    }

    public void setPjqd(String pjqd) {
        super.set("pjqd", pjqd);
    }

    /**
    * 是否回复
    */
    public String getSfhf() {
        return super.get("sfhf");
    }

    public void setSfhf(String sfhf) {
        super.set("sfhf", sfhf);
    }

    /**
    * 是否回访
    */
    public String getSfhfang() {
        return super.get("sfhfang");
    }

    public void setSfhfang(String sfhfang) {
        super.set("sfhfang", sfhfang);
    }

    /**
    * 考核意见
    */
    public String getKhyj() {
        return super.get("khyj");
    }

    public void setKhyj(String khyj) {
        super.set("khyj", khyj);
    }

    /**
    * 考核部门
    */
    public String getKhouname() {
        return super.get("khouname");
    }

    public void setKhouname(String khouname) {
        super.set("khouname", khouname);
    }

    /**
    * 领导批示
    */
    public String getLdps() {
        return super.get("ldps");
    }

    public void setLdps(String ldps) {
        super.set("ldps", ldps);
    }

    /**
    * 领导名称
    */
    public String getLdmc() {
        return super.get("ldmc");
    }

    public void setLdmc(String ldmc) {
        super.set("ldmc", ldmc);
    }

    /**
    * 领导id
    */
    public String getLdguid() {
        return super.get("ldguid");
    }

    public void setLdguid(String ldguid) {
        super.set("ldguid", ldguid);
    }

    /**
    * 是否恶意评价
    */
    public String getSfeypj() {
        return super.get("sfeypj");
    }

    public void setSfeypj(String sfeypj) {
        super.set("sfeypj", sfeypj);
    }

    /**
    * 回访调查
    */
    public String getHfdc() {
        return super.get("hfdc");
    }

    public void setHfdc(String hfdc) {
        super.set("hfdc", hfdc);
    }

    /**
    * 处理等级
    */
    public String getCldj() {
        return super.get("cldj");
    }

    public void setCldj(String cldj) {
        super.set("cldj", cldj);
    }

    /**
    * 拟办意见
    */
    public String getNbyj() {
        return super.get("nbyj");
    }

    public void setNbyj(String nbyj) {
        super.set("nbyj", nbyj);
    }
    /**
     * 服务次数
     */
     public int getServicenumber() {
         return super.getInt("servicenumber");
     }

     public void setServicenumber(int servicenumber) {
         super.set("servicenumber", servicenumber);
     }
    /**
    * 整改结果
    */
    public String getZgjg() {
        return super.get("zgjg");
    }

    public void setZgjg(String zgjg) {
        super.set("zgjg", zgjg);
    }

    /**
    * 回复结果
    */
    public String getHfjg() {
        return super.get("hfjg");
    }

    public void setHfjg(String hfjg) {
        super.set("hfjg", hfjg);
    }
    /**
     * 部门名称
     */
     public String getOrgName() {
         return super.get("orgName");
     }

     public void setOrgName(String orgName) {
         super.set("orgName", orgName);
     }

}
