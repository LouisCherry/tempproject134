package com.epoint.evainstanceck.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.evainstanceck.api.IEvainstanceCkService;
import com.epoint.evainstanceck.api.entity.EvainstanceCk;

/**
 * 好差评信息表详情页面对应的后台
 * 
 * @author 31220
 * @version [版本号, 2023-11-06 11:18:19]
 */
@RightRelation(EvainstanceCkListActionNew.class)
@RestController("evainstanceckdetailactionnew")
@Scope("request")
public class EvainstanceCkDetailActionNew extends BaseController
{
    @Autowired
    private IEvainstanceCkService service;

    /**
     * 好差评信息表实体对象
     */
    private EvainstanceCk dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new EvainstanceCk();
        }
    }

    public EvainstanceCk getDataBean() {
        return dataBean;
    }
}
