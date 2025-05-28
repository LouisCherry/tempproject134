package com.epoint.jn.auditqueue.auditqueuewindowhandledisplay.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.epoint.zhenggai.api.ZhenggaiService;

@RestController("jnwindowhandledisplayqueueaction")
@Scope("request")
public class JnWindowHandleDisplayQueueAction extends BaseController {

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
        Map<String, String> ouMap = new HashMap<>(16);
        Map<String, String> qnoMap = new HashMap<>(16);
        Map<String, String> waitcountMap = new HashMap<>(16);
        List<String> listwinguid=new ArrayList<String>();
        AuditZnsbEquipment equipmnet=equipmentservice.getDetailbyMacaddress(Macaddress).getResult();
        listwinguid=findwindowguid(equipmnet.getRowguid());
        SqlConditionUtil sql = new SqlConditionUtil();
       switch (hallguid) {
               //四楼
           case "009da827-a73c-42ef-853b-ffde53a1311b" :
               
           case "b3a6f2c3-ceb8-4c1c-a168-66cbc93fed9a" : 
               sql.in("LOBBYTYPE", "\"009da827-a73c-42ef-853b-ffde53a1311b\",\"b3a6f2c3-ceb8-4c1c-a168-66cbc93fed9a\"");
               break;
               //三楼
           case "3f11bd9f-550e-454c-8e02-1316f1f3d23c" :
               
           case "3c56f8cc-9a36-4a49-bccd-716b83dfa5d7" : 
               sql.in("LOBBYTYPE", "\"3f11bd9f-550e-454c-8e02-1316f1f3d23c\",\"3c56f8cc-9a36-4a49-bccd-716b83dfa5d7\"");
               break;
               //二楼
           case "f155b358-22b7-40b0-9c84-d5444448502a" :
               
           case "0e506bc8-341a-4399-8d96-3488de63c175" : 
               sql.in("LOBBYTYPE", "\"f155b358-22b7-40b0-9c84-d5444448502a\",\"0e506bc8-341a-4399-8d96-3488de63c175\"");
               break;
               //一楼
           case "63a5595b-fcd0-4b5b-a060-693c671ddd2c" :
               
           case "10fc66de-bfd4-4d96-828b-cbdbd3af9ade" : 
               sql.in("LOBBYTYPE", "\"63a5595b-fcd0-4b5b-a060-693c671ddd2c\",\"10fc66de-bfd4-4d96-828b-cbdbd3af9ade\"");
               break;
           default: 
               sql.eq("LOBBYTYPE", hallguid);
       }
     
       
        sql.eq("IS_USEQUEUE", QueueConstant.Common_yes_String);
        //TODO
        sql.in("rowguid", "'"+StringUtil.join(listwinguid, "','")+"'");
        List<AuditOrgaWindow> lstRecord = new ArrayList<AuditOrgaWindow>();
        List<AuditOrgaWindow> windowList = windowservice.getAllWindow(sql.getMap()).getResult();
        AuditQueueOrgaWindow queuewindow;
        for (AuditOrgaWindow window : windowList) {
            queuewindow = queuewindowservice.getDetailbyWindowguid(window.getRowguid()).getResult();
            if (StringUtil.isNotBlank(queuewindow)) {
                if (StringUtil.isNotBlank(queuewindow.getCurrenthandleqno())
                        || queueservice.getWindowWaitNum(window.getRowguid(), true).getResult() > 0) {
                    lstRecord.add(window);
                }
            }
        }
        Collections.sort(lstRecord, new Comparator<AuditOrgaWindow>() {
            @Override
            public int compare(AuditOrgaWindow o1, AuditOrgaWindow o2) {
                if (o1.getWindowno() == null){
                    return -1;
                }
                else if (o2.getWindowno() == null){
                    return 1;
                }
                else {
                    return o1.getWindowno().compareTo(o2.getWindowno());
                }
            }
        });

        int i = 0;
        for (AuditOrgaWindow Record : lstRecord) {
            windowMap.put("window" + i, Record.getWindowno());
            queuewindow = queuewindowservice.getDetailbyWindowguid(Record.getRowguid()).getResult();
            ouMap.put("ou" + i,
                    StringUtil.isNotBlank(ouService9.getOuByOuGuid(Record.getOuguid()).getOushortName())
                            ? ouService9.getOuByOuGuid(Record.getOuguid()).getOushortName()
                            : ouService9.getOuByOuGuid(Record.getOuguid()).getOuname());
            qnoMap.put("qno" + i, StringUtil.getNotNullString(queuewindow.getCurrenthandleqno()));
            waitcountMap.put("waitcount" + i,
                    StringUtil.getNotNullString(queueservice.getWindowWaitNum(Record.getRowguid(), true).getResult()));
            i++;
        }
        list.add(windowMap);
        list.add(ouMap);
        list.add(qnoMap);
        list.add(waitcountMap);
        return list;
    }
	// 从audit_queue_window表中获取windowguid
	public List<String> findwindowguid(String equmentguid){
		List<String> windowguid=zhenggaiImpl.getwindowbyguid(equmentguid);
	    return windowguid;
	}

}
