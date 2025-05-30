package com.epoint.xmz.rpaysgzzba.rest;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.xmz.rpadxyqsb.api.IRpaDxyqsbService;
import com.epoint.xmz.rpadxyqsb.api.entity.RpaDxyqsb;
import com.epoint.xmz.rpaysgzzba.api.IRpaYsgzzbaService;
import com.epoint.xmz.rpaysgzzba.api.entity.RpaYsgzzba;

import cn.hutool.core.lang.UUID;

@RestController
@RequestMapping("/jnrparest")
public class jnRpaRestController
{
    
    @Autowired
   	private IRpaYsgzzbaService iRpaYsgzzbaService;
    
    @Autowired
    private IRpaDxyqsbService iRpaDxyqsbService;
    
    /***
     * 
     *  在线办理成熟度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/insertYsgzzba", method = RequestMethod.POST)
    public String insertYsgzzba(@RequestBody String params, HttpServletRequest request) {
        try {
        	JSONObject jsonObject = JSONObject.parseObject(params);
        	String name = jsonObject.getString("name");
        	name = name.replaceAll("", "").replaceAll("\\s*|\r|\n|\t","");
        	String[] names = null;
        	if (name.contains("0") || name.contains("1")
        			|| name.contains("2") || name.contains("3")
        			|| name.contains("4") || name.contains("5")
        			|| name.contains("6") || name.contains("7")
        			|| name.contains("08") || name.contains("9")) {
        		names = name.split("0|1|2|3|4|5|6|7|8|9");
        	}
        	
        	if (names != null) {
        		for (String nam : names) {
        			if ("属地".equals(nam) || "济南".equals(nam) || "烟台".equals(nam)) {
        				continue;
        			}
        			Date noticedate = jsonObject.getDate("noticedate");
                	String sql = "select * from rpa_ysgzzba where companyname = ?";
                	RpaYsgzzba Ysgzzba =iRpaYsgzzbaService.find(sql, nam.trim());
                	if (Ysgzzba == null) {
                		RpaYsgzzba newYsg = new RpaYsgzzba();
                		newYsg.setRowguid(UUID.randomUUID().toString());
                		newYsg.setOperatedate(new Date());
                		newYsg.setCompanyname(nam.trim());
                		newYsg.setNoticedate(noticedate);
                		iRpaYsgzzbaService.insert(newYsg);
                	}
        			
            	}
        	}
        	return JsonUtils.zwdtRestReturn("1", "新增成功", "");
        	 
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    
    
    /***
     * 
     *  在线办理成熟度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/insertDxyqsb", method = RequestMethod.POST)
    public String insertDxyqsb(@RequestBody String params, HttpServletRequest request) {
        try {
        	
        	JSONObject jsonObject = JSONObject.parseObject(params);
        	JSONArray array = jsonObject.getJSONArray("data");
        	for (int i=0;i<array.size();i++) {
        		JSONArray object = array.getJSONArray(i);
        		String content = object.getString(0).toString();
        		if (content.contains("预约了")) {
        			String[] contents = content.split("预约了");
        			String name = contents[0].trim();
        			String good = contents[1].trim();
        			String sql = "select * from rpa_dxyqsb where name = ? and content = ?";
        			RpaDxyqsb dxyqsb =iRpaDxyqsbService.find(sql, name,good);
                	if (dxyqsb == null) {
                		RpaDxyqsb newdxyqsb = new RpaDxyqsb();
                		newdxyqsb.setRowguid(UUID.randomUUID().toString());
                		newdxyqsb.setOperatedate(new Date());
                		newdxyqsb.setName(name);
                		newdxyqsb.setContent(good);
                		iRpaDxyqsbService.insert(newdxyqsb);
                	}
        		}
        		
        	}
        	return JsonUtils.zwdtRestReturn("1", "新增成功", "");
        	 
        }
        catch (Exception e) {
        	e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    

}
