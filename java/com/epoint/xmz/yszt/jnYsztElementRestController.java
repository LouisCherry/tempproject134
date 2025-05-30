package com.epoint.xmz.yszt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

@RestController
@RequestMapping("/jnYsztElement")
public class jnYsztElementRestController
{
    
    @Autowired
   	private ICodeItemsService codeitemService;
    
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
    @RequestMapping(value = "/getelementInfo", method = RequestMethod.POST)
    public String getelementInfo(@RequestBody String params, HttpServletRequest request) {
        try {
        	JSONObject jsonObject = JSONObject.parseObject(params);
        	String token = jsonObject.getString("token");
        	if (ZwdtConstant.SysValidateData.equals(token)) {
                 JSONObject jsonData = JSONObject.parseObject(codeitemService.getItemValueByCodeID("1016179", "基层服务指标"));
                 return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", jsonData.toString());
        	}else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    

}
