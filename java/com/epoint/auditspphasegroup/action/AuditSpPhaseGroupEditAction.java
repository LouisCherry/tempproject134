package com.epoint.auditspphasegroup.action;

import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import com.epoint.auditspphasegroup.api.IAuditSpPhaseGroupService;
import com.epoint.auditspphasegroup.api.entity.AuditSpPhaseGroup;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 前四阶段分组配置表修改页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:01:54]
 */
@RightRelation(AuditSpPhaseGroupListAction.class)
@RestController("auditspphasegroupeditaction")
@Scope("request")
public class AuditSpPhaseGroupEditAction extends BaseController
{

    @Autowired
    private IAuditSpPhaseGroupService service;
    @Autowired
    private IAuditSpPhaseBaseinfoService iAuditSpPhaseBaseinfoService;

    private List<SelectItem> phasenameModel = null;

    private List<SelectItem> bigtypeModel = null;

    private List<SelectItem> issamelevelModel = null;

    private List<SelectItem> toonlyModel = null;

    /**
     * 前四阶段分组配置表实体对象
     */
    private AuditSpPhaseGroup dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpPhaseGroup();
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

    public List<SelectItem> getPhasenameModel() {
        if (phasenameModel == null) {
            phasenameModel = new ArrayList<>();
            SqlConditionUtil sql = new SqlConditionUtil();

            List<AuditSpPhaseBaseinfo> list = iAuditSpPhaseBaseinfoService.getAllPhaseBaseinfo();
            if (!list.isEmpty()) {
                for (AuditSpPhaseBaseinfo auditSpPhaseBaseinfo : list) {
                    phasenameModel.add(
                            new SelectItem(auditSpPhaseBaseinfo.getRowguid(), auditSpPhaseBaseinfo.getPhasename()));
                }
            }
        }
        return this.phasenameModel;
    }

    public List<SelectItem> getBigtypeModel() {
        if (bigtypeModel == null) {
            bigtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "展示类型", null, false));
        }
        return this.bigtypeModel;
    }

    public List<SelectItem> getIssamelevelModel() {
        if (issamelevelModel == null) {
            issamelevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.issamelevelModel;
    }

    public List<SelectItem> getToonlyModel() {
        if (toonlyModel == null) {
            toonlyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.toonlyModel;
    }

    public AuditSpPhaseGroup getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpPhaseGroup dataBean) {
        this.dataBean = dataBean;
    }

}
