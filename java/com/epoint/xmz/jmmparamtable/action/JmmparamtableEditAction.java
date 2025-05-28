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

/**
 * 居民码市县参数配置表修改页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-06-26 14:34:17]
 */
@RightRelation(JmmparamtableListAction.class)
@RestController("jmmparamtableeditaction")
@Scope("request")
public class JmmparamtableEditAction extends BaseController {

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
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new Jmmparamtable();
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

    public Jmmparamtable getDataBean() {
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
