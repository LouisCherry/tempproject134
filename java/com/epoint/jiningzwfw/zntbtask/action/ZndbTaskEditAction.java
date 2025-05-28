package com.epoint.jiningzwfw.zntbtask.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jiningzwfw.zntbtask.api.IZndbTaskService;
import com.epoint.jiningzwfw.zntbtask.api.entity.ZndbTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 智能导办办理事项表修改页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2023-09-18 14:43:48]
 */
@RightRelation(ZndbTaskListAction.class)
@RestController("zndbtaskeditaction")
@Scope("request")
public class ZndbTaskEditAction extends BaseController {

    @Autowired
    private IZndbTaskService service;

    /**
     * 智能导办办理事项表实体对象
     */
    private ZndbTask dataBean = null;

    /**
     * 事项类别下拉列表model
     */
    private List<SelectItem> tasktypeModel = null;
    /**
     * 工程阶段下拉列表model
     */
    private List<SelectItem> phaseModel = null;
    /**
     * 办件类型下拉列表model
     */
    private List<SelectItem> typeModel = null;

    private String priority1;
    private String priority2;
    private String priority3;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ZndbTask();
        }

        if ("1".equals(dataBean.getTasktype())) {
            addCallbackParam("showphase", true);
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

    public ZndbTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(ZndbTask dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getTasktypeModel() {
        if (tasktypeModel == null) {
            tasktypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.tasktypeModel;
    }

    public List<SelectItem> getPhaseModel() {
        if (phaseModel == null) {
            phaseModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "审批阶段", null, false));
        }
        return this.phaseModel;
    }

    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "智能导办办件类型", null, false));
        }
        return this.typeModel;
    }

    public String getPriority1() {
        return priority1;
    }

    public void setPriority1(String priority1) {
        this.priority1 = priority1;
    }

    public String getPriority2() {
        return priority2;
    }

    public void setPriority2(String priority2) {
        this.priority2 = priority2;
    }

    public String getPriority3() {
        return priority3;
    }

    public void setPriority3(String priority3) {
        this.priority3 = priority3;
    }
}
