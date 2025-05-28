package com.epoint.evainstanceck.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 好差评信息表实体
 * 
 * @作者 31220
 * @version [版本号, 2023-11-06 11:18:19]
 */
@Entity(table = "EVAINSTANCE_CK_temp", id = "rowid")
public class EvainstanceCk extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 是否整改
     */
    public String getIszg() {
        return super.get("iszg");
    }

    public void setIszg(String iszg) {
        super.set("iszg", iszg);
    }

    /**
     * BelongXiaQuCode
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
     * OperateUserName
     */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    /**
     * OperateDate
     */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
     * Row_ID
     */
    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
    }

    /**
     * YearFlag
     */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    /**
     * RowGuid
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * OperateUserGuid
     */
    public String getOperateuserguid() {
        return super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
    }

    /**
     * OperateUserOUGuid
     */
    public String getOperateuserouguid() {
        return super.get("operateuserouguid");
    }

    public void setOperateuserouguid(String operateuserouguid) {
        super.set("operateuserouguid", operateuserouguid);
    }

    /**
     * OperateUserBaseOUGuid
     */
    public String getOperateuserbaseouguid() {
        return super.get("operateuserbaseouguid");
    }

    public void setOperateuserbaseouguid(String operateuserbaseouguid) {
        super.set("operateuserbaseouguid", operateuserbaseouguid);
    }

    /**
     * PVIGuid
     */
    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

    /**
     * acceptDate
     */
    public String getAcceptdate() {
        return super.get("acceptdate");
    }

    public void setAcceptdate(String acceptdate) {
        super.set("acceptdate", acceptdate);
    }

    /**
     * Evacode
     */
    public String getEvacode() {
        return super.get("evacode");
    }

    public void setEvacode(String evacode) {
        super.set("evacode", evacode);
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
     * Taskname
     */
    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
    }

    /**
     * ProjectNO
     */
    public String getProjectno() {
        return super.get("projectno");
    }

    public void setProjectno(String projectno) {
        super.set("projectno", projectno);
    }

    /**
     * Createdate
     */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }

    /**
     * Evaversion
     */
    public Integer getEvaversion() {
        return super.getInt("evaversion");
    }

    public void setEvaversion(Integer evaversion) {
        super.set("evaversion", evaversion);
    }

    /**
     * areaCode
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
     * deptCode
     */
    public String getDeptcode() {
        return super.get("deptcode");
    }

    public void setDeptcode(String deptcode) {
        super.set("deptcode", deptcode);
    }

    /**
     * taskCode
     */
    public String getTaskcode() {
        return super.get("taskcode");
    }

    public void setTaskcode(String taskcode) {
        super.set("taskcode", taskcode);
    }

    /**
     * subMatter
     */
    public String getSubmatter() {
        return super.get("submatter");
    }

    public void setSubmatter(String submatter) {
        super.set("submatter", submatter);
    }

    /**
     * applicant
     */
    public String getApplicant() {
        return super.get("applicant");
    }

    public void setApplicant(String applicant) {
        super.set("applicant", applicant);
    }

    /**
     * proStatus
     */
    public String getProstatus() {
        return super.get("prostatus");
    }

    public void setProstatus(String prostatus) {
        super.set("prostatus", prostatus);
    }

    /**
     * proDepart
     */
    public String getProdepart() {
        return super.get("prodepart");
    }

    public void setProdepart(String prodepart) {
        super.set("prodepart", prodepart);
    }

    /**
     * mobile
     */
    public String getMobile() {
        return super.get("mobile");
    }

    public void setMobile(String mobile) {
        super.set("mobile", mobile);
    }

    /**
     * certKeyGOV
     */
    public String getCertkeygov() {
        return super.get("certkeygov");
    }

    public void setCertkeygov(String certkeygov) {
        super.set("certkeygov", certkeygov);
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
     * promisetime
     */
    public Integer getPromisetime() {
        return super.getInt("promisetime");
    }

    public void setPromisetime(Integer promisetime) {
        super.set("promisetime", promisetime);
    }

    /**
     * dataSource
     */
    public String getDatasource() {
        return super.get("datasource");
    }

    public void setDatasource(String datasource) {
        super.set("datasource", datasource);
    }

    /**
     * assessNumber
     */
    public Integer getAssessnumber() {
        return super.getInt("assessnumber");
    }

    public void setAssessnumber(Integer assessnumber) {
        super.set("assessnumber", assessnumber);
    }

    /**
     * satisfaction
     */
    public String getSatisfaction() {
        return super.get("satisfaction");
    }

    public void setSatisfaction(String satisfaction) {
        super.set("satisfaction", satisfaction);
    }

    /**
     * handleUserPageCode
     */
    public String getHandleuserpagecode() {
        return super.get("handleuserpagecode");
    }

    public void setHandleuserpagecode(String handleuserpagecode) {
        super.set("handleuserpagecode", handleuserpagecode);
    }

    /**
     * handleUserPageType
     */
    public String getHandleuserpagetype() {
        return super.get("handleuserpagetype");
    }

    public void setHandleuserpagetype(String handleuserpagetype) {
        super.set("handleuserpagetype", handleuserpagetype);
    }

    /**
     * handleUserName
     */
    public String getHandleusername() {
        return super.get("handleusername");
    }

    public void setHandleusername(String handleusername) {
        super.set("handleusername", handleusername);
    }

    /**
     * certKey
     */
    public String getCertkey() {
        return super.get("certkey");
    }

    public void setCertkey(String certkey) {
        super.set("certkey", certkey);
    }

    /**
     * userPageType
     */
    public String getUserpagetype() {
        return super.get("userpagetype");
    }

    public void setUserpagetype(String userpagetype) {
        super.set("userpagetype", userpagetype);
    }

    /**
     * userName
     */
    public String getUsername() {
        return super.get("username");
    }

    public void setUsername(String username) {
        super.set("username", username);
    }

    /**
     * userProp
     */
    public String getUserprop() {
        return super.get("userprop");
    }

    public void setUserprop(String userprop) {
        super.set("userprop", userprop);
    }

    /**
     * taskHandleItem
     */
    public String getTaskhandleitem() {
        return super.get("taskhandleitem");
    }

    public void setTaskhandleitem(String taskhandleitem) {
        super.set("taskhandleitem", taskhandleitem);
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
     * assessTime
     */
    public String getAssesstime() {
        return super.get("assesstime");
    }

    public void setAssesstime(String assesstime) {
        super.set("assesstime", assesstime);
    }

    /**
     * isprotion
     */
    public String getIsprotion() {
        return super.get("isprotion");
    }

    public void setIsprotion(String isprotion) {
        super.set("isprotion", isprotion);
    }

    /**
     * evalDetail
     */
    public String getEvaldetail() {
        return super.get("evaldetail");
    }

    public void setEvaldetail(String evaldetail) {
        super.set("evaldetail", evaldetail);
    }

    /**
     * writingEvaluation
     */
    public String getWritingevaluation() {
        return super.get("writingevaluation");
    }

    public void setWritingevaluation(String writingevaluation) {
        super.set("writingevaluation", writingevaluation);
    }

    /**
     * flag
     */
    public String getFlag() {
        return super.get("flag");
    }

    public void setFlag(String flag) {
        super.set("flag", flag);
    }

    /**
     * appStatus
     */
    public Integer getAppstatus() {
        return super.getInt("appstatus");
    }

    public void setAppstatus(Integer appstatus) {
        super.set("appstatus", appstatus);
    }

    /**
     * answeroutput
     */
    public String getAnsweroutput() {
        return super.get("answeroutput");
    }

    public void setAnsweroutput(String answeroutput) {
        super.set("answeroutput", answeroutput);
    }

    /**
     * issh
     */
    public String getIssh() {
        return super.get("issh");
    }

    public void setIssh(String issh) {
        super.set("issh", issh);
    }

    /**
     * shjg
     */
    public Integer getShjg() {
        return super.getInt("shjg");
    }

    public void setShjg(Integer shjg) {
        super.set("shjg", shjg);
    }

    /**
     * sourcetype
     */
    public String getSourcetype() {
        return super.get("sourcetype");
    }

    public void setSourcetype(String sourcetype) {
        super.set("sourcetype", sourcetype);
    }

    /**
     * sbsign
     */
    public String getSbsign() {
        return super.get("sbsign");
    }

    public void setSbsign(String sbsign) {
        super.set("sbsign", sbsign);
    }

    /**
     * sberrordesc
     */
    public String getSberrordesc() {
        return super.get("sberrordesc");
    }

    public void setSberrordesc(String sberrordesc) {
        super.set("sberrordesc", sberrordesc);
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
     * answer
     */
    public String getAnswer() {
        return super.get("answer");
    }

    public void setAnswer(String answer) {
        super.set("answer", answer);
    }

    /**
     * answerdate
     */
    public Date getAnswerdate() {
        return super.getDate("answerdate");
    }

    public void setAnswerdate(Date answerdate) {
        super.set("answerdate", answerdate);
    }

    /**
     * shenhedate
     */
    public Date getShenhedate() {
        return super.getDate("shenhedate");
    }

    public void setShenhedate(Date shenhedate) {
        super.set("shenhedate", shenhedate);
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
     * answerStatus
     */
    public String getAnswerstatus() {
        return super.get("answerstatus");
    }

    public void setAnswerstatus(String answerstatus) {
        super.set("answerstatus", answerstatus);
    }

    /**
     * answerday
     */
    public Integer getAnswerday() {
        return super.getInt("answerday");
    }

    public void setAnswerday(Integer answerday) {
        super.set("answerday", answerday);
    }

    /**
     * sendljcq
     */
    public String getSendljcq() {
        return super.get("sendljcq");
    }

    public void setSendljcq(String sendljcq) {
        super.set("sendljcq", sendljcq);
    }

    /**
     * sendcq
     */
    public String getSendcq() {
        return super.get("sendcq");
    }

    public void setSendcq(String sendcq) {
        super.set("sendcq", sendcq);
    }

    /**
     * ROWID
     */
    public Integer getRowid() {
        return super.getInt("rowid");
    }

    public void setRowid(Integer rowid) {
        super.set("rowid", rowid);
    }

    /**
     * effectivEvalua
     */
    public String getEffectivevalua() {
        return super.get("effectivevalua");
    }

    public void setEffectivevalua(String effectivevalua) {
        super.set("effectivevalua", effectivevalua);
    }

    /**
     * answerusername
     */
    public String getAnswerusername() {
        return super.get("answerusername");
    }

    public void setAnswerusername(String answerusername) {
        super.set("answerusername", answerusername);
    }

    /**
     * answeruserguid
     */
    public String getAnsweruserguid() {
        return super.get("answeruserguid");
    }

    public void setAnsweruserguid(String answeruserguid) {
        super.set("answeruserguid", answeruserguid);
    }

    /**
     * shenheusername
     */
    public String getShenheusername() {
        return super.get("shenheusername");
    }

    public void setShenheusername(String shenheusername) {
        super.set("shenheusername", shenheusername);
    }

    /**
     * shenheuserguid
     */
    public String getShenheuserguid() {
        return super.get("shenheuserguid");
    }

    public void setShenheuserguid(String shenheuserguid) {
        super.set("shenheuserguid", shenheuserguid);
    }

    /**
     * sfeypj
     */
    public String getSfeypj() {
        return super.get("sfeypj");
    }

    public void setSfeypj(String sfeypj) {
        super.set("sfeypj", sfeypj);
    }

    /**
     * sfhf
     */
    public String getSfhf() {
        return super.get("sfhf");
    }

    public void setSfhf(String sfhf) {
        super.set("sfhf", sfhf);
    }

    /**
     * sfhfang
     */
    public String getSfhfang() {
        return super.get("sfhfang");
    }

    public void setSfhfang(String sfhfang) {
        super.set("sfhfang", sfhfang);
    }

    /**
     * khyj
     */
    public String getKhyj() {
        return super.get("khyj");
    }

    public void setKhyj(String khyj) {
        super.set("khyj", khyj);
    }

    /**
     * khouname
     */
    public String getKhouname() {
        return super.get("khouname");
    }

    public void setKhouname(String khouname) {
        super.set("khouname", khouname);
    }

    /**
     * ldps
     */
    public String getLdps() {
        return super.get("ldps");
    }

    public void setLdps(String ldps) {
        super.set("ldps", ldps);
    }

    /**
     * ldmc
     */
    public String getLdmc() {
        return super.get("ldmc");
    }

    public void setLdmc(String ldmc) {
        super.set("ldmc", ldmc);
    }

    /**
     * taskType
     */
    public String getTasktype() {
        return super.get("tasktype");
    }

    public void setTasktype(String tasktype) {
        super.set("tasktype", tasktype);
    }

}
