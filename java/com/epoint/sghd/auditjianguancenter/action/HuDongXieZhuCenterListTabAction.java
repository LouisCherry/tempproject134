package com.epoint.sghd.auditjianguancenter.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 中心互动协助下的tab页面后台
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("hudongxiezhucenterlisttabaction")
@Scope("request")
public class HuDongXieZhuCenterListTabAction extends BaseController {

    private static final long serialVersionUID = 2089869847805045223L;

    @Override
    public void pageLoad() {
    }

    public void initTabData() {
        List<Record> recordList = new ArrayList<Record>();
        // 已发起的许可意见变更
        Record yfqbg = new Record();
        yfqbg.put("name", "已发起的许可意见变更");
        yfqbg.put("code", "yfqbg");
        yfqbg.put("url", "jiningzwfw/sghd/projectjianguan/yifaqiopinion");
        recordList.add(yfqbg);

        // 业务沟通交流
        Record shzbj = new Record();
        shzbj.put("name", "业务沟通交流");
        shzbj.put("code", "zbcx");
        shzbj.put("url", "jiningzwfw/sghd/projectjianguan/auditprojectcenterjianguanlist");
        recordList.add(shzbj);

        sendRespose(JsonUtil.listToJson(recordList));
    }
}
