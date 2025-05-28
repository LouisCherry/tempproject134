package com.epoint.xmz.thirdreporteddata.auditggconfig.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.thirdreporteddata.auditggconfig.api.IAuditGgConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工改配置信息表新增页面对应的后台
 *
 * @author shaoyuhui
 * @version [版本号, 2023-11-06 17:08:23]
 */
@RestController("auditggconfigaddaction")
@Scope("request")
public class AuditGgConfigAddAction extends BaseController {
    @Autowired
    private IAuditGgConfigService service;
    /**
     * 工改配置信息表实体对象
     */
    private Record dataBean = null;

    public void pageLoad() {
        dataBean = service.getAllConfig();
    }

    /**
     * 保存并关闭
     */
    public void add(String configName, String configValue) {
        service.saveNameAndValue(configName, configValue);
        addCallbackParam("msg", "保存成功！");
    }

    public Record getDataBean() {
        if (dataBean == null) {
            dataBean = new Record();
        }
        return dataBean;
    }

    public void setDataBean(Record dataBean) {
        this.dataBean = dataBean;
    }

}
