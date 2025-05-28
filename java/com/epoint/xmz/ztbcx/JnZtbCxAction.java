package com.epoint.xmz.ztbcx;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.mq.spgl.api.IJnOptionInService;
import com.epoint.mq.spgl.api.entity.JnOptionIn;
import com.epoint.util.TARequestUtil;


/**
 * 成品油零售经营企业库list页面对应的后台
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@RestController("jnztbcxaction")
@Scope("request")
public class JnZtbCxAction  extends BaseController
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String zwfwurl= ConfigUtil.getConfigValue("epointframe", "zwfwurl");

    /**
     * 成品油零售经营企业库实体对象
     */
    private Record dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;
    
    private String tendernum = null;
    
    private String tenderprojectcode = null;
    
    private String bidsectioncode = null;
    
    private String tendercorpname = null;
    
    private String tendercorpcode = null;
  	
    

    public void pageLoad()
    {
    }

    
    @SuppressWarnings("serial")
	public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	 JSONObject submit = new JSONObject();
                	 
                    if (StringUtil.isNotBlank(tendernum)) {
                    	submit.put("TenderNum", tendernum);
                    }
                    if (StringUtil.isNotBlank(tenderprojectcode)) {
                    	submit.put("TenderProjectCode", tenderprojectcode);
                    }
                    if (StringUtil.isNotBlank(bidsectioncode)) {
                    	submit.put("BidSectionCode", bidsectioncode);
                    }
                    if (StringUtil.isNotBlank(tendercorpname)) {
                    	submit.put("TenderCorpName", tendercorpname);
                    }
                    if (StringUtil.isNotBlank(tendercorpcode)) {
                    	submit.put("TenderCorpCode",tendercorpcode);
                    }
                    
                    List<Record> list = new ArrayList<Record>();
                    int count = 0;
                    
                    if(StringUtil.isNotBlank(submit.getString("TenderNum")) || StringUtil.isNotBlank(submit.getString("TenderProjectCode")) 
                    		|| StringUtil.isNotBlank(submit.getString("BidSectionCode")) || StringUtil.isNotBlank(submit.getString("TenderCorpName"))
                    		|| StringUtil.isNotBlank(submit.getString("TenderCorpCode"))) {
                    	
                         
                     	  String resultsign = TARequestUtil.sendPostInner(zwfwurl + "rest/jnzblist/getZbDetail", submit.toJSONString(), "", "");
                          
                          JSONObject jsonresult = JSON.parseObject(resultsign);
                          
                          if (jsonresult.getJSONArray("custom").size() > 0) {
                          	JSONArray customs = jsonresult.getJSONArray("custom");
                          	for (int i=0;i<customs.size();i++) {
                          		Record record = new Record();
                          		record.set("filename", customs.getJSONObject(i).getString("fileName"));
                          		record.set("url", customs.getJSONObject(i).getString("url"));
                          		list.add(record);
                          	}
                          	
                              count = 0;
                               
                          }
                    }
                   
                    this.setRowCount(count);
                  
                    return list;
                }

            };
        }
        return model;
    }
    
    
    
    public Record getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new Record();
    	}
        return dataBean;
    }

    public void setDataBean(Record dataBean)
    {
        this.dataBean = dataBean;
    }


	public String getTendernum() {
		return tendernum;
	}


	public void setTendernum(String tendernum) {
		this.tendernum = tendernum;
	}


	public String getTenderprojectcode() {
		return tenderprojectcode;
	}


	public void setTenderprojectcode(String tenderprojectcode) {
		this.tenderprojectcode = tenderprojectcode;
	}


	public String getBidsectioncode() {
		return bidsectioncode;
	}


	public void setBidsectioncode(String bidsectioncode) {
		this.bidsectioncode = bidsectioncode;
	}


	public String getTendercorpname() {
		return tendercorpname;
	}


	public void setTendercorpname(String tendercorpname) {
		this.tendercorpname = tendercorpname;
	}


	public String getTendercorpcode() {
		return tendercorpcode;
	}


	public void setTendercorpcode(String tendercorpcode) {
		this.tendercorpcode = tendercorpcode;
	}
    



}
