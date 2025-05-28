package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.EpointTreeHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.IAuditYjsCjrService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.CjrApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 残联账号表修改页面对应的后台
 *
 * @author ez
 * @version [版本号, 2021-04-15 16:18:31]
 */
@RightRelation(DisabledAccountListAction.class)
@RestController("disabledaccounteditaction")
@Scope("request")
public class DisabledAccountEditAction extends BaseController {

    @Autowired
    private IAuditYjsCjrService service;

    /**
     * 残联账号表实体对象
     */
    private CjrApiConfig dataBean = null;

    /**
     * 树数据模型
     */
    private LazyTreeModal9 treeModel = null;
    
    /**
     * 账号类别
     */
    private List<SelectItem> typeModel = null;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(CjrApiConfig.class, guid);
        if (dataBean == null) {
            dataBean = new CjrApiConfig();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(new EpointTreeHandler9(ConstValue9.FRAMEOU));

            treeModel.setRootName("所有部门");
            treeModel.setTreeType(ConstValue9.CHECK_MULTI);
        }
        return treeModel;
    }

    public CjrApiConfig getDataBean() {
        return dataBean;
    }

    public void setDataBean(CjrApiConfig dataBean) {
        this.dataBean = dataBean;
    }
    
    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
        	typeModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "账号类别", null, false));
        }
        return typeModel;
    }

}
