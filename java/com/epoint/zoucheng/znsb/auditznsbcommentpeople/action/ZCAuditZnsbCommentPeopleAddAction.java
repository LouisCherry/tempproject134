package com.epoint.zoucheng.znsb.auditznsbcommentpeople.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.zoucheng.znsb.auditznsbcommentpeople.domain.ZCAuditZnsbCommentPeople;
import com.epoint.zoucheng.znsb.auditznsbcommentpeople.inter.IZCAuditZnsbCommentPeopleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 工作台评价窗口人员记录表新增页面对应的后台
 * 
 * @author chencong
 * @version [版本号, 2020-04-01 16:23:16]
 */
@RightRelation(ZCAuditZnsbCommentPeopleListAction.class)
@RestController("zcauditznsbcommentpeopleaddaction")
@Scope("request")
public class ZCAuditZnsbCommentPeopleAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -1251506541509308490L;
    @Autowired
    private IZCAuditZnsbCommentPeopleService service;
    /**
     * 工作台评价窗口人员记录表实体对象
     */
    private ZCAuditZnsbCommentPeople dataBean = null;

    /**
    * 满意度评价选项下拉列表model
    */
    private List<SelectItem> satisfiedtextModel = null;

    public void pageLoad() {
        dataBean = new ZCAuditZnsbCommentPeople();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new ZCAuditZnsbCommentPeople();
    }

    public ZCAuditZnsbCommentPeople getDataBean() {
        if (dataBean == null) {
            dataBean = new ZCAuditZnsbCommentPeople();
        }
        return dataBean;
    }

    public void setDataBean(ZCAuditZnsbCommentPeople dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSatisfiedtextModel() {
        if (satisfiedtextModel == null) {
            satisfiedtextModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "工作台非常满意", null, false));
        }
        return this.satisfiedtextModel;
    }

}
