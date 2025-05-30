package com.epoint.hcp.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 评价实例表实体
 * 
 * @author lc
 * @version [版本号, 2019-09-06 14:54:18]
 */
@Entity(table = "EvaInstance", id = "rowguid")
public class Evainstance extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 申请时间
     */
    public String getAcceptdate() {
        return super.get("acceptdate");
    }

    public void setAcceptdate(String acceptdate) {
        super.set("acceptdate", acceptdate);
    }

    /**
     * 身份证
     */
    public String getCertkeygov() {
        return super.get("certkeygov");
    }

    public void setCertkeygov(String certkeygov) {
        super.set("certkeygov", certkeygov);
    }
    
    /**
     * certKey  申请人证件号码
     */
    public String getCertkey() {
        return super.get("certkey");
    }

    public void setCertkey(String certkey) {
        super.set("certkey", certkey);
    }
    
    /**
     * userPageType  申请人证件类型
     */
    public String getUserpagetype() {
        return super.get("userpagetype");
    }

    public void setUserpagetype(String userpagetype) {
        super.set("userpagetype", userpagetype);
    }
    
    /**
     * handleUserPageType  申请方经办人证件类型
     */
    public String getHandleuserpagetype() {
        return super.get("handleuserpagetype");
    }

    public void setHandleuserpagetype(String handleuserpagetype) {
        super.set("handleuserpagetype", handleuserpagetype);
    }
    
    /**
     * handleUserPageCode  申请方经办人证件号码
     */
    public String getHandleuserpagecode() {
        return super.get("handleuserpagecode");
    }

    public void setHandleuserpagecode(String handleuserpagecode) {
        super.set("handleuserpagecode", handleuserpagecode);
    }
    
    /**
     * appStatus 审核状态
     */
    public Integer getAppstatus() {
        return super.getInt("appstatus");
    }

    public void setAppstatus(Integer appstatus) {
        super.set("appstatus", appstatus);
    }
    
    /**
     * pf 评价渠道（pc端=1，移动端=2，二维码=3，政务大厅平板电脑=4，政务大厅其他终端=5）
     */
    public String getPf() {
        return super.get("pf");
    }

    public void setPf(String pf) {
        super.set("pf", pf);
    }
    
    /**
     * promisetime 即办件=1，承诺办件=2 
     */
    public Integer getPromisetime() {
        return super.getInt("promisetime");
    }

    public void setPromisetime(Integer promisetime) {
        super.set("promisetime", promisetime);
    }
    
    /**
     * dataSource 数据来源
     */
    public String getDatasource() {
        return super.get("datasource");
    }

    public void setDatasource(String datasource) {
        super.set("datasource", datasource);
    }
    
    /**
     * assessNumber 评价次数
     */
    public Integer getAssessnumber() {
        return super.getInt("assessnumber");
    }

    public void setAssessnumber(Integer assessnumber) {
        super.set("assessnumber", assessnumber);
    }
    
    /**
     * satisfaction 整体满意度
     */
    public String getSatisfaction() {
        return super.get("satisfaction");
    }

    public void setSatisfaction(String satisfaction) {
        super.set("satisfaction", satisfaction);
    }
    
    /**
     * handleUserName  申请方经办人姓名
     */
    public String getHandleusername() {
        return super.get("handleusername");
    }

    public void setHandleusername(String handleusername) {
        super.set("handleusername", handleusername);
    }

    /**
     * 申请人手机号
     */
    public String getMobile() {
        return super.get("mobile");
    }

    public void setMobile(String mobile) {
        super.set("mobile", mobile);
    }

    /**
     * 受理部门
     */
    public String getProdepart() {
        return super.get("prodepart");
    }

    public void setProdepart(String prodepart) {
        super.set("prodepart", prodepart);
    }
    
    /**
     * assessTime  评价时间
     */
     public String getAssesstime() {
         return super.get("assesstime");
     }

     public void setAssesstime(String assesstime) {
         super.set("assesstime", assesstime);
     }
     
     /**
      * userProp  申请人类型
      */
     public String getUserprop() {
         return super.get("userprop");
     }

     public void setUserprop(String userprop) {
         super.set("userprop", userprop);
     }

    /**
     * 办件状态
     */
    public String getProstatus() {
        return super.get("prostatus");
    }

    public void setProstatus(String prostatus) {
        super.set("prostatus", prostatus);
    }

    /**
     * 申请人
     */
    public String getApplicant() {
        return super.get("applicant");
    }

    public void setApplicant(String applicant) {
        super.set("applicant", applicant);
    }

    /**
     * 事项主题
     */
    public String getSubmatter() {
        return super.get("submatter");
    }

    public void setSubmatter(String submatter) {
        super.set("submatter", submatter);
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
     * 事项编码
     */
    public String getTaskcode() {
        return super.get("taskcode");
    }
    public void setTaskcode(String taskcode) {
        super.set("taskcode", taskcode);
    }
    
    
    /**
     *isprotion   默认评价
     */
    public String getIsprotion() {
        return super.get("isprotion");
    }

    public void setIsprotion(String isprotion) {
        super.set("isprotion", isprotion);
    }
    /**
     * flag   操作类型
     */
    public String getFlag() {
        return super.get("flag");
    }

    public void setFlag(String flag) {
        super.set("flag", flag);
    }
    
    /**
     * writingEvaluation   文字评价
     */
    public String getWritingevaluation() {
        return super.get("writingevaluation");
    }

    public void setWritingevaluation(String writingevaluation) {
        super.set("writingevaluation", writingevaluation);
    }
    
    /**
     * evalDetail   评价详情
     */
    public String getEvaldetail() {
        return super.get("evaldetail");
    }

    public void setEvaldetail(String evaldetail) {
        super.set("evaldetail", evaldetail);
    }

   
    
    /**
     * taskHandleItem   业务办理项编码
     */
    public String getTaskhandleitem() {
        return super.get("taskHandleItem");
    }

    public void setTaskhandleitem(String taskHandleItem) {
        super.set("taskHandleItem", taskHandleItem);
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
     * 部门编码
     */
    public String getDeptcode() {
        return super.get("deptcode");
    }

    public void setDeptcode(String deptcode) {
        super.set("deptcode", deptcode);
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
     * 辖区编码
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
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
     * 对应评价版本
     */
    public Integer getEvaversion() {
        return super.getInt("evaversion");
    }

    public void setEvaversion(Integer evaversion) {
        super.set("evaversion", evaversion);
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
     * 评价时间
     */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }
    
    /**
     * userName  申请人名称
     */
    public String getUsername() {
        return super.get("username");
    }

    public void setUsername(String username) {
        super.set("username", username);
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
     * 办件编号
     */
    public String getProjectno() {
        return super.get("projectno");
    }

    public void setProjectno(String projectno) {
        super.set("projectno", projectno);
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
     * 事项名称
     */
    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
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
     * 满意度等级
     */
    public String getEvalevel() {
        return super.get("evalevel");
    }

    public void setEvalevel(String evalevel) {
        super.set("evalevel", evalevel);
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
     * 评价内容编码
     */
    public String getEvacode() {
        return super.get("evacode");
    }

    public void setEvacode(String evacode) {
        super.set("evacode", evacode);
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
     * 评价内容
     */
    public String getEvacontant() {
        return super.get("evacontant");
    }

    public void setEvacontant(String evacontant) {
        super.set("evacontant", evacontant);
    }
    
    /**
     * 整改反馈
     */
    public String getRectification() {
        return super.get("rectification");
    }

    public void setRectification(String rectification) {
        super.set("rectification", rectification);
    }
    
    /**
     * 是否为有效评价数据
     */
    public String getEffectivEvalua() {
        return super.get("effectivEvalua");
    }

    public void setEffectivEvalua(String effectivEvalua) {
        super.set("effectivEvalua", effectivEvalua);
    }
    /**
     * 答复
     */
    public String getAnswer() {
        return super.get("answer");
    }

    public void setAnswer(String answer) {
        super.set("answer", answer);
    }
}
