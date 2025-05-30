package com.epoint.common.zwdt.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.zwdt.login.dhsignutil.SignatureUtil;
import com.epoint.core.utils.httpclient.HttpUtil;

@RestController
@RequestMapping("/testdhrest")
public class testdhrest {
	String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALmyz6dFGC+rTlKihktP55k1QFBjtJlKue3xz0XL7zaGGja0Ph7XLRpU9D6fX0kwdgU3b7YteeJt6qK34hp8JnX7uEjw5+8Ycebp9gvMyF1/415Kpl2X7eZRRY0Izs8hk8WLkigagyymecmS30/P/ivYBbhSHh5KNgApmmdhBhbDAgMBAAECgYBE9LFHW9DvYp1i8hcMQE+wBKYtM9kMU0NXqENfl6rshD+XsLagnSaScJmB54xqkSRCK6O/8oMgIdDPO5kqXI+q/SRxxIcq/i4v0awOETJH2nMalzOi6pkciQXmLV21BaooFDsV+iIAyGjUFTNFWkcausTYyLLlDDxpOvER9VtEgQJBAPRGdZQuRAdEpKpxutDmSBVFw3hnwaXUourjFaG1eZk6PSlunHJ2t2k3Dnt4t1DipD7RcHoyA6wTmVfB+aLER58CQQDCnJaB/wRiNK97paT/6z7a7oJO0BdV8tBA02k69fa9Ch0vx+CZJ0XVphIg6sbOwW5VCwHaJ+C6ZSnQY/2Utq5dAkAJtpuKTuSpnVMm6+rPz7hiECobqZSLsY7g5DLCAWNacnRoJ1vVOV9FscjDpCM19i0tyko8saxvtT/Nu4rfrAplAkB10IxBOJxzw4d06GvCTq0npvqWMd3et3ehtxreiecMvGG8+lka/q6FT9truBYRNwLPUzGU201uKi4tLUtny0w1AkEAqfxfhLx/DRXo2oekPQZW894TK9/Y2HGsOKlAi1pFpAWG5xlkkxXeuj+vF1/lLuSJCjB1KQgFcjv+zmxQ5bBcAw== ";
	/**
     *  获取咨询和投诉数量
     *     获取咨询数量、投诉数量、等待答复数量、已答复数量
     *  @param params 接口的入参
     *  @param request HTTP请求
	 * @return 
     *  @return    
	 * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "/ticketValidate", method = RequestMethod.POST)
	public String ticketValidate(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String appId ="jisjningzwfww";
		JSONObject rtn = DhUser_HttpUtil.getTicketvalidate(appId, privateKey, "0c3556219269669794e6ddd3978ee3f4", "0");
		return rtn.toJSONString();
	}
    
    
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	public String getUserInfo(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String appId ="jisjningzwfww";
		JSONObject rtn = DhUser_HttpUtil.getUserInfo(appId, privateKey, "0",  "dc29cb9a84aa4f2a9953158227db375120d4cb64685747448830da581bcfe5d1");
		return rtn.toJSONString();
	}
    
    @RequestMapping(value = "/getCoruserInfo", method = RequestMethod.POST)
	public String getCoruserInfo(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String appId ="jisjningzwfww";
		JSONObject rtn = DhUser_HttpUtil.getCoruserInfo(appId, privateKey, "0",  "e8662ca71d0c4e27ac59db736bf0636642b7c29296e94ecf8ea746c866f48a20");
		return rtn.toJSONString();
	}
    
    @RequestMapping(value = "/getCorInfo", method = RequestMethod.POST)
	public String getCorInfo(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String appId ="jisjningzwfww";
		JSONArray company = new JSONArray();
		JSONObject rtn = DhUser_HttpUtil.getCorInfo(appId, privateKey, "0",  "e8662ca71d0c4e27ac59db736bf0636642b7c29296e94ecf8ea746c866f48a20","0");
		if(rtn!=null) {
			Integer count = rtn.getInteger("count");
			if(count > 0) {
				String companystr = rtn.getString("list");
				company = JSONArray.parseArray(companystr);
			}
		}
		return company.toJSONString();
	}
    
    @RequestMapping(value = "/generateticket", method = RequestMethod.POST)
	public String generateticket(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String appId ="jisjningzwfww";
		JSONObject rtn = DhUser_HttpUtil.generateticket(appId, privateKey, "0", 
				"ATZyMnczdzEFN3NFf0wCMwE1djJ0NwxOBjQGOgFHdz90RAVBdjs=","jisgxqzwfww","kiIIkIlga6rbcI7V");
		return rtn.toJSONString();
	}

}
