package com.epoint.zoucheng.device.infopub.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.JsonUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.IInfopubPublishService;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.entity.InfopubPublish;
import com.epoint.zoucheng.device.infopub.programstatistic.api.IProgramstatisticService;
import com.epoint.zoucheng.device.infopub.programstatistic.api.entity.Programstatistic;

@RestController
@RequestMapping("/publishProgram")
public class PublishProgram
{
    @Autowired
    private IInfopubPublishService publishService;
    @Autowired
    private IProgramstatisticService programstatisticService;

    @RequestMapping(value = "/program", method = RequestMethod.POST)
    public String digitaldoorConfig(@RequestParam("number") int number) {
        Calendar c = Calendar.getInstance();
        List<InfopubPublish> list = publishService.findListByledNumber(number);
        List<JSONObject> timeAndGuidList = new ArrayList<JSONObject>();
        for (InfopubPublish info : list) {
            if ((c.get(Calendar.HOUR_OF_DAY) > info.getStarttimehour()
                    || (c.get(Calendar.HOUR_OF_DAY) == info.getStarttimehour()
                            && c.get(Calendar.MINUTE) > info.getStarttimeminute()))
                    && (c.get(Calendar.HOUR_OF_DAY) < info.getEndtimehour()
                            || (c.get(Calendar.HOUR_OF_DAY) == info.getEndtimehour()
                                    && c.get(Calendar.MINUTE) < info.getEndtimeminute()))) {
                JSONObject timeAndGuidJson = new JSONObject();
                timeAndGuidJson.put("programguid", info.getProgramguid());
                timeAndGuidJson.put("duration", info.getDuration());
                timeAndGuidList.add(timeAndGuidJson);
            }
        }
        return JSON.toJSONString(timeAndGuidList);
    }

    //在删除素材的时候,节目数量减一
    @RequestMapping(value = "/deleteprogram", method = RequestMethod.POST)
    public String getStatus(@RequestBody String params) {
        JSONObject json = JSON.parseObject(params);
        JsonUtils.checkUserAuth(json.getString("token"));
        JSONObject obj = (JSONObject) json.get("params");
        String programtype = obj.getString("programtype");
        String programform = "";
        switch (programtype) {
            case "image":
                programform = "1";
                break;
            case "video":
                programform = "2";
                break;
            case "text":
                programform = "3";
                break;
            default:
                break;
        }
        List<Programstatistic> list = programstatisticService.findListByProgramform(programform);
        if (StringUtil.isNotBlank(list)) {
            programstatisticService.deleteByRowguid(list.get(0).getRowguid());
        }
        return null;
    }
}
