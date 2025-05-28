package com.epoint.auditsppolicybasicinfo.action;

import com.epoint.auditsppolicybasicinfo.api.IAuditSpPolicyBasicinfoService;
import com.epoint.auditsppolicybasicinfo.api.entity.AuditSpPolicyBasicinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 政策信息表修改页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:26:17]
 */
@RightRelation(AuditSpPolicyBasicinfoListAction.class)
@RestController("auditsppolicybasicinfoeditaction")
@Scope("request")
public class AuditSpPolicyBasicinfoEditAction extends BaseController
{

    @Autowired
    private IAuditSpPolicyBasicinfoService service;

    private List<SelectItem> typeModel = null;

    private FileUploadModel9 fileUploadModel;

    /**
     * 政策信息表实体对象
     */
    private AuditSpPolicyBasicinfo dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpPolicyBasicinfo();
        }
        if (StringUtil.isBlank(dataBean.getClientguid())) {
            dataBean.setClientguid(UUID.randomUUID().toString());
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

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getClientguid(), null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }

        return fileUploadModel;
    }

    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目类别", null, false));
        }
        return this.typeModel;
    }

    public AuditSpPolicyBasicinfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpPolicyBasicinfo dataBean) {
        this.dataBean = dataBean;
    }

}
