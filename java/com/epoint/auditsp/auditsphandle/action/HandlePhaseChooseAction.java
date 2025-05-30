package com.epoint.auditsp.auditsphandle.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;

/**
 * 阶段选择页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@RestController("handlephasechooseaction")
@Scope("request")
public class HandlePhaseChooseAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7487597167764665058L;

	@Autowired
	private IAuditSpPhase auditSpPhaseService;

	@Autowired
	private IAuditSpISubapp auditSpISubappService;
	
	@Autowired
    private IAuditSpInstance iauditspinstance;
	

	@Override
	public void pageLoad() {
		String businessGuid = getRequestParameter("businessGuid");
		String biguid = getRequestParameter("biguid");
		JSONArray jsonArray = new JSONArray();
		List<AuditSpPhase> auditSpPhases = auditSpPhaseService.getSpPaseByBusinedssguid(businessGuid).getResult();
		List<AuditSpISubapp> auditspilist = auditSpISubappService.getSubappByBIGuid(biguid).getResult();// 获取主题下的所有子申报
		//获取项目申报的guid
		AuditSpInstance spi = iauditspinstance.getDetailByBIGuid(biguid).getResult();
		if(spi!=null){
		    addCallbackParam("itemguid", spi.getYewuguid());
		}
		if (auditSpPhases != null) {
			// 按ordernum从大到小排序
			auditSpPhases.sort((a, b) -> (b.getOrdernumber() != null ? b.getOrdernumber() : Integer.valueOf(0))
					.compareTo(a.getOrdernumber() != null ? a.getOrdernumber() : Integer.valueOf(0)));
			for (AuditSpPhase auditSpPhase : auditSpPhases) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("phaseGuid", auditSpPhase.getRowguid());
				jsonObject.put("phaseName", auditSpPhase.getPhasename());
				//如果spi 为null 则未申报过项目，默认状态
				jsonObject.put("statuscls", "wsb");
				jsonObject.put("statustext", "未申报");
				jsonObject.put("btncls", "nocf");
				if(spi!=null){
	                    //记录是否办结
	                    boolean isbj = true;
	                    boolean issb = false;
	                    boolean iscf = false;
	                    for (AuditSpISubapp auditSpISubapp : auditspilist) {
	                        if (auditSpPhase.getRowguid().equals(auditSpISubapp.getPhaseguid())) {
	                            if(!"-1".equals(auditSpISubapp.getStatus())){
	                                issb = true;
	                                //存在未办结
	                                if(!ZwfwConstant.LHSP_Status_YBJ.equals(auditSpISubapp.getStatus())){
	                                    isbj = false;
	                                }
	                                
	                                if(!auditSpISubapp.getYewuguid().equals(spi.getYewuguid())){
	                                    iscf = true;
	                                }
                                }
	                        }
	                    }
	                    //如果未申报
	                    if(!issb){
	                        jsonObject.put("btncls", "nocf");
	                    }else{
	                        if(isbj){
	                            jsonObject.put("statuscls", "bj");
	                            jsonObject.put("statustext", "已办结");
	                        }else{
	                            jsonObject.put("statuscls", "spz");
	                            jsonObject.put("statustext", "审批中");	                            
	                        }
	                        if(iscf){
	                            jsonObject.put("btncls", "nocf");
	                        }else{
	                            jsonObject.put("btncls", "nobtn");
	                        }
	                    }
	                    
	                    
				}
				
				
				if(!"5".equals(auditSpPhase.getPhaseId())){				    
				    jsonArray.add(jsonObject);
				}
			}
		}
		addCallbackParam("phase", jsonArray);
	}

}
