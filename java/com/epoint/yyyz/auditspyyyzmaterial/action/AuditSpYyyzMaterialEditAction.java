package com.epoint.yyyz.auditspyyyzmaterial.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditsp.auditspsharematerial.domain.AuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerial.inter.IAuditSpShareMaterial;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.yyyz.auditspyyyzmaterial.api.IAuditSpYyyzMaterialService;
import com.epoint.yyyz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;

/**
 * 主题共享材料表修改页面对应的后台
 * 
 * @author lyunang
 * @version [版本号, 2020-06-19 09:34:42]
 */
@RestController("auditspyyyzmaterialeditaction")
@Scope("request")
public class AuditSpYyyzMaterialEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 7180928150903839037L;



    /**
     * 主题共享材料表实体对象
     */
    private AuditSpYyyzMaterial dataBean = null;

    /**
     * 是否必须单选按钮组model
     */
    private List<SelectItem> necessityModel = null;

    /**
     * 是否容缺单选按钮组model
     */
    private List<SelectItem> rongqueModel = null;

    /**
     * 是否启用单选按钮组model
     */
    private List<SelectItem> statusModel = null;

    /**
     * 提交方式下拉列表model
     */
    private List<SelectItem> submittypeModel = null;

    /**
     * 作用范围下拉列表model
     */
    private List<SelectItem> effictiverangeModel = null;
    
    /**
     * 来源渠道下拉列表model
     */
    private List<SelectItem> filesourceModel = null;
    
    /**
     * 主题guid
     */
    private String businessGuid;

    /**
     * 主题共享材料接口的实现类
     */
    @Autowired
    private IAuditSpYyyzMaterialService spYyyzMaterialService;
    
    @Autowired
    private IAuditSpTask auditSpTaskService;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("rowguid");
        businessGuid = getRequestParameter("businedssguid");
        dataBean = spYyyzMaterialService.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpYyyzMaterial();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        String msg = "修改成功！";
        spYyyzMaterialService.update(dataBean);
        addCallbackParam("msg", msg);
    }

    /**
     * 
     *  [当前主题下配置的事项] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpTask> getTaskTypeList() {
        List<AuditSpTask> result = auditSpTaskService.getAllAuditSpTaskByBusinessGuid(businessGuid).getResult();
        return result;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getNecessityModel() {
        if (necessityModel == null) {
            necessityModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否必需", null, false));
        }
        return necessityModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getRongqueModel() {
        if (rongqueModel == null) {
            rongqueModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return rongqueModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return statusModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSubmittypeModel() {
        if (submittypeModel == null) {
            submittypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "材料提交方式", null, false));
        }
        return submittypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getEffictiverangeModel() {
        if (effictiverangeModel == null) {
            effictiverangeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "主题共享材料作用范围", null, false));
        }
        return effictiverangeModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getFilesourceModel() {
        if (filesourceModel == null) {
            filesourceModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "来源渠道", null, false));
        }
        return this.filesourceModel;
    }

    public AuditSpYyyzMaterial getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpYyyzMaterial dataBean) {
        this.dataBean = dataBean;
    }

}
