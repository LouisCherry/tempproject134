package com.epoint.zwzt.xxfb.xxfbinfocolumn.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zwzt.xxfb.constant.XxfbConstant;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.IXxfbInfoColumnService;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity.XxfbInfoColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信息栏目表详情页面对应的后台
 *
 * @author D0Be
 * @version [版本号, 2022-04-25 13:57:31]
 */
@RightRelation(XxfbInfoColumnListAction.class)
@RestController("xxfbinfocolumndetailaction")
@Scope("request")
public class XxfbInfoColumnDetailAction extends BaseController
{
    /**
     * 信息栏目表service
     */
    @Autowired
    private IXxfbInfoColumnService service;

    /**
     * 信息栏目表实体对象
     */
    private XxfbInfoColumn dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean != null) {
            if (StringUtil.isNotBlank(dataBean.getParent_column_rowguid())) {
                XxfbInfoColumn parent = service.find(dataBean.getParent_column_rowguid());
                if (parent != null && !parent.isEmpty()) {
                    dataBean.set("parent_column_name", parent.getInfo_column_name());
                }
            }
        }
        else {
            dataBean = new XxfbInfoColumn();
        }
        boolean hasType = "1".equals(XxfbConstant.ZWZT_XXFB_INFO_COLUMN_TYPE_IS);
        addCallbackParam("hasType", hasType);
    }

    public XxfbInfoColumn getDataBean() {
        return dataBean;
    }
}
