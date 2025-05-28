package com.epoint.auditsp.basedata.participantsinfo.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 阶段选择页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@RestController("participantsinfochooseaction")
@Scope("request")
public class ParticipantsInfoChooseAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 7487597167764665058L;

    @Autowired
    private ICodeItemsService iCodeItemsService;


    @Override
    public void pageLoad() {
        String subappguid = getRequestParameter("subappguid");
        JSONArray jsonArray = new JSONArray();
        List<CodeItems> corpTypeList = iCodeItemsService.listCodeItemsByCodeName("关联单位类型");
        if (EpointCollectionUtils.isNotEmpty(corpTypeList)) {
            // 按ordernum从大到小排序
            for (CodeItems corpType : corpTypeList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("phaseGuid", corpType.getItemValue());
                jsonObject.put("phaseName", corpType.getItemText());
                jsonObject.put("statuscls", "wsb");
                jsonObject.put("statustext", "待选择");
                jsonObject.put("btncls", "nocf");
                jsonArray.add(jsonObject);
            }
        }
        addCallbackParam("phase", jsonArray);
    }

}
