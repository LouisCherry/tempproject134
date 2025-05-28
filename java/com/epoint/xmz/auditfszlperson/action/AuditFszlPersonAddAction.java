package com.epoint.xmz.auditfszlperson.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;

/**
 * 反射工作人员表新增页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:38]
 */
@RightRelation(AuditFszlPersonListAction.class)
@RestController("auditfszlpersonaddaction")
@Scope("request")
public class AuditFszlPersonAddAction extends BaseController {
    @Autowired
    private IAuditFszlPersonService service;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 反射工作人员表实体对象
     */
    private AuditFszlPerson dataBean = null;


    public void pageLoad() {
        dataBean = new AuditFszlPerson();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setFszlguid(getRequestParameter("fszlGuid"));
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditFszlPerson();
    }

    public AuditFszlPerson getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditFszlPerson();
        }
        return dataBean;
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

    public void setDataBean(AuditFszlPerson dataBean) {
        this.dataBean = dataBean;
    }


}
