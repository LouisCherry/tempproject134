package com.epoint.xmz.duoceheyi;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.StringUtil;
import com.epoint.core.utils.httpclient.HttpUtil;

/**
 * 
 * 多测合一对接
 * 
 * @author zhoule
 * @version [版本号, 2023年5月17日]
 */
public class DuoceheyiApiUtil
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 
     * 获取token
     * 
     * @return token
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getToken(String apiUrl) {
        try {
            apiUrl += "authorization/public/getToken?client_id=JNSGGDJ&client_secret=RF7gtKO7";
            String returnmsg = HttpUtil.doGet(apiUrl);
            if (StringUtil.isBlank(returnmsg)) {
                log.info("调用多测合一接口错误！：" + returnmsg);
                return "";
            }
            JSONObject json = JSON.parseObject(returnmsg);
            String code = json.getString("code");
            if (!"0".equals(code)) {
                log.info("账号或密码错误！：" + returnmsg);
                return "";
            }
            JSONObject obj = (JSONObject) json.get("data");
            String token = obj.getString("access_token");
            return token;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 
     * [一句话功能简述]
     * 
     * @param token
     * @param apiUrl
     * @param gcdm
     * @param page
     *            第一页 1
     * @param pageSize
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public String getResultsSharing(String token, String apiUrl, String gcdm, int page, int pageSize) {
        apiUrl += "/external/public/getResultsSharing?token=" + token;
        JSONObject sendJson = new JSONObject();
        sendJson.put("GCBH", gcdm);
        sendJson.put("page", page);
        sendJson.put("pageSize", pageSize);
        System.out.println(sendJson.toJSONString());
        String returnmsg = HttpUtil.doPostJson(apiUrl, sendJson.toJSONString());
        return returnmsg;
    }

}
