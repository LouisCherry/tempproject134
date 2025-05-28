package com.epoint.xmz.jmmparamtable.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.jmmparamtable.api.entity.Jmmparamtable;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.jmmparamtable.api.IJmmparamtableService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 居民码市县参数配置表详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-06-26 14:34:17]
 */
@RightRelation(JmmparamtableListAction.class)
@RestController("jmmparamtabledetailaction")
@Scope("request")
public class JmmparamtableDetailAction extends BaseController {
    @Autowired
    private IJmmparamtableService service;

    /**
     * 居民码市县参数配置表实体对象
     */
    private Jmmparamtable dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new Jmmparamtable();
        }
    }


    public Jmmparamtable getDataBean() {
        return dataBean;
    }
}