package com.epoint.auditqueue.qhj.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueue.qhj.api.IJNOulistinner;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController("jnoulistinnerqueueaction")
@Scope("request")
public class JNOulistInnerQueueAction extends BaseController {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5744552639400444526L;
	@Autowired
	private IAuditOrgaWindowYjs windowservice;
	@Autowired
	private IOuService ouService9;
	@Autowired
	private IJNOulistinner oulistinner;
	@Autowired
    private IAuditZnsbEquipment equipmentservice;
	private final int size = 16; // 每页数据量
	private List<Record> rtnValue = new ArrayList<>();

	String ShiCenterguid;
	String quCenterguid;
	String hallguid;
    String macAddress;
    String quhallguid;
	
	@Override
	public void pageLoad() {
		ShiCenterguid = "46db0d30-b3ea-4d9c-8a66-771731e4b33a";
	    //ShiCenterguid = "ce6b4976-1f62-4255-9ece-a3a5f85f0874";
	   quCenterguid = "4391ec2f-6903-4a1a-af2d-6ba281bc5884";
	   // quCenterguid = "ce6b4976-1f62-4255-9ece-a3a5f85f0874";
	   hallguid = getRequestParameter("hallguid");
	   macAddress = getRequestParameter("MacAddress");
	   AuditZnsbEquipment  equipment = equipmentservice.getDetailbyMacaddress(macAddress).getResult();
	   quhallguid = equipment.get("quhallguid");
		// 获取数据的分页数
		addCallbackParam("pages", pages(ShiCenterguid, hallguid));
		addCallbackParam("qupages", pages(quCenterguid, quhallguid));
		addCallbackParam("quhallguid", quhallguid);
	}

	public int pages(String Centerguid, String hallguid) {
		String condition ="";
		condition += " and Centerguid='" + Centerguid +"'";
        if (!"all".equals(hallguid)) {
           
            hallguid = hallguid.replaceAll(",", "','");
            condition += " and LOBBYTYPE in ('" + hallguid +"')";
           
        }
        condition += " and IS_USEQUEUE='" + QueueConstant.Common_yes_String +"'";
	
		int num = oulistinner.getOrderdLinkedOucount(condition);
		return num = num % size == 0 ? num / size : num / size + 1;
	}

	public void getDataJson() {
	    String condition ="";
        condition += " and Centerguid='" + ShiCenterguid +"'";
        if (!"all".equals(hallguid)) {
            hallguid = hallguid.replaceAll(",", "','");
            condition += " and LOBBYTYPE in ('" + hallguid +"')";
           
        }
        condition += " and IS_USEQUEUE='" + QueueConstant.Common_yes_String +"'";
    List<FrameOu> oulist = oulistinner.getOrderdLinkedOulist(Integer.valueOf(getRequestParameter("cur")) * size, size, condition);
    for(FrameOu ou :oulist){
        Record record = new Record();
        String a = ou.getOuguid();
        if(!"4e398b61-0dd0-476f-83cd-f653bea67425".equals(a)){
            record.put("ouname",
                    StringUtil.isNotBlank(ou.getOushortName())
                            ?ou.getOushortName()
                            : ou.getOuname());
            record.put("ouguid", ou.getOuguid());
            rtnValue.add(record);  
        }
       
    }
   	addCallbackParam("html", JsonUtil.listToJson(rtnValue));
	}
	public void getQuDataJson() {
	
	     String condition ="";
	        condition += " and Centerguid='" + quCenterguid +"'";
	        if (!"all".equals(quhallguid)) {
	            quhallguid = quhallguid.replaceAll(",", "','");
	            condition += " and LOBBYTYPE in ('" + quhallguid +"')";
	           
	        }
	        condition += " and IS_USEQUEUE='" + QueueConstant.Common_yes_String +"'";
	    List<FrameOu> oulist = oulistinner.getOrderdLinkedOulist(Integer.valueOf(getRequestParameter("qucur")) * size, size, condition);
	    for(FrameOu ou :oulist){
	        Record record = new Record();
	        String a = ou.getOuguid();
	        if(!"37e207d7-bcb2-4a6a-81c7-46026ccbea34".equals(a)){
	            record.put("ouname",
                    StringUtil.isNotBlank(ou.getOushortName())
                    ?ou.getOushortName()
                    : ou.getOuname());
                record.put("ouguid", ou.getOuguid());
                rtnValue.add(record);
    }              
	    }
	    addCallbackParam("html", JsonUtil.listToJson(rtnValue));
    }
}
