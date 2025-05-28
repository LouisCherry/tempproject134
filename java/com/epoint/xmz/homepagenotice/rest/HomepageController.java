package com.epoint.xmz.homepagenotice.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.xmz.homepagenotice.api.IHomepageNoticeService;
import com.epoint.xmz.homepagenotice.api.entity.HomepageNotice;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("jnhomepagerest")
public class HomepageController {

    @Autowired
    private IHomepageNoticeService noticeService;

    transient Logger log = LogUtil.getLog(HomepageController.class);

    @RequestMapping(value = "getInfo", method = RequestMethod.POST)
    public String getInfo() throws Exception {
        JSONObject dataJson = new JSONObject();
        try {
            HomepageNotice homepageNotice = noticeService.getHomepage();
            if (homepageNotice != null) {
                dataJson.put("title", homepageNotice.getTitle());
                dataJson.put("content", homepageNotice.getContent());
                return JsonUtils.zwdtRestReturn("1", "获取成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取配置失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取配置失败！", "");
        }
    }

}
