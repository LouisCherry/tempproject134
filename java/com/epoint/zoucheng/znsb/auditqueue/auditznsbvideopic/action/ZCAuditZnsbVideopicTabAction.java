package com.epoint.zoucheng.znsb.auditqueue.auditznsbvideopic.action;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;

/**
 * 设备列表tab的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-09-19 16:14:42]
 */
@RestController("zcauditznsbvideopictabaction")
@Scope("request")
public class ZCAuditZnsbVideopicTabAction extends BaseController {
    private static final long serialVersionUID = 2089869847805045223L;

    public void pageLoad() {
    }

    public void initTabData() {
        List<Record> recordList = new ArrayList<Record>();
        Record video = new Record();
        video.put("name", "视频列表");
        video.put("code", "splb");
        video.put("url", "znsb/auditznsbvideopic/auditznsbvideolist?videopictype=1");
        recordList.add(video);
        // 待上报
        Record pic = new Record();
        pic.put("name", "图片列表");
        pic.put("code", "tplb");
        pic.put("url", "znsb/auditznsbvideopic/auditznsbpiclist?videopictype=2");
        recordList.add(pic);
        // 音频
        Record audio = new Record();
        audio.put("name", "音频列表");
        audio.put("code", "yplb");
        audio.put("url", "znsb/auditznsbvideopic/auditznsbaudiolist?videopictype=3");
        recordList.add(audio);
        sendRespose(JsonUtil.listToJson(recordList));
    }
}
