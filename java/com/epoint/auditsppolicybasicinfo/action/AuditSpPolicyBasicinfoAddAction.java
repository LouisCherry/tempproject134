package com.epoint.auditsppolicybasicinfo.action;

import com.epoint.auditsppolicybasicinfo.api.IAuditSpPolicyBasicinfoService;
import com.epoint.auditsppolicybasicinfo.api.entity.AuditSpPolicyBasicinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 政策信息表新增页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:26:17]
 */
@RightRelation(AuditSpPolicyBasicinfoListAction.class)
@RestController("auditsppolicybasicinfoaddaction")
@Scope("request")
public class AuditSpPolicyBasicinfoAddAction extends BaseController
{
    @Autowired
    private IAuditSpPolicyBasicinfoService service;
    /**
     * 政策信息表实体对象
     */
    private AuditSpPolicyBasicinfo dataBean = null;

    private List<SelectItem> typeModel = null;

    private FileUploadModel9 fileUploadModel;

    public void pageLoad() {
        dataBean = new AuditSpPolicyBasicinfo();
        if (StringUtil.isBlank(getViewData("clientguid"))) {
            dataBean.setClientguid(UUID.randomUUID().toString());
            addViewData("clientguid", dataBean.getClientguid());
        }

    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setClientguid(getViewData("clientguid"));
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
        dataBean = new AuditSpPolicyBasicinfo();
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
            // clientGuid一般是地址中获取到的，此处只做参考使用
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("clientguid"), null,
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
        if (dataBean == null) {
            dataBean = new AuditSpPolicyBasicinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPolicyBasicinfo dataBean) {
        this.dataBean = dataBean;
    }

}
