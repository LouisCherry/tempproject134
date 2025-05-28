package com.epoint.xmz.auditfszlperson.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 反射工作人员表修改页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:38]
 */
@RightRelation(AuditFszlPersonListAction.class)
@RestController("auditfszlpersoneditaction")
@Scope("request")
public class AuditFszlPersonEditAction extends BaseController {

    @Autowired
    private IAuditFszlPersonService service;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 反射工作人员表实体对象
     */
    private AuditFszlPerson dataBean = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditFszlPerson();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    /**
     * 执业类别下拉
     * @return
     */
    public List<SelectItem> getZylbModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("竣工许可执业类别");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 执业范围
     * @return
     */
    public List<SelectItem> getZyfwModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("竣工许可执业范围");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 执业分类
     * @return
     */
    public List<SelectItem> getZyflModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("竣工许可职业分类");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public AuditFszlPerson getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditFszlPerson dataBean) {
        this.dataBean = dataBean;
    }

}
