package com.epoint.auditsp.auditsphandle.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.ibatis.SqlMapClientImplWrapper;
import com.epoint.basic.auditresource.auditdggtview.domain.AuditDggtView;
import com.epoint.basic.auditresource.auditdggtview.inter.IAuditDggtView;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 阶段选择页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@RestController("jnaudititemchooseaction")
@Scope("request")
public class JNAuditItemChooseAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7487597167764665058L;

	private  DataGridModel<AuditRsItemBaseinfo>  model = null;
	
	@Autowired
	private  IAuditRsItemBaseinfo iauditrsitembaseinfo;
	@Autowired
	private  IAuditDggtView iauditdggtview;
	
	private String itemname;
	
	private String itemcode;

    @Override
	public void pageLoad() {
		
	}

	public   DataGridModel<AuditRsItemBaseinfo>   getDataGridData(){
	    if(model == null){
	        model = new DataGridModel<AuditRsItemBaseinfo>(){
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditRsItemBaseinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 去除所有草稿项目
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    if(StringUtil.isNotBlank(itemcode)){                        
                        sqlc.like("itemcode",itemcode );
                    }
                    if(StringUtil.isNotBlank(itemname)){
                        sqlc.like("itemname", itemname);
                    }
                    sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                    //只查询主项
                    sqlc.isBlankOrValue("parentid", ZwfwConstant.CONSTANT_STR_ZERO);
                    //为未申报的过的项目
                    //sqlc.isBlank("biguid");
                    
                    sqlc.setOrderAsc("operatedate");
                    PageData<AuditRsItemBaseinfo> pagadata = iauditrsitembaseinfo.getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, sqlc.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pagadata.getRowCount());
                    return pagadata.getList();
                }
	            
	        };
	    }
	    
	    return model;
	}
	
	
	public String getItemname() {
	    return itemname;
	}

	public void setItemname(String itemname) {
	   this.itemname = itemname;
	}

	public String getItemcode() {
	   return itemcode;
	}

	public void setItemcode(String itemcode) {
	    this.itemcode = itemcode;
	}
}
