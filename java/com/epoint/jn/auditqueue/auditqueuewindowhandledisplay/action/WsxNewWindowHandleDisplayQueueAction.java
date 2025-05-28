package com.epoint.jn.auditqueue.auditqueuewindowhandledisplay.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.jn.auditqueue.auditqueuewindowhandledisplay.inter.IJnWindowhandle;
import com.epoint.zhenggai.api.ZhenggaiService;

@RestController("wsxnewwindowhandledisplayqueueaction")
@Scope("request")
public class WsxNewWindowHandleDisplayQueueAction extends BaseController {

	private static final long serialVersionUID = 1L;
	@Autowired
	private IAuditOrgaWindowYjs windowservice;
	@Autowired
    private IAuditQueue queueservice;
	@Autowired
    private IOuService ouService9;
	@Autowired
    private IAuditQueueOrgaWindow queuewindowservice;
	@Autowired
	private IAuditZnsbEquipment equipmentservice;
	@Autowired
    private IJnWindowhandle Windowhandle;
	@Autowired
    private ZhenggaiService zhenggaiImpl;
	@Override
	public void pageLoad() {
		
	}

	public void changeDiv() {
	    try {
	        List<Map<String, String>> jsonList =returnHtmlJson(getRequestParameter("hallguid"),
	                getRequestParameter("Centerguid"), getRequestParameter("MacAddress"));
	        for (int i = 0; i < jsonList.size(); i++) {
	            Map<String, String> value = jsonList.get(i);
	            Iterator<?> it = value.entrySet().iterator();
	            while (it.hasNext()) {
	                Entry<?, ?> entry = (Entry<?, ?>) it.next();
	                addCallbackParam(entry.getKey().toString(), entry.getValue());
	            }
	        }
	        
	        addCallbackParam("total",jsonList.get(0).size());
        }
        catch (Exception e) {
            //system.out.println(e.getMessage());
        }
		
	}
	public List<Map<String, String>> returnHtmlJson(String hallguid, String Centerguid,String Macaddress ) {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Map<String, String> windowMap = new HashMap<>(16);
        Map<String, String> qnoMap = new HashMap<>(16);
        Map<String, String> waitcountMap = new HashMap<>(16);
    
        AuditZnsbEquipment equipmnet=equipmentservice.getDetailbyMacaddress(Macaddress).getResult();
       List<AuditQueue> auditqueuelist =  Windowhandle.getorderedlinkauditqueue(equipmnet.getRowguid());

        int i = 0;
        for (AuditQueue Record : auditqueuelist) {
            windowMap.put("window" + i, windowservice.getWindowByWindowGuid(Record.getHandlewindowguid()).getResult().getWindowno());
        
           
            qnoMap.put("qno" + i, StringUtil.getNotNullString(Record.getQno()));
            waitcountMap.put("waitcount" + i,
                    StringUtil.getNotNullString(queueservice.getWindowWaitNum(Record.getHandlewindowguid(), true).getResult()));
            i++;
        }
        list.add(windowMap);
        list.add(qnoMap);
        list.add(waitcountMap);
        return list;
    }
	public void changeDivNew() {
        try {
            List<Map<String, String>> jsonList =returnHtmlJsonNew(getRequestParameter("hallguid"),
                    getRequestParameter("Centerguid"), getRequestParameter("MacAddress"));
            for (int i = 0; i < jsonList.size(); i++) {
                Map<String, String> value = jsonList.get(i);
                Iterator<?> it = value.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<?, ?> entry = (Entry<?, ?>) it.next();
                    addCallbackParam(entry.getKey().toString(), entry.getValue());
                }
            }
            
            addCallbackParam("total",jsonList.get(0).size());
        }
        catch (Exception e) {
            //system.out.println(e.getMessage());
        }
        
    }
    public List<Map<String, String>> returnHtmlJsonNew(String hallguid, String Centerguid,String Macaddress ) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> qnoMap = new HashMap<>(16);
        AuditZnsbEquipment equipmnet=equipmentservice.getDetailbyMacaddress(Macaddress).getResult();
       List<AuditQueue> auditqueuelist =  Windowhandle.getauditqueuenohander(equipmnet.getRowguid());

        int i = 0;
        for (AuditQueue Record : auditqueuelist) {
            qnoMap.put("qno" + i, StringUtil.getNotNullString(Record.getQno()));
            i++;
        }
        list.add(qnoMap);
        return list;
    }
	// 从audit_queue_window表中获取windowguid
	public List<String> findwindowguid(String equmentguid){
		List<String> windowguid=zhenggaiImpl.getwindowbyguid(equmentguid);
	    return windowguid;
	}

}
