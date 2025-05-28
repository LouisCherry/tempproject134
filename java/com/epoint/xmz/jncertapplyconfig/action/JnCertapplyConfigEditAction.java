package com.epoint.xmz.jncertapplyconfig.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.jncertapplyconfig.api.IJnCertapplyConfigService;
import com.epoint.xmz.jncertapplyconfig.api.entity.JnCertapplyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 证照申办配置表修改页面对应的后台
 *
 * @author future
 * @version [版本号, 2023-03-21 15:06:54]
 */
@RightRelation(JnCertapplyConfigListAction.class)
@RestController("jncertapplyconfigeditaction")
@Scope("request")
public class JnCertapplyConfigEditAction extends BaseController {

    @Autowired
    private IJnCertapplyConfigService service;

    /**
     * 证照申办配置表实体对象
     */
    private JnCertapplyConfig dataBean = null;

    /**
     * 申请人类型单选按钮组model
     */
    private List<SelectItem> applyuser_typeModel = null;
    /**
     * 证照分类下拉列表model
     */
    private List<SelectItem> cert_typeModel = null;
    /**
     * 标签分类复选框组model
     */
    private List<SelectItem> tagtypeModel = null;
    /**
     * 跳转类型单选按钮组model
     */
    private List<SelectItem> target_typeModel = null;

    /**
     * 附件上传
     */
    private FileUploadModel9 fileUploadModel;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new JnCertapplyConfig();
        }

        addCallbackParam("taskname", dataBean.getStr("taskname"));
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            if (StringUtil.isNotBlank(dataBean.getCert_picture())) {
                fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getCert_picture(),
                        null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
            }
        }

        // 该属性设置他为只读，不能被删除
        // fileUploadModel1.setReadOnly("true");
        return fileUploadModel;
    }

    public JnCertapplyConfig getDataBean() {
        return dataBean;
    }

    public void setDataBean(JnCertapplyConfig dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getApplyuser_typeModel() {
        if (applyuser_typeModel == null) {
            applyuser_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请人类型", null, false));
        }
        return this.applyuser_typeModel;
    }

    public List<SelectItem> getCert_typeModel() {
        if (cert_typeModel == null) {
            cert_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照配置分类", null, false));
        }
        return this.cert_typeModel;
    }

    public List<SelectItem> getTagtypeModel() {
        if (tagtypeModel == null) {
            tagtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("复选框组", "证照配置标签维护", null, false));
        }
        return this.tagtypeModel;
    }

    public List<SelectItem> getTarget_typeModel() {
        if (target_typeModel == null) {
            target_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "证照配置跳转类型", null, false));
        }
        return this.target_typeModel;
    }

}
