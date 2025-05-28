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
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController("nooulistinnerqueueaction")
@Scope("request")
public class NoOulistInnerQueueAction extends BaseController {
	

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
	private final int size = 16; // 每页数据量
	private List<Record> rtnValue = new ArrayList<>();

	String Centerguid;
	String hallguid;
	
	@Override
	public void pageLoad() {
		Centerguid = getRequestParameter("Centerguid");
		hallguid = getRequestParameter("hallguid");

		// 获取数据的分页数
		addCallbackParam("pages", pages(Centerguid, hallguid));
	}

	public int pages(String Centerguid, String hallguid) {
	    String condition ="";
        condition += " and Centerguid='" + Centerguid +"'";
        if (!"all".equals(hallguid)) {
            condition += " and LOBBYTYPE='" + hallguid +"'";
           
        }
        condition += " and IS_USEQUEUE='" + QueueConstant.Common_yes_String +"'";
    
        int num = oulistinner.getOrderdLinkedOucount(condition);
		return num = num % size == 0 ? num / size : num / size + 1;
	}

	public void getDataJson() {
	    String condition ="";
        condition += " and Centerguid='" + Centerguid +"'";
        if (!"all".equals(hallguid)) {
            condition += " and LOBBYTYPE='" + hallguid +"'";
           
        }
        condition += " and IS_USEQUEUE='" + QueueConstant.Common_yes_String +"'";
    List<FrameOu> oulist = oulistinner.getOrderdLinkedOulist(Integer.valueOf(getRequestParameter("cur")) * size, size, condition);
    for(FrameOu ou :oulist){
        Record record = new Record();
        record.put("ouname",
                StringUtil.isNotBlank(ou.getOushortName())
                        ?ou.getOushortName()
                        : ou.getOuname());
        record.put("ouguid", ou.getOuguid());
        rtnValue.add(record);
    }
    addCallbackParam("html", JsonUtil.listToJson(rtnValue));
	}
}
