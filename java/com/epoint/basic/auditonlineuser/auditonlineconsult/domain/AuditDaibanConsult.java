package com.epoint.basic.auditonlineuser.auditonlineconsult.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 咨询投诉实体
 * 
 * @author yangjl
 * @version [版本号, 2017-04-11 16:28:20]
 */
@Entity(table = "AUDIT_DAIBAN_CONSULT", id = "rowguid")
public class AuditDaibanConsult extends BaseEntity implements Cloneable {
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
	 * 所属辖区号
	 */
	public String getAreaCode() {
		return super.get("areacode");
	}

	public void setAreaCode(String areacode) {
		super.set("areacode", areacode);
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
	 * 是否匿名  1 匿名 0不匿名
	 */
	public String getIsAnonymous() {
		return super.get("IsAnonymous");
	}

	public void setIsAnonymous(String IsAnonymous) {
		super.set("IsAnonymous", IsAnonymous);
	}
	
	/**
	 *   提问人手机
	 */
	public String getAskerMobile() {
		return super.get("ASKERMOBILE");
	}

	public void setAskerMobile(String ASKERMOBILE) {
		super.set("ASKERMOBILE", ASKERMOBILE);
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
	 * 来源
	 */
	public String getSource() {
		return super.get("source");
	}

	public void setSource(String source) {
		super.set("source", source);
	}

	/**
	 * 咨询与意见类型
	 */
	public String getConsulttype() {
		return super.get("consulttype");
	}

	public void setConsulttype(String consulttype) {
		super.set("consulttype", consulttype);
	}

	/**
	 * 事项标识
	 */
	public String getTaskguid() {
		return super.get("taskguid");
	}

	public void setTaskguid(String taskguid) {
		super.set("taskguid", taskguid);
	}

	/**
	 * 项目标识
	 */
	public String getProjectguid() {
		return super.get("projectguid");
	}

	public void setProjectguid(String projectguid) {
		super.set("projectguid", projectguid);
	}

	/**
	 * 访问用户名
	 */
	public String getAskerusername() {
		return super.get("askerusername");
	}

	public void setAskerusername(String askerusername) {
		super.set("askerusername", askerusername);
	}

	/**
	 * 访问用户标识
	 */
	public String getAskeruserguid() {
		return super.get("askeruserguid");
	}

	public void setAskeruserguid(String askeruserguid) {
		super.set("askeruserguid", askeruserguid);
	}

	/**
	 * 访问登录ID
	 */
	public String getAskerloginid() {
		return super.get("askerloginid");
	}

	public void setAskerloginid(String askerloginid) {
		super.set("askerloginid", askerloginid);
	}

	/**
	 * 提出日期
	 */
	public Date getAskdate() {
		return super.getDate("askdate");
	}

	public void setAskdate(Date askdate) {
		super.set("askdate", askdate);
	}

	/**
	 * 咨询建议内容
	 */
	public String getQuestion() {
		return super.get("question");
	}

	public void setQuestion(String question) {
		super.set("question", question);
	}

	/**
	 * 处理部门标识
	 */
	public String getOuguid() {
		return super.get("ouguid");
	}

	public void setOuguid(String ouguid) {
		super.set("ouguid", ouguid);
	}

	/**
	 * 是否对提出人答复
	 */
	public String getStatus() {
		return super.get("status");
	}

	public void setStatus(String status) {
		super.set("status", status);
	}

	/**
	 * 答复内容
	 */
	public String getAnswer() {
		return super.get("answer");
	}

	public void setAnswer(String answer) {
		super.set("answer", answer);
	}

	/**
	 * 答复人用户标识
	 */
	public String getAnsweruserguid() {
		return super.get("answeruserguid");
	}

	public void setAnsweruserguid(String answeruserguid) {
		super.set("answeruserguid", answeruserguid);
	}

	/**
	 * 答复人姓名
	 */
	public String getAnswerusername() {
		return super.get("answerusername");
	}

	public void setAnswerusername(String answerusername) {
		super.set("answerusername", answerusername);
	}

	/**
	 * 答复日期
	 */
	public Date getAnswerdate() {
		return super.getDate("answerdate");
	}

	public void setAnswerdate(Date answerdate) {
		super.set("answerdate", answerdate);
	}

	/**
	 * 内部处理方式
	 */
	public String getHandletype() {
		return super.get("handletype");
	}

	public void setHandletype(String handletype) {
		super.set("handletype", handletype);
	}

	/**
	 * 备注
	 */
	public String getNote() {
		return super.get("note");
	}

	public void setNote(String note) {
		super.set("note", note);
	}

	/**
	 * 转交情况记录
	 */
	public String getZhuanjiaolog() {
		return super.get("zhuanjiaolog");
	}

	public void setZhuanjiaolog(String zhuanjiaolog) {
		super.set("zhuanjiaolog", zhuanjiaolog);
	}

	/**
	 * 点击数
	 */
	public String getClickcount() {
		return super.get("clickcount");
	}

	public void setClickcount(String clickcount) {
		super.set("clickcount", clickcount);
	}

	/**
	 * 是否发布到网站
	 */
	public String getPublishonweb() {
		return super.get("publishonweb");
	}

	public void setPublishonweb(String publishonweb) {
		super.set("publishonweb", publishonweb);
	}

	/**
	 * 内部处理时间
	 */
	public Date getHandledate() {
		return super.getDate("handledate");
	}

	public void setHandledate(Date handledate) {
		super.set("handledate", handledate);
	}

	/**
	 * 关联附件标识
	 */
	public String getClientguid() {
		return super.get("clientguid");
	}

	public void setClientguid(String clientguid) {
		super.set("clientguid", clientguid);
	}

	/**
	 * 标题
	 */
	public String getTitle() {
		return super.get("title");
	}

	public void setTitle(String title) {
		super.set("title", title);
	}

    /**
     * 中心guid
     */
    public String getCenterguid() {
        return super.get("centerguid");
    }

    public void setCenterguid(String centerguid) {
        super.set("centerguid", centerguid);
    }
    
    /**
     * 区域编码
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }
    
    
    /**
     * 第一次答复时附件guid
     */
    public String getClientapplyguid() {
        return super.get("clientapplyguid");
    }

    public void setClientapplyguid(String clientapplyguid) {
        super.set("clientapplyguid", clientapplyguid);
    }
    
    /**
     * 主题标识
     */
    public String getBusinessguid() {
        return super.get("businessguid");
    }

    public void setBusinessguid(String businessguid) {
        super.set("businessguid", businessguid);
    }
    
    /**
     * 是否阅读过
     */
    public String getReadstatus() {
        return super.get("readstatus");
    }

    public void setReadstatus(String readstatus) {
        super.set("readstatus", readstatus);
    }

    /**
     * 第一次回答部门标识
     */
    public String getAnswerouguid() {
        return super.get("answerouguid");
    }

    public void setAnswerouguid(String answerouguid) {
        super.set("answerouguid", answerouguid);
    }
    
    /**
     * 并联审批项目guid
     */
    public String getItemguid() {
        return super.get("itemguid");
    }

    public void setItemguid(String itemguid) {
        super.set("itemguid", itemguid);
    }
    
    /**
     * 并联审批项目咨询内容代码项
     */
    public String getConsultcode() {
        return super.get("consultcode");
    }

    public void setConsultcode(String consultcode) {
        super.set("consultcode", consultcode);
    }
    
    /**
     * 并联审批项目咨询企业
     */
    public String getCompanyid() {
        return super.get("companyid");
    }

    public void setCompanyid(String companyid) {
        super.set("companyid", companyid);
    }
}
