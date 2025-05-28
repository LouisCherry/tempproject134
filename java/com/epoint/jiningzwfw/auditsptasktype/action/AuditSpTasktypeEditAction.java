package com.epoint.jiningzwfw.auditsptasktype.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jiningzwfw.auditsptasktype.api.IAuditSpTasktypeService;
import com.epoint.jiningzwfw.auditsptasktype.api.entity.AuditSpTasktype;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 事项分类修改页面对应的后台
 *
 * @author qichudong
 * @version [版本号, 2024-09-22 10:56:21]
 */
@RightRelation(AuditSpTasktypeListAction.class)
@RestController("auditsptasktypeeditaction")
@Scope("request")
public class AuditSpTasktypeEditAction extends BaseController {

    @Autowired
    private IAuditSpTasktypeService service;

    /**
     * 事项分类实体对象
     */
    private AuditSpTasktype dataBean = null;

    /**
     * 所属阶段下拉列表model
     */
    private List<SelectItem> phaseidModel = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpTasktype();
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

    public AuditSpTasktype getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpTasktype dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getPhaseidModel() {
        if (phaseidModel == null) {
            phaseidModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "审批阶段", null, false));
        }
        return this.phaseidModel;
    }

}
