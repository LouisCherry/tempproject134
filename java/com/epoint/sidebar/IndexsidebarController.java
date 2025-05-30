package com.epoint.sidebar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/indexsidebar")
public class IndexsidebarController {

    @Autowired
    private ICodeItemsService iCodeItemsService;

    /***
     *
     *  [根据辖区获取咨询电话]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getPhoneByArea", method = RequestMethod.POST)
    public String getPhoneByArea(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            JSONObject param = result.getJSONObject("params");
            String areacode = param.getString("areacode");
            CodeItems codeItems = iCodeItemsService.getCodeItemByCodeName("区县咨询电话", areacode);
            JSONObject dataJson = new JSONObject();
            if (codeItems != null) {
                dataJson.put("phone", codeItems.getItemText());
            } else {
                dataJson.put("phone", "0537-3512000");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
