package com.epoint.basic.ui.main.classic;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;

@RestController("turnledaction")
@Scope("request")
public class TurnLedAction extends BaseController {
	private static final long serialVersionUID = 589014641966489698L;

	public void turnout() throws Exception {
		//system.out.println("LED触发注销");
        String content="LED屏注销";
        String WindowNo=ZwfwUserSession.getInstance().getWindowNo();
        String CenterGuid=ZwfwUserSession.getInstance().getCenterGuid();
	    JSONObject dataJson = new JSONObject();
	    dataJson.put("regionName", WindowNo);
	    dataJson.put("content", content+",centerGuid:"+CenterGuid);
	    //system.out.println("LED触发注销发送消息");
		ProducerMQ.send("QueueLed_" + CenterGuid, "[" + dataJson.toString() + "]");
	}

	@Override
	public void pageLoad() {

	}

}
