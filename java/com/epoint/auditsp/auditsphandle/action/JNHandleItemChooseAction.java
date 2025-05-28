package com.epoint.auditsp.auditsphandle.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 阶段选择页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@RestController("jnhandleitemchooseaction")
@Scope("request")
public class JNHandleItemChooseAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7487597167764665058L;

	private  DataGridModel<AuditRsItemBaseinfo>  model = null;
	
	@Autowired
	private  IAuditRsItemBaseinfo iauditrsitembaseinfo;
	@Autowired
	private  IAuditSpISubapp iauditspisubapp;
	
	@Override
	public void pageLoad() {
		
	}

	public   DataGridModel<AuditRsItemBaseinfo>   getDataGridData(){
	    if(model == null){
	        model = new DataGridModel<AuditRsItemBaseinfo>(){
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditRsItemBaseinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    //去除所有草稿项目
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("biguid", getRequestParameter("biguid"));
                    sqlc.nq("status", "-1");
                    List<AuditSpISubapp> lists = iauditspisubapp.getSubappListByMap(sqlc.getMap()).getResult();
                    //system.out.println(lists);
                    List<String> ywguids = new ArrayList<>();
                    for (AuditSpISubapp auditSpISubapp : lists) {
                        if(!ywguids.contains(auditSpISubapp.getYewuguid())){
                            ywguids.add(auditSpISubapp.getYewuguid());
                        }
                    }
                    sqlc.clear();
                    sqlc.eq("biguid", getRequestParameter("biguid"));
                    //system.out.println(ywguids);
                    if(ywguids != null && ywguids.size() > 0 ) {
                        sqlc.in("rowguid", "'"+StringUtil.join(ywguids, "','")+"'");
                    }
                    sqlc.setOrderAsc("operatedate");
                    PageData<AuditRsItemBaseinfo> pagadata = iauditrsitembaseinfo.getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, sqlc.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pagadata.getRowCount());
                    return pagadata.getList();
                }
	            
	        };
	    }
	    
	    return model;
	}
	
	
}
