package com.epoint.cs.auditepidemiclog.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cs.auditepidemiclog.api.IAuditEpidemicLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 访客登记修改页面对应的后台
 * 
 * @author Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
@RightRelation(AuditEpidemicLogListAction.class)
@RestController("auditepidemiclogeditaction")
@Scope("request")
public class AuditEpidemicLogEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 7023661954769683090L;

    @Autowired
    private IAuditEpidemicLogService service;

    /**
     * 访客登记实体对象
     */
    private AuditEpidemicLog dataBean = null;

    /**
    * 登记状态单选按钮组model
    */
    private List<SelectItem> statusModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditEpidemicLog();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public AuditEpidemicLog getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditEpidemicLog dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "登记状态", null, false));
        }
        return this.statusModel;
    }

}
