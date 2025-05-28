package com.epoint.xmz.jmmparamtable.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.jmmparamtable.api.IJmmparamtableService;
import com.epoint.xmz.jmmparamtable.api.entity.Jmmparamtable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 居民码市县参数配置表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-06-26 14:34:16]
 */
@RightRelation(JmmparamtableListAction.class)
@RestController("jmmparamtableaddaction")
@Scope("request")
public class JmmparamtableAddAction extends BaseController {
    @Autowired
    private IJmmparamtableService service;
    /**
     * 居民码市县参数配置表实体对象
     */
    private Jmmparamtable dataBean = null;

    /**
     * 市县名称下拉列表model
     */
    private List<SelectItem> areanameModel = null;

    private List<SelectItem> agencyTypeModel = null;
    public void pageLoad() {
        dataBean = new Jmmparamtable();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new Jmmparamtable();
    }

    public Jmmparamtable getDataBean() {
        if (dataBean == null) {
            dataBean = new Jmmparamtable();
        }
        return dataBean;
    }

    public void setDataBean(Jmmparamtable dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getScene() {
        if (agencyTypeModel == null) {
            agencyTypeModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "居民码场景", null, false));
        }
        return agencyTypeModel;
    }
    public List<SelectItem> getAreanameModel() {
        if (areanameModel == null) {
            areanameModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区对应关系", null, false));
        }
        return this.areanameModel;
    }

}
