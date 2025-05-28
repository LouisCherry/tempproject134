package com.epoint.znsb.auditznsbpayment.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.znsb.auditznsbpayment.api.IAuditZnsbPaymentService;
import com.epoint.znsb.auditznsbpayment.api.entity.AuditZnsbPayment;
import com.epoint.znsb.auditznsbytjextend.api.IAuditZnsbYtjextendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;

@RestController
@RequestMapping(value = "/payment")
public class PayMentController {

    @Autowired
    private IAuditZnsbPaymentService paymentService;

    @Autowired
    private IAttachService attachService;

    /**
     * 水电气
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getlist", method = RequestMethod.POST)
    public String getlist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String type = obj.getString("type");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            //获取水电气机构数据
            List<AuditZnsbPayment> paylist = paymentService.findList("getListByType",centerguid,type);
            String readattachString = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/rest/auditattach/readAttach?attachguid=";
            for (int i = 0; i < paylist.size(); i++) {
                if (StringUtil.isNotBlank(paylist.get(i).getPngattachguid())) {
                    List<FrameAttachInfo> modulepngattachlist = attachService.getAttachInfoListByGuid(paylist.get(i).getPngattachguid());
                    if (modulepngattachlist != null && !modulepngattachlist.isEmpty()) {
                        String moduleattachguid = modulepngattachlist.get(0).getAttachGuid();
                        paylist.get(i).put("moduleimg", readattachString + moduleattachguid);
                    }
                }
            }
            dataJson.put("paylist", paylist);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }
}
