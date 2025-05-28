package com.epoint.rentcar.action;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.rentcar.Constant.RentCarConstant;
import com.epoint.rentcar.util.HttpUtil;
import com.epoint.search.inteligentsearch.sdk.util.EpointDateUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@RestController("sfhysearchaction")
@Scope("request")
public class SfhySearchAction extends BaseController {

    private String id = "";
    private String name = "";
    private String phone = "";

    @Override
    public void pageLoad() {

    }

    public void search() {
        if (StringUtil.isBlank(id) || StringUtil.isBlank(name) || StringUtil.isBlank(phone)){
            addCallbackParam("msg","请填写信息！");
        }
        // 构建参数
        HashMap<String, String> map = new HashMap<>();
        map.put("XM",name);
        map.put("ZJHM",id);
        JSONObject param = new JSONObject();
        param.put("operatorId", RentCarConstant.OPERATOR_ID);
        param.put("operatorQueryTime", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        param.put("operatorIp",request.getRemoteAddr());
        param.put("operatorUnitName", RentCarConstant.UNIT_NAME);
        param.put("operatorQueryReason", RentCarConstant.QUERY_REASON);
        param.put("operatorIdCard", RentCarConstant.ID_CARD);
        param.put("operatorPhoneNum", RentCarConstant.PHONENUM);
        param.put("requesterPhoneNum", phone);
        map.put("GASJGX_PARAM",param.toJSONString());
        // 密文
        try {
            String text = HttpUtil.httpPostWithForm(RentCarConstant.BJHY_URL, map);
            log.info("背景核查结果："+text);
            SymmetricCrypto symmetricCrypto = SmUtil.sm4(RentCarConstant.KEY.getBytes());
            // 解密
            String returnJson = symmetricCrypto.decryptStr(text);
            if (StringUtil.isBlank(returnJson)) {
                addCallbackParam("msg", "核验失败，请稍后重试！");
            }
            JSONObject returnJsonObject = JSONObject.parseObject(returnJson);
            if(!"200".equals(returnJsonObject.getString("code"))){
            	if ("500".equals(returnJsonObject.getString("code"))) {
            		addCallbackParam("msg", "当前申请号码已申请过！");
            	}else {
            		addCallbackParam("msg", "查询失败，请稍后重试！");
            	}
               
                log.info("接口调用失败！错误信息：" + returnJsonObject.getString("msg"));
                return;
            }
            JSONArray data = returnJsonObject.getJSONArray("data");
            StringBuilder sb = new StringBuilder();
        	 for (int i = 0; i < data.size(); i++) {
                 JSONObject jsonObject = data.getJSONObject(i);
                 Date wfrq = jsonObject.getDate("违法日期");
                 if (StringUtil.isNotBlank(wfrq)) {
                	  sb.append(EpointDateUtil.convertDate2String(jsonObject.getDate("违法日期"), EpointDateUtil.DATE_FORMAT));
                      sb.append(";");
                 }
             }
        	 if (sb.toString().length()>1) {
        		 addCallbackParam("msg", "该用户存在不良记录，时间为："+sb.toString());
        	 }else {
        		 addCallbackParam("msg", "未查到不良记录！");
        	 }
           
        }catch (Exception e){
            log.info("接口调用失败！错误信息：" + e.getMessage());
            e.printStackTrace();
            addCallbackParam("msg", "查询失败，请稍后重试！");
        }
    }

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
